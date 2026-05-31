goog.provide('devtools.reporter');
devtools.reporter.issues_url = "https://github.com/binaryage/cljs-devtools/issues";
devtools.reporter.report_internal_error_BANG_ = (function devtools$reporter$report_internal_error_BANG_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___19700 = arguments.length;
var i__5877__auto___19701 = (0);
while(true){
if((i__5877__auto___19701 < len__5876__auto___19700)){
args__5882__auto__.push((arguments[i__5877__auto___19701]));

var G__19702 = (i__5877__auto___19701 + (1));
i__5877__auto___19701 = G__19702;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return devtools.reporter.report_internal_error_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(devtools.reporter.report_internal_error_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (e,p__19665){
var vec__19666 = p__19665;
var context = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19666,(0),null);
var footer = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__19666,(1),null);
var console__$1 = devtools.context.get_console.call(null);
try{var message = (((e instanceof Error))?(function (){var or__5142__auto__ = e.message;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return e;
}
})():e);
var header = ["%cCLJS DevTools Error%c%s","background-color:red;color:white;font-weight:bold;padding:0px 3px;border-radius:2px;","color:red",(""+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(message))];
var context_msg = (""+"In "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(devtools.util.get_lib_info())+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(context)?(""+", "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(context)+"."):"."))+"\n\n");
var footer_msg = (((!((footer == null))))?footer:(""+"\n\n"+"---\n"+"Please report the issue here: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(devtools.reporter.issues_url)));
var details = [context_msg,e,footer_msg];
var group_collapsed = (console__$1["groupCollapsed"]);
var log = (console__$1["log"]);
var group_end = (console__$1["groupEnd"]);
if(cljs.core.truth_(group_collapsed)){
} else {
throw (new Error("Assert failed: group-collapsed"));
}

if(cljs.core.truth_(log)){
} else {
throw (new Error("Assert failed: log"));
}

if(cljs.core.truth_(group_end)){
} else {
throw (new Error("Assert failed: group-end"));
}

group_collapsed.apply(console__$1,header);

log.apply(console__$1,details);

return group_end.call(console__$1);
}catch (e19673){var e__$1 = e19673;
return console__$1.error("FATAL: report-internal-error! failed",e__$1);
}}));

(devtools.reporter.report_internal_error_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(devtools.reporter.report_internal_error_BANG_.cljs$lang$applyTo = (function (seq19655){
var G__19656 = cljs.core.first(seq19655);
var seq19655__$1 = cljs.core.next(seq19655);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__19656,seq19655__$1);
}));


//# sourceMappingURL=devtools.reporter.js.map
