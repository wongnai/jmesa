package org.jmesa.limit;

import org.apache.commons.compress.utils.IOUtils;
import org.jmesa.model.ExportTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(Arrays.equals(new Object[]{"name1","name2","name3","name4"}, set.getFilter("name").getValue()));

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

    }

    @Test
    void nestedFilterSets() throws IOException {
        String json = new String(IOUtils.toByteArray(this.getClass().getResourceAsStream("/request-filterset.json")));


        LimitActionFactoryJsonImpl local = new LimitActionFactoryJsonImpl(id, json);
        FilterSet set = local.getFilterSet();

        assertEquals("{filterSets=[{filters=[{property='property1', value=[true], comparison=IS}," +
                        " {property='property2', value=[abc], comparison=IS}], operator=and}, " +
                        "{filters=[{property='property3', value=[10, 100], comparison=BETWEEN}, " +
                        "{property='name', value=[name1, name2, name3, name4], comparison=IN}, " +
                        "{property='id', value=[5], comparison=GTE}], operator=or}], operator=and}",
                set.toString());

    }
}
