package com.method.highpoint.network

import com.method.highpoint.model.area.AreaModel
import com.method.highpoint.model.building.BuildingModel
import com.method.highpoint.model.category.CategoryModel
import com.method.highpoint.model.country.CountryModel
import com.method.highpoint.model.dates.DatesModel
import com.method.highpoint.model.event.EventModel
import com.method.highpoint.model.exhibitor.ExhibitorFilterModel
import com.method.highpoint.model.exhibitor.ExhibitorModel
import com.method.highpoint.model.exhibitor.ExhibitorUpdateModel
import com.method.highpoint.model.mymarket.MyMarketItem
import com.method.highpoint.model.maps.MapLocations
import com.method.highpoint.model.shuttle.ShuttlesModel
import com.method.highpoint.model.mymarket.MyMarketResponse
import com.method.highpoint.model.mymarket.MyMarketStar
import com.method.highpoint.model.options.OptionModel
import com.method.highpoint.model.pricepoint.PricePointsModel
import com.method.highpoint.model.shuttle.ShuttlesLine
import com.method.highpoint.model.signin.ForgotPassword
import com.method.highpoint.model.signin.UserInfo
import com.method.highpoint.model.style.StylesModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @GET("Areas")
    fun fetchExhibitorArea(): Call<List<AreaModel>>

    @GET("Buildings")
    fun fetchBuildingName(): Call<List<BuildingModel>>

    @GET("Categories")
    fun fetchCategory(): Call<List<CategoryModel>>

    @GET("Countries")
    fun fetchCountry(): Call<List<CountryModel>>

    @GET("Dates")
    fun fetchDates(): Call<DatesModel>

    @GET("Events")
    fun fetchEvents(): Call<List<EventModel>>

    @GET("Exhibitors")
    fun fetchExhibitorDetails(): Call<List<ExhibitorModel>>

    @GET("Exhibitors/Filters")
    fun fetchExhibitorFilter(): Call<List<ExhibitorFilterModel>>

    @GET("Exhibitors/Updates")
    fun fetchExhibitorUpdates(): Call<List<ExhibitorUpdateModel>>

    @GET("Styles")
    fun fetchStyles(): Call<List<StylesModel>>

    @GET("PricePoints")
    fun fetchPricePoints(): Call<List<PricePointsModel>>

    @GET("Options")
    fun fetchOptions(): Call<List<OptionModel>>

    @GET("Shuttles/Stops")
    fun fetchShuttleDetails(): Call<List<ShuttlesModel>>

    @GET("Shuttles/Lines")
    fun fetchShuttleLineDetails(): Call<List<ShuttlesLine>>

    @GET("MapLocations")
    fun fetchMapLocations(): Call<List<MapLocations>>

//    @GET("/v1-HPM/MapLocations/{UserGuid}")
//    fun fetchMyMapLocations(
//        @Path(value = "UserGuid", encoded = true) userGuid: String
//    ): Call<List<MapLocations>>

    @FormUrlEncoded
    @POST("oauth/auth_native_traditional")
    suspend fun signInInfo(@FieldMap params: HashMap<String?, String?>): Response<UserInfo>

    @FormUrlEncoded
    @POST("/oauth/register_native_traditional")
    suspend fun registrationInfo(@FieldMap params: HashMap<String?, String?>): Response<UserInfo>

    @FormUrlEncoded
    @POST("/oauth/forgot_password_native")
    suspend fun forgotPasswordInfo(@FieldMap params: HashMap<String?, String?>): Response<ForgotPassword>

//    @GET("/v1-HPM/MyMarket/{UserGuid}")
//    fun fetchMyMarket(
//        @Path(value = "UserGuid", encoded = true) userGuid: String
//    ) : Call<MyMarketResponse>

    @POST("/v1-HPM/MyMarket")
    fun postMyMarketItem(
        @Body myMarketStar: MyMarketStar
    ) : Call<MyMarketItem>

    @DELETE("/v1-HPM/MyMarket/{UserGuid}/{ItemId}")
    fun deleteMyMarketItem(
        @Path(value = "UserGuid") userGuid: String,
        @Path(value = "ItemId") itemId: Int
    ) : Call<ResponseBody>

    @PUT("/v1-HPM/MyMarket/{UserGuid}/{ItemId}")
    fun putMyMarketItem(
        @Path(value = "UserGuid") userGuid: String,
        @Path(value = "ItemId") itemId: Int,
        @Body myMarketStar: MyMarketStar
    ) : Call<ResponseBody>
}