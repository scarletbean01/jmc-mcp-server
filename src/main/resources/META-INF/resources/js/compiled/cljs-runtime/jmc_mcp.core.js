goog.provide('jmc_mcp.core');
jmc_mcp.core.dev_setup = (function jmc_mcp$core$dev_setup(){
if(goog.DEBUG){
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["dev mode"], 0));
} else {
return null;
}
});
jmc_mcp.core.mount_root = (function jmc_mcp$core$mount_root(){
re_frame.core.clear_subscription_cache_BANG_();

var root_el = document.getElementById("app");
reagent.dom.unmount_component_at_node(root_el);

return reagent.dom.render.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [jmc_mcp.views.layout.main_layout], null),root_el);
});
jmc_mcp.core.init = (function jmc_mcp$core$init(){
re_frame.core.dispatch_sync(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"initialize-db","initialize-db",230998432)], null));

jmc_mcp.core.dev_setup();

jmc_mcp.routes.init_routes_BANG_();

return jmc_mcp.core.mount_root();
});

//# sourceMappingURL=jmc_mcp.core.js.map
