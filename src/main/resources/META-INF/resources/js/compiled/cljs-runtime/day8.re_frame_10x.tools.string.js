goog.provide('day8.re_frame_10x.tools.string');
/**
 * Return a pluralized phrase, appending an s to the singular form if no plural is provided.
 *   For example:
 *   (pluralize 5 "month") => "5 months"
 *   (pluralize 1 "month") => "1 month"
 *   (pluralize 1 "radius" "radii") => "1 radius"
 *   (pluralize 9 "radius" "radii") => "9 radii"
 *   From https://github.com/flatland/useful/blob/194950/src/flatland/useful/string.clj#L25-L33
 */
day8.re_frame_10x.tools.string.pluralize = (function day8$re_frame_10x$tools$string$pluralize(var_args){
var args__5882__auto__ = [];
var len__5876__auto___40424 = arguments.length;
var i__5877__auto___40425 = (0);
while(true){
if((i__5877__auto___40425 < len__5876__auto___40424)){
args__5882__auto__.push((arguments[i__5877__auto___40425]));

var G__40426 = (i__5877__auto___40425 + (1));
i__5877__auto___40425 = G__40426;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return day8.re_frame_10x.tools.string.pluralize.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(day8.re_frame_10x.tools.string.pluralize.cljs$core$IFn$_invoke$arity$variadic = (function (num,singular,p__40400){
var vec__40401 = p__40400;
var plural = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__40401,(0),null);
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(num)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),num))?singular:(function (){var or__5142__auto__ = plural;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(singular)+"s");
}
})())));
}));

(day8.re_frame_10x.tools.string.pluralize.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(day8.re_frame_10x.tools.string.pluralize.cljs$lang$applyTo = (function (seq40382){
var G__40383 = cljs.core.first(seq40382);
var seq40382__$1 = cljs.core.next(seq40382);
var G__40384 = cljs.core.first(seq40382__$1);
var seq40382__$2 = cljs.core.next(seq40382__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__40383,G__40384,seq40382__$2);
}));

/**
 * Same as pluralize, but doesn't prepend the number to the pluralized string.
 */
day8.re_frame_10x.tools.string.pluralize_ = (function day8$re_frame_10x$tools$string$pluralize_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___40433 = arguments.length;
var i__5877__auto___40434 = (0);
while(true){
if((i__5877__auto___40434 < len__5876__auto___40433)){
args__5882__auto__.push((arguments[i__5877__auto___40434]));

var G__40435 = (i__5877__auto___40434 + (1));
i__5877__auto___40434 = G__40435;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return day8.re_frame_10x.tools.string.pluralize_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(day8.re_frame_10x.tools.string.pluralize_.cljs$core$IFn$_invoke$arity$variadic = (function (num,singular,p__40418){
var vec__40419 = p__40418;
var plural = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__40419,(0),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),num)){
return singular;
} else {
var or__5142__auto__ = plural;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(singular)+"s");
}
}
}));

(day8.re_frame_10x.tools.string.pluralize_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(day8.re_frame_10x.tools.string.pluralize_.cljs$lang$applyTo = (function (seq40412){
var G__40413 = cljs.core.first(seq40412);
var seq40412__$1 = cljs.core.next(seq40412);
var G__40414 = cljs.core.first(seq40412__$1);
var seq40412__$2 = cljs.core.next(seq40412__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__40413,G__40414,seq40412__$2);
}));


//# sourceMappingURL=day8.re_frame_10x.tools.string.js.map
