goog.provide('cljs.core.async');
goog.scope(function(){
  cljs.core.async.goog$module$goog$array = goog.module.get('goog.array');
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async46082 = (function (f,blockable,meta46083){
this.f = f;
this.blockable = blockable;
this.meta46083 = meta46083;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async46082.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_46084,meta46083__$1){
var self__ = this;
var _46084__$1 = this;
return (new cljs.core.async.t_cljs$core$async46082(self__.f,self__.blockable,meta46083__$1));
}));

(cljs.core.async.t_cljs$core$async46082.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_46084){
var self__ = this;
var _46084__$1 = this;
return self__.meta46083;
}));

(cljs.core.async.t_cljs$core$async46082.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async46082.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async46082.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.blockable;
}));

(cljs.core.async.t_cljs$core$async46082.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.f;
}));

(cljs.core.async.t_cljs$core$async46082.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"blockable","blockable",-28395259,null),new cljs.core.Symbol(null,"meta46083","meta46083",843584581,null)], null);
}));

(cljs.core.async.t_cljs$core$async46082.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async46082.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async46082");

(cljs.core.async.t_cljs$core$async46082.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async46082");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async46082.
 */
cljs.core.async.__GT_t_cljs$core$async46082 = (function cljs$core$async$__GT_t_cljs$core$async46082(f,blockable,meta46083){
return (new cljs.core.async.t_cljs$core$async46082(f,blockable,meta46083));
});


cljs.core.async.fn_handler = (function cljs$core$async$fn_handler(var_args){
var G__46074 = arguments.length;
switch (G__46074) {
case 1:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1 = (function (f){
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(f,true);
}));

(cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2 = (function (f,blockable){
return (new cljs.core.async.t_cljs$core$async46082(f,blockable,cljs.core.PersistentArrayMap.EMPTY));
}));

(cljs.core.async.fn_handler.cljs$lang$maxFixedArity = 2);

/**
 * Returns a fixed buffer of size n. When full, puts will block/park.
 */
cljs.core.async.buffer = (function cljs$core$async$buffer(n){
return cljs.core.async.impl.buffers.fixed_buffer(n);
});
/**
 * Returns a buffer of size n. When full, puts will complete but
 *   val will be dropped (no transfer).
 */
cljs.core.async.dropping_buffer = (function cljs$core$async$dropping_buffer(n){
return cljs.core.async.impl.buffers.dropping_buffer(n);
});
/**
 * Returns a buffer of size n. When full, puts will complete, and be
 *   buffered, but oldest elements in buffer will be dropped (not
 *   transferred).
 */
cljs.core.async.sliding_buffer = (function cljs$core$async$sliding_buffer(n){
return cljs.core.async.impl.buffers.sliding_buffer(n);
});
/**
 * Returns true if a channel created with buff will never block. That is to say,
 * puts into this buffer will never cause the buffer to be full. 
 */
cljs.core.async.unblocking_buffer_QMARK_ = (function cljs$core$async$unblocking_buffer_QMARK_(buff){
if((!((buff == null)))){
if(((false) || ((cljs.core.PROTOCOL_SENTINEL === buff.cljs$core$async$impl$protocols$UnblockingBuffer$)))){
return true;
} else {
if((!buff.cljs$lang$protocol_mask$partition$)){
return cljs.core.native_satisfies_QMARK_(cljs.core.async.impl.protocols.UnblockingBuffer,buff);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(cljs.core.async.impl.protocols.UnblockingBuffer,buff);
}
});
/**
 * Creates a channel with an optional buffer, an optional transducer (like (map f),
 *   (filter p) etc or a composition thereof), and an optional exception handler.
 *   If buf-or-n is a number, will create and use a fixed buffer of that size. If a
 *   transducer is supplied a buffer must be specified. ex-handler must be a
 *   fn of one argument - if an exception occurs during transformation it will be called
 *   with the thrown value as an argument, and any non-nil return value will be placed
 *   in the channel.
 */
cljs.core.async.chan = (function cljs$core$async$chan(var_args){
var G__46106 = arguments.length;
switch (G__46106) {
case 0:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1 = (function (buf_or_n){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(buf_or_n,null,null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2 = (function (buf_or_n,xform){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(buf_or_n,xform,null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3 = (function (buf_or_n,xform,ex_handler){
var buf_or_n__$1 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(buf_or_n,(0)))?null:buf_or_n);
if(cljs.core.truth_(xform)){
if(cljs.core.truth_(buf_or_n__$1)){
} else {
throw (new Error((""+"Assert failed: "+"buffer must be supplied when transducer is"+"\n"+"buf-or-n")));
}
} else {
}

return cljs.core.async.impl.channels.chan.cljs$core$IFn$_invoke$arity$3(((typeof buf_or_n__$1 === 'number')?cljs.core.async.buffer(buf_or_n__$1):buf_or_n__$1),xform,ex_handler);
}));

(cljs.core.async.chan.cljs$lang$maxFixedArity = 3);

/**
 * Creates a promise channel with an optional transducer, and an optional
 *   exception-handler. A promise channel can take exactly one value that consumers
 *   will receive. Once full, puts complete but val is dropped (no transfer).
 *   Consumers will block until either a value is placed in the channel or the
 *   channel is closed, then return the value (or nil) forever. See chan for the
 *   semantics of xform and ex-handler.
 */
cljs.core.async.promise_chan = (function cljs$core$async$promise_chan(var_args){
var G__46115 = arguments.length;
switch (G__46115) {
case 0:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1(null);
}));

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1 = (function (xform){
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2(xform,null);
}));

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2 = (function (xform,ex_handler){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(cljs.core.async.impl.buffers.promise_buffer(),xform,ex_handler);
}));

(cljs.core.async.promise_chan.cljs$lang$maxFixedArity = 2);

/**
 * Returns a channel that will close after msecs
 */
cljs.core.async.timeout = (function cljs$core$async$timeout(msecs){
return cljs.core.async.impl.timers.timeout(msecs);
});
/**
 * takes a val from port. Must be called inside a (go ...) block. Will
 *   return nil if closed. Will park if nothing is available.
 *   Returns true unless port is already closed
 */
cljs.core.async._LT__BANG_ = (function cljs$core$async$_LT__BANG_(port){
throw (new Error("<! used not in (go ...) block"));
});
/**
 * Asynchronously takes a val from port, passing to fn1. Will pass nil
 * if closed. If on-caller? (default true) is true, and value is
 * immediately available, will call fn1 on calling thread.
 * Returns nil.
 */
cljs.core.async.take_BANG_ = (function cljs$core$async$take_BANG_(var_args){
var G__46124 = arguments.length;
switch (G__46124) {
case 2:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,fn1){
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3(port,fn1,true);
}));

(cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,fn1,on_caller_QMARK_){
var ret = cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(fn1));
if(cljs.core.truth_(ret)){
var val_49357 = cljs.core.deref(ret);
if(cljs.core.truth_(on_caller_QMARK_)){
(fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(val_49357) : fn1.call(null,val_49357));
} else {
cljs.core.async.impl.dispatch.run((function (){
return (fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(val_49357) : fn1.call(null,val_49357));
}));
}
} else {
}

return null;
}));

(cljs.core.async.take_BANG_.cljs$lang$maxFixedArity = 3);

cljs.core.async.nop = (function cljs$core$async$nop(_){
return null;
});
cljs.core.async.fhnop = cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(cljs.core.async.nop);
/**
 * puts a val into port. nil values are not allowed. Must be called
 *   inside a (go ...) block. Will park if no buffer space is available.
 *   Returns true unless port is already closed.
 */
cljs.core.async._GT__BANG_ = (function cljs$core$async$_GT__BANG_(port,val){
throw (new Error(">! used not in (go ...) block"));
});
/**
 * Asynchronously puts a val into port, calling fn1 (if supplied) when
 * complete. nil values are not allowed. Will throw if closed. If
 * on-caller? (default true) is true, and the put is immediately
 * accepted, will call fn1 on calling thread.  Returns nil.
 */
cljs.core.async.put_BANG_ = (function cljs$core$async$put_BANG_(var_args){
var G__46131 = arguments.length;
switch (G__46131) {
case 2:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,val){
var temp__5823__auto__ = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fhnop);
if(cljs.core.truth_(temp__5823__auto__)){
var ret = temp__5823__auto__;
return cljs.core.deref(ret);
} else {
return true;
}
}));

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,val,fn1){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4(port,val,fn1,true);
}));

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4 = (function (port,val,fn1,on_caller_QMARK_){
var temp__5823__auto__ = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(fn1));
if(cljs.core.truth_(temp__5823__auto__)){
var retb = temp__5823__auto__;
var ret = cljs.core.deref(retb);
if(cljs.core.truth_(on_caller_QMARK_)){
(fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(ret) : fn1.call(null,ret));
} else {
cljs.core.async.impl.dispatch.run((function (){
return (fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(ret) : fn1.call(null,ret));
}));
}

return ret;
} else {
return true;
}
}));

(cljs.core.async.put_BANG_.cljs$lang$maxFixedArity = 4);

cljs.core.async.close_BANG_ = (function cljs$core$async$close_BANG_(port){
return cljs.core.async.impl.protocols.close_BANG_(port);
});
cljs.core.async.random_array = (function cljs$core$async$random_array(n){
var a = (new Array(n));
var n__5741__auto___49360 = n;
var x_49361 = (0);
while(true){
if((x_49361 < n__5741__auto___49360)){
(a[x_49361] = x_49361);

var G__49362 = (x_49361 + (1));
x_49361 = G__49362;
continue;
} else {
}
break;
}

cljs.core.async.goog$module$goog$array.shuffle(a);

return a;
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async46160 = (function (flag,meta46161){
this.flag = flag;
this.meta46161 = meta46161;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async46160.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_46162,meta46161__$1){
var self__ = this;
var _46162__$1 = this;
return (new cljs.core.async.t_cljs$core$async46160(self__.flag,meta46161__$1));
}));

(cljs.core.async.t_cljs$core$async46160.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_46162){
var self__ = this;
var _46162__$1 = this;
return self__.meta46161;
}));

(cljs.core.async.t_cljs$core$async46160.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async46160.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.deref(self__.flag);
}));

(cljs.core.async.t_cljs$core$async46160.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async46160.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.flag,null);

return true;
}));

(cljs.core.async.t_cljs$core$async46160.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"meta46161","meta46161",-1587027595,null)], null);
}));

(cljs.core.async.t_cljs$core$async46160.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async46160.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async46160");

(cljs.core.async.t_cljs$core$async46160.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async46160");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async46160.
 */
cljs.core.async.__GT_t_cljs$core$async46160 = (function cljs$core$async$__GT_t_cljs$core$async46160(flag,meta46161){
return (new cljs.core.async.t_cljs$core$async46160(flag,meta46161));
});


cljs.core.async.alt_flag = (function cljs$core$async$alt_flag(){
var flag = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(true);
return (new cljs.core.async.t_cljs$core$async46160(flag,cljs.core.PersistentArrayMap.EMPTY));
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async46228 = (function (flag,cb,meta46229){
this.flag = flag;
this.cb = cb;
this.meta46229 = meta46229;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async46228.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_46230,meta46229__$1){
var self__ = this;
var _46230__$1 = this;
return (new cljs.core.async.t_cljs$core$async46228(self__.flag,self__.cb,meta46229__$1));
}));

(cljs.core.async.t_cljs$core$async46228.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_46230){
var self__ = this;
var _46230__$1 = this;
return self__.meta46229;
}));

(cljs.core.async.t_cljs$core$async46228.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async46228.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.active_QMARK_(self__.flag);
}));

(cljs.core.async.t_cljs$core$async46228.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async46228.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.async.impl.protocols.commit(self__.flag);

return self__.cb;
}));

(cljs.core.async.t_cljs$core$async46228.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"cb","cb",-2064487928,null),new cljs.core.Symbol(null,"meta46229","meta46229",-562614071,null)], null);
}));

(cljs.core.async.t_cljs$core$async46228.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async46228.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async46228");

(cljs.core.async.t_cljs$core$async46228.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async46228");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async46228.
 */
cljs.core.async.__GT_t_cljs$core$async46228 = (function cljs$core$async$__GT_t_cljs$core$async46228(flag,cb,meta46229){
return (new cljs.core.async.t_cljs$core$async46228(flag,cb,meta46229));
});


cljs.core.async.alt_handler = (function cljs$core$async$alt_handler(flag,cb){
return (new cljs.core.async.t_cljs$core$async46228(flag,cb,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * returns derefable [val port] if immediate, nil if enqueued
 */
cljs.core.async.do_alts = (function cljs$core$async$do_alts(fret,ports,opts){
if((cljs.core.count(ports) > (0))){
} else {
throw (new Error((""+"Assert failed: "+"alts must have at least one channel operation"+"\n"+"(pos? (count ports))")));
}

var flag = cljs.core.async.alt_flag();
var ports__$1 = cljs.core.vec(ports);
var n = cljs.core.count(ports__$1);
var _ = (function (){var i = (0);
while(true){
if((i < n)){
var port_49366 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ports__$1,i);
if(cljs.core.vector_QMARK_(port_49366)){
if((!(((port_49366.cljs$core$IFn$_invoke$arity$1 ? port_49366.cljs$core$IFn$_invoke$arity$1((1)) : port_49366.call(null,(1))) == null)))){
} else {
throw (new Error((""+"Assert failed: "+"can't put nil on channel"+"\n"+"(some? (port 1))")));
}
} else {
}

var G__49367 = (i + (1));
i = G__49367;
continue;
} else {
return null;
}
break;
}
})();
var idxs = cljs.core.async.random_array(n);
var priority = new cljs.core.Keyword(null,"priority","priority",1431093715).cljs$core$IFn$_invoke$arity$1(opts);
var ret = (function (){var i = (0);
while(true){
if((i < n)){
var idx = (cljs.core.truth_(priority)?i:(idxs[i]));
var port = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ports__$1,idx);
var wport = ((cljs.core.vector_QMARK_(port))?(port.cljs$core$IFn$_invoke$arity$1 ? port.cljs$core$IFn$_invoke$arity$1((0)) : port.call(null,(0))):null);
var vbox = (cljs.core.truth_(wport)?(function (){var val = (port.cljs$core$IFn$_invoke$arity$1 ? port.cljs$core$IFn$_invoke$arity$1((1)) : port.call(null,(1)));
return cljs.core.async.impl.protocols.put_BANG_(wport,val,cljs.core.async.alt_handler(flag,((function (i,val,idx,port,wport,flag,ports__$1,n,_,idxs,priority){
return (function (p1__46319_SHARP_){
var G__46326 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__46319_SHARP_,wport], null);
return (fret.cljs$core$IFn$_invoke$arity$1 ? fret.cljs$core$IFn$_invoke$arity$1(G__46326) : fret.call(null,G__46326));
});})(i,val,idx,port,wport,flag,ports__$1,n,_,idxs,priority))
));
})():cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.alt_handler(flag,((function (i,idx,port,wport,flag,ports__$1,n,_,idxs,priority){
return (function (p1__46320_SHARP_){
var G__46327 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__46320_SHARP_,port], null);
return (fret.cljs$core$IFn$_invoke$arity$1 ? fret.cljs$core$IFn$_invoke$arity$1(G__46327) : fret.call(null,G__46327));
});})(i,idx,port,wport,flag,ports__$1,n,_,idxs,priority))
)));
if(cljs.core.truth_(vbox)){
return cljs.core.async.impl.channels.box(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.deref(vbox),(function (){var or__5142__auto__ = wport;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return port;
}
})()], null));
} else {
var G__49372 = (i + (1));
i = G__49372;
continue;
}
} else {
return null;
}
break;
}
})();
var or__5142__auto__ = ret;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
if(cljs.core.contains_QMARK_(opts,new cljs.core.Keyword(null,"default","default",-1987822328))){
var temp__5825__auto__ = (function (){var and__5140__auto__ = flag.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1(null);
if(cljs.core.truth_(and__5140__auto__)){
return flag.cljs$core$async$impl$protocols$Handler$commit$arity$1(null);
} else {
return and__5140__auto__;
}
})();
if(cljs.core.truth_(temp__5825__auto__)){
var got = temp__5825__auto__;
return cljs.core.async.impl.channels.box(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"default","default",-1987822328).cljs$core$IFn$_invoke$arity$1(opts),new cljs.core.Keyword(null,"default","default",-1987822328)], null));
} else {
return null;
}
} else {
return null;
}
}
});
/**
 * Completes at most one of several channel operations. Must be called
 * inside a (go ...) block. ports is a vector of channel endpoints,
 * which can be either a channel to take from or a vector of
 *   [channel-to-put-to val-to-put], in any combination. Takes will be
 *   made as if by <!, and puts will be made as if by >!. Unless
 *   the :priority option is true, if more than one port operation is
 *   ready a non-deterministic choice will be made. If no operation is
 *   ready and a :default value is supplied, [default-val :default] will
 *   be returned, otherwise alts! will park until the first operation to
 *   become ready completes. Returns [val port] of the completed
 *   operation, where val is the value taken for takes, and a
 *   boolean (true unless already closed, as per put!) for puts.
 * 
 *   opts are passed as :key val ... Supported options:
 * 
 *   :default val - the value to use if none of the operations are immediately ready
 *   :priority true - (default nil) when true, the operations will be tried in order.
 * 
 *   Note: there is no guarantee that the port exps or val exprs will be
 *   used, nor in what order should they be, so they should not be
 *   depended upon for side effects.
 */
cljs.core.async.alts_BANG_ = (function cljs$core$async$alts_BANG_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___49373 = arguments.length;
var i__5877__auto___49374 = (0);
while(true){
if((i__5877__auto___49374 < len__5876__auto___49373)){
args__5882__auto__.push((arguments[i__5877__auto___49374]));

var G__49375 = (i__5877__auto___49374 + (1));
i__5877__auto___49374 = G__49375;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (ports,p__46343){
var map__46344 = p__46343;
var map__46344__$1 = cljs.core.__destructure_map(map__46344);
var opts = map__46344__$1;
throw (new Error("alts! used not in (go ...) block"));
}));

(cljs.core.async.alts_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(cljs.core.async.alts_BANG_.cljs$lang$applyTo = (function (seq46336){
var G__46337 = cljs.core.first(seq46336);
var seq46336__$1 = cljs.core.next(seq46336);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__46337,seq46336__$1);
}));

/**
 * Puts a val into port if it's possible to do so immediately.
 *   nil values are not allowed. Never blocks. Returns true if offer succeeds.
 */
cljs.core.async.offer_BANG_ = (function cljs$core$async$offer_BANG_(port,val){
var ret = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref(ret);
} else {
return null;
}
});
/**
 * Takes a val from port if it's possible to do so immediately.
 *   Never blocks. Returns value if successful, nil otherwise.
 */
cljs.core.async.poll_BANG_ = (function cljs$core$async$poll_BANG_(port){
var ret = cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref(ret);
} else {
return null;
}
});
/**
 * Takes elements from the from channel and supplies them to the to
 * channel. By default, the to channel will be closed when the from
 * channel closes, but can be determined by the close?  parameter. Will
 * stop consuming the from channel if the to channel closes
 */
cljs.core.async.pipe = (function cljs$core$async$pipe(var_args){
var G__46360 = arguments.length;
switch (G__46360) {
case 2:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2 = (function (from,to){
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3(from,to,true);
}));

(cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3 = (function (from,to,close_QMARK_){
var c__45940__auto___49378 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_46425){
var state_val_46426 = (state_46425[(1)]);
if((state_val_46426 === (7))){
var inst_46419 = (state_46425[(2)]);
var state_46425__$1 = state_46425;
var statearr_46441_49379 = state_46425__$1;
(statearr_46441_49379[(2)] = inst_46419);

(statearr_46441_49379[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (1))){
var state_46425__$1 = state_46425;
var statearr_46443_49380 = state_46425__$1;
(statearr_46443_49380[(2)] = null);

(statearr_46443_49380[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (4))){
var inst_46382 = (state_46425[(7)]);
var inst_46382__$1 = (state_46425[(2)]);
var inst_46392 = (inst_46382__$1 == null);
var state_46425__$1 = (function (){var statearr_46445 = state_46425;
(statearr_46445[(7)] = inst_46382__$1);

return statearr_46445;
})();
if(cljs.core.truth_(inst_46392)){
var statearr_46446_49381 = state_46425__$1;
(statearr_46446_49381[(1)] = (5));

} else {
var statearr_46447_49382 = state_46425__$1;
(statearr_46447_49382[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (13))){
var state_46425__$1 = state_46425;
var statearr_46450_49383 = state_46425__$1;
(statearr_46450_49383[(2)] = null);

(statearr_46450_49383[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (6))){
var inst_46382 = (state_46425[(7)]);
var state_46425__$1 = state_46425;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_46425__$1,(11),to,inst_46382);
} else {
if((state_val_46426 === (3))){
var inst_46423 = (state_46425[(2)]);
var state_46425__$1 = state_46425;
return cljs.core.async.impl.ioc_helpers.return_chan(state_46425__$1,inst_46423);
} else {
if((state_val_46426 === (12))){
var state_46425__$1 = state_46425;
var statearr_46458_49384 = state_46425__$1;
(statearr_46458_49384[(2)] = null);

(statearr_46458_49384[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (2))){
var state_46425__$1 = state_46425;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46425__$1,(4),from);
} else {
if((state_val_46426 === (11))){
var inst_46412 = (state_46425[(2)]);
var state_46425__$1 = state_46425;
if(cljs.core.truth_(inst_46412)){
var statearr_46461_49385 = state_46425__$1;
(statearr_46461_49385[(1)] = (12));

} else {
var statearr_46462_49386 = state_46425__$1;
(statearr_46462_49386[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (9))){
var state_46425__$1 = state_46425;
var statearr_46463_49387 = state_46425__$1;
(statearr_46463_49387[(2)] = null);

(statearr_46463_49387[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (5))){
var state_46425__$1 = state_46425;
if(cljs.core.truth_(close_QMARK_)){
var statearr_46464_49388 = state_46425__$1;
(statearr_46464_49388[(1)] = (8));

} else {
var statearr_46465_49389 = state_46425__$1;
(statearr_46465_49389[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (14))){
var inst_46417 = (state_46425[(2)]);
var state_46425__$1 = state_46425;
var statearr_46466_49390 = state_46425__$1;
(statearr_46466_49390[(2)] = inst_46417);

(statearr_46466_49390[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (10))){
var inst_46404 = (state_46425[(2)]);
var state_46425__$1 = state_46425;
var statearr_46468_49391 = state_46425__$1;
(statearr_46468_49391[(2)] = inst_46404);

(statearr_46468_49391[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46426 === (8))){
var inst_46401 = cljs.core.async.close_BANG_(to);
var state_46425__$1 = state_46425;
var statearr_46470_49394 = state_46425__$1;
(statearr_46470_49394[(2)] = inst_46401);

(statearr_46470_49394[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
}
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_46473 = [null,null,null,null,null,null,null,null];
(statearr_46473[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_46473[(1)] = (1));

return statearr_46473;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_46425){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_46425);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e46476){var ex__45565__auto__ = e46476;
var statearr_46477_49395 = state_46425;
(statearr_46477_49395[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_46425[(4)]))){
var statearr_46478_49396 = state_46425;
(statearr_46478_49396[(1)] = cljs.core.first((state_46425[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49397 = state_46425;
state_46425 = G__49397;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_46425){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_46425);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_46479 = f__45941__auto__();
(statearr_46479[(6)] = c__45940__auto___49378);

return statearr_46479;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return to;
}));

(cljs.core.async.pipe.cljs$lang$maxFixedArity = 3);

cljs.core.async.pipeline_STAR_ = (function cljs$core$async$pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,type){
if((n > (0))){
} else {
throw (new Error("Assert failed: (pos? n)"));
}

var jobs = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(n);
var results = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(n);
var process__$1 = (function (p__46488){
var vec__46489 = p__46488;
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46489,(0),null);
var p = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46489,(1),null);
var job = vec__46489;
if((job == null)){
cljs.core.async.close_BANG_(results);

return null;
} else {
var res = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3((1),xf,ex_handler);
var c__45940__auto___49398 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_46498){
var state_val_46499 = (state_46498[(1)]);
if((state_val_46499 === (1))){
var state_46498__$1 = state_46498;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_46498__$1,(2),res,v);
} else {
if((state_val_46499 === (2))){
var inst_46495 = (state_46498[(2)]);
var inst_46496 = cljs.core.async.close_BANG_(res);
var state_46498__$1 = (function (){var statearr_46502 = state_46498;
(statearr_46502[(7)] = inst_46495);

return statearr_46502;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_46498__$1,inst_46496);
} else {
return null;
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0 = (function (){
var statearr_46503 = [null,null,null,null,null,null,null,null];
(statearr_46503[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__);

(statearr_46503[(1)] = (1));

return statearr_46503;
});
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1 = (function (state_46498){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_46498);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e46504){var ex__45565__auto__ = e46504;
var statearr_46505_49400 = state_46498;
(statearr_46505_49400[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_46498[(4)]))){
var statearr_46507_49403 = state_46498;
(statearr_46507_49403[(1)] = cljs.core.first((state_46498[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49406 = state_46498;
state_46498 = G__49406;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = function(state_46498){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1.call(this,state_46498);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_46509 = f__45941__auto__();
(statearr_46509[(6)] = c__45940__auto___49398);

return statearr_46509;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(p,res);

return true;
}
});
var async = (function (p__46510){
var vec__46511 = p__46510;
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46511,(0),null);
var p = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46511,(1),null);
var job = vec__46511;
if((job == null)){
cljs.core.async.close_BANG_(results);

return null;
} else {
var res = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
(xf.cljs$core$IFn$_invoke$arity$2 ? xf.cljs$core$IFn$_invoke$arity$2(v,res) : xf.call(null,v,res));

cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(p,res);

return true;
}
});
var n__5741__auto___49407 = n;
var __49408 = (0);
while(true){
if((__49408 < n__5741__auto___49407)){
var G__46514_49410 = type;
var G__46514_49411__$1 = (((G__46514_49410 instanceof cljs.core.Keyword))?G__46514_49410.fqn:null);
switch (G__46514_49411__$1) {
case "compute":
var c__45940__auto___49413 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (__49408,c__45940__auto___49413,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async){
return (function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = ((function (__49408,c__45940__auto___49413,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async){
return (function (state_46528){
var state_val_46529 = (state_46528[(1)]);
if((state_val_46529 === (1))){
var state_46528__$1 = state_46528;
var statearr_46533_49414 = state_46528__$1;
(statearr_46533_49414[(2)] = null);

(statearr_46533_49414[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46529 === (2))){
var state_46528__$1 = state_46528;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46528__$1,(4),jobs);
} else {
if((state_val_46529 === (3))){
var inst_46526 = (state_46528[(2)]);
var state_46528__$1 = state_46528;
return cljs.core.async.impl.ioc_helpers.return_chan(state_46528__$1,inst_46526);
} else {
if((state_val_46529 === (4))){
var inst_46517 = (state_46528[(2)]);
var inst_46519 = process__$1(inst_46517);
var state_46528__$1 = state_46528;
if(cljs.core.truth_(inst_46519)){
var statearr_46534_49415 = state_46528__$1;
(statearr_46534_49415[(1)] = (5));

} else {
var statearr_46535_49416 = state_46528__$1;
(statearr_46535_49416[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46529 === (5))){
var state_46528__$1 = state_46528;
var statearr_46536_49417 = state_46528__$1;
(statearr_46536_49417[(2)] = null);

(statearr_46536_49417[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46529 === (6))){
var state_46528__$1 = state_46528;
var statearr_46537_49418 = state_46528__$1;
(statearr_46537_49418[(2)] = null);

(statearr_46537_49418[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46529 === (7))){
var inst_46524 = (state_46528[(2)]);
var state_46528__$1 = state_46528;
var statearr_46539_49421 = state_46528__$1;
(statearr_46539_49421[(2)] = inst_46524);

(statearr_46539_49421[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__49408,c__45940__auto___49413,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async))
;
return ((function (__49408,switch__45561__auto__,c__45940__auto___49413,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0 = (function (){
var statearr_46542 = [null,null,null,null,null,null,null];
(statearr_46542[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__);

(statearr_46542[(1)] = (1));

return statearr_46542;
});
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1 = (function (state_46528){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_46528);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e46543){var ex__45565__auto__ = e46543;
var statearr_46544_49426 = state_46528;
(statearr_46544_49426[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_46528[(4)]))){
var statearr_46545_49427 = state_46528;
(statearr_46545_49427[(1)] = cljs.core.first((state_46528[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49429 = state_46528;
state_46528 = G__49429;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = function(state_46528){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1.call(this,state_46528);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__;
})()
;})(__49408,switch__45561__auto__,c__45940__auto___49413,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async))
})();
var state__45942__auto__ = (function (){var statearr_46546 = f__45941__auto__();
(statearr_46546[(6)] = c__45940__auto___49413);

return statearr_46546;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
});})(__49408,c__45940__auto___49413,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async))
);


break;
case "async":
var c__45940__auto___49434 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (__49408,c__45940__auto___49434,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async){
return (function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = ((function (__49408,c__45940__auto___49434,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async){
return (function (state_46564){
var state_val_46565 = (state_46564[(1)]);
if((state_val_46565 === (1))){
var state_46564__$1 = state_46564;
var statearr_46568_49437 = state_46564__$1;
(statearr_46568_49437[(2)] = null);

(statearr_46568_49437[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46565 === (2))){
var state_46564__$1 = state_46564;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46564__$1,(4),jobs);
} else {
if((state_val_46565 === (3))){
var inst_46562 = (state_46564[(2)]);
var state_46564__$1 = state_46564;
return cljs.core.async.impl.ioc_helpers.return_chan(state_46564__$1,inst_46562);
} else {
if((state_val_46565 === (4))){
var inst_46554 = (state_46564[(2)]);
var inst_46555 = async(inst_46554);
var state_46564__$1 = state_46564;
if(cljs.core.truth_(inst_46555)){
var statearr_46574_49441 = state_46564__$1;
(statearr_46574_49441[(1)] = (5));

} else {
var statearr_46575_49442 = state_46564__$1;
(statearr_46575_49442[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46565 === (5))){
var state_46564__$1 = state_46564;
var statearr_46580_49443 = state_46564__$1;
(statearr_46580_49443[(2)] = null);

(statearr_46580_49443[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46565 === (6))){
var state_46564__$1 = state_46564;
var statearr_46581_49445 = state_46564__$1;
(statearr_46581_49445[(2)] = null);

(statearr_46581_49445[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46565 === (7))){
var inst_46560 = (state_46564[(2)]);
var state_46564__$1 = state_46564;
var statearr_46585_49449 = state_46564__$1;
(statearr_46585_49449[(2)] = inst_46560);

(statearr_46585_49449[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__49408,c__45940__auto___49434,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async))
;
return ((function (__49408,switch__45561__auto__,c__45940__auto___49434,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0 = (function (){
var statearr_46586 = [null,null,null,null,null,null,null];
(statearr_46586[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__);

(statearr_46586[(1)] = (1));

return statearr_46586;
});
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1 = (function (state_46564){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_46564);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e46592){var ex__45565__auto__ = e46592;
var statearr_46594_49450 = state_46564;
(statearr_46594_49450[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_46564[(4)]))){
var statearr_46595_49451 = state_46564;
(statearr_46595_49451[(1)] = cljs.core.first((state_46564[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49454 = state_46564;
state_46564 = G__49454;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = function(state_46564){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1.call(this,state_46564);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__;
})()
;})(__49408,switch__45561__auto__,c__45940__auto___49434,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async))
})();
var state__45942__auto__ = (function (){var statearr_46599 = f__45941__auto__();
(statearr_46599[(6)] = c__45940__auto___49434);

return statearr_46599;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
});})(__49408,c__45940__auto___49434,G__46514_49410,G__46514_49411__$1,n__5741__auto___49407,jobs,results,process__$1,async))
);


break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__46514_49411__$1))));

}

var G__49455 = (__49408 + (1));
__49408 = G__49455;
continue;
} else {
}
break;
}

var c__45940__auto___49456 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_46636){
var state_val_46637 = (state_46636[(1)]);
if((state_val_46637 === (7))){
var inst_46630 = (state_46636[(2)]);
var state_46636__$1 = state_46636;
var statearr_46649_49457 = state_46636__$1;
(statearr_46649_49457[(2)] = inst_46630);

(statearr_46649_49457[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46637 === (1))){
var state_46636__$1 = state_46636;
var statearr_46655_49458 = state_46636__$1;
(statearr_46655_49458[(2)] = null);

(statearr_46655_49458[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46637 === (4))){
var inst_46606 = (state_46636[(7)]);
var inst_46606__$1 = (state_46636[(2)]);
var inst_46607 = (inst_46606__$1 == null);
var state_46636__$1 = (function (){var statearr_46660 = state_46636;
(statearr_46660[(7)] = inst_46606__$1);

return statearr_46660;
})();
if(cljs.core.truth_(inst_46607)){
var statearr_46669_49459 = state_46636__$1;
(statearr_46669_49459[(1)] = (5));

} else {
var statearr_46670_49460 = state_46636__$1;
(statearr_46670_49460[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46637 === (6))){
var inst_46606 = (state_46636[(7)]);
var inst_46614 = (state_46636[(8)]);
var inst_46614__$1 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var inst_46615 = cljs.core.PersistentVector.EMPTY_NODE;
var inst_46616 = [inst_46606,inst_46614__$1];
var inst_46617 = (new cljs.core.PersistentVector(null,2,(5),inst_46615,inst_46616,null));
var state_46636__$1 = (function (){var statearr_46678 = state_46636;
(statearr_46678[(8)] = inst_46614__$1);

return statearr_46678;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_46636__$1,(8),jobs,inst_46617);
} else {
if((state_val_46637 === (3))){
var inst_46632 = (state_46636[(2)]);
var state_46636__$1 = state_46636;
return cljs.core.async.impl.ioc_helpers.return_chan(state_46636__$1,inst_46632);
} else {
if((state_val_46637 === (2))){
var state_46636__$1 = state_46636;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46636__$1,(4),from);
} else {
if((state_val_46637 === (9))){
var inst_46625 = (state_46636[(2)]);
var state_46636__$1 = (function (){var statearr_46686 = state_46636;
(statearr_46686[(9)] = inst_46625);

return statearr_46686;
})();
var statearr_46687_49471 = state_46636__$1;
(statearr_46687_49471[(2)] = null);

(statearr_46687_49471[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46637 === (5))){
var inst_46609 = cljs.core.async.close_BANG_(jobs);
var state_46636__$1 = state_46636;
var statearr_46696_49472 = state_46636__$1;
(statearr_46696_49472[(2)] = inst_46609);

(statearr_46696_49472[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46637 === (8))){
var inst_46614 = (state_46636[(8)]);
var inst_46619 = (state_46636[(2)]);
var state_46636__$1 = (function (){var statearr_46697 = state_46636;
(statearr_46697[(10)] = inst_46619);

return statearr_46697;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_46636__$1,(9),results,inst_46614);
} else {
return null;
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0 = (function (){
var statearr_46704 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_46704[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__);

(statearr_46704[(1)] = (1));

return statearr_46704;
});
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1 = (function (state_46636){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_46636);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e46709){var ex__45565__auto__ = e46709;
var statearr_46710_49473 = state_46636;
(statearr_46710_49473[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_46636[(4)]))){
var statearr_46714_49474 = state_46636;
(statearr_46714_49474[(1)] = cljs.core.first((state_46636[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49476 = state_46636;
state_46636 = G__49476;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = function(state_46636){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1.call(this,state_46636);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_46716 = f__45941__auto__();
(statearr_46716[(6)] = c__45940__auto___49456);

return statearr_46716;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


var c__45940__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_46769){
var state_val_46770 = (state_46769[(1)]);
if((state_val_46770 === (7))){
var inst_46760 = (state_46769[(2)]);
var state_46769__$1 = state_46769;
var statearr_46777_49480 = state_46769__$1;
(statearr_46777_49480[(2)] = inst_46760);

(statearr_46777_49480[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (20))){
var state_46769__$1 = state_46769;
var statearr_46778_49481 = state_46769__$1;
(statearr_46778_49481[(2)] = null);

(statearr_46778_49481[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (1))){
var state_46769__$1 = state_46769;
var statearr_46779_49482 = state_46769__$1;
(statearr_46779_49482[(2)] = null);

(statearr_46779_49482[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (4))){
var inst_46720 = (state_46769[(7)]);
var inst_46720__$1 = (state_46769[(2)]);
var inst_46722 = (inst_46720__$1 == null);
var state_46769__$1 = (function (){var statearr_46781 = state_46769;
(statearr_46781[(7)] = inst_46720__$1);

return statearr_46781;
})();
if(cljs.core.truth_(inst_46722)){
var statearr_46782_49490 = state_46769__$1;
(statearr_46782_49490[(1)] = (5));

} else {
var statearr_46783_49491 = state_46769__$1;
(statearr_46783_49491[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (15))){
var inst_46737 = (state_46769[(8)]);
var state_46769__$1 = state_46769;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_46769__$1,(18),to,inst_46737);
} else {
if((state_val_46770 === (21))){
var inst_46755 = (state_46769[(2)]);
var state_46769__$1 = state_46769;
var statearr_46785_49494 = state_46769__$1;
(statearr_46785_49494[(2)] = inst_46755);

(statearr_46785_49494[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (13))){
var inst_46757 = (state_46769[(2)]);
var state_46769__$1 = (function (){var statearr_46787 = state_46769;
(statearr_46787[(9)] = inst_46757);

return statearr_46787;
})();
var statearr_46792_49495 = state_46769__$1;
(statearr_46792_49495[(2)] = null);

(statearr_46792_49495[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (6))){
var inst_46720 = (state_46769[(7)]);
var state_46769__$1 = state_46769;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46769__$1,(11),inst_46720);
} else {
if((state_val_46770 === (17))){
var inst_46746 = (state_46769[(2)]);
var state_46769__$1 = state_46769;
if(cljs.core.truth_(inst_46746)){
var statearr_46795_49500 = state_46769__$1;
(statearr_46795_49500[(1)] = (19));

} else {
var statearr_46796_49501 = state_46769__$1;
(statearr_46796_49501[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (3))){
var inst_46762 = (state_46769[(2)]);
var state_46769__$1 = state_46769;
return cljs.core.async.impl.ioc_helpers.return_chan(state_46769__$1,inst_46762);
} else {
if((state_val_46770 === (12))){
var inst_46734 = (state_46769[(10)]);
var state_46769__$1 = state_46769;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46769__$1,(14),inst_46734);
} else {
if((state_val_46770 === (2))){
var state_46769__$1 = state_46769;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46769__$1,(4),results);
} else {
if((state_val_46770 === (19))){
var state_46769__$1 = state_46769;
var statearr_46797_49503 = state_46769__$1;
(statearr_46797_49503[(2)] = null);

(statearr_46797_49503[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (11))){
var inst_46734 = (state_46769[(2)]);
var state_46769__$1 = (function (){var statearr_46801 = state_46769;
(statearr_46801[(10)] = inst_46734);

return statearr_46801;
})();
var statearr_46803_49504 = state_46769__$1;
(statearr_46803_49504[(2)] = null);

(statearr_46803_49504[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (9))){
var state_46769__$1 = state_46769;
var statearr_46804_49505 = state_46769__$1;
(statearr_46804_49505[(2)] = null);

(statearr_46804_49505[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (5))){
var state_46769__$1 = state_46769;
if(cljs.core.truth_(close_QMARK_)){
var statearr_46806_49506 = state_46769__$1;
(statearr_46806_49506[(1)] = (8));

} else {
var statearr_46807_49507 = state_46769__$1;
(statearr_46807_49507[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (14))){
var inst_46737 = (state_46769[(8)]);
var inst_46740 = (state_46769[(11)]);
var inst_46737__$1 = (state_46769[(2)]);
var inst_46739 = (inst_46737__$1 == null);
var inst_46740__$1 = cljs.core.not(inst_46739);
var state_46769__$1 = (function (){var statearr_46810 = state_46769;
(statearr_46810[(8)] = inst_46737__$1);

(statearr_46810[(11)] = inst_46740__$1);

return statearr_46810;
})();
if(inst_46740__$1){
var statearr_46811_49508 = state_46769__$1;
(statearr_46811_49508[(1)] = (15));

} else {
var statearr_46814_49509 = state_46769__$1;
(statearr_46814_49509[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (16))){
var inst_46740 = (state_46769[(11)]);
var state_46769__$1 = state_46769;
var statearr_46815_49511 = state_46769__$1;
(statearr_46815_49511[(2)] = inst_46740);

(statearr_46815_49511[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (10))){
var inst_46731 = (state_46769[(2)]);
var state_46769__$1 = state_46769;
var statearr_46816_49512 = state_46769__$1;
(statearr_46816_49512[(2)] = inst_46731);

(statearr_46816_49512[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (18))){
var inst_46743 = (state_46769[(2)]);
var state_46769__$1 = state_46769;
var statearr_46817_49513 = state_46769__$1;
(statearr_46817_49513[(2)] = inst_46743);

(statearr_46817_49513[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46770 === (8))){
var inst_46728 = cljs.core.async.close_BANG_(to);
var state_46769__$1 = state_46769;
var statearr_46818_49514 = state_46769__$1;
(statearr_46818_49514[(2)] = inst_46728);

(statearr_46818_49514[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0 = (function (){
var statearr_46819 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_46819[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__);

(statearr_46819[(1)] = (1));

return statearr_46819;
});
var cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1 = (function (state_46769){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_46769);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e46820){var ex__45565__auto__ = e46820;
var statearr_46821_49519 = state_46769;
(statearr_46821_49519[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_46769[(4)]))){
var statearr_46823_49520 = state_46769;
(statearr_46823_49520[(1)] = cljs.core.first((state_46769[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49521 = state_46769;
state_46769 = G__49521;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__ = function(state_46769){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1.call(this,state_46769);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__45562__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_46825 = f__45941__auto__();
(statearr_46825[(6)] = c__45940__auto__);

return statearr_46825;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));

return c__45940__auto__;
});
/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the async function af, with parallelism n. af
 *   must be a function of two arguments, the first an input value and
 *   the second a channel on which to place the result(s). The
 *   presumption is that af will return immediately, having launched some
 *   asynchronous operation whose completion/callback will put results on
 *   the channel, then close! it. Outputs will be returned in order
 *   relative to the inputs. By default, the to channel will be closed
 *   when the from channel closes, but can be determined by the close?
 *   parameter. Will stop consuming the from channel if the to channel
 *   closes. See also pipeline, pipeline-blocking.
 */
cljs.core.async.pipeline_async = (function cljs$core$async$pipeline_async(var_args){
var G__46828 = arguments.length;
switch (G__46828) {
case 4:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4 = (function (n,to,af,from){
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5(n,to,af,from,true);
}));

(cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5 = (function (n,to,af,from,close_QMARK_){
return cljs.core.async.pipeline_STAR_(n,to,af,from,close_QMARK_,null,new cljs.core.Keyword(null,"async","async",1050769601));
}));

(cljs.core.async.pipeline_async.cljs$lang$maxFixedArity = 5);

/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the transducer xf, with parallelism n. Because
 *   it is parallel, the transducer will be applied independently to each
 *   element, not across elements, and may produce zero or more outputs
 *   per input.  Outputs will be returned in order relative to the
 *   inputs. By default, the to channel will be closed when the from
 *   channel closes, but can be determined by the close?  parameter. Will
 *   stop consuming the from channel if the to channel closes.
 * 
 *   Note this is supplied for API compatibility with the Clojure version.
 *   Values of N > 1 will not result in actual concurrency in a
 *   single-threaded runtime.
 */
cljs.core.async.pipeline = (function cljs$core$async$pipeline(var_args){
var G__46834 = arguments.length;
switch (G__46834) {
case 4:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4 = (function (n,to,xf,from){
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5(n,to,xf,from,true);
}));

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5 = (function (n,to,xf,from,close_QMARK_){
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6(n,to,xf,from,close_QMARK_,null);
}));

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6 = (function (n,to,xf,from,close_QMARK_,ex_handler){
return cljs.core.async.pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,new cljs.core.Keyword(null,"compute","compute",1555393130));
}));

(cljs.core.async.pipeline.cljs$lang$maxFixedArity = 6);

/**
 * Takes a predicate and a source channel and returns a vector of two
 *   channels, the first of which will contain the values for which the
 *   predicate returned true, the second those for which it returned
 *   false.
 * 
 *   The out channels will be unbuffered by default, or two buf-or-ns can
 *   be supplied. The channels will close after the source channel has
 *   closed.
 */
cljs.core.async.split = (function cljs$core$async$split(var_args){
var G__46854 = arguments.length;
switch (G__46854) {
case 2:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 4:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.split.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$4(p,ch,null,null);
}));

(cljs.core.async.split.cljs$core$IFn$_invoke$arity$4 = (function (p,ch,t_buf_or_n,f_buf_or_n){
var tc = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(t_buf_or_n);
var fc = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(f_buf_or_n);
var c__45940__auto___49536 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_46913){
var state_val_46914 = (state_46913[(1)]);
if((state_val_46914 === (7))){
var inst_46909 = (state_46913[(2)]);
var state_46913__$1 = state_46913;
var statearr_46922_49543 = state_46913__$1;
(statearr_46922_49543[(2)] = inst_46909);

(statearr_46922_49543[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (1))){
var state_46913__$1 = state_46913;
var statearr_46926_49544 = state_46913__$1;
(statearr_46926_49544[(2)] = null);

(statearr_46926_49544[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (4))){
var inst_46869 = (state_46913[(7)]);
var inst_46869__$1 = (state_46913[(2)]);
var inst_46871 = (inst_46869__$1 == null);
var state_46913__$1 = (function (){var statearr_46932 = state_46913;
(statearr_46932[(7)] = inst_46869__$1);

return statearr_46932;
})();
if(cljs.core.truth_(inst_46871)){
var statearr_46934_49545 = state_46913__$1;
(statearr_46934_49545[(1)] = (5));

} else {
var statearr_46935_49546 = state_46913__$1;
(statearr_46935_49546[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (13))){
var state_46913__$1 = state_46913;
var statearr_46937_49547 = state_46913__$1;
(statearr_46937_49547[(2)] = null);

(statearr_46937_49547[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (6))){
var inst_46869 = (state_46913[(7)]);
var inst_46891 = (p.cljs$core$IFn$_invoke$arity$1 ? p.cljs$core$IFn$_invoke$arity$1(inst_46869) : p.call(null,inst_46869));
var state_46913__$1 = state_46913;
if(cljs.core.truth_(inst_46891)){
var statearr_46950_49548 = state_46913__$1;
(statearr_46950_49548[(1)] = (9));

} else {
var statearr_46951_49549 = state_46913__$1;
(statearr_46951_49549[(1)] = (10));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (3))){
var inst_46911 = (state_46913[(2)]);
var state_46913__$1 = state_46913;
return cljs.core.async.impl.ioc_helpers.return_chan(state_46913__$1,inst_46911);
} else {
if((state_val_46914 === (12))){
var state_46913__$1 = state_46913;
var statearr_46955_49550 = state_46913__$1;
(statearr_46955_49550[(2)] = null);

(statearr_46955_49550[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (2))){
var state_46913__$1 = state_46913;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_46913__$1,(4),ch);
} else {
if((state_val_46914 === (11))){
var inst_46869 = (state_46913[(7)]);
var inst_46896 = (state_46913[(2)]);
var state_46913__$1 = state_46913;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_46913__$1,(8),inst_46896,inst_46869);
} else {
if((state_val_46914 === (9))){
var state_46913__$1 = state_46913;
var statearr_46962_49551 = state_46913__$1;
(statearr_46962_49551[(2)] = tc);

(statearr_46962_49551[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (5))){
var inst_46877 = cljs.core.async.close_BANG_(tc);
var inst_46880 = cljs.core.async.close_BANG_(fc);
var state_46913__$1 = (function (){var statearr_46965 = state_46913;
(statearr_46965[(8)] = inst_46877);

return statearr_46965;
})();
var statearr_46966_49553 = state_46913__$1;
(statearr_46966_49553[(2)] = inst_46880);

(statearr_46966_49553[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (14))){
var inst_46907 = (state_46913[(2)]);
var state_46913__$1 = state_46913;
var statearr_46973_49554 = state_46913__$1;
(statearr_46973_49554[(2)] = inst_46907);

(statearr_46973_49554[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (10))){
var state_46913__$1 = state_46913;
var statearr_46974_49555 = state_46913__$1;
(statearr_46974_49555[(2)] = fc);

(statearr_46974_49555[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_46914 === (8))){
var inst_46898 = (state_46913[(2)]);
var state_46913__$1 = state_46913;
if(cljs.core.truth_(inst_46898)){
var statearr_46977_49556 = state_46913__$1;
(statearr_46977_49556[(1)] = (12));

} else {
var statearr_46979_49558 = state_46913__$1;
(statearr_46979_49558[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
}
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_46984 = [null,null,null,null,null,null,null,null,null];
(statearr_46984[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_46984[(1)] = (1));

return statearr_46984;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_46913){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_46913);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e46986){var ex__45565__auto__ = e46986;
var statearr_46988_49563 = state_46913;
(statearr_46988_49563[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_46913[(4)]))){
var statearr_46991_49564 = state_46913;
(statearr_46991_49564[(1)] = cljs.core.first((state_46913[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49568 = state_46913;
state_46913 = G__49568;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_46913){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_46913);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_46997 = f__45941__auto__();
(statearr_46997[(6)] = c__45940__auto___49536);

return statearr_46997;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tc,fc], null);
}));

(cljs.core.async.split.cljs$lang$maxFixedArity = 4);

/**
 * f should be a function of 2 arguments. Returns a channel containing
 *   the single result of applying f to init and the first item from the
 *   channel, then applying f to that result and the 2nd item, etc. If
 *   the channel closes without yielding items, returns init and f is not
 *   called. ch must close before reduce produces a result.
 */
cljs.core.async.reduce = (function cljs$core$async$reduce(f,init,ch){
var c__45940__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_47069){
var state_val_47070 = (state_47069[(1)]);
if((state_val_47070 === (7))){
var inst_47063 = (state_47069[(2)]);
var state_47069__$1 = state_47069;
var statearr_47080_49570 = state_47069__$1;
(statearr_47080_49570[(2)] = inst_47063);

(statearr_47080_49570[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47070 === (1))){
var inst_47027 = init;
var inst_47030 = inst_47027;
var state_47069__$1 = (function (){var statearr_47084 = state_47069;
(statearr_47084[(7)] = inst_47030);

return statearr_47084;
})();
var statearr_47087_49571 = state_47069__$1;
(statearr_47087_49571[(2)] = null);

(statearr_47087_49571[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47070 === (4))){
var inst_47036 = (state_47069[(8)]);
var inst_47036__$1 = (state_47069[(2)]);
var inst_47041 = (inst_47036__$1 == null);
var state_47069__$1 = (function (){var statearr_47091 = state_47069;
(statearr_47091[(8)] = inst_47036__$1);

return statearr_47091;
})();
if(cljs.core.truth_(inst_47041)){
var statearr_47093_49572 = state_47069__$1;
(statearr_47093_49572[(1)] = (5));

} else {
var statearr_47094_49573 = state_47069__$1;
(statearr_47094_49573[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47070 === (6))){
var inst_47030 = (state_47069[(7)]);
var inst_47036 = (state_47069[(8)]);
var inst_47047 = (state_47069[(9)]);
var inst_47047__$1 = (f.cljs$core$IFn$_invoke$arity$2 ? f.cljs$core$IFn$_invoke$arity$2(inst_47030,inst_47036) : f.call(null,inst_47030,inst_47036));
var inst_47050 = cljs.core.reduced_QMARK_(inst_47047__$1);
var state_47069__$1 = (function (){var statearr_47096 = state_47069;
(statearr_47096[(9)] = inst_47047__$1);

return statearr_47096;
})();
if(inst_47050){
var statearr_47097_49576 = state_47069__$1;
(statearr_47097_49576[(1)] = (8));

} else {
var statearr_47098_49577 = state_47069__$1;
(statearr_47098_49577[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47070 === (3))){
var inst_47067 = (state_47069[(2)]);
var state_47069__$1 = state_47069;
return cljs.core.async.impl.ioc_helpers.return_chan(state_47069__$1,inst_47067);
} else {
if((state_val_47070 === (2))){
var state_47069__$1 = state_47069;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_47069__$1,(4),ch);
} else {
if((state_val_47070 === (9))){
var inst_47047 = (state_47069[(9)]);
var inst_47030 = inst_47047;
var state_47069__$1 = (function (){var statearr_47103 = state_47069;
(statearr_47103[(7)] = inst_47030);

return statearr_47103;
})();
var statearr_47105_49581 = state_47069__$1;
(statearr_47105_49581[(2)] = null);

(statearr_47105_49581[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47070 === (5))){
var inst_47030 = (state_47069[(7)]);
var state_47069__$1 = state_47069;
var statearr_47107_49583 = state_47069__$1;
(statearr_47107_49583[(2)] = inst_47030);

(statearr_47107_49583[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47070 === (10))){
var inst_47061 = (state_47069[(2)]);
var state_47069__$1 = state_47069;
var statearr_47113_49584 = state_47069__$1;
(statearr_47113_49584[(2)] = inst_47061);

(statearr_47113_49584[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47070 === (8))){
var inst_47047 = (state_47069[(9)]);
var inst_47057 = cljs.core.deref(inst_47047);
var state_47069__$1 = state_47069;
var statearr_47115_49585 = state_47069__$1;
(statearr_47115_49585[(2)] = inst_47057);

(statearr_47115_49585[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
});
return (function() {
var cljs$core$async$reduce_$_state_machine__45562__auto__ = null;
var cljs$core$async$reduce_$_state_machine__45562__auto____0 = (function (){
var statearr_47116 = [null,null,null,null,null,null,null,null,null,null];
(statearr_47116[(0)] = cljs$core$async$reduce_$_state_machine__45562__auto__);

(statearr_47116[(1)] = (1));

return statearr_47116;
});
var cljs$core$async$reduce_$_state_machine__45562__auto____1 = (function (state_47069){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_47069);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e47118){var ex__45565__auto__ = e47118;
var statearr_47120_49586 = state_47069;
(statearr_47120_49586[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_47069[(4)]))){
var statearr_47121_49587 = state_47069;
(statearr_47121_49587[(1)] = cljs.core.first((state_47069[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49588 = state_47069;
state_47069 = G__49588;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$reduce_$_state_machine__45562__auto__ = function(state_47069){
switch(arguments.length){
case 0:
return cljs$core$async$reduce_$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$reduce_$_state_machine__45562__auto____1.call(this,state_47069);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$reduce_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$reduce_$_state_machine__45562__auto____0;
cljs$core$async$reduce_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$reduce_$_state_machine__45562__auto____1;
return cljs$core$async$reduce_$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_47124 = f__45941__auto__();
(statearr_47124[(6)] = c__45940__auto__);

return statearr_47124;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));

return c__45940__auto__;
});
/**
 * async/reduces a channel with a transformation (xform f).
 *   Returns a channel containing the result.  ch must close before
 *   transduce produces a result.
 */
cljs.core.async.transduce = (function cljs$core$async$transduce(xform,f,init,ch){
var f__$1 = (xform.cljs$core$IFn$_invoke$arity$1 ? xform.cljs$core$IFn$_invoke$arity$1(f) : xform.call(null,f));
var c__45940__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_47135){
var state_val_47136 = (state_47135[(1)]);
if((state_val_47136 === (1))){
var inst_47127 = cljs.core.async.reduce(f__$1,init,ch);
var state_47135__$1 = state_47135;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_47135__$1,(2),inst_47127);
} else {
if((state_val_47136 === (2))){
var inst_47130 = (state_47135[(2)]);
var inst_47133 = (f__$1.cljs$core$IFn$_invoke$arity$1 ? f__$1.cljs$core$IFn$_invoke$arity$1(inst_47130) : f__$1.call(null,inst_47130));
var state_47135__$1 = state_47135;
return cljs.core.async.impl.ioc_helpers.return_chan(state_47135__$1,inst_47133);
} else {
return null;
}
}
});
return (function() {
var cljs$core$async$transduce_$_state_machine__45562__auto__ = null;
var cljs$core$async$transduce_$_state_machine__45562__auto____0 = (function (){
var statearr_47152 = [null,null,null,null,null,null,null];
(statearr_47152[(0)] = cljs$core$async$transduce_$_state_machine__45562__auto__);

(statearr_47152[(1)] = (1));

return statearr_47152;
});
var cljs$core$async$transduce_$_state_machine__45562__auto____1 = (function (state_47135){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_47135);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e47153){var ex__45565__auto__ = e47153;
var statearr_47154_49589 = state_47135;
(statearr_47154_49589[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_47135[(4)]))){
var statearr_47157_49590 = state_47135;
(statearr_47157_49590[(1)] = cljs.core.first((state_47135[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49591 = state_47135;
state_47135 = G__49591;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$transduce_$_state_machine__45562__auto__ = function(state_47135){
switch(arguments.length){
case 0:
return cljs$core$async$transduce_$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$transduce_$_state_machine__45562__auto____1.call(this,state_47135);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$transduce_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$transduce_$_state_machine__45562__auto____0;
cljs$core$async$transduce_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$transduce_$_state_machine__45562__auto____1;
return cljs$core$async$transduce_$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_47166 = f__45941__auto__();
(statearr_47166[(6)] = c__45940__auto__);

return statearr_47166;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));

return c__45940__auto__;
});
/**
 * Puts the contents of coll into the supplied channel.
 * 
 *   By default the channel will be closed after the items are copied,
 *   but can be determined by the close? parameter.
 * 
 *   Returns a channel which will close after the items are copied.
 */
cljs.core.async.onto_chan_BANG_ = (function cljs$core$async$onto_chan_BANG_(var_args){
var G__47173 = arguments.length;
switch (G__47173) {
case 2:
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (ch,coll){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,true);
}));

(cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (ch,coll,close_QMARK_){
var c__45940__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_47231){
var state_val_47232 = (state_47231[(1)]);
if((state_val_47232 === (7))){
var inst_47205 = (state_47231[(2)]);
var state_47231__$1 = state_47231;
var statearr_47240_49599 = state_47231__$1;
(statearr_47240_49599[(2)] = inst_47205);

(statearr_47240_49599[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (1))){
var inst_47190 = cljs.core.seq(coll);
var inst_47191 = inst_47190;
var state_47231__$1 = (function (){var statearr_47243 = state_47231;
(statearr_47243[(7)] = inst_47191);

return statearr_47243;
})();
var statearr_47246_49601 = state_47231__$1;
(statearr_47246_49601[(2)] = null);

(statearr_47246_49601[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (4))){
var inst_47191 = (state_47231[(7)]);
var inst_47203 = cljs.core.first(inst_47191);
var state_47231__$1 = state_47231;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_47231__$1,(7),ch,inst_47203);
} else {
if((state_val_47232 === (13))){
var inst_47222 = (state_47231[(2)]);
var state_47231__$1 = state_47231;
var statearr_47250_49607 = state_47231__$1;
(statearr_47250_49607[(2)] = inst_47222);

(statearr_47250_49607[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (6))){
var inst_47208 = (state_47231[(2)]);
var state_47231__$1 = state_47231;
if(cljs.core.truth_(inst_47208)){
var statearr_47256_49608 = state_47231__$1;
(statearr_47256_49608[(1)] = (8));

} else {
var statearr_47257_49609 = state_47231__$1;
(statearr_47257_49609[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (3))){
var inst_47226 = (state_47231[(2)]);
var state_47231__$1 = state_47231;
return cljs.core.async.impl.ioc_helpers.return_chan(state_47231__$1,inst_47226);
} else {
if((state_val_47232 === (12))){
var state_47231__$1 = state_47231;
var statearr_47262_49610 = state_47231__$1;
(statearr_47262_49610[(2)] = null);

(statearr_47262_49610[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (2))){
var inst_47191 = (state_47231[(7)]);
var state_47231__$1 = state_47231;
if(cljs.core.truth_(inst_47191)){
var statearr_47263_49611 = state_47231__$1;
(statearr_47263_49611[(1)] = (4));

} else {
var statearr_47265_49613 = state_47231__$1;
(statearr_47265_49613[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (11))){
var inst_47217 = cljs.core.async.close_BANG_(ch);
var state_47231__$1 = state_47231;
var statearr_47269_49617 = state_47231__$1;
(statearr_47269_49617[(2)] = inst_47217);

(statearr_47269_49617[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (9))){
var state_47231__$1 = state_47231;
if(cljs.core.truth_(close_QMARK_)){
var statearr_47271_49618 = state_47231__$1;
(statearr_47271_49618[(1)] = (11));

} else {
var statearr_47272_49619 = state_47231__$1;
(statearr_47272_49619[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (5))){
var inst_47191 = (state_47231[(7)]);
var state_47231__$1 = state_47231;
var statearr_47273_49621 = state_47231__$1;
(statearr_47273_49621[(2)] = inst_47191);

(statearr_47273_49621[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (10))){
var inst_47224 = (state_47231[(2)]);
var state_47231__$1 = state_47231;
var statearr_47274_49624 = state_47231__$1;
(statearr_47274_49624[(2)] = inst_47224);

(statearr_47274_49624[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47232 === (8))){
var inst_47191 = (state_47231[(7)]);
var inst_47210 = cljs.core.next(inst_47191);
var inst_47191__$1 = inst_47210;
var state_47231__$1 = (function (){var statearr_47277 = state_47231;
(statearr_47277[(7)] = inst_47191__$1);

return statearr_47277;
})();
var statearr_47279_49625 = state_47231__$1;
(statearr_47279_49625[(2)] = null);

(statearr_47279_49625[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_47290 = [null,null,null,null,null,null,null,null];
(statearr_47290[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_47290[(1)] = (1));

return statearr_47290;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_47231){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_47231);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e47292){var ex__45565__auto__ = e47292;
var statearr_47293_49629 = state_47231;
(statearr_47293_49629[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_47231[(4)]))){
var statearr_47294_49630 = state_47231;
(statearr_47294_49630[(1)] = cljs.core.first((state_47231[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49631 = state_47231;
state_47231 = G__49631;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_47231){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_47231);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_47296 = f__45941__auto__();
(statearr_47296[(6)] = c__45940__auto__);

return statearr_47296;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));

return c__45940__auto__;
}));

(cljs.core.async.onto_chan_BANG_.cljs$lang$maxFixedArity = 3);

/**
 * Creates and returns a channel which contains the contents of coll,
 *   closing when exhausted.
 */
cljs.core.async.to_chan_BANG_ = (function cljs$core$async$to_chan_BANG_(coll){
var ch = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(cljs.core.bounded_count((100),coll));
cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2(ch,coll);

return ch;
});
/**
 * Deprecated - use onto-chan!
 */
cljs.core.async.onto_chan = (function cljs$core$async$onto_chan(var_args){
var G__47307 = arguments.length;
switch (G__47307) {
case 2:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2 = (function (ch,coll){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,true);
}));

(cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3 = (function (ch,coll,close_QMARK_){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,close_QMARK_);
}));

(cljs.core.async.onto_chan.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - use to-chan!
 */
cljs.core.async.to_chan = (function cljs$core$async$to_chan(coll){
return cljs.core.async.to_chan_BANG_(coll);
});

/**
 * @interface
 */
cljs.core.async.Mux = function(){};

var cljs$core$async$Mux$muxch_STAR_$dyn_49638 = (function (_){
var x__5498__auto__ = (((_ == null))?null:_);
var m__5499__auto__ = (cljs.core.async.muxch_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5499__auto__.call(null,_));
} else {
var m__5497__auto__ = (cljs.core.async.muxch_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5497__auto__.call(null,_));
} else {
throw cljs.core.missing_protocol("Mux.muxch*",_);
}
}
});
cljs.core.async.muxch_STAR_ = (function cljs$core$async$muxch_STAR_(_){
if((((!((_ == null)))) && ((!((_.cljs$core$async$Mux$muxch_STAR_$arity$1 == null)))))){
return _.cljs$core$async$Mux$muxch_STAR_$arity$1(_);
} else {
return cljs$core$async$Mux$muxch_STAR_$dyn_49638(_);
}
});


/**
 * @interface
 */
cljs.core.async.Mult = function(){};

var cljs$core$async$Mult$tap_STAR_$dyn_49639 = (function (m,ch,close_QMARK_){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.tap_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$3(m,ch,close_QMARK_) : m__5499__auto__.call(null,m,ch,close_QMARK_));
} else {
var m__5497__auto__ = (cljs.core.async.tap_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$3(m,ch,close_QMARK_) : m__5497__auto__.call(null,m,ch,close_QMARK_));
} else {
throw cljs.core.missing_protocol("Mult.tap*",m);
}
}
});
cljs.core.async.tap_STAR_ = (function cljs$core$async$tap_STAR_(m,ch,close_QMARK_){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$tap_STAR_$arity$3 == null)))))){
return m.cljs$core$async$Mult$tap_STAR_$arity$3(m,ch,close_QMARK_);
} else {
return cljs$core$async$Mult$tap_STAR_$dyn_49639(m,ch,close_QMARK_);
}
});

var cljs$core$async$Mult$untap_STAR_$dyn_49642 = (function (m,ch){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.untap_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5499__auto__.call(null,m,ch));
} else {
var m__5497__auto__ = (cljs.core.async.untap_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5497__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mult.untap*",m);
}
}
});
cljs.core.async.untap_STAR_ = (function cljs$core$async$untap_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$untap_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mult$untap_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mult$untap_STAR_$dyn_49642(m,ch);
}
});

var cljs$core$async$Mult$untap_all_STAR_$dyn_49645 = (function (m){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.untap_all_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5499__auto__.call(null,m));
} else {
var m__5497__auto__ = (cljs.core.async.untap_all_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5497__auto__.call(null,m));
} else {
throw cljs.core.missing_protocol("Mult.untap-all*",m);
}
}
});
cljs.core.async.untap_all_STAR_ = (function cljs$core$async$untap_all_STAR_(m){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$untap_all_STAR_$arity$1 == null)))))){
return m.cljs$core$async$Mult$untap_all_STAR_$arity$1(m);
} else {
return cljs$core$async$Mult$untap_all_STAR_$dyn_49645(m);
}
});


/**
* @constructor
 * @implements {cljs.core.async.Mult}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async47358 = (function (ch,cs,meta47359){
this.ch = ch;
this.cs = cs;
this.meta47359 = meta47359;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_47360,meta47359__$1){
var self__ = this;
var _47360__$1 = this;
return (new cljs.core.async.t_cljs$core$async47358(self__.ch,self__.cs,meta47359__$1));
}));

(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_47360){
var self__ = this;
var _47360__$1 = this;
return self__.meta47359;
}));

(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
}));

(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$async$Mult$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$async$Mult$tap_STAR_$arity$3 = (function (_,ch__$1,close_QMARK_){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.cs,cljs.core.assoc,ch__$1,close_QMARK_);

return null;
}));

(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$async$Mult$untap_STAR_$arity$2 = (function (_,ch__$1){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.dissoc,ch__$1);

return null;
}));

(cljs.core.async.t_cljs$core$async47358.prototype.cljs$core$async$Mult$untap_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return null;
}));

(cljs.core.async.t_cljs$core$async47358.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"meta47359","meta47359",-339354011,null)], null);
}));

(cljs.core.async.t_cljs$core$async47358.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async47358.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async47358");

(cljs.core.async.t_cljs$core$async47358.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async47358");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async47358.
 */
cljs.core.async.__GT_t_cljs$core$async47358 = (function cljs$core$async$__GT_t_cljs$core$async47358(ch,cs,meta47359){
return (new cljs.core.async.t_cljs$core$async47358(ch,cs,meta47359));
});


/**
 * Creates and returns a mult(iple) of the supplied channel. Channels
 *   containing copies of the channel can be created with 'tap', and
 *   detached with 'untap'.
 * 
 *   Each item is distributed to all taps in parallel and synchronously,
 *   i.e. each tap must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow taps from holding up the mult.
 * 
 *   Items received when there are no taps get dropped.
 * 
 *   If a tap puts to a closed channel, it will be removed from the mult.
 */
cljs.core.async.mult = (function cljs$core$async$mult(ch){
var cs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var m = (new cljs.core.async.t_cljs$core$async47358(ch,cs,cljs.core.PersistentArrayMap.EMPTY));
var dchan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var dctr = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var done = (function (_){
if((cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(dchan,true);
} else {
return null;
}
});
var c__45940__auto___49660 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_47558){
var state_val_47559 = (state_47558[(1)]);
if((state_val_47559 === (7))){
var inst_47548 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47566_49662 = state_47558__$1;
(statearr_47566_49662[(2)] = inst_47548);

(statearr_47566_49662[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (20))){
var inst_47430 = (state_47558[(7)]);
var inst_47449 = cljs.core.first(inst_47430);
var inst_47450 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_47449,(0),null);
var inst_47451 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_47449,(1),null);
var state_47558__$1 = (function (){var statearr_47567 = state_47558;
(statearr_47567[(8)] = inst_47450);

return statearr_47567;
})();
if(cljs.core.truth_(inst_47451)){
var statearr_47568_49666 = state_47558__$1;
(statearr_47568_49666[(1)] = (22));

} else {
var statearr_47569_49667 = state_47558__$1;
(statearr_47569_49667[(1)] = (23));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (27))){
var inst_47483 = (state_47558[(9)]);
var inst_47485 = (state_47558[(10)]);
var inst_47495 = (state_47558[(11)]);
var inst_47389 = (state_47558[(12)]);
var inst_47495__$1 = cljs.core._nth(inst_47483,inst_47485);
var inst_47496 = cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3(inst_47495__$1,inst_47389,done);
var state_47558__$1 = (function (){var statearr_47571 = state_47558;
(statearr_47571[(11)] = inst_47495__$1);

return statearr_47571;
})();
if(cljs.core.truth_(inst_47496)){
var statearr_47573_49668 = state_47558__$1;
(statearr_47573_49668[(1)] = (30));

} else {
var statearr_47575_49669 = state_47558__$1;
(statearr_47575_49669[(1)] = (31));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (1))){
var state_47558__$1 = state_47558;
var statearr_47579_49670 = state_47558__$1;
(statearr_47579_49670[(2)] = null);

(statearr_47579_49670[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (24))){
var inst_47430 = (state_47558[(7)]);
var inst_47456 = (state_47558[(2)]);
var inst_47457 = cljs.core.next(inst_47430);
var inst_47399 = inst_47457;
var inst_47400 = null;
var inst_47401 = (0);
var inst_47402 = (0);
var state_47558__$1 = (function (){var statearr_47580 = state_47558;
(statearr_47580[(13)] = inst_47456);

(statearr_47580[(14)] = inst_47399);

(statearr_47580[(15)] = inst_47400);

(statearr_47580[(16)] = inst_47401);

(statearr_47580[(17)] = inst_47402);

return statearr_47580;
})();
var statearr_47581_49674 = state_47558__$1;
(statearr_47581_49674[(2)] = null);

(statearr_47581_49674[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (39))){
var state_47558__$1 = state_47558;
var statearr_47590_49675 = state_47558__$1;
(statearr_47590_49675[(2)] = null);

(statearr_47590_49675[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (4))){
var inst_47389 = (state_47558[(12)]);
var inst_47389__$1 = (state_47558[(2)]);
var inst_47390 = (inst_47389__$1 == null);
var state_47558__$1 = (function (){var statearr_47592 = state_47558;
(statearr_47592[(12)] = inst_47389__$1);

return statearr_47592;
})();
if(cljs.core.truth_(inst_47390)){
var statearr_47593_49676 = state_47558__$1;
(statearr_47593_49676[(1)] = (5));

} else {
var statearr_47594_49677 = state_47558__$1;
(statearr_47594_49677[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (15))){
var inst_47402 = (state_47558[(17)]);
var inst_47399 = (state_47558[(14)]);
var inst_47400 = (state_47558[(15)]);
var inst_47401 = (state_47558[(16)]);
var inst_47419 = (state_47558[(2)]);
var inst_47421 = (inst_47402 + (1));
var tmp47584 = inst_47401;
var tmp47585 = inst_47400;
var tmp47586 = inst_47399;
var inst_47399__$1 = tmp47586;
var inst_47400__$1 = tmp47585;
var inst_47401__$1 = tmp47584;
var inst_47402__$1 = inst_47421;
var state_47558__$1 = (function (){var statearr_47597 = state_47558;
(statearr_47597[(18)] = inst_47419);

(statearr_47597[(14)] = inst_47399__$1);

(statearr_47597[(15)] = inst_47400__$1);

(statearr_47597[(16)] = inst_47401__$1);

(statearr_47597[(17)] = inst_47402__$1);

return statearr_47597;
})();
var statearr_47599_49678 = state_47558__$1;
(statearr_47599_49678[(2)] = null);

(statearr_47599_49678[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (21))){
var inst_47460 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47604_49679 = state_47558__$1;
(statearr_47604_49679[(2)] = inst_47460);

(statearr_47604_49679[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (31))){
var inst_47495 = (state_47558[(11)]);
var inst_47499 = m.cljs$core$async$Mult$untap_STAR_$arity$2(null,inst_47495);
var state_47558__$1 = state_47558;
var statearr_47606_49680 = state_47558__$1;
(statearr_47606_49680[(2)] = inst_47499);

(statearr_47606_49680[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (32))){
var inst_47485 = (state_47558[(10)]);
var inst_47482 = (state_47558[(19)]);
var inst_47483 = (state_47558[(9)]);
var inst_47484 = (state_47558[(20)]);
var inst_47501 = (state_47558[(2)]);
var inst_47502 = (inst_47485 + (1));
var tmp47601 = inst_47483;
var tmp47602 = inst_47484;
var tmp47603 = inst_47482;
var inst_47482__$1 = tmp47603;
var inst_47483__$1 = tmp47601;
var inst_47484__$1 = tmp47602;
var inst_47485__$1 = inst_47502;
var state_47558__$1 = (function (){var statearr_47613 = state_47558;
(statearr_47613[(21)] = inst_47501);

(statearr_47613[(19)] = inst_47482__$1);

(statearr_47613[(9)] = inst_47483__$1);

(statearr_47613[(20)] = inst_47484__$1);

(statearr_47613[(10)] = inst_47485__$1);

return statearr_47613;
})();
var statearr_47615_49681 = state_47558__$1;
(statearr_47615_49681[(2)] = null);

(statearr_47615_49681[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (40))){
var inst_47516 = (state_47558[(22)]);
var inst_47520 = m.cljs$core$async$Mult$untap_STAR_$arity$2(null,inst_47516);
var state_47558__$1 = state_47558;
var statearr_47619_49682 = state_47558__$1;
(statearr_47619_49682[(2)] = inst_47520);

(statearr_47619_49682[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (33))){
var inst_47505 = (state_47558[(23)]);
var inst_47509 = cljs.core.chunked_seq_QMARK_(inst_47505);
var state_47558__$1 = state_47558;
if(inst_47509){
var statearr_47621_49683 = state_47558__$1;
(statearr_47621_49683[(1)] = (36));

} else {
var statearr_47622_49684 = state_47558__$1;
(statearr_47622_49684[(1)] = (37));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (13))){
var inst_47411 = (state_47558[(24)]);
var inst_47416 = cljs.core.async.close_BANG_(inst_47411);
var state_47558__$1 = state_47558;
var statearr_47627_49685 = state_47558__$1;
(statearr_47627_49685[(2)] = inst_47416);

(statearr_47627_49685[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (22))){
var inst_47450 = (state_47558[(8)]);
var inst_47453 = cljs.core.async.close_BANG_(inst_47450);
var state_47558__$1 = state_47558;
var statearr_47632_49686 = state_47558__$1;
(statearr_47632_49686[(2)] = inst_47453);

(statearr_47632_49686[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (36))){
var inst_47505 = (state_47558[(23)]);
var inst_47511 = cljs.core.chunk_first(inst_47505);
var inst_47512 = cljs.core.chunk_rest(inst_47505);
var inst_47513 = cljs.core.count(inst_47511);
var inst_47482 = inst_47512;
var inst_47483 = inst_47511;
var inst_47484 = inst_47513;
var inst_47485 = (0);
var state_47558__$1 = (function (){var statearr_47635 = state_47558;
(statearr_47635[(19)] = inst_47482);

(statearr_47635[(9)] = inst_47483);

(statearr_47635[(20)] = inst_47484);

(statearr_47635[(10)] = inst_47485);

return statearr_47635;
})();
var statearr_47638_49690 = state_47558__$1;
(statearr_47638_49690[(2)] = null);

(statearr_47638_49690[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (41))){
var inst_47505 = (state_47558[(23)]);
var inst_47524 = (state_47558[(2)]);
var inst_47525 = cljs.core.next(inst_47505);
var inst_47482 = inst_47525;
var inst_47483 = null;
var inst_47484 = (0);
var inst_47485 = (0);
var state_47558__$1 = (function (){var statearr_47643 = state_47558;
(statearr_47643[(25)] = inst_47524);

(statearr_47643[(19)] = inst_47482);

(statearr_47643[(9)] = inst_47483);

(statearr_47643[(20)] = inst_47484);

(statearr_47643[(10)] = inst_47485);

return statearr_47643;
})();
var statearr_47646_49693 = state_47558__$1;
(statearr_47646_49693[(2)] = null);

(statearr_47646_49693[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (43))){
var state_47558__$1 = state_47558;
var statearr_47649_49694 = state_47558__$1;
(statearr_47649_49694[(2)] = null);

(statearr_47649_49694[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (29))){
var inst_47534 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47651_49696 = state_47558__$1;
(statearr_47651_49696[(2)] = inst_47534);

(statearr_47651_49696[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (44))){
var inst_47544 = (state_47558[(2)]);
var state_47558__$1 = (function (){var statearr_47656 = state_47558;
(statearr_47656[(26)] = inst_47544);

return statearr_47656;
})();
var statearr_47657_49697 = state_47558__$1;
(statearr_47657_49697[(2)] = null);

(statearr_47657_49697[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (6))){
var inst_47474 = (state_47558[(27)]);
var inst_47473 = cljs.core.deref(cs);
var inst_47474__$1 = cljs.core.keys(inst_47473);
var inst_47475 = cljs.core.count(inst_47474__$1);
var inst_47476 = cljs.core.reset_BANG_(dctr,inst_47475);
var inst_47481 = cljs.core.seq(inst_47474__$1);
var inst_47482 = inst_47481;
var inst_47483 = null;
var inst_47484 = (0);
var inst_47485 = (0);
var state_47558__$1 = (function (){var statearr_47662 = state_47558;
(statearr_47662[(27)] = inst_47474__$1);

(statearr_47662[(28)] = inst_47476);

(statearr_47662[(19)] = inst_47482);

(statearr_47662[(9)] = inst_47483);

(statearr_47662[(20)] = inst_47484);

(statearr_47662[(10)] = inst_47485);

return statearr_47662;
})();
var statearr_47664_49701 = state_47558__$1;
(statearr_47664_49701[(2)] = null);

(statearr_47664_49701[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (28))){
var inst_47482 = (state_47558[(19)]);
var inst_47505 = (state_47558[(23)]);
var inst_47505__$1 = cljs.core.seq(inst_47482);
var state_47558__$1 = (function (){var statearr_47666 = state_47558;
(statearr_47666[(23)] = inst_47505__$1);

return statearr_47666;
})();
if(inst_47505__$1){
var statearr_47668_49709 = state_47558__$1;
(statearr_47668_49709[(1)] = (33));

} else {
var statearr_47669_49710 = state_47558__$1;
(statearr_47669_49710[(1)] = (34));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (25))){
var inst_47485 = (state_47558[(10)]);
var inst_47484 = (state_47558[(20)]);
var inst_47491 = (inst_47485 < inst_47484);
var inst_47492 = inst_47491;
var state_47558__$1 = state_47558;
if(cljs.core.truth_(inst_47492)){
var statearr_47676_49713 = state_47558__$1;
(statearr_47676_49713[(1)] = (27));

} else {
var statearr_47677_49714 = state_47558__$1;
(statearr_47677_49714[(1)] = (28));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (34))){
var state_47558__$1 = state_47558;
var statearr_47679_49716 = state_47558__$1;
(statearr_47679_49716[(2)] = null);

(statearr_47679_49716[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (17))){
var state_47558__$1 = state_47558;
var statearr_47681_49717 = state_47558__$1;
(statearr_47681_49717[(2)] = null);

(statearr_47681_49717[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (3))){
var inst_47550 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
return cljs.core.async.impl.ioc_helpers.return_chan(state_47558__$1,inst_47550);
} else {
if((state_val_47559 === (12))){
var inst_47465 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47688_49718 = state_47558__$1;
(statearr_47688_49718[(2)] = inst_47465);

(statearr_47688_49718[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (2))){
var state_47558__$1 = state_47558;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_47558__$1,(4),ch);
} else {
if((state_val_47559 === (23))){
var state_47558__$1 = state_47558;
var statearr_47690_49719 = state_47558__$1;
(statearr_47690_49719[(2)] = null);

(statearr_47690_49719[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (35))){
var inst_47531 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47692_49720 = state_47558__$1;
(statearr_47692_49720[(2)] = inst_47531);

(statearr_47692_49720[(1)] = (29));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (19))){
var inst_47430 = (state_47558[(7)]);
var inst_47439 = cljs.core.chunk_first(inst_47430);
var inst_47441 = cljs.core.chunk_rest(inst_47430);
var inst_47442 = cljs.core.count(inst_47439);
var inst_47399 = inst_47441;
var inst_47400 = inst_47439;
var inst_47401 = inst_47442;
var inst_47402 = (0);
var state_47558__$1 = (function (){var statearr_47694 = state_47558;
(statearr_47694[(14)] = inst_47399);

(statearr_47694[(15)] = inst_47400);

(statearr_47694[(16)] = inst_47401);

(statearr_47694[(17)] = inst_47402);

return statearr_47694;
})();
var statearr_47695_49721 = state_47558__$1;
(statearr_47695_49721[(2)] = null);

(statearr_47695_49721[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (11))){
var inst_47399 = (state_47558[(14)]);
var inst_47430 = (state_47558[(7)]);
var inst_47430__$1 = cljs.core.seq(inst_47399);
var state_47558__$1 = (function (){var statearr_47697 = state_47558;
(statearr_47697[(7)] = inst_47430__$1);

return statearr_47697;
})();
if(inst_47430__$1){
var statearr_47700_49722 = state_47558__$1;
(statearr_47700_49722[(1)] = (16));

} else {
var statearr_47701_49723 = state_47558__$1;
(statearr_47701_49723[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (9))){
var inst_47467 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47704_49726 = state_47558__$1;
(statearr_47704_49726[(2)] = inst_47467);

(statearr_47704_49726[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (5))){
var inst_47396 = cljs.core.deref(cs);
var inst_47397 = cljs.core.seq(inst_47396);
var inst_47399 = inst_47397;
var inst_47400 = null;
var inst_47401 = (0);
var inst_47402 = (0);
var state_47558__$1 = (function (){var statearr_47705 = state_47558;
(statearr_47705[(14)] = inst_47399);

(statearr_47705[(15)] = inst_47400);

(statearr_47705[(16)] = inst_47401);

(statearr_47705[(17)] = inst_47402);

return statearr_47705;
})();
var statearr_47707_49729 = state_47558__$1;
(statearr_47707_49729[(2)] = null);

(statearr_47707_49729[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (14))){
var state_47558__$1 = state_47558;
var statearr_47708_49733 = state_47558__$1;
(statearr_47708_49733[(2)] = null);

(statearr_47708_49733[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (45))){
var inst_47541 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47709_49734 = state_47558__$1;
(statearr_47709_49734[(2)] = inst_47541);

(statearr_47709_49734[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (26))){
var inst_47474 = (state_47558[(27)]);
var inst_47536 = (state_47558[(2)]);
var inst_47538 = cljs.core.seq(inst_47474);
var state_47558__$1 = (function (){var statearr_47712 = state_47558;
(statearr_47712[(29)] = inst_47536);

return statearr_47712;
})();
if(inst_47538){
var statearr_47713_49736 = state_47558__$1;
(statearr_47713_49736[(1)] = (42));

} else {
var statearr_47714_49737 = state_47558__$1;
(statearr_47714_49737[(1)] = (43));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (16))){
var inst_47430 = (state_47558[(7)]);
var inst_47437 = cljs.core.chunked_seq_QMARK_(inst_47430);
var state_47558__$1 = state_47558;
if(inst_47437){
var statearr_47716_49738 = state_47558__$1;
(statearr_47716_49738[(1)] = (19));

} else {
var statearr_47718_49741 = state_47558__$1;
(statearr_47718_49741[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (38))){
var inst_47528 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47723_49742 = state_47558__$1;
(statearr_47723_49742[(2)] = inst_47528);

(statearr_47723_49742[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (30))){
var state_47558__$1 = state_47558;
var statearr_47724_49744 = state_47558__$1;
(statearr_47724_49744[(2)] = null);

(statearr_47724_49744[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (10))){
var inst_47400 = (state_47558[(15)]);
var inst_47402 = (state_47558[(17)]);
var inst_47410 = cljs.core._nth(inst_47400,inst_47402);
var inst_47411 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_47410,(0),null);
var inst_47413 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_47410,(1),null);
var state_47558__$1 = (function (){var statearr_47727 = state_47558;
(statearr_47727[(24)] = inst_47411);

return statearr_47727;
})();
if(cljs.core.truth_(inst_47413)){
var statearr_47728_49749 = state_47558__$1;
(statearr_47728_49749[(1)] = (13));

} else {
var statearr_47730_49753 = state_47558__$1;
(statearr_47730_49753[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (18))){
var inst_47463 = (state_47558[(2)]);
var state_47558__$1 = state_47558;
var statearr_47733_49754 = state_47558__$1;
(statearr_47733_49754[(2)] = inst_47463);

(statearr_47733_49754[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (42))){
var state_47558__$1 = state_47558;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_47558__$1,(45),dchan);
} else {
if((state_val_47559 === (37))){
var inst_47505 = (state_47558[(23)]);
var inst_47516 = (state_47558[(22)]);
var inst_47389 = (state_47558[(12)]);
var inst_47516__$1 = cljs.core.first(inst_47505);
var inst_47517 = cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3(inst_47516__$1,inst_47389,done);
var state_47558__$1 = (function (){var statearr_47735 = state_47558;
(statearr_47735[(22)] = inst_47516__$1);

return statearr_47735;
})();
if(cljs.core.truth_(inst_47517)){
var statearr_47736_49757 = state_47558__$1;
(statearr_47736_49757[(1)] = (39));

} else {
var statearr_47738_49758 = state_47558__$1;
(statearr_47738_49758[(1)] = (40));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47559 === (8))){
var inst_47402 = (state_47558[(17)]);
var inst_47401 = (state_47558[(16)]);
var inst_47404 = (inst_47402 < inst_47401);
var inst_47405 = inst_47404;
var state_47558__$1 = state_47558;
if(cljs.core.truth_(inst_47405)){
var statearr_47739_49761 = state_47558__$1;
(statearr_47739_49761[(1)] = (10));

} else {
var statearr_47740_49762 = state_47558__$1;
(statearr_47740_49762[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
});
return (function() {
var cljs$core$async$mult_$_state_machine__45562__auto__ = null;
var cljs$core$async$mult_$_state_machine__45562__auto____0 = (function (){
var statearr_47743 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_47743[(0)] = cljs$core$async$mult_$_state_machine__45562__auto__);

(statearr_47743[(1)] = (1));

return statearr_47743;
});
var cljs$core$async$mult_$_state_machine__45562__auto____1 = (function (state_47558){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_47558);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e47744){var ex__45565__auto__ = e47744;
var statearr_47746_49766 = state_47558;
(statearr_47746_49766[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_47558[(4)]))){
var statearr_47749_49767 = state_47558;
(statearr_47749_49767[(1)] = cljs.core.first((state_47558[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49768 = state_47558;
state_47558 = G__49768;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$mult_$_state_machine__45562__auto__ = function(state_47558){
switch(arguments.length){
case 0:
return cljs$core$async$mult_$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$mult_$_state_machine__45562__auto____1.call(this,state_47558);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mult_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mult_$_state_machine__45562__auto____0;
cljs$core$async$mult_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mult_$_state_machine__45562__auto____1;
return cljs$core$async$mult_$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_47754 = f__45941__auto__();
(statearr_47754[(6)] = c__45940__auto___49660);

return statearr_47754;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return m;
});
/**
 * Copies the mult source onto the supplied channel.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.tap = (function cljs$core$async$tap(var_args){
var G__47764 = arguments.length;
switch (G__47764) {
case 2:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2 = (function (mult,ch){
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3(mult,ch,true);
}));

(cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3 = (function (mult,ch,close_QMARK_){
cljs.core.async.tap_STAR_(mult,ch,close_QMARK_);

return ch;
}));

(cljs.core.async.tap.cljs$lang$maxFixedArity = 3);

/**
 * Disconnects a target channel from a mult
 */
cljs.core.async.untap = (function cljs$core$async$untap(mult,ch){
return cljs.core.async.untap_STAR_(mult,ch);
});
/**
 * Disconnects all target channels from a mult
 */
cljs.core.async.untap_all = (function cljs$core$async$untap_all(mult){
return cljs.core.async.untap_all_STAR_(mult);
});

/**
 * @interface
 */
cljs.core.async.Mix = function(){};

var cljs$core$async$Mix$admix_STAR_$dyn_49779 = (function (m,ch){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.admix_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5499__auto__.call(null,m,ch));
} else {
var m__5497__auto__ = (cljs.core.async.admix_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5497__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mix.admix*",m);
}
}
});
cljs.core.async.admix_STAR_ = (function cljs$core$async$admix_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$admix_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$admix_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mix$admix_STAR_$dyn_49779(m,ch);
}
});

var cljs$core$async$Mix$unmix_STAR_$dyn_49784 = (function (m,ch){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.unmix_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5499__auto__.call(null,m,ch));
} else {
var m__5497__auto__ = (cljs.core.async.unmix_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5497__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mix.unmix*",m);
}
}
});
cljs.core.async.unmix_STAR_ = (function cljs$core$async$unmix_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$unmix_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$unmix_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mix$unmix_STAR_$dyn_49784(m,ch);
}
});

var cljs$core$async$Mix$unmix_all_STAR_$dyn_49793 = (function (m){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.unmix_all_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5499__auto__.call(null,m));
} else {
var m__5497__auto__ = (cljs.core.async.unmix_all_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5497__auto__.call(null,m));
} else {
throw cljs.core.missing_protocol("Mix.unmix-all*",m);
}
}
});
cljs.core.async.unmix_all_STAR_ = (function cljs$core$async$unmix_all_STAR_(m){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$unmix_all_STAR_$arity$1 == null)))))){
return m.cljs$core$async$Mix$unmix_all_STAR_$arity$1(m);
} else {
return cljs$core$async$Mix$unmix_all_STAR_$dyn_49793(m);
}
});

var cljs$core$async$Mix$toggle_STAR_$dyn_49798 = (function (m,state_map){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.toggle_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(m,state_map) : m__5499__auto__.call(null,m,state_map));
} else {
var m__5497__auto__ = (cljs.core.async.toggle_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(m,state_map) : m__5497__auto__.call(null,m,state_map));
} else {
throw cljs.core.missing_protocol("Mix.toggle*",m);
}
}
});
cljs.core.async.toggle_STAR_ = (function cljs$core$async$toggle_STAR_(m,state_map){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$toggle_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$toggle_STAR_$arity$2(m,state_map);
} else {
return cljs$core$async$Mix$toggle_STAR_$dyn_49798(m,state_map);
}
});

var cljs$core$async$Mix$solo_mode_STAR_$dyn_49800 = (function (m,mode){
var x__5498__auto__ = (((m == null))?null:m);
var m__5499__auto__ = (cljs.core.async.solo_mode_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(m,mode) : m__5499__auto__.call(null,m,mode));
} else {
var m__5497__auto__ = (cljs.core.async.solo_mode_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(m,mode) : m__5497__auto__.call(null,m,mode));
} else {
throw cljs.core.missing_protocol("Mix.solo-mode*",m);
}
}
});
cljs.core.async.solo_mode_STAR_ = (function cljs$core$async$solo_mode_STAR_(m,mode){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$solo_mode_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$solo_mode_STAR_$arity$2(m,mode);
} else {
return cljs$core$async$Mix$solo_mode_STAR_$dyn_49800(m,mode);
}
});

cljs.core.async.ioc_alts_BANG_ = (function cljs$core$async$ioc_alts_BANG_(var_args){
var args__5882__auto__ = [];
var len__5876__auto___49805 = arguments.length;
var i__5877__auto___49806 = (0);
while(true){
if((i__5877__auto___49806 < len__5876__auto___49805)){
args__5882__auto__.push((arguments[i__5877__auto___49806]));

var G__49809 = (i__5877__auto___49806 + (1));
i__5877__auto___49806 = G__49809;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((3) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((3)),(0),null)):null);
return cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__5883__auto__);
});

(cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (state,cont_block,ports,p__47822){
var map__47823 = p__47822;
var map__47823__$1 = cljs.core.__destructure_map(map__47823);
var opts = map__47823__$1;
var statearr_47824_49811 = state;
(statearr_47824_49811[(1)] = cont_block);


var temp__5825__auto__ = cljs.core.async.do_alts((function (val){
var statearr_47827_49812 = state;
(statearr_47827_49812[(2)] = val);


return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state);
}),ports,opts);
if(cljs.core.truth_(temp__5825__auto__)){
var cb = temp__5825__auto__;
var statearr_47829_49814 = state;
(statearr_47829_49814[(2)] = cljs.core.deref(cb));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}));

(cljs.core.async.ioc_alts_BANG_.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(cljs.core.async.ioc_alts_BANG_.cljs$lang$applyTo = (function (seq47809){
var G__47810 = cljs.core.first(seq47809);
var seq47809__$1 = cljs.core.next(seq47809);
var G__47811 = cljs.core.first(seq47809__$1);
var seq47809__$2 = cljs.core.next(seq47809__$1);
var G__47812 = cljs.core.first(seq47809__$2);
var seq47809__$3 = cljs.core.next(seq47809__$2);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__47810,G__47811,G__47812,seq47809__$3);
}));


/**
* @constructor
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mix}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async47843 = (function (change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta47844){
this.change = change;
this.solo_mode = solo_mode;
this.pick = pick;
this.cs = cs;
this.calc_state = calc_state;
this.out = out;
this.changed = changed;
this.solo_modes = solo_modes;
this.attrs = attrs;
this.meta47844 = meta47844;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_47845,meta47844__$1){
var self__ = this;
var _47845__$1 = this;
return (new cljs.core.async.t_cljs$core$async47843(self__.change,self__.solo_mode,self__.pick,self__.cs,self__.calc_state,self__.out,self__.changed,self__.solo_modes,self__.attrs,meta47844__$1));
}));

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_47845){
var self__ = this;
var _47845__$1 = this;
return self__.meta47844;
}));

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.out;
}));

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mix$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mix$admix_STAR_$arity$2 = (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.cs,cljs.core.assoc,ch,cljs.core.PersistentArrayMap.EMPTY);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mix$unmix_STAR_$arity$2 = (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.dissoc,ch);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mix$unmix_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mix$toggle_STAR_$arity$2 = (function (_,state_map){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(cljs.core.merge_with,cljs.core.merge),state_map);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async47843.prototype.cljs$core$async$Mix$solo_mode_STAR_$arity$2 = (function (_,mode){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_((self__.solo_modes.cljs$core$IFn$_invoke$arity$1 ? self__.solo_modes.cljs$core$IFn$_invoke$arity$1(mode) : self__.solo_modes.call(null,mode)))){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"mode must be one of: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(self__.solo_modes)))+"\n"+"(solo-modes mode)")));
}

cljs.core.reset_BANG_(self__.solo_mode,mode);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async47843.getBasis = (function (){
return new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"change","change",477485025,null),new cljs.core.Symbol(null,"solo-mode","solo-mode",2031788074,null),new cljs.core.Symbol(null,"pick","pick",1300068175,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"calc-state","calc-state",-349968968,null),new cljs.core.Symbol(null,"out","out",729986010,null),new cljs.core.Symbol(null,"changed","changed",-2083710852,null),new cljs.core.Symbol(null,"solo-modes","solo-modes",882180540,null),new cljs.core.Symbol(null,"attrs","attrs",-450137186,null),new cljs.core.Symbol(null,"meta47844","meta47844",1449625420,null)], null);
}));

(cljs.core.async.t_cljs$core$async47843.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async47843.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async47843");

(cljs.core.async.t_cljs$core$async47843.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async47843");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async47843.
 */
cljs.core.async.__GT_t_cljs$core$async47843 = (function cljs$core$async$__GT_t_cljs$core$async47843(change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta47844){
return (new cljs.core.async.t_cljs$core$async47843(change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta47844));
});


/**
 * Creates and returns a mix of one or more input channels which will
 *   be put on the supplied out channel. Input sources can be added to
 *   the mix with 'admix', and removed with 'unmix'. A mix supports
 *   soloing, muting and pausing multiple inputs atomically using
 *   'toggle', and can solo using either muting or pausing as determined
 *   by 'solo-mode'.
 * 
 *   Each channel can have zero or more boolean modes set via 'toggle':
 * 
 *   :solo - when true, only this (ond other soloed) channel(s) will appear
 *        in the mix output channel. :mute and :pause states of soloed
 *        channels are ignored. If solo-mode is :mute, non-soloed
 *        channels are muted, if :pause, non-soloed channels are
 *        paused.
 * 
 *   :mute - muted channels will have their contents consumed but not included in the mix
 *   :pause - paused channels will not have their contents consumed (and thus also not included in the mix)
 */
cljs.core.async.mix = (function cljs$core$async$mix(out){
var cs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var solo_modes = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pause","pause",-2095325672),null,new cljs.core.Keyword(null,"mute","mute",1151223646),null], null), null);
var attrs = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(solo_modes,new cljs.core.Keyword(null,"solo","solo",-316350075));
var solo_mode = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"mute","mute",1151223646));
var change = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(cljs.core.async.sliding_buffer((1)));
var changed = (function (){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(change,true);
});
var pick = (function (attr,chs){
return cljs.core.reduce_kv((function (ret,c,v){
if(cljs.core.truth_((attr.cljs$core$IFn$_invoke$arity$1 ? attr.cljs$core$IFn$_invoke$arity$1(v) : attr.call(null,v)))){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ret,c);
} else {
return ret;
}
}),cljs.core.PersistentHashSet.EMPTY,chs);
});
var calc_state = (function (){
var chs = cljs.core.deref(cs);
var mode = cljs.core.deref(solo_mode);
var solos = pick(new cljs.core.Keyword(null,"solo","solo",-316350075),chs);
var pauses = pick(new cljs.core.Keyword(null,"pause","pause",-2095325672),chs);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"solos","solos",1441458643),solos,new cljs.core.Keyword(null,"mutes","mutes",1068806309),pick(new cljs.core.Keyword(null,"mute","mute",1151223646),chs),new cljs.core.Keyword(null,"reads","reads",-1215067361),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,new cljs.core.Keyword(null,"pause","pause",-2095325672))) && (cljs.core.seq(solos))))?cljs.core.vec(solos):cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(pauses,cljs.core.keys(chs)))),change)], null);
});
var m = (new cljs.core.async.t_cljs$core$async47843(change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,cljs.core.PersistentArrayMap.EMPTY));
var c__45940__auto___49846 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_47949){
var state_val_47950 = (state_47949[(1)]);
if((state_val_47950 === (7))){
var inst_47903 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
if(cljs.core.truth_(inst_47903)){
var statearr_47953_49848 = state_47949__$1;
(statearr_47953_49848[(1)] = (8));

} else {
var statearr_47954_49849 = state_47949__$1;
(statearr_47954_49849[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (20))){
var inst_47894 = (state_47949[(7)]);
var state_47949__$1 = state_47949;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_47949__$1,(23),out,inst_47894);
} else {
if((state_val_47950 === (1))){
var inst_47876 = calc_state();
var inst_47877 = cljs.core.__destructure_map(inst_47876);
var inst_47878 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_47877,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_47879 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_47877,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_47880 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_47877,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var inst_47881 = inst_47876;
var state_47949__$1 = (function (){var statearr_47959 = state_47949;
(statearr_47959[(8)] = inst_47878);

(statearr_47959[(9)] = inst_47879);

(statearr_47959[(10)] = inst_47880);

(statearr_47959[(11)] = inst_47881);

return statearr_47959;
})();
var statearr_47961_49852 = state_47949__$1;
(statearr_47961_49852[(2)] = null);

(statearr_47961_49852[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (24))){
var inst_47884 = (state_47949[(12)]);
var inst_47881 = inst_47884;
var state_47949__$1 = (function (){var statearr_47963 = state_47949;
(statearr_47963[(11)] = inst_47881);

return statearr_47963;
})();
var statearr_47964_49857 = state_47949__$1;
(statearr_47964_49857[(2)] = null);

(statearr_47964_49857[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (4))){
var inst_47894 = (state_47949[(7)]);
var inst_47897 = (state_47949[(13)]);
var inst_47893 = (state_47949[(2)]);
var inst_47894__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_47893,(0),null);
var inst_47895 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_47893,(1),null);
var inst_47897__$1 = (inst_47894__$1 == null);
var state_47949__$1 = (function (){var statearr_47965 = state_47949;
(statearr_47965[(7)] = inst_47894__$1);

(statearr_47965[(14)] = inst_47895);

(statearr_47965[(13)] = inst_47897__$1);

return statearr_47965;
})();
if(cljs.core.truth_(inst_47897__$1)){
var statearr_47966_49858 = state_47949__$1;
(statearr_47966_49858[(1)] = (5));

} else {
var statearr_47967_49859 = state_47949__$1;
(statearr_47967_49859[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (15))){
var inst_47885 = (state_47949[(15)]);
var inst_47917 = (state_47949[(16)]);
var inst_47917__$1 = cljs.core.empty_QMARK_(inst_47885);
var state_47949__$1 = (function (){var statearr_47970 = state_47949;
(statearr_47970[(16)] = inst_47917__$1);

return statearr_47970;
})();
if(inst_47917__$1){
var statearr_47972_49862 = state_47949__$1;
(statearr_47972_49862[(1)] = (17));

} else {
var statearr_47974_49863 = state_47949__$1;
(statearr_47974_49863[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (21))){
var inst_47884 = (state_47949[(12)]);
var inst_47881 = inst_47884;
var state_47949__$1 = (function (){var statearr_47975 = state_47949;
(statearr_47975[(11)] = inst_47881);

return statearr_47975;
})();
var statearr_47976_49864 = state_47949__$1;
(statearr_47976_49864[(2)] = null);

(statearr_47976_49864[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (13))){
var inst_47910 = (state_47949[(2)]);
var inst_47911 = calc_state();
var inst_47881 = inst_47911;
var state_47949__$1 = (function (){var statearr_47977 = state_47949;
(statearr_47977[(17)] = inst_47910);

(statearr_47977[(11)] = inst_47881);

return statearr_47977;
})();
var statearr_47978_49865 = state_47949__$1;
(statearr_47978_49865[(2)] = null);

(statearr_47978_49865[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (22))){
var inst_47943 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
var statearr_47980_49866 = state_47949__$1;
(statearr_47980_49866[(2)] = inst_47943);

(statearr_47980_49866[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (6))){
var inst_47895 = (state_47949[(14)]);
var inst_47901 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_47895,change);
var state_47949__$1 = state_47949;
var statearr_47981_49867 = state_47949__$1;
(statearr_47981_49867[(2)] = inst_47901);

(statearr_47981_49867[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (25))){
var state_47949__$1 = state_47949;
var statearr_47983_49868 = state_47949__$1;
(statearr_47983_49868[(2)] = null);

(statearr_47983_49868[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (17))){
var inst_47886 = (state_47949[(18)]);
var inst_47895 = (state_47949[(14)]);
var inst_47920 = (inst_47886.cljs$core$IFn$_invoke$arity$1 ? inst_47886.cljs$core$IFn$_invoke$arity$1(inst_47895) : inst_47886.call(null,inst_47895));
var inst_47921 = cljs.core.not(inst_47920);
var state_47949__$1 = state_47949;
var statearr_47984_49870 = state_47949__$1;
(statearr_47984_49870[(2)] = inst_47921);

(statearr_47984_49870[(1)] = (19));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (3))){
var inst_47947 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
return cljs.core.async.impl.ioc_helpers.return_chan(state_47949__$1,inst_47947);
} else {
if((state_val_47950 === (12))){
var state_47949__$1 = state_47949;
var statearr_47989_49871 = state_47949__$1;
(statearr_47989_49871[(2)] = null);

(statearr_47989_49871[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (2))){
var inst_47881 = (state_47949[(11)]);
var inst_47884 = (state_47949[(12)]);
var inst_47884__$1 = cljs.core.__destructure_map(inst_47881);
var inst_47885 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_47884__$1,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_47886 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_47884__$1,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_47887 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_47884__$1,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var state_47949__$1 = (function (){var statearr_47991 = state_47949;
(statearr_47991[(12)] = inst_47884__$1);

(statearr_47991[(15)] = inst_47885);

(statearr_47991[(18)] = inst_47886);

return statearr_47991;
})();
return cljs.core.async.ioc_alts_BANG_(state_47949__$1,(4),inst_47887);
} else {
if((state_val_47950 === (23))){
var inst_47932 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
if(cljs.core.truth_(inst_47932)){
var statearr_47992_49875 = state_47949__$1;
(statearr_47992_49875[(1)] = (24));

} else {
var statearr_47993_49877 = state_47949__$1;
(statearr_47993_49877[(1)] = (25));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (19))){
var inst_47924 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
var statearr_47995_49878 = state_47949__$1;
(statearr_47995_49878[(2)] = inst_47924);

(statearr_47995_49878[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (11))){
var inst_47895 = (state_47949[(14)]);
var inst_47907 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(cs,cljs.core.dissoc,inst_47895);
var state_47949__$1 = state_47949;
var statearr_47996_49880 = state_47949__$1;
(statearr_47996_49880[(2)] = inst_47907);

(statearr_47996_49880[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (9))){
var inst_47885 = (state_47949[(15)]);
var inst_47895 = (state_47949[(14)]);
var inst_47914 = (state_47949[(19)]);
var inst_47914__$1 = (inst_47885.cljs$core$IFn$_invoke$arity$1 ? inst_47885.cljs$core$IFn$_invoke$arity$1(inst_47895) : inst_47885.call(null,inst_47895));
var state_47949__$1 = (function (){var statearr_47997 = state_47949;
(statearr_47997[(19)] = inst_47914__$1);

return statearr_47997;
})();
if(cljs.core.truth_(inst_47914__$1)){
var statearr_47999_49881 = state_47949__$1;
(statearr_47999_49881[(1)] = (14));

} else {
var statearr_48000_49882 = state_47949__$1;
(statearr_48000_49882[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (5))){
var inst_47897 = (state_47949[(13)]);
var state_47949__$1 = state_47949;
var statearr_48002_49883 = state_47949__$1;
(statearr_48002_49883[(2)] = inst_47897);

(statearr_48002_49883[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (14))){
var inst_47914 = (state_47949[(19)]);
var state_47949__$1 = state_47949;
var statearr_48003_49885 = state_47949__$1;
(statearr_48003_49885[(2)] = inst_47914);

(statearr_48003_49885[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (26))){
var inst_47939 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
var statearr_48005_49890 = state_47949__$1;
(statearr_48005_49890[(2)] = inst_47939);

(statearr_48005_49890[(1)] = (22));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (16))){
var inst_47926 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
if(cljs.core.truth_(inst_47926)){
var statearr_48006_49891 = state_47949__$1;
(statearr_48006_49891[(1)] = (20));

} else {
var statearr_48007_49895 = state_47949__$1;
(statearr_48007_49895[(1)] = (21));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (10))){
var inst_47945 = (state_47949[(2)]);
var state_47949__$1 = state_47949;
var statearr_48009_49897 = state_47949__$1;
(statearr_48009_49897[(2)] = inst_47945);

(statearr_48009_49897[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (18))){
var inst_47917 = (state_47949[(16)]);
var state_47949__$1 = state_47949;
var statearr_48014_49899 = state_47949__$1;
(statearr_48014_49899[(2)] = inst_47917);

(statearr_48014_49899[(1)] = (19));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_47950 === (8))){
var inst_47894 = (state_47949[(7)]);
var inst_47905 = (inst_47894 == null);
var state_47949__$1 = state_47949;
if(cljs.core.truth_(inst_47905)){
var statearr_48015_49900 = state_47949__$1;
(statearr_48015_49900[(1)] = (11));

} else {
var statearr_48016_49901 = state_47949__$1;
(statearr_48016_49901[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
}
}
});
return (function() {
var cljs$core$async$mix_$_state_machine__45562__auto__ = null;
var cljs$core$async$mix_$_state_machine__45562__auto____0 = (function (){
var statearr_48018 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_48018[(0)] = cljs$core$async$mix_$_state_machine__45562__auto__);

(statearr_48018[(1)] = (1));

return statearr_48018;
});
var cljs$core$async$mix_$_state_machine__45562__auto____1 = (function (state_47949){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_47949);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e48023){var ex__45565__auto__ = e48023;
var statearr_48024_49905 = state_47949;
(statearr_48024_49905[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_47949[(4)]))){
var statearr_48025_49906 = state_47949;
(statearr_48025_49906[(1)] = cljs.core.first((state_47949[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__49908 = state_47949;
state_47949 = G__49908;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$mix_$_state_machine__45562__auto__ = function(state_47949){
switch(arguments.length){
case 0:
return cljs$core$async$mix_$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$mix_$_state_machine__45562__auto____1.call(this,state_47949);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mix_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mix_$_state_machine__45562__auto____0;
cljs$core$async$mix_$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mix_$_state_machine__45562__auto____1;
return cljs$core$async$mix_$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_48027 = f__45941__auto__();
(statearr_48027[(6)] = c__45940__auto___49846);

return statearr_48027;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return m;
});
/**
 * Adds ch as an input to the mix
 */
cljs.core.async.admix = (function cljs$core$async$admix(mix,ch){
return cljs.core.async.admix_STAR_(mix,ch);
});
/**
 * Removes ch as an input to the mix
 */
cljs.core.async.unmix = (function cljs$core$async$unmix(mix,ch){
return cljs.core.async.unmix_STAR_(mix,ch);
});
/**
 * removes all inputs from the mix
 */
cljs.core.async.unmix_all = (function cljs$core$async$unmix_all(mix){
return cljs.core.async.unmix_all_STAR_(mix);
});
/**
 * Atomically sets the state(s) of one or more channels in a mix. The
 *   state map is a map of channels -> channel-state-map. A
 *   channel-state-map is a map of attrs -> boolean, where attr is one or
 *   more of :mute, :pause or :solo. Any states supplied are merged with
 *   the current state.
 * 
 *   Note that channels can be added to a mix via toggle, which can be
 *   used to add channels in a particular (e.g. paused) state.
 */
cljs.core.async.toggle = (function cljs$core$async$toggle(mix,state_map){
return cljs.core.async.toggle_STAR_(mix,state_map);
});
/**
 * Sets the solo mode of the mix. mode must be one of :mute or :pause
 */
cljs.core.async.solo_mode = (function cljs$core$async$solo_mode(mix,mode){
return cljs.core.async.solo_mode_STAR_(mix,mode);
});

/**
 * @interface
 */
cljs.core.async.Pub = function(){};

var cljs$core$async$Pub$sub_STAR_$dyn_49911 = (function (p,v,ch,close_QMARK_){
var x__5498__auto__ = (((p == null))?null:p);
var m__5499__auto__ = (cljs.core.async.sub_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$4 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$4(p,v,ch,close_QMARK_) : m__5499__auto__.call(null,p,v,ch,close_QMARK_));
} else {
var m__5497__auto__ = (cljs.core.async.sub_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$4 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$4(p,v,ch,close_QMARK_) : m__5497__auto__.call(null,p,v,ch,close_QMARK_));
} else {
throw cljs.core.missing_protocol("Pub.sub*",p);
}
}
});
cljs.core.async.sub_STAR_ = (function cljs$core$async$sub_STAR_(p,v,ch,close_QMARK_){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$sub_STAR_$arity$4 == null)))))){
return p.cljs$core$async$Pub$sub_STAR_$arity$4(p,v,ch,close_QMARK_);
} else {
return cljs$core$async$Pub$sub_STAR_$dyn_49911(p,v,ch,close_QMARK_);
}
});

var cljs$core$async$Pub$unsub_STAR_$dyn_49914 = (function (p,v,ch){
var x__5498__auto__ = (((p == null))?null:p);
var m__5499__auto__ = (cljs.core.async.unsub_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$3(p,v,ch) : m__5499__auto__.call(null,p,v,ch));
} else {
var m__5497__auto__ = (cljs.core.async.unsub_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$3(p,v,ch) : m__5497__auto__.call(null,p,v,ch));
} else {
throw cljs.core.missing_protocol("Pub.unsub*",p);
}
}
});
cljs.core.async.unsub_STAR_ = (function cljs$core$async$unsub_STAR_(p,v,ch){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_STAR_$arity$3 == null)))))){
return p.cljs$core$async$Pub$unsub_STAR_$arity$3(p,v,ch);
} else {
return cljs$core$async$Pub$unsub_STAR_$dyn_49914(p,v,ch);
}
});

var cljs$core$async$Pub$unsub_all_STAR_$dyn_49915 = (function() {
var G__49916 = null;
var G__49916__1 = (function (p){
var x__5498__auto__ = (((p == null))?null:p);
var m__5499__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(p) : m__5499__auto__.call(null,p));
} else {
var m__5497__auto__ = (cljs.core.async.unsub_all_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(p) : m__5497__auto__.call(null,p));
} else {
throw cljs.core.missing_protocol("Pub.unsub-all*",p);
}
}
});
var G__49916__2 = (function (p,v){
var x__5498__auto__ = (((p == null))?null:p);
var m__5499__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(p,v) : m__5499__auto__.call(null,p,v));
} else {
var m__5497__auto__ = (cljs.core.async.unsub_all_STAR_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(p,v) : m__5497__auto__.call(null,p,v));
} else {
throw cljs.core.missing_protocol("Pub.unsub-all*",p);
}
}
});
G__49916 = function(p,v){
switch(arguments.length){
case 1:
return G__49916__1.call(this,p);
case 2:
return G__49916__2.call(this,p,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__49916.cljs$core$IFn$_invoke$arity$1 = G__49916__1;
G__49916.cljs$core$IFn$_invoke$arity$2 = G__49916__2;
return G__49916;
})()
;
cljs.core.async.unsub_all_STAR_ = (function cljs$core$async$unsub_all_STAR_(var_args){
var G__48079 = arguments.length;
switch (G__48079) {
case 1:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1 = (function (p){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$1 == null)))))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$1(p);
} else {
return cljs$core$async$Pub$unsub_all_STAR_$dyn_49915(p);
}
}));

(cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2 = (function (p,v){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$2 == null)))))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$2(p,v);
} else {
return cljs$core$async$Pub$unsub_all_STAR_$dyn_49915(p,v);
}
}));

(cljs.core.async.unsub_all_STAR_.cljs$lang$maxFixedArity = 2);



/**
* @constructor
 * @implements {cljs.core.async.Pub}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async48105 = (function (ch,topic_fn,buf_fn,mults,ensure_mult,meta48106){
this.ch = ch;
this.topic_fn = topic_fn;
this.buf_fn = buf_fn;
this.mults = mults;
this.ensure_mult = ensure_mult;
this.meta48106 = meta48106;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_48107,meta48106__$1){
var self__ = this;
var _48107__$1 = this;
return (new cljs.core.async.t_cljs$core$async48105(self__.ch,self__.topic_fn,self__.buf_fn,self__.mults,self__.ensure_mult,meta48106__$1));
}));

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_48107){
var self__ = this;
var _48107__$1 = this;
return self__.meta48106;
}));

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
}));

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$async$Pub$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$async$Pub$sub_STAR_$arity$4 = (function (p,topic,ch__$1,close_QMARK_){
var self__ = this;
var p__$1 = this;
var m = (self__.ensure_mult.cljs$core$IFn$_invoke$arity$1 ? self__.ensure_mult.cljs$core$IFn$_invoke$arity$1(topic) : self__.ensure_mult.call(null,topic));
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3(m,ch__$1,close_QMARK_);
}));

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$async$Pub$unsub_STAR_$arity$3 = (function (p,topic,ch__$1){
var self__ = this;
var p__$1 = this;
var temp__5825__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.mults),topic);
if(cljs.core.truth_(temp__5825__auto__)){
var m = temp__5825__auto__;
return cljs.core.async.untap(m,ch__$1);
} else {
return null;
}
}));

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.reset_BANG_(self__.mults,cljs.core.PersistentArrayMap.EMPTY);
}));

(cljs.core.async.t_cljs$core$async48105.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$2 = (function (_,topic){
var self__ = this;
var ___$1 = this;
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.mults,cljs.core.dissoc,topic);
}));

(cljs.core.async.t_cljs$core$async48105.getBasis = (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"topic-fn","topic-fn",-862449736,null),new cljs.core.Symbol(null,"buf-fn","buf-fn",-1200281591,null),new cljs.core.Symbol(null,"mults","mults",-461114485,null),new cljs.core.Symbol(null,"ensure-mult","ensure-mult",1796584816,null),new cljs.core.Symbol(null,"meta48106","meta48106",461138288,null)], null);
}));

(cljs.core.async.t_cljs$core$async48105.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async48105.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async48105");

(cljs.core.async.t_cljs$core$async48105.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async48105");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async48105.
 */
cljs.core.async.__GT_t_cljs$core$async48105 = (function cljs$core$async$__GT_t_cljs$core$async48105(ch,topic_fn,buf_fn,mults,ensure_mult,meta48106){
return (new cljs.core.async.t_cljs$core$async48105(ch,topic_fn,buf_fn,mults,ensure_mult,meta48106));
});


/**
 * Creates and returns a pub(lication) of the supplied channel,
 *   partitioned into topics by the topic-fn. topic-fn will be applied to
 *   each value on the channel and the result will determine the 'topic'
 *   on which that value will be put. Channels can be subscribed to
 *   receive copies of topics using 'sub', and unsubscribed using
 *   'unsub'. Each topic will be handled by an internal mult on a
 *   dedicated channel. By default these internal channels are
 *   unbuffered, but a buf-fn can be supplied which, given a topic,
 *   creates a buffer with desired properties.
 * 
 *   Each item is distributed to all subs in parallel and synchronously,
 *   i.e. each sub must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow subs from holding up the pub.
 * 
 *   Items received when there are no matching subs get dropped.
 * 
 *   Note that if buf-fns are used then each topic is handled
 *   asynchronously, i.e. if a channel is subscribed to more than one
 *   topic it should not expect them to be interleaved identically with
 *   the source.
 */
cljs.core.async.pub = (function cljs$core$async$pub(var_args){
var G__48097 = arguments.length;
switch (G__48097) {
case 2:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2 = (function (ch,topic_fn){
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3(ch,topic_fn,cljs.core.constantly(null));
}));

(cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3 = (function (ch,topic_fn,buf_fn){
var mults = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var ensure_mult = (function (topic){
var or__5142__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(mults),topic);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(mults,(function (p1__48090_SHARP_){
if(cljs.core.truth_((p1__48090_SHARP_.cljs$core$IFn$_invoke$arity$1 ? p1__48090_SHARP_.cljs$core$IFn$_invoke$arity$1(topic) : p1__48090_SHARP_.call(null,topic)))){
return p1__48090_SHARP_;
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__48090_SHARP_,topic,cljs.core.async.mult(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((buf_fn.cljs$core$IFn$_invoke$arity$1 ? buf_fn.cljs$core$IFn$_invoke$arity$1(topic) : buf_fn.call(null,topic)))));
}
})),topic);
}
});
var p = (new cljs.core.async.t_cljs$core$async48105(ch,topic_fn,buf_fn,mults,ensure_mult,cljs.core.PersistentArrayMap.EMPTY));
var c__45940__auto___49942 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_48236){
var state_val_48238 = (state_48236[(1)]);
if((state_val_48238 === (7))){
var inst_48232 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
var statearr_48244_49947 = state_48236__$1;
(statearr_48244_49947[(2)] = inst_48232);

(statearr_48244_49947[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (20))){
var state_48236__$1 = state_48236;
var statearr_48245_49948 = state_48236__$1;
(statearr_48245_49948[(2)] = null);

(statearr_48245_49948[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (1))){
var state_48236__$1 = state_48236;
var statearr_48252_49951 = state_48236__$1;
(statearr_48252_49951[(2)] = null);

(statearr_48252_49951[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (24))){
var inst_48204 = (state_48236[(7)]);
var inst_48224 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(mults,cljs.core.dissoc,inst_48204);
var state_48236__$1 = state_48236;
var statearr_48253_49952 = state_48236__$1;
(statearr_48253_49952[(2)] = inst_48224);

(statearr_48253_49952[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (4))){
var inst_48140 = (state_48236[(8)]);
var inst_48140__$1 = (state_48236[(2)]);
var inst_48142 = (inst_48140__$1 == null);
var state_48236__$1 = (function (){var statearr_48261 = state_48236;
(statearr_48261[(8)] = inst_48140__$1);

return statearr_48261;
})();
if(cljs.core.truth_(inst_48142)){
var statearr_48263_49953 = state_48236__$1;
(statearr_48263_49953[(1)] = (5));

} else {
var statearr_48265_49954 = state_48236__$1;
(statearr_48265_49954[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (15))){
var inst_48197 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
var statearr_48266_49956 = state_48236__$1;
(statearr_48266_49956[(2)] = inst_48197);

(statearr_48266_49956[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (21))){
var inst_48229 = (state_48236[(2)]);
var state_48236__$1 = (function (){var statearr_48267 = state_48236;
(statearr_48267[(9)] = inst_48229);

return statearr_48267;
})();
var statearr_48268_49960 = state_48236__$1;
(statearr_48268_49960[(2)] = null);

(statearr_48268_49960[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (13))){
var inst_48173 = (state_48236[(10)]);
var inst_48176 = cljs.core.chunked_seq_QMARK_(inst_48173);
var state_48236__$1 = state_48236;
if(inst_48176){
var statearr_48277_49962 = state_48236__$1;
(statearr_48277_49962[(1)] = (16));

} else {
var statearr_48278_49963 = state_48236__$1;
(statearr_48278_49963[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (22))){
var inst_48221 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
if(cljs.core.truth_(inst_48221)){
var statearr_48280_49964 = state_48236__$1;
(statearr_48280_49964[(1)] = (23));

} else {
var statearr_48281_49965 = state_48236__$1;
(statearr_48281_49965[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (6))){
var inst_48140 = (state_48236[(8)]);
var inst_48204 = (state_48236[(7)]);
var inst_48207 = (state_48236[(11)]);
var inst_48204__$1 = (topic_fn.cljs$core$IFn$_invoke$arity$1 ? topic_fn.cljs$core$IFn$_invoke$arity$1(inst_48140) : topic_fn.call(null,inst_48140));
var inst_48206 = cljs.core.deref(mults);
var inst_48207__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_48206,inst_48204__$1);
var state_48236__$1 = (function (){var statearr_48283 = state_48236;
(statearr_48283[(7)] = inst_48204__$1);

(statearr_48283[(11)] = inst_48207__$1);

return statearr_48283;
})();
if(cljs.core.truth_(inst_48207__$1)){
var statearr_48286_49967 = state_48236__$1;
(statearr_48286_49967[(1)] = (19));

} else {
var statearr_48289_49968 = state_48236__$1;
(statearr_48289_49968[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (25))){
var inst_48226 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
var statearr_48290_49969 = state_48236__$1;
(statearr_48290_49969[(2)] = inst_48226);

(statearr_48290_49969[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (17))){
var inst_48173 = (state_48236[(10)]);
var inst_48187 = cljs.core.first(inst_48173);
var inst_48189 = cljs.core.async.muxch_STAR_(inst_48187);
var inst_48190 = cljs.core.async.close_BANG_(inst_48189);
var inst_48191 = cljs.core.next(inst_48173);
var inst_48156 = inst_48191;
var inst_48157 = null;
var inst_48158 = (0);
var inst_48159 = (0);
var state_48236__$1 = (function (){var statearr_48292 = state_48236;
(statearr_48292[(12)] = inst_48190);

(statearr_48292[(13)] = inst_48156);

(statearr_48292[(14)] = inst_48157);

(statearr_48292[(15)] = inst_48158);

(statearr_48292[(16)] = inst_48159);

return statearr_48292;
})();
var statearr_48293_49970 = state_48236__$1;
(statearr_48293_49970[(2)] = null);

(statearr_48293_49970[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (3))){
var inst_48234 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
return cljs.core.async.impl.ioc_helpers.return_chan(state_48236__$1,inst_48234);
} else {
if((state_val_48238 === (12))){
var inst_48199 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
var statearr_48295_49973 = state_48236__$1;
(statearr_48295_49973[(2)] = inst_48199);

(statearr_48295_49973[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (2))){
var state_48236__$1 = state_48236;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_48236__$1,(4),ch);
} else {
if((state_val_48238 === (23))){
var state_48236__$1 = state_48236;
var statearr_48297_49974 = state_48236__$1;
(statearr_48297_49974[(2)] = null);

(statearr_48297_49974[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (19))){
var inst_48207 = (state_48236[(11)]);
var inst_48140 = (state_48236[(8)]);
var inst_48219 = cljs.core.async.muxch_STAR_(inst_48207);
var state_48236__$1 = state_48236;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_48236__$1,(22),inst_48219,inst_48140);
} else {
if((state_val_48238 === (11))){
var inst_48156 = (state_48236[(13)]);
var inst_48173 = (state_48236[(10)]);
var inst_48173__$1 = cljs.core.seq(inst_48156);
var state_48236__$1 = (function (){var statearr_48302 = state_48236;
(statearr_48302[(10)] = inst_48173__$1);

return statearr_48302;
})();
if(inst_48173__$1){
var statearr_48303_49975 = state_48236__$1;
(statearr_48303_49975[(1)] = (13));

} else {
var statearr_48304_49978 = state_48236__$1;
(statearr_48304_49978[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (9))){
var inst_48201 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
var statearr_48307_49979 = state_48236__$1;
(statearr_48307_49979[(2)] = inst_48201);

(statearr_48307_49979[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (5))){
var inst_48153 = cljs.core.deref(mults);
var inst_48154 = cljs.core.vals(inst_48153);
var inst_48155 = cljs.core.seq(inst_48154);
var inst_48156 = inst_48155;
var inst_48157 = null;
var inst_48158 = (0);
var inst_48159 = (0);
var state_48236__$1 = (function (){var statearr_48308 = state_48236;
(statearr_48308[(13)] = inst_48156);

(statearr_48308[(14)] = inst_48157);

(statearr_48308[(15)] = inst_48158);

(statearr_48308[(16)] = inst_48159);

return statearr_48308;
})();
var statearr_48309_49980 = state_48236__$1;
(statearr_48309_49980[(2)] = null);

(statearr_48309_49980[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (14))){
var state_48236__$1 = state_48236;
var statearr_48313_49982 = state_48236__$1;
(statearr_48313_49982[(2)] = null);

(statearr_48313_49982[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (16))){
var inst_48173 = (state_48236[(10)]);
var inst_48181 = cljs.core.chunk_first(inst_48173);
var inst_48183 = cljs.core.chunk_rest(inst_48173);
var inst_48184 = cljs.core.count(inst_48181);
var inst_48156 = inst_48183;
var inst_48157 = inst_48181;
var inst_48158 = inst_48184;
var inst_48159 = (0);
var state_48236__$1 = (function (){var statearr_48314 = state_48236;
(statearr_48314[(13)] = inst_48156);

(statearr_48314[(14)] = inst_48157);

(statearr_48314[(15)] = inst_48158);

(statearr_48314[(16)] = inst_48159);

return statearr_48314;
})();
var statearr_48315_49989 = state_48236__$1;
(statearr_48315_49989[(2)] = null);

(statearr_48315_49989[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (10))){
var inst_48157 = (state_48236[(14)]);
var inst_48159 = (state_48236[(16)]);
var inst_48156 = (state_48236[(13)]);
var inst_48158 = (state_48236[(15)]);
var inst_48166 = cljs.core._nth(inst_48157,inst_48159);
var inst_48167 = cljs.core.async.muxch_STAR_(inst_48166);
var inst_48168 = cljs.core.async.close_BANG_(inst_48167);
var inst_48169 = (inst_48159 + (1));
var tmp48310 = inst_48158;
var tmp48311 = inst_48157;
var tmp48312 = inst_48156;
var inst_48156__$1 = tmp48312;
var inst_48157__$1 = tmp48311;
var inst_48158__$1 = tmp48310;
var inst_48159__$1 = inst_48169;
var state_48236__$1 = (function (){var statearr_48317 = state_48236;
(statearr_48317[(17)] = inst_48168);

(statearr_48317[(13)] = inst_48156__$1);

(statearr_48317[(14)] = inst_48157__$1);

(statearr_48317[(15)] = inst_48158__$1);

(statearr_48317[(16)] = inst_48159__$1);

return statearr_48317;
})();
var statearr_48318_49993 = state_48236__$1;
(statearr_48318_49993[(2)] = null);

(statearr_48318_49993[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (18))){
var inst_48194 = (state_48236[(2)]);
var state_48236__$1 = state_48236;
var statearr_48319_49996 = state_48236__$1;
(statearr_48319_49996[(2)] = inst_48194);

(statearr_48319_49996[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48238 === (8))){
var inst_48159 = (state_48236[(16)]);
var inst_48158 = (state_48236[(15)]);
var inst_48162 = (inst_48159 < inst_48158);
var inst_48163 = inst_48162;
var state_48236__$1 = state_48236;
if(cljs.core.truth_(inst_48163)){
var statearr_48320_49999 = state_48236__$1;
(statearr_48320_49999[(1)] = (10));

} else {
var statearr_48321_50000 = state_48236__$1;
(statearr_48321_50000[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
}
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_48322 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_48322[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_48322[(1)] = (1));

return statearr_48322;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_48236){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_48236);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e48324){var ex__45565__auto__ = e48324;
var statearr_48325_50010 = state_48236;
(statearr_48325_50010[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_48236[(4)]))){
var statearr_48326_50012 = state_48236;
(statearr_48326_50012[(1)] = cljs.core.first((state_48236[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50013 = state_48236;
state_48236 = G__50013;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_48236){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_48236);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_48327 = f__45941__auto__();
(statearr_48327[(6)] = c__45940__auto___49942);

return statearr_48327;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return p;
}));

(cljs.core.async.pub.cljs$lang$maxFixedArity = 3);

/**
 * Subscribes a channel to a topic of a pub.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.sub = (function cljs$core$async$sub(var_args){
var G__48329 = arguments.length;
switch (G__48329) {
case 3:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3 = (function (p,topic,ch){
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4(p,topic,ch,true);
}));

(cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4 = (function (p,topic,ch,close_QMARK_){
return cljs.core.async.sub_STAR_(p,topic,ch,close_QMARK_);
}));

(cljs.core.async.sub.cljs$lang$maxFixedArity = 4);

/**
 * Unsubscribes a channel from a topic of a pub
 */
cljs.core.async.unsub = (function cljs$core$async$unsub(p,topic,ch){
return cljs.core.async.unsub_STAR_(p,topic,ch);
});
/**
 * Unsubscribes all channels from a pub, or a topic of a pub
 */
cljs.core.async.unsub_all = (function cljs$core$async$unsub_all(var_args){
var G__48338 = arguments.length;
switch (G__48338) {
case 1:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1 = (function (p){
return cljs.core.async.unsub_all_STAR_(p);
}));

(cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2 = (function (p,topic){
return cljs.core.async.unsub_all_STAR_(p,topic);
}));

(cljs.core.async.unsub_all.cljs$lang$maxFixedArity = 2);

/**
 * Takes a function and a collection of source channels, and returns a
 *   channel which contains the values produced by applying f to the set
 *   of first items taken from each source channel, followed by applying
 *   f to the set of second items from each channel, until any one of the
 *   channels is closed, at which point the output channel will be
 *   closed. The returned channel will be unbuffered by default, or a
 *   buf-or-n can be supplied
 */
cljs.core.async.map = (function cljs$core$async$map(var_args){
var G__48354 = arguments.length;
switch (G__48354) {
case 2:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.map.cljs$core$IFn$_invoke$arity$2 = (function (f,chs){
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$3(f,chs,null);
}));

(cljs.core.async.map.cljs$core$IFn$_invoke$arity$3 = (function (f,chs,buf_or_n){
var chs__$1 = cljs.core.vec(chs);
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var cnt = cljs.core.count(chs__$1);
var rets = cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(cnt);
var dchan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var dctr = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var done = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (i){
return (function (ret){
(rets[i] = ret);

if((cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(dchan,rets.slice((0)));
} else {
return null;
}
});
}),cljs.core.range.cljs$core$IFn$_invoke$arity$1(cnt));
if((cnt === (0))){
cljs.core.async.close_BANG_(out);
} else {
var c__45940__auto___50029 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_48414){
var state_val_48415 = (state_48414[(1)]);
if((state_val_48415 === (7))){
var state_48414__$1 = state_48414;
var statearr_48416_50034 = state_48414__$1;
(statearr_48416_50034[(2)] = null);

(statearr_48416_50034[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (1))){
var state_48414__$1 = state_48414;
var statearr_48417_50035 = state_48414__$1;
(statearr_48417_50035[(2)] = null);

(statearr_48417_50035[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (4))){
var inst_48366 = (state_48414[(7)]);
var inst_48365 = (state_48414[(8)]);
var inst_48368 = (inst_48366 < inst_48365);
var state_48414__$1 = state_48414;
if(cljs.core.truth_(inst_48368)){
var statearr_48418_50038 = state_48414__$1;
(statearr_48418_50038[(1)] = (6));

} else {
var statearr_48419_50039 = state_48414__$1;
(statearr_48419_50039[(1)] = (7));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (15))){
var inst_48394 = (state_48414[(9)]);
var inst_48400 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,inst_48394);
var state_48414__$1 = state_48414;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_48414__$1,(17),out,inst_48400);
} else {
if((state_val_48415 === (13))){
var inst_48394 = (state_48414[(9)]);
var inst_48394__$1 = (state_48414[(2)]);
var inst_48395 = cljs.core.some(cljs.core.nil_QMARK_,inst_48394__$1);
var state_48414__$1 = (function (){var statearr_48420 = state_48414;
(statearr_48420[(9)] = inst_48394__$1);

return statearr_48420;
})();
if(cljs.core.truth_(inst_48395)){
var statearr_48421_50044 = state_48414__$1;
(statearr_48421_50044[(1)] = (14));

} else {
var statearr_48422_50046 = state_48414__$1;
(statearr_48422_50046[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (6))){
var state_48414__$1 = state_48414;
var statearr_48424_50047 = state_48414__$1;
(statearr_48424_50047[(2)] = null);

(statearr_48424_50047[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (17))){
var inst_48402 = (state_48414[(2)]);
var state_48414__$1 = (function (){var statearr_48428 = state_48414;
(statearr_48428[(10)] = inst_48402);

return statearr_48428;
})();
var statearr_48429_50048 = state_48414__$1;
(statearr_48429_50048[(2)] = null);

(statearr_48429_50048[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (3))){
var inst_48407 = (state_48414[(2)]);
var state_48414__$1 = state_48414;
return cljs.core.async.impl.ioc_helpers.return_chan(state_48414__$1,inst_48407);
} else {
if((state_val_48415 === (12))){
var _ = (function (){var statearr_48432 = state_48414;
(statearr_48432[(4)] = cljs.core.rest((state_48414[(4)])));

return statearr_48432;
})();
var state_48414__$1 = state_48414;
var ex48427 = (state_48414__$1[(2)]);
var statearr_48434_50049 = state_48414__$1;
(statearr_48434_50049[(5)] = ex48427);


if((ex48427 instanceof Object)){
var statearr_48435_50054 = state_48414__$1;
(statearr_48435_50054[(1)] = (11));

(statearr_48435_50054[(5)] = null);

} else {
throw ex48427;

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (2))){
var inst_48364 = cljs.core.reset_BANG_(dctr,cnt);
var inst_48365 = cnt;
var inst_48366 = (0);
var state_48414__$1 = (function (){var statearr_48446 = state_48414;
(statearr_48446[(11)] = inst_48364);

(statearr_48446[(8)] = inst_48365);

(statearr_48446[(7)] = inst_48366);

return statearr_48446;
})();
var statearr_48447_50057 = state_48414__$1;
(statearr_48447_50057[(2)] = null);

(statearr_48447_50057[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (11))){
var inst_48373 = (state_48414[(2)]);
var inst_48374 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec);
var state_48414__$1 = (function (){var statearr_48449 = state_48414;
(statearr_48449[(12)] = inst_48373);

return statearr_48449;
})();
var statearr_48451_50062 = state_48414__$1;
(statearr_48451_50062[(2)] = inst_48374);

(statearr_48451_50062[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (9))){
var inst_48366 = (state_48414[(7)]);
var _ = (function (){var statearr_48452 = state_48414;
(statearr_48452[(4)] = cljs.core.cons((12),(state_48414[(4)])));

return statearr_48452;
})();
var inst_48380 = (chs__$1.cljs$core$IFn$_invoke$arity$1 ? chs__$1.cljs$core$IFn$_invoke$arity$1(inst_48366) : chs__$1.call(null,inst_48366));
var inst_48381 = (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(inst_48366) : done.call(null,inst_48366));
var inst_48382 = cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2(inst_48380,inst_48381);
var ___$1 = (function (){var statearr_48453 = state_48414;
(statearr_48453[(4)] = cljs.core.rest((state_48414[(4)])));

return statearr_48453;
})();
var state_48414__$1 = state_48414;
var statearr_48455_50069 = state_48414__$1;
(statearr_48455_50069[(2)] = inst_48382);

(statearr_48455_50069[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (5))){
var inst_48392 = (state_48414[(2)]);
var state_48414__$1 = (function (){var statearr_48458 = state_48414;
(statearr_48458[(13)] = inst_48392);

return statearr_48458;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_48414__$1,(13),dchan);
} else {
if((state_val_48415 === (14))){
var inst_48397 = cljs.core.async.close_BANG_(out);
var state_48414__$1 = state_48414;
var statearr_48461_50073 = state_48414__$1;
(statearr_48461_50073[(2)] = inst_48397);

(statearr_48461_50073[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (16))){
var inst_48405 = (state_48414[(2)]);
var state_48414__$1 = state_48414;
var statearr_48463_50075 = state_48414__$1;
(statearr_48463_50075[(2)] = inst_48405);

(statearr_48463_50075[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (10))){
var inst_48366 = (state_48414[(7)]);
var inst_48385 = (state_48414[(2)]);
var inst_48386 = (inst_48366 + (1));
var inst_48366__$1 = inst_48386;
var state_48414__$1 = (function (){var statearr_48464 = state_48414;
(statearr_48464[(14)] = inst_48385);

(statearr_48464[(7)] = inst_48366__$1);

return statearr_48464;
})();
var statearr_48465_50082 = state_48414__$1;
(statearr_48465_50082[(2)] = null);

(statearr_48465_50082[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48415 === (8))){
var inst_48390 = (state_48414[(2)]);
var state_48414__$1 = state_48414;
var statearr_48466_50086 = state_48414__$1;
(statearr_48466_50086[(2)] = inst_48390);

(statearr_48466_50086[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_48470 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_48470[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_48470[(1)] = (1));

return statearr_48470;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_48414){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_48414);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e48473){var ex__45565__auto__ = e48473;
var statearr_48474_50090 = state_48414;
(statearr_48474_50090[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_48414[(4)]))){
var statearr_48475_50091 = state_48414;
(statearr_48475_50091[(1)] = cljs.core.first((state_48414[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50094 = state_48414;
state_48414 = G__50094;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_48414){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_48414);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_48476 = f__45941__auto__();
(statearr_48476[(6)] = c__45940__auto___50029);

return statearr_48476;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));

}

return out;
}));

(cljs.core.async.map.cljs$lang$maxFixedArity = 3);

/**
 * Takes a collection of source channels and returns a channel which
 *   contains all values taken from them. The returned channel will be
 *   unbuffered by default, or a buf-or-n can be supplied. The channel
 *   will close after all the source channels have closed.
 */
cljs.core.async.merge = (function cljs$core$async$merge(var_args){
var G__48484 = arguments.length;
switch (G__48484) {
case 1:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1 = (function (chs){
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2(chs,null);
}));

(cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2 = (function (chs,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__45940__auto___50100 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_48520){
var state_val_48521 = (state_48520[(1)]);
if((state_val_48521 === (7))){
var inst_48498 = (state_48520[(7)]);
var inst_48499 = (state_48520[(8)]);
var inst_48498__$1 = (state_48520[(2)]);
var inst_48499__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_48498__$1,(0),null);
var inst_48500 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_48498__$1,(1),null);
var inst_48501 = (inst_48499__$1 == null);
var state_48520__$1 = (function (){var statearr_48527 = state_48520;
(statearr_48527[(7)] = inst_48498__$1);

(statearr_48527[(8)] = inst_48499__$1);

(statearr_48527[(9)] = inst_48500);

return statearr_48527;
})();
if(cljs.core.truth_(inst_48501)){
var statearr_48528_50103 = state_48520__$1;
(statearr_48528_50103[(1)] = (8));

} else {
var statearr_48529_50104 = state_48520__$1;
(statearr_48529_50104[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48521 === (1))){
var inst_48488 = cljs.core.vec(chs);
var inst_48489 = inst_48488;
var state_48520__$1 = (function (){var statearr_48532 = state_48520;
(statearr_48532[(10)] = inst_48489);

return statearr_48532;
})();
var statearr_48535_50105 = state_48520__$1;
(statearr_48535_50105[(2)] = null);

(statearr_48535_50105[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48521 === (4))){
var inst_48489 = (state_48520[(10)]);
var state_48520__$1 = state_48520;
return cljs.core.async.ioc_alts_BANG_(state_48520__$1,(7),inst_48489);
} else {
if((state_val_48521 === (6))){
var inst_48516 = (state_48520[(2)]);
var state_48520__$1 = state_48520;
var statearr_48540_50106 = state_48520__$1;
(statearr_48540_50106[(2)] = inst_48516);

(statearr_48540_50106[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48521 === (3))){
var inst_48518 = (state_48520[(2)]);
var state_48520__$1 = state_48520;
return cljs.core.async.impl.ioc_helpers.return_chan(state_48520__$1,inst_48518);
} else {
if((state_val_48521 === (2))){
var inst_48489 = (state_48520[(10)]);
var inst_48491 = cljs.core.count(inst_48489);
var inst_48492 = (inst_48491 > (0));
var state_48520__$1 = state_48520;
if(cljs.core.truth_(inst_48492)){
var statearr_48546_50110 = state_48520__$1;
(statearr_48546_50110[(1)] = (4));

} else {
var statearr_48548_50113 = state_48520__$1;
(statearr_48548_50113[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48521 === (11))){
var inst_48489 = (state_48520[(10)]);
var inst_48509 = (state_48520[(2)]);
var tmp48541 = inst_48489;
var inst_48489__$1 = tmp48541;
var state_48520__$1 = (function (){var statearr_48550 = state_48520;
(statearr_48550[(11)] = inst_48509);

(statearr_48550[(10)] = inst_48489__$1);

return statearr_48550;
})();
var statearr_48551_50116 = state_48520__$1;
(statearr_48551_50116[(2)] = null);

(statearr_48551_50116[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48521 === (9))){
var inst_48499 = (state_48520[(8)]);
var state_48520__$1 = state_48520;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_48520__$1,(11),out,inst_48499);
} else {
if((state_val_48521 === (5))){
var inst_48514 = cljs.core.async.close_BANG_(out);
var state_48520__$1 = state_48520;
var statearr_48559_50117 = state_48520__$1;
(statearr_48559_50117[(2)] = inst_48514);

(statearr_48559_50117[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48521 === (10))){
var inst_48512 = (state_48520[(2)]);
var state_48520__$1 = state_48520;
var statearr_48562_50118 = state_48520__$1;
(statearr_48562_50118[(2)] = inst_48512);

(statearr_48562_50118[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48521 === (8))){
var inst_48489 = (state_48520[(10)]);
var inst_48498 = (state_48520[(7)]);
var inst_48499 = (state_48520[(8)]);
var inst_48500 = (state_48520[(9)]);
var inst_48504 = (function (){var cs = inst_48489;
var vec__48494 = inst_48498;
var v = inst_48499;
var c = inst_48500;
return (function (p1__48477_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(c,p1__48477_SHARP_);
});
})();
var inst_48505 = cljs.core.filterv(inst_48504,inst_48489);
var inst_48489__$1 = inst_48505;
var state_48520__$1 = (function (){var statearr_48563 = state_48520;
(statearr_48563[(10)] = inst_48489__$1);

return statearr_48563;
})();
var statearr_48564_50121 = state_48520__$1;
(statearr_48564_50121[(2)] = null);

(statearr_48564_50121[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_48565 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_48565[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_48565[(1)] = (1));

return statearr_48565;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_48520){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_48520);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e48566){var ex__45565__auto__ = e48566;
var statearr_48567_50122 = state_48520;
(statearr_48567_50122[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_48520[(4)]))){
var statearr_48568_50123 = state_48520;
(statearr_48568_50123[(1)] = cljs.core.first((state_48520[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50124 = state_48520;
state_48520 = G__50124;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_48520){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_48520);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_48572 = f__45941__auto__();
(statearr_48572[(6)] = c__45940__auto___50100);

return statearr_48572;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return out;
}));

(cljs.core.async.merge.cljs$lang$maxFixedArity = 2);

/**
 * Returns a channel containing the single (collection) result of the
 *   items taken from the channel conjoined to the supplied
 *   collection. ch must close before into produces a result.
 */
cljs.core.async.into = (function cljs$core$async$into(coll,ch){
return cljs.core.async.reduce(cljs.core.conj,coll,ch);
});
/**
 * Returns a channel that will return, at most, n items from ch. After n items
 * have been returned, or ch has been closed, the return chanel will close.
 * 
 *   The output channel is unbuffered by default, unless buf-or-n is given.
 */
cljs.core.async.take = (function cljs$core$async$take(var_args){
var G__48577 = arguments.length;
switch (G__48577) {
case 2:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.take.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$3(n,ch,null);
}));

(cljs.core.async.take.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__45940__auto___50128 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_48606){
var state_val_48607 = (state_48606[(1)]);
if((state_val_48607 === (7))){
var inst_48588 = (state_48606[(7)]);
var inst_48588__$1 = (state_48606[(2)]);
var inst_48589 = (inst_48588__$1 == null);
var inst_48590 = cljs.core.not(inst_48589);
var state_48606__$1 = (function (){var statearr_48611 = state_48606;
(statearr_48611[(7)] = inst_48588__$1);

return statearr_48611;
})();
if(inst_48590){
var statearr_48614_50129 = state_48606__$1;
(statearr_48614_50129[(1)] = (8));

} else {
var statearr_48615_50130 = state_48606__$1;
(statearr_48615_50130[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (1))){
var inst_48582 = (0);
var state_48606__$1 = (function (){var statearr_48616 = state_48606;
(statearr_48616[(8)] = inst_48582);

return statearr_48616;
})();
var statearr_48617_50131 = state_48606__$1;
(statearr_48617_50131[(2)] = null);

(statearr_48617_50131[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (4))){
var state_48606__$1 = state_48606;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_48606__$1,(7),ch);
} else {
if((state_val_48607 === (6))){
var inst_48601 = (state_48606[(2)]);
var state_48606__$1 = state_48606;
var statearr_48618_50133 = state_48606__$1;
(statearr_48618_50133[(2)] = inst_48601);

(statearr_48618_50133[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (3))){
var inst_48603 = (state_48606[(2)]);
var inst_48604 = cljs.core.async.close_BANG_(out);
var state_48606__$1 = (function (){var statearr_48619 = state_48606;
(statearr_48619[(9)] = inst_48603);

return statearr_48619;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_48606__$1,inst_48604);
} else {
if((state_val_48607 === (2))){
var inst_48582 = (state_48606[(8)]);
var inst_48584 = (inst_48582 < n);
var state_48606__$1 = state_48606;
if(cljs.core.truth_(inst_48584)){
var statearr_48621_50134 = state_48606__$1;
(statearr_48621_50134[(1)] = (4));

} else {
var statearr_48622_50135 = state_48606__$1;
(statearr_48622_50135[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (11))){
var inst_48582 = (state_48606[(8)]);
var inst_48593 = (state_48606[(2)]);
var inst_48594 = (inst_48582 + (1));
var inst_48582__$1 = inst_48594;
var state_48606__$1 = (function (){var statearr_48623 = state_48606;
(statearr_48623[(10)] = inst_48593);

(statearr_48623[(8)] = inst_48582__$1);

return statearr_48623;
})();
var statearr_48624_50137 = state_48606__$1;
(statearr_48624_50137[(2)] = null);

(statearr_48624_50137[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (9))){
var state_48606__$1 = state_48606;
var statearr_48625_50138 = state_48606__$1;
(statearr_48625_50138[(2)] = null);

(statearr_48625_50138[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (5))){
var state_48606__$1 = state_48606;
var statearr_48626_50141 = state_48606__$1;
(statearr_48626_50141[(2)] = null);

(statearr_48626_50141[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (10))){
var inst_48598 = (state_48606[(2)]);
var state_48606__$1 = state_48606;
var statearr_48627_50143 = state_48606__$1;
(statearr_48627_50143[(2)] = inst_48598);

(statearr_48627_50143[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48607 === (8))){
var inst_48588 = (state_48606[(7)]);
var state_48606__$1 = state_48606;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_48606__$1,(11),out,inst_48588);
} else {
return null;
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
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_48632 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_48632[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_48632[(1)] = (1));

return statearr_48632;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_48606){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_48606);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e48633){var ex__45565__auto__ = e48633;
var statearr_48634_50148 = state_48606;
(statearr_48634_50148[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_48606[(4)]))){
var statearr_48635_50149 = state_48606;
(statearr_48635_50149[(1)] = cljs.core.first((state_48606[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50150 = state_48606;
state_48606 = G__50150;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_48606){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_48606);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_48637 = f__45941__auto__();
(statearr_48637[(6)] = c__45940__auto___50128);

return statearr_48637;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return out;
}));

(cljs.core.async.take.cljs$lang$maxFixedArity = 3);


/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async48651 = (function (f,ch,meta48644,_,fn1,meta48652){
this.f = f;
this.ch = ch;
this.meta48644 = meta48644;
this._ = _;
this.fn1 = fn1;
this.meta48652 = meta48652;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async48651.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_48653,meta48652__$1){
var self__ = this;
var _48653__$1 = this;
return (new cljs.core.async.t_cljs$core$async48651(self__.f,self__.ch,self__.meta48644,self__._,self__.fn1,meta48652__$1));
}));

(cljs.core.async.t_cljs$core$async48651.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_48653){
var self__ = this;
var _48653__$1 = this;
return self__.meta48652;
}));

(cljs.core.async.t_cljs$core$async48651.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48651.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
return cljs.core.async.impl.protocols.active_QMARK_(self__.fn1);
}));

(cljs.core.async.t_cljs$core$async48651.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async48651.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
var f1 = cljs.core.async.impl.protocols.commit(self__.fn1);
return (function (p1__48640_SHARP_){
var G__48662 = (((p1__48640_SHARP_ == null))?null:(self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(p1__48640_SHARP_) : self__.f.call(null,p1__48640_SHARP_)));
return (f1.cljs$core$IFn$_invoke$arity$1 ? f1.cljs$core$IFn$_invoke$arity$1(G__48662) : f1.call(null,G__48662));
});
}));

(cljs.core.async.t_cljs$core$async48651.getBasis = (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta48644","meta48644",-512952988,null),cljs.core.with_meta(new cljs.core.Symbol(null,"_","_",-1201019570,null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Symbol("cljs.core.async","t_cljs$core$async48643","cljs.core.async/t_cljs$core$async48643",1633893959,null)], null)),new cljs.core.Symbol(null,"fn1","fn1",895834444,null),new cljs.core.Symbol(null,"meta48652","meta48652",-1040087273,null)], null);
}));

(cljs.core.async.t_cljs$core$async48651.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async48651.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async48651");

(cljs.core.async.t_cljs$core$async48651.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async48651");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async48651.
 */
cljs.core.async.__GT_t_cljs$core$async48651 = (function cljs$core$async$__GT_t_cljs$core$async48651(f,ch,meta48644,_,fn1,meta48652){
return (new cljs.core.async.t_cljs$core$async48651(f,ch,meta48644,_,fn1,meta48652));
});



/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async48643 = (function (f,ch,meta48644){
this.f = f;
this.ch = ch;
this.meta48644 = meta48644;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_48645,meta48644__$1){
var self__ = this;
var _48645__$1 = this;
return (new cljs.core.async.t_cljs$core$async48643(self__.f,self__.ch,meta48644__$1));
}));

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_48645){
var self__ = this;
var _48645__$1 = this;
return self__.meta48644;
}));

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
var ret = cljs.core.async.impl.protocols.take_BANG_(self__.ch,(new cljs.core.async.t_cljs$core$async48651(self__.f,self__.ch,self__.meta48644,___$1,fn1,cljs.core.PersistentArrayMap.EMPTY)));
if(cljs.core.truth_((function (){var and__5140__auto__ = ret;
if(cljs.core.truth_(and__5140__auto__)){
return (!((cljs.core.deref(ret) == null)));
} else {
return and__5140__auto__;
}
})())){
return cljs.core.async.impl.channels.box((function (){var G__48666 = cljs.core.deref(ret);
return (self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(G__48666) : self__.f.call(null,G__48666));
})());
} else {
return ret;
}
}));

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48643.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,val,fn1);
}));

(cljs.core.async.t_cljs$core$async48643.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta48644","meta48644",-512952988,null)], null);
}));

(cljs.core.async.t_cljs$core$async48643.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async48643.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async48643");

(cljs.core.async.t_cljs$core$async48643.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async48643");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async48643.
 */
cljs.core.async.__GT_t_cljs$core$async48643 = (function cljs$core$async$__GT_t_cljs$core$async48643(f,ch,meta48644){
return (new cljs.core.async.t_cljs$core$async48643(f,ch,meta48644));
});


/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_LT_ = (function cljs$core$async$map_LT_(f,ch){
return (new cljs.core.async.t_cljs$core$async48643(f,ch,cljs.core.PersistentArrayMap.EMPTY));
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async48674 = (function (f,ch,meta48675){
this.f = f;
this.ch = ch;
this.meta48675 = meta48675;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_48676,meta48675__$1){
var self__ = this;
var _48676__$1 = this;
return (new cljs.core.async.t_cljs$core$async48674(self__.f,self__.ch,meta48675__$1));
}));

(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_48676){
var self__ = this;
var _48676__$1 = this;
return self__.meta48675;
}));

(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_(self__.ch,fn1);
}));

(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48674.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,(self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(val) : self__.f.call(null,val)),fn1);
}));

(cljs.core.async.t_cljs$core$async48674.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta48675","meta48675",1019788880,null)], null);
}));

(cljs.core.async.t_cljs$core$async48674.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async48674.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async48674");

(cljs.core.async.t_cljs$core$async48674.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async48674");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async48674.
 */
cljs.core.async.__GT_t_cljs$core$async48674 = (function cljs$core$async$__GT_t_cljs$core$async48674(f,ch,meta48675){
return (new cljs.core.async.t_cljs$core$async48674(f,ch,meta48675));
});


/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_GT_ = (function cljs$core$async$map_GT_(f,ch){
return (new cljs.core.async.t_cljs$core$async48674(f,ch,cljs.core.PersistentArrayMap.EMPTY));
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async48689 = (function (p,ch,meta48690){
this.p = p;
this.ch = ch;
this.meta48690 = meta48690;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_48691,meta48690__$1){
var self__ = this;
var _48691__$1 = this;
return (new cljs.core.async.t_cljs$core$async48689(self__.p,self__.ch,meta48690__$1));
}));

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_48691){
var self__ = this;
var _48691__$1 = this;
return self__.meta48690;
}));

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_(self__.ch,fn1);
}));

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async48689.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_((self__.p.cljs$core$IFn$_invoke$arity$1 ? self__.p.cljs$core$IFn$_invoke$arity$1(val) : self__.p.call(null,val)))){
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,val,fn1);
} else {
return cljs.core.async.impl.channels.box(cljs.core.not(cljs.core.async.impl.protocols.closed_QMARK_(self__.ch)));
}
}));

(cljs.core.async.t_cljs$core$async48689.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"p","p",1791580836,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta48690","meta48690",-747253013,null)], null);
}));

(cljs.core.async.t_cljs$core$async48689.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async48689.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async48689");

(cljs.core.async.t_cljs$core$async48689.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"cljs.core.async/t_cljs$core$async48689");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async48689.
 */
cljs.core.async.__GT_t_cljs$core$async48689 = (function cljs$core$async$__GT_t_cljs$core$async48689(p,ch,meta48690){
return (new cljs.core.async.t_cljs$core$async48689(p,ch,meta48690));
});


/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_GT_ = (function cljs$core$async$filter_GT_(p,ch){
return (new cljs.core.async.t_cljs$core$async48689(p,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_GT_ = (function cljs$core$async$remove_GT_(p,ch){
return cljs.core.async.filter_GT_(cljs.core.complement(p),ch);
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_LT_ = (function cljs$core$async$filter_LT_(var_args){
var G__48710 = arguments.length;
switch (G__48710) {
case 2:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3(p,ch,null);
}));

(cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__45940__auto___50183 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_48742){
var state_val_48743 = (state_48742[(1)]);
if((state_val_48743 === (7))){
var inst_48736 = (state_48742[(2)]);
var state_48742__$1 = state_48742;
var statearr_48746_50184 = state_48742__$1;
(statearr_48746_50184[(2)] = inst_48736);

(statearr_48746_50184[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (1))){
var state_48742__$1 = state_48742;
var statearr_48747_50186 = state_48742__$1;
(statearr_48747_50186[(2)] = null);

(statearr_48747_50186[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (4))){
var inst_48722 = (state_48742[(7)]);
var inst_48722__$1 = (state_48742[(2)]);
var inst_48723 = (inst_48722__$1 == null);
var state_48742__$1 = (function (){var statearr_48752 = state_48742;
(statearr_48752[(7)] = inst_48722__$1);

return statearr_48752;
})();
if(cljs.core.truth_(inst_48723)){
var statearr_48754_50189 = state_48742__$1;
(statearr_48754_50189[(1)] = (5));

} else {
var statearr_48755_50190 = state_48742__$1;
(statearr_48755_50190[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (6))){
var inst_48722 = (state_48742[(7)]);
var inst_48727 = (p.cljs$core$IFn$_invoke$arity$1 ? p.cljs$core$IFn$_invoke$arity$1(inst_48722) : p.call(null,inst_48722));
var state_48742__$1 = state_48742;
if(cljs.core.truth_(inst_48727)){
var statearr_48762_50192 = state_48742__$1;
(statearr_48762_50192[(1)] = (8));

} else {
var statearr_48763_50193 = state_48742__$1;
(statearr_48763_50193[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (3))){
var inst_48738 = (state_48742[(2)]);
var state_48742__$1 = state_48742;
return cljs.core.async.impl.ioc_helpers.return_chan(state_48742__$1,inst_48738);
} else {
if((state_val_48743 === (2))){
var state_48742__$1 = state_48742;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_48742__$1,(4),ch);
} else {
if((state_val_48743 === (11))){
var inst_48730 = (state_48742[(2)]);
var state_48742__$1 = state_48742;
var statearr_48766_50194 = state_48742__$1;
(statearr_48766_50194[(2)] = inst_48730);

(statearr_48766_50194[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (9))){
var state_48742__$1 = state_48742;
var statearr_48768_50195 = state_48742__$1;
(statearr_48768_50195[(2)] = null);

(statearr_48768_50195[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (5))){
var inst_48725 = cljs.core.async.close_BANG_(out);
var state_48742__$1 = state_48742;
var statearr_48770_50196 = state_48742__$1;
(statearr_48770_50196[(2)] = inst_48725);

(statearr_48770_50196[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (10))){
var inst_48733 = (state_48742[(2)]);
var state_48742__$1 = (function (){var statearr_48771 = state_48742;
(statearr_48771[(8)] = inst_48733);

return statearr_48771;
})();
var statearr_48772_50197 = state_48742__$1;
(statearr_48772_50197[(2)] = null);

(statearr_48772_50197[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48743 === (8))){
var inst_48722 = (state_48742[(7)]);
var state_48742__$1 = state_48742;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_48742__$1,(11),out,inst_48722);
} else {
return null;
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
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_48773 = [null,null,null,null,null,null,null,null,null];
(statearr_48773[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_48773[(1)] = (1));

return statearr_48773;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_48742){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_48742);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e48775){var ex__45565__auto__ = e48775;
var statearr_48776_50201 = state_48742;
(statearr_48776_50201[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_48742[(4)]))){
var statearr_48777_50202 = state_48742;
(statearr_48777_50202[(1)] = cljs.core.first((state_48742[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50203 = state_48742;
state_48742 = G__50203;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_48742){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_48742);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_48778 = f__45941__auto__();
(statearr_48778[(6)] = c__45940__auto___50183);

return statearr_48778;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return out;
}));

(cljs.core.async.filter_LT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_LT_ = (function cljs$core$async$remove_LT_(var_args){
var G__48786 = arguments.length;
switch (G__48786) {
case 2:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3(p,ch,null);
}));

(cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3(cljs.core.complement(p),ch,buf_or_n);
}));

(cljs.core.async.remove_LT_.cljs$lang$maxFixedArity = 3);

cljs.core.async.mapcat_STAR_ = (function cljs$core$async$mapcat_STAR_(f,in$,out){
var c__45940__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_48864){
var state_val_48865 = (state_48864[(1)]);
if((state_val_48865 === (7))){
var inst_48858 = (state_48864[(2)]);
var state_48864__$1 = state_48864;
var statearr_48867_50207 = state_48864__$1;
(statearr_48867_50207[(2)] = inst_48858);

(statearr_48867_50207[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (20))){
var inst_48828 = (state_48864[(7)]);
var inst_48839 = (state_48864[(2)]);
var inst_48840 = cljs.core.next(inst_48828);
var inst_48810 = inst_48840;
var inst_48811 = null;
var inst_48812 = (0);
var inst_48813 = (0);
var state_48864__$1 = (function (){var statearr_48868 = state_48864;
(statearr_48868[(8)] = inst_48839);

(statearr_48868[(9)] = inst_48810);

(statearr_48868[(10)] = inst_48811);

(statearr_48868[(11)] = inst_48812);

(statearr_48868[(12)] = inst_48813);

return statearr_48868;
})();
var statearr_48869_50208 = state_48864__$1;
(statearr_48869_50208[(2)] = null);

(statearr_48869_50208[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (1))){
var state_48864__$1 = state_48864;
var statearr_48870_50211 = state_48864__$1;
(statearr_48870_50211[(2)] = null);

(statearr_48870_50211[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (4))){
var inst_48798 = (state_48864[(13)]);
var inst_48798__$1 = (state_48864[(2)]);
var inst_48799 = (inst_48798__$1 == null);
var state_48864__$1 = (function (){var statearr_48871 = state_48864;
(statearr_48871[(13)] = inst_48798__$1);

return statearr_48871;
})();
if(cljs.core.truth_(inst_48799)){
var statearr_48873_50212 = state_48864__$1;
(statearr_48873_50212[(1)] = (5));

} else {
var statearr_48874_50213 = state_48864__$1;
(statearr_48874_50213[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (15))){
var state_48864__$1 = state_48864;
var statearr_48881_50214 = state_48864__$1;
(statearr_48881_50214[(2)] = null);

(statearr_48881_50214[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (21))){
var state_48864__$1 = state_48864;
var statearr_48884_50215 = state_48864__$1;
(statearr_48884_50215[(2)] = null);

(statearr_48884_50215[(1)] = (23));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (13))){
var inst_48813 = (state_48864[(12)]);
var inst_48810 = (state_48864[(9)]);
var inst_48811 = (state_48864[(10)]);
var inst_48812 = (state_48864[(11)]);
var inst_48824 = (state_48864[(2)]);
var inst_48825 = (inst_48813 + (1));
var tmp48877 = inst_48810;
var tmp48878 = inst_48812;
var tmp48879 = inst_48811;
var inst_48810__$1 = tmp48877;
var inst_48811__$1 = tmp48879;
var inst_48812__$1 = tmp48878;
var inst_48813__$1 = inst_48825;
var state_48864__$1 = (function (){var statearr_48886 = state_48864;
(statearr_48886[(14)] = inst_48824);

(statearr_48886[(9)] = inst_48810__$1);

(statearr_48886[(10)] = inst_48811__$1);

(statearr_48886[(11)] = inst_48812__$1);

(statearr_48886[(12)] = inst_48813__$1);

return statearr_48886;
})();
var statearr_48889_50216 = state_48864__$1;
(statearr_48889_50216[(2)] = null);

(statearr_48889_50216[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (22))){
var state_48864__$1 = state_48864;
var statearr_48896_50217 = state_48864__$1;
(statearr_48896_50217[(2)] = null);

(statearr_48896_50217[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (6))){
var inst_48798 = (state_48864[(13)]);
var inst_48808 = (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(inst_48798) : f.call(null,inst_48798));
var inst_48809 = cljs.core.seq(inst_48808);
var inst_48810 = inst_48809;
var inst_48811 = null;
var inst_48812 = (0);
var inst_48813 = (0);
var state_48864__$1 = (function (){var statearr_48897 = state_48864;
(statearr_48897[(9)] = inst_48810);

(statearr_48897[(10)] = inst_48811);

(statearr_48897[(11)] = inst_48812);

(statearr_48897[(12)] = inst_48813);

return statearr_48897;
})();
var statearr_48898_50218 = state_48864__$1;
(statearr_48898_50218[(2)] = null);

(statearr_48898_50218[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (17))){
var inst_48828 = (state_48864[(7)]);
var inst_48832 = cljs.core.chunk_first(inst_48828);
var inst_48833 = cljs.core.chunk_rest(inst_48828);
var inst_48834 = cljs.core.count(inst_48832);
var inst_48810 = inst_48833;
var inst_48811 = inst_48832;
var inst_48812 = inst_48834;
var inst_48813 = (0);
var state_48864__$1 = (function (){var statearr_48905 = state_48864;
(statearr_48905[(9)] = inst_48810);

(statearr_48905[(10)] = inst_48811);

(statearr_48905[(11)] = inst_48812);

(statearr_48905[(12)] = inst_48813);

return statearr_48905;
})();
var statearr_48907_50221 = state_48864__$1;
(statearr_48907_50221[(2)] = null);

(statearr_48907_50221[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (3))){
var inst_48860 = (state_48864[(2)]);
var state_48864__$1 = state_48864;
return cljs.core.async.impl.ioc_helpers.return_chan(state_48864__$1,inst_48860);
} else {
if((state_val_48865 === (12))){
var inst_48848 = (state_48864[(2)]);
var state_48864__$1 = state_48864;
var statearr_48912_50222 = state_48864__$1;
(statearr_48912_50222[(2)] = inst_48848);

(statearr_48912_50222[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (2))){
var state_48864__$1 = state_48864;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_48864__$1,(4),in$);
} else {
if((state_val_48865 === (23))){
var inst_48856 = (state_48864[(2)]);
var state_48864__$1 = state_48864;
var statearr_48914_50224 = state_48864__$1;
(statearr_48914_50224[(2)] = inst_48856);

(statearr_48914_50224[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (19))){
var inst_48843 = (state_48864[(2)]);
var state_48864__$1 = state_48864;
var statearr_48920_50226 = state_48864__$1;
(statearr_48920_50226[(2)] = inst_48843);

(statearr_48920_50226[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (11))){
var inst_48810 = (state_48864[(9)]);
var inst_48828 = (state_48864[(7)]);
var inst_48828__$1 = cljs.core.seq(inst_48810);
var state_48864__$1 = (function (){var statearr_48927 = state_48864;
(statearr_48927[(7)] = inst_48828__$1);

return statearr_48927;
})();
if(inst_48828__$1){
var statearr_48929_50227 = state_48864__$1;
(statearr_48929_50227[(1)] = (14));

} else {
var statearr_48930_50229 = state_48864__$1;
(statearr_48930_50229[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (9))){
var inst_48850 = (state_48864[(2)]);
var inst_48851 = cljs.core.async.impl.protocols.closed_QMARK_(out);
var state_48864__$1 = (function (){var statearr_48932 = state_48864;
(statearr_48932[(15)] = inst_48850);

return statearr_48932;
})();
if(cljs.core.truth_(inst_48851)){
var statearr_48933_50231 = state_48864__$1;
(statearr_48933_50231[(1)] = (21));

} else {
var statearr_48934_50232 = state_48864__$1;
(statearr_48934_50232[(1)] = (22));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (5))){
var inst_48801 = cljs.core.async.close_BANG_(out);
var state_48864__$1 = state_48864;
var statearr_48941_50234 = state_48864__$1;
(statearr_48941_50234[(2)] = inst_48801);

(statearr_48941_50234[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (14))){
var inst_48828 = (state_48864[(7)]);
var inst_48830 = cljs.core.chunked_seq_QMARK_(inst_48828);
var state_48864__$1 = state_48864;
if(inst_48830){
var statearr_48944_50236 = state_48864__$1;
(statearr_48944_50236[(1)] = (17));

} else {
var statearr_48945_50237 = state_48864__$1;
(statearr_48945_50237[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (16))){
var inst_48846 = (state_48864[(2)]);
var state_48864__$1 = state_48864;
var statearr_48948_50239 = state_48864__$1;
(statearr_48948_50239[(2)] = inst_48846);

(statearr_48948_50239[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_48865 === (10))){
var inst_48811 = (state_48864[(10)]);
var inst_48813 = (state_48864[(12)]);
var inst_48822 = cljs.core._nth(inst_48811,inst_48813);
var state_48864__$1 = state_48864;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_48864__$1,(13),out,inst_48822);
} else {
if((state_val_48865 === (18))){
var inst_48828 = (state_48864[(7)]);
var inst_48837 = cljs.core.first(inst_48828);
var state_48864__$1 = state_48864;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_48864__$1,(20),out,inst_48837);
} else {
if((state_val_48865 === (8))){
var inst_48813 = (state_48864[(12)]);
var inst_48812 = (state_48864[(11)]);
var inst_48815 = (inst_48813 < inst_48812);
var inst_48816 = inst_48815;
var state_48864__$1 = state_48864;
if(cljs.core.truth_(inst_48816)){
var statearr_48953_50243 = state_48864__$1;
(statearr_48953_50243[(1)] = (10));

} else {
var statearr_48954_50244 = state_48864__$1;
(statearr_48954_50244[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
});
return (function() {
var cljs$core$async$mapcat_STAR__$_state_machine__45562__auto__ = null;
var cljs$core$async$mapcat_STAR__$_state_machine__45562__auto____0 = (function (){
var statearr_48959 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_48959[(0)] = cljs$core$async$mapcat_STAR__$_state_machine__45562__auto__);

(statearr_48959[(1)] = (1));

return statearr_48959;
});
var cljs$core$async$mapcat_STAR__$_state_machine__45562__auto____1 = (function (state_48864){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_48864);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e48960){var ex__45565__auto__ = e48960;
var statearr_48962_50248 = state_48864;
(statearr_48962_50248[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_48864[(4)]))){
var statearr_48965_50250 = state_48864;
(statearr_48965_50250[(1)] = cljs.core.first((state_48864[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50251 = state_48864;
state_48864 = G__50251;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$mapcat_STAR__$_state_machine__45562__auto__ = function(state_48864){
switch(arguments.length){
case 0:
return cljs$core$async$mapcat_STAR__$_state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$mapcat_STAR__$_state_machine__45562__auto____1.call(this,state_48864);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mapcat_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mapcat_STAR__$_state_machine__45562__auto____0;
cljs$core$async$mapcat_STAR__$_state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mapcat_STAR__$_state_machine__45562__auto____1;
return cljs$core$async$mapcat_STAR__$_state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_48968 = f__45941__auto__();
(statearr_48968[(6)] = c__45940__auto__);

return statearr_48968;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));

return c__45940__auto__;
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_LT_ = (function cljs$core$async$mapcat_LT_(var_args){
var G__48971 = arguments.length;
switch (G__48971) {
case 2:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2 = (function (f,in$){
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3(f,in$,null);
}));

(cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3 = (function (f,in$,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
cljs.core.async.mapcat_STAR_(f,in$,out);

return out;
}));

(cljs.core.async.mapcat_LT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_GT_ = (function cljs$core$async$mapcat_GT_(var_args){
var G__48978 = arguments.length;
switch (G__48978) {
case 2:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2 = (function (f,out){
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3(f,out,null);
}));

(cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3 = (function (f,out,buf_or_n){
var in$ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
cljs.core.async.mapcat_STAR_(f,in$,out);

return in$;
}));

(cljs.core.async.mapcat_GT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.unique = (function cljs$core$async$unique(var_args){
var G__48982 = arguments.length;
switch (G__48982) {
case 1:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1 = (function (ch){
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2(ch,null);
}));

(cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2 = (function (ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__45940__auto___50258 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_49011){
var state_val_49012 = (state_49011[(1)]);
if((state_val_49012 === (7))){
var inst_49005 = (state_49011[(2)]);
var state_49011__$1 = state_49011;
var statearr_49017_50262 = state_49011__$1;
(statearr_49017_50262[(2)] = inst_49005);

(statearr_49017_50262[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49012 === (1))){
var inst_48987 = null;
var state_49011__$1 = (function (){var statearr_49018 = state_49011;
(statearr_49018[(7)] = inst_48987);

return statearr_49018;
})();
var statearr_49019_50276 = state_49011__$1;
(statearr_49019_50276[(2)] = null);

(statearr_49019_50276[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49012 === (4))){
var inst_48990 = (state_49011[(8)]);
var inst_48990__$1 = (state_49011[(2)]);
var inst_48991 = (inst_48990__$1 == null);
var inst_48992 = cljs.core.not(inst_48991);
var state_49011__$1 = (function (){var statearr_49020 = state_49011;
(statearr_49020[(8)] = inst_48990__$1);

return statearr_49020;
})();
if(inst_48992){
var statearr_49021_50277 = state_49011__$1;
(statearr_49021_50277[(1)] = (5));

} else {
var statearr_49022_50278 = state_49011__$1;
(statearr_49022_50278[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49012 === (6))){
var state_49011__$1 = state_49011;
var statearr_49023_50279 = state_49011__$1;
(statearr_49023_50279[(2)] = null);

(statearr_49023_50279[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49012 === (3))){
var inst_49007 = (state_49011[(2)]);
var inst_49008 = cljs.core.async.close_BANG_(out);
var state_49011__$1 = (function (){var statearr_49024 = state_49011;
(statearr_49024[(9)] = inst_49007);

return statearr_49024;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_49011__$1,inst_49008);
} else {
if((state_val_49012 === (2))){
var state_49011__$1 = state_49011;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_49011__$1,(4),ch);
} else {
if((state_val_49012 === (11))){
var inst_48990 = (state_49011[(8)]);
var inst_48999 = (state_49011[(2)]);
var inst_48987 = inst_48990;
var state_49011__$1 = (function (){var statearr_49025 = state_49011;
(statearr_49025[(10)] = inst_48999);

(statearr_49025[(7)] = inst_48987);

return statearr_49025;
})();
var statearr_49026_50280 = state_49011__$1;
(statearr_49026_50280[(2)] = null);

(statearr_49026_50280[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49012 === (9))){
var inst_48990 = (state_49011[(8)]);
var state_49011__$1 = state_49011;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_49011__$1,(11),out,inst_48990);
} else {
if((state_val_49012 === (5))){
var inst_48990 = (state_49011[(8)]);
var inst_48987 = (state_49011[(7)]);
var inst_48994 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_48990,inst_48987);
var state_49011__$1 = state_49011;
if(inst_48994){
var statearr_49028_50281 = state_49011__$1;
(statearr_49028_50281[(1)] = (8));

} else {
var statearr_49029_50283 = state_49011__$1;
(statearr_49029_50283[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49012 === (10))){
var inst_49002 = (state_49011[(2)]);
var state_49011__$1 = state_49011;
var statearr_49031_50284 = state_49011__$1;
(statearr_49031_50284[(2)] = inst_49002);

(statearr_49031_50284[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49012 === (8))){
var inst_48987 = (state_49011[(7)]);
var tmp49027 = inst_48987;
var inst_48987__$1 = tmp49027;
var state_49011__$1 = (function (){var statearr_49032 = state_49011;
(statearr_49032[(7)] = inst_48987__$1);

return statearr_49032;
})();
var statearr_49037_50286 = state_49011__$1;
(statearr_49037_50286[(2)] = null);

(statearr_49037_50286[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_49045 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_49045[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_49045[(1)] = (1));

return statearr_49045;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_49011){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_49011);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e49046){var ex__45565__auto__ = e49046;
var statearr_49047_50288 = state_49011;
(statearr_49047_50288[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_49011[(4)]))){
var statearr_49049_50289 = state_49011;
(statearr_49049_50289[(1)] = cljs.core.first((state_49011[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50291 = state_49011;
state_49011 = G__50291;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_49011){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_49011);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_49050 = f__45941__auto__();
(statearr_49050[(6)] = c__45940__auto___50258);

return statearr_49050;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return out;
}));

(cljs.core.async.unique.cljs$lang$maxFixedArity = 2);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition = (function cljs$core$async$partition(var_args){
var G__49054 = arguments.length;
switch (G__49054) {
case 2:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3(n,ch,null);
}));

(cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__45940__auto___50294 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_49101){
var state_val_49102 = (state_49101[(1)]);
if((state_val_49102 === (7))){
var inst_49097 = (state_49101[(2)]);
var state_49101__$1 = state_49101;
var statearr_49107_50295 = state_49101__$1;
(statearr_49107_50295[(2)] = inst_49097);

(statearr_49107_50295[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (1))){
var inst_49063 = (new Array(n));
var inst_49064 = inst_49063;
var inst_49065 = (0);
var state_49101__$1 = (function (){var statearr_49110 = state_49101;
(statearr_49110[(7)] = inst_49064);

(statearr_49110[(8)] = inst_49065);

return statearr_49110;
})();
var statearr_49113_50298 = state_49101__$1;
(statearr_49113_50298[(2)] = null);

(statearr_49113_50298[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (4))){
var inst_49068 = (state_49101[(9)]);
var inst_49068__$1 = (state_49101[(2)]);
var inst_49069 = (inst_49068__$1 == null);
var inst_49070 = cljs.core.not(inst_49069);
var state_49101__$1 = (function (){var statearr_49116 = state_49101;
(statearr_49116[(9)] = inst_49068__$1);

return statearr_49116;
})();
if(inst_49070){
var statearr_49119_50299 = state_49101__$1;
(statearr_49119_50299[(1)] = (5));

} else {
var statearr_49122_50300 = state_49101__$1;
(statearr_49122_50300[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (15))){
var inst_49090 = (state_49101[(2)]);
var state_49101__$1 = state_49101;
var statearr_49127_50302 = state_49101__$1;
(statearr_49127_50302[(2)] = inst_49090);

(statearr_49127_50302[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (13))){
var state_49101__$1 = state_49101;
var statearr_49130_50303 = state_49101__$1;
(statearr_49130_50303[(2)] = null);

(statearr_49130_50303[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (6))){
var inst_49065 = (state_49101[(8)]);
var inst_49086 = (inst_49065 > (0));
var state_49101__$1 = state_49101;
if(cljs.core.truth_(inst_49086)){
var statearr_49135_50307 = state_49101__$1;
(statearr_49135_50307[(1)] = (12));

} else {
var statearr_49136_50311 = state_49101__$1;
(statearr_49136_50311[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (3))){
var inst_49099 = (state_49101[(2)]);
var state_49101__$1 = state_49101;
return cljs.core.async.impl.ioc_helpers.return_chan(state_49101__$1,inst_49099);
} else {
if((state_val_49102 === (12))){
var inst_49064 = (state_49101[(7)]);
var inst_49088 = cljs.core.vec(inst_49064);
var state_49101__$1 = state_49101;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_49101__$1,(15),out,inst_49088);
} else {
if((state_val_49102 === (2))){
var state_49101__$1 = state_49101;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_49101__$1,(4),ch);
} else {
if((state_val_49102 === (11))){
var inst_49080 = (state_49101[(2)]);
var inst_49081 = (new Array(n));
var inst_49064 = inst_49081;
var inst_49065 = (0);
var state_49101__$1 = (function (){var statearr_49146 = state_49101;
(statearr_49146[(10)] = inst_49080);

(statearr_49146[(7)] = inst_49064);

(statearr_49146[(8)] = inst_49065);

return statearr_49146;
})();
var statearr_49148_50314 = state_49101__$1;
(statearr_49148_50314[(2)] = null);

(statearr_49148_50314[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (9))){
var inst_49064 = (state_49101[(7)]);
var inst_49078 = cljs.core.vec(inst_49064);
var state_49101__$1 = state_49101;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_49101__$1,(11),out,inst_49078);
} else {
if((state_val_49102 === (5))){
var inst_49064 = (state_49101[(7)]);
var inst_49065 = (state_49101[(8)]);
var inst_49068 = (state_49101[(9)]);
var inst_49073 = (state_49101[(11)]);
var inst_49072 = (inst_49064[inst_49065] = inst_49068);
var inst_49073__$1 = (inst_49065 + (1));
var inst_49074 = (inst_49073__$1 < n);
var state_49101__$1 = (function (){var statearr_49158 = state_49101;
(statearr_49158[(12)] = inst_49072);

(statearr_49158[(11)] = inst_49073__$1);

return statearr_49158;
})();
if(cljs.core.truth_(inst_49074)){
var statearr_49161_50315 = state_49101__$1;
(statearr_49161_50315[(1)] = (8));

} else {
var statearr_49163_50316 = state_49101__$1;
(statearr_49163_50316[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (14))){
var inst_49093 = (state_49101[(2)]);
var inst_49095 = cljs.core.async.close_BANG_(out);
var state_49101__$1 = (function (){var statearr_49167 = state_49101;
(statearr_49167[(13)] = inst_49093);

return statearr_49167;
})();
var statearr_49172_50317 = state_49101__$1;
(statearr_49172_50317[(2)] = inst_49095);

(statearr_49172_50317[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (10))){
var inst_49084 = (state_49101[(2)]);
var state_49101__$1 = state_49101;
var statearr_49176_50318 = state_49101__$1;
(statearr_49176_50318[(2)] = inst_49084);

(statearr_49176_50318[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49102 === (8))){
var inst_49064 = (state_49101[(7)]);
var inst_49073 = (state_49101[(11)]);
var tmp49165 = inst_49064;
var inst_49064__$1 = tmp49165;
var inst_49065 = inst_49073;
var state_49101__$1 = (function (){var statearr_49181 = state_49101;
(statearr_49181[(7)] = inst_49064__$1);

(statearr_49181[(8)] = inst_49065);

return statearr_49181;
})();
var statearr_49182_50323 = state_49101__$1;
(statearr_49182_50323[(2)] = null);

(statearr_49182_50323[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_49184 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_49184[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_49184[(1)] = (1));

return statearr_49184;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_49101){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_49101);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e49185){var ex__45565__auto__ = e49185;
var statearr_49186_50325 = state_49101;
(statearr_49186_50325[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_49101[(4)]))){
var statearr_49187_50326 = state_49101;
(statearr_49187_50326[(1)] = cljs.core.first((state_49101[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50327 = state_49101;
state_49101 = G__50327;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_49101){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_49101);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_49189 = f__45941__auto__();
(statearr_49189[(6)] = c__45940__auto___50294);

return statearr_49189;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return out;
}));

(cljs.core.async.partition.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition_by = (function cljs$core$async$partition_by(var_args){
var G__49192 = arguments.length;
switch (G__49192) {
case 2:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2 = (function (f,ch){
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3(f,ch,null);
}));

(cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3 = (function (f,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__45940__auto___50330 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__45941__auto__ = (function (){var switch__45561__auto__ = (function (state_49247){
var state_val_49248 = (state_49247[(1)]);
if((state_val_49248 === (7))){
var inst_49242 = (state_49247[(2)]);
var state_49247__$1 = state_49247;
var statearr_49256_50335 = state_49247__$1;
(statearr_49256_50335[(2)] = inst_49242);

(statearr_49256_50335[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (1))){
var inst_49194 = [];
var inst_49195 = inst_49194;
var inst_49196 = new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123);
var state_49247__$1 = (function (){var statearr_49261 = state_49247;
(statearr_49261[(7)] = inst_49195);

(statearr_49261[(8)] = inst_49196);

return statearr_49261;
})();
var statearr_49262_50344 = state_49247__$1;
(statearr_49262_50344[(2)] = null);

(statearr_49262_50344[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (4))){
var inst_49200 = (state_49247[(9)]);
var inst_49200__$1 = (state_49247[(2)]);
var inst_49201 = (inst_49200__$1 == null);
var inst_49202 = cljs.core.not(inst_49201);
var state_49247__$1 = (function (){var statearr_49263 = state_49247;
(statearr_49263[(9)] = inst_49200__$1);

return statearr_49263;
})();
if(inst_49202){
var statearr_49264_50346 = state_49247__$1;
(statearr_49264_50346[(1)] = (5));

} else {
var statearr_49265_50347 = state_49247__$1;
(statearr_49265_50347[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (15))){
var inst_49195 = (state_49247[(7)]);
var inst_49234 = cljs.core.vec(inst_49195);
var state_49247__$1 = state_49247;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_49247__$1,(18),out,inst_49234);
} else {
if((state_val_49248 === (13))){
var inst_49228 = (state_49247[(2)]);
var state_49247__$1 = state_49247;
var statearr_49266_50350 = state_49247__$1;
(statearr_49266_50350[(2)] = inst_49228);

(statearr_49266_50350[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (6))){
var inst_49195 = (state_49247[(7)]);
var inst_49230 = inst_49195.length;
var inst_49231 = (inst_49230 > (0));
var state_49247__$1 = state_49247;
if(cljs.core.truth_(inst_49231)){
var statearr_49267_50351 = state_49247__$1;
(statearr_49267_50351[(1)] = (15));

} else {
var statearr_49268_50352 = state_49247__$1;
(statearr_49268_50352[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (17))){
var inst_49239 = (state_49247[(2)]);
var inst_49240 = cljs.core.async.close_BANG_(out);
var state_49247__$1 = (function (){var statearr_49269 = state_49247;
(statearr_49269[(10)] = inst_49239);

return statearr_49269;
})();
var statearr_49270_50353 = state_49247__$1;
(statearr_49270_50353[(2)] = inst_49240);

(statearr_49270_50353[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (3))){
var inst_49244 = (state_49247[(2)]);
var state_49247__$1 = state_49247;
return cljs.core.async.impl.ioc_helpers.return_chan(state_49247__$1,inst_49244);
} else {
if((state_val_49248 === (12))){
var inst_49195 = (state_49247[(7)]);
var inst_49219 = cljs.core.vec(inst_49195);
var state_49247__$1 = state_49247;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_49247__$1,(14),out,inst_49219);
} else {
if((state_val_49248 === (2))){
var state_49247__$1 = state_49247;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_49247__$1,(4),ch);
} else {
if((state_val_49248 === (11))){
var inst_49195 = (state_49247[(7)]);
var inst_49200 = (state_49247[(9)]);
var inst_49204 = (state_49247[(11)]);
var inst_49212 = inst_49195.push(inst_49200);
var tmp49271 = inst_49195;
var inst_49195__$1 = tmp49271;
var inst_49196 = inst_49204;
var state_49247__$1 = (function (){var statearr_49277 = state_49247;
(statearr_49277[(12)] = inst_49212);

(statearr_49277[(7)] = inst_49195__$1);

(statearr_49277[(8)] = inst_49196);

return statearr_49277;
})();
var statearr_49278_50360 = state_49247__$1;
(statearr_49278_50360[(2)] = null);

(statearr_49278_50360[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (9))){
var inst_49196 = (state_49247[(8)]);
var inst_49208 = cljs.core.keyword_identical_QMARK_(inst_49196,new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123));
var state_49247__$1 = state_49247;
var statearr_49284_50364 = state_49247__$1;
(statearr_49284_50364[(2)] = inst_49208);

(statearr_49284_50364[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (5))){
var inst_49200 = (state_49247[(9)]);
var inst_49204 = (state_49247[(11)]);
var inst_49196 = (state_49247[(8)]);
var inst_49205 = (state_49247[(13)]);
var inst_49204__$1 = (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(inst_49200) : f.call(null,inst_49200));
var inst_49205__$1 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_49204__$1,inst_49196);
var state_49247__$1 = (function (){var statearr_49285 = state_49247;
(statearr_49285[(11)] = inst_49204__$1);

(statearr_49285[(13)] = inst_49205__$1);

return statearr_49285;
})();
if(inst_49205__$1){
var statearr_49286_50366 = state_49247__$1;
(statearr_49286_50366[(1)] = (8));

} else {
var statearr_49287_50367 = state_49247__$1;
(statearr_49287_50367[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (14))){
var inst_49200 = (state_49247[(9)]);
var inst_49204 = (state_49247[(11)]);
var inst_49221 = (state_49247[(2)]);
var inst_49224 = [];
var inst_49225 = inst_49224.push(inst_49200);
var inst_49195 = inst_49224;
var inst_49196 = inst_49204;
var state_49247__$1 = (function (){var statearr_49288 = state_49247;
(statearr_49288[(14)] = inst_49221);

(statearr_49288[(15)] = inst_49225);

(statearr_49288[(7)] = inst_49195);

(statearr_49288[(8)] = inst_49196);

return statearr_49288;
})();
var statearr_49290_50368 = state_49247__$1;
(statearr_49290_50368[(2)] = null);

(statearr_49290_50368[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (16))){
var state_49247__$1 = state_49247;
var statearr_49294_50369 = state_49247__$1;
(statearr_49294_50369[(2)] = null);

(statearr_49294_50369[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (10))){
var inst_49210 = (state_49247[(2)]);
var state_49247__$1 = state_49247;
if(cljs.core.truth_(inst_49210)){
var statearr_49295_50370 = state_49247__$1;
(statearr_49295_50370[(1)] = (11));

} else {
var statearr_49296_50371 = state_49247__$1;
(statearr_49296_50371[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (18))){
var inst_49236 = (state_49247[(2)]);
var state_49247__$1 = state_49247;
var statearr_49297_50374 = state_49247__$1;
(statearr_49297_50374[(2)] = inst_49236);

(statearr_49297_50374[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_49248 === (8))){
var inst_49205 = (state_49247[(13)]);
var state_49247__$1 = state_49247;
var statearr_49299_50375 = state_49247__$1;
(statearr_49299_50375[(2)] = inst_49205);

(statearr_49299_50375[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
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
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__45562__auto__ = null;
var cljs$core$async$state_machine__45562__auto____0 = (function (){
var statearr_49304 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_49304[(0)] = cljs$core$async$state_machine__45562__auto__);

(statearr_49304[(1)] = (1));

return statearr_49304;
});
var cljs$core$async$state_machine__45562__auto____1 = (function (state_49247){
while(true){
var ret_value__45563__auto__ = (function (){try{while(true){
var result__45564__auto__ = switch__45561__auto__(state_49247);
if(cljs.core.keyword_identical_QMARK_(result__45564__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__45564__auto__;
}
break;
}
}catch (e49305){var ex__45565__auto__ = e49305;
var statearr_49306_50377 = state_49247;
(statearr_49306_50377[(2)] = ex__45565__auto__);


if(cljs.core.seq((state_49247[(4)]))){
var statearr_49307_50378 = state_49247;
(statearr_49307_50378[(1)] = cljs.core.first((state_49247[(4)])));

} else {
throw ex__45565__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__45563__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__50380 = state_49247;
state_49247 = G__50380;
continue;
} else {
return ret_value__45563__auto__;
}
break;
}
});
cljs$core$async$state_machine__45562__auto__ = function(state_49247){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__45562__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__45562__auto____1.call(this,state_49247);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__45562__auto____0;
cljs$core$async$state_machine__45562__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__45562__auto____1;
return cljs$core$async$state_machine__45562__auto__;
})()
})();
var state__45942__auto__ = (function (){var statearr_49313 = f__45941__auto__();
(statearr_49313[(6)] = c__45940__auto___50330);

return statearr_49313;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__45942__auto__);
}));


return out;
}));

(cljs.core.async.partition_by.cljs$lang$maxFixedArity = 3);


//# sourceMappingURL=cljs.core.async.js.map
