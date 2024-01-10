package br.com.ams.appcatalogo.catalogo.dataadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.databinding.CatalogoDataAdapterBinding
import br.com.ams.appcatalogo.entity.Catalogo
import com.example.imagekit.android.picasso_extension.createWithPicasso
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition

class CatalogoDataAdapter(
    private val context: Context,
    private val onItemTouchListener: OnItemTouchListener
) :
    RecyclerView.Adapter<CatalogoDataAdapter.ViewHolder>() {
    private var registros: List<Catalogo>? = null

    fun carregarRegistros(registros: List<Catalogo>?) {
        this.registros = registros
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val binding = CatalogoDataAdapterBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: CatalogoDataAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun getItem(position: Int): Catalogo {
        return registros!![position]
    }


    override fun getItemId(position: Int): Long {
        return registros!![position].id
    }

    override fun getItemCount(): Int {
        return if (registros == null) 0 else registros!!.size
    }

    interface OnItemTouchListener {
        fun onDetalhar(view: View, position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(registros!![position]) {
                binding.cardviewcatalogoDescricao.text = descricao

                this.identificador?.let { identificador ->
                    this.avatar?.let { avatar ->
                        ImageKit.getInstance()
                            .url(
                                path = avatar,
                                transformationPosition = TransformationPosition.QUERY,
                                urlEndpoint = "${context.getString(R.string.imagekit_endpoint)}/catalogo/catalogos/${identificador}/"
                            )
                            .createWithPicasso()
                            .into(binding.cardviewcatalogoImg)
                    }
                }
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