goog.provide('re_com.tree_select');
re_com.tree_select.tree_select_dropdown_parts_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"legacy","legacy",1434943289),new cljs.core.Keyword(null,"level","level",1290497552),(0),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-dropdown",new cljs.core.Keyword(null,"impl","impl",1677848700),"[tree-select-dropdown]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.Keyword(null,"level","level",1290497552),(1),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-dropdown-wrapper",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"anchor","anchor",1549638489),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-dropdown-anchor",new cljs.core.Keyword(null,"impl","impl",1677848700),"[h-box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"counter","counter",804008177),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-dropdown-counter",new cljs.core.Keyword(null,"impl","impl",1677848700),"[box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"anchor-expander","anchor-expander",511699040),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-dropdown-anchor-expander",new cljs.core.Keyword(null,"impl","impl",1677848700),"[box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"backdrop","backdrop",-1291357381),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-dropdown-backdrop",new cljs.core.Keyword(null,"impl","impl",1677848700),"[:div]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"dropdown-wrapper","dropdown-wrapper",-1728548532),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-dropdown-dropdown-wrapper",new cljs.core.Keyword(null,"impl","impl",1677848700),"[v-box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-dropdown-body",new cljs.core.Keyword(null,"impl","impl",1677848700),"[tree-select]"], null)], null):null);
re_com.tree_select.tree_select_dropdown_parts = ((re_com.config.include_args_desc_QMARK_)?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),re_com.tree_select.tree_select_dropdown_parts_desc)):null);
re_com.tree_select.tree_select_parts_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"legacy","legacy",1434943289),new cljs.core.Keyword(null,"level","level",1290497552),(0),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select",new cljs.core.Keyword(null,"impl","impl",1677848700),"[tree-select]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.Keyword(null,"level","level",1290497552),(1),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-wrapper",new cljs.core.Keyword(null,"impl","impl",1677848700),"[v-box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choice","choice",-1375170727),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-choice",new cljs.core.Keyword(null,"impl","impl",1677848700),"[h-box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"group","group",582596132),new cljs.core.Keyword(null,"level","level",1290497552),(2),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-group",new cljs.core.Keyword(null,"impl","impl",1677848700),"[h-box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"offset","offset",296498311),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-offset",new cljs.core.Keyword(null,"impl","impl",1677848700),"[box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"expander","expander",379138924),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-expander",new cljs.core.Keyword(null,"impl","impl",1677848700),"[box]"], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"checkbox","checkbox",1612615655),new cljs.core.Keyword(null,"level","level",1290497552),(3),new cljs.core.Keyword(null,"class","class",-2030961996),"rc-tree-select-checkbox",new cljs.core.Keyword(null,"impl","impl",1677848700),"[checkbox]"], null)], null):null);
re_com.tree_select.tree_select_parts = ((re_com.config.include_args_desc_QMARK_)?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),re_com.tree_select.tree_select_parts_desc)):null);
re_com.tree_select.tree_select_args_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 18, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choices","choices",1385611597),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"vector of maps | r/atom",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.vector_of_maps_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Each map represents a choice. Values corresponding to id, & label are extracted by the functions ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":id-fn"], null)," & ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label-fn"], null),". See below."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"model","model",331153215),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"a set of ids | r/atom",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The set of the ids for currently selected choices. If nil or empty, see ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":placeholder"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"groups","groups",-136896102),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),"(reagent/atom nil)",new cljs.core.Keyword(null,"type","type",1174270348),"a set of paths | r/atom",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The set of currently expanded group paths."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"initial-expanded-groups","initial-expanded-groups",1858995438),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"keyword | set of paths",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"How to expand groups when the component first mounts."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-change","on-change",-732046149),new cljs.core.Keyword(null,"required","required",1807647006),true,new cljs.core.Keyword(null,"type","type",1174270348),"[set of choice ids, set of group vectors]  -> nil",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.fn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"This function is called whenever the selection changes. It is also responsible for updating the value of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":model"], null)," as needed."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"min-width","min-width",1926193728),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"Minimum width of the outer wrapper."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"Maximum width of the outer wrapper."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"min-height","min-height",398480837),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"Minimum height of the outer wrapper."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-height","max-height",-612563804),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"Maximum height of the outer wrapper."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),"if true, no user selection is allowed"], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"choice-disabled-fn","choice-disabled-fn",-1164473367),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),null,new cljs.core.Keyword(null,"type","type",1174270348),"choice map -> boolean",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Predicate on the set of maps given by ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"choices"], null),". Disables the subset of choices for which ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"choice-disabled?"], null)," returns ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"true"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),":label",new cljs.core.Keyword(null,"type","type",1174270348),"map -> hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A function which can turn a choice into a displayable label. Will be called for each element in ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":choices"], null),". Given one argument, a choice map, it returns a string or hiccup."], null)], null),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"group-label-fn","group-label-fn",-1050449155),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),"(comp name last)",new cljs.core.Keyword(null,"type","type",1174270348),"vector -> hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A function which can turn a group vector into a displayable label. Will be called for each element in ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":groups"], null),". Given one argument, a group vector, it returns a string or hiccup."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"CSS class names, space separated (applies to the outer container)"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"CSS style map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.css_style_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"CSS styles to add or override (applies to the outer container)"], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"HTML attr map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.html_attr_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"HTML attributes, like ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-mouse-move"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"No ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":class"], null)," or ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":style"], null),"allowed (applies to the outer container)"], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"parts","parts",849007691),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.parts_QMARK_(re_com.tree_select.tree_select_parts),new cljs.core.Keyword(null,"description","description",-1428560544),"See Parts section below."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"src","src",-1651076051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.map_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Used in dev builds to assist with debugging. Source code coordinates map containing keys",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":file"], null),"and",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":line"], null),". See 'Debugging'."], null)], null)], null):null);
re_com.tree_select.tree_select_dropdown_args_desc = ((re_com.config.include_args_desc_QMARK_)?cljs.core.into.cljs$core$IFn$_invoke$arity$2(re_com.util.remove_id_item.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"parts","parts",849007691),re_com.tree_select.tree_select_args_desc,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"name","name",1843675177)], null)], 0)),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),"(Dropdown version only). Background text shown when there's no selection."], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"field-label-fn","field-label-fn",-1349527853),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map -> string or hiccup",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),(""+"(Dropdown version only). Accepts a map, including keys :items, :group-label-fn and :label-fn. "+"Can return a string or hiccup, which will be rendered inside the dropdown anchor box.")], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"alt-text-fn","alt-text-fn",622903484),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map -> string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),(""+"(Dropdown version only). Accepts a map, including keys :items, :group-label-fn and :label-fn. "+"Returns a string that will display in the native browser tooltip that appears on mouse hover.")], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"parts","parts",849007691),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"map",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.parts_QMARK_(re_com.tree_select.tree_select_dropdown_parts),new cljs.core.Keyword(null,"description","description",-1428560544),"See Parts section below."], null)], null)):null);
re_com.tree_select.backdrop = (function re_com$tree_select$backdrop(p__21940){
var map__21941 = p__21940;
var map__21941__$1 = cljs.core.__destructure_map(map__21941);
var opacity = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21941__$1,new cljs.core.Keyword(null,"opacity","opacity",397153780));
var on_click = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21941__$1,new cljs.core.Keyword(null,"on-click","on-click",1632826543));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21941__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),(""+"noselect rc-backdrop "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"backdrop","backdrop",-1291357381),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"position","position",-2011731912),"fixed",new cljs.core.Keyword(null,"left","left",-399115937),"0px",new cljs.core.Keyword(null,"top","top",-1856271961),"0px",new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"height","height",1025178622),"100%",new cljs.core.Keyword(null,"background-color","background-color",570434026),"black",new cljs.core.Keyword(null,"opacity","opacity",397153780),(function (){var or__5142__auto__ = opacity;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return 0.0;
}
})()], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"backdrop","backdrop",-1291357381),new cljs.core.Keyword(null,"style","style",-496642736)], null))),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(cljs.core.truth_(on_click)?(function (event){
(on_click.cljs$core$IFn$_invoke$arity$0 ? on_click.cljs$core$IFn$_invoke$arity$0() : on_click.call(null));

return null;
}):null)], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"backdrop","backdrop",-1291357381),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)))], 0))], null);
});
re_com.tree_select.offset = (function re_com$tree_select$offset(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22406 = arguments.length;
var i__5877__auto___22407 = (0);
while(true){
if((i__5877__auto___22407 < len__5876__auto___22406)){
args__5882__auto__.push((arguments[i__5877__auto___22407]));

var G__22408 = (i__5877__auto___22407 + (1));
i__5877__auto___22407 = G__22408;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.tree_select.offset.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.tree_select.offset.cljs$core$IFn$_invoke$arity$variadic = (function (p__21980){
var map__21981 = p__21980;
var map__21981__$1 = cljs.core.__destructure_map(map__21981);
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21981__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var level = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21981__$1,new cljs.core.Keyword(null,"level","level",1290497552));
return new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tree_select.cljs",new cljs.core.Keyword(null,"line","line",212345235),193], null)),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"visibility","visibility",1338380893),"hidden"], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"offset","offset",296498311),new cljs.core.Keyword(null,"style","style",-496642736)], null))),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tree-select-offset "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"offset","offset",296498311),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"offset","offset",296498311),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)),new cljs.core.Keyword(null,"child","child",623967545),cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(level,"\u2BC8"))], null);
}));

(re_com.tree_select.offset.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.tree_select.offset.cljs$lang$applyTo = (function (seq21976){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21976));
}));

re_com.tree_select.choice_checkbox = (function re_com$tree_select$choice_checkbox(p__22005){
var map__22010 = p__22005;
var map__22010__$1 = cljs.core.__destructure_map(map__22010);
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22010__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var checked_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22010__$1,new cljs.core.Keyword(null,"checked?","checked?",2024809091));
var toggle_BANG_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22010__$1,new cljs.core.Keyword(null,"toggle!","toggle!",221329013));
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22010__$1,new cljs.core.Keyword(null,"label","label",1718410804));
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22010__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var attr = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22010__$1,new cljs.core.Keyword(null,"attr","attr",-604132353));
return new cljs.core.PersistentVector(null, 17, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.checkbox.checkbox,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tree_select.cljs",new cljs.core.Keyword(null,"line","line",212345235),201], null)),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"checkbox","checkbox",1612615655),new cljs.core.Keyword(null,"style","style",-496642736)], null)),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tree-select-checkbox "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"checkbox","checkbox",1612615655),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.into.cljs$core$IFn$_invoke$arity$2(attr,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"checkbox","checkbox",1612615655),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))),new cljs.core.Keyword(null,"model","model",331153215),checked_QMARK_,new cljs.core.Keyword(null,"on-change","on-change",-732046149),toggle_BANG_,new cljs.core.Keyword(null,"label","label",1718410804),label,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),disabled_QMARK_], null);
});
re_com.tree_select.choice_item = (function re_com$tree_select$choice_item(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22419 = arguments.length;
var i__5877__auto___22420 = (0);
while(true){
if((i__5877__auto___22420 < len__5876__auto___22419)){
args__5882__auto__.push((arguments[i__5877__auto___22420]));

var G__22421 = (i__5877__auto___22420 + (1));
i__5877__auto___22420 = G__22421;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.tree_select.choice_item.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.tree_select.choice_item.cljs$core$IFn$_invoke$arity$variadic = (function (p__22035){
var map__22037 = p__22035;
var map__22037__$1 = cljs.core.__destructure_map(map__22037);
var props = map__22037__$1;
var level = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22037__$1,new cljs.core.Keyword(null,"level","level",1290497552));
var showing_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22037__$1,new cljs.core.Keyword(null,"showing?","showing?",2094921488));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22037__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
if(cljs.core.truth_(showing_QMARK_)){
return new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tree_select.cljs",new cljs.core.Keyword(null,"line","line",212345235),213], null)),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choice","choice",-1375170727),new cljs.core.Keyword(null,"style","style",-496642736)], null)),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tree-select-choice "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choice","choice",-1375170727),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"choice","choice",-1375170727),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tree_select.offset,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"level","level",1290497552),level], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tree_select.choice_checkbox,props], null)], null)], null);
} else {
return null;
}
}));

(re_com.tree_select.choice_item.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.tree_select.choice_item.cljs$lang$applyTo = (function (seq22030){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq22030));
}));

re_com.tree_select.group_item = (function re_com$tree_select$group_item(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22438 = arguments.length;
var i__5877__auto___22439 = (0);
while(true){
if((i__5877__auto___22439 < len__5876__auto___22438)){
args__5882__auto__.push((arguments[i__5877__auto___22439]));

var G__22442 = (i__5877__auto___22439 + (1));
i__5877__auto___22439 = G__22442;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.tree_select.group_item.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.tree_select.group_item.cljs$core$IFn$_invoke$arity$variadic = (function (p__22054){
var map__22055 = p__22054;
var map__22055__$1 = cljs.core.__destructure_map(map__22055);
var props = map__22055__$1;
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var hide_show_BANG_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"hide-show!","hide-show!",-387822116));
var checked_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"checked?","checked?",2024809091));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var level = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"level","level",1290497552));
var showing_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"showing?","showing?",2094921488));
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"label","label",1718410804));
var toggle_BANG_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"toggle!","toggle!",221329013));
var open_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22055__$1,new cljs.core.Keyword(null,"open?","open?",1238443125));
if(cljs.core.truth_(showing_QMARK_)){
return new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tree_select.cljs",new cljs.core.Keyword(null,"line","line",212345235),224], null)),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"group","group",582596132),new cljs.core.Keyword(null,"style","style",-496642736)], null)),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tree-select-group "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"group","group",582596132),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"group","group",582596132),new cljs.core.Keyword(null,"attr","attr",-604132353)], null)),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tree_select.offset,new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"level","level",1290497552),(level - (1))], null),new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tree_select.cljs",new cljs.core.Keyword(null,"line","line",212345235),231], null)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),hide_show_BANG_], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expander","expander",379138924),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cursor","cursor",1011937484),"pointer"], null),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expander","expander",379138924),new cljs.core.Keyword(null,"style","style",-496642736)], null))),new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tree-select-expander "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expander","expander",379138924),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"child","child",623967545),(cljs.core.truth_(open_QMARK_)?"\u2BC6":"\u2BC8")], null)," ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tree_select.choice_checkbox,cljs.core.into.cljs$core$IFn$_invoke$arity$2(props,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"attr","attr",-604132353),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),(function (p1__22042_SHARP_){
if(cljs.core.truth_(p1__22042_SHARP_)){
return (p1__22042_SHARP_.indeterminate = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"some","some",-1951079573),checked_QMARK_));
} else {
return null;
}
})], null)], null))], null)], null)], null);
} else {
return null;
}
}));

(re_com.tree_select.group_item.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.tree_select.group_item.cljs$lang$applyTo = (function (seq22043){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq22043));
}));

re_com.tree_select.group_QMARK_ = cljs.core.comp.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"group","group",582596132),null], null), null),new cljs.core.Keyword(null,"type","type",1174270348));
re_com.tree_select.as_v = (function re_com$tree_select$as_v(x){
if((!((x == null)))){
if(cljs.core.vector_QMARK_(x)){
return x;
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [x], null);
}
} else {
return null;
}
});
re_com.tree_select.ancestor_paths = (function re_com$tree_select$ancestor_paths(path){
var G__22077 = path;
var G__22077__$1 = (((G__22077 == null))?null:re_com.tree_select.as_v(G__22077));
var G__22077__$2 = (((G__22077__$1 == null))?null:cljs.core.iterate(cljs.core.butlast,G__22077__$1));
var G__22077__$3 = (((G__22077__$2 == null))?null:cljs.core.take_while.cljs$core$IFn$_invoke$arity$2(cljs.core.identity,G__22077__$2));
if((G__22077__$3 == null)){
return null;
} else {
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.vec,G__22077__$3);
}
});
re_com.tree_select.infer_groups = (function re_com$tree_select$infer_groups(items){
return cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentHashSet.EMPTY,cljs.core.comp.cljs$core$IFn$_invoke$arity$variadic(cljs.core.keep.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"group","group",582596132)),cljs.core.map.cljs$core$IFn$_invoke$arity$1(re_com.tree_select.as_v),cljs.core.mapcat.cljs$core$IFn$_invoke$arity$1(re_com.tree_select.ancestor_paths),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.map.cljs$core$IFn$_invoke$arity$1((function (p1__22081_SHARP_){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"group","group",582596132),new cljs.core.Keyword(null,"group","group",582596132),p1__22081_SHARP_], null);
})),cljs.core.distinct.cljs$core$IFn$_invoke$arity$0()], 0)),items);
});
re_com.tree_select.infer_groups_STAR_ = cljs.core.memoize(re_com.tree_select.infer_groups);
re_com.tree_select.toggle = (function re_com$tree_select$toggle(s,k){
if(cljs.core.contains_QMARK_(s,k)){
return cljs.core.disj.cljs$core$IFn$_invoke$arity$2(s,k);
} else {
return cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core.conj,cljs.core.PersistentHashSet.EMPTY)(s,k);
}
});
re_com.tree_select.descendant_QMARK_ = (function re_com$tree_select$descendant_QMARK_(group_v,item){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(group_v,cljs.core.vec(cljs.core.take.cljs$core$IFn$_invoke$arity$2(cljs.core.count(group_v),re_com.tree_select.as_v(new cljs.core.Keyword(null,"group","group",582596132).cljs$core$IFn$_invoke$arity$1(item)))));
});
re_com.tree_select.filter_descendants = (function re_com$tree_select$filter_descendants(group_v,choices){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(re_com.tree_select.descendant_QMARK_,group_v),choices);
});
re_com.tree_select.filter_descendants_STAR_ = cljs.core.memoize(re_com.tree_select.filter_descendants);
re_com.tree_select.sort_items = (function re_com$tree_select$sort_items(items){
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(cljs.core.juxt.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2((function (p1__22125_SHARP_){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,re_com.tree_select.as_v(p1__22125_SHARP_));
}),new cljs.core.Keyword(null,"group","group",582596132)),cljs.core.complement(re_com.tree_select.group_QMARK_)),items);
});
re_com.tree_select.group_label = cljs.core.comp.cljs$core$IFn$_invoke$arity$variadic(clojure.string.capitalize,cljs.core.name,cljs.core.last,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"group","group",582596132)], 0));
re_com.tree_select.current_choices = (function re_com$tree_select$current_choices(model,choices){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.EMPTY,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(model,new cljs.core.Keyword(null,"id","id",-1388402092)),choices));
});
re_com.tree_select.current_groups = (function re_com$tree_select$current_groups(current_choices){
return re_com.tree_select.infer_groups_STAR_(current_choices);
});
re_com.tree_select.full_groups = (function re_com$tree_select$full_groups(model,choices){
var current_choices = re_com.tree_select.current_choices(model,choices);
var current_groups = re_com.tree_select.current_groups(current_choices);
var full_QMARK_ = (function (p__22193){
var map__22195 = p__22193;
var map__22195__$1 = cljs.core.__destructure_map(map__22195);
var group = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22195__$1,new cljs.core.Keyword(null,"group","group",582596132));
var group_v = re_com.tree_select.as_v(group);
var descendant_ids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092),re_com.tree_select.filter_descendants_STAR_(group_v,choices));
return cljs.core.every_QMARK_(model,descendant_ids);
});
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.EMPTY,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(full_QMARK_,current_groups));
});
re_com.tree_select.tree_select = (function re_com$tree_select$tree_select(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22464 = arguments.length;
var i__5877__auto___22465 = (0);
while(true){
if((i__5877__auto___22465 < len__5876__auto___22464)){
args__5882__auto__.push((arguments[i__5877__auto___22465]));

var G__22466 = (i__5877__auto___22465 + (1));
i__5877__auto___22465 = G__22466;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.tree_select.tree_select.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.tree_select.tree_select.cljs$core$IFn$_invoke$arity$variadic = (function (p__22213){
var map__22214 = p__22213;
var map__22214__$1 = cljs.core.__destructure_map(map__22214);
var model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22214__$1,new cljs.core.Keyword(null,"model","model",331153215));
var choices = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22214__$1,new cljs.core.Keyword(null,"choices","choices",1385611597));
var initial_expanded_groups = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22214__$1,new cljs.core.Keyword(null,"initial-expanded-groups","initial-expanded-groups",1858995438));
var id_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22214__$1,new cljs.core.Keyword(null,"id-fn","id-fn",316222798),new cljs.core.Keyword(null,"id","id",-1388402092));
var expanded_groups = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var temp__5829__auto___22468 = re_com.util.deref_or_value(initial_expanded_groups);
if((temp__5829__auto___22468 == null)){
} else {
var initial_expanded_groups_22469__$1 = temp__5829__auto___22468;
cljs.core.reset_BANG_(expanded_groups,(function (){var G__22223 = initial_expanded_groups_22469__$1;
var G__22223__$1 = (((G__22223 instanceof cljs.core.Keyword))?G__22223.fqn:null);
switch (G__22223__$1) {
case "all":
return cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"group","group",582596132),re_com.tree_select.infer_groups(choices)));

break;
case "none":
return cljs.core.PersistentHashSet.EMPTY;

break;
case "chosen":
return cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentHashSet.EMPTY,cljs.core.comp.cljs$core$IFn$_invoke$arity$3(cljs.core.filter.cljs$core$IFn$_invoke$arity$1((function (p1__22202_SHARP_){
return cljs.core.contains_QMARK_(re_com.util.deref_or_value(model),(id_fn.cljs$core$IFn$_invoke$arity$1 ? id_fn.cljs$core$IFn$_invoke$arity$1(p1__22202_SHARP_) : id_fn.call(null,p1__22202_SHARP_)));
})),cljs.core.keep.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"group","group",582596132)),cljs.core.mapcat.cljs$core$IFn$_invoke$arity$1(re_com.tree_select.ancestor_paths)),choices);

break;
default:
return initial_expanded_groups_22469__$1;

}
})());
}

return (function() { 
var re_com$tree_select$tree_select_render__delegate = function (p__22226){
var map__22228 = p__22226;
var map__22228__$1 = cljs.core.__destructure_map(map__22228);
var args = map__22228__$1;
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var group_label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"group-label-fn","group-label-fn",-1050449155));
var attr = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"attr","attr",-604132353));
var min_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"min-width","min-width",1926193728));
var label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263));
var max_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"max-height","max-height",-612563804));
var min_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"min-height","min-height",398480837));
var choice_disabled_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"choice-disabled-fn","choice-disabled-fn",-1164473367));
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var max_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"max-width","max-width",-1939924051));
var choices__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"choices","choices",1385611597));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22228__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var or__5142__auto__ = (((!(goog.DEBUG)))?null:re_com.validate.validate_args(re_com.validate.extract_arg_data(re_com.tree_select.tree_select_args_desc),args));
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var choices__$2 = re_com.util.deref_or_value(choices__$1);
var disabled_QMARK___$1 = re_com.util.deref_or_value(disabled_QMARK_);
var model__$1 = re_com.util.deref_or_value(model);
var label_fn__$1 = (function (){var or__5142__auto____$1 = label_fn;
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return new cljs.core.Keyword(null,"label","label",1718410804);
}
})();
var group_label_fn__$1 = (function (){var or__5142__auto____$1 = group_label_fn;
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return re_com.tree_select.group_label;
}
})();
var items = re_com.tree_select.sort_items(cljs.core.into.cljs$core$IFn$_invoke$arity$2(choices__$2,re_com.tree_select.infer_groups_STAR_(choices__$2)));
var item = (function (item_props){
var map__22242 = cljs.core.update.cljs$core$IFn$_invoke$arity$3(item_props,new cljs.core.Keyword(null,"group","group",582596132),re_com.tree_select.as_v);
var map__22242__$1 = cljs.core.__destructure_map(map__22242);
var item_props__$1 = map__22242__$1;
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22242__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var group = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22242__$1,new cljs.core.Keyword(null,"group","group",582596132));
if(cljs.core.truth_(re_com.tree_select.group_QMARK_(item_props__$1))){
var descendants = re_com.tree_select.filter_descendants_STAR_(group,choices__$2);
var descendant_ids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092),descendants);
var checked_QMARK_ = ((cljs.core.every_QMARK_(model__$1,descendant_ids))?new cljs.core.Keyword(null,"all","all",892129742):(cljs.core.truth_(cljs.core.some(model__$1,descendant_ids))?new cljs.core.Keyword(null,"some","some",-1951079573):null));
var new_model = cljs.core.set((function (){var G__22254 = model__$1;
var G__22255 = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092),(function (){var G__22258 = descendants;
if(cljs.core.truth_(choice_disabled_fn)){
return cljs.core.remove.cljs$core$IFn$_invoke$arity$2(choice_disabled_fn,G__22258);
} else {
return G__22258;
}
})());
var fexpr__22253 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"all","all",892129742),checked_QMARK_))?clojure.set.difference:clojure.set.union);
return (fexpr__22253.cljs$core$IFn$_invoke$arity$2 ? fexpr__22253.cljs$core$IFn$_invoke$arity$2(G__22254,G__22255) : fexpr__22253.call(null,G__22254,G__22255));
})());
var new_groups = cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentHashSet.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"group","group",582596132)),re_com.tree_select.full_groups(new_model,choices__$2));
return new cljs.core.PersistentVector(null, 23, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tree_select.group_item,new cljs.core.Keyword(null,"group","group",582596132),item_props__$1,new cljs.core.Keyword(null,"label","label",1718410804),(group_label_fn__$1.cljs$core$IFn$_invoke$arity$1 ? group_label_fn__$1.cljs$core$IFn$_invoke$arity$1(item_props__$1) : group_label_fn__$1.call(null,item_props__$1)),new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"hide-show!","hide-show!",-387822116),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(expanded_groups,re_com.tree_select.toggle,group);
}),new cljs.core.Keyword(null,"toggle!","toggle!",221329013),(function (event){
(on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(new_model,new_groups) : on_change.call(null,new_model,new_groups));

return null;
}),new cljs.core.Keyword(null,"open?","open?",1238443125),cljs.core.contains_QMARK_(cljs.core.deref(expanded_groups),group),new cljs.core.Keyword(null,"checked?","checked?",2024809091),checked_QMARK_,new cljs.core.Keyword(null,"model","model",331153215),model__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),(function (){var or__5142__auto____$1 = disabled_QMARK___$1;
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
if(cljs.core.truth_(choice_disabled_fn)){
return cljs.core.every_QMARK_(choice_disabled_fn,descendants);
} else {
return null;
}
}
})(),new cljs.core.Keyword(null,"showing?","showing?",2094921488),cljs.core.every_QMARK_(cljs.core.set(cljs.core.deref(expanded_groups)),cljs.core.rest(re_com.tree_select.ancestor_paths(group))),new cljs.core.Keyword(null,"level","level",1290497552),cljs.core.count(group)], null);
} else {
return new cljs.core.PersistentVector(null, 19, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tree_select.choice_item,new cljs.core.Keyword(null,"choice","choice",-1375170727),item_props__$1,new cljs.core.Keyword(null,"model","model",331153215),model__$1,new cljs.core.Keyword(null,"label","label",1718410804),(label_fn__$1.cljs$core$IFn$_invoke$arity$1 ? label_fn__$1.cljs$core$IFn$_invoke$arity$1(item_props__$1) : label_fn__$1.call(null,item_props__$1)),new cljs.core.Keyword(null,"parts","parts",849007691),parts,new cljs.core.Keyword(null,"showing?","showing?",2094921488),((cljs.core.not(group))?true:cljs.core.every_QMARK_(cljs.core.set(cljs.core.deref(expanded_groups)),re_com.tree_select.ancestor_paths(group))),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),(function (){var or__5142__auto____$1 = disabled_QMARK___$1;
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
if(cljs.core.truth_(choice_disabled_fn)){
return (choice_disabled_fn.cljs$core$IFn$_invoke$arity$1 ? choice_disabled_fn.cljs$core$IFn$_invoke$arity$1(item_props__$1) : choice_disabled_fn.call(null,item_props__$1));
} else {
return null;
}
}
})(),new cljs.core.Keyword(null,"toggle!","toggle!",221329013),(function (event){
var new_model_22474 = re_com.tree_select.toggle(model__$1,id);
var new_groups_22475 = cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentHashSet.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"group","group",582596132)),re_com.tree_select.full_groups(new_model_22474,choices__$2));
(on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(new_model_22474,new_groups_22475) : on_change.call(null,new_model_22474,new_groups_22475));

return null;
}),new cljs.core.Keyword(null,"checked?","checked?",2024809091),cljs.core.get.cljs$core$IFn$_invoke$arity$2(model__$1,id),new cljs.core.Keyword(null,"level","level",1290497552),(cljs.core.count(group) + (1))], null);
}
});
return new cljs.core.PersistentVector(null, 19, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.v_box,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tree_select.cljs",new cljs.core.Keyword(null,"line","line",212345235),355], null)),new cljs.core.Keyword(null,"min-width","min-width",1926193728),min_width,new cljs.core.Keyword(null,"max-width","max-width",-1939924051),max_width,new cljs.core.Keyword(null,"min-height","min-height",398480837),min_height,new cljs.core.Keyword(null,"max-height","max-height",-612563804),max_height,new cljs.core.Keyword(null,"class","class",-2030961996),(""+"rc-tree-select-wrapper "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(class$)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.Keyword(null,"class","class",-2030961996)], null)))),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"overflow-y","overflow-y",-1436589285),"auto"], null),style,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.Keyword(null,"style","style",-496642736)], null))], 0)),new cljs.core.Keyword(null,"attr","attr",-604132353),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([attr,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(parts,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"wrapper","wrapper",-969103524),new cljs.core.Keyword(null,"attr","attr",-604132353)], null))], 0)),new cljs.core.Keyword(null,"children","children",-940561982),cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(item,items)], null);
}
};
var re_com$tree_select$tree_select_render = function (var_args){
var p__22226 = null;
if (arguments.length > 0) {
var G__22476__i = 0, G__22476__a = new Array(arguments.length -  0);
while (G__22476__i < G__22476__a.length) {G__22476__a[G__22476__i] = arguments[G__22476__i + 0]; ++G__22476__i;}
  p__22226 = new cljs.core.IndexedSeq(G__22476__a,0,null);
} 
return re_com$tree_select$tree_select_render__delegate.call(this,p__22226);};
re_com$tree_select$tree_select_render.cljs$lang$maxFixedArity = 0;
re_com$tree_select$tree_select_render.cljs$lang$applyTo = (function (arglist__22477){
var p__22226 = cljs.core.seq(arglist__22477);
return re_com$tree_select$tree_select_render__delegate(p__22226);
});
re_com$tree_select$tree_select_render.cljs$core$IFn$_invoke$arity$variadic = re_com$tree_select$tree_select_render__delegate;
return re_com$tree_select$tree_select_render;
})()
;
}));

(re_com.tree_select.tree_select.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.tree_select.tree_select.cljs$lang$applyTo = (function (seq22212){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq22212));
}));

re_com.tree_select.field_label = (function re_com$tree_select$field_label(p__22280){
var map__22282 = p__22280;
var map__22282__$1 = cljs.core.__destructure_map(map__22282);
var items = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22282__$1,new cljs.core.Keyword(null,"items","items",1031954938));
var group_label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22282__$1,new cljs.core.Keyword(null,"group-label-fn","group-label-fn",-1050449155));
var label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22282__$1,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263));
var item_label_fn = (function (p1__22277_SHARP_){
var fexpr__22283 = (cljs.core.truth_(re_com.tree_select.group_QMARK_(p1__22277_SHARP_))?group_label_fn:label_fn);
return (fexpr__22283.cljs$core$IFn$_invoke$arity$1 ? fexpr__22283.cljs$core$IFn$_invoke$arity$1(p1__22277_SHARP_) : fexpr__22283.call(null,p1__22277_SHARP_));
});
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(", ",cljs.core.map.cljs$core$IFn$_invoke$arity$2(item_label_fn,items));
});
re_com.tree_select.labelable_items = (function re_com$tree_select$labelable_items(model,choices){
var current_choices = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.EMPTY,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(model,new cljs.core.Keyword(null,"id","id",-1388402092)),choices));
var current_groups = re_com.tree_select.infer_groups_STAR_(current_choices);
var full_QMARK_ = (function (p__22288){
var map__22289 = p__22288;
var map__22289__$1 = cljs.core.__destructure_map(map__22289);
var group = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22289__$1,new cljs.core.Keyword(null,"group","group",582596132));
var group_v = re_com.tree_select.as_v(group);
var descendant_ids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092),re_com.tree_select.filter_descendants_STAR_(group_v,choices));
return cljs.core.every_QMARK_(model,descendant_ids);
});
var full_groups = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.EMPTY,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(full_QMARK_,current_groups));
var highest_groups = (function (){var G__22293 = cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.count,new cljs.core.Keyword(null,"group","group",582596132)),full_groups);
var vec__22294 = G__22293;
var seq__22295 = cljs.core.seq(vec__22294);
var first__22296 = cljs.core.first(seq__22295);
var seq__22295__$1 = cljs.core.next(seq__22295);
var group = first__22296;
var remainder = seq__22295__$1;
var acc = cljs.core.PersistentVector.EMPTY;
var G__22293__$1 = G__22293;
var acc__$1 = acc;
while(true){
var vec__22310 = G__22293__$1;
var seq__22311 = cljs.core.seq(vec__22310);
var first__22312 = cljs.core.first(seq__22311);
var seq__22311__$1 = cljs.core.next(seq__22311);
var group__$1 = first__22312;
var remainder__$1 = seq__22311__$1;
var acc__$2 = acc__$1;
if(cljs.core.not(group__$1)){
return acc__$2;
} else {
var group_v = re_com.tree_select.as_v(new cljs.core.Keyword(null,"group","group",582596132).cljs$core$IFn$_invoke$arity$1(group__$1));
var G__22478 = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(re_com.tree_select.descendant_QMARK_,group_v),remainder__$1);
var G__22479 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(acc__$2,group__$1);
G__22293__$1 = G__22478;
acc__$1 = G__22479;
continue;
}
break;
}
})();
var highest_group_descendants = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.EMPTY,cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p1__22285_SHARP_){
return re_com.tree_select.filter_descendants_STAR_(new cljs.core.Keyword(null,"group","group",582596132).cljs$core$IFn$_invoke$arity$1(p1__22285_SHARP_),current_choices);
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([highest_groups], 0)));
return re_com.tree_select.sort_items(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(highest_group_descendants,cljs.core.into.cljs$core$IFn$_invoke$arity$2(current_choices,highest_groups)));
});
re_com.tree_select.tree_select_dropdown = (function re_com$tree_select$tree_select_dropdown(_){
var showing_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
return (function() { 
var re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render__delegate = function (p__22338){
var map__22340 = p__22338;
var map__22340__$1 = cljs.core.__destructure_map(map__22340);
var args = map__22340__$1;
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var alt_text_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"alt-text-fn","alt-text-fn",622903484));
var group_label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"group-label-fn","group-label-fn",-1050449155));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"height","height",1025178622));
var model = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"model","model",331153215));
var min_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"min-width","min-width",1926193728));
var label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22340__$1,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"label","label",1718410804));
var max_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"max-height","max-height",-612563804));
var min_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"min-height","min-height",398480837));
var placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22340__$1,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Select an item...");
var parts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"parts","parts",849007691));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"width","width",-384071477));
var main_theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"main-theme","main-theme",-411793492));
var max_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"max-width","max-width",-1939924051));
var choices = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"choices","choices",1385611597));
var id_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"id-fn","id-fn",316222798));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
var theme_vars = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"theme-vars","theme-vars",-1383796847));
var field_label_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"field-label-fn","field-label-fn",-1349527853));
var base_theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22340__$1,new cljs.core.Keyword(null,"base-theme","base-theme",-1857412877));
var state = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"enable","enable",-1839114332),((cljs.core.not(disabled_QMARK_))?new cljs.core.Keyword(null,"enabled","enabled",1195909756):new cljs.core.Keyword(null,"disabled","disabled",-1529784218))], null);
var themed = (function (part,props){
return re_com.theme.apply(props,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"state","state",-1988618099),state,new cljs.core.Keyword(null,"part","part",77757738),part,new cljs.core.Keyword(null,"transition!","transition!",123167659),(function (){
return cljs.core.List.EMPTY;
})], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"variables","variables",1563680814),theme_vars,new cljs.core.Keyword(null,"base","base",185279322),base_theme,new cljs.core.Keyword(null,"main","main",-2117802661),main_theme,new cljs.core.Keyword(null,"user","user",1532431356),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [theme,(re_com.theme.parts.cljs$core$IFn$_invoke$arity$1 ? re_com.theme.parts.cljs$core$IFn$_invoke$arity$1(parts) : re_com.theme.parts.call(null,parts)),(re_com.theme.args.cljs$core$IFn$_invoke$arity$1 ? re_com.theme.args.cljs$core$IFn$_invoke$arity$1(args) : re_com.theme.args.call(null,args))], null)], null));
});
var label_fn__$1 = (function (){var or__5142__auto__ = label_fn;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return new cljs.core.Keyword(null,"label","label",1718410804);
}
})();
var alt_text_fn__$1 = (function (){var or__5142__auto__ = alt_text_fn;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (function (p1__22320_SHARP_){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(", ",cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (){var or__5142__auto____$1 = label_fn__$1;
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return new cljs.core.Keyword(null,"label","label",1718410804);
}
})(),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(p1__22320_SHARP_)));
});
}
})();
var group_label_fn__$1 = (function (){var or__5142__auto__ = group_label_fn;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.comp.cljs$core$IFn$_invoke$arity$3(cljs.core.name,cljs.core.last,new cljs.core.Keyword(null,"group","group",582596132));
}
})();
var field_label_fn__$1 = (function (){var or__5142__auto__ = field_label_fn;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return re_com.tree_select.field_label;
}
})();
var labelable_items = re_com.tree_select.labelable_items(re_com.util.deref_or_value(model),choices);
var anchor_label = (function (){var G__22356 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"items","items",1031954938),labelable_items,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),label_fn__$1,new cljs.core.Keyword(null,"group-label-fn","group-label-fn",-1050449155),group_label_fn__$1], null);
return (field_label_fn__$1.cljs$core$IFn$_invoke$arity$1 ? field_label_fn__$1.cljs$core$IFn$_invoke$arity$1(G__22356) : field_label_fn__$1.call(null,G__22356));
})();
var anchor = (function (p__22357){
var map__22358 = p__22357;
var map__22358__$1 = cljs.core.__destructure_map(map__22358);
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22358__$1,new cljs.core.Keyword(null,"label","label",1718410804));
var placeholder__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22358__$1,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083));
var model__$1 = re_com.util.deref_or_value(model);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,themed(new cljs.core.Keyword("re-com.tree-select","dropdown-anchor","re-com.tree-select/dropdown-anchor",-798171263),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"width","width",-384071477),width,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"height","height",1025178622),height], null),new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [(((!(cljs.core.empty_QMARK_(model__$1))))?label:placeholder__$1),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.gap,new cljs.core.Keyword(null,"src","src",-1651076051),(((!(goog.DEBUG)))?null:new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"file","file",-1269645878),"re_com/tree_select.cljs",new cljs.core.Keyword(null,"line","line",212345235),427], null)),new cljs.core.Keyword(null,"size","size",1098693007),"1"], null),(function (){var temp__5825__auto__ = cljs.core.seq(model__$1);
if(temp__5825__auto__){
var model__$2 = temp__5825__auto__;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,themed(new cljs.core.Keyword("re-com.tree-select","dropdown-counter","re-com.tree-select/dropdown-counter",175574407),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"child","child",623967545),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(model__$2)))], null))], null);
} else {
return null;
}
})(),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"enable","enable",-1839114332).cljs$core$IFn$_invoke$arity$1(state),new cljs.core.Keyword(null,"disabled","disabled",-1529784218)))?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.box,themed(new cljs.core.Keyword("re-com.tree-select","dropdown-anchor-expander","re-com.tree-select/dropdown-anchor-expander",-755965282),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"child","child",623967545),(cljs.core.truth_(cljs.core.deref(showing_QMARK_))?"\u25B2":"\u25BC")], null))], null))], null)], null))], null);
});
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.dropdown.dropdown,new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"anchor","anchor",1549638489),anchor,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),(function (){var G__22375 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"items","items",1031954938),labelable_items,new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),label_fn__$1,new cljs.core.Keyword(null,"group-label-fn","group-label-fn",-1050449155),group_label_fn__$1], null);
return (alt_text_fn__$1.cljs$core$IFn$_invoke$arity$1 ? alt_text_fn__$1.cljs$core$IFn$_invoke$arity$1(G__22375) : alt_text_fn__$1.call(null,G__22375));
})(),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"max-width","max-width",-1939924051),max_width,new cljs.core.Keyword(null,"white-space","white-space",-707351930),"nowrap",new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden",new cljs.core.Keyword(null,"text-overflow","text-overflow",-1022366814),"ellipsis"], null)], null),anchor_label], null),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),placeholder,new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.tree_select.tree_select,themed(new cljs.core.Keyword("re-com.tree-select","dropdown-body","re-com.tree-select/dropdown-body",720859555),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"min-width","min-width",1926193728),new cljs.core.Keyword(null,"label-fn","label-fn",-860923263),new cljs.core.Keyword(null,"max-height","max-height",-612563804),new cljs.core.Keyword(null,"min-height","min-height",398480837),new cljs.core.Keyword(null,"choices","choices",1385611597),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),new cljs.core.Keyword(null,"on-change","on-change",-732046149),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),new cljs.core.Keyword(null,"group-label-fn","group-label-fn",-1050449155),new cljs.core.Keyword(null,"model","model",331153215)],[min_width,label_fn__$1,max_height,min_height,choices,max_width,on_change,disabled_QMARK_,group_label_fn__$1,model]))], null),new cljs.core.Keyword(null,"model","model",331153215),showing_QMARK_,new cljs.core.Keyword(null,"theme","theme",-1247880880),theme,new cljs.core.Keyword(null,"parts","parts",849007691),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([parts,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"body-wrapper","body-wrapper",-1657089346),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"width","width",-384071477),(function (){var or__5142__auto__ = width;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "212px";
}
})(),new cljs.core.Keyword(null,"height","height",1025178622),"212px",new cljs.core.Keyword(null,"min-width","min-width",1926193728),min_width], null)], null)], null)], 0))], null)], null);
};
var re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render = function (var_args){
var p__22338 = null;
if (arguments.length > 0) {
var G__22489__i = 0, G__22489__a = new Array(arguments.length -  0);
while (G__22489__i < G__22489__a.length) {G__22489__a[G__22489__i] = arguments[G__22489__i + 0]; ++G__22489__i;}
  p__22338 = new cljs.core.IndexedSeq(G__22489__a,0,null);
} 
return re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render__delegate.call(this,p__22338);};
re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render.cljs$lang$maxFixedArity = 0;
re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render.cljs$lang$applyTo = (function (arglist__22490){
var p__22338 = cljs.core.seq(arglist__22490);
return re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render__delegate(p__22338);
});
re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render.cljs$core$IFn$_invoke$arity$variadic = re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render__delegate;
return re_com$tree_select$tree_select_dropdown_$_tree_select_dropdown_render;
})()
;
});

//# sourceMappingURL=re_com.tree_select.js.map
