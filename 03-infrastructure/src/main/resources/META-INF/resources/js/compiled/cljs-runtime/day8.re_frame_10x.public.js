goog.provide('day8.re_frame_10x.public$');
/**
 * Integer that bumps with each public-surface contract revision,
 * including new public event identifiers downstream tools may gate on.
 * Consumers can branch on it via `(capabilities)` or read it directly
 * via `goog.global.day8.re_frame_10x.public.api_version`.
 */
day8.re_frame_10x.public$.api_version = (2);
goog.exportSymbol('day8.re_frame_10x.public$.api_version', day8.re_frame_10x.public$.api_version);
/**
 * True when this namespace is loaded — i.e. when the public surface
 * is available in the runtime. Stable feature-detection hook for
 * downstream tooling. The presence of the var IS the contract;
 * the body is `true` and consumers shouldn't read meaning into the
 * return value beyond 'this fn ran'.
 * 
 * Probed externally as `goog.global.day8.re_frame_10x.public.loaded_QMARK_`
 * so consumers don't have to compile against this namespace.
 */
day8.re_frame_10x.public$.loaded_QMARK_ = (function day8$re_frame_10x$public$loaded_QMARK_(){
return true;
});
goog.exportSymbol('day8.re_frame_10x.public$.loaded_QMARK_', day8.re_frame_10x.public$.loaded_QMARK_);
/**
 * Returns `{:api <int>}` describing the public-surface version the
 * currently-loaded 10x build implements. Bumps with public contract
 * revisions, including new public event identifiers and read API
 * shape or event-identifier semantic changes.
 * Consumers branch on this when they want to support multiple 10x
 * versions side-by-side.
 */
day8.re_frame_10x.public$.version = (function day8$re_frame_10x$public$version(){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"api","api",-899839580),day8.re_frame_10x.public$.api_version], null);
});
goog.exportSymbol('day8.re_frame_10x.public$.version', day8.re_frame_10x.public$.version);
/**
 * Set of feature keywords this build supports. Reserved for
 * future growth — today returns the baseline set. Consumers
 * should treat unknown keywords as 'not supported'.
 * 
 * Read API flags use a `:resource/action` shape — `:epochs/read`,
 * `:epochs/reset-app-db`, `:traces/read`,
 * `:settings/app-db-follows-events`. The
 * `:events/...` family flags the mutation API: `:events/navigate`,
 * `:events/reset`, `:events/replay`, `:events/reset-app-db` mark
 * the corresponding event identifier constants, and
 * `:events/dispatch!` marks the bridge fn that routes event
 * vectors into 10x's inlined re-frame router.
 * 
 * `:epochs/navigate` and `:events/navigate` are synonyms — the
 * former predates the `:events/...` family and is retained for
 * compatibility. New consumers should prefer the `:events/...`
 * namespace when branching on the mutation surface, since it
 * keeps reset / replay / dispatch! / navigate flags in one
 * semantic family.
 */
day8.re_frame_10x.public$.capabilities = (function day8$re_frame_10x$public$capabilities(){
return new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 12, [new cljs.core.Keyword("events","replay","events/replay",308211330),null,new cljs.core.Keyword("events","reset","events/reset",1597695043),null,new cljs.core.Keyword("settings","app-db-follows-events","settings/app-db-follows-events",-1575966268),null,new cljs.core.Keyword("public","v2","public/v2",1212177031),null,new cljs.core.Keyword("epochs","navigate","epochs/navigate",-1680216949),null,new cljs.core.Keyword("epochs","reset-app-db","epochs/reset-app-db",-1733227795),null,new cljs.core.Keyword("epochs","read","epochs/read",242974543),null,new cljs.core.Keyword("public","v1","public/v1",466868784),null,new cljs.core.Keyword("traces","read","traces/read",-196279789),null,new cljs.core.Keyword("events","dispatch!","events/dispatch!",-669489895),null,new cljs.core.Keyword("events","navigate","events/navigate",-1674985314),null,new cljs.core.Keyword("events","reset-app-db","events/reset-app-db",-1738555714),null], null), null);
});
goog.exportSymbol('day8.re_frame_10x.public$.capabilities', day8.re_frame_10x.public$.capabilities);
/**
 * Convert one internal match record into the public-epoch shape.
 * Public keys (`:sub-state-raw`, `:timings`) intentionally differ
 * from internal (`:sub-state`, `:timing`) so the public shape can
 * evolve independently. The match's id is hoisted to a top-level
 * `:id` for ergonomic indexing.
 */
day8.re_frame_10x.public$.match__GT_public_epoch = (function day8$re_frame_10x$public$match__GT_public_epoch(match){
if(cljs.core.truth_(match)){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"id","id",-1388402092),(function (){var G__23384 = match;
var G__23384__$1 = (((G__23384 == null))?null:new cljs.core.Keyword(null,"match-info","match-info",666319879).cljs$core$IFn$_invoke$arity$1(G__23384));
var G__23384__$2 = (((G__23384__$1 == null))?null:cljs.core.first(G__23384__$1));
if((G__23384__$2 == null)){
return null;
} else {
return new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(G__23384__$2);
}
})(),new cljs.core.Keyword(null,"match-info","match-info",666319879),new cljs.core.Keyword(null,"match-info","match-info",666319879).cljs$core$IFn$_invoke$arity$1(match),new cljs.core.Keyword(null,"sub-state-raw","sub-state-raw",1865618660),new cljs.core.Keyword(null,"sub-state","sub-state",-2129237981).cljs$core$IFn$_invoke$arity$1(match),new cljs.core.Keyword(null,"timings","timings",2084799169),new cljs.core.Keyword(null,"timing","timing",-1849225195).cljs$core$IFn$_invoke$arity$1(match)], null);
} else {
return null;
}
});
/**
 * Vec of every retained epoch in 10x's ring buffer, in the order
 * 10x stored them (oldest first; `last` is newest). Each element is
 * a public-epoch record (see `match->public-epoch`).
 * 
 * Returns `[]` when 10x's app-db hasn't initialised yet — this lets
 * consumers no-op gracefully on cold starts instead of having to
 * probe `loaded?` ahead of every call.
 */
day8.re_frame_10x.public$.epochs = (function (){var uncached_matches = ({});
var cache = cljs.core.volatile_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [uncached_matches,cljs.core.PersistentVector.EMPTY], null));
return (function (){
var matches = (function (){var G__23402 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db;
var G__23402__$1 = (((G__23402 == null))?null:cljs.core.deref(G__23402));
var G__23402__$2 = (((G__23402__$1 == null))?null:new cljs.core.Keyword(null,"epochs","epochs",1796936425).cljs$core$IFn$_invoke$arity$1(G__23402__$1));
if((G__23402__$2 == null)){
return null;
} else {
return new cljs.core.Keyword(null,"matches","matches",635497998).cljs$core$IFn$_invoke$arity$1(G__23402__$2);
}
})();
var vec__23395 = cljs.core.deref(cache);
var cached_matches = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23395,(0),null);
var cached_epochs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23395,(1),null);
if((matches === cached_matches)){
return cached_epochs;
} else {
var public_epochs = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(day8.re_frame_10x.public$.match__GT_public_epoch,matches);
cljs.core.vreset_BANG_(cache,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [matches,public_epochs], null));

return public_epochs;
}
});
})();
goog.exportSymbol('day8.re_frame_10x.public$.epochs', day8.re_frame_10x.public$.epochs);
/**
 * Number of retained epochs. Cheap — reads the `:match-ids` vec
 * length without rebuilding the full coerced epoch maps. Suitable
 * for poll-cadence callers (e.g. re-frame-pair's
 * `watch-epochs.sh`).
 */
day8.re_frame_10x.public$.epoch_count = (function day8$re_frame_10x$public$epoch_count(){
return cljs.core.count((function (){var G__23407 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db;
var G__23407__$1 = (((G__23407 == null))?null:cljs.core.deref(G__23407));
var G__23407__$2 = (((G__23407__$1 == null))?null:new cljs.core.Keyword(null,"epochs","epochs",1796936425).cljs$core$IFn$_invoke$arity$1(G__23407__$1));
if((G__23407__$2 == null)){
return null;
} else {
return new cljs.core.Keyword(null,"match-ids","match-ids",752973161).cljs$core$IFn$_invoke$arity$1(G__23407__$2);
}
})());
});
goog.exportSymbol('day8.re_frame_10x.public$.epoch_count', day8.re_frame_10x.public$.epoch_count);
/**
 * Id of the newest (most-recent) match in the buffer, or nil if
 * empty. Cheap — reads `:match-ids`' last element. 10x stores
 * epochs oldest-first, so the newest dispatch is at the tail.
 */
day8.re_frame_10x.public$.latest_epoch_id = (function day8$re_frame_10x$public$latest_epoch_id(){
var G__23416 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db;
var G__23416__$1 = (((G__23416 == null))?null:cljs.core.deref(G__23416));
var G__23416__$2 = (((G__23416__$1 == null))?null:new cljs.core.Keyword(null,"epochs","epochs",1796936425).cljs$core$IFn$_invoke$arity$1(G__23416__$1));
var G__23416__$3 = (((G__23416__$2 == null))?null:new cljs.core.Keyword(null,"match-ids","match-ids",752973161).cljs$core$IFn$_invoke$arity$1(G__23416__$2));
if((G__23416__$3 == null)){
return null;
} else {
return day8.re_frame_10x.tools.coll.last_in_vec(G__23416__$3);
}
});
goog.exportSymbol('day8.re_frame_10x.public$.latest_epoch_id', day8.re_frame_10x.public$.latest_epoch_id);
/**
 * Id of the epoch the 10x UI is currently focused on, or nil before
 * an epoch has been selected. On the live tail this is normally the
 * newest retained id, so consumers should compare it with
 * `latest-epoch-id` to detect whether 10x is following the tail.
 * When the user navigates back through history, `selected-epoch-id`
 * stays put while `latest-epoch-id` advances with new dispatches.
 */
day8.re_frame_10x.public$.selected_epoch_id = (function day8$re_frame_10x$public$selected_epoch_id(){
var G__23420 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db;
var G__23420__$1 = (((G__23420 == null))?null:cljs.core.deref(G__23420));
var G__23420__$2 = (((G__23420__$1 == null))?null:new cljs.core.Keyword(null,"epochs","epochs",1796936425).cljs$core$IFn$_invoke$arity$1(G__23420__$1));
if((G__23420__$2 == null)){
return null;
} else {
return new cljs.core.Keyword(null,"selected-epoch-id","selected-epoch-id",70601778).cljs$core$IFn$_invoke$arity$1(G__23420__$2);
}
});
goog.exportSymbol('day8.re_frame_10x.public$.selected_epoch_id', day8.re_frame_10x.public$.selected_epoch_id);
/**
 * Public-epoch record for the given match id, or nil if unknown
 * (id never existed, or aged out of the buffer).
 */
day8.re_frame_10x.public$.epoch_by_id = (function day8$re_frame_10x$public$epoch_by_id(id){
var G__23421 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db;
var G__23421__$1 = (((G__23421 == null))?null:cljs.core.deref(G__23421));
var G__23421__$2 = (((G__23421__$1 == null))?null:new cljs.core.Keyword(null,"epochs","epochs",1796936425).cljs$core$IFn$_invoke$arity$1(G__23421__$1));
var G__23421__$3 = (((G__23421__$2 == null))?null:new cljs.core.Keyword(null,"matches-by-id","matches-by-id",1749529562).cljs$core$IFn$_invoke$arity$1(G__23421__$2));
var G__23421__$4 = (((G__23421__$3 == null))?null:cljs.core.get.cljs$core$IFn$_invoke$arity$2(G__23421__$3,id));
if((G__23421__$4 == null)){
return null;
} else {
return day8.re_frame_10x.public$.match__GT_public_epoch(G__23421__$4);
}
});
goog.exportSymbol('day8.re_frame_10x.public$.epoch_by_id', day8.re_frame_10x.public$.epoch_by_id);
/**
 * The full retained trace stream — every `:event :sub/run :sub/create
 * :render :raf` etc. trace, in order. Vec; empty when 10x hasn't
 * initialised. Consumers that want to slice differently than 10x's
 * epoch-buffer (e.g. by op-type, time range, custom group key)
 * read this directly.
 */
day8.re_frame_10x.public$.all_traces = (function day8$re_frame_10x$public$all_traces(){
var or__5142__auto__ = (function (){var G__23431 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db;
var G__23431__$1 = (((G__23431 == null))?null:cljs.core.deref(G__23431));
var G__23431__$2 = (((G__23431__$1 == null))?null:new cljs.core.Keyword(null,"traces","traces",-1301138004).cljs$core$IFn$_invoke$arity$1(G__23431__$1));
if((G__23431__$2 == null)){
return null;
} else {
return new cljs.core.Keyword(null,"all","all",892129742).cljs$core$IFn$_invoke$arity$1(G__23431__$2);
}
})();
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
});
goog.exportSymbol('day8.re_frame_10x.public$.all_traces', day8.re_frame_10x.public$.all_traces);
/**
 * True iff 10x is currently configured to reset the user's app-db
 * to the focused epoch's `:app-db-after` snapshot when navigation
 * events fire (the default). When false, navigation events update
 * only 10x's UI cursor without touching userland — important for
 * downstream tools that drive 10x programmatically and need to
 * branch on whether `[load-epoch <id>]` will mutate userland or
 * just update the cursor.
 */
day8.re_frame_10x.public$.app_db_follows_events_QMARK_ = (function day8$re_frame_10x$public$app_db_follows_events_QMARK_(){
return cljs.core.boolean$((function (){var G__23441 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db;
var G__23441__$1 = (((G__23441 == null))?null:cljs.core.deref(G__23441));
var G__23441__$2 = (((G__23441__$1 == null))?null:new cljs.core.Keyword(null,"settings","settings",1556144875).cljs$core$IFn$_invoke$arity$1(G__23441__$1));
if((G__23441__$2 == null)){
return null;
} else {
return new cljs.core.Keyword(null,"app-db-follows-events?","app-db-follows-events?",-1566738462).cljs$core$IFn$_invoke$arity$1(G__23441__$2);
}
})());
});
goog.exportSymbol('day8.re_frame_10x.public$.app_db_follows_events_QMARK_', day8.re_frame_10x.public$.app_db_follows_events_QMARK_);
/**
 * Public event identifier. Dispatch via `(dispatch! [load-epoch <id>])`
 * to make 10x focus on the epoch with the given match id. When
 * `app-db-follows-events?` is true (the default), the user's app-db
 * resets to that epoch's `:app-db-after`.
 * 
 * Value is the fully-qualified string `"day8.re-frame-10x.public/load-epoch"`
 * — JS-constructable, so pure-JS callers via `goog.global` can
 * either read this var or build the same literal.
 */
day8.re_frame_10x.public$.load_epoch = "day8.re-frame-10x.public/load-epoch";
goog.exportSymbol('day8.re_frame_10x.public$.load_epoch', day8.re_frame_10x.public$.load_epoch);
/**
 * Public event identifier. Dispatch via `(dispatch! [most-recent-epoch])`
 * to make 10x focus on the newest match (the 'live tail'). Useful
 * after a programmatic load-epoch to return control to the user.
 * 
 * When `app-db-follows-events?` is true, this is the canonical way
 * to re-sync userland to the live tail after a programmatic
 * load-epoch overwrote the user's app-db with a historical
 * `:app-db-after` snapshot — it moves the cursor to the newest
 * match and resets the user's app-db to that match's
 * `:app-db-after`. No other public mutation event combines those
 * two steps; without it, userland app-db keeps evolving from the
 * historical starting point load-epoch left it in.
 * 
 * Value is the fully-qualified string
 * `"day8.re-frame-10x.public/most-recent-epoch"`.
 */
day8.re_frame_10x.public$.most_recent_epoch = "day8.re-frame-10x.public/most-recent-epoch";
goog.exportSymbol('day8.re_frame_10x.public$.most_recent_epoch', day8.re_frame_10x.public$.most_recent_epoch);
/**
 * Public event identifier. Dispatch via `(dispatch! [previous-epoch])`
 * to step the 10x UI cursor one match backwards from the currently
 * focused epoch. No-op when already at the oldest retained match.
 * When no epoch is focused (the 'live tail'), steps to the
 * second-newest retained match; no-op if fewer than two matches
 * are retained. When `app-db-follows-events?` is true, the user's
 * app-db resets to the new epoch's `:app-db-after`.
 * 
 * Value is the fully-qualified string
 * `"day8.re-frame-10x.public/previous-epoch"`.
 */
day8.re_frame_10x.public$.previous_epoch = "day8.re-frame-10x.public/previous-epoch";
goog.exportSymbol('day8.re_frame_10x.public$.previous_epoch', day8.re_frame_10x.public$.previous_epoch);
/**
 * Public event identifier. Dispatch via `(dispatch! [next-epoch])` to
 * step the 10x UI cursor one match forwards from the currently
 * focused epoch. No-op when already at the newest retained match.
 * When no epoch is focused, jumps to the live tail. When
 * `app-db-follows-events?` is true, the user's app-db resets to the
 * new epoch's `:app-db-after`.
 * 
 * Value is the fully-qualified string
 * `"day8.re-frame-10x.public/next-epoch"`.
 */
day8.re_frame_10x.public$.next_epoch = "day8.re-frame-10x.public/next-epoch";
goog.exportSymbol('day8.re_frame_10x.public$.next_epoch', day8.re_frame_10x.public$.next_epoch);
/**
 * Public event identifier. Dispatch via `(dispatch! [reset-epochs])` to
 * clear 10x's epoch buffer and reset re-frame.trace's id counter.
 * Equivalent to clicking the 'reset' button in 10x's UI.
 * 
 * Value is the fully-qualified string
 * `"day8.re-frame-10x.public/reset-epochs"`.
 */
day8.re_frame_10x.public$.reset_epochs = "day8.re-frame-10x.public/reset-epochs";
goog.exportSymbol('day8.re_frame_10x.public$.reset_epochs', day8.re_frame_10x.public$.reset_epochs);
/**
 * Public event identifier. Dispatch via `(dispatch! [replay-epoch])` to
 * replay the focused epoch's event against the app-db state captured
 * BEFORE that event originally fired (the epoch's `:app-db-before`).
 * Equivalent to time-travelling to the epoch and re-firing — the
 * resulting userland app-db is the post-event state of that epoch,
 * regardless of any subsequent dispatches. Idempotent: repeated
 * replays of the same epoch produce the same post-event state.
 * Equivalent to clicking 10x's 'replay' button.
 * 
 * Value is the fully-qualified string
 * `"day8.re-frame-10x.public/replay-epoch"`.
 */
day8.re_frame_10x.public$.replay_epoch = "day8.re-frame-10x.public/replay-epoch";
goog.exportSymbol('day8.re_frame_10x.public$.replay_epoch', day8.re_frame_10x.public$.replay_epoch);
/**
 * Public event identifier. Dispatch via
 * `(dispatch! [reset-app-db-event <id>])` to reset the user's app-db
 * to the `:app-db-after` snapshot for the epoch with the given match
 * id, without moving 10x's selected epoch cursor. No-op when
 * `app-db-follows-events?` is false.
 * 
 * Lower-level than `load-epoch`: `load-epoch` moves the 10x cursor
 * and then resets the user's app-db when following is enabled; this
 * event is only the app-db-reset half.
 * 
 * Value is the fully-qualified string
 * `"day8.re-frame-10x.public/reset-app-db"`.
 */
day8.re_frame_10x.public$.reset_app_db_event = "day8.re-frame-10x.public/reset-app-db";
goog.exportSymbol('day8.re_frame_10x.public$.reset_app_db_event', day8.re_frame_10x.public$.reset_app_db_event);
/**
 * Translation table from public mutation event identifiers (kw form
 * of the exported strings) to the internal inlined-rf event keywords
 * they fan out to. `dispatch!` consults this on every call; entries
 * not in the map (e.g. `::previous-epoch`, whose load-bearing cond
 * logic lives in a public.events forwarder) still resolve after
 * string heads are coerced to keyword form.
 * 
 * This is the contract boundary: public string identifiers are the
 * durable LHS, internal kws are the volatile RHS. A future internal
 * rename touches only this map — the public string consts above stay
 * put.
 */
day8.re_frame_10x.public$.public__GT_internal = cljs.core.PersistentArrayMap.createAsIfByAssoc([cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.public$.load_epoch),new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","load","day8.re-frame-10x.navigation.epochs.events/load",1738587584),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.public$.most_recent_epoch),new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","most-recent","day8.re-frame-10x.navigation.epochs.events/most-recent",-1146993774),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.public$.next_epoch),new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","next","day8.re-frame-10x.navigation.epochs.events/next",1388476595),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.public$.reset_epochs),new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","reset","day8.re-frame-10x.navigation.epochs.events/reset",-2105765050),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.public$.replay_epoch),new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","replay","day8.re-frame-10x.navigation.epochs.events/replay",-1356562037),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1("day8.re-frame-10x.public/reset-app-db"),new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","reset-current-epoch-app-db","day8.re-frame-10x.navigation.epochs.events/reset-current-epoch-app-db",1850636212)]);
/**
 * Mutation-API bridge: routes `event-vec` (e.g.
 * `[load-epoch 42]`) through 10x's *inlined* re-frame router.
 * Necessary because 10x events register against the inlined
 * `day8.re-frame-10x.inlined-deps.re-frame.v1v3v0` re-frame core
 * — a consumer's plain `(re-frame.core/dispatch ...)` would never
 * reach them. Use the string identifiers exported from this
 * namespace as the first element of `event-vec`; the strings are
 * the durable contract.
 * 
 * Coerces JS-array arguments to CLJS vectors, so pure-JS callers
 * via `goog.global.day8.re_frame_10x.public.dispatch_BANG_(['evt', arg])`
 * work — the inlined router validates events with `(vector? ...)`,
 * which JS arrays fail.
 * 
 * Coerces a string head to a keyword before forwarding, since
 * re-frame's handler-lookup keys are keywords. Pure-JS callers
 * that don't have access to `cljs.core.keyword` can therefore pass
 * the exported string identifiers directly.
 * 
 * Translates public mutation kws to their internal counterparts via
 * `public->internal` so the public surface and the internal handlers
 * need not share names — the public strings are durable, the internal
 * kws can rename freely. Unmapped string heads are keywordised before
 * dispatch, while unmapped keyword heads pass through as-is so direct
 * dispatches (e.g. the `::previous-epoch` forwarder, or ad-hoc
 * internal kws routed through this bridge by tooling) still resolve.
 */
day8.re_frame_10x.public$.dispatch_BANG_ = (function day8$re_frame_10x$public$dispatch_BANG_(event_vec){
var v = ((cljs.core.vector_QMARK_(event_vec))?event_vec:cljs.core.vec(cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1(event_vec)));
var h = cljs.core.first(v);
var kw = ((typeof h === 'string')?cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(h):h);
var translated = cljs.core.get.cljs$core$IFn$_invoke$arity$2(day8.re_frame_10x.public$.public__GT_internal,kw);
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.dispatch((((!((translated == null))))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(v,(0),translated):((typeof h === 'string')?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(v,(0),kw):v
)));
});
goog.exportSymbol('day8.re_frame_10x.public$.dispatch_BANG_', day8.re_frame_10x.public$.dispatch_BANG_);
var temp__5825__auto___23482 = (function (){var and__5140__auto__ = (typeof goog !== 'undefined');
if(and__5140__auto__){
return goog.global;
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_(temp__5825__auto___23482)){
var g_23483 = temp__5825__auto___23482;
var rf10x_23484 = (function (){var G__23458 = g_23483;
var G__23458__$1 = (((G__23458 == null))?null:(G__23458["day8"]));
if((G__23458__$1 == null)){
return null;
} else {
return (G__23458__$1["re_frame_10x"]);
}
})();
var suffixed_23485 = (function (){var G__23459 = rf10x_23484;
if((G__23459 == null)){
return null;
} else {
return (G__23459["public$"]);
}
})();
if(cljs.core.truth_((function (){var and__5140__auto__ = rf10x_23484;
if(cljs.core.truth_(and__5140__auto__)){
return suffixed_23485;
} else {
return and__5140__auto__;
}
})())){
(rf10x_23484["public"] = suffixed_23485);
} else {
}
} else {
}

//# sourceMappingURL=day8.re_frame_10x.public.js.map
