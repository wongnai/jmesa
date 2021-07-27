package org.jmesa.limit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * @author xwx
 */
public class FilterDeserializer extends StdDeserializer<Filter> {

    FilterDeserializer() {
        super(Filter.class);
    }

    @Override
    public Filter deserialize(
            JsonParser jp, DeserializationContext context)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
      return Filter.build(root.get("key").asText(),
                Comparison.valueOf(root.get("comparison").asText().toUpperCase()),
                root.get("value"));

    }

    public static void configObjectMapper(ObjectMapper objectMapper){
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        SimpleModule module =
                new SimpleModule("FilterDeserializerModule",
                        new Version(1, 0, 0, null, "com.github.yujiaao","jmesa"));
        module.addDeserializer(Filter.class, new FilterDeserializer());
        objectMapper.registerModule(module);
    }
}
