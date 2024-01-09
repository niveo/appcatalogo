package br.com.ams.appcatalogo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.ams.appcatalogo.databinding.ActivityMainBinding
import br.com.ams.appcatalogo.repository.CatalogoRepository
import br.com.ams.appcatalogo.repository.ProdutoRepository
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorker
import br.com.ams.appcatalogo.viewsmodel.CatalogoViewModel
import br.com.ams.appcatalogo.viewsmodel.CatalogoViewModelFactory
import com.blankj.utilcode.util.LogUtils
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var catalogoRepository: CatalogoRepository

    var viewModel: CatalogoViewModel = ViewModelProvider(
        this,
        CatalogoViewModelFactory(this, catalogoRepository)
    ).get(CatalogoViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApplicationLocate.component.inject(this)

        binding.button.setOnClickListener {


            AtualizarDadosServiceWorker.iniciar(this)


        }
    }
}