goog.provide('reagent.debug');
reagent.debug.has_console = (typeof console !== 'undefined');
reagent.debug.tracking = false;
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.warnings !== 'undefined')){
} else {
reagent.debug.warnings = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.track_console !== 'undefined')){
} else {
reagent.debug.track_console = (function (){var o = ({});
(o.warn = (function() { 
var G__20447__delegate = function (args){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args)], 0));
};
var G__20447 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__20448__i = 0, G__20448__a = new Array(arguments.length -  0);
while (G__20448__i < G__20448__a.length) {G__20448__a[G__20448__i] = arguments[G__20448__i + 0]; ++G__20448__i;}
  args = new cljs.core.IndexedSeq(G__20448__a,0,null);
} 
return G__20447__delegate.call(this,args);};
G__20447.cljs$lang$maxFixedArity = 0;
G__20447.cljs$lang$applyTo = (function (arglist__20449){
var args = cljs.core.seq(arglist__20449);
return G__20447__delegate(args);
});
G__20447.cljs$core$IFn$_invoke$arity$variadic = G__20447__delegate;
return G__20447;
})()
);

(o.error = (function() { 
var G__20450__delegate = function (args){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args)], 0));
};
var G__20450 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__20451__i = 0, G__20451__a = new Array(arguments.length -  0);
while (G__20451__i < G__20451__a.length) {G__20451__a[G__20451__i] = arguments[G__20451__i + 0]; ++G__20451__i;}
  args = new cljs.core.IndexedSeq(G__20451__a,0,null);
} 
return G__20450__delegate.call(this,args);};
G__20450.cljs$lang$maxFixedArity = 0;
G__20450.cljs$lang$applyTo = (function (arglist__20452){
var args = cljs.core.seq(arglist__20452);
return G__20450__delegate(args);
});
G__20450.cljs$core$IFn$_invoke$arity$variadic = G__20450__delegate;
return G__20450;
})()
);

return o;
})();
}
reagent.debug.track_warnings = (function reagent$debug$track_warnings(f){
(reagent.debug.tracking = true);

cljs.core.reset_BANG_(reagent.debug.warnings,null);

(f.cljs$core$IFn$_invoke$arity$0 ? f.cljs$core$IFn$_invoke$arity$0() : f.call(null));

var warns = cljs.core.deref(reagent.debug.warnings);
cljs.core.reset_BANG_(reagent.debug.warnings,null);

(reagent.debug.tracking = false);

return warns;
});

//# sourceMappingURL=reagent.debug.js.map
