goog.provide('frontend.events');
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"initialize-db","initialize-db",230998432),(function (_,___$1){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),frontend.db.default_db], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("route","changed","route/changed",1518454126),(function (p__27436,p__27437){
var map__27438 = p__27436;
var map__27438__$1 = cljs.core.__destructure_map(map__27438);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27438__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27442 = p__27437;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27442,(0),null);
var match = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27442,(1),null);
var route_name = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(match));
var params = new cljs.core.Keyword(null,"path-params","path-params",-48130597).cljs$core$IFn$_invoke$arity$1(match);
var prev_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564)], null));
var new_id = new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(params);
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),(function (){var G__27448 = cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.Keyword(null,"current","current",-1088038603)], null),route_name),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.Keyword(null,"params","params",710516235)], null),params);
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(route_name,new cljs.core.Keyword(null,"recording","recording",322996097))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(prev_id,new_id)))){
return cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(G__27448,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"active-analysis","active-analysis",1740725080)], null),null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"error","error",-978969032)], null),null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false);
} else {
return G__27448;
}
})()], null),(function (){var G__27449 = route_name;
var G__27449__$1 = (((G__27449 instanceof cljs.core.Keyword))?G__27449.fqn:null);
switch (G__27449__$1) {
case "library":
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","load-recordings","library/load-recordings",1562348153)], null)], null);

break;
case "recording":
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("recording","load-info","recording/load-info",540470661),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(params)], null)], null);

break;
case "compare":
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","load-recordings","library/load-recordings",1562348153)], null)], null);

break;
default:
return null;

}
})()], 0));
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("notification","add","notification/add",-797397960),(function (p__27450,p__27451){
var map__27452 = p__27450;
var map__27452__$1 = cljs.core.__destructure_map(map__27452);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27452__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27453 = p__27451;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27453,(0),null);
var map__27456 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27453,(1),null);
var map__27456__$1 = cljs.core.__destructure_map(map__27456);
var notification = map__27456__$1;
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27456__$1,new cljs.core.Keyword(null,"type","type",1174270348));
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27456__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var id = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.random_uuid()));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.update.cljs$core$IFn$_invoke$arity$4(db,new cljs.core.Keyword(null,"notifications","notifications",1685638001),cljs.core.conj,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(notification,new cljs.core.Keyword(null,"id","id",-1388402092),id)),new cljs.core.Keyword(null,"dispatch-later","dispatch-later",291951390),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ms","ms",-1152709733),(5000),new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("notification","remove","notification/remove",464922843),id], null)], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("notification","remove","notification/remove",464922843),(function (db,p__27460){
var vec__27461 = p__27460;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27461,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27461,(1),null);
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"notifications","notifications",1685638001),(function (p1__27459_SHARP_){
return cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.createAsIfByAssoc([id]),new cljs.core.Keyword(null,"id","id",-1388402092)),p1__27459_SHARP_));
}));
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"navigate","navigate",657596805),(function (_,p__27466){
var vec__27468 = p__27466;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27468,(0),null);
var route = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27468,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"navigate","navigate",657596805),route], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("ui","toggle-sidebar","ui/toggle-sidebar",909970235),(function (db,_){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ui","ui",-469653645),new cljs.core.Keyword(null,"sidebar-collapsed?","sidebar-collapsed?",-936962365)], null),cljs.core.not);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","load-recordings","library/load-recordings",1562348153),(function (p__27472,_){
var map__27473 = p__27472;
var map__27473__$1 = cljs.core.__destructure_map(map__27473);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27473__$1,new cljs.core.Keyword(null,"db","db",993250759));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http","http",382524695),frontend.api.client.get_recordings()], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","recordings-loaded","library/recordings-loaded",-1193177927),(function (p__27476,p__27477){
var map__27479 = p__27476;
var map__27479__$1 = cljs.core.__destructure_map(map__27479);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27479__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27480 = p__27477;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27480,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27480,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"items","items",1031954938)], null),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"error","error",-978969032)], null),null)], null);
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Failed to load recordings: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)))], null)], null);
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","recordings-failed","library/recordings-failed",1762644042),(function (p__27486,p__27487){
var map__27488 = p__27486;
var map__27488__$1 = cljs.core.__destructure_map(map__27488);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27488__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27489 = p__27487;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27489,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27489,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recordings","recordings",1958237829),new cljs.core.Keyword(null,"error","error",-978969032)], null),"Failed to load recordings"),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Failed to load recordings"], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","delete-recording","library/delete-recording",1598501410),(function (_,p__27501){
var vec__27502 = p__27501;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27502,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27502,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"http","http",382524695),frontend.api.client.delete_recording(id)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","recording-deleted","library/recording-deleted",-694082837),(function (_,p__27507){
var vec__27508 = p__27507;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27508,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27508,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","load-recordings","library/load-recordings",1562348153)], null),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"success","success",1890645906),new cljs.core.Keyword(null,"message","message",-406056002),"Recording deleted"], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("library","recording-delete-failed","library/recording-delete-failed",-2040482149),(function (_,p__27511){
var vec__27512 = p__27511;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27512,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27512,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Failed to delete recording"], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","select-file","upload/select-file",-895604093),(function (db,p__27521){
var vec__27522 = p__27521;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27522,(0),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27522,(1),null);
return cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"file","file",-1269645878)], null),file),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"error","error",-978969032)], null),null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","submit","upload/submit",721130508),(function (p__27526,_){
var map__27527 = p__27526;
var map__27527__$1 = cljs.core.__destructure_map(map__27527);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27527__$1,new cljs.core.Keyword(null,"db","db",993250759));
var file = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"file","file",-1269645878)], null));
if(cljs.core.truth_(file)){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"status","status",-1997798413)], null),new cljs.core.Keyword(null,"uploading","uploading",1069939393)),new cljs.core.Keyword(null,"http","http",382524695),frontend.api.client.upload_recording(file)], null);
} else {
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"warning","warning",-1685650671),new cljs.core.Keyword(null,"message","message",-406056002),"Please select a file first"], null)], null);
}
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","progress","upload/progress",-1600913698),(function (db,p__27531){
var vec__27532 = p__27531;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27532,(0),null);
var percent = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27532,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"progress","progress",244323547)], null),percent);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","success","upload/success",984925585),(function (p__27544,p__27545){
var map__27546 = p__27544;
var map__27546__$1 = cljs.core.__destructure_map(map__27546);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27546__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27547 = p__27545;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27547,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27547,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
var recording_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(response,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"data","data",-232669377),new cljs.core.Keyword(null,"recordingId","recordingId",-308363159)], null));
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"status","status",-1997798413)], null),new cljs.core.Keyword(null,"idle","idle",-2007156861)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"progress","progress",244323547)], null),(0)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"error","error",-978969032)], null),null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"file","file",-1269645878)], null),null),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"success","success",1890645906),new cljs.core.Keyword(null,"message","message",-406056002),"Recording uploaded successfully"], null),new cljs.core.Keyword(null,"dispatch-n","dispatch-n",-504469236),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("library","load-recordings","library/load-recordings",1562348153)], null),(cljs.core.truth_(recording_id)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"navigate","navigate",657596805),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording","recording",322996097),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"id","id",-1388402092),recording_id], null)], null)], null):null)], null)], null);
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"status","status",-1997798413)], null),new cljs.core.Keyword(null,"error","error",-978969032)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Upload failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)))], null)], null);
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("upload","failure","upload/failure",2094840442),(function (p__27553,p__27554){
var map__27555 = p__27553;
var map__27555__$1 = cljs.core.__destructure_map(map__27555);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27555__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27556 = p__27554;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27556,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27556,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"status","status",-1997798413)], null),new cljs.core.Keyword(null,"error","error",-978969032)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"upload","upload",-255769218),new cljs.core.Keyword(null,"error","error",-978969032)], null),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "Upload failed";
}
})()),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Upload failed"], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("recording","load-info","recording/load-info",540470661),(function (p__27565,p__27566){
var map__27567 = p__27565;
var map__27567__$1 = cljs.core.__destructure_map(map__27567);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27567__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27568 = p__27566;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27568,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27568,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564)], null),id),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http","http",382524695),frontend.api.client.get_recording_info(id)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("recording","info-loaded","recording/info-loaded",-1486213417),(function (p__27572,p__27573){
var map__27574 = p__27572;
var map__27574__$1 = cljs.core.__destructure_map(map__27574);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27574__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27575 = p__27573;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27575,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27575,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
var recording_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564)], null));
var active_analysis = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"active-analysis","active-analysis",1740725080)], null));
var has_overview_QMARK_ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"results","results",-1134170113),recording_id,new cljs.core.Keyword(null,"overview","overview",-435037267)], null));
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"info","info",-317069002)], null),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"error","error",-978969032)], null),null)], null),(cljs.core.truth_((function (){var and__5140__auto__ = (active_analysis == null);
if(and__5140__auto__){
var and__5140__auto____$1 = cljs.core.not(has_overview_QMARK_);
if(and__5140__auto____$1){
return recording_id;
} else {
return and__5140__auto____$1;
}
} else {
return and__5140__auto__;
}
})())?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch-n","dispatch-n",-504469236),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","select-type","analysis/select-type",102415334),new cljs.core.Keyword(null,"overview","overview",-435037267)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","run-sync","analysis/run-sync",-135774464)], null)], null)], null):null)], 0));
} else {
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false)], null);
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("recording","info-failed","recording/info-failed",1254243563),(function (p__27584,p__27585){
var map__27586 = p__27584;
var map__27586__$1 = cljs.core.__destructure_map(map__27586);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27586__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27587 = p__27585;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27587,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27587,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"error","error",-978969032)], null),"Failed to load recording info"),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Failed to load recording info"], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","select-type","analysis/select-type",102415334),(function (db,p__27592){
var vec__27595 = p__27592;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27595,(0),null);
var analysis_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27595,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"active-analysis","active-analysis",1740725080)], null),analysis_type);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","update-params","analysis/update-params",278999301),(function (db,p__27601){
var vec__27602 = p__27601;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27602,(0),null);
var key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27602,(1),null);
var value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27602,(2),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"analysis-params","analysis-params",-1122335336),key], null),value);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","run","analysis/run",-832355441),(function (p__27605,p__27606){
var map__27607 = p__27605;
var map__27607__$1 = cljs.core.__destructure_map(map__27607);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27607__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27608 = p__27606;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27608,(0),null);
var sync_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27608,(1),null);
var id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564)], null));
var analysis_type = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"active-analysis","active-analysis",1740725080)], null));
var params = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"analysis-params","analysis-params",-1122335336)], null));
if(cljs.core.truth_((function (){var and__5140__auto__ = id;
if(cljs.core.truth_(and__5140__auto__)){
return analysis_type;
} else {
return and__5140__auto__;
}
})())){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http","http",382524695),(cljs.core.truth_(sync_QMARK_)?frontend.api.client.run_analysis(id,analysis_type,params):frontend.api.client.run_analysis_async(id,analysis_type,params))], null);
} else {
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"warning","warning",-1685650671),new cljs.core.Keyword(null,"message","message",-406056002),"Please select an analysis type"], null)], null);
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","run-sync","analysis/run-sync",-135774464),(function (_,___$1){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","run","analysis/run",-832355441),true], null)], null);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","run-async","analysis/run-async",596115567),(function (_,___$1){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("analysis","run","analysis/run",-832355441),false], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","result","analysis/result",325641471),(function (db,p__27614){
var vec__27615 = p__27614;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27615,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27615,(1),null);
var recording_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564)], null));
var analysis_type = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"active-analysis","active-analysis",1740725080)], null));
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
return cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"results","results",-1134170113),recording_id,analysis_type], null),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"error","error",-978969032)], null),null);
} else {
return cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false);
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","async-started","analysis/async-started",-1117435448),(function (p__27621,p__27622){
var map__27624 = p__27621;
var map__27624__$1 = cljs.core.__destructure_map(map__27624);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27624__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27625 = p__27622;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27625,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27625,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
var job_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(response,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"data","data",-232669377),new cljs.core.Keyword(null,"jobId","jobId",1965699355)], null));
var recording_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564)], null));
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jobs","jobs",-313607120),job_id], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"status","status",-1997798413),new cljs.core.Keyword(null,"pending","pending",-220036727),new cljs.core.Keyword(null,"recording-id","recording-id",-1536489564),recording_id,new cljs.core.Keyword(null,"progress","progress",244323547),(0)], null)),new cljs.core.Keyword("sse","connect","sse/connect",1232641382),frontend.api.client.connect_sse(recording_id,job_id),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"info","info",-317069002),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Analysis job started: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(job_id))], null)], null);
} else {
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Failed to start analysis: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)))], null)], null);
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("analysis","failed","analysis/failed",-406541374),(function (p__27641,p__27642){
var map__27644 = p__27641;
var map__27644__$1 = cljs.core.__destructure_map(map__27644);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27644__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27645 = p__27642;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27645,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27645,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"recording-detail","recording-detail",-702248427),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "Analysis failed";
}
})()], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","update","job/update",1045618437),(function (db,p__27649){
var vec__27650 = p__27649;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27650,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27650,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
var job_id = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(response,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"data","data",-232669377),new cljs.core.Keyword(null,"jobId","jobId",1965699355)], null));
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jobs","jobs",-313607120),job_id], null),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response));
} else {
return db;
}
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","sse-message","job/sse-message",-1294649592),(function (db,p__27659){
var vec__27662 = p__27659;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27662,(0),null);
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27662,(1),null);
var job_id = new cljs.core.Keyword(null,"jobId","jobId",1965699355).cljs$core$IFn$_invoke$arity$1(data);
if(cljs.core.truth_(job_id)){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jobs","jobs",-313607120),job_id], null),cljs.core.merge,data);
} else {
return db;
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","sse-failed","job/sse-failed",-1664316261),(function (_,p__27666){
var vec__27667 = p__27666;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27667,(0),null);
var error = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27667,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"SSE connection failed"], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","sse-opened","job/sse-opened",209553114),(function (db,_){
return db;
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","remove","job/remove",-131517307),(function (db,p__27676){
var vec__27677 = p__27676;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27677,(0),null);
var job_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27677,(1),null);
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(db,new cljs.core.Keyword(null,"jobs","jobs",-313607120),cljs.core.dissoc,job_id);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("job","status-failed","job/status-failed",1339370171),(function (_,p__27683){
var vec__27684 = p__27683;
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27684,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27684,(1),null);
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Failed to fetch job status"], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("comparison","select-baseline","comparison/select-baseline",1932181617),(function (db,p__27692){
var vec__27693 = p__27692;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27693,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27693,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"baseline-id","baseline-id",807615351)], null),id);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("comparison","select-target","comparison/select-target",-161747456),(function (db,p__27705){
var vec__27707 = p__27705;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27707,(0),null);
var id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27707,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"target-id","target-id",-1438159155)], null),id);
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("comparison","run","comparison/run",-1524236354),(function (p__27716,_){
var map__27717 = p__27716;
var map__27717__$1 = cljs.core.__destructure_map(map__27717);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27717__$1,new cljs.core.Keyword(null,"db","db",993250759));
var baseline = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"baseline-id","baseline-id",807615351)], null));
var target = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"target-id","target-id",-1438159155)], null));
if(cljs.core.truth_((function (){var and__5140__auto__ = baseline;
if(cljs.core.truth_(and__5140__auto__)){
return target;
} else {
return and__5140__auto__;
}
})())){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true),new cljs.core.Keyword(null,"http","http",382524695),frontend.api.client.run_comparison(baseline,target)], null);
} else {
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"warning","warning",-1685650671),new cljs.core.Keyword(null,"message","message",-406056002),"Please select both recordings"], null)], null);
}
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("comparison","result","comparison/result",-507401234),(function (db,p__27727){
var vec__27728 = p__27727;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27728,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27728,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
return cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"result","result",1415092211)], null),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"error","error",-978969032)], null),null);
} else {
return cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false);
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("comparison","error","comparison/error",1926451741),(function (p__27741,p__27742){
var map__27743 = p__27741;
var map__27743__$1 = cljs.core.__destructure_map(map__27743);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__27743__$1,new cljs.core.Keyword(null,"db","db",993250759));
var vec__27744 = p__27742;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27744,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27744,(1),null);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"db","db",993250759),cljs.core.assoc_in(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comparison","comparison",-986182462),new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false),new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),(function (){var or__5142__auto__ = new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(response);
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return "Comparison failed";
}
})()], null)], null);
}));
re_frame.core.reg_event_db.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("health","loaded","health/loaded",-60084697),(function (db,p__27754){
var vec__27756 = p__27754;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27756,(0),null);
var response = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__27756,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(response))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"health","health",-295520649),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(response));
} else {
return db;
}
}));
re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("health","failed","health/failed",-209586366),(function (_,___$1){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"notify","notify",-1256867814),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"message","message",-406056002),"Health check failed"], null)], null);
}));

//# sourceMappingURL=frontend.events.js.map
