package br.com.ams.appcatalogo.catalogo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ams.appcatalogo.catalogo.utils.UtilCatalogo
import br.com.ams.appcatalogo.common.DateTimeUtil
import br.com.ams.appcatalogo.databinding.CardviewcatalogopaginaBinding
import br.com.ams.appcatalogo.model.CatalogoPaginaMapeadosDTO
import java.io.FileInputStream

class CardViewCatalogoPaginaAdapter(
    private val onItemTouchListener: OnItemTouchListener
) :
    RecyclerView.Adapter<CardViewCatalogoPaginaAdapter.ViewHolder>() {
    private var registros: List<CatalogoPaginaMapeadosDTO>? = null

    fun carregarRegistros(registros: List<CatalogoPaginaMapeadosDTO>?) {
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

    fun obterRegistro(position: Int): CatalogoPaginaMapeadosDTO {
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
                binding.cardviewcatalogopaginaAlterado.text =
                    DateTimeUtil.dataTimePatterBR(dataAlterado)
                binding.cardviewcatalogopaginaMapeados.text = if (contaMapeados!! > 0) {
                    "Mapeados: " + contaMapeados
                } else {
                    ""
                }

                val fileCatalogo = UtilCatalogo.arquivoCatalogo(this)

                if (fileCatalogo.exists() && fileCatalogo.isFile) {

                    val fis = FileInputStream(fileCatalogo)
                    val options = BitmapFactory.Options()
                    options.inPreferredConfig = Bitmap.Config.RGB_565
                    val myBitmap = BitmapFactory.decodeStream(fis, null, options)

                    //val myBitmap: Bitmap = ImageUtils.getBitmap(fileCatalogo)
                    binding.cardviewcatalogopaginaImg.setImageBitmap(myBitmap)

                } else {
                    binding.cardviewcatalogopaginaImg.setImageDrawable(null)
                }
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