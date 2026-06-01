goog.provide('frontend.views.job_monitor');
frontend.views.job_monitor.job_item = (function frontend$views$job_monitor$job_item(job_id,job){
var status = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(job);
var progress = (function (){var or__5142__auto__ = new cljs.core.Keyword(null,"progressPercent","progressPercent",-1571515311).cljs$core$IFn$_invoke$arity$1(job);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (0);
}
})();
var status_color = (function (){var G__29572 = status;
var G__29572__$1 = (((G__29572 instanceof cljs.core.Keyword))?G__29572.fqn:null);
switch (G__29572__$1) {
case "pending":
return "#f59e0b";

break;
case "running":
return "#3b82f6";

break;
case "completed":
return "#22c55e";

break;
case "failed":
return "#ef4444";

break;
default:
return "#64748b";

}
})();
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #f1f5f9",new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 0"], null)], null),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),21], null)),new cljs.core.Keyword(null,"justify","justify",-722524056),new cljs.core.Keyword(null,"between","between",1131099276),new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),25], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"2px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),28], null)),new cljs.core.Keyword(null,"label","label",1718410804),(""+"Job: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(job_id)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),32], null)),new cljs.core.Keyword(null,"label","label",1718410804),(""+"Recording: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564).cljs$core$IFn$_invoke$arity$1(job))),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"color","color",1011675173),"#94a3b8"], null)], null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"padding","padding",1660304693),"2px 8px",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"9999px",new cljs.core.Keyword(null,"background-color","background-color",570434026),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(status_color)+"15"),new cljs.core.Keyword(null,"color","color",1011675173),status_color,new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"700",new cljs.core.Keyword(null,"border","border",1444987323),(""+"1px solid "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(status_color)+"30")], null)], null),cljs.core.name(status)], null)], null)], null),(cljs.core.truth_((function (){var fexpr__29573 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pending","pending",-220036727),null,new cljs.core.Keyword(null,"running","running",1554969103),null], null), null);
return (fexpr__29573.cljs$core$IFn$_invoke$arity$1 ? fexpr__29573.cljs$core$IFn$_invoke$arity$1(status) : fexpr__29573.call(null,status));
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"margin-top","margin-top",392161226),"8px"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"height","height",1025178622),"6px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f1f5f9",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"9999px",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"height","height",1025178622),"100%",new cljs.core.Keyword(null,"width","width",-384071477),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(progress)+"%"),new cljs.core.Keyword(null,"background-color","background-color",570434026),"#3b82f6",new cljs.core.Keyword(null,"transition","transition",765692007),"width 0.3s ease-in-out"], null)], null)], null)], null)], null):null)], null);
});
frontend.views.job_monitor.job_monitor = (function frontend$views$job_monitor$job_monitor(){
var scheduled = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentHashSet.EMPTY);
return (function (){
var jobs = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("jobs","all","jobs/all",885192196)], null)));
var seq__29574_29613 = cljs.core.seq(jobs);
var chunk__29575_29614 = null;
var count__29576_29615 = (0);
var i__29577_29616 = (0);
while(true){
if((i__29577_29616 < count__29576_29615)){
var vec__29588_29617 = chunk__29575_29614.cljs$core$IIndexed$_nth$arity$2(null,i__29577_29616);
var id_29618 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29588_29617,(0),null);
var job_29619 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29588_29617,(1),null);
if(cljs.core.truth_((function (){var and__5140__auto__ = (function (){var G__29592 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(job_29619);
var fexpr__29591 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"completed","completed",-486056503),null,new cljs.core.Keyword(null,"failed","failed",-1397425762),null], null), null);
return (fexpr__29591.cljs$core$IFn$_invoke$arity$1 ? fexpr__29591.cljs$core$IFn$_invoke$arity$1(G__29592) : fexpr__29591.call(null,G__29592));
})();
if(cljs.core.truth_(and__5140__auto__)){
return (!(cljs.core.contains_QMARK_(cljs.core.deref(scheduled),id_29618)));
} else {
return and__5140__auto__;
}
})())){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(scheduled,cljs.core.conj,id_29618);

setTimeout(((function (seq__29574_29613,chunk__29575_29614,count__29576_29615,i__29577_29616,vec__29588_29617,id_29618,job_29619,jobs,scheduled){
return (function (){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("job","remove","job/remove",-131517307),id_29618], null));
});})(seq__29574_29613,chunk__29575_29614,count__29576_29615,i__29577_29616,vec__29588_29617,id_29618,job_29619,jobs,scheduled))
,(10000));
} else {
}


var G__29620 = seq__29574_29613;
var G__29621 = chunk__29575_29614;
var G__29622 = count__29576_29615;
var G__29623 = (i__29577_29616 + (1));
seq__29574_29613 = G__29620;
chunk__29575_29614 = G__29621;
count__29576_29615 = G__29622;
i__29577_29616 = G__29623;
continue;
} else {
var temp__5825__auto___29624 = cljs.core.seq(seq__29574_29613);
if(temp__5825__auto___29624){
var seq__29574_29625__$1 = temp__5825__auto___29624;
if(cljs.core.chunked_seq_QMARK_(seq__29574_29625__$1)){
var c__5673__auto___29626 = cljs.core.chunk_first(seq__29574_29625__$1);
var G__29627 = cljs.core.chunk_rest(seq__29574_29625__$1);
var G__29628 = c__5673__auto___29626;
var G__29629 = cljs.core.count(c__5673__auto___29626);
var G__29630 = (0);
seq__29574_29613 = G__29627;
chunk__29575_29614 = G__29628;
count__29576_29615 = G__29629;
i__29577_29616 = G__29630;
continue;
} else {
var vec__29593_29631 = cljs.core.first(seq__29574_29625__$1);
var id_29632 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29593_29631,(0),null);
var job_29633 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29593_29631,(1),null);
if(cljs.core.truth_((function (){var and__5140__auto__ = (function (){var G__29597 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(job_29633);
var fexpr__29596 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"completed","completed",-486056503),null,new cljs.core.Keyword(null,"failed","failed",-1397425762),null], null), null);
return (fexpr__29596.cljs$core$IFn$_invoke$arity$1 ? fexpr__29596.cljs$core$IFn$_invoke$arity$1(G__29597) : fexpr__29596.call(null,G__29597));
})();
if(cljs.core.truth_(and__5140__auto__)){
return (!(cljs.core.contains_QMARK_(cljs.core.deref(scheduled),id_29632)));
} else {
return and__5140__auto__;
}
})())){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(scheduled,cljs.core.conj,id_29632);

setTimeout(((function (seq__29574_29613,chunk__29575_29614,count__29576_29615,i__29577_29616,vec__29593_29631,id_29632,job_29633,seq__29574_29625__$1,temp__5825__auto___29624,jobs,scheduled){
return (function (){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("job","remove","job/remove",-131517307),id_29632], null));
});})(seq__29574_29613,chunk__29575_29614,count__29576_29615,i__29577_29616,vec__29593_29631,id_29632,job_29633,seq__29574_29625__$1,temp__5825__auto___29624,jobs,scheduled))
,(10000));
} else {
}


var G__29634 = cljs.core.next(seq__29574_29625__$1);
var G__29635 = null;
var G__29636 = (0);
var G__29637 = (0);
seq__29574_29613 = G__29634;
chunk__29575_29614 = G__29635;
count__29576_29615 = G__29636;
i__29577_29616 = G__29637;
continue;
}
} else {
}
}
break;
}

if(cljs.core.seq(jobs)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"box-shadow","box-shadow",1600206755),new cljs.core.Keyword(null,"max-height","max-height",-612563804),new cljs.core.Keyword(null,"bottom","bottom",-1550509018),new cljs.core.Keyword(null,"width","width",-384071477),new cljs.core.Keyword(null,"z-index","z-index",1892827090),new cljs.core.Keyword(null,"padding","padding",1660304693),new cljs.core.Keyword(null,"right","right",-452581833),new cljs.core.Keyword(null,"position","position",-2011731912),new cljs.core.Keyword(null,"overflow-y","overflow-y",-1436589285)],["0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)","400px","1.5rem","360px",(9998),"1rem","1.5rem","fixed","auto"])], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),76], null)),new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),79], null)),new cljs.core.Keyword(null,"justify","justify",-722524056),new cljs.core.Keyword(null,"between","between",1131099276),new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0",new cljs.core.Keyword(null,"padding-bottom","padding-bottom",-1899795591),"8px",new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"4px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),86], null)),new cljs.core.Keyword(null,"label","label",1718410804),"ACTIVE JOBS",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"700",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"letter-spacing","letter-spacing",-948993767),"0.05em"], null)], null),new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.md_icon_button,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/job_monitor.cljs",new cljs.core.Keyword(null,"line","line",212345235),90], null)),new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863),"zmdi-close",new cljs.core.Keyword(null,"size","size",1098693007),new cljs.core.Keyword(null,"smaller","smaller",-1619801498),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
var seq__29598 = cljs.core.seq(cljs.core.keys(jobs));
var chunk__29599 = null;
var count__29600 = (0);
var i__29601 = (0);
while(true){
if((i__29601 < count__29600)){
var id = chunk__29599.cljs$core$IIndexed$_nth$arity$2(null,i__29601);
re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("job","remove","job/remove",-131517307),id], null));


var G__29638 = seq__29598;
var G__29639 = chunk__29599;
var G__29640 = count__29600;
var G__29641 = (i__29601 + (1));
seq__29598 = G__29638;
chunk__29599 = G__29639;
count__29600 = G__29640;
i__29601 = G__29641;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__29598);
if(temp__5825__auto__){
var seq__29598__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__29598__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__29598__$1);
var G__29642 = cljs.core.chunk_rest(seq__29598__$1);
var G__29643 = c__5673__auto__;
var G__29644 = cljs.core.count(c__5673__auto__);
var G__29645 = (0);
seq__29598 = G__29642;
chunk__29599 = G__29643;
count__29600 = G__29644;
i__29601 = G__29645;
continue;
} else {
var id = cljs.core.first(seq__29598__$1);
re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("job","remove","job/remove",-131517307),id], null));


var G__29646 = cljs.core.next(seq__29598__$1);
var G__29647 = null;
var G__29648 = (0);
var G__29649 = (0);
seq__29598 = G__29646;
chunk__29599 = G__29647;
count__29600 = G__29648;
i__29601 = G__29649;
continue;
}
} else {
return null;
}
}
break;
}
}),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#94a3b8"], null)], null)], null)], null)], null),(function (){var iter__5628__auto__ = (function frontend$views$job_monitor$job_monitor_$_iter__29602(s__29603){
return (new cljs.core.LazySeq(null,(function (){
var s__29603__$1 = s__29603;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__29603__$1);
if(temp__5825__auto__){
var s__29603__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__29603__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29603__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29605 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29604 = (0);
while(true){
if((i__29604 < size__5627__auto__)){
var vec__29606 = cljs.core._nth(c__5626__auto__,i__29604);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29606,(0),null);
var job = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29606,(1),null);
cljs.core.chunk_append(b__29605,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.job_monitor.job_item,id,job], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),id], null)));

var G__29650 = (i__29604 + (1));
i__29604 = G__29650;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29605),frontend$views$job_monitor$job_monitor_$_iter__29602(cljs.core.chunk_rest(s__29603__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29605),null);
}
} else {
var vec__29609 = cljs.core.first(s__29603__$2);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29609,(0),null);
var job = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29609,(1),null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.job_monitor.job_item,id,job], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),id], null)),frontend$views$job_monitor$job_monitor_$_iter__29602(cljs.core.rest(s__29603__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(jobs);
})()))], null)], null);
} else {
return null;
}
});
});

//# sourceMappingURL=frontend.views.job_monitor.js.map
