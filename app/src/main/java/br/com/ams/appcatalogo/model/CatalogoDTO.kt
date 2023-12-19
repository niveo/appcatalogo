package br.com.ams.appcatalogo.model
import java.util.*
import kotlin.collections.ArrayList

class CatalogoDTO(
    val codigo: Long,
    val descricao: String?,
    val observacao: String?,
    val imagemUrl: String?,
    var dataCadastrado: Date? = null,
    var dataAlterado: Date? = null,
    var paginas: ArrayList<CatalogoPaginaDTO>? = null
)