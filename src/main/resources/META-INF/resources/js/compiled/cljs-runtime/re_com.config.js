goog.provide('re_com.config');
re_com.config.debug_QMARK_ = goog.DEBUG;
/**
 * @define {boolean}
 * @type {boolean}
 */
re_com.config.force_include_args_desc_QMARK_ = goog.define("re_com.config.force_include_args_desc_QMARK_",false);
/**
 * @define {string}
 * @type {string}
 */
re_com.config.root_url_for_compiler_output = goog.define("re_com.config.root_url_for_compiler_output","");
re_com.config.include_args_desc_QMARK_ = (function (){var or__5142__auto__ = re_com.config.debug_QMARK_;
if(cljs.core.truth_(or__5142__auto__)){
return or__5142__auto__;
} else {
return re_com.config.force_include_args_desc_QMARK_;
}
})();
/**
 * @define {string}
 * @type {string}
 */
re_com.config.version = goog.define("re_com.config.version","");

//# sourceMappingURL=re_com.config.js.map
