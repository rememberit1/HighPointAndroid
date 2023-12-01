package com.method.highpoint.model.mymarket

data class MyMarketResponse(
	val exhibitors: List<ExhibitorsItem?>? = null,
	val userGuid: String? = null,
	val events: List<EventsItem?>? = null
)
