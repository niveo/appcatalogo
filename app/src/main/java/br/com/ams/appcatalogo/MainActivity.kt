package br.com.ams.appcatalogo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.databinding.ActivityMainBinding
import br.com.ams.appcatalogo.repository.CatalogoRepository
import br.com.ams.appcatalogo.repository.ProdutoRepository
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorker
import br.com.ams.appcatalogo.viewsmodel.CatalogoViewModel
import br.com.ams.appcatalogo.viewsmodel.CatalogoViewModelFactory
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var produtoRepository: ProdutoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApplicationLocate.component.inject(this)

        binding.button.setOnClickListener {

        }

        val ls = arrayListOf<Long>(3L)
        ls.add(3)
        ls.add(84)
        ls.add(85)
        carregarTransacao(ls.toLongArray())
    }

    fun carregarTransacao(produtos: LongArray) {
        LogUtils.w(produtos)
        TaskObserver.runInSingle(this, {
            produtoRepository.obterProdutoCodigos(produtos.asList())
        }, {
           LogUtils.w(it)
        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }

}