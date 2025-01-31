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
package org.jmesa.view.pdfp;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.jmesa.view.AbstractViewExporter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * A PDF view that uses the iText PdfPTable.
 *
 * @since 2.3.4
 * @author Ismail Seyfi
 */
public class PdfPViewExporter extends AbstractViewExporter {

    @Override
    public void export() throws Exception {
        HttpServletResponse response = getHttpServletResponse();
        ServletOutputStream out = response.getOutputStream();
        export(out);
    }

    @Override
    public void export(OutputStream out) throws Exception {
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(out));
        Document document = new Document(pdfDoc);

        PdfPView pdfView = (PdfPView) getView();
        document.add(pdfView.getTableCaption());
        document.add(pdfView.render());
        document.close();
        responseHeaders();
        out.flush();
    }

    @Override
    public Object exportDirect(){
        throw  new UnsupportedOperationException();
    }

    @Override
    protected String getContextType() {
        return "application/pdf";
    }

    @Override
    protected String getExtensionName() {
        return "pdf";
    }
}
