goog.provide('re_com.nested_grid');
re_com.nested_grid.nested_grid_parts_desc = cljs.core.PersistentArrayMap.EMPTY;
re_com.nested_grid.nested_grid_parts = ((re_com.config.include_args_desc_QMARK_)?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),re_com.nested_grid.nested_grid_parts_desc)):null);
re_com.nested_grid.nested_grid_args_desc = ((re_com.config.include_args_desc_QMARK_)?new cljs.core.PersistentVector(null, 22, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"cell","cell",764245084),new cljs.core.Keyword(null,"default","default",-1987822328),"constantly nil",new cljs.core.Keyword(null,"type","type",1174270348),"part",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.part_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"String, hiccup or function. When a function, acceps keyword args ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-path"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-path"], null),". Returns either a string or hiccup, which will appear within a single grid cell."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"column-tree","column-tree",841938146),new cljs.core.Keyword(null,"default","default",-1987822328),"[]",new cljs.core.Keyword(null,"type","type",1174270348),"vector or seq of column-specs or column-trees",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.seq_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Describes a nested arrangement of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-spec"], null),"s. ","A spec's path derives from its depth within the hierarchy of vectors or seqs. "," When a non-vector A precedes a vector B, then the items of B are children of A."," When a non-vector C follows B, then C is a sibling of A."," This nesting can be arbitrarily deep."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"row-tree","row-tree",687483819),new cljs.core.Keyword(null,"default","default",-1987822328),"[]",new cljs.core.Keyword(null,"type","type",1174270348),"vector or seq of row-specs or row-trees",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.seq_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Describes a nested arrangement of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-spec"], null),"s. ","A spec's path derives from its depth within the hierarchy of vectors or seqs. "," When a non-vector A precedes a vector B, then the items of B are children of A."," When a non-vector C follows B, then C is a sibling of A."," This nesting can be arbitrarily deep."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"column-header","column-header",-1495823888),new cljs.core.Keyword(null,"type","type",1174270348),"part",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.part_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A string, hiccup, or function of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"{:keys [column-path]}"], null),"."," By default, returns the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label"], null),", ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":id"], null),", or else a string of the entire value of the last item in ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-path"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"row-header","row-header",1799050794),new cljs.core.Keyword(null,"type","type",1174270348),"part",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.part_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A string, hiccup, or function of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"{:keys [row-path]}"], null),"."," By default, returns the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":label"], null),", ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":id"], null),", or else a string of the entire value of the last item in ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-path"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"cell-wrapper","cell-wrapper",-1365129340),new cljs.core.Keyword(null,"type","type",1174270348),"part",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.part_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A wrapper div, responsible for positioning one ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":cell"], null)," within the css grid."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"column-header-wrapper","column-header-wrapper",-58772905),new cljs.core.Keyword(null,"type","type",1174270348),"part",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.part_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A wrapper div, responsible for positioning one ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-header"], null)," within the css grid."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"row-header-wrapper","row-header-wrapper",-1998665578),new cljs.core.Keyword(null,"type","type",1174270348),"part",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.part_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A wrapper div, responsible for positioning one ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-header"], null)," within the css grid."], null)], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"header-spacer-wrapper","header-spacer-wrapper",-1554966085),new cljs.core.Keyword(null,"type","type",1174270348),"part",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),re_com.validate.part_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"A wrapper responsible for positioning one ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":header-spacer"], null)," within the css grid."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"show-branch-paths?","show-branch-paths?",517719956),new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"default","default",-1987822328),"false",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.boolean_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"When ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"true"], null),", displays cells and headers for all ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-paths"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-paths"], null),", not just the leaf paths."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-height","max-height",-612563804),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"standard CSS max-height setting of the entire grid. ","Literally constrains the grid to the given width so that ","if the grid is taller than this it will add scrollbars. ","Ignored if value is larger than the combined width of ","all the rendered grid rows."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"max-width","max-width",-1939924051),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"standard CSS max-width setting of the entire grid. ","Literally constrains the grid to the given width so that ","if the grid is wider than this it will add scrollbars."," Ignored if value is larger than the combined width of all the rendered grid columns."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),new cljs.core.Keyword(null,"default","default",-1987822328),(30),new cljs.core.Keyword(null,"type","type",1174270348),"number",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The default height that a column-header will use. ","Can be overridden by a ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":height"], null),"key in the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-spec"], null),", or by component-local state."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"column-width","column-width",405119380),new cljs.core.Keyword(null,"default","default",-1987822328),(30),new cljs.core.Keyword(null,"type","type",1174270348),"number",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The default width that a column of grid cells will use. ","Can be overridden by a ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":height"], null),"key in the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-spec"], null),", or by component-local state."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"row-header-width","row-header-width",-1813601584),new cljs.core.Keyword(null,"default","default",-1987822328),(30),new cljs.core.Keyword(null,"type","type",1174270348),"number",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The default width that a row-header will use. ","Can be overridden by a ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":width"], null),"key in the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-spec"], null),", or by component-local state."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"row-width","row-width",1764289233),new cljs.core.Keyword(null,"default","default",-1987822328),(30),new cljs.core.Keyword(null,"type","type",1174270348),"number",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"The default width that a row of grid cells will use. ","Can be overridden by a ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":width"], null),"key in the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-spec"], null),", or by component-local state."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"When non-nil, adds a hiccup of ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":export-button-render"], null)," to the component tree."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-export","on-export",1803619391),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"function",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Called whenever the export button is clicked."," Expects keyword arguments ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":header-rows"], null)," and ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":main-rows"], null),"."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-export-cell","on-export-cell",1315067067),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"{:keys [row-path column-path]} -> string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 14, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Similar to ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":cell"], null),", but it should return a string value only."," After the export button is clicked, ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"nested-grid"], null)," maps ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-export-cell"], null),"over any cells marked for export, passing the ","results to ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-export"], null)," via the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":main-rows"], null)," prop."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-export-row-header","on-export-row-header",181805132),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"{:keys [row-path]} -> string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 14, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Similar to ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":row-header"], null),", but it should return a string value only."," After the export button is clicked, ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"nested-grid"], null)," maps ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-export-row-header"], null),"over any row headers marked for export, passing the ","results to ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-export"], null)," via the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":main-rows"], null)," prop."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"on-export-column-header","on-export-column-header",-899049368),new cljs.core.Keyword(null,"required","required",1807647006),false,new cljs.core.Keyword(null,"type","type",1174270348),"{:keys [column-path]} -> string",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.ifn_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 14, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"Similar to ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":column-header"], null),", but it should return a string value only."," After the export button is clicked, ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),"nested-grid"], null)," maps ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-export-column-header"], null),"over any cells marked for export, passing the ","results to ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":on-export"], null)," via the ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"code","code",1586293142),":header-rows"], null)," prop."], null)], null),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"show-selection-box?","show-selection-box?",-211398013),new cljs.core.Keyword(null,"default","default",-1987822328),false,new cljs.core.Keyword(null,"type","type",1174270348),"boolean",new cljs.core.Keyword(null,"validate-fn","validate-fn",1430169944),cljs.core.boolean_QMARK_,new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"when true, dragging the mouse causes an excel-style ","selection box to appear. When there is a selection box, any export behavior ","takes the bounds of that box into account. For instance, if 2 cells are ","selected, then only 2 cells are exported."], null)], null)], null):null);
re_com.nested_grid.descendant_QMARK_ = (function re_com$nested_grid$descendant_QMARK_(path_a,path_b){
return ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(path_a,path_b)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(path_a,cljs.core.vec(cljs.core.take.cljs$core$IFn$_invoke$arity$2(cljs.core.count(path_a),path_b)))));
});
re_com.nested_grid.ancestor_QMARK_ = (function re_com$nested_grid$ancestor_QMARK_(path_a,path_b){
return re_com.nested_grid.descendant_QMARK_(path_b,path_a);
});
re_com.nested_grid.spec_QMARK_ = cljs.core.some_fn.cljs$core$IFn$_invoke$arity$2(cljs.core.vector_QMARK_,cljs.core.seq_QMARK_);
re_com.nested_grid.item_QMARK_ = cljs.core.complement(re_com.nested_grid.spec_QMARK_);
re_com.nested_grid.header_spec__GT_header_paths = (function re_com$nested_grid$header_spec__GT_header_paths(var_args){
var G__21730 = arguments.length;
switch (G__21730) {
case 1:
return re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 3:
return re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$1 = (function (spec){
return re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentVector.EMPTY,cljs.core.PersistentVector.EMPTY,spec);
}));

(re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$3 = (function (path,acc,p__21732){
while(true){
var vec__21734 = p__21732;
var seq__21735 = cljs.core.seq(vec__21734);
var first__21736 = cljs.core.first(seq__21735);
var seq__21735__$1 = cljs.core.next(seq__21735);
var left = first__21736;
var vec__21737 = seq__21735__$1;
var right = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__21737,(0),null);
var remainder = vec__21737;
var next_acc = (cljs.core.truth_(re_com.nested_grid.item_QMARK_(left))?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(acc,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(path,left)):(cljs.core.truth_(re_com.nested_grid.spec_QMARK_(left))?re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$3(path,acc,left):null));
var next_path = (cljs.core.truth_((function (){var and__5140__auto__ = re_com.nested_grid.item_QMARK_(left);
if(cljs.core.truth_(and__5140__auto__)){
return re_com.nested_grid.spec_QMARK_(right);
} else {
return and__5140__auto__;
}
})())?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(path,left):(cljs.core.truth_((function (){var and__5140__auto__ = re_com.nested_grid.spec_QMARK_(left);
if(cljs.core.truth_(and__5140__auto__)){
var and__5140__auto____$1 = re_com.nested_grid.item_QMARK_(right);
if(cljs.core.truth_(and__5140__auto____$1)){
return cljs.core.seq(path);
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
})())?cljs.core.pop(path):path
));
if(cljs.core.empty_QMARK_(remainder)){
return next_acc;
} else {
var G__22313 = next_path;
var G__22314 = next_acc;
var G__22315 = remainder;
path = G__22313;
acc = G__22314;
p__21732 = G__22315;
continue;
}
break;
}
}));

(re_com.nested_grid.header_spec__GT_header_paths.cljs$lang$maxFixedArity = 3);

re_com.nested_grid.leaf_paths = (function re_com$nested_grid$leaf_paths(paths){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (paths__$1,p){
return cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.ancestor_QMARK_,p),paths__$1);
}),paths,paths);
});
re_com.nested_grid.spec__GT_headers_STAR_ = cljs.core.memoize(re_com.nested_grid.header_spec__GT_header_paths);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.Keyword(null,"b","b",1482224470),new cljs.core.Keyword(null,"c","c",-1763192079)], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null))){
} else {
throw (new Error("Assert failed: (= (header-spec->header-paths [:a :b :c]) [[:a] [:b] [:c]])"));
}
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.Keyword(null,"b","b",1482224470)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null))){
} else {
throw (new Error("Assert failed: (= (header-spec->header-paths [:a [:b :c]]) [[:a] [:a :b] [:a :c]])"));
}
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.Keyword(null,"b","b",1482224470),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null))){
} else {
throw (new Error("Assert failed: (= (header-spec->header-paths [:a :b [:c]]) [[:a] [:b] [:b :c]])"));
}
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null)], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.Keyword(null,"b","b",1482224470)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null))){
} else {
throw (new Error("Assert failed: (= (header-spec->header-paths [[:a [:b :c]]]) [[:a] [:a :b] [:a :c]])"));
}
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.header_spec__GT_header_paths.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"x","x",2099068185),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"y","y",-1757859776),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"b","b",1482224470),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null)], null)),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"x","x",2099068185)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"x","x",2099068185),new cljs.core.Keyword(null,"b","b",1482224470)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"x","x",2099068185),new cljs.core.Keyword(null,"c","c",-1763192079)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"y","y",-1757859776)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"y","y",-1757859776),new cljs.core.Keyword(null,"b","b",1482224470)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"y","y",-1757859776),new cljs.core.Keyword(null,"c","c",-1763192079)], null)], null))){
} else {
throw (new Error("Assert failed: (= (header-spec->header-paths [[:x [:b :c]] [:y [:b :c]]]) [[:x] [:x :b] [:x :c] [:y] [:y :b] [:y :c]])"));
}
re_com.nested_grid.header_cross_span = (function re_com$nested_grid$header_cross_span(path,all_paths){
return (cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.descendant_QMARK_,path),all_paths)) + (1));
});
re_com.nested_grid.header_main_span = (function re_com$nested_grid$header_main_span(path,all_paths){
return (((- cljs.core.count(path)) + cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.max,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.count,all_paths))) + (1));
});
re_com.nested_grid.resize_overlay = (function re_com$nested_grid$resize_overlay(p__21761){
var map__21762 = p__21761;
var map__21762__$1 = cljs.core.__destructure_map(map__21762);
var drag = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21762__$1,new cljs.core.Keyword(null,"drag","drag",449951290));
var mouse_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21762__$1,new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258));
var on_resize = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21762__$1,new cljs.core.Keyword(null,"on-resize","on-resize",-2005528129));
var last_mouse_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21762__$1,new cljs.core.Keyword(null,"last-mouse-x","last-mouse-x",150112047));
var mouse_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21762__$1,new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488));
var last_mouse_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21762__$1,new cljs.core.Keyword(null,"last-mouse-y","last-mouse-y",-1701805882));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-mouse-up","on-mouse-up",-1340533320),(function (){
return cljs.core.reset_BANG_(drag,false);
}),new cljs.core.Keyword(null,"on-mouse-move","on-mouse-move",-1386320874),(function (){var temp__5823__auto__ = cljs.core.deref(on_resize);
if(cljs.core.truth_(temp__5823__auto__)){
var on_resize__$1 = temp__5823__auto__;
return (function (p1__21759_SHARP_){
p1__21759_SHARP_.preventDefault();

var x = p1__21759_SHARP_.clientX;
var y = p1__21759_SHARP_.clientY;
cljs.core.reset_BANG_(mouse_x,x);

cljs.core.reset_BANG_(mouse_y,y);

var G__21768_22322 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"x-distance","x-distance",1622097731),(x - cljs.core.deref(last_mouse_x)),new cljs.core.Keyword(null,"y-distance","y-distance",-561335103),(y - cljs.core.deref(last_mouse_y))], null);
(on_resize__$1.cljs$core$IFn$_invoke$arity$1 ? on_resize__$1.cljs$core$IFn$_invoke$arity$1(G__21768_22322) : on_resize__$1.call(null,G__21768_22322));

cljs.core.reset_BANG_(last_mouse_x,x);

return cljs.core.reset_BANG_(last_mouse_y,y);
});
} else {
return (function (p1__21760_SHARP_){
p1__21760_SHARP_.preventDefault();

var x = p1__21760_SHARP_.clientX;
var y = p1__21760_SHARP_.clientY;
cljs.core.reset_BANG_(mouse_x,x);

cljs.core.reset_BANG_(last_mouse_x,x);

return cljs.core.reset_BANG_(last_mouse_y,y);
});
}
})(),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"position","position",-2011731912),"fixed",new cljs.core.Keyword(null,"z-index","z-index",1892827090),(3),new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"height","height",1025178622),"100%",new cljs.core.Keyword(null,"top","top",-1856271961),(0),new cljs.core.Keyword(null,"left","left",-399115937),(0),new cljs.core.Keyword(null,"font-size","font-size",-1847940346),(100),new cljs.core.Keyword(null,"cursor","cursor",1011937484),(function (){var G__21771 = cljs.core.deref(drag);
var G__21771__$1 = (((G__21771 instanceof cljs.core.Keyword))?G__21771.fqn:null);
switch (G__21771__$1) {
case "re-com.nested-grid/column":
return "col-resize";

break;
case "re-com.nested-grid/row":
return "row-resize";

break;
default:
return null;

}
})()], null)], null)], null);
});
re_com.nested_grid.resize_button = (function re_com$nested_grid$resize_button(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22332 = arguments.length;
var i__5877__auto___22333 = (0);
while(true){
if((i__5877__auto___22333 < len__5876__auto___22332)){
args__5882__auto__.push((arguments[i__5877__auto___22333]));

var G__22334 = (i__5877__auto___22333 + (1));
i__5877__auto___22333 = G__22334;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.nested_grid.resize_button.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.nested_grid.resize_button.cljs$core$IFn$_invoke$arity$variadic = (function (p__21785){
var map__21787 = p__21785;
var map__21787__$1 = cljs.core.__destructure_map(map__21787);
var drag = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21787__$1,new cljs.core.Keyword(null,"drag","drag",449951290));
var dragging_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
var hovering_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
return (function() { 
var G__22335__delegate = function (p__21793){
var map__21794 = p__21793;
var map__21794__$1 = cljs.core.__destructure_map(map__21794);
var on_resize = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"on-resize","on-resize",-2005528129));
var path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"path","path",-188191168));
var mouse_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258));
var last_mouse_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"last-mouse-y","last-mouse-y",-1701805882));
var selection_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"selection?","selection?",804764555));
var last_mouse_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"last-mouse-x","last-mouse-x",150112047));
var mouse_down_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"mouse-down-y","mouse-down-y",1419003887));
var dimension = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"dimension","dimension",543254198));
var mouse_down_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"mouse-down-x","mouse-down-x",-1461595497));
var resize_handler = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"resize-handler","resize-handler",-169459881));
var mouse_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21794__$1,new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-mouse-enter","on-mouse-enter",-1664921661),(function (){
return cljs.core.reset_BANG_(hovering_QMARK_,true);
}),new cljs.core.Keyword(null,"on-mouse-leave","on-mouse-leave",-1864319528),(function (){
return cljs.core.reset_BANG_(hovering_QMARK_,false);
}),new cljs.core.Keyword(null,"on-mouse-down","on-mouse-down",1147755470),(function (){var G__21796 = dimension;
var G__21796__$1 = (((G__21796 instanceof cljs.core.Keyword))?G__21796.fqn:null);
switch (G__21796__$1) {
case "column":
return (function (p1__21775_SHARP_){
p1__21775_SHARP_.preventDefault();

cljs.core.reset_BANG_(selection_QMARK_,null);

cljs.core.reset_BANG_(resize_handler,(function (props){
var G__21798 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"path","path",-188191168),path], null),props], 0));
return (on_resize.cljs$core$IFn$_invoke$arity$1 ? on_resize.cljs$core$IFn$_invoke$arity$1(G__21798) : on_resize.call(null,G__21798));
}));

cljs.core.reset_BANG_(drag,new cljs.core.Keyword("re-com.nested-grid","column","re-com.nested-grid/column",626823356));

cljs.core.reset_BANG_(mouse_down_x,p1__21775_SHARP_.clientX);

cljs.core.reset_BANG_(mouse_x,p1__21775_SHARP_.clientX);

return cljs.core.reset_BANG_(last_mouse_x,p1__21775_SHARP_.clientX);
});

break;
case "row":
return (function (p1__21776_SHARP_){
p1__21776_SHARP_.preventDefault();

cljs.core.reset_BANG_(selection_QMARK_,null);

cljs.core.reset_BANG_(resize_handler,(function (props){
var G__21801 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"path","path",-188191168),path], null),props], 0));
return (on_resize.cljs$core$IFn$_invoke$arity$1 ? on_resize.cljs$core$IFn$_invoke$arity$1(G__21801) : on_resize.call(null,G__21801));
}));

cljs.core.reset_BANG_(drag,new cljs.core.Keyword("re-com.nested-grid","row","re-com.nested-grid/row",-2106001844));

cljs.core.reset_BANG_(mouse_down_y,p1__21776_SHARP_.clientY);

cljs.core.reset_BANG_(mouse_y,p1__21776_SHARP_.clientY);

return cljs.core.reset_BANG_(last_mouse_y,p1__21776_SHARP_.clientY);
});

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__21796__$1))));

}
})(),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"position","position",-2011731912),"absolute",new cljs.core.Keyword(null,"opacity","opacity",397153780),(cljs.core.truth_((function (){var or__5142__auto__ = cljs.core.deref(hovering_QMARK_);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.deref(dragging_QMARK_);
}
})())?(1):(0)),new cljs.core.Keyword(null,"z-index","z-index",1892827090),(9999999),new cljs.core.Keyword(null,"background-color","background-color",570434026),"rgba(0,0,0,0.2)"], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"column","column",2078222095),dimension))?new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"top","top",-1856271961),(0),new cljs.core.Keyword(null,"cursor","cursor",1011937484),"col-resize",new cljs.core.Keyword(null,"height","height",1025178622),"100%",new cljs.core.Keyword(null,"width","width",-384071477),"20px",new cljs.core.Keyword(null,"right","right",-452581833),"-10px"], null):null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"row","row",-570139521),dimension))?new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"left","left",-399115937),(0),new cljs.core.Keyword(null,"cursor","cursor",1011937484),"row-resize",new cljs.core.Keyword(null,"height","height",1025178622),"20px",new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"bottom","bottom",-1550509018),"-10px"], null):null)], 0))], null)], null);
};
var G__22335 = function (var_args){
var p__21793 = null;
if (arguments.length > 0) {
var G__22346__i = 0, G__22346__a = new Array(arguments.length -  0);
while (G__22346__i < G__22346__a.length) {G__22346__a[G__22346__i] = arguments[G__22346__i + 0]; ++G__22346__i;}
  p__21793 = new cljs.core.IndexedSeq(G__22346__a,0,null);
} 
return G__22335__delegate.call(this,p__21793);};
G__22335.cljs$lang$maxFixedArity = 0;
G__22335.cljs$lang$applyTo = (function (arglist__22347){
var p__21793 = cljs.core.seq(arglist__22347);
return G__22335__delegate(p__21793);
});
G__22335.cljs$core$IFn$_invoke$arity$variadic = G__22335__delegate;
return G__22335;
})()
;
}));

(re_com.nested_grid.resize_button.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.nested_grid.resize_button.cljs$lang$applyTo = (function (seq21780){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21780));
}));

re_com.nested_grid.path__GT_grid_line_name = (function re_com$nested_grid$path__GT_grid_line_name(path){
return (""+"line__"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.hash(path))+"-start");
});
re_com.nested_grid.grid_template = (function re_com$nested_grid$grid_template(var_args){
var G__21816 = arguments.length;
switch (G__21816) {
case 1:
return re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
var args_arr__5901__auto__ = [];
var len__5876__auto___22350 = arguments.length;
var i__5877__auto___22351 = (0);
while(true){
if((i__5877__auto___22351 < len__5876__auto___22350)){
args_arr__5901__auto__.push((arguments[i__5877__auto___22351]));

var G__22352 = (i__5877__auto___22351 + (1));
i__5877__auto___22351 = G__22352;
continue;
} else {
}
break;
}

var argseq__5902__auto__ = ((((1) < args_arr__5901__auto__.length))?(new cljs.core.IndexedSeq(args_arr__5901__auto__.slice((1)),(0),null)):null);
return re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5902__auto__);

}
});

(re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$variadic = (function (tokens,more_tokens){
return re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.concat,tokens,more_tokens));
}));

/** @this {Function} */
(re_com.nested_grid.grid_template.cljs$lang$applyTo = (function (seq21814){
var G__21815 = cljs.core.first(seq21814);
var seq21814__$1 = cljs.core.next(seq21814);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__21815,seq21814__$1);
}));

(re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1 = (function (tokens){
var rf = (function (s,group){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(s)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(((typeof cljs.core.first(group) === 'number')?clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",cljs.core.map.cljs$core$IFn$_invoke$arity$2(re_com.util.px,group)):((typeof cljs.core.first(group) === 'string')?clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",group):(""+"["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",cljs.core.map.cljs$core$IFn$_invoke$arity$2(re_com.nested_grid.path__GT_grid_line_name,group)))+"]")
))));
});
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(rf,"",cljs.core.partition_by.cljs$core$IFn$_invoke$arity$2(cljs.core.some_fn.cljs$core$IFn$_invoke$arity$2(cljs.core.number_QMARK_,cljs.core.string_QMARK_),tokens)))+" [end]");
}));

(re_com.nested_grid.grid_template.cljs$lang$maxFixedArity = (1));

re_com.nested_grid.cell_part = (function re_com$nested_grid$cell_part(p__21828){
var map__21831 = p__21828;
var map__21831__$1 = cljs.core.__destructure_map(map__21831);
var column_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21831__$1,new cljs.core.Keyword(null,"column-path","column-path",-733367618));
var row_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21831__$1,new cljs.core.Keyword(null,"row-path","row-path",-709648669));
return null;
});
re_com.nested_grid.cell_wrapper_part = (function re_com$nested_grid$cell_wrapper_part(p__21834){
var map__21835 = p__21834;
var map__21835__$1 = cljs.core.__destructure_map(map__21835);
var args = map__21835__$1;
var column_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21835__$1,new cljs.core.Keyword(null,"column-path","column-path",-733367618));
var row_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21835__$1,new cljs.core.Keyword(null,"row-path","row-path",-709648669));
var cell = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21835__$1,new cljs.core.Keyword(null,"cell","cell",764245084));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21835__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),re_com.theme.apply(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"grid-column","grid-column",-1086912770),re_com.nested_grid.path__GT_grid_line_name(column_path),new cljs.core.Keyword(null,"grid-row","grid-row",-1737175087),re_com.nested_grid.path__GT_grid_line_name(row_path),new cljs.core.Keyword(null,"background-color","background-color",570434026),"#fff",new cljs.core.Keyword(null,"padding","padding",1660304693),"3px",new cljs.core.Keyword(null,"text-align","text-align",1786091845),"right",new cljs.core.Keyword(null,"border","border",1444987323),"0.5px solid #ccc",new cljs.core.Keyword(null,"position","position",-2011731912),"relative"], null)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"state","state",-1988618099),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.nested-grid","cell-wrapper","re-com.nested-grid/cell-wrapper",-987319991)], null),theme),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,cell,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(args,new cljs.core.Keyword(null,"cell","cell",764245084)),re_com.nested_grid.cell_part], null)], null);
});
re_com.nested_grid.header_label = (function re_com$nested_grid$header_label(path){
var header = cljs.core.last(path);
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5142__auto__ = new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(header);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(header);
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return header;
}
}
})()));
});
re_com.nested_grid.column_header_part = (function re_com$nested_grid$column_header_part(p__21837){
var map__21838 = p__21837;
var map__21838__$1 = cljs.core.__destructure_map(map__21838);
var column_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21838__$1,new cljs.core.Keyword(null,"column-path","column-path",-733367618));
return re_com.nested_grid.header_label(column_path);
});
re_com.theme.apply(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.nested-grid","column-header-wrapper","re-com.nested-grid/column-header-wrapper",-906387286)], null),cljs.core.PersistentVector.EMPTY);
re_com.nested_grid.column_header_wrapper_part = (function re_com$nested_grid$column_header_wrapper_part(p__21843){
var map__21845 = p__21843;
var map__21845__$1 = cljs.core.__destructure_map(map__21845);
var props = map__21845__$1;
var column_header = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21845__$1,new cljs.core.Keyword(null,"column-header","column-header",-1495823888));
var column_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21845__$1,new cljs.core.Keyword(null,"column-path","column-path",-733367618));
var column_paths = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21845__$1,new cljs.core.Keyword(null,"column-paths","column-paths",1944282824));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21845__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
var show_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21845__$1,new cljs.core.Keyword(null,"show?","show?",1543842127));
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"grid-column-start","grid-column-start",718549073),re_com.nested_grid.path__GT_grid_line_name(column_path),new cljs.core.Keyword(null,"grid-column-end","grid-column-end",592662211),(""+"span "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var G__21850 = column_path;
var G__21850__$1 = re_com.nested_grid.header_cross_span(G__21850,column_paths)
;
if(cljs.core.not(show_QMARK_)){
return (G__21850__$1 - (1));
} else {
return G__21850__$1;
}
})())),new cljs.core.Keyword(null,"grid-row-start","grid-row-start",-1827627988),cljs.core.count(column_path),new cljs.core.Keyword(null,"grid-row-end","grid-row-end",-128277830),(""+"span "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var G__21853 = column_path;
var G__21853__$1 = re_com.nested_grid.header_main_span(G__21853,column_paths)
;
if(cljs.core.not(show_QMARK_)){
return (G__21853__$1 - (1));
} else {
return G__21853__$1;
}
})())),new cljs.core.Keyword(null,"position","position",-2011731912),"relative"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),re_com.theme.apply(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"state","state",-1988618099),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.nested-grid","column-header-wrapper","re-com.nested-grid/column-header-wrapper",-906387286)], null),theme),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,column_header,props,re_com.nested_grid.column_header_part], null)], null),(cljs.core.truth_(show_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.resize_button,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([props,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dimension","dimension",543254198),new cljs.core.Keyword(null,"column","column",2078222095),new cljs.core.Keyword(null,"path","path",-188191168),column_path], null)], 0))], null):null)], null);
});
re_com.nested_grid.row_header_part = (function re_com$nested_grid$row_header_part(p__21860){
var map__21862 = p__21860;
var map__21862__$1 = cljs.core.__destructure_map(map__21862);
var row_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21862__$1,new cljs.core.Keyword(null,"row-path","row-path",-709648669));
return re_com.nested_grid.header_label(row_path);
});
re_com.nested_grid.row_header_wrapper_part = (function re_com$nested_grid$row_header_wrapper_part(p__21863){
var map__21864 = p__21863;
var map__21864__$1 = cljs.core.__destructure_map(map__21864);
var props = map__21864__$1;
var row_path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21864__$1,new cljs.core.Keyword(null,"row-path","row-path",-709648669));
var row_paths = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21864__$1,new cljs.core.Keyword(null,"row-paths","row-paths",-1900396315));
var row_header = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21864__$1,new cljs.core.Keyword(null,"row-header","row-header",1799050794));
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21864__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
var show_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21864__$1,new cljs.core.Keyword(null,"show?","show?",1543842127));
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"grid-row-start","grid-row-start",-1827627988),re_com.nested_grid.path__GT_grid_line_name(row_path),new cljs.core.Keyword(null,"grid-row-end","grid-row-end",-128277830),(""+"span "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var G__21871 = row_path;
var G__21871__$1 = re_com.nested_grid.header_cross_span(G__21871,row_paths)
;
if(cljs.core.not(show_QMARK_)){
return (G__21871__$1 - (1));
} else {
return G__21871__$1;
}
})())),new cljs.core.Keyword(null,"grid-column-start","grid-column-start",718549073),cljs.core.count(row_path),new cljs.core.Keyword(null,"grid-column-end","grid-column-end",592662211),(""+"span "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var G__21874 = row_path;
var G__21874__$1 = re_com.nested_grid.header_main_span(G__21874,row_paths)
;
if(cljs.core.not(show_QMARK_)){
return (G__21874__$1 - (1));
} else {
return G__21874__$1;
}
})())),new cljs.core.Keyword(null,"position","position",-2011731912),"relative"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),re_com.theme.apply(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"state","state",-1988618099),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.nested-grid","row-header-wrapper","re-com.nested-grid/row-header-wrapper",-983747999)], null),theme),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,row_header,props,re_com.nested_grid.row_header_part], null)], null),(cljs.core.truth_(show_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.resize_button,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([props,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dimension","dimension",543254198),new cljs.core.Keyword(null,"row","row",-570139521),new cljs.core.Keyword(null,"path","path",-188191168),row_path], null)], 0))], null):null)], null);
});
re_com.nested_grid.level = cljs.core.count;
re_com.nested_grid.clipboard_export_button = (function re_com$nested_grid$clipboard_export_button(p__21877){
var map__21878 = p__21877;
var map__21878__$1 = cljs.core.__destructure_map(map__21878);
var on_export = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21878__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391));
return null;
});
re_com.nested_grid.controls = (function re_com$nested_grid$controls(p__21879){
var map__21880 = p__21879;
var map__21880__$1 = cljs.core.__destructure_map(map__21880);
var show_export_button_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21880__$1,new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113));
var hover_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21880__$1,new cljs.core.Keyword(null,"hover?","hover?",-1201331489));
var on_export = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21880__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391));
return new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.h_box,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"grid-column-start","grid-column-start",718549073),(1),new cljs.core.Keyword(null,"grid-column-end","grid-column-end",592662211),"end",new cljs.core.Keyword(null,"grid-row","grid-row",-1737175087),(1)], null),new cljs.core.Keyword(null,"height","height",1025178622),"20px",new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"children","children",-940561982),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.box.gap,new cljs.core.Keyword(null,"size","size",1098693007),"1"], null),(cljs.core.truth_((function (){var and__5140__auto__ = show_export_button_QMARK_;
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.deref(hover_QMARK_);
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.buttons.row_button,new cljs.core.Keyword(null,"md-icon-name","md-icon-name",681785863),"zmdi zmdi-copy",new cljs.core.Keyword(null,"mouse-over-row?","mouse-over-row?",-446703882),true,new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),(""+"Copy table to clipboard."),new cljs.core.Keyword(null,"on-click","on-click",1632826543),on_export], null):null)], null)], null);
});
re_com.nested_grid.quantize = (function re_com$nested_grid$quantize(quanta,threshold){
return (cljs.core.count(cljs.core.take_while.cljs$core$IFn$_invoke$arity$2((function (p1__21881_SHARP_){
return (p1__21881_SHARP_ < threshold);
}),cljs.core.reductions.cljs$core$IFn$_invoke$arity$2(cljs.core._PLUS_,quanta))) - (1));
});
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),re_com.nested_grid.quantize(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [(10),(10),(10)], null),(29)))){
} else {
throw (new Error("Assert failed: (= 1 (quantize [10 10 10] 29))"));
}
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),re_com.nested_grid.quantize(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [(10),(10),(10)], null),(30)))){
} else {
throw (new Error("Assert failed: (= 1 (quantize [10 10 10] 30))"));
}
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((2),re_com.nested_grid.quantize(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [(10),(10),(10)], null),(31)))){
} else {
throw (new Error("Assert failed: (= 2 (quantize [10 10 10] 31))"));
}
re_com.nested_grid.drag_overlay = (function re_com$nested_grid$drag_overlay(p__21889){
var map__21890 = p__21889;
var map__21890__$1 = cljs.core.__destructure_map(map__21890);
var drag = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21890__$1,new cljs.core.Keyword(null,"drag","drag",449951290));
var selection_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21890__$1,new cljs.core.Keyword(null,"selection?","selection?",804764555));
var mouse_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21890__$1,new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258));
var mouse_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21890__$1,new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-mouse-up","on-mouse-up",-1340533320),(function (){
return cljs.core.reset_BANG_(drag,null);
}),new cljs.core.Keyword(null,"on-mouse-move","on-mouse-move",-1386320874),(function (p1__21886_SHARP_){
cljs.core.reset_BANG_(selection_QMARK_,true);

p1__21886_SHARP_.preventDefault();

cljs.core.reset_BANG_(mouse_x,p1__21886_SHARP_.clientX);

return cljs.core.reset_BANG_(mouse_y,p1__21886_SHARP_.clientY);
}),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"position","position",-2011731912),"fixed",new cljs.core.Keyword(null,"top","top",-1856271961),(0),new cljs.core.Keyword(null,"left","left",-399115937),(0),new cljs.core.Keyword(null,"z-index","z-index",1892827090),(2147483647),new cljs.core.Keyword(null,"height","height",1025178622),"100%",new cljs.core.Keyword(null,"width","width",-384071477),"100%"], null)], null)], null);
});
re_com.nested_grid.selection_part = (function re_com$nested_grid$selection_part(_){
return (function (___$1){
var _BANG_ref = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var reset_ref_BANG_ = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(cljs.core.reset_BANG_,_BANG_ref);
return (function (p__21906){
var map__21907 = p__21906;
var map__21907__$1 = cljs.core.__destructure_map(map__21907);
var props = map__21907__$1;
var drag = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"drag","drag",449951290));
var selection_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"selection?","selection?",804764555));
var grid_columns = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"grid-columns","grid-columns",-1398061070));
var grid_rows = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"grid-rows","grid-rows",-943125795));
var mouse_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258));
var mouse_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488));
var mouse_down_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"mouse-down-x","mouse-down-x",-1461595497));
var mouse_down_y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21907__$1,new cljs.core.Keyword(null,"mouse-down-y","mouse-down-y",1419003887));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"<>","<>",1280186386),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"ref","ref",1289896967),reset_ref_BANG_,new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"position","position",-2011731912),"absolute",new cljs.core.Keyword(null,"height","height",1025178622),"100%",new cljs.core.Keyword(null,"width","width",-384071477),"100%",new cljs.core.Keyword(null,"top","top",-1856271961),(0),new cljs.core.Keyword(null,"left","left",-399115937),(0)], null),new cljs.core.Keyword(null,"on-mouse-up","on-mouse-up",-1340533320),(function (){
return cljs.core.reset_BANG_(drag,false);
}),new cljs.core.Keyword(null,"on-mouse-down","on-mouse-down",1147755470),(function (p1__21901_SHARP_){
if(cljs.core.not(cljs.core.deref(selection_QMARK_))){
cljs.core.reset_BANG_(drag,new cljs.core.Keyword("re-com.nested-grid","selection","re-com.nested-grid/selection",1957297550));

cljs.core.reset_BANG_(selection_QMARK_,true);

cljs.core.reset_BANG_(mouse_down_y,p1__21901_SHARP_.clientY);

cljs.core.reset_BANG_(mouse_down_x,p1__21901_SHARP_.clientX);

cljs.core.reset_BANG_(mouse_y,p1__21901_SHARP_.clientY);

return cljs.core.reset_BANG_(mouse_x,p1__21901_SHARP_.clientX);
} else {
cljs.core.reset_BANG_(selection_QMARK_,false);

return cljs.core.reset_BANG_(drag,false);
}
})], null)], null),(cljs.core.truth_(cljs.core.deref(selection_QMARK_))?(function (){var grid_columns__$1 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.number_QMARK_,grid_columns);
var grid_rows__$1 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.number_QMARK_,grid_rows);
var bounds = cljs.core.deref(_BANG_ref).getBoundingClientRect();
var origin_x = bounds.x;
var origin_y = bounds.y;
var column_begin = re_com.nested_grid.quantize(grid_columns__$1,(cljs.core.deref(mouse_down_x) - origin_x));
var column_finish = re_com.nested_grid.quantize(grid_columns__$1,(cljs.core.deref(mouse_x) - origin_x));
var row_begin = re_com.nested_grid.quantize(grid_rows__$1,(cljs.core.deref(mouse_down_y) - origin_y));
var row_finish = re_com.nested_grid.quantize(grid_rows__$1,(cljs.core.deref(mouse_y) - origin_y));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"grid-column-start","grid-column-start",718549073),((2) + cljs.core.min.cljs$core$IFn$_invoke$arity$2(column_begin,column_finish)),new cljs.core.Keyword(null,"grid-column-end","grid-column-end",592662211),((3) + cljs.core.max.cljs$core$IFn$_invoke$arity$2(column_begin,column_finish)),new cljs.core.Keyword(null,"grid-row-start","grid-row-start",-1827627988),((2) + cljs.core.min.cljs$core$IFn$_invoke$arity$2(row_begin,row_finish)),new cljs.core.Keyword(null,"grid-row-end","grid-row-end",-128277830),((3) + cljs.core.max.cljs$core$IFn$_invoke$arity$2(row_begin,row_finish)),new cljs.core.Keyword(null,"border","border",1444987323),"2px solid dodgerblue",new cljs.core.Keyword(null,"background","background",-863952629),"rgba(127,127,255,.1)",new cljs.core.Keyword(null,"position","position",-2011731912),"relative",new cljs.core.Keyword(null,"pointer-events","pointer-events",-1053858853),"none"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"position","position",-2011731912),"absolute",new cljs.core.Keyword(null,"background","background",-863952629),"white",new cljs.core.Keyword(null,"border","border",1444987323),"2px solid grey",new cljs.core.Keyword(null,"height","height",1025178622),(10),new cljs.core.Keyword(null,"width","width",-384071477),(10),new cljs.core.Keyword(null,"right","right",-452581833),(-6),new cljs.core.Keyword(null,"bottom","bottom",-1550509018),(-6)], null)], null)], null)], null);
})():null)], null);
});
});
});
re_com.nested_grid.header_spacer_part = (function re_com$nested_grid$header_spacer_part(_){
return "";
});
re_com.nested_grid.header_spacer_wrapper_part = (function re_com$nested_grid$header_spacer_wrapper_part(p__21912){
var map__21913 = p__21912;
var map__21913__$1 = cljs.core.__destructure_map(map__21913);
var theme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21913__$1,new cljs.core.Keyword(null,"theme","theme",-1247880880));
var x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21913__$1,new cljs.core.Keyword(null,"x","x",2099068185));
var y = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21913__$1,new cljs.core.Keyword(null,"y","y",-1757859776));
var header_spacer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21913__$1,new cljs.core.Keyword(null,"header-spacer","header-spacer",700022605));
var props = re_com.theme.apply(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"grid-column","grid-column",-1086912770),(x + (1)),new cljs.core.Keyword(null,"grid-row","grid-row",-1737175087),(y + (1))], null)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"part","part",77757738),new cljs.core.Keyword("re-com.nested-grid","header-spacer","re-com.nested-grid/header-spacer",1648682330)], null),theme);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),props,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,header_spacer,props,re_com.nested_grid.header_spacer_part], null)], null);
});
re_com.nested_grid.scroll_container = (function re_com$nested_grid$scroll_container(p__21918,child){
var map__21919 = p__21918;
var map__21919__$1 = cljs.core.__destructure_map(map__21919);
var scroll_top = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21919__$1,new cljs.core.Keyword(null,"scroll-top","scroll-top",-46723100));
var scroll_left = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21919__$1,new cljs.core.Keyword(null,"scroll-left","scroll-left",-211761103));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21919__$1,new cljs.core.Keyword(null,"width","width",-384071477));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__21919__$1,new cljs.core.Keyword(null,"height","height",1025178622));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"max-height","max-height",-612563804),height,new cljs.core.Keyword(null,"max-width","max-width",-1939924051),width,new cljs.core.Keyword(null,"overflow","overflow",2058931880),"hidden"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"transform","transform",1381301764),(""+"translateX("+cljs.core.str.cljs$core$IFn$_invoke$arity$1((- re_com.util.deref_or_value(scroll_left)))+"px) "+"translateY("+cljs.core.str.cljs$core$IFn$_invoke$arity$1((- re_com.util.deref_or_value(scroll_top)))+"px)")], null)], null),child], null)], null);
});
re_com.nested_grid.nested_grid = (function re_com$nested_grid$nested_grid(var_args){
var args__5882__auto__ = [];
var len__5876__auto___22379 = arguments.length;
var i__5877__auto___22380 = (0);
while(true){
if((i__5877__auto___22380 < len__5876__auto___22379)){
args__5882__auto__.push((arguments[i__5877__auto___22380]));

var G__22386 = (i__5877__auto___22380 + (1));
i__5877__auto___22380 = G__22386;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((0) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((0)),(0),null)):null);
return re_com.nested_grid.nested_grid.cljs$core$IFn$_invoke$arity$variadic(argseq__5883__auto__);
});

(re_com.nested_grid.nested_grid.cljs$core$IFn$_invoke$arity$variadic = (function (p__21996){
var map__21998 = p__21996;
var map__21998__$1 = cljs.core.__destructure_map(map__21998);
var column_width = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21998__$1,new cljs.core.Keyword(null,"column-width","column-width",405119380),(60));
var row_height = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__21998__$1,new cljs.core.Keyword(null,"row-height","row-height",527360749),(30));
var column_state = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var row_state = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hover_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(false);
var drag = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var selection_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var mouse_down_x = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var mouse_down_y = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var last_mouse_x = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var last_mouse_y = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var mouse_x = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var mouse_y = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var scroll_top = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var scroll_left = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((0));
var column_header_prop = (function() { 
var G__22387__delegate = function (path,k,p__22011){
var vec__22014 = p__22011;
var default$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22014,(0),null);
var or__5142__auto__ = (function (){var G__22026 = cljs.core.deref(column_state);
var G__22026__$1 = (((G__22026 == null))?null:cljs.core.get.cljs$core$IFn$_invoke$arity$2(G__22026,path));
if((G__22026__$1 == null)){
return null;
} else {
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(G__22026__$1,k);
}
})();
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.meta(cljs.core.last(path)),k);
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
var or__5142__auto____$2 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.last(path),k);
if(cljs.core.truth_(or__5142__auto____$2)){
return or__5142__auto____$2;
} else {
return default$;
}
}
}
};
var G__22387 = function (path,k,var_args){
var p__22011 = null;
if (arguments.length > 2) {
var G__22396__i = 0, G__22396__a = new Array(arguments.length -  2);
while (G__22396__i < G__22396__a.length) {G__22396__a[G__22396__i] = arguments[G__22396__i + 2]; ++G__22396__i;}
  p__22011 = new cljs.core.IndexedSeq(G__22396__a,0,null);
} 
return G__22387__delegate.call(this,path,k,p__22011);};
G__22387.cljs$lang$maxFixedArity = 2;
G__22387.cljs$lang$applyTo = (function (arglist__22397){
var path = cljs.core.first(arglist__22397);
arglist__22397 = cljs.core.next(arglist__22397);
var k = cljs.core.first(arglist__22397);
var p__22011 = cljs.core.rest(arglist__22397);
return G__22387__delegate(path,k,p__22011);
});
G__22387.cljs$core$IFn$_invoke$arity$variadic = G__22387__delegate;
return G__22387;
})()
;
var header_prop = (function() { 
var G__22398__delegate = function (path,k,dimension,p__22031){
var vec__22032 = p__22031;
var default$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__22032,(0),null);
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2((function (){var G__22036 = dimension;
var G__22036__$1 = (((G__22036 instanceof cljs.core.Keyword))?G__22036.fqn:null);
switch (G__22036__$1) {
case "row":
return cljs.core.deref(row_state);

break;
case "column":
return cljs.core.deref(column_state);

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__22036__$1))));

}
})(),path);
return cljs.core.first(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.get.cljs$core$IFn$_invoke$arity$2(state,k),cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.meta(cljs.core.last(path)),k),cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.last(path),k),default$], null)));
};
var G__22398 = function (path,k,dimension,var_args){
var p__22031 = null;
if (arguments.length > 3) {
var G__22400__i = 0, G__22400__a = new Array(arguments.length -  3);
while (G__22400__i < G__22400__a.length) {G__22400__a[G__22400__i] = arguments[G__22400__i + 3]; ++G__22400__i;}
  p__22031 = new cljs.core.IndexedSeq(G__22400__a,0,null);
} 
return G__22398__delegate.call(this,path,k,dimension,p__22031);};
G__22398.cljs$lang$maxFixedArity = 3;
G__22398.cljs$lang$applyTo = (function (arglist__22401){
var path = cljs.core.first(arglist__22401);
arglist__22401 = cljs.core.next(arglist__22401);
var k = cljs.core.first(arglist__22401);
arglist__22401 = cljs.core.next(arglist__22401);
var dimension = cljs.core.first(arglist__22401);
var p__22031 = cljs.core.rest(arglist__22401);
return G__22398__delegate(path,k,dimension,p__22031);
});
G__22398.cljs$core$IFn$_invoke$arity$variadic = G__22398__delegate;
return G__22398;
})()
;
var max_props = (function (k,dimension,default$,paths){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (path_group){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.max,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__21924_SHARP_){
return header_prop(p1__21924_SHARP_,k,dimension,default$);
}),path_group));
}),cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.val,cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(cljs.core.key,cljs.core.group_by(re_com.nested_grid.level,paths))));
});
var resize_column_BANG_ = (function (p__22040){
var map__22041 = p__22040;
var map__22041__$1 = cljs.core.__destructure_map(map__22041);
var x_distance = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22041__$1,new cljs.core.Keyword(null,"x-distance","x-distance",1622097731));
var path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22041__$1,new cljs.core.Keyword(null,"path","path",-188191168));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(column_state,cljs.core.update_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [path,new cljs.core.Keyword(null,"width","width",-384071477)], null),(function (p1__21925_SHARP_){
return cljs.core.max.cljs$core$IFn$_invoke$arity$2(((function (){var or__5142__auto__ = p1__21925_SHARP_;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return column_header_prop(path,new cljs.core.Keyword(null,"width","width",-384071477),column_width);
}
})() + x_distance),(0));
}));
});
var resize_row_BANG_ = (function (p__22044){
var map__22045 = p__22044;
var map__22045__$1 = cljs.core.__destructure_map(map__22045);
var y_distance = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22045__$1,new cljs.core.Keyword(null,"y-distance","y-distance",-561335103));
var path = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22045__$1,new cljs.core.Keyword(null,"path","path",-188191168));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(row_state,cljs.core.update_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [path,new cljs.core.Keyword(null,"height","height",1025178622)], null),(function (p1__21926_SHARP_){
return cljs.core.max.cljs$core$IFn$_invoke$arity$2(((function (){var or__5142__auto__ = p1__21926_SHARP_;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return header_prop(path,new cljs.core.Keyword(null,"height","height",1025178622),new cljs.core.Keyword(null,"row","row",-570139521),row_height);
}
})() + y_distance),(0));
}));
});
var resize_handler = reagent.core.atom.cljs$core$IFn$_invoke$arity$1((function (){
return cljs.core.List.EMPTY;
}));
return (function() { 
var G__22402__delegate = function (p__22046){
var map__22047 = p__22046;
var map__22047__$1 = cljs.core.__destructure_map(map__22047);
var header_spacer_wrapper = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"header-spacer-wrapper","header-spacer-wrapper",-1554966085));
var on_export_cell = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"on-export-cell","on-export-cell",1315067067));
var cell = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"cell","cell",764245084));
var on_export = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"on-export","on-export",1803619391));
var column_tree = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"column-tree","column-tree",841938146));
var show_selection_box_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"show-selection-box?","show-selection-box?",-211398013),false);
var cell_wrapper = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"cell-wrapper","cell-wrapper",-1365129340));
var max_height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"max-height","max-height",-612563804));
var on_export_column_header = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"on-export-column-header","on-export-column-header",-899049368),re_com.nested_grid.header_label);
var show_export_button_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113),true);
var row_header = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"row-header","row-header",1799050794));
var row_tree = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"row-tree","row-tree",687483819));
var on_export_row_header = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"on-export-row-header","on-export-row-header",181805132),re_com.nested_grid.header_label);
var row_height__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"row-height","row-height",527360749),(30));
var header_spacer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"header-spacer","header-spacer",700022605));
var column_header = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"column-header","column-header",-1495823888));
var row_header_width = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"row-header-width","row-header-width",-1813601584),(100));
var column_header_height = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"column-header-height","column-header-height",-1680092558),(30));
var show_branch_paths_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"show-branch-paths?","show-branch-paths?",517719956),false);
var column_width__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__22047__$1,new cljs.core.Keyword(null,"column-width","column-width",405119380),(60));
var row_header_wrapper = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"row-header-wrapper","row-header-wrapper",-1998665578));
var column_header_wrapper = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22047__$1,new cljs.core.Keyword(null,"column-header-wrapper","column-header-wrapper",-58772905));
var theme = cljs.core.PersistentArrayMap.EMPTY;
var themed = (function (part,props){
return re_com.theme.apply(props,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"part","part",77757738),part], null),theme);
});
var column_paths = re_com.nested_grid.spec__GT_headers_STAR_(column_tree);
var column_leaf_paths = re_com.nested_grid.leaf_paths(column_paths);
var leaf_column_QMARK_ = cljs.core.set(column_leaf_paths);
var row_paths = re_com.nested_grid.spec__GT_headers_STAR_(row_tree);
var leaf_row_QMARK_ = cljs.core.set(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (paths,p){
return cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__21928_SHARP_){
return re_com.nested_grid.descendant_QMARK_(p1__21928_SHARP_,p);
}),paths);
}),row_paths,row_paths));
var leaf_QMARK_ = (function (path,dimension){
var G__22053 = dimension;
var G__22053__$1 = (((G__22053 instanceof cljs.core.Keyword))?G__22053.fqn:null);
switch (G__22053__$1) {
case "column":
return (leaf_column_QMARK_.cljs$core$IFn$_invoke$arity$1 ? leaf_column_QMARK_.cljs$core$IFn$_invoke$arity$1(path) : leaf_column_QMARK_.call(null,path));

break;
case "row":
return (leaf_row_QMARK_.cljs$core$IFn$_invoke$arity$1 ? leaf_row_QMARK_.cljs$core$IFn$_invoke$arity$1(path) : leaf_row_QMARK_.call(null,path));

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__22053__$1))));

}
});
var show_QMARK_ = (function (path,dimension){
var show_prop = header_prop(path,new cljs.core.Keyword(null,"show?","show?",1543842127),dimension);
var result = (function (){var and__5140__auto__ = (!(show_prop === false));
if(and__5140__auto__){
var or__5142__auto__ = show_prop === true;
if(or__5142__auto__){
return or__5142__auto__;
} else {
var or__5142__auto____$1 = show_branch_paths_QMARK_;
if(cljs.core.truth_(or__5142__auto____$1)){
return or__5142__auto____$1;
} else {
return leaf_QMARK_(path,dimension);
}
}
} else {
return and__5140__auto__;
}
})();
return result;
});
var showing_column_paths = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__21932_SHARP_){
return show_QMARK_(p1__21932_SHARP_,new cljs.core.Keyword(null,"column","column",2078222095));
}),column_paths);
var showing_row_paths = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__21933_SHARP_){
return show_QMARK_(p1__21933_SHARP_,new cljs.core.Keyword(null,"row","row",-570139521));
}),row_paths);
var showing_column_widths = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__21934_SHARP_){
return column_header_prop(p1__21934_SHARP_,new cljs.core.Keyword(null,"width","width",-384071477),column_width__$1);
}),showing_column_paths);
var showing_row_heights = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__21935_SHARP_){
return column_header_prop(p1__21935_SHARP_,new cljs.core.Keyword(null,"height","height",1025178622),row_height__$1);
}),showing_row_paths);
var max_column_heights = max_props(new cljs.core.Keyword(null,"height","height",1025178622),new cljs.core.Keyword(null,"column","column",2078222095),column_header_height,column_paths);
var max_row_widths = max_props(new cljs.core.Keyword(null,"width","width",-384071477),new cljs.core.Keyword(null,"row","row",-570139521),row_header_width,row_paths);
var column_depth = cljs.core.count(max_column_heights);
var row_depth = cljs.core.count(max_row_widths);
var on_export_cell__$1 = (function (){var or__5142__auto__ = on_export_cell;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.pr_str,cell);
}
})();
var default_on_export = (function re_com$nested_grid$on_export(p__22058){
var map__22059 = p__22058;
var map__22059__$1 = cljs.core.__destructure_map(map__22059);
var rows = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__22059__$1,new cljs.core.Keyword(null,"rows","rows",850049680));
return re_com.util.clipboard_write_BANG_(clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2(re_com.util.tsv_line,rows)));
});
var on_export__$1 = (function (){var or__5142__auto__ = on_export;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return default_on_export;
}
})();
var cell_grid_columns = cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (path){
var width = header_prop(path,new cljs.core.Keyword(null,"width","width",-384071477),new cljs.core.Keyword(null,"column","column",2078222095),column_width__$1);
if(cljs.core.truth_(show_QMARK_(path,new cljs.core.Keyword(null,"column","column",2078222095)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [path,width], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [path], null);
}
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([column_paths], 0));
var cell_grid_rows = cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (path){
var height = header_prop(path,new cljs.core.Keyword(null,"height","height",1025178622),new cljs.core.Keyword(null,"row","row",-570139521),row_height__$1);
if(cljs.core.truth_(show_QMARK_(path,new cljs.core.Keyword(null,"row","row",-570139521)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [path,height], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [path], null);
}
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([row_paths], 0));
var spacer_QMARK_ = cljs.core.number_QMARK_;
var export_column_headers = (function (){
var y_size = column_depth;
var x_size = cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(spacer_QMARK_,cell_grid_columns));
var result = cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(y_size,cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(x_size,null))));
var __GT_y = cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.dec,cljs.core.count);
var __GT_x = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (m,item){
if(cljs.core.truth_((spacer_QMARK_.cljs$core$IFn$_invoke$arity$1 ? spacer_QMARK_.cljs$core$IFn$_invoke$arity$1(item) : spacer_QMARK_.call(null,item)))){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(m,new cljs.core.Keyword("re-com.nested-grid","count","re-com.nested-grid/count",-849216222),cljs.core.inc);
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(m,item,(function (){var or__5142__auto__ = new cljs.core.Keyword("re-com.nested-grid","count","re-com.nested-grid/count",-849216222).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (0);
}
})());
}
}),cljs.core.PersistentArrayMap.EMPTY,cell_grid_columns);
var insert = (function (result__$1,path){
return cljs.core.assoc_in(result__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [__GT_y(path),(__GT_x.cljs$core$IFn$_invoke$arity$1 ? __GT_x.cljs$core$IFn$_invoke$arity$1(path) : __GT_x.call(null,path))], null),(on_export_column_header.cljs$core$IFn$_invoke$arity$1 ? on_export_column_header.cljs$core$IFn$_invoke$arity$1(path) : on_export_column_header.call(null,path)));
});
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(insert,result,column_paths);
});
var export_row_headers = (function (){
var y_size = cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(spacer_QMARK_,cell_grid_rows));
var x_size = row_depth;
var result = cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(y_size,cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(x_size,null))));
var __GT_y = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (m,item){
if(cljs.core.truth_((spacer_QMARK_.cljs$core$IFn$_invoke$arity$1 ? spacer_QMARK_.cljs$core$IFn$_invoke$arity$1(item) : spacer_QMARK_.call(null,item)))){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(m,new cljs.core.Keyword("re-com.nested-grid","count","re-com.nested-grid/count",-849216222),cljs.core.inc);
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(m,item,(function (){var or__5142__auto__ = new cljs.core.Keyword("re-com.nested-grid","count","re-com.nested-grid/count",-849216222).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return (0);
}
})());
}
}),cljs.core.PersistentArrayMap.EMPTY,cell_grid_rows);
var __GT_x = cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.dec,cljs.core.count);
var insert = (function (result__$1,path){
return cljs.core.assoc_in(result__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(__GT_y.cljs$core$IFn$_invoke$arity$1 ? __GT_y.cljs$core$IFn$_invoke$arity$1(path) : __GT_y.call(null,path)),__GT_x(path)], null),(on_export_row_header.cljs$core$IFn$_invoke$arity$1 ? on_export_row_header.cljs$core$IFn$_invoke$arity$1(path) : on_export_row_header.call(null,path)));
});
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(insert,result,row_paths);
});
var export_cells = (function (){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (row_path){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (column_path){
var G__22079 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"row-path","row-path",-709648669),row_path,new cljs.core.Keyword(null,"column-path","column-path",-733367618),column_path], null);
return (on_export_cell__$1.cljs$core$IFn$_invoke$arity$1 ? on_export_cell__$1.cljs$core$IFn$_invoke$arity$1(G__22079) : on_export_cell__$1.call(null,G__22079));
}),showing_column_paths);
}),showing_row_paths);
});
var export_spacers = (function (){
return cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(column_depth,cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(row_depth,null))));
});
var control_panel = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.controls,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"show-export-button?","show-export-button?",1564339113),show_export_button_QMARK_,new cljs.core.Keyword(null,"hover?","hover?",-1201331489),hover_QMARK_,new cljs.core.Keyword(null,"on-export","on-export",1803619391),(function (){
var column_headers = export_column_headers();
var row_headers = export_row_headers();
var spacers = export_spacers();
var cells = export_cells();
var header_rows = cljs.core.mapv.cljs$core$IFn$_invoke$arity$3(cljs.core.into,spacers,column_headers);
var main_rows = cljs.core.mapv.cljs$core$IFn$_invoke$arity$3(cljs.core.into,row_headers,cells);
var rows = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(header_rows,main_rows);
var G__22082 = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"column-headers","column-headers",-966500841),column_headers,new cljs.core.Keyword(null,"row-headers","row-headers",1790514903),row_headers,new cljs.core.Keyword(null,"spacers","spacers",1706634551),spacers,new cljs.core.Keyword(null,"cells","cells",-985166822),cells,new cljs.core.Keyword(null,"header-rows","header-rows",-1553465628),header_rows,new cljs.core.Keyword(null,"main-rows","main-rows",247629241),main_rows,new cljs.core.Keyword(null,"rows","rows",850049680),rows,new cljs.core.Keyword(null,"default","default",-1987822328),default_on_export], null);
return (on_export__$1.cljs$core$IFn$_invoke$arity$1 ? on_export__$1.cljs$core$IFn$_invoke$arity$1(G__22082) : on_export__$1.call(null,G__22082));
})], null)], null);
var cell_grid_container = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-scroll","on-scroll",1590848677),(function (p1__21970_SHARP_){
cljs.core.reset_BANG_(scroll_top,p1__21970_SHARP_.target.scrollTop);

return cljs.core.reset_BANG_(scroll_left,p1__21970_SHARP_.target.scrollLeft);
}),new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"max-height","max-height",-612563804),new cljs.core.Keyword(null,"overflow","overflow",2058931880),new cljs.core.Keyword(null,"background-color","background-color",570434026),new cljs.core.Keyword(null,"scrollbar-width","scrollbar-width",264849067),new cljs.core.Keyword(null,"grid-template-rows","grid-template-rows",-372292629),new cljs.core.Keyword(null,"cursor","cursor",1011937484),new cljs.core.Keyword(null,"padding","padding",1660304693),new cljs.core.Keyword(null,"gap","gap",80255254),new cljs.core.Keyword(null,"display","display",242065432),new cljs.core.Keyword(null,"position","position",-2011731912),new cljs.core.Keyword(null,"grid-template-columns","grid-template-columns",-594112133)],[max_height,"auto","transparent","thin",re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(cell_grid_rows),"crosshair","0px","0px","grid","relative",re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(cell_grid_columns)])], null)], null);
var column_header_cells = cljs.core.doall.cljs$core$IFn$_invoke$arity$1((function (){var iter__5628__auto__ = (function re_com$nested_grid$iter__22083(s__22084){
return (new cljs.core.LazySeq(null,(function (){
var s__22084__$1 = s__22084;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22084__$1);
if(temp__5825__auto__){
var s__22084__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__22084__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22084__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22086 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22085 = (0);
while(true){
if((i__22085 < size__5627__auto__)){
var path = cljs.core._nth(c__5626__auto__,i__22085);
var props = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258),new cljs.core.Keyword(null,"column-paths","column-paths",1944282824),new cljs.core.Keyword(null,"selection?","selection?",804764555),new cljs.core.Keyword(null,"show?","show?",1543842127),new cljs.core.Keyword(null,"last-mouse-x","last-mouse-x",150112047),new cljs.core.Keyword(null,"column-header","column-header",-1495823888),new cljs.core.Keyword(null,"mouse-down-x","mouse-down-x",-1461595497),new cljs.core.Keyword(null,"resize-handler","resize-handler",-169459881),new cljs.core.Keyword(null,"drag","drag",449951290),new cljs.core.Keyword(null,"column-path","column-path",-733367618),new cljs.core.Keyword(null,"on-resize","on-resize",-2005528129)],[mouse_x,column_paths,selection_QMARK_,show_QMARK_(path,new cljs.core.Keyword(null,"column","column",2078222095)),last_mouse_x,column_header,mouse_down_x,resize_handler,drag,path,resize_column_BANG_]);
cljs.core.chunk_append(b__22086,cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,column_header_wrapper,props,re_com.nested_grid.column_header_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","column","re-com.nested-grid/column",626823356),(function (){var or__5142__auto__ = path;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.gensym.cljs$core$IFn$_invoke$arity$0();
}
})()], null)], null)));

var G__22440 = (i__22085 + (1));
i__22085 = G__22440;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22086),re_com$nested_grid$iter__22083(cljs.core.chunk_rest(s__22084__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22086),null);
}
} else {
var path = cljs.core.first(s__22084__$2);
var props = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258),new cljs.core.Keyword(null,"column-paths","column-paths",1944282824),new cljs.core.Keyword(null,"selection?","selection?",804764555),new cljs.core.Keyword(null,"show?","show?",1543842127),new cljs.core.Keyword(null,"last-mouse-x","last-mouse-x",150112047),new cljs.core.Keyword(null,"column-header","column-header",-1495823888),new cljs.core.Keyword(null,"mouse-down-x","mouse-down-x",-1461595497),new cljs.core.Keyword(null,"resize-handler","resize-handler",-169459881),new cljs.core.Keyword(null,"drag","drag",449951290),new cljs.core.Keyword(null,"column-path","column-path",-733367618),new cljs.core.Keyword(null,"on-resize","on-resize",-2005528129)],[mouse_x,column_paths,selection_QMARK_,show_QMARK_(path,new cljs.core.Keyword(null,"column","column",2078222095)),last_mouse_x,column_header,mouse_down_x,resize_handler,drag,path,resize_column_BANG_]);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,column_header_wrapper,props,re_com.nested_grid.column_header_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","column","re-com.nested-grid/column",626823356),(function (){var or__5142__auto__ = path;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.gensym.cljs$core$IFn$_invoke$arity$0();
}
})()], null)], null)),re_com$nested_grid$iter__22083(cljs.core.rest(s__22084__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(column_paths);
})());
var row_header_cells = cljs.core.doall.cljs$core$IFn$_invoke$arity$1((function (){var iter__5628__auto__ = (function re_com$nested_grid$iter__22112(s__22113){
return (new cljs.core.LazySeq(null,(function (){
var s__22113__$1 = s__22113;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22113__$1);
if(temp__5825__auto__){
var s__22113__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__22113__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22113__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22115 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22114 = (0);
while(true){
if((i__22114 < size__5627__auto__)){
var path = cljs.core._nth(c__5626__auto__,i__22114);
var props = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"row-path","row-path",-709648669),new cljs.core.Keyword(null,"row-paths","row-paths",-1900396315),new cljs.core.Keyword(null,"last-mouse-y","last-mouse-y",-1701805882),new cljs.core.Keyword(null,"row-header","row-header",1799050794),new cljs.core.Keyword(null,"selection?","selection?",804764555),new cljs.core.Keyword(null,"show?","show?",1543842127),new cljs.core.Keyword(null,"mouse-down-y","mouse-down-y",1419003887),new cljs.core.Keyword(null,"resize-handler","resize-handler",-169459881),new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488),new cljs.core.Keyword(null,"drag","drag",449951290),new cljs.core.Keyword(null,"on-resize","on-resize",-2005528129)],[path,row_paths,last_mouse_y,row_header,selection_QMARK_,show_QMARK_(path,new cljs.core.Keyword(null,"row","row",-570139521)),mouse_down_y,resize_handler,mouse_y,drag,resize_row_BANG_]);
cljs.core.chunk_append(b__22115,cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,row_header_wrapper,props,re_com.nested_grid.row_header_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","row","re-com.nested-grid/row",-2106001844),(function (){var or__5142__auto__ = path;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.gensym.cljs$core$IFn$_invoke$arity$0();
}
})()], null)], null)));

var G__22454 = (i__22114 + (1));
i__22114 = G__22454;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22115),re_com$nested_grid$iter__22112(cljs.core.chunk_rest(s__22113__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22115),null);
}
} else {
var path = cljs.core.first(s__22113__$2);
var props = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"row-path","row-path",-709648669),new cljs.core.Keyword(null,"row-paths","row-paths",-1900396315),new cljs.core.Keyword(null,"last-mouse-y","last-mouse-y",-1701805882),new cljs.core.Keyword(null,"row-header","row-header",1799050794),new cljs.core.Keyword(null,"selection?","selection?",804764555),new cljs.core.Keyword(null,"show?","show?",1543842127),new cljs.core.Keyword(null,"mouse-down-y","mouse-down-y",1419003887),new cljs.core.Keyword(null,"resize-handler","resize-handler",-169459881),new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488),new cljs.core.Keyword(null,"drag","drag",449951290),new cljs.core.Keyword(null,"on-resize","on-resize",-2005528129)],[path,row_paths,last_mouse_y,row_header,selection_QMARK_,show_QMARK_(path,new cljs.core.Keyword(null,"row","row",-570139521)),mouse_down_y,resize_handler,mouse_y,drag,resize_row_BANG_]);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,row_header_wrapper,props,re_com.nested_grid.row_header_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","row","re-com.nested-grid/row",-2106001844),(function (){var or__5142__auto__ = path;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.gensym.cljs$core$IFn$_invoke$arity$0();
}
})()], null)], null)),re_com$nested_grid$iter__22112(cljs.core.rest(s__22113__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(row_paths);
})());
var header_spacer_cells = (function (){var iter__5628__auto__ = (function re_com$nested_grid$iter__22156(s__22157){
return (new cljs.core.LazySeq(null,(function (){
var s__22157__$1 = s__22157;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22157__$1);
if(temp__5825__auto__){
var xs__6385__auto__ = temp__5825__auto__;
var y = cljs.core.first(xs__6385__auto__);
var iterys__5624__auto__ = ((function (s__22157__$1,y,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height){
return (function re_com$nested_grid$iter__22156_$_iter__22159(s__22160){
return (new cljs.core.LazySeq(null,((function (s__22157__$1,y,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height){
return (function (){
var s__22160__$1 = s__22160;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__22160__$1);
if(temp__5825__auto____$1){
var s__22160__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__22160__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22160__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22162 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22161 = (0);
while(true){
if((i__22161 < size__5627__auto__)){
var x = cljs.core._nth(c__5626__auto__,i__22161);
cljs.core.chunk_append(b__22162,cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,header_spacer_wrapper,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"theme","theme",-1247880880),theme,new cljs.core.Keyword(null,"x","x",2099068185),x,new cljs.core.Keyword(null,"y","y",-1757859776),y,new cljs.core.Keyword(null,"header-spacer","header-spacer",700022605),header_spacer], null),re_com.nested_grid.header_spacer_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","header-spacer","re-com.nested-grid/header-spacer",1648682330),x,y], null)], null)));

var G__22456 = (i__22161 + (1));
i__22161 = G__22456;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22162),re_com$nested_grid$iter__22156_$_iter__22159(cljs.core.chunk_rest(s__22160__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22162),null);
}
} else {
var x = cljs.core.first(s__22160__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,header_spacer_wrapper,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"theme","theme",-1247880880),theme,new cljs.core.Keyword(null,"x","x",2099068185),x,new cljs.core.Keyword(null,"y","y",-1757859776),y,new cljs.core.Keyword(null,"header-spacer","header-spacer",700022605),header_spacer], null),re_com.nested_grid.header_spacer_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","header-spacer","re-com.nested-grid/header-spacer",1648682330),x,y], null)], null)),re_com$nested_grid$iter__22156_$_iter__22159(cljs.core.rest(s__22160__$2)));
}
} else {
return null;
}
break;
}
});})(s__22157__$1,y,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height))
,null,null));
});})(s__22157__$1,y,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height))
;
var fs__5625__auto__ = cljs.core.seq(iterys__5624__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(row_depth)));
if(fs__5625__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5625__auto__,re_com$nested_grid$iter__22156(cljs.core.rest(s__22157__$1)));
} else {
var G__22458 = cljs.core.rest(s__22157__$1);
s__22157__$1 = G__22458;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(column_depth));
})();
var cells = cljs.core.doall.cljs$core$IFn$_invoke$arity$1((function (){var iter__5628__auto__ = (function re_com$nested_grid$iter__22215(s__22216){
return (new cljs.core.LazySeq(null,(function (){
var s__22216__$1 = s__22216;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22216__$1);
if(temp__5825__auto__){
var xs__6385__auto__ = temp__5825__auto__;
var column_path = cljs.core.first(xs__6385__auto__);
var iterys__5624__auto__ = ((function (s__22216__$1,column_path,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,header_spacer_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height){
return (function re_com$nested_grid$iter__22215_$_iter__22217(s__22218){
return (new cljs.core.LazySeq(null,((function (s__22216__$1,column_path,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,header_spacer_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height){
return (function (){
var s__22218__$1 = s__22218;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__22218__$1);
if(temp__5825__auto____$1){
var s__22218__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__22218__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22218__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22220 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22219 = (0);
while(true){
if((i__22219 < size__5627__auto__)){
var row_path = cljs.core._nth(c__5626__auto__,i__22219);
var props = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"column-path","column-path",-733367618),column_path,new cljs.core.Keyword(null,"row-path","row-path",-709648669),row_path,new cljs.core.Keyword(null,"cell","cell",764245084),cell], null);
cljs.core.chunk_append(b__22220,cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,cell_wrapper,props,re_com.nested_grid.cell_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","cell","re-com.nested-grid/cell",1997200967),(function (){var or__5142__auto__ = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [column_path,row_path], null);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.gensym.cljs$core$IFn$_invoke$arity$0();
}
})()], null)], null)));

var G__22463 = (i__22219 + (1));
i__22219 = G__22463;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22220),re_com$nested_grid$iter__22215_$_iter__22217(cljs.core.chunk_rest(s__22218__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22220),null);
}
} else {
var row_path = cljs.core.first(s__22218__$2);
var props = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"column-path","column-path",-733367618),column_path,new cljs.core.Keyword(null,"row-path","row-path",-709648669),row_path,new cljs.core.Keyword(null,"cell","cell",764245084),cell], null);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.part,cell_wrapper,props,re_com.nested_grid.cell_wrapper_part], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","cell","re-com.nested-grid/cell",1997200967),(function (){var or__5142__auto__ = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [column_path,row_path], null);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return cljs.core.gensym.cljs$core$IFn$_invoke$arity$0();
}
})()], null)], null)),re_com$nested_grid$iter__22215_$_iter__22217(cljs.core.rest(s__22218__$2)));
}
} else {
return null;
}
break;
}
});})(s__22216__$1,column_path,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,header_spacer_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height))
,null,null));
});})(s__22216__$1,column_path,xs__6385__auto__,temp__5825__auto__,theme,themed,column_paths,column_leaf_paths,leaf_column_QMARK_,row_paths,leaf_row_QMARK_,leaf_QMARK_,show_QMARK_,showing_column_paths,showing_row_paths,showing_column_widths,showing_row_heights,max_column_heights,max_row_widths,column_depth,row_depth,on_export_cell__$1,default_on_export,on_export__$1,cell_grid_columns,cell_grid_rows,spacer_QMARK_,export_column_headers,export_row_headers,export_cells,export_spacers,control_panel,cell_grid_container,column_header_cells,row_header_cells,header_spacer_cells,map__22047,map__22047__$1,header_spacer_wrapper,on_export_cell,cell,on_export,column_tree,show_selection_box_QMARK_,cell_wrapper,max_height,on_export_column_header,show_export_button_QMARK_,row_header,row_tree,on_export_row_header,row_height__$1,header_spacer,column_header,row_header_width,column_header_height,show_branch_paths_QMARK_,column_width__$1,row_header_wrapper,column_header_wrapper,column_state,row_state,hover_QMARK_,drag,selection_QMARK_,mouse_down_x,mouse_down_y,last_mouse_x,last_mouse_y,mouse_x,mouse_y,scroll_top,scroll_left,column_header_prop,header_prop,max_props,resize_column_BANG_,resize_row_BANG_,resize_handler,map__21998,map__21998__$1,column_width,row_height))
;
var fs__5625__auto__ = cljs.core.seq(iterys__5624__auto__(showing_row_paths));
if(fs__5625__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5625__auto__,re_com$nested_grid$iter__22215(cljs.core.rest(s__22216__$1)));
} else {
var G__22467 = cljs.core.rest(s__22216__$1);
s__22216__$1 = G__22467;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(showing_column_paths);
})());
var zebra_stripes = (function (){var iter__5628__auto__ = (function re_com$nested_grid$iter__22245(s__22246){
return (new cljs.core.LazySeq(null,(function (){
var s__22246__$1 = s__22246;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__22246__$1);
if(temp__5825__auto__){
var s__22246__$2 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(s__22246__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__22246__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__22248 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__22247 = (0);
while(true){
if((i__22247 < size__5627__auto__)){
var i = cljs.core._nth(c__5626__auto__,i__22247);
cljs.core.chunk_append(b__22248,cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),themed(new cljs.core.Keyword("re-com.nested-grid","zebra-stripe","re-com.nested-grid/zebra-stripe",1996846481),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"grid-column-start","grid-column-start",718549073),(1),new cljs.core.Keyword(null,"grid-column-end","grid-column-end",592662211),"end",new cljs.core.Keyword(null,"grid-row","grid-row",-1737175087),i,new cljs.core.Keyword(null,"background-color","background-color",570434026),"cornflowerblue",new cljs.core.Keyword(null,"opacity","opacity",397153780),0.05,new cljs.core.Keyword(null,"z-index","z-index",1892827090),(2),new cljs.core.Keyword(null,"pointer-events","pointer-events",-1053858853),"none"], null)], null))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","zebra-stripe","re-com.nested-grid/zebra-stripe",1996846481),i], null)], null)));

var G__22471 = (i__22247 + (1));
i__22247 = G__22471;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__22248),re_com$nested_grid$iter__22245(cljs.core.chunk_rest(s__22246__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__22248),null);
}
} else {
var i = cljs.core.first(s__22246__$2);
return cljs.core.cons(cljs.core.with_meta(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),themed(new cljs.core.Keyword("re-com.nested-grid","zebra-stripe","re-com.nested-grid/zebra-stripe",1996846481),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"grid-column-start","grid-column-start",718549073),(1),new cljs.core.Keyword(null,"grid-column-end","grid-column-end",592662211),"end",new cljs.core.Keyword(null,"grid-row","grid-row",-1737175087),i,new cljs.core.Keyword(null,"background-color","background-color",570434026),"cornflowerblue",new cljs.core.Keyword(null,"opacity","opacity",397153780),0.05,new cljs.core.Keyword(null,"z-index","z-index",1892827090),(2),new cljs.core.Keyword(null,"pointer-events","pointer-events",-1053858853),"none"], null)], null))], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("re-com.nested-grid","zebra-stripe","re-com.nested-grid/zebra-stripe",1996846481),i], null)], null)),re_com$nested_grid$iter__22245(cljs.core.rest(s__22246__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.even_QMARK_,cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(row_paths))));
})();
var box_selector = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.selection_part,new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"drag","drag",449951290),drag,new cljs.core.Keyword(null,"grid-columns","grid-columns",-1398061070),cell_grid_columns,new cljs.core.Keyword(null,"grid-rows","grid-rows",-943125795),cell_grid_rows,new cljs.core.Keyword(null,"selection?","selection?",804764555),selection_QMARK_,new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258),mouse_x,new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488),mouse_y,new cljs.core.Keyword(null,"mouse-down-x","mouse-down-x",-1461595497),mouse_down_x,new cljs.core.Keyword(null,"mouse-down-y","mouse-down-y",1419003887),mouse_down_y], null)], null);
var native_scrollbar_width = (10);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"<>","<>",1280186386),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-mouse-enter","on-mouse-enter",-1664921661),(function (){
return cljs.core.reset_BANG_(hover_QMARK_,true);
}),new cljs.core.Keyword(null,"on-mouse-leave","on-mouse-leave",-1864319528),(function (){
return cljs.core.reset_BANG_(hover_QMARK_,false);
}),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"display","display",242065432),"grid",new cljs.core.Keyword(null,"grid-template-columns","grid-template-columns",-594112133),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.util.px(cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core._PLUS_,max_row_widths)),re_com.util.px((native_scrollbar_width + cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core._PLUS_,showing_column_widths)))], null)),new cljs.core.Keyword(null,"grid-template-rows","grid-template-rows",-372292629),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["20px",showing_column_widths,re_com.util.px(cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core._PLUS_,max_column_heights)),re_com.util.px(cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core._PLUS_,showing_row_heights))], null))], null)], null),control_panel,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"display","display",242065432),"grid",new cljs.core.Keyword(null,"grid-template-columns","grid-template-columns",-594112133),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(max_row_widths),new cljs.core.Keyword(null,"grid-template-rows","grid-template-rows",-372292629),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(max_column_heights)], null)], null),header_spacer_cells], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.scroll_container,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"scroll-left","scroll-left",-211761103),scroll_left], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"display","display",242065432),"grid",new cljs.core.Keyword(null,"grid-template-columns","grid-template-columns",-594112133),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(cell_grid_columns),new cljs.core.Keyword(null,"grid-template-rows","grid-template-rows",-372292629),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(max_column_heights)], null)], null),column_header_cells], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.scroll_container,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"scroll-top","scroll-top",-46723100),scroll_top,new cljs.core.Keyword(null,"height","height",1025178622),max_height], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"display","display",242065432),"grid",new cljs.core.Keyword(null,"grid-template-columns","grid-template-columns",-594112133),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(max_row_widths),new cljs.core.Keyword(null,"grid-template-rows","grid-template-rows",-372292629),re_com.nested_grid.grid_template.cljs$core$IFn$_invoke$arity$1(cell_grid_rows)], null)], null),row_header_cells], null)], null),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cell_grid_container,cells),zebra_stripes),(cljs.core.truth_(show_selection_box_QMARK_)?box_selector:null))], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("re-com.nested-grid","selection","re-com.nested-grid/selection",1957297550),cljs.core.deref(drag)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.drag_overlay,new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"drag","drag",449951290),drag,new cljs.core.Keyword(null,"grid-columns","grid-columns",-1398061070),cell_grid_columns,new cljs.core.Keyword(null,"grid-rows","grid-rows",-943125795),cell_grid_rows,new cljs.core.Keyword(null,"selection?","selection?",804764555),selection_QMARK_,new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258),mouse_x,new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488),mouse_y,new cljs.core.Keyword(null,"mouse-down-x","mouse-down-x",-1461595497),mouse_down_x,new cljs.core.Keyword(null,"mouse-down-y","mouse-down-y",1419003887),mouse_down_y], null)], null):null),(cljs.core.truth_((function (){var G__22271 = cljs.core.deref(drag);
var fexpr__22270 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("re-com.nested-grid","row","re-com.nested-grid/row",-2106001844),null,new cljs.core.Keyword("re-com.nested-grid","column","re-com.nested-grid/column",626823356),null], null), null);
return (fexpr__22270.cljs$core$IFn$_invoke$arity$1 ? fexpr__22270.cljs$core$IFn$_invoke$arity$1(G__22271) : fexpr__22270.call(null,G__22271));
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [re_com.nested_grid.resize_overlay,new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"drag","drag",449951290),drag,new cljs.core.Keyword(null,"mouse-x","mouse-x",-195915258),mouse_x,new cljs.core.Keyword(null,"mouse-y","mouse-y",83174488),mouse_y,new cljs.core.Keyword(null,"last-mouse-x","last-mouse-x",150112047),last_mouse_x,new cljs.core.Keyword(null,"last-mouse-y","last-mouse-y",-1701805882),last_mouse_y,new cljs.core.Keyword(null,"on-resize","on-resize",-2005528129),resize_handler], null)], null):null)], null);
};
var G__22402 = function (var_args){
var p__22046 = null;
if (arguments.length > 0) {
var G__22472__i = 0, G__22472__a = new Array(arguments.length -  0);
while (G__22472__i < G__22472__a.length) {G__22472__a[G__22472__i] = arguments[G__22472__i + 0]; ++G__22472__i;}
  p__22046 = new cljs.core.IndexedSeq(G__22472__a,0,null);
} 
return G__22402__delegate.call(this,p__22046);};
G__22402.cljs$lang$maxFixedArity = 0;
G__22402.cljs$lang$applyTo = (function (arglist__22473){
var p__22046 = cljs.core.seq(arglist__22473);
return G__22402__delegate(p__22046);
});
G__22402.cljs$core$IFn$_invoke$arity$variadic = G__22402__delegate;
return G__22402;
})()
;
}));

(re_com.nested_grid.nested_grid.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(re_com.nested_grid.nested_grid.cljs$lang$applyTo = (function (seq21979){
var self__5862__auto__ = this;
return self__5862__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq21979));
}));


//# sourceMappingURL=re_com.nested_grid.js.map
