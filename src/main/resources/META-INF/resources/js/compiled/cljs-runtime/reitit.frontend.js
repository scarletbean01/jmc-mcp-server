goog.provide('reitit.frontend');
reitit.frontend.query_param = (function reitit$frontend$query_param(q,k){
var vs = q.getValues(k);
if((vs.length < (2))){
return (vs[(0)]);
} else {
return cljs.core.vec(vs);
}
});
/**
 * Given goog.Uri, read query parameters into a Clojure map.
 */
reitit.frontend.query_params = (function reitit$frontend$query_params(uri){
var q = uri.getQueryData();
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.juxt.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword,(function (p1__22865_SHARP_){
return reitit.frontend.query_param(q,p1__22865_SHARP_);
})),q.getKeys()));
});
/**
 * Given Reitit-frontend path, update the query params
 *   with given function and arguments.
 * 
 *   Note: coercion is not applied to the query params
 */
reitit.frontend.set_query_params = (function reitit$frontend$set_query_params(path,new_query_or_update_fn){
var uri = goog.Uri.parse(path);
var new_query = ((cljs.core.fn_QMARK_(new_query_or_update_fn))?(function (){var G__22871 = reitit.frontend.query_params(uri);
return (new_query_or_update_fn.cljs$core$IFn$_invoke$arity$1 ? new_query_or_update_fn.cljs$core$IFn$_invoke$arity$1(G__22871) : new_query_or_update_fn.call(null,G__22871));
})():new_query_or_update_fn);
uri.setQueryData(goog.Uri.QueryData.createFromMap(cljs.core.clj__GT_js(new_query)));

return uri.toString();
});
/**
 * Create routing path from given match and optional query-string map and
 *   optional fragment string.
 */
reitit.frontend.match__GT_path = (function reitit$frontend$match__GT_path(var_args){
var G__22881 = arguments.length;
switch (G__22881) {
case 1:
return reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$1 = (function (match){
return reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$3(match,null,null);
}));

(reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$2 = (function (match,query_params){
return reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$3(match,query_params,null);
}));

(reitit.frontend.match__GT_path.cljs$core$IFn$_invoke$arity$3 = (function (match,query_params,fragment){
var temp__5825__auto__ = reitit.core.match__GT_path.cljs$core$IFn$_invoke$arity$2(match,query_params);
if(cljs.core.truth_(temp__5825__auto__)){
var path = temp__5825__auto__;
var G__22887 = path;
if(cljs.core.truth_((function (){var and__5140__auto__ = fragment;
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.seq(fragment);
} else {
return and__5140__auto__;
}
})())){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__22887)+"#"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(reitit.impl.form_encode(fragment)));
} else {
return G__22887;
}
} else {
return null;
}
}));

(reitit.frontend.match__GT_path.cljs$lang$maxFixedArity = 3);

/**
 * Given routing tree and current path, return match with possibly
 *   coerced parameters. Return nil if no match found.
 * 
 *   :on-coercion-error - a sideeffecting fn of `match exception -> nil`
 */
reitit.frontend.match_by_path = (function reitit$frontend$match_by_path(var_args){
var G__22892 = arguments.length;
switch (G__22892) {
case 2:
return reitit.frontend.match_by_path.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return reitit.frontend.match_by_path.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(reitit.frontend.match_by_path.cljs$core$IFn$_invoke$arity$2 = (function (router,path){
return reitit.frontend.match_by_path.cljs$core$IFn$_invoke$arity$3(router,path,null);
}));

(reitit.frontend.match_by_path.cljs$core$IFn$_invoke$arity$3 = (function (router,path,p__22894){
var map__22895 = p__22894;
var map__22895__$1 = cljs.core.__destructure_map(map__22895);
var on_coercion_error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22895__$1,new cljs.core.Keyword(null,"on-coercion-error","on-coercion-error",-970787));
var uri = goog.Uri.parse(path);
var coerce_BANG_ = (cljs.core.truth_(on_coercion_error)?(function (match){
try{return reitit.coercion.coerce_BANG_(match);
}catch (e22897){if((e22897 instanceof Error)){
var e = e22897;
(on_coercion_error.cljs$core$IFn$_invoke$arity$2 ? on_coercion_error.cljs$core$IFn$_invoke$arity$2(match,e) : on_coercion_error.call(null,match,e));

throw e;
} else {
throw e22897;

}
}}):reitit.coercion.coerce_BANG_);
var temp__5823__auto__ = reitit.core.match_by_path(router,uri.getPath());
if(cljs.core.truth_(temp__5823__auto__)){
var match = temp__5823__auto__;
var q = reitit.frontend.query_params(uri);
var fragment = (cljs.core.truth_(uri.hasFragment())?uri.getFragment():null);
var match__$1 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(match,new cljs.core.Keyword(null,"query-params","query-params",900640534),q,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"fragment","fragment",826775688),fragment], 0));
var parameters = (function (){var or__5142__auto__ = (coerce_BANG_.cljs$core$IFn$_invoke$arity$1 ? coerce_BANG_.cljs$core$IFn$_invoke$arity$1(match__$1) : coerce_BANG_.call(null,match__$1));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"path","path",-188191168),new cljs.core.Keyword(null,"path-params","path-params",-48130597).cljs$core$IFn$_invoke$arity$1(match__$1),new cljs.core.Keyword(null,"query","query",-1288509510),q,new cljs.core.Keyword(null,"fragment","fragment",826775688),fragment], null);
}
})();
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(match__$1,new cljs.core.Keyword(null,"parameters","parameters",-1229919748),parameters);
} else {
return null;
}
}));

(reitit.frontend.match_by_path.cljs$lang$maxFixedArity = 3);

/**
 * Given a router, route name and optionally path-parameters,
 *   will return a Match (exact match), PartialMatch (missing path-parameters)
 *   or `nil` (no match).
 */
reitit.frontend.match_by_name = (function reitit$frontend$match_by_name(var_args){
var G__22900 = arguments.length;
switch (G__22900) {
case 2:
return reitit.frontend.match_by_name.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return reitit.frontend.match_by_name.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(reitit.frontend.match_by_name.cljs$core$IFn$_invoke$arity$2 = (function (router,name){
return reitit.frontend.match_by_name.cljs$core$IFn$_invoke$arity$3(router,name,cljs.core.PersistentArrayMap.EMPTY);
}));

(reitit.frontend.match_by_name.cljs$core$IFn$_invoke$arity$3 = (function (router,name,path_params){
return reitit.core.match_by_name(router,name,path_params);
}));

(reitit.frontend.match_by_name.cljs$lang$maxFixedArity = 3);

/**
 * Create a `reitit.core.router` from raw route data and optionally an options map.
 *   Enables request coercion. See [[reitit.core/router]] for details on options.
 */
reitit.frontend.router = (function reitit$frontend$router(var_args){
var G__22907 = arguments.length;
switch (G__22907) {
case 1:
return reitit.frontend.router.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return reitit.frontend.router.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(reitit.frontend.router.cljs$core$IFn$_invoke$arity$1 = (function (raw_routes){
return reitit.frontend.router.cljs$core$IFn$_invoke$arity$2(raw_routes,cljs.core.PersistentArrayMap.EMPTY);
}));

(reitit.frontend.router.cljs$core$IFn$_invoke$arity$2 = (function (raw_routes,opts){
return reitit.core.router.cljs$core$IFn$_invoke$arity$2(raw_routes,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"compile","compile",608186429),reitit.coercion.compile_request_coercers], null),opts], 0)));
}));

(reitit.frontend.router.cljs$lang$maxFixedArity = 2);

/**
 * Logs problems using console.warn
 */
reitit.frontend.match_by_name_BANG_ = (function reitit$frontend$match_by_name_BANG_(var_args){
var G__22912 = arguments.length;
switch (G__22912) {
case 2:
return reitit.frontend.match_by_name_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return reitit.frontend.match_by_name_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(reitit.frontend.match_by_name_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (router,name){
return reitit.frontend.match_by_name_BANG_.cljs$core$IFn$_invoke$arity$3(router,name,cljs.core.PersistentArrayMap.EMPTY);
}));

(reitit.frontend.match_by_name_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (router,name,path_params){
var temp__5823__auto__ = reitit.frontend.match_by_name.cljs$core$IFn$_invoke$arity$3(router,name,path_params);
if(cljs.core.truth_(temp__5823__auto__)){
var match = temp__5823__auto__;
if(reitit.core.partial_match_QMARK_(match)){
if(cljs.core.every_QMARK_((function (p1__22910_SHARP_){
return cljs.core.contains_QMARK_(path_params,p1__22910_SHARP_);
}),new cljs.core.Keyword(null,"required","required",1807647006).cljs$core$IFn$_invoke$arity$1(match))){
return match;
} else {
var defined = cljs.core.set(cljs.core.keys(path_params));
var missing = clojure.set.difference.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"required","required",1807647006).cljs$core$IFn$_invoke$arity$1(match),defined);
console.warn("missing path-params for route",name,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"template","template",-702405684),new cljs.core.Keyword(null,"template","template",-702405684).cljs$core$IFn$_invoke$arity$1(match),new cljs.core.Keyword(null,"missing","missing",362507769),missing,new cljs.core.Keyword(null,"path-params","path-params",-48130597),path_params,new cljs.core.Keyword(null,"required","required",1807647006),new cljs.core.Keyword(null,"required","required",1807647006).cljs$core$IFn$_invoke$arity$1(match)], null));

return null;
}
} else {
return match;
}
} else {
console.warn("missing route",name);

return null;
}
}));

(reitit.frontend.match_by_name_BANG_.cljs$lang$maxFixedArity = 3);


//# sourceMappingURL=reitit.frontend.js.map
