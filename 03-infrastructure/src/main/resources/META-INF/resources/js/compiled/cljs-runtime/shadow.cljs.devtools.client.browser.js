goog.provide('shadow.cljs.devtools.client.browser');
shadow.cljs.devtools.client.browser.devtools_msg = (function shadow$cljs$devtools$client$browser$devtools_msg(var_args){
var args__5882__auto__ = [];
var len__5876__auto___52277 = arguments.length;
var i__5877__auto___52278 = (0);
while(true){
if((i__5877__auto___52278 < len__5876__auto___52277)){
args__5882__auto__.push((arguments[i__5877__auto___52278]));

var G__52279 = (i__5877__auto___52278 + (1));
i__5877__auto___52278 = G__52279;
continue;
} else {
}
break;
}

var argseq__5883__auto__ = ((((1) < args__5882__auto__.length))?(new cljs.core.IndexedSeq(args__5882__auto__.slice((1)),(0),null)):null);
return shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5883__auto__);
});

(shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic = (function (msg,args){
if(shadow.cljs.devtools.client.env.log){
if(cljs.core.seq(shadow.cljs.devtools.client.env.log_style)){
return console.log.apply(console,cljs.core.into_array.cljs$core$IFn$_invoke$arity$1(cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(""+"%cshadow-cljs: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg)),shadow.cljs.devtools.client.env.log_style], null),args)));
} else {
return console.log.apply(console,cljs.core.into_array.cljs$core$IFn$_invoke$arity$1(cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [(""+"shadow-cljs: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg))], null),args)));
}
} else {
return null;
}
}));

(shadow.cljs.devtools.client.browser.devtools_msg.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(shadow.cljs.devtools.client.browser.devtools_msg.cljs$lang$applyTo = (function (seq51779){
var G__51780 = cljs.core.first(seq51779);
var seq51779__$1 = cljs.core.next(seq51779);
var self__5861__auto__ = this;
return self__5861__auto__.cljs$core$IFn$_invoke$arity$variadic(G__51780,seq51779__$1);
}));

shadow.cljs.devtools.client.browser.script_eval = (function shadow$cljs$devtools$client$browser$script_eval(code){
return goog.globalEval(code);
});
shadow.cljs.devtools.client.browser.do_js_load = (function shadow$cljs$devtools$client$browser$do_js_load(sources){
var seq__51796 = cljs.core.seq(sources);
var chunk__51797 = null;
var count__51798 = (0);
var i__51799 = (0);
while(true){
if((i__51799 < count__51798)){
var map__51809 = chunk__51797.cljs$core$IIndexed$_nth$arity$2(null,i__51799);
var map__51809__$1 = cljs.core.__destructure_map(map__51809);
var src = map__51809__$1;
var resource_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51809__$1,new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582));
var output_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51809__$1,new cljs.core.Keyword(null,"output-name","output-name",-1769107767));
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51809__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51809__$1,new cljs.core.Keyword(null,"js","js",1768080579));
$CLJS.SHADOW_ENV.setLoaded(output_name);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load JS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([resource_name], 0));

shadow.cljs.devtools.client.env.before_load_src(src);

try{shadow.cljs.devtools.client.browser.script_eval((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js)+"\n//# sourceURL="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($CLJS.SHADOW_ENV.scriptBase)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(output_name)));
}catch (e51810){var e_52285 = e51810;
if(shadow.cljs.devtools.client.env.log){
console.error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)),e_52285);
} else {
}

throw (new Error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)+": "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(e_52285.message))));
}

var G__52286 = seq__51796;
var G__52287 = chunk__51797;
var G__52288 = count__51798;
var G__52289 = (i__51799 + (1));
seq__51796 = G__52286;
chunk__51797 = G__52287;
count__51798 = G__52288;
i__51799 = G__52289;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__51796);
if(temp__5825__auto__){
var seq__51796__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__51796__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__51796__$1);
var G__52290 = cljs.core.chunk_rest(seq__51796__$1);
var G__52291 = c__5673__auto__;
var G__52292 = cljs.core.count(c__5673__auto__);
var G__52293 = (0);
seq__51796 = G__52290;
chunk__51797 = G__52291;
count__51798 = G__52292;
i__51799 = G__52293;
continue;
} else {
var map__51811 = cljs.core.first(seq__51796__$1);
var map__51811__$1 = cljs.core.__destructure_map(map__51811);
var src = map__51811__$1;
var resource_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51811__$1,new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582));
var output_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51811__$1,new cljs.core.Keyword(null,"output-name","output-name",-1769107767));
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51811__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51811__$1,new cljs.core.Keyword(null,"js","js",1768080579));
$CLJS.SHADOW_ENV.setLoaded(output_name);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load JS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([resource_name], 0));

shadow.cljs.devtools.client.env.before_load_src(src);

try{shadow.cljs.devtools.client.browser.script_eval((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js)+"\n//# sourceURL="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($CLJS.SHADOW_ENV.scriptBase)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(output_name)));
}catch (e51812){var e_52294 = e51812;
if(shadow.cljs.devtools.client.env.log){
console.error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)),e_52294);
} else {
}

throw (new Error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)+": "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(e_52294.message))));
}

var G__52295 = cljs.core.next(seq__51796__$1);
var G__52296 = null;
var G__52297 = (0);
var G__52298 = (0);
seq__51796 = G__52295;
chunk__51797 = G__52296;
count__51798 = G__52297;
i__51799 = G__52298;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.do_js_reload = (function shadow$cljs$devtools$client$browser$do_js_reload(msg,sources,complete_fn,failure_fn){
return shadow.cljs.devtools.client.env.do_js_reload.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(msg,new cljs.core.Keyword(null,"log-missing-fn","log-missing-fn",732676765),(function (fn_sym){
return null;
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"log-call-async","log-call-async",183826192),(function (fn_sym){
return shadow.cljs.devtools.client.browser.devtools_msg((""+"call async "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym)));
}),new cljs.core.Keyword(null,"log-call","log-call",412404391),(function (fn_sym){
return shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym)));
})], 0)),(function (next){
shadow.cljs.devtools.client.browser.do_js_load(sources);

return (next.cljs$core$IFn$_invoke$arity$0 ? next.cljs$core$IFn$_invoke$arity$0() : next.call(null));
}),complete_fn,failure_fn);
});
/**
 * when (require '["some-str" :as x]) is done at the REPL we need to manually call the shadow.js.require for it
 * since the file only adds the shadow$provide. only need to do this for shadow-js.
 */
shadow.cljs.devtools.client.browser.do_js_requires = (function shadow$cljs$devtools$client$browser$do_js_requires(js_requires){
var seq__51818 = cljs.core.seq(js_requires);
var chunk__51821 = null;
var count__51823 = (0);
var i__51824 = (0);
while(true){
if((i__51824 < count__51823)){
var js_ns = chunk__51821.cljs$core$IIndexed$_nth$arity$2(null,i__51824);
var require_str_52299 = (""+"var "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+" = shadow.js.require(\""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+"\");");
shadow.cljs.devtools.client.browser.script_eval(require_str_52299);


var G__52300 = seq__51818;
var G__52301 = chunk__51821;
var G__52302 = count__51823;
var G__52303 = (i__51824 + (1));
seq__51818 = G__52300;
chunk__51821 = G__52301;
count__51823 = G__52302;
i__51824 = G__52303;
continue;
} else {
var temp__5825__auto__ = cljs.core.seq(seq__51818);
if(temp__5825__auto__){
var seq__51818__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__51818__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__51818__$1);
var G__52304 = cljs.core.chunk_rest(seq__51818__$1);
var G__52305 = c__5673__auto__;
var G__52306 = cljs.core.count(c__5673__auto__);
var G__52307 = (0);
seq__51818 = G__52304;
chunk__51821 = G__52305;
count__51823 = G__52306;
i__51824 = G__52307;
continue;
} else {
var js_ns = cljs.core.first(seq__51818__$1);
var require_str_52308 = (""+"var "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+" = shadow.js.require(\""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+"\");");
shadow.cljs.devtools.client.browser.script_eval(require_str_52308);


var G__52309 = cljs.core.next(seq__51818__$1);
var G__52310 = null;
var G__52311 = (0);
var G__52312 = (0);
seq__51818 = G__52309;
chunk__51821 = G__52310;
count__51823 = G__52311;
i__51824 = G__52312;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.handle_build_complete = (function shadow$cljs$devtools$client$browser$handle_build_complete(runtime,p__51836){
var map__51837 = p__51836;
var map__51837__$1 = cljs.core.__destructure_map(map__51837);
var msg = map__51837__$1;
var info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51837__$1,new cljs.core.Keyword(null,"info","info",-317069002));
var reload_info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51837__$1,new cljs.core.Keyword(null,"reload-info","reload-info",1648088086));
var warnings = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.distinct.cljs$core$IFn$_invoke$arity$1((function (){var iter__5628__auto__ = (function shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__51838(s__51839){
return (new cljs.core.LazySeq(null,(function (){
var s__51839__$1 = s__51839;
while(true){
var temp__5825__auto__ = cljs.core.seq(s__51839__$1);
if(temp__5825__auto__){
var xs__6385__auto__ = temp__5825__auto__;
var map__51845 = cljs.core.first(xs__6385__auto__);
var map__51845__$1 = cljs.core.__destructure_map(map__51845);
var src = map__51845__$1;
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51845__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var warnings = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51845__$1,new cljs.core.Keyword(null,"warnings","warnings",-735437651));
if(cljs.core.not(new cljs.core.Keyword(null,"from-jar","from-jar",1050932827).cljs$core$IFn$_invoke$arity$1(src))){
var iterys__5624__auto__ = ((function (s__51839__$1,map__51845,map__51845__$1,src,resource_name,warnings,xs__6385__auto__,temp__5825__auto__,map__51837,map__51837__$1,msg,info,reload_info){
return (function shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__51838_$_iter__51840(s__51841){
return (new cljs.core.LazySeq(null,((function (s__51839__$1,map__51845,map__51845__$1,src,resource_name,warnings,xs__6385__auto__,temp__5825__auto__,map__51837,map__51837__$1,msg,info,reload_info){
return (function (){
var s__51841__$1 = s__51841;
while(true){
var temp__5825__auto____$1 = cljs.core.seq(s__51841__$1);
if(temp__5825__auto____$1){
var s__51841__$2 = temp__5825__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__51841__$2)){
var c__5626__auto__ = cljs.core.chunk_first(s__51841__$2);
var size__5627__auto__ = cljs.core.count(c__5626__auto__);
var b__51843 = cljs.core.chunk_buffer(size__5627__auto__);
if((function (){var i__51842 = (0);
while(true){
if((i__51842 < size__5627__auto__)){
var warning = cljs.core._nth(c__5626__auto__,i__51842);
cljs.core.chunk_append(b__51843,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(warning,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100),resource_name));

var G__52313 = (i__51842 + (1));
i__51842 = G__52313;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__51843),shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__51838_$_iter__51840(cljs.core.chunk_rest(s__51841__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__51843),null);
}
} else {
var warning = cljs.core.first(s__51841__$2);
return cljs.core.cons(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(warning,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100),resource_name),shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__51838_$_iter__51840(cljs.core.rest(s__51841__$2)));
}
} else {
return null;
}
break;
}
});})(s__51839__$1,map__51845,map__51845__$1,src,resource_name,warnings,xs__6385__auto__,temp__5825__auto__,map__51837,map__51837__$1,msg,info,reload_info))
,null,null));
});})(s__51839__$1,map__51845,map__51845__$1,src,resource_name,warnings,xs__6385__auto__,temp__5825__auto__,map__51837,map__51837__$1,msg,info,reload_info))
;
var fs__5625__auto__ = cljs.core.seq(iterys__5624__auto__(warnings));
if(fs__5625__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5625__auto__,shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__51838(cljs.core.rest(s__51839__$1)));
} else {
var G__52314 = cljs.core.rest(s__51839__$1);
s__51839__$1 = G__52314;
continue;
}
} else {
var G__52315 = cljs.core.rest(s__51839__$1);
s__51839__$1 = G__52315;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5628__auto__(new cljs.core.Keyword(null,"sources","sources",-321166424).cljs$core$IFn$_invoke$arity$1(info));
})()));
if(shadow.cljs.devtools.client.env.log){
var seq__51851_52316 = cljs.core.seq(warnings);
var chunk__51852_52317 = null;
var count__51853_52318 = (0);
var i__51854_52319 = (0);
while(true){
if((i__51854_52319 < count__51853_52318)){
var map__51860_52320 = chunk__51852_52317.cljs$core$IIndexed$_nth$arity$2(null,i__51854_52319);
var map__51860_52321__$1 = cljs.core.__destructure_map(map__51860_52320);
var w_52322 = map__51860_52321__$1;
var msg_52323__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51860_52321__$1,new cljs.core.Keyword(null,"msg","msg",-1386103444));
var line_52324 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51860_52321__$1,new cljs.core.Keyword(null,"line","line",212345235));
var column_52325 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51860_52321__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var resource_name_52326 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51860_52321__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
console.warn((""+"BUILD-WARNING in "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name_52326)+" at ["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_52324)+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(column_52325)+"]\n\t"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg_52323__$1)));


var G__52327 = seq__51851_52316;
var G__52328 = chunk__51852_52317;
var G__52329 = count__51853_52318;
var G__52330 = (i__51854_52319 + (1));
seq__51851_52316 = G__52327;
chunk__51852_52317 = G__52328;
count__51853_52318 = G__52329;
i__51854_52319 = G__52330;
continue;
} else {
var temp__5825__auto___52331 = cljs.core.seq(seq__51851_52316);
if(temp__5825__auto___52331){
var seq__51851_52332__$1 = temp__5825__auto___52331;
if(cljs.core.chunked_seq_QMARK_(seq__51851_52332__$1)){
var c__5673__auto___52333 = cljs.core.chunk_first(seq__51851_52332__$1);
var G__52334 = cljs.core.chunk_rest(seq__51851_52332__$1);
var G__52335 = c__5673__auto___52333;
var G__52336 = cljs.core.count(c__5673__auto___52333);
var G__52337 = (0);
seq__51851_52316 = G__52334;
chunk__51852_52317 = G__52335;
count__51853_52318 = G__52336;
i__51854_52319 = G__52337;
continue;
} else {
var map__51866_52338 = cljs.core.first(seq__51851_52332__$1);
var map__51866_52339__$1 = cljs.core.__destructure_map(map__51866_52338);
var w_52340 = map__51866_52339__$1;
var msg_52341__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51866_52339__$1,new cljs.core.Keyword(null,"msg","msg",-1386103444));
var line_52342 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51866_52339__$1,new cljs.core.Keyword(null,"line","line",212345235));
var column_52343 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51866_52339__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var resource_name_52344 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51866_52339__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
console.warn((""+"BUILD-WARNING in "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name_52344)+" at ["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_52342)+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(column_52343)+"]\n\t"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg_52341__$1)));


var G__52345 = cljs.core.next(seq__51851_52332__$1);
var G__52346 = null;
var G__52347 = (0);
var G__52348 = (0);
seq__51851_52316 = G__52345;
chunk__51852_52317 = G__52346;
count__51853_52318 = G__52347;
i__51854_52319 = G__52348;
continue;
}
} else {
}
}
break;
}
} else {
}

if((!(shadow.cljs.devtools.client.env.autoload))){
return shadow.cljs.devtools.client.hud.load_end_success();
} else {
if(((cljs.core.empty_QMARK_(warnings)) || (shadow.cljs.devtools.client.env.ignore_warnings))){
var sources_to_get = shadow.cljs.devtools.client.env.filter_reload_sources(info,reload_info);
if(cljs.core.not(cljs.core.seq(sources_to_get))){
return shadow.cljs.devtools.client.hud.load_end_success();
} else {
if(cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"after-load","after-load",-1278503285)], null)))){
} else {
shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("reloading code but no :after-load hooks are configured!",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["https://shadow-cljs.github.io/docs/UsersGuide.html#_lifecycle_hooks"], 0));
}

return shadow.cljs.devtools.client.shared.load_sources(runtime,sources_to_get,(function (p1__51829_SHARP_){
return shadow.cljs.devtools.client.browser.do_js_reload(msg,p1__51829_SHARP_,shadow.cljs.devtools.client.hud.load_end_success,shadow.cljs.devtools.client.hud.load_failure);
}));
}
} else {
return null;
}
}
});
shadow.cljs.devtools.client.browser.page_load_uri = (cljs.core.truth_(goog.global.document)?goog.Uri.parse(document.location.href):null);
shadow.cljs.devtools.client.browser.match_paths = (function shadow$cljs$devtools$client$browser$match_paths(old,new$){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("file",shadow.cljs.devtools.client.browser.page_load_uri.getScheme())){
var rel_new = cljs.core.subs.cljs$core$IFn$_invoke$arity$2(new$,(1));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(old,rel_new)) || (clojure.string.starts_with_QMARK_(old,(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(rel_new)+"?"))))){
return rel_new;
} else {
return null;
}
} else {
var node_uri = goog.Uri.parse(old);
var node_uri_resolved = shadow.cljs.devtools.client.browser.page_load_uri.resolve(node_uri);
var node_abs = node_uri_resolved.getPath();
var and__5140__auto__ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$1(shadow.cljs.devtools.client.browser.page_load_uri.hasSameDomainAs(node_uri))) || (cljs.core.not(node_uri.hasDomain())));
if(and__5140__auto__){
var and__5140__auto____$1 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(node_abs,new$);
if(and__5140__auto____$1){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var G__51874 = node_uri;
G__51874.setQuery(null);

G__51874.setPath(new$);

return G__51874;
})()));
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
}
});
shadow.cljs.devtools.client.browser.handle_asset_update = (function shadow$cljs$devtools$client$browser$handle_asset_update(p__51875){
var map__51876 = p__51875;
var map__51876__$1 = cljs.core.__destructure_map(map__51876);
var msg = map__51876__$1;
var updates = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51876__$1,new cljs.core.Keyword(null,"updates","updates",2013983452));
var reload_info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51876__$1,new cljs.core.Keyword(null,"reload-info","reload-info",1648088086));
var seq__51877 = cljs.core.seq(updates);
var chunk__51879 = null;
var count__51880 = (0);
var i__51881 = (0);
while(true){
if((i__51881 < count__51880)){
var path = chunk__51879.cljs$core$IIndexed$_nth$arity$2(null,i__51881);
if(clojure.string.ends_with_QMARK_(path,"css")){
var seq__52022_52349 = cljs.core.seq(cljs.core.array_seq.cljs$core$IFn$_invoke$arity$1(document.querySelectorAll("link[rel=\"stylesheet\"]")));
var chunk__52026_52350 = null;
var count__52027_52351 = (0);
var i__52028_52352 = (0);
while(true){
if((i__52028_52352 < count__52027_52351)){
var node_52353 = chunk__52026_52350.cljs$core$IIndexed$_nth$arity$2(null,i__52028_52352);
if(cljs.core.not(node_52353.shadow$old)){
var path_match_52354 = shadow.cljs.devtools.client.browser.match_paths(node_52353.getAttribute("href"),path);
if(cljs.core.truth_(path_match_52354)){
var new_link_52355 = (function (){var G__52077 = node_52353.cloneNode(true);
G__52077.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_52354)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__52077;
})();
(node_52353.shadow$old = true);

(new_link_52355.onload = ((function (seq__52022_52349,chunk__52026_52350,count__52027_52351,i__52028_52352,seq__51877,chunk__51879,count__51880,i__51881,new_link_52355,path_match_52354,node_52353,path,map__51876,map__51876__$1,msg,updates,reload_info){
return (function (e){
var seq__52079_52356 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__52081_52357 = null;
var count__52082_52358 = (0);
var i__52083_52359 = (0);
while(true){
if((i__52083_52359 < count__52082_52358)){
var map__52088_52360 = chunk__52081_52357.cljs$core$IIndexed$_nth$arity$2(null,i__52083_52359);
var map__52088_52361__$1 = cljs.core.__destructure_map(map__52088_52360);
var task_52362 = map__52088_52361__$1;
var fn_str_52363 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52088_52361__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52364 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52088_52361__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52365 = goog.getObjectByName(fn_str_52363,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52364)));

(fn_obj_52365.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52365.cljs$core$IFn$_invoke$arity$2(path,new_link_52355) : fn_obj_52365.call(null,path,new_link_52355));


var G__52366 = seq__52079_52356;
var G__52367 = chunk__52081_52357;
var G__52368 = count__52082_52358;
var G__52369 = (i__52083_52359 + (1));
seq__52079_52356 = G__52366;
chunk__52081_52357 = G__52367;
count__52082_52358 = G__52368;
i__52083_52359 = G__52369;
continue;
} else {
var temp__5825__auto___52370 = cljs.core.seq(seq__52079_52356);
if(temp__5825__auto___52370){
var seq__52079_52371__$1 = temp__5825__auto___52370;
if(cljs.core.chunked_seq_QMARK_(seq__52079_52371__$1)){
var c__5673__auto___52372 = cljs.core.chunk_first(seq__52079_52371__$1);
var G__52373 = cljs.core.chunk_rest(seq__52079_52371__$1);
var G__52374 = c__5673__auto___52372;
var G__52375 = cljs.core.count(c__5673__auto___52372);
var G__52376 = (0);
seq__52079_52356 = G__52373;
chunk__52081_52357 = G__52374;
count__52082_52358 = G__52375;
i__52083_52359 = G__52376;
continue;
} else {
var map__52090_52377 = cljs.core.first(seq__52079_52371__$1);
var map__52090_52378__$1 = cljs.core.__destructure_map(map__52090_52377);
var task_52379 = map__52090_52378__$1;
var fn_str_52380 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52090_52378__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52381 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52090_52378__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52382 = goog.getObjectByName(fn_str_52380,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52381)));

(fn_obj_52382.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52382.cljs$core$IFn$_invoke$arity$2(path,new_link_52355) : fn_obj_52382.call(null,path,new_link_52355));


var G__52383 = cljs.core.next(seq__52079_52371__$1);
var G__52384 = null;
var G__52385 = (0);
var G__52386 = (0);
seq__52079_52356 = G__52383;
chunk__52081_52357 = G__52384;
count__52082_52358 = G__52385;
i__52083_52359 = G__52386;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_52353);
});})(seq__52022_52349,chunk__52026_52350,count__52027_52351,i__52028_52352,seq__51877,chunk__51879,count__51880,i__51881,new_link_52355,path_match_52354,node_52353,path,map__51876,map__51876__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_52354], 0));

goog.dom.insertSiblingAfter(new_link_52355,node_52353);


var G__52387 = seq__52022_52349;
var G__52388 = chunk__52026_52350;
var G__52389 = count__52027_52351;
var G__52390 = (i__52028_52352 + (1));
seq__52022_52349 = G__52387;
chunk__52026_52350 = G__52388;
count__52027_52351 = G__52389;
i__52028_52352 = G__52390;
continue;
} else {
var G__52391 = seq__52022_52349;
var G__52392 = chunk__52026_52350;
var G__52393 = count__52027_52351;
var G__52394 = (i__52028_52352 + (1));
seq__52022_52349 = G__52391;
chunk__52026_52350 = G__52392;
count__52027_52351 = G__52393;
i__52028_52352 = G__52394;
continue;
}
} else {
var G__52395 = seq__52022_52349;
var G__52396 = chunk__52026_52350;
var G__52397 = count__52027_52351;
var G__52398 = (i__52028_52352 + (1));
seq__52022_52349 = G__52395;
chunk__52026_52350 = G__52396;
count__52027_52351 = G__52397;
i__52028_52352 = G__52398;
continue;
}
} else {
var temp__5825__auto___52399 = cljs.core.seq(seq__52022_52349);
if(temp__5825__auto___52399){
var seq__52022_52400__$1 = temp__5825__auto___52399;
if(cljs.core.chunked_seq_QMARK_(seq__52022_52400__$1)){
var c__5673__auto___52401 = cljs.core.chunk_first(seq__52022_52400__$1);
var G__52402 = cljs.core.chunk_rest(seq__52022_52400__$1);
var G__52403 = c__5673__auto___52401;
var G__52404 = cljs.core.count(c__5673__auto___52401);
var G__52405 = (0);
seq__52022_52349 = G__52402;
chunk__52026_52350 = G__52403;
count__52027_52351 = G__52404;
i__52028_52352 = G__52405;
continue;
} else {
var node_52406 = cljs.core.first(seq__52022_52400__$1);
if(cljs.core.not(node_52406.shadow$old)){
var path_match_52407 = shadow.cljs.devtools.client.browser.match_paths(node_52406.getAttribute("href"),path);
if(cljs.core.truth_(path_match_52407)){
var new_link_52408 = (function (){var G__52094 = node_52406.cloneNode(true);
G__52094.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_52407)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__52094;
})();
(node_52406.shadow$old = true);

(new_link_52408.onload = ((function (seq__52022_52349,chunk__52026_52350,count__52027_52351,i__52028_52352,seq__51877,chunk__51879,count__51880,i__51881,new_link_52408,path_match_52407,node_52406,seq__52022_52400__$1,temp__5825__auto___52399,path,map__51876,map__51876__$1,msg,updates,reload_info){
return (function (e){
var seq__52097_52409 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__52099_52410 = null;
var count__52100_52411 = (0);
var i__52101_52412 = (0);
while(true){
if((i__52101_52412 < count__52100_52411)){
var map__52107_52413 = chunk__52099_52410.cljs$core$IIndexed$_nth$arity$2(null,i__52101_52412);
var map__52107_52414__$1 = cljs.core.__destructure_map(map__52107_52413);
var task_52415 = map__52107_52414__$1;
var fn_str_52416 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52107_52414__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52417 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52107_52414__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52418 = goog.getObjectByName(fn_str_52416,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52417)));

(fn_obj_52418.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52418.cljs$core$IFn$_invoke$arity$2(path,new_link_52408) : fn_obj_52418.call(null,path,new_link_52408));


var G__52419 = seq__52097_52409;
var G__52420 = chunk__52099_52410;
var G__52421 = count__52100_52411;
var G__52422 = (i__52101_52412 + (1));
seq__52097_52409 = G__52419;
chunk__52099_52410 = G__52420;
count__52100_52411 = G__52421;
i__52101_52412 = G__52422;
continue;
} else {
var temp__5825__auto___52423__$1 = cljs.core.seq(seq__52097_52409);
if(temp__5825__auto___52423__$1){
var seq__52097_52424__$1 = temp__5825__auto___52423__$1;
if(cljs.core.chunked_seq_QMARK_(seq__52097_52424__$1)){
var c__5673__auto___52425 = cljs.core.chunk_first(seq__52097_52424__$1);
var G__52426 = cljs.core.chunk_rest(seq__52097_52424__$1);
var G__52427 = c__5673__auto___52425;
var G__52428 = cljs.core.count(c__5673__auto___52425);
var G__52429 = (0);
seq__52097_52409 = G__52426;
chunk__52099_52410 = G__52427;
count__52100_52411 = G__52428;
i__52101_52412 = G__52429;
continue;
} else {
var map__52112_52430 = cljs.core.first(seq__52097_52424__$1);
var map__52112_52431__$1 = cljs.core.__destructure_map(map__52112_52430);
var task_52432 = map__52112_52431__$1;
var fn_str_52433 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52112_52431__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52434 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52112_52431__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52435 = goog.getObjectByName(fn_str_52433,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52434)));

(fn_obj_52435.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52435.cljs$core$IFn$_invoke$arity$2(path,new_link_52408) : fn_obj_52435.call(null,path,new_link_52408));


var G__52436 = cljs.core.next(seq__52097_52424__$1);
var G__52437 = null;
var G__52438 = (0);
var G__52439 = (0);
seq__52097_52409 = G__52436;
chunk__52099_52410 = G__52437;
count__52100_52411 = G__52438;
i__52101_52412 = G__52439;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_52406);
});})(seq__52022_52349,chunk__52026_52350,count__52027_52351,i__52028_52352,seq__51877,chunk__51879,count__51880,i__51881,new_link_52408,path_match_52407,node_52406,seq__52022_52400__$1,temp__5825__auto___52399,path,map__51876,map__51876__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_52407], 0));

goog.dom.insertSiblingAfter(new_link_52408,node_52406);


var G__52440 = cljs.core.next(seq__52022_52400__$1);
var G__52441 = null;
var G__52442 = (0);
var G__52443 = (0);
seq__52022_52349 = G__52440;
chunk__52026_52350 = G__52441;
count__52027_52351 = G__52442;
i__52028_52352 = G__52443;
continue;
} else {
var G__52444 = cljs.core.next(seq__52022_52400__$1);
var G__52445 = null;
var G__52446 = (0);
var G__52447 = (0);
seq__52022_52349 = G__52444;
chunk__52026_52350 = G__52445;
count__52027_52351 = G__52446;
i__52028_52352 = G__52447;
continue;
}
} else {
var G__52448 = cljs.core.next(seq__52022_52400__$1);
var G__52449 = null;
var G__52450 = (0);
var G__52451 = (0);
seq__52022_52349 = G__52448;
chunk__52026_52350 = G__52449;
count__52027_52351 = G__52450;
i__52028_52352 = G__52451;
continue;
}
}
} else {
}
}
break;
}


var G__52452 = seq__51877;
var G__52453 = chunk__51879;
var G__52454 = count__51880;
var G__52455 = (i__51881 + (1));
seq__51877 = G__52452;
chunk__51879 = G__52453;
count__51880 = G__52454;
i__51881 = G__52455;
continue;
} else {
var G__52456 = seq__51877;
var G__52457 = chunk__51879;
var G__52458 = count__51880;
var G__52459 = (i__51881 + (1));
seq__51877 = G__52456;
chunk__51879 = G__52457;
count__51880 = G__52458;
i__51881 = G__52459;
continue;
}
} else {
var temp__5825__auto__ = cljs.core.seq(seq__51877);
if(temp__5825__auto__){
var seq__51877__$1 = temp__5825__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__51877__$1)){
var c__5673__auto__ = cljs.core.chunk_first(seq__51877__$1);
var G__52460 = cljs.core.chunk_rest(seq__51877__$1);
var G__52461 = c__5673__auto__;
var G__52462 = cljs.core.count(c__5673__auto__);
var G__52463 = (0);
seq__51877 = G__52460;
chunk__51879 = G__52461;
count__51880 = G__52462;
i__51881 = G__52463;
continue;
} else {
var path = cljs.core.first(seq__51877__$1);
if(clojure.string.ends_with_QMARK_(path,"css")){
var seq__52117_52464 = cljs.core.seq(cljs.core.array_seq.cljs$core$IFn$_invoke$arity$1(document.querySelectorAll("link[rel=\"stylesheet\"]")));
var chunk__52121_52465 = null;
var count__52122_52466 = (0);
var i__52123_52467 = (0);
while(true){
if((i__52123_52467 < count__52122_52466)){
var node_52470 = chunk__52121_52465.cljs$core$IIndexed$_nth$arity$2(null,i__52123_52467);
if(cljs.core.not(node_52470.shadow$old)){
var path_match_52471 = shadow.cljs.devtools.client.browser.match_paths(node_52470.getAttribute("href"),path);
if(cljs.core.truth_(path_match_52471)){
var new_link_52472 = (function (){var G__52173 = node_52470.cloneNode(true);
G__52173.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_52471)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__52173;
})();
(node_52470.shadow$old = true);

(new_link_52472.onload = ((function (seq__52117_52464,chunk__52121_52465,count__52122_52466,i__52123_52467,seq__51877,chunk__51879,count__51880,i__51881,new_link_52472,path_match_52471,node_52470,path,seq__51877__$1,temp__5825__auto__,map__51876,map__51876__$1,msg,updates,reload_info){
return (function (e){
var seq__52174_52475 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__52176_52476 = null;
var count__52177_52477 = (0);
var i__52178_52478 = (0);
while(true){
if((i__52178_52478 < count__52177_52477)){
var map__52189_52479 = chunk__52176_52476.cljs$core$IIndexed$_nth$arity$2(null,i__52178_52478);
var map__52189_52480__$1 = cljs.core.__destructure_map(map__52189_52479);
var task_52481 = map__52189_52480__$1;
var fn_str_52482 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52189_52480__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52483 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52189_52480__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52485 = goog.getObjectByName(fn_str_52482,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52483)));

(fn_obj_52485.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52485.cljs$core$IFn$_invoke$arity$2(path,new_link_52472) : fn_obj_52485.call(null,path,new_link_52472));


var G__52486 = seq__52174_52475;
var G__52487 = chunk__52176_52476;
var G__52488 = count__52177_52477;
var G__52489 = (i__52178_52478 + (1));
seq__52174_52475 = G__52486;
chunk__52176_52476 = G__52487;
count__52177_52477 = G__52488;
i__52178_52478 = G__52489;
continue;
} else {
var temp__5825__auto___52491__$1 = cljs.core.seq(seq__52174_52475);
if(temp__5825__auto___52491__$1){
var seq__52174_52492__$1 = temp__5825__auto___52491__$1;
if(cljs.core.chunked_seq_QMARK_(seq__52174_52492__$1)){
var c__5673__auto___52493 = cljs.core.chunk_first(seq__52174_52492__$1);
var G__52494 = cljs.core.chunk_rest(seq__52174_52492__$1);
var G__52495 = c__5673__auto___52493;
var G__52496 = cljs.core.count(c__5673__auto___52493);
var G__52497 = (0);
seq__52174_52475 = G__52494;
chunk__52176_52476 = G__52495;
count__52177_52477 = G__52496;
i__52178_52478 = G__52497;
continue;
} else {
var map__52193_52498 = cljs.core.first(seq__52174_52492__$1);
var map__52193_52499__$1 = cljs.core.__destructure_map(map__52193_52498);
var task_52500 = map__52193_52499__$1;
var fn_str_52501 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52193_52499__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52502 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52193_52499__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52503 = goog.getObjectByName(fn_str_52501,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52502)));

(fn_obj_52503.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52503.cljs$core$IFn$_invoke$arity$2(path,new_link_52472) : fn_obj_52503.call(null,path,new_link_52472));


var G__52504 = cljs.core.next(seq__52174_52492__$1);
var G__52505 = null;
var G__52506 = (0);
var G__52507 = (0);
seq__52174_52475 = G__52504;
chunk__52176_52476 = G__52505;
count__52177_52477 = G__52506;
i__52178_52478 = G__52507;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_52470);
});})(seq__52117_52464,chunk__52121_52465,count__52122_52466,i__52123_52467,seq__51877,chunk__51879,count__51880,i__51881,new_link_52472,path_match_52471,node_52470,path,seq__51877__$1,temp__5825__auto__,map__51876,map__51876__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_52471], 0));

goog.dom.insertSiblingAfter(new_link_52472,node_52470);


var G__52508 = seq__52117_52464;
var G__52509 = chunk__52121_52465;
var G__52510 = count__52122_52466;
var G__52511 = (i__52123_52467 + (1));
seq__52117_52464 = G__52508;
chunk__52121_52465 = G__52509;
count__52122_52466 = G__52510;
i__52123_52467 = G__52511;
continue;
} else {
var G__52512 = seq__52117_52464;
var G__52513 = chunk__52121_52465;
var G__52514 = count__52122_52466;
var G__52515 = (i__52123_52467 + (1));
seq__52117_52464 = G__52512;
chunk__52121_52465 = G__52513;
count__52122_52466 = G__52514;
i__52123_52467 = G__52515;
continue;
}
} else {
var G__52516 = seq__52117_52464;
var G__52517 = chunk__52121_52465;
var G__52518 = count__52122_52466;
var G__52519 = (i__52123_52467 + (1));
seq__52117_52464 = G__52516;
chunk__52121_52465 = G__52517;
count__52122_52466 = G__52518;
i__52123_52467 = G__52519;
continue;
}
} else {
var temp__5825__auto___52520__$1 = cljs.core.seq(seq__52117_52464);
if(temp__5825__auto___52520__$1){
var seq__52117_52521__$1 = temp__5825__auto___52520__$1;
if(cljs.core.chunked_seq_QMARK_(seq__52117_52521__$1)){
var c__5673__auto___52522 = cljs.core.chunk_first(seq__52117_52521__$1);
var G__52523 = cljs.core.chunk_rest(seq__52117_52521__$1);
var G__52524 = c__5673__auto___52522;
var G__52525 = cljs.core.count(c__5673__auto___52522);
var G__52526 = (0);
seq__52117_52464 = G__52523;
chunk__52121_52465 = G__52524;
count__52122_52466 = G__52525;
i__52123_52467 = G__52526;
continue;
} else {
var node_52527 = cljs.core.first(seq__52117_52521__$1);
if(cljs.core.not(node_52527.shadow$old)){
var path_match_52528 = shadow.cljs.devtools.client.browser.match_paths(node_52527.getAttribute("href"),path);
if(cljs.core.truth_(path_match_52528)){
var new_link_52529 = (function (){var G__52196 = node_52527.cloneNode(true);
G__52196.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_52528)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__52196;
})();
(node_52527.shadow$old = true);

(new_link_52529.onload = ((function (seq__52117_52464,chunk__52121_52465,count__52122_52466,i__52123_52467,seq__51877,chunk__51879,count__51880,i__51881,new_link_52529,path_match_52528,node_52527,seq__52117_52521__$1,temp__5825__auto___52520__$1,path,seq__51877__$1,temp__5825__auto__,map__51876,map__51876__$1,msg,updates,reload_info){
return (function (e){
var seq__52197_52532 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__52199_52533 = null;
var count__52200_52534 = (0);
var i__52201_52535 = (0);
while(true){
if((i__52201_52535 < count__52200_52534)){
var map__52207_52536 = chunk__52199_52533.cljs$core$IIndexed$_nth$arity$2(null,i__52201_52535);
var map__52207_52537__$1 = cljs.core.__destructure_map(map__52207_52536);
var task_52538 = map__52207_52537__$1;
var fn_str_52539 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52207_52537__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52540 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52207_52537__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52543 = goog.getObjectByName(fn_str_52539,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52540)));

(fn_obj_52543.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52543.cljs$core$IFn$_invoke$arity$2(path,new_link_52529) : fn_obj_52543.call(null,path,new_link_52529));


var G__52545 = seq__52197_52532;
var G__52546 = chunk__52199_52533;
var G__52547 = count__52200_52534;
var G__52548 = (i__52201_52535 + (1));
seq__52197_52532 = G__52545;
chunk__52199_52533 = G__52546;
count__52200_52534 = G__52547;
i__52201_52535 = G__52548;
continue;
} else {
var temp__5825__auto___52549__$2 = cljs.core.seq(seq__52197_52532);
if(temp__5825__auto___52549__$2){
var seq__52197_52551__$1 = temp__5825__auto___52549__$2;
if(cljs.core.chunked_seq_QMARK_(seq__52197_52551__$1)){
var c__5673__auto___52552 = cljs.core.chunk_first(seq__52197_52551__$1);
var G__52553 = cljs.core.chunk_rest(seq__52197_52551__$1);
var G__52554 = c__5673__auto___52552;
var G__52555 = cljs.core.count(c__5673__auto___52552);
var G__52556 = (0);
seq__52197_52532 = G__52553;
chunk__52199_52533 = G__52554;
count__52200_52534 = G__52555;
i__52201_52535 = G__52556;
continue;
} else {
var map__52212_52557 = cljs.core.first(seq__52197_52551__$1);
var map__52212_52558__$1 = cljs.core.__destructure_map(map__52212_52557);
var task_52559 = map__52212_52558__$1;
var fn_str_52560 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52212_52558__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_52561 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52212_52558__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_52563 = goog.getObjectByName(fn_str_52560,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_52561)));

(fn_obj_52563.cljs$core$IFn$_invoke$arity$2 ? fn_obj_52563.cljs$core$IFn$_invoke$arity$2(path,new_link_52529) : fn_obj_52563.call(null,path,new_link_52529));


var G__52564 = cljs.core.next(seq__52197_52551__$1);
var G__52565 = null;
var G__52566 = (0);
var G__52567 = (0);
seq__52197_52532 = G__52564;
chunk__52199_52533 = G__52565;
count__52200_52534 = G__52566;
i__52201_52535 = G__52567;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_52527);
});})(seq__52117_52464,chunk__52121_52465,count__52122_52466,i__52123_52467,seq__51877,chunk__51879,count__51880,i__51881,new_link_52529,path_match_52528,node_52527,seq__52117_52521__$1,temp__5825__auto___52520__$1,path,seq__51877__$1,temp__5825__auto__,map__51876,map__51876__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_52528], 0));

goog.dom.insertSiblingAfter(new_link_52529,node_52527);


var G__52568 = cljs.core.next(seq__52117_52521__$1);
var G__52569 = null;
var G__52570 = (0);
var G__52571 = (0);
seq__52117_52464 = G__52568;
chunk__52121_52465 = G__52569;
count__52122_52466 = G__52570;
i__52123_52467 = G__52571;
continue;
} else {
var G__52572 = cljs.core.next(seq__52117_52521__$1);
var G__52573 = null;
var G__52574 = (0);
var G__52575 = (0);
seq__52117_52464 = G__52572;
chunk__52121_52465 = G__52573;
count__52122_52466 = G__52574;
i__52123_52467 = G__52575;
continue;
}
} else {
var G__52576 = cljs.core.next(seq__52117_52521__$1);
var G__52577 = null;
var G__52578 = (0);
var G__52579 = (0);
seq__52117_52464 = G__52576;
chunk__52121_52465 = G__52577;
count__52122_52466 = G__52578;
i__52123_52467 = G__52579;
continue;
}
}
} else {
}
}
break;
}


var G__52580 = cljs.core.next(seq__51877__$1);
var G__52581 = null;
var G__52582 = (0);
var G__52583 = (0);
seq__51877 = G__52580;
chunk__51879 = G__52581;
count__51880 = G__52582;
i__51881 = G__52583;
continue;
} else {
var G__52584 = cljs.core.next(seq__51877__$1);
var G__52585 = null;
var G__52586 = (0);
var G__52587 = (0);
seq__51877 = G__52584;
chunk__51879 = G__52585;
count__51880 = G__52586;
i__51881 = G__52587;
continue;
}
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.global_eval = (function shadow$cljs$devtools$client$browser$global_eval(js){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2("undefined",typeof(module))){
return eval(js);
} else {
return (0,eval)(js);;
}
});
shadow.cljs.devtools.client.browser.runtime_info = (((typeof SHADOW_CONFIG !== 'undefined'))?shadow.json.to_clj.cljs$core$IFn$_invoke$arity$1(SHADOW_CONFIG):null);
shadow.cljs.devtools.client.browser.client_info = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([shadow.cljs.devtools.client.browser.runtime_info,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"host","host",-1558485167),(cljs.core.truth_(goog.global.document)?new cljs.core.Keyword(null,"browser","browser",828191719):new cljs.core.Keyword(null,"browser-worker","browser-worker",1638998282)),new cljs.core.Keyword(null,"user-agent","user-agent",1220426212),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(goog.userAgent.OPERA)?"Opera":(cljs.core.truth_(goog.userAgent.product.CHROME)?"Chrome":(cljs.core.truth_(goog.userAgent.IE)?"MSIE":(cljs.core.truth_(goog.userAgent.EDGE)?"Edge":(cljs.core.truth_(goog.userAgent.GECKO)?"Firefox":(cljs.core.truth_(goog.userAgent.SAFARI)?"Safari":(cljs.core.truth_(goog.userAgent.WEBKIT)?"Webkit":null))))))))+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(goog.userAgent.VERSION)+" ["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(goog.userAgent.PLATFORM)+"]"),new cljs.core.Keyword(null,"dom","dom",-1236537922),(!((goog.global.document == null)))], null)], 0));
if((typeof shadow !== 'undefined') && (typeof shadow.cljs !== 'undefined') && (typeof shadow.cljs.devtools !== 'undefined') && (typeof shadow.cljs.devtools.client !== 'undefined') && (typeof shadow.cljs.devtools.client.browser !== 'undefined') && (typeof shadow.cljs.devtools.client.browser.ws_was_welcome_ref !== 'undefined')){
} else {
shadow.cljs.devtools.client.browser.ws_was_welcome_ref = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
}
if(((shadow.cljs.devtools.client.env.enabled) && ((shadow.cljs.devtools.client.env.worker_client_id > (0))))){
(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$remote$runtime$api$IEvalJS$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$remote$runtime$api$IEvalJS$_js_eval$arity$4 = (function (this$,code,success,fail){
var this$__$1 = this;
try{var G__52229 = shadow.cljs.devtools.client.browser.global_eval(code);
return (success.cljs$core$IFn$_invoke$arity$1 ? success.cljs$core$IFn$_invoke$arity$1(G__52229) : success.call(null,G__52229));
}catch (e52228){var e = e52228;
return (fail.cljs$core$IFn$_invoke$arity$1 ? fail.cljs$core$IFn$_invoke$arity$1(e) : fail.call(null,e));
}}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_invoke$arity$5 = (function (this$,ns,p__52231,success,fail){
var map__52232 = p__52231;
var map__52232__$1 = cljs.core.__destructure_map(map__52232);
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52232__$1,new cljs.core.Keyword(null,"js","js",1768080579));
var this$__$1 = this;
try{var G__52237 = shadow.cljs.devtools.client.browser.global_eval(js);
return (success.cljs$core$IFn$_invoke$arity$1 ? success.cljs$core$IFn$_invoke$arity$1(G__52237) : success.call(null,G__52237));
}catch (e52235){var e = e52235;
return (fail.cljs$core$IFn$_invoke$arity$1 ? fail.cljs$core$IFn$_invoke$arity$1(e) : fail.call(null,e));
}}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_repl_init$arity$4 = (function (runtime,p__52240,done,error){
var map__52241 = p__52240;
var map__52241__$1 = cljs.core.__destructure_map(map__52241);
var repl_sources = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52241__$1,new cljs.core.Keyword(null,"repl-sources","repl-sources",723867535));
var runtime__$1 = this;
return shadow.cljs.devtools.client.shared.load_sources(runtime__$1,cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2(shadow.cljs.devtools.client.env.src_is_loaded_QMARK_,repl_sources)),(function (sources){
shadow.cljs.devtools.client.browser.do_js_load(sources);

return (done.cljs$core$IFn$_invoke$arity$0 ? done.cljs$core$IFn$_invoke$arity$0() : done.call(null));
}));
}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_repl_require$arity$4 = (function (runtime,p__52244,done,error){
var map__52245 = p__52244;
var map__52245__$1 = cljs.core.__destructure_map(map__52245);
var msg = map__52245__$1;
var sources = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52245__$1,new cljs.core.Keyword(null,"sources","sources",-321166424));
var reload_namespaces = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52245__$1,new cljs.core.Keyword(null,"reload-namespaces","reload-namespaces",250210134));
var js_requires = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52245__$1,new cljs.core.Keyword(null,"js-requires","js-requires",-1311472051));
var runtime__$1 = this;
var sources_to_load = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p__52248){
var map__52249 = p__52248;
var map__52249__$1 = cljs.core.__destructure_map(map__52249);
var src = map__52249__$1;
var provides = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52249__$1,new cljs.core.Keyword(null,"provides","provides",-1634397992));
var and__5140__auto__ = shadow.cljs.devtools.client.env.src_is_loaded_QMARK_(src);
if(cljs.core.truth_(and__5140__auto__)){
return cljs.core.not(cljs.core.some(reload_namespaces,provides));
} else {
return and__5140__auto__;
}
}),sources));
if(cljs.core.not(cljs.core.seq(sources_to_load))){
var G__52250 = cljs.core.PersistentVector.EMPTY;
return (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(G__52250) : done.call(null,G__52250));
} else {
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$3(runtime__$1,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"cljs-load-sources","cljs-load-sources",-1458295962),new cljs.core.Keyword(null,"to","to",192099007),shadow.cljs.devtools.client.env.worker_client_id,new cljs.core.Keyword(null,"sources","sources",-321166424),cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentVector.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582)),sources_to_load)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cljs-sources","cljs-sources",31121610),(function (p__52251){
var map__52252 = p__52251;
var map__52252__$1 = cljs.core.__destructure_map(map__52252);
var msg__$1 = map__52252__$1;
var sources__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52252__$1,new cljs.core.Keyword(null,"sources","sources",-321166424));
try{shadow.cljs.devtools.client.browser.do_js_load(sources__$1);

if(cljs.core.seq(js_requires)){
shadow.cljs.devtools.client.browser.do_js_requires(js_requires);
} else {
}

return (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(sources_to_load) : done.call(null,sources_to_load));
}catch (e52253){var ex = e52253;
return (error.cljs$core$IFn$_invoke$arity$1 ? error.cljs$core$IFn$_invoke$arity$1(ex) : error.call(null,ex));
}})], null));
}
}));

shadow.cljs.devtools.client.shared.add_plugin_BANG_(new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282),cljs.core.PersistentHashSet.EMPTY,(function (p__52256){
var map__52257 = p__52256;
var map__52257__$1 = cljs.core.__destructure_map(map__52257);
var env = map__52257__$1;
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52257__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
var svc = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"runtime","runtime",-1331573996),runtime], null);
shadow.remote.runtime.api.add_extension(runtime,new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-welcome","on-welcome",1895317125),(function (){
cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,true);

shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

shadow.cljs.devtools.client.env.patch_goog_BANG_();

return shadow.cljs.devtools.client.browser.devtools_msg((""+"#"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"client-id","client-id",-464622140).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword(null,"state-ref","state-ref",2127874952).cljs$core$IFn$_invoke$arity$1(runtime))))+" ready!"));
}),new cljs.core.Keyword(null,"on-disconnect","on-disconnect",-809021814),(function (e){
if(cljs.core.truth_(cljs.core.deref(shadow.cljs.devtools.client.browser.ws_was_welcome_ref))){
shadow.cljs.devtools.client.hud.connection_error("The Websocket connection was closed!");

return cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,false);
} else {
return null;
}
}),new cljs.core.Keyword(null,"on-reconnect","on-reconnect",1239988702),(function (e){
return shadow.cljs.devtools.client.hud.connection_error("Reconnecting ...");
}),new cljs.core.Keyword(null,"ops","ops",1237330063),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"access-denied","access-denied",959449406),(function (msg){
cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,false);

return shadow.cljs.devtools.client.hud.connection_error((""+"Stale Output! Your loaded JS was not produced by the running shadow-cljs instance."+" Is the watch for this build running?"));
}),new cljs.core.Keyword(null,"cljs-asset-update","cljs-asset-update",1224093028),(function (msg){
return shadow.cljs.devtools.client.browser.handle_asset_update(msg);
}),new cljs.core.Keyword(null,"cljs-build-configure","cljs-build-configure",-2089891268),(function (msg){
return null;
}),new cljs.core.Keyword(null,"cljs-build-start","cljs-build-start",-725781241),(function (msg){
shadow.cljs.devtools.client.hud.hud_hide();

shadow.cljs.devtools.client.hud.load_start();

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-start","build-start",-959649480)));
}),new cljs.core.Keyword(null,"cljs-build-complete","cljs-build-complete",273626153),(function (msg){
var msg__$1 = shadow.cljs.devtools.client.env.add_warnings_to_info(msg);
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

shadow.cljs.devtools.client.hud.hud_warnings(msg__$1);

shadow.cljs.devtools.client.browser.handle_build_complete(runtime,msg__$1);

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg__$1,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-complete","build-complete",-501868472)));
}),new cljs.core.Keyword(null,"cljs-build-failure","cljs-build-failure",1718154990),(function (msg){
shadow.cljs.devtools.client.hud.load_end();

shadow.cljs.devtools.client.hud.hud_error(msg);

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-failure","build-failure",-2107487466)));
}),new cljs.core.Keyword("shadow.cljs.devtools.client.env","worker-notify","shadow.cljs.devtools.client.env/worker-notify",-1456820670),(function (p__52263){
var map__52264 = p__52263;
var map__52264__$1 = cljs.core.__destructure_map(map__52264);
var event_op = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52264__$1,new cljs.core.Keyword(null,"event-op","event-op",200358057));
var client_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52264__$1,new cljs.core.Keyword(null,"client-id","client-id",-464622140));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-disconnect","client-disconnect",640227957),event_op)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(client_id,shadow.cljs.devtools.client.env.worker_client_id)))){
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

return shadow.cljs.devtools.client.hud.connection_error("The watch for this build was stopped!");
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-connect","client-connect",-1113973888),event_op)){
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

return shadow.cljs.devtools.client.hud.connection_error("The watch for this build was restarted. Reload required!");
} else {
return null;
}
}
})], null)], null));

return svc;
}),(function (p__52268){
var map__52271 = p__52268;
var map__52271__$1 = cljs.core.__destructure_map(map__52271);
var svc = map__52271__$1;
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52271__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
return shadow.remote.runtime.api.del_extension(runtime,new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282));
}));

shadow.cljs.devtools.client.shared.init_runtime_BANG_(shadow.cljs.devtools.client.browser.client_info,shadow.cljs.devtools.client.websocket.start,shadow.cljs.devtools.client.websocket.send,shadow.cljs.devtools.client.websocket.stop);
} else {
}

//# sourceMappingURL=shadow.cljs.devtools.client.browser.js.map
