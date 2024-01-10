package br.com.ams.appcatalogo.common

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.SPUtils

class Config {

    companion object {
        val PATH_LOCAL: String
        val FILE_SEP: String = System.getProperty("file.separator")!!
        val LINE_SEP: String = System.getProperty("line.separator")!!
        var URL_SERVIDOR: String

        init {
            var pah = PathUtils.getExternalAppFilesPath()
            if (pah == null || pah.equals("")) {
                pah = PathUtils.getExternalAppFilesPath()
            }
            PATH_LOCAL = pah + FILE_SEP

            URL_SERVIDOR = Constantes.URL_SERVIDOR
        }
    }
}