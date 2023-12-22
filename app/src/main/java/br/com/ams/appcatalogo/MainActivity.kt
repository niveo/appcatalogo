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

        ApplicationLocate.component.inject(this)


        productDao.insertAll(Produto(1, "A", "B", 0.0))
        //productDao.getAll().observe(this, object : Observer<Produto> { })
    }

    fun teste() {
        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {

                    if (binding.imageView2.isReady) {

                        val sCoord: PointF =
                            binding.imageView2.viewToSourceCoord(e.x, e.y)!!


                    }

                    return true
                }

            })

        binding.imageView2.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(
                event
            )
        }

        TaskObserver.runInSingle(this, {
            ImageKit.getInstance()
                .url(
                    path = "1_zszxAdxQV",
                    transformationPosition = TransformationPosition.QUERY,
                    urlEndpoint = "https://ik.imagekit.io/aspofp9v1/catalogo/catalogos/993b612a-f8bd-4853-a769-00a377fb1720/paginas/"
                )
                .createWithPicasso()
                .error(R.drawable.ic_baseline_add_shopping_cart_24)
                .get()
        }, { result ->
            binding.imageView2.setImage(
                ImageSource.bitmap(
                    result!!
                )
            )
        }, { error ->
            LogUtils.e(error)
        })
    }


}