package org.jmesa.limit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
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
}
