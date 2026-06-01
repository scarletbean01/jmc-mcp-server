goog.provide('jmc_mcp.views.comparison');
jmc_mcp.views.comparison.comparison_header = (function jmc_mcp$views$comparison$comparison_header(){
var recordings = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("recordings","items","recordings/items",-1991597412)], null));
var baseline_id = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var target_id = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (function (){
return new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"gap","gap",80255254),"16px",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"20px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Baseline Recording",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.single_dropdown,new cljs.core.Keyword(null,"choices","choices",1385611597),(function (){var iter__5628__auto__ = (function jmc_mcp$views$comparison$comparison_header_$_iter__18907(s__18908){
return (new cljs.core.LazySeq(null,(function (){
var s__18908__$1 = s__18908;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18908__$1);
if(temp__5825__auto__){
var s__18908__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18908__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18908__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18910 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18909 = (0);
while(true){
if((i__18909 < size__5627__auto__)){
var r = cljs.core._nth(c__5626__auto__,i__18909);
cljs.core.chunk_append(b__18910,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"filename","filename",-1428840783).cljs$core$IFn$_invoke$arity$1(r)], null));

var G__18973 = (i__18909 + (1));
i__18909 = G__18973;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18910),jmc_mcp$views$comparison$comparison_header_$_iter__18907(cljs.core.chunk_rest(s__18908__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18910),null);
}
} else {
var r = cljs.core.first(s__18908__$2);
return cljs.core.cons(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"filename","filename",-1428840783).cljs$core$IFn$_invoke$arity$1(r)], null),jmc_mcp$views$comparison$comparison_header_$_iter__18907(cljs.core.rest(s__18908__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.deref(recordings));
})(),new cljs.core.Keyword(null,"model","model",331153215),baseline_id,new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__18904_SHARP_){
return cljs.core.reset_BANG_(baseline_id,p1__18904_SHARP_);
}),new cljs.core.Keyword(null,"width","width",-384071477),"300px",new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Select baseline"], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"i","i",-1386841315),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"zmdi zmdi-arrow-right",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"24px",new cljs.core.Keyword(null,"color","color",1011675173),"#94a3b8"], null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Target Recording",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null),new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.single_dropdown,new cljs.core.Keyword(null,"choices","choices",1385611597),(function (){var iter__5628__auto__ = (function jmc_mcp$views$comparison$comparison_header_$_iter__18917(s__18918){
return (new cljs.core.LazySeq(null,(function (){
var s__18918__$1 = s__18918;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18918__$1);
if(temp__5825__auto__){
var s__18918__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18918__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18918__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18920 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18919 = (0);
while(true){
if((i__18919 < size__5627__auto__)){
var r = cljs.core._nth(c__5626__auto__,i__18919);
cljs.core.chunk_append(b__18920,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"filename","filename",-1428840783).cljs$core$IFn$_invoke$arity$1(r)], null));

var G__18981 = (i__18919 + (1));
i__18919 = G__18981;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18920),jmc_mcp$views$comparison$comparison_header_$_iter__18917(cljs.core.chunk_rest(s__18918__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18920),null);
}
} else {
var r = cljs.core.first(s__18918__$2);
return cljs.core.cons(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"filename","filename",-1428840783).cljs$core$IFn$_invoke$arity$1(r)], null),jmc_mcp$views$comparison$comparison_header_$_iter__18917(cljs.core.rest(s__18918__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.deref(recordings));
})(),new cljs.core.Keyword(null,"model","model",331153215),target_id,new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__18906_SHARP_){
return cljs.core.reset_BANG_(target_id,p1__18906_SHARP_);
}),new cljs.core.Keyword(null,"width","width",-384071477),"300px",new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Select target"], null)], null)], null),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.button,new cljs.core.Keyword(null,"label","label",1718410804),"Compare",new cljs.core.Keyword(null,"class","class",-2030961996),"btn-primary",new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),cljs.core.not((function (){var and__5140__auto__ = cljs.core.deref(baseline_id);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.deref(target_id);
} else {
return and__5140__auto__;
}
})()),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","run","comparison/run",-1524236354),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"baselineRecordingId","baselineRecordingId",-751663510),cljs.core.deref(baseline_id),new cljs.core.Keyword(null,"comparisonRecordingId","comparisonRecordingId",490442984),cljs.core.deref(target_id),new cljs.core.Keyword(null,"analysisType","analysisType",244961091),"overview"], null)], null));
})], null)], null)], null);
});
});
jmc_mcp.views.comparison.metric_row = (function jmc_mcp$views$comparison$metric_row(metric){
var delta = new cljs.core.Keyword(null,"deltaPercent","deltaPercent",-1195175464).cljs$core$IFn$_invoke$arity$1(metric);
var color = (((delta == null))?"#64748b":(((delta > (10)))?"#ef4444":(((delta < (-10)))?"#10b981":"#64748b"
)));
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #f1f5f9"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(metric),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"250px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"baselineDisplay","baselineDisplay",-505640928).cljs$core$IFn$_invoke$arity$1(metric),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"150px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"targetDisplay","targetDisplay",-1074022164).cljs$core$IFn$_invoke$arity$1(metric),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"150px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(cljs.core.truth_(delta)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(delta)+"%"):"-"),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"width","width",-384071477),"100px",new cljs.core.Keyword(null,"color","color",1011675173),color,new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null);
});
jmc_mcp.views.comparison.comparison_results = (function jmc_mcp$views$comparison$comparison_results(result){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"gap","gap",80255254),"24px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"20px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Summary",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"12px"], null)], null),(function (){var iter__5628__auto__ = (function jmc_mcp$views$comparison$comparison_results_$_iter__18925(s__18926){
return (new cljs.core.LazySeq(null,(function (){
var s__18926__$1 = s__18926;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18926__$1);
if(temp__5825__auto__){
var s__18926__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18926__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18926__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18928 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18927 = (0);
while(true){
if((i__18927 < size__5627__auto__)){
var s = cljs.core._nth(c__5626__auto__,i__18927);
cljs.core.chunk_append(b__18928,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+"\u2022 "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"4px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),s], null)));

var G__18992 = (i__18927 + (1));
i__18927 = G__18992;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18928),jmc_mcp$views$comparison$comparison_results_$_iter__18925(cljs.core.chunk_rest(s__18926__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18928),null);
}
} else {
var s = cljs.core.first(s__18926__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),(""+"\u2022 "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"4px"], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),s], null)),jmc_mcp$views$comparison$comparison_results_$_iter__18925(cljs.core.rest(s__18926__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(new cljs.core.Keyword(null,"summary","summary",380847952).cljs$core$IFn$_invoke$arity$1(result));
})()], null)], null),(function (){var iter__5628__auto__ = (function jmc_mcp$views$comparison$comparison_results_$_iter__18933(s__18934){
return (new cljs.core.LazySeq(null,(function (){
var s__18934__$1 = s__18934;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18934__$1);
if(temp__5825__auto__){
var s__18934__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18934__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18934__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18936 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18935 = (0);
while(true){
if((i__18935 < size__5627__auto__)){
var vec__18941 = cljs.core._nth(c__5626__auto__,i__18935);
var cat = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18941,(0),null);
var metrics = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18941,(1),null);
cljs.core.chunk_append(b__18936,cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f8fafc",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(cat),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"250px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Baseline",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"150px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Target",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"150px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Delta",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"100px"], null)], null)], null)], null),(function (){var iter__5628__auto__ = ((function (i__18935,vec__18941,cat,metrics,c__5626__auto__,size__5627__auto__,b__18936,s__18934__$2,temp__5825__auto__){
return (function jmc_mcp$views$comparison$comparison_results_$_iter__18933_$_iter__18945(s__18946){
return (new cljs.core.LazySeq(null,((function (i__18935,vec__18941,cat,metrics,c__5626__auto__,size__5627__auto__,b__18936,s__18934__$2,temp__5825__auto__){
return (function (){
var s__18946__$1 = s__18946;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__18946__$1);
if(temp__5825__auto____$1){
var s__18946__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__18946__$2)){
var c__5626__auto____$1 = cljs.core.chunk_first(s__18946__$2);
var size__5627__auto____$1 = cljs.core.count(c__5626__auto____$1);
var b__18948 = cljs.core.chunk_buffer(size__5627__auto____$1);
if((function (){var i__18947 = (0);
while(true){
if((i__18947 < size__5627__auto____$1)){
var m = cljs.core._nth(c__5626__auto____$1,i__18947);
cljs.core.chunk_append(b__18948,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.comparison.metric_row,m], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(m)], null)));

var G__18997 = (i__18947 + (1));
i__18947 = G__18997;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18948),jmc_mcp$views$comparison$comparison_results_$_iter__18933_$_iter__18945(cljs.core.chunk_rest(s__18946__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18948),null);
}
} else {
var m = cljs.core.first(s__18946__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.comparison.metric_row,m], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(m)], null)),jmc_mcp$views$comparison$comparison_results_$_iter__18933_$_iter__18945(cljs.core.rest(s__18946__$2)));
}
} else {
return null;
}
break;
}
});})(i__18935,vec__18941,cat,metrics,c__5626__auto__,size__5627__auto__,b__18936,s__18934__$2,temp__5825__auto__))
,null,null));
});})(i__18935,vec__18941,cat,metrics,c__5626__auto__,size__5627__auto__,b__18936,s__18934__$2,temp__5825__auto__))
;
return iter__5628__auto__(metrics);
})()], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cat], null)));

var G__19002 = (i__18935 + (1));
i__18935 = G__19002;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18936),jmc_mcp$views$comparison$comparison_results_$_iter__18933(cljs.core.chunk_rest(s__18934__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18936),null);
}
} else {
var vec__18949 = cljs.core.first(s__18934__$2);
var cat = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18949,(0),null);
var metrics = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18949,(1),null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid #e2e8f0",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"8px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f8fafc",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),cljs.core.name(cat),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"250px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Baseline",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"150px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Target",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"150px"], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Delta",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700),new cljs.core.Keyword(null,"width","width",-384071477),"100px"], null)], null)], null)], null),(function (){var iter__5628__auto__ = ((function (vec__18949,cat,metrics,s__18934__$2,temp__5825__auto__){
return (function jmc_mcp$views$comparison$comparison_results_$_iter__18933_$_iter__18956(s__18957){
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
var m = cljs.core._nth(c__5626__auto__,i__18958);
cljs.core.chunk_append(b__18959,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.comparison.metric_row,m], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(m)], null)));

var G__19003 = (i__18958 + (1));
i__18958 = G__19003;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18959),jmc_mcp$views$comparison$comparison_results_$_iter__18933_$_iter__18956(cljs.core.chunk_rest(s__18957__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18959),null);
}
} else {
var m = cljs.core.first(s__18957__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.comparison.metric_row,m], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(m)], null)),jmc_mcp$views$comparison$comparison_results_$_iter__18933_$_iter__18956(cljs.core.rest(s__18957__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});})(vec__18949,cat,metrics,s__18934__$2,temp__5825__auto__))
;
return iter__5628__auto__(metrics);
})()], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cat], null)),jmc_mcp$views$comparison$comparison_results_$_iter__18933(cljs.core.rest(s__18934__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(new cljs.core.Keyword(null,"metrics","metrics",394093469).cljs$core$IFn$_invoke$arity$1(result));
})()], null)], null);
});
jmc_mcp.views.comparison.comparison_page = (function jmc_mcp$views$comparison$comparison_page(){
var result = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","result","comparison/result",-507401234)], null));
var loading_QMARK_ = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","loading?","comparison/loading?",-1153311694)], null));
return (function (){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"gap","gap",80255254),"24px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Recording Comparison",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"24px",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),(700)], null)], null)], null)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.comparison.comparison_header], null),(cljs.core.truth_(cljs.core.deref(loading_QMARK_))?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"padding","padding",1660304693),"100px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.components.spinner], null)], null)], null):(cljs.core.truth_(cljs.core.deref(result))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.comparison.comparison_results,cljs.core.deref(result)], null):new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"padding","padding",1660304693),"100px"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"label","label",1718410804),"Select recordings and click Compare"], null)], null)], null)))], null)], null);
});
});

//# sourceMappingURL=jmc_mcp.views.comparison.js.map
