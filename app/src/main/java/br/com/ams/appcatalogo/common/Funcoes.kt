package br.com.ams.appcatalogo.common

import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import java.net.ConnectException
import java.net.SocketTimeoutException

object Funcoes {

    fun alertaThrowable(error: Throwable, toast: Boolean? = false) {
        LogUtils.e("alertaThrowable", error)
        LogUtils.e("Instance Error Class", error.javaClass)
        try {
            if (error.cause !== null && error.cause is ConnectException) {
                if (toast == true)
                    ToastUtils.showLong(error.cause!!.message)
            } else {
                if (error.cause !== null && error.cause is SocketTimeoutException) {
                    if (toast == true)
                        ToastUtils.showLong(error.cause!!.message)
                } else {
                    if (error is CustomException) {
                        if (toast == true)
                            ToastUtils.showLong(error.message)
                    } else {
                        try {
                            val message =
                                GsonUtils.fromJson(
                                    error.message,
                                    ErrorMessage::class.java
                                )
                            LogUtils.e(message?.toString())
                            message?.developerMessage?.let {
                                if (toast == true)
                                    ToastUtils.showLong(it)
                            }
                        } catch (e: Exception) {
                            if (toast == true)
                                ToastUtils.showLong("Erro no processo.")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            LogUtils.e(e)
            ToastUtils.showLong("Erro no processo.")
        }
    }

    fun configurarRecyclerDefault(
        context: Context,
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        divider: Boolean
    ) {
        configurarRecyclerDefaultConfig(context, recyclerView, adapter, divider)
    }

    fun configurarRecyclerDefault(
        context: Context,
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        configurarRecyclerDefaultConfig(context, recyclerView, adapter, true)
    }

    private fun configurarRecyclerDefaultConfig(
        context: Context,
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        divider: Boolean
    ) {
        val linearLayoutManager =
            LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        if (divider) recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

}