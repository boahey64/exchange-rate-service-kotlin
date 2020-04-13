package com.boahey.exchangerateservice.testdata

fun aValidOffer(withTitle: String = "validTitle",
				withPrice: Int = 2000,
				withMerchantInfo: Pair<String?, String?> = Pair(null, null),
				withShopId: Long = 666) =
			null

fun aValidOfferWithoutShippingCosts(withTitle: String = "validTitle",
				withPrice: Int = 2000,
				withMerchantInfo: Pair<String?, String?> = Pair(null, null),
				withShopId: Long = 666) =
			null

fun aShopAttribute(withFreeReturnDays: Int? = null,
				   withFreeReturnLimit: Long? = null,
				   withName: String = "anyName") =
			null
