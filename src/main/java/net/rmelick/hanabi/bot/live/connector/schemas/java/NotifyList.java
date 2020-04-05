package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

@JsonDeserialize(using = NotifyList.Deserializer.class)
@JsonSerialize(using = NotifyList.Serializer.class)
public class NotifyList {
    public Notify _notifyValue;
    public List<Notify> _notifyArrayValue;

    static class Deserializer extends JsonDeserializer<NotifyList> {
        @Override
        public NotifyList deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            NotifyList value = new NotifyList();
            switch (jsonParser.getCurrentToken()) {
                case START_ARRAY:
                    value._notifyArrayValue = jsonParser.readValueAs(new TypeReference<List<Notify>>() {});
                    break;
                case START_OBJECT:
                    value._notifyValue = jsonParser.readValueAs(Notify.class);
                    break;
                default: throw new IOException("Cannot deserialize NotifyList");
            }
            return value;
        }
    }

    static class Serializer extends JsonSerializer<NotifyList> {
        @Override
        public void serialize(NotifyList obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (obj._notifyValue != null) {
                jsonGenerator.writeObject(obj._notifyValue);
                return;
            }
            if (obj._notifyArrayValue != null) {
                jsonGenerator.writeObject(obj._notifyArrayValue);
                return;
            }
            throw new IOException("NotifyList must not be null");
        }
    }
}