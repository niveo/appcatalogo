package br.com.ams.appcatalogo.catalogo.dataadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.databinding.CatalogoPaginaDataAdapterBinding
import br.com.ams.appcatalogo.entity.CatalogoPagina
import com.example.imagekit.android.picasso_extension.createWithPicasso
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import io.github.cdimascio.dotenv.dotenv

class CatalogoPaginaDataAdapter(
    private val identificador: String,
    private val onItemTouchListener: OnItemTouchListener
) :
    RecyclerView.Adapter<CatalogoPaginaDataAdapter.ViewHolder>() {
    private var registros: List<CatalogoPagina>? = null

    fun carregarRegistros(registros: List<CatalogoPagina>?) {
        this.registros = registros
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val binding = CatalogoPaginaDataAdapterBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: CatalogoPaginaDataAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return if (registros == null) 0 else registros!!.size
    }

    fun obterRegistro(position: Int): CatalogoPagina {
        return this.registros!![position]
    }

    interface OnItemTouchListener {
        fun onDetalhar(view: View, position: Int)
        fun onMenu(v: View?, absoluteAdapterPosition: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(registros!![position]) {
                binding.cardviewcatalogopaginaPagina.text = pagina.toString()
                ImageKit.getInstance()
                    .url(
                        path = this.name!!,
                        transformationPosition = TransformationPosition.QUERY,
                        urlEndpoint = "${ApplicationLocate.instance.dotenv[Constantes.IMAGEKIT_ENDPOINT]}/catalogo/catalogos/${identificador}/paginas/"
                    )
                    .createWithPicasso()
                    .into(binding.cardviewcatalogopaginaImg)
            }
            binding.cardviewcatalogopaginaMenu.setOnClickListener {
                onItemTouchListener.onMenu(
                    it,
                    adapterPosition
                )
            }
            holder.itemView.setOnClickListener {
                onItemTouchListener.onDetalhar(
                    it,
                    adapterPosition
                )
            }
        }
    }
}