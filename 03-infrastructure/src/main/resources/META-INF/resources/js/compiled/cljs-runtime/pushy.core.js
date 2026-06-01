goog.provide('pushy.core');
pushy.core.on_click = (function pushy$core$on_click(funk){
return goog.events.listen(document,"click",funk);
});
pushy.core.update_history_BANG_ = (function pushy$core$update_history_BANG_(h){
var G__46457 = h;
G__46457.setUseFragment(false);

G__46457.setPathPrefix("");

G__46457.setEnabled(true);

return G__46457;
});
pushy.core.set_retrieve_token_BANG_ = (function pushy$core$set_retrieve_token_BANG_(t){
(t.retrieveToken = (function (path_prefix,location){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(location.pathname)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(location.search)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(location.hash));
}));

return t;
});
pushy.core.set_create_url_BANG_ = (function pushy$core$set_create_url_BANG_(t){
(t.createUrl = (function (token,path_prefix,location){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_prefix)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(token));
}));

return t;
});
pushy.core.new_history = (function pushy$core$new_history(var_args){
var G__46469 = arguments.length;
switch (G__46469) {
case 0:
return pushy.core.new_history.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return pushy.core.new_history.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(pushy.core.new_history.cljs$core$IFn$_invoke$arity$0 = (function (){
return pushy.core.new_history.cljs$core$IFn$_invoke$arity$1(pushy.core.set_create_url_BANG_(pushy.core.set_retrieve_token_BANG_((new goog.history.Html5History.TokenTransformer()))));
}));

(pushy.core.new_history.cljs$core$IFn$_invoke$arity$1 = (function (transformer){
return pushy.core.update_history_BANG_((new goog.history.Html5History(window,transformer)));
}));

(pushy.core.new_history.cljs$lang$maxFixedArity = 1);


/**
 * @interface
 */
pushy.core.IHistory = function(){};

var pushy$core$IHistory$set_token_BANG_$dyn_46824 = (function() {
var G__46826 = null;
var G__46826__2 = (function (this$,token){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (pushy.core.set_token_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,token) : m__5499__auto__.call(null,this$,token));
} else {
var m__5497__auto__ = (pushy.core.set_token_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,token) : m__5497__auto__.call(null,this$,token));
} else {
throw cljs.core.missing_protocol("IHistory.set-token!",this$);
}
}
});
var G__46826__3 = (function (this$,token,title){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (pushy.core.set_token_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$3(this$,token,title) : m__5499__auto__.call(null,this$,token,title));
} else {
var m__5497__auto__ = (pushy.core.set_token_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$3(this$,token,title) : m__5497__auto__.call(null,this$,token,title));
} else {
throw cljs.core.missing_protocol("IHistory.set-token!",this$);
}
}
});
G__46826 = function(this$,token,title){
switch(arguments.length){
case 2:
return G__46826__2.call(this,this$,token);
case 3:
return G__46826__3.call(this,this$,token,title);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__46826.cljs$core$IFn$_invoke$arity$2 = G__46826__2;
G__46826.cljs$core$IFn$_invoke$arity$3 = G__46826__3;
return G__46826;
})()
;
pushy.core.set_token_BANG_ = (function pushy$core$set_token_BANG_(var_args){
var G__46487 = arguments.length;
switch (G__46487) {
case 2:
return pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (this$,token){
if((((!((this$ == null)))) && ((!((this$.pushy$core$IHistory$set_token_BANG_$arity$2 == null)))))){
return this$.pushy$core$IHistory$set_token_BANG_$arity$2(this$,token);
} else {
return pushy$core$IHistory$set_token_BANG_$dyn_46824(this$,token);
}
}));

(pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (this$,token,title){
if((((!((this$ == null)))) && ((!((this$.pushy$core$IHistory$set_token_BANG_$arity$3 == null)))))){
return this$.pushy$core$IHistory$set_token_BANG_$arity$3(this$,token,title);
} else {
return pushy$core$IHistory$set_token_BANG_$dyn_46824(this$,token,title);
}
}));

(pushy.core.set_token_BANG_.cljs$lang$maxFixedArity = 3);


var pushy$core$IHistory$replace_token_BANG_$dyn_46830 = (function() {
var G__46831 = null;
var G__46831__2 = (function (this$,token){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (pushy.core.replace_token_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$2(this$,token) : m__5499__auto__.call(null,this$,token));
} else {
var m__5497__auto__ = (pushy.core.replace_token_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$2(this$,token) : m__5497__auto__.call(null,this$,token));
} else {
throw cljs.core.missing_protocol("IHistory.replace-token!",this$);
}
}
});
var G__46831__3 = (function (this$,token,title){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (pushy.core.replace_token_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$3(this$,token,title) : m__5499__auto__.call(null,this$,token,title));
} else {
var m__5497__auto__ = (pushy.core.replace_token_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$3(this$,token,title) : m__5497__auto__.call(null,this$,token,title));
} else {
throw cljs.core.missing_protocol("IHistory.replace-token!",this$);
}
}
});
G__46831 = function(this$,token,title){
switch(arguments.length){
case 2:
return G__46831__2.call(this,this$,token);
case 3:
return G__46831__3.call(this,this$,token,title);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__46831.cljs$core$IFn$_invoke$arity$2 = G__46831__2;
G__46831.cljs$core$IFn$_invoke$arity$3 = G__46831__3;
return G__46831;
})()
;
pushy.core.replace_token_BANG_ = (function pushy$core$replace_token_BANG_(var_args){
var G__46508 = arguments.length;
switch (G__46508) {
case 2:
return pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (this$,token){
if((((!((this$ == null)))) && ((!((this$.pushy$core$IHistory$replace_token_BANG_$arity$2 == null)))))){
return this$.pushy$core$IHistory$replace_token_BANG_$arity$2(this$,token);
} else {
return pushy$core$IHistory$replace_token_BANG_$dyn_46830(this$,token);
}
}));

(pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (this$,token,title){
if((((!((this$ == null)))) && ((!((this$.pushy$core$IHistory$replace_token_BANG_$arity$3 == null)))))){
return this$.pushy$core$IHistory$replace_token_BANG_$arity$3(this$,token,title);
} else {
return pushy$core$IHistory$replace_token_BANG_$dyn_46830(this$,token,title);
}
}));

(pushy.core.replace_token_BANG_.cljs$lang$maxFixedArity = 3);


var pushy$core$IHistory$get_token$dyn_46835 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (pushy.core.get_token[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (pushy.core.get_token["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IHistory.get-token",this$);
}
}
});
pushy.core.get_token = (function pushy$core$get_token(this$){
if((((!((this$ == null)))) && ((!((this$.pushy$core$IHistory$get_token$arity$1 == null)))))){
return this$.pushy$core$IHistory$get_token$arity$1(this$);
} else {
return pushy$core$IHistory$get_token$dyn_46835(this$);
}
});

var pushy$core$IHistory$start_BANG_$dyn_46836 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (pushy.core.start_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (pushy.core.start_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IHistory.start!",this$);
}
}
});
pushy.core.start_BANG_ = (function pushy$core$start_BANG_(this$){
if((((!((this$ == null)))) && ((!((this$.pushy$core$IHistory$start_BANG_$arity$1 == null)))))){
return this$.pushy$core$IHistory$start_BANG_$arity$1(this$);
} else {
return pushy$core$IHistory$start_BANG_$dyn_46836(this$);
}
});

var pushy$core$IHistory$stop_BANG_$dyn_46837 = (function (this$){
var x__5498__auto__ = (((this$ == null))?null:this$);
var m__5499__auto__ = (pushy.core.stop_BANG_[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5499__auto__.call(null,this$));
} else {
var m__5497__auto__ = (pushy.core.stop_BANG_["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5497__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IHistory.stop!",this$);
}
}
});
pushy.core.stop_BANG_ = (function pushy$core$stop_BANG_(this$){
if((((!((this$ == null)))) && ((!((this$.pushy$core$IHistory$stop_BANG_$arity$1 == null)))))){
return this$.pushy$core$IHistory$stop_BANG_$arity$1(this$);
} else {
return pushy$core$IHistory$stop_BANG_$dyn_46837(this$);
}
});

pushy.core.processable_url_QMARK_ = (function pushy$core$processable_url_QMARK_(uri){
return (((!(clojure.string.blank_QMARK_(uri)))) && (((((cljs.core.not(uri.hasScheme())) && (cljs.core.not(uri.hasDomain())))) || ((!((cljs.core.re_matches(cljs.core.re_pattern((""+"^"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(location.origin)+".*$")),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(uri))) == null)))))));
});
pushy.core.get_token_from_uri = (function pushy$core$get_token_from_uri(uri){
var path = uri.getPath();
var query = uri.getQuery();
var fragment = uri.getFragment();
var G__46567 = path;
var G__46567__$1 = ((cljs.core.seq(query))?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__46567)+"?"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(query)):G__46567);
if(cljs.core.seq(fragment)){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__46567__$1)+"#"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fragment));
} else {
return G__46567__$1;
}
});
pushy.core.valid_link_QMARK_ = (function pushy$core$valid_link_QMARK_(el){
var and__5140__auto__ = (function (){var G__46573 = el.tagName;
var fexpr__46572 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["AREA",null,"A",null], null), null);
return (fexpr__46572.cljs$core$IFn$_invoke$arity$1 ? fexpr__46572.cljs$core$IFn$_invoke$arity$1(G__46573) : fexpr__46572.call(null,G__46573));
})();
if(cljs.core.truth_(and__5140__auto__)){
return el.hasAttribute("href");
} else {
return and__5140__auto__;
}
});

/**
* @constructor
 * @implements {pushy.core.IHistory}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
pushy.core.t_pushy$core46596 = (function (event_keys,identity_fn,prevent_default_when_no_match_QMARK_,dispatch_fn,history,map__46593,processable_url_QMARK_,p__46591,match_fn,meta46597){
this.event_keys = event_keys;
this.identity_fn = identity_fn;
this.prevent_default_when_no_match_QMARK_ = prevent_default_when_no_match_QMARK_;
this.dispatch_fn = dispatch_fn;
this.history = history;
this.map__46593 = map__46593;
this.processable_url_QMARK_ = processable_url_QMARK_;
this.p__46591 = p__46591;
this.match_fn = match_fn;
this.meta46597 = meta46597;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(pushy.core.t_pushy$core46596.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_46598,meta46597__$1){
var self__ = this;
var _46598__$1 = this;
return (new pushy.core.t_pushy$core46596(self__.event_keys,self__.identity_fn,self__.prevent_default_when_no_match_QMARK_,self__.dispatch_fn,self__.history,self__.map__46593,self__.processable_url_QMARK_,self__.p__46591,self__.match_fn,meta46597__$1));
}));

(pushy.core.t_pushy$core46596.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_46598){
var self__ = this;
var _46598__$1 = this;
return self__.meta46597;
}));

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$ = cljs.core.PROTOCOL_SENTINEL);

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$set_token_BANG_$arity$2 = (function (_,token){
var self__ = this;
var ___$1 = this;
return self__.history.setToken(token);
}));

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$set_token_BANG_$arity$3 = (function (_,token,title){
var self__ = this;
var ___$1 = this;
return self__.history.setToken(token,title);
}));

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$replace_token_BANG_$arity$2 = (function (_,token){
var self__ = this;
var ___$1 = this;
return self__.history.replaceToken(token);
}));

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$replace_token_BANG_$arity$3 = (function (_,token,title){
var self__ = this;
var ___$1 = this;
return self__.history.replaceToken(token,title);
}));

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$get_token$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.history.getToken();
}));

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$start_BANG_$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
this$__$1.pushy$core$IHistory$stop_BANG_$arity$1(null);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.event_keys,cljs.core.conj,goog.events.listen(self__.history,goog.history.EventType.NAVIGATE,(function (e){
var temp__5825__auto__ = (function (){var G__46657 = (function (){var G__46658 = e.token;
return (self__.match_fn.cljs$core$IFn$_invoke$arity$1 ? self__.match_fn.cljs$core$IFn$_invoke$arity$1(G__46658) : self__.match_fn.call(null,G__46658));
})();
return (self__.identity_fn.cljs$core$IFn$_invoke$arity$1 ? self__.identity_fn.cljs$core$IFn$_invoke$arity$1(G__46657) : self__.identity_fn.call(null,G__46657));
})();
if(cljs.core.truth_(temp__5825__auto__)){
var match = temp__5825__auto__;
return (self__.dispatch_fn.cljs$core$IFn$_invoke$arity$1 ? self__.dispatch_fn.cljs$core$IFn$_invoke$arity$1(match) : self__.dispatch_fn.call(null,match));
} else {
return null;
}
})));

var temp__5825__auto___46853 = (function (){var G__46666 = (function (){var G__46668 = this$__$1.pushy$core$IHistory$get_token$arity$1(null);
return (self__.match_fn.cljs$core$IFn$_invoke$arity$1 ? self__.match_fn.cljs$core$IFn$_invoke$arity$1(G__46668) : self__.match_fn.call(null,G__46668));
})();
return (self__.identity_fn.cljs$core$IFn$_invoke$arity$1 ? self__.identity_fn.cljs$core$IFn$_invoke$arity$1(G__46666) : self__.identity_fn.call(null,G__46666));
})();
if(cljs.core.truth_(temp__5825__auto___46853)){
var match_46855 = temp__5825__auto___46853;
(self__.dispatch_fn.cljs$core$IFn$_invoke$arity$1 ? self__.dispatch_fn.cljs$core$IFn$_invoke$arity$1(match_46855) : self__.dispatch_fn.call(null,match_46855));
} else {
}

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.event_keys,cljs.core.conj,pushy.core.on_click((function (e){
var temp__5825__auto__ = (function (){var G__46676 = e;
var G__46676__$1 = (((G__46676 == null))?null:G__46676.target);
if((G__46676__$1 == null)){
return null;
} else {
return goog.dom.getAncestor(G__46676__$1,pushy.core.valid_link_QMARK_,true);
}
})();
if(cljs.core.truth_(temp__5825__auto__)){
var el = temp__5825__auto__;
var uri = goog.Uri.parse(el.href);
if(cljs.core.truth_((function (){var and__5140__auto__ = (self__.processable_url_QMARK_.cljs$core$IFn$_invoke$arity$1 ? self__.processable_url_QMARK_.cljs$core$IFn$_invoke$arity$1(uri) : self__.processable_url_QMARK_.call(null,uri));
if(cljs.core.truth_(and__5140__auto__)){
return ((cljs.core.not(e.altKey)) && (((cljs.core.not(e.ctrlKey)) && (((cljs.core.not(e.metaKey)) && (((cljs.core.not(e.shiftKey)) && (((cljs.core.not(cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["_self",null,"_blank",null], null), null),el.getAttribute("target")))) && (((((cljs.core.not(el.hasAttribute("data-pushy-ignore"))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(el.getAttribute("data-pushy-ignore"),"false")))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((0),e.button)))))))))))));
} else {
return and__5140__auto__;
}
})())){
var next_token = pushy.core.get_token_from_uri(uri);
if(cljs.core.truth_((function (){var G__46695 = (self__.match_fn.cljs$core$IFn$_invoke$arity$1 ? self__.match_fn.cljs$core$IFn$_invoke$arity$1(next_token) : self__.match_fn.call(null,next_token));
return (self__.identity_fn.cljs$core$IFn$_invoke$arity$1 ? self__.identity_fn.cljs$core$IFn$_invoke$arity$1(G__46695) : self__.identity_fn.call(null,G__46695));
})())){
var temp__5823__auto___46857 = el.title;
if(cljs.core.truth_(temp__5823__auto___46857)){
var title_46858 = temp__5823__auto___46857;
this$__$1.pushy$core$IHistory$set_token_BANG_$arity$3(null,next_token,title_46858);
} else {
this$__$1.pushy$core$IHistory$set_token_BANG_$arity$2(null,next_token);
}

return e.preventDefault();
} else {
if(cljs.core.truth_((self__.prevent_default_when_no_match_QMARK_.cljs$core$IFn$_invoke$arity$1 ? self__.prevent_default_when_no_match_QMARK_.cljs$core$IFn$_invoke$arity$1(next_token) : self__.prevent_default_when_no_match_QMARK_.call(null,next_token)))){
return e.preventDefault();
} else {
return null;
}
}
} else {
return null;
}
} else {
return null;
}
})));

return null;
}));

(pushy.core.t_pushy$core46596.prototype.pushy$core$IHistory$stop_BANG_$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
var seq__46699_46861 = cljs.core.seq(cljs.core.deref(self__.event_keys));
var chunk__46700_46862 = null;
var count__46701_46863 = (0);
var i__46702_46864 = (0);
while(true){
if((i__46702_46864 < count__46701_46863)){
var key_46870 = chunk__46700_46862.cljs$core$IIndexed$_nth$arity$2(null,i__46702_46864);
goog.events.unlistenByKey(key_46870);


var G__46873 = seq__46699_46861;
var G__46874 = chunk__46700_46862;
var G__46875 = count__46701_46863;
var G__46876 = (i__46702_46864 + (1));
seq__46699_46861 = G__46873;
chunk__46700_46862 = G__46874;
count__46701_46863 = G__46875;
i__46702_46864 = G__46876;
continue;
} else {
var temp__5825__auto___46878 = cljs.core.seq(seq__46699_46861);
if(temp__5825__auto___46878){
var seq__46699_46882__$1 = temp__5825__auto___46878;
if(cljs.core.chunked_seq_QMARK_(seq__46699_46882__$1)){
var c__5673__auto___46885 = cljs.core.chunk_first(seq__46699_46882__$1);
var G__46887 = cljs.core.chunk_rest(seq__46699_46882__$1);
var G__46888 = c__5673__auto___46885;
var G__46889 = cljs.core.count(c__5673__auto___46885);
var G__46890 = (0);
seq__46699_46861 = G__46887;
chunk__46700_46862 = G__46888;
count__46701_46863 = G__46889;
i__46702_46864 = G__46890;
continue;
} else {
var key_46894 = cljs.core.first(seq__46699_46882__$1);
goog.events.unlistenByKey(key_46894);


var G__46899 = cljs.core.next(seq__46699_46882__$1);
var G__46900 = null;
var G__46901 = (0);
var G__46902 = (0);
seq__46699_46861 = G__46899;
chunk__46700_46862 = G__46900;
count__46701_46863 = G__46901;
i__46702_46864 = G__46902;
continue;
}
} else {
}
}
break;
}

return cljs.core.reset_BANG_(self__.event_keys,null);
}));

(pushy.core.t_pushy$core46596.getBasis = (function (){
return new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"event-keys","event-keys",804564896,null),new cljs.core.Symbol(null,"identity-fn","identity-fn",756348900,null),new cljs.core.Symbol(null,"prevent-default-when-no-match?","prevent-default-when-no-match?",1281907909,null),new cljs.core.Symbol(null,"dispatch-fn","dispatch-fn",-1401088155,null),new cljs.core.Symbol(null,"history","history",1393136307,null),new cljs.core.Symbol(null,"map__46593","map__46593",-501328364,null),new cljs.core.Symbol(null,"processable-url?","processable-url?",-789027433,null),new cljs.core.Symbol(null,"p__46591","p__46591",-1484912551,null),new cljs.core.Symbol(null,"match-fn","match-fn",-795226853,null),new cljs.core.Symbol(null,"meta46597","meta46597",-704894001,null)], null);
}));

(pushy.core.t_pushy$core46596.cljs$lang$type = true);

(pushy.core.t_pushy$core46596.cljs$lang$ctorStr = "pushy.core/t_pushy$core46596");

(pushy.core.t_pushy$core46596.cljs$lang$ctorPrWriter = (function (this__5434__auto__,writer__5435__auto__,opt__5436__auto__){
return cljs.core._write(writer__5435__auto__,"pushy.core/t_pushy$core46596");
}));

/**
 * Positional factory function for pushy.core/t_pushy$core46596.
 */
pushy.core.__GT_t_pushy$core46596 = (function pushy$core$__GT_t_pushy$core46596(event_keys,identity_fn,prevent_default_when_no_match_QMARK_,dispatch_fn,history,map__46593,processable_url_QMARK_,p__46591,match_fn,meta46597){
return (new pushy.core.t_pushy$core46596(event_keys,identity_fn,prevent_default_when_no_match_QMARK_,dispatch_fn,history,map__46593,processable_url_QMARK_,p__46591,match_fn,meta46597));
});


/**
 * Takes in three functions:
 *  * dispatch-fn: the function that dispatches when a match is found
 *  * match-fn: the function used to check if a particular route exists
 *  * identity-fn: (optional) extract the route from value returned by match-fn
 */
pushy.core.pushy = (function pushy$core$pushy(var_args){
var args__5882__auto__ = [];
var len__5876__auto___46915 = arguments.length;
var i__5877__auto___46916 = (0);
while(true){
if((i__5877__auto___46916 < len__5876__auto___46915)){
args__5882__auto__.push((arguments[i__5877__auto___46916]));

var G__46917 = (i__5877__auto___46916 + (1));
i__5877__auto___46916 = G__46917;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return pushy.core.pushy.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(pushy.core.pushy.cljs$core$IFn$_invoke$arity$variadic = (function (dispatch_fn,match_fn,p__46591){
var map__46593 = p__46591;
var map__46593__$1 = cljs.core.__destructure_map(map__46593);
var processable_url_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__46593__$1,new cljs.core.Keyword(null,"processable-url?","processable-url?",1865408336),pushy.core.processable_url_QMARK_);
var identity_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__46593__$1,new cljs.core.Keyword(null,"identity-fn","identity-fn",-884182627),cljs.core.identity);
var prevent_default_when_no_match_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__46593__$1,new cljs.core.Keyword(null,"prevent-default-when-no-match?","prevent-default-when-no-match?",-358623618),cljs.core.constantly(false));
var history__$1 = pushy.core.new_history.cljs$core$IFn$_invoke$arity$0();
var event_keys = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (new pushy.core.t_pushy$core46596(event_keys,identity_fn,prevent_default_when_no_match_QMARK_,dispatch_fn,history__$1,map__46593__$1,processable_url_QMARK_,p__46591,match_fn,cljs.core.PersistentArrayMap.EMPTY));
}));

(pushy.core.pushy.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(pushy.core.pushy.cljs$lang$applyTo = (function (seq46582){
var G__46583 = cljs.core.first(seq46582);
var seq46582__$1 = cljs.core.next(seq46582);
var G__46584 = cljs.core.first(seq46582__$1);
var seq46582__$2 = cljs.core.next(seq46582__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__46583,G__46584,seq46582__$2);
}));

/**
 * Returns whether Html5History is supported
 */
pushy.core.supported_QMARK_ = (function pushy$core$supported_QMARK_(var_args){
var G__46791 = arguments.length;
switch (G__46791) {
case 0:
return pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$0 = (function (){
return pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$1(window);
}));

(pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$1 = (function (window){
return goog.history.Html5History.isSupported(window);
}));

(pushy.core.supported_QMARK_.cljs$lang$maxFixedArity = 1);

pushy.core.push_state_BANG_ = (function pushy$core$push_state_BANG_(var_args){
var G__46802 = arguments.length;
switch (G__46802) {
case 2:
return pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (dispatch_fn,match_fn){
return pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$3(dispatch_fn,match_fn,cljs.core.identity);
}));

(pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (dispatch_fn,match_fn,identity_fn){
var h = pushy.core.pushy.cljs$core$IFn$_invoke$arity$variadic(dispatch_fn,match_fn,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"identity-fn","identity-fn",-884182627),identity_fn], 0));
h.pushy$core$IHistory$start_BANG_$arity$1(null);

return h;
}));

(pushy.core.push_state_BANG_.cljs$lang$maxFixedArity = 3);


//# sourceMappingURL=pushy.core.js.map
