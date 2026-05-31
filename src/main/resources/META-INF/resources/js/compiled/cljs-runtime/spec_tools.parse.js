goog.provide('spec_tools.parse');
spec_tools.parse.type_dispatch_value = (function spec_tools$parse$type_dispatch_value(type){
var fexpr__24697 = ((cljs.core.sequential_QMARK_(type))?cljs.core.first:cljs.core.identity);
return (fexpr__24697.cljs$core$IFn$_invoke$arity$1 ? fexpr__24697.cljs$core$IFn$_invoke$arity$1(type) : fexpr__24697.call(null,type));
});
spec_tools.parse.collection_type_QMARK_ = (function spec_tools$parse$collection_type_QMARK_(type){
return cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"tuple","tuple",-472667284),null,new cljs.core.Keyword(null,"vector","vector",1902966158),null,new cljs.core.Keyword(null,"map-of","map-of",1189682355),null,new cljs.core.Keyword(null,"set","set",304602554),null,new cljs.core.Keyword(null,"map","map",1371690461),null], null), null),type);
});
spec_tools.parse.leaf_type_QMARK_ = (function spec_tools$parse$leaf_type_QMARK_(type){
return (!(cljs.core.contains_QMARK_((spec_tools.parse.non_leaf_types.cljs$core$IFn$_invoke$arity$0 ? spec_tools.parse.non_leaf_types.cljs$core$IFn$_invoke$arity$0() : spec_tools.parse.non_leaf_types.call(null)),type)));
});
/**
 * Parses info out of a spec. Spec can be passed as a name, Spec or a form.
 *   Returns either `nil` or a map, with keys `:type` and other extra keys
 *   (like `:keys` for s/keys specs).
 */
spec_tools.parse.parse_spec = (function spec_tools$parse$parse_spec(var_args){
var G__24711 = arguments.length;
switch (G__24711) {
case 1:
return spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$1 = (function (x){
return spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2(x,null);
}));

(spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2 = (function (x,options){
while(true){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("cljs.spec.alpha","unknown","cljs.spec.alpha/unknown",651034818),x)){
return null;
} else {
if(cljs.core.qualified_keyword_QMARK_(x)){
if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("spec-tools.parse","visited","spec-tools.parse/visited",-1368315973).cljs$core$IFn$_invoke$arity$1(options),x))){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),x], null);
} else {
return spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2(cljs.spec.alpha.form(cljs.spec.alpha.get_spec(x)),cljs.core.update.cljs$core$IFn$_invoke$arity$4(options,new cljs.core.Keyword("spec-tools.parse","visited","spec-tools.parse/visited",-1368315973),cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core.conj,cljs.core.PersistentHashSet.EMPTY),x));
}
} else {
if((x instanceof cljs.core.Symbol)){
var G__24716 = spec_tools.impl.normalize_symbol(x);
var G__24717 = null;
var G__24718 = options;
return (spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3 ? spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3(G__24716,G__24717,G__24718) : spec_tools.parse.parse_form.call(null,G__24716,G__24717,G__24718));
} else {
if(cljs.core.seq_QMARK_(x)){
var G__24720 = spec_tools.impl.normalize_symbol(cljs.core.first(x));
var G__24721 = x;
var G__24722 = options;
return (spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3 ? spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3(G__24720,G__24721,G__24722) : spec_tools.parse.parse_form.call(null,G__24720,G__24721,G__24722));
} else {
if(cljs.core.truth_(cljs.spec.alpha.spec_QMARK_(x))){
var G__24953 = cljs.spec.alpha.form(x);
var G__24954 = options;
x = G__24953;
options = G__24954;
continue;
} else {
if(cljs.core.ifn_QMARK_(x)){
var G__24723 = spec_tools.impl.normalize_symbol(spec_tools.form.resolve_form.cljs$core$IFn$_invoke$arity$1(x));
var G__24724 = null;
var G__24725 = options;
return (spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3 ? spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3(G__24723,G__24724,G__24725) : spec_tools.parse.parse_form.call(null,G__24723,G__24724,G__24725));
} else {
return (spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3 ? spec_tools.parse.parse_form.cljs$core$IFn$_invoke$arity$3(x,null,options) : spec_tools.parse.parse_form.call(null,x,null,options));

}
}
}
}
}
}
break;
}
}));

(spec_tools.parse.parse_spec.cljs$lang$maxFixedArity = 2);

spec_tools.parse.parse_spec_with_spec_ref = (function spec_tools$parse$parse_spec_with_spec_ref(x,options){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2(x,options),((cljs.core.qualified_keyword_QMARK_(x))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),x], null):null)], 0));
});
spec_tools.parse.get_keys = (function spec_tools$parse$get_keys(parse_data){
var or__5142__auto__ = new cljs.core.Keyword("spec-tools.parse","keys","spec-tools.parse/keys",1331815460).cljs$core$IFn$_invoke$arity$1(parse_data);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
var G__24736 = parse_data;
var G__24736__$1 = (((G__24736 == null))?null:new cljs.core.Keyword("spec-tools.parse","items","spec-tools.parse/items",1255627600).cljs$core$IFn$_invoke$arity$1(G__24736));
var G__24736__$2 = (((G__24736__$1 == null))?null:cljs.core.keep.cljs$core$IFn$_invoke$arity$2(spec_tools.parse.get_keys,G__24736__$1));
var G__24736__$3 = (((G__24736__$2 == null))?null:cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.concat,G__24736__$2));
var G__24736__$4 = (((G__24736__$3 == null))?null:cljs.core.seq(G__24736__$3));
if((G__24736__$4 == null)){
return null;
} else {
return cljs.core.set(G__24736__$4);
}
}
});
if((typeof spec_tools !== 'undefined') && (typeof spec_tools.parse !== 'undefined') && (typeof spec_tools.parse.parse_form !== 'undefined')){
} else {
spec_tools.parse.parse_form = (function (){var method_table__5747__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__5748__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__5749__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__5750__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__5751__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"default","default",-1987822328),new cljs.core.Keyword("spec-tools.parse","default","spec-tools.parse/default",-1184283850)], null),new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__24744 = cljs.core.get_global_hierarchy;
return (fexpr__24744.cljs$core$IFn$_invoke$arity$0 ? fexpr__24744.cljs$core$IFn$_invoke$arity$0() : fexpr__24744.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("spec-tools.parse","parse-form"),(function (dispatch,_,___$1){
return dispatch;
}),new cljs.core.Keyword("spec-tools.parse","default","spec-tools.parse/default",-1184283850),hierarchy__5751__auto__,method_table__5747__auto__,prefer_table__5748__auto__,method_cache__5749__auto__,cached_hierarchy__5750__auto__));
})();
}
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("spec-tools.parse","default","spec-tools.parse/default",-1184283850),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"type","type",1174270348),null], null);
}));
spec_tools.parse.non_leaf_types = (function spec_tools$parse$non_leaf_types(){
return new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 9, [new cljs.core.Keyword(null,"or","or",235744169),null,new cljs.core.Keyword(null,"multi-spec","multi-spec",1274719724),null,new cljs.core.Keyword(null,"tuple","tuple",-472667284),null,new cljs.core.Keyword(null,"vector","vector",1902966158),null,new cljs.core.Keyword(null,"map-of","map-of",1189682355),null,new cljs.core.Keyword(null,"and","and",-971899817),null,new cljs.core.Keyword(null,"set","set",304602554),null,new cljs.core.Keyword(null,"map","map",1371690461),null,new cljs.core.Keyword(null,"nilable","nilable",1842307102),null], null), null);
});
spec_tools.parse.types = (function spec_tools$parse$types(){
return new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 21, [new cljs.core.Keyword(null,"date","date",-1463434462),null,new cljs.core.Keyword(null,"long","long",-171452093),null,new cljs.core.Keyword(null,"double","double",884886883),null,new cljs.core.Keyword(null,"symbol","symbol",-1038572696),null,new cljs.core.Keyword(null,"or","or",235744169),null,new cljs.core.Keyword(null,"multi-spec","multi-spec",1274719724),null,new cljs.core.Keyword(null,"tuple","tuple",-472667284),null,new cljs.core.Keyword(null,"string","string",-1989541586),null,new cljs.core.Keyword(null,"vector","vector",1902966158),null,new cljs.core.Keyword(null,"spec","spec",347520401),null,new cljs.core.Keyword(null,"map-of","map-of",1189682355),null,new cljs.core.Keyword(null,"keyword","keyword",811389747),null,new cljs.core.Keyword(null,"ratio","ratio",-926560044),null,new cljs.core.Keyword(null,"bigdec","bigdec",1019443956),null,new cljs.core.Keyword(null,"and","and",-971899817),null,new cljs.core.Keyword(null,"uri","uri",-774711847),null,new cljs.core.Keyword(null,"uuid","uuid",-2145095719),null,new cljs.core.Keyword(null,"set","set",304602554),null,new cljs.core.Keyword(null,"boolean","boolean",-1919418404),null,new cljs.core.Keyword(null,"map","map",1371690461),null,new cljs.core.Keyword(null,"nilable","nilable",1842307102),null], null), null);
});
spec_tools.parse.type_symbols = (function spec_tools$parse$type_symbols(){
return cljs.core.set(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.symbol_QMARK_,cljs.core.keys(cljs.core.methods$(spec_tools.parse.parse_form))));
});
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","any?","clojure.core/any?",-1093069272,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.any_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","some?","clojure.core/some?",-543337038,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.some_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","number?","clojure.core/number?",-1044499897,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.number_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"double","double",884886883)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","integer?","clojure.core/integer?",-1617881728,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.integer_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"long","long",-171452093)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","int?","clojure.core/int?",1026034806,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.int_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"long","long",-171452093)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","pos-int?","clojure.core/pos-int?",-1946393424,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.pos_int_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"long","long",-171452093)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","neg-int?","clojure.core/neg-int?",-830554117,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.neg_int_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"long","long",-171452093)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","nat-int?","clojure.core/nat-int?",-65390525,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.nat_int_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"long","long",-171452093)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","float?","clojure.core/float?",-99660463,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.float_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"double","double",884886883)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","double?","clojure.core/double?",1847770331,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.double_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"double","double",884886883)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","boolean?","clojure.core/boolean?",1566259823,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.boolean_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"boolean","boolean",-1919418404)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","string?","clojure.core/string?",-1902673477,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.string_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"string","string",-1989541586)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","ident?","clojure.core/ident?",1397717549,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.ident_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"keyword","keyword",811389747)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","simple-ident?","clojure.core/simple-ident?",1706467712,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.simple_ident_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"keyword","keyword",811389747)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","qualified-ident?","clojure.core/qualified-ident?",-1630579588,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.qualified_ident_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"keyword","keyword",811389747)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","keyword?","clojure.core/keyword?",543424180,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.keyword_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"keyword","keyword",811389747)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","simple-keyword?","clojure.core/simple-keyword?",406342760,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.simple_keyword_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"keyword","keyword",811389747)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","qualified-keyword?","clojure.core/qualified-keyword?",-398139912,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.qualified_keyword_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"keyword","keyword",811389747)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","symbol?","clojure.core/symbol?",1587987784,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.symbol_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"symbol","symbol",-1038572696)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","simple-symbol?","clojure.core/simple-symbol?",-1919094963,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.simple_symbol_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"symbol","symbol",-1038572696)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","qualified-symbol?","clojure.core/qualified-symbol?",1469032566,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.qualified_symbol_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"symbol","symbol",-1038572696)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","uuid?","clojure.core/uuid?",-100722718,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.uuid_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"uuid","uuid",-2145095719)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","inst?","clojure.core/inst?",-1302678916,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.inst_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"date","date",-1463434462)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","seqable?","clojure.core/seqable?",-696461980,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.seqable_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","indexed?","clojure.core/indexed?",-1145703303,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.indexed_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","map?","clojure.core/map?",-1425864013,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.map_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","vector?","clojure.core/vector?",-1380385430,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.vector_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","list?","clojure.core/list?",-775099136,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.list_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","seq?","clojure.core/seq?",-1182659926,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.seq_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","char?","clojure.core/char?",372498287,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.char_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","set?","clojure.core/set?",-1275117977,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.set_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","nil?","clojure.core/nil?",842444475,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.nil_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","false?","clojure.core/false?",-1754275840,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.false_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"boolean","boolean",-1919418404)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","true?","clojure.core/true?",-21483202,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.true_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"boolean","boolean",-1919418404)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","zero?","clojure.core/zero?",-313584680,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.zero_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"long","long",-171452093)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","coll?","clojure.core/coll?",1311547908,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.coll_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","empty?","clojure.core/empty?",1788889970,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.empty_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","associative?","clojure.core/associative?",634514106,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.associative_QMARK_,new cljs.core.Keyword(null,"type","type",1174270348),null], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.core","sequential?","clojure.core/sequential?",1943138316,null),(function (_,___$1,___$2){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.sequential_QMARK_], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("clojure.spec.alpha","unknown","clojure.spec.alpha/unknown",-246507596),(function (_,___$1,___$2){
return null;
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","keys","clojure.spec.alpha/keys",-90227326,null),(function (_,form,___$1){
var map__24834 = spec_tools.impl.parse_keys(form);
var map__24834__$1 = cljs.core.__destructure_map(map__24834);
var req = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24834__$1,new cljs.core.Keyword(null,"req","req",-326448303));
var opt = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24834__$1,new cljs.core.Keyword(null,"opt","opt",-794706369));
var req_un = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24834__$1,new cljs.core.Keyword(null,"req-un","req-un",1074571008));
var opt_un = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24834__$1,new cljs.core.Keyword(null,"opt-un","opt-un",883442496));
var key__GT_spec = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24834__$1,new cljs.core.Keyword(null,"key->spec","key->spec",1088543019));
var G__24841 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"map","map",1371690461),new cljs.core.Keyword("spec-tools.parse","key->spec","spec-tools.parse/key->spec",1347735257),key__GT_spec,new cljs.core.Keyword("spec-tools.parse","keys","spec-tools.parse/keys",1331815460),cljs.core.set(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(req,opt,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([req_un,opt_un], 0)))], null);
var G__24841__$1 = (cljs.core.truth_((function (){var or__5142__auto__ = req;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return req_un;
}
})())?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__24841,new cljs.core.Keyword("spec-tools.parse","keys-req","spec-tools.parse/keys-req",-1559250753),cljs.core.set(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(req,req_un))):G__24841);
if(cljs.core.truth_((function (){var or__5142__auto__ = opt;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return opt_un;
}
})())){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__24841__$1,new cljs.core.Keyword("spec-tools.parse","keys-opt","spec-tools.parse/keys-opt",-1090203455),cljs.core.set(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(opt,opt_un)));
} else {
return G__24841__$1;
}
}));
/**
 * Given a multi-spec form, call its multi method methods to retrieve
 * its subspecs.
 */
spec_tools.parse.get_multi_spec_sub_specs = (function spec_tools$parse$get_multi_spec_sub_specs(multi_spec_form){
var vec__24854 = multi_spec_form;
var seq__24855 = cljs.core.seq(vec__24854);
var first__24856 = cljs.core.first(seq__24855);
var seq__24855__$1 = cljs.core.next(seq__24855);
var _ = first__24856;
var first__24856__$1 = cljs.core.first(seq__24855__$1);
var seq__24855__$2 = cljs.core.next(seq__24855__$1);
var multi_method_symbol = first__24856__$1;
var ___$1 = seq__24855__$2;
var form = vec__24854;
var temp__5825__auto__ = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (v){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(form,cljs.spec.alpha.form(v));
}),cljs.core.vals(cljs.spec.alpha.registry())));
if(cljs.core.truth_(temp__5825__auto__)){
var spec = temp__5825__auto__;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__24862){
var vec__24863 = p__24862;
var spec_k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__24863,(0),null);
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__24863,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec_k,(method.cljs$core$IFn$_invoke$arity$1 ? method.cljs$core$IFn$_invoke$arity$1(null) : method.call(null,null))], null);
}),cljs.core.methods$(cljs.core.deref(spec.mmvar)));
} else {
return null;
}
});
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","multi-spec","clojure.spec.alpha/multi-spec",1408976740,null),(function (_,form,___$1){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"multi-spec","multi-spec",1274719724),new cljs.core.Keyword("spec-tools.parse","key","spec-tools.parse/key",-753897253),cljs.core.last(form),new cljs.core.Keyword("spec-tools.parse","dispatch","spec-tools.parse/dispatch",1593226563),cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,spec_tools.parse.get_multi_spec_sub_specs(form))], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","or","clojure.spec.alpha/or",434904251,null),(function (_,form,options){
var specs = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2((function (p1__24867_SHARP_){
return spec_tools.parse.parse_spec_with_spec_ref(p1__24867_SHARP_,options);
}),cljs.core.second),cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),cljs.core.rest(form)));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"or","or",235744169),cljs.core.vec(cljs.core.keep.cljs$core$IFn$_invoke$arity$2(cljs.core.identity,cljs.core.distinct.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348),specs))))], null),new cljs.core.Keyword("spec-tools.parse","items","spec-tools.parse/items",1255627600),specs], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","and","clojure.spec.alpha/and",-843882543,null),(function (_,form,options){
var specs = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__24874_SHARP_){
return spec_tools.parse.parse_spec_with_spec_ref(p1__24874_SHARP_,options);
}),cljs.core.rest(form));
var types = cljs.core.vec(cljs.core.keep.cljs$core$IFn$_invoke$arity$2(cljs.core.identity,cljs.core.distinct.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348),specs))));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"and","and",-971899817),types], null),new cljs.core.Keyword("spec-tools.parse","items","spec-tools.parse/items",1255627600),specs], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","merge","clojure.spec.alpha/merge",472136035,null),(function (_,form,options){
var type_priority = (function (p1__24881_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(p1__24881_SHARP_),new cljs.core.Keyword(null,"multi-spec","multi-spec",1274719724))){
return (1);
} else {
return (0);
}
});
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(spec_tools.impl.deep_merge,cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(type_priority,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__24883_SHARP_){
return spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2(p1__24883_SHARP_,options);
}),cljs.core.rest(form))));
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","every","clojure.spec.alpha/every",-1327408778,null),(function (_,form,options){
var map__24889 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,cljs.core.drop.cljs$core$IFn$_invoke$arity$2((2),form));
var map__24889__$1 = cljs.core.__destructure_map(map__24889);
var into = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24889__$1,new cljs.core.Keyword(null,"into","into",-150836029));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("spec-tools.parse","item","spec-tools.parse/item",-565704128),spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2(cljs.core.second(form),options),new cljs.core.Keyword(null,"type","type",1174270348),((cljs.core.map_QMARK_(into))?new cljs.core.Keyword(null,"map-of","map-of",1189682355):((cljs.core.set_QMARK_(into))?new cljs.core.Keyword(null,"set","set",304602554):new cljs.core.Keyword(null,"vector","vector",1902966158)
))], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","coll-of","clojure.spec.alpha/coll-of",-465249451,null),(function (_,form,options){
var map__24897 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,cljs.core.drop.cljs$core$IFn$_invoke$arity$2((2),form));
var map__24897__$1 = cljs.core.__destructure_map(map__24897);
var into = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__24897__$1,new cljs.core.Keyword(null,"into","into",-150836029));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("spec-tools.parse","item","spec-tools.parse/item",-565704128),spec_tools.parse.parse_spec_with_spec_ref(cljs.core.second(form),options),new cljs.core.Keyword(null,"type","type",1174270348),((cljs.core.map_QMARK_(into))?new cljs.core.Keyword(null,"map-of","map-of",1189682355):((cljs.core.set_QMARK_(into))?new cljs.core.Keyword(null,"set","set",304602554):new cljs.core.Keyword(null,"vector","vector",1902966158)
))], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","map-of","clojure.spec.alpha/map-of",2125010727,null),(function (_,p__24899,options){
var vec__24901 = p__24899;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__24901,(0),null);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__24901,(1),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__24901,(2),null);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"map-of","map-of",1189682355),new cljs.core.Keyword("spec-tools.parse","key","spec-tools.parse/key",-753897253),spec_tools.parse.parse_spec_with_spec_ref(k,options),new cljs.core.Keyword("spec-tools.parse","value","spec-tools.parse/value",-492706501),spec_tools.parse.parse_spec_with_spec_ref(v,options)], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("spec-tools.core","spec","spec-tools.core/spec",-497332036,null),(function (_,form,options){
var parsed = spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(cljs.core.last(form)),options);
if(cljs.core.truth_(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(parsed))){
return parsed;
} else {
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"spec","spec",347520401)], null);
}
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","tuple","clojure.spec.alpha/tuple",800350846,null),(function (_,p__24912,options){
var vec__24914 = p__24912;
var seq__24915 = cljs.core.seq(vec__24914);
var first__24916 = cljs.core.first(seq__24915);
var seq__24915__$1 = cljs.core.next(seq__24915);
var ___$1 = first__24916;
var values = seq__24915__$1;
var specs = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__24908_SHARP_){
return spec_tools.parse.parse_spec_with_spec_ref(p1__24908_SHARP_,options);
}),values);
var types = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348),specs);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"tuple","tuple",-472667284),types], null),new cljs.core.Keyword("spec-tools.parse","items","spec-tools.parse/items",1255627600),specs], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("clojure.spec.alpha","nilable","clojure.spec.alpha/nilable",-1718644550,null),(function (_,form,options){
var spec = spec_tools.parse.parse_spec_with_spec_ref(cljs.core.second(form),options);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"nilable","nilable",1842307102),new cljs.core.Keyword("spec-tools.parse","item","spec-tools.parse/item",-565704128),spec], null);
}));
spec_tools.parse.parse_form.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Symbol("spec-tools.core","merge","spec-tools.core/merge",2048449266,null),(function (_,form,options){
var type_priority = (function (p1__24921_SHARP_){
var G__24928 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"map","map",1371690461),(1),new cljs.core.Keyword(null,"multi-spec","multi-spec",1274719724),(0)], null);
var fexpr__24927 = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(p1__24921_SHARP_);
return (fexpr__24927.cljs$core$IFn$_invoke$arity$1 ? fexpr__24927.cljs$core$IFn$_invoke$arity$1(G__24928) : fexpr__24927.call(null,G__24928));
});
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(spec_tools.impl.deep_merge,cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(type_priority,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__24923_SHARP_){
return spec_tools.parse.parse_spec.cljs$core$IFn$_invoke$arity$2(p1__24923_SHARP_,options);
}),cljs.core.rest(form))));
}));

//# sourceMappingURL=spec_tools.parse.js.map
