package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
public class Serialize {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
// Opciones equivalentes a MetadataPropertyHandling.Ignore y DateParseHandling.None
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    // Método equivalente a C# ToJson()
    public static String toJson(ValoresDiarios[] valores) {
        try {
            return mapper.writeValueAsString(valores);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando JSON", e);
        }
    }
}