package com.method.highpoint.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.method.highpoint.model.style.StylesModel
import com.method.highpoint.network.ApiClient
import com.method.highpoint.network.ApiInterface
import com.method.highpoint.network.MyMarketApiClient
import com.method.highpoint.network.MyMarketApiInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository(context: Context) {
    private var apiInterface: ApiInterface? = null
    private var myMarketApiInterface: MyMarketApiInterface? = null
    val isLoading = MutableLiveData<Boolean>()

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
        myMarketApiInterface = MyMarketApiClient.getApiClient(context).create(MyMarketApiInterface::class.java)
    }

    fun fetchExhibitorArea(): MutableLiveData<List<AreaModel>?> {
        val data = MutableLiveData<List<AreaModel>?>()

        apiInterface?.fetchExhibitorArea()?.enqueue(object : Callback<List<AreaModel>> {
            override fun onFailure(call: Call<List<AreaModel>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<List<AreaModel>>,
                response: Response<List<AreaModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }
        })
        return data
    }

    fun fetchBuildingName(): MutableLiveData<List<BuildingModel>?> {
        val data = MutableLiveData<List<BuildingModel>?>()

        apiInterface?.fetchBuildingName()?.enqueue(object : Callback<List<BuildingModel>> {
            override fun onResponse(
                call: Call<List<BuildingModel>>,
                response: Response<List<BuildingModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<BuildingModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchCategories(): MutableLiveData<List<CategoryModel>?> {
        val data = MutableLiveData<List<CategoryModel>?>()

        apiInterface?.fetchCategory()?.enqueue(object : Callback<List<CategoryModel>> {
            override fun onResponse(
                call: Call<List<CategoryModel>>,
                response: Response<List<CategoryModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchCountry(): MutableLiveData<List<CountryModel>?> {
        val data = MutableLiveData<List<CountryModel>?>()

        apiInterface?.fetchCountry()?.enqueue(object : Callback<List<CountryModel>> {
            override fun onResponse(
                call: Call<List<CountryModel>>,
                response: Response<List<CountryModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<CountryModel>>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun fetchDates(): MutableLiveData<DatesModel?> {
        val data = MutableLiveData<DatesModel?>()

        apiInterface?.fetchDates()?.enqueue(object : Callback<DatesModel> {
            override fun onResponse(
                call: Call<DatesModel>,
                response: Response<DatesModel>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<DatesModel>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun fetchEvents(): MutableLiveData<List<EventModel>?> {
        val data = MutableLiveData<List<EventModel>?>()

        apiInterface?.fetchEvents()?.enqueue(object : Callback<List<EventModel>> {
            override fun onResponse(
                call: Call<List<EventModel>>,
                response: Response<List<EventModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else {
                    data.value = null
                }
            }
            override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchExhibitorDetails(): MutableLiveData<List<ExhibitorModel>?> {
        val data = MutableLiveData<List<ExhibitorModel>?>()

        apiInterface?.fetchExhibitorDetails()?.enqueue(object : Callback<List<ExhibitorModel>> {
            override fun onResponse(
                call: Call<List<ExhibitorModel>>,
                response: Response<List<ExhibitorModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<ExhibitorModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchExhibitorFilters(): MutableLiveData<List<ExhibitorFilterModel>?> {
        val data = MutableLiveData<List<ExhibitorFilterModel>?>()

        apiInterface?.fetchExhibitorFilter()?.enqueue(object : Callback<List<ExhibitorFilterModel>> {
            override fun onResponse(
                call: Call<List<ExhibitorFilterModel>>,
                response: Response<List<ExhibitorFilterModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<ExhibitorFilterModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchExhibitorUpdates(): MutableLiveData<List<ExhibitorUpdateModel>?> {
        val data = MutableLiveData<List<ExhibitorUpdateModel>?>()

        apiInterface?.fetchExhibitorUpdates()?.enqueue(object : Callback<List<ExhibitorUpdateModel>> {
            override fun onResponse(
                call: Call<List<ExhibitorUpdateModel>>,
                response: Response<List<ExhibitorUpdateModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<ExhibitorUpdateModel>>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun fetchStyles(): MutableLiveData<List<StylesModel>?> {
        val data = MutableLiveData<List<StylesModel>?>()

        apiInterface?.fetchStyles()?.enqueue(object : Callback<List<StylesModel>> {
            override fun onResponse(
                call: Call<List<StylesModel>>,
                response: Response<List<StylesModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<StylesModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchPricePoints(): MutableLiveData<List<PricePointsModel>?> {
        val data = MutableLiveData<List<PricePointsModel>?>()

        apiInterface?.fetchPricePoints()?.enqueue(object : Callback<List<PricePointsModel>> {
            override fun onResponse(
                call: Call<List<PricePointsModel>>,
                response: Response<List<PricePointsModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<PricePointsModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchOptions(): MutableLiveData<List<OptionModel>?> {
        val data = MutableLiveData<List<OptionModel>?>()

        apiInterface?.fetchOptions()?.enqueue(object : Callback<List<OptionModel>> {
            override fun onResponse(
                call: Call<List<OptionModel>>,
                response: Response<List<OptionModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<OptionModel>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchMyMarket(userGuid: String): MutableLiveData<MyMarketResponse?> {
        val data = MutableLiveData<MyMarketResponse?>()

        myMarketApiInterface?.fetchMyMarket(userGuid)?.enqueue(object : Callback<MyMarketResponse> {
            override fun onResponse(
                call: Call<MyMarketResponse>,
                response: Response<MyMarketResponse>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<MyMarketResponse>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchShuttleDetails(): MutableLiveData<List<ShuttlesModel>?> {
        val data = MutableLiveData<List<ShuttlesModel>?>()

        apiInterface?.fetchShuttleDetails()?.enqueue(object : Callback<List<ShuttlesModel>> {
            override fun onResponse(
                call: Call<List<ShuttlesModel>>,
                response: Response<List<ShuttlesModel>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<ShuttlesModel>>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun fetchShuttleLineDetails(): MutableLiveData<List<ShuttlesLine>?> {
        val data = MutableLiveData<List<ShuttlesLine>?>()

        apiInterface?.fetchShuttleLineDetails()?.enqueue(object : Callback<List<ShuttlesLine>> {
            override fun onResponse(
                call: Call<List<ShuttlesLine>>,
                response: Response<List<ShuttlesLine>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<ShuttlesLine>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchMapLocations(): MutableLiveData<List<MapLocations>?> {
        val data = MutableLiveData<List<MapLocations>?>()
        apiInterface?.fetchMapLocations()?.enqueue(object : Callback<List<MapLocations>> {
            override fun onResponse(
                call: Call<List<MapLocations>>,
                response: Response<List<MapLocations>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<MapLocations>>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    fun fetchMyMapLocations(userGuid: String): MutableLiveData<List<MapLocations>?> {
        isLoading.value = true
        val data = MutableLiveData<List<MapLocations>?>()
        myMarketApiInterface?.fetchMyMapLocations(userGuid)?.enqueue(object : Callback<List<MapLocations>> {
            override fun onResponse(
                call: Call<List<MapLocations>>,
                response: Response<List<MapLocations>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                    isLoading.value = false
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<List<MapLocations>>, t: Throwable) {
                data.value = null
                isLoading.value = false
            }

        })
        return data
    }

     fun postMyMarketItem(myMarketStar: MyMarketStar, onResult: (MyMarketItem?) -> Unit) {
        val data = MutableLiveData<MyMarketItem>()

        apiInterface?.postMyMarketItem(myMarketStar)?.enqueue(object : Callback<MyMarketItem> {
            override fun onResponse(call: Call<MyMarketItem>, response: Response<MyMarketItem>) {
                val res = response.body()
                if (response.code() == 201 && res != null) {
                    data.value = res
                    onResult(res)
                } else
                    data.value = null
            }

            override fun onFailure(call: Call<MyMarketItem>, t: Throwable) {
                data.value = null
                onResult(null)
            }

        })
    }

    fun deleteMyMarket(userGuid: String, itemId: Int, onResult: (ResponseBody?) -> Unit) {
        val data = MutableLiveData<ResponseBody>()

        apiInterface?.deleteMyMarketItem(userGuid, itemId)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                    onResult(res)
                } else {
                    data.value = null
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                data.value = null
                onResult(null)
            }

        })
    }

    fun putMyMarketItem(userGuid: String, itemId: Int, myMarketStar: MyMarketStar, onResult: (ResponseBody?) -> Unit) {
        val data = MutableLiveData<ResponseBody>()
        apiInterface?.putMyMarketItem(userGuid, itemId, myMarketStar)?.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                    onResult(res)
                } else {
                    data.value = null
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                data.value = null
                onResult(null)
            }
        })
    }
}