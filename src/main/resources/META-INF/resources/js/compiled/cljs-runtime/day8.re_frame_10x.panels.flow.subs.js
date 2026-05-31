goog.provide('day8.re_frame_10x.panels.flow.subs');
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","root","day8.re-frame-10x.panels.flow.subs/root",490034020),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"->","->",514830339),new cljs.core.Keyword(null,"flow","flow",590489032)], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","all-flows","day8.re-frame-10x.panels.flow.subs/all-flows",-771764824),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.traces.subs","filtered-by-epoch-always","day8.re-frame-10x.panels.traces.subs/filtered-by-epoch-always",-1807060768)], null),(function (p1__19597_SHARP_){
return cljs.core.filterv(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"flow","flow",590489032),null], null), null),new cljs.core.Keyword(null,"op-type","op-type",-1636141668)),p1__19597_SHARP_);
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","filter-str","day8.re-frame-10x.panels.flow.subs/filter-str",515716936),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","root","day8.re-frame-10x.panels.flow.subs/root",490034020)], null),new cljs.core.Keyword(null,"->","->",514830339),new cljs.core.Keyword(null,"filter-str","filter-str",1974484789)], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","flow-pins","day8.re-frame-10x.panels.flow.subs/flow-pins",1019720070),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.constantly(null)], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","visible-flows","day8.re-frame-10x.panels.flow.subs/visible-flows",-1313828595),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","all-flows","day8.re-frame-10x.panels.flow.subs/all-flows",-771764824)], null),new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","filter-str","day8.re-frame-10x.panels.flow.subs/filter-str",515716936)], null),new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","flow-pins","day8.re-frame-10x.panels.flow.subs/flow-pins",1019720070)], null),(function (p__19609){
var vec__19610 = p__19609;
var all_subs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19610,(0),null);
var filter_str = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19610,(1),null);
var pins = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19610,(2),null);
var compare_fn = (function (s1,s2){
var p1 = cljs.core.boolean$(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(pins,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(s1),new cljs.core.Keyword(null,"pin?","pin?",-1347894609)], null)));
var p2 = cljs.core.boolean$(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(pins,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(s2),new cljs.core.Keyword(null,"pin?","pin?",-1347894609)], null)));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1,p2)){
return cljs.core.compare(new cljs.core.Keyword(null,"path","path",-188191168).cljs$core$IFn$_invoke$arity$1(s1),new cljs.core.Keyword(null,"path","path",-188191168).cljs$core$IFn$_invoke$arity$1(s2));
} else {
return p1;
}
});
var G__19617 = all_subs;
var G__19617__$1 = cljs.core.sort.cljs$core$IFn$_invoke$arity$2(compare_fn,G__19617)
;
if(cljs.core.truth_(cljs.core.not_empty(filter_str))){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p__19618){
var map__19629 = p__19618;
var map__19629__$1 = cljs.core.__destructure_map(map__19629);
var operation = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19629__$1,new cljs.core.Keyword(null,"operation","operation",-1267664310));
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__19629__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var or__5142__auto__ = clojure.string.includes_QMARK_((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(operation)),filter_str);
if(or__5142__auto__){
return or__5142__auto__;
} else {
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(pins,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"pin?","pin?",-1347894609)], null));
}
}),G__19617__$1);
} else {
return G__19617__$1;
}
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","visible-flows-by-id","day8.re-frame-10x.panels.flow.subs/visible-flows-by-id",-2082785561),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","visible-flows","day8.re-frame-10x.panels.flow.subs/visible-flows",-1313828595)], null),new cljs.core.Keyword(null,"->","->",514830339),(function (flows){
return cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092),flows),flows);
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","inputs-diff","day8.re-frame-10x.panels.flow.subs/inputs-diff",-1442155837),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","visible-flows-by-id","day8.re-frame-10x.panels.flow.subs/visible-flows-by-id",-2082785561)], null),(function (flows,p__19641){
var vec__19642 = p__19641;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19642,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19642,(1),null);
var old = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(flows,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"id->old-in","id->old-in",-504402935)], null));
var new$ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(flows,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"id->in","id->in",-693826300)], null));
return clojure.data.diff(old,new$);
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","live-inputs-diff","day8.re-frame-10x.panels.flow.subs/live-inputs-diff",-1537038622),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.flow.subs","visible-flows-by-id","day8.re-frame-10x.panels.flow.subs/visible-flows-by-id",-2082785561)], null),(function (flows,p__19645){
var vec__19649 = p__19645;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19649,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19649,(1),null);
var old = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(flows,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"id->old-live-in","id->old-live-in",992766074)], null));
var new$ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(flows,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"id->live-in","id->live-in",998532194)], null));
return clojure.data.diff(old,new$);
})], 0));

//# sourceMappingURL=day8.re_frame_10x.panels.flow.subs.js.map
