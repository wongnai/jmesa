package org.jmesa.limit;

import org.jmesa.model.ExportTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
//                "        \"filter\": {\n" +
//                "            \"property1\":true,\n" +
//                "            \"property2\":\"abc\",\n" +
//                "            \"property3\":[\"10\", \"100\"]\n" +
//                "        },\n" +
                "\"filter\":[\n" +
                "{\"key\":\"property1\",\"comparison\":\"is\",\"value\":[true]}\n" +
                ",{\"key\":\"property2\",\"comparison\":\"is\",\"value\":[\"abc\"]}\n" +
                ",{\"key\":\"property3\",\"comparison\":\"between\",\"value\":[\"10\", \"100\"]}\n" +
                ",{\"key\":\"name\",\"comparison\":\"in\",\"value\":[\"name1\", \"name2\",\"name3\",\"name4\"]}\n" +
                ",{\"key\":\"id\",\"comparison\":\"gte\",\"value\":[5]}\n" +
                "],"+
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
        assertEquals(true, set.getFilter("property1").getValue()[0]);
        assertEquals("abc", set.getFilter("property2").getValue()[0]);
        assertTrue(Arrays.equals(new Object[]{"10","100"}, set.getFilter("property3").getValue()));
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


    @Test
    void pairValueTest(){
        String s ="{\"maxRows\":20,\"page\":1,\"exportType\":\"json\",\"sort\":{},\"filter\":[{\"key\":\"ExpirationDate\",\"comparison\":\"gte\", \"value\":[\"2021-04-01 00:00:00\"]}]}";

        LimitActionFactoryJsonImpl local = new LimitActionFactoryJsonImpl(id, s);
        FilterSet set = local.getFilterSet();

       assertEquals("{property='ExpirationDate', value=[2021-04-01 00:00:00], comparison=GTE}", set.getFilter("ExpirationDate").toString());

      //  System.out.println(set.toString());
    }
}
