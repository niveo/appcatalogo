package br.com.ams.appcatalogo.common

import android.content.Context
import androidx.appcompat.app.AlertDialog
import br.com.ams.appcatalogo.R


class ProgressDialogUtil(val context: Context?) {

    private var dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context!!)
        builder.setCancelable(true)
        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

}