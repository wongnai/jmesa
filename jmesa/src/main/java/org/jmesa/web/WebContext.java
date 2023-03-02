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
 * @author Jeff Johnston
 * @since 2.0
 */
public interface WebContext {

    Object getApplicationInitParameter(String name);

    Object getApplicationAttribute(String name);

    void setApplicationAttribute(String name, Object value);

    void removeApplicationAttribute(String name);

    Object getPageAttribute(String name);

    void setPageAttribute(String name, Object value);

    void removePageAttribute(String name);

    String getParameter(String name);

    Map<?, ?> getParameterMap();

    void setParameterMap(Map<String, Object> parameterMap);

    Object getRequestAttribute(String name);

    void setRequestAttribute(String name, Object value);

    void removeRequestAttribute(String name);

    Object getSessionAttribute(String name);

    void setSessionAttribute(String name, Object value);

    void removeSessionAttribute(String name);

    Writer getWriter();

    Locale getLocale();

    void setLocale(Locale locale);

    String getContextPath();

    String getRealPath(String path);

    Object getBackingObject();
}
