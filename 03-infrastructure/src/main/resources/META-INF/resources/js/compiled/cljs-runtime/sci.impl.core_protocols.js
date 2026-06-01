goog.provide('sci.impl.core_protocols');
if((typeof sci !== 'undefined') && (typeof sci.impl !== 'undefined') && (typeof sci.impl.core_protocols !== 'undefined') && (typeof sci.impl.core_protocols._deref !== 'undefined')){
} else {
sci.impl.core_protocols._deref = (function (){var method_table__5747__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__5748__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__5749__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__5750__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__5751__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__33621 = cljs.core.get_global_hierarchy;
return (fexpr__33621.cljs$core$IFn$_invoke$arity$0 ? fexpr__33621.cljs$core$IFn$_invoke$arity$0() : fexpr__33621.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("sci.impl.core-protocols","-deref"),sci.impl.types.type_impl,new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__5751__auto__,method_table__5747__auto__,prefer_table__5748__auto__,method_cache__5749__auto__,cached_hierarchy__5750__auto__));
})();
}
sci.impl.core_protocols._deref.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("sci.impl.protocols","reified","sci.impl.protocols/reified",-2019939396),(function (ref){
var methods$ = sci.impl.types.getMethods(ref);
var fexpr__33628 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(methods$,new cljs.core.Symbol(null,"-deref","-deref",-283116853,null));
return (fexpr__33628.cljs$core$IFn$_invoke$arity$1 ? fexpr__33628.cljs$core$IFn$_invoke$arity$1(ref) : fexpr__33628.call(null,ref));
}));
sci.impl.core_protocols.ideref_default = sci.impl.core_protocols._deref.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (ref){
return cljs.core.deref(ref);
}));
sci.impl.core_protocols.deref_STAR_ = (function sci$impl$core_protocols$deref_STAR_(x){
return sci.impl.core_protocols._deref.cljs$core$IFn$_invoke$arity$1(x);
});
sci.impl.core_protocols.cljs_core_ns = sci.lang.__GT_Namespace(new cljs.core.Symbol(null,"cljs.core","cljs.core",770546058,null),null);
sci.impl.core_protocols.deref_protocol = sci.impl.utils.new_var.cljs$core$IFn$_invoke$arity$3(new cljs.core.Symbol(null,"cljs.core.IDeref","cljs.core.IDeref",-783543206,null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"protocol","protocol",652470118),cljs.core.IDeref,new cljs.core.Keyword(null,"methods","methods",453930866),cljs.core.PersistentHashSet.createAsIfByAssoc([sci.impl.core_protocols._deref]),new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.core_protocols.cljs_core_ns], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.core_protocols.cljs_core_ns], null));
if((typeof sci !== 'undefined') && (typeof sci.impl !== 'undefined') && (typeof sci.impl.core_protocols !== 'undefined') && (typeof sci.impl.core_protocols._swap_BANG_ !== 'undefined')){
} else {
sci.impl.core_protocols._swap_BANG_ = (function (){var method_table__5747__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__5748__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__5749__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__5750__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__5751__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__33659 = cljs.core.get_global_hierarchy;
return (fexpr__33659.cljs$core$IFn$_invoke$arity$0 ? fexpr__33659.cljs$core$IFn$_invoke$arity$0() : fexpr__33659.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("sci.impl.core-protocols","-swap!"),sci.impl.types.type_impl,new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__5751__auto__,method_table__5747__auto__,prefer_table__5748__auto__,method_cache__5749__auto__,cached_hierarchy__5750__auto__));
})();
}
if((typeof sci !== 'undefined') && (typeof sci.impl !== 'undefined') && (typeof sci.impl.core_protocols !== 'undefined') && (typeof sci.impl.core_protocols._reset_BANG_ !== 'undefined')){
} else {
sci.impl.core_protocols._reset_BANG_ = (function (){var method_table__5747__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__5748__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__5749__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__5750__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__5751__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__33670 = cljs.core.get_global_hierarchy;
return (fexpr__33670.cljs$core$IFn$_invoke$arity$0 ? fexpr__33670.cljs$core$IFn$_invoke$arity$0() : fexpr__33670.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("sci.impl.core-protocols","-reset!"),sci.impl.types.type_impl,new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__5751__auto__,method_table__5747__auto__,prefer_table__5748__auto__,method_cache__5749__auto__,cached_hierarchy__5750__auto__));
})();
}
sci.impl.core_protocols._swap_BANG_.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("sci.impl.protocols","reified","sci.impl.protocols/reified",-2019939396),(function() {
var G__33865 = null;
var G__33865__2 = (function (ref,f){
var methods$ = sci.impl.types.getMethods(ref);
var fexpr__33683 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(methods$,new cljs.core.Symbol(null,"-swap!","-swap!",-535359318,null));
return (fexpr__33683.cljs$core$IFn$_invoke$arity$2 ? fexpr__33683.cljs$core$IFn$_invoke$arity$2(ref,f) : fexpr__33683.call(null,ref,f));
});
var G__33865__3 = (function (ref,f,a1){
var methods$ = sci.impl.types.getMethods(ref);
var fexpr__33685 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(methods$,new cljs.core.Symbol(null,"-swap!","-swap!",-535359318,null));
return (fexpr__33685.cljs$core$IFn$_invoke$arity$3 ? fexpr__33685.cljs$core$IFn$_invoke$arity$3(ref,f,a1) : fexpr__33685.call(null,ref,f,a1));
});
var G__33865__4 = (function (ref,f,a1,a2){
var methods$ = sci.impl.types.getMethods(ref);
var fexpr__33697 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(methods$,new cljs.core.Symbol(null,"-swap!","-swap!",-535359318,null));
return (fexpr__33697.cljs$core$IFn$_invoke$arity$4 ? fexpr__33697.cljs$core$IFn$_invoke$arity$4(ref,f,a1,a2) : fexpr__33697.call(null,ref,f,a1,a2));
});
var G__33865__5 = (function() { 
var G__33886__delegate = function (ref,f,a1,a2,args){
var methods$ = sci.impl.types.getMethods(ref);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$variadic(cljs.core.get.cljs$core$IFn$_invoke$arity$2(methods$,new cljs.core.Symbol(null,"-swap!","-swap!",-535359318,null)),ref,f,a1,a2,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([args], 0));
};
var G__33886 = function (ref,f,a1,a2,var_args){
var args = null;
if (arguments.length > 4) {
var G__33923__i = 0, G__33923__a = new Array(arguments.length -  4);
while (G__33923__i < G__33923__a.length) {G__33923__a[G__33923__i] = arguments[G__33923__i + 4]; ++G__33923__i;}
  args = new cljs.core.IndexedSeq(G__33923__a,0,null);
} 
return G__33886__delegate.call(this,ref,f,a1,a2,args);};
G__33886.cljs$lang$maxFixedArity = 4;
G__33886.cljs$lang$applyTo = (function (arglist__33924){
var ref = cljs.core.first(arglist__33924);
arglist__33924 = cljs.core.next(arglist__33924);
var f = cljs.core.first(arglist__33924);
arglist__33924 = cljs.core.next(arglist__33924);
var a1 = cljs.core.first(arglist__33924);
arglist__33924 = cljs.core.next(arglist__33924);
var a2 = cljs.core.first(arglist__33924);
var args = cljs.core.rest(arglist__33924);
return G__33886__delegate(ref,f,a1,a2,args);
});
G__33886.cljs$core$IFn$_invoke$arity$variadic = G__33886__delegate;
return G__33886;
})()
;
G__33865 = function(ref,f,a1,a2,var_args){
var args = var_args;
switch(arguments.length){
case 2:
return G__33865__2.call(this,ref,f);
case 3:
return G__33865__3.call(this,ref,f,a1);
case 4:
return G__33865__4.call(this,ref,f,a1,a2);
default:
var G__33935 = null;
if (arguments.length > 4) {
var G__33936__i = 0, G__33936__a = new Array(arguments.length -  4);
while (G__33936__i < G__33936__a.length) {G__33936__a[G__33936__i] = arguments[G__33936__i + 4]; ++G__33936__i;}
G__33935 = new cljs.core.IndexedSeq(G__33936__a,0,null);
}
return G__33865__5.cljs$core$IFn$_invoke$arity$variadic(ref,f,a1,a2, G__33935);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__33865.cljs$lang$maxFixedArity = 4;
G__33865.cljs$lang$applyTo = G__33865__5.cljs$lang$applyTo;
G__33865.cljs$core$IFn$_invoke$arity$2 = G__33865__2;
G__33865.cljs$core$IFn$_invoke$arity$3 = G__33865__3;
G__33865.cljs$core$IFn$_invoke$arity$4 = G__33865__4;
G__33865.cljs$core$IFn$_invoke$arity$variadic = G__33865__5.cljs$core$IFn$_invoke$arity$variadic;
return G__33865;
})()
);
sci.impl.core_protocols._reset_BANG_.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("sci.impl.protocols","reified","sci.impl.protocols/reified",-2019939396),(function (ref,v){
var methods$ = sci.impl.types.getMethods(ref);
var fexpr__33713 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(methods$,new cljs.core.Symbol(null,"-reset!","-reset!",1965723739,null));
return (fexpr__33713.cljs$core$IFn$_invoke$arity$2 ? fexpr__33713.cljs$core$IFn$_invoke$arity$2(ref,v) : fexpr__33713.call(null,ref,v));
}));
sci.impl.core_protocols.iatom_defaults = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sci.impl.core_protocols._swap_BANG_.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function() { 
var G__33937__delegate = function (ref,f,args){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(cljs.core.swap_BANG_,ref,f,args);
};
var G__33937 = function (ref,f,var_args){
var args = null;
if (arguments.length > 2) {
var G__33938__i = 0, G__33938__a = new Array(arguments.length -  2);
while (G__33938__i < G__33938__a.length) {G__33938__a[G__33938__i] = arguments[G__33938__i + 2]; ++G__33938__i;}
  args = new cljs.core.IndexedSeq(G__33938__a,0,null);
} 
return G__33937__delegate.call(this,ref,f,args);};
G__33937.cljs$lang$maxFixedArity = 2;
G__33937.cljs$lang$applyTo = (function (arglist__33939){
var ref = cljs.core.first(arglist__33939);
arglist__33939 = cljs.core.next(arglist__33939);
var f = cljs.core.first(arglist__33939);
var args = cljs.core.rest(arglist__33939);
return G__33937__delegate(ref,f,args);
});
G__33937.cljs$core$IFn$_invoke$arity$variadic = G__33937__delegate;
return G__33937;
})()
),sci.impl.core_protocols._reset_BANG_.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (ref,v){
return cljs.core.reset_BANG_(ref,v);
}))], null);
sci.impl.core_protocols.swap_BANG__STAR_ = (function sci$impl$core_protocols$swap_BANG__STAR_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___33941 = arguments.length;
var i__5877__auto___33942 = (0);
while(true){
if((i__5877__auto___33942 < len__5876__auto___33941)){
args__5882__auto__.push((arguments[i__5877__auto___33942]));

var G__33943 = (i__5877__auto___33942 + (1));
i__5877__auto___33942 = G__33943;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return sci.impl.core_protocols.swap_BANG__STAR_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(sci.impl.core_protocols.swap_BANG__STAR_.cljs$core$IFn$_invoke$arity$variadic = (function (ref,f,args){
if(cljs.core.truth_(args)){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(sci.impl.core_protocols._swap_BANG_,ref,f,args);
} else {
return sci.impl.core_protocols._swap_BANG_.cljs$core$IFn$_invoke$arity$2(ref,f);
}
}));

(sci.impl.core_protocols.swap_BANG__STAR_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(sci.impl.core_protocols.swap_BANG__STAR_.cljs$lang$applyTo = (function (seq33748){
var G__33749 = cljs.core.first(seq33748);
var seq33748__$1 = cljs.core.next(seq33748);
var G__33750 = cljs.core.first(seq33748__$1);
var seq33748__$2 = cljs.core.next(seq33748__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__33749,G__33750,seq33748__$2);
}));

sci.impl.core_protocols.reset_BANG__STAR_ = (function sci$impl$core_protocols$reset_BANG__STAR_(ref,v){
return sci.impl.core_protocols._reset_BANG_.cljs$core$IFn$_invoke$arity$2(ref,v);
});
sci.impl.core_protocols.swap_protocol = sci.impl.utils.new_var.cljs$core$IFn$_invoke$arity$3(new cljs.core.Symbol(null,"cljs.core.ISwap","cljs.core.ISwap",2045511362,null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"protocol","protocol",652470118),cljs.core.ISwap,new cljs.core.Keyword(null,"methods","methods",453930866),cljs.core.PersistentHashSet.createAsIfByAssoc([sci.impl.core_protocols._swap_BANG_]),new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.core_protocols.cljs_core_ns], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.core_protocols.cljs_core_ns], null));
sci.impl.core_protocols.reset_protocol = sci.impl.utils.new_var.cljs$core$IFn$_invoke$arity$3(new cljs.core.Symbol(null,"cljs.core.IReset","cljs.core.IReset",348905844,null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"protocol","protocol",652470118),cljs.core.IReset,new cljs.core.Keyword(null,"methods","methods",453930866),cljs.core.PersistentHashSet.createAsIfByAssoc([sci.impl.core_protocols._reset_BANG_]),new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.core_protocols.cljs_core_ns], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ns","ns",441598760),sci.impl.core_protocols.cljs_core_ns], null));
sci.impl.core_protocols.defaults = cljs.core.set(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(sci.impl.core_protocols.iatom_defaults,sci.impl.core_protocols.ideref_default));

//# sourceMappingURL=sci.impl.core_protocols.js.map
