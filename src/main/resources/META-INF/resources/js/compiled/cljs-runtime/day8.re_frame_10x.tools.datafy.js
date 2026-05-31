goog.provide('day8.re_frame_10x.tools.datafy');
day8.re_frame_10x.tools.datafy.keyboard_event = (function day8$re_frame_10x$tools$datafy$keyboard_event(e){
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"key","key",-1516042587),e.key,new cljs.core.Keyword(null,"altKey","altKey",-670253656),e.altKey,new cljs.core.Keyword(null,"ctrlKey","ctrlKey",430760908),e.ctrlKey,new cljs.core.Keyword(null,"metaKey","metaKey",1006742180),e.metaKey,new cljs.core.Keyword(null,"shiftKey","shiftKey",1716666823),e.shiftKey], null);
});
day8.re_frame_10x.tools.datafy.mod_key__GT_str = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"metaKey","metaKey",1006742180),"Meta",new cljs.core.Keyword(null,"ctrlKey","ctrlKey",430760908),"Ctrl",new cljs.core.Keyword(null,"altKey","altKey",-670253656),"Alt",new cljs.core.Keyword(null,"shiftKey","shiftKey",1716666823),"Shift"], null);
day8.re_frame_10x.tools.datafy.mod_key__GT_order = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"metaKey","metaKey",1006742180),(1),new cljs.core.Keyword(null,"ctrlKey","ctrlKey",430760908),(2),new cljs.core.Keyword(null,"altKey","altKey",-670253656),(3),new cljs.core.Keyword(null,"shiftKey","shiftKey",1716666823),(4)], null);
day8.re_frame_10x.tools.datafy.keyboard_event__GT_str = (function day8$re_frame_10x$tools$datafy$keyboard_event__GT_str(v){
var map__23510 = (function (){var G__23511 = v;
if((!(cljs.core.map_QMARK_(v)))){
return day8.re_frame_10x.tools.datafy.keyboard_event(G__23511);
} else {
return G__23511;
}
})();
var map__23510__$1 = cljs.core.__destructure_map(map__23510);
var m = map__23510__$1;
var key_str = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23510__$1,new cljs.core.Keyword(null,"key","key",-1516042587));
var mods = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(day8.re_frame_10x.tools.datafy.mod_key__GT_str,cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(day8.re_frame_10x.tools.datafy.mod_key__GT_order,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.key,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.true_QMARK_,cljs.core.val),m))));
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("-",cljs.core.conj.cljs$core$IFn$_invoke$arity$2(mods,key_str));
});
day8.re_frame_10x.tools.datafy.deep_sorted_map = (function day8$re_frame_10x$tools$datafy$deep_sorted_map(m){
return clojure.walk.postwalk((function (p1__23512_SHARP_){
var G__23515 = p1__23512_SHARP_;
if(cljs.core.map_QMARK_(p1__23512_SHARP_)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.sorted_map_by(arrangement.core.rank),G__23515);
} else {
return G__23515;
}
}),m);
});
day8.re_frame_10x.tools.datafy.alias = (function day8$re_frame_10x$tools$datafy$alias(k,ns__GT_alias){
var temp__5823__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(ns__GT_alias,cljs.core.namespace(k));
if(cljs.core.truth_(temp__5823__auto__)){
var a = temp__5823__auto__;
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$2((""+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(a)),cljs.core.name(k));
} else {
return k;
}
});
day8.re_frame_10x.tools.datafy.alias_namespaces = (function day8$re_frame_10x$tools$datafy$alias_namespaces(m,ns__GT_alias){
return clojure.walk.postwalk((function (p1__23521_SHARP_){
var G__23523 = p1__23521_SHARP_;
if((p1__23521_SHARP_ instanceof cljs.core.Keyword)){
return day8.re_frame_10x.tools.datafy.alias(G__23523,ns__GT_alias);
} else {
return G__23523;
}
}),m);
});
day8.re_frame_10x.tools.datafy.pr_str_safe = (function day8$re_frame_10x$tools$datafy$pr_str_safe(value){
return cljs.core.pr_str_with_opts(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [value], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"flush-on-newline","flush-on-newline",-151457939),true,new cljs.core.Keyword(null,"readably","readably",1129599760),true,new cljs.core.Keyword(null,"meta","meta",1499536964),false,new cljs.core.Keyword(null,"print-length","print-length",1931866356),null], null));
});

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
day8.re_frame_10x.tools.datafy.WrappedSortedMap = (function (data,__meta,__extmap,__hash){
this.data = data;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__5448__auto__,k__5449__auto__){
var self__ = this;
var this__5448__auto____$1 = this;
return this__5448__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__5449__auto__,null);
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__5450__auto__,k23528,else__5451__auto__){
var self__ = this;
var this__5450__auto____$1 = this;
var G__23542 = k23528;
var G__23542__$1 = (((G__23542 instanceof cljs.core.Keyword))?G__23542.fqn:null);
switch (G__23542__$1) {
case "data":
return self__.data;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k23528,else__5451__auto__);

}
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__5468__auto__,f__5469__auto__,init__5470__auto__){
var self__ = this;
var this__5468__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__5471__auto__,p__23547){
var vec__23548 = p__23547;
var k__5472__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23548,(0),null);
var v__5473__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23548,(1),null);
return (f__5469__auto__.cljs$core$IFn$_invoke$arity$3 ? f__5469__auto__.cljs$core$IFn$_invoke$arity$3(ret__5471__auto__,k__5472__auto__,v__5473__auto__) : f__5469__auto__.call(null,ret__5471__auto__,k__5472__auto__,v__5473__auto__));
}),init__5470__auto__,this__5468__auto____$1);
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__5463__auto__,writer__5464__auto__,opts__5465__auto__){
var self__ = this;
var this__5463__auto____$1 = this;
var pr_pair__5466__auto__ = (function (keyval__5467__auto__){
return cljs.core.pr_sequential_writer(writer__5464__auto__,cljs.core.pr_writer,""," ","",opts__5465__auto__,keyval__5467__auto__);
});
return cljs.core.pr_sequential_writer(writer__5464__auto__,pr_pair__5466__auto__,"#day8.re-frame-10x.tools.datafy.WrappedSortedMap{",", ","}",opts__5465__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"data","data",-232669377),self__.data],null))], null),self__.__extmap));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__23527){
var self__ = this;
var G__23527__$1 = this;
return (new cljs.core.RecordIter((0),G__23527__$1,1,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"data","data",-232669377)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__5446__auto__){
var self__ = this;
var this__5446__auto____$1 = this;
return self__.__meta;
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__5443__auto__){
var self__ = this;
var this__5443__auto____$1 = this;
return (new day8.re_frame_10x.tools.datafy.WrappedSortedMap(self__.data,self__.__meta,self__.__extmap,self__.__hash));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__5452__auto__){
var self__ = this;
var this__5452__auto____$1 = this;
return (1 + cljs.core.count(self__.__extmap));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__5444__auto__){
var self__ = this;
var this__5444__auto____$1 = this;
var h__5251__auto__ = self__.__hash;
if((!((h__5251__auto__ == null)))){
return h__5251__auto__;
} else {
var h__5251__auto____$1 = (function (coll__5445__auto__){
return (1539591843 ^ cljs.core.hash_unordered_coll(coll__5445__auto__));
})(this__5444__auto____$1);
(self__.__hash = h__5251__auto____$1);

return h__5251__auto____$1;
}
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this23529,other23530){
var self__ = this;
var this23529__$1 = this;
return (((!((other23530 == null)))) && ((((this23529__$1.constructor === other23530.constructor)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this23529__$1.data,other23530.data)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this23529__$1.__extmap,other23530.__extmap)))))));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__5458__auto__,k__5459__auto__){
var self__ = this;
var this__5458__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data","data",-232669377),null], null), null),k__5459__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__5458__auto____$1),self__.__meta),k__5459__auto__);
} else {
return (new day8.re_frame_10x.tools.datafy.WrappedSortedMap(self__.data,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__5459__auto__)),null));
}
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IAssociative$_contains_key_QMARK_$arity$2 = (function (this__5455__auto__,k23528){
var self__ = this;
var this__5455__auto____$1 = this;
var G__23559 = k23528;
var G__23559__$1 = (((G__23559 instanceof cljs.core.Keyword))?G__23559.fqn:null);
switch (G__23559__$1) {
case "data":
return true;

break;
default:
return cljs.core.contains_QMARK_(self__.__extmap,k23528);

}
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__5456__auto__,k__5457__auto__,G__23527){
var self__ = this;
var this__5456__auto____$1 = this;
var pred__23560 = cljs.core.keyword_identical_QMARK_;
var expr__23561 = k__5457__auto__;
if(cljs.core.truth_((pred__23560.cljs$core$IFn$_invoke$arity$2 ? pred__23560.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"data","data",-232669377),expr__23561) : pred__23560.call(null,new cljs.core.Keyword(null,"data","data",-232669377),expr__23561)))){
return (new day8.re_frame_10x.tools.datafy.WrappedSortedMap(G__23527,self__.__meta,self__.__extmap,null));
} else {
return (new day8.re_frame_10x.tools.datafy.WrappedSortedMap(self__.data,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__5457__auto__,G__23527),null));
}
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__5461__auto__){
var self__ = this;
var this__5461__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"data","data",-232669377),self__.data,null))], null),self__.__extmap));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__5447__auto__,G__23527){
var self__ = this;
var this__5447__auto____$1 = this;
return (new day8.re_frame_10x.tools.datafy.WrappedSortedMap(self__.data,G__23527,self__.__extmap,self__.__hash));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__5453__auto__,entry__5454__auto__){
var self__ = this;
var this__5453__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__5454__auto__)){
return this__5453__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__5454__auto__,(0)),cljs.core._nth(entry__5454__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__5453__auto____$1,entry__5454__auto__);
}
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.getBasis = (function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"data","data",1407862150,null)], null);
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.cljs$lang$type = true);

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.cljs$lang$ctorPrSeq = (function (this__5494__auto__){
return (new cljs.core.List(null,"day8.re-frame-10x.tools.datafy/WrappedSortedMap",null,(1),null));
}));

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.cljs$lang$ctorPrWriter = (function (this__5494__auto__,writer__5495__auto__){
return cljs.core._write(writer__5495__auto__,"day8.re-frame-10x.tools.datafy/WrappedSortedMap");
}));

/**
 * Positional factory function for day8.re-frame-10x.tools.datafy/WrappedSortedMap.
 */
day8.re_frame_10x.tools.datafy.__GT_WrappedSortedMap = (function day8$re_frame_10x$tools$datafy$__GT_WrappedSortedMap(data){
return (new day8.re_frame_10x.tools.datafy.WrappedSortedMap(data,null,null,null));
});

/**
 * Factory function for day8.re-frame-10x.tools.datafy/WrappedSortedMap, taking a map of keywords to field values.
 */
day8.re_frame_10x.tools.datafy.map__GT_WrappedSortedMap = (function day8$re_frame_10x$tools$datafy$map__GT_WrappedSortedMap(G__23534){
var extmap__5490__auto__ = (function (){var G__23567 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(G__23534,new cljs.core.Keyword(null,"data","data",-232669377));
if(cljs.core.record_QMARK_(G__23534)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__23567);
} else {
return G__23567;
}
})();
return (new day8.re_frame_10x.tools.datafy.WrappedSortedMap(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(G__23534),null,cljs.core.not_empty(extmap__5490__auto__),null));
});

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IPrintWithWriter$ = cljs.core.PROTOCOL_SENTINEL);

(day8.re_frame_10x.tools.datafy.WrappedSortedMap.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (o,writer,_){
var o__$1 = this;
return cljs.core.write_all.cljs$core$IFn$_invoke$arity$variadic(writer,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["#re-frame-10x/sorted-map ",cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,o__$1.data)], 0));
}));
day8.re_frame_10x.tools.datafy.wrap_sorted_map = (function day8$re_frame_10x$tools$datafy$wrap_sorted_map(x){
if((!((x instanceof cljs.core.PersistentTreeMap)))){
return x;
} else {
return day8.re_frame_10x.tools.datafy.__GT_WrappedSortedMap(x);
}
});
day8.re_frame_10x.tools.datafy.serialize_special_types = (function day8$re_frame_10x$tools$datafy$serialize_special_types(db){
return day8.re_frame_10x.tools.datafy.pr_str_safe(clojure.walk.postwalk(day8.re_frame_10x.tools.datafy.wrap_sorted_map,db));
});

//# sourceMappingURL=day8.re_frame_10x.tools.datafy.js.map
