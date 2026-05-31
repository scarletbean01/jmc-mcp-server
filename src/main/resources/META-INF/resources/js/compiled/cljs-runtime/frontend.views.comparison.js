goog.provide('frontend.views.comparison');
frontend.views.comparison.recording_dropdown = (function frontend$views$comparison$recording_dropdown(label,selected_id,on_change){
var recordings = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("recordings","items","recordings/items",-1991597412)], null)));
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),13], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"4px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),16], null)),new cljs.core.Keyword(null,"label","label",1718410804),label,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"color","color",1011675173),"#475569"], null)], null),new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.single_dropdown,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),20], null)),new cljs.core.Keyword(null,"choices","choices",1385611597),cljs.core.vec((function (){var iter__5628__auto__ = (function frontend$views$comparison$recording_dropdown_$_iter__30518(s__30519){
return (new cljs.core.LazySeq(null,(function (){
var s__30519__$1 = s__30519;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__30519__$1);
if(temp__5825__auto__){
var s__30519__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__30519__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__30519__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__30521 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__30520 = (0);
while(true){
if((i__30520 < size__5627__auto__)){
var r = cljs.core._nth(c__5626__auto__,i__30520);
cljs.core.chunk_append(b__30521,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"recordingId","recordingId",-308363159).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"label","label",1718410804),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"filename","filename",-1428840783).cljs$core$IFn$_invoke$arity$1(r);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"recordingId","recordingId",-308363159).cljs$core$IFn$_invoke$arity$1(r);
}
})()], null));

var G__30545 = (i__30520 + (1));
i__30520 = G__30545;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__30521),frontend$views$comparison$recording_dropdown_$_iter__30518(cljs.core.chunk_rest(s__30519__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__30521),null);
}
} else {
var r = cljs.core.first(s__30519__$2);
return cljs.core.cons(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"recordingId","recordingId",-308363159).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"label","label",1718410804),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"filename","filename",-1428840783).cljs$core$IFn$_invoke$arity$1(r);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"recordingId","recordingId",-308363159).cljs$core$IFn$_invoke$arity$1(r);
}
})()], null),frontend$views$comparison$recording_dropdown_$_iter__30518(cljs.core.rest(s__30519__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(recordings);
})()),new cljs.core.Keyword(null,"model","model",331153215),selected_id,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Select recording...",new cljs.core.Keyword(null,"on-change","on-change",-732046149),on_change,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"300px"], null)], null)], null)], null);
});
frontend.views.comparison.metric_row = (function frontend$views$comparison$metric_row(metric){
var indicator_color = (function (){var G__30523 = new cljs.core.Keyword(null,"indicator","indicator",1928219637).cljs$core$IFn$_invoke$arity$1(metric);
switch (G__30523) {
case "regression":
return "#ef4444";

break;
case "improvement":
return "#22c55e";

break;
case "warning":
return "#f59e0b";

break;
default:
return "#64748b";

}
})();
var indicator_bg = (function (){var G__30524 = new cljs.core.Keyword(null,"indicator","indicator",1928219637).cljs$core$IFn$_invoke$arity$1(metric);
switch (G__30524) {
case "regression":
return "#fef2f2";

break;
case "improvement":
return "#f0fdf4";

break;
case "warning":
return "#fffbeb";

break;
default:
return "#f8fafc";

}
})();
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tr","tr",-1424774646),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #f1f5f9"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"color","color",1011675173),"#1e293b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"500"], null)], null),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(metric);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "-";
}
})()], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"color","color",1011675173),"#475569"], null)], null),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"baselineDisplay","baselineDisplay",-505640928).cljs$core$IFn$_invoke$arity$1(metric);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"baselineValue","baselineValue",-1581339168).cljs$core$IFn$_invoke$arity$1(metric)));
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return "-";
}
}
})()], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"color","color",1011675173),"#475569"], null)], null),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"targetDisplay","targetDisplay",-1074022164).cljs$core$IFn$_invoke$arity$1(metric);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"targetValue","targetValue",-1073717921).cljs$core$IFn$_invoke$arity$1(metric)));
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return "-";
}
}
})()], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"right"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"padding","padding",1660304693),"2px 8px",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"9999px",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"700",new cljs.core.Keyword(null,"color","color",1011675173),indicator_color,new cljs.core.Keyword(null,"background-color","background-color",570434026),indicator_bg,new cljs.core.Keyword(null,"border","border",1444987323),(""+"1px solid "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(indicator_color))], null)], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"deltaPercent","deltaPercent",-1195175464).cljs$core$IFn$_invoke$arity$1(metric);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "-";
}
})())+"%")], null)], null)], null);
});
frontend.views.comparison.comparison_category_tab = (function frontend$views$comparison$comparison_category_tab(category,metrics){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"0",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden"], null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"table","table",-564943036),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"border-collapse","border-collapse",919100239),"collapse"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"thead","thead",-291875296),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f8fafc",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tr","tr",-1424774646),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"th","th",-545608566),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"left",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600"], null)], null),"Metric"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"th","th",-545608566),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"left",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600"], null)], null),"Baseline"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"th","th",-545608566),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"left",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600"], null)], null),"Target"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"th","th",-545608566),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"right",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600"], null)], null),"Delta %"], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tbody","tbody",-80678300),(function (){var iter__5628__auto__ = (function frontend$views$comparison$comparison_category_tab_$_iter__30526(s__30527){
return (new cljs.core.LazySeq(null,(function (){
var s__30527__$1 = s__30527;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__30527__$1);
if(temp__5825__auto__){
var s__30527__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__30527__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__30527__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__30529 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__30528 = (0);
while(true){
if((i__30528 < size__5627__auto__)){
var m = cljs.core._nth(c__5626__auto__,i__30528);
cljs.core.chunk_append(b__30529,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.comparison.metric_row,m], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.hash(m);
}
})()], null)));

var G__30548 = (i__30528 + (1));
i__30528 = G__30548;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__30529),frontend$views$comparison$comparison_category_tab_$_iter__30526(cljs.core.chunk_rest(s__30527__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__30529),null);
}
} else {
var m = cljs.core.first(s__30527__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.comparison.metric_row,m], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.hash(m);
}
})()], null)),frontend$views$comparison$comparison_category_tab_$_iter__30526(cljs.core.rest(s__30527__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(metrics);
})()], null)], null)], null);
});
frontend.views.comparison.category_tab_bar = (function frontend$views$comparison$category_tab_bar(categories,active_tab,on_change){
return new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),68], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"4px",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0",new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"1.5rem"], null),new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.vec((function (){var iter__5628__auto__ = (function frontend$views$comparison$category_tab_bar_$_iter__30534(s__30535){
return (new cljs.core.LazySeq(null,(function (){
var s__30535__$1 = s__30535;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__30535__$1);
if(temp__5825__auto__){
var s__30535__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__30535__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__30535__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__30537 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__30536 = (0);
while(true){
if((i__30536 < size__5627__auto__)){
var cat = cljs.core._nth(c__5626__auto__,i__30536);
cljs.core.chunk_append(b__30537,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"10px 20px",new cljs.core.Keyword(null,"cursor","cursor",1011937484),"pointer",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"2px solid transparent",new cljs.core.Keyword(null,"transition","transition",765692007),"all 0.2s"], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(active_tab,cat))?new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"border-bottom-color","border-bottom-color",-208763333),"#3b82f6",new cljs.core.Keyword(null,"color","color",1011675173),"#2563eb",new cljs.core.Keyword(null,"background","background",-863952629),"#eff6ff"], null):new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null))], 0)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__30536,cat,c__5626__auto__,size__5627__auto__,b__30537,s__30535__$2,temp__5825__auto__){
return (function (){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(cat) : on_change.call(null,cat));
});})(i__30536,cat,c__5626__auto__,size__5627__auto__,b__30537,s__30535__$2,temp__5825__auto__))
], null),cat], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cat], null)));

var G__30549 = (i__30536 + (1));
i__30536 = G__30549;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__30537),frontend$views$comparison$category_tab_bar_$_iter__30534(cljs.core.chunk_rest(s__30535__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__30537),null);
}
} else {
var cat = cljs.core.first(s__30535__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"10px 20px",new cljs.core.Keyword(null,"cursor","cursor",1011937484),"pointer",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"2px solid transparent",new cljs.core.Keyword(null,"transition","transition",765692007),"all 0.2s"], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(active_tab,cat))?new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"border-bottom-color","border-bottom-color",-208763333),"#3b82f6",new cljs.core.Keyword(null,"color","color",1011675173),"#2563eb",new cljs.core.Keyword(null,"background","background",-863952629),"#eff6ff"], null):new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null))], 0)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (cat,s__30535__$2,temp__5825__auto__){
return (function (){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(cat) : on_change.call(null,cat));
});})(cat,s__30535__$2,temp__5825__auto__))
], null),cat], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cat], null)),frontend$views$comparison$category_tab_bar_$_iter__30534(cljs.core.rest(s__30535__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(categories);
})())], null);
});
frontend.views.comparison.comparison_view = (function frontend$views$comparison$comparison_view(){
var active_tab = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (function (){
var baseline_id = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","baseline-id","comparison/baseline-id",-2072500368)], null)));
var target_id = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","target-id","comparison/target-id",-656665914)], null)));
var result = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","result","comparison/result",-507401234)], null)));
var loading_QMARK_ = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","loading?","comparison/loading?",-1153311694)], null)));
var error = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","error","comparison/error",1926451741)], null)));
var categories = cljs.core.keys(new cljs.core.Keyword(null,"metrics","metrics",394093469).cljs$core$IFn$_invoke$arity$1(result));
if(cljs.core.truth_((function (){var and__5140__auto__ = result;
if(cljs.core.truth_(and__5140__auto__)){
return (((cljs.core.deref(active_tab) == null)) && (cljs.core.seq(categories)));
} else {
return and__5140__auto__;
}
})())){
cljs.core.reset_BANG_(active_tab,cljs.core.first(categories));
} else {
}

return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),97], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"2rem",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.page_header,"Recording Comparison","Analyze performance regressions and improvements between two JFR files"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f8fafc",new cljs.core.Keyword(null,"border-style","border-style",-485574304),"dashed"], null)], null),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),102], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"24px",new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"end","end",-268185958),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.comparison.recording_dropdown,"Baseline Recording",baseline_id,(function (p1__30539_SHARP_){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","select-baseline","comparison/select-baseline",1932181617),p1__30539_SHARP_], null));
})], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.comparison.recording_dropdown,"Target Recording",target_id,(function (p1__30540_SHARP_){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","select-target","comparison/select-target",-161747456),p1__30540_SHARP_], null));
})], null),new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.button,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),110], null)),new cljs.core.Keyword(null,"label","label",1718410804),"Run Comparison",new cljs.core.Keyword(null,"class","class",-2030961996),"btn-primary",new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),(function (){var or__5142__auto__ = loading_QMARK_;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (((baseline_id == null)) || ((target_id == null)));
}
})(),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","run","comparison/run",-1524236354)], null));
})], null)], null)], null)], null),(cljs.core.truth_(error)?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.alert_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),117], null)),new cljs.core.Keyword(null,"alert-type","alert-type",405751817),new cljs.core.Keyword(null,"danger","danger",-624338030),new cljs.core.Keyword(null,"body","body",-2049205669),error], null):null),(cljs.core.truth_(loading_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.loading_spinner,"Comparing recordings..."], null):null),(cljs.core.truth_(result)?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),124], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"1.5rem",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [((cljs.core.seq(new cljs.core.Keyword(null,"warnings","warnings",-735437651).cljs$core$IFn$_invoke$arity$1(result)))?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.alert_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),128], null)),new cljs.core.Keyword(null,"alert-type","alert-type",405751817),new cljs.core.Keyword(null,"warning","warning",-1685650671),new cljs.core.Keyword(null,"body","body",-2049205669),clojure.string.join.cljs$core$IFn$_invoke$arity$2("; ",new cljs.core.Keyword(null,"warnings","warnings",-735437651).cljs$core$IFn$_invoke$arity$1(result))], null):null),((cljs.core.seq(new cljs.core.Keyword(null,"summary","summary",380847952).cljs$core$IFn$_invoke$arity$1(result)))?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.alert_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/comparison.cljs",new cljs.core.Keyword(null,"line","line",212345235),133], null)),new cljs.core.Keyword(null,"alert-type","alert-type",405751817),new cljs.core.Keyword(null,"info","info",-317069002),new cljs.core.Keyword(null,"body","body",-2049205669),clojure.string.join.cljs$core$IFn$_invoke$arity$2("; ",new cljs.core.Keyword(null,"summary","summary",380847952).cljs$core$IFn$_invoke$arity$1(result))], null):null),((cljs.core.seq(categories))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.comparison.category_tab_bar,categories,cljs.core.deref(active_tab),(function (p1__30541_SHARP_){
return cljs.core.reset_BANG_(active_tab,p1__30541_SHARP_);
})], null):null),(cljs.core.truth_(cljs.core.deref(active_tab))?(function (){var metrics = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(result,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"metrics","metrics",394093469),cljs.core.deref(active_tab)], null));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.comparison.comparison_category_tab,cljs.core.deref(active_tab),metrics], null);
})():null)], null)], null):null)], null)], null);
});
});

//# sourceMappingURL=frontend.views.comparison.js.map
