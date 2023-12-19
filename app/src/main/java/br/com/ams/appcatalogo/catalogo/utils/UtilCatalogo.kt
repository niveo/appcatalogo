package br.com.ams.appcatalogo.catalogo.utils

import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.DateTimeUtil
import br.com.ams.appcatalogo.model.CatalogoPaginaMapeadosDTO
import java.io.File

object UtilCatalogo {

    fun arquivoCatalogo(it: CatalogoPaginaMapeadosDTO): File {
        val dataFormat = DateTimeUtil.dataPatterBR(it.dataAlterado, "yyyyMMddHHmmss")
        return File(
            Config.PATH_AMS_CATALOGO + Config.FILE_SEP + it.codigoCatalogo + Config.FILE_SEP
                    + it.codigo + "_" + dataFormat
        )
    }
}