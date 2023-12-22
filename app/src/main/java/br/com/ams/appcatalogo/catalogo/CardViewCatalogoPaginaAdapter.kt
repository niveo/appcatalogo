package br.com.ams.appcatalogo.catalogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.databinding.CardviewcatalogopaginaBinding
import br.com.ams.appcatalogo.model.CatalogoPaginaDTO

class CardViewCatalogoPaginaAdapter(
    private val onItemTouchListener: OnItemTouchListener
) :
    RecyclerView.Adapter<CardViewCatalogoPaginaAdapter.ViewHolder>() {
    private var registros: List<CatalogoPaginaDTO>? = null

    fun carregarRegistros(registros: List<CatalogoPaginaDTO>?) {
        this.registros = registros
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val binding = CardviewcatalogopaginaBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: CardviewcatalogopaginaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return if (registros == null) 0 else registros!!.size
    }

    fun obterRegistro(position: Int): CatalogoPaginaDTO {
        return this.registros!!.get(position)
    }

    interface OnItemTouchListener {
        fun onDetalhar(view: View, position: Int)
        fun onMenu(v: View?, absoluteAdapterPosition: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(registros!![position]) {
                binding.cardviewcatalogopaginaPagina.text = pagina.toString()
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