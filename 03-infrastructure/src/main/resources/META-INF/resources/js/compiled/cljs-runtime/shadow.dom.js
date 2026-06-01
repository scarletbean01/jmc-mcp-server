goog.provide('shadow.dom');
shadow.dom.transition_supported_QMARK_ = true;

/**
 * @interface
 */
shadow.dom.IElement = function(){};

var shadow$dom$IElement$_to_dom$dyn_44219 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (shadow.dom._to_dom[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (shadow.dom._to_dom["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IElement.-to-dom",this$);
}
}
});
shadow.dom._to_dom = (function shadow$dom$_to_dom(this$){
if((((!((this$ == null)))) && ((!((this$.shadow$dom$IElement$_to_dom$arity$1 == null)))))){
return this$.shadow$dom$IElement$_to_dom$arity$1(this$);
} else {
return shadow$dom$IElement$_to_dom$dyn_44219(this$);
}
});


/**
 * @interface
 */
shadow.dom.SVGElement = function(){};

var shadow$dom$SVGElement$_to_svg$dyn_44222 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (shadow.dom._to_svg[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (shadow.dom._to_svg["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("SVGElement.-to-svg",this$);
}
}
});
shadow.dom._to_svg = (function shadow$dom$_to_svg(this$){
if((((!((this$ == null)))) && ((!((this$.shadow$dom$SVGElement$_to_svg$arity$1 == null)))))){
return this$.shadow$dom$SVGElement$_to_svg$arity$1(this$);
} else {
return shadow$dom$SVGElement$_to_svg$dyn_44222(this$);
}
});

shadow.dom.lazy_native_coll_seq = (function shadow$dom$lazy_native_coll_seq(coll,idx){
if((idx < coll.length)){
return (new cljs.core.LazySeq(null,(function (){
return cljs.core.cons((coll[idx]),(function (){var G__43266 = coll;
var G__43267 = (idx + (1));
return (shadow.dom.lazy_native_coll_seq.cljs$core$IFn$_invoke$arity$2 ? shadow.dom.lazy_native_coll_seq.cljs$core$IFn$_invoke$arity$2(G__43266,G__43267) : shadow.dom.lazy_native_coll_seq.call(null,G__43266,G__43267));
})());
}),null,null));
} else {
return null;
}
});

/**
* @constructor
 * @implements {cljs.core.IIndexed}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IDeref}
 * @implements {shadow.dom.IElement}
*/
shadow.dom.NativeColl = (function (coll){
this.coll = coll;
this.cljs$lang$protocol_mask$partition0$ = 8421394;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(shadow.dom.NativeColl.prototype.cljs$core$IDeref$_deref$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll;
}));

(shadow.dom.NativeColl.prototype.cljs$core$IIndexed$_nth$arity$2 = (function (this$,n){
var self__ = this;
var this$__$1 = this;
return (self__.coll[n]);
}));

(shadow.dom.NativeColl.prototype.cljs$core$IIndexed$_nth$arity$3 = (function (this$,n,not_found){
var self__ = this;
var this$__$1 = this;
var or__5142__auto__ = (self__.coll[n]);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return not_found;
}
}));

(shadow.dom.NativeColl.prototype.cljs$core$ICounted$_count$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll.length;
}));

(shadow.dom.NativeColl.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return shadow.dom.lazy_native_coll_seq(self__.coll,(0));
}));

(shadow.dom.NativeColl.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.dom.NativeColl.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll;
}));

(shadow.dom.NativeColl.getBasis = (function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"coll","coll",-1006698606,null)], null);
}));

(shadow.dom.NativeColl.cljs$lang$type = true);

(shadow.dom.NativeColl.cljs$lang$ctorStr = "shadow.dom/NativeColl");

(shadow.dom.NativeColl.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"shadow.dom/NativeColl");
}));

/**
 * Positional factory function for shadow.dom/NativeColl.
 */
shadow.dom.__GT_NativeColl = (function shadow$dom$__GT_NativeColl(coll){
return (new shadow.dom.NativeColl(coll));
});

shadow.dom.native_coll = (function shadow$dom$native_coll(coll){
return (new shadow.dom.NativeColl(coll));
});
shadow.dom.dom_node = (function shadow$dom$dom_node(el){
if((el == null)){
return null;
} else {
if((((!((el == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === el.shadow$dom$IElement$))))?true:false):false)){
return el.shadow$dom$IElement$_to_dom$arity$1(null);
} else {
if(typeof el === 'string'){
return document.createTextNode(el);
} else {
if(typeof el === 'number'){
return document.createTextNode((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(el)));
} else {
return el;

}
}
}
}
});
shadow.dom.query_one = (function shadow$dom$query_one(var_args){
var G__43297 = arguments.length;
switch (G__43297) {
case 1:
return shadow.dom.query_one.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.query_one.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.query_one.cljs$core$IFn$_invoke$arity$1 = (function (sel){
return document.querySelector(sel);
}));

(shadow.dom.query_one.cljs$core$IFn$_invoke$arity$2 = (function (sel,root){
return shadow.dom.dom_node(root).querySelector(sel);
}));

(shadow.dom.query_one.cljs$lang$maxFixedArity = 2);

shadow.dom.query = (function shadow$dom$query(var_args){
var G__43314 = arguments.length;
switch (G__43314) {
case 1:
return shadow.dom.query.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.query.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.query.cljs$core$IFn$_invoke$arity$1 = (function (sel){
return (new shadow.dom.NativeColl(document.querySelectorAll(sel)));
}));

(shadow.dom.query.cljs$core$IFn$_invoke$arity$2 = (function (sel,root){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(root).querySelectorAll(sel)));
}));

(shadow.dom.query.cljs$lang$maxFixedArity = 2);

shadow.dom.by_id = (function shadow$dom$by_id(var_args){
var G__43316 = arguments.length;
switch (G__43316) {
case 2:
return shadow.dom.by_id.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return shadow.dom.by_id.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.by_id.cljs$core$IFn$_invoke$arity$2 = (function (id,el){
return shadow.dom.dom_node(el).getElementById(id);
}));

(shadow.dom.by_id.cljs$core$IFn$_invoke$arity$1 = (function (id){
return document.getElementById(id);
}));

(shadow.dom.by_id.cljs$lang$maxFixedArity = 2);

shadow.dom.build = shadow.dom.dom_node;
shadow.dom.ev_stop = (function shadow$dom$ev_stop(var_args){
var G__43328 = arguments.length;
switch (G__43328) {
case 1:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 4:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1 = (function (e){
if(cljs.core.truth_(e.stopPropagation)){
e.stopPropagation();

e.preventDefault();
} else {
(e.cancelBubble = true);

(e.returnValue = false);
}

return e;
}));

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$2 = (function (e,el){
shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1(e);

return el;
}));

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$4 = (function (e,el,scope,owner){
shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1(e);

return el;
}));

(shadow.dom.ev_stop.cljs$lang$maxFixedArity = 4);

/**
 * check wether a parent node (or the document) contains the child
 */
shadow.dom.contains_QMARK_ = (function shadow$dom$contains_QMARK_(var_args){
var G__43334 = arguments.length;
switch (G__43334) {
case 1:
return shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$1 = (function (el){
return goog.dom.contains(document,shadow.dom.dom_node(el));
}));

(shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$2 = (function (parent,el){
return goog.dom.contains(shadow.dom.dom_node(parent),shadow.dom.dom_node(el));
}));

(shadow.dom.contains_QMARK_.cljs$lang$maxFixedArity = 2);

shadow.dom.add_class = (function shadow$dom$add_class(el,cls){
return goog.dom.classlist.add(shadow.dom.dom_node(el),cls);
});
shadow.dom.remove_class = (function shadow$dom$remove_class(el,cls){
return goog.dom.classlist.remove(shadow.dom.dom_node(el),cls);
});
shadow.dom.toggle_class = (function shadow$dom$toggle_class(var_args){
var G__43348 = arguments.length;
switch (G__43348) {
case 2:
return shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$2 = (function (el,cls){
return goog.dom.classlist.toggle(shadow.dom.dom_node(el),cls);
}));

(shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$3 = (function (el,cls,v){
if(cljs.core.truth_(v)){
return shadow.dom.add_class(el,cls);
} else {
return shadow.dom.remove_class(el,cls);
}
}));

(shadow.dom.toggle_class.cljs$lang$maxFixedArity = 3);

shadow.dom.dom_listen = (cljs.core.truth_((function (){var or__5142__auto__ = (!((typeof document !== 'undefined')));
if(or__5142__auto__){
return or__5142__auto__;
} else {
return document.addEventListener;
}
})())?(function shadow$dom$dom_listen_good(el,ev,handler){
return el.addEventListener(ev,handler,false);
}):(function shadow$dom$dom_listen_ie(el,ev,handler){
try{return el.attachEvent((""+"on"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ev)),(function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
}));
}catch (e43364){if((e43364 instanceof Object)){
var e = e43364;
return console.log("didnt support attachEvent",el,e);
} else {
throw e43364;

}
}}));
shadow.dom.dom_listen_remove = (cljs.core.truth_((function (){var or__5142__auto__ = (!((typeof document !== 'undefined')));
if(or__5142__auto__){
return or__5142__auto__;
} else {
return document.removeEventListener;
}
})())?(function shadow$dom$dom_listen_remove_good(el,ev,handler){
return el.removeEventListener(ev,handler,false);
}):(function shadow$dom$dom_listen_remove_ie(el,ev,handler){
return el.detachEvent((""+"on"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ev)),handler);
}));
shadow.dom.on_query = (function shadow$dom$on_query(root_el,ev,selector,handler){
var seq__43394 = cljs.core.seq(shadow.dom.query.cljs$core$IFn$_invoke$arity$2(selector,root_el));
var chunk__43395 = null;
var count__43396 = (0);
var i__43397 = (0);
while(true){
if((i__43397 < count__43396)){
var el = chunk__43395.cljs$core$IIndexed$_nth$arity$2(null,i__43397);
var handler_44250__$1 = ((function (seq__43394,chunk__43395,count__43396,i__43397,el){
return (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});})(seq__43394,chunk__43395,count__43396,i__43397,el))
;
shadow.dom.dom_listen(el,cljs.core.name(ev),handler_44250__$1);


var G__44252 = seq__43394;
var G__44253 = chunk__43395;
var G__44254 = count__43396;
var G__44255 = (i__43397 + (1));
seq__43394 = G__44252;
chunk__43395 = G__44253;
count__43396 = G__44254;
i__43397 = G__44255;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__43394);
if(temp__5825__auto__){
var seq__43394__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__43394__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__43394__$1);
var G__44256 = cljs.core.chunk_rest(seq__43394__$1);
var G__44257 = c__5673__auto__;
var G__44258 = cljs.core.count(c__5673__auto__);
var G__44259 = (0);
seq__43394 = G__44256;
chunk__43395 = G__44257;
count__43396 = G__44258;
i__43397 = G__44259;
continue;
} else {
var el = cljs.core.first(seq__43394__$1);
var handler_44261__$1 = ((function (seq__43394,chunk__43395,count__43396,i__43397,el,seq__43394__$1,temp__5825__auto__){
return (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});})(seq__43394,chunk__43395,count__43396,i__43397,el,seq__43394__$1,temp__5825__auto__))
;
shadow.dom.dom_listen(el,cljs.core.name(ev),handler_44261__$1);


var G__44262 = cljs.core.next(seq__43394__$1);
var G__44263 = null;
var G__44264 = (0);
var G__44265 = (0);
seq__43394 = G__44262;
chunk__43395 = G__44263;
count__43396 = G__44264;
i__43397 = G__44265;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.on = (function shadow$dom$on(var_args){
var G__43419 = arguments.length;
switch (G__43419) {
case 3:
return shadow.dom.on.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return shadow.dom.on.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.on.cljs$core$IFn$_invoke$arity$3 = (function (el,ev,handler){
return shadow.dom.on.cljs$core$IFn$_invoke$arity$4(el,ev,handler,false);
}));

(shadow.dom.on.cljs$core$IFn$_invoke$arity$4 = (function (el,ev,handler,capture){
if(cljs.core.vector_QMARK_(ev)){
return shadow.dom.on_query(el,cljs.core.first(ev),cljs.core.second(ev),handler);
} else {
var handler__$1 = (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});
return shadow.dom.dom_listen(shadow.dom.dom_node(el),cljs.core.name(ev),handler__$1);
}
}));

(shadow.dom.on.cljs$lang$maxFixedArity = 4);

shadow.dom.remove_event_handler = (function shadow$dom$remove_event_handler(el,ev,handler){
return shadow.dom.dom_listen_remove(shadow.dom.dom_node(el),cljs.core.name(ev),handler);
});
shadow.dom.add_event_listeners = (function shadow$dom$add_event_listeners(el,events){
var seq__43424 = cljs.core.seq(events);
var chunk__43425 = null;
var count__43426 = (0);
var i__43427 = (0);
while(true){
if((i__43427 < count__43426)){
var vec__43454 = chunk__43425.cljs$core$IIndexed$_nth$arity$2(null,i__43427);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43454,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43454,(1),null);
shadow.dom.on.cljs$core$IFn$_invoke$arity$3(el,k,v);


var G__44269 = seq__43424;
var G__44270 = chunk__43425;
var G__44271 = count__43426;
var G__44272 = (i__43427 + (1));
seq__43424 = G__44269;
chunk__43425 = G__44270;
count__43426 = G__44271;
i__43427 = G__44272;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__43424);
if(temp__5825__auto__){
var seq__43424__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__43424__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__43424__$1);
var G__44273 = cljs.core.chunk_rest(seq__43424__$1);
var G__44274 = c__5673__auto__;
var G__44275 = cljs.core.count(c__5673__auto__);
var G__44276 = (0);
seq__43424 = G__44273;
chunk__43425 = G__44274;
count__43426 = G__44275;
i__43427 = G__44276;
continue;
} else {
var vec__43458 = cljs.core.first(seq__43424__$1);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43458,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43458,(1),null);
shadow.dom.on.cljs$core$IFn$_invoke$arity$3(el,k,v);


var G__44277 = cljs.core.next(seq__43424__$1);
var G__44278 = null;
var G__44279 = (0);
var G__44280 = (0);
seq__43424 = G__44277;
chunk__43425 = G__44278;
count__43426 = G__44279;
i__43427 = G__44280;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.set_style = (function shadow$dom$set_style(el,styles){
var dom = shadow.dom.dom_node(el);
var seq__43466 = cljs.core.seq(styles);
var chunk__43467 = null;
var count__43468 = (0);
var i__43469 = (0);
while(true){
if((i__43469 < count__43468)){
var vec__43480 = chunk__43467.cljs$core$IIndexed$_nth$arity$2(null,i__43469);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43480,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43480,(1),null);
goog.style.setStyle(dom,cljs.core.name(k),(((v == null))?"":v));


var G__44281 = seq__43466;
var G__44282 = chunk__43467;
var G__44283 = count__43468;
var G__44284 = (i__43469 + (1));
seq__43466 = G__44281;
chunk__43467 = G__44282;
count__43468 = G__44283;
i__43469 = G__44284;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__43466);
if(temp__5825__auto__){
var seq__43466__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__43466__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__43466__$1);
var G__44286 = cljs.core.chunk_rest(seq__43466__$1);
var G__44287 = c__5673__auto__;
var G__44288 = cljs.core.count(c__5673__auto__);
var G__44289 = (0);
seq__43466 = G__44286;
chunk__43467 = G__44287;
count__43468 = G__44288;
i__43469 = G__44289;
continue;
} else {
var vec__43491 = cljs.core.first(seq__43466__$1);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43491,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43491,(1),null);
goog.style.setStyle(dom,cljs.core.name(k),(((v == null))?"":v));


var G__44291 = cljs.core.next(seq__43466__$1);
var G__44292 = null;
var G__44293 = (0);
var G__44294 = (0);
seq__43466 = G__44291;
chunk__43467 = G__44292;
count__43468 = G__44293;
i__43469 = G__44294;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.set_attr_STAR_ = (function shadow$dom$set_attr_STAR_(el,key,value){
var G__43497_44296 = key;
var G__43497_44297__$1 = (((G__43497_44296 instanceof cljs.core.Keyword))?G__43497_44296.fqn:null);
switch (G__43497_44297__$1) {
case "id":
(el.id = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)));

break;
case "class":
(el.className = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)));

break;
case "for":
(el.htmlFor = value);

break;
case "cellpadding":
el.setAttribute("cellPadding",value);

break;
case "cellspacing":
el.setAttribute("cellSpacing",value);

break;
case "colspan":
el.setAttribute("colSpan",value);

break;
case "frameborder":
el.setAttribute("frameBorder",value);

break;
case "height":
el.setAttribute("height",value);

break;
case "maxlength":
el.setAttribute("maxLength",value);

break;
case "role":
el.setAttribute("role",value);

break;
case "rowspan":
el.setAttribute("rowSpan",value);

break;
case "type":
el.setAttribute("type",value);

break;
case "usemap":
el.setAttribute("useMap",value);

break;
case "valign":
el.setAttribute("vAlign",value);

break;
case "width":
el.setAttribute("width",value);

break;
case "on":
shadow.dom.add_event_listeners(el,value);

break;
case "style":
if((value == null)){
} else {
if(typeof value === 'string'){
el.setAttribute("style",value);
} else {
if(cljs.core.map_QMARK_(value)){
shadow.dom.set_style(el,value);
} else {
goog.style.setStyle(el,value);

}
}
}

break;
default:
var ks_44303 = cljs.core.name(key);
if(cljs.core.truth_((function (){var or__5142__auto__ = goog.string.startsWith(ks_44303,"data-");
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return goog.string.startsWith(ks_44303,"aria-");
}
})())){
el.setAttribute(ks_44303,value);
} else {
(el[ks_44303] = value);
}

}

return el;
});
shadow.dom.set_attrs = (function shadow$dom$set_attrs(el,attrs){
return cljs.core.reduce_kv((function (el__$1,key,value){
shadow.dom.set_attr_STAR_(el__$1,key,value);

return el__$1;
}),shadow.dom.dom_node(el),attrs);
});
shadow.dom.set_attr = (function shadow$dom$set_attr(el,key,value){
return shadow.dom.set_attr_STAR_(shadow.dom.dom_node(el),key,value);
});
shadow.dom.has_class_QMARK_ = (function shadow$dom$has_class_QMARK_(el,cls){
return goog.dom.classlist.contains(shadow.dom.dom_node(el),cls);
});
shadow.dom.merge_class_string = (function shadow$dom$merge_class_string(current,extra_class){
if(cljs.core.seq(current)){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(current)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(extra_class));
} else {
return extra_class;
}
});
shadow.dom.parse_tag = (function shadow$dom$parse_tag(spec){
var spec__$1 = cljs.core.name(spec);
var fdot = spec__$1.indexOf(".");
var fhash = spec__$1.indexOf("#");
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fdot)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fhash)))){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1,null,null], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fhash)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fdot),null,clojure.string.replace(spec__$1.substring((fdot + (1))),/\./," ")], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fdot)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fhash),spec__$1.substring((fhash + (1))),null], null);
} else {
if((fhash > fdot)){
throw (""+"cant have id after class?"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(spec__$1));
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fhash),spec__$1.substring((fhash + (1)),fdot),clojure.string.replace(spec__$1.substring((fdot + (1))),/\./," ")], null);

}
}
}
}
});
shadow.dom.create_dom_node = (function shadow$dom$create_dom_node(tag_def,p__43520){
var map__43521 = p__43520;
var map__43521__$1 = cljs.core.__destructure_map(map__43521);
var props = map__43521__$1;
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43521__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var tag_props = ({});
var vec__43522 = shadow.dom.parse_tag(tag_def);
var tag_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43522,(0),null);
var tag_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43522,(1),null);
var tag_classes = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43522,(2),null);
if(cljs.core.truth_(tag_id)){
(tag_props["id"] = tag_id);
} else {
}

if(cljs.core.truth_(tag_classes)){
(tag_props["class"] = shadow.dom.merge_class_string(class$,tag_classes));
} else {
}

var G__43526 = goog.dom.createDom(tag_name,tag_props);
shadow.dom.set_attrs(G__43526,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(props,new cljs.core.Keyword(null,"class","class",-2030961996)));

return G__43526;
});
shadow.dom.append = (function shadow$dom$append(var_args){
var G__43528 = arguments.length;
switch (G__43528) {
case 1:
return shadow.dom.append.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.append.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.append.cljs$core$IFn$_invoke$arity$1 = (function (node){
if(cljs.core.truth_(node)){
var temp__5825__auto__ = shadow.dom.dom_node(node);
if(cljs.core.truth_(temp__5825__auto__)){
var n = temp__5825__auto__;
document.body.appendChild(n);

return n;
} else {
return null;
}
} else {
return null;
}
}));

(shadow.dom.append.cljs$core$IFn$_invoke$arity$2 = (function (el,node){
if(cljs.core.truth_(node)){
var temp__5825__auto__ = shadow.dom.dom_node(node);
if(cljs.core.truth_(temp__5825__auto__)){
var n = temp__5825__auto__;
shadow.dom.dom_node(el).appendChild(n);

return n;
} else {
return null;
}
} else {
return null;
}
}));

(shadow.dom.append.cljs$lang$maxFixedArity = 2);

shadow.dom.destructure_node = (function shadow$dom$destructure_node(create_fn,p__43530){
var vec__43531 = p__43530;
var seq__43532 = cljs.core.seq(vec__43531);
var first__43533 = cljs.core.first(seq__43532);
var seq__43532__$1 = cljs.core.next(seq__43532);
var nn = first__43533;
var first__43533__$1 = cljs.core.first(seq__43532__$1);
var seq__43532__$2 = cljs.core.next(seq__43532__$1);
var np = first__43533__$1;
var nc = seq__43532__$2;
var node = vec__43531;
if((nn instanceof cljs.core.Keyword)){
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("invalid dom node",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"node","node",581201198),node], null));
}

if((((np == null)) && ((nc == null)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__43534 = nn;
var G__43535 = cljs.core.PersistentArrayMap.EMPTY;
return (create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(G__43534,G__43535) : create_fn.call(null,G__43534,G__43535));
})(),cljs.core.List.EMPTY], null);
} else {
if(cljs.core.map_QMARK_(np)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(nn,np) : create_fn.call(null,nn,np)),nc], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__43536 = nn;
var G__43537 = cljs.core.PersistentArrayMap.EMPTY;
return (create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(G__43536,G__43537) : create_fn.call(null,G__43536,G__43537));
})(),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(nc,np)], null);

}
}
});
shadow.dom.make_dom_node = (function shadow$dom$make_dom_node(structure){
var vec__43538 = shadow.dom.destructure_node(shadow.dom.create_dom_node,structure);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43538,(0),null);
var node_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43538,(1),null);
var seq__43541_44320 = cljs.core.seq(node_children);
var chunk__43542_44321 = null;
var count__43543_44322 = (0);
var i__43544_44323 = (0);
while(true){
if((i__43544_44323 < count__43543_44322)){
var child_struct_44324 = chunk__43542_44321.cljs$core$IIndexed$_nth$arity$2(null,i__43544_44323);
var children_44325 = shadow.dom.dom_node(child_struct_44324);
if(cljs.core.seq_QMARK_(children_44325)){
var seq__43561_44327 = cljs.core.seq(cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom.dom_node,children_44325));
var chunk__43563_44328 = null;
var count__43564_44329 = (0);
var i__43565_44330 = (0);
while(true){
if((i__43565_44330 < count__43564_44329)){
var child_44331 = chunk__43563_44328.cljs$core$IIndexed$_nth$arity$2(null,i__43565_44330);
if(cljs.core.truth_(child_44331)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_44331);


var G__44332 = seq__43561_44327;
var G__44333 = chunk__43563_44328;
var G__44334 = count__43564_44329;
var G__44335 = (i__43565_44330 + (1));
seq__43561_44327 = G__44332;
chunk__43563_44328 = G__44333;
count__43564_44329 = G__44334;
i__43565_44330 = G__44335;
continue;
} else {
var G__44336 = seq__43561_44327;
var G__44337 = chunk__43563_44328;
var G__44338 = count__43564_44329;
var G__44339 = (i__43565_44330 + (1));
seq__43561_44327 = G__44336;
chunk__43563_44328 = G__44337;
count__43564_44329 = G__44338;
i__43565_44330 = G__44339;
continue;
}
} else {
var temp__5825__auto___44340 = cljs.core.seq(seq__43561_44327);
if(temp__5825__auto___44340){
var seq__43561_44341__$1 = temp__5825__auto___44340;
if(cljs.core.chunked_seq_QMARK_(seq__43561_44341__$1)){
var c__5673__auto___44342 = cljs.core.chunk_first(seq__43561_44341__$1);
var G__44343 = cljs.core.chunk_rest(seq__43561_44341__$1);
var G__44344 = c__5673__auto___44342;
var G__44345 = cljs.core.count(c__5673__auto___44342);
var G__44346 = (0);
seq__43561_44327 = G__44343;
chunk__43563_44328 = G__44344;
count__43564_44329 = G__44345;
i__43565_44330 = G__44346;
continue;
} else {
var child_44347 = cljs.core.first(seq__43561_44341__$1);
if(cljs.core.truth_(child_44347)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_44347);


var G__44348 = cljs.core.next(seq__43561_44341__$1);
var G__44349 = null;
var G__44350 = (0);
var G__44351 = (0);
seq__43561_44327 = G__44348;
chunk__43563_44328 = G__44349;
count__43564_44329 = G__44350;
i__43565_44330 = G__44351;
continue;
} else {
var G__44353 = cljs.core.next(seq__43561_44341__$1);
var G__44354 = null;
var G__44355 = (0);
var G__44356 = (0);
seq__43561_44327 = G__44353;
chunk__43563_44328 = G__44354;
count__43564_44329 = G__44355;
i__43565_44330 = G__44356;
continue;
}
}
} else {
}
}
break;
}
} else {
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,children_44325);
}


var G__44357 = seq__43541_44320;
var G__44358 = chunk__43542_44321;
var G__44359 = count__43543_44322;
var G__44360 = (i__43544_44323 + (1));
seq__43541_44320 = G__44357;
chunk__43542_44321 = G__44358;
count__43543_44322 = G__44359;
i__43544_44323 = G__44360;
continue;
} else {
var temp__5825__auto___44361 = cljs.core.seq(seq__43541_44320);
if(temp__5825__auto___44361){
var seq__43541_44362__$1 = temp__5825__auto___44361;
if(cljs.core.chunked_seq_QMARK_(seq__43541_44362__$1)){
var c__5673__auto___44364 = cljs.core.chunk_first(seq__43541_44362__$1);
var G__44365 = cljs.core.chunk_rest(seq__43541_44362__$1);
var G__44366 = c__5673__auto___44364;
var G__44367 = cljs.core.count(c__5673__auto___44364);
var G__44368 = (0);
seq__43541_44320 = G__44365;
chunk__43542_44321 = G__44366;
count__43543_44322 = G__44367;
i__43544_44323 = G__44368;
continue;
} else {
var child_struct_44369 = cljs.core.first(seq__43541_44362__$1);
var children_44370 = shadow.dom.dom_node(child_struct_44369);
if(cljs.core.seq_QMARK_(children_44370)){
var seq__43577_44371 = cljs.core.seq(cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom.dom_node,children_44370));
var chunk__43579_44372 = null;
var count__43580_44373 = (0);
var i__43581_44374 = (0);
while(true){
if((i__43581_44374 < count__43580_44373)){
var child_44375 = chunk__43579_44372.cljs$core$IIndexed$_nth$arity$2(null,i__43581_44374);
if(cljs.core.truth_(child_44375)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_44375);


var G__44376 = seq__43577_44371;
var G__44377 = chunk__43579_44372;
var G__44378 = count__43580_44373;
var G__44379 = (i__43581_44374 + (1));
seq__43577_44371 = G__44376;
chunk__43579_44372 = G__44377;
count__43580_44373 = G__44378;
i__43581_44374 = G__44379;
continue;
} else {
var G__44380 = seq__43577_44371;
var G__44381 = chunk__43579_44372;
var G__44382 = count__43580_44373;
var G__44383 = (i__43581_44374 + (1));
seq__43577_44371 = G__44380;
chunk__43579_44372 = G__44381;
count__43580_44373 = G__44382;
i__43581_44374 = G__44383;
continue;
}
} else {
var temp__5825__auto___44384__$1 = cljs.core.seq(seq__43577_44371);
if(temp__5825__auto___44384__$1){
var seq__43577_44385__$1 = temp__5825__auto___44384__$1;
if(cljs.core.chunked_seq_QMARK_(seq__43577_44385__$1)){
var c__5673__auto___44386 = cljs.core.chunk_first(seq__43577_44385__$1);
var G__44387 = cljs.core.chunk_rest(seq__43577_44385__$1);
var G__44388 = c__5673__auto___44386;
var G__44389 = cljs.core.count(c__5673__auto___44386);
var G__44390 = (0);
seq__43577_44371 = G__44387;
chunk__43579_44372 = G__44388;
count__43580_44373 = G__44389;
i__43581_44374 = G__44390;
continue;
} else {
var child_44391 = cljs.core.first(seq__43577_44385__$1);
if(cljs.core.truth_(child_44391)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_44391);


var G__44392 = cljs.core.next(seq__43577_44385__$1);
var G__44393 = null;
var G__44394 = (0);
var G__44395 = (0);
seq__43577_44371 = G__44392;
chunk__43579_44372 = G__44393;
count__43580_44373 = G__44394;
i__43581_44374 = G__44395;
continue;
} else {
var G__44396 = cljs.core.next(seq__43577_44385__$1);
var G__44397 = null;
var G__44398 = (0);
var G__44399 = (0);
seq__43577_44371 = G__44396;
chunk__43579_44372 = G__44397;
count__43580_44373 = G__44398;
i__43581_44374 = G__44399;
continue;
}
}
} else {
}
}
break;
}
} else {
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,children_44370);
}


var G__44402 = cljs.core.next(seq__43541_44362__$1);
var G__44403 = null;
var G__44404 = (0);
var G__44405 = (0);
seq__43541_44320 = G__44402;
chunk__43542_44321 = G__44403;
count__43543_44322 = G__44404;
i__43544_44323 = G__44405;
continue;
}
} else {
}
}
break;
}

return node;
});
(cljs.core.Keyword.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.Keyword.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_dom_node(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$__$1], null));
}));

(cljs.core.PersistentVector.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_dom_node(this$__$1);
}));

(cljs.core.LazySeq.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.LazySeq.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom._to_dom,this$__$1);
}));
if(cljs.core.truth_(((typeof HTMLElement) != 'undefined'))){
(HTMLElement.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(HTMLElement.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1;
}));
} else {
}
if(cljs.core.truth_(((typeof DocumentFragment) != 'undefined'))){
(DocumentFragment.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(DocumentFragment.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1;
}));
} else {
}
/**
 * clear node children
 */
shadow.dom.reset = (function shadow$dom$reset(node){
return goog.dom.removeChildren(shadow.dom.dom_node(node));
});
shadow.dom.remove = (function shadow$dom$remove(node){
if((((!((node == null))))?(((((node.cljs$lang$protocol_mask$partition0$ & (8388608))) || ((cljs.core.PROTOCOL_SENTINEL === node.cljs$core$ISeqable$))))?true:false):false)){
var seq__43605 = cljs.core.seq(node);
var chunk__43606 = null;
var count__43607 = (0);
var i__43608 = (0);
while(true){
if((i__43608 < count__43607)){
var n = chunk__43606.cljs$core$IIndexed$_nth$arity$2(null,i__43608);
(shadow.dom.remove.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.remove.cljs$core$IFn$_invoke$arity$1(n) : shadow.dom.remove.call(null,n));


var G__44406 = seq__43605;
var G__44407 = chunk__43606;
var G__44408 = count__43607;
var G__44409 = (i__43608 + (1));
seq__43605 = G__44406;
chunk__43606 = G__44407;
count__43607 = G__44408;
i__43608 = G__44409;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__43605);
if(temp__5825__auto__){
var seq__43605__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__43605__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__43605__$1);
var G__44411 = cljs.core.chunk_rest(seq__43605__$1);
var G__44412 = c__5673__auto__;
var G__44413 = cljs.core.count(c__5673__auto__);
var G__44414 = (0);
seq__43605 = G__44411;
chunk__43606 = G__44412;
count__43607 = G__44413;
i__43608 = G__44414;
continue;
} else {
var n = cljs.core.first(seq__43605__$1);
(shadow.dom.remove.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.remove.cljs$core$IFn$_invoke$arity$1(n) : shadow.dom.remove.call(null,n));


var G__44415 = cljs.core.next(seq__43605__$1);
var G__44416 = null;
var G__44417 = (0);
var G__44418 = (0);
seq__43605 = G__44415;
chunk__43606 = G__44416;
count__43607 = G__44417;
i__43608 = G__44418;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return goog.dom.removeNode(node);
}
});
shadow.dom.replace_node = (function shadow$dom$replace_node(old,new$){
return goog.dom.replaceNode(shadow.dom.dom_node(new$),shadow.dom.dom_node(old));
});
shadow.dom.text = (function shadow$dom$text(var_args){
var G__43627 = arguments.length;
switch (G__43627) {
case 2:
return shadow.dom.text.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return shadow.dom.text.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.text.cljs$core$IFn$_invoke$arity$2 = (function (el,new_text){
return (shadow.dom.dom_node(el).innerText = new_text);
}));

(shadow.dom.text.cljs$core$IFn$_invoke$arity$1 = (function (el){
return shadow.dom.dom_node(el).innerText;
}));

(shadow.dom.text.cljs$lang$maxFixedArity = 2);

shadow.dom.check = (function shadow$dom$check(var_args){
var G__43632 = arguments.length;
switch (G__43632) {
case 1:
return shadow.dom.check.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.check.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.check.cljs$core$IFn$_invoke$arity$1 = (function (el){
return shadow.dom.check.cljs$core$IFn$_invoke$arity$2(el,true);
}));

(shadow.dom.check.cljs$core$IFn$_invoke$arity$2 = (function (el,checked){
return (shadow.dom.dom_node(el).checked = checked);
}));

(shadow.dom.check.cljs$lang$maxFixedArity = 2);

shadow.dom.checked_QMARK_ = (function shadow$dom$checked_QMARK_(el){
return shadow.dom.dom_node(el).checked;
});
shadow.dom.form_elements = (function shadow$dom$form_elements(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).elements));
});
shadow.dom.children = (function shadow$dom$children(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).children));
});
shadow.dom.child_nodes = (function shadow$dom$child_nodes(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).childNodes));
});
shadow.dom.attr = (function shadow$dom$attr(var_args){
var G__43635 = arguments.length;
switch (G__43635) {
case 2:
return shadow.dom.attr.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.attr.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.attr.cljs$core$IFn$_invoke$arity$2 = (function (el,key){
return shadow.dom.dom_node(el).getAttribute(cljs.core.name(key));
}));

(shadow.dom.attr.cljs$core$IFn$_invoke$arity$3 = (function (el,key,default$){
var or__5142__auto__ = shadow.dom.dom_node(el).getAttribute(cljs.core.name(key));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return default$;
}
}));

(shadow.dom.attr.cljs$lang$maxFixedArity = 3);

shadow.dom.del_attr = (function shadow$dom$del_attr(el,key){
return shadow.dom.dom_node(el).removeAttribute(cljs.core.name(key));
});
shadow.dom.data = (function shadow$dom$data(el,key){
return shadow.dom.dom_node(el).getAttribute((""+"data-"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(key))));
});
shadow.dom.set_data = (function shadow$dom$set_data(el,key,value){
return shadow.dom.dom_node(el).setAttribute((""+"data-"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(key))),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)));
});
shadow.dom.set_html = (function shadow$dom$set_html(node,text){
return (shadow.dom.dom_node(node).innerHTML = text);
});
shadow.dom.get_html = (function shadow$dom$get_html(node){
return shadow.dom.dom_node(node).innerHTML;
});
shadow.dom.fragment = (function shadow$dom$fragment(var_args){
var args__5882__auto__ = [];
var len__5876__auto___44427 = arguments.length;
var i__5877__auto___44428 = (0);
while(true){
if((i__5877__auto___44428 < len__5876__auto___44427)){
args__5882__auto__.push((arguments[i__5877__auto___44428]));

var G__44429 = (i__5877__auto___44428 + (1));
i__5877__auto___44428 = G__44429;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return shadow.dom.fragment.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(shadow.dom.fragment.cljs$core$IFn$_invoke$arity$variadic = (function (nodes){
var fragment = document.createDocumentFragment();
var seq__43643_44431 = cljs.core.seq(nodes);
var chunk__43644_44432 = null;
var count__43645_44433 = (0);
var i__43646_44434 = (0);
while(true){
if((i__43646_44434 < count__43645_44433)){
var node_44435 = chunk__43644_44432.cljs$core$IIndexed$_nth$arity$2(null,i__43646_44434);
fragment.appendChild(shadow.dom._to_dom(node_44435));


var G__44436 = seq__43643_44431;
var G__44437 = chunk__43644_44432;
var G__44438 = count__43645_44433;
var G__44439 = (i__43646_44434 + (1));
seq__43643_44431 = G__44436;
chunk__43644_44432 = G__44437;
count__43645_44433 = G__44438;
i__43646_44434 = G__44439;
continue;
} else {
var temp__5825__auto___44440 = cljs.core.seq(seq__43643_44431);
if(temp__5825__auto___44440){
var seq__43643_44443__$1 = temp__5825__auto___44440;
if(cljs.core.chunked_seq_QMARK_(seq__43643_44443__$1)){
var c__5673__auto___44444 = cljs.core.chunk_first(seq__43643_44443__$1);
var G__44447 = cljs.core.chunk_rest(seq__43643_44443__$1);
var G__44448 = c__5673__auto___44444;
var G__44449 = cljs.core.count(c__5673__auto___44444);
var G__44450 = (0);
seq__43643_44431 = G__44447;
chunk__43644_44432 = G__44448;
count__43645_44433 = G__44449;
i__43646_44434 = G__44450;
continue;
} else {
var node_44451 = cljs.core.first(seq__43643_44443__$1);
fragment.appendChild(shadow.dom._to_dom(node_44451));


var G__44452 = cljs.core.next(seq__43643_44443__$1);
var G__44453 = null;
var G__44454 = (0);
var G__44455 = (0);
seq__43643_44431 = G__44452;
chunk__43644_44432 = G__44453;
count__43645_44433 = G__44454;
i__43646_44434 = G__44455;
continue;
}
} else {
}
}
break;
}

return (new shadow.dom.NativeColl(fragment));
}));

(shadow.dom.fragment.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(shadow.dom.fragment.cljs$lang$applyTo = (function (seq43640){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq43640));
}));

/**
 * given a html string, eval all <script> tags and return the html without the scripts
 * don't do this for everything, only content you trust.
 */
shadow.dom.eval_scripts = (function shadow$dom$eval_scripts(s){
var scripts = cljs.core.re_seq(/<script[^>]*?>(.+?)<\/script>/,s);
var seq__43659_44460 = cljs.core.seq(scripts);
var chunk__43660_44461 = null;
var count__43661_44462 = (0);
var i__43662_44463 = (0);
while(true){
if((i__43662_44463 < count__43661_44462)){
var vec__43675_44464 = chunk__43660_44461.cljs$core$IIndexed$_nth$arity$2(null,i__43662_44463);
var script_tag_44465 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43675_44464,(0),null);
var script_body_44466 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43675_44464,(1),null);
eval(script_body_44466);


var G__44467 = seq__43659_44460;
var G__44468 = chunk__43660_44461;
var G__44469 = count__43661_44462;
var G__44470 = (i__43662_44463 + (1));
seq__43659_44460 = G__44467;
chunk__43660_44461 = G__44468;
count__43661_44462 = G__44469;
i__43662_44463 = G__44470;
continue;
} else {
var temp__5825__auto___44471 = cljs.core.seq(seq__43659_44460);
if(temp__5825__auto___44471){
var seq__43659_44472__$1 = temp__5825__auto___44471;
if(cljs.core.chunked_seq_QMARK_(seq__43659_44472__$1)){
var c__5673__auto___44473 = cljs.core.chunk_first(seq__43659_44472__$1);
var G__44474 = cljs.core.chunk_rest(seq__43659_44472__$1);
var G__44475 = c__5673__auto___44473;
var G__44476 = cljs.core.count(c__5673__auto___44473);
var G__44477 = (0);
seq__43659_44460 = G__44474;
chunk__43660_44461 = G__44475;
count__43661_44462 = G__44476;
i__43662_44463 = G__44477;
continue;
} else {
var vec__43680_44478 = cljs.core.first(seq__43659_44472__$1);
var script_tag_44479 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43680_44478,(0),null);
var script_body_44480 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43680_44478,(1),null);
eval(script_body_44480);


var G__44481 = cljs.core.next(seq__43659_44472__$1);
var G__44482 = null;
var G__44483 = (0);
var G__44484 = (0);
seq__43659_44460 = G__44481;
chunk__43660_44461 = G__44482;
count__43661_44462 = G__44483;
i__43662_44463 = G__44484;
continue;
}
} else {
}
}
break;
}

return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (s__$1,p__43692){
var vec__43693 = p__43692;
var script_tag = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43693,(0),null);
var script_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43693,(1),null);
return clojure.string.replace(s__$1,script_tag,"");
}),s,scripts);
});
shadow.dom.str__GT_fragment = (function shadow$dom$str__GT_fragment(s){
var el = document.createElement("div");
(el.innerHTML = s);

return (new shadow.dom.NativeColl(goog.dom.childrenToNode_(document,el)));
});
shadow.dom.node_name = (function shadow$dom$node_name(el){
return shadow.dom.dom_node(el).nodeName;
});
shadow.dom.ancestor_by_class = (function shadow$dom$ancestor_by_class(el,cls){
return goog.dom.getAncestorByClass(shadow.dom.dom_node(el),cls);
});
shadow.dom.ancestor_by_tag = (function shadow$dom$ancestor_by_tag(var_args){
var G__43706 = arguments.length;
switch (G__43706) {
case 2:
return shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$2 = (function (el,tag){
return goog.dom.getAncestorByTagNameAndClass(shadow.dom.dom_node(el),cljs.core.name(tag));
}));

(shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$3 = (function (el,tag,cls){
return goog.dom.getAncestorByTagNameAndClass(shadow.dom.dom_node(el),cljs.core.name(tag),cljs.core.name(cls));
}));

(shadow.dom.ancestor_by_tag.cljs$lang$maxFixedArity = 3);

shadow.dom.get_value = (function shadow$dom$get_value(dom){
return goog.dom.forms.getValue(shadow.dom.dom_node(dom));
});
shadow.dom.set_value = (function shadow$dom$set_value(dom,value){
return goog.dom.forms.setValue(shadow.dom.dom_node(dom),value);
});
shadow.dom.px = (function shadow$dom$px(value){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((value | 0))+"px");
});
shadow.dom.pct = (function shadow$dom$pct(value){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)+"%");
});
shadow.dom.remove_style_STAR_ = (function shadow$dom$remove_style_STAR_(el,style){
return el.style.removeProperty(cljs.core.name(style));
});
shadow.dom.remove_style = (function shadow$dom$remove_style(el,style){
var el__$1 = shadow.dom.dom_node(el);
return shadow.dom.remove_style_STAR_(el__$1,style);
});
shadow.dom.remove_styles = (function shadow$dom$remove_styles(el,style_keys){
var el__$1 = shadow.dom.dom_node(el);
var seq__43726 = cljs.core.seq(style_keys);
var chunk__43727 = null;
var count__43728 = (0);
var i__43729 = (0);
while(true){
if((i__43729 < count__43728)){
var it = chunk__43727.cljs$core$IIndexed$_nth$arity$2(null,i__43729);
shadow.dom.remove_style_STAR_(el__$1,it);


var G__44489 = seq__43726;
var G__44490 = chunk__43727;
var G__44491 = count__43728;
var G__44492 = (i__43729 + (1));
seq__43726 = G__44489;
chunk__43727 = G__44490;
count__43728 = G__44491;
i__43729 = G__44492;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__43726);
if(temp__5825__auto__){
var seq__43726__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__43726__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__43726__$1);
var G__44494 = cljs.core.chunk_rest(seq__43726__$1);
var G__44495 = c__5673__auto__;
var G__44496 = cljs.core.count(c__5673__auto__);
var G__44497 = (0);
seq__43726 = G__44494;
chunk__43727 = G__44495;
count__43728 = G__44496;
i__43729 = G__44497;
continue;
} else {
var it = cljs.core.first(seq__43726__$1);
shadow.dom.remove_style_STAR_(el__$1,it);


var G__44498 = cljs.core.next(seq__43726__$1);
var G__44499 = null;
var G__44500 = (0);
var G__44501 = (0);
seq__43726 = G__44498;
chunk__43727 = G__44499;
count__43728 = G__44500;
i__43729 = G__44501;
continue;
}
} else {
return null;
}
}
break;
}
});

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
shadow.dom.Coordinate = (function (x,y,__meta,__extmap,__hash){
this.x = x;
this.y = y;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(shadow.dom.Coordinate.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__5448__auto__,k__5449__auto__){
var self__ = this;
var this__5448__auto____$1 = this;
return this__5448__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__5449__auto__,null);
}));

(shadow.dom.Coordinate.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__5450__auto__,k43748,else__5451__auto__){
var self__ = this;
var this__5450__auto____$1 = this;
var G__43761 = k43748;
var G__43761__$1 = (((G__43761 instanceof cljs.core.Keyword))?G__43761.fqn:null);
switch (G__43761__$1) {
case "x":
return self__.x;

break;
case "y":
return self__.y;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k43748,else__5451__auto__);

}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__5468__auto__,f__5469__auto__,init__5470__auto__){
var self__ = this;
var this__5468__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__5471__auto__,p__43767){
var vec__43768 = p__43767;
var k__5472__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43768,(0),null);
var v__5473__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43768,(1),null);
return (f__5469__auto__.cljs$core$IFn$_invoke$arity$3 ? f__5469__auto__.cljs$core$IFn$_invoke$arity$3(ret__5471__auto__,k__5472__auto__,v__5473__auto__) : f__5469__auto__.call(null,ret__5471__auto__,k__5472__auto__,v__5473__auto__));
}),init__5470__auto__,this__5468__auto____$1);
}));

(shadow.dom.Coordinate.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__5463__auto__,writer__5464__auto__,opts__5465__auto__){
var self__ = this;
var this__5463__auto____$1 = this;
var pr_pair__5466__auto__ = (function (keyval__5467__auto__){
return cljs.core.pr_sequential_writer(writer__5464__auto__,cljs.core.pr_writer,""," ","",opts__5465__auto__,keyval__5467__auto__);
});
return cljs.core.pr_sequential_writer(writer__5464__auto__,pr_pair__5466__auto__,"#shadow.dom.Coordinate{",", ","}",opts__5465__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"x","x",2099068185),self__.x],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"y","y",-1757859776),self__.y],null))], null),self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__43747){
var self__ = this;
var G__43747__$1 = this;
return (new cljs.core.RecordIter((0),G__43747__$1,2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"x","x",2099068185),new cljs.core.Keyword(null,"y","y",-1757859776)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__5446__auto__){
var self__ = this;
var this__5446__auto____$1 = this;
return self__.__meta;
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__5443__auto__){
var self__ = this;
var this__5443__auto____$1 = this;
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,self__.__extmap,self__.__hash));
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__5452__auto__){
var self__ = this;
var this__5452__auto____$1 = this;
return (2 + cljs.core.count(self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__5444__auto__){
var self__ = this;
var this__5444__auto____$1 = this;
var h__5251__auto__ = self__.__hash;
if((!((h__5251__auto__ == null)))){
return h__5251__auto__;
} else {
var h__5251__auto____$1 = (function (coll__5445__auto__){
return (145542109 ^ cljs.core.hash_unordered_coll(coll__5445__auto__));
})(this__5444__auto____$1);
(self__.__hash = h__5251__auto____$1);

return h__5251__auto____$1;
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this43749,other43750){
var self__ = this;
var this43749__$1 = this;
return (((!((other43750 == null)))) && ((((this43749__$1.constructor === other43750.constructor)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this43749__$1.x,other43750.x)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this43749__$1.y,other43750.y)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this43749__$1.__extmap,other43750.__extmap)))))))));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__5458__auto__,k__5459__auto__){
var self__ = this;
var this__5458__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"y","y",-1757859776),null,new cljs.core.Keyword(null,"x","x",2099068185),null], null), null),k__5459__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__5458__auto____$1),self__.__meta),k__5459__auto__);
} else {
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__5459__auto__)),null));
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IAssociative$_contains_key_QMARK_$arity$2 = (function (this__5455__auto__,k43748){
var self__ = this;
var this__5455__auto____$1 = this;
var G__43817 = k43748;
var G__43817__$1 = (((G__43817 instanceof cljs.core.Keyword))?G__43817.fqn:null);
switch (G__43817__$1) {
case "x":
case "y":
return true;

break;
default:
return cljs.core.contains_QMARK_(self__.__extmap,k43748);

}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__5456__auto__,k__5457__auto__,G__43747){
var self__ = this;
var this__5456__auto____$1 = this;
var pred__43827 = cljs.core.keyword_identical_QMARK_;
var expr__43828 = k__5457__auto__;
if(cljs.core.truth_((pred__43827.cljs$core$IFn$_invoke$arity$2 ? pred__43827.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"x","x",2099068185),expr__43828) : pred__43827.call(null,new cljs.core.Keyword(null,"x","x",2099068185),expr__43828)))){
return (new shadow.dom.Coordinate(G__43747,self__.y,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__43827.cljs$core$IFn$_invoke$arity$2 ? pred__43827.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"y","y",-1757859776),expr__43828) : pred__43827.call(null,new cljs.core.Keyword(null,"y","y",-1757859776),expr__43828)))){
return (new shadow.dom.Coordinate(self__.x,G__43747,self__.__meta,self__.__extmap,null));
} else {
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__5457__auto__,G__43747),null));
}
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__5461__auto__){
var self__ = this;
var this__5461__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"x","x",2099068185),self__.x,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"y","y",-1757859776),self__.y,null))], null),self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__5447__auto__,G__43747){
var self__ = this;
var this__5447__auto____$1 = this;
return (new shadow.dom.Coordinate(self__.x,self__.y,G__43747,self__.__extmap,self__.__hash));
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__5453__auto__,entry__5454__auto__){
var self__ = this;
var this__5453__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__5454__auto__)){
return this__5453__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__5454__auto__,(0)),cljs.core._nth(entry__5454__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__5453__auto____$1,entry__5454__auto__);
}
}));

(shadow.dom.Coordinate.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"x","x",-555367584,null),new cljs.core.Symbol(null,"y","y",-117328249,null)], null);
}));

(shadow.dom.Coordinate.cljs$lang$type = true);

(shadow.dom.Coordinate.cljs$lang$ctorPrSeq = (function (this__5494__auto__){
return (new cljs.core.List(null,"shadow.dom/Coordinate",null,(1),null));
}));

(shadow.dom.Coordinate.cljs$lang$ctorPrWriter = (function (this__5494__auto__,writer__5495__auto__){
return cljs.core._write(writer__5495__auto__,"shadow.dom/Coordinate");
}));

/**
 * Positional factory function for shadow.dom/Coordinate.
 */
shadow.dom.__GT_Coordinate = (function shadow$dom$__GT_Coordinate(x,y){
return (new shadow.dom.Coordinate(x,y,null,null,null));
});

/**
 * Factory function for shadow.dom/Coordinate, taking a map of keywords to field values.
 */
shadow.dom.map__GT_Coordinate = (function shadow$dom$map__GT_Coordinate(G__43752){
var extmap__5490__auto__ = (function (){var G__43861 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__43752,new cljs.core.Keyword(null,"x","x",2099068185),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"y","y",-1757859776)], 0));
if(cljs.core.record_QMARK_(G__43752)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__43861);
} else {
return G__43861;
}
})();
return (new shadow.dom.Coordinate(new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(G__43752),new cljs.core.Keyword(null,"y","y",-1757859776).cljs$core$IFn$_invoke$arity$1(G__43752),null,cljs.core.not_empty(extmap__5490__auto__),null));
});

shadow.dom.get_position = (function shadow$dom$get_position(el){
var pos = goog.style.getPosition(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});
shadow.dom.get_client_position = (function shadow$dom$get_client_position(el){
var pos = goog.style.getClientPosition(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});
shadow.dom.get_page_offset = (function shadow$dom$get_page_offset(el){
var pos = goog.style.getPageOffset(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
shadow.dom.Size = (function (w,h,__meta,__extmap,__hash){
this.w = w;
this.h = h;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(shadow.dom.Size.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__5448__auto__,k__5449__auto__){
var self__ = this;
var this__5448__auto____$1 = this;
return this__5448__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__5449__auto__,null);
}));

(shadow.dom.Size.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__5450__auto__,k43900,else__5451__auto__){
var self__ = this;
var this__5450__auto____$1 = this;
var G__43934 = k43900;
var G__43934__$1 = (((G__43934 instanceof cljs.core.Keyword))?G__43934.fqn:null);
switch (G__43934__$1) {
case "w":
return self__.w;

break;
case "h":
return self__.h;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k43900,else__5451__auto__);

}
}));

(shadow.dom.Size.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__5468__auto__,f__5469__auto__,init__5470__auto__){
var self__ = this;
var this__5468__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__5471__auto__,p__43939){
var vec__43940 = p__43939;
var k__5472__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43940,(0),null);
var v__5473__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43940,(1),null);
return (f__5469__auto__.cljs$core$IFn$_invoke$arity$3 ? f__5469__auto__.cljs$core$IFn$_invoke$arity$3(ret__5471__auto__,k__5472__auto__,v__5473__auto__) : f__5469__auto__.call(null,ret__5471__auto__,k__5472__auto__,v__5473__auto__));
}),init__5470__auto__,this__5468__auto____$1);
}));

(shadow.dom.Size.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__5463__auto__,writer__5464__auto__,opts__5465__auto__){
var self__ = this;
var this__5463__auto____$1 = this;
var pr_pair__5466__auto__ = (function (keyval__5467__auto__){
return cljs.core.pr_sequential_writer(writer__5464__auto__,cljs.core.pr_writer,""," ","",opts__5465__auto__,keyval__5467__auto__);
});
return cljs.core.pr_sequential_writer(writer__5464__auto__,pr_pair__5466__auto__,"#shadow.dom.Size{",", ","}",opts__5465__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"w","w",354169001),self__.w],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"h","h",1109658740),self__.h],null))], null),self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__43899){
var self__ = this;
var G__43899__$1 = this;
return (new cljs.core.RecordIter((0),G__43899__$1,2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"w","w",354169001),new cljs.core.Keyword(null,"h","h",1109658740)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(shadow.dom.Size.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__5446__auto__){
var self__ = this;
var this__5446__auto____$1 = this;
return self__.__meta;
}));

(shadow.dom.Size.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__5443__auto__){
var self__ = this;
var this__5443__auto____$1 = this;
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,self__.__extmap,self__.__hash));
}));

(shadow.dom.Size.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__5452__auto__){
var self__ = this;
var this__5452__auto____$1 = this;
return (2 + cljs.core.count(self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__5444__auto__){
var self__ = this;
var this__5444__auto____$1 = this;
var h__5251__auto__ = self__.__hash;
if((!((h__5251__auto__ == null)))){
return h__5251__auto__;
} else {
var h__5251__auto____$1 = (function (coll__5445__auto__){
return (-1228019642 ^ cljs.core.hash_unordered_coll(coll__5445__auto__));
})(this__5444__auto____$1);
(self__.__hash = h__5251__auto____$1);

return h__5251__auto____$1;
}
}));

(shadow.dom.Size.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this43901,other43902){
var self__ = this;
var this43901__$1 = this;
return (((!((other43902 == null)))) && ((((this43901__$1.constructor === other43902.constructor)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this43901__$1.w,other43902.w)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this43901__$1.h,other43902.h)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this43901__$1.__extmap,other43902.__extmap)))))))));
}));

(shadow.dom.Size.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__5458__auto__,k__5459__auto__){
var self__ = this;
var this__5458__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"w","w",354169001),null,new cljs.core.Keyword(null,"h","h",1109658740),null], null), null),k__5459__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__5458__auto____$1),self__.__meta),k__5459__auto__);
} else {
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__5459__auto__)),null));
}
}));

(shadow.dom.Size.prototype.cljs$core$IAssociative$_contains_key_QMARK_$arity$2 = (function (this__5455__auto__,k43900){
var self__ = this;
var this__5455__auto____$1 = this;
var G__43982 = k43900;
var G__43982__$1 = (((G__43982 instanceof cljs.core.Keyword))?G__43982.fqn:null);
switch (G__43982__$1) {
case "w":
case "h":
return true;

break;
default:
return cljs.core.contains_QMARK_(self__.__extmap,k43900);

}
}));

(shadow.dom.Size.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__5456__auto__,k__5457__auto__,G__43899){
var self__ = this;
var this__5456__auto____$1 = this;
var pred__43992 = cljs.core.keyword_identical_QMARK_;
var expr__43993 = k__5457__auto__;
if(cljs.core.truth_((pred__43992.cljs$core$IFn$_invoke$arity$2 ? pred__43992.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"w","w",354169001),expr__43993) : pred__43992.call(null,new cljs.core.Keyword(null,"w","w",354169001),expr__43993)))){
return (new shadow.dom.Size(G__43899,self__.h,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__43992.cljs$core$IFn$_invoke$arity$2 ? pred__43992.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"h","h",1109658740),expr__43993) : pred__43992.call(null,new cljs.core.Keyword(null,"h","h",1109658740),expr__43993)))){
return (new shadow.dom.Size(self__.w,G__43899,self__.__meta,self__.__extmap,null));
} else {
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__5457__auto__,G__43899),null));
}
}
}));

(shadow.dom.Size.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__5461__auto__){
var self__ = this;
var this__5461__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"w","w",354169001),self__.w,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"h","h",1109658740),self__.h,null))], null),self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__5447__auto__,G__43899){
var self__ = this;
var this__5447__auto____$1 = this;
return (new shadow.dom.Size(self__.w,self__.h,G__43899,self__.__extmap,self__.__hash));
}));

(shadow.dom.Size.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__5453__auto__,entry__5454__auto__){
var self__ = this;
var this__5453__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__5454__auto__)){
return this__5453__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__5454__auto__,(0)),cljs.core._nth(entry__5454__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__5453__auto____$1,entry__5454__auto__);
}
}));

(shadow.dom.Size.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"w","w",1994700528,null),new cljs.core.Symbol(null,"h","h",-1544777029,null)], null);
}));

(shadow.dom.Size.cljs$lang$type = true);

(shadow.dom.Size.cljs$lang$ctorPrSeq = (function (this__5494__auto__){
return (new cljs.core.List(null,"shadow.dom/Size",null,(1),null));
}));

(shadow.dom.Size.cljs$lang$ctorPrWriter = (function (this__5494__auto__,writer__5495__auto__){
return cljs.core._write(writer__5495__auto__,"shadow.dom/Size");
}));

/**
 * Positional factory function for shadow.dom/Size.
 */
shadow.dom.__GT_Size = (function shadow$dom$__GT_Size(w,h){
return (new shadow.dom.Size(w,h,null,null,null));
});

/**
 * Factory function for shadow.dom/Size, taking a map of keywords to field values.
 */
shadow.dom.map__GT_Size = (function shadow$dom$map__GT_Size(G__43914){
var extmap__5490__auto__ = (function (){var G__44016 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__43914,new cljs.core.Keyword(null,"w","w",354169001),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"h","h",1109658740)], 0));
if(cljs.core.record_QMARK_(G__43914)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__44016);
} else {
return G__44016;
}
})();
return (new shadow.dom.Size(new cljs.core.Keyword(null,"w","w",354169001).cljs$core$IFn$_invoke$arity$1(G__43914),new cljs.core.Keyword(null,"h","h",1109658740).cljs$core$IFn$_invoke$arity$1(G__43914),null,cljs.core.not_empty(extmap__5490__auto__),null));
});

shadow.dom.size__GT_clj = (function shadow$dom$size__GT_clj(size){
return (new shadow.dom.Size(size.width,size.height,null,null,null));
});
shadow.dom.get_size = (function shadow$dom$get_size(el){
return shadow.dom.size__GT_clj(goog.style.getSize(shadow.dom.dom_node(el)));
});
shadow.dom.get_height = (function shadow$dom$get_height(el){
return shadow.dom.get_size(el).h;
});
shadow.dom.get_viewport_size = (function shadow$dom$get_viewport_size(){
return shadow.dom.size__GT_clj(goog.dom.getViewportSize());
});
shadow.dom.first_child = (function shadow$dom$first_child(el){
return (shadow.dom.dom_node(el).children[(0)]);
});
shadow.dom.select_option_values = (function shadow$dom$select_option_values(el){
var native$ = shadow.dom.dom_node(el);
var opts = (native$["options"]);
var a__5738__auto__ = opts;
var l__5739__auto__ = a__5738__auto__.length;
var i = (0);
var ret = cljs.core.PersistentVector.EMPTY;
while(true){
if((i < l__5739__auto__)){
var G__44531 = (i + (1));
var G__44532 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ret,(opts[i]["value"]));
i = G__44531;
ret = G__44532;
continue;
} else {
return ret;
}
break;
}
});
shadow.dom.build_url = (function shadow$dom$build_url(path,query_params){
if(cljs.core.empty_QMARK_(query_params)){
return path;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path)+"?"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__44040){
var vec__44041 = p__44040;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44041,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44041,(1),null);
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(k))+"="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(encodeURIComponent((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)))));
}),query_params))));
}
});
shadow.dom.redirect = (function shadow$dom$redirect(var_args){
var G__44047 = arguments.length;
switch (G__44047) {
case 1:
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.redirect.cljs$core$IFn$_invoke$arity$1 = (function (path){
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2(path,cljs.core.PersistentArrayMap.EMPTY);
}));

(shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2 = (function (path,query_params){
return (document["location"]["href"] = shadow.dom.build_url(path,query_params));
}));

(shadow.dom.redirect.cljs$lang$maxFixedArity = 2);

shadow.dom.reload_BANG_ = (function shadow$dom$reload_BANG_(){
return (document.location.href = document.location.href);
});
shadow.dom.tag_name = (function shadow$dom$tag_name(el){
var dom = shadow.dom.dom_node(el);
return dom.tagName;
});
shadow.dom.insert_after = (function shadow$dom$insert_after(ref,new$){
var new_node = shadow.dom.dom_node(new$);
goog.dom.insertSiblingAfter(new_node,shadow.dom.dom_node(ref));

return new_node;
});
shadow.dom.insert_before = (function shadow$dom$insert_before(ref,new$){
var new_node = shadow.dom.dom_node(new$);
goog.dom.insertSiblingBefore(new_node,shadow.dom.dom_node(ref));

return new_node;
});
shadow.dom.insert_first = (function shadow$dom$insert_first(ref,new$){
var temp__5823__auto__ = shadow.dom.dom_node(ref).firstChild;
if(cljs.core.truth_(temp__5823__auto__)){
var child = temp__5823__auto__;
return shadow.dom.insert_before(child,new$);
} else {
return shadow.dom.append.cljs$core$IFn$_invoke$arity$2(ref,new$);
}
});
shadow.dom.index_of = (function shadow$dom$index_of(el){
var el__$1 = shadow.dom.dom_node(el);
var i = (0);
while(true){
var ps = el__$1.previousSibling;
if((ps == null)){
return i;
} else {
var G__44540 = ps;
var G__44541 = (i + (1));
el__$1 = G__44540;
i = G__44541;
continue;
}
break;
}
});
shadow.dom.get_parent = (function shadow$dom$get_parent(el){
return goog.dom.getParentElement(shadow.dom.dom_node(el));
});
shadow.dom.parents = (function shadow$dom$parents(el){
var parent = shadow.dom.get_parent(el);
if(cljs.core.truth_(parent)){
return cljs.core.cons(parent,(new cljs.core.LazySeq(null,(function (){
return (shadow.dom.parents.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.parents.cljs$core$IFn$_invoke$arity$1(parent) : shadow.dom.parents.call(null,parent));
}),null,null)));
} else {
return null;
}
});
shadow.dom.matches = (function shadow$dom$matches(el,sel){
return shadow.dom.dom_node(el).matches(sel);
});
shadow.dom.get_next_sibling = (function shadow$dom$get_next_sibling(el){
return goog.dom.getNextElementSibling(shadow.dom.dom_node(el));
});
shadow.dom.get_previous_sibling = (function shadow$dom$get_previous_sibling(el){
return goog.dom.getPreviousElementSibling(shadow.dom.dom_node(el));
});
shadow.dom.xmlns = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, ["svg","http://www.w3.org/2000/svg","xlink","http://www.w3.org/1999/xlink"], null));
shadow.dom.create_svg_node = (function shadow$dom$create_svg_node(tag_def,props){
var vec__44073 = shadow.dom.parse_tag(tag_def);
var tag_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44073,(0),null);
var tag_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44073,(1),null);
var tag_classes = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44073,(2),null);
var el = document.createElementNS("http://www.w3.org/2000/svg",tag_name);
if(cljs.core.truth_(tag_id)){
el.setAttribute("id",tag_id);
} else {
}

if(cljs.core.truth_(tag_classes)){
el.setAttribute("class",shadow.dom.merge_class_string(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(props),tag_classes));
} else {
}

var seq__44077_44555 = cljs.core.seq(props);
var chunk__44078_44557 = null;
var count__44079_44558 = (0);
var i__44080_44559 = (0);
while(true){
if((i__44080_44559 < count__44079_44558)){
var vec__44095_44560 = chunk__44078_44557.cljs$core$IIndexed$_nth$arity$2(null,i__44080_44559);
var k_44561 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44095_44560,(0),null);
var v_44562 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44095_44560,(1),null);
el.setAttributeNS((function (){var temp__5825__auto__ = cljs.core.namespace(k_44561);
if(cljs.core.truth_(temp__5825__auto__)){
var ns = temp__5825__auto__;
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(shadow.dom.xmlns),ns);
} else {
return null;
}
})(),cljs.core.name(k_44561),v_44562);


var G__44563 = seq__44077_44555;
var G__44564 = chunk__44078_44557;
var G__44565 = count__44079_44558;
var G__44566 = (i__44080_44559 + (1));
seq__44077_44555 = G__44563;
chunk__44078_44557 = G__44564;
count__44079_44558 = G__44565;
i__44080_44559 = G__44566;
continue;
} else {
var temp__5825__auto___44567 = cljs.core.seq(seq__44077_44555);
if(temp__5825__auto___44567){
var seq__44077_44568__$1 = temp__5825__auto___44567;
if(cljs.core.chunked_seq_QMARK_(seq__44077_44568__$1)){
var c__5673__auto___44569 = cljs.core.chunk_first(seq__44077_44568__$1);
var G__44570 = cljs.core.chunk_rest(seq__44077_44568__$1);
var G__44571 = c__5673__auto___44569;
var G__44572 = cljs.core.count(c__5673__auto___44569);
var G__44573 = (0);
seq__44077_44555 = G__44570;
chunk__44078_44557 = G__44571;
count__44079_44558 = G__44572;
i__44080_44559 = G__44573;
continue;
} else {
var vec__44100_44575 = cljs.core.first(seq__44077_44568__$1);
var k_44576 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44100_44575,(0),null);
var v_44577 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44100_44575,(1),null);
el.setAttributeNS((function (){var temp__5825__auto____$1 = cljs.core.namespace(k_44576);
if(cljs.core.truth_(temp__5825__auto____$1)){
var ns = temp__5825__auto____$1;
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(shadow.dom.xmlns),ns);
} else {
return null;
}
})(),cljs.core.name(k_44576),v_44577);


var G__44579 = cljs.core.next(seq__44077_44568__$1);
var G__44580 = null;
var G__44581 = (0);
var G__44582 = (0);
seq__44077_44555 = G__44579;
chunk__44078_44557 = G__44580;
count__44079_44558 = G__44581;
i__44080_44559 = G__44582;
continue;
}
} else {
}
}
break;
}

return el;
});
shadow.dom.svg_node = (function shadow$dom$svg_node(el){
if((el == null)){
return null;
} else {
if((((!((el == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === el.shadow$dom$SVGElement$))))?true:false):false)){
return el.shadow$dom$SVGElement$_to_svg$arity$1(null);
} else {
return el;

}
}
});
shadow.dom.make_svg_node = (function shadow$dom$make_svg_node(structure){
var vec__44110 = shadow.dom.destructure_node(shadow.dom.create_svg_node,structure);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44110,(0),null);
var node_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44110,(1),null);
var seq__44113_44586 = cljs.core.seq(node_children);
var chunk__44116_44587 = null;
var count__44118_44588 = (0);
var i__44119_44589 = (0);
while(true){
if((i__44119_44589 < count__44118_44588)){
var child_struct_44590 = chunk__44116_44587.cljs$core$IIndexed$_nth$arity$2(null,i__44119_44589);
if((!((child_struct_44590 == null)))){
if(typeof child_struct_44590 === 'string'){
var text_44591 = (node["textContent"]);
(node["textContent"] = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(text_44591)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(child_struct_44590)));
} else {
var children_44592 = shadow.dom.svg_node(child_struct_44590);
if(cljs.core.seq_QMARK_(children_44592)){
var seq__44164_44593 = cljs.core.seq(children_44592);
var chunk__44166_44594 = null;
var count__44167_44595 = (0);
var i__44168_44596 = (0);
while(true){
if((i__44168_44596 < count__44167_44595)){
var child_44598 = chunk__44166_44594.cljs$core$IIndexed$_nth$arity$2(null,i__44168_44596);
if(cljs.core.truth_(child_44598)){
node.appendChild(child_44598);


var G__44599 = seq__44164_44593;
var G__44600 = chunk__44166_44594;
var G__44601 = count__44167_44595;
var G__44602 = (i__44168_44596 + (1));
seq__44164_44593 = G__44599;
chunk__44166_44594 = G__44600;
count__44167_44595 = G__44601;
i__44168_44596 = G__44602;
continue;
} else {
var G__44604 = seq__44164_44593;
var G__44605 = chunk__44166_44594;
var G__44606 = count__44167_44595;
var G__44607 = (i__44168_44596 + (1));
seq__44164_44593 = G__44604;
chunk__44166_44594 = G__44605;
count__44167_44595 = G__44606;
i__44168_44596 = G__44607;
continue;
}
} else {
var temp__5825__auto___44608 = cljs.core.seq(seq__44164_44593);
if(temp__5825__auto___44608){
var seq__44164_44609__$1 = temp__5825__auto___44608;
if(cljs.core.chunked_seq_QMARK_(seq__44164_44609__$1)){
var c__5673__auto___44610 = cljs.core.chunk_first(seq__44164_44609__$1);
var G__44611 = cljs.core.chunk_rest(seq__44164_44609__$1);
var G__44612 = c__5673__auto___44610;
var G__44613 = cljs.core.count(c__5673__auto___44610);
var G__44614 = (0);
seq__44164_44593 = G__44611;
chunk__44166_44594 = G__44612;
count__44167_44595 = G__44613;
i__44168_44596 = G__44614;
continue;
} else {
var child_44615 = cljs.core.first(seq__44164_44609__$1);
if(cljs.core.truth_(child_44615)){
node.appendChild(child_44615);


var G__44616 = cljs.core.next(seq__44164_44609__$1);
var G__44617 = null;
var G__44618 = (0);
var G__44619 = (0);
seq__44164_44593 = G__44616;
chunk__44166_44594 = G__44617;
count__44167_44595 = G__44618;
i__44168_44596 = G__44619;
continue;
} else {
var G__44620 = cljs.core.next(seq__44164_44609__$1);
var G__44621 = null;
var G__44622 = (0);
var G__44623 = (0);
seq__44164_44593 = G__44620;
chunk__44166_44594 = G__44621;
count__44167_44595 = G__44622;
i__44168_44596 = G__44623;
continue;
}
}
} else {
}
}
break;
}
} else {
node.appendChild(children_44592);
}
}


var G__44624 = seq__44113_44586;
var G__44625 = chunk__44116_44587;
var G__44626 = count__44118_44588;
var G__44627 = (i__44119_44589 + (1));
seq__44113_44586 = G__44624;
chunk__44116_44587 = G__44625;
count__44118_44588 = G__44626;
i__44119_44589 = G__44627;
continue;
} else {
var G__44628 = seq__44113_44586;
var G__44629 = chunk__44116_44587;
var G__44630 = count__44118_44588;
var G__44631 = (i__44119_44589 + (1));
seq__44113_44586 = G__44628;
chunk__44116_44587 = G__44629;
count__44118_44588 = G__44630;
i__44119_44589 = G__44631;
continue;
}
} else {
var temp__5825__auto___44634 = cljs.core.seq(seq__44113_44586);
if(temp__5825__auto___44634){
var seq__44113_44635__$1 = temp__5825__auto___44634;
if(cljs.core.chunked_seq_QMARK_(seq__44113_44635__$1)){
var c__5673__auto___44636 = cljs.core.chunk_first(seq__44113_44635__$1);
var G__44637 = cljs.core.chunk_rest(seq__44113_44635__$1);
var G__44638 = c__5673__auto___44636;
var G__44639 = cljs.core.count(c__5673__auto___44636);
var G__44640 = (0);
seq__44113_44586 = G__44637;
chunk__44116_44587 = G__44638;
count__44118_44588 = G__44639;
i__44119_44589 = G__44640;
continue;
} else {
var child_struct_44641 = cljs.core.first(seq__44113_44635__$1);
if((!((child_struct_44641 == null)))){
if(typeof child_struct_44641 === 'string'){
var text_44642 = (node["textContent"]);
(node["textContent"] = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(text_44642)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(child_struct_44641)));
} else {
var children_44643 = shadow.dom.svg_node(child_struct_44641);
if(cljs.core.seq_QMARK_(children_44643)){
var seq__44177_44644 = cljs.core.seq(children_44643);
var chunk__44179_44645 = null;
var count__44180_44646 = (0);
var i__44181_44647 = (0);
while(true){
if((i__44181_44647 < count__44180_44646)){
var child_44648 = chunk__44179_44645.cljs$core$IIndexed$_nth$arity$2(null,i__44181_44647);
if(cljs.core.truth_(child_44648)){
node.appendChild(child_44648);


var G__44649 = seq__44177_44644;
var G__44650 = chunk__44179_44645;
var G__44651 = count__44180_44646;
var G__44652 = (i__44181_44647 + (1));
seq__44177_44644 = G__44649;
chunk__44179_44645 = G__44650;
count__44180_44646 = G__44651;
i__44181_44647 = G__44652;
continue;
} else {
var G__44653 = seq__44177_44644;
var G__44654 = chunk__44179_44645;
var G__44655 = count__44180_44646;
var G__44656 = (i__44181_44647 + (1));
seq__44177_44644 = G__44653;
chunk__44179_44645 = G__44654;
count__44180_44646 = G__44655;
i__44181_44647 = G__44656;
continue;
}
} else {
var temp__5825__auto___44657__$1 = cljs.core.seq(seq__44177_44644);
if(temp__5825__auto___44657__$1){
var seq__44177_44658__$1 = temp__5825__auto___44657__$1;
if(cljs.core.chunked_seq_QMARK_(seq__44177_44658__$1)){
var c__5673__auto___44659 = cljs.core.chunk_first(seq__44177_44658__$1);
var G__44660 = cljs.core.chunk_rest(seq__44177_44658__$1);
var G__44661 = c__5673__auto___44659;
var G__44662 = cljs.core.count(c__5673__auto___44659);
var G__44663 = (0);
seq__44177_44644 = G__44660;
chunk__44179_44645 = G__44661;
count__44180_44646 = G__44662;
i__44181_44647 = G__44663;
continue;
} else {
var child_44664 = cljs.core.first(seq__44177_44658__$1);
if(cljs.core.truth_(child_44664)){
node.appendChild(child_44664);


var G__44665 = cljs.core.next(seq__44177_44658__$1);
var G__44666 = null;
var G__44667 = (0);
var G__44668 = (0);
seq__44177_44644 = G__44665;
chunk__44179_44645 = G__44666;
count__44180_44646 = G__44667;
i__44181_44647 = G__44668;
continue;
} else {
var G__44670 = cljs.core.next(seq__44177_44658__$1);
var G__44671 = null;
var G__44672 = (0);
var G__44673 = (0);
seq__44177_44644 = G__44670;
chunk__44179_44645 = G__44671;
count__44180_44646 = G__44672;
i__44181_44647 = G__44673;
continue;
}
}
} else {
}
}
break;
}
} else {
node.appendChild(children_44643);
}
}


var G__44674 = cljs.core.next(seq__44113_44635__$1);
var G__44675 = null;
var G__44676 = (0);
var G__44677 = (0);
seq__44113_44586 = G__44674;
chunk__44116_44587 = G__44675;
count__44118_44588 = G__44676;
i__44119_44589 = G__44677;
continue;
} else {
var G__44678 = cljs.core.next(seq__44113_44635__$1);
var G__44679 = null;
var G__44680 = (0);
var G__44681 = (0);
seq__44113_44586 = G__44678;
chunk__44116_44587 = G__44679;
count__44118_44588 = G__44680;
i__44119_44589 = G__44681;
continue;
}
}
} else {
}
}
break;
}

return node;
});
(shadow.dom.SVGElement["string"] = true);

(shadow.dom._to_svg["string"] = (function (this$){
if((this$ instanceof cljs.core.Keyword)){
return shadow.dom.make_svg_node(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$], null));
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("strings cannot be in svgs",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"this","this",-611633625),this$], null));
}
}));

(cljs.core.PersistentVector.prototype.shadow$dom$SVGElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.shadow$dom$SVGElement$_to_svg$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_svg_node(this$__$1);
}));

(cljs.core.LazySeq.prototype.shadow$dom$SVGElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.LazySeq.prototype.shadow$dom$SVGElement$_to_svg$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom._to_svg,this$__$1);
}));

(shadow.dom.SVGElement["null"] = true);

(shadow.dom._to_svg["null"] = (function (_){
return null;
}));
shadow.dom.svg = (function shadow$dom$svg(var_args){
var args__5882__auto__ = [];
var len__5876__auto___44686 = arguments.length;
var i__5877__auto___44687 = (0);
while(true){
if((i__5877__auto___44687 < len__5876__auto___44686)){
args__5882__auto__.push((arguments[i__5877__auto___44687]));

var G__44688 = (i__5877__auto___44687 + (1));
i__5877__auto___44687 = G__44688;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return shadow.dom.svg.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(shadow.dom.svg.cljs$core$IFn$_invoke$arity$variadic = (function (attrs,children){
return shadow.dom._to_svg(cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),attrs], null),children)));
}));

(shadow.dom.svg.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(shadow.dom.svg.cljs$lang$applyTo = (function (seq44208){
var G__44209 = cljs.core.first(seq44208);
var seq44208__$1 = cljs.core.next(seq44208);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__44209,seq44208__$1);
}));


//# sourceMappingURL=shadow.dom.js.map
