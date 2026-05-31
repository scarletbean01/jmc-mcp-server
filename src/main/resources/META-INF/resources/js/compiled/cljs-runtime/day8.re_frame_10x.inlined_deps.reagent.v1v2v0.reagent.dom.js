goog.provide('day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom');
var module$node_modules$react_dom$index=shadow.js.require("module$node_modules$react_dom$index", {});
if((typeof day8 !== 'undefined') && (typeof day8.re_frame_10x !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.reagent !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.reagent.v1v2v0 !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom !== 'undefined') && (typeof day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.roots !== 'undefined')){
} else {
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.roots = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.unmount_comp = (function day8$re_frame_10x$inlined_deps$reagent$v1v2v0$reagent$dom$unmount_comp(container){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.roots,cljs.core.dissoc,container);

return module$node_modules$react_dom$index.unmountComponentAtNode(container);
});
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render_comp = (function day8$re_frame_10x$inlined_deps$reagent$v1v2v0$reagent$dom$render_comp(comp,container,callback){
var _STAR_always_update_STAR__orig_val__23632 = day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.util._STAR_always_update_STAR_;
var _STAR_always_update_STAR__temp_val__23633 = true;
(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.util._STAR_always_update_STAR_ = _STAR_always_update_STAR__temp_val__23633);

try{return module$node_modules$react_dom$index.render((comp.cljs$core$IFn$_invoke$arity$0 ? comp.cljs$core$IFn$_invoke$arity$0() : comp.call(null)),container,(function (){
var _STAR_always_update_STAR__orig_val__23637 = day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.util._STAR_always_update_STAR_;
var _STAR_always_update_STAR__temp_val__23638 = false;
(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.util._STAR_always_update_STAR_ = _STAR_always_update_STAR__temp_val__23638);

try{cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.roots,cljs.core.assoc,container,comp);

day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.batching.flush_after_render();

if((!((callback == null)))){
return (callback.cljs$core$IFn$_invoke$arity$0 ? callback.cljs$core$IFn$_invoke$arity$0() : callback.call(null));
} else {
return null;
}
}finally {(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.util._STAR_always_update_STAR_ = _STAR_always_update_STAR__orig_val__23637);
}}));
}finally {(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.util._STAR_always_update_STAR_ = _STAR_always_update_STAR__orig_val__23632);
}});
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.re_render_component = (function day8$re_frame_10x$inlined_deps$reagent$v1v2v0$reagent$dom$re_render_component(comp,container){
return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render_comp(comp,container,null);
});
/**
 * Render a Reagent component into the DOM. The first argument may be
 *   either a vector (using Reagent's Hiccup syntax), or a React element.
 *   The second argument should be a DOM node.
 * 
 *   Optionally takes a callback that is called when the component is in place.
 * 
 *   Returns the mounted component instance.
 */
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render = (function day8$re_frame_10x$inlined_deps$reagent$v1v2v0$reagent$dom$render(var_args){
var G__23649 = arguments.length;
switch (G__23649) {
case 2:
return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render.cljs$core$IFn$_invoke$arity$2 = (function (comp,container){
return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render.cljs$core$IFn$_invoke$arity$3(comp,container,day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.template._STAR_current_default_compiler_STAR_);
}));

(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render.cljs$core$IFn$_invoke$arity$3 = (function (comp,container,callback_or_compiler){
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.ratom.flush_BANG_();

var vec__23664 = ((cljs.core.map_QMARK_(callback_or_compiler))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"compiler","compiler",-267926731).cljs$core$IFn$_invoke$arity$1(callback_or_compiler),new cljs.core.Keyword(null,"callback","callback",-705136228).cljs$core$IFn$_invoke$arity$1(callback_or_compiler)], null):((cljs.core.fn_QMARK_(callback_or_compiler))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.template._STAR_current_default_compiler_STAR_,callback_or_compiler], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [callback_or_compiler,null], null)
));
var compiler = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23664,(0),null);
var callback = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23664,(1),null);
var f = (function (){
return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.protocols.as_element(compiler,((cljs.core.fn_QMARK_(comp))?(comp.cljs$core$IFn$_invoke$arity$0 ? comp.cljs$core$IFn$_invoke$arity$0() : comp.call(null)):comp));
});
return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render_comp(f,container,callback);
}));

(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.render.cljs$lang$maxFixedArity = 3);

/**
 * Remove a component from the given DOM node.
 */
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.unmount_component_at_node = (function day8$re_frame_10x$inlined_deps$reagent$v1v2v0$reagent$dom$unmount_component_at_node(container){
return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.unmount_comp(container);
});
/**
 * Returns the root DOM node of a mounted component.
 */
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.dom_node = (function day8$re_frame_10x$inlined_deps$reagent$v1v2v0$reagent$dom$dom_node(this$){
return module$node_modules$react_dom$index.findDOMNode(this$);
});
/**
 * Force re-rendering of all mounted Reagent components. This is
 *   probably only useful in a development environment, when you want to
 *   update components in response to some dynamic changes to code.
 * 
 *   Note that force-update-all may not update root components. This
 *   happens if a component 'foo' is mounted with `(render [foo])` (since
 *   functions are passed by value, and not by reference, in
 *   ClojureScript). To get around this you'll have to introduce a layer
 *   of indirection, for example by using `(render [#'foo])` instead.
 */
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.force_update_all = (function day8$re_frame_10x$inlined_deps$reagent$v1v2v0$reagent$dom$force_update_all(){
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.ratom.flush_BANG_();

var seq__23696_23729 = cljs.core.seq(cljs.core.deref(day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.roots));
var chunk__23697_23730 = null;
var count__23698_23731 = (0);
var i__23699_23732 = (0);
while(true){
if((i__23699_23732 < count__23698_23731)){
var vec__23712_23733 = chunk__23697_23730.cljs$core$IIndexed$_nth$arity$2(null,i__23699_23732);
var container_23734 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23712_23733,(0),null);
var comp_23735 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23712_23733,(1),null);
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.re_render_component(comp_23735,container_23734);


var G__23736 = seq__23696_23729;
var G__23737 = chunk__23697_23730;
var G__23738 = count__23698_23731;
var G__23739 = (i__23699_23732 + (1));
seq__23696_23729 = G__23736;
chunk__23697_23730 = G__23737;
count__23698_23731 = G__23738;
i__23699_23732 = G__23739;
continue;
} else {
var temp__5825__auto___23740 = cljs.core.seq(seq__23696_23729);
if(temp__5825__auto___23740){
var seq__23696_23741__$1 = temp__5825__auto___23740;
if(cljs.core.chunked_seq_QMARK_(seq__23696_23741__$1)){
var c__5673__auto___23748 = cljs.core.chunk_first(seq__23696_23741__$1);
var G__23749 = cljs.core.chunk_rest(seq__23696_23741__$1);
var G__23750 = c__5673__auto___23748;
var G__23751 = cljs.core.count(c__5673__auto___23748);
var G__23752 = (0);
seq__23696_23729 = G__23749;
chunk__23697_23730 = G__23750;
count__23698_23731 = G__23751;
i__23699_23732 = G__23752;
continue;
} else {
var vec__23716_23753 = cljs.core.first(seq__23696_23741__$1);
var container_23754 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23716_23753,(0),null);
var comp_23755 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23716_23753,(1),null);
day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.re_render_component(comp_23755,container_23754);


var G__23757 = cljs.core.next(seq__23696_23741__$1);
var G__23758 = null;
var G__23759 = (0);
var G__23760 = (0);
seq__23696_23729 = G__23757;
chunk__23697_23730 = G__23758;
count__23698_23731 = G__23759;
i__23699_23732 = G__23760;
continue;
}
} else {
}
}
break;
}

return day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.impl.batching.flush_after_render();
});

//# sourceMappingURL=day8.re_frame_10x.inlined_deps.reagent.v1v2v0.reagent.dom.js.map
