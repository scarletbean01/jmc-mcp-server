goog.provide('re_com.theme.util');
re_com.theme.util.merge_props = (function re_com$theme$util$merge_props(var_args){
var args__5882__auto__ = [];
var len__5876__auto___19340 = arguments.length;
var i__5877__auto___19341 = (0);
while(true){
if((i__5877__auto___19341 < len__5876__auto___19340)){
args__5882__auto__.push((arguments[i__5877__auto___19341]));

var G__19342 = (i__5877__auto___19341 + (1));
i__5877__auto___19341 = G__19342;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.theme.util.merge_props.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.theme.util.merge_props.cljs$core$IFn$_invoke$arity$variadic = (function (ms){
var class_vec = (function (p1__19306_SHARP_){
if(cljs.core.vector_QMARK_(p1__19306_SHARP_)){
return p1__19306_SHARP_;
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__19306_SHARP_], null);
}
});
var ms__$1 = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,ms);
var ms__$2 = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__19307_SHARP_){
var G__19327 = p1__19307_SHARP_;
if(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.map_QMARK_(p1__19307_SHARP_);
if(and__5140__auto__){
return new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(p1__19307_SHARP_);
} else {
return and__5140__auto__;
}
})())){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(G__19327,new cljs.core.Keyword(null,"class","class",-2030961996),class_vec);
} else {
return G__19327;
}
}),ms__$1);
if(cljs.core.every_QMARK_(cljs.core.map_QMARK_,ms__$2)){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.merge_with,re_com.theme.util.merge_props,ms__$2);
} else {
if(cljs.core.every_QMARK_(cljs.core.vector_QMARK_,ms__$2)){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$2(cljs.core.into,ms__$2);
} else {
return cljs.core.last(ms__$2);

}
}
}));

(re_com.theme.util.merge_props.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.theme.util.merge_props.cljs$lang$applyTo = (function (seq19311){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq19311));
}));

re_com.theme.util.parts = (function re_com$theme$util$parts(part__GT_props){
return (function (props,p__19331){
var map__19332 = p__19331;
var map__19332__$1 = cljs.core.__destructure_map(map__19332);
var part = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19332__$1,new cljs.core.Keyword(null,"part","part",77757738));
var temp__5823__auto__ = (function (){var or__5142__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(part__GT_props,part);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(part__GT_props,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(cljs.core.name(part)));
}
})();
if(cljs.core.truth_(temp__5823__auto__)){
var v = temp__5823__auto__;
return re_com.theme.util.merge_props.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([props,v], 0));
} else {
return props;
}
});
});
re_com.theme.util.args = (function re_com$theme$util$args(p__19336){
var map__19338 = p__19336;
var map__19338__$1 = cljs.core.__destructure_map(map__19338);
var attr = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19338__$1,new cljs.core.Keyword(null,"attr","attr",-604132353));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19338__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19338__$1,new cljs.core.Keyword(null,"style","style",-496642736));
return (function (props,_){
return re_com.theme.util.merge_props.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([props,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"attr","attr",-604132353),attr,new cljs.core.Keyword(null,"class","class",-2030961996),class$,new cljs.core.Keyword(null,"style","style",-496642736),style], null)], 0));
});
});

//# sourceMappingURL=re_com.theme.util.js.map
