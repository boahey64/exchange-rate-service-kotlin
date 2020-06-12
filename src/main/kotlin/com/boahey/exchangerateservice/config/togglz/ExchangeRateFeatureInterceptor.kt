package com.boahey.exchangerateservice.config.togglz

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.togglz.core.Feature
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ExchangeRateFeatureInterceptor : HandlerInterceptorAdapter() {

	val logger = KotlinLogging.logger {}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		val features = request.getHeaders(headerName).toList()
				.flatMap {
					it.split("\\s*,\\s*".toRegex()) }
				.toSet()

		features.let {
			if (!it.isEmpty()) {
				enabledFeatures.set(it)
				logger.info { "dynamically enabling Features $it" }
			}
		}

		return super.preHandle(request, response, handler)
	}

	override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
		enabledFeatures.remove()
	}

	companion object {
		const val headerName = "X-HANNIBAL-FEATURE"

		val enabledFeatures: ThreadLocal<Set<String>> = ThreadLocal.withInitial {
			emptySet<String>()
		}

		fun isFeatureActive(feature: Feature) =
				enabledFeatures.get().any { feature.name() == it }
	}
}
