package br.com.ams.appcatalogo.catalogo.dataadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.common.ValorRealUtil
import br.com.ams.appcatalogo.databinding.CatalogoProdutosCordenadasDataAdapterBinding
import br.com.ams.appcatalogo.entity.Produto

class CatalogoProdutosCordenadasDataAdapter(
    private val onItemTouchListener: OnItemTouchListener
) : RecyclerView.Adapter<CatalogoProdutosCordenadasDataAdapter.ViewHolder>() {

    private var registros: List<Produto>? = null

    fun carregarRegistros(registros: List<Produto>?) {
        this.registros = registros
        this.notifyDataSetChanged()
    }

    fun obterRegistro(position: Int): Produto {
        return registros!![position]
    }

    inner class ViewHolder(val binding: CatalogoProdutosCordenadasDataAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val binding = CatalogoProdutosCordenadasDataAdapterBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (registros == null) 0 else registros!!.size
    }

    interface OnItemTouchListener {
        fun onDetalhar(view: View, position: Int)
        fun onPedido(view: View, position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(registros!![position]) {
                binding.cardviewprodutoscordenadasDescricao.text = descricao
                binding.cardviewprodutoscordenadasValor.text =
                    ValorRealUtil.formatarValorReal(valor)
            }
            holder.itemView.setOnClickListener {
                onItemTouchListener.onDetalhar(
                    it,
                    adapterPosition
                )
            }
            holder.binding.cardviewprodutoscordenadasPedido.setOnClickListener {
                onItemTouchListener.onPedido(
                    it,
                    adapterPosition
                )
            }
        }
    }
}