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
package org.jmesa.view.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jmesa.view.AbstractExportView;
import org.jmesa.view.component.Column;
import org.jmesa.view.renderer.CellRenderer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @since 4.1
 * @author  xwx
 */
public class JsonView extends AbstractExportView {

    public JsonView() {}


    @Override
    public Object render() {
        try {
            StringBuilder results = new StringBuilder();

            List<Column> columns = getTable().getRow().getColumns();

            results.append("{\n\"caption\":\"").append(escapeValue(getTable().getCaption())).append("\",\n");
            addColumnTitles(results, columns);
            addTableData(results, columns);
            addParams(results, columns);

            results.append("}\n");

            return results.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addParams(StringBuilder results, List<Column> columns) {
            addSortableColumns(results, columns);

    }

    private void addSortableColumns(StringBuilder results, List<Column> columns) {

    }

    private void addTableData(StringBuilder results, List<Column> columns) throws JsonProcessingException {
        int rowcount = 0;
        results.append("\"data\": [");
        Collection<?> items = getCoreContext().getPageItems();
        ObjectMapper mapper = new ObjectMapper();


        for (Object item : items ) {
            rowcount++;
            results.append("{\n");

            Iterator<Column> bodyIterator = columns.iterator();
            while (bodyIterator.hasNext()) {
                Column column = bodyIterator.next();
                CellRenderer cellRenderer = column.getCellRenderer();
                Object value = cellRenderer.render(item, rowcount);
                results.append("\"").append(escapeValue(column.getProperty())).append("\"");
                results.append(":");
                String jsonString = mapper.writeValueAsString(value);
                results.append(jsonString);

                if (bodyIterator.hasNext()) {
                    results.append(",\n");
                }
            }
            results.append("}");
            if(rowcount< items.size()){
                results.append(",\n");
            }
        }
        results.append("\n]\n");
    }

    private void addColumnTitles(StringBuilder results, List<Column> columns) {
        results.append("\"titles\": [\n");
        Iterator<Column> headerIterator = columns.iterator();
        while (headerIterator.hasNext()) {
            Column column = headerIterator.next();
            String title = column.getTitle();
            results.append("\"").append(escapeValue(title)).append("\"");
            if (headerIterator.hasNext()) {
                results.append(",\n");
            }
        }
        results.append("],\n");
    }

    String escapeValue(Object value) {

        if (value == null) {
            return "";
        }

        String stringval = String.valueOf(value);
        return escapeJavascript(stringval);
    }

    private String escapeJavascript(CharSequence t) {
        StringBuilder e = new StringBuilder();
        for(int a = 0; a < t.length(); a++) {
            e.append(javascriptEscapeCode(t.charAt(a)));
        }
        return e.toString();
    }

    /**
     * https://docs.oracle.com/javase/tutorial/java/data/characters.html
     * @param t
     * @return
     */
    private CharSequence javascriptEscapeCode(char t) {
        switch (t) {
            case '\r':
                return "\\r";
            case '\n':
                return "\\n";
            // \v vertical tab
            // case '\\0x00b':
            //     return "\\v";
            case '\'':
                return "\\'";
            case '"':
                return "\\\"";
           // case '&':
           //     return "\\&";
            case '\\':
                return "\\\\";
            case '\t':
                return "\\t";
            case '\b':
                return "\\b";
            case '\f':
                return "\\f";
            default:
                return t+"";
        }
    }
}
