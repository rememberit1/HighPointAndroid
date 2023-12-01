package com.method.highpoint.repository

import com.method.highpoint.BuildConfig
import com.method.highpoint.R
import com.method.highpoint.model.signin.ForgotPassword
import com.method.highpoint.model.signin.UserInfo
import com.method.highpoint.network.ApiInterface
import com.method.highpoint.network.JainRainApiClient
import kotlinx.coroutines.*

class JanRainApiRepository {
    private var apiInterface: ApiInterface? = null

    init {
        apiInterface = JainRainApiClient.getApiClient().create(ApiInterface::class.java)
    }

    suspend fun signInUser(userName: String?, password: String?) : UserInfo? = runBlocking {
        val params = HashMap<String?, String?>()
        params["client_id"] = "zz75czk4avyq9h4fejscq77r3xrfavmk"
        params["flow"] = "standard"
        params["flow_version"] = "acfda6ae-2579-449a-b87b-0121c6526e74"
        params["locale"] = "en-US"
        params["redirect_uri"] = "https://highpointmarket.janraincapture.com"
        params["response_type"] = "token"
        params["form"] = "signInForm"
        params["signInEmailAddress"] = "$userName"
        params["currentPassword"] = "$password"
            val response = apiInterface?.signInInfo(params)
            if (response?.isSuccessful == true) {
                return@runBlocking response.body()
            } else {
                return@runBlocking response?.body()
            }
    }

    suspend fun registerUser(
        emailAddress: String, newPassword: String, lastName: String,
        firstName: String, displayName: String = "$lastName $firstName"
        ) : UserInfo? = runBlocking {
        val params = HashMap<String?, String?>()
        params["client_id"] = "zz75czk4avyq9h4fejscq77r3xrfavmk"
        params["flow"] = "standard"
        params["flow_version"] = "acfda6ae-2579-449a-b87b-0121c6526e74"
        params["locale"] = "en-US"
        params["redirect_uri"] = "https://highpointmarket.janraincapture.com"
        params["response_type"] = "token"
        params["form"] = "registrationForm"
        params["emailAddress"] = emailAddress
        params["newPassword"] = newPassword
        params["newPasswordConfirm"] = newPassword
        params["lastName"] = lastName
        params["firstName"] = firstName
        params["displayName"] = displayName
        val response = apiInterface?.registrationInfo(params)
        if (response?.isSuccessful == true) {
            return@runBlocking response.body()
        } else {
            return@runBlocking response?.body()
        }
    }

    suspend fun forgotPassword(
        emailAddress: String
    ) : ForgotPassword? = runBlocking {
        val params = HashMap<String?, String?>()
        params["client_id"] = "zz75czk4avyq9h4fejscq77r3xrfavmk"
        params["flow"] = "standard"
        params["flow_version"] = "acfda6ae-2579-449a-b87b-0121c6526e74"
        params["locale"] = "en-US"
        params["redirect_uri"] = "https://highpointmarket.janraincapture.com"
        params["response_type"] = "token"
        params["form"] = "forgotPasswordForm"
        params["signInEmailAddress"] = emailAddress
        val response = apiInterface?.forgotPasswordInfo(params)
        if (response?.isSuccessful == true) {
            return@runBlocking response.body()
        } else {
            return@runBlocking response?.body()
        }
    }
}