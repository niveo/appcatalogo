package br.com.ams.appcatalogo.viewsmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.ams.appcatalogo.repository.CatalogoRepository

class CatalogoViewModelFactory(private val context: Context, private val repository: CatalogoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogoViewModel(context, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}