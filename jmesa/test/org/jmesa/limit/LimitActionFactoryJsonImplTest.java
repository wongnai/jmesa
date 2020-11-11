package org.jmesa.limit;

import org.jmesa.model.ExportTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitActionFactoryJsonImplTest {

    private static final String id = "my-table";
    LimitActionFactoryJsonImpl laf;

    @BeforeEach
    public void setUp() {
        String src =" {\n" +
                "        \"id\": \"table-id\",\n" +
                "        \"action\": \"\",\n" +
                "        \"maxRows\": 500,\n" +
                "        \"page\": 123,\n" +
                "        \"filter\": {\n" +
                "            \"property1\":true,\n" +
                "            \"property2\":\"abc\"\n" +
                "        },\n" +
                "        \"sort\":{\n" +
                "            \"property1\":\"asc\",\n" +
                "            \"property2\":\"desc\"\n" +
                "        },\n" +
                "        \"exportType\":\"json\"\n" +
                "     }";
        laf = new LimitActionFactoryJsonImpl(id, src);
    }

    @Test
    void getId() {
        assertEquals(id, laf.getId());
    }

    @Test
    void getMaxRows() {
        assertEquals(500,laf.getMaxRows());
    }

    @Test
    void getPage() {
        assertEquals(123,laf.getPage());
    }

    @Test
    void getFilterSet() {
        FilterSet set = laf.getFilterSet();
        assertEquals("true", set.getFilter("property1").getValue());
        assertEquals("abc", set.getFilter("property2").getValue());
    }

    @Test
    void getSortSet() {
        SortSet set = laf.getSortSet();
        assertEquals(Order.ASC, set.getSort("property1").getOrder());
        assertEquals(Order.DESC, set.getSort("property2").getOrder());
    }

    @Test
    void getExportType() {
        assertEquals(ExportTypes.JSON, laf.getExportType());
    }
}
