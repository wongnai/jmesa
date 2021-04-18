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
package org.jmesa.core.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jmesa.limit.Comparison;
import org.junit.jupiter.api.Test;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class StringFilterMatcherTest {

	@Test
	public void evaluateTest() {

		StringFilterMatcher match = new StringFilterMatcher();

		boolean evaluate = match.evaluate(null, Comparison.CONTAIN, "geo");
		assertFalse(evaluate);

		evaluate = match.evaluate("george",Comparison.CONTAIN, null);
		assertFalse(evaluate);

		evaluate = match.evaluate("george", Comparison.CONTAIN, "geo");
		assertTrue(evaluate);

		evaluate = match.evaluate("George",Comparison.CONTAIN,  "Geo");
		assertTrue(evaluate);
	}
}
