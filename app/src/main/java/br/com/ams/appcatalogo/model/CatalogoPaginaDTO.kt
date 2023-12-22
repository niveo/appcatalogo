package br.com.ams.appcatalogo.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

class CatalogoPaginaDTO(
    var id: Long,
    var catalogoId: Long?,
    var pagina: Int?,
    var size: Int?,
    var height: Int?,
    var width: Int?,
    var name: String?,
    //val mapeamentos: ArrayList<CatalogoPaginaMapeadosDTO>? = null
)