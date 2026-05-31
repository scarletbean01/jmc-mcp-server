# Package: frontend

This package contains the ClojureScript Single Page Application (SPA) that acts as the JMC MCP Dashboard, providing a graphical interface to interact with the Java backend.

## Tech Stack & Architecture

- **Language:** ClojureScript
- **Build Tool:** `shadow-cljs` (integrated with Maven via `frontend-maven-plugin`)
- **State Management:** `re-frame` (CQRS-like pattern)
- **View Layer:** `reagent` (React wrapper)
- **UI Components:** `re-com` (with significant custom overrides)
- **Styling:** **Tailwind CSS** (via CDN in `public/index.html`)
- **Routing:** `reitit-frontend`

## Key Directories

- **`src/jmc_mcp/`**: Core application logic.
  - **`views/`**: Reagent UI components organized by feature (Library, Analysis Hub, Comparison).
  - **`api/`**: `cljs-ajax` client configurations for communicating with the Quarkus REST API.
  - **`events.cljs`**: All `re-frame` event handlers (mutations, async effects).
  - **`subs.cljs`**: All `re-frame` subscriptions (queries).
  - **`db.cljs`**: The default schema for the application state (app-db).
  - **`routes.cljs`**: Client-side routing definitions.
- **`resources/public/`**: Static assets, including the root `index.html`.

## Architectural Guidelines

1. **State Management:**
   - **NEVER** mutate state directly in views. All state changes MUST flow through `re-frame` events using `rf/dispatch`.
   - Views should only read state via `rf/subscribe`.
   - Keep `db.cljs` updated when adding new features so the initial state shape is always clear.

2. **Styling & Aesthetics:**
   - The project has moved away from inline styles and standard Bootstrap/re-com defaults.
   - **USE TAILWIND CSS** utility classes for layout, typography, spacing, and colors.
   - Prefer modern, card-based layouts with subtle shadows (`shadow-sm`), rounded corners (`rounded-xl` or `rounded-2xl`), and high-contrast typography (e.g., uppercase small headers in `text-slate-500`).
   - Use `collapsible-card` components for any high-volume data tables or metrics grids.

3. **Data Fetching:**
   - Use `day8.re-frame/http-fx` for HTTP requests inside event handlers.
   - Always handle `:on-failure` to provide user feedback (e.g., via the notification system).

4. **Interactivity:**
   - For deeply nested data (like Differential Call Trees), use recursive Reagent components and track expansion state centrally in `re-frame` rather than relying on local component state (to allow global "Expand All" functionality).

## Development & Building

- During regular Maven builds (`./mvnw compile` or `./mvnw package`), the `frontend-maven-plugin` automatically downloads Node.js, installs dependencies via `npm install`, and compiles the ClojureScript application via `shadow-cljs release`. The output is bundled into the final JAR under `META-INF/resources`.
- To fix compilation errors during UI development, ensure correct Clojure symbol matching and proper closing of Hiccup vectors (`[]`). 
