package br.com.ams.appcatalogo.model
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

class CatalogoDTO(
    var id: Long,
    var descricao: String?,
    var titulo: String?,
    var logo: String?,
    var avatar: String?,
    //var paginas: ArrayList<CatalogoPaginaDTO>? = null
)