goog.provide('re_frame.router');
re_frame.router.later_fns = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"flush-dom","flush-dom",-933676816),(function (f){
var G__21866 = (function (){
return (re_frame.interop.next_tick.cljs$core$IFn$_invoke$arity$1 ? re_frame.interop.next_tick.cljs$core$IFn$_invoke$arity$1(f) : re_frame.interop.next_tick.call(null,f));
});
return (re_frame.interop.after_render.cljs$core$IFn$_invoke$arity$1 ? re_frame.interop.after_render.cljs$core$IFn$_invoke$arity$1(G__21866) : re_frame.interop.after_render.call(null,G__21866));
}),new cljs.core.Keyword(null,"yield","yield",177875009),re_frame.interop.next_tick], null);

/**
 * @interface
 */
re_frame.router.IEventQueue = function(){};

var re_frame$router$IEventQueue$push$dyn_22313 = (function (this$,event){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router.push[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,event) : m__5499__auto__.call(null,this$,event));
} else {
var m__5497__auto__ = (re_frame.router.push["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,event) : m__5497__auto__.call(null,this$,event));
} else {
throw cljs.core.missing_protocol("IEventQueue.push",this$);
}
}
});
re_frame.router.push = (function re_frame$router$push(this$,event){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$push$arity$2 == null)))))){
return this$.re_frame$router$IEventQueue$push$arity$2(this$,event);
} else {
return re_frame$router$IEventQueue$push$dyn_22313(this$,event);
}
});

var re_frame$router$IEventQueue$add_post_event_callback$dyn_22314 = (function (this$,id,callback_fn){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router.add_post_event_callback[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$3(this$,id,callback_fn) : m__5499__auto__.call(null,this$,id,callback_fn));
} else {
var m__5497__auto__ = (re_frame.router.add_post_event_callback["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$3(this$,id,callback_fn) : m__5497__auto__.call(null,this$,id,callback_fn));
} else {
throw cljs.core.missing_protocol("IEventQueue.add-post-event-callback",this$);
}
}
});
re_frame.router.add_post_event_callback = (function re_frame$router$add_post_event_callback(this$,id,callback_fn){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$add_post_event_callback$arity$3 == null)))))){
return this$.re_frame$router$IEventQueue$add_post_event_callback$arity$3(this$,id,callback_fn);
} else {
return re_frame$router$IEventQueue$add_post_event_callback$dyn_22314(this$,id,callback_fn);
}
});

var re_frame$router$IEventQueue$remove_post_event_callback$dyn_22320 = (function (this$,id){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router.remove_post_event_callback[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,id) : m__5499__auto__.call(null,this$,id));
} else {
var m__5497__auto__ = (re_frame.router.remove_post_event_callback["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,id) : m__5497__auto__.call(null,this$,id));
} else {
throw cljs.core.missing_protocol("IEventQueue.remove-post-event-callback",this$);
}
}
});
re_frame.router.remove_post_event_callback = (function re_frame$router$remove_post_event_callback(this$,id){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$remove_post_event_callback$arity$2 == null)))))){
return this$.re_frame$router$IEventQueue$remove_post_event_callback$arity$2(this$,id);
} else {
return re_frame$router$IEventQueue$remove_post_event_callback$dyn_22320(this$,id);
}
});

var re_frame$router$IEventQueue$purge$dyn_22321 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router.purge[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (re_frame.router.purge["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IEventQueue.purge",this$);
}
}
});
re_frame.router.purge = (function re_frame$router$purge(this$){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$purge$arity$1 == null)))))){
return this$.re_frame$router$IEventQueue$purge$arity$1(this$);
} else {
return re_frame$router$IEventQueue$purge$dyn_22321(this$);
}
});

var re_frame$router$IEventQueue$_fsm_trigger$dyn_22324 = (function (this$,trigger,arg){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._fsm_trigger[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$3(this$,trigger,arg) : m__5499__auto__.call(null,this$,trigger,arg));
} else {
var m__5497__auto__ = (re_frame.router._fsm_trigger["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$3(this$,trigger,arg) : m__5497__auto__.call(null,this$,trigger,arg));
} else {
throw cljs.core.missing_protocol("IEventQueue.-fsm-trigger",this$);
}
}
});
re_frame.router._fsm_trigger = (function re_frame$router$_fsm_trigger(this$,trigger,arg){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_fsm_trigger$arity$3 == null)))))){
return this$.re_frame$router$IEventQueue$_fsm_trigger$arity$3(this$,trigger,arg);
} else {
return re_frame$router$IEventQueue$_fsm_trigger$dyn_22324(this$,trigger,arg);
}
});

var re_frame$router$IEventQueue$_add_event$dyn_22328 = (function (this$,event){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._add_event[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,event) : m__5499__auto__.call(null,this$,event));
} else {
var m__5497__auto__ = (re_frame.router._add_event["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,event) : m__5497__auto__.call(null,this$,event));
} else {
throw cljs.core.missing_protocol("IEventQueue.-add-event",this$);
}
}
});
re_frame.router._add_event = (function re_frame$router$_add_event(this$,event){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_add_event$arity$2 == null)))))){
return this$.re_frame$router$IEventQueue$_add_event$arity$2(this$,event);
} else {
return re_frame$router$IEventQueue$_add_event$dyn_22328(this$,event);
}
});

var re_frame$router$IEventQueue$_process_1st_event_in_queue$dyn_22331 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._process_1st_event_in_queue[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (re_frame.router._process_1st_event_in_queue["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IEventQueue.-process-1st-event-in-queue",this$);
}
}
});
re_frame.router._process_1st_event_in_queue = (function re_frame$router$_process_1st_event_in_queue(this$){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_process_1st_event_in_queue$arity$1 == null)))))){
return this$.re_frame$router$IEventQueue$_process_1st_event_in_queue$arity$1(this$);
} else {
return re_frame$router$IEventQueue$_process_1st_event_in_queue$dyn_22331(this$);
}
});

var re_frame$router$IEventQueue$_run_next_tick$dyn_22335 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._run_next_tick[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (re_frame.router._run_next_tick["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IEventQueue.-run-next-tick",this$);
}
}
});
re_frame.router._run_next_tick = (function re_frame$router$_run_next_tick(this$){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_run_next_tick$arity$1 == null)))))){
return this$.re_frame$router$IEventQueue$_run_next_tick$arity$1(this$);
} else {
return re_frame$router$IEventQueue$_run_next_tick$dyn_22335(this$);
}
});

var re_frame$router$IEventQueue$_run_queue$dyn_22336 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._run_queue[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (re_frame.router._run_queue["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IEventQueue.-run-queue",this$);
}
}
});
re_frame.router._run_queue = (function re_frame$router$_run_queue(this$){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_run_queue$arity$1 == null)))))){
return this$.re_frame$router$IEventQueue$_run_queue$arity$1(this$);
} else {
return re_frame$router$IEventQueue$_run_queue$dyn_22336(this$);
}
});

var re_frame$router$IEventQueue$_exception$dyn_22339 = (function (this$,ex){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._exception[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,ex) : m__5499__auto__.call(null,this$,ex));
} else {
var m__5497__auto__ = (re_frame.router._exception["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,ex) : m__5497__auto__.call(null,this$,ex));
} else {
throw cljs.core.missing_protocol("IEventQueue.-exception",this$);
}
}
});
re_frame.router._exception = (function re_frame$router$_exception(this$,ex){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_exception$arity$2 == null)))))){
return this$.re_frame$router$IEventQueue$_exception$arity$2(this$,ex);
} else {
return re_frame$router$IEventQueue$_exception$dyn_22339(this$,ex);
}
});

var re_frame$router$IEventQueue$_pause$dyn_22343 = (function (this$,later_fn){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._pause[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,later_fn) : m__5499__auto__.call(null,this$,later_fn));
} else {
var m__5497__auto__ = (re_frame.router._pause["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,later_fn) : m__5497__auto__.call(null,this$,later_fn));
} else {
throw cljs.core.missing_protocol("IEventQueue.-pause",this$);
}
}
});
re_frame.router._pause = (function re_frame$router$_pause(this$,later_fn){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_pause$arity$2 == null)))))){
return this$.re_frame$router$IEventQueue$_pause$arity$2(this$,later_fn);
} else {
return re_frame$router$IEventQueue$_pause$dyn_22343(this$,later_fn);
}
});

var re_frame$router$IEventQueue$_resume$dyn_22344 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._resume[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (re_frame.router._resume["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IEventQueue.-resume",this$);
}
}
});
re_frame.router._resume = (function re_frame$router$_resume(this$){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_resume$arity$1 == null)))))){
return this$.re_frame$router$IEventQueue$_resume$arity$1(this$);
} else {
return re_frame$router$IEventQueue$_resume$dyn_22344(this$);
}
});

var re_frame$router$IEventQueue$_call_post_event_callbacks$dyn_22345 = (function (this$,event){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (re_frame.router._call_post_event_callbacks[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,event) : m__5499__auto__.call(null,this$,event));
} else {
var m__5497__auto__ = (re_frame.router._call_post_event_callbacks["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,event) : m__5497__auto__.call(null,this$,event));
} else {
throw cljs.core.missing_protocol("IEventQueue.-call-post-event-callbacks",this$);
}
}
});
re_frame.router._call_post_event_callbacks = (function re_frame$router$_call_post_event_callbacks(this$,event){
if((((!((this$ == null)))) && ((!((this$.re_frame$router$IEventQueue$_call_post_event_callbacks$arity$2 == null)))))){
return this$.re_frame$router$IEventQueue$_call_post_event_callbacks$arity$2(this$,event);
} else {
return re_frame$router$IEventQueue$_call_post_event_callbacks$dyn_22345(this$,event);
}
});


/**
* @constructor
 * @implements {re_frame.router.IEventQueue}
*/
re_frame.router.EventQueue = (function (fsm_state,queue,post_event_callback_fns){
this.fsm_state = fsm_state;
this.queue = queue;
this.post_event_callback_fns = post_event_callback_fns;
});
(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$ = cljs.core.PROTOCOL_SENTINEL);

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_run_queue$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
var n = cljs.core.count(self__.queue);
while(true){
if((n === (0))){
return this$__$1.re_frame$router$IEventQueue$_fsm_trigger$arity$3(null,new cljs.core.Keyword(null,"finish-run","finish-run",753148477),null);
} else {
var temp__5823__auto__ = cljs.core.some(re_frame.router.later_fns,cljs.core.keys(cljs.core.meta(cljs.core.peek(self__.queue))));
if(cljs.core.truth_(temp__5823__auto__)){
var later_fn = temp__5823__auto__;
return this$__$1.re_frame$router$IEventQueue$_fsm_trigger$arity$3(null,new cljs.core.Keyword(null,"pause","pause",-2095325672),later_fn);
} else {
this$__$1.re_frame$router$IEventQueue$_process_1st_event_in_queue$arity$1(null);

var G__22347 = (n - (1));
n = G__22347;
continue;
}
}
break;
}
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$remove_post_event_callback$arity$2 = (function (_,id){
var self__ = this;
var ___$1 = this;
if((!(cljs.core.contains_QMARK_(self__.post_event_callback_fns,id)))){
return re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: could not remove post event call back with id:",id], 0));
} else {
return (self__.post_event_callback_fns = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.post_event_callback_fns,id));
}
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_add_event$arity$2 = (function (_,event){
var self__ = this;
var ___$1 = this;
return (self__.queue = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(self__.queue,event));
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_resume$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
this$__$1.re_frame$router$IEventQueue$_process_1st_event_in_queue$arity$1(null);

return this$__$1.re_frame$router$IEventQueue$_run_queue$arity$1(null);
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$push$arity$2 = (function (this$,event){
var self__ = this;
var this$__$1 = this;
return this$__$1.re_frame$router$IEventQueue$_fsm_trigger$arity$3(null,new cljs.core.Keyword(null,"add-event","add-event",938429088),event);
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_run_next_tick$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
var G__22075 = (function (){
return this$__$1.re_frame$router$IEventQueue$_fsm_trigger$arity$3(null,new cljs.core.Keyword(null,"run-queue","run-queue",-1701798027),null);
});
return (re_frame.interop.next_tick.cljs$core$IFn$_invoke$arity$1 ? re_frame.interop.next_tick.cljs$core$IFn$_invoke$arity$1(G__22075) : re_frame.interop.next_tick.call(null,G__22075));
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_fsm_trigger$arity$3 = (function (this$,trigger,arg){
var self__ = this;
var this$__$1 = this;
if(re_frame.trace.is_trace_enabled_QMARK_()){
var _STAR_current_trace_STAR__orig_val__22077 = re_frame.trace._STAR_current_trace_STAR_;
var _STAR_current_trace_STAR__temp_val__22078 = re_frame.trace.start_trace(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"op-type","op-type",-1636141668),new cljs.core.Keyword("re-frame.router","fsm-trigger","re-frame.router/fsm-trigger",1379787274)], null));
(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__temp_val__22078);

try{try{var vec__22084 = (function (){var G__22087 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [self__.fsm_state,trigger], null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"idle","idle",-2007156861),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),(function (){
this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);

return this$__$1.re_frame$router$IEventQueue$_run_next_tick$arity$1(null);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"exception","exception",-335277064)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"idle","idle",-2007156861),(function (){
return this$__$1.re_frame$router$IEventQueue$_exception$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"finish-run","finish-run",753148477)], null),G__22087)){
if(cljs.core.empty_QMARK_(self__.queue)){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"idle","idle",-2007156861)], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),(function (){
return this$__$1.re_frame$router$IEventQueue$_run_next_tick$arity$1(null);
})], null);
}
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"pause","pause",-2095325672)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),(function (){
return this$__$1.re_frame$router$IEventQueue$_pause$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),new cljs.core.Keyword(null,"resume","resume",-118572261)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),(function (){
return this$__$1.re_frame$router$IEventQueue$_resume$arity$1(null);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),new cljs.core.Keyword(null,"run-queue","run-queue",-1701798027)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),(function (){
return this$__$1.re_frame$router$IEventQueue$_run_queue$arity$1(null);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),(function (){
return this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),(function (){
return this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22087)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),(function (){
return this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);
})], null);
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2((""+"re-frame: router state transition not found. "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(self__.fsm_state)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(trigger)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fsm-state","fsm-state",1656310533),self__.fsm_state,new cljs.core.Keyword(null,"trigger","trigger",103466139),trigger], null));

}
}
}
}
}
}
}
}
}
})();
var new_fsm_state = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22084,(0),null);
var action_fn = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22084,(1),null);
if(re_frame.trace.is_trace_enabled_QMARK_()){
var new_trace__21478__auto___22352 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$4(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"tags","tags",1771418977),cljs.core.merge,new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"operation","operation",-1267664310),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [self__.fsm_state,trigger], null),new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"current-state","current-state",1048284452),self__.fsm_state,new cljs.core.Keyword(null,"new-state","new-state",-490349212),new_fsm_state], null)], null))),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"operation","operation",-1267664310),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [self__.fsm_state,trigger], null),new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"current-state","current-state",1048284452),self__.fsm_state,new cljs.core.Keyword(null,"new-state","new-state",-490349212),new_fsm_state], null)], null),new cljs.core.Keyword(null,"tags","tags",1771418977))], 0));
(re_frame.trace._STAR_current_trace_STAR_ = new_trace__21478__auto___22352);

} else {
}

(self__.fsm_state = new_fsm_state);

if(cljs.core.truth_(action_fn)){
return (action_fn.cljs$core$IFn$_invoke$arity$0 ? action_fn.cljs$core$IFn$_invoke$arity$0() : action_fn.call(null));
} else {
return null;
}
}finally {if(re_frame.trace.is_trace_enabled_QMARK_()){
var end__21475__auto___22355 = re_frame.interop.now();
var duration__21476__auto___22356 = (end__21475__auto___22355 - new cljs.core.Keyword(null,"start","start",-355208981).cljs$core$IFn$_invoke$arity$1(re_frame.trace._STAR_current_trace_STAR_));
var finished__21477__auto___22357 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"duration","duration",1444101068),duration__21476__auto___22356,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"end","end",-268185958),end__21475__auto___22355], 0));
if(re_frame.trace.validate_trace_enabled_QMARK_){
re_frame.trace.check_trace_against_schema(finished__21477__auto___22357);
} else {
}

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(re_frame.trace.traces,cljs.core.conj,finished__21477__auto___22357);

re_frame.trace.run_tracing_callbacks_BANG_(end__21475__auto___22355);
} else {
}
}}finally {(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__orig_val__22077);
}} else {
var vec__22109 = (function (){var G__22112 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [self__.fsm_state,trigger], null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"idle","idle",-2007156861),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),(function (){
this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);

return this$__$1.re_frame$router$IEventQueue$_run_next_tick$arity$1(null);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"exception","exception",-335277064)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"idle","idle",-2007156861),(function (){
return this$__$1.re_frame$router$IEventQueue$_exception$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"finish-run","finish-run",753148477)], null),G__22112)){
if(cljs.core.empty_QMARK_(self__.queue)){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"idle","idle",-2007156861)], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),(function (){
return this$__$1.re_frame$router$IEventQueue$_run_next_tick$arity$1(null);
})], null);
}
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"pause","pause",-2095325672)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),(function (){
return this$__$1.re_frame$router$IEventQueue$_pause$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),new cljs.core.Keyword(null,"resume","resume",-118572261)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),(function (){
return this$__$1.re_frame$router$IEventQueue$_resume$arity$1(null);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),new cljs.core.Keyword(null,"run-queue","run-queue",-1701798027)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),(function (){
return this$__$1.re_frame$router$IEventQueue$_run_queue$arity$1(null);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"paused","paused",-1710376127),(function (){
return this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"running","running",1554969103),(function (){
return this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);
})], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),new cljs.core.Keyword(null,"add-event","add-event",938429088)], null),G__22112)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"scheduled","scheduled",553898551),(function (){
return this$__$1.re_frame$router$IEventQueue$_add_event$arity$2(null,arg);
})], null);
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2((""+"re-frame: router state transition not found. "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(self__.fsm_state)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(trigger)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fsm-state","fsm-state",1656310533),self__.fsm_state,new cljs.core.Keyword(null,"trigger","trigger",103466139),trigger], null));

}
}
}
}
}
}
}
}
}
})();
var new_fsm_state = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22109,(0),null);
var action_fn = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22109,(1),null);
if(re_frame.trace.is_trace_enabled_QMARK_()){
var new_trace__21478__auto___22359 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$4(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"tags","tags",1771418977),cljs.core.merge,new cljs.core.Keyword(null,"tags","tags",1771418977).cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"operation","operation",-1267664310),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [self__.fsm_state,trigger], null),new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"current-state","current-state",1048284452),self__.fsm_state,new cljs.core.Keyword(null,"new-state","new-state",-490349212),new_fsm_state], null)], null))),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"operation","operation",-1267664310),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [self__.fsm_state,trigger], null),new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"current-state","current-state",1048284452),self__.fsm_state,new cljs.core.Keyword(null,"new-state","new-state",-490349212),new_fsm_state], null)], null),new cljs.core.Keyword(null,"tags","tags",1771418977))], 0));
(re_frame.trace._STAR_current_trace_STAR_ = new_trace__21478__auto___22359);

} else {
}

(self__.fsm_state = new_fsm_state);

if(cljs.core.truth_(action_fn)){
return (action_fn.cljs$core$IFn$_invoke$arity$0 ? action_fn.cljs$core$IFn$_invoke$arity$0() : action_fn.call(null));
} else {
return null;
}
}
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_pause$arity$2 = (function (this$,later_fn){
var self__ = this;
var this$__$1 = this;
var G__22128 = (function (){
return this$__$1.re_frame$router$IEventQueue$_fsm_trigger$arity$3(null,new cljs.core.Keyword(null,"resume","resume",-118572261),null);
});
return (later_fn.cljs$core$IFn$_invoke$arity$1 ? later_fn.cljs$core$IFn$_invoke$arity$1(G__22128) : later_fn.call(null,G__22128));
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$add_post_event_callback$arity$3 = (function (_,id,callback_fn){
var self__ = this;
var ___$1 = this;
if(cljs.core.contains_QMARK_(self__.post_event_callback_fns,id)){
re_frame.loggers.console.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"warn","warn",-436710552),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["re-frame: overwriting existing post event call back with id:",id], 0));
} else {
}

return (self__.post_event_callback_fns = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.post_event_callback_fns,id,callback_fn));
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_process_1st_event_in_queue$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
var event_v = cljs.core.peek(self__.queue);
try{var _STAR_handling_STAR__orig_val__22134_22362 = re_frame.events._STAR_handling_STAR_;
var _STAR_current_dispatch_id_STAR__orig_val__22135_22363 = re_frame.events._STAR_current_dispatch_id_STAR_;
var _STAR_on_dispatch_id_STAR__orig_val__22136_22364 = re_frame.events._STAR_on_dispatch_id_STAR_;
var _STAR_current_trace_STAR__orig_val__22137_22365 = re_frame.trace._STAR_current_trace_STAR_;
var _STAR_handling_STAR__temp_val__22138_22366 = null;
var _STAR_current_dispatch_id_STAR__temp_val__22139_22367 = null;
var _STAR_on_dispatch_id_STAR__temp_val__22140_22368 = null;
var _STAR_current_trace_STAR__temp_val__22141_22369 = null;
(re_frame.events._STAR_handling_STAR_ = _STAR_handling_STAR__temp_val__22138_22366);

(re_frame.events._STAR_current_dispatch_id_STAR_ = _STAR_current_dispatch_id_STAR__temp_val__22139_22367);

(re_frame.events._STAR_on_dispatch_id_STAR_ = _STAR_on_dispatch_id_STAR__temp_val__22140_22368);

(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__temp_val__22141_22369);

try{re_frame.events.handle(event_v);
}finally {(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__orig_val__22137_22365);

(re_frame.events._STAR_on_dispatch_id_STAR_ = _STAR_on_dispatch_id_STAR__orig_val__22136_22364);

(re_frame.events._STAR_current_dispatch_id_STAR_ = _STAR_current_dispatch_id_STAR__orig_val__22135_22363);

(re_frame.events._STAR_handling_STAR_ = _STAR_handling_STAR__orig_val__22134_22362);
}
(self__.queue = cljs.core.pop(self__.queue));

return this$__$1.re_frame$router$IEventQueue$_call_post_event_callbacks$arity$2(null,event_v);
}catch (e22132){var ex = e22132;
return this$__$1.re_frame$router$IEventQueue$_fsm_trigger$arity$3(null,new cljs.core.Keyword(null,"exception","exception",-335277064),ex);
}}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_call_post_event_callbacks$arity$2 = (function (_,event_v){
var self__ = this;
var ___$1 = this;
var seq__22142 = cljs.core.seq(cljs.core.vals(self__.post_event_callback_fns));
var chunk__22143 = null;
var count__22144 = (0);
var i__22145 = (0);
while(true){
if((i__22145 < count__22144)){
var callback = chunk__22143.cljs$core$IIndexed$_nth$arity$2(null,i__22145);
(callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(event_v,self__.queue) : callback.call(null,event_v,self__.queue));


var G__22371 = seq__22142;
var G__22372 = chunk__22143;
var G__22373 = count__22144;
var G__22374 = (i__22145 + (1));
seq__22142 = G__22371;
chunk__22143 = G__22372;
count__22144 = G__22373;
i__22145 = G__22374;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__22142);
if(temp__5825__auto__){
var seq__22142__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__22142__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__22142__$1);
var G__22377 = cljs.core.chunk_rest(seq__22142__$1);
var G__22378 = c__5673__auto__;
var G__22379 = cljs.core.count(c__5673__auto__);
var G__22380 = (0);
seq__22142 = G__22377;
chunk__22143 = G__22378;
count__22144 = G__22379;
i__22145 = G__22380;
continue;
} else {
var callback = cljs.core.first(seq__22142__$1);
(callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(event_v,self__.queue) : callback.call(null,event_v,self__.queue));


var G__22384 = cljs.core.next(seq__22142__$1);
var G__22385 = null;
var G__22386 = (0);
var G__22387 = (0);
seq__22142 = G__22384;
chunk__22143 = G__22385;
count__22144 = G__22386;
i__22145 = G__22387;
continue;
}
} else {
return null;
}
}
break;
}
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$purge$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return (self__.queue = re_frame.interop.empty_queue);
}));

(re_frame.router.EventQueue.prototype.re_frame$router$IEventQueue$_exception$arity$2 = (function (this$,ex){
var self__ = this;
var this$__$1 = this;
this$__$1.re_frame$router$IEventQueue$purge$arity$1(null);

throw ex;
}));

(re_frame.router.EventQueue.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.with_meta(new cljs.core.Symbol(null,"fsm-state","fsm-state",-998125236,null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"mutable","mutable",875778266),true], null)),cljs.core.with_meta(new cljs.core.Symbol(null,"queue","queue",-1198599890,null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"mutable","mutable",875778266),true], null)),cljs.core.with_meta(new cljs.core.Symbol(null,"post-event-callback-fns","post-event-callback-fns",-297038335,null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"mutable","mutable",875778266),true], null))], null);
}));

(re_frame.router.EventQueue.cljs$lang$type = true);

(re_frame.router.EventQueue.cljs$lang$ctorStr = "re-frame.router/EventQueue");

(re_frame.router.EventQueue.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"re-frame.router/EventQueue");
}));

/**
 * Positional factory function for re-frame.router/EventQueue.
 */
re_frame.router.__GT_EventQueue = (function re_frame$router$__GT_EventQueue(fsm_state,queue,post_event_callback_fns){
return (new re_frame.router.EventQueue(fsm_state,queue,post_event_callback_fns));
});

re_frame.router.event_queue = re_frame.router.__GT_EventQueue(new cljs.core.Keyword(null,"idle","idle",-2007156861),re_frame.interop.empty_queue,cljs.core.PersistentArrayMap.EMPTY);
re_frame.router.can_carry_meta_QMARK_ = (function re_frame$router$can_carry_meta_QMARK_(x){
if((!((x == null)))){
if((((x.cljs$lang$protocol_mask$partition0$ & (262144))) || ((cljs.core.PROTOCOL_SENTINEL === x.cljs$core$IWithMeta$)))){
return true;
} else {
if((!x.cljs$lang$protocol_mask$partition0$)){
return cljs.core.native_satisfies_QMARK_(cljs.core.IWithMeta,x);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(cljs.core.IWithMeta,x);
}
});
re_frame.router.inherit_event_meta = (function re_frame$router$inherit_event_meta(var_args){
var G__22155 = arguments.length;
switch (G__22155) {
case 3:
return re_frame.router.inherit_event_meta.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return re_frame.router.inherit_event_meta.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(re_frame.router.inherit_event_meta.cljs$core$IFn$_invoke$arity$3 = (function (event,k,v){
return re_frame.router.inherit_event_meta.cljs$core$IFn$_invoke$arity$4(event,k,v,true);
}));

(re_frame.router.inherit_event_meta.cljs$core$IFn$_invoke$arity$4 = (function (event,k,v,overwrite_QMARK_){
if(cljs.core.truth_((function (){var or__5142__auto__ = (v == null);
if(or__5142__auto__){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = (function (){var and__5140__auto__ = cljs.core.not(overwrite_QMARK_);
if(and__5140__auto__){
var G__22161 = event;
var G__22161__$1 = (((G__22161 == null))?null:cljs.core.meta(G__22161));
if((G__22161__$1 == null)){
return null;
} else {
return (k.cljs$core$IFn$_invoke$arity$1 ? k.cljs$core$IFn$_invoke$arity$1(G__22161__$1) : k.call(null,G__22161__$1));
}
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return (!(re_frame.router.can_carry_meta_QMARK_(event)));
}
}
})())){
return event;
} else {
return cljs.core.vary_meta.cljs$core$IFn$_invoke$arity$4(event,cljs.core.assoc,k,v);
}
}));

(re_frame.router.inherit_event_meta.cljs$lang$maxFixedArity = 4);

re_frame.router.tag_with_parent_dispatch_id = (function re_frame$router$tag_with_parent_dispatch_id(event){
return re_frame.router.inherit_event_meta.cljs$core$IFn$_invoke$arity$3(event,new cljs.core.Keyword("re-frame","parent-dispatch-id","re-frame/parent-dispatch-id",-525731802),re_frame.events._STAR_current_dispatch_id_STAR_);
});
re_frame.router.tag_with_fx_overrides = (function re_frame$router$tag_with_fx_overrides(event){
return re_frame.router.inherit_event_meta.cljs$core$IFn$_invoke$arity$4(event,new cljs.core.Keyword("re-frame","fx-overrides","re-frame/fx-overrides",1984520294),(function (){var G__22165 = re_frame.events._STAR_handling_STAR_;
var G__22165__$1 = (((G__22165 == null))?null:cljs.core.meta(G__22165));
if((G__22165__$1 == null)){
return null;
} else {
return new cljs.core.Keyword("re-frame","fx-overrides","re-frame/fx-overrides",1984520294).cljs$core$IFn$_invoke$arity$1(G__22165__$1);
}
})(),false);
});
re_frame.router.dispatch = (function re_frame$router$dispatch(event){
if((event == null)){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("re-frame: you called \"dispatch\" without an event vector.",cljs.core.PersistentArrayMap.EMPTY);
} else {
re_frame.router.event_queue.re_frame$router$IEventQueue$push$arity$2(null,(function (){var G__22169 = event;
var G__22169__$1 = (cljs.core.truth_(re_frame.events._STAR_current_dispatch_id_STAR_)?re_frame.router.tag_with_parent_dispatch_id(G__22169):G__22169);
if(cljs.core.truth_(re_frame.events._STAR_handling_STAR_)){
return re_frame.router.tag_with_fx_overrides(G__22169__$1);
} else {
return G__22169__$1;
}
})());
}

return null;
});
re_frame.router.dispatch_sync = (function re_frame$router$dispatch_sync(event_v){
re_frame.events.handle(event_v);

re_frame.router.event_queue.re_frame$router$IEventQueue$_call_post_event_callbacks$arity$2(null,event_v);

if(re_frame.trace.is_trace_enabled_QMARK_()){
var _STAR_current_trace_STAR__orig_val__22170_22402 = re_frame.trace._STAR_current_trace_STAR_;
var _STAR_current_trace_STAR__temp_val__22171_22403 = re_frame.trace.start_trace(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"op-type","op-type",-1636141668),new cljs.core.Keyword(null,"sync","sync",-624148946)], null));
(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__temp_val__22171_22403);

try{try{}finally {if(re_frame.trace.is_trace_enabled_QMARK_()){
var end__21475__auto___22405 = re_frame.interop.now();
var duration__21476__auto___22406 = (end__21475__auto___22405 - new cljs.core.Keyword(null,"start","start",-355208981).cljs$core$IFn$_invoke$arity$1(re_frame.trace._STAR_current_trace_STAR_));
var finished__21477__auto___22407 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(re_frame.trace._STAR_current_trace_STAR_,new cljs.core.Keyword(null,"duration","duration",1444101068),duration__21476__auto___22406,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"end","end",-268185958),end__21475__auto___22405], 0));
if(re_frame.trace.validate_trace_enabled_QMARK_){
re_frame.trace.check_trace_against_schema(finished__21477__auto___22407);
} else {
}

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(re_frame.trace.traces,cljs.core.conj,finished__21477__auto___22407);

re_frame.trace.run_tracing_callbacks_BANG_(end__21475__auto___22405);
} else {
}
}}finally {(re_frame.trace._STAR_current_trace_STAR_ = _STAR_current_trace_STAR__orig_val__22170_22402);
}} else {
}

return null;
});
re_frame.router.mk_deferred = (function re_frame$router$mk_deferred(){
var resolve_fn = cljs.core.volatile_BANG_(null);
var p = (new Promise((function (resolve,_reject){
return cljs.core.vreset_BANG_(resolve_fn,resolve);
})));
var done_QMARK_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),p,new cljs.core.Keyword(null,"resolve!","resolve!",500381887),(function (v){
if(cljs.core.compare_and_set_BANG_(done_QMARK_,false,true)){
var fexpr__22176 = cljs.core.deref(resolve_fn);
return (fexpr__22176.cljs$core$IFn$_invoke$arity$1 ? fexpr__22176.cljs$core$IFn$_invoke$arity$1(v) : fexpr__22176.call(null,v));
} else {
return null;
}
})], null);
});

/**
 * @interface
 */
re_frame.router.ICascadeTracker = function(){};

var re_frame$router$ICascadeTracker$_register_BANG_$dyn_22408 = (function (_,on_cascade){
var x__5498__auto__ = (((_ == null))?null:_);
var m__5499__auto__ = (re_frame.router._register_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(_,on_cascade) : m__5499__auto__.call(null,_,on_cascade));
} else {
var m__5497__auto__ = (re_frame.router._register_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(_,on_cascade) : m__5497__auto__.call(null,_,on_cascade));
} else {
throw cljs.core.missing_protocol("ICascadeTracker.-register!",_);
}
}
});
/**
 * Subscribe to event/epoch arrivals. Calls `on-cascade` once per
 *   delivery that contributed something to the cascade.
 */
re_frame.router._register_BANG_ = (function re_frame$router$_register_BANG_(_,on_cascade){
if((((!((_ == null)))) && ((!((_.re_frame$router$ICascadeTracker$_register_BANG_$arity$2 == null)))))){
return _.re_frame$router$ICascadeTracker$_register_BANG_$arity$2(_,on_cascade);
} else {
return re_frame$router$ICascadeTracker$_register_BANG_$dyn_22408(_,on_cascade);
}
});

var re_frame$router$ICascadeTracker$_unregister_BANG_$dyn_22411 = (function (_){
var x__5498__auto__ = (((_ == null))?null:_);
var m__5499__auto__ = (re_frame.router._unregister_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5499__auto__.call(null,_));
} else {
var m__5497__auto__ = (re_frame.router._unregister_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5497__auto__.call(null,_));
} else {
throw cljs.core.missing_protocol("ICascadeTracker.-unregister!",_);
}
}
});
/**
 * Tear down the subscription and reset internal collections.
 */
re_frame.router._unregister_BANG_ = (function re_frame$router$_unregister_BANG_(_){
if((((!((_ == null)))) && ((!((_.re_frame$router$ICascadeTracker$_unregister_BANG_$arity$1 == null)))))){
return _.re_frame$router$ICascadeTracker$_unregister_BANG_$arity$1(_);
} else {
return re_frame$router$ICascadeTracker$_unregister_BANG_$dyn_22411(_);
}
});

var re_frame$router$ICascadeTracker$_after_root_dispatched_BANG_$dyn_22412 = (function (_){
var x__5498__auto__ = (((_ == null))?null:_);
var m__5499__auto__ = (re_frame.router._after_root_dispatched_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5499__auto__.call(null,_));
} else {
var m__5497__auto__ = (re_frame.router._after_root_dispatched_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5497__auto__.call(null,_));
} else {
throw cljs.core.missing_protocol("ICascadeTracker.-after-root-dispatched!",_);
}
}
});
/**
 * Hook fired after `dispatch-sync` returns. Trackers that key off
 *   a captured root-id seed their bookkeeping here.
 */
re_frame.router._after_root_dispatched_BANG_ = (function re_frame$router$_after_root_dispatched_BANG_(_){
if((((!((_ == null)))) && ((!((_.re_frame$router$ICascadeTracker$_after_root_dispatched_BANG_$arity$1 == null)))))){
return _.re_frame$router$ICascadeTracker$_after_root_dispatched_BANG_$arity$1(_);
} else {
return re_frame$router$ICascadeTracker$_after_root_dispatched_BANG_$dyn_22412(_);
}
});

var re_frame$router$ICascadeTracker$_result$dyn_22413 = (function (_,include_cascaded_QMARK_){
var x__5498__auto__ = (((_ == null))?null:_);
var m__5499__auto__ = (re_frame.router._result[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(_,include_cascaded_QMARK_) : m__5499__auto__.call(null,_,include_cascaded_QMARK_));
} else {
var m__5497__auto__ = (re_frame.router._result["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(_,include_cascaded_QMARK_) : m__5497__auto__.call(null,_,include_cascaded_QMARK_));
} else {
throw cljs.core.missing_protocol("ICascadeTracker.-result",_);
}
}
});
/**
 * Build the success result map, or nil if the cascade hasn't started
 *   yet (root not seen). Honours `include-cascaded?`.
 */
re_frame.router._result = (function re_frame$router$_result(_,include_cascaded_QMARK_){
if((((!((_ == null)))) && ((!((_.re_frame$router$ICascadeTracker$_result$arity$2 == null)))))){
return _.re_frame$router$ICascadeTracker$_result$arity$2(_,include_cascaded_QMARK_);
} else {
return re_frame$router$ICascadeTracker$_result$dyn_22413(_,include_cascaded_QMARK_);
}
});

var re_frame$router$ICascadeTracker$_captured$dyn_22414 = (function (_){
var x__5498__auto__ = (((_ == null))?null:_);
var m__5499__auto__ = (re_frame.router._captured[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5499__auto__.call(null,_));
} else {
var m__5497__auto__ = (re_frame.router._captured["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5497__auto__.call(null,_));
} else {
throw cljs.core.missing_protocol("ICascadeTracker.-captured",_);
}
}
});
/**
 * Snapshot of collected records for the timeout result's
 *   `:captured-epochs` key.
 */
re_frame.router._captured = (function re_frame$router$_captured(_){
if((((!((_ == null)))) && ((!((_.re_frame$router$ICascadeTracker$_captured$arity$1 == null)))))){
return _.re_frame$router$ICascadeTracker$_captured$arity$1(_);
} else {
return re_frame$router$ICascadeTracker$_captured$dyn_22414(_);
}
});

/**
 * Return event vectors synchronously queued by re-frame's built-in
 * dispatch effects. `:dispatch-later` is intentionally excluded: it is
 * timer-driven async work, not part of dispatch-and-settle's synchronous
 * cascade contract.
 */
re_frame.router.immediate_dispatch_events = (function re_frame$router$immediate_dispatch_events(effects){
var event_vectors = (function re_frame$router$immediate_dispatch_events_$_event_vectors(effect_key,effect_value){
var G__22197 = effect_key;
var G__22197__$1 = (((G__22197 instanceof cljs.core.Keyword))?G__22197.fqn:null);
switch (G__22197__$1) {
case "dispatch":
if(cljs.core.vector_QMARK_(effect_value)){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [effect_value], null);
} else {
return cljs.core.PersistentVector.EMPTY;
}

break;
case "dispatch-n":
if(cljs.core.sequential_QMARK_(effect_value)){
return cljs.core.filterv(cljs.core.vector_QMARK_,cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,effect_value));
} else {
return cljs.core.PersistentVector.EMPTY;
}

break;
case "fx":
if(cljs.core.sequential_QMARK_(effect_value)){
return cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p__22198){
var vec__22199 = p__22198;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22199,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22199,(1),null);
return re_frame$router$immediate_dispatch_events_$_event_vectors(k,v);
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,effect_value)], 0));
} else {
return cljs.core.PersistentVector.EMPTY;
}

break;
default:
return cljs.core.PersistentVector.EMPTY;

}
});
return cljs.core.vec(cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p__22202){
var vec__22203 = p__22202;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22203,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22203,(1),null);
return event_vectors(k,v);
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([effects], 0)));
});
re_frame.router.decrement_pending_child = (function re_frame$router$decrement_pending_child(pending,parent_id){
if(cljs.core.truth_(parent_id)){
var n = cljs.core.get.cljs$core$IFn$_invoke$arity$3(pending,parent_id,(0));
if((n > (1))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(pending,parent_id,(n - (1)));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(n,(1))){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(pending,parent_id);
} else {
return pending;

}
}
} else {
return pending;
}
});
re_frame.router.trace_tracker_initial_state = (function re_frame$router$trace_tracker_initial_state(){
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"cascade-ids","cascade-ids",-212936960),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"seen-ids","seen-ids",-1177312243),cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword(null,"cascade-epochs","cascade-epochs",2068026941),cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword(null,"pending-children","pending-children",469249061),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"unmatched-epochs","unmatched-epochs",661963895),cljs.core.PersistentVector.EMPTY], null);
});
re_frame.router.trace_epoch_accepted_QMARK_ = (function re_frame$router$trace_epoch_accepted_QMARK_(root_id,state,epoch){
var id = new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678).cljs$core$IFn$_invoke$arity$1(epoch);
var parent_id = new cljs.core.Keyword(null,"parent-dispatch-id","parent-dispatch-id",1812056977).cljs$core$IFn$_invoke$arity$1(epoch);
return (((!(cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"seen-ids","seen-ids",-1177312243).cljs$core$IFn$_invoke$arity$1(state),id)))) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(root_id,id)) || (cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"cascade-ids","cascade-ids",-212936960).cljs$core$IFn$_invoke$arity$1(state),parent_id)))));
});
re_frame.router.accept_trace_epoch = (function re_frame$router$accept_trace_epoch(state,epoch){
var id = new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678).cljs$core$IFn$_invoke$arity$1(epoch);
var parent_id = new cljs.core.Keyword(null,"parent-dispatch-id","parent-dispatch-id",1812056977).cljs$core$IFn$_invoke$arity$1(epoch);
var expected = cljs.core.count(re_frame.router.immediate_dispatch_events(new cljs.core.Keyword(null,"effects","effects",-282369292).cljs$core$IFn$_invoke$arity$1(epoch)));
var G__22223 = cljs.core.update.cljs$core$IFn$_invoke$arity$4(cljs.core.update.cljs$core$IFn$_invoke$arity$4(cljs.core.update.cljs$core$IFn$_invoke$arity$4(state,new cljs.core.Keyword(null,"seen-ids","seen-ids",-1177312243),cljs.core.conj,id),new cljs.core.Keyword(null,"pending-children","pending-children",469249061),re_frame.router.decrement_pending_child,parent_id),new cljs.core.Keyword(null,"cascade-epochs","cascade-epochs",2068026941),cljs.core.conj,epoch);
var G__22223__$1 = (((expected > (0)))?cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(G__22223,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"pending-children","pending-children",469249061),id], null),cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core._PLUS_,(0)),expected):G__22223);
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__22223__$1,new cljs.core.Keyword(null,"cascade-ids","cascade-ids",-212936960),cljs.core.conj,id);

});
re_frame.router.process_trace_epochs = (function re_frame$router$process_trace_epochs(root_id,changed_QMARK_,state,epochs){
var state__$1 = cljs.core.update.cljs$core$IFn$_invoke$arity$4(state,new cljs.core.Keyword(null,"unmatched-epochs","unmatched-epochs",661963895),cljs.core.into,epochs);
while(true){
var map__22233 = cljs.core.group_by(((function (state__$1){
return (function (p1__22228_SHARP_){
return re_frame.router.trace_epoch_accepted_QMARK_(root_id,state__$1,p1__22228_SHARP_);
});})(state__$1))
,new cljs.core.Keyword(null,"unmatched-epochs","unmatched-epochs",661963895).cljs$core$IFn$_invoke$arity$1(state__$1));
var map__22233__$1 = cljs.core.__destructure_map(map__22233);
var accepted = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22233__$1,true);
var pending = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22233__$1,false);
if(cljs.core.seq(accepted)){
cljs.core.reset_BANG_(changed_QMARK_,true);

var G__22417 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(re_frame.router.accept_trace_epoch,state__$1,accepted),new cljs.core.Keyword(null,"unmatched-epochs","unmatched-epochs",661963895),cljs.core.vec(pending));
state__$1 = G__22417;
continue;
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(state__$1,new cljs.core.Keyword(null,"unmatched-epochs","unmatched-epochs",661963895),cljs.core.vec(pending));
}
break;
}
});

/**
* @constructor
 * @implements {re_frame.router.ICascadeTracker}
*/
re_frame.router.TraceTracker = (function (cb_key,root_id,state){
this.cb_key = cb_key;
this.root_id = root_id;
this.state = state;
});
(re_frame.router.TraceTracker.prototype.re_frame$router$ICascadeTracker$ = cljs.core.PROTOCOL_SENTINEL);

(re_frame.router.TraceTracker.prototype.re_frame$router$ICascadeTracker$_register_BANG_$arity$2 = (function (_,on_cascade){
var self__ = this;
var ___$1 = this;
return re_frame.trace.register_epoch_cb(self__.cb_key,(function (epochs){
var changed_QMARK_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(self__.state,(function (p1__22234_SHARP_){
return re_frame.router.process_trace_epochs(cljs.core.deref(self__.root_id),changed_QMARK_,p1__22234_SHARP_,epochs);
}));

if(cljs.core.truth_(cljs.core.deref(changed_QMARK_))){
return (on_cascade.cljs$core$IFn$_invoke$arity$0 ? on_cascade.cljs$core$IFn$_invoke$arity$0() : on_cascade.call(null));
} else {
return null;
}
}));
}));

(re_frame.router.TraceTracker.prototype.re_frame$router$ICascadeTracker$_unregister_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
re_frame.trace.remove_epoch_cb(self__.cb_key);

cljs.core.reset_BANG_(self__.state,re_frame.router.trace_tracker_initial_state());

return cljs.core.reset_BANG_(self__.root_id,null);
}));

(re_frame.router.TraceTracker.prototype.re_frame$router$ICascadeTracker$_after_root_dispatched_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
var temp__5825__auto__ = cljs.core.deref(self__.root_id);
if(cljs.core.truth_(temp__5825__auto__)){
var id = temp__5825__auto__;
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(self__.state,cljs.core.update,new cljs.core.Keyword(null,"cascade-ids","cascade-ids",-212936960),cljs.core.conj,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([id], 0));
} else {
return null;
}
}));

(re_frame.router.TraceTracker.prototype.re_frame$router$ICascadeTracker$_result$arity$2 = (function (_,include_cascaded_QMARK_){
var self__ = this;
var ___$1 = this;
if((!((cljs.core.deref(self__.root_id) == null)))){
var map__22252 = cljs.core.deref(self__.state);
var map__22252__$1 = cljs.core.__destructure_map(map__22252);
var cascade_epochs = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22252__$1,new cljs.core.Keyword(null,"cascade-epochs","cascade-epochs",2068026941));
var pending_children = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22252__$1,new cljs.core.Keyword(null,"pending-children","pending-children",469249061));
var eps = cascade_epochs;
var root_ep = cljs.core.some((function (p1__22237_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.root_id),new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678).cljs$core$IFn$_invoke$arity$1(p1__22237_SHARP_))){
return p1__22237_SHARP_;
} else {
return null;
}
}),eps);
var cascaded = cljs.core.filterv((function (p1__22238_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.root_id),new cljs.core.Keyword(null,"dispatch-id","dispatch-id",1118805678).cljs$core$IFn$_invoke$arity$1(p1__22238_SHARP_));
}),eps);
if(cljs.core.truth_((function (){var and__5140__auto__ = root_ep;
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.empty_QMARK_(pending_children);
} else {
return and__5140__auto__;
}
})())){
var G__22255 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ok?","ok?",447310304),true,new cljs.core.Keyword(null,"root-epoch","root-epoch",-2136185377),root_ep], null);
if(cljs.core.truth_(include_cascaded_QMARK_)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__22255,new cljs.core.Keyword(null,"cascaded-epochs","cascaded-epochs",-1926999754),cascaded);
} else {
return G__22255;
}
} else {
return null;
}
} else {
return null;
}
}));

(re_frame.router.TraceTracker.prototype.re_frame$router$ICascadeTracker$_captured$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return new cljs.core.Keyword(null,"cascade-epochs","cascade-epochs",2068026941).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(self__.state));
}));

(re_frame.router.TraceTracker.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"cb-key","cb-key",2009412887,null),new cljs.core.Symbol(null,"root-id","root-id",-1359751960,null),new cljs.core.Symbol(null,"state","state",-348086572,null)], null);
}));

(re_frame.router.TraceTracker.cljs$lang$type = true);

(re_frame.router.TraceTracker.cljs$lang$ctorStr = "re-frame.router/TraceTracker");

(re_frame.router.TraceTracker.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"re-frame.router/TraceTracker");
}));

/**
 * Positional factory function for re-frame.router/TraceTracker.
 */
re_frame.router.__GT_TraceTracker = (function re_frame$router$__GT_TraceTracker(cb_key,root_id,state){
return (new re_frame.router.TraceTracker(cb_key,root_id,state));
});


/**
* @constructor
 * @implements {re_frame.router.ICascadeTracker}
*/
re_frame.router.PostEventTracker = (function (post_cb_key,cascade_events){
this.post_cb_key = post_cb_key;
this.cascade_events = cascade_events;
});
(re_frame.router.PostEventTracker.prototype.re_frame$router$ICascadeTracker$ = cljs.core.PROTOCOL_SENTINEL);

(re_frame.router.PostEventTracker.prototype.re_frame$router$ICascadeTracker$_register_BANG_$arity$2 = (function (_,on_cascade){
var self__ = this;
var ___$1 = this;
return re_frame.router.event_queue.re_frame$router$IEventQueue$add_post_event_callback$arity$3(null,self__.post_cb_key,(function (event_v,_queue){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cascade_events,cljs.core.conj,event_v);

return (on_cascade.cljs$core$IFn$_invoke$arity$0 ? on_cascade.cljs$core$IFn$_invoke$arity$0() : on_cascade.call(null));
}));
}));

(re_frame.router.PostEventTracker.prototype.re_frame$router$ICascadeTracker$_unregister_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
re_frame.router.event_queue.re_frame$router$IEventQueue$remove_post_event_callback$arity$2(null,self__.post_cb_key);

return cljs.core.reset_BANG_(self__.cascade_events,cljs.core.PersistentVector.EMPTY);
}));

(re_frame.router.PostEventTracker.prototype.re_frame$router$ICascadeTracker$_after_root_dispatched_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return null;
}));

(re_frame.router.PostEventTracker.prototype.re_frame$router$ICascadeTracker$_result$arity$2 = (function (_,include_cascaded_QMARK_){
var self__ = this;
var ___$1 = this;
var evs = cljs.core.deref(self__.cascade_events);
if(cljs.core.seq(evs)){
var G__22267 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ok?","ok?",447310304),true,new cljs.core.Keyword(null,"root-epoch","root-epoch",-2136185377),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"event","event",301435442),cljs.core.first(evs)], null)], null);
if(cljs.core.truth_(include_cascaded_QMARK_)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__22267,new cljs.core.Keyword(null,"cascaded-epochs","cascaded-epochs",-1926999754),cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (ev){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"event","event",301435442),ev], null);
}),cljs.core.rest(evs)));
} else {
return G__22267;
}
} else {
return null;
}
}));

(re_frame.router.PostEventTracker.prototype.re_frame$router$ICascadeTracker$_captured$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.PersistentVector.EMPTY;
}));

(re_frame.router.PostEventTracker.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"post-cb-key","post-cb-key",1594213235,null),new cljs.core.Symbol(null,"cascade-events","cascade-events",-841855849,null)], null);
}));

(re_frame.router.PostEventTracker.cljs$lang$type = true);

(re_frame.router.PostEventTracker.cljs$lang$ctorStr = "re-frame.router/PostEventTracker");

(re_frame.router.PostEventTracker.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"re-frame.router/PostEventTracker");
}));

/**
 * Positional factory function for re-frame.router/PostEventTracker.
 */
re_frame.router.__GT_PostEventTracker = (function re_frame$router$__GT_PostEventTracker(post_cb_key,cascade_events){
return (new re_frame.router.PostEventTracker(post_cb_key,cascade_events));
});

re_frame.router.mk_tracker = (function re_frame$router$mk_tracker(root_id){
if(re_frame.trace.is_trace_enabled_QMARK_()){
return re_frame.router.__GT_TraceTracker(re_frame.interop.new_uuid(),root_id,cljs.core.atom.cljs$core$IFn$_invoke$arity$1(re_frame.router.trace_tracker_initial_state()));
} else {
return re_frame.router.__GT_PostEventTracker(re_frame.interop.new_uuid(),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY));
}
});
/**
 * Dispatch `event` synchronously, then await the cascade of
 * `:fx [:dispatch ...]` children. See the long comment above for
 * shape, semantics, and limitations.
 */
re_frame.router.dispatch_and_settle = (function re_frame$router$dispatch_and_settle(var_args){
var G__22277 = arguments.length;
switch (G__22277) {
case 1:
return re_frame.router.dispatch_and_settle.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return re_frame.router.dispatch_and_settle.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(re_frame.router.dispatch_and_settle.cljs$core$IFn$_invoke$arity$1 = (function (event){
return re_frame.router.dispatch_and_settle.cljs$core$IFn$_invoke$arity$2(event,cljs.core.PersistentArrayMap.EMPTY);
}));

(re_frame.router.dispatch_and_settle.cljs$core$IFn$_invoke$arity$2 = (function (event,opts){
var opts__$1 = (function (){var or__5142__auto__ = opts;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.PersistentArrayMap.EMPTY;
}
})();
var map__22284 = opts__$1;
var map__22284__$1 = cljs.core.__destructure_map(map__22284);
var timeout_ms = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22284__$1,new cljs.core.Keyword(null,"timeout-ms","timeout-ms",754221406),(5000));
var settle_window_ms = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22284__$1,new cljs.core.Keyword(null,"settle-window-ms","settle-window-ms",-1199809943),(100));
var include_cascaded_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22284__$1,new cljs.core.Keyword(null,"include-cascaded?","include-cascaded?",1266375815),true);
var dispatch_event = ((cljs.core.contains_QMARK_(opts__$1,new cljs.core.Keyword(null,"overrides","overrides",1738628867)))?cljs.core.vary_meta.cljs$core$IFn$_invoke$arity$4(event,cljs.core.assoc,new cljs.core.Keyword("re-frame","fx-overrides","re-frame/fx-overrides",1984520294),new cljs.core.Keyword(null,"overrides","overrides",1738628867).cljs$core$IFn$_invoke$arity$1(opts__$1)):event);
var map__22285 = re_frame.router.mk_deferred();
var map__22285__$1 = cljs.core.__destructure_map(map__22285);
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22285__$1,new cljs.core.Keyword(null,"value","value",305978217));
var resolve_BANG_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22285__$1,new cljs.core.Keyword(null,"resolve!","resolve!",500381887));
var root_id = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var tracker = re_frame.router.mk_tracker(root_id);
var settle_tick = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var cleaned_QMARK_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
var overall_timer = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var settle_timers = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
var finish_BANG_ = (function (result){
if(cljs.core.compare_and_set_BANG_(cleaned_QMARK_,false,true)){
re_frame.router._unregister_BANG_(tracker);

re_frame.interop.clear_timeout_BANG_(cljs.core.deref(overall_timer));

var seq__22293_22444 = cljs.core.seq(cljs.core.deref(settle_timers));
var chunk__22294_22445 = null;
var count__22295_22446 = (0);
var i__22296_22447 = (0);
while(true){
if((i__22296_22447 < count__22295_22446)){
var h_22448 = chunk__22294_22445.cljs$core$IIndexed$_nth$arity$2(null,i__22296_22447);
re_frame.interop.clear_timeout_BANG_(h_22448);


var G__22449 = seq__22293_22444;
var G__22450 = chunk__22294_22445;
var G__22451 = count__22295_22446;
var G__22452 = (i__22296_22447 + (1));
seq__22293_22444 = G__22449;
chunk__22294_22445 = G__22450;
count__22295_22446 = G__22451;
i__22296_22447 = G__22452;
continue;
} else {
var temp__5825__auto___22453 = cljs.core.seq(seq__22293_22444);
if(temp__5825__auto___22453){
var seq__22293_22455__$1 = temp__5825__auto___22453;
if(cljs.core.chunked_seq_QMARK_(seq__22293_22455__$1)){
var c__5673__auto___22458 = cljs.core.chunk_first(seq__22293_22455__$1);
var G__22459 = cljs.core.chunk_rest(seq__22293_22455__$1);
var G__22460 = c__5673__auto___22458;
var G__22461 = cljs.core.count(c__5673__auto___22458);
var G__22462 = (0);
seq__22293_22444 = G__22459;
chunk__22294_22445 = G__22460;
count__22295_22446 = G__22461;
i__22296_22447 = G__22462;
continue;
} else {
var h_22463 = cljs.core.first(seq__22293_22455__$1);
re_frame.interop.clear_timeout_BANG_(h_22463);


var G__22464 = cljs.core.next(seq__22293_22455__$1);
var G__22465 = null;
var G__22466 = (0);
var G__22467 = (0);
seq__22293_22444 = G__22464;
chunk__22294_22445 = G__22465;
count__22295_22446 = G__22466;
i__22296_22447 = G__22467;
continue;
}
} else {
}
}
break;
}

cljs.core.reset_BANG_(settle_timers,cljs.core.PersistentVector.EMPTY);
} else {
}

return (resolve_BANG_.cljs$core$IFn$_invoke$arity$1 ? resolve_BANG_.cljs$core$IFn$_invoke$arity$1(result) : resolve_BANG_.call(null,result));
});
var schedule_settle_check = (function re_frame$router$schedule_settle_check(){
var tick = cljs.core.deref(settle_tick);
var handle = re_frame.interop.set_timeout_BANG_((function (){
if(((cljs.core.not(cljs.core.deref(cleaned_QMARK_))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(tick,cljs.core.deref(settle_tick))))){
var temp__5825__auto__ = re_frame.router._result(tracker,include_cascaded_QMARK_);
if(cljs.core.truth_(temp__5825__auto__)){
var result = temp__5825__auto__;
return finish_BANG_(result);
} else {
return null;
}
} else {
return null;
}
}),settle_window_ms);
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(settle_timers,cljs.core.conj,handle);
});
re_frame.router._register_BANG_(tracker,(function (){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(settle_tick,cljs.core.inc);

return schedule_settle_check();
}));

cljs.core.reset_BANG_(overall_timer,re_frame.interop.set_timeout_BANG_((function (){
return finish_BANG_(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"ok?","ok?",447310304),false,new cljs.core.Keyword(null,"reason","reason",-2070751759),new cljs.core.Keyword(null,"timeout","timeout",-318625318),new cljs.core.Keyword(null,"event","event",301435442),event,new cljs.core.Keyword(null,"captured-epochs","captured-epochs",-610618893),re_frame.router._captured(tracker)], null));
}),timeout_ms));

var _STAR_on_dispatch_id_STAR__orig_val__22307_22481 = re_frame.events._STAR_on_dispatch_id_STAR_;
var _STAR_on_dispatch_id_STAR__temp_val__22308_22482 = (function (p1__22269_SHARP_){
return cljs.core.reset_BANG_(root_id,p1__22269_SHARP_);
});
(re_frame.events._STAR_on_dispatch_id_STAR_ = _STAR_on_dispatch_id_STAR__temp_val__22308_22482);

try{re_frame.router.dispatch_sync(dispatch_event);
}finally {(re_frame.events._STAR_on_dispatch_id_STAR_ = _STAR_on_dispatch_id_STAR__orig_val__22307_22481);
}
re_frame.router._after_root_dispatched_BANG_(tracker);

schedule_settle_check();

return value;
}));

(re_frame.router.dispatch_and_settle.cljs$lang$maxFixedArity = 2);


//# sourceMappingURL=re_frame.router.js.map
