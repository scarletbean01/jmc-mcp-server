goog.provide('jmc_mcp.views.job_monitor');
jmc_mcp.views.job_monitor.job_item = (function jmc_mcp$views$job_monitor$job_item(job_id,job){
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"8px 12px",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #f1f5f9",new cljs.core.Keyword(null,"gap","gap",80255254),"12px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"size","size",1098693007),"1",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+"Job: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(new cljs.core.Keyword(null,"analysisType","analysisType",244961091).cljs$core$IFn$_invoke$arity$1(job)))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(600)], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+"Status: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(job))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px"], null)], null)], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.progress_bar,new cljs.core.Keyword(null,"model","model",331153215),new cljs.core.Keyword(null,"progressPercent","progressPercent",-1571515311).cljs$core$IFn$_invoke$arity$1(job),new cljs.core.Keyword(null,"width","width",-384071477),"100px"], null)], null)], null);
});
jmc_mcp.views.job_monitor.job_monitor = (function jmc_mcp$views$job_monitor$job_monitor(){
var jobs = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("jobs","all","jobs/all",885192196)], null));
return (function (){
if(cljs.core.seq(cljs.core.deref(jobs))){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"box-shadow","box-shadow",1600206755),new cljs.core.Keyword(null,"bottom","bottom",-1550509018),new cljs.core.Keyword(null,"background-color","background-color",570434026),new cljs.core.Keyword(null,"width","width",-384071477),new cljs.core.Keyword(null,"z-index","z-index",1892827090),new cljs.core.Keyword(null,"right","right",-452581833),new cljs.core.Keyword(null,"position","position",-2011731912),new cljs.core.Keyword(null,"border","border",1444987323),new cljs.core.Keyword(null,"border-radius","border-radius",419594011)],["0 4px 6px -1px rgb(0 0 0 / 0.1)","20px","white","300px",(100),"20px","fixed","1px solid #e2e8f0","8px"]),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0",new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f8fafc"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Active Jobs",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = (function jmc_mcp$views$job_monitor$job_monitor_$_iter__19014(s__19015){
return (new cljs.core.LazySeq(null,(function (){
var s__19015__$1 = s__19015;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__19015__$1);
if(temp__5825__auto__){
var s__19015__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__19015__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__19015__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__19019 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__19017 = (0);
while(true){
if((i__19017 < size__5627__auto__)){
var vec__19020 = cljs.core._nth(c__5626__auto__,i__19017);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19020,(0),null);
var job = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19020,(1),null);
cljs.core.chunk_append(b__19019,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.job_monitor.job_item,id,job], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),id], null)));

var G__19031 = (i__19017 + (1));
i__19017 = G__19031;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__19019),jmc_mcp$views$job_monitor$job_monitor_$_iter__19014(cljs.core.chunk_rest(s__19015__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__19019),null);
}
} else {
var vec__19023 = cljs.core.first(s__19015__$2);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19023,(0),null);
var job = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19023,(1),null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.job_monitor.job_item,id,job], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),id], null)),jmc_mcp$views$job_monitor$job_monitor_$_iter__19014(cljs.core.rest(s__19015__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.deref(jobs));
})()], null)], null)], null);
} else {
return null;
}
});
});

//# sourceMappingURL=jmc_mcp.views.job_monitor.js.map
