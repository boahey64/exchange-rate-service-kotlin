package com.boahey.exchangerateservice.controller.mainpage.templates.common

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.style

fun FlowContent.simpleSpinner() =
    div {
        id = "loading-animation"
        style = "display:block!important"
        div("spinner absolutely-centered") {
            div("double-bounce1") {}
            div("double-bounce2") {}
        }
    }
