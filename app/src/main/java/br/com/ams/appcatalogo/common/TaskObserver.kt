package br.com.ams.appcatalogo.common

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class NullableObject<T>(val value: T?)

object TaskObserver {

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
                    callback(result.value)
                }
            }
    }

    fun <T> T.toNullableObject(): NullableObject<T> = NullableObject(this)
}