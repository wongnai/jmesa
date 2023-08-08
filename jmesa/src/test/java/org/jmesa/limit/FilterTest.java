package org.jmesa.limit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilterTest {

    @Test
    void getProperty() {
    }

    @Test
    void getValue() {
    }

    @Test
    void getComparison() throws JsonProcessingException {
        String s="{\"property\":\"曝光量\",\"comparison\":\"GT\",\"value\":[\"0.1\"]}";
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
//        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        String sss = objectMapper.writeValueAsString(new SingleValueFilter("曝光量", Comparison.GT, new Object[]{"0.1"}));
        System.out.println(sss);


        Filter res = objectMapper.readValue(s, Filter.class);
        assertEquals("曝光量", res.getProperty());
        assertEquals(Comparison.GT, res.getComparison());
        assertEquals("0.1", res.getValue()[0]);

    }

    @Test
    void build() throws JsonProcessingException {
        String s = "{\"operator\":0,\"filterSets\":[],\"filters\":[{\"property\":\"曝光量\",\"comparison\":\"GT\",\"value\":[\"0.1\"]}]}";
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
//        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        System.out.println(Comparison.GT);

        FilterSet res = objectMapper.readValue(s, FilterSet.class);

        assertEquals(1, res.getFilters().size());
        assertEquals("曝光量", ((Filter)(res.getFilters().toArray()[0])).getProperty());
        assertEquals(Comparison.GT, ((Filter)(res.getFilters().toArray()[0])).getComparison());
        assertEquals("0.1", ((Filter)(res.getFilters().toArray()[0])).getValue()[0]);
    }
}
