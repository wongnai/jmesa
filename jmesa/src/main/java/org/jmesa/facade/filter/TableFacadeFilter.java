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
package org.jmesa.facade.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * A filter to put the request and response on the ThreadLocal to pass to the
 * TableFacade implementation. Very useful for controllers in frameworks that
 * try to keep the request and response inaccessable.
 * </p>
 *
 * <p>
 *  &lt;filter&gt;
 *     &lt;filter-name&gt;TableFacadeFilter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;org.jmesa.facade.filter.TableFacadeFilter&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 *
 *  &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;TableFacadeFilter&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * <p>
 *
 * @since 2.2
 * @author Jeff Johnston
 */
public class TableFacadeFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        FilterThreadLocal.set((HttpServletRequest) request, (HttpServletResponse) response);

        // run the other filters
        chain.doFilter(request, response);

        FilterThreadLocal.set(null, null);
    }

    @Override
    public void destroy() {}

    public static class FilterThreadLocal {

        private static ThreadLocal<FilterValue> tLocal = new ThreadLocal<>();

        static void set(HttpServletRequest request, HttpServletResponse response) {

            if (request == null && response == null) {
                tLocal.set(null);
                return;
            }

            tLocal.set(new FilterValue(request, response));
        }

        public static HttpServletRequest getHttpServletRequest() {

            FilterValue filterValue = tLocal.get();
            return filterValue.getRequest();
        }

        public static HttpServletResponse getHttpServletResponse() {

            FilterValue filterValue = tLocal.get();
            return filterValue.getResponse();
        }

        private static class FilterValue {

            private final HttpServletRequest request;
            private final HttpServletResponse response;

            public FilterValue(HttpServletRequest request, HttpServletResponse response) {

                this.request = request;
                this.response = response;
            }

            public HttpServletRequest getRequest() {

                return request;
            }

            public HttpServletResponse getResponse() {

                return response;
            }
        }
    }
}
