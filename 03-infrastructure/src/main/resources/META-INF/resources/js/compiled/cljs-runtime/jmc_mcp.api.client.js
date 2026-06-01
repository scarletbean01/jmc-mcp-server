goog.provide('jmc_mcp.api.client');
jmc_mcp.api.client.url = (function jmc_mcp$api$client$url(var_args){
var args__5882__auto__ = [];
var len__5876__auto___17417 = arguments.length;
var i__5877__auto___17418 = (0);
while(true){
if((i__5877__auto___17418 < len__5876__auto___17417)){
args__5882__auto__.push((arguments[i__5877__auto___17418]));

var G__17419 = (i__5877__auto___17418 + (1));
i__5877__auto___17418 = G__17419;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic = (function (paths){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.str,jmc_mcp.config.api_url,paths);
}));

(jmc_mcp.api.client.url.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(jmc_mcp.api.client.url.cljs$lang$applyTo = (function (seq17387){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq17387));
}));

jmc_mcp.api.client.GET = (function jmc_mcp$api$client$GET(path,options){
return ajax.core.GET.cljs$core$IFn$_invoke$arity$variadic(jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path], 0)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (e){
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["API Error:",e], 0));
})], null),options], 0))], 0));
});
jmc_mcp.api.client.POST = (function jmc_mcp$api$client$POST(path,options){
return ajax.core.POST.cljs$core$IFn$_invoke$arity$variadic(jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path], 0)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"format","format",-1306924766),ajax.core.json_request_format(),new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (e){
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["API Error:",e], 0));
})], null),options], 0))], 0));
});
jmc_mcp.api.client.DELETE = (function jmc_mcp$api$client$DELETE(path,options){
return ajax.core.DELETE.cljs$core$IFn$_invoke$arity$variadic(jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path], 0)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (e){
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["API Error:",e], 0));
})], null),options], 0))], 0));
});

//# sourceMappingURL=jmc_mcp.api.client.js.map
