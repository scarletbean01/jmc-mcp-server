goog.provide('day8.re_frame.tracing');
/**
 * @define {boolean}
 * @type {boolean}
 */
day8.re_frame.tracing.trace_enabled_QMARK_ = goog.define("day8.re_frame.tracing.trace_enabled_QMARK_",false);
/**
 * See https://groups.google.com/d/msg/clojurescript/jk43kmYiMhA/IHglVr_TPdgJ for more details
 */
day8.re_frame.tracing.is_trace_enabled_QMARK_ = (function day8$re_frame$tracing$is_trace_enabled_QMARK_(){
return day8.re_frame.tracing.trace_enabled_QMARK_;
});
day8.re_frame.tracing.reset_indent_level_BANG_ = day8.re_frame.debux.common.util.reset_indent_level_BANG_;
day8.re_frame.tracing.set_date_time_fn_BANG_ = day8.re_frame.debux.common.util.set_date_time_fn_BANG_;
day8.re_frame.tracing.set_print_length_BANG_ = day8.re_frame.debux.common.util.set_print_length_BANG_;
day8.re_frame.tracing.set_print_seq_length_BANG_ = day8.re_frame.debux.common.util.set_print_seq_length_BANG_;
day8.re_frame.tracing.set_tap_output_BANG_ = day8.re_frame.debux.common.util.set_tap_output_BANG_;
day8.re_frame.tracing.set_trace_frames_output_BANG_ = day8.re_frame.debux.common.util.set_trace_frames_output_BANG_;
day8.re_frame.tracing.reset_once_state_BANG_ = day8.re_frame.debux.common.util._reset_once_state_BANG_;
day8.re_frame.tracing.find_symbols = (function day8$re_frame$tracing$find_symbols(args){

var loc = day8.re_frame.debux.common.util.sequential_zip(args);
var seen = cljs.core.PersistentVector.EMPTY;
while(true){
var node = clojure.zip.node(loc);
if(clojure.zip.end_QMARK_(loc)){
return seen;
} else {
if((((node instanceof cljs.core.Symbol)) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol(null,"&","&",-2144855648,null),node)))){
var G__48900 = clojure.zip.next(loc);
var G__48901 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(seen,node);
loc = G__48900;
seen = G__48901;
continue;
} else {
var G__48903 = clojure.zip.next(loc);
var G__48904 = seen;
loc = G__48903;
seen = G__48904;
continue;

}
}
break;
}
});
/**
 * If the leading form of a fn-traced / defn-traced definition is a
 * map literal, treat it as the opts map (:locals, :if) and return
 * [opts (rest definition)]. Otherwise [nil definition].
 * The map sniffer is unambiguous because clojure.core/fn forbids a
 * map in this slot — `(fn {} [args] ...)` is always invalid.
 */
day8.re_frame.tracing.split_opts = (function day8$re_frame$tracing$split_opts(definition){
if(cljs.core.map_QMARK_(cljs.core.first(definition))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.first(definition),cljs.core.rest(definition)], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [null,definition], null);
}
});
/**
 * Build the traced body for day8.re-frame.tracing macros.
 * This is the richer helper for the public tracing surface: it handles
 * opts, frame markers, locals, send-form metadata, and fx-effect tracing.
 * The legacy day8.re-frame.debux.core macros use legacy-fn-body in core.clj.
 */
day8.re_frame.tracing.fn_body = (function day8$re_frame$tracing$fn_body(var_args){
var args__5882__auto__ = [];
var len__5876__auto___48906 = arguments.length;
var i__5877__auto___48908 = (0);
while(true){
if((i__5877__auto___48908 < len__5876__auto___48906)){
args__5882__auto__.push((arguments[i__5877__auto___48908]));

var G__48909 = (i__5877__auto___48908 + (1));
i__5877__auto___48908 = G__48909;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return day8.re_frame.tracing.fn_body.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(day8.re_frame.tracing.fn_body.cljs$core$IFn$_invoke$arity$variadic = (function (args_PLUS_body,opts,send_form){
var args = (function (){var or__5142__auto__ = new cljs.core.Keyword(null,"args","args",1315556576).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"args","args",1315556576).cljs$core$IFn$_invoke$arity$1(args_PLUS_body));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var body_or_prepost = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(args_PLUS_body),(0));
var body = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(args_PLUS_body),(1));
var prepost_QMARK_ = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"body","body",-2049205669),body_or_prepost);
var prepost_form = ((prepost_QMARK_)?new cljs.core.Keyword(null,"prepost","prepost",1251610712).cljs$core$IFn$_invoke$arity$1(body):null);
var traced_body = ((prepost_QMARK_)?new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(body):body);
var args_symbols = day8.re_frame.tracing.find_symbols(args);
var frame_id = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.gensym.cljs$core$IFn$_invoke$arity$1("frame_")));
var r = cljs.core.gensym.cljs$core$IFn$_invoke$arity$1("fn-traced-result_");
var emit_frames_QMARK_ = cljs.core.gensym.cljs$core$IFn$_invoke$arity$1("emit-frames?_");
var fx_trace = new cljs.core.Keyword(null,"fx-trace","fx-trace",-277681052).cljs$core$IFn$_invoke$arity$1(opts);
var frame_msg = day8.re_frame.debux.common.util.msg_opt(opts);
var emit_fx_form = (cljs.core.truth_(fx_trace)?((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"ctx","ctx",-493610118),fx_trace))?cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol("day8.re-frame.debux.common.util","-emit-fx-traces!","day8.re-frame.debux.common.util/-emit-fx-traces!",-772744444,null),null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Keyword(null,"effects","effects",-282369292),null,(1),null)),(new cljs.core.List(null,r,null,(1),null))))),null,(1),null))))):cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,new cljs.core.Symbol("day8.re-frame.debux.common.util","-emit-fx-traces!","day8.re-frame.debux.common.util/-emit-fx-traces!",-772744444,null),null,(1),null)),(new cljs.core.List(null,r,null,(1),null)))))):null);
return cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,args,null,(1),null)),(cljs.core.truth_(prepost_form)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [prepost_form], null):null),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","let","cljs.core/let",-308701135,null),null,(1),null)),(new cljs.core.List(null,cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,emit_frames_QMARK_,null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$1((new cljs.core.List(null,new cljs.core.Symbol("day8.re-frame.debux.common.util","frame-markers-enabled?","day8.re-frame.debux.common.util/frame-markers-enabled?",595980307,null),null,(1),null))))),null,(1),null)))))),null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","when","cljs.core/when",120293186,null),null,(1),null)),(new cljs.core.List(null,emit_frames_QMARK_,null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("day8.re-frame.debux.common.util","-send-frame-enter!","day8.re-frame.debux.common.util/-send-frame-enter!",10132037,null),null,(1),null)),(new cljs.core.List(null,frame_id,null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,frame_msg,null,(1),null))], 0)))),null,(1),null))], 0)))),null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","let","cljs.core/let",-308701135,null),null,(1),null)),(new cljs.core.List(null,cljs.core.vec(cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2((new cljs.core.List(null,r,null,(1),null)),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("dbgn","dbgn-forms","dbgn/dbgn-forms",-1354469701,null),null,(1),null)),(new cljs.core.List(null,traced_body,null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,send_form,null,(1),null)),(new cljs.core.List(null,args_symbols,null,(1),null)),(new cljs.core.List(null,opts,null,(1),null))], 0)))),null,(1),null)))))),null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_(emit_fx_form)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [emit_fx_form], null):null),(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("cljs.core","when","cljs.core/when",120293186,null),null,(1),null)),(new cljs.core.List(null,emit_frames_QMARK_,null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,cljs.core.sequence.cljs$core$IFn$_invoke$arity$1(cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic((new cljs.core.List(null,new cljs.core.Symbol("day8.re-frame.debux.common.util","-send-frame-exit!","day8.re-frame.debux.common.util/-send-frame-exit!",594151596,null),null,(1),null)),(new cljs.core.List(null,frame_id,null,(1),null)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new cljs.core.List(null,r,null,(1),null)),(new cljs.core.List(null,frame_msg,null,(1),null))], 0)))),null,(1),null))], 0)))),null,(1),null)),(new cljs.core.List(null,r,null,(1),null))], 0)))),null,(1),null))], 0)))),null,(1),null))], 0))));
}));

(day8.re_frame.tracing.fn_body.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(day8.re_frame.tracing.fn_body.cljs$lang$applyTo = (function (seq48790){
var G__48791 = cljs.core.first(seq48790);
var seq48790__$1 = cljs.core.next(seq48790);
var G__48792 = cljs.core.first(seq48790__$1);
var seq48790__$2 = cljs.core.next(seq48790__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__48791,G__48792,seq48790__$2);
}));


//# sourceMappingURL=day8.re_frame.tracing.js.map
