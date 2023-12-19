package br.com.ams.appcatalogo.service

import android.content.Context
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.CustomException
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.entity.CatalogoPagina
import br.com.ams.appcatalogo.entity.CatalogoPaginaProduto
import br.com.ams.appcatalogo.entity.Produto
import br.com.ams.appcatalogo.retrofit.RetrofitConfig
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class AtualizarDadosLocalService {
    fun iniciar(context: Context, onSucesso: () -> Unit, onError: (e: Throwable) -> Unit) {

        if (!NetworkUtils.isConnected()) {
            onError(CustomException(context.getString(R.string.erro_conexao_indisponivel)))
            return
        }

        val produtoDao = ApplicationLocate.instance.dataBase.produtoDao()
        val catalogoPaginaDao = ApplicationLocate.instance.dataBase.catalogoPaginaDao()
        val catalogoDao = ApplicationLocate.instance.dataBase.catalogoDao()
        val catalogoPaginaProdutoDao =
            ApplicationLocate.instance.dataBase.catalogoPaginaProdutoDao()

        val comp1 = RetrofitConfig.instance.getProdutoService().obterTodos()
            .flatMapCompletable {
                val newProduto = ArrayList<Produto>()
                it.forEach {
                    newProduto.add(
                        Produto(
                            it.codigo,
                            it.referencia,
                            it.descricao,
                            it.dataCadastrado,
                            it.dataAlterado,
                            it.valor
                        )
                    )
                }
                produtoDao.insertAll(*newProduto.toTypedArray())
                Completable.complete()
            }

        val comp2 = RetrofitConfig.instance.getCatalogoService().obterTodos()
            .flatMapCompletable {
                val newCatalogo = ArrayList<Catalogo>()
                val newCatalogoPagina = ArrayList<CatalogoPagina>()
                val newCatalogoPaginaProdito = ArrayList<CatalogoPaginaProduto>()
                it.forEach { catalogo ->
                    newCatalogo.add(
                        Catalogo(
                            catalogo.codigo,
                            catalogo.descricao,
                            catalogo.observacao,
                            catalogo.imagemUrl,
                            catalogo.dataCadastrado,
                            catalogo.dataAlterado
                        )
                    )
                    catalogo.paginas?.forEach { catalogoPagina ->
                        LogUtils.d(catalogoPagina.pagina)
                        newCatalogoPagina.add(
                            CatalogoPagina(
                                catalogoPagina.codigo,
                                catalogoPagina.pagina,
                                catalogo.codigo,
                                catalogoPagina.dataAlterado
                            )
                        )
                        catalogoPagina.mapeados?.forEach { catalogoPaginaProduto ->
                            newCatalogoPaginaProdito.add(
                                CatalogoPaginaProduto(
                                    catalogoPaginaProduto.codigo,
                                    catalogoPagina.codigo,
                                    catalogoPaginaProduto.produto.codigo,
                                    catalogoPaginaProduto.inicialPosicalX,
                                    catalogoPaginaProduto.finalPosicalX,
                                    catalogoPaginaProduto.inicialPosicalY,
                                    catalogoPaginaProduto.finalPosicalY,
                                    catalogoPaginaProduto.width,
                                    catalogoPaginaProduto.height
                                )
                            )
                        }
                    }
                }

                catalogoDao.insertAll(*newCatalogo.toTypedArray())
                catalogoPaginaDao.insertAll(*newCatalogoPagina.toTypedArray())
                catalogoPaginaProdutoDao.insertAll(*newCatalogoPaginaProdito.toTypedArray())
                Completable.complete()
            }

        Completable.concatArray(comp1, comp2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSucesso()
            }, {
                LogUtils.e(it)
                onError(it)
            })
    }
}