package br.com.ams.appcatalogo.catalogo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.DateTimeUtil
import br.com.ams.appcatalogo.databinding.CardviewcatalogoBinding
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.model.CatalogoDTO
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.example.imagekit.android.picasso_extension.createWithPicasso
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import com.squareup.picasso.Picasso
import java.io.File

class CardViewCatalogoAdapter(
    private val context: Context,
    private val onItemTouchListener: OnItemTouchListener
) :
    RecyclerView.Adapter<CardViewCatalogoAdapter.ViewHolder>() {
    private var registros: List<Catalogo>? = null

    fun carregarRegistros(registros: List<Catalogo>?) {
        this.registros = registros
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val binding = CardviewcatalogoBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: CardviewcatalogoBinding) :
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