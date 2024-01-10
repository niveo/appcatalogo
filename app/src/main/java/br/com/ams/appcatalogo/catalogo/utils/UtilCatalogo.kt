package br.com.ams.appcatalogo.catalogo.utils

import androidx.core.os.bundleOf
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.model.bus.MessageBusIdentificador
import org.greenrobot.eventbus.EventBus

object UtilCatalogo {

    fun listarProdutosDoCatalogo(catalogoId: Long, catalogoIdPagina: Long? = null) {
        val msg = if (catalogoIdPagina != null) {
            MessageBusIdentificador(
                Constantes.NT_CONSULTA_PRODUTO, bundleOf(
                    Constantes.CATALOGO_ID to catalogoId,
                    Constantes.CATALOGO_PAGINA_ID to catalogoIdPagina,
                )
            )
        } else {
            MessageBusIdentificador(
                Constantes.NT_CONSULTA_PRODUTO, bundleOf(
                    Constantes.CATALOGO_ID to catalogoId
                )
            )
        }
        EventBus.getDefault().post(msg)
    }
}