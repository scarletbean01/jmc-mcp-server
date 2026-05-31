goog.provide('day8.re_frame_10x.fx.clipboard');
day8.re_frame_10x.fx.clipboard.copy_BANG_ = (function day8$re_frame_10x$fx$clipboard$copy_BANG_(text){
var doc = (function (){var or__5142__auto__ = (function (){var G__37837 = cljs.core.deref(day8.re_frame_10x.fx.window.popout_window);
if((G__37837 == null)){
return null;
} else {
return G__37837.document;
}
})();
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return document;
}
})();
var el = doc.createElement("textarea");
(el.value = text);

(el.style.position = "absolute");

(el.style.left = "-9999px");

doc.body.appendChild(el);

el.select();

doc.execCommand("copy");

return doc.body.removeChild(el);
});

//# sourceMappingURL=day8.re_frame_10x.fx.clipboard.js.map
