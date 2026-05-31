goog.provide('re_frame.registrar');
re_frame.registrar.kinds = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"sub","sub",-2093760025),null,new cljs.core.Keyword(null,"event","event",301435442),null,new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"cofx","cofx",2013202907),null,new cljs.core.Keyword(null,"fx","fx",-1237829572),null], null), null);
if((typeof re_frame !== 'undefined') && (typeof re_frame.registrar !== 'undefined') && (typeof re_frame.registrar.kind__GT_id__GT_handler !== 'undefined')){
} else {
re_frame.registrar.kind__GT_id__GT_handler = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
re_frame.registrar.get_handler = (function re_frame$registrar$get_handler(var_args){
var G__21512 = arguments.length;
switch (G__21512) {
case 1:
return re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$1 = (function (kind){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(re_frame.registrar.kind__GT_id__GT_handler),kind);
}));

(re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$2 = (function (kind,id){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(re_frame.registrar.kind__GT_id__GT_handler),kind),id);
}));

(re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3 = (function (kind,id,required_QMARK_){
var handler = re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$2(kind,id);
if(re_frame.interop.debug_enabled_QMARK_){
if(cljs.core.truth_((function (){var and__5140__auto__ = required_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return (handler == null);
} else {
return and__5140__auto__;
}
})())){
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: no",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(kind)),"handler registered for:",id], 0));
} else {
}
} else {
}

return handler;
}));

(re_frame.registrar.get_handler.cljs$lang$maxFixedArity = 3);

re_frame.registrar.register_handler = (function re_frame$registrar$register_handler(kind,id,handler_fn){
if(cljs.core.truth_((function (){var and__5140__auto__ = re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$3(kind,id,false);
if(cljs.core.truth_(and__5140__auto__)){
return (((!(re_frame.interop.debug_enabled_QMARK_))) || (cljs.core.not(re_frame.settings.loaded_QMARK_())));
} else {
return and__5140__auto__;
}
})())){
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: overwriting",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(kind)),"handler for:",id], 0));
} else {
}

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(re_frame.registrar.kind__GT_id__GT_handler,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [kind,id], null),handler_fn);

return handler_fn;
});
/**
 * Re-register the handler at [kind id] with `src-meta` attached as
 * metadata. Used by the reg-event-db / reg-event-fx / reg-event-ctx /
 * reg-sub / reg-fx macros to bake the call-site {:file :line} onto
 * the registered value so `(meta (get-handler kind id))` returns it.
 * 
 * No-op when the handler isn't registered. If the registered value
 * doesn't implement IObj (some opaque IFn shapes), the with-meta call
 * throws and we silently skip — the macro contract is best-effort.
 * 
 * The leading dash signals 'internal, but reachable' — users opt in
 * via the macros, not by calling this directly.
 */
re_frame.registrar._decorate_handler_meta_BANG_ = (function re_frame$registrar$_decorate_handler_meta_BANG_(kind,id,src_meta){
var temp__5825__auto___21540 = re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$2(kind,id);
if(cljs.core.truth_(temp__5825__auto___21540)){
var handler_21541 = temp__5825__auto___21540;
try{var chain_or_fn_21542 = ((((cljs.core.seq_QMARK_(handler_21541)) && ((!(cljs.core.vector_QMARK_(handler_21541))))))?cljs.core.vec(handler_21541):handler_21541);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(re_frame.registrar.kind__GT_id__GT_handler,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [kind,id], null),cljs.core.with_meta(chain_or_fn_21542,src_meta));
}catch (e21516){var __21547 = e21516;
}} else {
}

return null;
});
re_frame.registrar.clear_handlers = (function re_frame$registrar$clear_handlers(var_args){
var G__21522 = arguments.length;
switch (G__21522) {
case 0:
return re_frame.registrar.clear_handlers.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return re_frame.registrar.clear_handlers.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return re_frame.registrar.clear_handlers.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(re_frame.registrar.clear_handlers.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.reset_BANG_(re_frame.registrar.kind__GT_id__GT_handler,cljs.core.PersistentArrayMap.EMPTY);
}));

(re_frame.registrar.clear_handlers.cljs$core$IFn$_invoke$arity$1 = (function (kind){
if(cljs.core.truth_((re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1 ? re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1(kind) : re_frame.registrar.kinds.call(null,kind)))){
} else {
throw (new Error("Assert failed: (kinds kind)"));
}

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(re_frame.registrar.kind__GT_id__GT_handler,cljs.core.dissoc,kind);
}));

(re_frame.registrar.clear_handlers.cljs$core$IFn$_invoke$arity$2 = (function (kind,id){
if(cljs.core.truth_((re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1 ? re_frame.registrar.kinds.cljs$core$IFn$_invoke$arity$1(kind) : re_frame.registrar.kinds.call(null,kind)))){
} else {
throw (new Error("Assert failed: (kinds kind)"));
}

if(cljs.core.truth_(re_frame.registrar.get_handler.cljs$core$IFn$_invoke$arity$2(kind,id))){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(re_frame.registrar.kind__GT_id__GT_handler,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [kind], null),cljs.core.dissoc,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([id], 0));
} else {
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: can't clear",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(kind)),"handler for",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(id)+". Handler not found.")], 0));
}
}));

(re_frame.registrar.clear_handlers.cljs$lang$maxFixedArity = 2);


//# sourceMappingURL=re_frame.registrar.js.map
