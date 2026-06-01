goog.provide('day8.re_frame_10x.public$.events');
/**
 * Return a re-frame effect map that intentionally dispatches no effects.
 */
day8.re_frame_10x.public$.events.no_op_fx = (function day8$re_frame_10x$public$events$no_op_fx(){
return cljs.core.PersistentArrayMap.EMPTY;
});
/**
 * Return the match id immediately before the live tail, or nil when absent.
 */
day8.re_frame_10x.public$.events.second_newest_match_id = (function day8$re_frame_10x$public$events$second_newest_match_id(match_ids){
if((cljs.core.count(match_ids) > (1))){
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(match_ids,(cljs.core.count(match_ids) - (2)));
} else {
return null;
}
});
/**
 * Decide the fx for the `:day8.re-frame-10x.public/previous-epoch`
 * forwarder, gating no-op cases the internal handler does not.
 * 
 * Public to this namespace so unit tests can call it directly without
 * `#'` reach-around: the inner `::nav.events/previous` clobbers
 * `:selected-epoch-id` to nil at the oldest match and from the live
 * tail, so a behavioural test through the public surface can't
 * observe the no-op decisions this helper gates on.
 */
day8.re_frame_10x.public$.events.previous_epoch_fx = (function day8$re_frame_10x$public$events$previous_epoch_fx(p__23347){
var map__23348 = p__23347;
var map__23348__$1 = cljs.core.__destructure_map(map__23348);
var match_ids = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23348__$1,new cljs.core.Keyword(null,"match-ids","match-ids",752973161));
var selected_epoch_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23348__$1,new cljs.core.Keyword(null,"selected-epoch-id","selected-epoch-id",70601778));
var oldest_match_id = cljs.core.first(match_ids);
var at_oldest_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(selected_epoch_id,oldest_match_id);
var at_live_tail_QMARK_ = (selected_epoch_id == null);
var previous_tail_id = day8.re_frame_10x.public$.events.second_newest_match_id(match_ids);
if(((cljs.core.empty_QMARK_(match_ids)) || (at_oldest_QMARK_))){
return day8.re_frame_10x.public$.events.no_op_fx();
} else {
if(cljs.core.truth_((function (){var and__5140__auto__ = at_live_tail_QMARK_;
if(and__5140__auto__){
return previous_tail_id;
} else {
return and__5140__auto__;
}
})())){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","load","day8.re-frame-10x.navigation.epochs.events/load",1738587584),previous_tail_id], null)], null);
} else {
if(at_live_tail_QMARK_){
return day8.re_frame_10x.public$.events.no_op_fx();
} else {
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("day8.re-frame-10x.navigation.epochs.events","previous","day8.re-frame-10x.navigation.epochs.events/previous",2061328788)], null)], null);

}
}
}
});
day8.re_frame_10x.inlined_deps.re_frame.v1v3v0.re_frame.core.reg_event_fx.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("day8.re-frame-10x.public","previous-epoch","day8.re-frame-10x.public/previous-epoch",-137702738),(function (p__23352,_){
var map__23353 = p__23352;
var map__23353__$1 = cljs.core.__destructure_map(map__23353);
var db = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__23353__$1,new cljs.core.Keyword(null,"db","db",993250759));
return day8.re_frame_10x.public$.events.previous_epoch_fx(new cljs.core.Keyword(null,"epochs","epochs",1796936425).cljs$core$IFn$_invoke$arity$1(db));
}));

//# sourceMappingURL=day8.re_frame_10x.public.events.js.map
