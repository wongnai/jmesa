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

import org.jmesa.limit.Comparison;

/**
 * <p>
 * Used to filter out values.
 * </p>
 *
 * <p>
 * The following is a String matching example:
 * </p>
 *
 * <pre>
 * public boolean evaluate(Object itemValue, String matchValue) {
 *     String value = StringUtils.lowerCase((String) itemValue);
 *     if (StringUtils.contains(value, matchValue)) {
 *         return true;
 *     }
 *
 *     return false;
 * }
 * </pre>
 *
 * @author Jeff Johnston
 * @since 2.0
 */
public interface FilterMatcher {

    /**
     * <p>
     * Take the current item value and evaluate whether or not it is the same as
     * the match value.
     * </p>
     *
     * @param itemValue   The value that will be performing the match against.
     * @param filterValue The value to match with.
     * @return Is true if the itemValue and the matchValue are the same.
     */
    boolean evaluate(Object itemValue, Comparison comparison, Object... filterValue);
}
