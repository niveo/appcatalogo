package br.com.ams.appcatalogo.catalogo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.DateTimeUtil
import br.com.ams.appcatalogo.databinding.CardviewcatalogoBinding
import br.com.ams.appcatalogo.model.CatalogoMapeadosDto
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.squareup.picasso.Picasso
import java.io.File

class CardViewCatalogoAdapter(
    private val onItemTouchListener: OnItemTouchListener
) :
    RecyclerView.Adapter<CardViewCatalogoAdapter.ViewHolder>() {
    private var registros: List<CatalogoMapeadosDto>? = null

    fun carregarRegistros(registros: List<CatalogoMapeadosDto>?) {
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

    fun getItem(position: Int): CatalogoMapeadosDto? {
        if (registros != null && (position < 0 || position >= registros!!.size)) {
            return null
        } else {
            return registros!!.get(position)
        }
    }

    fun progressRegistro(progress: Boolean, position: Int) {
        registros!!.get(position).progress = progress
        notifyItemChanged(position)
    }

    override fun getItemId(position: Int): Long {
        return registros!!.get(position).codigo!!
    }

    override fun getItemCount(): Int {
        return if (registros == null) 0 else registros!!.size
    }

    interface OnItemTouchListener {
        fun onDetalhar(view: View, position: Int)
        fun onMenu(view: View, position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(registros!![position]) {
                binding.cardviewcatalogoDescricao.text = descricao
                binding.atualizado.text = DateTimeUtil.dataTimePatterBR(dataAlterado)

                if (File(Config.PATH_AMS_CATALOGO + Config.FILE_SEP + this.codigo).exists()) {
                    binding.cardviewcatalogoDownload.visibility = View.GONE
                } else {
                    binding.cardviewcatalogoDownload.visibility = View.VISIBLE
                }

                if (progress) {
                    binding.progressCatalogo.visibility = View.VISIBLE
                    binding.cardviewcatalogoMenu.visibility = View.GONE
                } else {
                    binding.progressCatalogo.visibility = View.GONE
                    binding.cardviewcatalogoMenu.visibility = View.VISIBLE
                }

                if (mapeados != null && mapeados!! > 0) {
                    binding.mapeados.visibility = View.VISIBLE
                    binding.mapeados.text = "Mapeados: ${mapeados}"
                } else {
                    binding.mapeados.visibility = View.GONE
                }

                imagemUrl?.let {
                    var url: String = imagemUrl!!
                    if (!imagemUrl!!.startsWith("http")) {
                        url = Config.URL_SERVIDOR + "/" + imagemUrl
                    }
                    if (AppUtils.isAppDebug()) LogUtils.i("URL LOGO MARCA CATALOGO ", url)
                    Picasso.get()
                        .load(url)
                        .resize(50, 50)
                        .into(binding.cardviewcatalogoImg)
                } ?: run {
                    try {
                        binding.cardviewcatalogoImg.setImageResource(R.drawable.ic_baseline_auto_stories_24)
                    } catch (e: Exception) {
                        LogUtils.e(e)
                    }
                }
            }
            binding.cardviewcatalogoMenu.setOnClickListener {
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