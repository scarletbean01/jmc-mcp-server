goog.provide('re_com.simple_v_table');
re_com.simple_v_table.default_sort_criterion = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"keyfn","keyfn",780060332),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"order","order",-1254677256),new cljs.core.Keyword(null,"asc","asc",356854569)], null);
re_com.simple_v_table.update_sort_criteria = (function re_com$simple_v_table$update_sort_criteria(criteria,new_criterion){
var map__22121 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([re_com.simple_v_table.default_sort_criterion,new_criterion], 0));
var map__22121__$1 = cljs.core.__destructure_map(map__22121);
var new_criterion__$1 = map__22121__$1;
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22121__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22121__$1,new cljs.core.Keyword(null,"order","order",-1254677256));
var this_QMARK_ = cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.createAsIfByAssoc([id]),new cljs.core.Keyword(null,"id","id",-1388402092));
var this_criterion = re_com.util.item_for_id(id,criteria);
var operation = (((this_criterion == null))?new cljs.core.Keyword(null,"add","add",235287739):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(order,new cljs.core.Keyword(null,"order","order",-1254677256).cljs$core$IFn$_invoke$arity$1(this_criterion)))?new cljs.core.Keyword(null,"flip","flip",2033871302):new cljs.core.Keyword(null,"drop","drop",364481611)
));
var flip = (function (p1__22119_SHARP_){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(p1__22119_SHARP_,new cljs.core.Keyword(null,"order","order",-1254677256),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"asc","asc",356854569),new cljs.core.Keyword(null,"desc","desc",2093485764),new cljs.core.Keyword(null,"desc","desc",2093485764),new cljs.core.Keyword(null,"asc","asc",356854569)], null));
});
var G__22126 = operation;
var G__22126__$1 = (((G__22126 instanceof cljs.core.Keyword))?G__22126.fqn:null);
switch (G__22126__$1) {
case "flip":
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__22120_SHARP_){
var G__22131 = p1__22120_SHARP_;
if(cljs.core.truth_(this_QMARK_(p1__22120_SHARP_))){
return flip(G__22131);
} else {
return G__22131;
}
}),criteria);

break;
case "drop":
return re_com.util.remove_id_item(id,criteria);

break;
case "add":
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(criteria,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([re_com.simple_v_table.default_sort_criterion,new_criterion__$1], 0))));

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__22126__$1))));

}
});
re_com.simple_v_table.sort_icon = (function re_com$simple_v_table$sort_icon(p__22148){
var map__22150 = p__22148;
var map__22150__$1 = cljs.core.__destructure_map(map__22150);
var size = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22150__$1,new cljs.core.Keyword(null,"size","size",1098693007),"24px");
var fill = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22150__$1,new cljs.core.Keyword(null,"fill","fill",883462889),"black");
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"width","width",-384071477),size,new cljs.core.Keyword(null,"height","height",1025178622),size,new cljs.core.Keyword(null,"viewBox","viewBox",-469489477),"0 0 24 24"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"path","path",-188191168),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fill","fill",883462889),fill,new cljs.core.Keyword(null,"d","d",1972142424),"M3 18h6v-2H3v2zM3 6v2h18V6H3zm0 7h12v-2H3v2z"], null)], null)], null);
});
re_com.simple_v_table.arrow_down_icon = (function re_com$simple_v_table$arrow_down_icon(p__22173){
var map__22178 = p__22173;
var map__22178__$1 = cljs.core.__destructure_map(map__22178);
var size = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22178__$1,new cljs.core.Keyword(null,"size","size",1098693007),"24px");
var fill = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22178__$1,new cljs.core.Keyword(null,"fill","fill",883462889),"black");
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"width","width",-384071477),size,new cljs.core.Keyword(null,"height","height",1025178622),size,new cljs.core.Keyword(null,"viewBox","viewBox",-469489477),"0 0 24 24"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"path","path",-188191168),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fill","fill",883462889),fill,new cljs.core.Keyword(null,"d","d",1972142424),"M7 10l5 5 5-5H7z"], null)], null)], null);
});
re_com.simple_v_table.arrow_up_icon = (function re_com$simple_v_table$arrow_up_icon(p__22199){
var map__22200 = p__22199;
var map__22200__$1 = cljs.core.__destructure_map(map__22200);
var size = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22200__$1,new cljs.core.Keyword(null,"size","size",1098693007),"24px");
var fill = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22200__$1,new cljs.core.Keyword(null,"fill","fill",883462889),"black");
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"width","width",-384071477),size,new cljs.core.Keyword(null,"height","height",1025178622),size,new cljs.core.Keyword(null,"viewBox","viewBox",-469489477),"0 0 24 24"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"path","path",-188191168),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fill","fill",883462889),fill,new cljs.core.Keyword(null,"d","d",1972142424),"M7 14l5-5 5 5H7z"], null)], null)], null);
});
re_com.simple_v_table.align__GT_justify = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"left","left",-399115937),new cljs.core.Keyword(null,"start","start",-355208981),new cljs.core.Keyword(null,"right","right",-452581833),new cljs.core.Keyword(null,"end","end",-268185958),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"center","center",-748944368)], null);
re_com.simple_v_table.column_header_item = (function re_com$simple_v_table$column_header_item(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22532 = arguments.length;
var i__5877__auto___22533 = (0);
while(true){
if((i__5877__auto___22533 < len__5876__auto___22532)){
args__5882__auto__.push((arguments[i__5877__auto___22533]));

var G__22534 = (i__5877__auto___22533 + (1));
i__5877__auto___22533 = G__22534;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.simple_v_table.column_header_item.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.simple_v_table.column_header_item.cljs$core$IFn$_invoke$arity$variadic = (function (_){
return (function (p__22232){
var map__22235 = p__22232;
var map__22235__$1 = cljs.core.__destructure_map(map__22235);
var map__22237 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22235__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var map__22237__$1 = cljs.core.__destructure_map(map__22237);
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22237__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var row_label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22237__$1,new cljs.core.Keyword(null,"row-label-fn","row-label-fn",1457434308));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22237__$1,new cljs.core.Keyword(null,"width","width",-384071477));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22237__$1,new cljs.core.Keyword(null,"height","height",1025178622));
var align = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22237__$1,new cljs.core.Keyword(null,"align","align",1964212802));
var header_label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22237__$1,new cljs.core.Keyword(null,"header-label","header-label",765876429));
var sort_by = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22237__$1,new cljs.core.Keyword(null,"sort-by","sort-by",-322599303));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22235__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var sort_by_column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22235__$1,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302));
var hover_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22235__$1,new cljs.core.Keyword(null,"hover?","hover?",-1201331489));
var column_header_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22235__$1,new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558));
var header_label__$1 = (function (){var or__5142__auto__ = header_label;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.name(id);
}
})();
var sort_by__$1 = ((sort_by === true)?cljs.core.PersistentArrayMap.EMPTY:sort_by
);
var default_sort_by = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"key-fn","key-fn",-636154479),row_label_fn,new cljs.core.Keyword(null,"comp","comp",1191953630),cljs.core.compare,new cljs.core.Keyword(null,"id","id",-1388402092),id,new cljs.core.Keyword(null,"order","order",-1254677256),new cljs.core.Keyword(null,"asc","asc",356854569)], null);
var ps = re_com.util.position_for_id(id,cljs.core.deref(sort_by_column));
var map__22241 = re_com.util.item_for_id(id,cljs.core.deref(sort_by_column));
var map__22241__$1 = cljs.core.__destructure_map(map__22241);
var current_order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22241__$1,new cljs.core.Keyword(null,"order","order",-1254677256));
var add_criteria_BANG_ = (function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(sort_by_column,re_com.simple_v_table.update_sort_criteria,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([default_sort_by,sort_by__$1], 0)));
});
var replace_criteria_BANG_ = (function (){
return cljs.core.reset_BANG_(sort_by_column,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([default_sort_by,sort_by__$1], 0))], null));
});
var on_click = (function (p1__22204_SHARP_){
if(cljs.core.truth_((function (){var or__5142__auto__ = p1__22204_SHARP_.shiftKey;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.empty_QMARK_(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.createAsIfByAssoc([id]),new cljs.core.Keyword(null,"id","id",-1388402092)),cljs.core.deref(sort_by_column)));
}
})())){
return add_criteria_BANG_();
} else {
return replace_criteria_BANG_();
}
});
var justify = cljs.core.get.cljs$core$IFn$_invoke$arity$3(re_com.simple_v_table.align__GT_justify,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(align),new cljs.core.Keyword(null,"start","start",-355208981));
var multiple_columns_sorted_QMARK_ = (cljs.core.count(cljs.core.deref(sort_by_column)) > (1));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.v_box,new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 15, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-column-header-item "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header-item","simple-column-header-item",-1259129102),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"width","width",-384071477),re_com.util.px(width),new cljs.core.Keyword(null,"justify","justify",-722524056),justify,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"padding","padding",1660304693),"0px 12px",new cljs.core.Keyword(null,"min-height","min-height",398480837),"24px",new cljs.core.Keyword(null,"height","height",1025178622),re_com.util.px(height),new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"bold",new cljs.core.Keyword(null,"white-space","white-space",-707351930),"nowrap",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden",new cljs.core.Keyword(null,"text-overflow","text-overflow",-1022366814),"ellipsis"], null),(cljs.core.truth_(sort_by__$1)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cursor","cursor",1011937484),"pointer"], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header-item","simple-column-header-item",-1259129102),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_(sort_by__$1)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),on_click], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header-item","simple-column-header-item",-1259129102),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [header_label__$1,(cljs.core.truth_(sort_by__$1)?new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-column-header-sort-label "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(current_order)?"rc-simple-v-table-column-header-sort-active":null))),new cljs.core.Keyword(null,"min-width","min-width",1926193728),"35px",new cljs.core.Keyword(null,"style","style",-496642736),(cljs.core.truth_(current_order)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"opacity","opacity",397153780),0.3], null):null),new cljs.core.Keyword(null,"justify","justify",-722524056),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),((cljs.core.not((function (){var or__5142__auto__ = hover_QMARK_;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return current_order;
}
})()))?cljs.core.PersistentVector.EMPTY:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__22274 = current_order;
var G__22274__$1 = (((G__22274 instanceof cljs.core.Keyword))?G__22274.fqn:null);
switch (G__22274__$1) {
case "asc":
return re_com.simple_v_table.arrow_up_icon;

break;
case "desc":
return re_com.simple_v_table.arrow_down_icon;

break;
default:
return re_com.simple_v_table.sort_icon;

}
})(),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"size","size",1098693007),(function (){var or__5142__auto__ = height;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "16px";
}
})(),new cljs.core.Keyword(null,"fill","fill",883462889),"#777"], null)], null),(cljs.core.truth_(ps)?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.text.label,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"visibility","visibility",1338380893),((multiple_columns_sorted_QMARK_)?null:"hidden")], null),new cljs.core.Keyword(null,"label","label",1718410804),(ps + (1))], null):null)], null))], null):null)], null)], null)], null)], null);
});
}));

(re_com.simple_v_table.column_header_item.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.simple_v_table.column_header_item.cljs$lang$applyTo = (function (seq22209){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq22209));
}));

/**
 * :column-header-renderer AND :top-left-renderer - Render the table header
 */
re_com.simple_v_table.column_header_renderer = (function re_com$simple_v_table$column_header_renderer(p__22286){
var map__22287 = p__22286;
var map__22287__$1 = cljs.core.__destructure_map(map__22287);
var columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22287__$1,new cljs.core.Keyword(null,"columns","columns",1998437288));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22287__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var sort_by_column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22287__$1,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302));
var column_header_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22287__$1,new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558));
var hover_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22287__$1,new cljs.core.Keyword(null,"hover?","hover?",-1201331489));
return new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-column-header noselect "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header","simple-column-header",-1241463404),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"padding","padding",1660304693),"4px 0px",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden",new cljs.core.Keyword(null,"white-space","white-space",-707351930),"nowrap"], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header","simple-column-header",-1241463404),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-mouse-enter","on-mouse-enter",-1664921661),(function (){
return cljs.core.reset_BANG_(hover_QMARK_,true);
}),new cljs.core.Keyword(null,"on-mouse-leave","on-mouse-leave",-1864319528),(function (){
return cljs.core.reset_BANG_(hover_QMARK_,false);
}),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (event){
re_com.v_table.show_row_data_on_alt_click(columns,(0),event);

return null;
})], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header","simple-column-header",-1241463404),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,(function (){var iter__5628__auto__ = (function re_com$simple_v_table$column_header_renderer_$_iter__22304(s__22305){
return (new cljs.core.LazySeq(null,(function (){
var s__22305__$1 = s__22305;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22305__$1);
if(temp__5825__auto__){
var s__22305__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__22305__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22305__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22307 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22306 = (0);
while(true){
if((i__22306 < size__5627__auto__)){
var column = cljs.core._nth(c__5626__auto__,i__22306);
cljs.core.chunk_append(b__22307,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.column_header_item,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),column_header_height,new cljs.core.Keyword(null,"column","column",2078222095),column,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302),sort_by_column,new cljs.core.Keyword(null,"hover?","hover?",-1201331489),hover_QMARK_], null)], null));

var G__22536 = (i__22306 + (1));
i__22306 = G__22536;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22307),re_com$simple_v_table$column_header_renderer_$_iter__22304(cljs.core.chunk_rest(s__22305__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22307),null);
}
} else {
var column = cljs.core.first(s__22305__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.column_header_item,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),column_header_height,new cljs.core.Keyword(null,"column","column",2078222095),column,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302),sort_by_column,new cljs.core.Keyword(null,"hover?","hover?",-1201331489),hover_QMARK_], null)], null),re_com$simple_v_table$column_header_renderer_$_iter__22304(cljs.core.rest(s__22305__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(columns);
})())], null);
});
/**
 * Render a single row item (column) of a single row
 */
re_com.simple_v_table.row_item = (function re_com$simple_v_table$row_item(row,p__22328,cell_style,parts){
var map__22329 = p__22328;
var map__22329__$1 = cljs.core.__destructure_map(map__22329);
var column = map__22329__$1;
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22329__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22329__$1,new cljs.core.Keyword(null,"width","width",-384071477));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22329__$1,new cljs.core.Keyword(null,"height","height",1025178622));
var align = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22329__$1,new cljs.core.Keyword(null,"align","align",1964212802));
var vertical_align = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22329__$1,new cljs.core.Keyword(null,"vertical-align","vertical-align",651007333));
var row_label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22329__$1,new cljs.core.Keyword(null,"row-label-fn","row-label-fn",1457434308));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-row-item "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-row-item","simple-row-item",1270085292),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"text-overflow","text-overflow",-1022366814),new cljs.core.Keyword(null,"text-align","text-align",1786091845),new cljs.core.Keyword(null,"vertical-align","vertical-align",651007333),new cljs.core.Keyword(null,"white-space","white-space",-707351930),new cljs.core.Keyword(null,"overflow","overflow",2058931880),new cljs.core.Keyword(null,"width","width",-384071477),new cljs.core.Keyword(null,"padding","padding",1660304693),new cljs.core.Keyword(null,"display","display",242065432),new cljs.core.Keyword(null,"height","height",1025178622)],["ellipsis",align,vertical_align,"nowrap","hidden",re_com.util.px(width),(""+"0px "+"12px"),"inline-block",re_com.util.px(height)]),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-row-item","simple-row-item",1270085292),new cljs.core.Keyword(null,"style","style",-496642736)], null)),((cljs.core.fn_QMARK_(cell_style))?(cell_style.cljs$core$IFn$_invoke$arity$2 ? cell_style.cljs$core$IFn$_invoke$arity$2(row,column) : cell_style.call(null,row,column)):cell_style)], 0))], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-row-item","simple-row-item",1270085292),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),(function (){var fexpr__22348 = (function (){var or__5142__auto__ = row_label_fn;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.str,id);
}
})();
return (fexpr__22348.cljs$core$IFn$_invoke$arity$1 ? fexpr__22348.cljs$core$IFn$_invoke$arity$1(row) : fexpr__22348.call(null,row));
})()], null);
});
/**
 * :row-renderer AND :row-header-renderer: Render a single row of the table data
 */
re_com.simple_v_table.row_renderer = (function re_com$simple_v_table$row_renderer(p__22359){
var map__22360 = p__22359;
var map__22360__$1 = cljs.core.__destructure_map(map__22360);
var cell_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"cell-style","cell-style",1087536089));
var striped_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"striped?","striped?",-797214979));
var row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"row","row",-570139521));
var on_leave_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"on-leave-row","on-leave-row",1361264390));
var row_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"row-index","row-index",-828710296));
var columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"columns","columns",1998437288));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var on_enter_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"on-enter-row","on-enter-row",-139996948));
var row_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"row-height","row-height",527360749));
var on_click_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"on-click-row","on-click-row",-383826673));
var table_row_line_color = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"table-row-line-color","table-row-line-color",680569135));
var row_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22360__$1,new cljs.core.Keyword(null,"row-style","row-style",1352472052));
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-row "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-row","simple-row",-252554766),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"padding","padding",1660304693),"4px 0px",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden",new cljs.core.Keyword(null,"white-space","white-space",-707351930),"nowrap",new cljs.core.Keyword(null,"height","height",1025178622),re_com.util.px(row_height),new cljs.core.Keyword(null,"border-top","border-top",-158897573),(""+"1px solid "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(table_row_line_color)),new cljs.core.Keyword(null,"cursor","cursor",1011937484),(cljs.core.truth_(on_click_row)?"pointer":null)], null),(cljs.core.truth_((function (){var and__5140__auto__ = striped_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.odd_QMARK_(row_index);
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"#f2f2f2"], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-row","simple-row",-252554766),new cljs.core.Keyword(null,"style","style",-496642736)], null)),((cljs.core.fn_QMARK_(row_style))?(row_style.cljs$core$IFn$_invoke$arity$1 ? row_style.cljs$core$IFn$_invoke$arity$1(row) : row_style.call(null,row)):row_style)], 0)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (event){
re_com.v_table.show_row_data_on_alt_click(row,row_index,event);

if(cljs.core.truth_(on_click_row)){
(on_click_row.cljs$core$IFn$_invoke$arity$1 ? on_click_row.cljs$core$IFn$_invoke$arity$1(row_index) : on_click_row.call(null,row_index));
} else {
}

return null;
}),new cljs.core.Keyword(null,"on-mouse-enter","on-mouse-enter",-1664921661),(cljs.core.truth_(on_enter_row)?(function (event){
(on_enter_row.cljs$core$IFn$_invoke$arity$1 ? on_enter_row.cljs$core$IFn$_invoke$arity$1(row_index) : on_enter_row.call(null,row_index));

return null;
}):null),new cljs.core.Keyword(null,"on-mouse-leave","on-mouse-leave",-1864319528),(cljs.core.truth_(on_leave_row)?(function (event){
(on_leave_row.cljs$core$IFn$_invoke$arity$1 ? on_leave_row.cljs$core$IFn$_invoke$arity$1(row_index) : on_leave_row.call(null,row_index));

return null;
}):null)], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-row","simple-row",-252554766),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0))], null),(function (){var iter__5628__auto__ = (function re_com$simple_v_table$row_renderer_$_iter__22371(s__22372){
return (new cljs.core.LazySeq(null,(function (){
var s__22372__$1 = s__22372;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22372__$1);
if(temp__5825__auto__){
var s__22372__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__22372__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22372__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22374 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22373 = (0);
while(true){
if((i__22373 < size__5627__auto__)){
var column = cljs.core._nth(c__5626__auto__,i__22373);
cljs.core.chunk_append(b__22374,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.row_item,row,column,cell_style,parts], null));

var G__22537 = (i__22373 + (1));
i__22373 = G__22537;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22374),re_com$simple_v_table$row_renderer_$_iter__22371(cljs.core.chunk_rest(s__22372__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22374),null);
}
} else {
var column = cljs.core.first(s__22372__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.row_item,row,column,cell_style,parts], null),re_com$simple_v_table$row_renderer_$_iter__22371(cljs.core.rest(s__22372__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(columns);
})());
});
re_com.simple_v_table.simple_v_table_exclusive_parts_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"simple-wrapper","simple-wrapper",-507652041),new cljs.core.Keyword(null,"level","level",1290497552),(0),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-simple-v-table-wrapper",new cljs.core.Keyword(null,"impl","impl",1677848700),"[simple-v-table]",new cljs.core.Keyword(null,"notes","notes",-1039600523),"Outer container of the simple-v-table"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"simple-column-header","simple-column-header",-1241463404),new cljs.core.Keyword(null,"level","level",1290497552),(5),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-simple-v-table-column-header",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]",new cljs.core.Keyword(null,"notes","notes",-1039600523),"Simple-v-table's container for column headers (placed under v-table's :column-header-content/:top-left)"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"simple-column-header-item","simple-column-header-item",-1259129102),new cljs.core.Keyword(null,"level","level",1290497552),(6),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-simple-v-table-column-header-item",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]",new cljs.core.Keyword(null,"notes","notes",-1039600523),"Individual column header item/cell components"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"simple-row","simple-row",-252554766),new cljs.core.Keyword(null,"level","level",1290497552),(5),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-simple-v-table-row",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]",new cljs.core.Keyword(null,"notes","notes",-1039600523),"Simple-v-table's container for rows (placed under v-table's :row-content/:row-header-content)"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"simple-row-item","simple-row-item",1270085292),new cljs.core.Keyword(null,"level","level",1290497552),(6),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-simple-v-table-row-item",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]",new cljs.core.Keyword(null,"notes","notes",-1039600523),"Individual row item/cell components"], null)], null):null);
re_com.simple_v_table.simple_v_table_exclusive_parts = ((re_com.config.include_args_desc_QMARK_)?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),re_com.simple_v_table.simple_v_table_exclusive_parts_desc)):null);
re_com.simple_v_table.simple_v_table_parts_desc = ((re_com.config.include_args_desc_QMARK_)?cljs.core.into.cljs$core$IFn$_invoke$arity$2(re_com.simple_v_table.simple_v_table_exclusive_parts_desc,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__22395_SHARP_){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(p1__22395_SHARP_,new cljs.core.Keyword(null,"level","level",1290497552),cljs.core.inc);
}),re_com.v_table.v_table_parts_desc)):null);
re_com.simple_v_table.simple_v_table_parts = ((re_com.config.include_args_desc_QMARK_)?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),re_com.simple_v_table.simple_v_table_parts_desc)):null);
re_com.simple_v_table.simple_v_table_args_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 24, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"model","model",331153215),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"r/atom containing vec of maps",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.vector_atom_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"one element for each row in the table."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"columns","columns",1998437288),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"vector of maps",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.vector_of_maps_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 28, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"one element for each column in the table. Must contain ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":id"], null),",",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":header-label"], null),",",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-label-fn"], null),",",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":width"], null),", and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":height"], null),". Optionally contains ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":sort-by"], null),", ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":align"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":vertical-align"], null),". ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":sort-by"], null)," can be ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"true"], null)," or a map optionally containing ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":key-fn"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":comp"], null)," ala ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"cljs.core/sort-by"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"fixed-column-count","fixed-column-count",721535030),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),(0),new cljs.core.Keyword(null,"type","type",1174270348),"integer",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"the number of fixed (non-scrolling) columns on the left."], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"fixed-column-border-color","fixed-column-border-color",2036669142),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),"#BBBEC0",new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The CSS color of the horizontal border between the fixed columns on the left, and the other columns on the right. ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":fixed-column-count"], null)," must be > 0 to be visible."], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),(31),new cljs.core.Keyword(null,"type","type",1174270348),"integer",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"px height of the column header section. Typically, equals ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-height"], null)," * number-of-column-header-rows."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"column-header-renderer","column-header-renderer",-1886265578),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"cols parts sort-by-col -> hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"You can provide a renderer function to override the inbuilt renderer for the columns headings"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"When non-nil, adds a hiccup of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":export-button-render"], null)," to the component tree."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-export","on-export",1803619391),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"columns, sorted-rows -> nil",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"Called whenever the export button is clicked."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"export-button-renderer","export-button-renderer",1141971581),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"{:keys [columns rows on-export]} -> hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Pass a component function to override the built-in export button. Declares a hiccup of your component in the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":top-right-renderer"], null),"position of the underlying ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"v-table"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"standard CSS max-width setting of the entire table. Literally constrains the table to the given width so that if the table is wider than this it will add scrollbars. Ignored if value is larger than the combined width of all the columns and table padding."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-rows","max-rows",-2131113613),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"integer",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"The maximum number of rows to display in the table without scrolling. If not provided will take up all available vertical space."], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"row-height","row-height",527360749),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),(31),new cljs.core.Keyword(null,"type","type",1174270348),"integer",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"px height of each row."], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"table-padding","table-padding",-702873839),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),(19),new cljs.core.Keyword(null,"type","type",1174270348),"integer",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"Padding in pixels for the entire table."], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"table-row-line-color","table-row-line-color",680569135),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),"#EAEEF1",new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"The CSS color of the lines between rows."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-click-row","on-click-row",-383826673),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"function",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"This function is called when the user clicks a row. Called with the row index. Do not use for adjusting row styles, use styling instead."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-enter-row","on-enter-row",-139996948),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"function",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"This function is called when the user's mouse pointer enters a row. Called with the row index. Do not use for adjusting row styles, use styling instead."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-leave-row","on-leave-row",1361264390),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"function",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"This function is called when the user's mouse pointer leaves a row. Called with the row index. Do not use for adjusting row styles, use styling instead."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"striped?","striped?",-797214979),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"when true, adds zebra-striping to the table's rows."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"row-style","row-style",1352472052),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map | function",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),(function (p1__22403_SHARP_){
return ((cljs.core.fn_QMARK_(p1__22403_SHARP_)) || (cljs.core.map_QMARK_(p1__22403_SHARP_)));
}),new cljs.core.Keyword(null,"description","description",-1428560544),"Style each row container either statically by passing a CSS map or dynamically by passing a function which receives the data for that row."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"cell-style","cell-style",1087536089),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map | function",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),(function (p1__22404_SHARP_){
return ((cljs.core.fn_QMARK_(p1__22404_SHARP_)) || (cljs.core.map_QMARK_(p1__22404_SHARP_)));
}),new cljs.core.Keyword(null,"description","description",-1428560544),"Style each cell in a row either statically by passing a CSS map or dynamically by passing a function which receives the data for that row and the cell definition from the columns arg."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"CSS class names, space separated (applies to the outer container)."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"parts","parts",849007691),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.parts_QMARK_(re_com.simple_v_table.simple_v_table_parts),new cljs.core.Keyword(null,"description","description",-1428560544),"See Parts section below."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"src","src",-1651076051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Used in dev builds to assist with debugging. Source code coordinates map containing keys",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":file"], null),"and",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":line"], null),". See 'Debugging'."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"debug-as","debug-as",283322354),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Used in dev builds to assist with debugging, when one component is used implement another component, and we want the implementation component to masquerade as the original component in debug output, such as component stacks. A map optionally containing keys",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":component"], null),"and",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":args"], null),"."], null)], null)], null):null);
re_com.simple_v_table.criteria_compare = (function re_com$simple_v_table$criteria_compare(a,b,p__22410){
var map__22411 = p__22410;
var map__22411__$1 = cljs.core.__destructure_map(map__22411);
var key_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22411__$1,new cljs.core.Keyword(null,"key-fn","key-fn",-636154479),new cljs.core.Keyword(null,"label","label",1718410804));
var comp_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22411__$1,new cljs.core.Keyword(null,"comp-fn","comp-fn",1661028128),cljs.core.compare);
var order = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22411__$1,new cljs.core.Keyword(null,"order","order",-1254677256),new cljs.core.Keyword(null,"asc","asc",356854569));
var G__22412 = (function (){var G__22413 = (key_fn.cljs$core$IFn$_invoke$arity$1 ? key_fn.cljs$core$IFn$_invoke$arity$1(a) : key_fn.call(null,a));
var G__22414 = (key_fn.cljs$core$IFn$_invoke$arity$1 ? key_fn.cljs$core$IFn$_invoke$arity$1(b) : key_fn.call(null,b));
return (comp_fn.cljs$core$IFn$_invoke$arity$2 ? comp_fn.cljs$core$IFn$_invoke$arity$2(G__22413,G__22414) : comp_fn.call(null,G__22413,G__22414));
})();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"desc","desc",2093485764),order)){
return (- G__22412);
} else {
return G__22412;
}
});
re_com.simple_v_table.multi_comparator = (function re_com$simple_v_table$multi_comparator(criteria){
return (function (a,b){
var or__5142__auto__ = cljs.core.first(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.zero_QMARK_,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$3(re_com.simple_v_table.criteria_compare,a,b),criteria)));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (0);
}
});
});
re_com.simple_v_table.clipboard_export_button = (function re_com$simple_v_table$clipboard_export_button(p__22429){
var map__22431 = p__22429;
var map__22431__$1 = cljs.core.__destructure_map(map__22431);
var columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22431__$1,new cljs.core.Keyword(null,"columns","columns",1998437288));
var rows = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22431__$1,new cljs.core.Keyword(null,"rows","rows",850049680));
var on_export = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22431__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391));
return new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.buttons.row_button,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/simple_v_table.cljs",new cljs.core.Keyword(null,"line","line",212345235),250], null)),new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863),"zmdi zmdi-copy",new cljs.core.Keyword(null,"mouse-over-row?","mouse-over-row?",-446703882),true,new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),(""+"Copy "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(rows))+" rows, "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(columns))+" columns to clipboard."),new cljs.core.Keyword(null,"on-click","on-click",1632826543),on_export], null);
});
re_com.simple_v_table.default_args = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113),new cljs.core.Keyword(null,"row-height","row-height",527360749),new cljs.core.Keyword(null,"table-row-line-color","table-row-line-color",680569135),new cljs.core.Keyword(null,"table-padding","table-padding",-702873839),new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),new cljs.core.Keyword(null,"fixed-column-count","fixed-column-count",721535030),new cljs.core.Keyword(null,"fixed-column-border-color","fixed-column-border-color",2036669142),new cljs.core.Keyword(null,"column-header-renderer","column-header-renderer",-1886265578),new cljs.core.Keyword(null,"export-button-renderer","export-button-renderer",1141971581),new cljs.core.Keyword(null,"on-export","on-export",1803619391)],[false,(31),"#EAEEF1",(19),(31),(0),"#BBBEC0",re_com.simple_v_table.column_header_renderer,re_com.simple_v_table.clipboard_export_button,(function (p__22447){
var map__22448 = p__22447;
var map__22448__$1 = cljs.core.__destructure_map(map__22448);
var columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22448__$1,new cljs.core.Keyword(null,"columns","columns",1998437288));
var rows = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22448__$1,new cljs.core.Keyword(null,"rows","rows",850049680));
return re_com.util.clipboard_write_BANG_(re_com.util.table__GT_tsv(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.false_QMARK_,new cljs.core.Keyword(null,"export?","export?",534848234)),columns),rows));
})]);
/**
 * Render a v-table and introduce the concept of columns (provide a spec for each).
 *   Of the nine possible sections of v-table, this table only supports four:
 *   top-left (1), row-headers (2), col-headers (4) and rows (5)
 *   Note that row-style and cell-style can either be a style map or functions which return a style map:
 * - (row-style row)
 * - (cell-style row col)
 *   where row is the data for that row and col is the definition map for that column
 *   
 */
re_com.simple_v_table.simple_v_table = (function re_com$simple_v_table$simple_v_table(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22541 = arguments.length;
var i__5877__auto___22542 = (0);
while(true){
if((i__5877__auto___22542 < len__5876__auto___22541)){
args__5882__auto__.push((arguments[i__5877__auto___22542]));

var G__22543 = (i__5877__auto___22542 + (1));
i__5877__auto___22542 = G__22543;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.simple_v_table.simple_v_table.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.simple_v_table.simple_v_table.cljs$core$IFn$_invoke$arity$variadic = (function (p__22457){
var map__22459 = p__22457;
var map__22459__$1 = cljs.core.__destructure_map(map__22459);
var static_args = map__22459__$1;
var src = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22459__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var or__5142__auto__ = (((!(goog.DEBUG)))?null:re_com.validate.validate_args(re_com.validate.extract_arg_data(re_com.simple_v_table.simple_v_table_args_desc),static_args));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var sort_by_column = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var header_hover_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (function() { 
var re_com$simple_v_table$simple_v_table_render__delegate = function (p__22460){
var map__22461 = p__22460;
var map__22461__$1 = cljs.core.__destructure_map(map__22461);
var dynamic_args = map__22461__$1;
var or__5142__auto____$1 = (((!(goog.DEBUG)))?null:re_com.validate.validate_args(re_com.validate.extract_arg_data(re_com.simple_v_table.simple_v_table_args_desc),dynamic_args));
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
var map__22462 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([re_com.simple_v_table.default_args,dynamic_args], 0));
var map__22462__$1 = cljs.core.__destructure_map(map__22462);
var args = map__22462__$1;
var export_button_renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"export-button-renderer","export-button-renderer",1141971581));
var striped_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"striped?","striped?",-797214979));
var model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"model","model",331153215));
var on_export = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391));
var on_leave_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"on-leave-row","on-leave-row",1361264390));
var columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"columns","columns",1998437288));
var show_export_button_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var on_enter_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"on-enter-row","on-enter-row",-139996948));
var row_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"row-height","row-height",527360749));
var max_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"max-width","max-width",-1939924051));
var src__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var on_click_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"on-click-row","on-click-row",-383826673));
var table_row_line_color = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"table-row-line-color","table-row-line-color",680569135));
var table_padding = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"table-padding","table-padding",-702873839));
var column_header_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558));
var debug_as = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"debug-as","debug-as",283322354));
var max_rows = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"max-rows","max-rows",-2131113613));
var row_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"row-style","row-style",1352472052));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var fixed_column_border_color = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"fixed-column-border-color","fixed-column-border-color",2036669142));
var column_header_renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"column-header-renderer","column-header-renderer",-1886265578));
var fixed_column_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22462__$1,new cljs.core.Keyword(null,"fixed-column-count","fixed-column-count",721535030));
var fcc_bounded = cljs.core.min.cljs$core$IFn$_invoke$arity$2(fixed_column_count,cljs.core.count(columns));
var fixed_cols = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(columns,(0),fcc_bounded);
var content_cols = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(columns,fcc_bounded,cljs.core.count(columns));
var fixed_content_width = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._PLUS_,(0),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__22452_SHARP_){
return new cljs.core.Keyword(null,"width","width",-384071477).cljs$core$IFn$_invoke$arity$2(p1__22452_SHARP_,(25));
}),fixed_cols));
var content_width = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._PLUS_,(0),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__22453_SHARP_){
return new cljs.core.Keyword(null,"width","width",-384071477).cljs$core$IFn$_invoke$arity$2(p1__22453_SHARP_,(25));
}),content_cols));
var table_border_style = (""+"1px solid "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(table_row_line_color));
var fixed_col_border_style = (""+"1px solid "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fixed_column_border_color));
var actual_table_width = (((((fixed_content_width + (((fixed_column_count > (0)))?(1):null)) + content_width) + re_com.v_table.scrollbar_tot_thick) + ((2) * table_padding)) + (2));
return new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),src__$1,new cljs.core.Keyword(null,"debug-as","debug-as",283322354),(function (){var or__5142__auto____$2 = debug_as;
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component","component",1555936782),re_com.debug.short_component_name(reagent.impl.component.component_name(reagent.core.current_component())),new cljs.core.Keyword(null,"args","args",1315556576),args], null);
}
})(),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-wrapper "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-wrapper","simple-wrapper",-507652041),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"flex","flex",-1425124628),(cljs.core.truth_(max_rows)?"0 1 auto":"100%"),new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"padding","padding",1660304693),re_com.util.px(table_padding),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),(function (){var or__5142__auto____$2 = max_width;
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return re_com.util.px(actual_table_width);
}
})(),new cljs.core.Keyword(null,"border","border",1444987323),table_border_style,new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"3px"], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-wrapper","simple-wrapper",-507652041),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-wrapper","simple-wrapper",-507652041),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)),new cljs.core.Keyword(null,"child","child",623967545),new cljs.core.PersistentVector(null, 29, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.v_table.v_table,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/simple_v_table.cljs",new cljs.core.Keyword(null,"line","line",212345235),326], null)),new cljs.core.Keyword(null,"model","model",331153215),model,new cljs.core.Keyword(null,"sort-comp","sort-comp",-667012852),re_com.simple_v_table.multi_comparator(re_com.util.__GT_v(cljs.core.deref(sort_by_column))),new cljs.core.Keyword(null,"column-header-renderer","column-header-renderer",-1886265578),(function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [column_header_renderer,cljs.core.into.cljs$core$IFn$_invoke$arity$2(args,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"columns","columns",1998437288),content_cols,new cljs.core.Keyword(null,"hover?","hover?",-1201331489),cljs.core.deref(header_hover_QMARK_),new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302),sort_by_column], null))], null);
}),new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),column_header_height,new cljs.core.Keyword(null,"row-header-renderer","row-header-renderer",-355094585),(function (){
return (function (i,row){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.row_renderer,cljs.core.into.cljs$core$IFn$_invoke$arity$2(args,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"columns","columns",1998437288),fixed_cols,new cljs.core.Keyword(null,"row","row",-570139521),row,new cljs.core.Keyword(null,"row-index","row-index",-828710296),i], null))], null);
});
}),new cljs.core.Keyword(null,"row-renderer","row-renderer",314053346),(function (){
return (function (i,row){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.row_renderer,cljs.core.into.cljs$core$IFn$_invoke$arity$2(args,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"columns","columns",1998437288),content_cols,new cljs.core.Keyword(null,"row","row",-570139521),row,new cljs.core.Keyword(null,"row-index","row-index",-828710296),i], null))], null);
});
}),new cljs.core.Keyword(null,"row-content-width","row-content-width",-1986261648),content_width,new cljs.core.Keyword(null,"row-height","row-height",527360749),row_height,new cljs.core.Keyword(null,"max-row-viewport-height","max-row-viewport-height",2061202688),(cljs.core.truth_(max_rows)?(max_rows * row_height):null),new cljs.core.Keyword(null,"top-left-renderer","top-left-renderer",2010514596),(function (i,row){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [column_header_renderer,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"columns","columns",1998437288),fixed_cols,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302),sort_by_column], null)], null);
}),new cljs.core.Keyword(null,"top-right-renderer","top-right-renderer",-1691262321),(cljs.core.truth_(show_export_button_QMARK_)?(function (){
var rows = re_com.util.deref_or_value(model);
var columns__$1 = re_com.util.deref_or_value(columns);
var sort_by_column__$1 = re_com.util.deref_or_value(sort_by_column);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [export_button_renderer,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"rows","rows",850049680),rows,new cljs.core.Keyword(null,"columns","columns",1998437288),columns__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391),(function (_){
var G__22480 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"columns","columns",1998437288),columns__$1,new cljs.core.Keyword(null,"rows","rows",850049680),(function (){var G__22481 = rows;
if(cljs.core.truth_(sort_by_column__$1)){
return cljs.core.sort.cljs$core$IFn$_invoke$arity$2(re_com.simple_v_table.multi_comparator(re_com.util.__GT_v(sort_by_column__$1)),G__22481);
} else {
return G__22481;
}
})()], null);
return (on_export.cljs$core$IFn$_invoke$arity$1 ? on_export.cljs$core$IFn$_invoke$arity$1(G__22480) : on_export.call(null,G__22480));
})], null)], null);
}):null),new cljs.core.Keyword(null,"class","class",-2030961996),class$,new cljs.core.Keyword(null,"parts","parts",849007691),(function (){var G__22482 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"13px",new cljs.core.Keyword(null,"cursor","cursor",1011937484),"default"], null)], null)], null);
var G__22482__$1 = (((fixed_column_count > (0)))?(function (){var G__22483 = G__22482;
var G__22484 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"top-left","top-left",-1396159636),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"border-right","border-right",-668932860),fixed_col_border_style], null)], null),new cljs.core.Keyword(null,"row-headers","row-headers",1790514903),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"border-right","border-right",-668932860),fixed_col_border_style], null)], null)], null);
return (re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2 ? re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2(G__22483,G__22484) : re_com.theme.merge_props.call(null,G__22483,G__22484));
})():G__22482);
var G__22485 = G__22482__$1;
var G__22486 = cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc,parts,re_com.simple_v_table.simple_v_table_exclusive_parts);
return (re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2 ? re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2(G__22485,G__22486) : re_com.theme.merge_props.call(null,G__22485,G__22486));

})()], null)], null);
}
};
var re_com$simple_v_table$simple_v_table_render = function (var_args){
var p__22460 = null;
if (arguments.length > 0) {
var G__22550__i = 0, G__22550__a = new Array(arguments.length -  0);
while (G__22550__i < G__22550__a.length) {G__22550__a[G__22550__i] = arguments[G__22550__i + 0]; ++G__22550__i;}
  p__22460 = new cljs.core.IndexedSeq(G__22550__a,0,null);
} 
return re_com$simple_v_table$simple_v_table_render__delegate.call(this,p__22460);};
re_com$simple_v_table$simple_v_table_render.cljs$lang$maxFixedArity = 0;
re_com$simple_v_table$simple_v_table_render.cljs$lang$applyTo = (function (arglist__22551){
var p__22460 = cljs.core.seq(arglist__22551);
return re_com$simple_v_table$simple_v_table_render__delegate(p__22460);
});
re_com$simple_v_table$simple_v_table_render.cljs$core$IFn$_invoke$arity$variadic = re_com$simple_v_table$simple_v_table_render__delegate;
return re_com$simple_v_table$simple_v_table_render;
})()
;
}
}));

(re_com.simple_v_table.simple_v_table.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.simple_v_table.simple_v_table.cljs$lang$applyTo = (function (seq22455){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq22455));
}));

re_com.simple_v_table.nested_column = (function re_com$simple_v_table$nested_column(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22552 = arguments.length;
var i__5877__auto___22553 = (0);
while(true){
if((i__5877__auto___22553 < len__5876__auto___22552)){
args__5882__auto__.push((arguments[i__5877__auto___22553]));

var G__22554 = (i__5877__auto___22553 + (1));
i__5877__auto___22553 = G__22554;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.simple_v_table.nested_column.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.simple_v_table.nested_column.cljs$core$IFn$_invoke$arity$variadic = (function (_){
return (function (p__22491){
var map__22492 = p__22491;
var map__22492__$1 = cljs.core.__destructure_map(map__22492);
var map__22493 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22492__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var map__22493__$1 = cljs.core.__destructure_map(map__22493);
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22493__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var row_label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22493__$1,new cljs.core.Keyword(null,"row-label-fn","row-label-fn",1457434308));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22493__$1,new cljs.core.Keyword(null,"width","width",-384071477));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22493__$1,new cljs.core.Keyword(null,"height","height",1025178622));
var align = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22493__$1,new cljs.core.Keyword(null,"align","align",1964212802));
var header_label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22493__$1,new cljs.core.Keyword(null,"header-label","header-label",765876429));
var sort_by = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22493__$1,new cljs.core.Keyword(null,"sort-by","sort-by",-322599303));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22492__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var sort_by_column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22492__$1,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302));
var hover_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22492__$1,new cljs.core.Keyword(null,"hover?","hover?",-1201331489));
var column_header_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22492__$1,new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558));
var rows = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22492__$1,new cljs.core.Keyword(null,"rows","rows",850049680));
var header_label__$1 = (function (){var or__5142__auto__ = header_label;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.name(id);
}
})();
var sort_by__$1 = ((sort_by === true)?cljs.core.PersistentArrayMap.EMPTY:sort_by
);
var default_sort_by = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"key-fn","key-fn",-636154479),row_label_fn,new cljs.core.Keyword(null,"comp","comp",1191953630),cljs.core.compare,new cljs.core.Keyword(null,"id","id",-1388402092),id,new cljs.core.Keyword(null,"order","order",-1254677256),new cljs.core.Keyword(null,"asc","asc",356854569)], null);
var ps = re_com.util.position_for_id(id,cljs.core.deref(sort_by_column));
var map__22494 = re_com.util.item_for_id(id,cljs.core.deref(sort_by_column));
var map__22494__$1 = cljs.core.__destructure_map(map__22494);
var current_order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22494__$1,new cljs.core.Keyword(null,"order","order",-1254677256));
var add_criteria_BANG_ = (function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(sort_by_column,re_com.simple_v_table.update_sort_criteria,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([default_sort_by,sort_by__$1], 0)));
});
var replace_criteria_BANG_ = (function (){
return cljs.core.reset_BANG_(sort_by_column,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([default_sort_by,sort_by__$1], 0))], null));
});
var on_click = (function (p1__22487_SHARP_){
if(cljs.core.truth_((function (){var or__5142__auto__ = p1__22487_SHARP_.shiftKey;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.empty_QMARK_(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.createAsIfByAssoc([id]),new cljs.core.Keyword(null,"id","id",-1388402092)),cljs.core.deref(sort_by_column)));
}
})())){
return add_criteria_BANG_();
} else {
return replace_criteria_BANG_();
}
});
var justify = cljs.core.get.cljs$core$IFn$_invoke$arity$3(re_com.simple_v_table.align__GT_justify,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(align),new cljs.core.Keyword(null,"start","start",-355208981));
var multiple_columns_sorted_QMARK_ = (cljs.core.count(cljs.core.deref(sort_by_column)) > (1));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.v_box,new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 15, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-column-header-item "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header-item","simple-column-header-item",-1259129102),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"width","width",-384071477),re_com.util.px(width),new cljs.core.Keyword(null,"justify","justify",-722524056),justify,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"padding","padding",1660304693),"0px 12px",new cljs.core.Keyword(null,"min-height","min-height",398480837),"24px",new cljs.core.Keyword(null,"height","height",1025178622),re_com.util.px(height),new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),"bold",new cljs.core.Keyword(null,"white-space","white-space",-707351930),"nowrap",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden",new cljs.core.Keyword(null,"text-overflow","text-overflow",-1022366814),"ellipsis"], null),(cljs.core.truth_(sort_by__$1)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cursor","cursor",1011937484),"pointer"], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header-item","simple-column-header-item",-1259129102),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_(sort_by__$1)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),on_click], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-column-header-item","simple-column-header-item",-1259129102),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [header_label__$1,(cljs.core.truth_(sort_by__$1)?new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-column-header-sort-label "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(current_order)?"rc-simple-v-table-column-header-sort-active":null))),new cljs.core.Keyword(null,"min-width","min-width",1926193728),"35px",new cljs.core.Keyword(null,"style","style",-496642736),(cljs.core.truth_(current_order)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"opacity","opacity",397153780),0.3], null):null),new cljs.core.Keyword(null,"justify","justify",-722524056),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),((cljs.core.not((function (){var or__5142__auto__ = hover_QMARK_;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return current_order;
}
})()))?cljs.core.PersistentVector.EMPTY:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__22495 = current_order;
var G__22495__$1 = (((G__22495 instanceof cljs.core.Keyword))?G__22495.fqn:null);
switch (G__22495__$1) {
case "asc":
return re_com.simple_v_table.arrow_up_icon;

break;
case "desc":
return re_com.simple_v_table.arrow_down_icon;

break;
default:
return re_com.simple_v_table.sort_icon;

}
})(),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"size","size",1098693007),(function (){var or__5142__auto__ = height;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "16px";
}
})(),new cljs.core.Keyword(null,"fill","fill",883462889),"#777"], null)], null),(cljs.core.truth_(ps)?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.text.label,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"visibility","visibility",1338380893),((multiple_columns_sorted_QMARK_)?null:"hidden")], null),new cljs.core.Keyword(null,"label","label",1718410804),(ps + (1))], null):null)], null))], null):null)], null)], null)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(rows))], null)], null))], null);
});
}));

(re_com.simple_v_table.nested_column.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.simple_v_table.nested_column.cljs$lang$applyTo = (function (seq22488){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq22488));
}));

re_com.simple_v_table.column_node = (function re_com$simple_v_table$column_node(p__22496){
var map__22497 = p__22496;
var map__22497__$1 = cljs.core.__destructure_map(map__22497);
var column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22497__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var column_model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22497__$1,new cljs.core.Keyword(null,"column-model","column-model",1553156630));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"HI"], null);
});
re_com.simple_v_table.descendant_QMARK_ = (function re_com$simple_v_table$descendant_QMARK_(group_a,group_b){
return ((cljs.core.empty_QMARK_(group_a)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(group_a,cljs.core.vec(cljs.core.take.cljs$core$IFn$_invoke$arity$2(cljs.core.count(group_a),group_b)))));
});
/**
 * :column-header-renderer AND :top-left-renderer - Render the table header
 */
re_com.simple_v_table.nested_columns = (function re_com$simple_v_table$nested_columns(p__22501){
var map__22502 = p__22501;
var map__22502__$1 = cljs.core.__destructure_map(map__22502);
var args = map__22502__$1;
var columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22502__$1,new cljs.core.Keyword(null,"columns","columns",1998437288));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22502__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var sort_by_column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22502__$1,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302));
var column_header_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22502__$1,new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558));
var hover_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22502__$1,new cljs.core.Keyword(null,"hover?","hover?",-1201331489));
var column_model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22502__$1,new cljs.core.Keyword(null,"column-model","column-model",1553156630));
var ancestry = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22502__$1,new cljs.core.Keyword(null,"ancestry","ancestry",-2066754950));
var level = cljs.core.count(ancestry);
var vec__22503 = cljs.core.juxt.cljs$core$IFn$_invoke$arity$2((function (p1__22498_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(p1__22498_SHARP_,level);
}),(function (p1__22499_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(p1__22499_SHARP_,(level + (1)));
}))(cljs.core.group_by(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.count,cljs.core.key),cljs.core.deref(column_model)));
var this_level = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22503,(0),null);
var next_level = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22503,(1),null);
var descendants = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__22500_SHARP_){
return re_com.simple_v_table.descendant_QMARK_(ancestry,cljs.core.key(p1__22500_SHARP_));
}),next_level);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.v_box,new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),(7)], null)], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ancestry))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"children","children",-940561982),(function (){var iter__5628__auto__ = (function re_com$simple_v_table$nested_columns_$_iter__22506(s__22507){
return (new cljs.core.LazySeq(null,(function (){
var s__22507__$1 = s__22507;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22507__$1);
if(temp__5825__auto__){
var s__22507__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__22507__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22507__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22509 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22508 = (0);
while(true){
if((i__22508 < size__5627__auto__)){
var vec__22510 = cljs.core._nth(c__5626__auto__,i__22508);
var path = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22510,(0),null);
var state = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22510,(1),null);
cljs.core.chunk_append(b__22509,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.nested_columns,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([args,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ancestry","ancestry",-2066754950),path], null)], 0))], null));

var G__22564 = (i__22508 + (1));
i__22508 = G__22564;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22509),re_com$simple_v_table$nested_columns_$_iter__22506(cljs.core.chunk_rest(s__22507__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22509),null);
}
} else {
var vec__22513 = cljs.core.first(s__22507__$2);
var path = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22513,(0),null);
var state = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22513,(1),null);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.nested_columns,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([args,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ancestry","ancestry",-2066754950),path], null)], 0))], null),re_com$simple_v_table$nested_columns_$_iter__22506(cljs.core.rest(s__22507__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(descendants);
})()], null)], null)], null);
});
/**
 * Render a v-table and introduce the concept of columns (provide a spec for each).
 *   Of the nine possible sections of v-table, this table only supports four:
 *   top-left (1), row-headers (2), col-headers (4) and rows (5)
 *   Note that row-style and cell-style can either be a style map or functions which return a style map:
 * - (row-style row)
 *   x   - (cell-style row col)
 *   where row is the data for that row and col is the definition map for that column
 *   
 */
re_com.simple_v_table.tree_v_table = (function re_com$simple_v_table$tree_v_table(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22567 = arguments.length;
var i__5877__auto___22568 = (0);
while(true){
if((i__5877__auto___22568 < len__5876__auto___22567)){
args__5882__auto__.push((arguments[i__5877__auto___22568]));

var G__22569 = (i__5877__auto___22568 + (1));
i__5877__auto___22568 = G__22569;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.simple_v_table.tree_v_table.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.simple_v_table.tree_v_table.cljs$core$IFn$_invoke$arity$variadic = (function (p__22519){
var map__22520 = p__22519;
var map__22520__$1 = cljs.core.__destructure_map(map__22520);
var static_args = map__22520__$1;
var src = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22520__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var sort_by_column = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var header_hover_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (function() { 
var re_com$simple_v_table$tree_v_table_render__delegate = function (p__22521){
var map__22522 = p__22521;
var map__22522__$1 = cljs.core.__destructure_map(map__22522);
var dynamic_args = map__22522__$1;
var map__22523 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([re_com.simple_v_table.default_args,dynamic_args], 0));
var map__22523__$1 = cljs.core.__destructure_map(map__22523);
var args = map__22523__$1;
var export_button_renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"export-button-renderer","export-button-renderer",1141971581));
var striped_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"striped?","striped?",-797214979));
var model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"model","model",331153215));
var on_export = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391));
var on_leave_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"on-leave-row","on-leave-row",1361264390));
var columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"columns","columns",1998437288));
var show_export_button_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var on_enter_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"on-enter-row","on-enter-row",-139996948));
var row_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"row-height","row-height",527360749));
var max_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"max-width","max-width",-1939924051));
var src__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var on_click_row = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"on-click-row","on-click-row",-383826673));
var table_row_line_color = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"table-row-line-color","table-row-line-color",680569135));
var table_padding = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"table-padding","table-padding",-702873839));
var column_header_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558));
var debug_as = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"debug-as","debug-as",283322354));
var max_rows = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"max-rows","max-rows",-2131113613));
var row_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"row-style","row-style",1352472052));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var fixed_column_border_color = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"fixed-column-border-color","fixed-column-border-color",2036669142));
var column_header_renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"column-header-renderer","column-header-renderer",-1886265578));
var fixed_column_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22523__$1,new cljs.core.Keyword(null,"fixed-column-count","fixed-column-count",721535030));
var fcc_bounded = cljs.core.min.cljs$core$IFn$_invoke$arity$2(fixed_column_count,cljs.core.count(columns));
var fixed_cols = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(columns,(0),fcc_bounded);
var content_cols = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(columns,fcc_bounded,cljs.core.count(columns));
var fixed_content_width = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._PLUS_,(0),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__22516_SHARP_){
return new cljs.core.Keyword(null,"width","width",-384071477).cljs$core$IFn$_invoke$arity$2(p1__22516_SHARP_,(25));
}),fixed_cols));
var content_width = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._PLUS_,(0),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__22517_SHARP_){
return new cljs.core.Keyword(null,"width","width",-384071477).cljs$core$IFn$_invoke$arity$2(p1__22517_SHARP_,(25));
}),content_cols));
var table_border_style = (""+"1px solid "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(table_row_line_color));
var fixed_col_border_style = (""+"1px solid "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fixed_column_border_color));
var actual_table_width = (((((fixed_content_width + (((fixed_column_count > (0)))?(1):null)) + content_width) + re_com.v_table.scrollbar_tot_thick) + ((2) * table_padding)) + (2));
return new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),src__$1,new cljs.core.Keyword(null,"debug-as","debug-as",283322354),(function (){var or__5142__auto__ = debug_as;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component","component",1555936782),re_com.debug.short_component_name(reagent.impl.component.component_name(reagent.core.current_component())),new cljs.core.Keyword(null,"args","args",1315556576),args], null);
}
})(),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-simple-v-table-wrapper "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-wrapper","simple-wrapper",-507652041),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"flex","flex",-1425124628),(cljs.core.truth_(max_rows)?"0 1 auto":"100%"),new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"padding","padding",1660304693),re_com.util.px(table_padding),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),(function (){var or__5142__auto__ = max_width;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return re_com.util.px(actual_table_width);
}
})(),new cljs.core.Keyword(null,"border","border",1444987323),table_border_style,new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"3px"], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-wrapper","simple-wrapper",-507652041),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"simple-wrapper","simple-wrapper",-507652041),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)),new cljs.core.Keyword(null,"child","child",623967545),new cljs.core.PersistentVector(null, 29, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.v_table.v_table,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/simple_v_table.cljs",new cljs.core.Keyword(null,"line","line",212345235),498], null)),new cljs.core.Keyword(null,"model","model",331153215),model,new cljs.core.Keyword(null,"sort-comp","sort-comp",-667012852),re_com.simple_v_table.multi_comparator(re_com.util.__GT_v(cljs.core.deref(sort_by_column))),new cljs.core.Keyword(null,"column-header-renderer","column-header-renderer",-1886265578),(function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.nested_columns,cljs.core.into.cljs$core$IFn$_invoke$arity$2(args,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"columns","columns",1998437288),content_cols,new cljs.core.Keyword(null,"hover?","hover?",-1201331489),header_hover_QMARK_,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302),sort_by_column], null))], null);
}),new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),column_header_height,new cljs.core.Keyword(null,"row-header-renderer","row-header-renderer",-355094585),(function (){
return (function (i,row){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.simple_v_table.row_renderer,cljs.core.into.cljs$core$IFn$_invoke$arity$2(args,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"columns","columns",1998437288),fixed_cols,new cljs.core.Keyword(null,"row","row",-570139521),row,new cljs.core.Keyword(null,"row-index","row-index",-828710296),i], null))], null);
});
}),new cljs.core.Keyword(null,"row-renderer","row-renderer",314053346),cljs.core.constantly(null),new cljs.core.Keyword(null,"row-content-width","row-content-width",-1986261648),content_width,new cljs.core.Keyword(null,"row-height","row-height",527360749),row_height,new cljs.core.Keyword(null,"max-row-viewport-height","max-row-viewport-height",2061202688),(cljs.core.truth_(max_rows)?(max_rows * row_height):null),new cljs.core.Keyword(null,"top-left-renderer","top-left-renderer",2010514596),(function (i,row){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [column_header_renderer,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"columns","columns",1998437288),fixed_cols,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"sort-by-column","sort-by-column",-1857171302),sort_by_column], null)], null);
}),new cljs.core.Keyword(null,"top-right-renderer","top-right-renderer",-1691262321),(cljs.core.truth_(show_export_button_QMARK_)?(function (){
var rows = re_com.util.deref_or_value(model);
var columns__$1 = re_com.util.deref_or_value(columns);
var sort_by_column__$1 = re_com.util.deref_or_value(sort_by_column);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [export_button_renderer,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"rows","rows",850049680),rows,new cljs.core.Keyword(null,"columns","columns",1998437288),columns__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391),(function (_){
var G__22524 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"columns","columns",1998437288),columns__$1,new cljs.core.Keyword(null,"rows","rows",850049680),(function (){var G__22525 = rows;
if(cljs.core.truth_(sort_by_column__$1)){
return cljs.core.sort.cljs$core$IFn$_invoke$arity$2(re_com.simple_v_table.multi_comparator(re_com.util.__GT_v(sort_by_column__$1)),G__22525);
} else {
return G__22525;
}
})()], null);
return (on_export.cljs$core$IFn$_invoke$arity$1 ? on_export.cljs$core$IFn$_invoke$arity$1(G__22524) : on_export.call(null,G__22524));
})], null)], null);
}):null),new cljs.core.Keyword(null,"class","class",-2030961996),class$,new cljs.core.Keyword(null,"parts","parts",849007691),(function (){var G__22526 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"13px",new cljs.core.Keyword(null,"cursor","cursor",1011937484),"default"], null)], null)], null);
var G__22526__$1 = (((fixed_column_count > (0)))?(function (){var G__22527 = G__22526;
var G__22528 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"top-left","top-left",-1396159636),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"border-right","border-right",-668932860),fixed_col_border_style], null)], null),new cljs.core.Keyword(null,"row-headers","row-headers",1790514903),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"border-right","border-right",-668932860),fixed_col_border_style], null)], null)], null);
return (re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2 ? re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2(G__22527,G__22528) : re_com.theme.merge_props.call(null,G__22527,G__22528));
})():G__22526);
var G__22529 = G__22526__$1;
var G__22530 = cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc,parts,re_com.simple_v_table.simple_v_table_exclusive_parts);
return (re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2 ? re_com.theme.merge_props.cljs$core$IFn$_invoke$arity$2(G__22529,G__22530) : re_com.theme.merge_props.call(null,G__22529,G__22530));

})()], null)], null);
};
var re_com$simple_v_table$tree_v_table_render = function (var_args){
var p__22521 = null;
if (arguments.length > 0) {
var G__22602__i = 0, G__22602__a = new Array(arguments.length -  0);
while (G__22602__i < G__22602__a.length) {G__22602__a[G__22602__i] = arguments[G__22602__i + 0]; ++G__22602__i;}
  p__22521 = new cljs.core.IndexedSeq(G__22602__a,0,null);
} 
return re_com$simple_v_table$tree_v_table_render__delegate.call(this,p__22521);};
re_com$simple_v_table$tree_v_table_render.cljs$lang$maxFixedArity = 0;
re_com$simple_v_table$tree_v_table_render.cljs$lang$applyTo = (function (arglist__22603){
var p__22521 = cljs.core.seq(arglist__22603);
return re_com$simple_v_table$tree_v_table_render__delegate(p__22521);
});
re_com$simple_v_table$tree_v_table_render.cljs$core$IFn$_invoke$arity$variadic = re_com$simple_v_table$tree_v_table_render__delegate;
return re_com$simple_v_table$tree_v_table_render;
})()
;
}));

(re_com.simple_v_table.tree_v_table.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.simple_v_table.tree_v_table.cljs$lang$applyTo = (function (seq22518){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq22518));
}));


//# sourceMappingURL=re_com.simple_v_table.js.map
