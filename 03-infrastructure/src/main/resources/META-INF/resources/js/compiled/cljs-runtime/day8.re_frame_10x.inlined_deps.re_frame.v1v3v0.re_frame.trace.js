goog.provide('day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace');
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.id = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace._STAR_current_trace_STAR_ = null;
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.reset_tracing_BANG_ = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$reset_tracing_BANG_(){
return cljs.core.reset_BANG_(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.id,(0));
});
/**
 * @define {boolean}
 * @type {boolean}
 */
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_enabled_QMARK_ = goog.define("day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_enabled_QMARK_",false);
/**
 * See https://groups.google.com/d/msg/clojurescript/jk43kmYiMhA/IHglVr_TPdgJ for more details
 */
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.is_trace_enabled_QMARK_ = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$is_trace_enabled_QMARK_(){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_enabled_QMARK_;
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_cbs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
if((typeof day8 !== 'undefined') && (typeof day8.re_frame_10x !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0 !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces !== 'undefined')){
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
}
if((typeof day8 !== 'undefined') && (typeof day8.re_frame_10x !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0 !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.next_delivery !== 'undefined')){
} else {
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.next_delivery = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
}
/**
 * Registers a tracing callback function which will receive a collection of one or more traces.
 *   Will replace an existing callback function if it shares the same key.
 */
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.register_trace_cb = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$register_trace_cb(key,f){
if(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_enabled_QMARK_){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_cbs,cljs.core.assoc,key,f);
} else {
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Tracing is not enabled. Please set {\"re_frame.trace.trace_enabled_QMARK_\" true} in :closure-defines. See: https://github.com/day8/re-frame-10x#installation."], 0));
}
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.remove_trace_cb = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$remove_trace_cb(key){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_cbs,cljs.core.dissoc,key);

return null;
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.next_id = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$next_id(){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.id,cljs.core.inc);
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.start_trace = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$start_trace(p__19379){
var map__19380 = p__19379;
var map__19380__$1 = cljs.core.__destructure_map(map__19380);
var operation = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19380__$1,new cljs.core.Keyword(null,"operation","operation",-1267664310));
var op_type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19380__$1,new cljs.core.Keyword(null,"op-type","op-type",-1636141668));
var tags = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19380__$1,new cljs.core.Keyword(null,"tags","tags",1771418977));
var child_of = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19380__$1,new cljs.core.Keyword(null,"child-of","child-of",-903376662));
return new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"id","id",-1388402092),day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.next_id(),new cljs.core.Keyword(null,"operation","operation",-1267664310),operation,new cljs.core.Keyword(null,"op-type","op-type",-1636141668),op_type,new cljs.core.Keyword(null,"tags","tags",1771418977),tags,new cljs.core.Keyword(null,"child-of","child-of",-903376662),(function (){var or__5142__auto__ = child_of;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace._STAR_current_trace_STAR_);
}
})(),new cljs.core.Keyword(null,"start","start",-355208981),day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.interop.now()], null);
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.debounce_time = (50);
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.debounce = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$debounce(f,interval){
return goog.functions.debounce(f,interval);
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.schedule_debounce = day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.debounce((function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$tracing_cb_debounced(){
var seq__19391_19449 = cljs.core.seq(cljs.core.deref(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.trace_cbs));
var chunk__19392_19450 = null;
var count__19393_19451 = (0);
var i__19394_19452 = (0);
while(true){
if((i__19394_19452 < count__19393_19451)){
var vec__19421_19453 = chunk__19392_19450.cljs$core$IIndexed$_nth$arity$2(null,i__19394_19452);
var k_19454 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19421_19453,(0),null);
var cb_19455 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19421_19453,(1),null);
try{var G__19426_19456 = cljs.core.deref(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces);
(cb_19455.cljs$core$IFn$_invoke$arity$1 ? cb_19455.cljs$core$IFn$_invoke$arity$1(G__19426_19456) : cb_19455.call(null,G__19426_19456));
}catch (e19424){var e_19457 = e19424;
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Error thrown from trace cb",k_19454,"while storing",cljs.core.deref(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces),e_19457], 0));
}

var G__19458 = seq__19391_19449;
var G__19459 = chunk__19392_19450;
var G__19460 = count__19393_19451;
var G__19461 = (i__19394_19452 + (1));
seq__19391_19449 = G__19458;
chunk__19392_19450 = G__19459;
count__19393_19451 = G__19460;
i__19394_19452 = G__19461;
continue;
} else {
var temp__5825__auto___19462 = cljs.core.seq(seq__19391_19449);
if(temp__5825__auto___19462){
var seq__19391_19463__$1 = temp__5825__auto___19462;
if(cljs.core.chunked_seq_QMARK_(seq__19391_19463__$1)){
var c__5673__auto___19464 = cljs.core.chunk_first(seq__19391_19463__$1);
var G__19465 = cljs.core.chunk_rest(seq__19391_19463__$1);
var G__19466 = c__5673__auto___19464;
var G__19467 = cljs.core.count(c__5673__auto___19464);
var G__19468 = (0);
seq__19391_19449 = G__19465;
chunk__19392_19450 = G__19466;
count__19393_19451 = G__19467;
i__19394_19452 = G__19468;
continue;
} else {
var vec__19427_19469 = cljs.core.first(seq__19391_19463__$1);
var k_19470 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19427_19469,(0),null);
var cb_19471 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19427_19469,(1),null);
try{var G__19431_19472 = cljs.core.deref(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces);
(cb_19471.cljs$core$IFn$_invoke$arity$1 ? cb_19471.cljs$core$IFn$_invoke$arity$1(G__19431_19472) : cb_19471.call(null,G__19431_19472));
}catch (e19430){var e_19473 = e19430;
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Error thrown from trace cb",k_19470,"while storing",cljs.core.deref(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces),e_19473], 0));
}

var G__19474 = cljs.core.next(seq__19391_19463__$1);
var G__19475 = null;
var G__19476 = (0);
var G__19477 = (0);
seq__19391_19449 = G__19474;
chunk__19392_19450 = G__19475;
count__19393_19451 = G__19476;
i__19394_19452 = G__19477;
continue;
}
} else {
}
}
break;
}

return cljs.core.reset_BANG_(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.traces,cljs.core.PersistentVector.EMPTY);
}),day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.debounce_time);
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.run_tracing_callbacks_BANG_ = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$trace$run_tracing_callbacks_BANG_(now){
if(((cljs.core.deref(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.next_delivery) - (25)) < now)){
(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.schedule_debounce.cljs$core$IFn$_invoke$arity$0 ? day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.schedule_debounce.cljs$core$IFn$_invoke$arity$0() : day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.schedule_debounce.call(null));

return cljs.core.reset_BANG_(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.next_delivery,(now + day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.debounce_time));
} else {
return null;
}
});

//# sourceMappingURL=day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.trace.js.map
