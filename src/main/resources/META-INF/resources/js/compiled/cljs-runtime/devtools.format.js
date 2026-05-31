goog.provide('devtools.format');

/**
 * @interface
 */
devtools.format.IDevtoolsFormat = function(){};

var devtools$format$IDevtoolsFormat$_header$dyn_15553 = (function (value){
var x__5498__auto__ = (((value == null))?null:value);
var m__5499__auto__ = (devtools.format._header[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(value) : m__5499__auto__.call(null,value));
} else {
var m__5497__auto__ = (devtools.format._header["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(value) : m__5497__auto__.call(null,value));
} else {
throw cljs.core.missing_protocol("IDevtoolsFormat.-header",value);
}
}
});
devtools.format._header = (function devtools$format$_header(value){
if((((!((value == null)))) && ((!((value.devtools$format$IDevtoolsFormat$_header$arity$1 == null)))))){
return value.devtools$format$IDevtoolsFormat$_header$arity$1(value);
} else {
return devtools$format$IDevtoolsFormat$_header$dyn_15553(value);
}
});

var devtools$format$IDevtoolsFormat$_has_body$dyn_15572 = (function (value){
var x__5498__auto__ = (((value == null))?null:value);
var m__5499__auto__ = (devtools.format._has_body[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(value) : m__5499__auto__.call(null,value));
} else {
var m__5497__auto__ = (devtools.format._has_body["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(value) : m__5497__auto__.call(null,value));
} else {
throw cljs.core.missing_protocol("IDevtoolsFormat.-has-body",value);
}
}
});
devtools.format._has_body = (function devtools$format$_has_body(value){
if((((!((value == null)))) && ((!((value.devtools$format$IDevtoolsFormat$_has_body$arity$1 == null)))))){
return value.devtools$format$IDevtoolsFormat$_has_body$arity$1(value);
} else {
return devtools$format$IDevtoolsFormat$_has_body$dyn_15572(value);
}
});

var devtools$format$IDevtoolsFormat$_body$dyn_15594 = (function (value){
var x__5498__auto__ = (((value == null))?null:value);
var m__5499__auto__ = (devtools.format._body[goog.typeOf(x__5498__auto__)]);
if((!((m__5499__auto__ == null)))){
return (m__5499__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5499__auto__.cljs$core$IFn$_invoke$arity$1(value) : m__5499__auto__.call(null,value));
} else {
var m__5497__auto__ = (devtools.format._body["_"]);
if((!((m__5497__auto__ == null)))){
return (m__5497__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5497__auto__.cljs$core$IFn$_invoke$arity$1(value) : m__5497__auto__.call(null,value));
} else {
throw cljs.core.missing_protocol("IDevtoolsFormat.-body",value);
}
}
});
devtools.format._body = (function devtools$format$_body(value){
if((((!((value == null)))) && ((!((value.devtools$format$IDevtoolsFormat$_body$arity$1 == null)))))){
return value.devtools$format$IDevtoolsFormat$_body$arity$1(value);
} else {
return devtools$format$IDevtoolsFormat$_body$dyn_15594(value);
}
});

devtools.format.setup_BANG_ = (function devtools$format$setup_BANG_(){
if(cljs.core.truth_(devtools.format._STAR_setup_done_STAR_)){
return null;
} else {
(devtools.format._STAR_setup_done_STAR_ = true);

devtools.format.make_template_fn = (function (){var temp__5823__auto__ = (devtools.context.get_root.call(null)["devtools"]);
if(cljs.core.truth_(temp__5823__auto__)){
var o15179 = temp__5823__auto__;
var temp__5823__auto____$1 = (o15179["formatters"]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var o15180 = temp__5823__auto____$1;
var temp__5823__auto____$2 = (o15180["templating"]);
if(cljs.core.truth_(temp__5823__auto____$2)){
var o15181 = temp__5823__auto____$2;
return (o15181["make_template"]);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})();

devtools.format.make_group_fn = (function (){var temp__5823__auto__ = (devtools.context.get_root.call(null)["devtools"]);
if(cljs.core.truth_(temp__5823__auto__)){
var o15193 = temp__5823__auto__;
var temp__5823__auto____$1 = (o15193["formatters"]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var o15194 = temp__5823__auto____$1;
var temp__5823__auto____$2 = (o15194["templating"]);
if(cljs.core.truth_(temp__5823__auto____$2)){
var o15196 = temp__5823__auto____$2;
return (o15196["make_group"]);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})();

devtools.format.make_reference_fn = (function (){var temp__5823__auto__ = (devtools.context.get_root.call(null)["devtools"]);
if(cljs.core.truth_(temp__5823__auto__)){
var o15202 = temp__5823__auto__;
var temp__5823__auto____$1 = (o15202["formatters"]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var o15203 = temp__5823__auto____$1;
var temp__5823__auto____$2 = (o15203["templating"]);
if(cljs.core.truth_(temp__5823__auto____$2)){
var o15204 = temp__5823__auto____$2;
return (o15204["make_reference"]);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})();

devtools.format.make_surrogate_fn = (function (){var temp__5823__auto__ = (devtools.context.get_root.call(null)["devtools"]);
if(cljs.core.truth_(temp__5823__auto__)){
var o15212 = temp__5823__auto__;
var temp__5823__auto____$1 = (o15212["formatters"]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var o15213 = temp__5823__auto____$1;
var temp__5823__auto____$2 = (o15213["templating"]);
if(cljs.core.truth_(temp__5823__auto____$2)){
var o15214 = temp__5823__auto____$2;
return (o15214["make_surrogate"]);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})();

devtools.format.render_markup_fn = (function (){var temp__5823__auto__ = (devtools.context.get_root.call(null)["devtools"]);
if(cljs.core.truth_(temp__5823__auto__)){
var o15224 = temp__5823__auto__;
var temp__5823__auto____$1 = (o15224["formatters"]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var o15225 = temp__5823__auto____$1;
var temp__5823__auto____$2 = (o15225["templating"]);
if(cljs.core.truth_(temp__5823__auto____$2)){
var o15226 = temp__5823__auto____$2;
return (o15226["render_markup"]);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})();

devtools.format._LT_header_GT__fn = (function (){var temp__5823__auto__ = (devtools.context.get_root.call(null)["devtools"]);
if(cljs.core.truth_(temp__5823__auto__)){
var o15235 = temp__5823__auto__;
var temp__5823__auto____$1 = (o15235["formatters"]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var o15236 = temp__5823__auto____$1;
var temp__5823__auto____$2 = (o15236["markup"]);
if(cljs.core.truth_(temp__5823__auto____$2)){
var o15237 = temp__5823__auto____$2;
return (o15237["_LT_header_GT_"]);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})();

devtools.format._LT_standard_body_GT__fn = (function (){var temp__5823__auto__ = (devtools.context.get_root.call(null)["devtools"]);
if(cljs.core.truth_(temp__5823__auto__)){
var o15249 = temp__5823__auto__;
var temp__5823__auto____$1 = (o15249["formatters"]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var o15250 = temp__5823__auto____$1;
var temp__5823__auto____$2 = (o15250["markup"]);
if(cljs.core.truth_(temp__5823__auto____$2)){
var o15251 = temp__5823__auto____$2;
return (o15251["_LT_standard_body_GT_"]);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
})();

if(cljs.core.truth_(devtools.format.make_template_fn)){
} else {
throw (new Error("Assert failed: make-template-fn"));
}

if(cljs.core.truth_(devtools.format.make_group_fn)){
} else {
throw (new Error("Assert failed: make-group-fn"));
}

if(cljs.core.truth_(devtools.format.make_reference_fn)){
} else {
throw (new Error("Assert failed: make-reference-fn"));
}

if(cljs.core.truth_(devtools.format.make_surrogate_fn)){
} else {
throw (new Error("Assert failed: make-surrogate-fn"));
}

if(cljs.core.truth_(devtools.format.render_markup_fn)){
} else {
throw (new Error("Assert failed: render-markup-fn"));
}

if(cljs.core.truth_(devtools.format._LT_header_GT__fn)){
} else {
throw (new Error("Assert failed: <header>-fn"));
}

if(cljs.core.truth_(devtools.format._LT_standard_body_GT__fn)){
return null;
} else {
throw (new Error("Assert failed: <standard-body>-fn"));
}
}
});
devtools.format.render_markup = (function devtools$format$render_markup(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15734 = arguments.length;
var i__5877__auto___15735 = (0);
while(true){
if((i__5877__auto___15735 < len__5876__auto___15734)){
args__5882__auto__.push((arguments[i__5877__auto___15735]));

var G__15739 = (i__5877__auto___15735 + (1));
i__5877__auto___15735 = G__15739;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.render_markup.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.render_markup.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.render_markup_fn,args);
}));

(devtools.format.render_markup.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.render_markup.cljs$lang$applyTo = (function (seq15282){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15282));
}));

devtools.format.make_template = (function devtools$format$make_template(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15759 = arguments.length;
var i__5877__auto___15760 = (0);
while(true){
if((i__5877__auto___15760 < len__5876__auto___15759)){
args__5882__auto__.push((arguments[i__5877__auto___15760]));

var G__15762 = (i__5877__auto___15760 + (1));
i__5877__auto___15760 = G__15762;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.make_template.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.make_template.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.make_template_fn,args);
}));

(devtools.format.make_template.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.make_template.cljs$lang$applyTo = (function (seq15321){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15321));
}));

devtools.format.make_group = (function devtools$format$make_group(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15769 = arguments.length;
var i__5877__auto___15770 = (0);
while(true){
if((i__5877__auto___15770 < len__5876__auto___15769)){
args__5882__auto__.push((arguments[i__5877__auto___15770]));

var G__15772 = (i__5877__auto___15770 + (1));
i__5877__auto___15770 = G__15772;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.make_group.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.make_group.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.make_group_fn,args);
}));

(devtools.format.make_group.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.make_group.cljs$lang$applyTo = (function (seq15343){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15343));
}));

devtools.format.make_surrogate = (function devtools$format$make_surrogate(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15779 = arguments.length;
var i__5877__auto___15781 = (0);
while(true){
if((i__5877__auto___15781 < len__5876__auto___15779)){
args__5882__auto__.push((arguments[i__5877__auto___15781]));

var G__15785 = (i__5877__auto___15781 + (1));
i__5877__auto___15781 = G__15785;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.make_surrogate.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.make_surrogate.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.make_surrogate_fn,args);
}));

(devtools.format.make_surrogate.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.make_surrogate.cljs$lang$applyTo = (function (seq15357){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15357));
}));

devtools.format.template = (function devtools$format$template(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15793 = arguments.length;
var i__5877__auto___15794 = (0);
while(true){
if((i__5877__auto___15794 < len__5876__auto___15793)){
args__5882__auto__.push((arguments[i__5877__auto___15794]));

var G__15795 = (i__5877__auto___15794 + (1));
i__5877__auto___15794 = G__15795;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.template.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.template.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.make_template_fn,args);
}));

(devtools.format.template.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.template.cljs$lang$applyTo = (function (seq15371){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15371));
}));

devtools.format.group = (function devtools$format$group(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15802 = arguments.length;
var i__5877__auto___15803 = (0);
while(true){
if((i__5877__auto___15803 < len__5876__auto___15802)){
args__5882__auto__.push((arguments[i__5877__auto___15803]));

var G__15804 = (i__5877__auto___15803 + (1));
i__5877__auto___15803 = G__15804;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.group.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.group.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.make_group_fn,args);
}));

(devtools.format.group.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.group.cljs$lang$applyTo = (function (seq15379){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15379));
}));

devtools.format.surrogate = (function devtools$format$surrogate(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15812 = arguments.length;
var i__5877__auto___15813 = (0);
while(true){
if((i__5877__auto___15813 < len__5876__auto___15812)){
args__5882__auto__.push((arguments[i__5877__auto___15813]));

var G__15814 = (i__5877__auto___15813 + (1));
i__5877__auto___15813 = G__15814;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.surrogate.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.surrogate.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.make_surrogate_fn,args);
}));

(devtools.format.surrogate.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.surrogate.cljs$lang$applyTo = (function (seq15406){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15406));
}));

devtools.format.reference = (function devtools$format$reference(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15821 = arguments.length;
var i__5877__auto___15822 = (0);
while(true){
if((i__5877__auto___15822 < len__5876__auto___15821)){
args__5882__auto__.push((arguments[i__5877__auto___15822]));

var G__15823 = (i__5877__auto___15822 + (1));
i__5877__auto___15822 = G__15823;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return devtools.format.reference.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(devtools.format.reference.cljs$core$IFn$_invoke$arity$variadic = (function (object,p__15446){
var vec__15448 = p__15446;
var state_override = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__15448,(0),null);
devtools.format.setup_BANG_();

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format.make_reference_fn,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [object,(function (p1__15426_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__15426_SHARP_,state_override], 0));
})], null));
}));

(devtools.format.reference.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(devtools.format.reference.cljs$lang$applyTo = (function (seq15430){
var G__15431 = cljs.core.first(seq15430);
var seq15430__$1 = cljs.core.next(seq15430);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__15431,seq15430__$1);
}));

devtools.format.standard_reference = (function devtools$format$standard_reference(target){
devtools.format.setup_BANG_();

var G__15463 = new cljs.core.Keyword(null,"ol","ol",932524051);
var G__15464 = new cljs.core.Keyword(null,"standard-ol-style","standard-ol-style",2143825615);
var G__15465 = (function (){var G__15470 = new cljs.core.Keyword(null,"li","li",723558921);
var G__15471 = new cljs.core.Keyword(null,"standard-li-style","standard-li-style",413442955);
var G__15472 = (devtools.format.make_reference_fn.cljs$core$IFn$_invoke$arity$1 ? devtools.format.make_reference_fn.cljs$core$IFn$_invoke$arity$1(target) : devtools.format.make_reference_fn.call(null,target));
return (devtools.format.make_template_fn.cljs$core$IFn$_invoke$arity$3 ? devtools.format.make_template_fn.cljs$core$IFn$_invoke$arity$3(G__15470,G__15471,G__15472) : devtools.format.make_template_fn.call(null,G__15470,G__15471,G__15472));
})();
return (devtools.format.make_template_fn.cljs$core$IFn$_invoke$arity$3 ? devtools.format.make_template_fn.cljs$core$IFn$_invoke$arity$3(G__15463,G__15464,G__15465) : devtools.format.make_template_fn.call(null,G__15463,G__15464,G__15465));
});
devtools.format.build_header = (function devtools$format$build_header(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15846 = arguments.length;
var i__5877__auto___15847 = (0);
while(true){
if((i__5877__auto___15847 < len__5876__auto___15846)){
args__5882__auto__.push((arguments[i__5877__auto___15847]));

var G__15850 = (i__5877__auto___15847 + (1));
i__5877__auto___15847 = G__15850;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return devtools.format.build_header.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(devtools.format.build_header.cljs$core$IFn$_invoke$arity$variadic = (function (args){
devtools.format.setup_BANG_();

return devtools.format.render_markup.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format._LT_header_GT__fn,args)], 0));
}));

(devtools.format.build_header.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(devtools.format.build_header.cljs$lang$applyTo = (function (seq15484){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq15484));
}));

devtools.format.standard_body_template = (function devtools$format$standard_body_template(var_args){
var args__5882__auto__ = [];
var len__5876__auto___15855 = arguments.length;
var i__5877__auto___15857 = (0);
while(true){
if((i__5877__auto___15857 < len__5876__auto___15855)){
args__5882__auto__.push((arguments[i__5877__auto___15857]));

var G__15860 = (i__5877__auto___15857 + (1));
i__5877__auto___15857 = G__15860;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return devtools.format.standard_body_template.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(devtools.format.standard_body_template.cljs$core$IFn$_invoke$arity$variadic = (function (lines,rest){
devtools.format.setup_BANG_();

var args = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (x){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [x], null);
}),lines)], null),rest);
return devtools.format.render_markup.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(devtools.format._LT_standard_body_GT__fn,args)], 0));
}));

(devtools.format.standard_body_template.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(devtools.format.standard_body_template.cljs$lang$applyTo = (function (seq15514){
var G__15515 = cljs.core.first(seq15514);
var seq15514__$1 = cljs.core.next(seq15514);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__15515,seq15514__$1);
}));


//# sourceMappingURL=devtools.format.js.map
