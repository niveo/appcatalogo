package br.com.ams.appcatalogo.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.databinding.FragmentEnviarCopiaEmailDialogBinding
import br.com.ams.appcatalogo.enuns.TipoEnvioEmail
import com.blankj.utilcode.util.ToastUtils
import java.io.File

class EnviarCopiaEmailDialogFragment : DialogFragment() {

    var listItems = ArrayList<String>()
    var adapter: ArrayAdapter<String>? = null
    lateinit var binding: FragmentEnviarCopiaEmailDialogBinding

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun afterTextChanged(s: Editable) {
            carregarEmailsLista()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnviarCopiaEmailDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtEmails.addTextChangedListener(textWatcher)
        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            listItems
        )
        binding.fragmentEnviarCopiaEmailLista.adapter = adapter
        binding.fragmentEnviarCopiaEmailEnviar.setOnClickListener { this.enviarCopia() }

        binding.btnSair.setOnClickListener {
            dismiss()
        }

        val emails = requireArguments().getString(EXTRA_EMAIL)
        if (emails != null) {
            binding.txtEmail.text = emails
        } else {
            binding.txtAlerta.visibility = View.GONE
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    fun enviarCopia() {
        ToastUtils.showLong(getString(R.string.nao_implementado))
    }

    fun carregarEmailsLista() {
        val emailsSplit = binding.txtEmails.text!!.split(";")
        listItems.clear()
        for (email in emailsSplit) {
            listItems.add(email)
        }
        adapter!!.notifyDataSetChanged()
    }

    fun openDialog(fm: FragmentManager) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(
                fm,
                DIALOG_TAG
            )
        }
    }

    companion object {

        private val EXTRA_TIPO = "tipo"
        private val EXTRA_CODIGO = "codigo"
        private val EXTRA_EMAIL = "emailNfe"
        private val EXTRA_EMAIL_ARQUIVO = "emaiArquivo"
        private val EXTRA_EMAIL_SUBJECT = "emailSubject"
        private val EXTRA_EMAIL_BODY = "emailBody"
        private const val DIALOG_TAG = "enviarCopiaEmailDialogFragment"

        @JvmStatic
        fun newInstance(
            tipo: TipoEnvioEmail,
            codigo: String?,
            email: String?,

            subject: String? = null,
            body: String? = null,
            file: File? = null
        ): EnviarCopiaEmailDialogFragment {

            val fragment =
                EnviarCopiaEmailDialogFragment()

            val bundle = Bundle()
            bundle.putSerializable(EXTRA_TIPO, tipo)
            bundle.putString(EXTRA_CODIGO, codigo)
            bundle.putString(EXTRA_EMAIL, email)

            bundle.putString(EXTRA_EMAIL_SUBJECT, subject)
            bundle.putString(EXTRA_EMAIL_BODY, body)
            bundle.putString(EXTRA_EMAIL_ARQUIVO, file.toString())

            fragment.arguments = bundle

            return fragment
        }
    }
}

