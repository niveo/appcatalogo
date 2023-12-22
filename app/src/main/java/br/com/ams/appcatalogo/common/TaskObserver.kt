package br.com.ams.appcatalogo.common

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NullableObject<T>(val value: T?)

object TaskObserver {


    inline fun <T> runInSingle3(
        context: Context,
        crossinline block: () -> T?,
        crossinline callback: (T?) -> Unit,
        progress: Boolean = false,
        dsp: CoroutineDispatcher = Dispatchers.IO
    ) {
        runInSingle3(context, block, callback, {}, {}, progress, dsp)
    }

    inline fun <T> runInSingle3(
        context: Context,
        crossinline block: () -> T?,
        crossinline callback: (T?) -> Unit,
        crossinline onError: (Throwable) -> Unit?,
        progress: Boolean = false,
        dsp: CoroutineDispatcher = Dispatchers.IO
    ) {
        runInSingle3(context, block, callback, onError, {}, progress, dsp)
    }


    inline fun <T> runInSingle3(
        context: Context,
        crossinline block: () -> T?,
        crossinline callback: (T?) -> Unit,
        crossinline onError: (Throwable) -> Unit?,
        crossinline onFinally: () -> Unit?,
        dsp: CoroutineDispatcher = Dispatchers.IO
    ) {
        runInSingle3(context, block, callback, onError, onFinally, false, dsp)
    }


    inline fun <T> runInSingle3(
        context: Context,
        crossinline block: () -> T?,
        crossinline callback: (T?) -> Unit,
        crossinline onError: (Throwable) -> Unit?,
        crossinline onFinally: () -> Unit?,
        progress: Boolean = false,
        dsp: CoroutineDispatcher = Dispatchers.IO
    ) {
        var dspIn = dsp
        if (progress) {
            dspIn = Dispatchers.Main
        }
        GlobalScope.launch(dspIn) {
            var view: ProgressDialogUtil? = null
            if (progress) {
                view = ProgressDialogUtil(context)
            }
            try {
                view?.show()
                callback(block())
            } catch (e: Exception) {
                onError(e)
            } finally {
                view?.dismiss()
                onFinally()
            }
        }
    }

    suspend inline fun <T> runInSingle2(
        context: Context,
        crossinline block: () -> T?,
        progress: Boolean = false
    ): T? {
        return suspendCoroutine {
            var view: ProgressDialogUtil? = null
            if (progress) {
                view = ProgressDialogUtil(context)
            }
            try {
                view?.show()
                it.resume(block())
            } catch (e: Exception) {
                it.resumeWithException(e)
            } finally {
                view?.dismiss()
            }
        }
    }

    inline fun <T> runInSingle(
        context: Context,
        crossinline block: () -> T,
        crossinline callback: (T?) -> Unit,
        crossinline onError: (Throwable) -> Unit,
        progress: Boolean = false
    ) {

        var view: ProgressDialogUtil? = null
        if (progress) {
            view = ProgressDialogUtil(context)
        }

        Single.fromCallable {
            return@fromCallable block().toNullableObject()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view?.show()
            }
            .doOnError {
                LogUtils.e(it)
                view?.dismiss()
            }
            .doFinally {
                view?.dismiss()
            }
            .doOnDispose {
                view?.dismiss()
            }
            .doOnTerminate {
                view?.dismiss()
            }
            .doAfterTerminate {
                view?.dismiss()
            }
            .subscribe { result, error ->
                if (error != null) {
                    onError(error)
                } else {
                    callback(result?.value)
                }
            }
    }

    fun <T> T.toNullableObject(): NullableObject<T> = NullableObject(this)
}