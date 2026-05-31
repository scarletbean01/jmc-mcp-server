goog.provide('frontend.fx');
re_frame.core.reg_fx(new cljs.core.Keyword(null,"http","http",382524695),(function (p__17376){
var map__17378 = p__17376;
var map__17378__$1 = cljs.core.__destructure_map(map__17378);
var uri = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"uri","uri",-774711847));
var timeout = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"timeout","timeout",-318625318));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var format = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"format","format",-1306924766));
var method = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"method","method",55703592));
var on_progress = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"on-progress","on-progress",1196110410));
var response_format = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"response-format","response-format",1664465322));
var params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"params","params",710516235));
var on_success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"on-success","on-success",1786904109));
var headers = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"headers","headers",-835030129));
var on_failure = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17378__$1,new cljs.core.Keyword(null,"on-failure","on-failure",842888245));
var success_handler = (function (response){
if(cljs.core.truth_(on_success)){
return re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_success,response));
} else {
return null;
}
});
var error_handler = (function (response){
if(cljs.core.truth_(on_failure)){
return re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_failure,response));
} else {
return null;
}
});
var progress_handler = (cljs.core.truth_(on_progress)?(function (e){
if(cljs.core.truth_(e.lengthComputable)){
var percent = ((100) * (e.loaded / e.total));
return re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_progress,percent));
} else {
return null;
}
}):null);
var opts = (function (){var G__17379 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"handler","handler",-195596612),success_handler,new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),error_handler,new cljs.core.Keyword(null,"timeout","timeout",-318625318),(function (){var or__5142__auto__ = timeout;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (30000);
}
})(),new cljs.core.Keyword(null,"response-format","response-format",1664465322),(function (){var or__5142__auto__ = response_format;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null));
}
})()], null);
var G__17379__$1 = (cljs.core.truth_(params)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17379,new cljs.core.Keyword(null,"params","params",710516235),params):G__17379);
var G__17379__$2 = (cljs.core.truth_(body)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17379__$1,new cljs.core.Keyword(null,"body","body",-2049205669),body):G__17379__$1);
var G__17379__$3 = (cljs.core.truth_(headers)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17379__$2,new cljs.core.Keyword(null,"headers","headers",-835030129),headers):G__17379__$2);
var G__17379__$4 = (cljs.core.truth_(format)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17379__$3,new cljs.core.Keyword(null,"format","format",-1306924766),format):G__17379__$3);
var G__17379__$5 = (((((format == null)) && ((body == null))))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17379__$4,new cljs.core.Keyword(null,"format","format",-1306924766),ajax.core.json_request_format()):G__17379__$4);
if(cljs.core.truth_(progress_handler)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17379__$5,new cljs.core.Keyword(null,"progress-handler","progress-handler",333585589),progress_handler);
} else {
return G__17379__$5;
}
})();
var G__17393 = method;
var G__17393__$1 = (((G__17393 instanceof cljs.core.Keyword))?G__17393.fqn:null);
switch (G__17393__$1) {
case "get":
return ajax.core.GET.cljs$core$IFn$_invoke$arity$variadic(uri,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([opts], 0));

break;
case "post":
return ajax.core.POST.cljs$core$IFn$_invoke$arity$variadic(uri,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([opts], 0));

break;
case "put":
return ajax.core.PUT.cljs$core$IFn$_invoke$arity$variadic(uri,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([opts], 0));

break;
case "delete":
return ajax.core.DELETE.cljs$core$IFn$_invoke$arity$variadic(uri,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([opts], 0));

break;
default:
return ajax.core.GET.cljs$core$IFn$_invoke$arity$variadic(uri,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([opts], 0));

}
}));
re_frame.core.reg_fx(new cljs.core.Keyword("sse","connect","sse/connect",1232641382),(function (p__17400){
var map__17401 = p__17400;
var map__17401__$1 = cljs.core.__destructure_map(map__17401);
var url = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17401__$1,new cljs.core.Keyword(null,"url","url",276297046));
var on_message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17401__$1,new cljs.core.Keyword(null,"on-message","on-message",1662987808));
var on_error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17401__$1,new cljs.core.Keyword(null,"on-error","on-error",1728533530));
var on_open = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17401__$1,new cljs.core.Keyword(null,"on-open","on-open",-1391088163));
var source = (new EventSource(url));
(source.onmessage = (function (e){
var data = cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(JSON.parse(e.data),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
if(cljs.core.truth_(on_message)){
return re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_message,data));
} else {
return null;
}
}));

(source.onerror = (function (e){
if(cljs.core.truth_(on_error)){
re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_error,e));
} else {
}

return source.close();
}));

return (source.onopen = (function (e){
if(cljs.core.truth_(on_open)){
return re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_open,e));
} else {
return null;
}
}));
}));
re_frame.core.reg_fx(new cljs.core.Keyword(null,"notify","notify",-1256867814),(function (notification){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("notification","add","notification/add",-797397960),notification], null));
}));
re_frame.core.reg_fx(new cljs.core.Keyword(null,"navigate","navigate",657596805),(function (route){
if(cljs.core.vector_QMARK_(route)){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(reitit.frontend.easy.push_state,route);
} else {
return reitit.frontend.easy.push_state.cljs$core$IFn$_invoke$arity$1(route);
}
}));
re_frame.core.reg_fx(new cljs.core.Keyword(null,"dispatch-later","dispatch-later",291951390),(function (p__17425){
var map__17426 = p__17425;
var map__17426__$1 = cljs.core.__destructure_map(map__17426);
var ms = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17426__$1,new cljs.core.Keyword(null,"ms","ms",-1152709733));
var dispatch = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17426__$1,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009));
return setTimeout((function (){
return re_frame.core.dispatch(dispatch);
}),ms);
}));

//# sourceMappingURL=frontend.fx.js.map
