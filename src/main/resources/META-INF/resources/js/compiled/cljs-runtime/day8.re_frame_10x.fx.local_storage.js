goog.provide('day8.re_frame_10x.fx.local_storage');
goog.scope(function(){
  day8.re_frame_10x.fx.local_storage.goog$module$goog$testing$storage$FakeMechanism = goog.module.get('goog.testing.storage.FakeMechanism');
});
/**
 * LocalStorage is not available in sandboxed iframes, so check
 *   window.localStorage and use the fake storage mechanism if it's not available.
 *   re-frame-10x settings will not persist, but it will work.
 */
day8.re_frame_10x.fx.local_storage.storage_mechanism = (function (){try{if(cljs.core.truth_(localStorage)){
return (new goog.storage.mechanism.HTML5LocalStorage());
} else {
return null;
}
}catch (e24154){if((e24154 instanceof Error)){
var _ = e24154;
return (new day8.re_frame_10x.fx.local_storage.goog$module$goog$testing$storage$FakeMechanism());
} else {
throw e24154;

}
}})();
day8.re_frame_10x.fx.local_storage.storage = (new goog.storage.Storage(day8.re_frame_10x.fx.local_storage.storage_mechanism));
day8.re_frame_10x.fx.local_storage.safe_prefix = "day8.re-frame-10x.";
/**
 * Adds a unique prefix to local storage keys to ensure they don't collide with the host application
 */
day8.re_frame_10x.fx.local_storage.safe_key = (function day8$re_frame_10x$fx$local_storage$safe_key(key){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(day8.re_frame_10x.fx.local_storage.safe_prefix)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(key));
});
/**
 * Loads a re-frame-10x value from local storage.
 */
day8.re_frame_10x.fx.local_storage.load = (function day8$re_frame_10x$fx$local_storage$load(key){
var value = day8.re_frame_10x.fx.local_storage.storage.get(day8.re_frame_10x.fx.local_storage.safe_key(key));
if((void 0 === value)){
return null;
} else {
return cljs.reader.read_string.cljs$core$IFn$_invoke$arity$1(value);
}
});
day8.re_frame_10x.fx.local_storage.all_keys = (function day8$re_frame_10x$fx$local_storage$all_keys(){
try{return Object.keys(localStorage);
}catch (e24161){if((e24161 instanceof Error)){
var _ = e24161;
return cljs.core.PersistentVector.EMPTY;
} else {
throw e24161;

}
}});
/**
 * Deletes all re-frame-10x config keys
 */
day8.re_frame_10x.fx.local_storage.delete_all_keys_BANG_ = (function day8$re_frame_10x$fx$local_storage$delete_all_keys_BANG_(){
var seq__24164 = cljs.core.seq(day8.re_frame_10x.fx.local_storage.all_keys());
var chunk__24165 = null;
var count__24166 = (0);
var i__24167 = (0);
while(true){
if((i__24167 < count__24166)){
var k = chunk__24165.cljs$core$IIndexed$_nth$arity$2(null,i__24167);
if(clojure.string.starts_with_QMARK_(k,day8.re_frame_10x.fx.local_storage.safe_prefix)){
day8.re_frame_10x.fx.local_storage.storage.remove(k);
} else {
}


var G__24184 = seq__24164;
var G__24185 = chunk__24165;
var G__24186 = count__24166;
var G__24187 = (i__24167 + (1));
seq__24164 = G__24184;
chunk__24165 = G__24185;
count__24166 = G__24186;
i__24167 = G__24187;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__24164);
if(temp__5825__auto__){
var seq__24164__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__24164__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__24164__$1);
var G__24188 = cljs.core.chunk_rest(seq__24164__$1);
var G__24189 = c__5673__auto__;
var G__24190 = cljs.core.count(c__5673__auto__);
var G__24191 = (0);
seq__24164 = G__24188;
chunk__24165 = G__24189;
count__24166 = G__24190;
i__24167 = G__24191;
continue;
} else {
var k = cljs.core.first(seq__24164__$1);
if(clojure.string.starts_with_QMARK_(k,day8.re_frame_10x.fx.local_storage.safe_prefix)){
day8.re_frame_10x.fx.local_storage.storage.remove(k);
} else {
}


var G__24192 = cljs.core.next(seq__24164__$1);
var G__24193 = null;
var G__24194 = (0);
var G__24195 = (0);
seq__24164 = G__24192;
chunk__24165 = G__24193;
count__24166 = G__24194;
i__24167 = G__24195;
continue;
}
} else {
return null;
}
}
break;
}
});
day8.re_frame_10x.fx.local_storage.save = (function day8$re_frame_10x$fx$local_storage$save(var_args){
var G__24174 = arguments.length;
switch (G__24174) {
case 1:
return day8.re_frame_10x.fx.local_storage.save.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
var args_arr__5901__auto__ = [];
var len__5876__auto___24199 = arguments.length;
var i__5877__auto___24200 = (0);
while(true){
if((i__5877__auto___24200 < len__5876__auto___24199)){
args_arr__5901__auto__.push((arguments[i__5877__auto___24200]));

var G__24201 = (i__5877__auto___24200 + (1));
i__5877__auto___24200 = G__24201;
continue;
} else {
}
break;
}

var argseq__5902__auto__ = ((((1) < args_arr__5901__auto__.length))?(new cljs.core.IndexedSeq(args_arr__5901__auto__.slice((1)),(0),null)):null);
return day8.re_frame_10x.fx.local_storage.save.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5902__auto__);

}
});

(day8.re_frame_10x.fx.local_storage.save.cljs$core$IFn$_invoke$arity$1 = (function (key){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.after((function (db){
return day8.re_frame_10x.fx.local_storage.storage.set(day8.re_frame_10x.fx.local_storage.safe_key(key),day8.re_frame_10x.tools.datafy.pr_str_safe(db));
}));
}));

(day8.re_frame_10x.fx.local_storage.save.cljs$core$IFn$_invoke$arity$variadic = (function (key,ks){
return day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.after((function (db){
return cljs.core.run_BANG_((function (k){
var v = ((cljs.core.vector_QMARK_(k))?cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,k):cljs.core.get.cljs$core$IFn$_invoke$arity$2(db,k));
return day8.re_frame_10x.fx.local_storage.storage.set(day8.re_frame_10x.fx.local_storage.safe_key(key),day8.re_frame_10x.tools.datafy.pr_str_safe(v));
}),ks);
}));
}));

/** @this {Function} */
(day8.re_frame_10x.fx.local_storage.save.cljs$lang$applyTo = (function (seq24172){
var G__24173 = cljs.core.first(seq24172);
var seq24172__$1 = cljs.core.next(seq24172);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__24173,seq24172__$1);
}));

(day8.re_frame_10x.fx.local_storage.save.cljs$lang$maxFixedArity = (1));

day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_cofx(new cljs.core.Keyword("day8.re-frame-10x.fx.local-storage","load","day8.re-frame-10x.fx.local-storage/load",1482432658),(function (coeffects,p__24177){
var map__24178 = p__24177;
var map__24178__$1 = cljs.core.__destructure_map(map__24178);
var storage_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24178__$1,new cljs.core.Keyword(null,"key","key",-1516042587));
var fallback = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24178__$1,new cljs.core.Keyword(null,"or","or",235744169));
var k = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(storage_key);
var v = day8.re_frame_10x.fx.local_storage.load(storage_key);
var G__24181 = coeffects;
var G__24181__$1 = (((!((fallback == null))))?cljs.core.assoc_in(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__24181,k,fallback),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.fx.local-storage","fallback","day8.re-frame-10x.fx.local-storage/fallback",-294997201),k], null),fallback):G__24181);
if((!((v == null)))){
return cljs.core.assoc_in(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__24181__$1,k,v),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.fx.local-storage","stored","day8.re-frame-10x.fx.local-storage/stored",1674400390),k], null),v);
} else {
return G__24181__$1;
}
}));

//# sourceMappingURL=day8.re_frame_10x.fx.local_storage.js.map
