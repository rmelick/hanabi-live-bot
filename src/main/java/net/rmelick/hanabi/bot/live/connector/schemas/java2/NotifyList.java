package net.rmelick.hanabi.bot.live.connector.schemas.java2;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

@JsonDeserialize(using = NotifyList.Deserializer.class)
@JsonSerialize(using = NotifyList.Serializer.class)
public class NotifyList {
    public List<NotifyListClass> notifyListClassArrayValue;
    public NotifyListClass notifyListClassValue;

    static class Deserializer extends JsonDeserializer<NotifyList> {
        @Override
        public NotifyList deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            NotifyList value = new NotifyList();
            switch (jsonParser.getCurrentToken()) {
            case START_ARRAY:
                value.notifyListClassArrayValue = jsonParser.readValueAs(new TypeReference<List<NotifyListClass>>() {});
                break;
            case START_OBJECT:
                value.notifyListClassValue = jsonParser.readValueAs(NotifyListClass.class);
                break;
            default: throw new IOException("Cannot deserialize NotifyList");
            }
            return value;
        }
    }

    static class Serializer extends JsonSerializer<NotifyList> {
        @Override
        public void serialize(NotifyList obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (obj.notifyListClassArrayValue != null) {
                jsonGenerator.writeObject(obj.notifyListClassArrayValue);
                return;
            }
            if (obj.notifyListClassValue != null) {
                jsonGenerator.writeObject(obj.notifyListClassValue);
                return;
            }
            throw new IOException("NotifyList must not be null");
        }
    }
}
