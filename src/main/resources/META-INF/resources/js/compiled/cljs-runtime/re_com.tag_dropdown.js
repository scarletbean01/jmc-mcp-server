goog.provide('re_com.tag_dropdown');
re_com.tag_dropdown.label_style = (function re_com$tag_dropdown$label_style(var_args){
var G__21602 = arguments.length;
switch (G__21602) {
case 2:
return re_com.tag_dropdown.label_style.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return re_com.tag_dropdown.label_style.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(re_com.tag_dropdown.label_style.cljs$core$IFn$_invoke$arity$2 = (function (selected_QMARK_,as_exclusions_QMARK_){
return re_com.tag_dropdown.label_style.cljs$core$IFn$_invoke$arity$3(selected_QMARK_,as_exclusions_QMARK_,null);
}));

(re_com.tag_dropdown.label_style.cljs$core$IFn$_invoke$arity$3 = (function (selected_QMARK_,as_exclusions_QMARK_,selected_color){
var base_style = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"margin-top","margin-top",392161226),"1px",new cljs.core.Keyword(null,"color","color",1011675173),"#333"], null);
var base_style__$1 = (cljs.core.truth_((function (){var and__5140__auto__ = selected_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return as_exclusions_QMARK_;
} else {
return and__5140__auto__;
}
})())?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([base_style,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"text-decoration","text-decoration",1836813207),"line-through"], null)], 0)):base_style);
var base_style__$2 = (cljs.core.truth_((function (){var and__5140__auto__ = selected_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return selected_color;
} else {
return and__5140__auto__;
}
})())?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([base_style__$1,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),selected_color], null)], 0)):base_style__$1);
return base_style__$2;
}));

(re_com.tag_dropdown.label_style.cljs$lang$maxFixedArity = 3);

re_com.tag_dropdown.text_tag = (function re_com$tag_dropdown$text_tag(){
var over_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
return (function() { 
var re_com$tag_dropdown$text_tag_$_text_tag_render__delegate = function (p__21642){
var map__21645 = p__21642;
var map__21645__$1 = cljs.core.__destructure_map(map__21645);
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var on_unselect = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"on-unselect","on-unselect",279420763));
var hover_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"hover-style","hover-style",976094077));
var tooltip = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058));
var attr = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"attr","attr",-604132353));
var label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21645__$1,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"label","label",1718410804));
var description_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"description-fn","description-fn",-1507979835));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var id_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21645__$1,new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"id","id",-1388402092));
var on_click = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"on-click","on-click",1632826543));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var tag_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21645__$1,new cljs.core.Keyword(null,"tag-data","tag-data",1007525109));
var tag_id = (id_fn.cljs$core$IFn$_invoke$arity$1 ? id_fn.cljs$core$IFn$_invoke$arity$1(tag_data) : id_fn.call(null,tag_data));
var tag_id_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(tag_id);
var clickable_QMARK_ = (((!((on_click == null)))) && (cljs.core.not(disabled_QMARK_)));
var unselectable_QMARK_ = (((!((on_unselect == null)))) && (cljs.core.not(disabled_QMARK_)));
var placeholder_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(tag_data),new cljs.core.Keyword(null,"$placeholder$","$placeholder$",223628665));
var border = ((placeholder_QMARK_)?"1px dashed #828282":null);
var tag_label = (label_fn.cljs$core$IFn$_invoke$arity$1 ? label_fn.cljs$core$IFn$_invoke$arity$1(tag_data) : label_fn.call(null,tag_data));
var tag_description = (cljs.core.truth_(description_fn)?(description_fn.cljs$core$IFn$_invoke$arity$1 ? description_fn.cljs$core$IFn$_invoke$arity$1(tag_data) : description_fn.call(null,tag_data)):null);
return new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),52], null)),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tag-dropdown-tag "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tag_id_kw,new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"style","style",-496642736)], null)),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tag_id_kw,new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tag_id_kw,new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"start","start",-355208981),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 21, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),58], null)),new cljs.core.Keyword(null,"align-self","align-self",1475936794),new cljs.core.Keyword(null,"start","start",-355208981),new cljs.core.Keyword(null,"justify","justify",-722524056),((placeholder_QMARK_)?new cljs.core.Keyword(null,"end","end",-268185958):new cljs.core.Keyword(null,"center","center",-748944368)),new cljs.core.Keyword(null,"min-width","min-width",1926193728),((placeholder_QMARK_)?new cljs.core.Keyword(null,"width","width",-384071477).cljs$core$IFn$_invoke$arity$1(tag_data):null),new cljs.core.Keyword(null,"padding","padding",1660304693),"0px 4px",new cljs.core.Keyword(null,"margin","margin",-995903681),re_com.util.px_n.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(2),((placeholder_QMARK_)?(0):(6)),(2),(0)], 0)),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"noselect rc-text-tag "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(class$)),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"color","color",1011675173),"white",new cljs.core.Keyword(null,"background-color","background-color",570434026),new cljs.core.Keyword(null,"background-color","background-color",570434026).cljs$core$IFn$_invoke$arity$1(tag_data),new cljs.core.Keyword(null,"cursor","cursor",1011937484),((cljs.core.not(disabled_QMARK_))?"pointer":"default"),new cljs.core.Keyword(null,"font-size","font-size",-1847940346),"12px",new cljs.core.Keyword(null,"border","border",1444987323),border,new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"3px"], null),(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.deref(over_QMARK_);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.not(disabled_QMARK_);
} else {
return and__5140__auto__;
}
})())?hover_style:null),style], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),tooltip,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (event){
if(((placeholder_QMARK_) && (cljs.core.not(disabled_QMARK_)))){
var G__21663_21795 = new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(tag_data);
(on_click.cljs$core$IFn$_invoke$arity$1 ? on_click.cljs$core$IFn$_invoke$arity$1(G__21663_21795) : on_click.call(null,G__21663_21795));
} else {
}

return null;
}),new cljs.core.Keyword(null,"on-mouse-enter","on-mouse-enter",-1664921661),(function (event){
cljs.core.reset_BANG_(over_QMARK_,true);

return null;
}),new cljs.core.Keyword(null,"on-mouse-leave","on-mouse-leave",-1864319528),(function (event){
cljs.core.reset_BANG_(over_QMARK_,false);

return null;
})], null),attr], 0)),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [((placeholder_QMARK_)?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),87], null)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"hsl(194, 61%, 85%)"], null),new cljs.core.Keyword(null,"child","child",623967545),goog.string.unescapeEntities("&#9660;")], null):new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),91], null)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cursor","cursor",1011937484),((clickable_QMARK_)?"pointer":null)], null),new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (event){
if(clickable_QMARK_){
var G__21668_21800 = new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(tag_data);
(on_click.cljs$core$IFn$_invoke$arity$1 ? on_click.cljs$core$IFn$_invoke$arity$1(G__21668_21800) : on_click.call(null,G__21668_21800));
} else {
}

return null;
})], null),new cljs.core.Keyword(null,"child","child",623967545),(function (){var or__5142__auto__ = tag_label;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "???";
}
})()], null)),((((unselectable_QMARK_) && ((!(placeholder_QMARK_)))))?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),100], null)),new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),103], null)),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"margin-left","margin-left",2015598377),"4px",new cljs.core.Keyword(null,"margin-right","margin-right",809689658),"3px"], null),new cljs.core.Keyword(null,"child","child",623967545),"|"], null),new cljs.core.PersistentVector(null, 15, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.close_button.close_button,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),108], null)),new cljs.core.Keyword(null,"color","color",1011675173),"white",new cljs.core.Keyword(null,"hover-color","hover-color",663962326),"#ccc",new cljs.core.Keyword(null,"div-size","div-size",1661625995),(13),new cljs.core.Keyword(null,"font-size","font-size",-1847940346),(13),new cljs.core.Keyword(null,"top-offset","top-offset",1235816245),(1),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
if(unselectable_QMARK_){
var G__21676 = new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(tag_data);
return (on_unselect.cljs$core$IFn$_invoke$arity$1 ? on_unselect.cljs$core$IFn$_invoke$arity$1(G__21676) : on_unselect.call(null,G__21676));
} else {
return null;
}
})], null)], null)], null):null)], null)], null),(cljs.core.truth_(tag_description)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),"#586069"], null)], null),tag_description], null):null)], null)], null);
};
var re_com$tag_dropdown$text_tag_$_text_tag_render = function (var_args){
var p__21642 = null;
if (arguments.length > 0) {
var G__21804__i = 0, G__21804__a = new Array(arguments.length -  0);
while (G__21804__i < G__21804__a.length) {G__21804__a[G__21804__i] = arguments[G__21804__i + 0]; ++G__21804__i;}
  p__21642 = new cljs.core.IndexedSeq(G__21804__a,0,null);
} 
return re_com$tag_dropdown$text_tag_$_text_tag_render__delegate.call(this,p__21642);};
re_com$tag_dropdown$text_tag_$_text_tag_render.cljs$lang$maxFixedArity = 0;
re_com$tag_dropdown$text_tag_$_text_tag_render.cljs$lang$applyTo = (function (arglist__21805){
var p__21642 = cljs.core.seq(arglist__21805);
return re_com$tag_dropdown$text_tag_$_text_tag_render__delegate(p__21642);
});
re_com$tag_dropdown$text_tag_$_text_tag_render.cljs$core$IFn$_invoke$arity$variadic = re_com$tag_dropdown$text_tag_$_text_tag_render__delegate;
return re_com$tag_dropdown$text_tag_$_text_tag_render;
})()
;
});
re_com.tag_dropdown.tag_dropdown_exclusive_parts_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"popover-anchor-wrapper","popover-anchor-wrapper",-564325032),new cljs.core.Keyword(null,"level","level",1290497552),(0),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tag-dropdown-popover-anchor-wrapper",new cljs.core.Keyword(null,"impl","impl",1677848700),"[popover-anchor-wrapper]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"main","main",-2117802661),new cljs.core.Keyword(null,"level","level",1290497552),(1),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tag-dropdown",new cljs.core.Keyword(null,"impl","impl",1677848700),"[h-box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tag-dropdown-tags",new cljs.core.Keyword(null,"impl","impl",1677848700),"[h-box]"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tag-dropdown-tag",new cljs.core.Keyword(null,"impl","impl",1677848700),"[h-box]",new cljs.core.Keyword(null,"notes","notes",-1039600523),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Each individual tag can be independently targeted with the keyword of its ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":id"], null)], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"selection-list","selection-list",-999656475),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tag-dropdown-selection-list",new cljs.core.Keyword(null,"impl","impl",1677848700),"[selection-list]"], null)], null):null);
re_com.tag_dropdown.tag_dropdown_parts_desc = ((re_com.config.include_args_desc_QMARK_)?cljs.core.into.cljs$core$IFn$_invoke$arity$2(re_com.tag_dropdown.tag_dropdown_exclusive_parts_desc,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__21682_SHARP_){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(p1__21682_SHARP_,new cljs.core.Keyword(null,"level","level",1290497552),cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.inc,cljs.core.inc));
}),cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__21681_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"legacy","legacy",1434943289),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(p1__21681_SHARP_));
}),re_com.selection_list.selection_list_parts_desc))):null);
re_com.tag_dropdown.tag_dropdown_parts = ((re_com.config.include_args_desc_QMARK_)?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),re_com.tag_dropdown.tag_dropdown_parts_desc)):null);
re_com.tag_dropdown.tag_dropdown_args_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 18, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choices","choices",1385611597),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"vector of maps | r/atom",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.vector_of_maps_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Each map represents a choice. Values corresponding to id, label, short label and tag background color are extracted by the functions ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":id"], null),", ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label-fn"], null)," & ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":short-label-fn"], null)," & ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":background-color"], null),". See below."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"model","model",331153215),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"a set of ids | r/atom",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The set of the ids for currently selected choices. If nil or empty, see ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":placeholder"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"Background text shown when there's no selection."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-change","on-change",-732046149),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"id -> nil",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.fn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"This function is called whenever the selection changes. Called with one argument, the set of selected ids. See ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":model"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"if true, no user selection is allowed"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"required?","required?",-872514462),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean | r/atom",new cljs.core.Keyword(null,"description","description",-1428560544),"when true, at least one item must be selected."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"unselect-buttons?","unselect-buttons?",-1841823085),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"When true, \"X\" buttons will be added to the display of selected tags (on the right), allowing the user to directly unselect them."], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),":label",new cljs.core.Keyword(null,"type","type",1174270348),"map -> hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A function which can turn a choice into a displayable label. Will be called for each element in ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),". Given one argument, a choice map, it returns a string or hiccup."], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"description-fn","description-fn",-1507979835),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),":description",new cljs.core.Keyword(null,"type","type",1174270348),"map -> hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A function which can turn a choice into a displayable description. Will be called for each element in ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),". Given one argument, a choice map, it returns a string or hiccup."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"abbrev-fn","abbrev-fn",589483206),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"choice -> hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A function which can turn a choice into an abbreviated label. Will be called for each element in ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),". Given one argument, a choice map, it returns a string or hiccup."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"abbrev-threshold","abbrev-threshold",-726872663),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"number",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 12, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The text displayed for selected choices is obtained via either ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label-fn"], null)," or ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"abbrev-fn"], null),". When the total number of characters displayed is less than this argument then ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label-fn"], null)," will be used, otherwise ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"abbrev-fn"], null),". You should set this value taking into account the width of this component. If not set, only ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label-fn"], null)," is used."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"min-width","min-width",1926193728),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"the CSS min-width, like \"100px\" or \"20em\". This is the natural display width of the Component. It prevents the width from becoming smaller than the value specified, yet allows growth horizontally if sufficient choices are selected up to ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":max-width"], null)," or unbounded growth if ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":max-width"], null)," is not provided."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"the CSS max-width, like \"100px\" or \"20em\". It prevents the width from becoming larger than the value specified. If sufficient choices are selected to go beyond the maximum then some choices will be hidden by overflow."], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"height","height",1025178622),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),"25px",new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"the specific height of the component"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"CSS styles to add or override"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"parts","parts",849007691),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.parts_QMARK_(re_com.tag_dropdown.tag_dropdown_parts),new cljs.core.Keyword(null,"description","description",-1428560544),"See Parts section below."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"src","src",-1651076051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Used in dev builds to assist with debugging. Source code coordinates map containing keys",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":file"], null),"and",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":line"], null),". See 'Debugging'."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"debug-as","debug-as",283322354),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Used in dev builds to assist with debugging, when one component is used implement another component, and we want the implementation component to masquerade as the original component in debug output, such as component stacks. A map optionally containing keys",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":component"], null),"and",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":args"], null),"."], null)], null)], null):null);
re_com.tag_dropdown.tag_dropdown = (function re_com$tag_dropdown$tag_dropdown(var_args){
var args__5882__auto__ = [];
var len__5876__auto___21822 = arguments.length;
var i__5877__auto___21823 = (0);
while(true){
if((i__5877__auto___21823 < len__5876__auto___21822)){
args__5882__auto__.push((arguments[i__5877__auto___21823]));

var G__21824 = (i__5877__auto___21823 + (1));
i__5877__auto___21823 = G__21824;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.tag_dropdown.tag_dropdown.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.tag_dropdown.tag_dropdown.cljs$core$IFn$_invoke$arity$variadic = (function (p__21706){
var map__21708 = p__21706;
var map__21708__$1 = cljs.core.__destructure_map(map__21708);
var args = map__21708__$1;
var or__5142__auto__ = (((!(goog.DEBUG)))?null:re_com.validate.validate_args(re_com.validate.extract_arg_data(re_com.tag_dropdown.tag_dropdown_args_desc),args));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var showing_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
return (function() { 
var re_com$tag_dropdown$tag_dropdown_render__delegate = function (p__21711){
var map__21712 = p__21711;
var map__21712__$1 = cljs.core.__destructure_map(map__21712);
var args__$1 = map__21712__$1;
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21712__$1,new cljs.core.Keyword(null,"height","height",1025178622),"25px");
var model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"model","model",331153215));
var min_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"min-width","min-width",1926193728));
var label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21712__$1,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"label","label",1718410804));
var required_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"required?","required?",-872514462));
var description_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21712__$1,new cljs.core.Keyword(null,"description-fn","description-fn",-1507979835),new cljs.core.Keyword(null,"description","description",-1428560544));
var placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083));
var abbrev_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"abbrev-fn","abbrev-fn",589483206));
var abbrev_threshold = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"abbrev-threshold","abbrev-threshold",-726872663));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var max_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"max-width","max-width",-1939924051));
var src = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
var choices = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"choices","choices",1385611597));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var debug_as = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"debug-as","debug-as",283322354));
var unselect_buttons_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21712__$1,new cljs.core.Keyword(null,"unselect-buttons?","unselect-buttons?",-1841823085));
var or__5142__auto____$1 = (((!(goog.DEBUG)))?null:re_com.validate.validate_args(re_com.validate.extract_arg_data(re_com.tag_dropdown.tag_dropdown_args_desc),args__$1));
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
var choices__$1 = re_com.util.deref_or_value(choices);
var model__$1 = re_com.util.deref_or_value(model);
var abbrev_threshold__$1 = re_com.util.deref_or_value(abbrev_threshold);
var required_QMARK___$1 = re_com.util.deref_or_value(required_QMARK_);
var disabled_QMARK___$1 = re_com.util.deref_or_value(disabled_QMARK_);
var unselect_buttons_QMARK___$1 = re_com.util.deref_or_value(unselect_buttons_QMARK_);
var choices_num_chars = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (n,choice){
if(cljs.core.contains_QMARK_(model__$1,new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(choice))){
return (n + cljs.core.count((label_fn.cljs$core$IFn$_invoke$arity$1 ? label_fn.cljs$core$IFn$_invoke$arity$1(choice) : label_fn.call(null,choice))));
} else {
return n;
}
}),(0),choices__$1);
var abbrev_QMARK_ = (((choices_num_chars >= abbrev_threshold__$1)) && (((typeof abbrev_threshold__$1 === 'number') && (cljs.core.fn_QMARK_(abbrev_fn)))));
var placeholder_tag = new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tag_dropdown.text_tag,new cljs.core.Keyword(null,"tag-data","tag-data",1007525109),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"$placeholder$","$placeholder$",223628665),new cljs.core.Keyword(null,"label","label",1718410804),"",new cljs.core.Keyword(null,"background-color","background-color",570434026),"white",new cljs.core.Keyword(null,"width","width",-384071477),((abbrev_QMARK_)?"20px":"40px")], null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(showing_QMARK_,true);
}),new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),"Click to select tags",new cljs.core.Keyword(null,"hover-style","hover-style",976094077),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"background-color","background-color",570434026),"#eee"], null)], null);
var tag_list_body = new cljs.core.PersistentVector(null, 27, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.selection_list.selection_list,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),204], null)),new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.get_in.cljs$core$IFn$_invoke$arity$3(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"selection-list","selection-list",-999656475),new cljs.core.Keyword(null,"class","class",-2030961996)], null),""),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"selection-list","selection-list",-999656475),new cljs.core.Keyword(null,"style","style",-496642736)], null)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"selection-list","selection-list",-999656475),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),disabled_QMARK___$1,new cljs.core.Keyword(null,"required?","required?",-872514462),required_QMARK___$1,new cljs.core.Keyword(null,"parts","parts",849007691),re_com.util.assoc_in_if_empty(re_com.util.assoc_in_if_empty(re_com.util.assoc_in_if_empty(cljs.core.select_keys(parts,re_com.selection_list.selection_list_parts),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"list-group-item","list-group-item",1978708789),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"border","border",1444987323)], null),"1px solid #ddd"),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"list-group-item","list-group-item",1978708789),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"height","height",1025178622)], null),"auto"),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"list-group-item","list-group-item",1978708789),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"padding","padding",1660304693)], null),"10px 15px"),new cljs.core.Keyword(null,"choices","choices",1385611597),choices__$1,new cljs.core.Keyword(null,"hide-border?","hide-border?",1792698922),true,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),(function (tag){
return new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tag_dropdown.text_tag,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),label_fn,new cljs.core.Keyword(null,"description-fn","description-fn",-1507979835),description_fn,new cljs.core.Keyword(null,"tag-data","tag-data",1007525109),tag,new cljs.core.Keyword(null,"style","style",-496642736),style], null);
}),new cljs.core.Keyword(null,"model","model",331153215),model__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__21693_SHARP_){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(p1__21693_SHARP_) : on_change.call(null,p1__21693_SHARP_));
}),new cljs.core.Keyword(null,"multi-select?","multi-select?",973156151),true], null);
var tag_main = new cljs.core.PersistentVector(null, 21, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),227], null)),new cljs.core.Keyword(null,"min-width","min-width",1926193728),min_width,new cljs.core.Keyword(null,"max-width","max-width",-1939924051),max_width,new cljs.core.Keyword(null,"height","height",1025178622),height,new cljs.core.Keyword(null,"align","align",1964212802),new cljs.core.Keyword(null,"center","center",-748944368),new cljs.core.Keyword(null,"padding","padding",1660304693),"0px 6px",new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tag-dropdown "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"main","main",-2117802661),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"background-color","background-color",570434026),(cljs.core.truth_(disabled_QMARK___$1)?"#EEE":"white"),new cljs.core.Keyword(null,"color","color",1011675173),"#BBB",new cljs.core.Keyword(null,"border","border",1444987323),"1px solid lightgrey",new cljs.core.Keyword(null,"border-radius","border-radius",419594011),"2px",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden",new cljs.core.Keyword(null,"cursor","cursor",1011937484),(cljs.core.truth_(disabled_QMARK___$1)?"default":"pointer")], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"main","main",-2117802661),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.PersistentArrayMap.EMPTY,((cljs.core.not(disabled_QMARK___$1))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (event){
cljs.core.reset_BANG_(showing_QMARK_,true);

return null;
})], null):null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"main","main",-2117802661),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),245], null)),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tag-dropdown-tags "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tags","tags",1771418977),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"size","size",1098693007),"1",new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden"], null),new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.conj.cljs$core$IFn$_invoke$arity$variadic(cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (tag){
if(cljs.core.contains_QMARK_(model__$1,new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(tag))){
return new cljs.core.PersistentVector(null, 19, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tag_dropdown.text_tag,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),((abbrev_QMARK_)?abbrev_fn:label_fn),new cljs.core.Keyword(null,"tag-data","tag-data",1007525109),tag,new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(tag),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),disabled_QMARK___$1,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(showing_QMARK_,true);
}),new cljs.core.Keyword(null,"on-unselect","on-unselect",279420763),(cljs.core.truth_((function (){var and__5140__auto__ = unselect_buttons_QMARK___$1;
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.not((function (){var and__5140__auto____$1 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),cljs.core.count(model__$1));
if(and__5140__auto____$1){
return required_QMARK___$1;
} else {
return and__5140__auto____$1;
}
})());
} else {
return and__5140__auto__;
}
})())?(function (p1__21696_SHARP_){
var G__21744 = cljs.core.disj.cljs$core$IFn$_invoke$arity$2(model__$1,p1__21696_SHARP_);
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__21744) : on_change.call(null,G__21744));
}):null),new cljs.core.Keyword(null,"hover-style","hover-style",976094077),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"opacity","opacity",397153780),"0.8"], null),new cljs.core.Keyword(null,"style","style",-496642736),style,new cljs.core.Keyword(null,"parts","parts",849007691),parts], null);
} else {
return null;
}
}),choices__$1),((cljs.core.not(disabled_QMARK___$1))?placeholder_tag:null),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.gap,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),266], null)),new cljs.core.Keyword(null,"size","size",1098693007),"20px"], null),(((cljs.core.count(model__$1) === (0)))?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),270], null)),new cljs.core.Keyword(null,"child","child",623967545),(cljs.core.truth_(placeholder)?placeholder:"")], null):null)], 0))], null),(cljs.core.truth_((function (){var and__5140__auto__ = cljs.core.not_empty(model__$1);
if(cljs.core.truth_(and__5140__auto__)){
return ((cljs.core.not(disabled_QMARK___$1)) && (cljs.core.not(required_QMARK___$1)));
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.close_button.close_button,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),275], null)),new cljs.core.Keyword(null,"parts","parts",849007691),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"margin-left","margin-left",2015598377),"5px"], null)], null)], null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
var G__21752 = cljs.core.PersistentHashSet.EMPTY;
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__21752) : on_change.call(null,G__21752));
})], null):null)], null)], null);
return new cljs.core.PersistentVector(null, 15, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.popover.popover_anchor_wrapper,new cljs.core.Keyword(null,"src","src",-1651076051),src,new cljs.core.Keyword(null,"debug-as","debug-as",283322354),(function (){var or__5142__auto____$2 = debug_as;
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component","component",1555936782),re_com.debug.short_component_name(reagent.impl.component.component_name(reagent.core.current_component())),new cljs.core.Keyword(null,"args","args",1315556576),args__$1], null);
}
})(),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tag-dropdown-popover-anchor-wrapper "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"popover-anchor-wrapper","popover-anchor-wrapper",-564325032),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"showing?","showing?",2094921488),showing_QMARK_,new cljs.core.Keyword(null,"position","position",-2011731912),new cljs.core.Keyword(null,"below-center","below-center",-2126885397),new cljs.core.Keyword(null,"anchor","anchor",1549638489),tag_main,new cljs.core.Keyword(null,"popover","popover",-1809582136),new cljs.core.PersistentVector(null, 17, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.popover.popover_content_wrapper,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tag_dropdown.cljs",new cljs.core.Keyword(null,"line","line",212345235),286], null)),new cljs.core.Keyword(null,"arrow-length","arrow-length",934916707),(0),new cljs.core.Keyword(null,"arrow-width","arrow-width",1926673833),(0),new cljs.core.Keyword(null,"arrow-gap","arrow-gap",1490206257),(1),new cljs.core.Keyword(null,"no-clip?","no-clip?",-188884951),true,new cljs.core.Keyword(null,"on-cancel","on-cancel",-2071892932),(function (){
return cljs.core.reset_BANG_(showing_QMARK_,false);
}),new cljs.core.Keyword(null,"padding","padding",1660304693),"19px 19px",new cljs.core.Keyword(null,"body","body",-2049205669),tag_list_body], null)], null);
}
};
var re_com$tag_dropdown$tag_dropdown_render = function (var_args){
var p__21711 = null;
if (arguments.length > 0) {
var G__21867__i = 0, G__21867__a = new Array(arguments.length -  0);
while (G__21867__i < G__21867__a.length) {G__21867__a[G__21867__i] = arguments[G__21867__i + 0]; ++G__21867__i;}
  p__21711 = new cljs.core.IndexedSeq(G__21867__a,0,null);
} 
return re_com$tag_dropdown$tag_dropdown_render__delegate.call(this,p__21711);};
re_com$tag_dropdown$tag_dropdown_render.cljs$lang$maxFixedArity = 0;
re_com$tag_dropdown$tag_dropdown_render.cljs$lang$applyTo = (function (arglist__21868){
var p__21711 = cljs.core.seq(arglist__21868);
return re_com$tag_dropdown$tag_dropdown_render__delegate(p__21711);
});
re_com$tag_dropdown$tag_dropdown_render.cljs$core$IFn$_invoke$arity$variadic = re_com$tag_dropdown$tag_dropdown_render__delegate;
return re_com$tag_dropdown$tag_dropdown_render;
})()
;
}
}));

(re_com.tag_dropdown.tag_dropdown.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.tag_dropdown.tag_dropdown.cljs$lang$applyTo = (function (seq21700){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21700));
}));


//# sourceMappingURL=re_com.tag_dropdown.js.map
