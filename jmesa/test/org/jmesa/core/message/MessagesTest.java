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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.jmesa.core.message.Messages;
import org.jmesa.web.HttpServletRequestWebContext;
import org.jmesa.web.WebContext;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class MessagesTest {

	@Test
	public void getPreference() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		WebContext webContext = new HttpServletRequestWebContext(request);
		webContext.setLocale(Locale.US);

		Messages messages = new ResourceBundleMessages("org.jmesa.core.message.testResourceBundle", webContext);
		String message = messages.getMessage("test.normal");
		assertNotNull(message);
		assertTrue(message.equals("foo"));

		message = messages.getMessage("test.args", new String[] {"1"});
		assertNotNull(message);
		assertTrue(message.equals("foo 1"));
	}
}
