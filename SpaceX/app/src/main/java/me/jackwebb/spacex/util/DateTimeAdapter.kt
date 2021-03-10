package me.jackwebb.spacex.util

import com.google.gson.*
import com.google.gson.JsonParseException
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type


object DateTimeAdapter : JsonDeserializer<DateTime>, JsonSerializer<DateTime> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): DateTime? {
        // Do not try to deserialize null or empty values
        if (json.asString == null || json.asString.isEmpty()) {
            return null
        }
        val format = ISODateTimeFormat.dateTimeParser().withOffsetParsed()
        return format.parseDateTime(json.asString)
    }

    override fun serialize(
        src: DateTime,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val format = ISODateTimeFormat.dateTime()
        return JsonPrimitive(format.print(src))
    }

}