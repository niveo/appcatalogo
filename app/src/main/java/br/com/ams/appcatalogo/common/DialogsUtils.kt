package br.com.ams.appcatalogo.common

import android.R
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.LogUtils

object DialogsUtils {
    fun showConfirmacao(
        context: Context,
        title: String?,
        mensagem: String,
        listener: (retorno: Boolean) -> Unit
    ) {
        showConfirmacaoBuilder(context, title, mensagem, listener)
    }

    private fun showConfirmacaoBuilder(
        context: Context,
        title: String?,
        mensagem: String,
        listener: (retorno: Boolean) -> Unit
    ) {
        showConfirmacaoBuilder(context, title, mensagem, "Sim", "Não", listener)
    }

    fun showConfirmacaoBuilder(
        context: Context,
        title: String?,
        mensagem: String,
        btnPositivo: String,
        btnNegativo: String,
        listener: (retorno: Boolean) -> Unit
    ) {
        try {
            val builder =
                AlertDialog.Builder(context)
                    .setMessage(mensagem)
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setPositiveButton(
                        btnPositivo
                    ) { dialog: DialogInterface, _: Int ->
                        listener(true)
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        btnNegativo
                    ) { dialog: DialogInterface, _: Int ->
                        listener(false)
                        dialog.dismiss()
                    }
            if (title != null) {
                builder.setTitle(title)
            }
            builder.show()
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }
}