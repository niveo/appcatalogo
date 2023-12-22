package br.com.ams.appcatalogo.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

class CatalogoPaginaMapeadosDTO {
    var id: Long? = null
    var catalogoPaginaId: Long? = null
    var inicialPosicalX: Float? = null
    var finalPosicalX: Float? = null
    var inicialPosicalY: Float? = null
    var finalPosicalY: Float? = null
    var width: Float? = null
    var height: Float? = null
    //var produtos: ArrayList<ProdutoDTO>? = null
}