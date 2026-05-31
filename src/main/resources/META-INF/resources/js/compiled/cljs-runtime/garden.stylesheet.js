goog.provide('garden.stylesheet');
/**
 * Create a rule function for the given selector. The `selector`
 *   argument must be valid selector (ie. a keyword, string, or symbol).
 *   Additional arguments may consist of extra selectors or
 *   declarations.
 * 
 *   The returned function accepts any number of arguments which represent
 *   the rule's children.
 * 
 *   Ex.
 *    (let [text-field (rule "[type="text"])]
 *     (text-field {:border ["1px" :solid "black"]}))
 *    ;; => ["[type="text"] {:boder ["1px" :solid "black"]}]
 */
garden.stylesheet.rule = (function garden$stylesheet$rule(var_args){
var args__5882__auto__ = [];
var len__5876__auto___51193 = arguments.length;
var i__5877__auto___51194 = (0);
while(true){
if((i__5877__auto___51194 < len__5876__auto___51193)){
args__5882__auto__.push((arguments[i__5877__auto___51194]));

var G__51195 = (i__5877__auto___51194 + (1));
i__5877__auto___51194 = G__51195;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return garden.stylesheet.rule.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(garden.stylesheet.rule.cljs$core$IFn$_invoke$arity$variadic = (function (selector,more){
if((!((((selector instanceof cljs.core.Keyword)) || (((typeof selector === 'string') || ((selector instanceof cljs.core.Symbol)))))))){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("Selector must be either a keyword, string, or symbol.",cljs.core.PersistentArrayMap.EMPTY);
} else {
return (function() { 
var G__51197__delegate = function (children){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.vector,selector,more),children);
};
var G__51197 = function (var_args){
var children = null;
if (arguments.length > 0) {
var G__51200__i = 0, G__51200__a = new Array(arguments.length -  0);
while (G__51200__i < G__51200__a.length) {G__51200__a[G__51200__i] = arguments[G__51200__i + 0]; ++G__51200__i;}
  children = new cljs.core.IndexedSeq(G__51200__a,0,null);
} 
return G__51197__delegate.call(this,children);};
G__51197.cljs$lang$maxFixedArity = 0;
G__51197.cljs$lang$applyTo = (function (arglist__51201){
var children = cljs.core.seq(arglist__51201);
return G__51197__delegate(children);
});
G__51197.cljs$core$IFn$_invoke$arity$variadic = G__51197__delegate;
return G__51197;
})()
;
}
}));

(garden.stylesheet.rule.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(garden.stylesheet.rule.cljs$lang$applyTo = (function (seq51128){
var G__51129 = cljs.core.first(seq51128);
var seq51128__$1 = cljs.core.next(seq51128);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__51129,seq51128__$1);
}));

garden.stylesheet.cssfn = (function garden$stylesheet$cssfn(fn_name){
return (function() { 
var G__51202__delegate = function (args){
return (new garden.types.CSSFunction(fn_name,args,null,null,null));
};
var G__51202 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__51203__i = 0, G__51203__a = new Array(arguments.length -  0);
while (G__51203__i < G__51203__a.length) {G__51203__a[G__51203__i] = arguments[G__51203__i + 0]; ++G__51203__i;}
  args = new cljs.core.IndexedSeq(G__51203__a,0,null);
} 
return G__51202__delegate.call(this,args);};
G__51202.cljs$lang$maxFixedArity = 0;
G__51202.cljs$lang$applyTo = (function (arglist__51204){
var args = cljs.core.seq(arglist__51204);
return G__51202__delegate(args);
});
G__51202.cljs$core$IFn$_invoke$arity$variadic = G__51202__delegate;
return G__51202;
})()
;
});
garden.stylesheet.at_rule = (function garden$stylesheet$at_rule(identifier,value){
return (new garden.types.CSSAtRule(identifier,value,null,null,null));
});
/**
 * Create a CSS @font-face rule.
 */
garden.stylesheet.at_font_face = (function garden$stylesheet$at_font_face(var_args){
var args__5882__auto__ = [];
var len__5876__auto___51205 = arguments.length;
var i__5877__auto___51206 = (0);
while(true){
if((i__5877__auto___51206 < len__5876__auto___51205)){
args__5882__auto__.push((arguments[i__5877__auto___51206]));

var G__51207 = (i__5877__auto___51206 + (1));
i__5877__auto___51206 = G__51207;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return garden.stylesheet.at_font_face.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(garden.stylesheet.at_font_face.cljs$core$IFn$_invoke$arity$variadic = (function (font_properties){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@font-face",font_properties], null);
}));

(garden.stylesheet.at_font_face.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(garden.stylesheet.at_font_face.cljs$lang$applyTo = (function (seq51134){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq51134));
}));

/**
 * Create a CSS @import rule.
 */
garden.stylesheet.at_import = (function garden$stylesheet$at_import(var_args){
var G__51146 = arguments.length;
switch (G__51146) {
case 1:
return garden.stylesheet.at_import.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
var args_arr__5901__auto__ = [];
var len__5876__auto___51223 = arguments.length;
var i__5877__auto___51224 = (0);
while(true){
if((i__5877__auto___51224 < len__5876__auto___51223)){
args_arr__5901__auto__.push((arguments[i__5877__auto___51224]));

var G__51225 = (i__5877__auto___51224 + (1));
i__5877__auto___51224 = G__51225;
continue;
} else {
}
break;
}

var argseq__5902__auto__ = ((((1) < args_arr__5901__auto__.length))?(new cljs.core.IndexedSeq(args_arr__5901__auto__.slice((1)),(0),null)):null);
return garden.stylesheet.at_import.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5902__auto__);

}
});

(garden.stylesheet.at_import.cljs$core$IFn$_invoke$arity$1 = (function (url){
return garden.stylesheet.at_rule(new cljs.core.Keyword(null,"import","import",-1399500709),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"url","url",276297046),url,new cljs.core.Keyword(null,"media-queries","media-queries",-1563277678),null], null));
}));

(garden.stylesheet.at_import.cljs$core$IFn$_invoke$arity$variadic = (function (url,media_queries){
return garden.stylesheet.at_rule(new cljs.core.Keyword(null,"import","import",-1399500709),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"url","url",276297046),url,new cljs.core.Keyword(null,"media-queries","media-queries",-1563277678),media_queries], null));
}));

/** @this {Function} */
(garden.stylesheet.at_import.cljs$lang$applyTo = (function (seq51144){
var G__51145 = cljs.core.first(seq51144);
var seq51144__$1 = cljs.core.next(seq51144);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__51145,seq51144__$1);
}));

(garden.stylesheet.at_import.cljs$lang$maxFixedArity = (1));

/**
 * Create a CSS @media rule.
 */
garden.stylesheet.at_media = (function garden$stylesheet$at_media(var_args){
var args__5882__auto__ = [];
var len__5876__auto___51229 = arguments.length;
var i__5877__auto___51230 = (0);
while(true){
if((i__5877__auto___51230 < len__5876__auto___51229)){
args__5882__auto__.push((arguments[i__5877__auto___51230]));

var G__51244 = (i__5877__auto___51230 + (1));
i__5877__auto___51230 = G__51244;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return garden.stylesheet.at_media.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(garden.stylesheet.at_media.cljs$core$IFn$_invoke$arity$variadic = (function (media_queries,rules){
return garden.stylesheet.at_rule(new cljs.core.Keyword(null,"media","media",-1066138403),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"media-queries","media-queries",-1563277678),media_queries,new cljs.core.Keyword(null,"rules","rules",1198912366),rules], null));
}));

(garden.stylesheet.at_media.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(garden.stylesheet.at_media.cljs$lang$applyTo = (function (seq51156){
var G__51157 = cljs.core.first(seq51156);
var seq51156__$1 = cljs.core.next(seq51156);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__51157,seq51156__$1);
}));

garden.stylesheet.at_supports = (function garden$stylesheet$at_supports(var_args){
var args__5882__auto__ = [];
var len__5876__auto___51260 = arguments.length;
var i__5877__auto___51262 = (0);
while(true){
if((i__5877__auto___51262 < len__5876__auto___51260)){
args__5882__auto__.push((arguments[i__5877__auto___51262]));

var G__51263 = (i__5877__auto___51262 + (1));
i__5877__auto___51262 = G__51263;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return garden.stylesheet.at_supports.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(garden.stylesheet.at_supports.cljs$core$IFn$_invoke$arity$variadic = (function (feature_queries,rules){

return garden.stylesheet.at_rule(new cljs.core.Keyword(null,"feature","feature",27242652),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"feature-queries","feature-queries",-1340998408),feature_queries,new cljs.core.Keyword(null,"rules","rules",1198912366),rules], null));
}));

(garden.stylesheet.at_supports.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(garden.stylesheet.at_supports.cljs$lang$applyTo = (function (seq51170){
var G__51171 = cljs.core.first(seq51170);
var seq51170__$1 = cljs.core.next(seq51170);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__51171,seq51170__$1);
}));

/**
 * Create a CSS @keyframes rule.
 */
garden.stylesheet.at_keyframes = (function garden$stylesheet$at_keyframes(var_args){
var args__5882__auto__ = [];
var len__5876__auto___51268 = arguments.length;
var i__5877__auto___51269 = (0);
while(true){
if((i__5877__auto___51269 < len__5876__auto___51268)){
args__5882__auto__.push((arguments[i__5877__auto___51269]));

var G__51271 = (i__5877__auto___51269 + (1));
i__5877__auto___51269 = G__51271;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return garden.stylesheet.at_keyframes.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(garden.stylesheet.at_keyframes.cljs$core$IFn$_invoke$arity$variadic = (function (identifier,frames){
return garden.stylesheet.at_rule(new cljs.core.Keyword(null,"keyframes","keyframes",-1437976012),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"identifier","identifier",-805503498),identifier,new cljs.core.Keyword(null,"frames","frames",1765687497),frames], null));
}));

(garden.stylesheet.at_keyframes.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(garden.stylesheet.at_keyframes.cljs$lang$applyTo = (function (seq51175){
var G__51176 = cljs.core.first(seq51175);
var seq51175__$1 = cljs.core.next(seq51175);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__51176,seq51175__$1);
}));

/**
 * Create a color from RGB values.
 */
garden.stylesheet.rgb = (function garden$stylesheet$rgb(r,g,b){
return garden.color.rgb.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [r,g,b], null));
});
/**
 * Create a color from HSL values.
 */
garden.stylesheet.hsl = (function garden$stylesheet$hsl(h,s,l){
return garden.color.hsl.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [h,s,l], null));
});

//# sourceMappingURL=garden.stylesheet.js.map
