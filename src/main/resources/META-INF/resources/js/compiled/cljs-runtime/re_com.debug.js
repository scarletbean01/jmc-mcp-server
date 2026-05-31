goog.provide('re_com.debug');
goog.scope(function(){
  re_com.debug.goog$module$goog$object = goog.module.get('goog.object');
});
/**
 * Returns the interesting part of component-name
 */
re_com.debug.short_component_name = (function re_com$debug$short_component_name(component_name){
return clojure.string.replace(clojure.string.replace(cljs.core.last(clojure.string.split.cljs$core$IFn$_invoke$arity$2(component_name,/\./)),/_render/,""),/_/,"-");
});
/**
 * Return a version of args which is stripped of uninteresting values, suitable for logging.
 */
re_com.debug.loggable_args = (function re_com$debug$loggable_args(args){
if(cljs.core.map_QMARK_(args)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.second),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(args,new cljs.core.Keyword(null,"src","src",-1651076051),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"child","child",623967545),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.Keyword(null,"panel-1","panel-1",998274139),new cljs.core.Keyword(null,"panel-2","panel-2",244198907),new cljs.core.Keyword(null,"debug-as","debug-as",283322354)], 0))));
} else {
return args;
}
});
re_com.debug.__GT_attr = (function re_com$debug$__GT_attr(p__17391){
var map__17392 = p__17391;
var map__17392__$1 = cljs.core.__destructure_map(map__17392);
var args = map__17392__$1;
var src = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17392__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var debug_as = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17392__$1,new cljs.core.Keyword(null,"debug-as","debug-as",283322354));
if(cljs.core.not(re_com.config.debug_QMARK_)){
return cljs.core.PersistentArrayMap.EMPTY;
} else {
var rc_component = (function (){var or__5142__auto__ = new cljs.core.Keyword(null,"component","component",1555936782).cljs$core$IFn$_invoke$arity$1(debug_as);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return re_com.debug.short_component_name(reagent.impl.component.component_name(reagent.core.current_component()));
}
})();
var rc_args = re_com.debug.loggable_args((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"args","args",1315556576).cljs$core$IFn$_invoke$arity$1(debug_as);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return args;
}
})());
var ref_fn = (function (el){
if(cljs.core.truth_(el)){
re_com.debug.goog$module$goog$object.set(el,"__rc-args",rc_args);
} else {
}

var temp__5825__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(args,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.Keyword(null,"ref","ref",1289896967)], null));
if(cljs.core.truth_(temp__5825__auto__)){
var user_ref_fn = temp__5825__auto__;
if(cljs.core.fn_QMARK_(user_ref_fn)){
return (user_ref_fn.cljs$core$IFn$_invoke$arity$1 ? user_ref_fn.cljs$core$IFn$_invoke$arity$1(el) : user_ref_fn.call(null,el));
} else {
return null;
}
} else {
return null;
}
});
var map__17393 = src;
var map__17393__$1 = cljs.core.__destructure_map(map__17393);
var file = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17393__$1,new cljs.core.Keyword(null,"file","file",-1269645878));
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17393__$1,new cljs.core.Keyword(null,"line","line",212345235));
var G__17401 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),ref_fn,new cljs.core.Keyword(null,"data-rc","data-rc",1949262543),rc_component], null);
if(cljs.core.truth_(src)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17401,new cljs.core.Keyword(null,"data-rc-src","data-rc-src",-344701880),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file)+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line)));
} else {
return G__17401;
}
}
});
re_com.debug.component_stack = (function re_com$debug$component_stack(var_args){
var G__17410 = arguments.length;
switch (G__17410) {
case 1:
return re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$1 = (function (el){
return re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,el);
}));

(re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$2 = (function (stack,el){
if(cljs.core.not(el)){
return stack;
} else {
var component = el.dataset.rc;
var parent = el.parentElement;
return re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$2(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("stack-spy",component))?stack:cljs.core.conj.cljs$core$IFn$_invoke$arity$2(stack,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"el","el",-1618201118),el,new cljs.core.Keyword(null,"src","src",-1651076051),el.dataset.rcSrc,new cljs.core.Keyword(null,"component","component",1555936782),component,new cljs.core.Keyword(null,"args","args",1315556576),re_com.debug.goog$module$goog$object.get(el,"__rc-args")], null))),parent);
}
}));

(re_com.debug.component_stack.cljs$lang$maxFixedArity = 2);

re_com.debug.validate_args_problems_style = (function re_com$debug$validate_args_problems_style(){
return new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"min-width","min-width",1926193728),"32px",new cljs.core.Keyword(null,"min-height","min-height",398480837),"32px",new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"1.4em",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"center",new cljs.core.Keyword(null,"vertical-align","vertical-align",651007333),"center",new cljs.core.Keyword(null,"background","background",-863952629),"#FF4136"], null);
});
re_com.debug.h1_style = "background: #FF4136; color: white; font-size: 1.4em; padding: 3px";
re_com.debug.h2_style = "background: #0074D9; color: white; padding: 0.25em";
re_com.debug.code_style = "font-family: monospace; font-weight: bold; background: #eee; color: #333; padding: 3px";
re_com.debug.error_style = "font-weight: bold";
re_com.debug.index_style = "font-weight: bold; font-size: 1.1em";
re_com.debug.collision_icon = "\uD83D\uDCA5";
re_com.debug.gear_icon = "\u2699\uFE0F";
re_com.debug.blue_book_icon = "\uD83D\uDCD8";
re_com.debug.confused_icon = "\uD83D\uDE15";
re_com.debug.globe_icon = "\uD83C\uDF10";
re_com.debug.log_component_stack = (function re_com$debug$log_component_stack(stack){
console.groupCollapsed((""+"\u2022 %c Component stack (click me)"),re_com.debug.h2_style);

var seq__17439_17746 = cljs.core.seq(cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2((function (p1__17428_SHARP_,p2__17427_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p2__17427_SHARP_,new cljs.core.Keyword(null,"i","i",-1386841315),(p1__17428_SHARP_ + (1)));
}),stack));
var chunk__17440_17747 = null;
var count__17441_17748 = (0);
var i__17442_17749 = (0);
while(true){
if((i__17442_17749 < count__17441_17748)){
var map__17489_17753 = chunk__17440_17747.cljs$core$IIndexed$_nth$arity$2(null,i__17442_17749);
var map__17489_17754__$1 = cljs.core.__destructure_map(map__17489_17753);
var i_17755 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17489_17754__$1,new cljs.core.Keyword(null,"i","i",-1386841315));
var el_17756 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17489_17754__$1,new cljs.core.Keyword(null,"el","el",-1618201118));
var component_17757 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17489_17754__$1,new cljs.core.Keyword(null,"component","component",1555936782));
var src_17758 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17489_17754__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var args_17759 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17489_17754__$1,new cljs.core.Keyword(null,"args","args",1315556576));
if(cljs.core.truth_(component_17757)){
if(cljs.core.truth_(src_17758)){
var vec__17491_17765 = clojure.string.split.cljs$core$IFn$_invoke$arity$2(src_17758,/:/);
var file_17766 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17491_17765,(0),null);
var line_17767 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17491_17765,(1),null);
if(cljs.core.truth_(args_17759)){
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17755)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+" %c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(component_17757)+" ...]%c in file %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file_17766)+"%c at line %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_17767)+"%c\n      Parameters: %O\n      DOM: %o"),re_com.debug.index_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",args_17759,el_17756);
} else {
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17755)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+" %c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(component_17757)+" ...]%c in file %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file_17766)+"%c at line %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_17767)+"%c\n      DOM: %o"),re_com.debug.index_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",el_17756);
}
} else {
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17755)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+" %c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(component_17757)+" ...]%c\n      Parameters: %O\n      DOM: %o"),re_com.debug.index_style,"",re_com.debug.code_style,"",args_17759,el_17756);
}
} else {
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17755)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.globe_icon)+" %o"),re_com.debug.index_style,"",el_17756);
}


var G__17777 = seq__17439_17746;
var G__17778 = chunk__17440_17747;
var G__17779 = count__17441_17748;
var G__17780 = (i__17442_17749 + (1));
seq__17439_17746 = G__17777;
chunk__17440_17747 = G__17778;
count__17441_17748 = G__17779;
i__17442_17749 = G__17780;
continue;
} else {
var temp__5825__auto___17783 = cljs.core.seq(seq__17439_17746);
if(temp__5825__auto___17783){
var seq__17439_17784__$1 = temp__5825__auto___17783;
if(cljs.core.chunked_seq_QMARK_(seq__17439_17784__$1)){
var c__5673__auto___17785 = cljs.core.chunk_first(seq__17439_17784__$1);
var G__17786 = cljs.core.chunk_rest(seq__17439_17784__$1);
var G__17787 = c__5673__auto___17785;
var G__17788 = cljs.core.count(c__5673__auto___17785);
var G__17789 = (0);
seq__17439_17746 = G__17786;
chunk__17440_17747 = G__17787;
count__17441_17748 = G__17788;
i__17442_17749 = G__17789;
continue;
} else {
var map__17509_17790 = cljs.core.first(seq__17439_17784__$1);
var map__17509_17791__$1 = cljs.core.__destructure_map(map__17509_17790);
var i_17792 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17509_17791__$1,new cljs.core.Keyword(null,"i","i",-1386841315));
var el_17793 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17509_17791__$1,new cljs.core.Keyword(null,"el","el",-1618201118));
var component_17794 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17509_17791__$1,new cljs.core.Keyword(null,"component","component",1555936782));
var src_17795 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17509_17791__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var args_17796 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17509_17791__$1,new cljs.core.Keyword(null,"args","args",1315556576));
if(cljs.core.truth_(component_17794)){
if(cljs.core.truth_(src_17795)){
var vec__17510_17798 = clojure.string.split.cljs$core$IFn$_invoke$arity$2(src_17795,/:/);
var file_17799 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17510_17798,(0),null);
var line_17800 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17510_17798,(1),null);
if(cljs.core.truth_(args_17796)){
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17792)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+" %c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(component_17794)+" ...]%c in file %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file_17799)+"%c at line %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_17800)+"%c\n      Parameters: %O\n      DOM: %o"),re_com.debug.index_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",args_17796,el_17793);
} else {
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17792)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+" %c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(component_17794)+" ...]%c in file %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file_17799)+"%c at line %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_17800)+"%c\n      DOM: %o"),re_com.debug.index_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"",el_17793);
}
} else {
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17792)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+" %c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(component_17794)+" ...]%c\n      Parameters: %O\n      DOM: %o"),re_com.debug.index_style,"",re_com.debug.code_style,"",args_17796,el_17793);
}
} else {
console.log((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(i_17792)+"%c "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.globe_icon)+" %o"),re_com.debug.index_style,"",el_17793);
}


var G__17816 = cljs.core.next(seq__17439_17784__$1);
var G__17817 = null;
var G__17818 = (0);
var G__17819 = (0);
seq__17439_17746 = G__17816;
chunk__17440_17747 = G__17817;
count__17441_17748 = G__17818;
i__17442_17749 = G__17819;
continue;
}
} else {
}
}
break;
}

return console.groupEnd();
});
re_com.debug.log_validate_args_error_problems = (function re_com$debug$log_validate_args_error_problems(problems){
var seq__17525 = cljs.core.seq(problems);
var chunk__17526 = null;
var count__17527 = (0);
var i__17528 = (0);
while(true){
if((i__17528 < count__17527)){
var map__17555 = chunk__17526.cljs$core$IIndexed$_nth$arity$2(null,i__17528);
var map__17555__$1 = cljs.core.__destructure_map(map__17555);
var problem = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17555__$1,new cljs.core.Keyword(null,"problem","problem",1168155148));
var arg_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17555__$1,new cljs.core.Keyword(null,"arg-name","arg-name",6205923));
var expected = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17555__$1,new cljs.core.Keyword(null,"expected","expected",1583670997));
var actual = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17555__$1,new cljs.core.Keyword(null,"actual","actual",107306363));
var validate_fn_result = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17555__$1,new cljs.core.Keyword(null,"validate-fn-result","validate-fn-result",280916497));
var G__17556_17834 = problem;
var G__17556_17835__$1 = (((G__17556_17834 instanceof cljs.core.Keyword))?G__17556_17834.fqn:null);
switch (G__17556_17835__$1) {
case "unknown":
console.log((""+"\u2022 %cUnknown parameter: %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)),re_com.debug.error_style,re_com.debug.code_style);

break;
case "required":
console.log((""+"\u2022 %cMissing required parameter: %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)),re_com.debug.error_style,re_com.debug.code_style);

break;
case "ref":
console.log((""+"\u2022 %cParameter %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)+"%c expected a reactive atom but got a %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(actual)),re_com.debug.error_style,re_com.debug.code_style,re_com.debug.error_style,re_com.debug.code_style);

break;
case "validate-fn":
console.log((""+"\u2022 %cParameter %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)+"%c expected %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(expected))+"%c but got %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(actual)),re_com.debug.error_style,re_com.debug.code_style,re_com.debug.error_style,re_com.debug.code_style,re_com.debug.error_style,re_com.debug.code_style);

break;
case "validate-fn-map":
console.log((""+"\u2022 %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(validate_fn_result))),re_com.debug.error_style);

break;
default:
console.log("\u2022 ",re_com.debug.confused_icon," Unknown problem reported");

}


var G__17848 = seq__17525;
var G__17849 = chunk__17526;
var G__17850 = count__17527;
var G__17851 = (i__17528 + (1));
seq__17525 = G__17848;
chunk__17526 = G__17849;
count__17527 = G__17850;
i__17528 = G__17851;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__17525);
if(temp__5825__auto__){
var seq__17525__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__17525__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__17525__$1);
var G__17856 = cljs.core.chunk_rest(seq__17525__$1);
var G__17858 = c__5673__auto__;
var G__17859 = cljs.core.count(c__5673__auto__);
var G__17860 = (0);
seq__17525 = G__17856;
chunk__17526 = G__17858;
count__17527 = G__17859;
i__17528 = G__17860;
continue;
} else {
var map__17567 = cljs.core.first(seq__17525__$1);
var map__17567__$1 = cljs.core.__destructure_map(map__17567);
var problem = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17567__$1,new cljs.core.Keyword(null,"problem","problem",1168155148));
var arg_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17567__$1,new cljs.core.Keyword(null,"arg-name","arg-name",6205923));
var expected = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17567__$1,new cljs.core.Keyword(null,"expected","expected",1583670997));
var actual = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17567__$1,new cljs.core.Keyword(null,"actual","actual",107306363));
var validate_fn_result = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17567__$1,new cljs.core.Keyword(null,"validate-fn-result","validate-fn-result",280916497));
var G__17572_17868 = problem;
var G__17572_17869__$1 = (((G__17572_17868 instanceof cljs.core.Keyword))?G__17572_17868.fqn:null);
switch (G__17572_17869__$1) {
case "unknown":
console.log((""+"\u2022 %cUnknown parameter: %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)),re_com.debug.error_style,re_com.debug.code_style);

break;
case "required":
console.log((""+"\u2022 %cMissing required parameter: %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)),re_com.debug.error_style,re_com.debug.code_style);

break;
case "ref":
console.log((""+"\u2022 %cParameter %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)+"%c expected a reactive atom but got a %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(actual)),re_com.debug.error_style,re_com.debug.code_style,re_com.debug.error_style,re_com.debug.code_style);

break;
case "validate-fn":
console.log((""+"\u2022 %cParameter %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(arg_name)+"%c expected %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(expected))+"%c but got %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(actual)),re_com.debug.error_style,re_com.debug.code_style,re_com.debug.error_style,re_com.debug.code_style,re_com.debug.error_style,re_com.debug.code_style);

break;
case "validate-fn-map":
console.log((""+"\u2022 %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(validate_fn_result))),re_com.debug.error_style);

break;
default:
console.log("\u2022 ",re_com.debug.confused_icon," Unknown problem reported");

}


var G__17883 = cljs.core.next(seq__17525__$1);
var G__17884 = null;
var G__17885 = (0);
var G__17886 = (0);
seq__17525 = G__17883;
chunk__17526 = G__17884;
count__17527 = G__17885;
i__17528 = G__17886;
continue;
}
} else {
return null;
}
}
break;
}
});
re_com.debug.log_validate_args_error = (function re_com$debug$log_validate_args_error(element,problems,component_name,p__17579){
var map__17581 = p__17579;
var map__17581__$1 = cljs.core.__destructure_map(map__17581);
var src = map__17581__$1;
var file = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17581__$1,new cljs.core.Keyword(null,"file","file",-1269645878));
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17581__$1,new cljs.core.Keyword(null,"line","line",212345235));
var source_url = (((!(cljs.core.empty_QMARK_(re_com.config.root_url_for_compiler_output))))?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.config.root_url_for_compiler_output)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file)+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line)):null);
console.group((""+"%c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.collision_icon)+" re-com validation error "),re_com.debug.h1_style);

if(cljs.core.truth_(src)){
if(cljs.core.truth_(source_url)){
console.log((""+"\u2022 "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+"%c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.short_component_name(component_name))+" ...]%c in file %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file)+"%c at line %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line)+"%c see "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(source_url)),re_com.debug.code_style,"",re_com.debug.code_style,"",re_com.debug.code_style,"");
} else {
console.log((""+"\u2022 "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+"%c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.short_component_name(component_name))+" ...]%c in file %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(file)+"%c at line %c"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line)),re_com.debug.code_style,"",re_com.debug.code_style,"",re_com.debug.code_style);

console.log((""+"\u2022 To enable clickable source urls, add %cre-com.config/root-url-for-compiler-output%c to your %c:closure-defines%c. See https://re-com.day8.com.au/#/config"),re_com.debug.code_style,"",re_com.debug.code_style,"");
}
} else {
console.log((""+"\u2022 "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.gear_icon)+"%c["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(re_com.debug.short_component_name(component_name))+" ...]"),re_com.debug.code_style);

console.log((""+"\u2022 Learn how to add source coordinates to your components at https://re-com.day8.com.au/#/debug"));
}

re_com.debug.log_validate_args_error_problems(problems);

re_com.debug.log_component_stack(re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(element)));

return console.groupEnd();
});
re_com.debug.validate_args_error = (function re_com$debug$validate_args_error(var_args){
var args__5882__auto__ = [];
var len__5876__auto___17904 = arguments.length;
var i__5877__auto___17907 = (0);
while(true){
if((i__5877__auto___17907 < len__5876__auto___17904)){
args__5882__auto__.push((arguments[i__5877__auto___17907]));

var G__17909 = (i__5877__auto___17907 + (1));
i__5877__auto___17907 = G__17909;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.debug.validate_args_error.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.debug.validate_args_error.cljs$core$IFn$_invoke$arity$variadic = (function (p__17608){
var map__17609 = p__17608;
var map__17609__$1 = cljs.core.__destructure_map(map__17609);
var problems = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17609__$1,new cljs.core.Keyword(null,"problems","problems",2097327077));
var component = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17609__$1,new cljs.core.Keyword(null,"component","component",1555936782));
var args = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17609__$1,new cljs.core.Keyword(null,"args","args",1315556576));
var element = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var ref_fn = (function (el){
if(cljs.core.truth_(el)){
return cljs.core.reset_BANG_(element,el);
} else {
return null;
}
});
var internal_problems = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(problems);
var internal_component = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(component);
var internal_args = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(args);
return reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"display-name","display-name",694513143),"validate-args-error",new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),(function (this$){
return re_com.debug.log_validate_args_error(element,cljs.core.deref(internal_problems),cljs.core.deref(internal_component),new cljs.core.Keyword(null,"src","src",-1651076051).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(internal_args)));
}),new cljs.core.Keyword(null,"component-did-update","component-did-update",-1468549173),(function (this$,argv,old_state,snapshot){
return re_com.debug.log_validate_args_error(element,cljs.core.deref(internal_problems),cljs.core.deref(internal_component),new cljs.core.Keyword(null,"src","src",-1651076051).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(internal_args)));
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function() { 
var G__17926__delegate = function (p__17612){
var map__17613 = p__17612;
var map__17613__$1 = cljs.core.__destructure_map(map__17613);
var problems__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17613__$1,new cljs.core.Keyword(null,"problems","problems",2097327077));
var component__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17613__$1,new cljs.core.Keyword(null,"component","component",1555936782));
var args__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17613__$1,new cljs.core.Keyword(null,"args","args",1315556576));
cljs.core.reset_BANG_(internal_problems,problems__$1);

cljs.core.reset_BANG_(internal_component,component__$1);

cljs.core.reset_BANG_(internal_args,args__$1);

return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([re_com.debug.__GT_attr(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"src","src",-1651076051),new cljs.core.Keyword(null,"src","src",-1651076051).cljs$core$IFn$_invoke$arity$1(args__$1),new cljs.core.Keyword(null,"debug-as","debug-as",283322354),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component","component",1555936782),component__$1,new cljs.core.Keyword(null,"args","args",1315556576),args__$1], null),new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),ref_fn], null)], null)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"re-com validation error. Look in the DevTools console.",new cljs.core.Keyword(null,"style","style",-496642736),re_com.debug.validate_args_problems_style()], null)], 0)),re_com.debug.collision_icon], null);
};
var G__17926 = function (var_args){
var p__17612 = null;
if (arguments.length > 0) {
var G__17929__i = 0, G__17929__a = new Array(arguments.length -  0);
while (G__17929__i < G__17929__a.length) {G__17929__a[G__17929__i] = arguments[G__17929__i + 0]; ++G__17929__i;}
  p__17612 = new cljs.core.IndexedSeq(G__17929__a,0,null);
} 
return G__17926__delegate.call(this,p__17612);};
G__17926.cljs$lang$maxFixedArity = 0;
G__17926.cljs$lang$applyTo = (function (arglist__17930){
var p__17612 = cljs.core.seq(arglist__17930);
return G__17926__delegate(p__17612);
});
G__17926.cljs$core$IFn$_invoke$arity$variadic = G__17926__delegate;
return G__17926;
})()
], null));
}));

(re_com.debug.validate_args_error.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.debug.validate_args_error.cljs$lang$applyTo = (function (seq17599){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq17599));
}));

re_com.debug.stack_spy = (function re_com$debug$stack_spy(var_args){
var args__5882__auto__ = [];
var len__5876__auto___17935 = arguments.length;
var i__5877__auto___17936 = (0);
while(true){
if((i__5877__auto___17936 < len__5876__auto___17935)){
args__5882__auto__.push((arguments[i__5877__auto___17936]));

var G__17937 = (i__5877__auto___17936 + (1));
i__5877__auto___17936 = G__17937;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.debug.stack_spy.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.debug.stack_spy.cljs$core$IFn$_invoke$arity$variadic = (function (p__17648){
var map__17650 = p__17648;
var map__17650__$1 = cljs.core.__destructure_map(map__17650);
var component = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17650__$1,new cljs.core.Keyword(null,"component","component",1555936782));
var src = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17650__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var element = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var ref_fn = (function (el){
if(cljs.core.truth_(el)){
return cljs.core.reset_BANG_(element,el);
} else {
return null;
}
});
var log_fn = (function (){
var el = cljs.core.deref(element);
if(cljs.core.truth_(el)){
var first_child = cljs.core.first(el.children);
console.group("%c[stack-spy ...]",re_com.debug.code_style);

re_com.debug.log_component_stack(re_com.debug.component_stack.cljs$core$IFn$_invoke$arity$1(first_child));

return console.groupEnd();
} else {
return null;
}
});
return reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"display-name","display-name",694513143),"stack-spy",new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),log_fn,new cljs.core.Keyword(null,"component-did-update","component-did-update",-1468549173),log_fn,new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function() { 
var G__17947__delegate = function (p__17668){
var map__17670 = p__17668;
var map__17670__$1 = cljs.core.__destructure_map(map__17670);
var component__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17670__$1,new cljs.core.Keyword(null,"component","component",1555936782));
var src__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17670__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),re_com.debug.__GT_attr(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"src","src",-1651076051),src__$1,new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),ref_fn], null)], null)),component__$1], null);
};
var G__17947 = function (var_args){
var p__17668 = null;
if (arguments.length > 0) {
var G__17951__i = 0, G__17951__a = new Array(arguments.length -  0);
while (G__17951__i < G__17951__a.length) {G__17951__a[G__17951__i] = arguments[G__17951__i + 0]; ++G__17951__i;}
  p__17668 = new cljs.core.IndexedSeq(G__17951__a,0,null);
} 
return G__17947__delegate.call(this,p__17668);};
G__17947.cljs$lang$maxFixedArity = 0;
G__17947.cljs$lang$applyTo = (function (arglist__17952){
var p__17668 = cljs.core.seq(arglist__17952);
return G__17947__delegate(p__17668);
});
G__17947.cljs$core$IFn$_invoke$arity$variadic = G__17947__delegate;
return G__17947;
})()
], null));
}));

(re_com.debug.stack_spy.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.debug.stack_spy.cljs$lang$applyTo = (function (seq17633){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq17633));
}));


//# sourceMappingURL=re_com.debug.js.map
