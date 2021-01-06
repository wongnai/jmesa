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

import org.junit.jupiter.api.Test;

/**
 * @since 4.0
 * @author Jason Ward
 */
/**
 * @author jward
 *
 */
public class StringWildCardFilterMatcherTest {

    @Test
    public void evaluateTest() {

        StringWildCardFilterMatcher match = new StringWildCardFilterMatcher();

        boolean evaluate = match.evaluate(null, "geo");
        assertFalse(evaluate);

        evaluate = match.evaluate("george", null);
        assertFalse(evaluate);

        evaluate = match.evaluate("george", "geo");
        assertTrue(evaluate);

        evaluate = match.evaluate("george", "g*");
        assertTrue(evaluate);

        evaluate = match.evaluate("washington", "g*");
        assertFalse(evaluate);

        evaluate = match.evaluate("george", "g(");
        assertFalse(evaluate);

        evaluate = match.evaluate("George", "Geo");
        assertTrue(evaluate);
    }

    @Test
    public void testCaseInsensitivity() {

        StringWildCardFilterMatcher match = new StringWildCardFilterMatcher();

        boolean evaluate = false;

        evaluate = match.evaluate("george", "george");
        assertTrue(evaluate);

        evaluate = match.evaluate("george", "GEORGE");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", "george");
        assertTrue(evaluate);

    }

    @Test
    public void testRegExChars() {

        StringWildCardFilterMatcher match = new StringWildCardFilterMatcher();

        boolean evaluate = false;

        evaluate = match.evaluate("george)", "g");
        assertTrue(evaluate);

        evaluate = match.evaluate("george)", "george");
        assertTrue(evaluate);

        evaluate = match.evaluate("george)", "george)");
        assertTrue(evaluate);

        evaluate = match.evaluate("george)", "GEORGE)");
        assertTrue(evaluate);

        evaluate = match.evaluate("ge)", "GE*)");
        assertTrue(evaluate);

        evaluate = match.evaluate("ge)", "GE*");
        assertTrue(evaluate);

        evaluate = match.evaluate("george", "GE*RGE");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", "ge?rge");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", "ge?r*e");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", "w*s?i*g?on");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", "w*s?i*t?n");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", "w*s?i*g?n");
        assertFalse(evaluate);

        evaluate = match.evaluate("WASHING", "w*s?i*g?");
        assertFalse(evaluate);

        evaluate = match.evaluate("WASHINGTON", "w*s?i*g?");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", "w*h?n*o?");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", "w*h?n*o?*");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", "ge)rge");
        assertFalse(evaluate);

    }
}
