goog.provide('cljs.analyzer.impl.namespaces');
/**
 * Given a libspec return a map of :as-alias alias, if was present. Return the
 * libspec with :as-alias elided. If the libspec was *only* :as-alias do not
 * return it.
 */
cljs.analyzer.impl.namespaces.check_and_remove_as_alias = (function cljs$analyzer$impl$namespaces$check_and_remove_as_alias(libspec){
if((((libspec instanceof cljs.core.Symbol)) || ((libspec instanceof cljs.core.Keyword)))){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"libspec","libspec",1228503756),libspec], null);
} else {
var vec__20421 = libspec;
var seq__20422 = cljs.core.seq(vec__20421);
var first__20423 = cljs.core.first(seq__20422);
var seq__20422__$1 = cljs.core.next(seq__20422);
var lib = first__20423;
var spec = seq__20422__$1;
var libspec__$1 = vec__20421;
var vec__20424 = cljs.core.split_with(cljs.core.complement(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"as-alias","as-alias",82482467),null], null), null)),spec);
var pre_spec = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20424,(0),null);
var vec__20427 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20424,(1),null);
var seq__20428 = cljs.core.seq(vec__20427);
var first__20429 = cljs.core.first(seq__20428);
var seq__20428__$1 = cljs.core.next(seq__20428);
var _ = first__20429;
var first__20429__$1 = cljs.core.first(seq__20428__$1);
var seq__20428__$2 = cljs.core.next(seq__20428__$1);
var alias = first__20429__$1;
var post_spec = seq__20428__$2;
var post = vec__20427;
if(cljs.core.seq(post)){
var libspec_SINGLEQUOTE_ = cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [lib], null),cljs.core.concat.cljs$core$IFn$_invoke$arity$2(pre_spec,post_spec));
if((alias instanceof cljs.core.Symbol)){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+":as-alias must be followed by a symbol, got: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(alias)))+"\n"+"(symbol? alias)")));
}

var G__20433 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"as-alias","as-alias",82482467),cljs.core.PersistentArrayMap.createAsIfByAssoc([alias,lib])], null);
if((cljs.core.count(libspec_SINGLEQUOTE_) > (1))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__20433,new cljs.core.Keyword(null,"libspec","libspec",1228503756),libspec_SINGLEQUOTE_);
} else {
return G__20433;
}
} else {
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"libspec","libspec",1228503756),libspec__$1], null);
}
}
});
cljs.analyzer.impl.namespaces.check_as_alias_duplicates = (function cljs$analyzer$impl$namespaces$check_as_alias_duplicates(as_aliases,new_as_aliases){
var seq__20437 = cljs.core.seq(new_as_aliases);
var chunk__20438 = null;
var count__20439 = (0);
var i__20440 = (0);
while(true){
if((i__20440 < count__20439)){
var vec__20453 = chunk__20438.cljs$core$IIndexed$_nth$arity$2(null,i__20440);
var alias = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20453,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20453,(1),null);
if((!(cljs.core.contains_QMARK_(as_aliases,alias)))){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"Duplicate :as-alias "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(alias)+", already in use for lib "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(as_aliases,alias))))+"\n"+"(not (contains? as-aliases alias))")));
}


var G__20496 = seq__20437;
var G__20497 = chunk__20438;
var G__20498 = count__20439;
var G__20499 = (i__20440 + (1));
seq__20437 = G__20496;
chunk__20438 = G__20497;
count__20439 = G__20498;
i__20440 = G__20499;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__20437);
if(temp__5825__auto__){
var seq__20437__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__20437__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__20437__$1);
var G__20504 = cljs.core.chunk_rest(seq__20437__$1);
var G__20505 = c__5673__auto__;
var G__20506 = cljs.core.count(c__5673__auto__);
var G__20507 = (0);
seq__20437 = G__20504;
chunk__20438 = G__20505;
count__20439 = G__20506;
i__20440 = G__20507;
continue;
} else {
var vec__20457 = cljs.core.first(seq__20437__$1);
var alias = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20457,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__20457,(1),null);
if((!(cljs.core.contains_QMARK_(as_aliases,alias)))){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"Duplicate :as-alias "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(alias)+", already in use for lib "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(as_aliases,alias))))+"\n"+"(not (contains? as-aliases alias))")));
}


var G__20508 = cljs.core.next(seq__20437__$1);
var G__20509 = null;
var G__20510 = (0);
var G__20511 = (0);
seq__20437 = G__20508;
chunk__20438 = G__20509;
count__20439 = G__20510;
i__20440 = G__20511;
continue;
}
} else {
return null;
}
}
break;
}
});
/**
 * Given libspecs, elide all :as-alias. Return a map of :libspecs (filtered)
 * and :as-aliases.
 */
cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs = (function cljs$analyzer$impl$namespaces$elide_aliases_from_libspecs(var_args){
var G__20461 = arguments.length;
switch (G__20461) {
case 1:
return cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs.cljs$core$IFn$_invoke$arity$1 = (function (libspecs){
return cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs.cljs$core$IFn$_invoke$arity$2(libspecs,cljs.core.PersistentArrayMap.EMPTY);
}));

(cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs.cljs$core$IFn$_invoke$arity$2 = (function (libspecs,as_aliases){
var ret = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"as-aliases","as-aliases",1485064798),as_aliases,new cljs.core.Keyword(null,"libspecs","libspecs",59807195),cljs.core.PersistentVector.EMPTY], null);
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__$1,libspec){
var map__20463 = cljs.analyzer.impl.namespaces.check_and_remove_as_alias(libspec);
var map__20463__$1 = cljs.core.__destructure_map(map__20463);
var as_alias = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20463__$1,new cljs.core.Keyword(null,"as-alias","as-alias",82482467));
var libspec__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20463__$1,new cljs.core.Keyword(null,"libspec","libspec",1228503756));
cljs.analyzer.impl.namespaces.check_as_alias_duplicates(new cljs.core.Keyword(null,"as-aliases","as-aliases",1485064798).cljs$core$IFn$_invoke$arity$1(ret__$1),as_alias);

var G__20466 = ret__$1;
var G__20466__$1 = (cljs.core.truth_(libspec__$1)?cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__20466,new cljs.core.Keyword(null,"libspecs","libspecs",59807195),cljs.core.conj,libspec__$1):G__20466);
if(cljs.core.truth_(as_alias)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__20466__$1,new cljs.core.Keyword(null,"as-aliases","as-aliases",1485064798),cljs.core.merge,as_alias);
} else {
return G__20466__$1;
}
}),ret,libspecs);
}));

(cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs.cljs$lang$maxFixedArity = 2);

cljs.analyzer.impl.namespaces.elide_aliases_from_ns_specs = (function cljs$analyzer$impl$namespaces$elide_aliases_from_ns_specs(ns_specs){

var ret = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"as-aliases","as-aliases",1485064798),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"libspecs","libspecs",59807195),cljs.core.PersistentVector.EMPTY], null);
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p__20470,p__20471){
var map__20472 = p__20470;
var map__20472__$1 = cljs.core.__destructure_map(map__20472);
var ret__$1 = map__20472__$1;
var as_aliases = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20472__$1,new cljs.core.Keyword(null,"as-aliases","as-aliases",1485064798));
var vec__20473 = p__20471;
var seq__20474 = cljs.core.seq(vec__20473);
var first__20475 = cljs.core.first(seq__20474);
var seq__20474__$1 = cljs.core.next(seq__20474);
var spec_key = first__20475;
var libspecs = seq__20474__$1;
if((!(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"refer-clojure","refer-clojure",813784440),spec_key)))){
var map__20476 = cljs.analyzer.impl.namespaces.elide_aliases_from_libspecs.cljs$core$IFn$_invoke$arity$2(libspecs,as_aliases);
var map__20476__$1 = cljs.core.__destructure_map(map__20476);
var as_aliases__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20476__$1,new cljs.core.Keyword(null,"as-aliases","as-aliases",1485064798));
var libspecs__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__20476__$1,new cljs.core.Keyword(null,"libspecs","libspecs",59807195));
var G__20479 = ret__$1;
var G__20479__$1 = (((!(cljs.core.empty_QMARK_(as_aliases__$1))))?cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__20479,new cljs.core.Keyword(null,"as-aliases","as-aliases",1485064798),cljs.core.merge,as_aliases__$1):G__20479);
if((!(cljs.core.empty_QMARK_(libspecs__$1)))){
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__20479__$1,new cljs.core.Keyword(null,"libspecs","libspecs",59807195),cljs.core.conj,cljs.core.list_STAR_.cljs$core$IFn$_invoke$arity$2(spec_key,libspecs__$1));
} else {
return G__20479__$1;
}
} else {
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(ret__$1,new cljs.core.Keyword(null,"libspecs","libspecs",59807195),cljs.core.conj,cljs.core.list_STAR_.cljs$core$IFn$_invoke$arity$2(spec_key,libspecs));
}
}),ret,ns_specs);
});

//# sourceMappingURL=cljs.analyzer.impl.namespaces.js.map
