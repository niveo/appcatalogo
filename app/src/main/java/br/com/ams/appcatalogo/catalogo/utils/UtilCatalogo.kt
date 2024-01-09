package br.com.ams.appcatalogo.catalogo.utils

import androidx.core.os.bundleOf
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.model.bus.MessageBusIdentificador
import org.greenrobot.eventbus.EventBus

object UtilCatalogo {

    fun listarProdutosDoCatalogo(catalogoId: Long, catalogoIdPagina: Long? = null) {
        EventBus.getDefault().post(
            MessageBusIdentificador(
                Constantes.NT_CONSULTA_PRODUTO, bundleOf(
                    "CATAOGO_ID" to catalogoId,
                    "CATAOGO_PAGINA_ID" to catalogoIdPagina,
                )
            )
        )
    }
}