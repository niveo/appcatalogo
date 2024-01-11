package br.com.ams.appcatalogo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.ams.appcatalogo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}