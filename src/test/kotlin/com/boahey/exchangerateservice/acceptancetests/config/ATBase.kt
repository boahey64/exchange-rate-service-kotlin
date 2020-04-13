package com.boahey.exchangerateservice.acceptancetests.config

//import org.togglz.junit.TogglzRule
import com.boahey.exchangerateservice.ExchangeRateServiceKotlinApplication
import mu.KotlinLogging
import org.fluentlenium.adapter.junit.FluentTest
import org.fluentlenium.configuration.ConfigurationProperties
import org.fluentlenium.core.domain.FluentList
import org.fluentlenium.core.domain.FluentWebElement
import org.fluentlenium.core.search.SearchFilter
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.openqa.selenium.*
import org.openqa.selenium.support.events.EventFiringWebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import java.util.NoSuchElementException
import java.util.concurrent.TimeUnit


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ExchangeRateServiceKotlinApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

abstract class ATBase : FluentTest() {

	@LocalServerPort
	var port: Int = 0

	val logger = KotlinLogging.logger {}

	override fun newWebDriver(): WebDriver {

		logger.info { "using WebDriver ${TestConfig.browser}" }

		val driver = when (TestConfig.browser) {
			"firefox" -> firefox()
			"chrome" -> chrome()
			"chrome-headless" -> chromeHeadless()
			"safari" -> safari()
			else -> chromeHeadless()
		}

		driver.manage().timeouts().pageLoadTimeout(TestConfig.timeout, TimeUnit.SECONDS)
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
		driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS)
		driver.manage().window().size = Dimension(1400, 900)

		return EventFiringWebDriver(driver)
	}

	@BeforeEach
	fun setUp() {
		screenshotMode = ConfigurationProperties.TriggerMode.AUTOMATIC_ON_FAIL
		screenshotPath = "build/screenshots"
		awaitAtMost = 30_000

//		events().beforeClickOn { element, _ ->
//			executeScript("arguments[0].style.border = '3px solid red'; ", element.element)
//		}

//		events().beforeNavigateTo { url, _ ->
//			logger.info { "open URL $url" }
//		}

//		events().afterNavigateTo { url, _ ->
//			logger.info { "opened URL $url" }
//			logger.info { "window title: ${window().title()}" }
//		}
	}

	@get:Rule
	val logTestProgress = object : TestWatcher() {

		val ANSI_RESET = "\u001B[0m"
		val ANSI_RED = "\u001B[31m"
		val ANSI_GREEN = "\u001B[32m"
		val ANSI_BLUE = "\u001B[34m"

		fun color(string: String, ansi: String) =
				"$ansi$string$ANSI_RESET"

		override fun starting(description: Description) {
			println(color(""">>> starting test! ${description.displayName}""", ANSI_BLUE))
		}

		override fun failed(e: Throwable?, description: Description) {
			println(color("""<<< test failed! ${description.displayName}""", ANSI_RED))
		}

		override fun succeeded(description: Description) {
			println(color("""<<< test succeeded! ${description.displayName}""", ANSI_GREEN))
		}
	}

	fun jq(selector: String, vararg filter: SearchFilter): FluentList<FluentWebElement> = `$`(selector, *filter)

	fun navigateToExchangeRateRate() {
		goTo("http://localhost:$port/exchange-rate/rate")
	}
}


