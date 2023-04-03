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


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jeff Johnston
 * @since 2.0
 */
public class HttpServletRequestWebContext implements WebContext {

    private final HttpServletRequest request;
    private final ServletContext ctx;
    private Map<String, Object> parameterMap;
    private Locale locale;

    public HttpServletRequestWebContext(HttpServletRequest request, ServletContext ctx) {

        this.request = request;
        this.ctx = ctx;
    }

    public HttpServletRequestWebContext(HttpServletRequest request) {

        this.request = request;
        HttpSession session= request==null? null : request.getSession();
        if(session!=null) {
            this.ctx = session.getServletContext();
        }else{
            this.ctx = null;
        }
    }

    /**
     * @param map
     * @since 4.1
     */
    public HttpServletRequestWebContext(Map<String, Object> map) {
        this.request = null;
        this.ctx = null;
        this.parameterMap = map;
    }


    protected HttpServletRequest getHttpServletRequest() {
        return request;
    }

    @Override
    public Object getApplicationInitParameter(String name) {
        if(ctx==null) {
            return null;
        }
        return ctx.getInitParameter(name);
    }

    @Override
    public Object getApplicationAttribute(String name) {
        if(ctx==null) {
            return null;
        }
        return ctx.getAttribute(name);
    }

    @Override
    public void setApplicationAttribute(String name, Object value) {
        if(ctx==null) {
            return ;
        }
        ctx.setAttribute(name, value);
    }

    @Override
    public void removeApplicationAttribute(String name) {
        if(ctx==null) {
            return ;
        }
        ctx.removeAttribute(name);
    }

    @Override
    public Object getPageAttribute(String name) {

        return request.getAttribute(name);
    }

    @Override
    public void setPageAttribute(String name, Object value) {

        request.setAttribute(name, value);
    }

    @Override
    public void removePageAttribute(String name) {

        request.removeAttribute(name);
    }

    @Override
    public String getParameter(String name) {

        if (parameterMap != null) {
            String[] values = WebContextUtils.getValueAsArray(parameterMap.get(name));
            if (values != null && values.length > 0) {
                return values[0];
            }
        }

        return request.getParameter(name);
    }

    @Override
    public Map<?, ?> getParameterMap() {

        if (parameterMap != null) {
            return parameterMap;
        }

        return request.getParameterMap();
    }

    @Override
    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public Object getRequestAttribute(String name) {
        if(request!=null)
            return request.getAttribute(name);
        return parameterMap.get(name);
    }

    @Override
    public void setRequestAttribute(String name, Object value) {
        if(request!=null)
            request.setAttribute(name, value);
        else parameterMap.put(name, value);
    }

    @Override
    public void removeRequestAttribute(String name) {
        if(request!=null)
            request.removeAttribute(name);
        else parameterMap.remove(name);
    }

    @Override
    public Object getSessionAttribute(String name) {
        HttpSession session= request.getSession();
        if(session!=null) {
            return session.getAttribute(name);
        }
        return null;
    }


    @Override
    public void setSessionAttribute(String name, Object value) {
        if(request!=null) {
            HttpSession session = request.getSession();
            if (session != null) {
                session.setAttribute(name, value);
            }
        }
    }

    @Override
    public void removeSessionAttribute(String name) {
        if(request!=null) {
            HttpSession session = request.getSession();
            if (session != null) {
                session.removeAttribute(name);
            }
        }
    }

    @Override
    public Writer getWriter() {

        return new StringWriter();
    }

    @Override
    public Locale getLocale() {

        if (locale != null) {
            return locale;
        }

        return request==null? Locale.getDefault(): request.getLocale();
    }

    @Override
    public void setLocale(Locale locale) {

        if (this.locale == null) {
            this.locale = locale;
        }
    }

    @Override
    public String getContextPath() {
        if(request!=null) {
            return request.getContextPath();
        }
        return "";
    }

    @Override
    public String getRealPath(String path) {
        if(ctx!=null)
            return ctx.getRealPath(path);
        return path;
    }

    @Override
    public HttpServletRequest getBackingObject() {

        return request;
    }
}
