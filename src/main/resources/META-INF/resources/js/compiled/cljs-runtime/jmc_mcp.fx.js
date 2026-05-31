goog.provide('jmc_mcp.fx');
re_frame.core.reg_fx(new cljs.core.Keyword("sse","connect","sse/connect",1232641382),(function (p__17388){
var map__17389 = p__17388;
var map__17389__$1 = cljs.core.__destructure_map(map__17389);
var url = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17389__$1,new cljs.core.Keyword(null,"url","url",276297046));
var on_message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17389__$1,new cljs.core.Keyword(null,"on-message","on-message",1662987808));
var on_error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17389__$1,new cljs.core.Keyword(null,"on-error","on-error",1728533530));
var source = (new EventSource(url));
(source.onmessage = (function (event){
var data = JSON.parse(event.data);
return re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_message,cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0))));
}));

(source.onerror = (function (event){
return re_frame.core.dispatch(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(on_error,event));
}));

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("sse","sources","sse/sources",-321313379)], null)),cljs.core.assoc,url,source);
}));
re_frame.core.reg_fx(new cljs.core.Keyword(null,"notify","notify",-1256867814),(function (p__17390){
var map__17391 = p__17390;
var map__17391__$1 = cljs.core.__destructure_map(map__17391);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17391__$1,new cljs.core.Keyword(null,"type","type",1174270348));
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17391__$1,new cljs.core.Keyword(null,"message","message",-406056002));
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("notification","add","notification/add",-797397960),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),type,new cljs.core.Keyword(null,"message","message",-406056002),message], null)], null));
}));

//# sourceMappingURL=jmc_mcp.fx.js.map
