goog.provide('re_frame.trace');
if((typeof re_frame !== 'undefined') && (typeof re_frame.trace !== 'undefined') && (typeof re_frame.trace.id !== 'undefined')){
} else {
re_frame.trace.id = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
}
re_frame.trace._STAR_current_trace_STAR_ = null;
re_frame.trace.reset_tracing_BANG_ = (function re_frame$trace$reset_tracing_BANG_(){
return cljs.core.reset_BANG_(re_frame.trace.id,(0));
});
/**
 * Schema for `:tags` of every op-type re-frame emits. Entries:
 * 
 *     <op-type> {:required #{<key> ...}    ; tags every emit MUST carry
 *                :optional #{<key> ...}    ; tags an emit MAY carry
 *                :doc      "..."}        ; one-liner — what the trace marks
 * 
 * `:tags` is a map; keys not listed under `:required` or `:optional`
 * for the matching op-type are 'unknown' and (when
 * `(validate-trace?)` is true) raise a console warning so consumers
 * can spot drift early.
 * 
 * This schema is the load-bearing contract for downstream tooling.
 * Adding a key = additive, no version bump. Renaming or removing a
 * key = breaking, must be staged with a deprecation cycle. The
 * doc-only / opt-in posture means production builds pay zero cost
 * for the contract — the schema lives here as a reference, and
 * only flips on under explicit dev opt-in.
 */
re_frame.trace.tag_schema = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"raf-end","raf-end",-220596864),new cljs.core.Keyword("sub","run","sub/run",-1821315581),new cljs.core.Keyword("reagent","quiescent","reagent/quiescent",-16138681),new cljs.core.Keyword("sub","create","sub/create",-1301317560),new cljs.core.Keyword(null,"flow","flow",590489032),new cljs.core.Keyword("re-frame.router","fsm-trigger","re-frame.router/fsm-trigger",1379787274),new cljs.core.Keyword(null,"sync","sync",-624148946),new cljs.core.Keyword("event","handler","event/handler",-295903150),new cljs.core.Keyword(null,"render","render",-1408033454),new cljs.core.Keyword(null,"event","event",301435442),new cljs.core.Keyword("event","do-fx","event/do-fx",1357330452),new cljs.core.Keyword(null,"raf","raf",-1295410152),new cljs.core.Keyword("sub","dispose","sub/dispose",365440536)],[new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"doc","doc",1913296891),"End of the reagent next-tick batch \u2014 emitted by re-frame-10x's batching patch."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"query-v","query-v",-1514170131),null,new cljs.core.Keyword(null,"reaction","reaction",490869788),null], null), null),new cljs.core.Keyword(null,"optional","optional",2053951509),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"dyn-v","dyn-v",949994592),null,new cljs.core.Keyword(null,"value","value",305978217),null,new cljs.core.Keyword(null,"input-query-vs","input-query-vs",1142346991),null,new cljs.core.Keyword(null,"input-signals","input-signals",563633497),null], null), null),new cljs.core.Keyword(null,"doc","doc",1913296891),"Subscription compute fn ran. Result is in :value."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"doc","doc",1913296891),"Reagent render queue is idle \u2014 emitted by re-frame-10x's batching patch."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"query-v","query-v",-1514170131),null], null), null),new cljs.core.Keyword(null,"optional","optional",2053951509),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"dyn-v","dyn-v",949994592),null,new cljs.core.Keyword(null,"cached?","cached?",86081880),null,new cljs.core.Keyword(null,"reaction","reaction",490869788),null], null), null),new cljs.core.Keyword(null,"doc","doc",1913296891),"Subscribe call \u2014 either resolves from the reaction cache (`:cached? true`) or builds a new reaction."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"id->live-in","id->live-in",998532194),null,new cljs.core.Keyword(null,"id->in","id->in",-693826300),null,new cljs.core.Keyword(null,"transition","transition",765692007),null,new cljs.core.Keyword(null,"db","db",993250759),null,new cljs.core.Keyword(null,"id->old-in","id->old-in",-504402935),null,new cljs.core.Keyword(null,"flow-spec","flow-spec",-119790292),null,new cljs.core.Keyword(null,"new-db","new-db",1305352401),null,new cljs.core.Keyword(null,"id->old-live-in","id->old-live-in",992766074),null], null), null),new cljs.core.Keyword(null,"optional","optional",2053951509),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"doc","doc",1913296891),"Flow alpha transition \u2014 emitted when a registered flow updates its derived output."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"current-state","current-state",1048284452),null,new cljs.core.Keyword(null,"new-state","new-state",-490349212),null], null), null),new cljs.core.Keyword(null,"doc","doc",1913296891),"Router queue state-machine transition. Fires as the dispatch queue changes state."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"doc","doc",1913296891),"Synchronous dispatch boundary \u2014 emitted after dispatch-sync runs the event and post-event callbacks."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678),null,new cljs.core.Keyword(null,"parent-dispatch-id","parent-dispatch-id",1812056977),null,new cljs.core.Keyword(null,"event","event",301435442),null], null), null),new cljs.core.Keyword(null,"doc","doc",1913296891),"The user's reg-event-* fn body, fired inside the event interceptor chain."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-name","component-name",-1318676056),null,new cljs.core.Keyword(null,"reaction","reaction",490869788),null], null), null),new cljs.core.Keyword(null,"doc","doc",1913296891),"Component render \u2014 emitted by re-frame-10x's reagent patch (NOT by re-frame core); included here so the schema covers what consumers actually read."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"event","event",301435442),null], null), null),new cljs.core.Keyword(null,"optional","optional",2053951509),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 9, [new cljs.core.Keyword("event","original","event/original",463298689),null,new cljs.core.Keyword(null,"app-db-after","app-db-after",1477492964),null,new cljs.core.Keyword(null,"app-db-before","app-db-before",-1442902645),null,new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678),null,new cljs.core.Keyword(null,"parent-dispatch-id","parent-dispatch-id",1812056977),null,new cljs.core.Keyword(null,"effects","effects",-282369292),null,new cljs.core.Keyword(null,"code","code",1586293142),null,new cljs.core.Keyword(null,"coeffects","coeffects",497912985),null,new cljs.core.Keyword(null,"interceptors","interceptors",-1546782951),null], null), null),new cljs.core.Keyword(null,"doc","doc",1913296891),"Top-level dispatch \u2014 fired by re-frame.router/dispatch / dispatch-sync."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678),null,new cljs.core.Keyword(null,"parent-dispatch-id","parent-dispatch-id",1812056977),null], null), null),new cljs.core.Keyword(null,"doc","doc",1913296891),"do-fx interceptor \u2014 fires registered fx handlers for the event's :effects map."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"optional","optional",2053951509),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"doc","doc",1913296891),"Reagent next-tick boundary \u2014 emitted by re-frame-10x's batching patch."], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"required","required",1807647006),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"query-v","query-v",-1514170131),null,new cljs.core.Keyword(null,"reaction","reaction",490869788),null], null), null),new cljs.core.Keyword(null,"optional","optional",2053951509),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"doc","doc",1913296891),"Reaction garbage-collected (Reagent on-dispose)."], null)]);
re_frame.trace.validate_trace_enabled_QMARK_ = false;
/**
 * True iff the runtime should validate that emitted trace `:tags`
 * conform to `tag-schema`. Off by default; toggle with
 * `set-validate-trace!`. Intended for dev / CI; production builds
 * should leave it off (the trace machinery is itself gated on
 * `is-trace-enabled?`, but validation adds a per-trace map-walk
 * that's not free).
 */
re_frame.trace.validate_trace_QMARK_ = (function re_frame$trace$validate_trace_QMARK_(){
return re_frame.trace.validate_trace_enabled_QMARK_;
});
/**
 * Enable / disable trace-tag validation. When true, every
 * `finish-trace` checks `:tags` against `tag-schema` and warns via
 * `console :warn` on missing required keys or unknown keys.
 */
re_frame.trace.set_validate_trace_BANG_ = (function re_frame$trace$set_validate_trace_BANG_(enabled_QMARK_){
return (re_frame.trace.validate_trace_enabled_QMARK_ = cljs.core.boolean$(enabled_QMARK_));
});
/**
 * Walk a finished trace map and warn about missing/unknown tag
 * keys for its op-type. No-op when op-type isn't in the schema —
 * third-party op-types stay unconstrained.
 * 
 * Public because `finish-trace` is a macro that expands in the
 * caller's namespace (`re-frame.events`, custom instrumentation,
 * etc.); a private var here would fail the var-resolution check
 * when the expansion's `(check-trace-against-schema ...)` form
 * compiles in the caller. Callers shouldn't invoke this directly
 * — it's part of the validation flow gated by
 * `validate-trace?`.
 */
re_frame.trace.check_trace_against_schema = (function re_frame$trace$check_trace_against_schema(trace){
var temp__5825__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(re_frame.trace.tag_schema,new cljs.core.Keyword(null,"op-type","op-type",-1636141668).cljs$core$IFn$_invoke$arity$1(trace));
if(cljs.core.truth_(temp__5825__auto__)){
var map__21509 = temp__5825__auto__;
var map__21509__$1 = cljs.core.__destructure_map(map__21509);
var required = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21509__$1,new cljs.core.Keyword(null,"required","required",1807647006));
var optional = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21509__$1,new cljs.core.Keyword(null,"optional","optional",2053951509));
var doc = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21509__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var tags = (function (){var or__5142__auto__ = new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(trace);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.PersistentArrayMap.EMPTY;
}
})();
var present = cljs.core.set(cljs.core.keys(tags));
var missing = clojure.set.difference.cljs$core$IFn$_invoke$arity$2(required,present);
var allowed = clojure.set.union.cljs$core$IFn$_invoke$arity$2(required,optional);
var unknown = clojure.set.difference.cljs$core$IFn$_invoke$arity$2(present,allowed);
if(cljs.core.seq(missing)){
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame.trace: trace",new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(trace),"of op-type",new cljs.core.Keyword(null,"op-type","op-type",-1636141668).cljs$core$IFn$_invoke$arity$1(trace),"is missing required tag key(s)",missing,"\u2014 see re-frame.trace/tag-schema."], 0));
} else {
}

if(cljs.core.seq(unknown)){
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame.trace: trace",new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(trace),"of op-type",new cljs.core.Keyword(null,"op-type","op-type",-1636141668).cljs$core$IFn$_invoke$arity$1(trace),"carries unknown tag key(s)",unknown,"\u2014 either register them in re-frame.trace/tag-schema","or treat them as not-part-of-the-public-contract."], 0));
} else {
return null;
}
} else {
return null;
}
});
/**
 * @define {boolean}
 * @type {boolean}
 */
re_frame.trace.trace_enabled_QMARK_ = goog.define("re_frame.trace.trace_enabled_QMARK_",false);
/**
 * See https://groups.google.com/d/msg/clojurescript/jk43kmYiMhA/IHglVr_TPdgJ for more details
 */
re_frame.trace.is_trace_enabled_QMARK_ = (function re_frame$trace$is_trace_enabled_QMARK_(){
return re_frame.trace.trace_enabled_QMARK_;
});
if((typeof re_frame !== 'undefined') && (typeof re_frame.trace !== 'undefined') && (typeof re_frame.trace.trace_cbs !== 'undefined')){
} else {
re_frame.trace.trace_cbs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
if((typeof re_frame !== 'undefined') && (typeof re_frame.trace !== 'undefined') && (typeof re_frame.trace.traces !== 'undefined')){
} else {
re_frame.trace.traces = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
}
if((typeof re_frame !== 'undefined') && (typeof re_frame.trace !== 'undefined') && (typeof re_frame.trace.next_delivery !== 'undefined')){
} else {
re_frame.trace.next_delivery = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
}
/**
 * Registers a tracing callback function which will receive a collection of one or more traces.
 *   Will replace an existing callback function if it shares the same key.
 * 
 *   See also: `register-epoch-cb` for assembled per-dispatch epoch
 *   records, and `tag-schema` for the documented `:tags` shape of
 *   emitted traces.
 */
re_frame.trace.register_trace_cb = (function re_frame$trace$register_trace_cb(key,f){
if(re_frame.trace.trace_enabled_QMARK_){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(re_frame.trace.trace_cbs,cljs.core.assoc,key,f);
} else {
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Tracing is not enabled. Please set {\"re_frame.trace.trace_enabled_QMARK_\" true} in :closure-defines. See: https://github.com/day8/re-frame-10x#installation."], 0));
}
});
re_frame.trace.remove_trace_cb = (function re_frame$trace$remove_trace_cb(key){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(re_frame.trace.trace_cbs,cljs.core.dissoc,key);

return null;
});
if((typeof re_frame !== 'undefined') && (typeof re_frame.trace !== 'undefined') && (typeof re_frame.trace.epoch_cbs !== 'undefined')){
} else {
re_frame.trace.epoch_cbs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
/**
 * Register a callback `f` keyed on `key` that will receive a
 * collection of one or more assembled EPOCH records — one per
 * `:event` trace in each delivery batch. See the namespace docstring
 * above `epoch-cbs` for the epoch shape and the four cascade /
 * assembly-location decisions.
 * 
 * Like `register-trace-cb`: gated on `trace-enabled?`; replaces an
 * existing cb sharing the same key; warns when tracing is disabled.
 */
re_frame.trace.register_epoch_cb = (function re_frame$trace$register_epoch_cb(key,f){
if(re_frame.trace.trace_enabled_QMARK_){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(re_frame.trace.epoch_cbs,cljs.core.assoc,key,f);
} else {
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame.trace: register-epoch-cb skipped \u2014 tracing is not enabled. Set {\"re_frame.trace.trace_enabled_QMARK_\" true} in :closure-defines."], 0));
}
});
re_frame.trace.remove_epoch_cb = (function re_frame$trace$remove_epoch_cb(key){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(re_frame.trace.epoch_cbs,cljs.core.dissoc,key);

return null;
});
re_frame.trace.next_id = (function re_frame$trace$next_id(){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(re_frame.trace.id,cljs.core.inc);
});
re_frame.trace.start_trace = (function re_frame$trace$start_trace(p__21513){
var map__21514 = p__21513;
var map__21514__$1 = cljs.core.__destructure_map(map__21514);
var operation = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21514__$1,new cljs.core.Keyword(null,"operation","operation",-1267664310));
var op_type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21514__$1,new cljs.core.Keyword(null,"op-type","op-type",-1636141668));
var tags = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21514__$1,new cljs.core.Keyword(null,"tags","tags",1771418977));
var child_of = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21514__$1,new cljs.core.Keyword(null,"child-of","child-of",-903376662));
return new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"id","id",-1388402092),re_frame.trace.next_id(),new cljs.core.Keyword(null,"operation","operation",-1267664310),operation,new cljs.core.Keyword(null,"op-type","op-type",-1636141668),op_type,new cljs.core.Keyword(null,"tags","tags",1771418977),tags,new cljs.core.Keyword(null,"child-of","child-of",-903376662),(function (){var or__5142__auto__ = child_of;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(re_frame.trace._STAR_current_trace_STAR_);
}
})(),new cljs.core.Keyword(null,"start","start",-355208981),re_frame.interop.now()], null);
});
re_frame.trace.debounce_time = (50);
re_frame.trace.debounce = (function re_frame$trace$debounce(f,interval){
return goog.functions.debounce(f,interval);
});
/**
 * Walk a batch of finished traces; emit one epoch record per
 * `:event` trace. Child traces are partitioned via the
 * `:child-of` link `start-trace` already populates from
 * `*current-trace*`.
 * 
 * KNOWN LIMITATIONS
 * 
 * - `:render` traces fire on a later RAF tick (after the user's
 *   event handler has returned), with `*current-trace*` either
 *   unbound or in some outer scope. They typically have
 *   `:child-of` nil, so this assembly DOESN'T attach them to the
 *   parent epoch. Consumers that want renders should subscribe
 *   to `register-trace-cb` and correlate by time / op-type — same
 *   as 10x does today via its own batching patch.
 * 
 * - Only direct `:child-of` children are picked up. Grandchildren
 *   (e.g. a `:sub/run` whose ratom-deref triggers another
 *   `:sub/run`) live as separate `:sub/run` entries on this same
 *   epoch — flat, not nested. The trace tree's full shape is in
 *   the underlying trace stream for callers that want it.
 */
re_frame.trace.assemble_epochs = (function re_frame$trace$assemble_epochs(batch){
var by_parent = cljs.core.group_by(new cljs.core.Keyword(null,"child-of","child-of",-903376662),batch);
var events = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__21515_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"event","event",301435442),new cljs.core.Keyword(null,"op-type","op-type",-1636141668).cljs$core$IFn$_invoke$arity$1(p1__21515_SHARP_));
}),batch);
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (event_tr){
var event_id = new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(event_tr);
var children = cljs.core.get.cljs$core$IFn$_invoke$arity$3(by_parent,event_id,cljs.core.PersistentVector.EMPTY);
var by_op = cljs.core.group_by(new cljs.core.Keyword(null,"op-type","op-type",-1636141668),children);
var tags = new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(event_tr);
return cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword("event","original","event/original",463298689),new cljs.core.Keyword("app-db","before","app-db/before",1917573250),new cljs.core.Keyword(null,"event-handler","event-handler",-487718843),new cljs.core.Keyword("app-db","after","app-db/after",391310536),new cljs.core.Keyword(null,"sub-creates","sub-creates",1645524584),new cljs.core.Keyword(null,"event-do-fx","event-do-fx",-893983895),new cljs.core.Keyword(null,"start","start",-355208981),new cljs.core.Keyword(null,"sub-runs","sub-runs",-2081830484),new cljs.core.Keyword(null,"duration","duration",1444101068),new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678),new cljs.core.Keyword(null,"parent-dispatch-id","parent-dispatch-id",1812056977),new cljs.core.Keyword(null,"event","event",301435442),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"effects","effects",-282369292),new cljs.core.Keyword(null,"coeffects","coeffects",497912985),new cljs.core.Keyword(null,"interceptors","interceptors",-1546782951),new cljs.core.Keyword(null,"end","end",-268185958)],[new cljs.core.Keyword("event","original","event/original",463298689).cljs$core$IFn$_invoke$arity$1(tags),new cljs.core.Keyword(null,"app-db-before","app-db-before",-1442902645).cljs$core$IFn$_invoke$arity$1(tags),cljs.core.first(new cljs.core.Keyword("event","handler","event/handler",-295903150).cljs$core$IFn$_invoke$arity$1(by_op)),new cljs.core.Keyword(null,"app-db-after","app-db-after",1477492964).cljs$core$IFn$_invoke$arity$1(tags),cljs.core.vec(new cljs.core.Keyword("sub","create","sub/create",-1301317560).cljs$core$IFn$_invoke$arity$1(by_op)),cljs.core.first(new cljs.core.Keyword("event","do-fx","event/do-fx",1357330452).cljs$core$IFn$_invoke$arity$1(by_op)),new cljs.core.Keyword(null,"start","start",-355208981).cljs$core$IFn$_invoke$arity$1(event_tr),cljs.core.vec(new cljs.core.Keyword("sub","run","sub/run",-1821315581).cljs$core$IFn$_invoke$arity$1(by_op)),new cljs.core.Keyword(null,"duration","duration",1444101068).cljs$core$IFn$_invoke$arity$1(event_tr),new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678).cljs$core$IFn$_invoke$arity$1(tags),new cljs.core.Keyword(null,"parent-dispatch-id","parent-dispatch-id",1812056977).cljs$core$IFn$_invoke$arity$1(tags),new cljs.core.Keyword(null,"event","event",301435442).cljs$core$IFn$_invoke$arity$1(tags),event_id,new cljs.core.Keyword(null,"effects","effects",-282369292).cljs$core$IFn$_invoke$arity$1(tags),new cljs.core.Keyword(null,"coeffects","coeffects",497912985).cljs$core$IFn$_invoke$arity$1(tags),new cljs.core.Keyword(null,"interceptors","interceptors",-1546782951).cljs$core$IFn$_invoke$arity$1(tags),new cljs.core.Keyword(null,"end","end",-268185958).cljs$core$IFn$_invoke$arity$1(event_tr)]);
}),events);
});
re_frame.trace.schedule_debounce = re_frame.trace.debounce((function re_frame$trace$tracing_cb_debounced(){
var batch_21605 = cljs.core.deref(re_frame.trace.traces);
var seq__21518_21606 = cljs.core.seq(cljs.core.deref(re_frame.trace.trace_cbs));
var chunk__21519_21607 = null;
var count__21520_21608 = (0);
var i__21521_21609 = (0);
while(true){
if((i__21521_21609 < count__21520_21608)){
var vec__21531_21610 = chunk__21519_21607.cljs$core$IIndexed$_nth$arity$2(null,i__21521_21609);
var k_21611 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21531_21610,(0),null);
var cb_21612 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21531_21610,(1),null);
try{(cb_21612.cljs$core$IFn$_invoke$arity$1 ? cb_21612.cljs$core$IFn$_invoke$arity$1(batch_21605) : cb_21612.call(null,batch_21605));
}catch (e21535){var e_21613 = e21535;
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Error thrown from trace cb",k_21611,"while storing",batch_21605,e_21613], 0));
}

var G__21614 = seq__21518_21606;
var G__21615 = chunk__21519_21607;
var G__21616 = count__21520_21608;
var G__21617 = (i__21521_21609 + (1));
seq__21518_21606 = G__21614;
chunk__21519_21607 = G__21615;
count__21520_21608 = G__21616;
i__21521_21609 = G__21617;
continue;
} else {
var temp__5825__auto___21618 = cljs.core.seq(seq__21518_21606);
if(temp__5825__auto___21618){
var seq__21518_21619__$1 = temp__5825__auto___21618;
if(cljs.core.chunked_seq_QMARK_(seq__21518_21619__$1)){
var c__5673__auto___21620 = cljs.core.chunk_first(seq__21518_21619__$1);
var G__21621 = cljs.core.chunk_rest(seq__21518_21619__$1);
var G__21622 = c__5673__auto___21620;
var G__21623 = cljs.core.count(c__5673__auto___21620);
var G__21624 = (0);
seq__21518_21606 = G__21621;
chunk__21519_21607 = G__21622;
count__21520_21608 = G__21623;
i__21521_21609 = G__21624;
continue;
} else {
var vec__21536_21625 = cljs.core.first(seq__21518_21619__$1);
var k_21626 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21536_21625,(0),null);
var cb_21627 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21536_21625,(1),null);
try{(cb_21627.cljs$core$IFn$_invoke$arity$1 ? cb_21627.cljs$core$IFn$_invoke$arity$1(batch_21605) : cb_21627.call(null,batch_21605));
}catch (e21539){var e_21628 = e21539;
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Error thrown from trace cb",k_21626,"while storing",batch_21605,e_21628], 0));
}

var G__21629 = cljs.core.next(seq__21518_21619__$1);
var G__21630 = null;
var G__21631 = (0);
var G__21632 = (0);
seq__21518_21606 = G__21629;
chunk__21519_21607 = G__21630;
count__21520_21608 = G__21631;
i__21521_21609 = G__21632;
continue;
}
} else {
}
}
break;
}

if(cljs.core.seq(cljs.core.deref(re_frame.trace.epoch_cbs))){
var epochs_21633 = re_frame.trace.assemble_epochs(batch_21605);
var seq__21543_21634 = cljs.core.seq(cljs.core.deref(re_frame.trace.epoch_cbs));
var chunk__21544_21635 = null;
var count__21545_21636 = (0);
var i__21546_21637 = (0);
while(true){
if((i__21546_21637 < count__21545_21636)){
var vec__21560_21639 = chunk__21544_21635.cljs$core$IIndexed$_nth$arity$2(null,i__21546_21637);
var k_21640 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21560_21639,(0),null);
var cb_21641 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21560_21639,(1),null);
try{(cb_21641.cljs$core$IFn$_invoke$arity$1 ? cb_21641.cljs$core$IFn$_invoke$arity$1(epochs_21633) : cb_21641.call(null,epochs_21633));
}catch (e21563){var e_21642 = e21563;
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Error thrown from epoch cb",k_21640,"while delivering",cljs.core.count(epochs_21633),"epoch(s)",e_21642], 0));
}

var G__21644 = seq__21543_21634;
var G__21645 = chunk__21544_21635;
var G__21646 = count__21545_21636;
var G__21647 = (i__21546_21637 + (1));
seq__21543_21634 = G__21644;
chunk__21544_21635 = G__21645;
count__21545_21636 = G__21646;
i__21546_21637 = G__21647;
continue;
} else {
var temp__5825__auto___21648 = cljs.core.seq(seq__21543_21634);
if(temp__5825__auto___21648){
var seq__21543_21649__$1 = temp__5825__auto___21648;
if(cljs.core.chunked_seq_QMARK_(seq__21543_21649__$1)){
var c__5673__auto___21650 = cljs.core.chunk_first(seq__21543_21649__$1);
var G__21651 = cljs.core.chunk_rest(seq__21543_21649__$1);
var G__21652 = c__5673__auto___21650;
var G__21653 = cljs.core.count(c__5673__auto___21650);
var G__21654 = (0);
seq__21543_21634 = G__21651;
chunk__21544_21635 = G__21652;
count__21545_21636 = G__21653;
i__21546_21637 = G__21654;
continue;
} else {
var vec__21565_21657 = cljs.core.first(seq__21543_21649__$1);
var k_21658 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21565_21657,(0),null);
var cb_21659 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21565_21657,(1),null);
try{(cb_21659.cljs$core$IFn$_invoke$arity$1 ? cb_21659.cljs$core$IFn$_invoke$arity$1(epochs_21633) : cb_21659.call(null,epochs_21633));
}catch (e21568){var e_21660 = e21568;
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Error thrown from epoch cb",k_21658,"while delivering",cljs.core.count(epochs_21633),"epoch(s)",e_21660], 0));
}

var G__21661 = cljs.core.next(seq__21543_21649__$1);
var G__21662 = null;
var G__21663 = (0);
var G__21664 = (0);
seq__21543_21634 = G__21661;
chunk__21544_21635 = G__21662;
count__21545_21636 = G__21663;
i__21546_21637 = G__21664;
continue;
}
} else {
}
}
break;
}
} else {
}

return cljs.core.reset_BANG_(re_frame.trace.traces,cljs.core.PersistentVector.EMPTY);
}),re_frame.trace.debounce_time);
re_frame.trace.run_tracing_callbacks_BANG_ = (function re_frame$trace$run_tracing_callbacks_BANG_(now){
if(((cljs.core.deref(re_frame.trace.next_delivery) - (25)) < now)){
(re_frame.trace.schedule_debounce.cljs$core$IFn$_invoke$arity$0 ? re_frame.trace.schedule_debounce.cljs$core$IFn$_invoke$arity$0() : re_frame.trace.schedule_debounce.call(null));

return cljs.core.reset_BANG_(re_frame.trace.next_delivery,(now + re_frame.trace.debounce_time));
} else {
return null;
}
});

//# sourceMappingURL=re_frame.trace.js.map
