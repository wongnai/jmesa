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
package org.jmesa.web;

import java.io.Writer;
import java.util.Locale;
import java.util.Map;

/**
 * <p>A general-purpose decorator object for a WebContext.  By default, all
 * methods delegate to the underlying WebContext.  Subclasses can override this
 * default behavior to mix in functionality to an existing context.</p>
 *
 * @author bgould
 * @version 2.3.4
 */
public class WebContextWrapper implements WebContext {

    private final WebContext webContext;

    public WebContextWrapper(WebContext webContext) {

        this.webContext = webContext;
    }

    public WebContext getWebContext() {

        return webContext;
    }

    @Override
    public Object getApplicationAttribute(String name) {

        return getWebContext().getApplicationAttribute(name);
    }

    @Override
    public Object getApplicationInitParameter(String name) {

        return getWebContext().getApplicationInitParameter(name);
    }

    @Override
    public Object getBackingObject() {

        return getWebContext().getBackingObject();
    }

    @Override
    public String getContextPath() {

        return getWebContext().getContextPath();
    }

    @Override
    public Locale getLocale() {

        return getWebContext().getLocale();
    }

    @Override
    public Object getPageAttribute(String name) {

        return getWebContext().getPageAttribute(name);
    }

    @Override
    public String getParameter(String name) {

        return getWebContext().getParameter(name);
    }

    @Override
    public Map<?, ?> getParameterMap() {

        return getWebContext().getParameterMap();
    }

    @Override
    public String getRealPath(String path) {

        return getWebContext().getRealPath(path);
    }

    @Override
    public Object getRequestAttribute(String name) {

        return getWebContext().getRequestAttribute(name);
    }

    @Override
    public Object getSessionAttribute(String name) {

        return getWebContext().getSessionAttribute(name);
    }

    @Override
    public Writer getWriter() {

        return getWebContext().getWriter();
    }

    @Override
    public void removeApplicationAttribute(String name) {

        getWebContext().removeApplicationAttribute(name);
    }

    @Override
    public void removePageAttribute(String name) {

        getWebContext().removePageAttribute(name);
    }

    @Override
    public void removeRequestAttribute(String name) {

        getWebContext().removeRequestAttribute(name);
    }

    @Override
    public void removeSessionAttribute(String name) {

        getWebContext().removeSessionAttribute(name);
    }

    @Override
    public void setApplicationAttribute(String name, Object value) {

        getWebContext().setApplicationAttribute(name, value);
    }

    @Override
    public void setLocale(Locale locale) {

        getWebContext().setLocale(locale);
    }

    @Override
    public void setPageAttribute(String name, Object value) {

        getWebContext().setPageAttribute(name, value);
    }

    @Override
    public void setParameterMap(Map<?, ?> parameterMap) {

        getWebContext().setParameterMap(parameterMap);
    }

    @Override
    public void setRequestAttribute(String name, Object value) {

        getWebContext().setRequestAttribute(name, value);
    }

    @Override
    public void setSessionAttribute(String name, Object value) {

        getWebContext().setSessionAttribute(name, value);
    }
}
