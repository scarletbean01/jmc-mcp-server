goog.provide('cljs.repl');
cljs.repl.print_doc = (function cljs$repl$print_doc(p__45646){
var map__45648 = p__45646;
var map__45648__$1 = cljs.core.__destructure_map(map__45648);
var m = map__45648__$1;
var n = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45648__$1,new cljs.core.Keyword(null,"ns","ns",441598760));
var nm = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45648__$1,new cljs.core.Keyword(null,"name","name",1843675177));
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["-------------------------"], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var temp__5825__auto__ = new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(temp__5825__auto__)){
var ns = temp__5825__auto__;
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ns)+"/");
} else {
return null;
}
})())+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m)));
}
})()], 0));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Protocol"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m))){
var seq__45650_45848 = cljs.core.seq(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m));
var chunk__45651_45849 = null;
var count__45652_45850 = (0);
var i__45653_45851 = (0);
while(true){
if((i__45653_45851 < count__45652_45850)){
var f_45852 = chunk__45651_45849.cljs$core$IIndexed$_nth$arity$2(null,i__45653_45851);
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["  ",f_45852], 0));


var G__45853 = seq__45650_45848;
var G__45854 = chunk__45651_45849;
var G__45855 = count__45652_45850;
var G__45856 = (i__45653_45851 + (1));
seq__45650_45848 = G__45853;
chunk__45651_45849 = G__45854;
count__45652_45850 = G__45855;
i__45653_45851 = G__45856;
continue;
} else {
var temp__5825__auto___45857 = cljs.core.seq(seq__45650_45848);
if(temp__5825__auto___45857){
var seq__45650_45858__$1 = temp__5825__auto___45857;
if(cljs.core.chunked_seq_QMARK_(seq__45650_45858__$1)){
var c__5673__auto___45859 = cljs.core.chunk_first(seq__45650_45858__$1);
var G__45860 = cljs.core.chunk_rest(seq__45650_45858__$1);
var G__45861 = c__5673__auto___45859;
var G__45862 = cljs.core.count(c__5673__auto___45859);
var G__45863 = (0);
seq__45650_45848 = G__45860;
chunk__45651_45849 = G__45861;
count__45652_45850 = G__45862;
i__45653_45851 = G__45863;
continue;
} else {
var f_45864 = cljs.core.first(seq__45650_45858__$1);
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["  ",f_45864], 0));


var G__45865 = cljs.core.next(seq__45650_45858__$1);
var G__45866 = null;
var G__45867 = (0);
var G__45868 = (0);
seq__45650_45848 = G__45865;
chunk__45651_45849 = G__45866;
count__45652_45850 = G__45867;
i__45653_45851 = G__45868;
continue;
}
} else {
}
}
break;
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m))){
var arglists_45869 = new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m);
}
})())){
cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([arglists_45869], 0));
} else {
cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.first(arglists_45869)))?cljs.core.second(arglists_45869):arglists_45869)], 0));
}
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"special-form","special-form",-1326536374).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Special Form"], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m)], 0));

if(cljs.core.contains_QMARK_(m,new cljs.core.Keyword(null,"url","url",276297046))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))){
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n  Please see http://clojure.org/"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m)))], 0));
} else {
return null;
}
} else {
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n  Please see http://clojure.org/special_forms#"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m)))], 0));
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Macro"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Spec"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["REPL Special Function"], 0));
} else {
}

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m)], 0));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
var seq__45657_45870 = cljs.core.seq(new cljs.core.Keyword(null,"methods","methods",453930866).cljs$core$IFn$_invoke$arity$1(m));
var chunk__45658_45871 = null;
var count__45659_45872 = (0);
var i__45660_45873 = (0);
while(true){
if((i__45660_45873 < count__45659_45872)){
var vec__45685_45874 = chunk__45658_45871.cljs$core$IIndexed$_nth$arity$2(null,i__45660_45873);
var name_45875 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45685_45874,(0),null);
var map__45688_45876 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45685_45874,(1),null);
var map__45688_45877__$1 = cljs.core.__destructure_map(map__45688_45876);
var doc_45878 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45688_45877__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_45879 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45688_45877__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println();

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",name_45875], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",arglists_45879], 0));

if(cljs.core.truth_(doc_45878)){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",doc_45878], 0));
} else {
}


var G__45880 = seq__45657_45870;
var G__45881 = chunk__45658_45871;
var G__45882 = count__45659_45872;
var G__45883 = (i__45660_45873 + (1));
seq__45657_45870 = G__45880;
chunk__45658_45871 = G__45881;
count__45659_45872 = G__45882;
i__45660_45873 = G__45883;
continue;
} else {
var temp__5825__auto___45884 = cljs.core.seq(seq__45657_45870);
if(temp__5825__auto___45884){
var seq__45657_45885__$1 = temp__5825__auto___45884;
if(cljs.core.chunked_seq_QMARK_(seq__45657_45885__$1)){
var c__5673__auto___45886 = cljs.core.chunk_first(seq__45657_45885__$1);
var G__45887 = cljs.core.chunk_rest(seq__45657_45885__$1);
var G__45888 = c__5673__auto___45886;
var G__45889 = cljs.core.count(c__5673__auto___45886);
var G__45890 = (0);
seq__45657_45870 = G__45887;
chunk__45658_45871 = G__45888;
count__45659_45872 = G__45889;
i__45660_45873 = G__45890;
continue;
} else {
var vec__45691_45891 = cljs.core.first(seq__45657_45885__$1);
var name_45892 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45691_45891,(0),null);
var map__45694_45893 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45691_45891,(1),null);
var map__45694_45894__$1 = cljs.core.__destructure_map(map__45694_45893);
var doc_45895 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45694_45894__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_45896 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45694_45894__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println();

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",name_45892], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",arglists_45896], 0));

if(cljs.core.truth_(doc_45895)){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",doc_45895], 0));
} else {
}


var G__45899 = cljs.core.next(seq__45657_45885__$1);
var G__45900 = null;
var G__45901 = (0);
var G__45902 = (0);
seq__45657_45870 = G__45899;
chunk__45658_45871 = G__45900;
count__45659_45872 = G__45901;
i__45660_45873 = G__45902;
continue;
}
} else {
}
}
break;
}
} else {
}

if(cljs.core.truth_(n)){
var temp__5825__auto__ = cljs.spec.alpha.get_spec(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.ns_name(n))),cljs.core.name(nm)));
if(cljs.core.truth_(temp__5825__auto__)){
var fnspec = temp__5825__auto__;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Spec"], 0));

var seq__45702 = cljs.core.seq(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"args","args",1315556576),new cljs.core.Keyword(null,"ret","ret",-468222814),new cljs.core.Keyword(null,"fn","fn",-1175266204)], null));
var chunk__45703 = null;
var count__45704 = (0);
var i__45705 = (0);
while(true){
if((i__45705 < count__45704)){
var role = chunk__45703.cljs$core$IIndexed$_nth$arity$2(null,i__45705);
var temp__5825__auto___45903__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(fnspec,role);
if(cljs.core.truth_(temp__5825__auto___45903__$1)){
var spec_45904 = temp__5825__auto___45903__$1;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(role))+":"),cljs.spec.alpha.describe(spec_45904)], 0));
} else {
}


var G__45905 = seq__45702;
var G__45906 = chunk__45703;
var G__45907 = count__45704;
var G__45908 = (i__45705 + (1));
seq__45702 = G__45905;
chunk__45703 = G__45906;
count__45704 = G__45907;
i__45705 = G__45908;
continue;
} else {
var temp__5825__auto____$1 = cljs.core.seq(seq__45702);
if(temp__5825__auto____$1){
var seq__45702__$1 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(seq__45702__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__45702__$1);
var G__45911 = cljs.core.chunk_rest(seq__45702__$1);
var G__45912 = c__5673__auto__;
var G__45913 = cljs.core.count(c__5673__auto__);
var G__45914 = (0);
seq__45702 = G__45911;
chunk__45703 = G__45912;
count__45704 = G__45913;
i__45705 = G__45914;
continue;
} else {
var role = cljs.core.first(seq__45702__$1);
var temp__5825__auto___45915__$2 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(fnspec,role);
if(cljs.core.truth_(temp__5825__auto___45915__$2)){
var spec_45916 = temp__5825__auto___45915__$2;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(role))+":"),cljs.spec.alpha.describe(spec_45916)], 0));
} else {
}


var G__45918 = cljs.core.next(seq__45702__$1);
var G__45919 = null;
var G__45920 = (0);
var G__45921 = (0);
seq__45702 = G__45918;
chunk__45703 = G__45919;
count__45704 = G__45920;
i__45705 = G__45921;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
} else {
return null;
}
}
});
/**
 * Constructs a data representation for a Error with keys:
 *  :cause - root cause message
 *  :phase - error phase
 *  :via - cause chain, with cause keys:
 *           :type - exception class symbol
 *           :message - exception message
 *           :data - ex-data
 *           :at - top stack element
 *  :trace - root cause stack elements
 */
cljs.repl.Error__GT_map = (function cljs$repl$Error__GT_map(o){
return cljs.core.Throwable__GT_map(o);
});
/**
 * Returns an analysis of the phase, error, cause, and location of an error that occurred
 *   based on Throwable data, as returned by Throwable->map. All attributes other than phase
 *   are optional:
 *  :clojure.error/phase - keyword phase indicator, one of:
 *    :read-source :compile-syntax-check :compilation :macro-syntax-check :macroexpansion
 *    :execution :read-eval-result :print-eval-result
 *  :clojure.error/source - file name (no path)
 *  :clojure.error/line - integer line number
 *  :clojure.error/column - integer column number
 *  :clojure.error/symbol - symbol being expanded/compiled/invoked
 *  :clojure.error/class - cause exception class symbol
 *  :clojure.error/cause - cause exception message
 *  :clojure.error/spec - explain-data for spec error
 */
cljs.repl.ex_triage = (function cljs$repl$ex_triage(datafied_throwable){
var map__45722 = datafied_throwable;
var map__45722__$1 = cljs.core.__destructure_map(map__45722);
var via = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45722__$1,new cljs.core.Keyword(null,"via","via",-1904457336));
var trace = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45722__$1,new cljs.core.Keyword(null,"trace","trace",-1082747415));
var phase = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__45722__$1,new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"execution","execution",253283524));
var map__45723 = cljs.core.last(via);
var map__45723__$1 = cljs.core.__destructure_map(map__45723);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45723__$1,new cljs.core.Keyword(null,"type","type",1174270348));
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45723__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45723__$1,new cljs.core.Keyword(null,"data","data",-232669377));
var map__45724 = data;
var map__45724__$1 = cljs.core.__destructure_map(map__45724);
var problems = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45724__$1,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814));
var fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45724__$1,new cljs.core.Keyword("cljs.spec.alpha","fn","cljs.spec.alpha/fn",408600443));
var caller = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45724__$1,new cljs.core.Keyword("cljs.spec.test.alpha","caller","cljs.spec.test.alpha/caller",-398302390));
var map__45725 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.first(via));
var map__45725__$1 = cljs.core.__destructure_map(map__45725);
var top_data = map__45725__$1;
var source = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45725__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3((function (){var G__45729 = phase;
var G__45729__$1 = (((G__45729 instanceof cljs.core.Keyword))?G__45729.fqn:null);
switch (G__45729__$1) {
case "read-source":
var map__45730 = data;
var map__45730__$1 = cljs.core.__destructure_map(map__45730);
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45730__$1,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471));
var column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45730__$1,new cljs.core.Keyword("clojure.error","column","clojure.error/column",304721553));
var G__45732 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.second(via)),top_data], 0));
var G__45732__$1 = (cljs.core.truth_(source)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45732,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),source):G__45732);
var G__45732__$2 = (cljs.core.truth_((function (){var fexpr__45733 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__45733.cljs$core$IFn$_invoke$arity$1 ? fexpr__45733.cljs$core$IFn$_invoke$arity$1(source) : fexpr__45733.call(null,source));
})())?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(G__45732__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397)):G__45732__$1);
if(cljs.core.truth_(message)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45732__$2,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message);
} else {
return G__45732__$2;
}

break;
case "compile-syntax-check":
case "compilation":
case "macro-syntax-check":
case "macroexpansion":
var G__45736 = top_data;
var G__45736__$1 = (cljs.core.truth_(source)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45736,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),source):G__45736);
var G__45736__$2 = (cljs.core.truth_((function (){var fexpr__45745 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__45745.cljs$core$IFn$_invoke$arity$1 ? fexpr__45745.cljs$core$IFn$_invoke$arity$1(source) : fexpr__45745.call(null,source));
})())?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(G__45736__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397)):G__45736__$1);
var G__45736__$3 = (cljs.core.truth_(type)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45736__$2,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type):G__45736__$2);
var G__45736__$4 = (cljs.core.truth_(message)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45736__$3,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message):G__45736__$3);
if(cljs.core.truth_(problems)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45736__$4,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595),data);
} else {
return G__45736__$4;
}

break;
case "read-eval-result":
case "print-eval-result":
var vec__45746 = cljs.core.first(trace);
var source__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45746,(0),null);
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45746,(1),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45746,(2),null);
var line = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45746,(3),null);
var G__45749 = top_data;
var G__45749__$1 = (cljs.core.truth_(line)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45749,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471),line):G__45749);
var G__45749__$2 = (cljs.core.truth_(file)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45749__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),file):G__45749__$1);
var G__45749__$3 = (cljs.core.truth_((function (){var and__5140__auto__ = source__$1;
if(cljs.core.truth_(and__5140__auto__)){
return method;
} else {
return and__5140__auto__;
}
})())?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45749__$2,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[source__$1,method],null))):G__45749__$2);
var G__45749__$4 = (cljs.core.truth_(type)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45749__$3,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type):G__45749__$3);
if(cljs.core.truth_(message)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45749__$4,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message);
} else {
return G__45749__$4;
}

break;
case "execution":
var vec__45750 = cljs.core.first(trace);
var source__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45750,(0),null);
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45750,(1),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45750,(2),null);
var line = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45750,(3),null);
var file__$1 = cljs.core.first(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__45721_SHARP_){
var or__5142__auto__ = (p1__45721_SHARP_ == null);
if(or__5142__auto__){
return or__5142__auto__;
} else {
var fexpr__45753 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__45753.cljs$core$IFn$_invoke$arity$1 ? fexpr__45753.cljs$core$IFn$_invoke$arity$1(p1__45721_SHARP_) : fexpr__45753.call(null,p1__45721_SHARP_));
}
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"file","file",-1269645878).cljs$core$IFn$_invoke$arity$1(caller),file], null)));
var err_line = (function (){var or__5142__auto__ = new cljs.core.Keyword(null,"line","line",212345235).cljs$core$IFn$_invoke$arity$1(caller);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return line;
}
})();
var G__45755 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type], null);
var G__45755__$1 = (cljs.core.truth_(err_line)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45755,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471),err_line):G__45755);
var G__45755__$2 = (cljs.core.truth_(message)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45755__$1,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message):G__45755__$1);
var G__45755__$3 = (cljs.core.truth_((function (){var or__5142__auto__ = fn;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var and__5140__auto__ = source__$1;
if(cljs.core.truth_(and__5140__auto__)){
return method;
} else {
return and__5140__auto__;
}
}
})())?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45755__$2,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994),(function (){var or__5142__auto__ = fn;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[source__$1,method],null));
}
})()):G__45755__$2);
var G__45755__$4 = (cljs.core.truth_(file__$1)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45755__$3,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),file__$1):G__45755__$3);
if(cljs.core.truth_(problems)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__45755__$4,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595),data);
} else {
return G__45755__$4;
}

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__45729__$1))));

}
})(),new cljs.core.Keyword("clojure.error","phase","clojure.error/phase",275140358),phase);
});
/**
 * Returns a string from exception data, as produced by ex-triage.
 *   The first line summarizes the exception phase and location.
 *   The subsequent lines describe the cause.
 */
cljs.repl.ex_str = (function cljs$repl$ex_str(p__45762){
var map__45763 = p__45762;
var map__45763__$1 = cljs.core.__destructure_map(map__45763);
var triage_data = map__45763__$1;
var phase = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","phase","clojure.error/phase",275140358));
var source = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397));
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471));
var column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","column","clojure.error/column",304721553));
var symbol = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890));
var cause = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742));
var spec = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45763__$1,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595));
var loc = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = source;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "<cljs repl>";
}
})())+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = line;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (1);
}
})())+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(column)?(""+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(column)):"")));
var class_name = cljs.core.name((function (){var or__5142__auto__ = class$;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "";
}
})());
var simple_class = class_name;
var cause_type = ((cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["RuntimeException",null,"Exception",null], null), null),simple_class))?"":(""+" ("+cljs.core.str.cljs$core$IFn$_invoke$arity$1(simple_class)+")"));
var format = goog.string.format;
var G__45765 = phase;
var G__45765__$1 = (((G__45765 instanceof cljs.core.Keyword))?G__45765.fqn:null);
switch (G__45765__$1) {
case "read-source":
return (format.cljs$core$IFn$_invoke$arity$3 ? format.cljs$core$IFn$_invoke$arity$3("Syntax error reading source at (%s).\n%s\n",loc,cause) : format.call(null,"Syntax error reading source at (%s).\n%s\n",loc,cause));

break;
case "macro-syntax-check":
var G__45766 = "Syntax error macroexpanding %sat (%s).\n%s";
var G__45767 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__45768 = loc;
var G__45769 = (cljs.core.truth_(spec)?(function (){var sb__5795__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR__orig_val__45773_45984 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR__orig_val__45774_45985 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_newline_STAR__temp_val__45775_45986 = true;
var _STAR_print_fn_STAR__temp_val__45776_45987 = (function (x__5796__auto__){
return sb__5795__auto__.append(x__5796__auto__);
});
(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__45775_45986);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__45776_45987);

try{cljs.spec.alpha.explain_out(cljs.core.update.cljs$core$IFn$_invoke$arity$3(spec,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814),(function (probs){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__45756_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__45756_SHARP_,new cljs.core.Keyword(null,"in","in",-1531184865));
}),probs);
}))
);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__45774_45985);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__45773_45984);
}
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__5795__auto__));
})():(format.cljs$core$IFn$_invoke$arity$2 ? format.cljs$core$IFn$_invoke$arity$2("%s\n",cause) : format.call(null,"%s\n",cause)));
return (format.cljs$core$IFn$_invoke$arity$4 ? format.cljs$core$IFn$_invoke$arity$4(G__45766,G__45767,G__45768,G__45769) : format.call(null,G__45766,G__45767,G__45768,G__45769));

break;
case "macroexpansion":
var G__45782 = "Unexpected error%s macroexpanding %sat (%s).\n%s\n";
var G__45783 = cause_type;
var G__45784 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__45785 = loc;
var G__45786 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__45782,G__45783,G__45784,G__45785,G__45786) : format.call(null,G__45782,G__45783,G__45784,G__45785,G__45786));

break;
case "compile-syntax-check":
var G__45788 = "Syntax error%s compiling %sat (%s).\n%s\n";
var G__45789 = cause_type;
var G__45790 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__45791 = loc;
var G__45792 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__45788,G__45789,G__45790,G__45791,G__45792) : format.call(null,G__45788,G__45789,G__45790,G__45791,G__45792));

break;
case "compilation":
var G__45797 = "Unexpected error%s compiling %sat (%s).\n%s\n";
var G__45798 = cause_type;
var G__45799 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__45800 = loc;
var G__45801 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__45797,G__45798,G__45799,G__45800,G__45801) : format.call(null,G__45797,G__45798,G__45799,G__45800,G__45801));

break;
case "read-eval-result":
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5("Error reading eval result%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause) : format.call(null,"Error reading eval result%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause));

break;
case "print-eval-result":
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5("Error printing return value%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause) : format.call(null,"Error printing return value%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause));

break;
case "execution":
if(cljs.core.truth_(spec)){
var G__45805 = "Execution error - invalid arguments to %s at (%s).\n%s";
var G__45806 = symbol;
var G__45807 = loc;
var G__45808 = (function (){var sb__5795__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR__orig_val__45813_45996 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR__orig_val__45815_45997 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_newline_STAR__temp_val__45816_45998 = true;
var _STAR_print_fn_STAR__temp_val__45817_45999 = (function (x__5796__auto__){
return sb__5795__auto__.append(x__5796__auto__);
});
(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__45816_45998);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__45817_45999);

try{cljs.spec.alpha.explain_out(cljs.core.update.cljs$core$IFn$_invoke$arity$3(spec,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814),(function (probs){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__45757_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__45757_SHARP_,new cljs.core.Keyword(null,"in","in",-1531184865));
}),probs);
}))
);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__45815_45997);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__45813_45996);
}
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__5795__auto__));
})();
return (format.cljs$core$IFn$_invoke$arity$4 ? format.cljs$core$IFn$_invoke$arity$4(G__45805,G__45806,G__45807,G__45808) : format.call(null,G__45805,G__45806,G__45807,G__45808));
} else {
var G__45832 = "Execution error%s at %s(%s).\n%s\n";
var G__45833 = cause_type;
var G__45834 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__45835 = loc;
var G__45836 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__45832,G__45833,G__45834,G__45835,G__45836) : format.call(null,G__45832,G__45833,G__45834,G__45835,G__45836));
}

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__45765__$1))));

}
});
cljs.repl.error__GT_str = (function cljs$repl$error__GT_str(error){
return cljs.repl.ex_str(cljs.repl.ex_triage(cljs.repl.Error__GT_map(error)));
});

//# sourceMappingURL=cljs.repl.js.map
