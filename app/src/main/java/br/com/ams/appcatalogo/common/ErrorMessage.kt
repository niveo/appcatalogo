package br.com.ams.appcatalogo.common

import java.io.Serializable

data class ErrorMessage(
    var status: Int = 0,

    var message: String = "",

    var developerMessage: String = "",

//@field:SerializedName("timestamp")
//var timestamp: Date? = null
) : Serializable