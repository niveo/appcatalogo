package br.com.ams.appcatalogo.retrofit

import android.content.Context
import br.com.ams.appcatalogo.common.Constantes
import com.blankj.utilcode.util.SPUtils
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = "Bearer " + SPUtils.getInstance().getString(Constantes.KEY_TOKEN_BEARER)
        val userId = SPUtils.getInstance().getString(Constantes.KEY_TOKEN_USER_ID)
        requestBuilder.addHeader("Authorization", token)
        requestBuilder.addHeader("userid", userId)
        return chain.proceed(requestBuilder.build())
    }
}