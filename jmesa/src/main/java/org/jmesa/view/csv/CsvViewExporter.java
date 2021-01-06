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
package org.jmesa.view.csv;

import javax.servlet.ServletOutputStream;
import org.jmesa.view.AbstractViewExporter;

import java.io.OutputStream;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class CsvViewExporter extends AbstractViewExporter {

    @Override
    public void export()
            throws Exception {
        responseHeaders();
        export(getHttpServletResponse().getOutputStream());
    }

    @Override
    public void export(OutputStream out) throws Exception {
        String viewData = (String) getView().render();
        byte[] contents = (viewData).getBytes();
        out.write(contents);
        out.flush();
    }

    @Override
    protected String getContextType() {

        return "text/csv";
    }

    @Override
    protected String getExtensionName() {

        return "csv";
    }
}
