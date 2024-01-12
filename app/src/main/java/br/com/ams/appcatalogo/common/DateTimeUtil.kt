package br.com.ams.appcatalogo.common

import com.blankj.utilcode.util.TimeUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    val ISO_8601BASIC_DATE_PATTERN = "yyyyMMdd'T'HHmmss'Z'"

    val MONTH_LONG_NAME = arrayOf(
        "Janeiro",
        "Fevereiro",
        "Março",
        "Abril",
        "Maio",
        "Junho",
        "Julho",
        "Agosto",
        "Setembro",
        "Outubro",
        "Novembro",
        "Dezembro"
    )

    fun months(mes: Int, upperCase: Boolean = true): String {
        if (upperCase)
            return MONTH_LONG_NAME.get(mes).uppercase()
        else
            return MONTH_LONG_NAME.get(mes)
    }

    fun monthsShort(mes: Int, upperCase: Boolean = true): String {
        if (upperCase)
            return MONTH_LONG_NAME.get(mes).substring(0, 3).uppercase()
        else
            return MONTH_LONG_NAME.get(mes).substring(0, 3)
    }

    fun obterDateTime(data: Any, inicial: Boolean): Date? {
        return obterDateTimeBuild(data, inicial)
    }

    private fun obterDateTimeBuild(
        data: Any,
        inicial: Boolean
    ): Date? {
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("America/Sao_Paulo")
        c.time = data as Date
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        if (!inicial) {
            c[Calendar.HOUR_OF_DAY] = 23
            c[Calendar.MINUTE] = 59
            c[Calendar.SECOND] = 59
        }
        return c.time
    }

    //se a conversão falhar no formato yyyy-MM-dd tentar no formato dd/MM/yyyy
    fun formataData(data: String?): Date? {
        if (data == null) return null
        if (data.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            return TimeUtils.string2Date(data, "yyyy-MM-dd")
        } else {
            if (data.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
                return TimeUtils.string2Date(data, "dd/MM/yyyy")
            } else {
                return null
            }
        }
    }

    fun formataData(data: String?, format: String?): Date? {
        return TimeUtils.string2Date(data, format!!)
    }


    fun getDateCalendar(
        data: Date?,
        hora: Int,
        minuto: Int,
        segundo: Int
    ): Date? {
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("America/Sao_Paulo")
        c.time = data as Date
        c[Calendar.HOUR_OF_DAY] = hora
        c[Calendar.MINUTE] = minuto
        c[Calendar.SECOND] = segundo
        return c.time
    }


    fun removeTime(date: Date?): Date? {
        val cal = Calendar.getInstance()
        cal.time = date as Date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.time
    }

    fun dataStringAws(date: Date): String {
        val tz: TimeZone = TimeZone.getTimeZone("UTC")
        val df: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
        df.timeZone = tz
        return df.format(date)
    }

    fun dataStringISO8601(date: Date): String {
        val tz: TimeZone = TimeZone.getTimeZone("UTC")
        val df: DateFormat =
            SimpleDateFormat(ISO_8601BASIC_DATE_PATTERN)
        df.timeZone = tz
        return df.format(date)
    }

    fun stringDataISO8601(date: String): Date {
        val tz: TimeZone = TimeZone.getTimeZone("UTC")
        val df: DateFormat =
            SimpleDateFormat(ISO_8601BASIC_DATE_PATTERN)
        df.timeZone = tz
        return df.parse(date)
    }

    fun dataPatterBR(data: Any?, format: String?): String? {
        return if (data == null || data === "") "" else TimeUtils.date2String(
            data as Date?,
            format!!
        )
    }

    fun timePatterBR(data: Any?): String? {
        return if (data == null || data === "") "" else dataPatterBR(data, "HH:mm:ss")
    }

    fun dataPatterEU(data: Any?): String? {
        return if (data == null || data === "") "" else dataPatterBR(data, "yyyy-MM-dd")
    }

    fun dataPatterBR(data: Any?): String? {
        return if (data == null || data === "") "" else dataPatterBR(data, "dd/MM/yyyy")
    }

    fun dataTimePatterBR(data: Any?): String? {
        return if (data == null || data === "") "" else dataPatterBR(data, "dd/MM/yyyy HH:mm:ss")
    }

    fun zerarHoras1(data: Date?): Date? {
        val cal = Calendar.getInstance()
        cal.time = data as Date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.time
    }

    fun daysBetween(sDate: Date?, eDate: Date?): Int {
        val startDate = Calendar.getInstance()
        startDate.time = sDate as Date
        val endDate = Calendar.getInstance()
        endDate.time = eDate as Date
        val MILLIS_PER_WEEK = 24L * 60L * 60L * 1000L
        val deltaInMillis = startDate.time.time - endDate.time
            .time
        return (deltaInMillis / MILLIS_PER_WEEK).toInt()
    }


    fun daysBetween(startDate: Calendar, endDate: Calendar): Int {
        val MILLIS_PER_WEEK = 24L * 60L * 60L * 1000L
        val deltaInMillis = startDate.time.time - endDate.time
            .time
        return (deltaInMillis / MILLIS_PER_WEEK).toInt()
    }
}