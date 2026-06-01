goog.provide('sci.impl.io');
/**
 * create a dynamic var with clojure.core :ns meta
 */
sci.impl.io.core_dynamic_var = (function sci$impl$io$core_dynamic_var(var_args){
var G__35096 = arguments.length;
switch (G__35096) {
case 1:
return sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$1 = (function (name){
return sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$2(name,null);
}));

(sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$2 = (function (name,init_val){
return sci.impl.utils.dynamic_var.cljs$core$IFn$_invoke$arity$3(name,init_val,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true], null));
}));

(sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$3 = (function (name,init_val,extra_meta){
return sci.impl.utils.dynamic_var.cljs$core$IFn$_invoke$arity$3(name,init_val,cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(extra_meta,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true], 0)));
}));

(sci.impl.io.core_dynamic_var.cljs$lang$maxFixedArity = 3);

sci.impl.io.in$ = (function (){var _STAR_unrestricted_STAR__orig_val__35099 = sci.impl.unrestrict._STAR_unrestricted_STAR_;
var _STAR_unrestricted_STAR__temp_val__35100 = true;
(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__temp_val__35100);

try{var G__35102 = sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"*in*","*in*",1130010229,null));
sci.impl.vars.unbind(G__35102);

return G__35102;
}finally {(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__orig_val__35099);
}})();
sci.impl.io.out = (function (){var _STAR_unrestricted_STAR__orig_val__35103 = sci.impl.unrestrict._STAR_unrestricted_STAR_;
var _STAR_unrestricted_STAR__temp_val__35104 = true;
(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__temp_val__35104);

try{var G__35105 = sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"*out*","*out*",1277591796,null));
sci.impl.vars.unbind(G__35105);

return G__35105;
}finally {(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__orig_val__35103);
}})();
sci.impl.io.err = (function (){var _STAR_unrestricted_STAR__orig_val__35106 = sci.impl.unrestrict._STAR_unrestricted_STAR_;
var _STAR_unrestricted_STAR__temp_val__35107 = true;
(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__temp_val__35107);

try{var G__35109 = sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"*err*","*err*",2070937226,null));
sci.impl.vars.unbind(G__35109);

return G__35109;
}finally {(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__orig_val__35106);
}})();
sci.impl.io.print_fn = (function (){var _STAR_unrestricted_STAR__orig_val__35111 = sci.impl.unrestrict._STAR_unrestricted_STAR_;
var _STAR_unrestricted_STAR__temp_val__35112 = true;
(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__temp_val__35112);

try{var G__35113 = sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"*print-fn*","*print-fn*",138509853,null));
sci.impl.vars.unbind(G__35113);

return G__35113;
}finally {(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__orig_val__35111);
}})();
sci.impl.io.print_err_fn = (function (){var _STAR_unrestricted_STAR__orig_val__35116 = sci.impl.unrestrict._STAR_unrestricted_STAR_;
var _STAR_unrestricted_STAR__temp_val__35117 = true;
(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__temp_val__35117);

try{var G__35119 = sci.impl.io.core_dynamic_var.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"*print-err-fn*","*print-err-fn*",1241679298,null));
sci.impl.vars.unbind(G__35119);

return G__35119;
}finally {(sci.impl.unrestrict._STAR_unrestricted_STAR_ = _STAR_unrestricted_STAR__orig_val__35116);
}})();
sci.impl.io.print_meta = (new sci.lang.Var(cljs.core._STAR_print_meta_STAR_,new cljs.core.Symbol(null,"*print-meta*","*print-meta*",-919406644,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*print-meta*","*print-meta*",-919406644,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"If set to logical true, when printing an object, its metadata will also\n  be printed in a form that can be read back by the reader.\n\n  Defaults to false.",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.print_length = (new sci.lang.Var(cljs.core._STAR_print_length_STAR_,new cljs.core.Symbol(null,"*print-length*","*print-length*",-687693654,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*print-length*","*print-length*",-687693654,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"*print-length* controls how many items of each collection the\n  printer will print. If it is bound to logical false, there is no\n  limit. Otherwise, it must be bound to an integer indicating the maximum\n  number of items of each collection to print. If a collection contains\n  more items, the printer will print items up to the limit followed by\n  '...' to represent the remaining items. The root binding is nil\n  indicating no limit.",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.print_level = (new sci.lang.Var(cljs.core._STAR_print_level_STAR_,new cljs.core.Symbol(null,"*print-level*","*print-level*",-634488505,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*print-level*","*print-level*",-634488505,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"*print-level* controls how many levels deep the printer will\n  print nested objects. If it is bound to logical false, there is no\n  limit. Otherwise, it must be bound to an integer indicating the maximum\n  level to print. Each argument to print is at level 0; if an argument is a\n  collection, its items are at level 1; and so on. If an object is a\n  collection and is at a level greater than or equal to the value bound to\n  *print-level*, the printer prints '#' to represent it. The root binding\n  is nil indicating no limit.",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.print_namespace_maps = (new sci.lang.Var(true,new cljs.core.Symbol(null,"*print-namespace-maps*","*print-namespace-maps*",-1759108415,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*print-namespace-maps*","*print-namespace-maps*",-1759108415,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"*print-namespace-maps* controls whether the printer will print\n  namespace map literal syntax.\n\n  Defaults to false, but the REPL binds it to true.",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.flush_on_newline = (new sci.lang.Var(cljs.core._STAR_flush_on_newline_STAR_,new cljs.core.Symbol(null,"*flush-on-newline*","*flush-on-newline*",-737526501,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*flush-on-newline*","*flush-on-newline*",-737526501,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"When set to true, output will be flushed whenever a newline is printed.\n\n  Defaults to true.",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.print_readably = (new sci.lang.Var(cljs.core._STAR_print_readably_STAR_,new cljs.core.Symbol(null,"*print-readably*","*print-readably*",-761361221,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*print-readably*","*print-readably*",-761361221,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"When set to logical false, strings and characters will be printed with\n  non-alphanumeric characters converted to the appropriate escape sequences.\n\n  Defaults to true",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.print_dup_var = (new sci.lang.Var(cljs.core._STAR_print_dup_STAR_,new cljs.core.Symbol(null,"*print-dup*","*print-dup*",103854877,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*print-dup*","*print-dup*",103854877,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"When set to logical true, objects will be printed in a way that preserves\n  their type when read in later.\n\n  Defaults to false.",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.print_newline = (new sci.lang.Var(cljs.core._STAR_print_newline_STAR_,new cljs.core.Symbol(null,"*print-newline*","*print-newline*",1478078956,null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Symbol(null,"*print-newline*","*print-newline*",1478078956,null),new cljs.core.Keyword(null,"arglists","arglists",1661989754),null,new cljs.core.Keyword(null,"doc","doc",1913296891),"When set to logical false will drop newlines from printing calls.\n  This is to work around the implicit newlines emitted by standard JavaScript\n  console objects.",new cljs.core.Keyword(null,"dynamic","dynamic",704819571),true,new cljs.core.Keyword("sci","built-in","sci/built-in",1244659599),true,new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.utils.clojure_core_ns], null),false,null,null));
sci.impl.io.string_print = (function sci$impl$io$string_print(x){
var _STAR_print_fn_STAR__orig_val__35122 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_fn_STAR__temp_val__35123 = cljs.core.deref(sci.impl.io.print_fn);
(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35123);

try{return cljs.core.string_print(x);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35122);
}});
sci.impl.io.pr = (function sci$impl$io$pr(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35341 = arguments.length;
var i__5877__auto___35343 = (0);
while(true){
if((i__5877__auto___35343 < len__5876__auto___35341)){
args__5882__auto__.push((arguments[i__5877__auto___35343]));

var G__35344 = (i__5877__auto___35343 + (1));
i__5877__auto___35343 = G__35344;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return sci.impl.io.pr.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(sci.impl.io.pr.cljs$core$IFn$_invoke$arity$variadic = (function (objs){
var _STAR_print_fn_STAR__orig_val__35131 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_length_STAR__orig_val__35132 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_level_STAR__orig_val__35133 = cljs.core._STAR_print_level_STAR_;
var _STAR_print_meta_STAR__orig_val__35134 = cljs.core._STAR_print_meta_STAR_;
var _STAR_print_namespace_maps_STAR__orig_val__35135 = cljs.core._STAR_print_namespace_maps_STAR_;
var _STAR_print_readably_STAR__orig_val__35136 = cljs.core._STAR_print_readably_STAR_;
var _STAR_print_newline_STAR__orig_val__35137 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_dup_STAR__orig_val__35138 = cljs.core._STAR_print_dup_STAR_;
var _STAR_print_fn_STAR__temp_val__35139 = cljs.core.deref(sci.impl.io.print_fn);
var _STAR_print_length_STAR__temp_val__35140 = cljs.core.deref(sci.impl.io.print_length);
var _STAR_print_level_STAR__temp_val__35141 = cljs.core.deref(sci.impl.io.print_level);
var _STAR_print_meta_STAR__temp_val__35142 = cljs.core.deref(sci.impl.io.print_meta);
var _STAR_print_namespace_maps_STAR__temp_val__35143 = cljs.core.deref(sci.impl.io.print_namespace_maps);
var _STAR_print_readably_STAR__temp_val__35144 = cljs.core.deref(sci.impl.io.print_readably);
var _STAR_print_newline_STAR__temp_val__35145 = cljs.core.deref(sci.impl.io.print_newline);
var _STAR_print_dup_STAR__temp_val__35146 = cljs.core.deref(sci.impl.io.print_dup_var);
(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35139);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__35140);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__temp_val__35141);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__temp_val__35142);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__temp_val__35143);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__temp_val__35144);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35145);

(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__temp_val__35146);

try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.pr,objs);
}finally {(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__orig_val__35138);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35137);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__orig_val__35136);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__orig_val__35135);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__orig_val__35134);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__orig_val__35133);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__35132);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35131);
}}));

(sci.impl.io.pr.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sci.impl.io.pr.cljs$lang$applyTo = (function (seq35127){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq35127));
}));

sci.impl.io.flush = (function sci$impl$io$flush(){
return null;
});
sci.impl.io.newline = (function sci$impl$io$newline(){
var _STAR_print_fn_STAR__orig_val__35150 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_fn_STAR__temp_val__35151 = cljs.core.deref(sci.impl.io.print_fn);
(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35151);

try{return cljs.core.newline.cljs$core$IFn$_invoke$arity$0();
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35150);
}});
/**
 * pr to a string, returning it
 */
sci.impl.io.pr_str = (function sci$impl$io$pr_str(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35352 = arguments.length;
var i__5877__auto___35353 = (0);
while(true){
if((i__5877__auto___35353 < len__5876__auto___35352)){
args__5882__auto__.push((arguments[i__5877__auto___35353]));

var G__35354 = (i__5877__auto___35353 + (1));
i__5877__auto___35353 = G__35354;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return sci.impl.io.pr_str.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(sci.impl.io.pr_str.cljs$core$IFn$_invoke$arity$variadic = (function (objs){
var _STAR_print_length_STAR__orig_val__35159 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_level_STAR__orig_val__35160 = cljs.core._STAR_print_level_STAR_;
var _STAR_print_meta_STAR__orig_val__35161 = cljs.core._STAR_print_meta_STAR_;
var _STAR_print_namespace_maps_STAR__orig_val__35162 = cljs.core._STAR_print_namespace_maps_STAR_;
var _STAR_print_readably_STAR__orig_val__35163 = cljs.core._STAR_print_readably_STAR_;
var _STAR_print_newline_STAR__orig_val__35164 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_dup_STAR__orig_val__35165 = cljs.core._STAR_print_dup_STAR_;
var _STAR_print_length_STAR__temp_val__35166 = cljs.core.deref(sci.impl.io.print_length);
var _STAR_print_level_STAR__temp_val__35167 = cljs.core.deref(sci.impl.io.print_level);
var _STAR_print_meta_STAR__temp_val__35168 = cljs.core.deref(sci.impl.io.print_meta);
var _STAR_print_namespace_maps_STAR__temp_val__35169 = cljs.core.deref(sci.impl.io.print_namespace_maps);
var _STAR_print_readably_STAR__temp_val__35170 = cljs.core.deref(sci.impl.io.print_readably);
var _STAR_print_newline_STAR__temp_val__35171 = cljs.core.deref(sci.impl.io.print_newline);
var _STAR_print_dup_STAR__temp_val__35172 = cljs.core.deref(sci.impl.io.print_dup_var);
(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__35166);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__temp_val__35167);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__temp_val__35168);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__temp_val__35169);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__temp_val__35170);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35171);

(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__temp_val__35172);

try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.pr_str,objs);
}finally {(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__orig_val__35165);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35164);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__orig_val__35163);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__orig_val__35162);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__orig_val__35161);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__orig_val__35160);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__35159);
}}));

(sci.impl.io.pr_str.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sci.impl.io.pr_str.cljs$lang$applyTo = (function (seq35156){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq35156));
}));

sci.impl.io.prn = (function sci$impl$io$prn(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35356 = arguments.length;
var i__5877__auto___35357 = (0);
while(true){
if((i__5877__auto___35357 < len__5876__auto___35356)){
args__5882__auto__.push((arguments[i__5877__auto___35357]));

var G__35358 = (i__5877__auto___35357 + (1));
i__5877__auto___35357 = G__35358;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return sci.impl.io.prn.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(sci.impl.io.prn.cljs$core$IFn$_invoke$arity$variadic = (function (objs){
var _STAR_print_fn_STAR__orig_val__35177 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_length_STAR__orig_val__35178 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_level_STAR__orig_val__35179 = cljs.core._STAR_print_level_STAR_;
var _STAR_print_meta_STAR__orig_val__35180 = cljs.core._STAR_print_meta_STAR_;
var _STAR_print_namespace_maps_STAR__orig_val__35181 = cljs.core._STAR_print_namespace_maps_STAR_;
var _STAR_print_readably_STAR__orig_val__35182 = cljs.core._STAR_print_readably_STAR_;
var _STAR_print_newline_STAR__orig_val__35183 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_dup_STAR__orig_val__35184 = cljs.core._STAR_print_dup_STAR_;
var _STAR_print_fn_STAR__temp_val__35185 = cljs.core.deref(sci.impl.io.print_fn);
var _STAR_print_length_STAR__temp_val__35186 = cljs.core.deref(sci.impl.io.print_length);
var _STAR_print_level_STAR__temp_val__35187 = cljs.core.deref(sci.impl.io.print_level);
var _STAR_print_meta_STAR__temp_val__35188 = cljs.core.deref(sci.impl.io.print_meta);
var _STAR_print_namespace_maps_STAR__temp_val__35189 = cljs.core.deref(sci.impl.io.print_namespace_maps);
var _STAR_print_readably_STAR__temp_val__35190 = cljs.core.deref(sci.impl.io.print_readably);
var _STAR_print_newline_STAR__temp_val__35191 = cljs.core.deref(sci.impl.io.print_newline);
var _STAR_print_dup_STAR__temp_val__35192 = cljs.core.deref(sci.impl.io.print_dup_var);
(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35185);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__35186);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__temp_val__35187);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__temp_val__35188);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__temp_val__35189);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__temp_val__35190);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35191);

(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__temp_val__35192);

try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.prn,objs);
}finally {(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__orig_val__35184);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35183);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__orig_val__35182);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__orig_val__35181);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__orig_val__35180);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__orig_val__35179);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__35178);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35177);
}}));

(sci.impl.io.prn.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sci.impl.io.prn.cljs$lang$applyTo = (function (seq35175){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq35175));
}));

/**
 * prn to a string, returning it
 */
sci.impl.io.prn_str = (function sci$impl$io$prn_str(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35363 = arguments.length;
var i__5877__auto___35364 = (0);
while(true){
if((i__5877__auto___35364 < len__5876__auto___35363)){
args__5882__auto__.push((arguments[i__5877__auto___35364]));

var G__35365 = (i__5877__auto___35364 + (1));
i__5877__auto___35364 = G__35365;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return sci.impl.io.prn_str.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(sci.impl.io.prn_str.cljs$core$IFn$_invoke$arity$variadic = (function (objs){
var _STAR_print_length_STAR__orig_val__35203 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_level_STAR__orig_val__35204 = cljs.core._STAR_print_level_STAR_;
var _STAR_print_meta_STAR__orig_val__35205 = cljs.core._STAR_print_meta_STAR_;
var _STAR_print_namespace_maps_STAR__orig_val__35206 = cljs.core._STAR_print_namespace_maps_STAR_;
var _STAR_print_readably_STAR__orig_val__35207 = cljs.core._STAR_print_readably_STAR_;
var _STAR_print_newline_STAR__orig_val__35208 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_dup_STAR__orig_val__35209 = cljs.core._STAR_print_dup_STAR_;
var _STAR_print_length_STAR__temp_val__35210 = cljs.core.deref(sci.impl.io.print_length);
var _STAR_print_level_STAR__temp_val__35211 = cljs.core.deref(sci.impl.io.print_level);
var _STAR_print_meta_STAR__temp_val__35212 = cljs.core.deref(sci.impl.io.print_meta);
var _STAR_print_namespace_maps_STAR__temp_val__35213 = cljs.core.deref(sci.impl.io.print_namespace_maps);
var _STAR_print_readably_STAR__temp_val__35214 = cljs.core.deref(sci.impl.io.print_readably);
var _STAR_print_newline_STAR__temp_val__35215 = cljs.core.deref(sci.impl.io.print_newline);
var _STAR_print_dup_STAR__temp_val__35216 = cljs.core.deref(sci.impl.io.print_dup_var);
(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__35210);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__temp_val__35211);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__temp_val__35212);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__temp_val__35213);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__temp_val__35214);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35215);

(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__temp_val__35216);

try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.prn_str,objs);
}finally {(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__orig_val__35209);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35208);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__orig_val__35207);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__orig_val__35206);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__orig_val__35205);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__orig_val__35204);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__35203);
}}));

(sci.impl.io.prn_str.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sci.impl.io.prn_str.cljs$lang$applyTo = (function (seq35198){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq35198));
}));

sci.impl.io.print = (function sci$impl$io$print(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35370 = arguments.length;
var i__5877__auto___35371 = (0);
while(true){
if((i__5877__auto___35371 < len__5876__auto___35370)){
args__5882__auto__.push((arguments[i__5877__auto___35371]));

var G__35372 = (i__5877__auto___35371 + (1));
i__5877__auto___35371 = G__35372;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return sci.impl.io.print.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(sci.impl.io.print.cljs$core$IFn$_invoke$arity$variadic = (function (objs){
var _STAR_print_fn_STAR__orig_val__35228 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_length_STAR__orig_val__35229 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_level_STAR__orig_val__35230 = cljs.core._STAR_print_level_STAR_;
var _STAR_print_namespace_maps_STAR__orig_val__35231 = cljs.core._STAR_print_namespace_maps_STAR_;
var _STAR_print_readably_STAR__orig_val__35232 = cljs.core._STAR_print_readably_STAR_;
var _STAR_print_newline_STAR__orig_val__35233 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_dup_STAR__orig_val__35234 = cljs.core._STAR_print_dup_STAR_;
var _STAR_print_fn_STAR__temp_val__35235 = cljs.core.deref(sci.impl.io.print_fn);
var _STAR_print_length_STAR__temp_val__35236 = cljs.core.deref(sci.impl.io.print_length);
var _STAR_print_level_STAR__temp_val__35237 = cljs.core.deref(sci.impl.io.print_level);
var _STAR_print_namespace_maps_STAR__temp_val__35238 = cljs.core.deref(sci.impl.io.print_namespace_maps);
var _STAR_print_readably_STAR__temp_val__35239 = null;
var _STAR_print_newline_STAR__temp_val__35240 = cljs.core.deref(sci.impl.io.print_newline);
var _STAR_print_dup_STAR__temp_val__35241 = cljs.core.deref(sci.impl.io.print_dup_var);
(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35235);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__35236);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__temp_val__35237);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__temp_val__35238);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__temp_val__35239);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35240);

(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__temp_val__35241);

try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.print,objs);
}finally {(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__orig_val__35234);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35233);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__orig_val__35232);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__orig_val__35231);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__orig_val__35230);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__35229);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35228);
}}));

(sci.impl.io.print.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sci.impl.io.print.cljs$lang$applyTo = (function (seq35226){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq35226));
}));

/**
 * print to a string, returning it
 */
sci.impl.io.print_str = (function sci$impl$io$print_str(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35373 = arguments.length;
var i__5877__auto___35374 = (0);
while(true){
if((i__5877__auto___35374 < len__5876__auto___35373)){
args__5882__auto__.push((arguments[i__5877__auto___35374]));

var G__35376 = (i__5877__auto___35374 + (1));
i__5877__auto___35374 = G__35376;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return sci.impl.io.print_str.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(sci.impl.io.print_str.cljs$core$IFn$_invoke$arity$variadic = (function (objs){
var _STAR_print_length_STAR__orig_val__35253 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_level_STAR__orig_val__35254 = cljs.core._STAR_print_level_STAR_;
var _STAR_print_meta_STAR__orig_val__35255 = cljs.core._STAR_print_meta_STAR_;
var _STAR_print_namespace_maps_STAR__orig_val__35256 = cljs.core._STAR_print_namespace_maps_STAR_;
var _STAR_print_readably_STAR__orig_val__35257 = cljs.core._STAR_print_readably_STAR_;
var _STAR_print_newline_STAR__orig_val__35258 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_dup_STAR__orig_val__35259 = cljs.core._STAR_print_dup_STAR_;
var _STAR_print_length_STAR__temp_val__35260 = cljs.core.deref(sci.impl.io.print_length);
var _STAR_print_level_STAR__temp_val__35261 = cljs.core.deref(sci.impl.io.print_level);
var _STAR_print_meta_STAR__temp_val__35262 = cljs.core.deref(sci.impl.io.print_meta);
var _STAR_print_namespace_maps_STAR__temp_val__35263 = cljs.core.deref(sci.impl.io.print_namespace_maps);
var _STAR_print_readably_STAR__temp_val__35264 = cljs.core.deref(sci.impl.io.print_readably);
var _STAR_print_newline_STAR__temp_val__35265 = cljs.core.deref(sci.impl.io.print_newline);
var _STAR_print_dup_STAR__temp_val__35266 = cljs.core.deref(sci.impl.io.print_dup_var);
(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__35260);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__temp_val__35261);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__temp_val__35262);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__temp_val__35263);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__temp_val__35264);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35265);

(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__temp_val__35266);

try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.print_str,objs);
}finally {(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__orig_val__35259);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35258);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__orig_val__35257);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__orig_val__35256);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__orig_val__35255);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__orig_val__35254);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__35253);
}}));

(sci.impl.io.print_str.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sci.impl.io.print_str.cljs$lang$applyTo = (function (seq35249){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq35249));
}));

sci.impl.io.println = (function sci$impl$io$println(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35382 = arguments.length;
var i__5877__auto___35383 = (0);
while(true){
if((i__5877__auto___35383 < len__5876__auto___35382)){
args__5882__auto__.push((arguments[i__5877__auto___35383]));

var G__35384 = (i__5877__auto___35383 + (1));
i__5877__auto___35383 = G__35384;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return sci.impl.io.println.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(sci.impl.io.println.cljs$core$IFn$_invoke$arity$variadic = (function (objs){
var _STAR_print_fn_STAR__orig_val__35276 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_length_STAR__orig_val__35277 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_level_STAR__orig_val__35278 = cljs.core._STAR_print_level_STAR_;
var _STAR_print_meta_STAR__orig_val__35279 = cljs.core._STAR_print_meta_STAR_;
var _STAR_print_namespace_maps_STAR__orig_val__35280 = cljs.core._STAR_print_namespace_maps_STAR_;
var _STAR_print_readably_STAR__orig_val__35281 = cljs.core._STAR_print_readably_STAR_;
var _STAR_print_newline_STAR__orig_val__35282 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_dup_STAR__orig_val__35283 = cljs.core._STAR_print_dup_STAR_;
var _STAR_print_fn_STAR__temp_val__35284 = cljs.core.deref(sci.impl.io.print_fn);
var _STAR_print_length_STAR__temp_val__35285 = cljs.core.deref(sci.impl.io.print_length);
var _STAR_print_level_STAR__temp_val__35286 = cljs.core.deref(sci.impl.io.print_level);
var _STAR_print_meta_STAR__temp_val__35287 = cljs.core.deref(sci.impl.io.print_meta);
var _STAR_print_namespace_maps_STAR__temp_val__35288 = cljs.core.deref(sci.impl.io.print_namespace_maps);
var _STAR_print_readably_STAR__temp_val__35289 = cljs.core.deref(sci.impl.io.print_readably);
var _STAR_print_newline_STAR__temp_val__35290 = cljs.core.deref(sci.impl.io.print_newline);
var _STAR_print_dup_STAR__temp_val__35291 = cljs.core.deref(sci.impl.io.print_dup_var);
(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35284);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__35285);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__temp_val__35286);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__temp_val__35287);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__temp_val__35288);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__temp_val__35289);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35290);

(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__temp_val__35291);

try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.println,objs);
}finally {(cljs.core._STAR_print_dup_STAR_ = _STAR_print_dup_STAR__orig_val__35283);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35282);

(cljs.core._STAR_print_readably_STAR_ = _STAR_print_readably_STAR__orig_val__35281);

(cljs.core._STAR_print_namespace_maps_STAR_ = _STAR_print_namespace_maps_STAR__orig_val__35280);

(cljs.core._STAR_print_meta_STAR_ = _STAR_print_meta_STAR__orig_val__35279);

(cljs.core._STAR_print_level_STAR_ = _STAR_print_level_STAR__orig_val__35278);

(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__35277);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35276);
}}));

(sci.impl.io.println.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sci.impl.io.println.cljs$lang$applyTo = (function (seq35270){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq35270));
}));

sci.impl.io.with_out_str = (function sci$impl$io$with_out_str(var_args){
var args__5882__auto__ = [];
var len__5876__auto___35387 = arguments.length;
var i__5877__auto___35388 = (0);
while(true){
if((i__5877__auto___35388 < len__5876__auto___35387)){
args__5882__auto__.push((arguments[i__5877__auto___35388]));

var G__35390 = (i__5877__auto___35388 + (1));
i__5877__auto___35388 = G__35390;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return sci.impl.io.with_out_str.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(sci.impl.io.with_out_str.cljs$core$IFn$_invoke$arity$variadic = (function (_,___$1,body){
return cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","let","cljs.core/let",-308701135,null),null,(1),null)),(new cljs.core.List(null,cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol(null,"s__35298__auto__","s__35298__auto__",-1949991504,null),null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol(null,"new","new",-444906321,null),null,(1),null)),(new cljs.core.List(null,new cljs.core.Symbol(null,"goog.string.StringBuffer","goog.string.StringBuffer",-1220229842,null),null,(1),null))))),null,(1),null)))))),null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","binding","cljs.core/binding",2050379843,null),null,(1),null)),(new cljs.core.List(null,cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","*print-newline*","cljs.core/*print-newline*",6231625,null),null,(1),null)),(new cljs.core.List(null,true,null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,new cljs.core.Symbol("cljs.core","*print-fn*","cljs.core/*print-fn*",1342365176,null),null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","fn","cljs.core/fn",-1065745098,null),null,(1),null)),(new cljs.core.List(null,cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$1((new cljs.core.List(null,new cljs.core.Symbol(null,"x__35299__auto__","x__35299__auto__",-320044091,null),null,(1),null)))))),null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol(null,".",".",1975675962,null),null,(1),null)),(new cljs.core.List(null,new cljs.core.Symbol(null,"s__35298__auto__","s__35298__auto__",-1949991504,null),null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,sci.impl.utils.allowed_append,null,(1),null)),(new cljs.core.List(null,new cljs.core.Symbol(null,"x__35299__auto__","x__35299__auto__",-320044091,null),null,(1),null))], 0)))),null,(1),null))], 0)))),null,(1),null))], 0))))),null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([body,(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","str","cljs.core/str",-1971828991,null),null,(1),null)),(new cljs.core.List(null,new cljs.core.Symbol(null,"s__35298__auto__","s__35298__auto__",-1949991504,null),null,(1),null))))),null,(1),null))], 0)))),null,(1),null))], 0))));
}));

(sci.impl.io.with_out_str.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(sci.impl.io.with_out_str.cljs$lang$applyTo = (function (seq35300){
var G__35301 = cljs.core.first(seq35300);
var seq35300__$1 = cljs.core.next(seq35300);
var G__35302 = cljs.core.first(seq35300__$1);
var seq35300__$2 = cljs.core.next(seq35300__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__35301,G__35302,seq35300__$2);
}));


//# sourceMappingURL=sci.impl.io.js.map
