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
package org.jmesa.worksheet.servlet;

import org.jmesa.core.message.Messages;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.jmesa.web.HttpServletRequestWebContext;
import org.jmesa.web.WebContext;
import org.jmesa.worksheet.UniqueProperty;
import org.jmesa.worksheet.Worksheet;
import org.jmesa.worksheet.WorksheetRow;
import org.jmesa.worksheet.WorksheetUpdater;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @since 2.3
 * @author Jeff Johnston
 */
public class WorksheetServletTest {

    protected static final String ID = "pres";

    @Test
    public void getWorksheet() {

        WorksheetUpdaterTemp servlet = new WorksheetUpdaterTemp();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", ID);

        WebContext webContext = new HttpServletRequestWebContext(request);

        Worksheet worksheet = servlet.getAccessToWorksheet(null, webContext);

        WorksheetRow row = new WorksheetRow(new UniqueProperty(null, null));
        worksheet.addRow(row);

        assertNotNull(worksheet);
        assertTrue(() -> worksheet.getRows().size() == 1, "There are no rows in the worksheet.");

        Worksheet worksheet2 = servlet.getAccessToWorksheet(null, webContext);
        assertNotNull(worksheet2);
        assertTrue(() -> worksheet == worksheet2, "Did not return the same worksheet.");
    }

    private class WorksheetUpdaterTemp extends WorksheetUpdater {

        public Worksheet getAccessToWorksheet(Messages messages, WebContext webContext) {

            return super.getWorksheet(messages, webContext);
        }
    }
}
