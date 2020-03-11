package com.boahey.exchangerateservice.controller.mainpage.templates

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize

private const val PATH_CSS = "/exchange-rate-service-kotlin/dist/bundle-react.css"

private const val PATH_JS = "/exchange-rate-service-kotlin/dist/bundle-react.js"

fun renderTemplate(demoListMeta: DemoListMeta, content: BODY.() -> Unit) = createHTMLDocument().html {
//    attributes["lang"] = getCurrentLanguage()

    head {
//        title { +translateKey("title") }

        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")
        meta(name = "theme-color", content = "#082036")
//        meta(name = "buildVersion", content = BUILDINFO.buildVersion())
        meta(name = "runtimeStage", content = demoListMeta.stage)

        demoListMeta.metaTags.forEach {
            meta(name = it.key, content = it.value)
        }

        // wish list data as JSON rendered by react
        meta(name = "reactModel", content = demoListMeta.reactModel)

        link(rel = "shortcut icon", href = "/favicon.ico")

        // https://www.filamentgroup.com/lab/load-css-simpler/
        link(rel = "preload", href = PATH_CSS) {
            attributes["as"] = "style"
        }
        link(rel = "stylesheet", href = PATH_CSS) {
            attributes["media"] = "print"
            attributes["onload"] = "this.media='all'"
        }
    }

    body {
        id = "wish-list"

        content()

        // log JS bundle load errors
        script() {
            unsafe {
                raw("""
						var wishListReactApp = document.createElement("script");

						wishListReactApp.src = "$PATH_JS";
						wishListReactApp.type = "text/javascript";
						wishListReactApp.async = true;

						document.body.appendChild(wishListReactApp);
					""".trimIndent())
            }
        }
    }
}.serialize(true)
