package br.com.ams.appcatalogo

import android.graphics.PointF
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.dao.ProdutoDao
import br.com.ams.appcatalogo.databinding.ActivityMainBinding
import br.com.ams.appcatalogo.entity.Produto
import br.com.ams.appcatalogo.repository.ProdutoRepository
import br.com.ams.appcatalogo.service.AtualizarDadosLocalService
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorker
import com.auth0.android.Auth0
import com.blankj.utilcode.util.LogUtils
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.imagekit.android.picasso_extension.createWithPicasso
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private lateinit var account: Auth0
    private lateinit var binding: ActivityMainBinding

    @Inject
    public lateinit var productDao: ProdutoRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApplicationLocate.component.inject(this)



        binding.button.setOnClickListener {
            AtualizarDadosServiceWorker.iniciar(this)
        }

        //productDao.insertAll(Produto(1, "A", "B", 0.0))
        //productDao.getAll().observe(this, object : Observer<Produto> { })
    }
}