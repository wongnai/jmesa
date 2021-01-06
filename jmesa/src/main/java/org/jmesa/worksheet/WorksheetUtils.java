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
package org.jmesa.worksheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jmesa.view.component.Row;

/**
 * The utilities to work with the Worksheet.
 * 
 * @author Jeff Johnston
 */
public class WorksheetUtils {
		
    private WorksheetUtils() {}

    /**
     * @return Get the unique row property name. If there are no rows then return null.
     */
    public static String getUniquePropertyName(Worksheet worksheet) {
		
        if (worksheet.getRows() == null || worksheet.getRows().isEmpty()) {
            return null;
        }
        
        return worksheet.getRows().iterator().next().getUniqueProperty().getName();
    }

    /**
     * @return Get the unique property values.
     */
    public static List<String> getUniquePropertyValues(Worksheet worksheet) {
		
        List<String> result = new ArrayList<String>();

        Collection<WorksheetRow> worksheetRows = worksheet.getRows();
        for (WorksheetRow worksheetRow : worksheetRows) {
            UniqueProperty uniqueProperty = worksheetRow.getUniqueProperty();
            result.add(uniqueProperty.getValue());
        }

        return result;
    }

    public static boolean isRowRemoved(Worksheet worksheet, Row row, Object item) {
		
        WorksheetRow worksheetRow = getWorksheetRow(worksheet, row, item);
        if (worksheetRow == null) {
            return false;
        }

        return (worksheetRow.getRowStatus().equals(WorksheetRowStatus.REMOVE));
    }

    public static boolean hasRowError(Worksheet worksheet, Row row, Object item) {
		
        WorksheetRow worksheetRow = getWorksheetRow(worksheet, row, item);
        if (worksheetRow == null) {
            return false;
        }

        return worksheetRow.hasError();
    }

    public static String getRowError(Worksheet worksheet, Row row, Object item) {
		
        WorksheetRow worksheetRow = getWorksheetRow(worksheet, row, item);
        if (worksheetRow == null) {
            return null;
        }

        return worksheetRow.getError();
    }

    public static WorksheetRow getWorksheetRow(Worksheet worksheet, Row row, Object item) {
		
        if (worksheet == null) {
            return null;
        }

        UniqueProperty uniqueProperty = row.getUniqueProperty(item);
        return worksheet.getRow(uniqueProperty);
    }
}
