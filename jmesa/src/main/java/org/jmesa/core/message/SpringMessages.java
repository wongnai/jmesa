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
package org.jmesa.core.message;

import org.jmesa.web.SpringWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * The Spring specific messages. Will use the default messages if they are not defined in Spring.
 *
 * @author Oscar Perez
 * @since 2.3.3
 */
public class SpringMessages implements Messages {

    private static final Logger logger = LoggerFactory.getLogger(SpringMessages.class);

    private final Messages defaultMessages;
    private final SpringWebContext springWebContext;
    private final MessageSource messageSource;

    public SpringMessages(Messages defaultMessages, SpringWebContext springWebContext) {

        this.defaultMessages = defaultMessages;
        this.springWebContext = springWebContext;
        this.messageSource = springWebContext.getApplicationContext();
    }

    /**
     * Try to get the messages from Spring first or else retrieve from the default messages.
     */
    @Override
    public String getMessage(String code) {

        return getMessage(code, null);
    }

    /**
     * Try to get the messages from Spring first or else retrieve from the default messages.
     */
    @Override
    public String getMessage(String code, Object[] args) {

        if (messageSource == null) {
            logger.warn("There is no Spring MessageSource defined. Will get the default messages instead.");
            return defaultMessages.getMessage(code, args);
        }

        String message;

        try {
            message = messageSource.getMessage(code, args, springWebContext.getLocale());
        } catch (NoSuchMessageException ex) {
            message = defaultMessages.getMessage(code, args);
        }

        return message;
    }
}
