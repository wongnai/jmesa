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
package org.jmesa.view;

import org.jmesa.core.CoreContextSupport;
import org.jmesa.util.DownloadFileNameEncoder;
import org.jmesa.web.HttpServletResponseSupport;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.jmesa.core.CoreContext;


import static org.jmesa.view.ExportConstants.ENCODING;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public abstract class AbstractViewExporter implements ViewExporter, CoreContextSupport, HttpServletResponseSupport {

    private View view;
    private String fileName;
    private CoreContext coreContext;
    private HttpServletResponse response;
	private String userAgent;

    public void responseHeaders()
            throws Exception {

        response.setContentType(getContextType());
        String encoding = getEncoding();

        if (encoding == null) {
            encoding = Charset.defaultCharset().name();
        }
        String fn = new String(fileName.getBytes(encoding), encoding);
        if(!fn.endsWith(getExtensionName())){
        	fn += getExtensionName();
        }

        // added by xwx
        fn = DownloadFileNameEncoder.codedFileName(getUserAgent(), fn, encoding);

 //       String fn = getFileName() + "." + getExtensionName();
   //     fn = new String(fn.getBytes(encoding), encoding);
        //response.setHeader("Content-Disposition", "attachment;filename=\"" + fn + "\"");
        response.setHeader("Content-Disposition", "attachment;filename\"" + fn + "\"");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setDateHeader("Expires", (System.currentTimeMillis() + 1000));
    }



    @Override
    public View getView() {

        return view;
    }

    @Override
    public void setView(View view) {

        this.view = view;
    }

    @Override
    public String getFileName() {

        return fileName;
    }

    @Override
    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    @Override
    public CoreContext getCoreContext() {

        return coreContext;
    }

    @Override
    public void setCoreContext(CoreContext coreContext) {

        this.coreContext = coreContext;
    }

    @Override
    public HttpServletResponse getHttpServletResponse() {

        return response;
    }

    @Override
    public void setHttpServletResponse(HttpServletResponse response) {

        this.response = response;
    }

    protected String getEncoding() {

        String encoding = coreContext.getPreference(ENCODING);
        if (encoding == null) {
            encoding = Charset.defaultCharset().name();
        }
        return encoding;
    }

    protected abstract String getContextType();
    protected abstract String getExtensionName();


    public String getUserAgent() {
        return userAgent;
    }

    public AbstractViewExporter setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }



}
