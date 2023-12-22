package br.com.ams.appcatalogo.catalogo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.DateTimeUtil
import br.com.ams.appcatalogo.databinding.CardviewcatalogoBinding
import br.com.ams.appcatalogo.model.CatalogoDTO
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.squareup.picasso.Picasso
import java.io.File

class CardViewCatalogoAdapter(
    private val onItemTouchListener: OnItemTouchListener
) :
    RecyclerView.Adapter<CardViewCatalogoAdapter.ViewHolder>() {
    private var registros: List<CatalogoDTO>? = null

    fun carregarRegistros(registros: List<CatalogoDTO>?) {
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

    fun getItem(position: Int): CatalogoDTO? {
        if (registros != null && (position < 0 || position >= registros!!.size)) {
            return null
        } else {
            return registros!!.get(position)
        }
    }


    override fun getItemId(position: Int): Long {
        return registros!!.get(position).id!!
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

               /* imagemUrl?.let {
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
                }*/
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