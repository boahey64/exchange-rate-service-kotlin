package com.boahey.exchangerateservice.config.togglz;

import org.togglz.core.Feature;
import org.togglz.core.context.FeatureContext;

public enum ExchangeRateFeature implements Feature {

	DISABLE_SPRING_HEADERS,
	ENABLE_ANIMATED_SEARCH;

	public boolean isActive() {
		return FeatureContext.getFeatureManager().isActive(this);
	}
}
