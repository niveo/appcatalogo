package br.com.ams.appcatalogo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.ams.appcatalogo.ui.theme.CatalogoApplicationTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalogoApplicationTheme {
                Greeting("OIS23")
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text (text = "Hello $name!")
    }
}