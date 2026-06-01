goog.provide('day8.re_frame_10x.tools.identicon');
day8.re_frame_10x.tools.identicon.build_grid = (function day8$re_frame_10x$tools$identicon$build_grid(hash){
var grid_pattern = (hash & (1048575));
var grid = cljs.core.volatile_BANG_(cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2((25),false)));
var n__5741__auto___36116 = (15);
var i_36117 = (0);
while(true){
if((i_36117 < n__5741__auto___36116)){
if(((grid_pattern & ((1) << i_36117)) > (0))){
var row_36118 = cljs.core.quot(i_36117,(3));
var col_36119 = cljs.core.rem(i_36117,(3));
grid.cljs$core$IVolatile$_vreset_BANG_$arity$2(null,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(grid.cljs$core$IDeref$_deref$arity$1(null),((row_36118 * (5)) + col_36119),true));
} else {
}

var G__36120 = (i_36117 + (1));
i_36117 = G__36120;
continue;
} else {
}
break;
}

var n__5741__auto___36122 = (5);
var row_36123 = (0);
while(true){
if((row_36123 < n__5741__auto___36122)){
grid.cljs$core$IVolatile$_vreset_BANG_$arity$2(null,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(grid.cljs$core$IDeref$_deref$arity$1(null),((row_36123 * (5)) + (4)),cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(grid),((row_36123 * (5)) + (0)))));

grid.cljs$core$IVolatile$_vreset_BANG_$arity$2(null,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(grid.cljs$core$IDeref$_deref$arity$1(null),((row_36123 * (5)) + (3)),cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(grid),((row_36123 * (5)) + (1)))));

var G__36125 = (row_36123 + (1));
row_36123 = G__36125;
continue;
} else {
}
break;
}

return cljs.core.deref(grid);
});
day8.re_frame_10x.tools.identicon.foreground_color = (function day8$re_frame_10x$tools$identicon$foreground_color(hash){
var hue = cljs.core.mod((hash >> (20)),(360));
var saturation = (65);
var lightness = (40);
return (""+"hsl("+cljs.core.str.cljs$core$IFn$_invoke$arity$1(hue)+","+cljs.core.str.cljs$core$IFn$_invoke$arity$1(saturation)+"%,"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(lightness)+"%)");
});
/**
 * Generates identicon SVG data as Hiccup.
 * Takes a string value and an options map.
 * Options:
 *   :size       - width/height of the SVG (default 100)
 *   :background - vector [r g b a] 0-255 (default [240 240 240 255])
 *   :margin     - decimal percentage margin (default 0.08)
 */
day8.re_frame_10x.tools.identicon.svg = (function day8$re_frame_10x$tools$identicon$svg(var_args){
var args__5882__auto__ = [];
var len__5876__auto___36126 = arguments.length;
var i__5877__auto___36127 = (0);
while(true){
if((i__5877__auto___36127 < len__5876__auto___36126)){
args__5882__auto__.push((arguments[i__5877__auto___36127]));

var G__36128 = (i__5877__auto___36127 + (1));
i__5877__auto___36127 = G__36128;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return day8.re_frame_10x.tools.identicon.svg.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(day8.re_frame_10x.tools.identicon.svg.cljs$core$IFn$_invoke$arity$variadic = (function (value,p__36076){
var map__36081 = p__36076;
var map__36081__$1 = cljs.core.__destructure_map(map__36081);
var size = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__36081__$1,new cljs.core.Keyword(null,"size","size",1098693007),(14));
var background = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__36081__$1,new cljs.core.Keyword(null,"background","background",-863952629),"white");
var margin = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__36081__$1,new cljs.core.Keyword(null,"margin","margin",-995903681),(0));
if(clojure.string.blank_QMARK_(value)){
return null;
} else {
var hash = cljs.core.hash_string(value);
var fg_color = day8.re_frame_10x.tools.identicon.foreground_color(hash);
var grid = day8.re_frame_10x.tools.identicon.build_grid(hash);
var cell_size = (size / 5.0);
var base_margin = (size * margin);
var cell_margin = (base_margin / 2.0);
var final_size = (size + base_margin);
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"width","width",-384071477),final_size,new cljs.core.Keyword(null,"height","height",1025178622),final_size,new cljs.core.Keyword(null,"viewBox","viewBox",-469489477),(""+"0 0 "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(final_size)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(final_size)),new cljs.core.Keyword(null,"xmlns","xmlns",-1862095571),"http://www.w3.org/2000/svg",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"background","background",-863952629),background], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"text","text",-1790561697),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"x","x",2099068185),(0),new cljs.core.Keyword(null,"y","y",-1757859776),(0),new cljs.core.Keyword(null,"text-anchor","text-anchor",585613696),"middle",new cljs.core.Keyword(null,"alignment-baseline","alignment-baseline",-311060879),"middle",new cljs.core.Keyword(null,"fill","fill",883462889),"rgba(0,0,0,0)",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user-select","user-select",-346451650),new cljs.core.Keyword(null,"text","text",-1790561697)], null)], null),cljs.core.subs.cljs$core$IFn$_invoke$arity$2(value,(5))], null)], null),(function (){var iter__5628__auto__ = (function day8$re_frame_10x$tools$identicon$iter__36088(s__36089){
return (new cljs.core.LazySeq(null,(function (){
var s__36089__$1 = s__36089;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__36089__$1);
if(temp__5825__auto__){
var xs__6385__auto__ = temp__5825__auto__;
var row = cljs.core.first(xs__6385__auto__);
var iterys__5624__auto__ = ((function (s__36089__$1,row,xs__6385__auto__,temp__5825__auto__,hash,fg_color,grid,cell_size,base_margin,cell_margin,final_size,map__36081,map__36081__$1,size,background,margin){
return (function day8$re_frame_10x$tools$identicon$iter__36088_$_iter__36090(s__36091){
return (new cljs.core.LazySeq(null,((function (s__36089__$1,row,xs__6385__auto__,temp__5825__auto__,hash,fg_color,grid,cell_size,base_margin,cell_margin,final_size,map__36081,map__36081__$1,size,background,margin){
return (function (){
var s__36091__$1 = s__36091;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__36091__$1);
if(temp__5825__auto____$1){
var s__36091__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__36091__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__36091__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__36093 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__36092 = (0);
while(true){
if((i__36092 < size__5627__auto__)){
var col = cljs.core._nth(c__5626__auto__,i__36092);
var idx = ((row * (5)) + col);
if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(grid,idx))){
cljs.core.chunk_append(b__36093,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"rect","rect",-108902628),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"x","x",2099068185),(cell_margin + (col * cell_size)),new cljs.core.Keyword(null,"y","y",-1757859776),(cell_margin + (row * cell_size)),new cljs.core.Keyword(null,"width","width",-384071477),cell_size,new cljs.core.Keyword(null,"height","height",1025178622),cell_size,new cljs.core.Keyword(null,"fill","fill",883462889),fg_color], null)], null));

var G__36137 = (i__36092 + (1));
i__36092 = G__36137;
continue;
} else {
var G__36138 = (i__36092 + (1));
i__36092 = G__36138;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__36093),day8$re_frame_10x$tools$identicon$iter__36088_$_iter__36090(cljs.core.chunk_rest(s__36091__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__36093),null);
}
} else {
var col = cljs.core.first(s__36091__$2);
var idx = ((row * (5)) + col);
if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(grid,idx))){
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"rect","rect",-108902628),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"x","x",2099068185),(cell_margin + (col * cell_size)),new cljs.core.Keyword(null,"y","y",-1757859776),(cell_margin + (row * cell_size)),new cljs.core.Keyword(null,"width","width",-384071477),cell_size,new cljs.core.Keyword(null,"height","height",1025178622),cell_size,new cljs.core.Keyword(null,"fill","fill",883462889),fg_color], null)], null),day8$re_frame_10x$tools$identicon$iter__36088_$_iter__36090(cljs.core.rest(s__36091__$2)));
} else {
var G__36139 = cljs.core.rest(s__36091__$2);
s__36091__$1 = G__36139;
continue;
}
}
} else {
return null;
}
break;
}
});})(s__36089__$1,row,xs__6385__auto__,temp__5825__auto__,hash,fg_color,grid,cell_size,base_margin,cell_margin,final_size,map__36081,map__36081__$1,size,background,margin))
,null,null));
});})(s__36089__$1,row,xs__6385__auto__,temp__5825__auto__,hash,fg_color,grid,cell_size,base_margin,cell_margin,final_size,map__36081,map__36081__$1,size,background,margin))
;
var fs__5625__auto__ = cljs.core.seq(iterys__5624__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1((5))));
if(fs__5625__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5625__auto__,day8$re_frame_10x$tools$identicon$iter__36088(cljs.core.rest(s__36089__$1)));
} else {
var G__36140 = cljs.core.rest(s__36089__$1);
s__36089__$1 = G__36140;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1((5)));
})());
}
}));

(day8.re_frame_10x.tools.identicon.svg.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(day8.re_frame_10x.tools.identicon.svg.cljs$lang$applyTo = (function (seq36070){
var G__36072 = cljs.core.first(seq36070);
var seq36070__$1 = cljs.core.next(seq36070);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__36072,seq36070__$1);
}));


//# sourceMappingURL=day8.re_frame_10x.tools.identicon.js.map
