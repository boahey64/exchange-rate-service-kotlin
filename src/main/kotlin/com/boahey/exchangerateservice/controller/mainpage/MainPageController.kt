package com.boahey.exchangerateservice.controller.mainpage

import com.boahey.exchangerateservice.config.togglz.ExchangeRateFeature
import com.boahey.exchangerateservice.controller.mainpage.templates.DemoListMeta
import com.boahey.exchangerateservice.controller.mainpage.templates.renderDemoList
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/exchange-rate")
class MainPageController {
    @ResponseBody
    @GetMapping(value = ["rate"], produces = [MediaType.TEXT_HTML_VALUE])
    fun ratePage(request: HttpServletRequest, response: HttpServletResponse) =
        ResponseEntity(mainPageContent(request, response, ::renderDemoList), HttpStatus.OK)

    @ResponseBody
    @GetMapping(value = ["history"], produces = [MediaType.TEXT_HTML_VALUE])
    fun historyPage(request: HttpServletRequest, response: HttpServletResponse) =
            ResponseEntity(mainPageContent(request, response, ::renderDemoList), HttpStatus.OK)

    @ResponseBody
    @GetMapping(value = ["usage"], produces = [MediaType.TEXT_HTML_VALUE])
    fun usagePage(request: HttpServletRequest, response: HttpServletResponse) =
            ResponseEntity(mainPageContent(request, response, ::renderDemoList), HttpStatus.OK)

    private fun mainPageContent(request: HttpServletRequest,
                                response: HttpServletResponse,
                                renderer: (meta: DemoListMeta) -> String): String {

        val meta = DemoListMeta("dev",
                basicMetaTags(),
                null
        )
        return renderer(meta)
    }

    private fun basicMetaTags(): Map<String, String> = mapOf(
        "boahey-demo-i18n" to "frontendI18nBundle()",
        "boahey-demo-tld" to "site.language",
            "wishlist-features" to calculateFeatureToggles()
    )

    private fun calculateFeatureToggles(): String {
        val activeToggles = getActiveToggles()
        if (activeToggles.isNotEmpty()) {
            return "$activeToggles, EXTRA_FEATURE"
        }
        return "EXTRA_FEATURE"
    }
    private fun getActiveToggles(): String =
            ExchangeRateFeature.values().filter { it.isActive }.joinToString(separator = ",") { it.name }

}