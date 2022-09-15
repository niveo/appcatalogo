package br.com.ams.appcatalogo.model
import br.com.ams.appcatalogo.entity.Catalogo
import java.util.*
import kotlin.collections.ArrayList

class CatalogoJson(
    val codigo: Long,
    val descricao: String?,
    val observacao: String?,
    val imagemUrl: String?,
    var dataCadastrado: Date? = null,
    var dataAlterado: Date? = null,
    var catalogoPaginas: ArrayList<CatalogoPaginaJson>? = null
)