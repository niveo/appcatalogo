package br.com.ams.appcatalogo.common

import android.util.Log
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

object ValorRealUtil {

    fun ConvertStringValueToBigDecimal(
        numero: String,
        qtdeCasasDecimais: Int
    ): String? {
        var casasDecimais = ""
        var num = numero

        return try {
            if (qtdeCasasDecimais > 0) {
                for (i in 0 until qtdeCasasDecimais) {
                    casasDecimais = casasDecimais + "0"
                }
                if (num == "") {
                    num = "0.$casasDecimais"
                }
                val df = DecimalFormat(
                    "#,##0.$casasDecimais",
                    DecimalFormatSymbols(Locale("pt", "BR"))
                )
                df.isParseBigDecimal = true // aqui o pulo do gato
                df.roundingMode = RoundingMode.DOWN
                df.format(num.toDouble())
            } else {
                if (num == "") {
                    num = "0"
                }
                val df = DecimalFormat("###########")
                df.isParseBigDecimal = true
                df.roundingMode = RoundingMode.DOWN
                df.format(num.toDouble())
            }
        } catch (ex: Exception) {
            ""
        }
    }


    fun formatarValorReal(value: Double?): String {
        return formatarValorReal(value, false)
    }

    fun formatarValorReal(value: Double?, pattern: String): String? {
        val decimalFormat: NumberFormat = DecimalFormat(
            pattern,
            DecimalFormatSymbols(Locale("pt", "BR"))
        )
        return decimalFormat.format(value ?: 0)
    }

    fun formatarValorReal(value: Double? = 0.0, inicial: Boolean): String {
        val decimalFormat: NumberFormat = DecimalFormat(
            (if (inicial) "R$ " else "") + "#,##0.00",
            DecimalFormatSymbols(Locale("pt", "BR"))
        )
        return decimalFormat.format(value ?: 0)
    }

    fun formatarValorReal(value: Float?): String? {
        return formatarValorReal(value, false)
    }

    fun formatarValorReal(value: Float?, inicial: Boolean): String? {
        val decimalFormat: NumberFormat = DecimalFormat(
            (if (inicial) "R$ " else "") + "#,##0.00",
            DecimalFormatSymbols(Locale("pt", "BR"))
        )
        return decimalFormat.format(value ?: 0)
    }


    fun correcaoDecimalString(valueReal: String): Double {
        var value = valueReal
        if (value.contains(".")) {
            value = value.replace("\\.".toRegex(), ",")
        }
        val numberFormat =
            DecimalFormat.getInstance(Locale("pt", "BR"))
        val theNumber: Number?
        return try {
            theNumber = numberFormat.parse(value)
            theNumber.toDouble()
        } catch (e: ParseException) {
            val valueWithDot = value.replace(",".toRegex(), ".")
            try {
                java.lang.Double.valueOf(valueWithDot)
            } catch (e2: NumberFormatException) {
                Log.w("CORE", "Warning: Value is not a number$value")
                0.0
            }
        }
    }

    fun obterPatternsValor(value: Double?): String? {
        val decimalFormat = DecimalFormat(
            "#,##0.00",
            DecimalFormatSymbols(Locale("pt", "BR"))
        )
        return decimalFormat.format(value)
    }

    fun obterNumeroFormatado(
        value: Double?,
        formato: String?,
        simbolo: String?,
        inicialSimbolo: Boolean
    ): String {
        val df =
            DecimalFormat(formato ?: "#,##0.00")
        var ret = (simbolo ?: "") + df.format(value)
        if (!inicialSimbolo) {
            ret = df.format(value) + (simbolo ?: "")
        }
        return ret
    }
}