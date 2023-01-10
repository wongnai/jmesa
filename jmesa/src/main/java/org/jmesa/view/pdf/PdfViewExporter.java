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
package org.jmesa.view.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.jmesa.view.AbstractViewExporter;
import org.jmesa.web.HttpServletRequestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//import org.xhtmlrenderer.util.XRLog;
//import org.xhtmlrenderer.resource.FSEntityResolver;

/**
 * @since 2.2
 * @author Paul Horn
 */
public class PdfViewExporter extends AbstractViewExporter implements HttpServletRequestSupport {

    private static Logger logger = LoggerFactory.getLogger(PdfViewExporter.class);

    private HttpServletRequest request;

    @Override
    public void export() throws Exception {
        HttpServletResponse response = getHttpServletResponse();
        export(response.getOutputStream());
    }

    @Override
    public void export(OutputStream out) throws Exception {

        String string = (String) getView().render();

        byte[] contents = null;
        try {
            contents = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.info("Not able to process the PDF file using the UTF-8 encoding.");
        }

        responseHeaders();

//        System.setProperty("xr.util-logging.loggingEnabled", "false");
//        System.setProperty("xr.util-logging.java.util.logging.ConsoleHandler.level", "WARN");
//        System.setProperty("xr.util-logging..level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.config.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.exception.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.general.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.init.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.load.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.load.xml-entities.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.match.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.cascade.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.css-parse.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.layout.level", "WARN");
//        System.setProperty("xr.util-logging.plumbing.render.level", "WARN");
//
//        XRLog.setLoggingEnabled(false);

//        ITextRenderer renderer = new ITextRenderer();
//
//        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//        FSEntityResolver er= FSEntityResolver.instance();
//        builder.setEntityResolver(er);
//        Document doc = builder.parse(new ByteArrayInputStream(contents));
//
//        renderer.setDocument(doc, getBaseUrl());
//        renderer.layout();
        HttpServletResponse response = getHttpServletResponse();
//        renderer.createPDF(response.getOutputStream());
        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(getBaseUrl());
        properties.setCharset("UTF-8");
        HtmlConverter.convertToPdf(new ByteArrayInputStream(contents), response.getOutputStream(),
                properties);


    }

    /**
     * @return The base url to the web application.
     */
    private String getBaseUrl() {

        if (request != null) {
            return request.getRequestURL().toString();
        }
        return null;
    }

    @Override
    public HttpServletRequest getHttpServletRequest() {

        return request;
    }

    @Override
    public void setHttpServletRequest(HttpServletRequest request) {

        this.request = request;
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
