goog.provide('re_frame.interop');
re_frame.interop.on_load = (function re_frame$interop$on_load(listener){
try{return goog.events.listen(self,"load",listener);
}catch (e21237){var _ = e21237;
return null;
}});
re_frame.interop.next_tick = goog.async.nextTick;
re_frame.interop.empty_queue = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentQueue.EMPTY,cljs.core.PersistentVector.EMPTY);
re_frame.interop.after_render = reagent.core.after_render;
/**
 * @define {boolean}
 */
re_frame.interop.debug_enabled_QMARK_ = goog.DEBUG;
re_frame.interop.new_uuid = (function re_frame$interop$new_uuid(){
return cljs.core.random_uuid();
});
re_frame.interop.ratom = (function re_frame$interop$ratom(x){
return reagent.core.atom.cljs$core$IFn$_invoke$arity$1(x);
});
re_frame.interop.ratom_QMARK_ = (function re_frame$interop$ratom_QMARK_(x){
if((!((x == null)))){
if(((false) || ((cljs.core.PROTOCOL_SENTINEL === x.reagent$ratom$IReactiveAtom$)))){
return true;
} else {
if((!x.cljs$lang$protocol_mask$partition$)){
return cljs.core.native_satisfies_QMARK_(reagent.ratom.IReactiveAtom,x);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(reagent.ratom.IReactiveAtom,x);
}
});
re_frame.interop.deref_QMARK_ = (function re_frame$interop$deref_QMARK_(x){
if((!((x == null)))){
if((((x.cljs$lang$protocol_mask$partition0$ & (32768))) || ((cljs.core.PROTOCOL_SENTINEL === x.cljs$core$IDeref$)))){
return true;
} else {
if((!x.cljs$lang$protocol_mask$partition0$)){
return cljs.core.native_satisfies_QMARK_(cljs.core.IDeref,x);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(cljs.core.IDeref,x);
}
});
re_frame.interop.make_reaction = (function re_frame$interop$make_reaction(f){
return reagent.ratom.make_reaction(f);
});
re_frame.interop.add_on_dispose_BANG_ = (function re_frame$interop$add_on_dispose_BANG_(a_ratom,f){
return reagent.ratom.add_on_dispose_BANG_(a_ratom,f);
});
re_frame.interop.dispose_BANG_ = (function re_frame$interop$dispose_BANG_(a_ratom){
return reagent.ratom.dispose_BANG_(a_ratom);
});
/**
 * Schedule `f` to run after `ms` milliseconds. Returns a handle that
 *   can be passed to `clear-timeout!` to cancel before it fires.
 */
re_frame.interop.set_timeout_BANG_ = (function re_frame$interop$set_timeout_BANG_(f,ms){
return setTimeout(f,ms);
});
/**
 * Cancel a pending timeout previously scheduled with `set-timeout!`.
 *   No-op if `handle` is nil or the timeout has already fired.
 */
re_frame.interop.clear_timeout_BANG_ = (function re_frame$interop$clear_timeout_BANG_(handle){
if((!((handle == null)))){
clearTimeout(handle);
} else {
}

return null;
});
re_frame.interop.now = (function re_frame$interop$now(){
if((((typeof performance !== 'undefined')) && ((typeof performance !== 'undefined') && (typeof performance.now !== 'undefined')))){
return performance.now();
} else {
return Date.now();
}
});
/**
 * Produces an id for reactive Reagent values
 *   e.g. reactions, ratoms, cursors.
 */
re_frame.interop.reagent_id = (function re_frame$interop$reagent_id(reactive_val){
if((((!((reactive_val == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === reactive_val.reagent$ratom$IReactiveAtom$))))?true:false):false)){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var pred__21278 = cljs.core.instance_QMARK_;
var expr__21279 = reactive_val;
if(cljs.core.truth_((pred__21278.cljs$core$IFn$_invoke$arity$2 ? pred__21278.cljs$core$IFn$_invoke$arity$2(reagent.ratom.RAtom,expr__21279) : pred__21278.call(null,reagent.ratom.RAtom,expr__21279)))){
return "ra";
} else {
if(cljs.core.truth_((pred__21278.cljs$core$IFn$_invoke$arity$2 ? pred__21278.cljs$core$IFn$_invoke$arity$2(reagent.ratom.RCursor,expr__21279) : pred__21278.call(null,reagent.ratom.RCursor,expr__21279)))){
return "rc";
} else {
if(cljs.core.truth_((pred__21278.cljs$core$IFn$_invoke$arity$2 ? pred__21278.cljs$core$IFn$_invoke$arity$2(reagent.ratom.Reaction,expr__21279) : pred__21278.call(null,reagent.ratom.Reaction,expr__21279)))){
return "rx";
} else {
if(cljs.core.truth_((pred__21278.cljs$core$IFn$_invoke$arity$2 ? pred__21278.cljs$core$IFn$_invoke$arity$2(reagent.ratom.Track,expr__21279) : pred__21278.call(null,reagent.ratom.Track,expr__21279)))){
return "tr";
} else {
return "other";
}
}
}
}
})())+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.hash(reactive_val)));
} else {
return null;
}
});
re_frame.interop.reactive_QMARK_ = (function re_frame$interop$reactive_QMARK_(){
return reagent.ratom.reactive_QMARK_();
});

//# sourceMappingURL=re_frame.interop.js.map
