package com.boahey.exchangerateservice.controller.mainpage.templates

import com.boahey.exchangerateservice.controller.mainpage.templates.common.simpleSpinner
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.style

fun renderDemoList(meta: DemoListMeta): String =
    renderTemplate(meta) {
        div {
            id = "SinglePageApp"
            style = "display: flex; flex: 1 0 auto; flex-direction: column; min-height: 500px;"
            simpleSpinner()
        }
    }

data class DemoListMeta(
    val stage: String,
    val metaTags: Map<String, String> = emptyMap(),
    val reactModel: String? = null
)
