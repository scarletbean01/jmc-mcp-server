goog.provide('ajax.xml_http_request');
ajax.xml_http_request.ready_state = (function ajax$xml_http_request$ready_state(e){
var G__20106 = e.target.readyState;
var fexpr__20105 = new cljs.core.PersistentArrayMap(null, 5, [(0),new cljs.core.Keyword(null,"not-initialized","not-initialized",-1937378906),(1),new cljs.core.Keyword(null,"connection-established","connection-established",-1403749733),(2),new cljs.core.Keyword(null,"request-received","request-received",2110590540),(3),new cljs.core.Keyword(null,"processing-request","processing-request",-264947221),(4),new cljs.core.Keyword(null,"response-ready","response-ready",245208276)], null);
return (fexpr__20105.cljs$core$IFn$_invoke$arity$1 ? fexpr__20105.cljs$core$IFn$_invoke$arity$1(G__20106) : fexpr__20105.call(null,G__20106));
});
ajax.xml_http_request.append = (function ajax$xml_http_request$append(current,next){
if(cljs.core.truth_(current)){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(current)+", "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(next));
} else {
return next;
}
});
ajax.xml_http_request.process_headers = (function ajax$xml_http_request$process_headers(header_str){
if(cljs.core.truth_(header_str)){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (headers,header_line){
if(cljs.core.truth_(goog.string.isEmptyOrWhitespace(header_line))){
return headers;
} else {
var key_value = goog.string.splitLimit(header_line,": ",(2));
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(headers,(key_value[(0)]),ajax.xml_http_request.append,(key_value[(1)]));
}
}),cljs.core.PersistentArrayMap.EMPTY,header_str.split("\r\n"));
} else {
return cljs.core.PersistentArrayMap.EMPTY;
}
});
ajax.xml_http_request.xmlhttprequest = (((typeof goog !== 'undefined') && (typeof goog.global !== 'undefined') && (typeof goog.global.XMLHttpRequest !== 'undefined'))?goog.global.XMLHttpRequest:(((typeof require !== 'undefined'))?(function (){var req = require;
return (req.cljs$core$IFn$_invoke$arity$1 ? req.cljs$core$IFn$_invoke$arity$1("xmlhttprequest") : req.call(null,"xmlhttprequest")).XMLHttpRequest;
})():null));
(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxImpl$ = cljs.core.PROTOCOL_SENTINEL);

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxImpl$_js_ajax_request$arity$3 = (function (this$,p__20123,handler){
var map__20124 = p__20123;
var map__20124__$1 = cljs.core.__destructure_map(map__20124);
var uri = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20124__$1,new cljs.core.Keyword(null,"uri","uri",-774711847));
var method = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20124__$1,new cljs.core.Keyword(null,"method","method",55703592));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20124__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var headers = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20124__$1,new cljs.core.Keyword(null,"headers","headers",-835030129));
var timeout = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__20124__$1,new cljs.core.Keyword(null,"timeout","timeout",-318625318),(0));
var with_credentials = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__20124__$1,new cljs.core.Keyword(null,"with-credentials","with-credentials",-1163127235),false);
var response_format = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20124__$1,new cljs.core.Keyword(null,"response-format","response-format",1664465322));
var this$__$1 = this;
(this$__$1.withCredentials = with_credentials);

(this$__$1.onreadystatechange = (function (p1__20120_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"response-ready","response-ready",245208276),ajax.xml_http_request.ready_state(p1__20120_SHARP_))){
return (handler.cljs$core$IFn$_invoke$arity$1 ? handler.cljs$core$IFn$_invoke$arity$1(this$__$1) : handler.call(null,this$__$1));
} else {
return null;
}
}));

this$__$1.open(method,uri,true);

(this$__$1.timeout = timeout);

var temp__5825__auto___20155 = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(response_format);
if(cljs.core.truth_(temp__5825__auto___20155)){
var response_type_20156 = temp__5825__auto___20155;
(this$__$1.responseType = cljs.core.name(response_type_20156));
} else {
}

var seq__20130_20157 = cljs.core.seq(headers);
var chunk__20131_20158 = null;
var count__20132_20159 = (0);
var i__20133_20160 = (0);
while(true){
if((i__20133_20160 < count__20132_20159)){
var vec__20146_20163 = chunk__20131_20158.cljs$core$IIndexed$_nth$arity$2(null,i__20133_20160);
var k_20164 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20146_20163,(0),null);
var v_20165 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20146_20163,(1),null);
this$__$1.setRequestHeader(k_20164,v_20165);


var G__20166 = seq__20130_20157;
var G__20167 = chunk__20131_20158;
var G__20168 = count__20132_20159;
var G__20169 = (i__20133_20160 + (1));
seq__20130_20157 = G__20166;
chunk__20131_20158 = G__20167;
count__20132_20159 = G__20168;
i__20133_20160 = G__20169;
continue;
} else {
var temp__5825__auto___20170 = cljs.core.seq(seq__20130_20157);
if(temp__5825__auto___20170){
var seq__20130_20171__$1 = temp__5825__auto___20170;
if(cljs.core.chunked_seq_QMARK_(seq__20130_20171__$1)){
var c__5673__auto___20172 = cljs.core.chunk_first(seq__20130_20171__$1);
var G__20173 = cljs.core.chunk_rest(seq__20130_20171__$1);
var G__20174 = c__5673__auto___20172;
var G__20175 = cljs.core.count(c__5673__auto___20172);
var G__20176 = (0);
seq__20130_20157 = G__20173;
chunk__20131_20158 = G__20174;
count__20132_20159 = G__20175;
i__20133_20160 = G__20176;
continue;
} else {
var vec__20149_20177 = cljs.core.first(seq__20130_20171__$1);
var k_20178 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20149_20177,(0),null);
var v_20179 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20149_20177,(1),null);
this$__$1.setRequestHeader(k_20178,v_20179);


var G__20180 = cljs.core.next(seq__20130_20171__$1);
var G__20181 = null;
var G__20182 = (0);
var G__20183 = (0);
seq__20130_20157 = G__20180;
chunk__20131_20158 = G__20181;
count__20132_20159 = G__20182;
i__20133_20160 = G__20183;
continue;
}
} else {
}
}
break;
}

this$__$1.send((function (){var or__5142__auto__ = body;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "";
}
})());

return this$__$1;
}));

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxRequest$ = cljs.core.PROTOCOL_SENTINEL);

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxRequest$_abort$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.abort();
}));

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxResponse$ = cljs.core.PROTOCOL_SENTINEL);

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxResponse$_body$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.response;
}));

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxResponse$_status$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.status;
}));

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxResponse$_status_text$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.statusText;
}));

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxResponse$_get_all_headers$arity$1 = (function (this$){
var this$__$1 = this;
return ajax.xml_http_request.process_headers(this$__$1.getAllResponseHeaders());
}));

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxResponse$_get_response_header$arity$2 = (function (this$,header){
var this$__$1 = this;
return this$__$1.getResponseHeader(header);
}));

(ajax.xml_http_request.xmlhttprequest.prototype.ajax$protocols$AjaxResponse$_was_aborted$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((0),this$__$1.readyState);
}));

//# sourceMappingURL=ajax.xml_http_request.js.map
