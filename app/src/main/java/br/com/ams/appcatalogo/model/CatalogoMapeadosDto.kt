package br.com.ams.appcatalogo.model

import androidx.room.Ignore
import java.util.*

  class CatalogoMapeadosDto {
    var codigo: Long? = null
    var descricao: String? = null
    var observacao: String? = null
    var imagemUrl: String? = null
    var dataAlterado: Date? = null
    var mapeados: Int? = null
    @Ignore
    var progress: Boolean = false
}