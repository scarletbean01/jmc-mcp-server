goog.provide('day8.re_frame_10x.panels.settings.subs');
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (p__23795,_){
var map__23797 = p__23795;
var map__23797__$1 = cljs.core.__destructure_map(map__23797);
var settings = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23797__$1,new cljs.core.Keyword(null,"settings","settings",1556144875));
return settings;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","panel-width%","day8.re-frame-10x.panels.settings.subs/panel-width%",1546996863),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23798,_){
var map__23799 = p__23798;
var map__23799__$1 = cljs.core.__destructure_map(map__23799);
var panel_width_PERCENT_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23799__$1,new cljs.core.Keyword(null,"panel-width%","panel-width%",-110515341));
return panel_width_PERCENT_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","panel-width%-rounded","day8.re-frame-10x.panels.settings.subs/panel-width%-rounded",16963626),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","panel-width%","day8.re-frame-10x.panels.settings.subs/panel-width%",1546996863)], null),(function (panel_width_PERCENT_,p__23801){
var vec__23802 = p__23801;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23802,(0),null);
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23802,(1),null);
return ((Math.ceil(((panel_width_PERCENT_ * (100)) / n)) * n) / 100.0);
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","window-width","day8.re-frame-10x.panels.settings.subs/window-width",419613467),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23807,_){
var map__23808 = p__23807;
var map__23808__$1 = cljs.core.__destructure_map(map__23808);
var window_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23808__$1,new cljs.core.Keyword(null,"window-width","window-width",2057825599));
return window_width;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","window-width-rounded","day8.re-frame-10x.panels.settings.subs/window-width-rounded",193095108),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","window-width","day8.re-frame-10x.panels.settings.subs/window-width",419613467)], null),(function (width,p__23809){
var vec__23810 = p__23809;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23810,(0),null);
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23810,(1),null);
return (Math.ceil((width / n)) * n);
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","show-panel?","day8.re-frame-10x.panels.settings.subs/show-panel?",-1305358312),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23813,_){
var map__23814 = p__23813;
var map__23814__$1 = cljs.core.__destructure_map(map__23814);
var show_panel_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23814__$1,new cljs.core.Keyword(null,"show-panel?","show-panel?",1475128892));
return show_panel_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","showing-settings?","day8.re-frame-10x.panels.settings.subs/showing-settings?",1700568638),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23815,_){
var map__23816 = p__23815;
var map__23816__$1 = cljs.core.__destructure_map(map__23816);
var showing_settings_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23816__$1,new cljs.core.Keyword(null,"showing-settings?","showing-settings?",-140540878));
return showing_settings_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","selected-tab","day8.re-frame-10x.panels.settings.subs/selected-tab",1020534208),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","showing-settings?","day8.re-frame-10x.panels.settings.subs/showing-settings?",1700568638)], null),(function (p__23817,_){
var vec__23819 = p__23817;
var map__23822 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23819,(0),null);
var map__23822__$1 = cljs.core.__destructure_map(map__23822);
var selected_tab = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23822__$1,new cljs.core.Keyword(null,"selected-tab","selected-tab",-1558510156));
var showing_settings_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23819,(1),null);
if(cljs.core.truth_(showing_settings_QMARK_)){
return new cljs.core.Keyword(null,"settings","settings",1556144875);
} else {
return selected_tab;
}
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","number-of-retained-epochs","day8.re-frame-10x.panels.settings.subs/number-of-retained-epochs",-789938705),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23824,_){
var map__23825 = p__23824;
var map__23825__$1 = cljs.core.__destructure_map(map__23825);
var number_of_epochs = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23825__$1,new cljs.core.Keyword(null,"number-of-epochs","number-of-epochs",57769252));
return number_of_epochs;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","ignored-events","day8.re-frame-10x.panels.settings.subs/ignored-events",216559761),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23827,_){
var map__23828 = p__23827;
var map__23828__$1 = cljs.core.__destructure_map(map__23828);
var ignored_events = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23828__$1,new cljs.core.Keyword(null,"ignored-events","ignored-events",1738756589));
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort","sort",953465918),cljs.core.vals(ignored_events));
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","filtered-view-trace","day8.re-frame-10x.panels.settings.subs/filtered-view-trace",-192195211),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23829,_){
var map__23830 = p__23829;
var map__23830__$1 = cljs.core.__destructure_map(map__23830);
var filtered_view_trace = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23830__$1,new cljs.core.Keyword(null,"filtered-view-trace","filtered-view-trace",-901876599));
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort","sort",953465918),cljs.core.vals(filtered_view_trace));
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","low-level-trace","day8.re-frame-10x.panels.settings.subs/low-level-trace",-929378536),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23833,_){
var map__23834 = p__23833;
var map__23834__$1 = cljs.core.__destructure_map(map__23834);
var low_level_trace = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23834__$1,new cljs.core.Keyword(null,"low-level-trace","low-level-trace",638447092));
return low_level_trace;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","debug?","day8.re-frame-10x.panels.settings.subs/debug?",-1124155521),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23837,_){
var map__23838 = p__23837;
var map__23838__$1 = cljs.core.__destructure_map(map__23838);
var debug_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23838__$1,new cljs.core.Keyword(null,"debug?","debug?",-1831756173));
return debug_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","app-db-follows-events?","day8.re-frame-10x.panels.settings.subs/app-db-follows-events?",1075206342),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23839,_){
var map__23841 = p__23839;
var map__23841__$1 = cljs.core.__destructure_map(map__23841);
var app_db_follows_events_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23841__$1,new cljs.core.Keyword(null,"app-db-follows-events?","app-db-follows-events?",-1566738462));
return app_db_follows_events_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","ambiance","day8.re-frame-10x.panels.settings.subs/ambiance",-230258012),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23843,_){
var map__23844 = p__23843;
var map__23844__$1 = cljs.core.__destructure_map(map__23844);
var ambiance = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23844__$1,new cljs.core.Keyword(null,"ambiance","ambiance",-1936594032));
return ambiance;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","syntax-color-scheme","day8.re-frame-10x.panels.settings.subs/syntax-color-scheme",-661033240),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23845,_){
var map__23846 = p__23845;
var map__23846__$1 = cljs.core.__destructure_map(map__23846);
var syntax_color_scheme = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23846__$1,new cljs.core.Keyword(null,"syntax-color-scheme","syntax-color-scheme",2062388740));
return syntax_color_scheme;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","show-event-history?","day8.re-frame-10x.panels.settings.subs/show-event-history?",561736700),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23849,_){
var map__23850 = p__23849;
var map__23850__$1 = cljs.core.__destructure_map(map__23850);
var show_event_history_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23850__$1,new cljs.core.Keyword(null,"show-event-history?","show-event-history?",398887712));
return show_event_history_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","open-new-inspectors?","day8.re-frame-10x.panels.settings.subs/open-new-inspectors?",965777560),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23851,_){
var map__23852 = p__23851;
var map__23852__$1 = cljs.core.__destructure_map(map__23852);
var open_new_inspectors_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23852__$1,new cljs.core.Keyword(null,"open-new-inspectors?","open-new-inspectors?",-3558540));
return open_new_inspectors_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","handle-keys?","day8.re-frame-10x.panels.settings.subs/handle-keys?",980361219),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23853,_){
var map__23854 = p__23853;
var map__23854__$1 = cljs.core.__destructure_map(map__23854);
var handle_keys_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23854__$1,new cljs.core.Keyword(null,"handle-keys?","handle-keys?",-793509665));
return handle_keys_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","ready-to-bind-key","day8.re-frame-10x.panels.settings.subs/ready-to-bind-key",-1320760670),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23855,_){
var map__23856 = p__23855;
var map__23856__$1 = cljs.core.__destructure_map(map__23856);
var ready_to_bind_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23856__$1,new cljs.core.Keyword(null,"ready-to-bind-key","ready-to-bind-key",1469196678));
return ready_to_bind_key;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","key-bindings","day8.re-frame-10x.panels.settings.subs/key-bindings",1115746214),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23857,p__23858){
var map__23859 = p__23857;
var map__23859__$1 = cljs.core.__destructure_map(map__23859);
var key_bindings = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23859__$1,new cljs.core.Keyword(null,"key-bindings","key-bindings",-1464217982));
var vec__23860 = p__23858;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23860,(0),null);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23860,(1),null);
if(cljs.core.truth_(k)){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(key_bindings,k);
} else {
return key_bindings;
}
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","log-outputs","day8.re-frame-10x.panels.settings.subs/log-outputs",549166949),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23864,p__23865){
var map__23866 = p__23864;
var map__23866__$1 = cljs.core.__destructure_map(map__23866);
var log_outputs = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23866__$1,new cljs.core.Keyword(null,"log-outputs","log-outputs",-687309247));
var vec__23867 = p__23865;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23867,(0),null);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__23867,(1),null);
if(cljs.core.truth_(k)){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(log_outputs,k);
} else {
return log_outputs;
}
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","any-log-outputs?","day8.re-frame-10x.panels.settings.subs/any-log-outputs?",-1386238756),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","log-outputs","day8.re-frame-10x.panels.settings.subs/log-outputs",549166949)], null),(function (log_outputs,_){
return (!((cljs.core.seq(log_outputs) == null)));
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","log-pretty?","day8.re-frame-10x.panels.settings.subs/log-pretty?",995509818),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23874,_){
var map__23875 = p__23874;
var map__23875__$1 = cljs.core.__destructure_map(map__23875);
var log_pretty_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23875__$1,new cljs.core.Keyword(null,"log-pretty?","log-pretty?",-1627576290));
return log_pretty_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","alias-namespaces?","day8.re-frame-10x.panels.settings.subs/alias-namespaces?",507809565),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23878,_){
var map__23881 = p__23878;
var map__23881__$1 = cljs.core.__destructure_map(map__23881);
var alias_namespaces_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23881__$1,new cljs.core.Keyword(null,"alias-namespaces?","alias-namespaces?",-587355207));
return alias_namespaces_QMARK_;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","ns-aliases","day8.re-frame-10x.panels.settings.subs/ns-aliases",1731500737),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),(function (p__23882,_){
var map__23883 = p__23882;
var map__23883__$1 = cljs.core.__destructure_map(map__23883);
var ns_aliases = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23883__$1,new cljs.core.Keyword(null,"ns-aliases","ns-aliases",1290254821));
return ns_aliases;
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","ns->alias","day8.re-frame-10x.panels.settings.subs/ns->alias",925252888),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","ns-aliases","day8.re-frame-10x.panels.settings.subs/ns-aliases",1731500737)], null),(function (ns_aliases){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.juxt.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"ns-full","ns-full",225339386),new cljs.core.Keyword(null,"ns-alias","ns-alias",1705921737)),cljs.core.val),ns_aliases));
})], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","expansion-limit","day8.re-frame-10x.panels.settings.subs/expansion-limit",740463148),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),new cljs.core.Keyword(null,"->","->",514830339),new cljs.core.Keyword(null,"expansion-limit","expansion-limit",-2040810736)], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","trace-when","day8.re-frame-10x.panels.settings.subs/trace-when",-1240430671),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),new cljs.core.Keyword(null,"->","->",514830339),new cljs.core.Keyword(null,"trace-when","trace-when",-1902271347)], 0));
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_sub.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","display-uuids-as","day8.re-frame-10x.panels.settings.subs/display-uuids-as",-1663111459),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"<-","<-",760412998),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.panels.settings.subs","root","day8.re-frame-10x.panels.settings.subs/root",-2012357833)], null),new cljs.core.Keyword(null,"->","->",514830339),new cljs.core.Keyword(null,"display-uuids-as","display-uuids-as",-1180595271)], 0));

//# sourceMappingURL=day8.re_frame_10x.panels.settings.subs.js.map
