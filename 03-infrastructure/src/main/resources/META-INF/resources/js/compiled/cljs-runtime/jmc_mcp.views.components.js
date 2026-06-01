goog.provide('jmc_mcp.views.components');
jmc_mcp.views.components.notification_toast = (function jmc_mcp$views$components$notification_toast(notification){
return new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.alert_box,new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(notification),new cljs.core.Keyword(null,"alert-type","alert-type",405751817),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(notification),new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(notification),new cljs.core.Keyword(null,"closeable?","closeable?",1490064409),true,new cljs.core.Keyword(null,"on-close","on-close",-761178394),(function (){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("notification","remove","notification/remove",464922843),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(notification)], null));
})], null);
});
jmc_mcp.views.components.notification_stack = (function jmc_mcp$views$components$notification_stack(){
var notifications = re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"notifications","notifications",1685638001)], null));
return (function (){
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"class","class",-2030961996),"notification-stack",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"position","position",-2011731912),"fixed",new cljs.core.Keyword(null,"top","top",-1856271961),"20px",new cljs.core.Keyword(null,"right","right",-452581833),"20px",new cljs.core.Keyword(null,"z-index","z-index",1892827090),(1000),new cljs.core.Keyword(null,"gap","gap",80255254),"10px"], null),new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = (function jmc_mcp$views$components$notification_stack_$_iter__18900(s__18901){
return (new cljs.core.LazySeq(null,(function (){
var s__18901__$1 = s__18901;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18901__$1);
if(temp__5825__auto__){
var s__18901__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18901__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18901__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18903 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18902 = (0);
while(true){
if((i__18902 < size__5627__auto__)){
var n = cljs.core._nth(c__5626__auto__,i__18902);
cljs.core.chunk_append(b__18903,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.components.notification_toast,n], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n)], null)));

var G__18905 = (i__18902 + (1));
i__18902 = G__18905;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18903),jmc_mcp$views$components$notification_stack_$_iter__18900(cljs.core.chunk_rest(s__18901__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18903),null);
}
} else {
var n = cljs.core.first(s__18901__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.components.notification_toast,n], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n)], null)),jmc_mcp$views$components$notification_stack_$_iter__18900(cljs.core.rest(s__18901__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.deref(notifications));
})()], null);
});
});
jmc_mcp.views.components.spinner = (function jmc_mcp$views$components$spinner(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"i","i",-1386841315),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"zmdi zmdi-refresh zmdi-hc-spin",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"24px"], null)], null)], null);
});

//# sourceMappingURL=jmc_mcp.views.components.js.map
