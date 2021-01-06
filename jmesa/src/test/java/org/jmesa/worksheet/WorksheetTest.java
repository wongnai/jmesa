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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

/**
 * @since 2.3
 * @author Jeff Johnston
 */
public class WorksheetTest {

    @Test
    public void removeRow() {


        UniqueProperty firstRowMap = new UniqueProperty("id", "1");
        WorksheetRow firstRow = new WorksheetRow(firstRowMap);
        firstRow.setRowStatus(WorksheetRowStatus.ADD);

        UniqueProperty secondRowMap = new UniqueProperty("id", "2");
        WorksheetRow secondRow = new WorksheetRow(secondRowMap);
        secondRow.setRowStatus(WorksheetRowStatus.MODIFY);

        UniqueProperty thirdRowMap = new UniqueProperty("id", "3");
        WorksheetRow thirdRow = new WorksheetRow(thirdRowMap);
        thirdRow.setRowStatus(WorksheetRowStatus.REMOVE);

        Worksheet worksheet = new Worksheet("pres");
        worksheet.addRow(firstRow);
        worksheet.addRow(secondRow);
        worksheet.addRow(thirdRow);

        assertTrue(() -> worksheet.getRows().size() == 3, "The rows are not accounted for.");

        worksheet.removeRow(worksheet.getRow(secondRowMap));

        assertTrue(() -> worksheet.getRows().size() == 2, "The rows are not accounted for.");

        Iterator<WorksheetRow> iter = worksheet.getRows().iterator();
        final WorksheetRowStatus status = iter.next().getRowStatus();
        assertTrue(()-> status == WorksheetRowStatus.ADD || status == WorksheetRowStatus.REMOVE, "The first row exists.");
        final WorksheetRowStatus status1 = iter.next().getRowStatus();
        assertTrue(()->  status1 == WorksheetRowStatus.ADD || status1 == WorksheetRowStatus.REMOVE, "The third row exists.");
    }
}
