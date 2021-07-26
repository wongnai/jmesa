/*
 * Copyright 2004 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmesa.limit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.Signature;
import java.util.Arrays;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class FilterSetTest {

    @Test
    public void isFiltered() {

        FilterSet filterSet = new FilterSet();
        boolean filterable = filterSet.isFiltered();
        assertFalse(filterable, "default constructor");

        filterSet = getFilterSet();
        filterable = filterSet.isFiltered();
        assertTrue(filterable, "filtered");
    }

    @Test
    public void getFilterValue() {

        FilterSet filterSet = getFilterSet();
        String nickname = filterSet.getFilterValue("nickname");
        assertNotNull(nickname);
        assertEquals(nickname, "Father Of His Country");
    }

    @Test
    public void getFilter() {

        FilterSet filterSet = getFilterSet();
        Filter filter = filterSet.getFilter("nickname");
        assertNotNull(filter);
        assertEquals(filter.getValue()[0], "Father Of His Country");
    }

    @Test
    public void getInRange() {

        FilterSet filterSet = getFilterSet();
        Object[] age = filterSet.getFilter("age").getValue();
        assertNotNull(age);
        assertTrue(Arrays.equals(age, new Object[]{"20","35"}));

        assertTrue( new RangeFilter.Pair(age[0].toString(), age[1].toString()).inRange(30));
        assertFalse( new RangeFilter.Pair(age[0].toString(), age[1].toString()).inRange(35));

    }

    private FilterSet getFilterSet() {

        FilterSet filters = new FilterSet();
        filters.addFilter(new SingleValueFilter("fullName", Comparison.IS, new Object[]{ "George Washington"}));
        filters.addFilter(new SingleValueFilter("nickname",Comparison.IS,  new Object[]{ "Father Of His Country"}));
        filters.addFilter(new RangeFilter("age", Comparison.BETWEEN, new Object[]{"20", "35"}));
        return filters;
    }

    @Test
    @DisplayName("test Nested FilterSet")
    public void testPredicates() throws IOException {
        String json = new String(IOUtils.toByteArray(this.getClass().getResourceAsStream("/predicates.json")));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        SimpleModule module =
                new SimpleModule("FilterDeserializerModule",
                        new Version(1, 0, 0, null, "com.github.yujiaao","jmesa"));
        module.addDeserializer(Filter.class, new FilterDeserializer());
        objectMapper.registerModule(module);

        FilterSet res = objectMapper.readValue(json, FilterSet.class);

        System.out.println(res);

    }
}
