goog.provide('sci.impl.fns');
sci.impl.fns.fun = (function sci$impl$fns$fun(var_args){
var G__33498 = arguments.length;
switch (G__33498) {
case 5:
return sci.impl.fns.fun.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 11:
return sci.impl.fns.fun.cljs$core$IFn$_invoke$arity$11((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]),(arguments[(6)]),(arguments[(7)]),(arguments[(8)]),(arguments[(9)]),(arguments[(10)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(sci.impl.fns.fun.cljs$core$IFn$_invoke$arity$5 = (function (ctx,enclosed_array,fn_body,fn_name,macro_QMARK_){
return sci.impl.fns.fun.cljs$core$IFn$_invoke$arity$11(ctx,enclosed_array,fn_body,fn_name,macro_QMARK_,new cljs.core.Keyword(null,"fixed-arity","fixed-arity",1586445869).cljs$core$IFn$_invoke$arity$1(fn_body),new cljs.core.Keyword(null,"copy-enclosed->invocation","copy-enclosed->invocation",-1322388729).cljs$core$IFn$_invoke$arity$1(fn_body),new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(fn_body),new cljs.core.Keyword(null,"invoc-size","invoc-size",2053298058).cljs$core$IFn$_invoke$arity$1(fn_body),sci.impl.utils.current_ns_name(),new cljs.core.Keyword(null,"vararg-idx","vararg-idx",-590991228).cljs$core$IFn$_invoke$arity$1(fn_body));
}));

(sci.impl.fns.fun.cljs$core$IFn$_invoke$arity$11 = (function (ctx,enclosed_array,fn_body,fn_name,macro_QMARK_,fixed_arity,enclosed__GT_invocation,body,invoc_size,nsm,vararg_idx){
var f = (cljs.core.truth_(vararg_idx)?(function (){var G__33515 = fixed_arity;
switch (G__33515) {
case (0):
return (function() { 
var sci$impl$fns$arity_0__delegate = function (G__33516){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[vararg_idx] = G__33516);

while(true){
var ret__32382__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32382__auto__)){
continue;
} else {
return ret__32382__auto__;
}
break;
}
};
var sci$impl$fns$arity_0 = function (var_args){
var G__33516 = null;
if (arguments.length > 0) {
var G__34671__i = 0, G__34671__a = new Array(arguments.length -  0);
while (G__34671__i < G__34671__a.length) {G__34671__a[G__34671__i] = arguments[G__34671__i + 0]; ++G__34671__i;}
  G__33516 = new cljs.core.IndexedSeq(G__34671__a,0,null);
} 
return sci$impl$fns$arity_0__delegate.call(this,G__33516);};
sci$impl$fns$arity_0.cljs$lang$maxFixedArity = 0;
sci$impl$fns$arity_0.cljs$lang$applyTo = (function (arglist__34672){
var G__33516 = cljs.core.seq(arglist__34672);
return sci$impl$fns$arity_0__delegate(G__33516);
});
sci$impl$fns$arity_0.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_0__delegate;
return sci$impl$fns$arity_0;
})()
;

break;
case (1):
return (function() { 
var sci$impl$fns$arity_1__delegate = function (G__33523,G__33524){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33523);

(invoc_array[vararg_idx] = G__33524);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_1 = function (G__33523,var_args){
var G__33524 = null;
if (arguments.length > 1) {
var G__34673__i = 0, G__34673__a = new Array(arguments.length -  1);
while (G__34673__i < G__34673__a.length) {G__34673__a[G__34673__i] = arguments[G__34673__i + 1]; ++G__34673__i;}
  G__33524 = new cljs.core.IndexedSeq(G__34673__a,0,null);
} 
return sci$impl$fns$arity_1__delegate.call(this,G__33523,G__33524);};
sci$impl$fns$arity_1.cljs$lang$maxFixedArity = 1;
sci$impl$fns$arity_1.cljs$lang$applyTo = (function (arglist__34674){
var G__33523 = cljs.core.first(arglist__34674);
var G__33524 = cljs.core.rest(arglist__34674);
return sci$impl$fns$arity_1__delegate(G__33523,G__33524);
});
sci$impl$fns$arity_1.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_1__delegate;
return sci$impl$fns$arity_1;
})()
;

break;
case (2):
return (function() { 
var sci$impl$fns$arity_2__delegate = function (G__33570,G__33571,G__33572){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33570);

(invoc_array[(1)] = G__33571);

(invoc_array[vararg_idx] = G__33572);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_2 = function (G__33570,G__33571,var_args){
var G__33572 = null;
if (arguments.length > 2) {
var G__34675__i = 0, G__34675__a = new Array(arguments.length -  2);
while (G__34675__i < G__34675__a.length) {G__34675__a[G__34675__i] = arguments[G__34675__i + 2]; ++G__34675__i;}
  G__33572 = new cljs.core.IndexedSeq(G__34675__a,0,null);
} 
return sci$impl$fns$arity_2__delegate.call(this,G__33570,G__33571,G__33572);};
sci$impl$fns$arity_2.cljs$lang$maxFixedArity = 2;
sci$impl$fns$arity_2.cljs$lang$applyTo = (function (arglist__34676){
var G__33570 = cljs.core.first(arglist__34676);
arglist__34676 = cljs.core.next(arglist__34676);
var G__33571 = cljs.core.first(arglist__34676);
var G__33572 = cljs.core.rest(arglist__34676);
return sci$impl$fns$arity_2__delegate(G__33570,G__33571,G__33572);
});
sci$impl$fns$arity_2.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_2__delegate;
return sci$impl$fns$arity_2;
})()
;

break;
case (3):
return (function() { 
var sci$impl$fns$arity_3__delegate = function (G__33600,G__33601,G__33602,G__33603){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33600);

(invoc_array[(1)] = G__33601);

(invoc_array[(2)] = G__33602);

(invoc_array[vararg_idx] = G__33603);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_3 = function (G__33600,G__33601,G__33602,var_args){
var G__33603 = null;
if (arguments.length > 3) {
var G__34677__i = 0, G__34677__a = new Array(arguments.length -  3);
while (G__34677__i < G__34677__a.length) {G__34677__a[G__34677__i] = arguments[G__34677__i + 3]; ++G__34677__i;}
  G__33603 = new cljs.core.IndexedSeq(G__34677__a,0,null);
} 
return sci$impl$fns$arity_3__delegate.call(this,G__33600,G__33601,G__33602,G__33603);};
sci$impl$fns$arity_3.cljs$lang$maxFixedArity = 3;
sci$impl$fns$arity_3.cljs$lang$applyTo = (function (arglist__34678){
var G__33600 = cljs.core.first(arglist__34678);
arglist__34678 = cljs.core.next(arglist__34678);
var G__33601 = cljs.core.first(arglist__34678);
arglist__34678 = cljs.core.next(arglist__34678);
var G__33602 = cljs.core.first(arglist__34678);
var G__33603 = cljs.core.rest(arglist__34678);
return sci$impl$fns$arity_3__delegate(G__33600,G__33601,G__33602,G__33603);
});
sci$impl$fns$arity_3.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_3__delegate;
return sci$impl$fns$arity_3;
})()
;

break;
case (4):
return (function() { 
var sci$impl$fns$arity_4__delegate = function (G__33616,G__33617,G__33618,G__33619,G__33620){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33616);

(invoc_array[(1)] = G__33617);

(invoc_array[(2)] = G__33618);

(invoc_array[(3)] = G__33619);

(invoc_array[vararg_idx] = G__33620);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_4 = function (G__33616,G__33617,G__33618,G__33619,var_args){
var G__33620 = null;
if (arguments.length > 4) {
var G__34679__i = 0, G__34679__a = new Array(arguments.length -  4);
while (G__34679__i < G__34679__a.length) {G__34679__a[G__34679__i] = arguments[G__34679__i + 4]; ++G__34679__i;}
  G__33620 = new cljs.core.IndexedSeq(G__34679__a,0,null);
} 
return sci$impl$fns$arity_4__delegate.call(this,G__33616,G__33617,G__33618,G__33619,G__33620);};
sci$impl$fns$arity_4.cljs$lang$maxFixedArity = 4;
sci$impl$fns$arity_4.cljs$lang$applyTo = (function (arglist__34680){
var G__33616 = cljs.core.first(arglist__34680);
arglist__34680 = cljs.core.next(arglist__34680);
var G__33617 = cljs.core.first(arglist__34680);
arglist__34680 = cljs.core.next(arglist__34680);
var G__33618 = cljs.core.first(arglist__34680);
arglist__34680 = cljs.core.next(arglist__34680);
var G__33619 = cljs.core.first(arglist__34680);
var G__33620 = cljs.core.rest(arglist__34680);
return sci$impl$fns$arity_4__delegate(G__33616,G__33617,G__33618,G__33619,G__33620);
});
sci$impl$fns$arity_4.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_4__delegate;
return sci$impl$fns$arity_4;
})()
;

break;
case (5):
return (function() { 
var sci$impl$fns$arity_5__delegate = function (G__33622,G__33623,G__33624,G__33625,G__33626,G__33627){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33622);

(invoc_array[(1)] = G__33623);

(invoc_array[(2)] = G__33624);

(invoc_array[(3)] = G__33625);

(invoc_array[(4)] = G__33626);

(invoc_array[vararg_idx] = G__33627);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_5 = function (G__33622,G__33623,G__33624,G__33625,G__33626,var_args){
var G__33627 = null;
if (arguments.length > 5) {
var G__34683__i = 0, G__34683__a = new Array(arguments.length -  5);
while (G__34683__i < G__34683__a.length) {G__34683__a[G__34683__i] = arguments[G__34683__i + 5]; ++G__34683__i;}
  G__33627 = new cljs.core.IndexedSeq(G__34683__a,0,null);
} 
return sci$impl$fns$arity_5__delegate.call(this,G__33622,G__33623,G__33624,G__33625,G__33626,G__33627);};
sci$impl$fns$arity_5.cljs$lang$maxFixedArity = 5;
sci$impl$fns$arity_5.cljs$lang$applyTo = (function (arglist__34684){
var G__33622 = cljs.core.first(arglist__34684);
arglist__34684 = cljs.core.next(arglist__34684);
var G__33623 = cljs.core.first(arglist__34684);
arglist__34684 = cljs.core.next(arglist__34684);
var G__33624 = cljs.core.first(arglist__34684);
arglist__34684 = cljs.core.next(arglist__34684);
var G__33625 = cljs.core.first(arglist__34684);
arglist__34684 = cljs.core.next(arglist__34684);
var G__33626 = cljs.core.first(arglist__34684);
var G__33627 = cljs.core.rest(arglist__34684);
return sci$impl$fns$arity_5__delegate(G__33622,G__33623,G__33624,G__33625,G__33626,G__33627);
});
sci$impl$fns$arity_5.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_5__delegate;
return sci$impl$fns$arity_5;
})()
;

break;
case (6):
return (function() { 
var sci$impl$fns$arity_6__delegate = function (G__33634,G__33635,G__33636,G__33637,G__33638,G__33639,G__33640){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33634);

(invoc_array[(1)] = G__33635);

(invoc_array[(2)] = G__33636);

(invoc_array[(3)] = G__33637);

(invoc_array[(4)] = G__33638);

(invoc_array[(5)] = G__33639);

(invoc_array[vararg_idx] = G__33640);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_6 = function (G__33634,G__33635,G__33636,G__33637,G__33638,G__33639,var_args){
var G__33640 = null;
if (arguments.length > 6) {
var G__34685__i = 0, G__34685__a = new Array(arguments.length -  6);
while (G__34685__i < G__34685__a.length) {G__34685__a[G__34685__i] = arguments[G__34685__i + 6]; ++G__34685__i;}
  G__33640 = new cljs.core.IndexedSeq(G__34685__a,0,null);
} 
return sci$impl$fns$arity_6__delegate.call(this,G__33634,G__33635,G__33636,G__33637,G__33638,G__33639,G__33640);};
sci$impl$fns$arity_6.cljs$lang$maxFixedArity = 6;
sci$impl$fns$arity_6.cljs$lang$applyTo = (function (arglist__34687){
var G__33634 = cljs.core.first(arglist__34687);
arglist__34687 = cljs.core.next(arglist__34687);
var G__33635 = cljs.core.first(arglist__34687);
arglist__34687 = cljs.core.next(arglist__34687);
var G__33636 = cljs.core.first(arglist__34687);
arglist__34687 = cljs.core.next(arglist__34687);
var G__33637 = cljs.core.first(arglist__34687);
arglist__34687 = cljs.core.next(arglist__34687);
var G__33638 = cljs.core.first(arglist__34687);
arglist__34687 = cljs.core.next(arglist__34687);
var G__33639 = cljs.core.first(arglist__34687);
var G__33640 = cljs.core.rest(arglist__34687);
return sci$impl$fns$arity_6__delegate(G__33634,G__33635,G__33636,G__33637,G__33638,G__33639,G__33640);
});
sci$impl$fns$arity_6.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_6__delegate;
return sci$impl$fns$arity_6;
})()
;

break;
case (7):
return (function() { 
var sci$impl$fns$arity_7__delegate = function (G__33650,G__33651,G__33652,G__33653,G__33654,G__33655,G__33656,G__33657){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33650);

(invoc_array[(1)] = G__33651);

(invoc_array[(2)] = G__33652);

(invoc_array[(3)] = G__33653);

(invoc_array[(4)] = G__33654);

(invoc_array[(5)] = G__33655);

(invoc_array[(6)] = G__33656);

(invoc_array[vararg_idx] = G__33657);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_7 = function (G__33650,G__33651,G__33652,G__33653,G__33654,G__33655,G__33656,var_args){
var G__33657 = null;
if (arguments.length > 7) {
var G__34691__i = 0, G__34691__a = new Array(arguments.length -  7);
while (G__34691__i < G__34691__a.length) {G__34691__a[G__34691__i] = arguments[G__34691__i + 7]; ++G__34691__i;}
  G__33657 = new cljs.core.IndexedSeq(G__34691__a,0,null);
} 
return sci$impl$fns$arity_7__delegate.call(this,G__33650,G__33651,G__33652,G__33653,G__33654,G__33655,G__33656,G__33657);};
sci$impl$fns$arity_7.cljs$lang$maxFixedArity = 7;
sci$impl$fns$arity_7.cljs$lang$applyTo = (function (arglist__34692){
var G__33650 = cljs.core.first(arglist__34692);
arglist__34692 = cljs.core.next(arglist__34692);
var G__33651 = cljs.core.first(arglist__34692);
arglist__34692 = cljs.core.next(arglist__34692);
var G__33652 = cljs.core.first(arglist__34692);
arglist__34692 = cljs.core.next(arglist__34692);
var G__33653 = cljs.core.first(arglist__34692);
arglist__34692 = cljs.core.next(arglist__34692);
var G__33654 = cljs.core.first(arglist__34692);
arglist__34692 = cljs.core.next(arglist__34692);
var G__33655 = cljs.core.first(arglist__34692);
arglist__34692 = cljs.core.next(arglist__34692);
var G__33656 = cljs.core.first(arglist__34692);
var G__33657 = cljs.core.rest(arglist__34692);
return sci$impl$fns$arity_7__delegate(G__33650,G__33651,G__33652,G__33653,G__33654,G__33655,G__33656,G__33657);
});
sci$impl$fns$arity_7.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_7__delegate;
return sci$impl$fns$arity_7;
})()
;

break;
case (8):
return (function() { 
var sci$impl$fns$arity_8__delegate = function (G__33661,G__33662,G__33663,G__33664,G__33665,G__33666,G__33667,G__33668,G__33669){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33661);

(invoc_array[(1)] = G__33662);

(invoc_array[(2)] = G__33663);

(invoc_array[(3)] = G__33664);

(invoc_array[(4)] = G__33665);

(invoc_array[(5)] = G__33666);

(invoc_array[(6)] = G__33667);

(invoc_array[(7)] = G__33668);

(invoc_array[vararg_idx] = G__33669);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_8 = function (G__33661,G__33662,G__33663,G__33664,G__33665,G__33666,G__33667,G__33668,var_args){
var G__33669 = null;
if (arguments.length > 8) {
var G__34693__i = 0, G__34693__a = new Array(arguments.length -  8);
while (G__34693__i < G__34693__a.length) {G__34693__a[G__34693__i] = arguments[G__34693__i + 8]; ++G__34693__i;}
  G__33669 = new cljs.core.IndexedSeq(G__34693__a,0,null);
} 
return sci$impl$fns$arity_8__delegate.call(this,G__33661,G__33662,G__33663,G__33664,G__33665,G__33666,G__33667,G__33668,G__33669);};
sci$impl$fns$arity_8.cljs$lang$maxFixedArity = 8;
sci$impl$fns$arity_8.cljs$lang$applyTo = (function (arglist__34694){
var G__33661 = cljs.core.first(arglist__34694);
arglist__34694 = cljs.core.next(arglist__34694);
var G__33662 = cljs.core.first(arglist__34694);
arglist__34694 = cljs.core.next(arglist__34694);
var G__33663 = cljs.core.first(arglist__34694);
arglist__34694 = cljs.core.next(arglist__34694);
var G__33664 = cljs.core.first(arglist__34694);
arglist__34694 = cljs.core.next(arglist__34694);
var G__33665 = cljs.core.first(arglist__34694);
arglist__34694 = cljs.core.next(arglist__34694);
var G__33666 = cljs.core.first(arglist__34694);
arglist__34694 = cljs.core.next(arglist__34694);
var G__33667 = cljs.core.first(arglist__34694);
arglist__34694 = cljs.core.next(arglist__34694);
var G__33668 = cljs.core.first(arglist__34694);
var G__33669 = cljs.core.rest(arglist__34694);
return sci$impl$fns$arity_8__delegate(G__33661,G__33662,G__33663,G__33664,G__33665,G__33666,G__33667,G__33668,G__33669);
});
sci$impl$fns$arity_8.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_8__delegate;
return sci$impl$fns$arity_8;
})()
;

break;
case (9):
return (function() { 
var sci$impl$fns$arity_9__delegate = function (G__33673,G__33674,G__33675,G__33676,G__33677,G__33678,G__33679,G__33680,G__33681,G__33682){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33673);

(invoc_array[(1)] = G__33674);

(invoc_array[(2)] = G__33675);

(invoc_array[(3)] = G__33676);

(invoc_array[(4)] = G__33677);

(invoc_array[(5)] = G__33678);

(invoc_array[(6)] = G__33679);

(invoc_array[(7)] = G__33680);

(invoc_array[(8)] = G__33681);

(invoc_array[vararg_idx] = G__33682);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_9 = function (G__33673,G__33674,G__33675,G__33676,G__33677,G__33678,G__33679,G__33680,G__33681,var_args){
var G__33682 = null;
if (arguments.length > 9) {
var G__34696__i = 0, G__34696__a = new Array(arguments.length -  9);
while (G__34696__i < G__34696__a.length) {G__34696__a[G__34696__i] = arguments[G__34696__i + 9]; ++G__34696__i;}
  G__33682 = new cljs.core.IndexedSeq(G__34696__a,0,null);
} 
return sci$impl$fns$arity_9__delegate.call(this,G__33673,G__33674,G__33675,G__33676,G__33677,G__33678,G__33679,G__33680,G__33681,G__33682);};
sci$impl$fns$arity_9.cljs$lang$maxFixedArity = 9;
sci$impl$fns$arity_9.cljs$lang$applyTo = (function (arglist__34697){
var G__33673 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33674 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33675 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33676 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33677 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33678 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33679 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33680 = cljs.core.first(arglist__34697);
arglist__34697 = cljs.core.next(arglist__34697);
var G__33681 = cljs.core.first(arglist__34697);
var G__33682 = cljs.core.rest(arglist__34697);
return sci$impl$fns$arity_9__delegate(G__33673,G__33674,G__33675,G__33676,G__33677,G__33678,G__33679,G__33680,G__33681,G__33682);
});
sci$impl$fns$arity_9.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_9__delegate;
return sci$impl$fns$arity_9;
})()
;

break;
case (10):
return (function() { 
var sci$impl$fns$arity_10__delegate = function (G__33686,G__33687,G__33688,G__33689,G__33690,G__33691,G__33692,G__33693,G__33694,G__33695,G__33696){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33686);

(invoc_array[(1)] = G__33687);

(invoc_array[(2)] = G__33688);

(invoc_array[(3)] = G__33689);

(invoc_array[(4)] = G__33690);

(invoc_array[(5)] = G__33691);

(invoc_array[(6)] = G__33692);

(invoc_array[(7)] = G__33693);

(invoc_array[(8)] = G__33694);

(invoc_array[(9)] = G__33695);

(invoc_array[vararg_idx] = G__33696);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_10 = function (G__33686,G__33687,G__33688,G__33689,G__33690,G__33691,G__33692,G__33693,G__33694,G__33695,var_args){
var G__33696 = null;
if (arguments.length > 10) {
var G__34703__i = 0, G__34703__a = new Array(arguments.length -  10);
while (G__34703__i < G__34703__a.length) {G__34703__a[G__34703__i] = arguments[G__34703__i + 10]; ++G__34703__i;}
  G__33696 = new cljs.core.IndexedSeq(G__34703__a,0,null);
} 
return sci$impl$fns$arity_10__delegate.call(this,G__33686,G__33687,G__33688,G__33689,G__33690,G__33691,G__33692,G__33693,G__33694,G__33695,G__33696);};
sci$impl$fns$arity_10.cljs$lang$maxFixedArity = 10;
sci$impl$fns$arity_10.cljs$lang$applyTo = (function (arglist__34704){
var G__33686 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33687 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33688 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33689 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33690 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33691 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33692 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33693 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33694 = cljs.core.first(arglist__34704);
arglist__34704 = cljs.core.next(arglist__34704);
var G__33695 = cljs.core.first(arglist__34704);
var G__33696 = cljs.core.rest(arglist__34704);
return sci$impl$fns$arity_10__delegate(G__33686,G__33687,G__33688,G__33689,G__33690,G__33691,G__33692,G__33693,G__33694,G__33695,G__33696);
});
sci$impl$fns$arity_10.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_10__delegate;
return sci$impl$fns$arity_10;
})()
;

break;
case (11):
return (function() { 
var sci$impl$fns$arity_11__delegate = function (G__33714,G__33715,G__33716,G__33717,G__33718,G__33719,G__33720,G__33721,G__33722,G__33723,G__33724,G__33725){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33714);

(invoc_array[(1)] = G__33715);

(invoc_array[(2)] = G__33716);

(invoc_array[(3)] = G__33717);

(invoc_array[(4)] = G__33718);

(invoc_array[(5)] = G__33719);

(invoc_array[(6)] = G__33720);

(invoc_array[(7)] = G__33721);

(invoc_array[(8)] = G__33722);

(invoc_array[(9)] = G__33723);

(invoc_array[(10)] = G__33724);

(invoc_array[vararg_idx] = G__33725);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_11 = function (G__33714,G__33715,G__33716,G__33717,G__33718,G__33719,G__33720,G__33721,G__33722,G__33723,G__33724,var_args){
var G__33725 = null;
if (arguments.length > 11) {
var G__34709__i = 0, G__34709__a = new Array(arguments.length -  11);
while (G__34709__i < G__34709__a.length) {G__34709__a[G__34709__i] = arguments[G__34709__i + 11]; ++G__34709__i;}
  G__33725 = new cljs.core.IndexedSeq(G__34709__a,0,null);
} 
return sci$impl$fns$arity_11__delegate.call(this,G__33714,G__33715,G__33716,G__33717,G__33718,G__33719,G__33720,G__33721,G__33722,G__33723,G__33724,G__33725);};
sci$impl$fns$arity_11.cljs$lang$maxFixedArity = 11;
sci$impl$fns$arity_11.cljs$lang$applyTo = (function (arglist__34710){
var G__33714 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33715 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33716 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33717 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33718 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33719 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33720 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33721 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33722 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33723 = cljs.core.first(arglist__34710);
arglist__34710 = cljs.core.next(arglist__34710);
var G__33724 = cljs.core.first(arglist__34710);
var G__33725 = cljs.core.rest(arglist__34710);
return sci$impl$fns$arity_11__delegate(G__33714,G__33715,G__33716,G__33717,G__33718,G__33719,G__33720,G__33721,G__33722,G__33723,G__33724,G__33725);
});
sci$impl$fns$arity_11.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_11__delegate;
return sci$impl$fns$arity_11;
})()
;

break;
case (12):
return (function() { 
var sci$impl$fns$arity_12__delegate = function (G__33733,G__33734,G__33735,G__33736,G__33737,G__33738,G__33739,G__33740,G__33741,G__33742,G__33743,G__33744,G__33745){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33733);

(invoc_array[(1)] = G__33734);

(invoc_array[(2)] = G__33735);

(invoc_array[(3)] = G__33736);

(invoc_array[(4)] = G__33737);

(invoc_array[(5)] = G__33738);

(invoc_array[(6)] = G__33739);

(invoc_array[(7)] = G__33740);

(invoc_array[(8)] = G__33741);

(invoc_array[(9)] = G__33742);

(invoc_array[(10)] = G__33743);

(invoc_array[(11)] = G__33744);

(invoc_array[vararg_idx] = G__33745);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_12 = function (G__33733,G__33734,G__33735,G__33736,G__33737,G__33738,G__33739,G__33740,G__33741,G__33742,G__33743,G__33744,var_args){
var G__33745 = null;
if (arguments.length > 12) {
var G__34711__i = 0, G__34711__a = new Array(arguments.length -  12);
while (G__34711__i < G__34711__a.length) {G__34711__a[G__34711__i] = arguments[G__34711__i + 12]; ++G__34711__i;}
  G__33745 = new cljs.core.IndexedSeq(G__34711__a,0,null);
} 
return sci$impl$fns$arity_12__delegate.call(this,G__33733,G__33734,G__33735,G__33736,G__33737,G__33738,G__33739,G__33740,G__33741,G__33742,G__33743,G__33744,G__33745);};
sci$impl$fns$arity_12.cljs$lang$maxFixedArity = 12;
sci$impl$fns$arity_12.cljs$lang$applyTo = (function (arglist__34714){
var G__33733 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33734 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33735 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33736 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33737 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33738 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33739 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33740 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33741 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33742 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33743 = cljs.core.first(arglist__34714);
arglist__34714 = cljs.core.next(arglist__34714);
var G__33744 = cljs.core.first(arglist__34714);
var G__33745 = cljs.core.rest(arglist__34714);
return sci$impl$fns$arity_12__delegate(G__33733,G__33734,G__33735,G__33736,G__33737,G__33738,G__33739,G__33740,G__33741,G__33742,G__33743,G__33744,G__33745);
});
sci$impl$fns$arity_12.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_12__delegate;
return sci$impl$fns$arity_12;
})()
;

break;
case (13):
return (function() { 
var sci$impl$fns$arity_13__delegate = function (G__33758,G__33759,G__33760,G__33761,G__33762,G__33763,G__33764,G__33765,G__33766,G__33767,G__33768,G__33769,G__33770,G__33771){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33758);

(invoc_array[(1)] = G__33759);

(invoc_array[(2)] = G__33760);

(invoc_array[(3)] = G__33761);

(invoc_array[(4)] = G__33762);

(invoc_array[(5)] = G__33763);

(invoc_array[(6)] = G__33764);

(invoc_array[(7)] = G__33765);

(invoc_array[(8)] = G__33766);

(invoc_array[(9)] = G__33767);

(invoc_array[(10)] = G__33768);

(invoc_array[(11)] = G__33769);

(invoc_array[(12)] = G__33770);

(invoc_array[vararg_idx] = G__33771);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_13 = function (G__33758,G__33759,G__33760,G__33761,G__33762,G__33763,G__33764,G__33765,G__33766,G__33767,G__33768,G__33769,G__33770,var_args){
var G__33771 = null;
if (arguments.length > 13) {
var G__34717__i = 0, G__34717__a = new Array(arguments.length -  13);
while (G__34717__i < G__34717__a.length) {G__34717__a[G__34717__i] = arguments[G__34717__i + 13]; ++G__34717__i;}
  G__33771 = new cljs.core.IndexedSeq(G__34717__a,0,null);
} 
return sci$impl$fns$arity_13__delegate.call(this,G__33758,G__33759,G__33760,G__33761,G__33762,G__33763,G__33764,G__33765,G__33766,G__33767,G__33768,G__33769,G__33770,G__33771);};
sci$impl$fns$arity_13.cljs$lang$maxFixedArity = 13;
sci$impl$fns$arity_13.cljs$lang$applyTo = (function (arglist__34718){
var G__33758 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33759 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33760 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33761 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33762 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33763 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33764 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33765 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33766 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33767 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33768 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33769 = cljs.core.first(arglist__34718);
arglist__34718 = cljs.core.next(arglist__34718);
var G__33770 = cljs.core.first(arglist__34718);
var G__33771 = cljs.core.rest(arglist__34718);
return sci$impl$fns$arity_13__delegate(G__33758,G__33759,G__33760,G__33761,G__33762,G__33763,G__33764,G__33765,G__33766,G__33767,G__33768,G__33769,G__33770,G__33771);
});
sci$impl$fns$arity_13.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_13__delegate;
return sci$impl$fns$arity_13;
})()
;

break;
case (14):
return (function() { 
var sci$impl$fns$arity_14__delegate = function (G__33777,G__33778,G__33779,G__33780,G__33781,G__33782,G__33783,G__33784,G__33785,G__33786,G__33787,G__33788,G__33789,G__33790,G__33791){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33777);

(invoc_array[(1)] = G__33778);

(invoc_array[(2)] = G__33779);

(invoc_array[(3)] = G__33780);

(invoc_array[(4)] = G__33781);

(invoc_array[(5)] = G__33782);

(invoc_array[(6)] = G__33783);

(invoc_array[(7)] = G__33784);

(invoc_array[(8)] = G__33785);

(invoc_array[(9)] = G__33786);

(invoc_array[(10)] = G__33787);

(invoc_array[(11)] = G__33788);

(invoc_array[(12)] = G__33789);

(invoc_array[(13)] = G__33790);

(invoc_array[vararg_idx] = G__33791);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_14 = function (G__33777,G__33778,G__33779,G__33780,G__33781,G__33782,G__33783,G__33784,G__33785,G__33786,G__33787,G__33788,G__33789,G__33790,var_args){
var G__33791 = null;
if (arguments.length > 14) {
var G__34719__i = 0, G__34719__a = new Array(arguments.length -  14);
while (G__34719__i < G__34719__a.length) {G__34719__a[G__34719__i] = arguments[G__34719__i + 14]; ++G__34719__i;}
  G__33791 = new cljs.core.IndexedSeq(G__34719__a,0,null);
} 
return sci$impl$fns$arity_14__delegate.call(this,G__33777,G__33778,G__33779,G__33780,G__33781,G__33782,G__33783,G__33784,G__33785,G__33786,G__33787,G__33788,G__33789,G__33790,G__33791);};
sci$impl$fns$arity_14.cljs$lang$maxFixedArity = 14;
sci$impl$fns$arity_14.cljs$lang$applyTo = (function (arglist__34720){
var G__33777 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33778 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33779 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33780 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33781 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33782 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33783 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33784 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33785 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33786 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33787 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33788 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33789 = cljs.core.first(arglist__34720);
arglist__34720 = cljs.core.next(arglist__34720);
var G__33790 = cljs.core.first(arglist__34720);
var G__33791 = cljs.core.rest(arglist__34720);
return sci$impl$fns$arity_14__delegate(G__33777,G__33778,G__33779,G__33780,G__33781,G__33782,G__33783,G__33784,G__33785,G__33786,G__33787,G__33788,G__33789,G__33790,G__33791);
});
sci$impl$fns$arity_14.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_14__delegate;
return sci$impl$fns$arity_14;
})()
;

break;
case (15):
return (function() { 
var sci$impl$fns$arity_15__delegate = function (G__33797,G__33798,G__33799,G__33800,G__33801,G__33802,G__33803,G__33804,G__33805,G__33806,G__33807,G__33808,G__33809,G__33810,G__33811,G__33812){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33797);

(invoc_array[(1)] = G__33798);

(invoc_array[(2)] = G__33799);

(invoc_array[(3)] = G__33800);

(invoc_array[(4)] = G__33801);

(invoc_array[(5)] = G__33802);

(invoc_array[(6)] = G__33803);

(invoc_array[(7)] = G__33804);

(invoc_array[(8)] = G__33805);

(invoc_array[(9)] = G__33806);

(invoc_array[(10)] = G__33807);

(invoc_array[(11)] = G__33808);

(invoc_array[(12)] = G__33809);

(invoc_array[(13)] = G__33810);

(invoc_array[(14)] = G__33811);

(invoc_array[vararg_idx] = G__33812);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_15 = function (G__33797,G__33798,G__33799,G__33800,G__33801,G__33802,G__33803,G__33804,G__33805,G__33806,G__33807,G__33808,G__33809,G__33810,G__33811,var_args){
var G__33812 = null;
if (arguments.length > 15) {
var G__34727__i = 0, G__34727__a = new Array(arguments.length -  15);
while (G__34727__i < G__34727__a.length) {G__34727__a[G__34727__i] = arguments[G__34727__i + 15]; ++G__34727__i;}
  G__33812 = new cljs.core.IndexedSeq(G__34727__a,0,null);
} 
return sci$impl$fns$arity_15__delegate.call(this,G__33797,G__33798,G__33799,G__33800,G__33801,G__33802,G__33803,G__33804,G__33805,G__33806,G__33807,G__33808,G__33809,G__33810,G__33811,G__33812);};
sci$impl$fns$arity_15.cljs$lang$maxFixedArity = 15;
sci$impl$fns$arity_15.cljs$lang$applyTo = (function (arglist__34728){
var G__33797 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33798 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33799 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33800 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33801 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33802 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33803 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33804 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33805 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33806 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33807 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33808 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33809 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33810 = cljs.core.first(arglist__34728);
arglist__34728 = cljs.core.next(arglist__34728);
var G__33811 = cljs.core.first(arglist__34728);
var G__33812 = cljs.core.rest(arglist__34728);
return sci$impl$fns$arity_15__delegate(G__33797,G__33798,G__33799,G__33800,G__33801,G__33802,G__33803,G__33804,G__33805,G__33806,G__33807,G__33808,G__33809,G__33810,G__33811,G__33812);
});
sci$impl$fns$arity_15.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_15__delegate;
return sci$impl$fns$arity_15;
})()
;

break;
case (16):
return (function() { 
var sci$impl$fns$arity_16__delegate = function (G__33823,G__33824,G__33825,G__33826,G__33827,G__33828,G__33829,G__33830,G__33831,G__33832,G__33833,G__33834,G__33835,G__33836,G__33837,G__33838,G__33839){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33823);

(invoc_array[(1)] = G__33824);

(invoc_array[(2)] = G__33825);

(invoc_array[(3)] = G__33826);

(invoc_array[(4)] = G__33827);

(invoc_array[(5)] = G__33828);

(invoc_array[(6)] = G__33829);

(invoc_array[(7)] = G__33830);

(invoc_array[(8)] = G__33831);

(invoc_array[(9)] = G__33832);

(invoc_array[(10)] = G__33833);

(invoc_array[(11)] = G__33834);

(invoc_array[(12)] = G__33835);

(invoc_array[(13)] = G__33836);

(invoc_array[(14)] = G__33837);

(invoc_array[(15)] = G__33838);

(invoc_array[vararg_idx] = G__33839);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_16 = function (G__33823,G__33824,G__33825,G__33826,G__33827,G__33828,G__33829,G__33830,G__33831,G__33832,G__33833,G__33834,G__33835,G__33836,G__33837,G__33838,var_args){
var G__33839 = null;
if (arguments.length > 16) {
var G__34729__i = 0, G__34729__a = new Array(arguments.length -  16);
while (G__34729__i < G__34729__a.length) {G__34729__a[G__34729__i] = arguments[G__34729__i + 16]; ++G__34729__i;}
  G__33839 = new cljs.core.IndexedSeq(G__34729__a,0,null);
} 
return sci$impl$fns$arity_16__delegate.call(this,G__33823,G__33824,G__33825,G__33826,G__33827,G__33828,G__33829,G__33830,G__33831,G__33832,G__33833,G__33834,G__33835,G__33836,G__33837,G__33838,G__33839);};
sci$impl$fns$arity_16.cljs$lang$maxFixedArity = 16;
sci$impl$fns$arity_16.cljs$lang$applyTo = (function (arglist__34734){
var G__33823 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33824 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33825 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33826 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33827 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33828 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33829 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33830 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33831 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33832 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33833 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33834 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33835 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33836 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33837 = cljs.core.first(arglist__34734);
arglist__34734 = cljs.core.next(arglist__34734);
var G__33838 = cljs.core.first(arglist__34734);
var G__33839 = cljs.core.rest(arglist__34734);
return sci$impl$fns$arity_16__delegate(G__33823,G__33824,G__33825,G__33826,G__33827,G__33828,G__33829,G__33830,G__33831,G__33832,G__33833,G__33834,G__33835,G__33836,G__33837,G__33838,G__33839);
});
sci$impl$fns$arity_16.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_16__delegate;
return sci$impl$fns$arity_16;
})()
;

break;
case (17):
return (function() { 
var sci$impl$fns$arity_17__delegate = function (G__33894,G__33895,G__33896,G__33897,G__33898,G__33899,G__33900,G__33901,G__33902,G__33903,G__33904,G__33905,G__33906,G__33907,G__33908,G__33909,G__33910,G__33911){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33894);

(invoc_array[(1)] = G__33895);

(invoc_array[(2)] = G__33896);

(invoc_array[(3)] = G__33897);

(invoc_array[(4)] = G__33898);

(invoc_array[(5)] = G__33899);

(invoc_array[(6)] = G__33900);

(invoc_array[(7)] = G__33901);

(invoc_array[(8)] = G__33902);

(invoc_array[(9)] = G__33903);

(invoc_array[(10)] = G__33904);

(invoc_array[(11)] = G__33905);

(invoc_array[(12)] = G__33906);

(invoc_array[(13)] = G__33907);

(invoc_array[(14)] = G__33908);

(invoc_array[(15)] = G__33909);

(invoc_array[(16)] = G__33910);

(invoc_array[vararg_idx] = G__33911);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_17 = function (G__33894,G__33895,G__33896,G__33897,G__33898,G__33899,G__33900,G__33901,G__33902,G__33903,G__33904,G__33905,G__33906,G__33907,G__33908,G__33909,G__33910,var_args){
var G__33911 = null;
if (arguments.length > 17) {
var G__34739__i = 0, G__34739__a = new Array(arguments.length -  17);
while (G__34739__i < G__34739__a.length) {G__34739__a[G__34739__i] = arguments[G__34739__i + 17]; ++G__34739__i;}
  G__33911 = new cljs.core.IndexedSeq(G__34739__a,0,null);
} 
return sci$impl$fns$arity_17__delegate.call(this,G__33894,G__33895,G__33896,G__33897,G__33898,G__33899,G__33900,G__33901,G__33902,G__33903,G__33904,G__33905,G__33906,G__33907,G__33908,G__33909,G__33910,G__33911);};
sci$impl$fns$arity_17.cljs$lang$maxFixedArity = 17;
sci$impl$fns$arity_17.cljs$lang$applyTo = (function (arglist__34740){
var G__33894 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33895 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33896 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33897 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33898 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33899 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33900 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33901 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33902 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33903 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33904 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33905 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33906 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33907 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33908 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33909 = cljs.core.first(arglist__34740);
arglist__34740 = cljs.core.next(arglist__34740);
var G__33910 = cljs.core.first(arglist__34740);
var G__33911 = cljs.core.rest(arglist__34740);
return sci$impl$fns$arity_17__delegate(G__33894,G__33895,G__33896,G__33897,G__33898,G__33899,G__33900,G__33901,G__33902,G__33903,G__33904,G__33905,G__33906,G__33907,G__33908,G__33909,G__33910,G__33911);
});
sci$impl$fns$arity_17.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_17__delegate;
return sci$impl$fns$arity_17;
})()
;

break;
case (18):
return (function() { 
var sci$impl$fns$arity_18__delegate = function (G__33952,G__33953,G__33954,G__33955,G__33956,G__33957,G__33958,G__33959,G__33960,G__33961,G__33962,G__33963,G__33964,G__33965,G__33966,G__33967,G__33968,G__33969,G__33970){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33952);

(invoc_array[(1)] = G__33953);

(invoc_array[(2)] = G__33954);

(invoc_array[(3)] = G__33955);

(invoc_array[(4)] = G__33956);

(invoc_array[(5)] = G__33957);

(invoc_array[(6)] = G__33958);

(invoc_array[(7)] = G__33959);

(invoc_array[(8)] = G__33960);

(invoc_array[(9)] = G__33961);

(invoc_array[(10)] = G__33962);

(invoc_array[(11)] = G__33963);

(invoc_array[(12)] = G__33964);

(invoc_array[(13)] = G__33965);

(invoc_array[(14)] = G__33966);

(invoc_array[(15)] = G__33967);

(invoc_array[(16)] = G__33968);

(invoc_array[(17)] = G__33969);

(invoc_array[vararg_idx] = G__33970);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_18 = function (G__33952,G__33953,G__33954,G__33955,G__33956,G__33957,G__33958,G__33959,G__33960,G__33961,G__33962,G__33963,G__33964,G__33965,G__33966,G__33967,G__33968,G__33969,var_args){
var G__33970 = null;
if (arguments.length > 18) {
var G__34742__i = 0, G__34742__a = new Array(arguments.length -  18);
while (G__34742__i < G__34742__a.length) {G__34742__a[G__34742__i] = arguments[G__34742__i + 18]; ++G__34742__i;}
  G__33970 = new cljs.core.IndexedSeq(G__34742__a,0,null);
} 
return sci$impl$fns$arity_18__delegate.call(this,G__33952,G__33953,G__33954,G__33955,G__33956,G__33957,G__33958,G__33959,G__33960,G__33961,G__33962,G__33963,G__33964,G__33965,G__33966,G__33967,G__33968,G__33969,G__33970);};
sci$impl$fns$arity_18.cljs$lang$maxFixedArity = 18;
sci$impl$fns$arity_18.cljs$lang$applyTo = (function (arglist__34743){
var G__33952 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33953 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33954 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33955 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33956 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33957 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33958 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33959 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33960 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33961 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33962 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33963 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33964 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33965 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33966 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33967 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33968 = cljs.core.first(arglist__34743);
arglist__34743 = cljs.core.next(arglist__34743);
var G__33969 = cljs.core.first(arglist__34743);
var G__33970 = cljs.core.rest(arglist__34743);
return sci$impl$fns$arity_18__delegate(G__33952,G__33953,G__33954,G__33955,G__33956,G__33957,G__33958,G__33959,G__33960,G__33961,G__33962,G__33963,G__33964,G__33965,G__33966,G__33967,G__33968,G__33969,G__33970);
});
sci$impl$fns$arity_18.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_18__delegate;
return sci$impl$fns$arity_18;
})()
;

break;
case (19):
return (function() { 
var sci$impl$fns$arity_19__delegate = function (G__33993,G__33994,G__33995,G__33996,G__33997,G__33998,G__33999,G__34000,G__34001,G__34002,G__34003,G__34004,G__34005,G__34006,G__34007,G__34008,G__34009,G__34010,G__34011,G__34012){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__33993);

(invoc_array[(1)] = G__33994);

(invoc_array[(2)] = G__33995);

(invoc_array[(3)] = G__33996);

(invoc_array[(4)] = G__33997);

(invoc_array[(5)] = G__33998);

(invoc_array[(6)] = G__33999);

(invoc_array[(7)] = G__34000);

(invoc_array[(8)] = G__34001);

(invoc_array[(9)] = G__34002);

(invoc_array[(10)] = G__34003);

(invoc_array[(11)] = G__34004);

(invoc_array[(12)] = G__34005);

(invoc_array[(13)] = G__34006);

(invoc_array[(14)] = G__34007);

(invoc_array[(15)] = G__34008);

(invoc_array[(16)] = G__34009);

(invoc_array[(17)] = G__34010);

(invoc_array[(18)] = G__34011);

(invoc_array[vararg_idx] = G__34012);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_19 = function (G__33993,G__33994,G__33995,G__33996,G__33997,G__33998,G__33999,G__34000,G__34001,G__34002,G__34003,G__34004,G__34005,G__34006,G__34007,G__34008,G__34009,G__34010,G__34011,var_args){
var G__34012 = null;
if (arguments.length > 19) {
var G__34747__i = 0, G__34747__a = new Array(arguments.length -  19);
while (G__34747__i < G__34747__a.length) {G__34747__a[G__34747__i] = arguments[G__34747__i + 19]; ++G__34747__i;}
  G__34012 = new cljs.core.IndexedSeq(G__34747__a,0,null);
} 
return sci$impl$fns$arity_19__delegate.call(this,G__33993,G__33994,G__33995,G__33996,G__33997,G__33998,G__33999,G__34000,G__34001,G__34002,G__34003,G__34004,G__34005,G__34006,G__34007,G__34008,G__34009,G__34010,G__34011,G__34012);};
sci$impl$fns$arity_19.cljs$lang$maxFixedArity = 19;
sci$impl$fns$arity_19.cljs$lang$applyTo = (function (arglist__34749){
var G__33993 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__33994 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__33995 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__33996 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__33997 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__33998 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__33999 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34000 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34001 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34002 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34003 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34004 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34005 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34006 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34007 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34008 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34009 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34010 = cljs.core.first(arglist__34749);
arglist__34749 = cljs.core.next(arglist__34749);
var G__34011 = cljs.core.first(arglist__34749);
var G__34012 = cljs.core.rest(arglist__34749);
return sci$impl$fns$arity_19__delegate(G__33993,G__33994,G__33995,G__33996,G__33997,G__33998,G__33999,G__34000,G__34001,G__34002,G__34003,G__34004,G__34005,G__34006,G__34007,G__34008,G__34009,G__34010,G__34011,G__34012);
});
sci$impl$fns$arity_19.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_19__delegate;
return sci$impl$fns$arity_19;
})()
;

break;
case (20):
return (function() { 
var sci$impl$fns$arity_20__delegate = function (G__34033,G__34034,G__34035,G__34036,G__34037,G__34038,G__34039,G__34040,G__34041,G__34042,G__34043,G__34044,G__34045,G__34046,G__34047,G__34048,G__34049,G__34050,G__34051,G__34052,G__34053){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34033);

(invoc_array[(1)] = G__34034);

(invoc_array[(2)] = G__34035);

(invoc_array[(3)] = G__34036);

(invoc_array[(4)] = G__34037);

(invoc_array[(5)] = G__34038);

(invoc_array[(6)] = G__34039);

(invoc_array[(7)] = G__34040);

(invoc_array[(8)] = G__34041);

(invoc_array[(9)] = G__34042);

(invoc_array[(10)] = G__34043);

(invoc_array[(11)] = G__34044);

(invoc_array[(12)] = G__34045);

(invoc_array[(13)] = G__34046);

(invoc_array[(14)] = G__34047);

(invoc_array[(15)] = G__34048);

(invoc_array[(16)] = G__34049);

(invoc_array[(17)] = G__34050);

(invoc_array[(18)] = G__34051);

(invoc_array[(19)] = G__34052);

(invoc_array[vararg_idx] = G__34053);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
};
var sci$impl$fns$arity_20 = function (G__34033,G__34034,G__34035,G__34036,G__34037,G__34038,G__34039,G__34040,G__34041,G__34042,G__34043,G__34044,G__34045,G__34046,G__34047,G__34048,G__34049,G__34050,G__34051,G__34052,var_args){
var G__34053 = null;
if (arguments.length > 20) {
var G__34753__i = 0, G__34753__a = new Array(arguments.length -  20);
while (G__34753__i < G__34753__a.length) {G__34753__a[G__34753__i] = arguments[G__34753__i + 20]; ++G__34753__i;}
  G__34053 = new cljs.core.IndexedSeq(G__34753__a,0,null);
} 
return sci$impl$fns$arity_20__delegate.call(this,G__34033,G__34034,G__34035,G__34036,G__34037,G__34038,G__34039,G__34040,G__34041,G__34042,G__34043,G__34044,G__34045,G__34046,G__34047,G__34048,G__34049,G__34050,G__34051,G__34052,G__34053);};
sci$impl$fns$arity_20.cljs$lang$maxFixedArity = 20;
sci$impl$fns$arity_20.cljs$lang$applyTo = (function (arglist__34755){
var G__34033 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34034 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34035 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34036 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34037 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34038 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34039 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34040 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34041 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34042 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34043 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34044 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34045 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34046 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34047 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34048 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34049 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34050 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34051 = cljs.core.first(arglist__34755);
arglist__34755 = cljs.core.next(arglist__34755);
var G__34052 = cljs.core.first(arglist__34755);
var G__34053 = cljs.core.rest(arglist__34755);
return sci$impl$fns$arity_20__delegate(G__34033,G__34034,G__34035,G__34036,G__34037,G__34038,G__34039,G__34040,G__34041,G__34042,G__34043,G__34044,G__34045,G__34046,G__34047,G__34048,G__34049,G__34050,G__34051,G__34052,G__34053);
});
sci$impl$fns$arity_20.cljs$core$IFn$_invoke$arity$variadic = sci$impl$fns$arity_20__delegate;
return sci$impl$fns$arity_20;
})()
;

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__33515))));

}
})():(function (){var G__34067 = fixed_arity;
switch (G__34067) {
case (0):
return (function sci$impl$fns$arity_0(){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

while(true){
var ret__32382__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32382__auto__)){
continue;
} else {
return ret__32382__auto__;
}
break;
}
});

break;
case (1):
return (function sci$impl$fns$arity_1(G__34072){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34072);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (2):
return (function sci$impl$fns$arity_2(G__34082,G__34083){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34082);

(invoc_array[(1)] = G__34083);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (3):
return (function sci$impl$fns$arity_3(G__34089,G__34090,G__34091){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34089);

(invoc_array[(1)] = G__34090);

(invoc_array[(2)] = G__34091);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (4):
return (function sci$impl$fns$arity_4(G__34104,G__34105,G__34106,G__34107){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34104);

(invoc_array[(1)] = G__34105);

(invoc_array[(2)] = G__34106);

(invoc_array[(3)] = G__34107);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (5):
return (function sci$impl$fns$arity_5(G__34115,G__34116,G__34117,G__34118,G__34119){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34115);

(invoc_array[(1)] = G__34116);

(invoc_array[(2)] = G__34117);

(invoc_array[(3)] = G__34118);

(invoc_array[(4)] = G__34119);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (6):
return (function sci$impl$fns$arity_6(G__34120,G__34121,G__34122,G__34123,G__34124,G__34125){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34120);

(invoc_array[(1)] = G__34121);

(invoc_array[(2)] = G__34122);

(invoc_array[(3)] = G__34123);

(invoc_array[(4)] = G__34124);

(invoc_array[(5)] = G__34125);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (7):
return (function sci$impl$fns$arity_7(G__34143,G__34144,G__34145,G__34146,G__34147,G__34148,G__34149){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34143);

(invoc_array[(1)] = G__34144);

(invoc_array[(2)] = G__34145);

(invoc_array[(3)] = G__34146);

(invoc_array[(4)] = G__34147);

(invoc_array[(5)] = G__34148);

(invoc_array[(6)] = G__34149);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (8):
return (function sci$impl$fns$arity_8(G__34154,G__34155,G__34156,G__34157,G__34158,G__34159,G__34160,G__34161){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34154);

(invoc_array[(1)] = G__34155);

(invoc_array[(2)] = G__34156);

(invoc_array[(3)] = G__34157);

(invoc_array[(4)] = G__34158);

(invoc_array[(5)] = G__34159);

(invoc_array[(6)] = G__34160);

(invoc_array[(7)] = G__34161);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (9):
return (function sci$impl$fns$arity_9(G__34165,G__34166,G__34167,G__34168,G__34169,G__34170,G__34171,G__34172,G__34173){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34165);

(invoc_array[(1)] = G__34166);

(invoc_array[(2)] = G__34167);

(invoc_array[(3)] = G__34168);

(invoc_array[(4)] = G__34169);

(invoc_array[(5)] = G__34170);

(invoc_array[(6)] = G__34171);

(invoc_array[(7)] = G__34172);

(invoc_array[(8)] = G__34173);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (10):
return (function sci$impl$fns$arity_10(G__34183,G__34184,G__34185,G__34186,G__34187,G__34188,G__34189,G__34190,G__34191,G__34192){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34183);

(invoc_array[(1)] = G__34184);

(invoc_array[(2)] = G__34185);

(invoc_array[(3)] = G__34186);

(invoc_array[(4)] = G__34187);

(invoc_array[(5)] = G__34188);

(invoc_array[(6)] = G__34189);

(invoc_array[(7)] = G__34190);

(invoc_array[(8)] = G__34191);

(invoc_array[(9)] = G__34192);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (11):
return (function sci$impl$fns$arity_11(G__34195,G__34196,G__34197,G__34198,G__34199,G__34200,G__34201,G__34202,G__34203,G__34204,G__34205){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34195);

(invoc_array[(1)] = G__34196);

(invoc_array[(2)] = G__34197);

(invoc_array[(3)] = G__34198);

(invoc_array[(4)] = G__34199);

(invoc_array[(5)] = G__34200);

(invoc_array[(6)] = G__34201);

(invoc_array[(7)] = G__34202);

(invoc_array[(8)] = G__34203);

(invoc_array[(9)] = G__34204);

(invoc_array[(10)] = G__34205);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (12):
return (function sci$impl$fns$arity_12(G__34216,G__34217,G__34218,G__34219,G__34220,G__34221,G__34222,G__34223,G__34224,G__34225,G__34226,G__34227){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34216);

(invoc_array[(1)] = G__34217);

(invoc_array[(2)] = G__34218);

(invoc_array[(3)] = G__34219);

(invoc_array[(4)] = G__34220);

(invoc_array[(5)] = G__34221);

(invoc_array[(6)] = G__34222);

(invoc_array[(7)] = G__34223);

(invoc_array[(8)] = G__34224);

(invoc_array[(9)] = G__34225);

(invoc_array[(10)] = G__34226);

(invoc_array[(11)] = G__34227);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (13):
return (function sci$impl$fns$arity_13(G__34237,G__34238,G__34239,G__34240,G__34241,G__34242,G__34243,G__34244,G__34245,G__34246,G__34248,G__34249,G__34252){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34237);

(invoc_array[(1)] = G__34238);

(invoc_array[(2)] = G__34239);

(invoc_array[(3)] = G__34240);

(invoc_array[(4)] = G__34241);

(invoc_array[(5)] = G__34242);

(invoc_array[(6)] = G__34243);

(invoc_array[(7)] = G__34244);

(invoc_array[(8)] = G__34245);

(invoc_array[(9)] = G__34246);

(invoc_array[(10)] = G__34248);

(invoc_array[(11)] = G__34249);

(invoc_array[(12)] = G__34252);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (14):
return (function sci$impl$fns$arity_14(G__34267,G__34268,G__34269,G__34270,G__34271,G__34272,G__34273,G__34274,G__34275,G__34276,G__34277,G__34278,G__34279,G__34280){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34267);

(invoc_array[(1)] = G__34268);

(invoc_array[(2)] = G__34269);

(invoc_array[(3)] = G__34270);

(invoc_array[(4)] = G__34271);

(invoc_array[(5)] = G__34272);

(invoc_array[(6)] = G__34273);

(invoc_array[(7)] = G__34274);

(invoc_array[(8)] = G__34275);

(invoc_array[(9)] = G__34276);

(invoc_array[(10)] = G__34277);

(invoc_array[(11)] = G__34278);

(invoc_array[(12)] = G__34279);

(invoc_array[(13)] = G__34280);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (15):
return (function sci$impl$fns$arity_15(G__34292,G__34293,G__34294,G__34295,G__34296,G__34297,G__34298,G__34299,G__34300,G__34301,G__34302,G__34303,G__34304,G__34305,G__34306){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34292);

(invoc_array[(1)] = G__34293);

(invoc_array[(2)] = G__34294);

(invoc_array[(3)] = G__34295);

(invoc_array[(4)] = G__34296);

(invoc_array[(5)] = G__34297);

(invoc_array[(6)] = G__34298);

(invoc_array[(7)] = G__34299);

(invoc_array[(8)] = G__34300);

(invoc_array[(9)] = G__34301);

(invoc_array[(10)] = G__34302);

(invoc_array[(11)] = G__34303);

(invoc_array[(12)] = G__34304);

(invoc_array[(13)] = G__34305);

(invoc_array[(14)] = G__34306);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (16):
return (function sci$impl$fns$arity_16(G__34328,G__34329,G__34330,G__34331,G__34332,G__34333,G__34334,G__34335,G__34336,G__34337,G__34338,G__34339,G__34340,G__34341,G__34343,G__34344){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34328);

(invoc_array[(1)] = G__34329);

(invoc_array[(2)] = G__34330);

(invoc_array[(3)] = G__34331);

(invoc_array[(4)] = G__34332);

(invoc_array[(5)] = G__34333);

(invoc_array[(6)] = G__34334);

(invoc_array[(7)] = G__34335);

(invoc_array[(8)] = G__34336);

(invoc_array[(9)] = G__34337);

(invoc_array[(10)] = G__34338);

(invoc_array[(11)] = G__34339);

(invoc_array[(12)] = G__34340);

(invoc_array[(13)] = G__34341);

(invoc_array[(14)] = G__34343);

(invoc_array[(15)] = G__34344);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (17):
return (function sci$impl$fns$arity_17(G__34352,G__34353,G__34354,G__34355,G__34356,G__34357,G__34358,G__34359,G__34360,G__34361,G__34362,G__34363,G__34364,G__34365,G__34366,G__34367,G__34368){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34352);

(invoc_array[(1)] = G__34353);

(invoc_array[(2)] = G__34354);

(invoc_array[(3)] = G__34355);

(invoc_array[(4)] = G__34356);

(invoc_array[(5)] = G__34357);

(invoc_array[(6)] = G__34358);

(invoc_array[(7)] = G__34359);

(invoc_array[(8)] = G__34360);

(invoc_array[(9)] = G__34361);

(invoc_array[(10)] = G__34362);

(invoc_array[(11)] = G__34363);

(invoc_array[(12)] = G__34364);

(invoc_array[(13)] = G__34365);

(invoc_array[(14)] = G__34366);

(invoc_array[(15)] = G__34367);

(invoc_array[(16)] = G__34368);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (18):
return (function sci$impl$fns$arity_18(G__34372,G__34373,G__34374,G__34375,G__34376,G__34377,G__34378,G__34379,G__34380,G__34381,G__34382,G__34383,G__34384,G__34385,G__34386,G__34387,G__34388,G__34389){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34372);

(invoc_array[(1)] = G__34373);

(invoc_array[(2)] = G__34374);

(invoc_array[(3)] = G__34375);

(invoc_array[(4)] = G__34376);

(invoc_array[(5)] = G__34377);

(invoc_array[(6)] = G__34378);

(invoc_array[(7)] = G__34379);

(invoc_array[(8)] = G__34380);

(invoc_array[(9)] = G__34381);

(invoc_array[(10)] = G__34382);

(invoc_array[(11)] = G__34383);

(invoc_array[(12)] = G__34384);

(invoc_array[(13)] = G__34385);

(invoc_array[(14)] = G__34386);

(invoc_array[(15)] = G__34387);

(invoc_array[(16)] = G__34388);

(invoc_array[(17)] = G__34389);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (19):
return (function sci$impl$fns$arity_19(G__34399,G__34400,G__34401,G__34402,G__34403,G__34404,G__34405,G__34406,G__34407,G__34408,G__34409,G__34410,G__34411,G__34412,G__34413,G__34414,G__34415,G__34416,G__34417){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34399);

(invoc_array[(1)] = G__34400);

(invoc_array[(2)] = G__34401);

(invoc_array[(3)] = G__34402);

(invoc_array[(4)] = G__34403);

(invoc_array[(5)] = G__34404);

(invoc_array[(6)] = G__34405);

(invoc_array[(7)] = G__34406);

(invoc_array[(8)] = G__34407);

(invoc_array[(9)] = G__34408);

(invoc_array[(10)] = G__34409);

(invoc_array[(11)] = G__34410);

(invoc_array[(12)] = G__34411);

(invoc_array[(13)] = G__34412);

(invoc_array[(14)] = G__34413);

(invoc_array[(15)] = G__34414);

(invoc_array[(16)] = G__34415);

(invoc_array[(17)] = G__34416);

(invoc_array[(18)] = G__34417);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
case (20):
return (function sci$impl$fns$arity_20(G__34436,G__34437,G__34438,G__34439,G__34440,G__34441,G__34442,G__34443,G__34444,G__34445,G__34446,G__34447,G__34448,G__34449,G__34450,G__34451,G__34452,G__34453,G__34454,G__34455){
var invoc_array = (((invoc_size === (0)))?null:cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(invoc_size));
if(cljs.core.truth_(enclosed__GT_invocation)){
(enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2 ? enclosed__GT_invocation.cljs$core$IFn$_invoke$arity$2(enclosed_array,invoc_array) : enclosed__GT_invocation.call(null,enclosed_array,invoc_array));
} else {
}

(invoc_array[(0)] = G__34436);

(invoc_array[(1)] = G__34437);

(invoc_array[(2)] = G__34438);

(invoc_array[(3)] = G__34439);

(invoc_array[(4)] = G__34440);

(invoc_array[(5)] = G__34441);

(invoc_array[(6)] = G__34442);

(invoc_array[(7)] = G__34443);

(invoc_array[(8)] = G__34444);

(invoc_array[(9)] = G__34445);

(invoc_array[(10)] = G__34446);

(invoc_array[(11)] = G__34447);

(invoc_array[(12)] = G__34448);

(invoc_array[(13)] = G__34449);

(invoc_array[(14)] = G__34450);

(invoc_array[(15)] = G__34451);

(invoc_array[(16)] = G__34452);

(invoc_array[(17)] = G__34453);

(invoc_array[(18)] = G__34454);

(invoc_array[(19)] = G__34455);

while(true){
var ret__32383__auto__ = sci.impl.types.eval(body,ctx,invoc_array);
if(cljs.core.keyword_identical_QMARK_(new cljs.core.Keyword("sci.impl.analyzer","recur","sci.impl.analyzer/recur",2033369355),ret__32383__auto__)){
continue;
} else {
return ret__32383__auto__;
}
break;
}
});

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__34067))));

}
})());
return f;
}));

(sci.impl.fns.fun.cljs$lang$maxFixedArity = 11);

sci.impl.fns.lookup_by_arity = (function sci$impl$fns$lookup_by_arity(arities,arity){
var or__5142__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(arities,arity);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"variadic","variadic",882626057).cljs$core$IFn$_invoke$arity$1(arities);
}
});
sci.impl.fns.fn_arity_map = (function sci$impl$fns$fn_arity_map(ctx,enclosed_array,fn_name,macro_QMARK_,fn_bodies){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (arity_map,fn_body){
var f = sci.impl.fns.fun.cljs$core$IFn$_invoke$arity$5(ctx,enclosed_array,fn_body,fn_name,macro_QMARK_);
var var_arg_QMARK_ = new cljs.core.Keyword(null,"var-arg-name","var-arg-name",-1100024887).cljs$core$IFn$_invoke$arity$1(fn_body);
var fixed_arity = new cljs.core.Keyword(null,"fixed-arity","fixed-arity",1586445869).cljs$core$IFn$_invoke$arity$1(fn_body);
if(cljs.core.truth_(var_arg_QMARK_)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(arity_map,new cljs.core.Keyword(null,"variadic","variadic",882626057),f);
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(arity_map,fixed_arity,f);
}
}),cljs.core.PersistentArrayMap.EMPTY,fn_bodies);
});
sci.impl.fns.maybe_destructured = (function sci$impl$fns$maybe_destructured(params,body){
if(cljs.core.every_QMARK_(cljs.core.symbol_QMARK_,params)){
return cljs.core.cons(params,body);
} else {
var params__$1 = params;
var new_params = cljs.core.with_meta(cljs.core.PersistentVector.EMPTY,cljs.core.meta(params__$1));
var lets = cljs.core.PersistentVector.EMPTY;
while(true){
if(cljs.core.truth_(params__$1)){
if((cljs.core.first(params__$1) instanceof cljs.core.Symbol)){
var G__34765 = cljs.core.next(params__$1);
var G__34766 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new_params,cljs.core.first(params__$1));
var G__34767 = lets;
params__$1 = G__34765;
new_params = G__34766;
lets = G__34767;
continue;
} else {
var gparam = cljs.core.gensym.cljs$core$IFn$_invoke$arity$1("p__");
var G__34768 = cljs.core.next(params__$1);
var G__34769 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new_params,gparam);
var G__34770 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(lets,cljs.core.first(params__$1)),gparam);
params__$1 = G__34768;
new_params = G__34769;
lets = G__34770;
continue;
}
} else {
return cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new_params,null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","let","cljs.core/let",-308701135,null),null,(1),null)),(new cljs.core.List(null,lets,null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([body], 0)))),null,(1),null)))));
}
break;
}
}
});
sci.impl.fns.fn_STAR__STAR_ = (function sci$impl$fns$fn_STAR__STAR_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___34771 = arguments.length;
var i__5877__auto___34772 = (0);
while(true){
if((i__5877__auto___34772 < len__5876__auto___34771)){
args__5882__auto__.push((arguments[i__5877__auto___34772]));

var G__34773 = (i__5877__auto___34772 + (1));
i__5877__auto___34772 = G__34773;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return sci.impl.fns.fn_STAR__STAR_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(sci.impl.fns.fn_STAR__STAR_.cljs$core$IFn$_invoke$arity$variadic = (function (form,_,sigs){
var name = (((cljs.core.first(sigs) instanceof cljs.core.Symbol))?cljs.core.first(sigs):null);
var sigs__$1 = (cljs.core.truth_(name)?cljs.core.next(sigs):sigs);
var sigs__$2 = ((cljs.core.vector_QMARK_(cljs.core.first(sigs__$1)))?(new cljs.core.List(null,sigs__$1,null,(1),null)):((cljs.core.seq_QMARK_(cljs.core.first(sigs__$1)))?sigs__$1:sci.impl.utils.throw_error_with_location.cljs$core$IFn$_invoke$arity$2(((cljs.core.seq(sigs__$1))?(""+"Parameter declaration "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first(sigs__$1))+" should be a vector"):(""+"Parameter declaration missing")),form)));
var psig = (function (sig){
if((!(cljs.core.seq_QMARK_(sig)))){
throw sci.impl.utils.throw_error_with_location.cljs$core$IFn$_invoke$arity$2((""+"Invalid signature "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sig)+" should be a list"),form);
} else {
}

var vec__34561 = sig;
var seq__34562 = cljs.core.seq(vec__34561);
var first__34563 = cljs.core.first(seq__34562);
var seq__34562__$1 = cljs.core.next(seq__34562);
var params = first__34563;
var body = seq__34562__$1;
var ___$1 = (((!(cljs.core.vector_QMARK_(params))))?sci.impl.utils.throw_error_with_location.cljs$core$IFn$_invoke$arity$2(((cljs.core.seq_QMARK_(cljs.core.first(sigs__$2)))?(""+"Parameter declaration "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(params)+" should be a vector"):(""+"Invalid signature "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sig)+" should be a list")),form):null);
var conds = ((((cljs.core.next(body)) && (cljs.core.map_QMARK_(cljs.core.first(body)))))?cljs.core.first(body):null);
var body__$1 = (cljs.core.truth_(conds)?cljs.core.next(body):body);
var conds__$1 = (function (){var or__5142__auto__ = conds;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.meta(params);
}
})();
var pre = new cljs.core.Keyword(null,"pre","pre",2118456869).cljs$core$IFn$_invoke$arity$1(conds__$1);
var post = new cljs.core.Keyword(null,"post","post",269697687).cljs$core$IFn$_invoke$arity$1(conds__$1);
var body__$2 = (cljs.core.truth_(post)?cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$1((new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","let","cljs.core/let",-308701135,null),null,(1),null)),(new cljs.core.List(null,cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol(null,"%","%",-950237169,null),null,(1),null)),(new cljs.core.List(null,((((1) < cljs.core.count(body__$1)))?cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol(null,"do","do",1686842252,null),null,(1),null)),body__$1))):cljs.core.first(body__$1)),null,(1),null)))))),null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (c){
return cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","assert","cljs.core/assert",1075777968,null),null,(1),null)),(new cljs.core.List(null,c,null,(1),null)))));
}),post),(new cljs.core.List(null,new cljs.core.Symbol(null,"%","%",-950237169,null),null,(1),null))], 0)))),null,(1),null))))):body__$1);
var body__$3 = (cljs.core.truth_(pre)?cljs.core.concat.cljs$core$IFn$_invoke$arity$2(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (c){
return cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","assert","cljs.core/assert",1075777968,null),null,(1),null)),(new cljs.core.List(null,c,null,(1),null)))));
}),pre),body__$2):body__$2);
return sci.impl.fns.maybe_destructured(params,body__$3);
});
var new_sigs = cljs.core.map.cljs$core$IFn$_invoke$arity$2(psig,sigs__$2);
var expr = cljs.core.with_meta((cljs.core.truth_(name)?cljs.core.list_STAR_.cljs$core$IFn$_invoke$arity$3(new cljs.core.Symbol(null,"fn*","fn*",-752876845,null),name,new_sigs):cljs.core.cons(new cljs.core.Symbol(null,"fn*","fn*",-752876845,null),new_sigs)),cljs.core.meta(form));
return expr;
}));

(sci.impl.fns.fn_STAR__STAR_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(sci.impl.fns.fn_STAR__STAR_.cljs$lang$applyTo = (function (seq34525){
var G__34526 = cljs.core.first(seq34525);
var seq34525__$1 = cljs.core.next(seq34525);
var G__34527 = cljs.core.first(seq34525__$1);
var seq34525__$2 = cljs.core.next(seq34525__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__34526,G__34527,seq34525__$2);
}));

sci.impl.fns.sigs = (function sci$impl$fns$sigs(fdecl){
var asig = (function (fdecl__$1){
var arglist = cljs.core.first(fdecl__$1);
var arglist__$1 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol(null,"&form","&form",1482799337,null),cljs.core.first(arglist)))?cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(arglist,(2),cljs.core.count(arglist)):arglist);
var body = cljs.core.next(fdecl__$1);
if(cljs.core.map_QMARK_(cljs.core.first(body))){
if(cljs.core.next(body)){
return cljs.core.with_meta(arglist__$1,cljs.core.conj.cljs$core$IFn$_invoke$arity$2((cljs.core.truth_(cljs.core.meta(arglist__$1))?cljs.core.meta(arglist__$1):cljs.core.PersistentArrayMap.EMPTY),cljs.core.first(body)));
} else {
return arglist__$1;
}
} else {
return arglist__$1;
}
});
if(cljs.core.seq_QMARK_(cljs.core.first(fdecl))){
var ret = cljs.core.PersistentVector.EMPTY;
var fdecls = fdecl;
while(true){
if(cljs.core.truth_(fdecls)){
var G__34774 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ret,cljs.core.identity(asig(cljs.core.first(fdecls))));
var G__34775 = cljs.core.next(fdecls);
ret = G__34774;
fdecls = G__34775;
continue;
} else {
return cljs.core.seq(ret);
}
break;
}
} else {
return (new cljs.core.List(null,cljs.core.identity(asig(fdecl)),null,(1),null));
}
});
sci.impl.fns.defn_STAR_ = (function sci$impl$fns$defn_STAR_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___34776 = arguments.length;
var i__5877__auto___34777 = (0);
while(true){
if((i__5877__auto___34777 < len__5876__auto___34776)){
args__5882__auto__.push((arguments[i__5877__auto___34777]));

var G__34778 = (i__5877__auto___34777 + (1));
i__5877__auto___34777 = G__34778;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((3) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((3)),(0),null)):null);
return sci.impl.fns.defn_STAR_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__5883__auto__);
});

(sci.impl.fns.defn_STAR_.cljs$core$IFn$_invoke$arity$variadic = (function (form,_,name,fdecl){
if((name instanceof cljs.core.Symbol)){
} else {
sci.impl.utils.throw_error_with_location.cljs$core$IFn$_invoke$arity$2("First argument to defn must be a symbol",form);
}

var m = ((typeof cljs.core.first(fdecl) === 'string')?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"doc","doc",1913296891),cljs.core.first(fdecl)], null):cljs.core.PersistentArrayMap.EMPTY);
var fdecl__$1 = ((typeof cljs.core.first(fdecl) === 'string')?cljs.core.next(fdecl):fdecl);
var m__$1 = ((cljs.core.map_QMARK_(cljs.core.first(fdecl__$1)))?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(m,cljs.core.first(fdecl__$1)):m);
var fdecl__$2 = ((cljs.core.map_QMARK_(cljs.core.first(fdecl__$1)))?cljs.core.next(fdecl__$1):fdecl__$1);
var fdecl__$3 = ((cljs.core.vector_QMARK_(cljs.core.first(fdecl__$2)))?(new cljs.core.List(null,fdecl__$2,null,(1),null)):fdecl__$2);
var m__$2 = ((cljs.core.map_QMARK_(cljs.core.last(fdecl__$3)))?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(m__$1,cljs.core.last(fdecl__$3)):m__$1);
var fdecl__$4 = ((cljs.core.map_QMARK_(cljs.core.last(fdecl__$3)))?cljs.core.butlast(fdecl__$3):fdecl__$3);
var m__$3 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"arglists","arglists",1661989754),(new cljs.core.List(null,new cljs.core.Symbol(null,"quote","quote",1377916282,null),(new cljs.core.List(null,sci.impl.fns.sigs(fdecl__$4),null,(1),null)),(2),null))], null),m__$2);
var name_m = cljs.core.meta(name);
var m__$4 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2((cljs.core.truth_(name_m)?name_m:cljs.core.PersistentArrayMap.EMPTY),m__$3);
var macro_QMARK_ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(name_m);
var expr = cljs.core.cons(new cljs.core.Symbol("cljs.core","fn","cljs.core/fn",-1065745098,null),fdecl__$4);
var expr__$1 = (new cljs.core.List(null,new cljs.core.Symbol(null,"def","def",597100991,null),(new cljs.core.List(null,cljs.core.with_meta(name,m__$4),(new cljs.core.List(null,(cljs.core.truth_((function (){var or__5142__auto__ = macro_QMARK_;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return name;
}
})())?cljs.core.with_meta(expr,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("sci.impl","fn","sci.impl/fn",1695180073),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"macro","macro",-867863404),macro_QMARK_,new cljs.core.Keyword(null,"fn-name","fn-name",-766594004),name], null)], null)):expr),null,(1),null)),(2),null)),(3),null));
return expr__$1;
}));

(sci.impl.fns.defn_STAR_.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(sci.impl.fns.defn_STAR_.cljs$lang$applyTo = (function (seq34631){
var G__34632 = cljs.core.first(seq34631);
var seq34631__$1 = cljs.core.next(seq34631);
var G__34633 = cljs.core.first(seq34631__$1);
var seq34631__$2 = cljs.core.next(seq34631__$1);
var G__34634 = cljs.core.first(seq34631__$2);
var seq34631__$3 = cljs.core.next(seq34631__$2);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__34632,G__34633,G__34634,seq34631__$3);
}));

sci.impl.fns.defmacro_STAR_ = (function sci$impl$fns$defmacro_STAR_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___34779 = arguments.length;
var i__5877__auto___34780 = (0);
while(true){
if((i__5877__auto___34780 < len__5876__auto___34779)){
args__5882__auto__.push((arguments[i__5877__auto___34780]));

var G__34781 = (i__5877__auto___34780 + (1));
i__5877__auto___34780 = G__34781;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((3) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((3)),(0),null)):null);
return sci.impl.fns.defmacro_STAR_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__5883__auto__);
});

(sci.impl.fns.defmacro_STAR_.cljs$core$IFn$_invoke$arity$variadic = (function (__AMPERSAND_form,__AMPERSAND_env,name,args){
var name__$1 = cljs.core.vary_meta.cljs$core$IFn$_invoke$arity$4(name,cljs.core.assoc,new cljs.core.Keyword(null,"macro","macro",-867863404),true);
var prefix = (function (){var p = (new cljs.core.List(null,name__$1,null,(1),null));
var args__$1 = args;
while(true){
var f = cljs.core.first(args__$1);
if(typeof f === 'string'){
var G__34782 = cljs.core.cons(f,p);
var G__34783 = cljs.core.next(args__$1);
p = G__34782;
args__$1 = G__34783;
continue;
} else {
if(cljs.core.map_QMARK_(f)){
var G__34784 = cljs.core.cons(f,p);
var G__34785 = cljs.core.next(args__$1);
p = G__34784;
args__$1 = G__34785;
continue;
} else {
return p;
}
}
break;
}
})();
var fdecl = (function (){var fd = args;
while(true){
if(typeof cljs.core.first(fd) === 'string'){
var G__34786 = cljs.core.next(fd);
fd = G__34786;
continue;
} else {
if(cljs.core.map_QMARK_(cljs.core.first(fd))){
var G__34787 = cljs.core.next(fd);
fd = G__34787;
continue;
} else {
return fd;
}
}
break;
}
})();
var fdecl__$1 = ((cljs.core.vector_QMARK_(cljs.core.first(fdecl)))?(new cljs.core.List(null,fdecl,null,(1),null)):fdecl);
var add_implicit_args = (function (fd){
var args__$1 = cljs.core.first(fd);
return cljs.core.cons(cljs.core.vec(cljs.core.cons(new cljs.core.Symbol(null,"&form","&form",1482799337,null),cljs.core.cons(new cljs.core.Symbol(null,"&env","&env",-919163083,null),args__$1))),cljs.core.next(fd));
});
var add_args = (function (acc,ds){
while(true){
if((ds == null)){
return acc;
} else {
var d = cljs.core.first(ds);
if(cljs.core.map_QMARK_(d)){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(acc,d);
} else {
var G__34788 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(acc,add_implicit_args(d));
var G__34789 = cljs.core.next(ds);
acc = G__34788;
ds = G__34789;
continue;
}
}
break;
}
});
var fdecl__$2 = cljs.core.seq(add_args(cljs.core.PersistentVector.EMPTY,fdecl__$1));
var decl = (function (){var p = prefix;
var d = fdecl__$2;
while(true){
if(cljs.core.truth_(p)){
var G__34790 = cljs.core.next(p);
var G__34791 = cljs.core.cons(cljs.core.first(p),d);
p = G__34790;
d = G__34791;
continue;
} else {
return d;
}
break;
}
})();
return (new cljs.core.List(null,new cljs.core.Symbol(null,"do","do",1686842252,null),(new cljs.core.List(null,cljs.core.cons(new cljs.core.Symbol("cljs.core","defn","cljs.core/defn",-1606493717,null),decl),(new cljs.core.List(null,(new cljs.core.List(null,new cljs.core.Symbol(null,"var","var",870848730,null),(new cljs.core.List(null,name__$1,null,(1),null)),(2),null)),null,(1),null)),(2),null)),(3),null));
}));

(sci.impl.fns.defmacro_STAR_.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(sci.impl.fns.defmacro_STAR_.cljs$lang$applyTo = (function (seq34650){
var G__34651 = cljs.core.first(seq34650);
var seq34650__$1 = cljs.core.next(seq34650);
var G__34652 = cljs.core.first(seq34650__$1);
var seq34650__$2 = cljs.core.next(seq34650__$1);
var G__34653 = cljs.core.first(seq34650__$2);
var seq34650__$3 = cljs.core.next(seq34650__$2);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__34651,G__34652,G__34653,seq34650__$3);
}));


//# sourceMappingURL=sci.impl.fns.js.map
