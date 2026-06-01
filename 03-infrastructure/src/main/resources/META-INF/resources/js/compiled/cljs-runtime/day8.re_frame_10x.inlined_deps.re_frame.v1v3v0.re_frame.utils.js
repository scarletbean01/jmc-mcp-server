goog.provide('day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils');
/**
 * Dissociates an entry from a nested associative structure returning a new
 *   nested structure. keys is a sequence of keys. Any empty maps that result
 *   will not be present in the new structure.
 *   The key thing is that 'm' remains identical? to itself if the path was never present
 */
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.dissoc_in = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$utils$dissoc_in(m,p__18314){
var vec__18319 = p__18314;
var seq__18320 = cljs.core.seq(vec__18319);
var first__18321 = cljs.core.first(seq__18320);
var seq__18320__$1 = cljs.core.next(seq__18320);
var k = first__18321;
var ks = seq__18320__$1;
var keys = vec__18319;
if(ks){
var temp__5823__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(m,k);
if(cljs.core.truth_(temp__5823__auto__)){
var nextmap = temp__5823__auto__;
var newmap = (day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.dissoc_in.cljs$core$IFn$_invoke$arity$2 ? day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.dissoc_in.cljs$core$IFn$_invoke$arity$2(nextmap,ks) : day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.dissoc_in.call(null,nextmap,ks));
if(cljs.core.seq(newmap)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(m,k,newmap);
} else {
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(m,k);
}
} else {
return m;
}
} else {
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(m,k);
}
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.first_in_vector = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$utils$first_in_vector(v){
if(cljs.core.vector_QMARK_(v)){
return cljs.core.first(v);
} else {
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: expected a vector, but got:",v], 0));
}
});
/**
 * Like apply, but f takes keyword arguments and the last argument is
 *   not a seq but a map with the arguments for f
 */
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.apply_kw = (function day8$re_frame_10x$inlined_deps$re_frame$v1v3v0$re_frame$utils$apply_kw(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18346 = arguments.length;
var i__5877__auto___18347 = (0);
while(true){
if((i__5877__auto___18347 < len__5876__auto___18346)){
args__5882__auto__.push((arguments[i__5877__auto___18347]));

var G__18348 = (i__5877__auto___18347 + (1));
i__5877__auto___18347 = G__18348;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.apply_kw.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.apply_kw.cljs$core$IFn$_invoke$arity$variadic = (function (f,args){
if(cljs.core.map_QMARK_(cljs.core.last(args))){
} else {
throw (new Error("Assert failed: (map? (last args))"));
}

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.concat,cljs.core.butlast(args),cljs.core.last(args)));
}));

(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.apply_kw.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.apply_kw.cljs$lang$applyTo = (function (seq18325){
var G__18326 = cljs.core.first(seq18325);
var seq18325__$1 = cljs.core.next(seq18325);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__18326,seq18325__$1);
}));


//# sourceMappingURL=day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.utils.js.map
