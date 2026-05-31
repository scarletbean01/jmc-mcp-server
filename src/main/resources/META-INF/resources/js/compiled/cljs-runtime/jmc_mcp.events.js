goog.provide('jmc_mcp.events');
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"initialize-db","initialize-db",230998432),(function (_,___$1){
return jmc_mcp.db.default_db;
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("route","changed","route/changed",1518454126),(function (db,p__17379){
var vec__17380 = p__17379;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17380,(0),null);
var new_match = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17380,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"current","current",-1088038603),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(new_match,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"data","data",-232669377),new cljs.core.Keyword(null,"name","name",1843675177)], null)),new cljs.core.Keyword(null,"params","params",710516235),new cljs.core.Keyword(null,"path-params","path-params",-48130597).cljs$core$IFn$_invoke$arity$1(new_match)], null));
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("notification","add","notification/add",-797397960),(function (db,p__17383){
var vec__17384 = p__17383;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17384,(0),null);
var notification = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17384,(1),null);
var id = cljs.core.random_uuid();
var new_notification = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(notification,new cljs.core.Keyword(null,"id","id",-1388402092),id);
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(db,new cljs.core.Keyword(null,"notifications","notifications",1685638001),cljs.core.conj,new_notification);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("notification","remove","notification/remove",464922843),(function (p__17388,p__17389){
var map__17390 = p__17388;
var map__17390__$1 = cljs.core.__destructure_map(map__17390);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17390__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17391 = p__17389;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17391,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17391,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.update.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"notifications","notifications",1685638001),(function (ns){
return cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__17387_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(p1__17387_SHARP_),id);
}),ns);
}))], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","load-recordings","library/load-recordings",1562348153),(function (p__17394,_){
var map__17395 = p__17394;
var map__17395__$1 = cljs.core.__destructure_map(map__17395);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17395__$1,new cljs.core.Keyword(null,"db","db",993250759));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http-xhrio","http-xhrio",1846166714),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"get","get",1683182755),new cljs.core.Keyword(null,"uri","uri",-774711847),jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["/recordings"], 0)),new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"on-success","on-success",1786904109),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","recordings-loaded","library/recordings-loaded",-1193177927)], null),new cljs.core.Keyword(null,"on-failure","on-failure",842888245),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","recordings-failed","library/recordings-failed",1762644042)], null)], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","recordings-loaded","library/recordings-loaded",-1193177927),(function (db,p__17396){
var vec__17397 = p__17396;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17397,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17397,(1),null);
return cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"items","items",1031954938)], null),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response));
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","recordings-failed","library/recordings-failed",1762644042),(function (p__17406,p__17407){
var map__17409 = p__17406;
var map__17409__$1 = cljs.core.__destructure_map(map__17409);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17409__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17410 = p__17407;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17410,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17410,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Failed to load recordings"], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","delete-recording","library/delete-recording",1598501410),(function (p__17421,p__17422){
var map__17423 = p__17421;
var map__17423__$1 = cljs.core.__destructure_map(map__17423);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17423__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17424 = p__17422;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17424,(0),null);
var recording_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17424,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"http-xhrio","http-xhrio",1846166714),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"delete","delete",-1768633620),new cljs.core.Keyword(null,"uri","uri",-774711847),jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["/recordings/",recording_id], 0)),new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"on-success","on-success",1786904109),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","recording-deleted","library/recording-deleted",-694082837),recording_id], null),new cljs.core.Keyword(null,"on-failure","on-failure",842888245),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("notification","add","notification/add",-797397960),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Failed to delete recording"], null)], null)], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","recording-deleted","library/recording-deleted",-694082837),(function (p__17428,p__17429){
var map__17430 = p__17428;
var map__17430__$1 = cljs.core.__destructure_map(map__17430);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17430__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17431 = p__17429;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17431,(0),null);
var recording_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17431,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"items","items",1031954938)], null),(function (items){
return cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__17427_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(p1__17427_SHARP_),recording_id);
}),items);
})),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"success","success",1890645906),new cljs.core.Keyword(null,"message","message",-406056002),"Recording deleted"], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","submit","upload/submit",721130508),(function (p__17434,p__17435){
var map__17436 = p__17434;
var map__17436__$1 = cljs.core.__destructure_map(map__17436);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17436__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17437 = p__17435;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17437,(0),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17437,(1),null);
var form_data = (new FormData());
form_data.append("file",file);

return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"uploading","uploading",1069939393),new cljs.core.Keyword(null,"progress","progress",244323547),(0),new cljs.core.Keyword(null,"error","error",-978969032),null], null)),new cljs.core.Keyword(null,"http-xhrio","http-xhrio",1846166714),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"post","post",269697687),new cljs.core.Keyword(null,"uri","uri",-774711847),jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["/recordings/upload"], 0)),new cljs.core.Keyword(null,"body","body",-2049205669),form_data,new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"on-success","on-success",1786904109),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("upload","success","upload/success",984925585)], null),new cljs.core.Keyword(null,"on-failure","on-failure",842888245),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("upload","failure","upload/failure",2094840442)], null)], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","success","upload/success",984925585),(function (p__17455,p__17456){
var map__17457 = p__17455;
var map__17457__$1 = cljs.core.__destructure_map(map__17457);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17457__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17458 = p__17456;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17458,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17458,(1),null);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"success","success",1890645906),new cljs.core.Keyword(null,"progress","progress",244323547),(100),new cljs.core.Keyword(null,"error","error",-978969032),null], null)),new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","load-recordings","library/load-recordings",1562348153)], null),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"success","success",1890645906),new cljs.core.Keyword(null,"message","message",-406056002),"Upload successful"], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","failure","upload/failure",2094840442),(function (db,p__17471){
var vec__17472 = p__17471;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17472,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17472,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"progress","progress",244323547),(0),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)], null));
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","select-type","analysis/select-type",102415334),(function (p__17475,p__17476){
var map__17477 = p__17475;
var map__17477__$1 = cljs.core.__destructure_map(map__17477);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17477__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17478 = p__17476;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17478,(0),null);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17478,(1),null);
var recording_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.Keyword(null,"params","params",710516235),new cljs.core.Keyword(null,"id","id",-1388402092)], null));
var result = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"results","results",-1134170113),type], null));
var G__17481 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"active-analysis","active-analysis",1740725080)], null),type)], null);
if(cljs.core.not(result)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17481,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","run","analysis/run",-832355441),recording_id,type,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"analysis-params","analysis-params",-1122335336)], null))], null));
} else {
return G__17481;
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","run","analysis/run",-832355441),(function (p__17482,p__17483){
var map__17484 = p__17482;
var map__17484__$1 = cljs.core.__destructure_map(map__17484);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17484__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17485 = p__17483;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17485,(0),null);
var recording_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17485,(1),null);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17485,(2),null);
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17485,(3),null);
if(cljs.core.truth_((function (){var and__5140__auto__ = recording_id;
if(cljs.core.truth_(and__5140__auto__)){
return type;
} else {
return and__5140__auto__;
}
})())){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http-xhrio","http-xhrio",1846166714),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"post","post",269697687),new cljs.core.Keyword(null,"uri","uri",-774711847),jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["/recordings/",recording_id,"/analyze/",cljs.core.name(type)], 0)),new cljs.core.Keyword(null,"params","params",710516235),params,new cljs.core.Keyword(null,"format","format",-1306924766),ajax.core.json_request_format(),new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"on-success","on-success",1786904109),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","result","analysis/result",325641471),type], null),new cljs.core.Keyword(null,"on-failure","on-failure",842888245),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","failed","analysis/failed",-406541374),type], null)], null)], null);
} else {
return null;
}
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","result","analysis/result",325641471),(function (db,p__17488){
var vec__17489 = p__17488;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17489,(0),null);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17489,(1),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17489,(2),null);
return cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"results","results",-1134170113),type], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"done","done",-889844188),new cljs.core.Keyword(null,"data","data",-232669377),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response)], null));
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","failed","analysis/failed",-406541374),(function (p__17492,p__17493){
var map__17494 = p__17492;
var map__17494__$1 = cljs.core.__destructure_map(map__17494);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17494__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17495 = p__17493;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17495,(0),null);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17495,(1),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17495,(2),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Analysis failed for "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(type)))], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("comparison","run","comparison/run",-1524236354),(function (p__17508,p__17509){
var map__17512 = p__17508;
var map__17512__$1 = cljs.core.__destructure_map(map__17512);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17512__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17513 = p__17509;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17513,(0),null);
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17513,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http-xhrio","http-xhrio",1846166714),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"post","post",269697687),new cljs.core.Keyword(null,"uri","uri",-774711847),jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["/compare/structured"], 0)),new cljs.core.Keyword(null,"params","params",710516235),params,new cljs.core.Keyword(null,"format","format",-1306924766),ajax.core.json_request_format(),new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"on-success","on-success",1786904109),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","result","comparison/result",-507401234)], null),new cljs.core.Keyword(null,"on-failure","on-failure",842888245),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comparison","failed","comparison/failed",452445589)], null)], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("comparison","result","comparison/result",-507401234),(function (db,p__17522){
var vec__17524 = p__17522;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17524,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17524,(1),null);
return cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"result","result",1415092211)], null),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response));
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","run-async","analysis/run-async",596115567),(function (p__17532,p__17533){
var map__17534 = p__17532;
var map__17534__$1 = cljs.core.__destructure_map(map__17534);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17534__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17535 = p__17533;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17535,(0),null);
var recording_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17535,(1),null);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17535,(2),null);
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17535,(3),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http-xhrio","http-xhrio",1846166714),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"post","post",269697687),new cljs.core.Keyword(null,"uri","uri",-774711847),jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["/recordings/",recording_id,"/analyze/",cljs.core.name(type),"/async"], 0)),new cljs.core.Keyword(null,"params","params",710516235),params,new cljs.core.Keyword(null,"format","format",-1306924766),ajax.core.json_request_format(),new cljs.core.Keyword(null,"response-format","response-format",1664465322),ajax.core.json_response_format(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true], null)),new cljs.core.Keyword(null,"on-success","on-success",1786904109),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","job-created","analysis/job-created",1056396973),recording_id,type], null),new cljs.core.Keyword(null,"on-failure","on-failure",842888245),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","failed","analysis/failed",-406541374),type], null)], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","job-created","analysis/job-created",1056396973),(function (p__17557,p__17558){
var map__17561 = p__17557;
var map__17561__$1 = cljs.core.__destructure_map(map__17561);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17561__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17562 = p__17558;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17562,(0),null);
var recording_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17562,(1),null);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17562,(2),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17562,(3),null);
var job_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(response,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"data","data",-232669377),new cljs.core.Keyword(null,"jobId","jobId",1965699355)], null));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jobs","jobs",-313607120),job_id], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564),recording_id,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"analysisType","analysisType",244961091),type], 0))),new cljs.core.Keyword("sse","connect","sse/connect",1232641382),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"url","url",276297046),jmc_mcp.api.client.url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["/recordings/",recording_id,"/analyze/jobs/",job_id,"/stream"], 0)),new cljs.core.Keyword(null,"on-message","on-message",1662987808),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("job","update","job/update",1045618437),job_id], null),new cljs.core.Keyword(null,"on-error","on-error",1728533530),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("job","sse-failed","job/sse-failed",-1664316261),job_id], null)], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","update","job/update",1045618437),(function (p__17580,p__17581){
var map__17586 = p__17580;
var map__17586__$1 = cljs.core.__destructure_map(map__17586);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__17586__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__17587 = p__17581;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17587,(0),null);
var job_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17587,(1),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17587,(2),null);
var status_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response);
var status = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(status_data);
var job = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jobs","jobs",-313607120),job_id], null));
var G__17595 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jobs","jobs",-313607120),job_id], null),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([job,status_data], 0)))], null);
var G__17595__$1 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,"COMPLETED"))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17595,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","result","analysis/result",325641471),new cljs.core.Keyword(null,"analysisType","analysisType",244961091).cljs$core$IFn$_invoke$arity$1(job),response], null)):G__17595);
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,"COMPLETED")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,"FAILED")))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__17595__$1,new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,"COMPLETED"))?new cljs.core.Keyword(null,"success","success",1890645906):new cljs.core.Keyword(null,"error","error",-978969032)),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Job "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(new cljs.core.Keyword(null,"analysisType","analysisType",244961091).cljs$core$IFn$_invoke$arity$1(job)))+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(status))], null));
} else {
return G__17595__$1;
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","sse-failed","job/sse-failed",-1664316261),(function (_,p__17613){
var vec__17635 = p__17613;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17635,(0),null);
var job_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__17635,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),(""+"SSE connection failed for job "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(job_id))], null)], null);
}));

//# sourceMappingURL=jmc_mcp.events.js.map
