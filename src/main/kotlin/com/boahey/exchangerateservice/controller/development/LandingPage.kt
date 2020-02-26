package com.boahey.exchangerateservice.controller.development

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletResponse

@RestController
class LandingPage(
    private val environment: Environment
) {

    val groups = listOf(
            Triple("rate", "ExchangeRate", "\uD83C\uDDE9\uD83C\uDDEA"),
            Triple("history", "ExchangeRateHistory", "\uD83C\uDDE6\uD83C\uDDF9"),
            Triple("usage", "ExchangeRateUsage", "\uD83C\uDDEA\uD83C\uDDF8")
    )

    @GetMapping("/")
    fun showPage(): String =
            buildString {
                appendln("<!DOCTYPE html>")

                appendHTML(true).html {
                    head {
                        meta(charset = "utf-8")
                        meta(name = "viewport", content = "width=device-width, initial-scale=1, shrink-to-fit=no")
                        // <link rel="icon" type="image/png" href="https://pre00.deviantart.net/9bb8/th/pre/i/2016/144/4/5/blade_runner_tyrell_corporation_logo_by_viperaviator-da3nsmf.png">
                        link(rel = "icon", href = "https://pre00.deviantart.net/9bb8/th/pre/i/2016/144/4/5/blade_runner_tyrell_corporation_logo_by_viperaviator-da3nsmf.png", type = "image/png")
                        style {
                            unsafe {
                                raw("""
							body {
								background-color: #272727;
								color: #ccc;
								font-family: arial;
								margin: 75px;
							}

							.box {
								background-color: #505050;
								border: #a6a6a6 3px solid;
								border-radius: 5px;
								display: inline-block;
								margin-top: 50px;
							}

							.box .title {
								background-color: #a6a6a6;
								border-radius: 5px;
								color: black;
								display: inline;
								font-weight: bolder;
								font-size: x-large;
								line-height: 1;
								margin-left: 1em;
								margin-right: 1em;
								padding: 5px 15px;
								position: relative;
								top: -0.5em;
							}

							.box .content {
								padding: 10px;
							}

							.button-group {
								background-color: #6e6e6e;
								border: 2px solid #7b7b7b;
								border-radius: 5px;
								display: inline-block;
								margin: 5px;
								text-align: center;
							}

							.button-group a {
								border-top: 2px solid #7b7b7b;
								padding-left: 10px;
								padding-right: 10px;
							}

							.button-group a:nth-child(even) {
								border-left: 2px solid #7b7b7b;
							}

							.button-group a:visited, .button-group a:link, .button-group a:active {
								color: #ddd;
								text-decoration: none;
							}

							.button-group a:hover {
								background-color: #4e4e4e;
								color: white;
							}

							img {
								width: 150px;
								opacity: .5;
								position: fixed;
								right: 20px;
								top: 20px;
							}

							@media screen and (min-width: 800px) {
								img {
									bottom: 20px;
									top: auto;
								}
							}

							img:hover {
								opacity: 1;
							}

							""")
                            }
                        }
                    }
                    body {
                        h1 { +"Boahey Demo UI - Landing Page" }

                        if (environment.acceptsProfiles(Profiles.of("local", "it"))) {
                            div("box") {

                                div("title") {
                                    +"Local"
                                }

                                div("content") {
                                    groups.forEach { country ->
                                        div("button-group") {
                                            span { +"${country.third} ${country.second}" }
                                            br {}
                                            a("/exchange-rate/${country.first}", classes = "list-view") {
                                                +"${country.first}"
                                            }
                                        }
                                    }

                                    div("button-group") {
                                        a("/dependencies.html") {
                                            +"Dependencies"
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }


    @GetMapping("/{locale}/boahey-demo-list-cookie/{uuid}/{view}")
    fun createWishListCookie(
            @PathVariable locale: String,
            @PathVariable uuid: String,
            @PathVariable view: String,
            response: HttpServletResponse
    ): RedirectView {

//		val cookieValue = wishListCookieEncryptionService.encrypt(uuid)
//		val cookie = Cookie(wishListBackendConfig.wishListCookieName, cookieValue).apply {
//			path = "/"
//			maxAge = Duration.ofHours(24).seconds.toInt()
//		}
//		response.addCookie(cookie)

        return RedirectView("/boahey-demo/$locale/$view")
    }

}