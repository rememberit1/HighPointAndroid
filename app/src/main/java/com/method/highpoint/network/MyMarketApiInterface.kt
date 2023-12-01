package com.method.highpoint.network

import com.method.highpoint.model.maps.MapLocations
import com.method.highpoint.model.mymarket.MyMarketResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyMarketApiInterface {

    @GET("/v1-HPM/MyMarket/{UserGuid}")
    fun fetchMyMarket(
        @Path(value = "UserGuid", encoded = true) userGuid: String
    ) : Call<MyMarketResponse>

    @GET("/v1-HPM/MapLocations/{UserGuid}")
    fun fetchMyMapLocations(
        @Path(value = "UserGuid", encoded = true) userGuid: String
    ): Call<List<MapLocations>>
}