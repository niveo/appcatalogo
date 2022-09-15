package br.com.ams.appcatalogo.common.deserializer

import br.com.ams.appcatalogo.entity.CatalogoPagina
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class CatalogoPaginaDeserializer: JsonDeserializer<CatalogoPagina> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CatalogoPagina? {

        val jsonObject = json.asJsonObject
        LogUtils.d(jsonObject.get("codigo"))
        LogUtils.d(jsonObject.get("pagina"))
        LogUtils.d(jsonObject.get("dataAlterado"))
        LogUtils.d(jsonObject.get("catalogo"))
        return null
    }

}