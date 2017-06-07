package com.smartu.utilidades;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 07/06/2017.
 */

public class ConversoresJSON {
    /**
     * Sirven para serializar y deserializar un objeto de tipo boolean que viene como int
     * Son custom serializadores y deserializadores
     */
    public static class DateTimeSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date fecha, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
            generator.writeNumber(fecha.getTime());
        }
    }

    public static class DateTimeDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=new Date();
            try {
                date = sdf.parse(parser.getText());
            } catch (Exception e) {
                e.printStackTrace();

            }
            return date;
        }
    }

    /**
     * Sirven para serializar y deserializar un objeto de tipo boolean que viene como int
     * Son custom serializadores y deserializadores
     */
    public static class NumericBooleanSerializer extends JsonSerializer<Boolean> {

        @Override
        public void serialize(Boolean bool, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
            generator.writeString(bool ? "1" : "0");
        }
    }

    public static class NumericBooleanDeserializer extends JsonDeserializer<Boolean> {

        @Override
        public Boolean deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            return !"0".equals(parser.getText());
        }
    }
}
