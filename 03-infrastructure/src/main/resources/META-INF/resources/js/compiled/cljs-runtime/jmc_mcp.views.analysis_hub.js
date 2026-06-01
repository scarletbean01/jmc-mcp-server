goog.provide('jmc_mcp.views.analysis_hub');
var module$node_modules$react_chartjs_2$dist$index_cjs=shadow.js.require("module$node_modules$react_chartjs_2$dist$index_cjs", {});
var module$node_modules$chart_DOT_js$auto$auto_cjs=shadow.js.require("module$node_modules$chart_DOT_js$auto$auto_cjs", {});
jmc_mcp.views.analysis_hub.timeline_renderer = (function jmc_mcp$views$analysis_hub$timeline_renderer(data,x_key,y_key){
var chart_data = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"labels","labels",-626734591),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__18900_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(p1__18900_SHARP_,x_key);
}),data),new cljs.core.Keyword(null,"datasets","datasets",1896364419),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(y_key),new cljs.core.Keyword(null,"data","data",-232669377),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__18901_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(p1__18901_SHARP_,y_key);
}),data),new cljs.core.Keyword(null,"borderColor","borderColor",1372977096),"rgb(75, 192, 192)",new cljs.core.Keyword(null,"tension","tension",58343589),0.1], null)], null)], null);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"20px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,">",">",-555517146),module$node_modules$react_chartjs_2$dist$index_cjs.Line,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data","data",-232669377),cljs.core.clj__GT_js(chart_data)], null)], null)], null);
});
jmc_mcp.views.analysis_hub.flame_graph_renderer = (function jmc_mcp$views$analysis_hub$flame_graph_renderer(data){
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"40px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Flame Graph (SVG Implementation)"], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"height","height",1025178622),"400"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"rect","rect",-108902628),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"x","x",2099068185),"0",new cljs.core.Keyword(null,"y","y",-1757859776),"0",new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"height","height",1025178622),"400",new cljs.core.Keyword(null,"fill","fill",883462889),"#f8fafc"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"SVG Flame Graph goes here",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"margin-top","margin-top",392161226),"180px"], null)], null)], null)], null)], null);
});
jmc_mcp.views.analysis_hub.analysis_config = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"overview","overview",-435037267),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"label","label",1718410804),"Overview",new cljs.core.Keyword(null,"shape","shape",1190694006),new cljs.core.Keyword(null,"metrics","metrics",394093469),new cljs.core.Keyword(null,"sections","sections",-886710106),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jvm","jvm",-1214221423),new cljs.core.Keyword(null,"cpu","cpu",106162238),new cljs.core.Keyword(null,"memory","memory",-1449401430),new cljs.core.Keyword(null,"gc","gc",-177389165)], null)], null),new cljs.core.Keyword(null,"hot-methods","hot-methods",-608820376),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"label","label",1718410804),"Hot Methods",new cljs.core.Keyword(null,"shape","shape",1190694006),new cljs.core.Keyword(null,"table","table",-564943036),new cljs.core.Keyword(null,"columns","columns",1998437288),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"methodName","methodName",-2055958885),new cljs.core.Keyword(null,"label","label",1718410804),"Method"], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"sampleCount","sampleCount",-1510449569),new cljs.core.Keyword(null,"label","label",1718410804),"Samples"], null)], null)], null),new cljs.core.Keyword(null,"cpu-flame","cpu-flame",806360903),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),"CPU Flame Graph",new cljs.core.Keyword(null,"shape","shape",1190694006),new cljs.core.Keyword(null,"flame-graph","flame-graph",690002780),new cljs.core.Keyword(null,"depth-key","depth-key",227913967),new cljs.core.Keyword(null,"stackDepth","stackDepth",-2127627386),new cljs.core.Keyword(null,"value-key","value-key",395360462),new cljs.core.Keyword(null,"sampleCount","sampleCount",-1510449569)], null),new cljs.core.Keyword(null,"call-tree","call-tree",-2038557250),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),"Call Tree",new cljs.core.Keyword(null,"shape","shape",1190694006),new cljs.core.Keyword(null,"tree","tree",-196312028),new cljs.core.Keyword(null,"children-key","children-key",1977529285),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.Keyword(null,"label-key","label-key",1868394642),new cljs.core.Keyword(null,"methodName","methodName",-2055958885)], null),new cljs.core.Keyword(null,"heap-trends","heap-trends",1815450127),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),"Heap Trends",new cljs.core.Keyword(null,"shape","shape",1190694006),new cljs.core.Keyword(null,"timeline","timeline",192903161),new cljs.core.Keyword(null,"x-key","x-key",1333106756),new cljs.core.Keyword(null,"timestamp","timestamp",579478971),new cljs.core.Keyword(null,"y-key","y-key",-1934779752),new cljs.core.Keyword(null,"heapUsed","heapUsed",1856579262)], null),new cljs.core.Keyword(null,"exceptions","exceptions",-676341543),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"label","label",1718410804),"Exceptions",new cljs.core.Keyword(null,"shape","shape",1190694006),new cljs.core.Keyword(null,"table","table",-564943036),new cljs.core.Keyword(null,"columns","columns",1998437288),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"label","label",1718410804),"Type"], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"message","message",-406056002),new cljs.core.Keyword(null,"label","label",1718410804),"Message"], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"count","count",2139924085),new cljs.core.Keyword(null,"label","label",1718410804),"Count"], null)], null)], null)], null);
jmc_mcp.views.analysis_hub.categories = new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"label","label",1718410804),"Overview",new cljs.core.Keyword(null,"items","items",1031954938),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"overview","overview",-435037267)], null)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"label","label",1718410804),"CPU & Compilation",new cljs.core.Keyword(null,"items","items",1031954938),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"hot-methods","hot-methods",-608820376),new cljs.core.Keyword(null,"cpu-flame","cpu-flame",806360903),new cljs.core.Keyword(null,"call-tree","call-tree",-2038557250)], null)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"label","label",1718410804),"Memory",new cljs.core.Keyword(null,"items","items",1031954938),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"heap-trends","heap-trends",1815450127)], null)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"label","label",1718410804),"Diagnostics",new cljs.core.Keyword(null,"items","items",1031954938),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"exceptions","exceptions",-676341543)], null)], null)], null);
jmc_mcp.views.analysis_hub.analysis_sidebar_item = (function jmc_mcp$views$analysis_hub$analysis_sidebar_item(type){
var config = cljs.core.get.cljs$core$IFn$_invoke$arity$2(jmc_mcp.views.analysis_hub.analysis_config,type);
var active_type = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("recording-detail","active-analysis","recording-detail/active-analysis",-1756182815)], null));
return (function (){
return new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"analysis-item "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(active_type),type))?"active":null))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"8px 16px",new cljs.core.Keyword(null,"cursor","cursor",1011937484),"pointer",new cljs.core.Keyword(null,"gap","gap",80255254),"8px",new cljs.core.Keyword(null,"background-color","background-color",570434026),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(active_type),type))?"#f1f5f9":null)], null),new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","select-type","analysis/select-type",102415334),type], null));
})], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(config),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"14px"], null)], null)], null)], null);
});
});
jmc_mcp.views.analysis_hub.analysis_sidebar = (function jmc_mcp$views$analysis_hub$analysis_sidebar(){
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"width","width",-384071477),"220px",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"border-right","border-right",-668932860),"1px solid #e2e8f0"], null),new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = (function jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902(s__18903){
return (new cljs.core.LazySeq(null,(function (){
var s__18903__$1 = s__18903;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18903__$1);
if(temp__5825__auto__){
var s__18903__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18903__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18903__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18905 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18904 = (0);
while(true){
if((i__18904 < size__5627__auto__)){
var cat = cljs.core._nth(c__5626__auto__,i__18904);
cljs.core.chunk_append(b__18905,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(cat),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px"], null)], null),(function (){var iter__5628__auto__ = ((function (i__18904,cat,c__5626__auto__,size__5627__auto__,b__18905,s__18903__$2,temp__5825__auto__){
return (function jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902_$_iter__18906(s__18907){
return (new cljs.core.LazySeq(null,((function (i__18904,cat,c__5626__auto__,size__5627__auto__,b__18905,s__18903__$2,temp__5825__auto__){
return (function (){
var s__18907__$1 = s__18907;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__18907__$1);
if(temp__5825__auto____$1){
var s__18907__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__18907__$2)){
var c__5626__auto____$1 = cljs.core.chunk_first(s__18907__$2);
var size__5627__auto____$1 = cljs.core.count(c__5626__auto____$1);
var b__18909 = cljs.core.chunk_buffer(size__5627__auto____$1);
if((function (){var i__18908 = (0);
while(true){
if((i__18908 < size__5627__auto____$1)){
var item = cljs.core._nth(c__5626__auto____$1,i__18908);
cljs.core.chunk_append(b__18909,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.analysis_sidebar_item,item], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),item], null)));

var G__18962 = (i__18908 + (1));
i__18908 = G__18962;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18909),jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902_$_iter__18906(cljs.core.chunk_rest(s__18907__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18909),null);
}
} else {
var item = cljs.core.first(s__18907__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.analysis_sidebar_item,item], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),item], null)),jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902_$_iter__18906(cljs.core.rest(s__18907__$2)));
}
} else {
return null;
}
break;
}
});})(i__18904,cat,c__5626__auto__,size__5627__auto__,b__18905,s__18903__$2,temp__5825__auto__))
,null,null));
});})(i__18904,cat,c__5626__auto__,size__5627__auto__,b__18905,s__18903__$2,temp__5825__auto__))
;
return iter__5628__auto__(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(cat));
})()], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(cat)], null)));

var G__18964 = (i__18904 + (1));
i__18904 = G__18964;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18905),jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902(cljs.core.chunk_rest(s__18903__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18905),null);
}
} else {
var cat = cljs.core.first(s__18903__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(cat),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px"], null)], null),(function (){var iter__5628__auto__ = ((function (cat,s__18903__$2,temp__5825__auto__){
return (function jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902_$_iter__18910(s__18911){
return (new cljs.core.LazySeq(null,(function (){
var s__18911__$1 = s__18911;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__18911__$1);
if(temp__5825__auto____$1){
var s__18911__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__18911__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18911__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18913 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18912 = (0);
while(true){
if((i__18912 < size__5627__auto__)){
var item = cljs.core._nth(c__5626__auto__,i__18912);
cljs.core.chunk_append(b__18913,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.analysis_sidebar_item,item], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),item], null)));

var G__18965 = (i__18912 + (1));
i__18912 = G__18965;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18913),jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902_$_iter__18910(cljs.core.chunk_rest(s__18911__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18913),null);
}
} else {
var item = cljs.core.first(s__18911__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.analysis_sidebar_item,item], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),item], null)),jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902_$_iter__18910(cljs.core.rest(s__18911__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});})(cat,s__18903__$2,temp__5825__auto__))
;
return iter__5628__auto__(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(cat));
})()], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(cat)], null)),jmc_mcp$views$analysis_hub$analysis_sidebar_$_iter__18902(cljs.core.rest(s__18903__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(jmc_mcp.views.analysis_hub.categories);
})()], null);
});
jmc_mcp.views.analysis_hub.metrics_renderer = (function jmc_mcp$views$analysis_hub$metrics_renderer(data){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"gap","gap",80255254),"20px",new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = (function jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914(s__18915){
return (new cljs.core.LazySeq(null,(function (){
var s__18915__$1 = s__18915;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18915__$1);
if(temp__5825__auto__){
var s__18915__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18915__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18915__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18917 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18916 = (0);
while(true){
if((i__18916 < size__5627__auto__)){
var vec__18918 = cljs.core._nth(c__5626__auto__,i__18916);
var section = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18918,(0),null);
var metrics = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18918,(1),null);
cljs.core.chunk_append(b__18917,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"gap","gap",80255254),"12px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(section),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"gap","gap",80255254),"16px",new cljs.core.Keyword(null,"wrap?","wrap?",-1677427054),true,new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = ((function (i__18916,vec__18918,section,metrics,c__5626__auto__,size__5627__auto__,b__18917,s__18915__$2,temp__5825__auto__){
return (function jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914_$_iter__18921(s__18922){
return (new cljs.core.LazySeq(null,((function (i__18916,vec__18918,section,metrics,c__5626__auto__,size__5627__auto__,b__18917,s__18915__$2,temp__5825__auto__){
return (function (){
var s__18922__$1 = s__18922;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__18922__$1);
if(temp__5825__auto____$1){
var s__18922__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__18922__$2)){
var c__5626__auto____$1 = cljs.core.chunk_first(s__18922__$2);
var size__5627__auto____$1 = cljs.core.count(c__5626__auto____$1);
var b__18924 = cljs.core.chunk_buffer(size__5627__auto____$1);
if((function (){var i__18923 = (0);
while(true){
if((i__18923 < size__5627__auto____$1)){
var vec__18925 = cljs.core._nth(c__5626__auto____$1,i__18923);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18925,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18925,(1),null);
cljs.core.chunk_append(b__18924,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"padding","padding",1660304693),"16px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px",new cljs.core.Keyword(null,"min-width","min-width",1926193728),"180px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(k),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"18px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),k], null)));

var G__18966 = (i__18923 + (1));
i__18923 = G__18966;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18924),jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914_$_iter__18921(cljs.core.chunk_rest(s__18922__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18924),null);
}
} else {
var vec__18928 = cljs.core.first(s__18922__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18928,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18928,(1),null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"padding","padding",1660304693),"16px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px",new cljs.core.Keyword(null,"min-width","min-width",1926193728),"180px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(k),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"18px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),k], null)),jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914_$_iter__18921(cljs.core.rest(s__18922__$2)));
}
} else {
return null;
}
break;
}
});})(i__18916,vec__18918,section,metrics,c__5626__auto__,size__5627__auto__,b__18917,s__18915__$2,temp__5825__auto__))
,null,null));
});})(i__18916,vec__18918,section,metrics,c__5626__auto__,size__5627__auto__,b__18917,s__18915__$2,temp__5825__auto__))
;
return iter__5628__auto__(metrics);
})()], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),section], null)));

var G__18967 = (i__18916 + (1));
i__18916 = G__18967;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18917),jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914(cljs.core.chunk_rest(s__18915__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18917),null);
}
} else {
var vec__18931 = cljs.core.first(s__18915__$2);
var section = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18931,(0),null);
var metrics = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18931,(1),null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"gap","gap",80255254),"12px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(section),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"gap","gap",80255254),"16px",new cljs.core.Keyword(null,"wrap?","wrap?",-1677427054),true,new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = ((function (vec__18931,section,metrics,s__18915__$2,temp__5825__auto__){
return (function jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914_$_iter__18934(s__18935){
return (new cljs.core.LazySeq(null,(function (){
var s__18935__$1 = s__18935;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__18935__$1);
if(temp__5825__auto____$1){
var s__18935__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__18935__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18935__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18937 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18936 = (0);
while(true){
if((i__18936 < size__5627__auto__)){
var vec__18938 = cljs.core._nth(c__5626__auto__,i__18936);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18938,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18938,(1),null);
cljs.core.chunk_append(b__18937,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"padding","padding",1660304693),"16px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px",new cljs.core.Keyword(null,"min-width","min-width",1926193728),"180px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(k),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"18px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),k], null)));

var G__18968 = (i__18936 + (1));
i__18936 = G__18968;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18937),jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914_$_iter__18934(cljs.core.chunk_rest(s__18935__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18937),null);
}
} else {
var vec__18941 = cljs.core.first(s__18935__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18941,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18941,(1),null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"padding","padding",1660304693),"16px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px",new cljs.core.Keyword(null,"min-width","min-width",1926193728),"180px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(k),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"18px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),k], null)),jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914_$_iter__18934(cljs.core.rest(s__18935__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});})(vec__18931,section,metrics,s__18915__$2,temp__5825__auto__))
;
return iter__5628__auto__(metrics);
})()], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),section], null)),jmc_mcp$views$analysis_hub$metrics_renderer_$_iter__18914(cljs.core.rest(s__18915__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(data);
})()], null);
});
jmc_mcp.views.analysis_hub.table_renderer = (function jmc_mcp$views$analysis_hub$table_renderer(data,columns){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f8fafc",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0"], null),new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = (function jmc_mcp$views$analysis_hub$table_renderer_$_iter__18944(s__18945){
return (new cljs.core.LazySeq(null,(function (){
var s__18945__$1 = s__18945;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18945__$1);
if(temp__5825__auto__){
var s__18945__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18945__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18945__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18947 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18946 = (0);
while(true){
if((i__18946 < size__5627__auto__)){
var col = cljs.core._nth(c__5626__auto__,i__18946);
cljs.core.chunk_append(b__18947,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(col),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"200px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)], null)));

var G__18969 = (i__18946 + (1));
i__18946 = G__18969;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18947),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18944(cljs.core.chunk_rest(s__18945__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18947),null);
}
} else {
var col = cljs.core.first(s__18945__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(col),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"200px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)], null)),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18944(cljs.core.rest(s__18945__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(columns);
})()], null),(function (){var iter__5628__auto__ = (function jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948(s__18949){
return (new cljs.core.LazySeq(null,(function (){
var s__18949__$1 = s__18949;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18949__$1);
if(temp__5825__auto__){
var s__18949__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18949__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18949__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18951 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18950 = (0);
while(true){
if((i__18950 < size__5627__auto__)){
var row = cljs.core._nth(c__5626__auto__,i__18950);
cljs.core.chunk_append(b__18951,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #f1f5f9"], null),new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = ((function (i__18950,row,c__5626__auto__,size__5627__auto__,b__18951,s__18949__$2,temp__5825__auto__){
return (function jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948_$_iter__18952(s__18953){
return (new cljs.core.LazySeq(null,((function (i__18950,row,c__5626__auto__,size__5627__auto__,b__18951,s__18949__$2,temp__5825__auto__){
return (function (){
var s__18953__$1 = s__18953;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__18953__$1);
if(temp__5825__auto____$1){
var s__18953__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__18953__$2)){
var c__5626__auto____$1 = cljs.core.chunk_first(s__18953__$2);
var size__5627__auto____$1 = cljs.core.count(c__5626__auto____$1);
var b__18955 = cljs.core.chunk_buffer(size__5627__auto____$1);
if((function (){var i__18954 = (0);
while(true){
if((i__18954 < size__5627__auto____$1)){
var col = cljs.core._nth(c__5626__auto____$1,i__18954);
cljs.core.chunk_append(b__18955,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"200px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)], null)));

var G__18970 = (i__18954 + (1));
i__18954 = G__18970;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18955),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948_$_iter__18952(cljs.core.chunk_rest(s__18953__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18955),null);
}
} else {
var col = cljs.core.first(s__18953__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"200px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)], null)),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948_$_iter__18952(cljs.core.rest(s__18953__$2)));
}
} else {
return null;
}
break;
}
});})(i__18950,row,c__5626__auto__,size__5627__auto__,b__18951,s__18949__$2,temp__5825__auto__))
,null,null));
});})(i__18950,row,c__5626__auto__,size__5627__auto__,b__18951,s__18949__$2,temp__5825__auto__))
;
return iter__5628__auto__(columns);
})()], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(row))], null)));

var G__18971 = (i__18950 + (1));
i__18950 = G__18971;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18951),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948(cljs.core.chunk_rest(s__18949__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18951),null);
}
} else {
var row = cljs.core.first(s__18949__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #f1f5f9"], null),new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = ((function (row,s__18949__$2,temp__5825__auto__){
return (function jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948_$_iter__18956(s__18957){
return (new cljs.core.LazySeq(null,(function (){
var s__18957__$1 = s__18957;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__18957__$1);
if(temp__5825__auto____$1){
var s__18957__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__18957__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18957__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18959 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18958 = (0);
while(true){
if((i__18958 < size__5627__auto__)){
var col = cljs.core._nth(c__5626__auto__,i__18958);
cljs.core.chunk_append(b__18959,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"200px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)], null)));

var G__18972 = (i__18958 + (1));
i__18958 = G__18972;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18959),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948_$_iter__18956(cljs.core.chunk_rest(s__18957__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18959),null);
}
} else {
var col = cljs.core.first(s__18957__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"200px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(col)], null)),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948_$_iter__18956(cljs.core.rest(s__18957__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});})(row,s__18949__$2,temp__5825__auto__))
;
return iter__5628__auto__(columns);
})()], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(row))], null)),jmc_mcp$views$analysis_hub$table_renderer_$_iter__18948(cljs.core.rest(s__18949__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(data);
})()], null)], null);
});
jmc_mcp.views.analysis_hub.result_panel = (function jmc_mcp$views$analysis_hub$result_panel(){
var active_type = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("recording-detail","active-analysis","recording-detail/active-analysis",-1756182815)], null));
var result = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("recording-detail","result","recording-detail/result",1154446370),cljs.core.deref(active_type)], null));
var loading_QMARK_ = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("recording-detail","loading?","recording-detail/loading?",1646338942)], null));
return (function (){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"size","size",1098693007),"1",new cljs.core.Keyword(null,"children","children",-940561982),(cljs.core.truth_(cljs.core.deref(loading_QMARK_))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"padding","padding",1660304693),"100px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.components.spinner], null)], null)], null)], null):(function (){var temp__5823__auto__ = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(result));
if(cljs.core.truth_(temp__5823__auto__)){
var data = temp__5823__auto__;
var config = cljs.core.get.cljs$core$IFn$_invoke$arity$2(jmc_mcp.views.analysis_hub.analysis_config,cljs.core.deref(active_type));
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__18960 = new cljs.core.Keyword(null,"shape","shape",1190694006).cljs$core$IFn$_invoke$arity$1(config);
var G__18960__$1 = (((G__18960 instanceof cljs.core.Keyword))?G__18960.fqn:null);
switch (G__18960__$1) {
case "metrics":
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.metrics_renderer,data], null);

break;
case "table":
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.table_renderer,data,new cljs.core.Keyword(null,"columns","columns",1998437288).cljs$core$IFn$_invoke$arity$1(config)], null);

break;
case "timeline":
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.timeline_renderer,data,new cljs.core.Keyword(null,"x-key","x-key",1333106756).cljs$core$IFn$_invoke$arity$1(config),new cljs.core.Keyword(null,"y-key","y-key",-1934779752).cljs$core$IFn$_invoke$arity$1(config)], null);

break;
case "flame-graph":
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.flame_graph_renderer,data], null);

break;
default:
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+"Renderer for "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"shape","shape",1190694006).cljs$core$IFn$_invoke$arity$1(config))+" not implemented")], null);

}
})()], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"padding","padding",1660304693),"100px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"No data"], null)], null)], null)], null);
}
})())], null);
});
});
jmc_mcp.views.analysis_hub.analysis_hub_page = (function jmc_mcp$views$analysis_hub$analysis_hub_page(){
var recording_id = new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("route","params","route/params",601886674)], null))));
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"gap","gap",80255254),"24px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Analysis Hub",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"24px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"size","size",1098693007),"1",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px",new cljs.core.Keyword(null,"min-height","min-height",398480837),"600px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.analysis_sidebar], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.analysis_hub.result_panel], null)], null)], null)], null)], null);
});

//# sourceMappingURL=jmc_mcp.views.analysis_hub.js.map
