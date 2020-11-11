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

import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;

/**
 * @author bgould
 * @version 2.3.4
 */
public class PortletRequestSpringWebContext extends PortletRequestWebContext implements SpringWebContext {

    public PortletRequestSpringWebContext(PortletRequest request) {

        super(request);
    }

    public PortletRequestSpringWebContext(PortletRequest request, PortletContext context, int sessionScope) {

        super(request, context, sessionScope);
    }

    @Override
    public ApplicationContext getApplicationContext() {

        return PortletApplicationContextUtils.getWebApplicationContext(getPortletContext());
    }
}
