goog.provide('devtools.formatters.budgeting');
devtools.formatters.budgeting.header_expander_depth_cost = (2);
devtools.formatters.budgeting.over_budget_values = (((typeof WeakSet !== 'undefined'))?(new WeakSet()):cljs.core.volatile_BANG_(cljs.core.PersistentHashSet.EMPTY));
devtools.formatters.budgeting.add_over_budget_value_BANG_ = (function devtools$formatters$budgeting$add_over_budget_value_BANG_(value){
if(cljs.core.volatile_QMARK_(devtools.formatters.budgeting.over_budget_values)){
return cljs.core.vreset_BANG_(devtools.formatters.budgeting.over_budget_values,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(devtools.formatters.budgeting.over_budget_values),value));
} else {
var o__15057__auto__ = devtools.formatters.budgeting.over_budget_values;
return (o__15057__auto__["add"]).call(o__15057__auto__,value);
}
});
devtools.formatters.budgeting.delete_over_budget_value_BANG_ = (function devtools$formatters$budgeting$delete_over_budget_value_BANG_(value){
if(cljs.core.volatile_QMARK_(devtools.formatters.budgeting.over_budget_values)){
return cljs.core.vreset_BANG_(devtools.formatters.budgeting.over_budget_values,cljs.core.disj.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(devtools.formatters.budgeting.over_budget_values),value));
} else {
var o__15057__auto__ = devtools.formatters.budgeting.over_budget_values;
return (o__15057__auto__["delete"]).call(o__15057__auto__,value);
}
});
devtools.formatters.budgeting.has_over_budget_value_QMARK_ = (function devtools$formatters$budgeting$has_over_budget_value_QMARK_(value){
if(cljs.core.volatile_QMARK_(devtools.formatters.budgeting.over_budget_values)){
return cljs.core.contains_QMARK_(cljs.core.deref(devtools.formatters.budgeting.over_budget_values),value);
} else {
var o__15057__auto__ = devtools.formatters.budgeting.over_budget_values;
return (o__15057__auto__["has"]).call(o__15057__auto__,value);
}
});
devtools.formatters.budgeting.object_reference_QMARK_ = (function devtools$formatters$budgeting$object_reference_QMARK_(json_ml){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.first(json_ml),"object");
});
devtools.formatters.budgeting.determine_depth = (function devtools$formatters$budgeting$determine_depth(json_ml){
if(cljs.core.truth_(cljs.core.array_QMARK_(json_ml))){
return (cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.max,cljs.core.map.cljs$core$IFn$_invoke$arity$2(devtools.formatters.budgeting.determine_depth,json_ml)) + (1));
} else {
return (0);
}
});
devtools.formatters.budgeting.has_any_object_reference_QMARK_ = (function devtools$formatters$budgeting$has_any_object_reference_QMARK_(json_ml){
if(cljs.core.truth_(cljs.core.array_QMARK_(json_ml))){
if(devtools.formatters.budgeting.object_reference_QMARK_(json_ml)){
return true;
} else {
return cljs.core.some(devtools.formatters.budgeting.has_any_object_reference_QMARK_,json_ml);
}
} else {
return null;
}
});
devtools.formatters.budgeting.transfer_remaining_depth_budget_BANG_ = (function devtools$formatters$budgeting$transfer_remaining_depth_budget_BANG_(object_reference,depth_budget){
if((!((depth_budget < (0))))){
} else {
throw (new Error("Assert failed: (not (neg? depth-budget))"));
}

var data = cljs.core.second(object_reference);
var _ = ((cljs.core.object_QMARK_(data))?null:(function(){throw (new Error("Assert failed: (object? data)"))})());
var config = (data["config"]);
var G__20821 = data;
var target__15068__auto__ = G__20821;
if(cljs.core.truth_(target__15068__auto__)){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"unable to locate object path "+" in "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__20821)))+"\n"+"target__15068__auto__")));
}

(target__15068__auto__["config"] = devtools.formatters.state.set_depth_budget(config,depth_budget));

return G__20821;
});
devtools.formatters.budgeting.distribute_budget_BANG_ = (function devtools$formatters$budgeting$distribute_budget_BANG_(json_ml,depth_budget){
if((!((depth_budget < (0))))){
} else {
throw (new Error("Assert failed: (not (neg? depth-budget))"));
}

if(cljs.core.truth_(cljs.core.array_QMARK_(json_ml))){
var new_depth_budget_20837 = (depth_budget - (1));
if(devtools.formatters.budgeting.object_reference_QMARK_(json_ml)){
devtools.formatters.budgeting.transfer_remaining_depth_budget_BANG_(json_ml,new_depth_budget_20837);
} else {
var seq__20825_20840 = cljs.core.seq(json_ml);
var chunk__20826_20841 = null;
var count__20827_20842 = (0);
var i__20828_20843 = (0);
while(true){
if((i__20828_20843 < count__20827_20842)){
var item_20844 = chunk__20826_20841.cljs$core$IIndexed$_nth$arity$2(null,i__20828_20843);
(devtools.formatters.budgeting.distribute_budget_BANG_.cljs$core$IFn$_invoke$arity$2 ? devtools.formatters.budgeting.distribute_budget_BANG_.cljs$core$IFn$_invoke$arity$2(item_20844,new_depth_budget_20837) : devtools.formatters.budgeting.distribute_budget_BANG_.call(null,item_20844,new_depth_budget_20837));


var G__20845 = seq__20825_20840;
var G__20846 = chunk__20826_20841;
var G__20847 = count__20827_20842;
var G__20848 = (i__20828_20843 + (1));
seq__20825_20840 = G__20845;
chunk__20826_20841 = G__20846;
count__20827_20842 = G__20847;
i__20828_20843 = G__20848;
continue;
} else {
var temp__5825__auto___20850 = cljs.core.seq(seq__20825_20840);
if(temp__5825__auto___20850){
var seq__20825_20851__$1 = temp__5825__auto___20850;
if(cljs.core.chunked_seq_QMARK_(seq__20825_20851__$1)){
var c__5673__auto___20852 = cljs.core.chunk_first(seq__20825_20851__$1);
var G__20853 = cljs.core.chunk_rest(seq__20825_20851__$1);
var G__20854 = c__5673__auto___20852;
var G__20855 = cljs.core.count(c__5673__auto___20852);
var G__20856 = (0);
seq__20825_20840 = G__20853;
chunk__20826_20841 = G__20854;
count__20827_20842 = G__20855;
i__20828_20843 = G__20856;
continue;
} else {
var item_20859 = cljs.core.first(seq__20825_20851__$1);
(devtools.formatters.budgeting.distribute_budget_BANG_.cljs$core$IFn$_invoke$arity$2 ? devtools.formatters.budgeting.distribute_budget_BANG_.cljs$core$IFn$_invoke$arity$2(item_20859,new_depth_budget_20837) : devtools.formatters.budgeting.distribute_budget_BANG_.call(null,item_20859,new_depth_budget_20837));


var G__20860 = cljs.core.next(seq__20825_20851__$1);
var G__20861 = null;
var G__20862 = (0);
var G__20863 = (0);
seq__20825_20840 = G__20860;
chunk__20826_20841 = G__20861;
count__20827_20842 = G__20862;
i__20828_20843 = G__20863;
continue;
}
} else {
}
}
break;
}
}
} else {
}

return json_ml;
});
devtools.formatters.budgeting.was_over_budget_QMARK__BANG_ = (function devtools$formatters$budgeting$was_over_budget_QMARK__BANG_(value){
if(cljs.core.truth_(devtools.formatters.budgeting.has_over_budget_value_QMARK_(value))){
devtools.formatters.budgeting.delete_over_budget_value_BANG_(value);

return true;
} else {
return null;
}
});
devtools.formatters.budgeting.alter_json_ml_to_fit_in_remaining_budget_BANG_ = (function devtools$formatters$budgeting$alter_json_ml_to_fit_in_remaining_budget_BANG_(value,json_ml){
var temp__5823__auto__ = devtools.formatters.helpers.pref(new cljs.core.Keyword(null,"initial-hierarchy-depth-budget","initial-hierarchy-depth-budget",-482715807));
if(cljs.core.truth_(temp__5823__auto__)){
var initial_hierarchy_depth_budget = temp__5823__auto__;
var remaining_depth_budget = (function (){var or__5142__auto__ = devtools.formatters.state.get_depth_budget();
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (initial_hierarchy_depth_budget - (1));
}
})();
var depth = devtools.formatters.budgeting.determine_depth(json_ml);
var final_QMARK_ = cljs.core.not(devtools.formatters.budgeting.has_any_object_reference_QMARK_(json_ml));
var needed_depth = ((final_QMARK_)?depth:(depth + devtools.formatters.budgeting.header_expander_depth_cost));
if((remaining_depth_budget >= needed_depth)){
return devtools.formatters.budgeting.distribute_budget_BANG_(json_ml,remaining_depth_budget);
} else {
var expander_ml = devtools.formatters.templating.render_markup(devtools.formatters.markup._LT_header_expander_GT_(value));
devtools.formatters.budgeting.add_over_budget_value_BANG_(value);

return expander_ml;
}
} else {
return json_ml;
}
});

//# sourceMappingURL=devtools.formatters.budgeting.js.map
