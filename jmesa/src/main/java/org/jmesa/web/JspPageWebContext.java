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

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.PageContext;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jeff Johnston
 * @since 2.0
 */
public class JspPageWebContext implements WebContext {

    private final PageContext pageContext;
    private Map<?, ?> parameterMap;
    private Locale locale;

    public JspPageWebContext(PageContext pageContext) {

        this.pageContext = pageContext;
    }

    protected PageContext getPageContext() {

        return pageContext;
    }

    @Override
    public Object getApplicationInitParameter(String name) {

        return pageContext.getServletContext().getInitParameter(name);
    }

    @Override
    public Object getApplicationAttribute(String name) {

        return pageContext.getServletContext().getAttribute(name);
    }

    @Override
    public void setApplicationAttribute(String name, Object value) {

        pageContext.getServletContext().setAttribute(name, value);
    }

    @Override
    public void removeApplicationAttribute(String name) {

        pageContext.getServletContext().removeAttribute(name);
    }

    @Override
    public Object getPageAttribute(String name) {

        return pageContext.getAttribute(name);
    }

    @Override
    public void setPageAttribute(String name, Object value) {

        pageContext.setAttribute(name, value);
    }

    @Override
    public void removePageAttribute(String name) {

        pageContext.removeAttribute(name);
    }

    @Override
    public String getParameter(String name) {

        return pageContext.getRequest().getParameter(name);
    }

    @Override
    public Map<?, ?> getParameterMap() {

        if (parameterMap != null) {
            return parameterMap;
        }

        return pageContext.getRequest().getParameterMap();
    }

    @Override
    public void setParameterMap(Map<?, ?> parameterMap) {

        this.parameterMap = parameterMap;
    }

    @Override
    public Object getRequestAttribute(String name) {

        return pageContext.getRequest().getAttribute(name);
    }

    @Override
    public void setRequestAttribute(String name, Object value) {

        pageContext.getRequest().setAttribute(name, value);
    }

    @Override
    public void removeRequestAttribute(String name) {

        pageContext.getRequest().removeAttribute(name);
    }

    @Override
    public Object getSessionAttribute(String name) {

        return pageContext.getSession().getAttribute(name);
    }

    @Override
    public void setSessionAttribute(String name, Object value) {

        pageContext.getSession().setAttribute(name, value);
    }

    @Override
    public void removeSessionAttribute(String name) {

        pageContext.getSession().removeAttribute(name);
    }

    @Override
    public Writer getWriter() {

        return pageContext.getOut();
    }

    @Override
    public Locale getLocale() {

        if (locale != null) {
            return locale;
        }

        return pageContext.getRequest().getLocale();
    }

    @Override
    public void setLocale(Locale locale) {

        if (this.locale == null) {
            this.locale = locale;
        }
    }

    @Override
    public String getContextPath() {

        ServletRequest request = pageContext.getRequest();
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getContextPath();
        }

        throw new UnsupportedOperationException("There is no context path associated with the request.");
    }

    @Override
    public String getRealPath(String path) {

        if (pageContext.getRequest() instanceof HttpServletRequest) {
            return ((HttpServletRequest) pageContext.getRequest()).getSession().getServletContext().getRealPath(path);
        }

        throw new UnsupportedOperationException("There is no real path associated with the request.");
    }

    @Override
    public PageContext getBackingObject() {

        return pageContext;
    }
}
