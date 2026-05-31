goog.provide('day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx');
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind = new cljs.core.Keyword(null,"fx","fx",-1237829572);
if(cljs.core.truth_((day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1 ? day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind) : day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.kinds.call(null,day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind)))){
} else {
throw (new Error("Assert failed: (day8.re-frame-10x.inlined-deps.re-frame.v1v3v0.re-frame.registrar/kinds kind)"));
}
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.reg_fx = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$fx$reg_fx(id,handler){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.register_handler(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,id,handler);
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
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.do_fx = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.interceptor.__GT_interceptor.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"do-fx","do-fx",1194163050),new cljs.core.Keyword(null,"after","after",594996914),(function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$fx$do_fx_after(context){
if(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.is_trace_enabled_QMARK_()){
var _STAR_current_trace_STAR__orig_val__20162 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace._STAR_current_trace_STAR_;
var _STAR_current_trace_STAR__temp_val__20163 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.start_trace(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"op-type","op-type",-1636141668),new cljs.core.Keyword("event","do-fx","event/do-fx",1357330452)], null));
(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__temp_val__20163);

try{try{var effects = new cljs.core.Keyword(null,"effects","effects",-282369292).cljs$core$IFn$_invoke$arity$1(context);
var effects_without_db = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(effects,new cljs.core.Keyword(null,"db","db",993250759));
var temp__5825__auto___20383 = new cljs.core.Keyword(null,"db","db",993250759).cljs$core$IFn$_invoke$arity$1(effects);
if(cljs.core.truth_(temp__5825__auto___20383)){
var new_db_20384 = temp__5825__auto___20383;
var fexpr__20174_20385 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,new cljs.core.Keyword(null,"db","db",993250759),false);
(fexpr__20174_20385.cljs$core$IFn$_invoke$arity$1 ? fexpr__20174_20385.cljs$core$IFn$_invoke$arity$1(new_db_20384) : fexpr__20174_20385.call(null,new_db_20384));
} else {
}

var seq__20175 = cljs.core.seq(effects_without_db);
var chunk__20176 = null;
var count__20177 = (0);
var i__20178 = (0);
while(true){
if((i__20178 < count__20177)){
var vec__20216 = chunk__20176.cljs$core$IIndexed$_nth$arity$2(null,i__20178);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20216,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20216,(1),null);
var temp__5823__auto___20388 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,effect_key,false);
if(cljs.core.truth_(temp__5823__auto___20388)){
var effect_fn_20391 = temp__5823__auto___20388;
(effect_fn_20391.cljs$core$IFn$_invoke$arity$1 ? effect_fn_20391.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_20391.call(null,effect_value));
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: no handler registered for effect:",effect_key,". Ignoring."], 0));
}


var G__20392 = seq__20175;
var G__20393 = chunk__20176;
var G__20394 = count__20177;
var G__20395 = (i__20178 + (1));
seq__20175 = G__20392;
chunk__20176 = G__20393;
count__20177 = G__20394;
i__20178 = G__20395;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__20175);
if(temp__5825__auto__){
var seq__20175__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__20175__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__20175__$1);
var G__20396 = cljs.core.chunk_rest(seq__20175__$1);
var G__20397 = c__5673__auto__;
var G__20398 = cljs.core.count(c__5673__auto__);
var G__20399 = (0);
seq__20175 = G__20396;
chunk__20176 = G__20397;
count__20177 = G__20398;
i__20178 = G__20399;
continue;
} else {
var vec__20224 = cljs.core.first(seq__20175__$1);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20224,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20224,(1),null);
var temp__5823__auto___20400 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,effect_key,false);
if(cljs.core.truth_(temp__5823__auto___20400)){
var effect_fn_20401 = temp__5823__auto___20400;
(effect_fn_20401.cljs$core$IFn$_invoke$arity$1 ? effect_fn_20401.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_20401.call(null,effect_value));
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: no handler registered for effect:",effect_key,". Ignoring."], 0));
}


var G__20402 = cljs.core.next(seq__20175__$1);
var G__20403 = null;
var G__20404 = (0);
var G__20405 = (0);
seq__20175 = G__20402;
chunk__20176 = G__20403;
count__20177 = G__20404;
i__20178 = G__20405;
continue;
}
} else {
return null;
}
}
break;
}
}finally {if(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.is_trace_enabled_QMARK_()){
var end__19328__auto___20406 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.interop.now();
var duration__19329__auto___20407 = (end__19328__auto___20406 - new cljs.core.Keyword(null,"start","start",-355208981).cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace._STAR_current_trace_STAR_));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces,cljs.core.conj,cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"duration","duration",1444101068),duration__19329__auto___20407,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"end","end",-268185958),day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.interop.now()], 0)));

day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.run_tracing_callbacks_BANG_(end__19328__auto___20406);
} else {
}
}}finally {(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__orig_val__20162);
}} else {
var effects = new cljs.core.Keyword(null,"effects","effects",-282369292).cljs$core$IFn$_invoke$arity$1(context);
var effects_without_db = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(effects,new cljs.core.Keyword(null,"db","db",993250759));
var temp__5825__auto___20408 = new cljs.core.Keyword(null,"db","db",993250759).cljs$core$IFn$_invoke$arity$1(effects);
if(cljs.core.truth_(temp__5825__auto___20408)){
var new_db_20409 = temp__5825__auto___20408;
var fexpr__20231_20410 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,new cljs.core.Keyword(null,"db","db",993250759),false);
(fexpr__20231_20410.cljs$core$IFn$_invoke$arity$1 ? fexpr__20231_20410.cljs$core$IFn$_invoke$arity$1(new_db_20409) : fexpr__20231_20410.call(null,new_db_20409));
} else {
}

var seq__20232 = cljs.core.seq(effects_without_db);
var chunk__20233 = null;
var count__20234 = (0);
var i__20235 = (0);
while(true){
if((i__20235 < count__20234)){
var vec__20250 = chunk__20233.cljs$core$IIndexed$_nth$arity$2(null,i__20235);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20250,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20250,(1),null);
var temp__5823__auto___20411 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,effect_key,false);
if(cljs.core.truth_(temp__5823__auto___20411)){
var effect_fn_20412 = temp__5823__auto___20411;
(effect_fn_20412.cljs$core$IFn$_invoke$arity$1 ? effect_fn_20412.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_20412.call(null,effect_value));
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: no handler registered for effect:",effect_key,". Ignoring."], 0));
}


var G__20415 = seq__20232;
var G__20416 = chunk__20233;
var G__20417 = count__20234;
var G__20418 = (i__20235 + (1));
seq__20232 = G__20415;
chunk__20233 = G__20416;
count__20234 = G__20417;
i__20235 = G__20418;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__20232);
if(temp__5825__auto__){
var seq__20232__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__20232__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__20232__$1);
var G__20419 = cljs.core.chunk_rest(seq__20232__$1);
var G__20420 = c__5673__auto__;
var G__20421 = cljs.core.count(c__5673__auto__);
var G__20422 = (0);
seq__20232 = G__20419;
chunk__20233 = G__20420;
count__20234 = G__20421;
i__20235 = G__20422;
continue;
} else {
var vec__20263 = cljs.core.first(seq__20232__$1);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20263,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20263,(1),null);
var temp__5823__auto___20423 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,effect_key,false);
if(cljs.core.truth_(temp__5823__auto___20423)){
var effect_fn_20424 = temp__5823__auto___20423;
(effect_fn_20424.cljs$core$IFn$_invoke$arity$1 ? effect_fn_20424.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_20424.call(null,effect_value));
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: no handler registered for effect:",effect_key,". Ignoring."], 0));
}


var G__20425 = cljs.core.next(seq__20232__$1);
var G__20426 = null;
var G__20427 = (0);
var G__20428 = (0);
seq__20232 = G__20425;
chunk__20233 = G__20426;
count__20234 = G__20427;
i__20235 = G__20428;
continue;
}
} else {
return null;
}
}
break;
}
}
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.dispatch_later = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$fx$dispatch_later(p__20273){
var map__20274 = p__20273;
var map__20274__$1 = cljs.core.__destructure_map(map__20274);
var effect = map__20274__$1;
var ms = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20274__$1,new cljs.core.Keyword(null,"ms","ms",-1152709733));
var dispatch = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20274__$1,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009));
if(((cljs.core.empty_QMARK_(dispatch)) || ((!(typeof ms === 'number'))))){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: ignoring bad :dispatch-later value:",effect], 0));
} else {
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.interop.set_timeout_BANG_((function (){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.router.dispatch(dispatch);
}),ms);
}
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.reg_fx(new cljs.core.Keyword(null,"dispatch-later","dispatch-later",291951390),(function (value){
if(cljs.core.map_QMARK_(value)){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.dispatch_later(value);
} else {
var seq__20281 = cljs.core.seq(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,value));
var chunk__20282 = null;
var count__20283 = (0);
var i__20284 = (0);
while(true){
if((i__20284 < count__20283)){
var effect = chunk__20282.cljs$core$IIndexed$_nth$arity$2(null,i__20284);
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.dispatch_later(effect);


var G__20434 = seq__20281;
var G__20435 = chunk__20282;
var G__20436 = count__20283;
var G__20437 = (i__20284 + (1));
seq__20281 = G__20434;
chunk__20282 = G__20435;
count__20283 = G__20436;
i__20284 = G__20437;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__20281);
if(temp__5825__auto__){
var seq__20281__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__20281__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__20281__$1);
var G__20438 = cljs.core.chunk_rest(seq__20281__$1);
var G__20439 = c__5673__auto__;
var G__20440 = cljs.core.count(c__5673__auto__);
var G__20441 = (0);
seq__20281 = G__20438;
chunk__20282 = G__20439;
count__20283 = G__20440;
i__20284 = G__20441;
continue;
} else {
var effect = cljs.core.first(seq__20281__$1);
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.dispatch_later(effect);


var G__20442 = cljs.core.next(seq__20281__$1);
var G__20443 = null;
var G__20444 = (0);
var G__20445 = (0);
seq__20281 = G__20442;
chunk__20282 = G__20443;
count__20283 = G__20444;
i__20284 = G__20445;
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
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.reg_fx(new cljs.core.Keyword(null,"fx","fx",-1237829572),(function (seq_of_effects){
if((!(cljs.core.sequential_QMARK_(seq_of_effects)))){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: \":fx\" effect expects a seq, but was given ",cljs.core.type(seq_of_effects)], 0));
} else {
var seq__20299 = cljs.core.seq(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,seq_of_effects));
var chunk__20300 = null;
var count__20301 = (0);
var i__20302 = (0);
while(true){
if((i__20302 < count__20301)){
var vec__20321 = chunk__20300.cljs$core$IIndexed$_nth$arity$2(null,i__20302);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20321,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20321,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"db","db",993250759),effect_key)){
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: \":fx\" effect should not contain a :db effect"], 0));
} else {
}

var temp__5823__auto___20453 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,effect_key,false);
if(cljs.core.truth_(temp__5823__auto___20453)){
var effect_fn_20454 = temp__5823__auto___20453;
(effect_fn_20454.cljs$core$IFn$_invoke$arity$1 ? effect_fn_20454.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_20454.call(null,effect_value));
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: in \":fx\" effect found ",effect_key," which has no associated handler. Ignoring."], 0));
}


var G__20455 = seq__20299;
var G__20456 = chunk__20300;
var G__20457 = count__20301;
var G__20458 = (i__20302 + (1));
seq__20299 = G__20455;
chunk__20300 = G__20456;
count__20301 = G__20457;
i__20302 = G__20458;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__20299);
if(temp__5825__auto__){
var seq__20299__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__20299__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__20299__$1);
var G__20459 = cljs.core.chunk_rest(seq__20299__$1);
var G__20460 = c__5673__auto__;
var G__20461 = cljs.core.count(c__5673__auto__);
var G__20462 = (0);
seq__20299 = G__20459;
chunk__20300 = G__20460;
count__20301 = G__20461;
i__20302 = G__20462;
continue;
} else {
var vec__20335 = cljs.core.first(seq__20299__$1);
var effect_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20335,(0),null);
var effect_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20335,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"db","db",993250759),effect_key)){
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: \":fx\" effect should not contain a :db effect"], 0));
} else {
}

var temp__5823__auto___20463 = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.kind,effect_key,false);
if(cljs.core.truth_(temp__5823__auto___20463)){
var effect_fn_20464 = temp__5823__auto___20463;
(effect_fn_20464.cljs$core$IFn$_invoke$arity$1 ? effect_fn_20464.cljs$core$IFn$_invoke$arity$1(effect_value) : effect_fn_20464.call(null,effect_value));
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: in \":fx\" effect found ",effect_key," which has no associated handler. Ignoring."], 0));
}


var G__20465 = cljs.core.next(seq__20299__$1);
var G__20466 = null;
var G__20467 = (0);
var G__20468 = (0);
seq__20299 = G__20465;
chunk__20300 = G__20466;
count__20301 = G__20467;
i__20302 = G__20468;
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
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.reg_fx(new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),(function (value){
if((!(cljs.core.vector_QMARK_(value)))){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: ignoring bad :dispatch value. Expected a vector, but got:",value], 0));
} else {
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.router.dispatch(value);
}
}));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.reg_fx(new cljs.core.Keyword(null,"dispatch-n","dispatch-n",-504469236),(function (value){
if((!(cljs.core.sequential_QMARK_(value)))){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: ignoring bad :dispatch-n value. Expected a collection, but got:",value], 0));
} else {
var seq__20349 = cljs.core.seq(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,value));
var chunk__20350 = null;
var count__20351 = (0);
var i__20352 = (0);
while(true){
if((i__20352 < count__20351)){
var event = chunk__20350.cljs$core$IIndexed$_nth$arity$2(null,i__20352);
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.router.dispatch(event);


var G__20471 = seq__20349;
var G__20472 = chunk__20350;
var G__20473 = count__20351;
var G__20474 = (i__20352 + (1));
seq__20349 = G__20471;
chunk__20350 = G__20472;
count__20351 = G__20473;
i__20352 = G__20474;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__20349);
if(temp__5825__auto__){
var seq__20349__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__20349__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__20349__$1);
var G__20475 = cljs.core.chunk_rest(seq__20349__$1);
var G__20476 = c__5673__auto__;
var G__20477 = cljs.core.count(c__5673__auto__);
var G__20478 = (0);
seq__20349 = G__20475;
chunk__20350 = G__20476;
count__20351 = G__20477;
i__20352 = G__20478;
continue;
} else {
var event = cljs.core.first(seq__20349__$1);
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.router.dispatch(event);


var G__20479 = cljs.core.next(seq__20349__$1);
var G__20480 = null;
var G__20481 = (0);
var G__20482 = (0);
seq__20349 = G__20479;
chunk__20350 = G__20480;
count__20351 = G__20481;
i__20352 = G__20482;
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
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.reg_fx(new cljs.core.Keyword(null,"deregister-event-handler","deregister-event-handler",-1096518994),(function (value){
var clear_event = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.registrar.clear_handlers,day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.events.kind);
if(cljs.core.sequential_QMARK_(value)){
var seq__20363 = cljs.core.seq(value);
var chunk__20364 = null;
var count__20365 = (0);
var i__20366 = (0);
while(true){
if((i__20366 < count__20365)){
var event = chunk__20364.cljs$core$IIndexed$_nth$arity$2(null,i__20366);
clear_event(event);


var G__20483 = seq__20363;
var G__20484 = chunk__20364;
var G__20485 = count__20365;
var G__20486 = (i__20366 + (1));
seq__20363 = G__20483;
chunk__20364 = G__20484;
count__20365 = G__20485;
i__20366 = G__20486;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__20363);
if(temp__5825__auto__){
var seq__20363__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__20363__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__20363__$1);
var G__20487 = cljs.core.chunk_rest(seq__20363__$1);
var G__20488 = c__5673__auto__;
var G__20489 = cljs.core.count(c__5673__auto__);
var G__20490 = (0);
seq__20363 = G__20487;
chunk__20364 = G__20488;
count__20365 = G__20489;
i__20366 = G__20490;
continue;
} else {
var event = cljs.core.first(seq__20363__$1);
clear_event(event);


var G__20491 = cljs.core.next(seq__20363__$1);
var G__20492 = null;
var G__20493 = (0);
var G__20494 = (0);
seq__20363 = G__20491;
chunk__20364 = G__20492;
count__20365 = G__20493;
i__20366 = G__20494;
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
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.reg_fx(new cljs.core.Keyword(null,"db","db",993250759),(function (value){
if((!((cljs.core.deref(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db) === value)))){
return cljs.core.reset_BANG_(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.db.app_db,value);
} else {
return null;
}
}));

//# sourceMappingURL=day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.fx.js.map
