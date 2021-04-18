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

        boolean evaluate = match.evaluate(null, Comparison.CONTAIN, "geo");
        assertFalse(evaluate);

        evaluate = match.evaluate("george", Comparison.IS_NULL, null);
        assertFalse(evaluate);

        evaluate = match.evaluate("george", Comparison.CONTAIN,"geo");
        assertTrue(evaluate);

        evaluate = match.evaluate("george", Comparison.CONTAIN, "g*");
        assertTrue(evaluate);

        evaluate = match.evaluate("washington",Comparison.CONTAIN, "g*");
        assertFalse(evaluate);

        evaluate = match.evaluate("george",Comparison.CONTAIN, "g(");
        assertFalse(evaluate);

        evaluate = match.evaluate("George",Comparison.CONTAIN, "Geo");
        assertTrue(evaluate);
    }

    @Test
    public void testCaseInsensitivity() {

        StringWildCardFilterMatcher match = new StringWildCardFilterMatcher();

        boolean evaluate = false;

        evaluate = match.evaluate("george", Comparison.CONTAIN, "george");
        assertTrue(evaluate);

        evaluate = match.evaluate("george", Comparison.CONTAIN,"GEORGE");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", Comparison.CONTAIN,"george");
        assertTrue(evaluate);

    }

    @Test
    public void testRegExChars() {

        StringWildCardFilterMatcher match = new StringWildCardFilterMatcher();

        boolean evaluate = false;

        evaluate = match.evaluate("george)", Comparison.CONTAIN, "g");
        assertTrue(evaluate);

        evaluate = match.evaluate("george)", Comparison.CONTAIN, "george");
        assertTrue(evaluate);

        evaluate = match.evaluate("george)", Comparison.CONTAIN, "george)");
        assertTrue(evaluate);

        evaluate = match.evaluate("george)", Comparison.CONTAIN,"GEORGE)");
        assertTrue(evaluate);

        evaluate = match.evaluate("ge)", Comparison.CONTAIN,"GE*)");
        assertTrue(evaluate);

        evaluate = match.evaluate("ge)", Comparison.CONTAIN,"GE*");
        assertTrue(evaluate);

        evaluate = match.evaluate("george",Comparison.CONTAIN, "GE*RGE");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", Comparison.CONTAIN,"ge?rge");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", Comparison.CONTAIN,"ge?r*e");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", Comparison.CONTAIN,"w*s?i*g?on");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON",Comparison.CONTAIN, "w*s?i*t?n");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", Comparison.CONTAIN,"w*s?i*g?n");
        assertFalse(evaluate);

        evaluate = match.evaluate("WASHING", Comparison.CONTAIN,"w*s?i*g?");
        assertFalse(evaluate);

        evaluate = match.evaluate("WASHINGTON", Comparison.CONTAIN,"w*s?i*g?");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON",Comparison.CONTAIN, "w*h?n*o?");
        assertTrue(evaluate);

        evaluate = match.evaluate("WASHINGTON", Comparison.CONTAIN,"w*h?n*o?*");
        assertTrue(evaluate);

        evaluate = match.evaluate("GEORGE", Comparison.CONTAIN,"ge)rge");
        assertFalse(evaluate);

    }
}
