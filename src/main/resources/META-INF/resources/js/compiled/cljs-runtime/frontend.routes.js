goog.provide('frontend.routes');
frontend.routes.routes = new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"library","library",467978288)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["recordings/:id",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"recording","recording",322996097)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["compare",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"compare","compare",-530677770)], null)], null)], null);
frontend.routes.router = (function frontend$routes$router(){
return reitit.frontend.router.cljs$core$IFn$_invoke$arity$2(frontend.routes.routes,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data","data",-232669377),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"coercion","coercion",904067157),reitit.coercion.spec.coercion], null)], null));
});
frontend.routes.on_navigate = (function frontend$routes$on_navigate(new_match){
if(cljs.core.truth_(new_match)){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("route","changed","route/changed",1518454126),new_match], null));
} else {
return null;
}
});
frontend.routes.start_BANG_ = (function frontend$routes$start_BANG_(){
return reitit.frontend.easy.start_BANG_(frontend.routes.router(),frontend.routes.on_navigate,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"use-fragment","use-fragment",-1617737154),false], null));
});
frontend.routes.href = (function frontend$routes$href(var_args){
var args__5882__auto__ = [];
var len__5876__auto___28046 = arguments.length;
var i__5877__auto___28047 = (0);
while(true){
if((i__5877__auto___28047 < len__5876__auto___28046)){
args__5882__auto__.push((arguments[i__5877__auto___28047]));

var G__28048 = (i__5877__auto___28047 + (1));
i__5877__auto___28047 = G__28048;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return frontend.routes.href.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(frontend.routes.href.cljs$core$IFn$_invoke$arity$variadic = (function (name,p__28042){
var vec__28043 = p__28042;
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__28043,(0),null);
var query = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__28043,(1),null);
return reitit.frontend.easy.href.cljs$core$IFn$_invoke$arity$3(name,params,query);
}));

(frontend.routes.href.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(frontend.routes.href.cljs$lang$applyTo = (function (seq28040){
var G__28041 = cljs.core.first(seq28040);
var seq28040__$1 = cljs.core.next(seq28040);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__28041,seq28040__$1);
}));


//# sourceMappingURL=frontend.routes.js.map
