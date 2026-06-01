goog.provide('re_frame.fx');
re_frame.fx.kind = new cljs.core.Keyword(null,"fx","fx",-1237829572);
if(cljs.core.truth_((re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1 ? re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1(re_frame.fx.kind) : re_frame.registrar.kinds.call(null,re_frame.fx.kind)))){
} else {
throw (new Error("Assert failed: (re-frame.registrar/kinds kind)"));
}
re_frame.fx.reg_fx = (function re_frame$fx$reg_fx(id,handler){
return re_frame.registrar.register_handler(re_frame.fx.kind,id,handler);
});
/**
 * Per-fx-execution-frame override map. Bound by `do-fx-after`
 * from the current event's `:re-frame/fx-overrides` metadata.
 */
re_frame.fx._STAR_current_overrides_STAR_ = null;
/**
 * Resolve an fx handler: prefer an override from
 * `*current-overrides*` (set by do-fx-after for the active
 * dispatch); fall back to the global registrar.
 */
re_frame.fx.effect_handler = (function re_frame$fx$effect_handler(effect_key){
var or__5142__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(re_frame.fx._STAR_current_overrides_STAR_,effect_key);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(re_frame.fx.kind,effect_key,false);
}
});
/**
 * Apply the `:db` effect first, then every other effect via
 * `effect-handler`. Extracted so `do-fx-after` can call it with OR
 * without a `*current-overrides*` binding pushed — the no-overrides
 * hot path skips the binding entirely.
 */
re_frame.fx.run_effects_BANG_ = (function re_frame$fx$run_effects_BANG_(effects,effects_without_db){
var temp__5825__auto___22534 = new cljs.core.Keyword(null,"db","db",993250759).cljs$core$IFn$_invoke$arity$1(effects);
if(cljs.core.truth_(temp__5825__auto___22534)){
var new_db_22535 = temp__5825__auto___22534;
var fexpr__22418_22536 = re_frame.fx.effect_handler(new cljs.core.Keyword(null,"db","db",993250759));
(fexpr__22418_22536.cljs$core$IFn$_invoke$arity$1 ? fexpr__22418_22536.cljs$core$IFn$_invoke$arity$1(new_db_22535) : fexpr__22418_22536.call(null,new_db_22535));
} else {
}

var seq__22419 = cljs.core.seq(effects_without_db);
var chunk__22420 = null;
var count__22421 = (0);
var i__22422 = (0);
while(true){
if((i__22422 < count__22421)){
var vec__22430 = chunk__22420.cljs$core$IIndexed$_nth$arity$2(null,i__22422);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22430,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22430,(1),null);
var temp__5823__auto___22537 = re_frame.fx.effect_handler(effect_key);
if(cljs.core.truth_(temp__5823__auto___22537)){
var effect_fn_22538 = temp__5823__auto___22537;
(effect_fn_22538.cljs$core$IFn$_invoke$arity$1 ? effect_fn_22538.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_22538.call(null,effect_value));
} else {
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: no handler registered for effect:",effect_key,". Ignoring.",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"event","event",301435442),effect_key))?(""+"You may be trying to return a coeffect map from an event-fx handler. "+"See https://day8.github.io/re-frame/FAQs/use-cofx-as-fx/"):null)], 0));
}


var G__22539 = seq__22419;
var G__22540 = chunk__22420;
var G__22541 = count__22421;
var G__22542 = (i__22422 + (1));
seq__22419 = G__22539;
chunk__22420 = G__22540;
count__22421 = G__22541;
i__22422 = G__22542;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__22419);
if(temp__5825__auto__){
var seq__22419__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__22419__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__22419__$1);
var G__22543 = cljs.core.chunk_rest(seq__22419__$1);
var G__22544 = c__5673__auto__;
var G__22545 = cljs.core.count(c__5673__auto__);
var G__22546 = (0);
seq__22419 = G__22543;
chunk__22420 = G__22544;
count__22421 = G__22545;
i__22422 = G__22546;
continue;
} else {
var vec__22439 = cljs.core.first(seq__22419__$1);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22439,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22439,(1),null);
var temp__5823__auto___22547 = re_frame.fx.effect_handler(effect_key);
if(cljs.core.truth_(temp__5823__auto___22547)){
var effect_fn_22548 = temp__5823__auto___22547;
(effect_fn_22548.cljs$core$IFn$_invoke$arity$1 ? effect_fn_22548.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_22548.call(null,effect_value));
} else {
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: no handler registered for effect:",effect_key,". Ignoring.",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"event","event",301435442),effect_key))?(""+"You may be trying to return a coeffect map from an event-fx handler. "+"See https://day8.github.io/re-frame/FAQs/use-cofx-as-fx/"):null)], 0));
}


var G__22549 = cljs.core.next(seq__22419__$1);
var G__22550 = null;
var G__22551 = (0);
var G__22552 = (0);
seq__22419 = G__22549;
chunk__22420 = G__22550;
count__22421 = G__22551;
i__22422 = G__22552;
continue;
}
} else {
return null;
}
}
break;
}
});
/**
 * An interceptor whose `:after` actions the contents of `:effects`. As a result,
 *   this interceptor is Domino 3.
 * 
 *   This interceptor is silently added (by reg-event-db etc) to the front of
 *   interceptor chains for all events.
 * 
 *   For each key in `:effects` (a map), it calls the registered `effects handler`
 *   (see `reg-fx` for registration of effect handlers).
 * 
 *   So, if `:effects` was:
 *    {:dispatch  [:hello 42]
 *     :db        {...}
 *     :undo      "set flag"}
 * 
 *   it will call the registered effect handlers for each of the map's keys:
 *   `:dispatch`, `:undo` and `:db`. When calling each handler, provides the map
 *   value for that key - so in the example above the effect handler for :dispatch
 *   will be given one arg `[:hello 42]`.
 * 
 *   You cannot rely on the ordering in which effects are executed, other than that
 *   `:db` is guaranteed to be executed first.
 */
re_frame.fx.do_fx = re_frame.interceptor.__GT_interceptor.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"do-fx","do-fx",1194163050),new cljs.core.Keyword(null,"after","after",594996914),(function re_frame$fx$do_fx_after(context){
if(re_frame.trace.is_trace_enabled_QMARK_()){
var _STAR_current_trace_STAR__orig_val__22456 = re_frame.trace._STAR_current_trace_STAR_;
var _STAR_current_trace_STAR__temp_val__22457 = re_frame.trace.start_trace(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"op-type","op-type",-1636141668),new cljs.core.Keyword("event","do-fx","event/do-fx",1357330452)], null));
(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__temp_val__22457);

try{try{var effects = new cljs.core.Keyword(null,"effects","effects",-282369292).cljs$core$IFn$_invoke$arity$1(context);
var effects_without_db = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(effects,new cljs.core.Keyword(null,"db","db",993250759));
var event = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(context,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"coeffects","coeffects",497912985),new cljs.core.Keyword(null,"event","event",301435442)], null));
var overrides = new cljs.core.Keyword("re-frame","fx-overrides","re-frame/fx-overrides",1984520294).cljs$core$IFn$_invoke$arity$1(cljs.core.meta(event));
if(cljs.core.truth_(overrides)){
var _STAR_current_overrides_STAR__orig_val__22473 = re_frame.fx._STAR_current_overrides_STAR_;
var _STAR_current_overrides_STAR__temp_val__22474 = overrides;
(re_frame.fx._STAR_current_overrides_STAR_ = _STAR_current_overrides_STAR__temp_val__22474);

try{return re_frame.fx.run_effects_BANG_(effects,effects_without_db);
}finally {(re_frame.fx._STAR_current_overrides_STAR_ = _STAR_current_overrides_STAR__orig_val__22473);
}} else {
return re_frame.fx.run_effects_BANG_(effects,effects_without_db);
}
}finally {if(re_frame.trace.is_trace_enabled_QMARK_()){
var end__21475__auto___22553 = re_frame.interop.now();
var duration__21476__auto___22554 = (end__21475__auto___22553 - new cljs.core.Keyword(null,"start","start",-355208981).cljs$core$IFn$_invoke$arity$1(re_frame.trace._STAR_current_trace_STAR_));
var finished__21477__auto___22555 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"duration","duration",1444101068),duration__21476__auto___22554,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"end","end",-268185958),end__21475__auto___22553], 0));
if(re_frame.trace.validate_trace_enabled_QMARK_){
re_frame.trace.check_trace_against_schema(finished__21477__auto___22555);
} else {
}

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(re_frame.trace.traces,cljs.core.conj,finished__21477__auto___22555);

re_frame.trace.run_tracing_callbacks_BANG_(end__21475__auto___22553);
} else {
}
}}finally {(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__orig_val__22456);
}} else {
var effects = new cljs.core.Keyword(null,"effects","effects",-282369292).cljs$core$IFn$_invoke$arity$1(context);
var effects_without_db = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(effects,new cljs.core.Keyword(null,"db","db",993250759));
var event = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(context,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"coeffects","coeffects",497912985),new cljs.core.Keyword(null,"event","event",301435442)], null));
var overrides = new cljs.core.Keyword("re-frame","fx-overrides","re-frame/fx-overrides",1984520294).cljs$core$IFn$_invoke$arity$1(cljs.core.meta(event));
if(cljs.core.truth_(overrides)){
var _STAR_current_overrides_STAR__orig_val__22476 = re_frame.fx._STAR_current_overrides_STAR_;
var _STAR_current_overrides_STAR__temp_val__22477 = overrides;
(re_frame.fx._STAR_current_overrides_STAR_ = _STAR_current_overrides_STAR__temp_val__22477);

try{return re_frame.fx.run_effects_BANG_(effects,effects_without_db);
}finally {(re_frame.fx._STAR_current_overrides_STAR_ = _STAR_current_overrides_STAR__orig_val__22476);
}} else {
return re_frame.fx.run_effects_BANG_(effects,effects_without_db);
}
}
})], 0));
re_frame.fx.dispatch_later = (function re_frame$fx$dispatch_later(p__22478){
var map__22479 = p__22478;
var map__22479__$1 = cljs.core.__destructure_map(map__22479);
var effect = map__22479__$1;
var ms = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22479__$1,new cljs.core.Keyword(null,"ms","ms",-1152709733));
var dispatch = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22479__$1,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009));
if(((cljs.core.empty_QMARK_(dispatch)) || ((!(typeof ms === 'number'))))){
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: ignoring bad :dispatch-later value:",effect], 0));
} else {
return re_frame.interop.set_timeout_BANG_((function (){
return re_frame.router.dispatch(dispatch);
}),ms);
}
});
re_frame.fx.reg_fx(new cljs.core.Keyword(null,"dispatch-later","dispatch-later",291951390),(function (value){
if(cljs.core.map_QMARK_(value)){
return re_frame.fx.dispatch_later(value);
} else {
var seq__22486 = cljs.core.seq(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,value));
var chunk__22487 = null;
var count__22488 = (0);
var i__22489 = (0);
while(true){
if((i__22489 < count__22488)){
var effect = chunk__22487.cljs$core$IIndexed$_nth$arity$2(null,i__22489);
re_frame.fx.dispatch_later(effect);


var G__22556 = seq__22486;
var G__22557 = chunk__22487;
var G__22558 = count__22488;
var G__22559 = (i__22489 + (1));
seq__22486 = G__22556;
chunk__22487 = G__22557;
count__22488 = G__22558;
i__22489 = G__22559;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__22486);
if(temp__5825__auto__){
var seq__22486__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__22486__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__22486__$1);
var G__22560 = cljs.core.chunk_rest(seq__22486__$1);
var G__22561 = c__5673__auto__;
var G__22562 = cljs.core.count(c__5673__auto__);
var G__22563 = (0);
seq__22486 = G__22560;
chunk__22487 = G__22561;
count__22488 = G__22562;
i__22489 = G__22563;
continue;
} else {
var effect = cljs.core.first(seq__22486__$1);
re_frame.fx.dispatch_later(effect);


var G__22564 = cljs.core.next(seq__22486__$1);
var G__22565 = null;
var G__22566 = (0);
var G__22567 = (0);
seq__22486 = G__22564;
chunk__22487 = G__22565;
count__22488 = G__22566;
i__22489 = G__22567;
continue;
}
} else {
return null;
}
}
break;
}
}
}));
re_frame.fx.reg_fx(new cljs.core.Keyword(null,"fx","fx",-1237829572),(function (seq_of_effects){
if((!(cljs.core.sequential_QMARK_(seq_of_effects)))){
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: \":fx\" effect expects a seq, but was given ",cljs.core.type(seq_of_effects)], 0));
} else {
var seq__22495 = cljs.core.seq(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,seq_of_effects));
var chunk__22496 = null;
var count__22497 = (0);
var i__22498 = (0);
while(true){
if((i__22498 < count__22497)){
var vec__22513 = chunk__22496.cljs$core$IIndexed$_nth$arity$2(null,i__22498);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22513,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22513,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"db","db",993250759),effect_key)){
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: \":fx\" effect should not contain a :db effect"], 0));
} else {
}

var temp__5823__auto___22568 = re_frame.fx.effect_handler(effect_key);
if(cljs.core.truth_(temp__5823__auto___22568)){
var effect_fn_22569 = temp__5823__auto___22568;
(effect_fn_22569.cljs$core$IFn$_invoke$arity$1 ? effect_fn_22569.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_22569.call(null,effect_value));
} else {
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: in \":fx\" effect found ",effect_key," which has no associated handler. Ignoring."], 0));
}


var G__22573 = seq__22495;
var G__22574 = chunk__22496;
var G__22575 = count__22497;
var G__22576 = (i__22498 + (1));
seq__22495 = G__22573;
chunk__22496 = G__22574;
count__22497 = G__22575;
i__22498 = G__22576;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__22495);
if(temp__5825__auto__){
var seq__22495__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__22495__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__22495__$1);
var G__22577 = cljs.core.chunk_rest(seq__22495__$1);
var G__22578 = c__5673__auto__;
var G__22579 = cljs.core.count(c__5673__auto__);
var G__22580 = (0);
seq__22495 = G__22577;
chunk__22496 = G__22578;
count__22497 = G__22579;
i__22498 = G__22580;
continue;
} else {
var vec__22520 = cljs.core.first(seq__22495__$1);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22520,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22520,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"db","db",993250759),effect_key)){
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: \":fx\" effect should not contain a :db effect"], 0));
} else {
}

var temp__5823__auto___22581 = re_frame.fx.effect_handler(effect_key);
if(cljs.core.truth_(temp__5823__auto___22581)){
var effect_fn_22582 = temp__5823__auto___22581;
(effect_fn_22582.cljs$core$IFn$_invoke$arity$1 ? effect_fn_22582.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_22582.call(null,effect_value));
} else {
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: in \":fx\" effect found ",effect_key," which has no associated handler. Ignoring."], 0));
}


var G__22583 = cljs.core.next(seq__22495__$1);
var G__22584 = null;
var G__22585 = (0);
var G__22586 = (0);
seq__22495 = G__22583;
chunk__22496 = G__22584;
count__22497 = G__22585;
i__22498 = G__22586;
continue;
}
} else {
return null;
}
}
break;
}
}
}));
re_frame.fx.reg_fx(new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),(function (value){
if((!(cljs.core.vector_QMARK_(value)))){
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: ignoring bad :dispatch value. Expected a vector, but got:",value], 0));
} else {
return re_frame.router.dispatch(value);
}
}));
re_frame.fx.reg_fx(new cljs.core.Keyword(null,"dispatch-n","dispatch-n",-504469236),(function (value){
if((!(cljs.core.sequential_QMARK_(value)))){
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: ignoring bad :dispatch-n value. Expected a collection, but got:",value], 0));
} else {
var seq__22524 = cljs.core.seq(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,value));
var chunk__22525 = null;
var count__22526 = (0);
var i__22527 = (0);
while(true){
if((i__22527 < count__22526)){
var event = chunk__22525.cljs$core$IIndexed$_nth$arity$2(null,i__22527);
re_frame.router.dispatch(event);


var G__22588 = seq__22524;
var G__22589 = chunk__22525;
var G__22590 = count__22526;
var G__22591 = (i__22527 + (1));
seq__22524 = G__22588;
chunk__22525 = G__22589;
count__22526 = G__22590;
i__22527 = G__22591;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__22524);
if(temp__5825__auto__){
var seq__22524__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__22524__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__22524__$1);
var G__22594 = cljs.core.chunk_rest(seq__22524__$1);
var G__22595 = c__5673__auto__;
var G__22596 = cljs.core.count(c__5673__auto__);
var G__22597 = (0);
seq__22524 = G__22594;
chunk__22525 = G__22595;
count__22526 = G__22596;
i__22527 = G__22597;
continue;
} else {
var event = cljs.core.first(seq__22524__$1);
re_frame.router.dispatch(event);


var G__22598 = cljs.core.next(seq__22524__$1);
var G__22599 = null;
var G__22600 = (0);
var G__22601 = (0);
seq__22524 = G__22598;
chunk__22525 = G__22599;
count__22526 = G__22600;
i__22527 = G__22601;
continue;
}
} else {
return null;
}
}
break;
}
}
}));
re_frame.fx.reg_fx(new cljs.core.Keyword(null,"deregister-event-handler","deregister-event-handler",-1096518994),(function (value){
var clear_event = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(re_frame.registrar.clear_handlers,re_frame.events.kind);
if(cljs.core.sequential_QMARK_(value)){
var seq__22528 = cljs.core.seq(value);
var chunk__22529 = null;
var count__22530 = (0);
var i__22531 = (0);
while(true){
if((i__22531 < count__22530)){
var event = chunk__22529.cljs$core$IIndexed$_nth$arity$2(null,i__22531);
clear_event(event);


var G__22602 = seq__22528;
var G__22603 = chunk__22529;
var G__22604 = count__22530;
var G__22605 = (i__22531 + (1));
seq__22528 = G__22602;
chunk__22529 = G__22603;
count__22530 = G__22604;
i__22531 = G__22605;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__22528);
if(temp__5825__auto__){
var seq__22528__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__22528__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__22528__$1);
var G__22606 = cljs.core.chunk_rest(seq__22528__$1);
var G__22607 = c__5673__auto__;
var G__22608 = cljs.core.count(c__5673__auto__);
var G__22609 = (0);
seq__22528 = G__22606;
chunk__22529 = G__22607;
count__22530 = G__22608;
i__22531 = G__22609;
continue;
} else {
var event = cljs.core.first(seq__22528__$1);
clear_event(event);


var G__22610 = cljs.core.next(seq__22528__$1);
var G__22611 = null;
var G__22612 = (0);
var G__22613 = (0);
seq__22528 = G__22610;
chunk__22529 = G__22611;
count__22530 = G__22612;
i__22531 = G__22613;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return clear_event(value);
}
}));
re_frame.fx.reg_fx(new cljs.core.Keyword(null,"db","db",993250759),(function (value){
if((!((cljs.core.deref(re_frame.db.app_db) === value)))){
return cljs.core.reset_BANG_(re_frame.db.app_db,value);
} else {
if(re_frame.trace.is_trace_enabled_QMARK_()){
var _STAR_current_trace_STAR__orig_val__22532 = re_frame.trace._STAR_current_trace_STAR_;
var _STAR_current_trace_STAR__temp_val__22533 = re_frame.trace.start_trace(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"op-type","op-type",-1636141668),new cljs.core.Keyword("reagent","quiescent","reagent/quiescent",-16138681)], null));
(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__temp_val__22533);

try{try{return null;
}finally {if(re_frame.trace.is_trace_enabled_QMARK_()){
var end__21475__auto___22614 = re_frame.interop.now();
var duration__21476__auto___22615 = (end__21475__auto___22614 - new cljs.core.Keyword(null,"start","start",-355208981).cljs$core$IFn$_invoke$arity$1(re_frame.trace._STAR_current_trace_STAR_));
var finished__21477__auto___22616 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"duration","duration",1444101068),duration__21476__auto___22615,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"end","end",-268185958),end__21475__auto___22614], 0));
if(re_frame.trace.validate_trace_enabled_QMARK_){
re_frame.trace.check_trace_against_schema(finished__21477__auto___22616);
} else {
}

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(re_frame.trace.traces,cljs.core.conj,finished__21477__auto___22616);

re_frame.trace.run_tracing_callbacks_BANG_(end__21475__auto___22614);
} else {
}
}}finally {(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__orig_val__22532);
}} else {
return null;
}
}
}));

//# sourceMappingURL=re_frame.fx.js.map
