goog.provide('day8.re_frame.debux.common.util');
day8.re_frame.debux.common.util.map__GT_seq = (function day8$re_frame$debux$common$util$map__GT_seq(m){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (r,p__46154){
var vec__46155 = p__46154;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46155,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46155,(1),null);
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(r,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [k,v], null));
}),cljs.core.PersistentVector.EMPTY,m);
});
day8.re_frame.debux.common.util.sequential_zip = (function day8$re_frame$debux$common$util$sequential_zip(root){
return clojure.zip.zipper((function (p1__46165_SHARP_){
return ((cljs.core.sequential_QMARK_(p1__46165_SHARP_)) || (cljs.core.map_QMARK_(p1__46165_SHARP_)));
}),(function (x){
if(cljs.core.map_QMARK_(x)){
return cljs.core.with_meta(day8.re_frame.debux.common.util.map__GT_seq(x),cljs.core.meta(x));
} else {
return x;

}
}),(function (x,children){
if(cljs.core.vector_QMARK_(x)){
return cljs.core.with_meta(cljs.core.vec(children),cljs.core.meta(x));
} else {
if(cljs.core.map_QMARK_(x)){
return cljs.core.with_meta(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (r,p__46167){
var vec__46168 = p__46167;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46168,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46168,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(r,k,v);
}),cljs.core.PersistentArrayMap.EMPTY,cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),children)),cljs.core.meta(x));
} else {
return children;

}
}
}),root);
});
day8.re_frame.debux.common.util.right_or_next = (function day8$re_frame$debux$common$util$right_or_next(loc){
while(true){
var temp__5823__auto__ = clojure.zip.right(loc);
if(cljs.core.truth_(temp__5823__auto__)){
var right = temp__5823__auto__;
return right;
} else {
if(cljs.core.sequential_QMARK_(clojure.zip.node(loc))){
var rightmost = clojure.zip.rightmost(clojure.zip.down(loc));
if(cljs.core.sequential_QMARK_(clojure.zip.node(rightmost))){
var G__46838 = rightmost;
loc = G__46838;
continue;
} else {
return clojure.zip.next(rightmost);
}
} else {
return clojure.zip.next(loc);
}
}
break;
}
});
day8.re_frame.debux.common.util.auto_gensym_pattern = /(.*)__\d+__auto__/;
day8.re_frame.debux.common.util.anon_gensym_pattern = /G__\d+/;
day8.re_frame.debux.common.util.named_gensym_pattern = /(.*?)\d+/;
day8.re_frame.debux.common.util.anon_param_pattern = /p(\d+)__\d+#/;
day8.re_frame.debux.common.util.form_tree_seq = (function day8$re_frame$debux$common$util$form_tree_seq(form){
return cljs.core.tree_seq(cljs.core.sequential_QMARK_,cljs.core.seq,form);
});
/**
 * Reverse gensym'd names to their original source form to make them easier to read.
 */
day8.re_frame.debux.common.util.with_gensyms_names = (function day8$re_frame$debux$common$util$with_gensyms_names(form,mapping){
var gen_name = (function (result,name){
if((!(cljs.core.contains_QMARK_(result,(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(name)+"#"))))){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(name)+"#");
} else {
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46218_SHARP_){
return (!(cljs.core.contains_QMARK_(result,p1__46218_SHARP_)));
}),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46216_SHARP_){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(name)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__46216_SHARP_)+"#");
}),cljs.core.iterate(cljs.core.inc,(2)))));
}
});
var name_for = (function (result,sym_name){
var temp__5823__auto__ = cljs.core.re_matches(day8.re_frame.debux.common.util.auto_gensym_pattern,sym_name);
if(cljs.core.truth_(temp__5823__auto__)){
var groups = temp__5823__auto__;
return gen_name(result,cljs.core.second(groups));
} else {
if(cljs.core.truth_(cljs.core.re_matches(day8.re_frame.debux.common.util.anon_gensym_pattern,sym_name))){
return gen_name(result,"gensym");
} else {
var temp__5823__auto____$1 = cljs.core.re_matches(day8.re_frame.debux.common.util.named_gensym_pattern,sym_name);
if(cljs.core.truth_(temp__5823__auto____$1)){
var groups = temp__5823__auto____$1;
return gen_name(result,cljs.core.second(groups));
} else {
var temp__5823__auto____$2 = cljs.core.re_matches(day8.re_frame.debux.common.util.anon_param_pattern,sym_name);
if(cljs.core.truth_(temp__5823__auto____$2)){
var groups = temp__5823__auto____$2;
return (""+"%"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.second(groups)));
} else {
return null;
}
}
}
}
});
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (result,sym_name){
if(cljs.core.contains_QMARK_(result,sym_name)){
return result;
} else {
var temp__5823__auto__ = name_for(result,sym_name);
if(cljs.core.truth_(temp__5823__auto__)){
var new_name = temp__5823__auto__;
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(result,sym_name,new_name);
} else {
return result;
}
}
}),mapping,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.name,cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46223_SHARP_){
return (((p1__46223_SHARP_ instanceof cljs.core.Symbol)) && ((cljs.core.namespace(p1__46223_SHARP_) == null)));
}),day8.re_frame.debux.common.util.form_tree_seq(form))));
});
/**
 * Tidy up fully qualified names that have aliases in the existing namespace.
 */
day8.re_frame.debux.common.util.with_symbols_names = (function day8$re_frame$debux$common$util$with_symbols_names(form,p__46243,mapping){
var map__46244 = p__46243;
var map__46244__$1 = cljs.core.__destructure_map(map__46244);
var state = map__46244__$1;
var context = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46244__$1,new cljs.core.Keyword(null,"context","context",-830191113));
var refers = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46244__$1,new cljs.core.Keyword(null,"refers","refers",158076809));
var aliases = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46244__$1,new cljs.core.Keyword(null,"aliases","aliases",1346874714));
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (result,sym){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("clojure.core",cljs.core.namespace(sym))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(result,cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([sym], 0)),cljs.core.name(sym));
} else {
return result;
}
}),mapping,cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46242_SHARP_){
var and__5140__auto__ = (p1__46242_SHARP_ instanceof cljs.core.Symbol);
if(and__5140__auto__){
return cljs.core.namespace(p1__46242_SHARP_);
} else {
return and__5140__auto__;
}
}),day8.re_frame.debux.common.util.form_tree_seq(form)));
});
/**
 * Takes a macroexpanded form and tidies it up to be more readable by
 *   unmapping gensyms and replacing fully qualified namespaces with aliases
 *   or nothing if the function is referred.
 */
day8.re_frame.debux.common.util.tidy_macroexpanded_form = (function day8$re_frame$debux$common$util$tidy_macroexpanded_form(form,state){
var mapping = day8.re_frame.debux.common.util.with_symbols_names(form,state,day8.re_frame.debux.common.util.with_gensyms_names(form,cljs.core.PersistentArrayMap.EMPTY));
var loc = day8.re_frame.debux.common.util.sequential_zip(form);
while(true){
if(clojure.zip.end_QMARK_(loc)){
return clojure.zip.root(loc);
} else {
if((clojure.zip.node(loc) instanceof cljs.core.Symbol)){
var G__46848 = clojure.zip.next(clojure.zip.edit(loc,((function (loc,mapping){
return (function (sym){
return cljs.core.symbol.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$3(mapping,cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([sym], 0)),sym));
});})(loc,mapping))
));
loc = G__46848;
continue;
} else {
var G__46849 = clojure.zip.next(loc);
loc = G__46849;
continue;
}
}
break;
}
});
day8.re_frame.debux.common.util.production_mode_warning_enabled_QMARK_ = true;
day8.re_frame.debux.common.util.reset_production_mode_warning_BANG_ = (function day8$re_frame$debux$common$util$reset_production_mode_warning_BANG_(){
return (day8.re_frame.debux.common.util.production_mode_warning_enabled_QMARK_ = true);
});
day8.re_frame.debux.common.util.maybe_warn_production_mode_BANG_ = (function day8$re_frame$debux$common$util$maybe_warn_production_mode_BANG_(){
if(day8.re_frame.debux.common.util.production_mode_warning_enabled_QMARK_){
(day8.re_frame.debux.common.util.production_mode_warning_enabled_QMARK_ = false);

try{if(goog.DEBUG === false){
return console.warn((""+"re-frame-debux: send-trace! is firing in a build with "+"goog.DEBUG=false. The day8.re-frame.tracing namespace "+"is loaded and active in what looks like a production "+"build (advanced compilation usually sets goog.DEBUG to "+"false). This bloats your bundle and emits trace noise "+"into 10x. Check your build config: shadow-cljs users "+"should set :ns-aliases to redirect day8.re-frame.tracing "+"\u2192 day8.re-frame.tracing-stubs in release builds; "+"lein/cljsbuild users should put day8.re-frame/tracing-stubs "+"in the production profile instead of day8.re-frame/tracing. "+"See https://github.com/day8/re-frame-debux#two-libraries "+"for details. (This warning fires once per session.)"));
} else {
return null;
}
}catch (e46293){var _ = e46293;
return null;
}} else {
return null;
}
});
if((typeof day8 !== 'undefined') && (typeof day8.re_frame !== 'undefined') && (typeof day8.re_frame.debux !== 'undefined') && (typeof day8.re_frame.debux.common !== 'undefined') && (typeof day8.re_frame.debux.common.util !== 'undefined') && (typeof day8.re_frame.debux.common.util.tap_output_QMARK_ !== 'undefined')){
} else {
day8.re_frame.debux.common.util.tap_output_QMARK_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
}
if((typeof day8 !== 'undefined') && (typeof day8.re_frame !== 'undefined') && (typeof day8.re_frame.debux !== 'undefined') && (typeof day8.re_frame.debux.common !== 'undefined') && (typeof day8.re_frame.debux.common.util !== 'undefined') && (typeof day8.re_frame.debux.common.util.trace_frames_output_QMARK_ !== 'undefined')){
} else {
day8.re_frame.debux.common.util.trace_frames_output_QMARK_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
}
if((typeof day8 !== 'undefined') && (typeof day8.re_frame !== 'undefined') && (typeof day8.re_frame.debux !== 'undefined') && (typeof day8.re_frame.debux.common !== 'undefined') && (typeof day8.re_frame.debux.common.util !== 'undefined') && (typeof day8.re_frame.debux.common.util.date_time_fn_STAR_ !== 'undefined')){
} else {
day8.re_frame.debux.common.util.date_time_fn_STAR_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
day8.re_frame.debux.common.util.now_ms = (function day8$re_frame$debux$common$util$now_ms(){
return Date.now();
});
day8.re_frame.debux.common.util.stamp_tap_payload = (function day8$re_frame$debux$common$util$stamp_tap_payload(var_args){
var G__46324 = arguments.length;
switch (G__46324) {
case 1:
return day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$1 = (function (payload){
return day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$2(payload,day8.re_frame.debux.common.util.now_ms());
}));

(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$2 = (function (payload,t){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(payload,new cljs.core.Keyword(null,"t","t",-1397832519),t,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"date-time","date-time",177938180),(function (){var temp__5823__auto__ = cljs.core.deref(day8.re_frame.debux.common.util.date_time_fn_STAR_);
if(cljs.core.truth_(temp__5823__auto__)){
var date_time_fn = temp__5823__auto__;
return (date_time_fn.cljs$core$IFn$_invoke$arity$0 ? date_time_fn.cljs$core$IFn$_invoke$arity$0() : date_time_fn.call(null));
} else {
return t;
}
})()], 0));
}));

(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$lang$maxFixedArity = 2);

/**
 * When `enabled?` is truthy, every debux trace emitter (`send-form!`,
 * `send-trace!`, `-send-frame-enter!`, `-send-frame-exit!`,
 * `-emit-fx-traces!`) also calls `tap>` so any `add-tap` consumer
 * sees the trace alongside `trace/merge-trace!`. Each payload carries
 * a `:debux/kind` discriminator (`:form`, `:code`, `:frame-enter`,
 * `:frame-exit`, `:fx-effect`). False by default — preserves the
 * existing trace-only behaviour. Independent of whether re-frame's
 * trace machinery is enabled.
 */
day8.re_frame.debux.common.util.set_tap_output_BANG_ = (function day8$re_frame$debux$common$util$set_tap_output_BANG_(enabled_QMARK_){
return cljs.core.reset_BANG_(day8.re_frame.debux.common.util.tap_output_QMARK_,cljs.core.boolean$(enabled_QMARK_));
});
/**
 * Set the zero-arg function used to stamp tap> payloads with a
 * caller-controlled `:date-time` value. Pass nil to restore the
 * default, where `:date-time` is the same millisecond value as `:t`.
 * The numeric `:t` field is always retained for existing consumers.
 */
day8.re_frame.debux.common.util.set_date_time_fn_BANG_ = (function day8$re_frame$debux$common$util$set_date_time_fn_BANG_(f){
return cljs.core.reset_BANG_(day8.re_frame.debux.common.util.date_time_fn_STAR_,f);
});
/**
 * When `enabled?` is truthy, `fn-traced` / `defn-traced` /
 * `fx-traced` invocations emit paired `:enter` / `:exit` markers onto
 * the active trace's :tags :trace-frames vector. False by default so
 * normal tracing does not pay frame marker timestamp / merge overhead.
 * 
 * Independent of `set-tap-output!`: tap output still emits frame
 * payloads when tap output is on.
 */
day8.re_frame.debux.common.util.set_trace_frames_output_BANG_ = (function day8$re_frame$debux$common$util$set_trace_frames_output_BANG_(enabled_QMARK_){
return cljs.core.reset_BANG_(day8.re_frame.debux.common.util.trace_frames_output_QMARK_,cljs.core.boolean$(enabled_QMARK_));
});
/**
 * Internal predicate used by macro expansions to avoid calling frame
 * marker emitters unless a trace-frame or tap consumer is enabled.
 */
day8.re_frame.debux.common.util.frame_markers_enabled_QMARK_ = (function day8$re_frame$debux$common$util$frame_markers_enabled_QMARK_(){
var or__5142__auto__ = cljs.core.deref(day8.re_frame.debux.common.util.trace_frames_output_QMARK_);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_);
}
});
day8.re_frame.debux.common.util.send_form_BANG_ = (function day8$re_frame$debux$common$util$send_form_BANG_(form){
if(cljs.core.truth_((function (){var or__5142__auto__ = cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.deref(new cljs.core.Var(function(){return re_frame.trace.trace_enabled_QMARK_;},new cljs.core.Symbol("re-frame.trace","trace-enabled?","re-frame.trace/trace-enabled?",-673774556,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"jsdoc","jsdoc",1745183516),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"re-frame.trace","re-frame.trace",-1584328505,null),new cljs.core.Symbol(null,"trace-enabled?","trace-enabled?",-1877362722,null),"re_frame/trace.cljc",37,10,196,196,new cljs.core.Symbol(null,"boolean","boolean",-278886877,null),cljs.core.List.EMPTY,null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@define {boolean}\n@type {boolean}"], null),((re_frame.trace.trace_enabled_QMARK_)?re_frame.trace.trace_enabled_QMARK_.cljs$lang$test:null)])));
}
})())){
day8.re_frame.debux.common.util.maybe_warn_production_mode_BANG_();

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Var(function(){return re_frame.trace.trace_enabled_QMARK_;},new cljs.core.Symbol("re-frame.trace","trace-enabled?","re-frame.trace/trace-enabled?",-673774556,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"jsdoc","jsdoc",1745183516),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"re-frame.trace","re-frame.trace",-1584328505,null),new cljs.core.Symbol(null,"trace-enabled?","trace-enabled?",-1877362722,null),"re_frame/trace.cljc",37,10,196,196,new cljs.core.Symbol(null,"boolean","boolean",-278886877,null),cljs.core.List.EMPTY,null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@define {boolean}\n@type {boolean}"], null),((re_frame.trace.trace_enabled_QMARK_)?re_frame.trace.trace_enabled_QMARK_.cljs$lang$test:null)]))))){
if(re_frame.trace.is_trace_enabled_QMARK_()){
var new_trace__21478__auto___46884 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$4(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"tags","tags",1771418977),cljs.core.merge,new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"form","form",-1624062471),form], null)], null))),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"form","form",-1624062471),form], null)], null),new cljs.core.Keyword(null,"tags","tags",1771418977))], 0));
(re_frame.trace._STAR_current_trace_STAR_ = new_trace__21478__auto___46884);

} else {
}
} else {
}

if(cljs.core.truth_(cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_))){
return cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("debux","kind","debux/kind",-1416724783),new cljs.core.Keyword(null,"form","form",-1624062471),new cljs.core.Keyword(null,"form","form",-1624062471),form], null)));
} else {
return null;
}
} else {
return null;
}
});
day8.re_frame.debux.common.util.send_trace_BANG_ = (function day8$re_frame$debux$common$util$send_trace_BANG_(code_trace){
if(cljs.core.truth_((function (){var or__5142__auto__ = cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.deref(new cljs.core.Var(function(){return re_frame.trace.trace_enabled_QMARK_;},new cljs.core.Symbol("re-frame.trace","trace-enabled?","re-frame.trace/trace-enabled?",-673774556,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"jsdoc","jsdoc",1745183516),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"re-frame.trace","re-frame.trace",-1584328505,null),new cljs.core.Symbol(null,"trace-enabled?","trace-enabled?",-1877362722,null),"re_frame/trace.cljc",37,10,196,196,new cljs.core.Symbol(null,"boolean","boolean",-278886877,null),cljs.core.List.EMPTY,null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@define {boolean}\n@type {boolean}"], null),((re_frame.trace.trace_enabled_QMARK_)?re_frame.trace.trace_enabled_QMARK_.cljs$lang$test:null)])));
}
})())){
day8.re_frame.debux.common.util.maybe_warn_production_mode_BANG_();

var entry = (function (){var G__46349 = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"form","form",-1624062471),(cljs.core.truth_(new cljs.core.Keyword("day8.re-frame.debux.common.util","form-tidied?","day8.re-frame.debux.common.util/form-tidied?",-1927384671).cljs$core$IFn$_invoke$arity$1(cljs.core.meta(code_trace)))?new cljs.core.Keyword(null,"form","form",-1624062471).cljs$core$IFn$_invoke$arity$1(code_trace):day8.re_frame.debux.common.util.tidy_macroexpanded_form(new cljs.core.Keyword(null,"form","form",-1624062471).cljs$core$IFn$_invoke$arity$1(code_trace),cljs.core.PersistentArrayMap.EMPTY)),new cljs.core.Keyword(null,"result","result",1415092211),new cljs.core.Keyword(null,"result","result",1415092211).cljs$core$IFn$_invoke$arity$1(code_trace),new cljs.core.Keyword(null,"indent-level","indent-level",-258835684),new cljs.core.Keyword(null,"indent-level","indent-level",-258835684).cljs$core$IFn$_invoke$arity$1(code_trace),new cljs.core.Keyword(null,"syntax-order","syntax-order",-990682045),new cljs.core.Keyword(null,"syntax-order","syntax-order",-990682045).cljs$core$IFn$_invoke$arity$1(code_trace),new cljs.core.Keyword(null,"num-seen","num-seen",-1576518431),new cljs.core.Keyword(null,"num-seen","num-seen",-1576518431).cljs$core$IFn$_invoke$arity$1(code_trace)], null);
var G__46349__$1 = ((cljs.core.contains_QMARK_(code_trace,new cljs.core.Keyword(null,"locals","locals",535295783)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__46349,new cljs.core.Keyword(null,"locals","locals",535295783),new cljs.core.Keyword(null,"locals","locals",535295783).cljs$core$IFn$_invoke$arity$1(code_trace)):G__46349);
var G__46349__$2 = ((cljs.core.contains_QMARK_(code_trace,new cljs.core.Keyword(null,"name","name",1843675177)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__46349__$1,new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(code_trace)):G__46349__$1);
if(cljs.core.contains_QMARK_(code_trace,new cljs.core.Keyword(null,"msg","msg",-1386103444))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__46349__$2,new cljs.core.Keyword(null,"msg","msg",-1386103444),new cljs.core.Keyword(null,"msg","msg",-1386103444).cljs$core$IFn$_invoke$arity$1(code_trace));
} else {
return G__46349__$2;
}
})();
if(re_frame.trace.trace_enabled_QMARK_){
var code_46927 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"code","code",1586293142)], null),cljs.core.PersistentVector.EMPTY);
if(re_frame.trace.is_trace_enabled_QMARK_()){
var new_trace__21478__auto___46929 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$4(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"tags","tags",1771418977),cljs.core.merge,new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"code","code",1586293142),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(code_46927,entry)], null)], null))),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"code","code",1586293142),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(code_46927,entry)], null)], null),new cljs.core.Keyword(null,"tags","tags",1771418977))], 0));
(re_frame.trace._STAR_current_trace_STAR_ = new_trace__21478__auto___46929);

} else {
}
} else {
}

if(cljs.core.truth_(cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_))){
return cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$1(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry,new cljs.core.Keyword("debux","kind","debux/kind",-1416724783),new cljs.core.Keyword(null,"code","code",1586293142))));
} else {
return null;
}
} else {
return null;
}
});
/**
 * If a re-frame trace event is in flight, accumulate `payload` onto
 * the active event's :tags :code via `send-trace!`. Otherwise tap>
 * so REPL callers still see output.
 * 
 * `tap-also?` — when true, ALSO tap> alongside the in-trace emit
 * (for callers that want both signals). Out-of-trace, tap> always
 * fires regardless. Returns nil.
 */
day8.re_frame.debux.common.util.send_trace_or_tap_BANG_ = (function day8$re_frame$debux$common$util$send_trace_or_tap_BANG_(payload,tap_also_QMARK_){
if((!((re_frame.trace._STAR_current_trace_STAR_ == null)))){
day8.re_frame.debux.common.util.send_trace_BANG_(payload);

if(cljs.core.truth_(tap_also_QMARK_)){
cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$1(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(payload,new cljs.core.Keyword("debux","dbg","debux/dbg",241541734),true)));
} else {
}
} else {
cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$1(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(payload,new cljs.core.Keyword("debux","dbg","debux/dbg",241541734),true)));
}

return null;
});
/**
 * Emit a `:enter` marker on the active trace's :trace-frames vector.
 * No-op on the trace channel unless `set-trace-frames-output!` is on,
 * trace-enabled? is on, and a trace is in flight. Independently, when
 * `set-tap-output!` is enabled, also emits a
 * `{:debux/kind :frame-enter ...}` payload to tap>. Internal — called
 * by the fn-traced / defn-traced expansion at body-entry.
 */
day8.re_frame.debux.common.util._send_frame_enter_BANG_ = (function day8$re_frame$debux$common$util$_send_frame_enter_BANG_(var_args){
var G__46361 = arguments.length;
switch (G__46361) {
case 1:
return day8.re_frame.debux.common.util._send_frame_enter_BANG_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return day8.re_frame.debux.common.util._send_frame_enter_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(day8.re_frame.debux.common.util._send_frame_enter_BANG_.cljs$core$IFn$_invoke$arity$1 = (function (frame_id){
return day8.re_frame.debux.common.util._send_frame_enter_BANG_.cljs$core$IFn$_invoke$arity$2(frame_id,null);
}));

(day8.re_frame.debux.common.util._send_frame_enter_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (frame_id,msg){
var trace_QMARK__46952 = (function (){var and__5140__auto__ = cljs.core.deref(day8.re_frame.debux.common.util.trace_frames_output_QMARK_);
if(cljs.core.truth_(and__5140__auto__)){
var and__5140__auto____$1 = (!((re_frame.trace._STAR_current_trace_STAR_ == null)));
if(and__5140__auto____$1){
return cljs.core.deref(new cljs.core.Var(function(){return re_frame.trace.trace_enabled_QMARK_;},new cljs.core.Symbol("re-frame.trace","trace-enabled?","re-frame.trace/trace-enabled?",-673774556,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"jsdoc","jsdoc",1745183516),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"re-frame.trace","re-frame.trace",-1584328505,null),new cljs.core.Symbol(null,"trace-enabled?","trace-enabled?",-1877362722,null),"re_frame/trace.cljc",37,10,196,196,new cljs.core.Symbol(null,"boolean","boolean",-278886877,null),cljs.core.List.EMPTY,null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@define {boolean}\n@type {boolean}"], null),((re_frame.trace.trace_enabled_QMARK_)?re_frame.trace.trace_enabled_QMARK_.cljs$lang$test:null)])));
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
})();
var tap_QMARK__46953 = cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_);
if(cljs.core.truth_((function (){var or__5142__auto__ = trace_QMARK__46952;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return tap_QMARK__46953;
}
})())){
var t_46956 = day8.re_frame.debux.common.util.now_ms();
var entry_46957 = (function (){var G__46362 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"enter","enter",1792452624),new cljs.core.Keyword(null,"frame-id","frame-id",-636372072),frame_id,new cljs.core.Keyword(null,"t","t",-1397832519),t_46956], null);
if(cljs.core.truth_(msg)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__46362,new cljs.core.Keyword(null,"msg","msg",-1386103444),msg);
} else {
return G__46362;
}
})();
if(cljs.core.truth_(trace_QMARK__46952)){
var frames_46963 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"trace-frames","trace-frames",-1089001116)], null),cljs.core.PersistentVector.EMPTY);
if(re_frame.trace.is_trace_enabled_QMARK_()){
var new_trace__21478__auto___46964 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$4(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"tags","tags",1771418977),cljs.core.merge,new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"trace-frames","trace-frames",-1089001116),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(frames_46963,entry_46957)], null)], null))),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"trace-frames","trace-frames",-1089001116),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(frames_46963,entry_46957)], null)], null),new cljs.core.Keyword(null,"tags","tags",1771418977))], 0));
(re_frame.trace._STAR_current_trace_STAR_ = new_trace__21478__auto___46964);

} else {
}
} else {
}

if(cljs.core.truth_(tap_QMARK__46953)){
cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$2((function (){var G__46370 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("debux","kind","debux/kind",-1416724783),new cljs.core.Keyword(null,"frame-enter","frame-enter",11863623),new cljs.core.Keyword(null,"frame-id","frame-id",-636372072),frame_id], null);
if(cljs.core.truth_(msg)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__46370,new cljs.core.Keyword(null,"msg","msg",-1386103444),msg);
} else {
return G__46370;
}
})(),t_46956));
} else {
}
} else {
}

return null;
}));

(day8.re_frame.debux.common.util._send_frame_enter_BANG_.cljs$lang$maxFixedArity = 2);

/**
 * Emit an `:exit` marker. Mirrors `-send-frame-enter!`: trace-channel
 * emission is opt-in via `set-trace-frames-output!` and no-op
 * off-trace, and when `set-tap-output!` is enabled an independent
 * `{:debux/kind :frame-exit ...}` payload also lands on tap>. The
 * `result` arg is accepted for call-site compatibility but is not
 * stored on the marker; consumers that need the return value should
 * read the surrounding :code entry. Internal — called by the
 * fn-traced / defn-traced expansion right before returning.
 */
day8.re_frame.debux.common.util._send_frame_exit_BANG_ = (function day8$re_frame$debux$common$util$_send_frame_exit_BANG_(var_args){
var G__46374 = arguments.length;
switch (G__46374) {
case 2:
return day8.re_frame.debux.common.util._send_frame_exit_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return day8.re_frame.debux.common.util._send_frame_exit_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(day8.re_frame.debux.common.util._send_frame_exit_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (frame_id,result){
return day8.re_frame.debux.common.util._send_frame_exit_BANG_.cljs$core$IFn$_invoke$arity$3(frame_id,result,null);
}));

(day8.re_frame.debux.common.util._send_frame_exit_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (frame_id,_result,msg){
var trace_QMARK__46980 = (function (){var and__5140__auto__ = cljs.core.deref(day8.re_frame.debux.common.util.trace_frames_output_QMARK_);
if(cljs.core.truth_(and__5140__auto__)){
var and__5140__auto____$1 = (!((re_frame.trace._STAR_current_trace_STAR_ == null)));
if(and__5140__auto____$1){
return cljs.core.deref(new cljs.core.Var(function(){return re_frame.trace.trace_enabled_QMARK_;},new cljs.core.Symbol("re-frame.trace","trace-enabled?","re-frame.trace/trace-enabled?",-673774556,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"jsdoc","jsdoc",1745183516),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"re-frame.trace","re-frame.trace",-1584328505,null),new cljs.core.Symbol(null,"trace-enabled?","trace-enabled?",-1877362722,null),"re_frame/trace.cljc",37,10,196,196,new cljs.core.Symbol(null,"boolean","boolean",-278886877,null),cljs.core.List.EMPTY,null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@define {boolean}\n@type {boolean}"], null),((re_frame.trace.trace_enabled_QMARK_)?re_frame.trace.trace_enabled_QMARK_.cljs$lang$test:null)])));
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
})();
var tap_QMARK__46981 = cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_);
if(cljs.core.truth_((function (){var or__5142__auto__ = trace_QMARK__46980;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return tap_QMARK__46981;
}
})())){
var t_46992 = day8.re_frame.debux.common.util.now_ms();
var entry_46993 = (function (){var G__46376 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"exit","exit",351849638),new cljs.core.Keyword(null,"frame-id","frame-id",-636372072),frame_id,new cljs.core.Keyword(null,"t","t",-1397832519),t_46992], null);
if(cljs.core.truth_(msg)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__46376,new cljs.core.Keyword(null,"msg","msg",-1386103444),msg);
} else {
return G__46376;
}
})();
if(cljs.core.truth_(trace_QMARK__46980)){
var frames_47001 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"trace-frames","trace-frames",-1089001116)], null),cljs.core.PersistentVector.EMPTY);
if(re_frame.trace.is_trace_enabled_QMARK_()){
var new_trace__21478__auto___47003 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$4(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"tags","tags",1771418977),cljs.core.merge,new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"trace-frames","trace-frames",-1089001116),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(frames_47001,entry_46993)], null)], null))),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"trace-frames","trace-frames",-1089001116),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(frames_47001,entry_46993)], null)], null),new cljs.core.Keyword(null,"tags","tags",1771418977))], 0));
(re_frame.trace._STAR_current_trace_STAR_ = new_trace__21478__auto___47003);

} else {
}
} else {
}

if(cljs.core.truth_(tap_QMARK__46981)){
cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$2((function (){var G__46380 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("debux","kind","debux/kind",-1416724783),new cljs.core.Keyword(null,"frame-exit","frame-exit",-1626409942),new cljs.core.Keyword(null,"frame-id","frame-id",-636372072),frame_id], null);
if(cljs.core.truth_(msg)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__46380,new cljs.core.Keyword(null,"msg","msg",-1386103444),msg);
} else {
return G__46380;
}
})(),t_46992));
} else {
}
} else {
}

return null;
}));

(day8.re_frame.debux.common.util._send_frame_exit_BANG_.cljs$lang$maxFixedArity = 3);

/**
 * When a `fx-traced` body returns an effect-map (the standard reg-event-fx
 * contract: `{:db ... :http {...} :dispatch [...]}`), emit one entry
 * per key onto the active trace's :tags :fx-effects vector. Each entry
 * is `{:fx-key <k> :value <v> :t <ms>}`. No-op on the trace channel
 * when trace-enabled? is off or no trace is in flight; no-op entirely
 * when the return isn't a map (a malformed handler — the misuse is
 * reported via re-frame's normal error path, not here).
 * 
 * When `set-tap-output!` is enabled, ALSO emits one
 * `{:debux/kind :fx-effect ...}` payload per effect-key to tap>,
 * independent of trace state. All keys of one return share a single
 * timestamp so consumers can group them.
 * 
 * :fx-effects is a separate :tags key from :code so consumers reading
 * the :code panel don't see fx entries inflating the form-by-form
 * trace.
 */
day8.re_frame.debux.common.util._emit_fx_traces_BANG_ = (function day8$re_frame$debux$common$util$_emit_fx_traces_BANG_(effect_map){
if(cljs.core.map_QMARK_(effect_map)){
var trace_QMARK__47007 = (function (){var and__5140__auto__ = (!((re_frame.trace._STAR_current_trace_STAR_ == null)));
if(and__5140__auto__){
return cljs.core.deref(new cljs.core.Var(function(){return re_frame.trace.trace_enabled_QMARK_;},new cljs.core.Symbol("re-frame.trace","trace-enabled?","re-frame.trace/trace-enabled?",-673774556,null),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ns","ns",441598760),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"file","file",-1269645878),new cljs.core.Keyword(null,"end-column","end-column",1425389514),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"line","line",212345235),new cljs.core.Keyword(null,"end-line","end-line",1837326455),new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"arglists","arglists",1661989754),new cljs.core.Keyword(null,"doc","doc",1913296891),new cljs.core.Keyword(null,"jsdoc","jsdoc",1745183516),new cljs.core.Keyword(null,"test","test",577538877)],[new cljs.core.Symbol(null,"re-frame.trace","re-frame.trace",-1584328505,null),new cljs.core.Symbol(null,"trace-enabled?","trace-enabled?",-1877362722,null),"re_frame/trace.cljc",37,10,196,196,new cljs.core.Symbol(null,"boolean","boolean",-278886877,null),cljs.core.List.EMPTY,null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@define {boolean}\n@type {boolean}"], null),((re_frame.trace.trace_enabled_QMARK_)?re_frame.trace.trace_enabled_QMARK_.cljs$lang$test:null)])));
} else {
return and__5140__auto__;
}
})();
var tap_QMARK__47008 = cljs.core.deref(day8.re_frame.debux.common.util.tap_output_QMARK_);
if(cljs.core.truth_((function (){var or__5142__auto__ = trace_QMARK__47007;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return tap_QMARK__47008;
}
})())){
var t_47010 = day8.re_frame.debux.common.util.now_ms();
var new_47011 = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p__46389){
var vec__46393 = p__46389;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46393,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46393,(1),null);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"fx-key","fx-key",-516894324),k,new cljs.core.Keyword(null,"value","value",305978217),v,new cljs.core.Keyword(null,"t","t",-1397832519),t_47010], null);
}),effect_map);
if(cljs.core.truth_(trace_QMARK__47007)){
var existing_47016 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"fx-effects","fx-effects",1867188130)], null),cljs.core.PersistentVector.EMPTY);
if(re_frame.trace.is_trace_enabled_QMARK_()){
var new_trace__21478__auto___47017 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$4(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"tags","tags",1771418977),cljs.core.merge,new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"fx-effects","fx-effects",1867188130),cljs.core.into.cljs$core$IFn$_invoke$arity$2(existing_47016,new_47011)], null)], null))),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"fx-effects","fx-effects",1867188130),cljs.core.into.cljs$core$IFn$_invoke$arity$2(existing_47016,new_47011)], null)], null),new cljs.core.Keyword(null,"tags","tags",1771418977))], 0));
(re_frame.trace._STAR_current_trace_STAR_ = new_trace__21478__auto___47017);

} else {
}
} else {
}

if(cljs.core.truth_(tap_QMARK__47008)){
var seq__46406_47022 = cljs.core.seq(new_47011);
var chunk__46407_47023 = null;
var count__46408_47024 = (0);
var i__46409_47025 = (0);
while(true){
if((i__46409_47025 < count__46408_47024)){
var entry_47029 = chunk__46407_47023.cljs$core$IIndexed$_nth$arity$2(null,i__46409_47025);
cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_47029,new cljs.core.Keyword("debux","kind","debux/kind",-1416724783),new cljs.core.Keyword(null,"fx-effect","fx-effect",-1124821948)),t_47010));


var G__47037 = seq__46406_47022;
var G__47038 = chunk__46407_47023;
var G__47039 = count__46408_47024;
var G__47040 = (i__46409_47025 + (1));
seq__46406_47022 = G__47037;
chunk__46407_47023 = G__47038;
count__46408_47024 = G__47039;
i__46409_47025 = G__47040;
continue;
} else {
var temp__5825__auto___47046 = cljs.core.seq(seq__46406_47022);
if(temp__5825__auto___47046){
var seq__46406_47048__$1 = temp__5825__auto___47046;
if(cljs.core.chunked_seq_QMARK_(seq__46406_47048__$1)){
var c__5673__auto___47052 = cljs.core.chunk_first(seq__46406_47048__$1);
var G__47053 = cljs.core.chunk_rest(seq__46406_47048__$1);
var G__47054 = c__5673__auto___47052;
var G__47055 = cljs.core.count(c__5673__auto___47052);
var G__47056 = (0);
seq__46406_47022 = G__47053;
chunk__46407_47023 = G__47054;
count__46408_47024 = G__47055;
i__46409_47025 = G__47056;
continue;
} else {
var entry_47064 = cljs.core.first(seq__46406_47048__$1);
cljs.core.tap_GT_(day8.re_frame.debux.common.util.stamp_tap_payload.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_47064,new cljs.core.Keyword("debux","kind","debux/kind",-1416724783),new cljs.core.Keyword(null,"fx-effect","fx-effect",-1124821948)),t_47010));


var G__47071 = cljs.core.next(seq__46406_47048__$1);
var G__47072 = null;
var G__47073 = (0);
var G__47074 = (0);
seq__46406_47022 = G__47071;
chunk__46407_47023 = G__47072;
count__46408_47024 = G__47073;
i__46409_47025 = G__47074;
continue;
}
} else {
}
}
break;
}
} else {
}
} else {
}
} else {
}

return null;
});
day8.re_frame.debux.common.util.once_state_limit = (10000);
day8.re_frame.debux.common.util.once_state_prune_to = cljs.core.quot(day8.re_frame.debux.common.util.once_state_limit,(2));
if((typeof day8 !== 'undefined') && (typeof day8.re_frame !== 'undefined') && (typeof day8.re_frame.debux !== 'undefined') && (typeof day8.re_frame.debux.common !== 'undefined') && (typeof day8.re_frame.debux.common.util !== 'undefined') && (typeof day8.re_frame.debux.common.util.once_state !== 'undefined')){
} else {
day8.re_frame.debux.common.util.once_state = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"next-order","next-order",1602893507),(0),new cljs.core.Keyword(null,"entries","entries",-86943161),cljs.core.PersistentArrayMap.EMPTY], null));
}
day8.re_frame.debux.common.util.result_fingerprint = (function day8$re_frame$debux$common$util$result_fingerprint(result){
return cljs.core.hash(result);
});
/**
 * Accept the current state shape and the pre-fingerprint map shape
 * that may survive across a REPL hot-reload because `once-state` is a
 * defonce. The migration hashes old retained values once, then drops
 * those references when the caller CASes the normalized state back in.
 */
day8.re_frame.debux.common.util.normalize_once_state = (function day8$re_frame$debux$common$util$normalize_once_state(state){
if(((cljs.core.map_QMARK_(state)) && (cljs.core.contains_QMARK_(state,new cljs.core.Keyword(null,"entries","entries",-86943161))))){
return state;
} else {
var entries = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2((function (idx,p__46451){
var vec__46452 = p__46451;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46452,(0),null);
var result = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46452,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [k,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fingerprint","fingerprint",598613022),day8.re_frame.debux.common.util.result_fingerprint(result),new cljs.core.Keyword(null,"seen-order","seen-order",741466967),(idx + (1))], null)], null);
}),state));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"next-order","next-order",1602893507),cljs.core.count(entries),new cljs.core.Keyword(null,"entries","entries",-86943161),entries], null);
}
});
day8.re_frame.debux.common.util.prune_once_entries = (function day8$re_frame$debux$common$util$prune_once_entries(entries){
if((cljs.core.count(entries) <= day8.re_frame.debux.common.util.once_state_limit)){
return entries;
} else {
var keep_keys = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.key,cljs.core.take.cljs$core$IFn$_invoke$arity$2(day8.re_frame.debux.common.util.once_state_prune_to,cljs.core.sort_by.cljs$core$IFn$_invoke$arity$3(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"seen-order","seen-order",741466967),cljs.core.val),cljs.core._GT_,entries))));
return cljs.core.select_keys(entries,keep_keys);
}
});
/**
 * Drop all `:once` dedup state. Used by the integration-test fixture
 * (so cross-test contamination doesn't make a previous test's last
 * emission silence the next one) and exposed publicly so REPL callers
 * running a long live-debug session can clear the slate without
 * waiting for a hot-reload to invalidate keys.
 * 
 * Public callers should prefer the re-export at
 * `day8.re-frame.tracing/reset-once-state!`; this internal name (with
 * the leading dash) stays for in-tree call sites that already require
 * `common.util` directly.
 */
day8.re_frame.debux.common.util._reset_once_state_BANG_ = (function day8$re_frame$debux$common$util$_reset_once_state_BANG_(){
return cljs.core.reset_BANG_(day8.re_frame.debux.common.util.once_state,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"next-order","next-order",1602893507),(0),new cljs.core.Keyword(null,"entries","entries",-86943161),cljs.core.PersistentArrayMap.EMPTY], null));
});
day8.re_frame.debux.common.util.next_once_state = (function day8$re_frame$debux$common$util$next_once_state(state,k,fingerprint){
var map__46472 = day8.re_frame.debux.common.util.normalize_once_state(state);
var map__46472__$1 = cljs.core.__destructure_map(map__46472);
var entries = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46472__$1,new cljs.core.Keyword(null,"entries","entries",-86943161));
var next_order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46472__$1,new cljs.core.Keyword(null,"next-order","next-order",1602893507));
var prev = cljs.core.get.cljs$core$IFn$_invoke$arity$3(entries,k,new cljs.core.Keyword("day8.re-frame.debux.common.util","unseen","day8.re-frame.debux.common.util/unseen",-1506806218));
if(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(prev,new cljs.core.Keyword("day8.re-frame.debux.common.util","unseen","day8.re-frame.debux.common.util/unseen",-1506806218))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"fingerprint","fingerprint",598613022).cljs$core$IFn$_invoke$arity$1(prev),fingerprint)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [state,false], null);
} else {
var order = (next_order + (1));
var entries_SINGLEQUOTE_ = day8.re_frame.debux.common.util.prune_once_entries(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entries,k,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fingerprint","fingerprint",598613022),fingerprint,new cljs.core.Keyword(null,"seen-order","seen-order",741466967),order], null)));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"next-order","next-order",1602893507),order,new cljs.core.Keyword(null,"entries","entries",-86943161),entries_SINGLEQUOTE_], null),true], null);
}
});
/**
 * Returns true if a `:once`-gated form should emit its trace right
 * now, false if the same (form, result) pair was the most recent
 * emission and should be suppressed.
 * 
 * Side effect: when emit-allowed (returns true), the new result is
 * hashed and recorded as the latest fingerprint for
 * `[trace-id syntax-order]`, so the next call with the same result
 * returns false without retaining the live result value.
 * 
 * `nil` and `false` are distinguishable from `::unseen` (the sentinel
 * for 'never emitted'), so a form that legitimately produces a stable
 * `nil` result emits ONCE (on first sighting) and then dedupes.
 */
day8.re_frame.debux.common.util._once_emit_QMARK_ = (function day8$re_frame$debux$common$util$_once_emit_QMARK_(trace_id,syntax_order,new_result){
var k = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [trace_id,syntax_order], null);
var fingerprint = day8.re_frame.debux.common.util.result_fingerprint(new_result);
while(true){
var state = cljs.core.deref(day8.re_frame.debux.common.util.once_state);
var vec__46482 = day8.re_frame.debux.common.util.next_once_state(state,k,fingerprint);
var state_SINGLEQUOTE_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46482,(0),null);
var emit_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46482,(1),null);
if((state === state_SINGLEQUOTE_)){
return emit_QMARK_;
} else {
if(cljs.core.compare_and_set_BANG_(day8.re_frame.debux.common.util.once_state,state,state_SINGLEQUOTE_)){
return emit_QMARK_;
} else {
continue;
}
}
break;
}
});
day8.re_frame.debux.common.util.indent_level_STAR_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((1));
day8.re_frame.debux.common.util.reset_indent_level_BANG_ = (function day8$re_frame$debux$common$util$reset_indent_level_BANG_(){
return cljs.core.reset_BANG_(day8.re_frame.debux.common.util.indent_level_STAR_,(1));
});
day8.re_frame.debux.common.util.print_seq_length_STAR_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((100));
day8.re_frame.debux.common.util.print_length_STAR_ = day8.re_frame.debux.common.util.print_seq_length_STAR_;
day8.re_frame.debux.common.util.set_print_length_BANG_ = (function day8$re_frame$debux$common$util$set_print_length_BANG_(num){
return cljs.core.reset_BANG_(day8.re_frame.debux.common.util.print_seq_length_STAR_,num);
});
day8.re_frame.debux.common.util.set_print_seq_length_BANG_ = (function day8$re_frame$debux$common$util$set_print_seq_length_BANG_(num){
return day8.re_frame.debux.common.util.set_print_length_BANG_(num);
});
day8.re_frame.debux.common.util.cljs_env_QMARK_ = (function day8$re_frame$debux$common$util$cljs_env_QMARK_(env){
return cljs.core.boolean$(new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(env));
});
/**
 * Transsub-forms a vector into an array-map with key/value pairs.
 *   (def a 10)
 *   (def b 20)
 *   (vec-map [a b :c [30 40]])
 *   => {:a 10 :b 20 ::c :c :[30 40] [30 40]}
 */
day8.re_frame.debux.common.util.vec__GT_map = (function day8$re_frame$debux$common$util$vec__GT_map(v){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.array_map,cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (elm){
return cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(elm))),null,(1),null)),(new cljs.core.List(null,elm,null,(1),null))))));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([v], 0)));
});
day8.re_frame.debux.common.util.replace__AMPERSAND_ = (function day8$re_frame$debux$common$util$replace__AMPERSAND_(v){
return clojure.walk.postwalk_replace(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Symbol(null,"&","&",-2144855648,null),cljs.core.list(new cljs.core.Symbol(null,"quote","quote",1377916282,null),new cljs.core.Symbol(null,"&","&",-2144855648,null))], null),v);
});
day8.re_frame.debux.common.util.take_n_if_seq = (function day8$re_frame$debux$common$util$take_n_if_seq(n,result){
if(cljs.core.coll_QMARK_(result)){
return cljs.core.take.cljs$core$IFn$_invoke$arity$2((function (){var or__5142__auto__ = n;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.deref(day8.re_frame.debux.common.util.print_seq_length_STAR_);
}
})(),result);
} else {
return result;
}
});
day8.re_frame.debux.common.util.truncate = (function day8$re_frame$debux$common$util$truncate(s){
if((cljs.core.count(s) > (70))){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(s.substring((0),(70)))+" ...");
} else {
return s;
}
});
day8.re_frame.debux.common.util.make_bars_ = (function day8$re_frame$debux$common$util$make_bars_(times){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(times,"|"));
});
day8.re_frame.debux.common.util.make_bars = cljs.core.memoize(day8.re_frame.debux.common.util.make_bars_);
day8.re_frame.debux.common.util.prepend_bars = (function day8$re_frame$debux$common$util$prepend_bars(line,indent_level){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(day8.re_frame.debux.common.util.make_bars(indent_level))+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line));
});
day8.re_frame.debux.common.util.print_form_with_indent = (function day8$re_frame$debux$common$util$print_form_with_indent(form,indent_level){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([day8.re_frame.debux.common.util.prepend_bars(form,indent_level)], 0));

return cljs.core.flush();
});
day8.re_frame.debux.common.util.form_header = (function day8$re_frame$debux$common$util$form_header(var_args){
var args__5882__auto__ = [];
var len__5876__auto___47138 = arguments.length;
var i__5877__auto___47140 = (0);
while(true){
if((i__5877__auto___47140 < len__5876__auto___47138)){
args__5882__auto__.push((arguments[i__5877__auto___47140]));

var G__47143 = (i__5877__auto___47140 + (1));
i__5877__auto___47140 = G__47143;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return day8.re_frame.debux.common.util.form_header.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(day8.re_frame.debux.common.util.form_header.cljs$core$IFn$_invoke$arity$variadic = (function (form,p__46547){
var vec__46548 = p__46547;
var msg = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46548,(0),null);
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(day8.re_frame.debux.common.util.truncate(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([form], 0))))+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var and__5140__auto__ = msg;
if(cljs.core.truth_(and__5140__auto__)){
return (""+"   <"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg)+">");
} else {
return and__5140__auto__;
}
})())+" =>");
}));

(day8.re_frame.debux.common.util.form_header.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(day8.re_frame.debux.common.util.form_header.cljs$lang$applyTo = (function (seq46540){
var G__46541 = cljs.core.first(seq46540);
var seq46540__$1 = cljs.core.next(seq46540);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__46541,seq46540__$1);
}));

day8.re_frame.debux.common.util.prepend_blanks = (function day8$re_frame$debux$common$util$prepend_blanks(lines){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__46566_SHARP_){
return (""+"  "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__46566_SHARP_));
}),lines);
});
day8.re_frame.debux.common.util.pprint_result_with_indent = (function day8$re_frame$debux$common$util$pprint_result_with_indent(result,indent_level){
var res = result;
var result__$1 = (function (){var _STAR_print_length_STAR__orig_val__46570 = cljs.core._STAR_print_length_STAR_;
var _STAR_print_length_STAR__temp_val__46571 = (function (){var or__5142__auto__ = cljs.core.deref(day8.re_frame.debux.common.util.print_seq_length_STAR_);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core._STAR_print_length_STAR_;
}
})();
(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__temp_val__46571);

try{var sb__5795__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR__orig_val__46576_47159 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR__orig_val__46577_47160 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_newline_STAR__temp_val__46578_47161 = true;
var _STAR_print_fn_STAR__temp_val__46579_47162 = (function (x__5796__auto__){
return sb__5795__auto__.append(x__5796__auto__);
});
(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__46578_47161);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__46579_47162);

try{cljs.pprint.pprint.cljs$core$IFn$_invoke$arity$1(res);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__46577_47160);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__46576_47159);
}
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__5795__auto__));
}finally {(cljs.core._STAR_print_length_STAR_ = _STAR_print_length_STAR__orig_val__46570);
}})();
var pprint = clojure.string.trim(result__$1);
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([clojure.string.join.cljs$core$IFn$_invoke$arity$2("\n",cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__46569_SHARP_){
return day8.re_frame.debux.common.util.prepend_bars(p1__46569_SHARP_,indent_level);
}),day8.re_frame.debux.common.util.prepend_blanks(clojure.string.split.cljs$core$IFn$_invoke$arity$2(pprint,/\n/))))], 0));

return cljs.core.flush();
});
day8.re_frame.debux.common.util.insert_blank_line = (function day8$re_frame$debux$common$util$insert_blank_line(){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" "], 0));

return cljs.core.flush();
});
/**
 * True when an opts map enables :once using either public spelling.
 */
day8.re_frame.debux.common.util.once_opt_QMARK_ = (function day8$re_frame$debux$common$util$once_opt_QMARK_(opts){
return cljs.core.boolean$((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"once","once",-262568523).cljs$core$IFn$_invoke$arity$1(opts);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"o","o",-1350007228).cljs$core$IFn$_invoke$arity$1(opts);
}
})());
});
/**
 * True when an opts map enables :final using either public spelling.
 */
day8.re_frame.debux.common.util.final_opt_QMARK_ = (function day8$re_frame$debux$common$util$final_opt_QMARK_(opts){
return cljs.core.boolean$((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"final","final",1157881357).cljs$core$IFn$_invoke$arity$1(opts);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"f","f",-1597136552).cljs$core$IFn$_invoke$arity$1(opts);
}
})());
});
/**
 * Return the trace label from an opts map. :msg wins over :m.
 */
day8.re_frame.debux.common.util.msg_opt = (function day8$re_frame$debux$common$util$msg_opt(opts){
var or__5142__auto__ = new cljs.core.Keyword(null,"msg","msg",-1386103444).cljs$core$IFn$_invoke$arity$1(opts);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"m","m",1632677161).cljs$core$IFn$_invoke$arity$1(opts);
}
});
/**
 * Parse trailing macro option tokens into the normalized opts map used
 * by dbgn/dbg-style trace emitters.
 * 
 * Recognized forms:
 *   number        -> {:n number}
 *   string        -> {:msg string}
 *   :if pred      -> {:if pred}
 *   :js           -> {:js true}
 *   :once or :o   -> {:once true}
 *   :final or :f  -> {:final true}
 *   :msg/:m value -> {:msg value}
 *   :verbose      -> {:verbose true}
 *   :show-all     -> {:verbose true}
 *   :style/:s val -> {:style val}
 *   :clog         -> {:clog true}
 * 
 * Unrecognized options (typos, or options from a future debux version
 * this fork hasn't picked up yet) are logged via console.warn (cljs)
 * or *err* (clj) and skipped — the loop continues so prior and later
 * recognized options are preserved instead of being silently dropped
 * when the cond falls through.
 * 
 * Callers that already accept an opts map, such as fn-traced and
 * dbgn-forms, use once-opt?, final-opt?, and msg-opt to honor the
 * same aliases without reparsing a map as positional tokens.
 */
day8.re_frame.debux.common.util.parse_opts = (function day8$re_frame$debux$common$util$parse_opts(opts){
var opts__$1 = opts;
var acc = cljs.core.PersistentArrayMap.EMPTY;
while(true){
var f = cljs.core.first(opts__$1);
var s = cljs.core.second(opts__$1);
if(cljs.core.empty_QMARK_(opts__$1)){
return acc;
} else {
if(typeof f === 'number'){
var G__47174 = cljs.core.next(opts__$1);
var G__47175 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"n","n",562130025),f);
opts__$1 = G__47174;
acc = G__47175;
continue;
} else {
if(typeof f === 'string'){
var G__47178 = cljs.core.next(opts__$1);
var G__47179 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"msg","msg",-1386103444),f);
opts__$1 = G__47178;
acc = G__47179;
continue;
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(f,new cljs.core.Keyword(null,"if","if",-458814265))){
var G__47180 = cljs.core.nnext(opts__$1);
var G__47181 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"if","if",-458814265),s);
opts__$1 = G__47180;
acc = G__47181;
continue;
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(f,new cljs.core.Keyword(null,"js","js",1768080579))){
var G__47182 = cljs.core.next(opts__$1);
var G__47183 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"js","js",1768080579),true);
opts__$1 = G__47182;
acc = G__47183;
continue;
} else {
if(cljs.core.truth_((function (){var fexpr__46698 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"o","o",-1350007228),null,new cljs.core.Keyword(null,"once","once",-262568523),null], null), null);
return (fexpr__46698.cljs$core$IFn$_invoke$arity$1 ? fexpr__46698.cljs$core$IFn$_invoke$arity$1(f) : fexpr__46698.call(null,f));
})())){
var G__47188 = cljs.core.next(opts__$1);
var G__47189 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"once","once",-262568523),true);
opts__$1 = G__47188;
acc = G__47189;
continue;
} else {
if(cljs.core.truth_((function (){var fexpr__46703 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"final","final",1157881357),null,new cljs.core.Keyword(null,"f","f",-1597136552),null], null), null);
return (fexpr__46703.cljs$core$IFn$_invoke$arity$1 ? fexpr__46703.cljs$core$IFn$_invoke$arity$1(f) : fexpr__46703.call(null,f));
})())){
var G__47195 = cljs.core.next(opts__$1);
var G__47198 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"final","final",1157881357),true);
opts__$1 = G__47195;
acc = G__47198;
continue;
} else {
if(cljs.core.truth_((function (){var fexpr__46705 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"m","m",1632677161),null,new cljs.core.Keyword(null,"msg","msg",-1386103444),null], null), null);
return (fexpr__46705.cljs$core$IFn$_invoke$arity$1 ? fexpr__46705.cljs$core$IFn$_invoke$arity$1(f) : fexpr__46705.call(null,f));
})())){
var G__47201 = cljs.core.nnext(opts__$1);
var G__47202 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"msg","msg",-1386103444),s);
opts__$1 = G__47201;
acc = G__47202;
continue;
} else {
if(cljs.core.truth_((function (){var fexpr__46708 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"verbose","verbose",1694226060),null,new cljs.core.Keyword(null,"show-all","show-all",715701051),null], null), null);
return (fexpr__46708.cljs$core$IFn$_invoke$arity$1 ? fexpr__46708.cljs$core$IFn$_invoke$arity$1(f) : fexpr__46708.call(null,f));
})())){
var G__47211 = cljs.core.next(opts__$1);
var G__47212 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"verbose","verbose",1694226060),true);
opts__$1 = G__47211;
acc = G__47212;
continue;
} else {
if(cljs.core.truth_((function (){var fexpr__46713 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"s","s",1705939918),null,new cljs.core.Keyword(null,"style","style",-496642736),null], null), null);
return (fexpr__46713.cljs$core$IFn$_invoke$arity$1 ? fexpr__46713.cljs$core$IFn$_invoke$arity$1(f) : fexpr__46713.call(null,f));
})())){
var G__47220 = cljs.core.nnext(opts__$1);
var G__47221 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"style","style",-496642736),s);
opts__$1 = G__47220;
acc = G__47221;
continue;
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(f,new cljs.core.Keyword(null,"clog","clog",954273629))){
var G__47229 = cljs.core.next(opts__$1);
var G__47230 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,new cljs.core.Keyword(null,"clog","clog",954273629),true);
opts__$1 = G__47229;
acc = G__47230;
continue;
} else {
console.warn((""+"[debux] parse-opts: ignoring unrecognized option "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([f], 0)))));

var G__47233 = cljs.core.next(opts__$1);
var G__47234 = acc;
opts__$1 = G__47233;
acc = G__47234;
continue;

}
}
}
}
}
}
}
}
}
}
}
break;
}
});
day8.re_frame.debux.common.util.quote_val = (function day8$re_frame$debux$common$util$quote_val(p__46721){
var vec__46724 = p__46721;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46724,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46724,(1),null);
return cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,k,null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol(null,"quote","quote",1377916282,null),null,(1),null)),(new cljs.core.List(null,v,null,(1),null))))),null,(1),null))))));
});
day8.re_frame.debux.common.util.quote_vals = (function day8$re_frame$debux$common$util$quote_vals(m){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2(day8.re_frame.debux.common.util.quote_val,m));
});
day8.re_frame.debux.common.util.include_recur_QMARK_ = (function day8$re_frame$debux$common$util$include_recur_QMARK_(form){
var G__46776 = new cljs.core.Symbol(null,"recur","recur",1202958259,null);
var fexpr__46775 = cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.set,cljs.core.flatten)(form);
return (fexpr__46775.cljs$core$IFn$_invoke$arity$1 ? fexpr__46775.cljs$core$IFn$_invoke$arity$1(G__46776) : fexpr__46775.call(null,G__46776));
});
/**
 * True iff `sym` is the fully-qualified `o-skip` macro name. Used by
 * `insert-o-skip-for-recur` (skip.cljc) to detect a node that's
 * already been wrapped on a prior pass and avoid re-wrapping.
 * 
 * The fqn was `'debux.common.macro-specs/o-skip` — left over from
 * the upstream philoskim/debux library before this fork renamed the
 * namespace to `day8.re-frame.debux.common.macro-specs`. With the
 * stale fqn the predicate ALWAYS returned false, so the recur-walker
 * re-wrapped already-wrapped nodes and `dbgn` macroexpansion
 * diverged on `loop`+`recur` (issue #40).
 * 
 * Other callers (skip-place? at line 478ish) already used the right
 * fqn — only this defn was stale.
 */
day8.re_frame.debux.common.util.o_skip_QMARK_ = (function day8$re_frame$debux$common$util$o_skip_QMARK_(sym){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol("day8.re-frame.debux.common.macro-specs","o-skip","day8.re-frame.debux.common.macro-specs/o-skip",1764322383,null),sym);
});
day8.re_frame.debux.common.util.spy_first = (function day8$re_frame$debux$common$util$spy_first(result,quoted_form,indent){
if(cljs.core.integer_QMARK_(indent)){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"indent was not correctly replaced for form "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.prn_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([quoted_form], 0)))+"\nThis is a bug, please report it to https://github.com/Day8/re-frame-debux"))+"\n"+"(integer? indent)")));
}

day8.re_frame.debux.common.util.send_trace_BANG_(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"form","form",-1624062471),(function (){var G__46793 = quoted_form;
var G__46794 = new cljs.core.Symbol(null,"dummy","dummy",-594669915,null);
return (day8.re_frame.debux.common.util.remove_d.cljs$core$IFn$_invoke$arity$2 ? day8.re_frame.debux.common.util.remove_d.cljs$core$IFn$_invoke$arity$2(G__46793,G__46794) : day8.re_frame.debux.common.util.remove_d.call(null,G__46793,G__46794));
})(),new cljs.core.Keyword(null,"result","result",1415092211),result,new cljs.core.Keyword(null,"indent-level","indent-level",-258835684),indent], null));

return result;
});
day8.re_frame.debux.common.util.spy_last = (function day8$re_frame$debux$common$util$spy_last(quoted_form,indent,result){
if(cljs.core.integer_QMARK_(indent)){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"indent was not correctly replaced for form "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.prn_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([quoted_form], 0)))+"\nThis is a bug, please report it to https://github.com/Day8/re-frame-debux"))+"\n"+"(integer? indent)")));
}

day8.re_frame.debux.common.util.send_trace_BANG_(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"form","form",-1624062471),(function (){var G__46799 = quoted_form;
var G__46800 = new cljs.core.Symbol(null,"dummy","dummy",-594669915,null);
return (day8.re_frame.debux.common.util.remove_d.cljs$core$IFn$_invoke$arity$2 ? day8.re_frame.debux.common.util.remove_d.cljs$core$IFn$_invoke$arity$2(G__46799,G__46800) : day8.re_frame.debux.common.util.remove_d.call(null,G__46799,G__46800));
})(),new cljs.core.Keyword(null,"result","result",1415092211),result,new cljs.core.Keyword(null,"indent-level","indent-level",-258835684),indent], null));

return result;
});
day8.re_frame.debux.common.util.spy_comp = (function day8$re_frame$debux$common$util$spy_comp(quoted_form,indent,form){
return (function() { 
var G__47261__delegate = function (arg){
var result = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(form,arg);
if(cljs.core.integer_QMARK_(indent)){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"indent was not correctly replaced for form "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.prn_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([quoted_form], 0)))+"\nThis is a bug, please report it to https://github.com/Day8/re-frame-debux"))+"\n"+"(integer? indent)")));
}

day8.re_frame.debux.common.util.send_trace_BANG_(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"form","form",-1624062471),(function (){var G__46812 = quoted_form;
var G__46813 = new cljs.core.Symbol(null,"dummy","dummy",-594669915,null);
return (day8.re_frame.debux.common.util.remove_d.cljs$core$IFn$_invoke$arity$2 ? day8.re_frame.debux.common.util.remove_d.cljs$core$IFn$_invoke$arity$2(G__46812,G__46813) : day8.re_frame.debux.common.util.remove_d.call(null,G__46812,G__46813));
})(),new cljs.core.Keyword(null,"result","result",1415092211),result,new cljs.core.Keyword(null,"indent-level","indent-level",-258835684),indent], null));

return result;
};
var G__47261 = function (var_args){
var arg = null;
if (arguments.length > 0) {
var G__47267__i = 0, G__47267__a = new Array(arguments.length -  0);
while (G__47267__i < G__47267__a.length) {G__47267__a[G__47267__i] = arguments[G__47267__i + 0]; ++G__47267__i;}
  arg = new cljs.core.IndexedSeq(G__47267__a,0,null);
} 
return G__47261__delegate.call(this,arg);};
G__47261.cljs$lang$maxFixedArity = 0;
G__47261.cljs$lang$applyTo = (function (arglist__47268){
var arg = cljs.core.seq(arglist__47268);
return G__47261__delegate(arg);
});
G__47261.cljs$core$IFn$_invoke$arity$variadic = G__47261__delegate;
return G__47261;
})()
;
});
day8.re_frame.debux.common.util.debux_skip_symbol_QMARK_ = (function day8$re_frame$debux$common$util$debux_skip_symbol_QMARK_(sym){
return cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Symbol("day8.re-frame.debux.common.macro-specs","skip","day8.re-frame.debux.common.macro-specs/skip",155781441,null),null,new cljs.core.Symbol("day8.re-frame.debux.common.macro-specs","o-skip","day8.re-frame.debux.common.macro-specs/o-skip",1764322383,null),null,new cljs.core.Symbol("day8.re-frame.debux.common.macro-specs","skip-outer","day8.re-frame.debux.common.macro-specs/skip-outer",1719173285,null),null,new cljs.core.Keyword("day8.re-frame.debux.common.macro-specs","skip-place","day8.re-frame.debux.common.macro-specs/skip-place",-1613962318),null], null), null),sym);
});
day8.re_frame.debux.common.util.spy_first_QMARK_ = (function day8$re_frame$debux$common$util$spy_first_QMARK_(sym){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol("day8.re-frame.debux.common.util","spy-first","day8.re-frame.debux.common.util/spy-first",-45028370,null),sym);
});
day8.re_frame.debux.common.util.remove_d = (function day8$re_frame$debux$common$util$remove_d(form,d_sym){
var loc = day8.re_frame.debux.common.util.sequential_zip(form);
while(true){
var node = clojure.zip.node(loc);
if(clojure.zip.end_QMARK_(loc)){
return clojure.zip.root(loc);
} else {
if(((cljs.core.seq_QMARK_(node)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(d_sym,cljs.core.first(node))) || (((day8.re_frame.debux.common.util.debux_skip_symbol_QMARK_(cljs.core.first(node))) || (day8.re_frame.debux.common.util.spy_first_QMARK_(cljs.core.first(node))))))))){
var G__47275 = clojure.zip.replace(loc,cljs.core.last(node));
loc = G__47275;
continue;
} else {
if(((cljs.core.seq_QMARK_(node)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol("day8.re-frame.debux.common.util","spy-last","day8.re-frame.debux.common.util/spy-last",-1681078777,null),cljs.core.first(node))))){
var G__47276 = clojure.zip.replace(loc,cljs.core.last(node));
loc = G__47276;
continue;
} else {
var G__47278 = clojure.zip.next(loc);
loc = G__47278;
continue;

}
}
}
break;
}
});

//# sourceMappingURL=day8.re_frame.debux.common.util.js.map
