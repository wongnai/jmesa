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
package org.jmesa.facade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jmesa.core.message.Messages;
import org.jmesa.core.message.MessagesFactory;
import org.jmesa.core.message.SpringMessages;
import org.jmesa.web.HttpServletRequestSpringWebContext;
import org.jmesa.web.SpringWebContext;
import org.jmesa.web.WebContext;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * A factory to create TableFacade implementations.
 *
 * @since 2.2
 * @author Jeff Johnston
 */
public class TableFacadeFactory {

    public static TableFacade createTableFacade(String id, HttpServletRequest request) {

        TableFacade tableFacade = new TableFacade(id, request);
        return tableFacade;
    }

    public static TableFacade createTableFacade(String id, HttpServletRequest request, HttpServletResponse response) {

        TableFacade tableFacade = new TableFacade(id, request, response);
        return tableFacade;
    }

    public static TableFacade createTableFacade(String id, WebContext webContext) {

        TableFacade tableFacade = new TableFacade(id, null);
        tableFacade.setWebContext(webContext);
        return tableFacade;
    }

    public static TableFacade createTableFacade(String id, WebContext webContext, HttpServletResponse response) {

        TableFacade tableFacade = new TableFacade(id, null, response);
        tableFacade.setWebContext(webContext);
        return tableFacade;
    }

//    public static TableFacade createPortletTableFacade(String id, PortletRequest request) {
//
//        return createTableFacade(id, new PortletRequestWebContext(request));
//    }
//
//    public static TableFacade createSpringPortletTableFacade(String id, PortletRequest request) {
//
//        SpringWebContext springWebContext = new PortletRequestSpringWebContext(request);
//        return createSpringTableFacade(id, springWebContext);
//    }

    public static TableFacade createSpringTableFacade(String id, HttpServletRequest request) {

        SpringWebContext springWebContext = new HttpServletRequestSpringWebContext(request);
        return createSpringTableFacade(id, springWebContext);
    }

    public static TableFacade createSpringTableFacade(String id, HttpServletRequest request, HttpServletResponse response) {

        SpringWebContext springWebContext = new HttpServletRequestSpringWebContext(request);
        return createSpringTableFacade(id, springWebContext, response);
    }

    public static TableFacade createSpringTableFacade(String id, SpringWebContext springWebContext) {

        springWebContext.setLocale(LocaleContextHolder.getLocale());

        TableFacade tableFacade = createTableFacade(id, springWebContext);
        Messages messages = MessagesFactory.getMessages(springWebContext);
        SpringMessages springMessages = new SpringMessages(messages, springWebContext);
        tableFacade.setMessages(springMessages);
        return tableFacade;
    }

    public static TableFacade createSpringTableFacade(String id, SpringWebContext springWebContext, HttpServletResponse response) {

        springWebContext.setLocale(LocaleContextHolder.getLocale());

        TableFacade tableFacade = createTableFacade(id, springWebContext, response);
        Messages messages = MessagesFactory.getMessages(springWebContext);
        SpringMessages springMessages = new SpringMessages(messages, springWebContext);
        tableFacade.setMessages(springMessages);
        return tableFacade;
    }



}
