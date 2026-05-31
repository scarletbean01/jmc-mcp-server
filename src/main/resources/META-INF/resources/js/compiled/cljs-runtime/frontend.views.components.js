goog.provide('frontend.views.components');
frontend.views.components.md_icon = (function frontend$views$components$md_icon(var_args){
var args__5882__auto__ = [];
var len__5876__auto___29420 = arguments.length;
var i__5877__auto___29421 = (0);
while(true){
if((i__5877__auto___29421 < len__5876__auto___29420)){
args__5882__auto__.push((arguments[i__5877__auto___29421]));

var G__29422 = (i__5877__auto___29421 + (1));
i__5877__auto___29421 = G__29422;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return frontend.views.components.md_icon.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(frontend.views.components.md_icon.cljs$core$IFn$_invoke$arity$variadic = (function (args){
var map__29358 = ((cljs.core.map_QMARK_(cljs.core.first(args)))?cljs.core.first(args):cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,args));
var map__29358__$1 = cljs.core.__destructure_map(map__29358);
var md_icon_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29358__$1,new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863));
var size = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29358__$1,new cljs.core.Keyword(null,"size","size",1098693007));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29358__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29358__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var color = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29358__$1,new cljs.core.Keyword(null,"color","color",1011675173));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"i","i",-1386841315),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"zmdi "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(md_icon_name)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(class$)?class$:null))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_(color)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),color], null):null),(cljs.core.truth_(size)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),(function (){var G__29359 = size;
var G__29359__$1 = (((G__29359 instanceof cljs.core.Keyword))?G__29359.fqn:null);
switch (G__29359__$1) {
case "smaller":
return "12px";

break;
case "small":
return "14px";

break;
case "regular":
return "18px";

break;
case "large":
return "24px";

break;
case "larger":
return "32px";

break;
default:
return size;

}
})()], null):null),style], 0))], null)], null);
}));

(frontend.views.components.md_icon.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(frontend.views.components.md_icon.cljs$lang$applyTo = (function (seq29357){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq29357));
}));

frontend.views.components.notification_toast = (function frontend$views$components$notification_toast(p__29360){
var map__29361 = p__29360;
var map__29361__$1 = cljs.core.__destructure_map(map__29361);
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29361__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29361__$1,new cljs.core.Keyword(null,"type","type",1174270348));
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29361__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var alert_type = (function (){var G__29362 = type;
var G__29362__$1 = (((G__29362 instanceof cljs.core.Keyword))?G__29362.fqn:null);
switch (G__29362__$1) {
case "success":
return new cljs.core.Keyword(null,"success","success",1890645906);

break;
case "error":
return new cljs.core.Keyword(null,"danger","danger",-624338030);

break;
case "warning":
return new cljs.core.Keyword(null,"warning","warning",-1685650671);

break;
case "info":
return new cljs.core.Keyword(null,"info","info",-317069002);

break;
default:
return new cljs.core.Keyword(null,"info","info",-317069002);

}
})();
return new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.alert_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),32], null)),new cljs.core.Keyword(null,"alert-type","alert-type",405751817),alert_type,new cljs.core.Keyword(null,"body","body",-2049205669),message,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"8px",new cljs.core.Keyword(null,"box-shadow","box-shadow",1600206755),"0 4px 6px -1px rgba(0,0,0,0.1)"], null),new cljs.core.Keyword(null,"closeable?","closeable?",1490064409),true,new cljs.core.Keyword(null,"on-close","on-close",-761178394),(function (){
return re_frame.core.dispatch(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("notification","remove","notification/remove",464922843),id], null));
})], null);
});
frontend.views.components.notifications_panel = (function frontend$views$components$notifications_panel(){
var notifications = cljs.core.deref(re_frame.core.subscribe.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"notifications","notifications",1685638001)], null)));
if(cljs.core.seq(notifications)){
return new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),43], null)),new cljs.core.Keyword(null,"class","class",-2030961996),"notification-container",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"position","position",-2011731912),"fixed",new cljs.core.Keyword(null,"top","top",-1856271961),"1.5rem",new cljs.core.Keyword(null,"right","right",-452581833),"1.5rem",new cljs.core.Keyword(null,"z-index","z-index",1892827090),(9999),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),"400px"], null),new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.vec((function (){var iter__5628__auto__ = (function frontend$views$components$notifications_panel_$_iter__29363(s__29364){
return (new cljs.core.LazySeq(null,(function (){
var s__29364__$1 = s__29364;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__29364__$1);
if(temp__5825__auto__){
var s__29364__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__29364__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29364__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29366 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29365 = (0);
while(true){
if((i__29365 < size__5627__auto__)){
var n = cljs.core._nth(c__5626__auto__,i__29365);
cljs.core.chunk_append(b__29366,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.notification_toast,n], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n)], null)));

var G__29432 = (i__29365 + (1));
i__29365 = G__29432;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29366),frontend$views$components$notifications_panel_$_iter__29363(cljs.core.chunk_rest(s__29364__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29366),null);
}
} else {
var n = cljs.core.first(s__29364__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.notification_toast,n], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n)], null)),frontend$views$components$notifications_panel_$_iter__29363(cljs.core.rest(s__29364__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(notifications);
})())], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632)], null);
}
});
frontend.views.components.loading_spinner = (function frontend$views$components$loading_spinner(var_args){
var G__29368 = arguments.length;
switch (G__29368) {
case 0:
return frontend.views.components.loading_spinner.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return frontend.views.components.loading_spinner.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(frontend.views.components.loading_spinner.cljs$core$IFn$_invoke$arity$0 = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.loading_spinner,"Loading..."], null);
}));

(frontend.views.components.loading_spinner.cljs$core$IFn$_invoke$arity$1 = (function (text){
return new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),59], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"12px",new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"justify","justify",-722524056),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"padding","padding",1660304693),"2rem"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.throbber,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),65], null)),new cljs.core.Keyword(null,"size","size",1098693007),new cljs.core.Keyword(null,"regular","regular",-1153375582)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),68], null)),new cljs.core.Keyword(null,"label","label",1718410804),text,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"500",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null)], null)], null);
}));

(frontend.views.components.loading_spinner.cljs$lang$maxFixedArity = 1);

frontend.views.components.file_size_str = (function frontend$views$components$file_size_str(bytes){
if((bytes == null)){
return "Unknown";
} else {
if((bytes >= (((1024) * (1024)) * (1024)))){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((((bytes / (1024)) / (1024)) / (1024)).toFixed((2)))+" GB");
} else {
if((bytes >= ((1024) * (1024)))){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(((bytes / (1024)) / (1024)).toFixed((2)))+" MB");
} else {
if((bytes >= (1024))){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((bytes / (1024)).toFixed((2)))+" KB");
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(bytes)+" B");

}
}
}
}
});
frontend.views.components.empty_state = (function frontend$views$components$empty_state(var_args){
var args__5882__auto__ = [];
var len__5876__auto___29442 = arguments.length;
var i__5877__auto___29443 = (0);
while(true){
if((i__5877__auto___29443 < len__5876__auto___29442)){
args__5882__auto__.push((arguments[i__5877__auto___29443]));

var G__29444 = (i__5877__auto___29443 + (1));
i__5877__auto___29443 = G__29444;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return frontend.views.components.empty_state.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(frontend.views.components.empty_state.cljs$core$IFn$_invoke$arity$variadic = (function (message,p__29371){
var vec__29372 = p__29371;
var action = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29372,(0),null);
return new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),83], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"16px",new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"justify","justify",-722524056),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"4rem 2rem",new cljs.core.Keyword(null,"background","background",-863952629),"#fff",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"12px",new cljs.core.Keyword(null,"border","border",1444987323),"1px dashed #e2e8f0"], null),new cljs.core.Keyword(null,"children","children",-940561982),(function (){var G__29375 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.md_icon,new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863),"zmdi-inbox",new cljs.core.Keyword(null,"size","size",1098693007),new cljs.core.Keyword(null,"larger","larger",1304935444),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#cbd5e1"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),93], null)),new cljs.core.Keyword(null,"label","label",1718410804),message,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"1.1rem"], null)], null)], null);
if(cljs.core.truth_(action)){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(G__29375,action);
} else {
return G__29375;
}
})()], null);
}));

(frontend.views.components.empty_state.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(frontend.views.components.empty_state.cljs$lang$applyTo = (function (seq29369){
var G__29370 = cljs.core.first(seq29369);
var seq29369__$1 = cljs.core.next(seq29369);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__29370,seq29369__$1);
}));

frontend.views.components.page_header = (function frontend$views$components$page_header(var_args){
var args__5882__auto__ = [];
var len__5876__auto___29450 = arguments.length;
var i__5877__auto___29451 = (0);
while(true){
if((i__5877__auto___29451 < len__5876__auto___29450)){
args__5882__auto__.push((arguments[i__5877__auto___29451]));

var G__29453 = (i__5877__auto___29451 + (1));
i__5877__auto___29451 = G__29453;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return frontend.views.components.page_header.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(frontend.views.components.page_header.cljs$core$IFn$_invoke$arity$variadic = (function (title,p__29378){
var vec__29379 = p__29378;
var subtitle = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29379,(0),null);
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),101], null)),new cljs.core.Keyword(null,"class","class",-2030961996),"page-header",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.title,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),104], null)),new cljs.core.Keyword(null,"label","label",1718410804),title,new cljs.core.Keyword(null,"level","level",1290497552),new cljs.core.Keyword(null,"level2","level2",-2044031830),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"margin","margin",-995903681),"0",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"700",new cljs.core.Keyword(null,"color","color",1011675173),"#0f172a"], null)], null),(cljs.core.truth_(subtitle)?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),110], null)),new cljs.core.Keyword(null,"label","label",1718410804),subtitle,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"1rem",new cljs.core.Keyword(null,"margin-top","margin-top",392161226),"4px"], null)], null):null)], null)], null);
}));

(frontend.views.components.page_header.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(frontend.views.components.page_header.cljs$lang$applyTo = (function (seq29376){
var G__29377 = cljs.core.first(seq29376);
var seq29376__$1 = cljs.core.next(seq29376);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__29377,seq29376__$1);
}));

frontend.views.components.metrics_renderer = (function frontend$views$components$metrics_renderer(data){
var items = ((cljs.core.map_QMARK_(data))?cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__29382){
var vec__29383 = p__29382;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29383,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29383,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),cljs.core.name(k),new cljs.core.Keyword(null,"value","value",305978217),v], null);
}),data):((cljs.core.sequential_QMARK_(data))?data:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),"Result",new cljs.core.Keyword(null,"value","value",305978217),data], null)], null)
));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"display","display",242065432),"grid",new cljs.core.Keyword(null,"grid-template-columns","grid-template-columns",-594112133),"repeat(auto-fill, minmax(240px, 1fr))",new cljs.core.Keyword(null,"gap","gap",80255254),"1rem"], null)], null),(function (){var iter__5628__auto__ = (function frontend$views$components$metrics_renderer_$_iter__29386(s__29387){
return (new cljs.core.LazySeq(null,(function (){
var s__29387__$1 = s__29387;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__29387__$1);
if(temp__5825__auto__){
var s__29387__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__29387__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29387__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29389 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29388 = (0);
while(true){
if((i__29388 < size__5627__auto__)){
var item = cljs.core._nth(c__5626__auto__,i__29388);
cljs.core.chunk_append(b__29389,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"1rem",new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"0"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),128], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"4px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),131], null)),new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item);
}
})())),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"500"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),135], null)),new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"val","val",128701612).cljs$core$IFn$_invoke$arity$1(item);
}
})())),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"1.25rem",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"color","color",1011675173),"#0f172a"], null)], null)], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(item));
}
})()], null)));

var G__29486 = (i__29388 + (1));
i__29388 = G__29486;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29389),frontend$views$components$metrics_renderer_$_iter__29386(cljs.core.chunk_rest(s__29387__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29389),null);
}
} else {
var item = cljs.core.first(s__29387__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"1rem",new cljs.core.Keyword(null,"margin-bottom","margin-bottom",388334941),"0"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),128], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"4px",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),131], null)),new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item);
}
})())),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"500"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),135], null)),new cljs.core.Keyword(null,"label","label",1718410804),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"val","val",128701612).cljs$core$IFn$_invoke$arity$1(item);
}
})())),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"1.25rem",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"color","color",1011675173),"#0f172a"], null)], null)], null)], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(item));
}
})()], null)),frontend$views$components$metrics_renderer_$_iter__29386(cljs.core.rest(s__29387__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(items);
})()], null);
});
frontend.views.components.table_renderer = (function frontend$views$components$table_renderer(data){
var rows = ((cljs.core.sequential_QMARK_(data))?data:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [data], null));
if(cljs.core.empty_QMARK_(rows)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.empty_state,"No data available"], null);
} else {
var cols = cljs.core.keys(cljs.core.first(rows));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"padding","padding",1660304693),"0",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden"], null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"table","table",-564943036),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"border-collapse","border-collapse",919100239),"collapse"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"thead","thead",-291875296),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f8fafc",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #e2e8f0"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tr","tr",-1424774646),(function (){var iter__5628__auto__ = (function frontend$views$components$table_renderer_$_iter__29390(s__29391){
return (new cljs.core.LazySeq(null,(function (){
var s__29391__$1 = s__29391;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__29391__$1);
if(temp__5825__auto__){
var s__29391__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__29391__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29391__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29393 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29392 = (0);
while(true){
if((i__29392 < size__5627__auto__)){
var c = cljs.core._nth(c__5626__auto__,i__29392);
cljs.core.chunk_append(b__29393,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"th","th",-545608566),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"left",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600"], null)], null),cljs.core.name(c)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),c], null)));

var G__29492 = (i__29392 + (1));
i__29392 = G__29492;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29393),frontend$views$components$table_renderer_$_iter__29390(cljs.core.chunk_rest(s__29391__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29393),null);
}
} else {
var c = cljs.core.first(s__29391__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"th","th",-545608566),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"left",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.75rem",new cljs.core.Keyword(null,"text-transform","text-transform",1685000676),"uppercase",new cljs.core.Keyword(null,"color","color",1011675173),"#64748b",new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600"], null)], null),cljs.core.name(c)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),c], null)),frontend$views$components$table_renderer_$_iter__29390(cljs.core.rest(s__29391__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cols);
})()], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tbody","tbody",-80678300),(function (){var iter__5628__auto__ = (function frontend$views$components$table_renderer_$_iter__29394(s__29395){
return (new cljs.core.LazySeq(null,(function (){
var s__29395__$1 = s__29395;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__29395__$1);
if(temp__5825__auto__){
var s__29395__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__29395__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29395__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29397 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29396 = (0);
while(true){
if((i__29396 < size__5627__auto__)){
var vec__29398 = cljs.core._nth(c__5626__auto__,i__29396);
var idx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29398,(0),null);
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29398,(1),null);
cljs.core.chunk_append(b__29397,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tr","tr",-1424774646),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(idx,(cljs.core.count(rows) - (1))))?null:"1px solid #f1f5f9"),new cljs.core.Keyword(null,"background-color","background-color",570434026),((cljs.core.odd_QMARK_(idx))?"#fafafa":null)], null)], null),(function (){var iter__5628__auto__ = ((function (i__29396,vec__29398,idx,row,c__5626__auto__,size__5627__auto__,b__29397,s__29395__$2,temp__5825__auto__,cols,rows){
return (function frontend$views$components$table_renderer_$_iter__29394_$_iter__29401(s__29402){
return (new cljs.core.LazySeq(null,((function (i__29396,vec__29398,idx,row,c__5626__auto__,size__5627__auto__,b__29397,s__29395__$2,temp__5825__auto__,cols,rows){
return (function (){
var s__29402__$1 = s__29402;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__29402__$1);
if(temp__5825__auto____$1){
var s__29402__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__29402__$2)){
var c__5626__auto____$1 = cljs.core.chunk_first(s__29402__$2);
var size__5627__auto____$1 = cljs.core.count(c__5626__auto____$1);
var b__29404 = cljs.core.chunk_buffer(size__5627__auto____$1);
if((function (){var i__29403 = (0);
while(true){
if((i__29403 < size__5627__auto____$1)){
var c = cljs.core._nth(c__5626__auto____$1,i__29403);
cljs.core.chunk_append(b__29404,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"color","color",1011675173),"#334155",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem"], null)], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,c)))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),c], null)));

var G__29504 = (i__29403 + (1));
i__29403 = G__29504;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29404),frontend$views$components$table_renderer_$_iter__29394_$_iter__29401(cljs.core.chunk_rest(s__29402__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29404),null);
}
} else {
var c = cljs.core.first(s__29402__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"color","color",1011675173),"#334155",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem"], null)], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,c)))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),c], null)),frontend$views$components$table_renderer_$_iter__29394_$_iter__29401(cljs.core.rest(s__29402__$2)));
}
} else {
return null;
}
break;
}
});})(i__29396,vec__29398,idx,row,c__5626__auto__,size__5627__auto__,b__29397,s__29395__$2,temp__5825__auto__,cols,rows))
,null,null));
});})(i__29396,vec__29398,idx,row,c__5626__auto__,size__5627__auto__,b__29397,s__29395__$2,temp__5825__auto__,cols,rows))
;
return iter__5628__auto__(cols);
})()], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cljs.core.hash(row)], null)));

var G__29508 = (i__29396 + (1));
i__29396 = G__29508;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29397),frontend$views$components$table_renderer_$_iter__29394(cljs.core.chunk_rest(s__29395__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29397),null);
}
} else {
var vec__29405 = cljs.core.first(s__29395__$2);
var idx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29405,(0),null);
var row = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29405,(1),null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tr","tr",-1424774646),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(idx,(cljs.core.count(rows) - (1))))?null:"1px solid #f1f5f9"),new cljs.core.Keyword(null,"background-color","background-color",570434026),((cljs.core.odd_QMARK_(idx))?"#fafafa":null)], null)], null),(function (){var iter__5628__auto__ = ((function (vec__29405,idx,row,s__29395__$2,temp__5825__auto__,cols,rows){
return (function frontend$views$components$table_renderer_$_iter__29394_$_iter__29408(s__29409){
return (new cljs.core.LazySeq(null,(function (){
var s__29409__$1 = s__29409;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__29409__$1);
if(temp__5825__auto____$1){
var s__29409__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__29409__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29409__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29411 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29410 = (0);
while(true){
if((i__29410 < size__5627__auto__)){
var c = cljs.core._nth(c__5626__auto__,i__29410);
cljs.core.chunk_append(b__29411,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"color","color",1011675173),"#334155",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem"], null)], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,c)))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),c], null)));

var G__29519 = (i__29410 + (1));
i__29410 = G__29519;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29411),frontend$views$components$table_renderer_$_iter__29394_$_iter__29408(cljs.core.chunk_rest(s__29409__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29411),null);
}
} else {
var c = cljs.core.first(s__29409__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"td","td",1479933353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"12px 16px",new cljs.core.Keyword(null,"color","color",1011675173),"#334155",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem"], null)], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(row,c)))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),c], null)),frontend$views$components$table_renderer_$_iter__29394_$_iter__29408(cljs.core.rest(s__29409__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});})(vec__29405,idx,row,s__29395__$2,temp__5825__auto__,cols,rows))
;
return iter__5628__auto__(cols);
})()], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cljs.core.hash(row)], null)),frontend$views$components$table_renderer_$_iter__29394(cljs.core.rest(s__29395__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2(cljs.core.vector,rows));
})()], null)], null)], null);
}
});
frontend.views.components.tree_node = (function frontend$views$components$tree_node(node,level){
var expanded_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(true);
return (function (node__$1,level__$1){
var label = (function (){var or__5142__auto__ = new cljs.core.Keyword(null,"methodName","methodName",-2055958885).cljs$core$IFn$_invoke$arity$1(node__$1);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(node__$1);
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
var or__5142__auto____$2 = new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(node__$1);
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(node__$1));
}
}
}
})();
var children = cljs.core.seq((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"children","children",-940561982).cljs$core$IFn$_invoke$arity$1(node__$1);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"childNodes","childNodes",1292700019).cljs$core$IFn$_invoke$arity$1(node__$1);
}
})());
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),168], null)),new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),171], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"8px",new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding-left","padding-left",-1180879053),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((level__$1 * (20)))+"px"),new cljs.core.Keyword(null,"padding","padding",1660304693),"6px 0",new cljs.core.Keyword(null,"border-bottom","border-bottom",2110948415),"1px solid #f8fafc"], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [((children)?new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.md_icon_button,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),179], null)),new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863),(cljs.core.truth_(cljs.core.deref(expanded_QMARK_))?"zmdi-chevron-down":"zmdi-chevron-right"),new cljs.core.Keyword(null,"size","size",1098693007),new cljs.core.Keyword(null,"smaller","smaller",-1619801498),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(expanded_QMARK_,cljs.core.not);
}),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"0",new cljs.core.Keyword(null,"width","width",-384071477),"20px",new cljs.core.Keyword(null,"height","height",1025178622),"20px"], null)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),"20px"], null)], null)], null)),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),186], null)),new cljs.core.Keyword(null,"label","label",1718410804),label,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"0.875rem",new cljs.core.Keyword(null,"color","color",1011675173),"#334155"], null)], null)], null)], null)], null),(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.deref(expanded_QMARK_);
if(cljs.core.truth_(and__5140__auto__)){
return children;
} else {
return and__5140__auto__;
}
})())?(function (){var iter__5628__auto__ = (function frontend$views$components$tree_node_$_iter__29412(s__29413){
return (new cljs.core.LazySeq(null,(function (){
var s__29413__$1 = s__29413;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__29413__$1);
if(temp__5825__auto__){
var s__29413__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__29413__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29413__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29415 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29414 = (0);
while(true){
if((i__29414 < size__5627__auto__)){
var child = cljs.core._nth(c__5626__auto__,i__29414);
cljs.core.chunk_append(b__29415,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.tree_node,child,(level__$1 + (1))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cljs.core.hash(child)], null)));

var G__29534 = (i__29414 + (1));
i__29414 = G__29534;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29415),frontend$views$components$tree_node_$_iter__29412(cljs.core.chunk_rest(s__29413__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29415),null);
}
} else {
var child = cljs.core.first(s__29413__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.tree_node,child,(level__$1 + (1))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cljs.core.hash(child)], null)),frontend$views$components$tree_node_$_iter__29412(cljs.core.rest(s__29413__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(children);
})():null)))], null);
});
});
frontend.views.components.tree_renderer = (function frontend$views$components$tree_renderer(data){
var roots = ((cljs.core.sequential_QMARK_(data))?data:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [data], null));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"padding","padding",1660304693),"1rem"], null)], null),(function (){var iter__5628__auto__ = (function frontend$views$components$tree_renderer_$_iter__29416(s__29417){
return (new cljs.core.LazySeq(null,(function (){
var s__29417__$1 = s__29417;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__29417__$1);
if(temp__5825__auto__){
var s__29417__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__29417__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__29417__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__29419 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__29418 = (0);
while(true){
if((i__29418 < size__5627__auto__)){
var root = cljs.core._nth(c__5626__auto__,i__29418);
cljs.core.chunk_append(b__29419,cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.tree_node,root,(0)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cljs.core.hash(root)], null)));

var G__29554 = (i__29418 + (1));
i__29418 = G__29554;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__29419),frontend$views$components$tree_renderer_$_iter__29416(cljs.core.chunk_rest(s__29417__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__29419),null);
}
} else {
var root = cljs.core.first(s__29417__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.tree_node,root,(0)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),cljs.core.hash(root)], null)),frontend$views$components$tree_renderer_$_iter__29416(cljs.core.rest(s__29417__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(roots);
})()], null);
});
frontend.views.components.flame_graph_placeholder = (function frontend$views$components$flame_graph_placeholder(data){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"3rem",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"center",new cljs.core.Keyword(null,"background","background",-863952629),"#f8fafc",new cljs.core.Keyword(null,"border","border",1444987323),"2px dashed #e2e8f0"], null)], null),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),204], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"12px",new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.md_icon,new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863),"zmdi-fire",new cljs.core.Keyword(null,"size","size",1098693007),new cljs.core.Keyword(null,"larger","larger",1304935444),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#ef4444"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),212], null)),new cljs.core.Keyword(null,"label","label",1718410804),"Flame Graph Visualization",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"1.1rem"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),216], null)),new cljs.core.Keyword(null,"label","label",1718410804),"(Placeholder \u2014 D3 integration in Phase 7)",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null)], null)], null)], null);
});
frontend.views.components.timeline_placeholder = (function frontend$views$components$timeline_placeholder(data){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"card",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"padding","padding",1660304693),"3rem",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"center",new cljs.core.Keyword(null,"background","background",-863952629),"#f8fafc",new cljs.core.Keyword(null,"border","border",1444987323),"2px dashed #e2e8f0"], null)], null),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),223], null)),new cljs.core.Keyword(null,"gap","gap",80255254),"12px",new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [frontend.views.components.md_icon,new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863),"zmdi-chart",new cljs.core.Keyword(null,"size","size",1098693007),new cljs.core.Keyword(null,"larger","larger",1304935444),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#3b82f6"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),231], null)),new cljs.core.Keyword(null,"label","label",1718410804),"Timeline Visualization",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"600",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"1.1rem"], null)], null),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.core.label,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"frontend/views/components.cljs",new cljs.core.Keyword(null,"line","line",212345235),235], null)),new cljs.core.Keyword(null,"label","label",1718410804),"(Placeholder \u2014 chart library in Phase 7)",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#64748b"], null)], null)], null)], null)], null);
});

//# sourceMappingURL=frontend.views.components.js.map
