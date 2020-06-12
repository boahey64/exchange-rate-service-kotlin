package com.boahey.exchangerateservice.config.togglz

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.togglz.core.Feature
import org.togglz.core.repository.FeatureState
import org.togglz.core.repository.StateRepository
import org.togglz.core.repository.cache.CachingStateRepository
import org.togglz.core.repository.jdbc.JDBCStateRepository
import org.togglz.core.repository.util.DefaultMapSerializer
import org.togglz.core.spi.FeatureProvider
import javax.sql.DataSource

@Configuration
@DependsOn("liquibase")
class ExchangeRateFeatureConfiguration(
		private val dataSource: DataSource
) {

	private val logger = KotlinLogging.logger { }


	fun stateRepository(): StateRepository {
		val repository = JDBCStateRepository.newBuilder(dataSource)
				.tableName("TOGGLZ")
				.createTable(false)
				.serializer(DefaultMapSerializer.singleline())
				.noCommit(true)
				.build()

		return CachingStateRepository(repository, 10000)
	}

	@Bean
	fun notifyingStateRepository(featureProvider: FeatureProvider): StateRepository {

		val delegate = stateRepository()

		return object : StateRepository {
			override fun getFeatureState(feature: Feature): FeatureState? {
				return delegate.getFeatureState(feature)
			}

			override fun setFeatureState(featureState: FeatureState) {
				delegate.setFeatureState(featureState)

				afterSetFeatureState(featureState)
			}

			private fun afterSetFeatureState(featureState: FeatureState) {
				val formattedFeatureState = formattedFeatureState(featureState)

					logger.trace { "set togglz feature: $formattedFeatureState" }
			}

			private fun formattedFeatureState(state: FeatureState): String =
					featureProvider.features.joinToString(
							separator = ""
					) { createFeatureString(it, state) }

			private fun createFeatureString(feature: Feature, featureState: FeatureState): String =
					if (featureState.feature == feature) createBoldString(feature) else createParagraph(feature)

			private fun createBoldString(feature: Feature): String =
					"<b>${createParagraph(feature)}</b>"

			private fun createParagraph(feature: Feature): String =
					"<p>$feature</p>"
//			"<p>${StringUtils.abbreviate(feature.name(), 30)}<br> => ${createFeatureStateString(feature)}</p>"

			private fun createFeatureStateString(feature: Feature): String {
				val featureState = getFeatureState(feature) ?: return "DEFAULT"

				val isEnabled = featureState.isEnabled
				return if (isEnabled) "ON, ${featureState.parameterMap}" else "OFF"
			}
		}
	}
}
