goog.provide('re_com.theme');
re_com.theme.registry = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"base-variables","base-variables",1989708737),re_com.theme.default$.base_variables,new cljs.core.Keyword(null,"main-variables","main-variables",871207486),re_com.theme.default$.main_variables,new cljs.core.Keyword(null,"user-variables","user-variables",-1579155551),cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword(null,"base","base",185279322),re_com.theme.default$.base,new cljs.core.Keyword(null,"main","main",-2117802661),re_com.theme.default$.main,new cljs.core.Keyword(null,"user","user",1532431356),cljs.core.PersistentVector.EMPTY], null));
re_com.theme.named__GT_vec = cljs.core.memoize(cljs.core.juxt.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"base-variables","base-variables",1989708737),new cljs.core.Keyword(null,"main-variables","main-variables",871207486),new cljs.core.Keyword(null,"user-variables","user-variables",-1579155551),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"base","base",185279322),new cljs.core.Keyword(null,"main","main",-2117802661),new cljs.core.Keyword(null,"user","user",1532431356)], 0)));
re_com.theme.global = reagent.ratom.make_reaction((function (){
return cljs.core.flatten(re_com.theme.named__GT_vec(cljs.core.deref(re_com.theme.registry)));
}));
re_com.theme.merge_props = re_com.theme.util.merge_props;
re_com.theme.parts = re_com.theme.util.parts;
re_com.theme.args = re_com.theme.util.args;
re_com.theme.rf = (function re_com$theme$rf(p__19424,theme){
var vec__19425 = p__19424;
var props = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19425,(0),null);
var ctx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19425,(1),null);
var result = (theme.cljs$core$IFn$_invoke$arity$2 ? theme.cljs$core$IFn$_invoke$arity$2(props,ctx) : theme.call(null,props,ctx));
if(cljs.core.vector_QMARK_(result)){
return result;
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [result,ctx], null);
}
});
re_com.theme.apply = (function re_com$theme$apply(props,ctx,themes){
return cljs.core.first(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(re_com.theme.rf,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [props,ctx], null),cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.flatten(re_com.theme.named__GT_vec((((!(cljs.core.map_QMARK_(themes))))?cljs.core.update.cljs$core$IFn$_invoke$arity$4(cljs.core.deref(re_com.theme.registry),new cljs.core.Keyword(null,"user","user",1532431356),cljs.core.conj,themes):(function (){var map__19430 = themes;
var map__19430__$1 = cljs.core.__destructure_map(map__19430);
var base = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19430__$1,new cljs.core.Keyword(null,"base","base",185279322));
var main = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19430__$1,new cljs.core.Keyword(null,"main","main",-2117802661));
var user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19430__$1,new cljs.core.Keyword(null,"user","user",1532431356));
var main_variables = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19430__$1,new cljs.core.Keyword(null,"main-variables","main-variables",871207486));
var user_variables = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19430__$1,new cljs.core.Keyword(null,"user-variables","user-variables",-1579155551));
var base_variables = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19430__$1,new cljs.core.Keyword(null,"base-variables","base-variables",1989708737));
var G__19433 = cljs.core.deref(re_com.theme.registry);
var G__19433__$1 = (cljs.core.truth_(base_variables)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__19433,new cljs.core.Keyword(null,"base-variables","base-variables",1989708737),base_variables):G__19433);
var G__19433__$2 = (cljs.core.truth_(main_variables)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__19433__$1,new cljs.core.Keyword(null,"main-variables","main-variables",871207486),main_variables):G__19433__$1);
var G__19433__$3 = (cljs.core.truth_(user_variables)?cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__19433__$2,new cljs.core.Keyword(null,"user-variables","user-variables",-1579155551),cljs.core.conj,user_variables):G__19433__$2);
var G__19433__$4 = (cljs.core.truth_(base)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__19433__$3,new cljs.core.Keyword(null,"base","base",185279322),base):G__19433__$3);
var G__19433__$5 = (cljs.core.truth_(main)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__19433__$4,new cljs.core.Keyword(null,"main","main",-2117802661),main):G__19433__$4);
if(cljs.core.truth_(user)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__19433__$5,new cljs.core.Keyword(null,"user","user",1532431356),cljs.core.conj,user);
} else {
return G__19433__$5;
}
})()))))));
});
re_com.theme.props = (function re_com$theme$props(ctx,themes){
return re_com.theme.apply(cljs.core.PersistentArrayMap.EMPTY,ctx,themes);
});

//# sourceMappingURL=re_com.theme.js.map
