goog.provide('re_com.util');
/**
 * Takes a function 'f' amd a map 'm'.  Applies 'f' to each value in 'm' and returns.
 * (fmap  inc  {:a 4  :b 2})   =>   {:a 5  :b 3}
 */
re_com.util.fmap = (function re_com$util$fmap(f,m){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,(function (){var iter__5628__auto__ = (function re_com$util$fmap_$_iter__18043(s__18044){
return (new cljs.core.LazySeq(null,(function (){
var s__18044__$1 = s__18044;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__18044__$1);
if(temp__5825__auto__){
var s__18044__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__18044__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__18044__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__18049 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__18048 = (0);
while(true){
if((i__18048 < size__5627__auto__)){
var vec__18050 = cljs.core._nth(c__5626__auto__,i__18048);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18050,(0),null);
var val = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18050,(1),null);
cljs.core.chunk_append(b__18049,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [k,(f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(val) : f.call(null,val))], null));

var G__18268 = (i__18048 + (1));
i__18048 = G__18268;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__18049),re_com$util$fmap_$_iter__18043(cljs.core.chunk_rest(s__18044__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__18049),null);
}
} else {
var vec__18053 = cljs.core.first(s__18044__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18053,(0),null);
var val = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18053,(1),null);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [k,(f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(val) : f.call(null,val))], null),re_com$util$fmap_$_iter__18043(cljs.core.rest(s__18044__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(m);
})());
});
/**
 * Recursively merges maps. If vals are not maps, the last value wins.
 */
re_com.util.deep_merge = (function re_com$util$deep_merge(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18276 = arguments.length;
var i__5877__auto___18277 = (0);
while(true){
if((i__5877__auto___18277 < len__5876__auto___18276)){
args__5882__auto__.push((arguments[i__5877__auto___18277]));

var G__18278 = (i__5877__auto___18277 + (1));
i__5877__auto___18277 = G__18278;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.util.deep_merge.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.util.deep_merge.cljs$core$IFn$_invoke$arity$variadic = (function (vals){
if(cljs.core.every_QMARK_(cljs.core.map_QMARK_,vals)){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.merge_with,re_com.util.deep_merge,vals);
} else {
return cljs.core.last(vals);
}
}));

(re_com.util.deep_merge.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.util.deep_merge.cljs$lang$applyTo = (function (seq18056){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq18056));
}));

/**
 * Only assoc-in if no value exists at ks
 */
re_com.util.assoc_in_if_empty = (function re_com$util$assoc_in_if_empty(m,ks,v){
return cljs.core.assoc_in(m,ks,cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(m,ks,v));
});
/**
 * Takes a value or an atom
 *   If it's a value, returns it
 *   If it's an object that supports IDeref, returns the value inside it by derefing
 *   
 */
re_com.util.deref_or_value = (function re_com$util$deref_or_value(val_or_atom){
if((((!((val_or_atom == null))))?(((((val_or_atom.cljs$lang$protocol_mask$partition0$ & (32768))) || ((cljs.core.PROTOCOL_SENTINEL === val_or_atom.cljs$core$IDeref$))))?true:(((!val_or_atom.cljs$lang$protocol_mask$partition0$))?cljs.core.native_satisfies_QMARK_(cljs.core.IDeref,val_or_atom):false)):cljs.core.native_satisfies_QMARK_(cljs.core.IDeref,val_or_atom))){
return cljs.core.deref(val_or_atom);
} else {
return val_or_atom;
}
});
/**
 * Takes a value or an atom
 *   If it's a value, returns it
 *   If it's a Reagent object that supports IDeref, returns the value inside it, but WITHOUT derefing
 * 
 *   The arg validation code uses this, since calling deref-or-value adds this arg to the watched ratom list for the component
 *   in question, which in turn can cause different rendering behaviour between dev (where we validate) and prod (where we don't).
 * 
 *   This was experienced in popover-content-wrapper with the position-injected atom which was not derefed there, however
 *   the dev-only validation caused it to be derefed, modifying its render behaviour and causing mayhem and madness for the developer.
 * 
 *   See below that different Reagent types have different ways of retrieving the value without causing capture, although in the case of
 *   Track, we just deref it as there is no peek or state, so hopefully this won't cause issues (surely this is used very rarely).
 *   
 */
re_com.util.deref_or_value_peek = (function re_com$util$deref_or_value_peek(val_or_atom){
if((((!((val_or_atom == null))))?(((((val_or_atom.cljs$lang$protocol_mask$partition0$ & (32768))) || ((cljs.core.PROTOCOL_SENTINEL === val_or_atom.cljs$core$IDeref$))))?true:(((!val_or_atom.cljs$lang$protocol_mask$partition0$))?cljs.core.native_satisfies_QMARK_(cljs.core.IDeref,val_or_atom):false)):cljs.core.native_satisfies_QMARK_(cljs.core.IDeref,val_or_atom))){
if((val_or_atom instanceof reagent.ratom.RAtom)){
return val_or_atom.state;
} else {
if((val_or_atom instanceof reagent.ratom.Reaction)){
return val_or_atom._peek_at();
} else {
if((val_or_atom instanceof reagent.ratom.RCursor)){
return val_or_atom._peek();
} else {
if((val_or_atom instanceof reagent.ratom.Track)){
return cljs.core.deref(val_or_atom);
} else {
if((val_or_atom instanceof reagent.ratom.Wrapper)){
return val_or_atom.state;
} else {
throw (new Error("Unknown reactive data type"));

}
}
}
}
}
} else {
return val_or_atom;
}
});
re_com.util.get_element_by_id = (function re_com$util$get_element_by_id(id){
return document.getElementById(id);
});
/**
 * Left pad a string 's' with '0', until 's' has length 'len'. Return 's' unchanged, if it is already len or greater
 */
re_com.util.pad_zero = (function re_com$util$pad_zero(s,len){
if((cljs.core.count(s) < len)){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,cljs.core.take_last(len,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(len,"0"),s)));
} else {
return s;
}
});
/**
 * return 'num' as a string of 'len' characters, left padding with '0' as necessary
 */
re_com.util.pad_zero_number = (function re_com$util$pad_zero_number(num,len){
return re_com.util.pad_zero((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(num)),len);
});
/**
 * takes a number (and optional :negative keyword to indicate a negative value) and returns that number as a string with 'px' at the end
 */
re_com.util.px = (function re_com$util$px(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18302 = arguments.length;
var i__5877__auto___18303 = (0);
while(true){
if((i__5877__auto___18303 < len__5876__auto___18302)){
args__5882__auto__.push((arguments[i__5877__auto___18303]));

var G__18304 = (i__5877__auto___18303 + (1));
i__5877__auto___18303 = G__18304;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return re_com.util.px.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(re_com.util.px.cljs$core$IFn$_invoke$arity$variadic = (function (val,negative){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(negative)?(- val):val))+"px");
}));

(re_com.util.px.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(re_com.util.px.cljs$lang$applyTo = (function (seq18075){
var G__18076 = cljs.core.first(seq18075);
var seq18075__$1 = cljs.core.next(seq18075);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__18076,seq18075__$1);
}));

/**
 * takes n numbers (could also be strings) and converts them to a space separated px string
 *   e.g. (px-n 10 2 30 4) => '10px 2px 30px 4px' for use in :padding, :margin etc.
 *   Most useful when the args are calculations
 *   e.g. (px-n top-margin (inc h-width) (- top-margin 5) (dec h-width))
 *   Note: Doesn't support :negative like px above but it will work with negative numbers
 */
re_com.util.px_n = (function re_com$util$px_n(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18309 = arguments.length;
var i__5877__auto___18310 = (0);
while(true){
if((i__5877__auto___18310 < len__5876__auto___18309)){
args__5882__auto__.push((arguments[i__5877__auto___18310]));

var G__18311 = (i__5877__auto___18310 + (1));
i__5877__auto___18310 = G__18311;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.util.px_n.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.util.px_n.cljs$core$IFn$_invoke$arity$variadic = (function (vals){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__18088_SHARP_){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__18088_SHARP_)+"px");
}),vals));
}));

(re_com.util.px_n.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.util.px_n.cljs$lang$applyTo = (function (seq18089){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq18089));
}));

/**
 * Return a pluralized phrase, appending an s to the singular form if no plural is provided.
 *   For example:
 *   (pluralize 5 "month") => "5 months"
 *   (pluralize 1 "month") => "1 month"
 *   (pluralize 1 "radius" "radii") => "1 radius"
 *   (pluralize 9 "radius" "radii") => "9 radii"
 *   From https://github.com/flatland/useful/blob/194950/src/flatland/useful/string.clj#L25-L33
 */
re_com.util.pluralize = (function re_com$util$pluralize(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18316 = arguments.length;
var i__5877__auto___18317 = (0);
while(true){
if((i__5877__auto___18317 < len__5876__auto___18316)){
args__5882__auto__.push((arguments[i__5877__auto___18317]));

var G__18318 = (i__5877__auto___18317 + (1));
i__5877__auto___18317 = G__18318;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return re_com.util.pluralize.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(re_com.util.pluralize.cljs$core$IFn$_invoke$arity$variadic = (function (num,singular,p__18103){
var vec__18104 = p__18103;
var plural = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18104,(0),null);
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(num)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),num))?singular:(function (){var or__5142__auto__ = plural;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(singular)+"s");
}
})())));
}));

(re_com.util.pluralize.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(re_com.util.pluralize.cljs$lang$applyTo = (function (seq18097){
var G__18098 = cljs.core.first(seq18097);
var seq18097__$1 = cljs.core.next(seq18097);
var G__18099 = cljs.core.first(seq18097__$1);
var seq18097__$2 = cljs.core.next(seq18097__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__18098,G__18099,seq18097__$2);
}));

/**
 * Removes the item at position n from a vector v, returning a shrunk vector
 */
re_com.util.remove_nth = (function re_com$util$remove_nth(v,n){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(v,(0),n),cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(v,(n + (1)),cljs.core.count(v))));
});
re_com.util.insert_nth = (function re_com$util$insert_nth(vect,index,item){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(cljs.core.merge,cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(vect,(0),index),item,cljs.core.subvec.cljs$core$IFn$_invoke$arity$2(vect,index));
});
re_com.util.__GT_v = (function re_com$util$__GT_v(x){
if(cljs.core.vector_QMARK_(x)){
return x;
} else {
if(cljs.core.sequential_QMARK_(x)){
return cljs.core.vec(x);
} else {
if((x == null)){
return null;
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [x], null);

}
}
}
});
/**
 * Takes a vector of maps 'v'. Returns the position of the first item in 'v' whose id-fn (default :id) matches 'id'.
 * Returns nil if id not found
 */
re_com.util.position_for_id = (function re_com$util$position_for_id(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18327 = arguments.length;
var i__5877__auto___18328 = (0);
while(true){
if((i__5877__auto___18328 < len__5876__auto___18327)){
args__5882__auto__.push((arguments[i__5877__auto___18328]));

var G__18331 = (i__5877__auto___18328 + (1));
i__5877__auto___18328 = G__18331;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return re_com.util.position_for_id.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(re_com.util.position_for_id.cljs$core$IFn$_invoke$arity$variadic = (function (id,v,p__18134){
var map__18137 = p__18134;
var map__18137__$1 = cljs.core.__destructure_map(map__18137);
var id_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__18137__$1,new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"id","id",-1388402092));
var index_fn = (function (index,item){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((id_fn.cljs$core$IFn$_invoke$arity$1 ? id_fn.cljs$core$IFn$_invoke$arity$1(item) : id_fn.call(null,item)),id)){
return index;
} else {
return null;
}
});
return cljs.core.first(cljs.core.keep_indexed.cljs$core$IFn$_invoke$arity$2(index_fn,v));
}));

(re_com.util.position_for_id.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(re_com.util.position_for_id.cljs$lang$applyTo = (function (seq18124){
var G__18126 = cljs.core.first(seq18124);
var seq18124__$1 = cljs.core.next(seq18124);
var G__18127 = cljs.core.first(seq18124__$1);
var seq18124__$2 = cljs.core.next(seq18124__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__18126,G__18127,seq18124__$2);
}));

/**
 * Takes a vector of maps 'v'. Returns the first item in 'v' whose id-fn (default :id) matches 'id'.
 * Returns nil if id not found
 */
re_com.util.item_for_id = (function re_com$util$item_for_id(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18337 = arguments.length;
var i__5877__auto___18338 = (0);
while(true){
if((i__5877__auto___18338 < len__5876__auto___18337)){
args__5882__auto__.push((arguments[i__5877__auto___18338]));

var G__18339 = (i__5877__auto___18338 + (1));
i__5877__auto___18338 = G__18339;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return re_com.util.item_for_id.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(re_com.util.item_for_id.cljs$core$IFn$_invoke$arity$variadic = (function (id,v,p__18156){
var map__18159 = p__18156;
var map__18159__$1 = cljs.core.__destructure_map(map__18159);
var id_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__18159__$1,new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"id","id",-1388402092));
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__18142_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((id_fn.cljs$core$IFn$_invoke$arity$1 ? id_fn.cljs$core$IFn$_invoke$arity$1(p1__18142_SHARP_) : id_fn.call(null,p1__18142_SHARP_)),id);
}),v));
}));

(re_com.util.item_for_id.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(re_com.util.item_for_id.cljs$lang$applyTo = (function (seq18145){
var G__18146 = cljs.core.first(seq18145);
var seq18145__$1 = cljs.core.next(seq18145);
var G__18147 = cljs.core.first(seq18145__$1);
var seq18145__$2 = cljs.core.next(seq18145__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__18146,G__18147,seq18145__$2);
}));

/**
 * Takes a vector of maps 'v', each of which has an id-fn (default :id) key.
 *   Return v where item matching 'id' is excluded
 */
re_com.util.remove_id_item = (function re_com$util$remove_id_item(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18345 = arguments.length;
var i__5877__auto___18346 = (0);
while(true){
if((i__5877__auto___18346 < len__5876__auto___18345)){
args__5882__auto__.push((arguments[i__5877__auto___18346]));

var G__18349 = (i__5877__auto___18346 + (1));
i__5877__auto___18346 = G__18349;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return re_com.util.remove_id_item.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(re_com.util.remove_id_item.cljs$core$IFn$_invoke$arity$variadic = (function (id,v,p__18177){
var map__18178 = p__18177;
var map__18178__$1 = cljs.core.__destructure_map(map__18178);
var id_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__18178__$1,new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"id","id",-1388402092));
return cljs.core.filterv((function (p1__18166_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2((id_fn.cljs$core$IFn$_invoke$arity$1 ? id_fn.cljs$core$IFn$_invoke$arity$1(p1__18166_SHARP_) : id_fn.call(null,p1__18166_SHARP_)),id);
}),v);
}));

(re_com.util.remove_id_item.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(re_com.util.remove_id_item.cljs$lang$applyTo = (function (seq18167){
var G__18168 = cljs.core.first(seq18167);
var seq18167__$1 = cljs.core.next(seq18167);
var G__18169 = cljs.core.first(seq18167__$1);
var seq18167__$2 = cljs.core.next(seq18167__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__18168,G__18169,seq18167__$2);
}));

/**
 * (for [[index item first? last?] (enumerate coll)] ...)
 */
re_com.util.enumerate = (function re_com$util$enumerate(coll){
var c = (cljs.core.count(coll) - (1));
var f = (function (index,item){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [index,item,cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((0),index),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(c,index)], null);
});
return cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2(f,coll);
});
/**
 * Given a DOM node, I traverse through all ascendant nodes (until I reach body), summing any scrollLeft and scrollTop values
 * and return these sums in a map
 */
re_com.util.sum_scroll_offsets = (function re_com$util$sum_scroll_offsets(node){
var current_node = node.parentNode;
var sum_scroll_left = (0);
var sum_scroll_top = (0);
while(true){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(current_node.tagName,"BODY")){
var G__18356 = current_node.parentNode;
var G__18357 = (sum_scroll_left + current_node.scrollLeft);
var G__18358 = (sum_scroll_top + current_node.scrollTop);
current_node = G__18356;
sum_scroll_left = G__18357;
sum_scroll_top = G__18358;
continue;
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"left","left",-399115937),sum_scroll_left,new cljs.core.Keyword(null,"top","top",-1856271961),sum_scroll_top], null);
}
break;
}
});
/**
 * Return a goog.date.UtcDateTime based on local date/time.
 */
re_com.util.now__GT_utc = (function re_com$util$now__GT_utc(){
var local_date_time = (new goog.date.DateTime());
return (new goog.date.UtcDateTime(local_date_time.getYear(),local_date_time.getMonth(),local_date_time.getDate(),(0),(0),(0),(0)));
});
re_com.util.clipboard_write_BANG_ = (function re_com$util$clipboard_write_BANG_(s){
return navigator.clipboard.writeText(s);
});
re_com.util.tsv_line = (function re_com$util$tsv_line(row){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$2("\t",row))+"\n");
});
re_com.util.table__GT_tsv = (function re_com$util$table__GT_tsv(columns,rows){
var header_value_fn = cljs.core.some_fn.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"export-header-label","export-header-label",-121029282),new cljs.core.Keyword(null,"header-label","header-label",765876429),cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.name,new cljs.core.Keyword(null,"id","id",-1388402092)));
var row_value_fn = cljs.core.some_fn.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"row-export-fn","row-export-fn",566159751),new cljs.core.Keyword(null,"row-label-fn","row-label-fn",1457434308),new cljs.core.Keyword(null,"id","id",-1388402092));
var row__GT_cells = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.juxt,cljs.core.map.cljs$core$IFn$_invoke$arity$2(row_value_fn,columns));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,cljs.core.map.cljs$core$IFn$_invoke$arity$2(re_com.util.tsv_line,cljs.core.cons(cljs.core.map.cljs$core$IFn$_invoke$arity$2(header_value_fn,columns),cljs.core.map.cljs$core$IFn$_invoke$arity$2(row__GT_cells,rows))));
});
re_com.util.hiccup_QMARK_ = cljs.core.vector_QMARK_;
re_com.util.part = (function re_com$util$part(var_args){
var args__5882__auto__ = [];
var len__5876__auto___18367 = arguments.length;
var i__5877__auto___18368 = (0);
while(true){
if((i__5877__auto___18368 < len__5876__auto___18367)){
args__5882__auto__.push((arguments[i__5877__auto___18368]));

var G__18369 = (i__5877__auto___18368 + (1));
i__5877__auto___18368 = G__18369;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((2) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((2)),(0),null)):null);
return re_com.util.part.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5883__auto__);
});

(re_com.util.part.cljs$core$IFn$_invoke$arity$variadic = (function (x,props,p__18234){
var vec__18235 = p__18234;
var default$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__18235,(0),null);
if(cljs.core.truth_((re_com.util.hiccup_QMARK_.cljs$core$IFn$_invoke$arity$1 ? re_com.util.hiccup_QMARK_.cljs$core$IFn$_invoke$arity$1(x) : re_com.util.hiccup_QMARK_.call(null,x)))){
return x;
} else {
if(cljs.core.ifn_QMARK_(x)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [x,props], null);
} else {
if(cljs.core.truth_(default$)){
return re_com.util.part(default$,props);
} else {
return null;

}
}
}
}));

(re_com.util.part.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(re_com.util.part.cljs$lang$applyTo = (function (seq18223){
var G__18224 = cljs.core.first(seq18223);
var seq18223__$1 = cljs.core.next(seq18223);
var G__18225 = cljs.core.first(seq18223__$1);
var seq18223__$2 = cljs.core.next(seq18223__$1);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__18224,G__18225,seq18223__$2);
}));

re_com.util.reduce__GT_ = (function re_com$util$reduce__GT_(p1__18247_SHARP_,p2__18246_SHARP_,p3__18248_SHARP_){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(p2__18246_SHARP_,p1__18247_SHARP_,p3__18248_SHARP_);
});

//# sourceMappingURL=re_com.util.js.map
