package com.method.highpoint.model.signin

data class UserInfo(
    val access_token: String? = "",
    val capture_user: CaptureUser,
    val sso_code: Any,
    val stat: String? = null
)