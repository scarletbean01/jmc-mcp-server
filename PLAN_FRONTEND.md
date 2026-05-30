# Frontend Implementation Plan — JMC MCP Dashboard

> **Status:** Phase 0 & Phase 1 scaffolding in progress.
> **Stack:** ClojureScript, re-frame, reagent, re-com, shadow-cljs, reitit.
> **Location:** `frontend/` subdirectory in this monorepo.

---

## Architecture at a Glance

| Decision | Choice |
|---|---|
| **Scope** | Dashboard SPA — Recording Library → Analysis Hub → Job Monitor |
| **Location** | `frontend/` subdirectory in this monorepo |
| **Build Tool** | shadow-cljs + deps.edn |
| **Routing** | `reitit` frontend router with deep-linking |
| **State Management** | re-frame + `http-fx` + `async-flow-fx` |
| **UI Components** | re-com (tabs, panels, inputs, tables, modals) |
| **HTTP Client** | `cljs-ajax` via `http-fx` effect handlers |
| **Async Jobs** | SSE primary (`EventSource`), polling fallback |
| **Analysis Rendering** | 5 shape-based generic renderers |
| **Sidebar** | Collapsible categories + search/filter |
| **Serving** | Bundled into Quarkus JAR via `META-INF/resources` |
| **Errors** | Global toast notification stack + inline form validation |
| **Comparison** | Rich interactive UI driven by `/api/v1/compare/structured` |

---

## Backend Contract (REST API)

The frontend consumes the existing Quarkus REST API. All responses are wrapped in `ApiResponse<T>`:

```json
{
  "success": true,
  "data": { ... },
  "error": null,
  "timestamp": "2026-05-30T23:39:16Z"
}
```

### Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `POST /api/v1/recordings/upload` | Upload | Multipart file upload. Returns `UploadResponse` |
| `GET /api/v1/recordings/{id}` | Status | Returns `RecordingInfo` |
| `DELETE /api/v1/recordings/{id}` | Cleanup | No content on success |
| `POST /api/v1/recordings/{id}/analyze/{type}` | Sync Analysis | Body: `AnalysisRequest`. Returns analysis result |
| `POST /api/v1/recordings/{id}/analyze/{type}/async` | Async Analysis | Returns `202` with `{jobId, status: "PENDING"}` |
| `GET /api/v1/recordings/{id}/analyze/jobs/{jobId}` | Poll | Returns `JobStatusResponse` |
| `GET /api/v1/recordings/{id}/analyze/jobs/{jobId}/stream` | SSE | `job-update` events as JSON |
| `POST /api/v1/compare/structured` | Comparison | Body: `CompareRequest`. Returns `RecordingComparisonResult` |
| `GET /api/v1/health` | Health | JVM metrics |

### Key DTO Shapes

**AnalysisRequest**
```json
{
  "startTime": "2024-01-01T00:00:00Z",
  "endTime": "2024-01-01T01:00:00Z",
  "params": {
    "topN": 20,
    "packagePrefix": "com.example",
    "threadName": "main"
  }
}
```

**JobStatusResponse**
```json
{
  "jobId": "uuid",
  "status": "PENDING | RUNNING | COMPLETED | FAILED",
  "result": { ... },
  "error": null,
  "createdAt": "...",
  "completedAt": null,
  "progressPercent": 10
}
```

**RecordingComparisonResult** (new structured endpoint)
```json
{
  "baseline": {"path": "...", "durationSeconds": 120.5},
  "target": {"path": "...", "durationSeconds": 118.2},
  "warnings": ["Recording durations differ significantly..."],
  "summary": ["No major regressions detected."],
  "metrics": {
    "CPU": [
      {"label": "Avg Machine Total", "baselineValue": 45.2, "baselineDisplay": "45.2%",
       "targetValue": 47.1, "targetDisplay": "47.1%", "deltaPercent": 4.2,
       "normalized": false, "indicator": "info"}
    ],
    "GC": [...],
    "Memory": [...],
    "Contention": [...],
    "I/O": [...],
    "Runtime": [...],
    "JVM Internals": [...]
  },
  "ruleChanges": {
    "regressions": [{"rule": "HeapUsage", "baselineSeverity": "OK", "targetSeverity": "WARNING"}],
    "improvements": []
  },
  "cpuDeltas": [{"key": "java.lang.Thread.run", "baselineRate": 1.2, "targetRate": 3.4, "delta": 2.2}],
  "allocationDeltas": [...],
  "contentionDeltas": [...],
  "exceptionDeltas": [...]
}
```

---

## App-DB State Shape

```clojure
{:route {:current :library           ; keyword from reitit
         :params {}}                 ; path params (e.g., {:id "uuid"})

 :recordings {:items []               ; vec of RecordingInfo
              :loading? false
              :error nil}

 :recording-detail {:recording-id nil
                    :info nil         ; RecordingInfo
                    :active-analysis nil
                    :analysis-params {:start-time nil
                                      :end-time nil
                                      :topN 20}
                    :results {}      ; analysis-type -> {:status :loading/:done/:error :data ... :job-id ...}
                    :loading? false}

 :upload {:status :idle              ; :idle :uploading :success :error
          :progress 0
          :error nil}

 :jobs {}                            ; job-id -> JobStatusResponse + :recording-id + :analysis-type

 :comparison {:baseline-id nil
              :target-id nil
              :result nil            ; RecordingComparisonResult
              :loading? false
              :error nil}

 :notifications []                   ; vec of {:id uuid :type :error/:success/:info :message str}

 :ui {:sidebar-collapsed? false}}
```

---

## Analysis Type Configuration

All 45 analysis types map to one of 5 visual shapes via a central config map:

```clojure
(def analysis-config
  {:overview         {:shape :metrics :sections [:jvm :cpu :memory :gc]}
   :hot-methods      {:shape :table :columns [{:key :methodName :label "Method"}
                                               {:key :sampleCount :label "Samples"}]}
   :cpu-flame        {:shape :flame-graph :depth-key :stackDepth :value-key :sampleCount}
   :call-tree        {:shape :tree :children-key :children :label-key :methodName}
   :heap-trends      {:shape :timeline :x-key :timestamp :y-key :heapUsed}
   :time-series      {:shape :timeline :x-key :timestamp :y-key :value}
   ...})
```

### Category Breakdown for Sidebar

```
Overview
├── overview
└── quick-analysis

CPU & Compilation
├── hot-methods, cpu-flame, call-tree, thread-cpu
├── high-cpu-diagnostic, jit-compilation, code-cache

Memory
├── heap-trends, memory-leaks, allocation-flame
├── object-statistics, predictive-leak, native-memory

GC
├── gc-detail, gc-cause, gc-recommendations

Threads & Locks
├── thread-dump, thread-cpu, thread-contention
├── thread-activity, thread-allocation, virtual-threads
├── deadlock-detection, blocking-summary
├── lock-analysis, lock-flame, lock-resolver
├── thread-pool, thread-starvation

IO & Network
├── io-analysis, io-hotspots, network-analysis

Diagnostics & Search
├── exceptions, errors, safepoint-analysis
├── vm-operations, class-loading, system-health
├── container-metrics, jdk-bug-reference

Events & Timeline
├── incident-timeline, time-series, event-stats
├── search-events, stack-trace-search, request-waterfall

Advanced
├── jfr-rules, recording-settings
├── correlate, diff-call-tree, diff-stack-traces
```

---

## Re-frame Event Catalog

### Global
| Event | Payload | Effect |
|---|---|---|
| `:initialize-db` | — | Reset to `default-db`, load recordings |
| `:route/changed` | reitit match | Update `:route` |
| `:notification/add` | `{:type :error/:success/:info :message str}` | Push to stack, auto-dismiss after 5s |
| `:notification/remove` | id | Remove from stack |

### Library
| Event | Payload | Effect |
|---|---|---|
| `:library/load-recordings` | — | `GET /recordings` |
| `:library/recordings-loaded` | ApiResponse | Store in `:recordings :items` |
| `:library/recordings-failed` | error response | Toast + store error |
| `:library/delete-recording` | recording-id | `DELETE /recordings/{id}` |
| `:library/recording-deleted` | recording-id | Remove from list, toast |

### Upload
| Event | Payload | Effect |
|---|---|---|
| `:upload/select-file` | JS File object | Store in `:upload :file` |
| `:upload/submit` | — | `POST /recordings/upload` via FormData |
| `:upload/progress` | percent | Update `:upload :progress` |
| `:upload/success` | ApiResponse | Add to library, toast, reset after 1s |
| `:upload/failure` | error response | Toast + store error |
| `:upload/reset` | — | Reset upload state |

### Analysis
| Event | Payload | Effect |
|---|---|---|
| `:recording/load-info` | recording-id | `GET /recordings/{id}` |
| `:recording/info-loaded` | ApiResponse | Store info |
| `:analysis/select-type` | analysis-type keyword | Update `:active-analysis` |
| `:analysis/update-param` | `[key value]` | Update `:analysis-params` |
| `:analysis/run` | analysis-type | `POST /analyze/{type}` |
| `:analysis/result` | `[type response]` | Store result, clear loading |
| `:analysis/failed` | `[type response]` | Store error, toast |
| `:analysis/run-async` | analysis-type | `POST /analyze/{type}/async` |
| `:analysis/job-created` | `[recording-id type response]` | Store job, open SSE |
| `:job/update` | `[job-id status-data]` | Update job, store result if completed |

### Comparison
| Event | Payload | Effect |
|---|---|---|
| `:comparison/select-baseline` | recording-id | Store baseline |
| `:comparison/select-target` | recording-id | Store target |
| `:comparison/run` | — | `POST /compare/structured` |
| `:comparison/result` | ApiResponse | Store result |
| `:comparison/failed` | error response | Toast + store error |

---

## Custom Effect Handlers

### `:sse/connect`
Opens an `EventSource` to the backend SSE stream. Dispatches re-frame events on messages.

```clojure
{:sse/connect {:recording-id "uuid"
               :job-id "job-uuid"
               :on-message [:job/update "job-uuid"]
               :on-error [:job/sse-failed "job-uuid"]}}
```

The SSE endpoint sends `job-update` events with `ApiResponse<JobStatusResponse>` shape. The effect handler parses JSON and dispatches the inner `:data`.

### `:notify`
Shorthand to dispatch a notification.

```clojure
{:notify {:type :error :message "Upload failed"}}
```

---

## Routing Map

```clojure
["/"
 ["" {:name :library}]
 ["library" {:name :library}]
 ["recordings/:id" {:name :recording-detail}]
 ["compare" {:name :compare}]]
```

- **Dev:** shadow-cljs dev server on `:3000` proxies API calls to Quarkus on `:8080`.
- **Prod:** Quarkus serves compiled frontend from `META-INF/resources` (same origin).

---

## Project File Structure

```
frontend/
├── deps.edn
├── shadow-cljs.edn
├── package.json
├── public/
│   └── index.html
└── src/main/jmc_mcp/
    ├── core.cljs
    ├── config.cljs
    ├── db.cljs
    ├── routes.cljs
    ├── events.cljs
    ├── subs.cljs
    ├── fx.cljs
    ├── api/
    │   └── client.cljs
    └── views/
        ├── layout.cljs
        ├── library.cljs
        ├── analysis_hub.cljs
        ├── comparison.cljs
        ├── job_monitor.cljs
        └── components.cljs
```

---

## Implementation Phases

### Phase 0: Backend Prerequisites ✅
**Goal:** Ensure the backend exposes all data the frontend needs in machine-readable formats.

- [x] Create `RecordingComparisonResult` domain record
- [x] Create `RecordingComparisonMetricRow` domain record
- [x] Create `RecordingComparisonRuleChange` domain record
- [x] Create `RecordingComparisonRules` domain record
- [x] Create `RecordingComparisonRecordingInfo` domain record
- [x] Refactor `CompareRecordingsService` to expose `analyzeStructured()`
- [x] Add `analyzeStructured()` to `CompareRecordingsApplicationService`
- [x] Add `compareRecordingsStructured()` to `AnalysisDispatcher`
- [x] Add `POST /api/v1/compare/structured` endpoint to `ComparisonResource`
- [x] Verify compilation succeeds
- [x] Verify existing `CompareRecordingsToolTest` still passes

**Decision:** Option C — new structured endpoint while keeping the existing markdown endpoint for MCP compatibility.

---

### Phase 1: Project Scaffolding 🔄
**Goal:** Create a working ClojureScript build that compiles and hot-reloads.

- [x] Create `frontend/deps.edn` with re-frame, reagent, re-com, reitit, http-fx
- [x] Create `frontend/shadow-cljs.edn` with browser target and dev/prod API URLs
- [x] Create `frontend/package.json` with React dependencies
- [x] Create `frontend/public/index.html` with app mount point
- [ ] Install npm dependencies (`npm install` in `frontend/`)
- [ ] Verify shadow-cljs compiles: `npx shadow-cljs watch app`
- [ ] Verify hot-reload works in browser at `http://localhost:3000`

**Config Notes:**
- Dev API URL: `http://localhost:8080/api/v1`
- Prod API URL: `/api/v1` (same origin)
- CORS is already enabled on the backend (`quarkus.http.cors.enabled=true`)

---

### Phase 2: Core Infrastructure
**Goal:** Routing, state management, API client, and SSE plumbing.

- [ ] Implement `config.cljs` with `goog-define` API URL
- [ ] Implement `db.cljs` with `default-db`
- [ ] Implement `routes.cljs` with reitit frontend router
- [ ] Implement `fx.cljs` with `:sse/connect` and `:notify` effects
- [ ] Implement `api/client.cljs` with endpoint request builders
- [ ] Implement `events.cljs` with all event handlers
- [ ] Implement `subs.cljs` with all subscriptions
- [ ] Implement `core.cljs` — mount app, initialize routes and DB
- [ ] Wire notification system into layout

**Subscriptions to build:**
- `:route/current` — current route name
- `:route/params` — path params
- `:recordings/items` — list of recordings
- `:recordings/loading?` — loading state
- `:recording-detail/info` — selected recording info
- `:recording-detail/active-analysis` — selected analysis type
- `:recording-detail/result` — analysis result for active type
- `:recording-detail/loading?` — analysis loading state
- `:jobs/all` — all jobs
- `:jobs/running` — pending + running jobs
- `:comparison/result` — comparison result
- `:comparison/loading?` — comparison loading state
- `:notifications` — all notifications

---

### Phase 3: Recording Library & Upload
**Goal:** Users can upload JFR files and browse their recording library.

- [ ] Build `views/layout.cljs` — shell with header, nav sidebar, main area
- [ ] Build `views/library.cljs`:
  - Upload zone (drag-and-drop or file input)
  - Upload progress indicator
  - Recordings table with columns: filename, size, event count, upload time
  - Row actions: View (navigate to analysis hub), Delete
  - Empty state when no recordings
- [ ] Implement file upload via `FormData` + `cljs-ajax`
- [ ] Auto-refresh library after successful upload
- [ ] Navigate to new recording's analysis hub on upload success

**UX Notes:**
- Upload zone should be prominent on the library page
- Show human-readable file sizes (KB, MB)
- Upload progress should be a determinate progress bar
- Deletion requires no confirmation (backend has 24h retention anyway)

---

### Phase 4: Analysis Hub
**Goal:** Users can run any of the 45 analysis types against a selected recording.

- [ ] Build `views/analysis_hub.cljs`:
  - Recording info header (filename, duration, event count)
  - Categorized left sidebar with collapsible groups
  - Search/filter input for analysis types
  - Parameter panel (time range, topN, filters) — shown per analysis type
  - Result panel with shape-based renderer
- [ ] Build `views/components.cljs`:
  - **Metrics renderer** — card grid with key-value pairs
  - **Table renderer** — sortable data table with column config
  - **Tree renderer** — recursive collapsible tree
  - **Flame graph renderer** — placeholder (D3 integration in Phase 7)
  - **Timeline renderer** — placeholder (chart library in Phase 7)
- [ ] Implement sync analysis flow: select type → configure params → run → display
- [ ] Implement async analysis flow: select type → run async → show job progress → display result
- [ ] Cache analysis results in `:recording-detail :results` keyed by `[recording-id analysis-type]`

**UX Notes:**
- Auto-run `overview` analysis when entering a recording detail page
- Show loading spinner in the result panel during analysis
- Async jobs should show a progress bar or indeterminate spinner
- Failed analyses show inline error message + toast

---

### Phase 5: Comparison View
**Goal:** Side-by-side A/B comparison of two recordings with rich interactive visualizations.

- [ ] Build `views/comparison.cljs`:
  - Baseline recording selector (dropdown from library)
  - Target recording selector
  - "Compare" button
  - Category tabs (CPU, GC, Memory, Contention, I/O, Runtime, JVM Internals)
  - Metric table per category with:
    - Baseline value, Target value, Delta %
    - Color coding: green (improvement), red (regression), gray (neutral)
    - Normalized indicator (`*`) tooltip
  - Summary panel at top with warnings and major findings
  - JMC Rules panel: regressions and improvements tables
  - Delta drill-downs:
    - CPU Hotspot Deltas (top 5 regressions)
    - Allocation Deltas (top 5 regressions)
    - Lock Contention Deltas (top 5 regressions)
    - Exception Deltas (top 5 regressions)
- [ ] Implement delta sorting and filtering

**Data Source:** `POST /api/v1/compare/structured`

**UX Notes:**
- Default to first two recordings if library has ≥2 items
- Show "No major regressions" green banner when appropriate
- Duration warning should be a prominent alert when present
- Delta values > +10% are regressions (red), < -10% are improvements (green)

---

### Phase 6: Job Monitor & SSE
**Goal:** Real-time tracking of async analysis jobs.

- [ ] Build `views/job_monitor.cljs`:
  - Collapsible panel or toast area showing active jobs
  - Job list item: analysis type, recording name, progress bar, status
  - Auto-remove completed/failed jobs after 10 seconds (or keep with clear button)
- [ ] Implement SSE connection lifecycle:
  - Open on async job creation
  - Listen for `job-update` events
  - Close when job reaches `COMPLETED` or `FAILED`
  - Reconnect with exponential backoff on error
- [ ] Fallback polling: if SSE fails, poll `GET /jobs/{jobId}` every 2 seconds
- [ ] Dispatch `:job/update` events from SSE messages
- [ ] Update result panel automatically when job completes

**SSE Endpoint:**
```
GET /api/v1/recordings/{recordingId}/analyze/jobs/{jobId}/stream
```

**Event Format:**
```json
event: job-update
data: {"success":true,"data":{"jobId":"...","status":"RUNNING","progressPercent":50,...},"error":null,...}
```

---

### Phase 7: Visualization & Polish
**Goal:** Rich charts, flame graphs, and responsive design.

- [ ] Integrate a charting library (d3 or react-chartjs-2 via npm interop)
- [ ] Implement **flame graph renderer** using d3-flame-graph or custom SVG
- [ ] Implement **timeline/time-series renderer** with line/bar charts
- [ ] Add responsive breakpoints (desktop-first, tablet-friendly)
- [ ] Dark mode toggle (optional)
- [ ] Keyboard shortcuts (e.g., `?` for help, `/` for search)
- [ ] Loading skeletons for better perceived performance
- [ ] Error boundary for renderer crashes

**Charting Candidates:**
- `d3` — maximum control, steep learning curve
- `react-chartjs-2` — easier, good for timelines
- `recharts` (React) — easiest, but heavier bundle

**Decision:** Start with `react-chartjs-2` for timelines, custom SVG for flame graphs.

---

### Phase 8: Build & Deployment
**Goal:** Single deployable artifact with backend + frontend.

- [ ] Add Maven step to build frontend before packaging
- [ ] Copy `frontend/public/` → `target/classes/META-INF/resources/`
- [ ] Configure `shadow-cljs.edn` release build with `:optimization :advanced`
- [ ] Verify production bundle loads at `/` and API calls hit `/api/v1/*`
- [ ] Test upload, analysis, comparison, and SSE in production mode
- [ ] Document build commands in `README.md`

**Build Pipeline:**
```bash
cd frontend && npm install
npx shadow-cljs release app
cp -r frontend/public/* src/main/resources/META-INF/resources/
mvn package
java -jar target/jmc-mcp-*-runner.jar
```

**Alternative:** Use `frontend-maven-plugin` to run npm/shadow-cljs during Maven build.

---

## Open Questions / Future Work

1. **Authentication:** Currently none (open API). If needed later, add JWT or session-based auth.
2. **Recording Persistence:** Backend keeps 24h retention. Frontend could cache metadata in `localStorage`.
3. **Call Tree Expansion:** The backend has `POST /call-tree/{treeId}/expand`. Not included in MVP.
4. **Live Recordings:** `LiveRecordingTool` exists in MCP. REST equivalent not yet implemented.
5. **Export:** Export analysis results as PDF/CSV. Nice-to-have.
6. **WebSocket vs SSE:** SSE is sufficient for one-way server→client push. If bidirectional needed, upgrade to WebSocket.

---

## Appendix: Backend Changes Log

| Date | Change | Files |
|---|---|---|
| 2026-05-31 | Added structured comparison domain records | `RecordingComparisonResult.java`, `RecordingComparisonMetricRow.java`, `RecordingComparisonRuleChange.java`, `RecordingComparisonRules.java`, `RecordingComparisonRecordingInfo.java` |
| 2026-05-31 | Refactored `CompareRecordingsService` with `analyzeStructured()` | `CompareRecordingsService.java` |
| 2026-05-31 | Exposed structured comparison in application layer | `CompareRecordingsApplicationService.java` |
| 2026-05-31 | Added structured comparison to dispatcher | `AnalysisDispatcher.java` |
| 2026-05-31 | Added `POST /api/v1/compare/structured` endpoint | `ComparisonResource.java` |
