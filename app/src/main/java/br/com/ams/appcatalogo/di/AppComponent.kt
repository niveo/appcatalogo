package br.com.ams.appcatalogo.di

import br.com.ams.appcatalogo.MainActivity
import br.com.ams.appcatalogo.catalogo.CatalogoActivity
import br.com.ams.appcatalogo.catalogo.CatalogoPaginaFragment
import br.com.ams.appcatalogo.catalogo.CatalogoProdutosCordenadasFragment
import br.com.ams.appcatalogo.catalogo.VisualizaImagemActivity
import br.com.ams.appcatalogo.produto.ProdutoListaFragment
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorker
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        RoomModule::class
    ]
)
interface AppComponent {
    fun inject(view: MainActivity?)

    fun inject(view: CatalogoActivity?)

    fun inject(view: AtualizarDadosServiceWorker?)

    fun inject(catalogoPaginaFragment: CatalogoPaginaFragment)

    fun inject(produtoListaFragment: ProdutoListaFragment)

    fun inject(visualizaImagemActivity: VisualizaImagemActivity)

    fun inject(catalogoProdutosCordenadasFragment: CatalogoProdutosCordenadasFragment)
}