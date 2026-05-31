goog.provide('shadow.remote.runtime.shared');
shadow.remote.runtime.shared.init_state = (function shadow$remote$runtime$shared$init_state(client_info){
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"extensions","extensions",-1103629196),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"ops","ops",1237330063),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"client-info","client-info",1958982504),client_info,new cljs.core.Keyword(null,"call-id-seq","call-id-seq",-1679248218),(0),new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),cljs.core.PersistentArrayMap.EMPTY], null);
});
shadow.remote.runtime.shared.now = (function shadow$remote$runtime$shared$now(){
return Date.now();
});
shadow.remote.runtime.shared.get_client_id = (function shadow$remote$runtime$shared$get_client_id(p__45708){
var map__45709 = p__45708;
var map__45709__$1 = cljs.core.__destructure_map(map__45709);
var runtime = map__45709__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45709__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var or__5142__auto__ = new cljs.core.Keyword(null,"client-id","client-id",-464622140).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("runtime has no assigned runtime-id",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"runtime","runtime",-1331573996),runtime], null));
}
});
shadow.remote.runtime.shared.relay_msg = (function shadow$remote$runtime$shared$relay_msg(runtime,msg){
var self_id_46000 = shadow.remote.runtime.shared.get_client_id(runtime);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"to","to",192099007).cljs$core$IFn$_invoke$arity$1(msg),self_id_46000)){
shadow.remote.runtime.api.relay_msg(runtime,msg);
} else {
Promise.resolve((1)).then((function (){
var G__45710 = runtime;
var G__45711 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"from","from",1815293044),self_id_46000);
return (shadow.remote.runtime.shared.process.cljs$core$IFn$_invoke$arity$2 ? shadow.remote.runtime.shared.process.cljs$core$IFn$_invoke$arity$2(G__45710,G__45711) : shadow.remote.runtime.shared.process.call(null,G__45710,G__45711));
}));
}

return msg;
});
shadow.remote.runtime.shared.reply = (function shadow$remote$runtime$shared$reply(runtime,p__45713,res){
var map__45714 = p__45713;
var map__45714__$1 = cljs.core.__destructure_map(map__45714);
var call_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45714__$1,new cljs.core.Keyword(null,"call-id","call-id",1043012968));
var from = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45714__$1,new cljs.core.Keyword(null,"from","from",1815293044));
var res__$1 = (function (){var G__45715 = res;
var G__45715__$1 = (cljs.core.truth_(call_id)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45715,new cljs.core.Keyword(null,"call-id","call-id",1043012968),call_id):G__45715);
if(cljs.core.truth_(from)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45715__$1,new cljs.core.Keyword(null,"to","to",192099007),from);
} else {
return G__45715__$1;
}
})();
return shadow.remote.runtime.api.relay_msg(runtime,res__$1);
});
shadow.remote.runtime.shared.call = (function shadow$remote$runtime$shared$call(var_args){
var G__45717 = arguments.length;
switch (G__45717) {
case 3:
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$3 = (function (runtime,msg,handlers){
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$4(runtime,msg,handlers,(0));
}));

(shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$4 = (function (p__45718,msg,handlers,timeout_after_ms){
var map__45719 = p__45718;
var map__45719__$1 = cljs.core.__destructure_map(map__45719);
var runtime = map__45719__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45719__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
if(cljs.core.map_QMARK_(msg)){
} else {
throw (new Error("Assert failed: (map? msg)"));
}

if(cljs.core.map_QMARK_(handlers)){
} else {
throw (new Error("Assert failed: (map? handlers)"));
}

if(cljs.core.nat_int_QMARK_(timeout_after_ms)){
} else {
throw (new Error("Assert failed: (nat-int? timeout-after-ms)"));
}

var call_id = new cljs.core.Keyword(null,"call-id-seq","call-id-seq",-1679248218).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(state_ref,cljs.core.update,new cljs.core.Keyword(null,"call-id-seq","call-id-seq",-1679248218),cljs.core.inc);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(state_ref,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),call_id], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"handlers","handlers",79528781),handlers,new cljs.core.Keyword(null,"called-at","called-at",607081160),shadow.remote.runtime.shared.now(),new cljs.core.Keyword(null,"msg","msg",-1386103444),msg,new cljs.core.Keyword(null,"timeout","timeout",-318625318),timeout_after_ms], null));

return shadow.remote.runtime.api.relay_msg(runtime,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"call-id","call-id",1043012968),call_id));
}));

(shadow.remote.runtime.shared.call.cljs$lang$maxFixedArity = 4);

shadow.remote.runtime.shared.trigger_BANG_ = (function shadow$remote$runtime$shared$trigger_BANG_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___46009 = arguments.length;
var i__5877__auto___46010 = (0);
while(true){
if((i__5877__auto___46010 < len__5876__auto___46009)){
args__5882__auto__.push((arguments[i__5877__auto___46010]));

var G__46011 = (i__5877__auto___46010 + (1));
i__5877__auto___46010 = G__46011;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return shadow.remote.runtime.shared.trigger_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(shadow.remote.runtime.shared.trigger_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (p__45734,ev,args){
var map__45735 = p__45734;
var map__45735__$1 = cljs.core.__destructure_map(map__45735);
var runtime = map__45735__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45735__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var seq__45737 = cljs.core.seq(cljs.core.vals(new cljs.core.Keyword(null,"extensions","extensions",-1103629196).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref))));
var chunk__45740 = null;
var count__45741 = (0);
var i__45742 = (0);
while(true){
if((i__45742 < count__45741)){
var ext = chunk__45740.cljs$core$IIndexed$_nth$arity$2(null,i__45742);
var ev_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(ext,ev);
if(cljs.core.truth_(ev_fn)){
cljs.core.apply.cljs$core$IFn$_invoke$arity$2(ev_fn,args);


var G__46014 = seq__45737;
var G__46015 = chunk__45740;
var G__46016 = count__45741;
var G__46017 = (i__45742 + (1));
seq__45737 = G__46014;
chunk__45740 = G__46015;
count__45741 = G__46016;
i__45742 = G__46017;
continue;
} else {
var G__46018 = seq__45737;
var G__46019 = chunk__45740;
var G__46020 = count__45741;
var G__46021 = (i__45742 + (1));
seq__45737 = G__46018;
chunk__45740 = G__46019;
count__45741 = G__46020;
i__45742 = G__46021;
continue;
}
} else {
var temp__5825__auto__ = cljs.core.seq(seq__45737);
if(temp__5825__auto__){
var seq__45737__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__45737__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__45737__$1);
var G__46023 = cljs.core.chunk_rest(seq__45737__$1);
var G__46024 = c__5673__auto__;
var G__46025 = cljs.core.count(c__5673__auto__);
var G__46026 = (0);
seq__45737 = G__46023;
chunk__45740 = G__46024;
count__45741 = G__46025;
i__45742 = G__46026;
continue;
} else {
var ext = cljs.core.first(seq__45737__$1);
var ev_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(ext,ev);
if(cljs.core.truth_(ev_fn)){
cljs.core.apply.cljs$core$IFn$_invoke$arity$2(ev_fn,args);


var G__46027 = cljs.core.next(seq__45737__$1);
var G__46028 = null;
var G__46029 = (0);
var G__46030 = (0);
seq__45737 = G__46027;
chunk__45740 = G__46028;
count__45741 = G__46029;
i__45742 = G__46030;
continue;
} else {
var G__46031 = cljs.core.next(seq__45737__$1);
var G__46032 = null;
var G__46033 = (0);
var G__46034 = (0);
seq__45737 = G__46031;
chunk__45740 = G__46032;
count__45741 = G__46033;
i__45742 = G__46034;
continue;
}
}
} else {
return null;
}
}
break;
}
}));

(shadow.remote.runtime.shared.trigger_BANG_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(shadow.remote.runtime.shared.trigger_BANG_.cljs$lang$applyTo = (function (seq45726){
var G__45727 = cljs.core.first(seq45726);
var seq45726__$1 = cljs.core.next(seq45726);
var G__45728 = cljs.core.first(seq45726__$1);
var seq45726__$2 = cljs.core.next(seq45726__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__45727,G__45728,seq45726__$2);
}));

shadow.remote.runtime.shared.welcome = (function shadow$remote$runtime$shared$welcome(p__45758,p__45759){
var map__45760 = p__45758;
var map__45760__$1 = cljs.core.__destructure_map(map__45760);
var runtime = map__45760__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45760__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var map__45761 = p__45759;
var map__45761__$1 = cljs.core.__destructure_map(map__45761);
var msg = map__45761__$1;
var client_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45761__$1,new cljs.core.Keyword(null,"client-id","client-id",-464622140));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(state_ref,cljs.core.assoc,new cljs.core.Keyword(null,"client-id","client-id",-464622140),client_id,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"welcome","welcome",-578152123),true], 0));

var map__45764 = cljs.core.deref(state_ref);
var map__45764__$1 = cljs.core.__destructure_map(map__45764);
var client_info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45764__$1,new cljs.core.Keyword(null,"client-info","client-info",1958982504));
var extensions = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45764__$1,new cljs.core.Keyword(null,"extensions","extensions",-1103629196));
shadow.remote.runtime.shared.relay_msg(runtime,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"hello","hello",-245025397),new cljs.core.Keyword(null,"client-info","client-info",1958982504),client_info], null));

return shadow.remote.runtime.shared.trigger_BANG_(runtime,new cljs.core.Keyword(null,"on-welcome","on-welcome",1895317125));
});
shadow.remote.runtime.shared.ping = (function shadow$remote$runtime$shared$ping(runtime,msg){
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"pong","pong",-172484958)], null));
});
shadow.remote.runtime.shared.request_supported_ops = (function shadow$remote$runtime$shared$request_supported_ops(p__45770,msg){
var map__45771 = p__45770;
var map__45771__$1 = cljs.core.__destructure_map(map__45771);
var runtime = map__45771__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45771__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"supported-ops","supported-ops",337914702),new cljs.core.Keyword(null,"ops","ops",1237330063),cljs.core.disj.cljs$core$IFn$_invoke$arity$variadic(cljs.core.set(cljs.core.keys(new cljs.core.Keyword(null,"ops","ops",1237330063).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref)))),new cljs.core.Keyword(null,"welcome","welcome",-578152123),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"unknown-relay-op","unknown-relay-op",170832753),new cljs.core.Keyword(null,"unknown-op","unknown-op",1900385996),new cljs.core.Keyword(null,"request-supported-ops","request-supported-ops",-1034994502),new cljs.core.Keyword(null,"tool-disconnect","tool-disconnect",189103996)], 0))], null));
});
shadow.remote.runtime.shared.unknown_relay_op = (function shadow$remote$runtime$shared$unknown_relay_op(msg){
return console.warn("unknown-relay-op",msg);
});
shadow.remote.runtime.shared.unknown_op = (function shadow$remote$runtime$shared$unknown_op(msg){
return console.warn("unknown-op",msg);
});
shadow.remote.runtime.shared.add_extension_STAR_ = (function shadow$remote$runtime$shared$add_extension_STAR_(p__45810,key,p__45811){
var map__45818 = p__45810;
var map__45818__$1 = cljs.core.__destructure_map(map__45818);
var state = map__45818__$1;
var extensions = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45818__$1,new cljs.core.Keyword(null,"extensions","extensions",-1103629196));
var map__45819 = p__45811;
var map__45819__$1 = cljs.core.__destructure_map(map__45819);
var spec = map__45819__$1;
var ops = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45819__$1,new cljs.core.Keyword(null,"ops","ops",1237330063));
var transit_write_handlers = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45819__$1,new cljs.core.Keyword(null,"transit-write-handlers","transit-write-handlers",1886308716));
if(cljs.core.contains_QMARK_(extensions,key)){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("extension already registered",new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"spec","spec",347520401),spec], null));
} else {
}

return cljs.core.reduce_kv((function (state__$1,op_kw,op_handler){
if(cljs.core.truth_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063),op_kw], null)))){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("op already registered",new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"op","op",-1882987955),op_kw], null));
} else {
}

return cljs.core.assoc_in(state__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063),op_kw], null),op_handler);
}),cljs.core.assoc_in(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"extensions","extensions",-1103629196),key], null),spec),ops);
});
shadow.remote.runtime.shared.add_extension = (function shadow$remote$runtime$shared$add_extension(p__45841,key,spec){
var map__45842 = p__45841;
var map__45842__$1 = cljs.core.__destructure_map(map__45842);
var runtime = map__45842__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45842__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(state_ref,shadow.remote.runtime.shared.add_extension_STAR_,key,spec);

var temp__5829__auto___46038 = new cljs.core.Keyword(null,"on-welcome","on-welcome",1895317125).cljs$core$IFn$_invoke$arity$1(spec);
if((temp__5829__auto___46038 == null)){
} else {
var on_welcome_46039 = temp__5829__auto___46038;
if(cljs.core.truth_(new cljs.core.Keyword(null,"welcome","welcome",-578152123).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref)))){
(on_welcome_46039.cljs$core$IFn$_invoke$arity$0 ? on_welcome_46039.cljs$core$IFn$_invoke$arity$0() : on_welcome_46039.call(null));
} else {
}
}

return runtime;
});
shadow.remote.runtime.shared.add_defaults = (function shadow$remote$runtime$shared$add_defaults(runtime){
return shadow.remote.runtime.shared.add_extension(runtime,new cljs.core.Keyword("shadow.remote.runtime.shared","defaults","shadow.remote.runtime.shared/defaults",-1821257543),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ops","ops",1237330063),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"welcome","welcome",-578152123),(function (p1__45843_SHARP_){
return shadow.remote.runtime.shared.welcome(runtime,p1__45843_SHARP_);
}),new cljs.core.Keyword(null,"unknown-relay-op","unknown-relay-op",170832753),(function (p1__45844_SHARP_){
return shadow.remote.runtime.shared.unknown_relay_op(p1__45844_SHARP_);
}),new cljs.core.Keyword(null,"unknown-op","unknown-op",1900385996),(function (p1__45845_SHARP_){
return shadow.remote.runtime.shared.unknown_op(p1__45845_SHARP_);
}),new cljs.core.Keyword(null,"ping","ping",-1670114784),(function (p1__45846_SHARP_){
return shadow.remote.runtime.shared.ping(runtime,p1__45846_SHARP_);
}),new cljs.core.Keyword(null,"request-supported-ops","request-supported-ops",-1034994502),(function (p1__45847_SHARP_){
return shadow.remote.runtime.shared.request_supported_ops(runtime,p1__45847_SHARP_);
})], null)], null));
});
shadow.remote.runtime.shared.del_extension_STAR_ = (function shadow$remote$runtime$shared$del_extension_STAR_(state,key){
var ext = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"extensions","extensions",-1103629196),key], null));
if(cljs.core.not(ext)){
return state;
} else {
return cljs.core.reduce_kv((function (state__$1,op_kw,op_handler){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(state__$1,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063)], null),cljs.core.dissoc,op_kw);
}),cljs.core.update.cljs$core$IFn$_invoke$arity$4(state,new cljs.core.Keyword(null,"extensions","extensions",-1103629196),cljs.core.dissoc,key),new cljs.core.Keyword(null,"ops","ops",1237330063).cljs$core$IFn$_invoke$arity$1(ext));
}
});
shadow.remote.runtime.shared.del_extension = (function shadow$remote$runtime$shared$del_extension(p__45897,key){
var map__45898 = p__45897;
var map__45898__$1 = cljs.core.__destructure_map(map__45898);
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45898__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(state_ref,shadow.remote.runtime.shared.del_extension_STAR_,key);
});
shadow.remote.runtime.shared.unhandled_call_result = (function shadow$remote$runtime$shared$unhandled_call_result(call_config,msg){
return console.warn("unhandled call result",msg,call_config);
});
shadow.remote.runtime.shared.unhandled_client_not_found = (function shadow$remote$runtime$shared$unhandled_client_not_found(p__45917,msg){
var map__45922 = p__45917;
var map__45922__$1 = cljs.core.__destructure_map(map__45922);
var runtime = map__45922__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45922__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
return shadow.remote.runtime.shared.trigger_BANG_.cljs$core$IFn$_invoke$arity$variadic(runtime,new cljs.core.Keyword(null,"on-client-not-found","on-client-not-found",-642452849),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([msg], 0));
});
shadow.remote.runtime.shared.reply_unknown_op = (function shadow$remote$runtime$shared$reply_unknown_op(runtime,msg){
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"unknown-op","unknown-op",1900385996),new cljs.core.Keyword(null,"msg","msg",-1386103444),msg], null));
});
shadow.remote.runtime.shared.process = (function shadow$remote$runtime$shared$process(p__45932,p__45933){
var map__45934 = p__45932;
var map__45934__$1 = cljs.core.__destructure_map(map__45934);
var runtime = map__45934__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45934__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var map__45935 = p__45933;
var map__45935__$1 = cljs.core.__destructure_map(map__45935);
var msg = map__45935__$1;
var op = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45935__$1,new cljs.core.Keyword(null,"op","op",-1882987955));
var call_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45935__$1,new cljs.core.Keyword(null,"call-id","call-id",1043012968));
var state = cljs.core.deref(state_ref);
var op_handler = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063),op], null));
if(cljs.core.truth_(call_id)){
var cfg = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),call_id], null));
var call_handler = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cfg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"handlers","handlers",79528781),op], null));
if(cljs.core.truth_(call_handler)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(state_ref,cljs.core.update,new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),cljs.core.dissoc,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([call_id], 0));

return (call_handler.cljs$core$IFn$_invoke$arity$1 ? call_handler.cljs$core$IFn$_invoke$arity$1(msg) : call_handler.call(null,msg));
} else {
if(cljs.core.truth_(op_handler)){
return (op_handler.cljs$core$IFn$_invoke$arity$1 ? op_handler.cljs$core$IFn$_invoke$arity$1(msg) : op_handler.call(null,msg));
} else {
return shadow.remote.runtime.shared.unhandled_call_result(cfg,msg);

}
}
} else {
if(cljs.core.truth_(op_handler)){
return (op_handler.cljs$core$IFn$_invoke$arity$1 ? op_handler.cljs$core$IFn$_invoke$arity$1(msg) : op_handler.call(null,msg));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-not-found","client-not-found",-1754042614),op)){
return shadow.remote.runtime.shared.unhandled_client_not_found(runtime,msg);
} else {
return shadow.remote.runtime.shared.reply_unknown_op(runtime,msg);

}
}
}
});
shadow.remote.runtime.shared.run_on_idle = (function shadow$remote$runtime$shared$run_on_idle(state_ref){
var seq__45944 = cljs.core.seq(cljs.core.vals(new cljs.core.Keyword(null,"extensions","extensions",-1103629196).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref))));
var chunk__45946 = null;
var count__45947 = (0);
var i__45948 = (0);
while(true){
if((i__45948 < count__45947)){
var map__45967 = chunk__45946.cljs$core$IIndexed$_nth$arity$2(null,i__45948);
var map__45967__$1 = cljs.core.__destructure_map(map__45967);
var on_idle = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45967__$1,new cljs.core.Keyword(null,"on-idle","on-idle",2044706602));
if(cljs.core.truth_(on_idle)){
(on_idle.cljs$core$IFn$_invoke$arity$0 ? on_idle.cljs$core$IFn$_invoke$arity$0() : on_idle.call(null));


var G__46049 = seq__45944;
var G__46050 = chunk__45946;
var G__46051 = count__45947;
var G__46052 = (i__45948 + (1));
seq__45944 = G__46049;
chunk__45946 = G__46050;
count__45947 = G__46051;
i__45948 = G__46052;
continue;
} else {
var G__46053 = seq__45944;
var G__46054 = chunk__45946;
var G__46055 = count__45947;
var G__46056 = (i__45948 + (1));
seq__45944 = G__46053;
chunk__45946 = G__46054;
count__45947 = G__46055;
i__45948 = G__46056;
continue;
}
} else {
var temp__5825__auto__ = cljs.core.seq(seq__45944);
if(temp__5825__auto__){
var seq__45944__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__45944__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__45944__$1);
var G__46057 = cljs.core.chunk_rest(seq__45944__$1);
var G__46058 = c__5673__auto__;
var G__46059 = cljs.core.count(c__5673__auto__);
var G__46060 = (0);
seq__45944 = G__46057;
chunk__45946 = G__46058;
count__45947 = G__46059;
i__45948 = G__46060;
continue;
} else {
var map__45980 = cljs.core.first(seq__45944__$1);
var map__45980__$1 = cljs.core.__destructure_map(map__45980);
var on_idle = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45980__$1,new cljs.core.Keyword(null,"on-idle","on-idle",2044706602));
if(cljs.core.truth_(on_idle)){
(on_idle.cljs$core$IFn$_invoke$arity$0 ? on_idle.cljs$core$IFn$_invoke$arity$0() : on_idle.call(null));


var G__46061 = cljs.core.next(seq__45944__$1);
var G__46062 = null;
var G__46063 = (0);
var G__46064 = (0);
seq__45944 = G__46061;
chunk__45946 = G__46062;
count__45947 = G__46063;
i__45948 = G__46064;
continue;
} else {
var G__46065 = cljs.core.next(seq__45944__$1);
var G__46066 = null;
var G__46067 = (0);
var G__46068 = (0);
seq__45944 = G__46065;
chunk__45946 = G__46066;
count__45947 = G__46067;
i__45948 = G__46068;
continue;
}
}
} else {
return null;
}
}
break;
}
});

//# sourceMappingURL=shadow.remote.runtime.shared.js.map
