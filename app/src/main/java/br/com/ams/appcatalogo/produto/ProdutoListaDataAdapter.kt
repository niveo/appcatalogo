package br.com.ams.appcatalogo.produto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.common.ValorRealUtil
import br.com.ams.appcatalogo.databinding.ProdutoListaDataAdapterBinding
import br.com.ams.appcatalogo.entity.Produto
import com.blankj.utilcode.util.LogUtils

class ProdutoListaDataAdapter :
    RecyclerView.Adapter<ProdutoListaDataAdapter.ViewHolder>() {
    private var registros: List<Produto>? = null

    fun carregarRegistros(registros: List<Produto>?) {
        this.registros = registros
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ViewHolder {
        val binding = ProdutoListaDataAdapterBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ProdutoListaDataAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return if (registros == null) 0 else registros!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(registros!![position]) {
                LogUtils.w(this)
                binding.produtoListaDataAdapterDescricao.text = descricao
                binding.produtoListaDataAdapterReferencia.text = referencia
                binding.produtoListaDataAdapterValor.text = ValorRealUtil.formatarValorReal(valor)
            }
        }
    }
}