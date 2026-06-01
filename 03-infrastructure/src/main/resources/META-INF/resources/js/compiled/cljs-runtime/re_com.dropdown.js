goog.provide('re_com.dropdown');
var module$node_modules$react$index=shadow.js.require("module$node_modules$react$index", {});
re_com.dropdown.anchor_part = (function re_com$dropdown$anchor_part(p__21174){
var map__21175 = p__21174;
var map__21175__$1 = cljs.core.__destructure_map(map__21175);
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21175__$1,new cljs.core.Keyword(null,"label","label",1718410804));
var placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21175__$1,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083));
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21175__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21175__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),re_com.theme.props(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"state","state",-1988618099),state,new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.dropdown","anchor","re-com.dropdown/anchor",812839969)], null),theme),(function (){var or__5142__auto__ = label;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = placeholder;
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return "Select an item";
}
}
})()], null);
});
re_com.dropdown.backdrop_part = (function re_com$dropdown$backdrop_part(p__21176){
var map__21177 = p__21176;
var map__21177__$1 = cljs.core.__destructure_map(map__21177);
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21177__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
var transition_BANG_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21177__$1,new cljs.core.Keyword(null,"transition!","transition!",123167659));
return (function (p__21179){
var map__21180 = p__21179;
var map__21180__$1 = cljs.core.__destructure_map(map__21180);
var dropdown_open_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21180__$1,new cljs.core.Keyword(null,"dropdown-open?","dropdown-open?",-2082396323));
var state__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21180__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21180__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21180__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),re_com.theme.props(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"transition!","transition!",123167659),transition_BANG_,new cljs.core.Keyword(null,"state","state",-1988618099),state__$1,new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.dropdown","backdrop","re-com.dropdown/backdrop",358193923)], null),theme)], null);
});
});
re_com.dropdown.nearest = (function re_com$dropdown$nearest(x,a,b){
if((Math.abs((a - x)) < Math.abs((b - x)))){
return a;
} else {
return b;
}
});
/**
 * Returns an [x y] position for popover, relative to anchor.
 *   Considers two possible vertical positions - above or below the anchor.
 *   Picks the vertical position whose midpoint is nearest to the viewport's midpoint.
 *   Calculates a left-justified horizontal position, constrained by the viewport width
 *   and the right edge of the anchor.
 * 
 *   In other words, the popover slides left & right within the anchor width,
 *   and blinks up & down, to find the least cut-off position.
 */
re_com.dropdown.optimize_position_BANG_ = (function re_com$dropdown$optimize_position_BANG_(anchor_el,popover_el){
var a_rect = anchor_el.getBoundingClientRect();
var a_x = a_rect.x;
var a_y = a_rect.y;
var a_h = a_rect.height;
var a_w = a_rect.width;
var a_b = a_rect.bottom;
var p_h = popover_el.offsetHeight;
var p_w = popover_el.offsetWidth;
var v_mid_y = (window.innerHeight / (2));
var lo_mid_y = (a_b + (p_w / (2)));
var hi_mid_y = (a_y - (p_h / (2)));
var v_pos = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(lo_mid_y,re_com.dropdown.nearest(v_mid_y,lo_mid_y,hi_mid_y)))?new cljs.core.Keyword(null,"low","low",-1601362409):new cljs.core.Keyword(null,"high","high",2027297808));
var left_bound = cljs.core.max.cljs$core$IFn$_invoke$arity$2((- a_x),(0));
var right_bound = cljs.core.max.cljs$core$IFn$_invoke$arity$2((a_w - p_w),(0));
var best_x = cljs.core.min.cljs$core$IFn$_invoke$arity$2(left_bound,right_bound);
var best_y = (function (){var G__21195 = v_pos;
var G__21195__$1 = (((G__21195 instanceof cljs.core.Keyword))?G__21195.fqn:null);
switch (G__21195__$1) {
case "low":
return a_h;

break;
case "high":
return (- p_h);

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__21195__$1))));

}
})();
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [best_x,best_y], null);
});
re_com.dropdown.body_wrapper = (function re_com$dropdown$body_wrapper(var_args){
var args__5882__auto__ = [];
var len__5876__auto___21915 = arguments.length;
var i__5877__auto___21916 = (0);
while(true){
if((i__5877__auto___21916 < len__5876__auto___21915)){
args__5882__auto__.push((arguments[i__5877__auto___21916]));

var G__21917 = (i__5877__auto___21916 + (1));
i__5877__auto___21916 = G__21917;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return re_com.dropdown.body_wrapper.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(re_com.dropdown.body_wrapper.cljs$core$IFn$_invoke$arity$variadic = (function (p__21205,children){
var map__21206 = p__21205;
var map__21206__$1 = cljs.core.__destructure_map(map__21206);
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21206__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21206__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21206__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
var anchor_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21206__$1,new cljs.core.Keyword(null,"anchor-ref","anchor-ref",-2073624017));
var popover_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21206__$1,new cljs.core.Keyword(null,"popover-ref","popover-ref",1517042135));
var anchor_position = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21206__$1,new cljs.core.Keyword(null,"anchor-position","anchor-position",379564665));
var set_popover_ref_BANG_ = (function (p1__21198_SHARP_){
return cljs.core.reset_BANG_(popover_ref,p1__21198_SHARP_);
});
var optimize_position_BANG_ = (function (){
return cljs.core.reset_BANG_(anchor_position,re_com.dropdown.optimize_position_BANG_(cljs.core.deref(anchor_ref),cljs.core.deref(popover_ref)));
});
var mounted_BANG_ = (function (){
optimize_position_BANG_();

window.addEventListener("resize",optimize_position_BANG_);

return window.addEventListener("scroll",optimize_position_BANG_);
});
var unmounted_BANG_ = (function (){
window.removeEventListener("resize",optimize_position_BANG_);

return window.removeEventListener("scroll",optimize_position_BANG_);
});
return reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),mounted_BANG_,new cljs.core.Keyword(null,"component-will-unmount","component-will-unmount",-2058314698),unmounted_BANG_,new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
var vec__21215 = (function (){var or__5142__auto__ = cljs.core.deref(anchor_position);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(0)], null);
}
})();
var left = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21215,(0),null);
var top = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21215,(1),null);
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div#popover","div#popover",-1687560121),re_com.theme.apply(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),set_popover_ref_BANG_,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"z-index","z-index",1892827090),(99999),new cljs.core.Keyword(null,"position","position",-2011731912),"absolute",new cljs.core.Keyword(null,"top","top",-1856271961),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(top)+"px"),new cljs.core.Keyword(null,"left","left",-399115937),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(left)+"px"),new cljs.core.Keyword(null,"opacity","opacity",397153780),(cljs.core.truth_(cljs.core.deref(anchor_position))?(1):(0)),new cljs.core.Keyword(null,"transition","transition",765692007),"opacity 0.2s"], null)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"state","state",-1988618099),state,new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.dropdown","body-wrapper","re-com.dropdown/body-wrapper",-1183586298)], null),theme)], null),children);
})], null));
}));

(re_com.dropdown.body_wrapper.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(re_com.dropdown.body_wrapper.cljs$lang$applyTo = (function (seq21200){
var G__21201 = cljs.core.first(seq21200);
var seq21200__$1 = cljs.core.next(seq21200);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__21201,seq21200__$1);
}));

/**
 * A clickable anchor above an openable, floating body.
 *   
 */
re_com.dropdown.dropdown = (function re_com$dropdown$dropdown(var_args){
var args__5882__auto__ = [];
var len__5876__auto___21920 = arguments.length;
var i__5877__auto___21921 = (0);
while(true){
if((i__5877__auto___21921 < len__5876__auto___21920)){
args__5882__auto__.push((arguments[i__5877__auto___21921]));

var G__21923 = (i__5877__auto___21921 + (1));
i__5877__auto___21921 = G__21923;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.dropdown.dropdown.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.dropdown.dropdown.cljs$core$IFn$_invoke$arity$variadic = (function (p__21242){
var map__21243 = p__21242;
var map__21243__$1 = cljs.core.__destructure_map(map__21243);
var model = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21243__$1,new cljs.core.Keyword(null,"model","model",331153215),reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null));
var vec__21244 = cljs.core.repeatedly.cljs$core$IFn$_invoke$arity$1((function (){
return reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}));
var focused_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21244,(0),null);
var anchor_ref = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21244,(1),null);
var popover_ref = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21244,(2),null);
var anchor_position = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21244,(3),null);
var anchor_ref_BANG_ = (function (p1__21229_SHARP_){
return cljs.core.reset_BANG_(anchor_ref,p1__21229_SHARP_);
});
var transitionable = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(cljs.core.deref(model))?new cljs.core.Keyword(null,"in","in",-1531184865):new cljs.core.Keyword(null,"out","out",-910545517)));
return (function() { 
var re_com$dropdown$dropdown_render__delegate = function (p__21256){
var map__21258 = p__21256;
var map__21258__$1 = cljs.core.__destructure_map(map__21258);
var args = map__21258__$1;
var anchor = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"anchor","anchor",1549638489));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var backdrop = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"backdrop","backdrop",-1291357381));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"height","height",1025178622));
var anchor_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"anchor-height","anchor-height",589311520));
var min_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"min-width","min-width",1926193728));
var tab_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"tab-index","tab-index",895755393));
var max_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"max-height","max-height",-612563804));
var min_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"min-height","min-height",398480837));
var placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"width","width",-384071477));
var main_theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"main-theme","main-theme",-411793492));
var max_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"max-width","max-width",-1939924051));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
var theme_vars = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"theme-vars","theme-vars",-1383796847));
var base_theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"base-theme","base-theme",-1857412877));
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21258__$1,new cljs.core.Keyword(null,"label","label",1718410804));
var theme__$1 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"variables","variables",1563680814),theme_vars,new cljs.core.Keyword(null,"base","base",185279322),base_theme,new cljs.core.Keyword(null,"main","main",-2117802661),main_theme,new cljs.core.Keyword(null,"user","user",1532431356),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [theme,(re_com.theme.parts.cljs$core$IFn$_invoke$arity$1 ? re_com.theme.parts.cljs$core$IFn$_invoke$arity$1(parts) : re_com.theme.parts.call(null,parts))], null)], null);
var state = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"openable","openable",1470121013),(cljs.core.truth_(cljs.core.deref(model))?new cljs.core.Keyword(null,"open","open",-1763596448):new cljs.core.Keyword(null,"closed","closed",-919675359)),new cljs.core.Keyword(null,"enable","enable",-1839114332),(cljs.core.truth_(disabled_QMARK_)?new cljs.core.Keyword(null,"disabled","disabled",-1529784218):new cljs.core.Keyword(null,"enabled","enabled",1195909756)),new cljs.core.Keyword(null,"tab-index","tab-index",895755393),tab_index,new cljs.core.Keyword(null,"focusable","focusable",1031236480),(cljs.core.truth_(cljs.core.deref(focused_QMARK_))?new cljs.core.Keyword(null,"focused","focused",1851572115):new cljs.core.Keyword(null,"blurred","blurred",944900151)),new cljs.core.Keyword(null,"transitionable","transitionable",-1988279536),cljs.core.deref(transitionable)], null);
var open_BANG_ = (cljs.core.truth_(on_change)?(function (event){
(on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(true) : on_change.call(null,true));

return null;
}):(function (event){
cljs.core.reset_BANG_(model,true);

return null;
}));
var close_BANG_ = (cljs.core.truth_(on_change)?(function (event){
(on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(false) : on_change.call(null,false));

return null;
}):(function (event){
cljs.core.reset_BANG_(model,false);

return null;
}));
var transition_BANG_ = (function (k){
var fexpr__21309 = (function (){var G__21310 = k;
var G__21310__$1 = (((G__21310 instanceof cljs.core.Keyword))?G__21310.fqn:null);
switch (G__21310__$1) {
case "toggle":
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"openable","openable",1470121013).cljs$core$IFn$_invoke$arity$1(state),new cljs.core.Keyword(null,"open","open",-1763596448))){
return open_BANG_;
} else {
return close_BANG_;
}

break;
case "open":
return open_BANG_;

break;
case "close":
return close_BANG_;

break;
case "focus":
return (function (){
return cljs.core.reset_BANG_(focused_QMARK_,true);
});

break;
case "blur":
return (function (){
return cljs.core.reset_BANG_(focused_QMARK_,false);
});

break;
case "enter":
return (function (){
return setTimeout((function (){
return cljs.core.reset_BANG_(transitionable,new cljs.core.Keyword(null,"in","in",-1531184865));
}),(50));
});

break;
case "exit":
return (function (){
return setTimeout((function (){
return cljs.core.reset_BANG_(transitionable,new cljs.core.Keyword(null,"out","out",-910545517));
}),(50));
});

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__21310__$1))));

}
})();
return (fexpr__21309.cljs$core$IFn$_invoke$arity$0 ? fexpr__21309.cljs$core$IFn$_invoke$arity$0() : fexpr__21309.call(null));
});
var themed = (function (part,props){
return re_com.theme.apply(props,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"state","state",-1988618099),state,new cljs.core.Keyword(null,"part","part",77757738),part,new cljs.core.Keyword(null,"transition!","transition!",123167659),transition_BANG_], null),theme__$1);
});
var part_props = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),placeholder,new cljs.core.Keyword(null,"transition!","transition!",123167659),transition_BANG_,new cljs.core.Keyword(null,"label","label",1718410804),label,new cljs.core.Keyword(null,"theme","theme",-1247880880),theme__$1,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"state","state",-1988618099),state], null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.v_box,themed(new cljs.core.Keyword("re-com.dropdown","wrapper","re-com.dropdown/wrapper",1783074340),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),142], null)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"height","height",1025178622),anchor_height], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"open","open",-1763596448),new cljs.core.Keyword(null,"openable","openable",1470121013).cljs$core$IFn$_invoke$arity$1(state)))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,backdrop,part_props,re_com.dropdown.backdrop_part], null):null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,themed(new cljs.core.Keyword("re-com.dropdown","anchor-wrapper","re-com.dropdown/anchor-wrapper",-310465993),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),149], null)),new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),anchor_ref_BANG_,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(model,cljs.core.not);
})], null),new cljs.core.Keyword(null,"child","child",623967545),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,anchor,part_props,re_com.dropdown.anchor_part], null)], null))], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"open","open",-1763596448),new cljs.core.Keyword(null,"openable","openable",1470121013).cljs$core$IFn$_invoke$arity$1(state)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.dropdown.body_wrapper,new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"anchor-ref","anchor-ref",-2073624017),anchor_ref,new cljs.core.Keyword(null,"popover-ref","popover-ref",1517042135),popover_ref,new cljs.core.Keyword(null,"anchor-position","anchor-position",379564665),anchor_position,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"state","state",-1988618099),state,new cljs.core.Keyword(null,"theme","theme",-1247880880),theme__$1], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,body,part_props], null)], null):null)], null)], null))], null);
};
var re_com$dropdown$dropdown_render = function (var_args){
var p__21256 = null;
if (arguments.length > 0) {
var G__21977__i = 0, G__21977__a = new Array(arguments.length -  0);
while (G__21977__i < G__21977__a.length) {G__21977__a[G__21977__i] = arguments[G__21977__i + 0]; ++G__21977__i;}
  p__21256 = new cljs.core.IndexedSeq(G__21977__a,0,null);
} 
return re_com$dropdown$dropdown_render__delegate.call(this,p__21256);};
re_com$dropdown$dropdown_render.cljs$lang$maxFixedArity = 0;
re_com$dropdown$dropdown_render.cljs$lang$applyTo = (function (arglist__21978){
var p__21256 = cljs.core.seq(arglist__21978);
return re_com$dropdown$dropdown_render__delegate(p__21256);
});
re_com$dropdown$dropdown_render.cljs$core$IFn$_invoke$arity$variadic = re_com$dropdown$dropdown_render__delegate;
return re_com$dropdown$dropdown_render;
})()
;
}));

(re_com.dropdown.dropdown.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.dropdown.dropdown.cljs$lang$applyTo = (function (seq21233){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21233));
}));

/**
 * In a vector of maps (where each map has an :id), return the id of the choice offset posititions away
 * from id (usually +1 or -1 to go to next/previous). Also accepts :start and :end
 */
re_com.dropdown.move_to_new_choice = (function re_com$dropdown$move_to_new_choice(choices,id_fn,id,offset){
var current_index = re_com.util.position_for_id.cljs$core$IFn$_invoke$arity$variadic(id,choices,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"id-fn","id-fn",316222798),id_fn], 0));
var new_index = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(offset,new cljs.core.Keyword(null,"start","start",-355208981)))?(0):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(offset,new cljs.core.Keyword(null,"end","end",-268185958)))?(cljs.core.count(choices) - (1)):(((current_index == null))?(0):cljs.core.mod((current_index + offset),cljs.core.count(choices))
)));
if(cljs.core.truth_((function (){var and__5140__auto__ = new_index;
if(cljs.core.truth_(and__5140__auto__)){
return (cljs.core.count(choices) > (0));
} else {
return and__5140__auto__;
}
})())){
var G__21337 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(choices,new_index);
return (id_fn.cljs$core$IFn$_invoke$arity$1 ? id_fn.cljs$core$IFn$_invoke$arity$1(G__21337) : id_fn.call(null,G__21337));
} else {
return null;
}
});
/**
 * If necessary, inserts group headings entries into the choices
 */
re_com.dropdown.choices_with_group_headings = (function re_com$dropdown$choices_with_group_headings(opts,group_fn){
var groups = cljs.core.partition_by.cljs$core$IFn$_invoke$arity$2(group_fn,opts);
var group_headers = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__21339_SHARP_){
return cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"group","group",582596132)],[cljs.core.gensym.cljs$core$IFn$_invoke$arity$0(),p1__21339_SHARP_]);
}),cljs.core.map.cljs$core$IFn$_invoke$arity$2(group_fn,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.first,groups)));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [group_headers,groups], null);
});
/**
 * Filter a list of choices based on a filter string using plain string searches (case insensitive). Less powerful
 * than regex's but no confusion with reserved characters
 */
re_com.dropdown.filter_choices = (function re_com$dropdown$filter_choices(choices,group_fn,label_fn,filter_text){
var lower_filter_text = clojure.string.lower_case(filter_text);
var filter_fn = (function (opt){
var group = ((((group_fn.cljs$core$IFn$_invoke$arity$1 ? group_fn.cljs$core$IFn$_invoke$arity$1(opt) : group_fn.call(null,opt)) == null))?"":(group_fn.cljs$core$IFn$_invoke$arity$1 ? group_fn.cljs$core$IFn$_invoke$arity$1(opt) : group_fn.call(null,opt)));
var label = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((label_fn.cljs$core$IFn$_invoke$arity$1 ? label_fn.cljs$core$IFn$_invoke$arity$1(opt) : label_fn.call(null,opt))));
return (((clojure.string.lower_case(group).indexOf(lower_filter_text) >= (0))) || ((clojure.string.lower_case(label).indexOf(lower_filter_text) >= (0))));
});
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(filter_fn,choices);
});
/**
 * Filter a list of choices based on a filter string using regex's (case insensitive). More powerful but can cause
 * confusion for users entering reserved characters such as [ ] * + . ( ) etc.
 */
re_com.dropdown.filter_choices_regex = (function re_com$dropdown$filter_choices_regex(choices,group_fn,label_fn,filter_text){
var re = (function (){try{return (new RegExp(filter_text,"i"));
}catch (e21353){if((e21353 instanceof Object)){
var e = e21353;
return null;
} else {
throw e21353;

}
}})();
var filter_fn = cljs.core.partial.cljs$core$IFn$_invoke$arity$2((function (re__$1,opt){
if((re__$1 == null)){
return null;
} else {
var or__5142__auto__ = re__$1.test((group_fn.cljs$core$IFn$_invoke$arity$1 ? group_fn.cljs$core$IFn$_invoke$arity$1(opt) : group_fn.call(null,opt)));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return re__$1.test((label_fn.cljs$core$IFn$_invoke$arity$1 ? label_fn.cljs$core$IFn$_invoke$arity$1(opt) : label_fn.call(null,opt)));
}
}
}),re);
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(filter_fn,choices);
});
/**
 * Filter a list of choices extra data within the choices vector
 */
re_com.dropdown.filter_choices_by_keyword = (function re_com$dropdown$filter_choices_by_keyword(choices,keyword,value){
var filter_fn = (function (opt){
return ((keyword.cljs$core$IFn$_invoke$arity$1 ? keyword.cljs$core$IFn$_invoke$arity$1(opt) : keyword.call(null,opt)).indexOf(value) >= (0));
});
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(filter_fn,choices);
});
/**
 * Return text after insertion in place of selection
 */
re_com.dropdown.insert = (function re_com$dropdown$insert(var_args){
var args__5882__auto__ = [];
var len__5876__auto___21989 = arguments.length;
var i__5877__auto___21990 = (0);
while(true){
if((i__5877__auto___21990 < len__5876__auto___21989)){
args__5882__auto__.push((arguments[i__5877__auto___21990]));

var G__22004 = (i__5877__auto___21990 + (1));
i__5877__auto___21990 = G__22004;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.dropdown.insert.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.dropdown.insert.cljs$core$IFn$_invoke$arity$variadic = (function (p__21357){
var map__21358 = p__21357;
var map__21358__$1 = cljs.core.__destructure_map(map__21358);
var text = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21358__$1,new cljs.core.Keyword(null,"text","text",-1790561697));
var sel_start = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21358__$1,new cljs.core.Keyword(null,"sel-start","sel-start",175359789));
var sel_end = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21358__$1,new cljs.core.Keyword(null,"sel-end","sel-end",81085265));
var ins = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21358__$1,new cljs.core.Keyword(null,"ins","ins",-1021983570));
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(text)?cljs.core.subs.cljs$core$IFn$_invoke$arity$3(text,(0),sel_start):null))+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ins)+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(text)?cljs.core.subs.cljs$core$IFn$_invoke$arity$2(text,sel_end):null)));
}));

(re_com.dropdown.insert.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.dropdown.insert.cljs$lang$applyTo = (function (seq21354){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21354));
}));

/**
 * Return text/selection map after insertion in place of selection & completion
 */
re_com.dropdown.auto_complete = (function re_com$dropdown$auto_complete(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22012 = arguments.length;
var i__5877__auto___22013 = (0);
while(true){
if((i__5877__auto___22013 < len__5876__auto___22012)){
args__5882__auto__.push((arguments[i__5877__auto___22013]));

var G__22017 = (i__5877__auto___22013 + (1));
i__5877__auto___22013 = G__22017;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.dropdown.auto_complete.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.dropdown.auto_complete.cljs$core$IFn$_invoke$arity$variadic = (function (p__21368){
var map__21369 = p__21368;
var map__21369__$1 = cljs.core.__destructure_map(map__21369);
var choices = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21369__$1,new cljs.core.Keyword(null,"choices","choices",1385611597));
var text = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21369__$1,new cljs.core.Keyword(null,"text","text",-1790561697));
var sel_start = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21369__$1,new cljs.core.Keyword(null,"sel-start","sel-start",175359789));
var sel_end = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21369__$1,new cljs.core.Keyword(null,"sel-end","sel-end",81085265));
var ins = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21369__$1,new cljs.core.Keyword(null,"ins","ins",-1021983570));
var text_SINGLEQUOTE_ = re_com.dropdown.insert.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"text","text",-1790561697),text,new cljs.core.Keyword(null,"sel-start","sel-start",175359789),sel_start,new cljs.core.Keyword(null,"sel-end","sel-end",81085265),sel_end,new cljs.core.Keyword(null,"ins","ins",-1021983570),ins], 0));
var find = (function (p1__21363_SHARP_){
return goog.string.caseInsensitiveStartsWith(p1__21363_SHARP_,text_SINGLEQUOTE_);
});
var ret = (function (){var temp__5825__auto__ = cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(find,choices));
if(temp__5825__auto__){
var xs__6385__auto__ = temp__5825__auto__;
var choice = cljs.core.first(xs__6385__auto__);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"text","text",-1790561697),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(text_SINGLEQUOTE_)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.subs.cljs$core$IFn$_invoke$arity$2(choice,((text_SINGLEQUOTE_).length)))),new cljs.core.Keyword(null,"sel-start","sel-start",175359789),(sel_start + cljs.core.count(ins)),new cljs.core.Keyword(null,"sel-end","sel-end",81085265),cljs.core.count(choice)], null);
} else {
return null;
}
})();
if(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sel-start","sel-start",175359789).cljs$core$IFn$_invoke$arity$1(ret),new cljs.core.Keyword(null,"sel-end","sel-end",81085265).cljs$core$IFn$_invoke$arity$1(ret))) && (cljs.core.seq(ins)))){
return ret;
} else {
return null;
}
}));

(re_com.dropdown.auto_complete.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.dropdown.auto_complete.cljs$lang$applyTo = (function (seq21365){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21365));
}));

/**
 * Capitalize the first letter leaving the rest as is
 */
re_com.dropdown.capitalize_first_letter = (function re_com$dropdown$capitalize_first_letter(text){
if(cljs.core.seq(text)){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.upper_case(cljs.core.first(text)))+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.subs.cljs$core$IFn$_invoke$arity$2(text,(1))));
} else {
return text;
}
});
re_com.dropdown.show_selected_item = (function re_com$dropdown$show_selected_item(node){
var item_offset_top = node.offsetTop;
var item_offset_bottom = (item_offset_top + node.clientHeight);
var parent = node.parentNode;
var parent_height = parent.clientHeight;
var parent_visible_top = parent.scrollTop;
var parent_visible_bottom = (parent_visible_top + parent_height);
var new_scroll_top = (((item_offset_bottom > parent_visible_bottom))?cljs.core.max.cljs$core$IFn$_invoke$arity$2((item_offset_bottom - parent_height),(0)):(((item_offset_top < parent_visible_top))?item_offset_top:null));
if(cljs.core.truth_(new_scroll_top)){
return (parent.scrollTop = new_scroll_top);
} else {
return null;
}
});
/**
 * Render a group heading
 */
re_com.dropdown.make_group_heading = (function re_com$dropdown$make_group_heading(m){
return cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.group-result","li.group-result",1074686727),new cljs.core.Keyword(null,"group","group",582596132).cljs$core$IFn$_invoke$arity$1(m)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(m)], null));
});
/**
 * Render a choice item and set up appropriate mouse events
 */
re_com.dropdown.choice_item = (function re_com$dropdown$choice_item(id,label,on_click,internal_model){
var mouse_over_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
var _BANG_ref = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var ref_BANG_ = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(cljs.core.reset_BANG_,_BANG_ref);
var show_BANG_ = (function (){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(internal_model),id)){
return re_com.dropdown.show_selected_item(cljs.core.deref(_BANG_ref));
} else {
return null;
}
});
return reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),show_BANG_,new cljs.core.Keyword(null,"component-did-update","component-did-update",-1468549173),show_BANG_,new cljs.core.Keyword(null,"display-name","display-name",694513143),"choice-item",new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (id__$1,label__$1,on_click__$1,internal_model__$1){
var selected = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(internal_model__$1),id__$1);
var class$ = ((selected)?"highlighted":(cljs.core.truth_(cljs.core.deref(mouse_over_QMARK_))?"mouseover":null));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"active-result group-option "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(class$)),new cljs.core.Keyword(null,"ref","ref",1289896967),ref_BANG_,new cljs.core.Keyword(null,"on-mouse-over","on-mouse-over",-858472552),(function (event){
cljs.core.reset_BANG_(mouse_over_QMARK_,true);

return null;
}),new cljs.core.Keyword(null,"on-mouse-out","on-mouse-out",643448647),(function (event){
cljs.core.reset_BANG_(mouse_over_QMARK_,false);

return null;
}),new cljs.core.Keyword(null,"on-mouse-down","on-mouse-down",1147755470),(function (event){
(on_click__$1.cljs$core$IFn$_invoke$arity$1 ? on_click__$1.cljs$core$IFn$_invoke$arity$1(id__$1) : on_click__$1.call(null,id__$1));

event.preventDefault();

return null;
})], null),label__$1], null);
})], null));
});
re_com.dropdown.make_choice_item = (function re_com$dropdown$make_choice_item(id_fn,render_fn,callback,internal_model,opt){
var id = (id_fn.cljs$core$IFn$_invoke$arity$1 ? id_fn.cljs$core$IFn$_invoke$arity$1(opt) : id_fn.call(null,opt));
var markup = (render_fn.cljs$core$IFn$_invoke$arity$1 ? render_fn.cljs$core$IFn$_invoke$arity$1(opt) : render_fn.call(null,opt));
return cljs.core.with_meta(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.dropdown.choice_item,id,markup,callback,internal_model], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(id))], null));
});
/**
 * Render a filter text box
 */
re_com.dropdown.filter_text_box = (function re_com$dropdown$filter_text_box(filter_box_QMARK_,filter_text,key_handler,drop_showing_QMARK_,set_filter_text,filter_placeholder){
var _BANG_ref = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var ref_BANG_ = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(cljs.core.reset_BANG_,_BANG_ref);
var focus_BANG_ = (function (){
return cljs.core.deref(_BANG_ref).firstChild.focus();
});
return reagent.core.create_class.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),focus_BANG_,new cljs.core.Keyword(null,"component-did-update","component-did-update",-1468549173),focus_BANG_,new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (filter_box_QMARK___$1,filter_text__$1,key_handler__$1,drop_showing_QMARK___$1,set_filter_text__$1,filter_placeholder__$1){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.chosen-search","div.chosen-search",-210987404),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),ref_BANG_], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"auto-complete","auto-complete",244958848),"off",new cljs.core.Keyword(null,"style","style",-496642736),(cljs.core.truth_(filter_box_QMARK___$1)?null:new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"position","position",-2011731912),"absolute",new cljs.core.Keyword(null,"width","width",-384071477),"0px",new cljs.core.Keyword(null,"padding","padding",1660304693),"0px",new cljs.core.Keyword(null,"border","border",1444987323),"none"], null)),new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(filter_text__$1),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),filter_placeholder__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (event){
var G__21403_22048 = event.target.value;
(set_filter_text__$1.cljs$core$IFn$_invoke$arity$1 ? set_filter_text__$1.cljs$core$IFn$_invoke$arity$1(G__21403_22048) : set_filter_text__$1.call(null,G__21403_22048));

return null;
}),new cljs.core.Keyword(null,"on-key-down","on-key-down",-1374733765),(function (event){
if(cljs.core.truth_((key_handler__$1.cljs$core$IFn$_invoke$arity$1 ? key_handler__$1.cljs$core$IFn$_invoke$arity$1(event) : key_handler__$1.call(null,event)))){
} else {
event.stopPropagation();

event.preventDefault();
}

return null;
}),new cljs.core.Keyword(null,"on-blur","on-blur",814300747),(function (event){
cljs.core.reset_BANG_(drop_showing_QMARK___$1,false);

return null;
})], null)], null)], null);
})], null));
});
/**
 * Render the top part of the dropdown, with the clickable area and the up/down arrow
 */
re_com.dropdown.dropdown_top = (function re_com$dropdown$dropdown_top(){
var ignore_click = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
return (function (internal_model,choices,id_fn,label_fn,tab_index,placeholder,dropdown_click,key_handler,filter_box_QMARK_,drop_showing_QMARK_,title_QMARK_,disabled_QMARK_){
var _ = reagent.core.set_state(reagent.core.current_component(),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"filter-box?","filter-box?",-1157583688),filter_box_QMARK_], null));
var text = (((!((cljs.core.deref(internal_model) == null))))?(function (){var G__21412 = re_com.util.item_for_id.cljs$core$IFn$_invoke$arity$variadic(cljs.core.deref(internal_model),choices,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"id-fn","id-fn",316222798),id_fn], 0));
return (label_fn.cljs$core$IFn$_invoke$arity$1 ? label_fn.cljs$core$IFn$_invoke$arity$1(G__21412) : label_fn.call(null,G__21412));
})():placeholder);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.chosen-single.chosen-default","a.chosen-single.chosen-default",-2089562458),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"tab-index","tab-index",895755393),(function (){var or__5142__auto__ = tab_index;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (0);
}
})(),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (event){
if(cljs.core.truth_(cljs.core.deref(ignore_click))){
cljs.core.reset_BANG_(ignore_click,false);
} else {
(dropdown_click.cljs$core$IFn$_invoke$arity$0 ? dropdown_click.cljs$core$IFn$_invoke$arity$0() : dropdown_click.call(null));
}

return null;
}),new cljs.core.Keyword(null,"on-mouse-down","on-mouse-down",1147755470),(function (event){
if(cljs.core.truth_(cljs.core.deref(drop_showing_QMARK_))){
cljs.core.reset_BANG_(ignore_click,true);
} else {
}

return null;
}),new cljs.core.Keyword(null,"on-key-down","on-key-down",-1374733765),(function (event){
(key_handler.cljs$core$IFn$_invoke$arity$1 ? key_handler.cljs$core$IFn$_invoke$arity$1(event) : key_handler.call(null,event));

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(event.key,"Enter")){
cljs.core.reset_BANG_(ignore_click,true);
} else {
}

return null;
})], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),(cljs.core.truth_(title_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"title","title",636505583),text], null):null),text], null),((cljs.core.not(disabled_QMARK_))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470)], null)], null):null)], null);
});
});
re_com.dropdown.handle_free_text_insertion = (function re_com$dropdown$handle_free_text_insertion(event,ins,auto_complete_QMARK_,capitalize_QMARK_,choices,internal_model,free_text_sel_range,free_text_change){
var input = event.target;
var input_sel_start = input.selectionStart;
var input_sel_end = input.selectionEnd;
var ins_SINGLEQUOTE_ = (function (){var G__21422 = ins;
if(cljs.core.truth_((function (){var and__5140__auto__ = capitalize_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return (input_sel_start === (0));
} else {
return and__5140__auto__;
}
})())){
return re_com.dropdown.capitalize_first_letter(G__21422);
} else {
return G__21422;
}
})();
var auto_complete_ret = (function (){var and__5140__auto__ = auto_complete_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return re_com.dropdown.auto_complete.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"choices","choices",1385611597),choices,new cljs.core.Keyword(null,"text","text",-1790561697),cljs.core.deref(internal_model),new cljs.core.Keyword(null,"sel-start","sel-start",175359789),input_sel_start,new cljs.core.Keyword(null,"sel-end","sel-end",81085265),input_sel_end,new cljs.core.Keyword(null,"ins","ins",-1021983570),ins_SINGLEQUOTE_], 0));
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_(auto_complete_ret)){
var map__21425 = auto_complete_ret;
var map__21425__$1 = cljs.core.__destructure_map(map__21425);
var text = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21425__$1,new cljs.core.Keyword(null,"text","text",-1790561697));
var sel_start = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21425__$1,new cljs.core.Keyword(null,"sel-start","sel-start",175359789));
var sel_end = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21425__$1,new cljs.core.Keyword(null,"sel-end","sel-end",81085265));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(internal_model),text)){
input.setSelectionRange(sel_start,sel_end);
} else {
cljs.core.reset_BANG_(free_text_sel_range,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sel_start,sel_end], null));

(free_text_change.cljs$core$IFn$_invoke$arity$1 ? free_text_change.cljs$core$IFn$_invoke$arity$1(text) : free_text_change.call(null,text));
}

return event.preventDefault();
} else {
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(ins,ins_SINGLEQUOTE_)){
cljs.core.reset_BANG_(free_text_sel_range,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(input_sel_start + cljs.core.count(ins)),(input_sel_start + cljs.core.count(ins))], null));

var G__21427_22065 = re_com.dropdown.insert.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"text","text",-1790561697),cljs.core.deref(internal_model),new cljs.core.Keyword(null,"sel-start","sel-start",175359789),input_sel_start,new cljs.core.Keyword(null,"sel-end","sel-end",81085265),input_sel_end,new cljs.core.Keyword(null,"ins","ins",-1021983570),ins_SINGLEQUOTE_], 0));
(free_text_change.cljs$core$IFn$_invoke$arity$1 ? free_text_change.cljs$core$IFn$_invoke$arity$1(G__21427_22065) : free_text_change.call(null,G__21427_22065));

return event.preventDefault();
} else {
return null;
}
}
});
/**
 * Base function (before lifecycle metadata) to render the top part of the dropdown (free-text), with the editable area and the up/down arrow
 */
re_com.dropdown.free_text_dropdown_top_base = (function re_com$dropdown$free_text_dropdown_top_base(free_text_input,select_free_text_QMARK_,free_text_focused_QMARK_,free_text_sel_range,internal_model,tab_index,placeholder,dropdown_click,key_handler,filter_box_QMARK_,drop_showing_QMARK_,cancel,width,free_text_change,auto_complete_QMARK_,choices,capitalize_QMARK_,disabled_QMARK_){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul.chosen-choices","ul.chosen-choices",753954766),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.search-field","li.search-field",371261414),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.free-text","div.free-text",-1830216539),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),(cljs.core.truth_(disabled_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"#EEE"], null):null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"auto-complete","auto-complete",244958848),new cljs.core.Keyword(null,"tab-index","tab-index",895755393),new cljs.core.Keyword(null,"on-key-press","on-key-press",-399563677),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"on-focus","on-focus",-13737624),new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"on-blur","on-blur",814300747),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"on-mouse-down","on-mouse-down",1147755470),new cljs.core.Keyword(null,"on-paste","on-paste",-50859856),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"on-change","on-change",-732046149),new cljs.core.Keyword(null,"on-key-down","on-key-down",-1374733765)],["off",tab_index,(function (event){
var ins_22068 = event.key;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(ins_22068),(1))){
re_com.dropdown.handle_free_text_insertion(event,ins_22068,auto_complete_QMARK_,capitalize_QMARK_,choices,internal_model,free_text_sel_range,free_text_change);
} else {
}

return null;
}),placeholder,disabled_QMARK_,(function (p1__21433_SHARP_){
return cljs.core.reset_BANG_(free_text_input,p1__21433_SHARP_);
}),(function (event){
cljs.core.reset_BANG_(free_text_focused_QMARK_,true);

cljs.core.reset_BANG_(select_free_text_QMARK_,true);

return null;
}),cljs.core.deref(internal_model),(function (event){
if(cljs.core.truth_(filter_box_QMARK_)){
} else {
(cancel.cljs$core$IFn$_invoke$arity$0 ? cancel.cljs$core$IFn$_invoke$arity$0() : cancel.call(null));
}

cljs.core.reset_BANG_(free_text_focused_QMARK_,false);

return null;
}),"text",(function (event){
if(cljs.core.truth_(cljs.core.deref(drop_showing_QMARK_))){
(cancel.cljs$core$IFn$_invoke$arity$0 ? cancel.cljs$core$IFn$_invoke$arity$0() : cancel.call(null));

event.preventDefault();
} else {
}

return null;
}),(function (event){
var ins_22070 = event.clipboardData.getData("Text");
re_com.dropdown.handle_free_text_insertion(event,ins_22070,auto_complete_QMARK_,capitalize_QMARK_,choices,internal_model,free_text_sel_range,free_text_change);

return null;
}),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),width], null),"form-control",(function (event){
var value_22071 = event.target.value;
var G__21447_22072 = (function (){var G__21448 = value_22071;
if(cljs.core.truth_(capitalize_QMARK_)){
return re_com.dropdown.capitalize_first_letter(G__21448);
} else {
return G__21448;
}
})();
(free_text_change.cljs$core$IFn$_invoke$arity$1 ? free_text_change.cljs$core$IFn$_invoke$arity$1(G__21447_22072) : free_text_change.call(null,G__21447_22072));

return null;
}),(function (event){
if(cljs.core.truth_((key_handler.cljs$core$IFn$_invoke$arity$1 ? key_handler.cljs$core$IFn$_invoke$arity$1(event) : key_handler.call(null,event)))){
} else {
event.stopPropagation();

event.preventDefault();
}

return null;
})])], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.b-wrapper","span.b-wrapper",126573946),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-mouse-down","on-mouse-down",1147755470),(function (event){
(dropdown_click.cljs$core$IFn$_invoke$arity$0 ? dropdown_click.cljs$core$IFn$_invoke$arity$0() : dropdown_click.call(null));

if(cljs.core.truth_(cljs.core.deref(free_text_focused_QMARK_))){
event.preventDefault();
} else {
}

return null;
})], null),((cljs.core.not(disabled_QMARK_))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470)], null):null)], null)], null)], null)], null);
});
/**
 * Render the top part of the dropdown (free-text), with the editable area and the up/down arrow
 */
re_com.dropdown.free_text_dropdown_top = cljs.core.with_meta(re_com.dropdown.free_text_dropdown_top_base,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"component-did-update","component-did-update",-1468549173),(function (p1__21450_SHARP_){
var vec__21452 = reagent.core.argv(p1__21450_SHARP_);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21452,(0),null);
var free_text_input = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21452,(1),null);
var select_free_text_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21452,(2),null);
var free_text_focused_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21452,(3),null);
var free_text_sel_range = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21452,(4),null);
if(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.deref(free_text_input);
if(cljs.core.truth_(and__5140__auto__)){
var and__5140__auto____$1 = cljs.core.deref(select_free_text_QMARK_);
if(cljs.core.truth_(and__5140__auto____$1)){
return cljs.core.deref(free_text_focused_QMARK_);
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
})())){
cljs.core.deref(free_text_input).select();
} else {
}

if(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.deref(free_text_input);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.deref(free_text_sel_range);
} else {
return and__5140__auto__;
}
})())){
cljs.core.deref(free_text_input).setSelectionRange(cljs.core.first(cljs.core.deref(free_text_sel_range)),cljs.core.second(cljs.core.deref(free_text_sel_range)));

return cljs.core.reset_BANG_(free_text_sel_range,null);
} else {
return null;
}
})], null));
re_com.dropdown.fn_or_vector_of_maps_or_strings_QMARK_ = (function re_com$dropdown$fn_or_vector_of_maps_or_strings_QMARK_(v){
return ((cljs.core.fn_QMARK_(v)) || (((re_com.validate.vector_of_maps_QMARK_(v)) || (((cljs.core.sequential_QMARK_(v)) && (((cljs.core.empty_QMARK_(v)) || (typeof cljs.core.first(v) === 'string'))))))));
});
/**
 * Load choices if choices is callback.
 */
re_com.dropdown.load_choices_STAR_ = (function re_com$dropdown$load_choices_STAR_(choices_state,choices,text,regex_filter_QMARK_){
var id = (new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(choices_state)) + (1));
var callback = (function (p__21466){
var map__21467 = p__21466;
var map__21467__$1 = cljs.core.__destructure_map(map__21467);
var args = map__21467__$1;
var result = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21467__$1,new cljs.core.Keyword(null,"result","result",1415092211));
var error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21467__$1,new cljs.core.Keyword(null,"error","error",-978969032));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(id,new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(choices_state)))){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(choices_state,cljs.core.assoc,new cljs.core.Keyword(null,"loading?","loading?",1905707049),false,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"error","error",-978969032),error,new cljs.core.Keyword(null,"choices","choices",1385611597),result], 0));
} else {
return null;
}
});
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(choices_state,cljs.core.assoc,new cljs.core.Keyword(null,"loading?","loading?",1905707049),true,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"id","id",-1388402092),id,new cljs.core.Keyword(null,"timer","timer",-1266967739),null], 0));

var G__21468 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"filter-text","filter-text",-381699202),text,new cljs.core.Keyword(null,"regex-filter?","regex-filter?",-824895668),regex_filter_QMARK_], null);
var G__21469 = (function (p1__21462_SHARP_){
return callback(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"result","result",1415092211),p1__21462_SHARP_], null));
});
var G__21470 = (function (p1__21463_SHARP_){
return callback(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),p1__21463_SHARP_], null));
});
return (choices.cljs$core$IFn$_invoke$arity$3 ? choices.cljs$core$IFn$_invoke$arity$3(G__21468,G__21469,G__21470) : choices.call(null,G__21468,G__21469,G__21470));
});
/**
 * Load choices or schedule lodaing depending on debounce?
 */
re_com.dropdown.load_choices = (function re_com$dropdown$load_choices(choices_state,choices,debounce_delay,text,regex_filter_QMARK_,debounce_QMARK_){
if(cljs.core.fn_QMARK_(choices)){
var temp__5825__auto___22092 = new cljs.core.Keyword(null,"timer","timer",-1266967739).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(choices_state));
if(cljs.core.truth_(temp__5825__auto___22092)){
var timer_22095 = temp__5825__auto___22092;
clearTimeout(timer_22095);
} else {
}

if(cljs.core.truth_(debounce_QMARK_)){
var timer = setTimeout((function (){
return re_com.dropdown.load_choices_STAR_(choices_state,choices,text,regex_filter_QMARK_);
}),debounce_delay);
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(choices_state,cljs.core.assoc,new cljs.core.Keyword(null,"timer","timer",-1266967739),timer);
} else {
return re_com.dropdown.load_choices_STAR_(choices_state,choices,text,regex_filter_QMARK_);
}
} else {
return null;
}
});
re_com.dropdown.single_dropdown_parts_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),new cljs.core.Keyword(null,"level","level",1290497552),(0),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown-tooltip",new cljs.core.Keyword(null,"impl","impl",1677848700),"[popover-tooltip]",new cljs.core.Keyword(null,"notes","notes",-1039600523),"Tooltip for the dropdown, if enabled."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"legacy","legacy",1434943289),new cljs.core.Keyword(null,"level","level",1290497552),(1),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]",new cljs.core.Keyword(null,"notes","notes",-1039600523),"The container for the rest of the dropdown."], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"chosen-drop","chosen-drop",1349975153),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown-chosen-drop",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"chosen-results","chosen-results",-2092754283),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown-chosen-results",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:ul]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choices-loading","choices-loading",57752856),new cljs.core.Keyword(null,"level","level",1290497552),(4),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown-choices-loading",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:li]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choices-error","choices-error",2121956865),new cljs.core.Keyword(null,"level","level",1290497552),(4),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown-choices-error",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:li]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choices-no-results","choices-no-results",134106368),new cljs.core.Keyword(null,"level","level",1290497552),(4),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown-choices-no-results",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:li]"], null)], null):null);
re_com.dropdown.single_dropdown_parts = ((re_com.config.include_args_desc_QMARK_)?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),re_com.dropdown.single_dropdown_parts_desc)):null);
re_com.dropdown.single_dropdown_args_desc = ((re_com.config.include_args_desc_QMARK_)?cljs.core.PersistentVector.fromArray([new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choices","choices",1385611597),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"vector of choices | r/atom | (opts, done, fail) -> nil",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.dropdown.fn_or_vector_of_maps_or_strings_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Each is expected to have an id, label and, optionally, a group, provided by ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":id-fn"], null),", ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label-fn"], null)," & ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":group-fn"], null),". May also be a callback ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"(opts, done, fail)"], null)," where opts is map of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":filter-text"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":regex-filter?."], null)], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"model","model",331153215),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"the id of a choice | r/atom",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"the id of the selected choice. If nil, ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":placeholder"], null)," text is shown"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-change","on-change",-732046149),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"id -> nil",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.fn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"called when a new choice is selected. Passed the id of new choice"], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"type","type",1174270348),"choice -> anything",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"given an element of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),", returns its unique identifier (aka id)"], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"type","type",1174270348),"choice -> string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"given an element of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),", returns its displayable label."], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"group-fn","group-fn",129203707),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),new cljs.core.Keyword(null,"group","group",582596132),new cljs.core.Keyword(null,"type","type",1174270348),"choice -> anything",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"given an element of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),", returns its group identifier"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"render-fn","render-fn",398796518),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"choice -> string | hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"given an element of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),", returns the markup that will be rendered for that choice. Defaults to the label if no custom markup is required."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean | r/atom",new cljs.core.Keyword(null,"description","description",-1428560544),"if true, no user selection is allowed"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"filter-box?","filter-box?",-1157583688),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"if true, a filter text field is placed at the top of the dropdown"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"regex-filter?","regex-filter?",-824895668),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean | r/atom",new cljs.core.Keyword(null,"description","description",-1428560544),"if true, the filter text field will support JavaScript regular expressions. If false, just plain text"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"background text when no selection"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"title?","title?",-1510254555),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"if true, allows the title for the selected dropdown to be displayed via a mouse over. Handy when dropdown width is small and text is truncated"], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"width","width",-384071477),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),"100%",new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"the CSS width. e.g.: \"500px\" or \"20em\""], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-height","max-height",-612563804),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),"240px",new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"the maximum height of the dropdown part"], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"tab-index","tab-index",895755393),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),(0),new cljs.core.Keyword(null,"type","type",1174270348),"integer | string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.number_or_string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"component's tabindex. A value of -1 removes from order"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"debounce-delay","debounce-delay",-608187982),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"integer",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"delay to debounce loading requests when using callback ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null)], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string | hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.string_or_hiccup_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"what to show in the tooltip"], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"tooltip-position","tooltip-position",936197013),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),new cljs.core.Keyword(null,"below-center","below-center",-2126885397),new cljs.core.Keyword(null,"type","type",1174270348),"keyword",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.position_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"relative to this anchor. One of ",re_com.validate.position_options_list], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"free-text?","free-text?",1157444543),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"is the text freely editable? If true then ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":chocies"], null)," is a vector of strings, ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":model"], null)," is a string (atom) and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-change"], null)," is called with a string"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"auto-complete?","auto-complete?",979505177),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"auto-complete text while typing using dropdown choices. Has no effect if ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":free-text?"], null)," is not turned on"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"capitalize?","capitalize?",-2078576456),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"capitalize the first letter. Has no effect if ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":free-text?"], null)," is not turned on"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"enter-drop?","enter-drop?",1054029717),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),true,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"should pressing Enter display the dropdown part?"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"cancelable?","cancelable?",-986378679),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),true,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"should pressing Esc or clicking outside the dropdown part cancel selection made with arrow keys?"], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"set-to-filter","set-to-filter",1270184073),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"type","type",1174270348),"set",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.set_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"when ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":filter-box?"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":free-text?"], null)," are turned on and there are no results, current text can be set to filter text ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-enter-press"], null)," and/or ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-no-results-match-click"], null)], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"filter-placeholder","filter-placeholder",-1736876526),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"background text in filter box when no filter"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"can-drop-above?","can-drop-above?",-1140582782),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"should the dropdown part be displayed above if it does not fit below the top part?"], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"est-item-height","est-item-height",-264466439),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),(30),new cljs.core.Keyword(null,"type","type",1174270348),"integer",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"estimated dropdown item height (for ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":can-drop-above?"], null),"). used only *before* the dropdown part is displayed to guess whether it fits below the top part or not which is later verified when the dropdown is displayed"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"just-drop?","just-drop?",-378249297),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"display just the dropdown part"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"repeat-change?","repeat-change?",-961675100),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"repeat ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-change"], null)," events if an already selected item is selected again"], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"i18n","i18n",-563422499),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"internationalization map with optional keys ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":loading"], null),", ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":no-results"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":no-results-match"], null)], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-drop","on-drop",1867868491),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"() -> nil",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.fn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"called when the dropdown part is displayed"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"CSS class names, space separated (applies to the outer container)"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"CSS style map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.css_style_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"CSS styles to add or override (applies to the outer container)"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"HTML attr map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.html_attr_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"HTML attributes, like ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-mouse-move"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"No ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":class"], null)," or ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":style"], null),"allowed (applies to the outer container)"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"parts","parts",849007691),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.parts_QMARK_(re_com.dropdown.single_dropdown_parts),new cljs.core.Keyword(null,"description","description",-1428560544),"See Parts section below."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"src","src",-1651076051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Used in dev builds to assist with debugging. Source code coordinates map containing keys",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":file"], null),"and",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":line"], null),". See 'Debugging'."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"debug-as","debug-as",283322354),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Used in dev builds to assist with debugging, when one component is used implement another component, and we want the implementation component to masquerade as the original component in debug output, such as component stacks. A map optionally containing keys",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":component"], null),"and",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":args"], null),"."], null)], null)], true):null);
/**
 * Render a single dropdown component which emulates the bootstrap-choosen style. Sample choices object:
 *   [{:id "AU" :label "Australia"      :group "Group 1"}
 *    {:id "US" :label "United States"  :group "Group 1"}
 *    {:id "GB" :label "United Kingdom" :group "Group 1"}
 *    {:id "AF" :label "Afghanistan"    :group "Group 2"}]
 */
re_com.dropdown.single_dropdown = (function re_com$dropdown$single_dropdown(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22206 = arguments.length;
var i__5877__auto___22207 = (0);
while(true){
if((i__5877__auto___22207 < len__5876__auto___22206)){
args__5882__auto__.push((arguments[i__5877__auto___22207]));

var G__22208 = (i__5877__auto___22207 + (1));
i__5877__auto___22207 = G__22208;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.dropdown.single_dropdown.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.dropdown.single_dropdown.cljs$core$IFn$_invoke$arity$variadic = (function (p__21568){
var map__21569 = p__21568;
var map__21569__$1 = cljs.core.__destructure_map(map__21569);
var args = map__21569__$1;
var choices = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21569__$1,new cljs.core.Keyword(null,"choices","choices",1385611597));
var model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21569__$1,new cljs.core.Keyword(null,"model","model",331153215));
var regex_filter_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21569__$1,new cljs.core.Keyword(null,"regex-filter?","regex-filter?",-824895668));
var debounce_delay = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21569__$1,new cljs.core.Keyword(null,"debounce-delay","debounce-delay",-608187982),(250));
var just_drop_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21569__$1,new cljs.core.Keyword(null,"just-drop?","just-drop?",-378249297));
var or__5142__auto__ = (((!(goog.DEBUG)))?null:re_com.validate.validate_args(re_com.validate.extract_arg_data(re_com.dropdown.single_dropdown_args_desc),args));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var external_model = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(re_com.util.deref_or_value(model));
var internal_model = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(external_model));
var drop_showing_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.boolean$(just_drop_QMARK_));
var filter_text = reagent.core.atom.cljs$core$IFn$_invoke$arity$1("");
var choices_fn_QMARK_ = cljs.core.fn_QMARK_(choices);
var choices_state = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"loading?","loading?",1905707049),choices_fn_QMARK_,new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"choices","choices",1385611597),cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword(null,"id","id",-1388402092),(0),new cljs.core.Keyword(null,"timer","timer",-1266967739),null], null));
var load_choices = cljs.core.partial.cljs$core$IFn$_invoke$arity$4(re_com.dropdown.load_choices,choices_state,choices,debounce_delay);
var set_filter_text = (function (text,p__21593,debounce_QMARK_){
var map__21594 = p__21593;
var map__21594__$1 = cljs.core.__destructure_map(map__21594);
var args__$1 = map__21594__$1;
var regex_filter_QMARK___$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21594__$1,new cljs.core.Keyword(null,"regex-filter?","regex-filter?",-824895668));
load_choices(text,regex_filter_QMARK___$1,debounce_QMARK_);

return cljs.core.reset_BANG_(filter_text,text);
});
var over_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
var showing_QMARK_ = reagent.core.track((function (){
var and__5140__auto__ = cljs.core.not(cljs.core.deref(drop_showing_QMARK_));
if(and__5140__auto__){
return cljs.core.deref(over_QMARK_);
} else {
return and__5140__auto__;
}
}));
var free_text_focused_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
var free_text_input = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var select_free_text_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
var free_text_sel_range = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var focus_free_text = (function (){
if(cljs.core.truth_(cljs.core.deref(free_text_input))){
return cljs.core.deref(free_text_input).focus();
} else {
return null;
}
});
var node = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var focus_anchor = (function (){
var G__21619 = cljs.core.deref(node);
var G__21619__$1 = (((G__21619 == null))?null:G__21619.getElementsByClassName("chosen-single"));
var G__21619__$2 = (((G__21619__$1 == null))?null:G__21619__$1.item((0)));
if((G__21619__$2 == null)){
return null;
} else {
return G__21619__$2.focus();
}
});
load_choices("",regex_filter_QMARK_,false);

return (function() { 
var re_com$dropdown$single_dropdown_render__delegate = function (p__21624){
var map__21626 = p__21624;
var map__21626__$1 = cljs.core.__destructure_map(map__21626);
var args__$1 = map__21626__$1;
var est_item_height = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21626__$1,new cljs.core.Keyword(null,"est-item-height","est-item-height",-264466439),(30));
var auto_complete_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"auto-complete?","auto-complete?",979505177));
var group_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21626__$1,new cljs.core.Keyword(null,"group-fn","group-fn",129203707),new cljs.core.Keyword(null,"group","group",582596132));
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var i18n = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"i18n","i18n",-563422499));
var tooltip = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058));
var model__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"model","model",331153215));
var free_text_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"free-text?","free-text?",1157444543));
var attr = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"attr","attr",-604132353));
var label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21626__$1,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"label","label",1718410804));
var tab_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"tab-index","tab-index",895755393));
var can_drop_above_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"can-drop-above?","can-drop-above?",-1140582782));
var repeat_change_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"repeat-change?","repeat-change?",-961675100));
var max_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"max-height","max-height",-612563804));
var placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083));
var title_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"title?","title?",-1510254555));
var render_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21626__$1,new cljs.core.Keyword(null,"render-fn","render-fn",398796518),label_fn);
var cancelable_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21626__$1,new cljs.core.Keyword(null,"cancelable?","cancelable?",-986378679),true);
var set_to_filter = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"set-to-filter","set-to-filter",1270184073));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"width","width",-384071477));
var on_drop = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"on-drop","on-drop",1867868491));
var regex_filter_QMARK___$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"regex-filter?","regex-filter?",-824895668));
var choices__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"choices","choices",1385611597));
var id_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21626__$1,new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"id","id",-1388402092));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var filter_placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"filter-placeholder","filter-placeholder",-1736876526));
var debounce_delay__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"debounce-delay","debounce-delay",-608187982));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var enter_drop_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21626__$1,new cljs.core.Keyword(null,"enter-drop?","enter-drop?",1054029717),true);
var tooltip_position = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"tooltip-position","tooltip-position",936197013));
var filter_box_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"filter-box?","filter-box?",-1157583688));
var capitalize_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21626__$1,new cljs.core.Keyword(null,"capitalize?","capitalize?",-2078576456));
var or__5142__auto____$1 = (((!(goog.DEBUG)))?null:re_com.validate.validate_args(re_com.validate.extract_arg_data(re_com.dropdown.single_dropdown_args_desc),args__$1));
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
var choices__$2 = ((choices_fn_QMARK_)?new cljs.core.Keyword(null,"choices","choices",1385611597).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(choices_state)):re_com.util.deref_or_value(choices__$1));
var id_fn__$1 = (cljs.core.truth_(free_text_QMARK_)?cljs.core.identity:id_fn);
var label_fn__$1 = (cljs.core.truth_(free_text_QMARK_)?cljs.core.identity:label_fn);
var render_fn__$1 = (cljs.core.truth_(free_text_QMARK_)?cljs.core.identity:render_fn);
var disabled_QMARK___$1 = re_com.util.deref_or_value(disabled_QMARK_);
var regex_filter_QMARK___$2 = re_com.util.deref_or_value(regex_filter_QMARK___$1);
var latest_ext_model = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(re_com.util.deref_or_value(model__$1));
var _ = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(external_model),cljs.core.deref(latest_ext_model)))?(function (){
cljs.core.reset_BANG_(external_model,cljs.core.deref(latest_ext_model));

return cljs.core.reset_BANG_(internal_model,cljs.core.deref(latest_ext_model));
})()
:null);
var changeable_QMARK_ = (function (){var and__5140__auto__ = on_change;
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.not(disabled_QMARK___$1);
} else {
return and__5140__auto__;
}
})();
var call_on_change = (function (){
if(cljs.core.truth_((function (){var and__5140__auto__ = changeable_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
var or__5142__auto____$2 = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(internal_model),cljs.core.deref(latest_ext_model));
if(or__5142__auto____$2){
return or__5142__auto____$2;
} else {
return repeat_change_QMARK_;
}
} else {
return and__5140__auto__;
}
})())){
cljs.core.reset_BANG_(external_model,cljs.core.deref(internal_model));

var G__21672 = cljs.core.deref(internal_model);
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__21672) : on_change.call(null,G__21672));
} else {
return null;
}
});
var callback = (function (p1__21533_SHARP_){
cljs.core.reset_BANG_(internal_model,(function (){var G__21675 = p1__21533_SHARP_;
if(cljs.core.truth_((function (){var and__5140__auto__ = free_text_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return capitalize_QMARK_;
} else {
return and__5140__auto__;
}
})())){
return re_com.dropdown.capitalize_first_letter(G__21675);
} else {
return G__21675;
}
})());

cljs.core.reset_BANG_(select_free_text_QMARK_,true);

call_on_change();

var current_drop_showing_QMARK__22250 = cljs.core.deref(drop_showing_QMARK_);
if(cljs.core.truth_(current_drop_showing_QMARK__22250)){
focus_free_text();
} else {
}

if(cljs.core.truth_(just_drop_QMARK_)){
} else {
cljs.core.reset_BANG_(drop_showing_QMARK_,cljs.core.not(current_drop_showing_QMARK__22250));
}

if(cljs.core.truth_(current_drop_showing_QMARK__22250)){
focus_anchor();
} else {
}

return set_filter_text("",args__$1,false);
});
var free_text_change = (function (p1__21536_SHARP_){
cljs.core.reset_BANG_(internal_model,p1__21536_SHARP_);

cljs.core.reset_BANG_(select_free_text_QMARK_,false);

return call_on_change();
});
var cancel = (function (){
if(cljs.core.truth_(cljs.core.deref(free_text_focused_QMARK_))){
} else {
focus_free_text();
}

cljs.core.reset_BANG_(drop_showing_QMARK_,false);

set_filter_text("",args__$1,false);

return cljs.core.reset_BANG_(internal_model,cljs.core.deref(external_model));
});
var dropdown_click = (function (){
if(cljs.core.truth_(disabled_QMARK___$1)){
return null;
} else {
if(cljs.core.truth_(cljs.core.deref(drop_showing_QMARK_))){
return cancel();
} else {
cljs.core.reset_BANG_(drop_showing_QMARK_,true);

focus_free_text();

return cljs.core.reset_BANG_(select_free_text_QMARK_,true);
}
}
});
var filtered_choices = ((choices_fn_QMARK_)?choices__$2:(cljs.core.truth_(regex_filter_QMARK___$2)?re_com.dropdown.filter_choices_regex(choices__$2,group_fn,label_fn__$1,cljs.core.deref(filter_text)):re_com.dropdown.filter_choices(choices__$2,group_fn,label_fn__$1,cljs.core.deref(filter_text))));
var visible_count = (function (){
var results_node = (function (){var and__5140__auto__ = cljs.core.deref(node);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.deref(node).getElementsByClassName("chosen-results").item((0));
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_((function (){var and__5140__auto__ = results_node;
if(cljs.core.truth_(and__5140__auto__)){
return results_node.firstChild;
} else {
return and__5140__auto__;
}
})())){
return cljs.core.quot(results_node.clientHeight,results_node.firstChild.offsetHeight);
} else {
return (0);
}
});
var est_drop_height = (function (){
var items_height = (cljs.core.count(filtered_choices) * est_item_height);
var drop_margin = (12);
var filter_height = (32);
var maxh = ((cljs.core.not(max_height))?(240):((clojure.string.ends_with_QMARK_(max_height,"px"))?parseInt(max_height,(10)):(function (){
re_com.validate.log_warning.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["max-height is not in pxs, using 240px for estimation"], 0));

return (240);
})()

));
return cljs.core.min.cljs$core$IFn$_invoke$arity$2(((items_height + drop_margin) + (cljs.core.truth_(filter_box_QMARK_)?filter_height:(0))),maxh);
});
var drop_height = reagent.core.track((function (){
var temp__5823__auto__ = (function (){var and__5140__auto__ = cljs.core.deref(node);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.deref(node).getElementsByClassName("chosen-drop").item((0));
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_(temp__5823__auto__)){
var drop_node = temp__5823__auto__;
return drop_node.getBoundingClientRect().height;
} else {
return est_drop_height();
}
}));
var top_height = (34);
var drop_above_QMARK_ = reagent.core.track((function (){
if(cljs.core.truth_((function (){var and__5140__auto__ = can_drop_above_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.deref(node);
} else {
return and__5140__auto__;
}
})())){
var node_top = cljs.core.deref(node).getBoundingClientRect().top;
var window_height = document.documentElement.clientHeight;
return (((node_top + top_height) + cljs.core.deref(drop_height)) > window_height);
} else {
return null;
}
}));
var press_enter = (function (){
var drop_was_showing_QMARK_ = cljs.core.deref(drop_showing_QMARK_);
if(cljs.core.truth_(disabled_QMARK___$1)){
cancel();
} else {
if(cljs.core.truth_((function (){var and__5140__auto__ = new cljs.core.Keyword(null,"on-enter-press","on-enter-press",1454045387).cljs$core$IFn$_invoke$arity$1(set_to_filter);
if(cljs.core.truth_(and__5140__auto__)){
var and__5140__auto____$1 = cljs.core.seq(cljs.core.deref(filter_text));
if(and__5140__auto____$1){
var and__5140__auto____$2 = cljs.core.empty_QMARK_(filtered_choices);
if(and__5140__auto____$2){
var and__5140__auto____$3 = free_text_QMARK_;
if(cljs.core.truth_(and__5140__auto____$3)){
return cljs.core.deref(drop_showing_QMARK_);
} else {
return and__5140__auto____$3;
}
} else {
return and__5140__auto____$2;
}
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
})())){
callback(cljs.core.deref(filter_text));
} else {
if(cljs.core.truth_((function (){var or__5142__auto____$2 = cljs.core.deref(drop_showing_QMARK_);
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return enter_drop_QMARK_;
}
})())){
callback(cljs.core.deref(internal_model));
} else {
}
}
}

return cljs.core.not(drop_was_showing_QMARK_);
});
var press_escape = (function (){
var drop_was_showing_QMARK_ = cljs.core.deref(drop_showing_QMARK_);
cancel();

if(cljs.core.truth_(drop_was_showing_QMARK_)){
focus_anchor();
} else {
}

return cljs.core.not(drop_was_showing_QMARK_);
});
var press_tab = (function (shift_key_QMARK_){
if(cljs.core.truth_(disabled_QMARK___$1)){
cancel();
} else {
var drop_was_showing_QMARK__22264 = cljs.core.deref(drop_showing_QMARK_);
call_on_change();

cljs.core.reset_BANG_(drop_showing_QMARK_,false);

set_filter_text("",args__$1,false);

if(cljs.core.truth_((function (){var and__5140__auto__ = drop_was_showing_QMARK__22264;
if(cljs.core.truth_(and__5140__auto__)){
return shift_key_QMARK_;
} else {
return and__5140__auto__;
}
})())){
focus_anchor();
} else {
}
}

cljs.core.reset_BANG_(drop_showing_QMARK_,false);

return true;
});
var press_arrow = (function (offset){
if(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.deref(drop_showing_QMARK_);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.seq(filtered_choices);
} else {
return and__5140__auto__;
}
})())){
cljs.core.reset_BANG_(internal_model,re_com.dropdown.move_to_new_choice(filtered_choices,id_fn__$1,cljs.core.deref(internal_model),offset));

if(cljs.core.truth_(cancelable_QMARK_)){
} else {
call_on_change();
}
} else {
}

cljs.core.reset_BANG_(drop_showing_QMARK_,true);

cljs.core.reset_BANG_(select_free_text_QMARK_,true);

return true;
});
var press_up = (function (){
return press_arrow((-1));
});
var press_down = (function (){
return press_arrow((1));
});
var press_page_up = (function (){
return press_arrow((- (visible_count() - (1))));
});
var press_page_down = (function (){
return press_arrow((visible_count() - (1)));
});
var press_home_or_end = (function (offset){
if(((cljs.core.not(cljs.core.deref(free_text_focused_QMARK_))) && (cljs.core.seq(filtered_choices)))){
cljs.core.reset_BANG_(internal_model,re_com.dropdown.move_to_new_choice(filtered_choices,id_fn__$1,cljs.core.deref(internal_model),offset));

cljs.core.reset_BANG_(select_free_text_QMARK_,true);
} else {
}

return true;
});
var press_home = (function (){
return press_home_or_end(new cljs.core.Keyword(null,"start","start",-355208981));
});
var press_end = (function (){
return press_home_or_end(new cljs.core.Keyword(null,"end","end",-268185958));
});
var key_handler = (function (p1__21547_SHARP_){
if(cljs.core.truth_(disabled_QMARK___$1)){
return false;
} else {
var G__21781 = p1__21547_SHARP_.key;
switch (G__21781) {
case "Enter":
return press_enter();

break;
case "Escape":
return press_escape();

break;
case "Tab":
return press_tab(p1__21547_SHARP_.shiftKey);

break;
case "ArrowUp":
return press_up();

break;
case "ArrowDown":
return press_down();

break;
case "PageUp":
return press_page_up();

break;
case "PageDown":
return press_page_down();

break;
case "Home":
return press_home();

break;
case "End":
return press_end();

break;
default:
var or__5142__auto____$2 = filter_box_QMARK_;
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return free_text_QMARK_;
}

}
}
});
var anchor = new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.dropdown.dropdown_top,internal_model,choices__$2,id_fn__$1,label_fn__$1,tab_index,placeholder,dropdown_click,key_handler,filter_box_QMARK_,drop_showing_QMARK_,title_QMARK_,disabled_QMARK___$1], null);
var dropdown = new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-dropdown chosen-container "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(free_text_QMARK_)?"chosen-container-multi ":"chosen-container-single "))+"noselect "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_((function (){var or__5142__auto____$2 = cljs.core.deref(drop_showing_QMARK_);
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return cljs.core.deref(free_text_focused_QMARK_);
}
})())?"chosen-container-active ":null))+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(cljs.core.deref(drop_showing_QMARK_))?"chosen-with-drop ":null))+cljs.core.str.cljs$core$IFn$_invoke$arity$1(class$)),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([re_com.box.flex_child_style((cljs.core.truth_(width)?"0 0 auto":"auto")),re_com.box.align_style(new cljs.core.Keyword(null,"align-self","align-self",1475936794),new cljs.core.Keyword(null,"start","start",-355208981)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),width], null),style], 0)),new cljs.core.Keyword(null,"ref","ref",1289896967),(function (p1__21552_SHARP_){
return cljs.core.reset_BANG_(node,p1__21552_SHARP_);
})], null),(cljs.core.truth_(tooltip)?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-mouse-over","on-mouse-over",-858472552),(function (event){
cljs.core.reset_BANG_(over_QMARK_,true);

return null;
}),new cljs.core.Keyword(null,"on-mouse-out","on-mouse-out",643448647),(function (event){
cljs.core.reset_BANG_(over_QMARK_,false);

return null;
})], null):null),re_com.debug.__GT_attr(args__$1),attr], 0)),anchor,(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.deref(drop_showing_QMARK_);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.not(disabled_QMARK___$1);
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"chosen-drop rc-dropdown-chosen-drop "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"chosen-drop","chosen-drop",1349975153),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_(cljs.core.deref(drop_above_QMARK_))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"transform","transform",1381301764),goog.string.format("translate3d(0px, -%ipx, 0px)",((top_height + cljs.core.deref(drop_height)) + (-2)))], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"chosen-drop","chosen-drop",1349975153),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0))], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"chosen-drop","chosen-drop",1349975153),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),(cljs.core.truth_((function (){var and__5140__auto__ = (function (){var or__5142__auto____$2 = filter_box_QMARK_;
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return cljs.core.not(free_text_QMARK_);
}
})();
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.not(just_drop_QMARK_);
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.dropdown.filter_text_box,filter_box_QMARK_,filter_text,key_handler,drop_showing_QMARK_,(function (p1__21553_SHARP_){
return set_filter_text(p1__21553_SHARP_,args__$1,true);
}),filter_placeholder], null):null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul","ul",-1349521403),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"chosen-results rc-dropdown-chosen-results "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"chosen-results","chosen-results",-2092754283),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_(max_height)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"max-height","max-height",-612563804),max_height], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"chosen-results","chosen-results",-2092754283),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0))], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"chosen-results","chosen-results",-2092754283),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),(cljs.core.truth_((function (){var and__5140__auto__ = choices_fn_QMARK_;
if(and__5140__auto__){
return new cljs.core.Keyword(null,"loading?","loading?",1905707049).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(choices_state));
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"loading rc-dropdown-choices-loading "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-loading","choices-loading",57752856),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-loading","choices-loading",57752856),new cljs.core.Keyword(null,"style","style",-496642736)], null),cljs.core.PersistentArrayMap.EMPTY)], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-loading","choices-loading",57752856),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),cljs.core.get.cljs$core$IFn$_invoke$arity$3(i18n,new cljs.core.Keyword(null,"loading","loading",-737050189),"Loading...")], null):(cljs.core.truth_((function (){var and__5140__auto__ = choices_fn_QMARK_;
if(and__5140__auto__){
return new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(choices_state));
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"error rc-dropdown-choices-error "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-error","choices-error",2121956865),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-error","choices-error",2121956865),new cljs.core.Keyword(null,"style","style",-496642736)], null),cljs.core.PersistentArrayMap.EMPTY)], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-error","choices-error",2121956865),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(choices_state))], null):(((cljs.core.count(filtered_choices) > (0)))?(function (){var vec__21825 = re_com.dropdown.choices_with_group_headings(filtered_choices,group_fn);
var group_names = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21825,(0),null);
var group_opt_lists = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21825,(1),null);
var make_a_choice = cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(re_com.dropdown.make_choice_item,id_fn__$1,render_fn__$1,callback,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([internal_model], 0));
var make_choices = (function (p1__21555_SHARP_){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(make_a_choice,p1__21555_SHARP_);
});
var make_h_then_choices = (function (h,opts){
return cljs.core.cons(re_com.dropdown.make_group_heading(h),make_choices(opts));
});
var has_no_group_names_QMARK_ = (new cljs.core.Keyword(null,"group","group",582596132).cljs$core$IFn$_invoke$arity$1(cljs.core.first(group_names)) == null);
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),cljs.core.count(group_opt_lists))) && (has_no_group_names_QMARK_))){
return make_choices(cljs.core.first(group_opt_lists));
} else {
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.concat,cljs.core.map.cljs$core$IFn$_invoke$arity$3(make_h_then_choices,group_names,group_opt_lists));
}
})():new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"no-results rc-dropdown-choices-no-results "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-no-results","choices-no-results",134106368),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-no-results","choices-no-results",134106368),new cljs.core.Keyword(null,"style","style",-496642736)], null),cljs.core.PersistentArrayMap.EMPTY),new cljs.core.Keyword(null,"on-mouse-down","on-mouse-down",1147755470),(function (event){
if(cljs.core.truth_((function (){var and__5140__auto__ = new cljs.core.Keyword(null,"on-no-results-match-click","on-no-results-match-click",1927765286).cljs$core$IFn$_invoke$arity$1(set_to_filter);
if(cljs.core.truth_(and__5140__auto__)){
var and__5140__auto____$1 = cljs.core.seq(cljs.core.deref(filter_text));
if(and__5140__auto____$1){
return free_text_QMARK_;
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
})())){
callback(cljs.core.deref(filter_text));
} else {
}

return null;
})], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choices-no-results","choices-no-results",134106368),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),goog.string.format((function (){var or__5142__auto____$2 = (function (){var and__5140__auto__ = cljs.core.seq(cljs.core.deref(filter_text));
if(and__5140__auto__){
return new cljs.core.Keyword(null,"no-results-match","no-results-match",-1830285472).cljs$core$IFn$_invoke$arity$1(i18n);
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
var or__5142__auto____$3 = (function (){var and__5140__auto__ = cljs.core.empty_QMARK_(cljs.core.deref(filter_text));
if(and__5140__auto__){
return new cljs.core.Keyword(null,"no-results","no-results",1269438172).cljs$core$IFn$_invoke$arity$1(i18n);
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_(or__5142__auto____$3)){
return or__5142__auto____$3;
} else {
var or__5142__auto____$4 = new cljs.core.Keyword(null,"no-results-match","no-results-match",-1830285472).cljs$core$IFn$_invoke$arity$1(i18n);
if(cljs.core.truth_(or__5142__auto____$4)){
return or__5142__auto____$4;
} else {
return "No results match \"%s\"";
}
}
}
})(),cljs.core.deref(filter_text))], null)
)))], null)], null):null)], null);
var ___$1 = (cljs.core.truth_(tooltip)?cljs.core.add_watch(drop_showing_QMARK_,new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),(function (){
return cljs.core.reset_BANG_(over_QMARK_,false);
})):null);
var ___$2 = (cljs.core.truth_(on_drop)?cljs.core.add_watch(drop_showing_QMARK_,new cljs.core.Keyword(null,"on-drop","on-drop",1867868491),(function (p1__21558_SHARP_,p2__21559_SHARP_,p3__21556_SHARP_,p4__21557_SHARP_){
if(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.not(p3__21556_SHARP_);
if(and__5140__auto__){
return p4__21557_SHARP_;
} else {
return and__5140__auto__;
}
})())){
return (on_drop.cljs$core$IFn$_invoke$arity$0 ? on_drop.cljs$core$IFn$_invoke$arity$0() : on_drop.call(null));
} else {
return null;
}
})):null);
return dropdown;
}
};
var re_com$dropdown$single_dropdown_render = function (var_args){
var p__21624 = null;
if (arguments.length > 0) {
var G__22316__i = 0, G__22316__a = new Array(arguments.length -  0);
while (G__22316__i < G__22316__a.length) {G__22316__a[G__22316__i] = arguments[G__22316__i + 0]; ++G__22316__i;}
  p__21624 = new cljs.core.IndexedSeq(G__22316__a,0,null);
} 
return re_com$dropdown$single_dropdown_render__delegate.call(this,p__21624);};
re_com$dropdown$single_dropdown_render.cljs$lang$maxFixedArity = 0;
re_com$dropdown$single_dropdown_render.cljs$lang$applyTo = (function (arglist__22317){
var p__21624 = cljs.core.seq(arglist__22317);
return re_com$dropdown$single_dropdown_render__delegate(p__21624);
});
re_com$dropdown$single_dropdown_render.cljs$core$IFn$_invoke$arity$variadic = re_com$dropdown$single_dropdown_render__delegate;
return re_com$dropdown$single_dropdown_render;
})()
;
}
}));

(re_com.dropdown.single_dropdown.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.dropdown.single_dropdown.cljs$lang$applyTo = (function (seq21560){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21560));
}));


//# sourceMappingURL=re_com.dropdown.js.map
