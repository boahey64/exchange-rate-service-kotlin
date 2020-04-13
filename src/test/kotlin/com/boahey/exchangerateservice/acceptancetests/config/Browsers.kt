package com.boahey.exchangerateservice.acceptancetests.config

import io.github.bonigarcia.wdm.WebDriverManager
import mu.KotlinLogging
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.logging.LoggingPreferences
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.safari.SafariOptions
import java.io.File
import java.util.logging.Level

private fun capabilities(): DesiredCapabilities {
	val logPrefs = LoggingPreferences().apply {
		enable(LogType.BROWSER, Level.ALL)
	}

	return DesiredCapabilities().apply {
		isJavascriptEnabled = true
		setCapability(CapabilityType.LOGGING_PREFS, logPrefs)
	}
}

fun safari(): WebDriver {
	return SafariDriver(SafariOptions(capabilities()))
}

fun firefox(): WebDriver {
	WebDriverManager.firefoxdriver().setup()

	return FirefoxDriver(FirefoxOptions(capabilities()))
}

private fun chromeOptions() =
		ChromeOptions()
				.merge(capabilities())
				.addArguments("--dns-prefetch-disable")

fun chrome(): WebDriver {
	WebDriverManager.chromedriver().version("2.40").setup()

	return ChromeDriver(chromeOptions())
}

fun chromeHeadless(): WebDriver {
	// use chrome driver version that fits to chrome version on CI
	// see: http://chromedriver.chromium.org/downloads
	WebDriverManager.chromedriver().version("2.40").setup()

	val options = chromeOptions().setHeadless(true)
	val chromeBinary = System.getProperty("chrome.binary")

	chromeBinary?.let {
		if (it != "auto") {
			if (!File(it).canExecute()) throw IllegalArgumentException("wrong configuration for chrome binary $it")

			TestConfig.logger.info { "setting chrome binary path -> $it" }
			options.setBinary(it)
		}
	}

	return ChromeDriver(options)
}

object TestConfig {
	val logger = KotlinLogging.logger {}
	val browser: String = System.getProperty("browser", "chrome-headless")
	val timeout: Long = Integer.getInteger("page_load_timeout", 15).toLong()
}
