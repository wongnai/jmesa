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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jmesa.core.CoreContext;
import org.jmesa.test.AbstractTestCase;
import org.jmesa.view.component.Row;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.web.WebContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @since 2.3
 * @author Jeff Johnston
 */
@Disabled
public class WorksheetCellEditorTest extends AbstractTestCase {

    @Test
    public void getEditedValue() {

        WebContext webContext = createWebContext();
        CoreContext coreContext = createCoreContext(webContext);

        // get the column
        HtmlColumn column = new HtmlColumn("name.firstName");

        column.setCoreContext(coreContext);

        // set the row unique properties
        Row row = new HtmlRow();
        row.setUniqueProperty("id");
        row.addColumn(column); // add column for back reference

        Object item = coreContext.getAllItems().iterator().next();
        Object value = column.getCellEditor().getValue(item, column.getProperty(), 1);

        assertNotNull(value);
    }

    @Override
    protected Worksheet getWorksheet() {

        UniqueProperty firstRowMap = new UniqueProperty("id", "1");
        WorksheetRow firstRow = new WorksheetRow(firstRowMap);
        firstRow.setRowStatus(WorksheetRowStatus.MODIFY);

        UniqueProperty secondRowMap = new UniqueProperty("id", "2");
        WorksheetRow secondRow = new WorksheetRow(secondRowMap);
        secondRow.setRowStatus(WorksheetRowStatus.MODIFY);

        UniqueProperty thirdRowMap = new UniqueProperty("id", "3");
        WorksheetRow thirdRow = new WorksheetRow(thirdRowMap);
        thirdRow.setRowStatus(WorksheetRowStatus.MODIFY);

        Worksheet worksheet = new Worksheet(ID);

        WorksheetColumn column = new WorksheetColumn("name.firstName", "");
        column.setChangedValue("Changed Name");
        firstRow.addColumn(column);
        worksheet.addRow(firstRow);

        worksheet.addRow(secondRow);
        worksheet.addRow(thirdRow);

        return worksheet;
    }
}
