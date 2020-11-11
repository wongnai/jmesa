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
package org.jmesa.worksheet.state;

import org.jmesa.web.WebContext;
import org.jmesa.worksheet.Worksheet;

/**
 * @author Jeff Johnston
 * @since 2.3
 */
public class SessionWorksheetState implements WorksheetState {

    private final String id;
    private final WebContext webContext;

    public SessionWorksheetState(String id, WebContext webContext) {

        this.id = id + "_WORKSHEET";
        this.webContext = webContext;
    }

    @Override
    public Worksheet retrieveWorksheet() {

        return (Worksheet) webContext.getSessionAttribute(id);
    }

    @Override
    public void persistWorksheet(Worksheet worksheet) {

        webContext.setSessionAttribute(id, worksheet);
    }
}
