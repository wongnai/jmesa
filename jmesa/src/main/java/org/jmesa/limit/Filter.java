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
package org.jmesa.limit;

import org.apache.commons.lang.NotImplementedException;
import org.jmesa.core.filter.StringWildCardFilterMatcher;

import java.io.Serializable;

/**
 * <p>
 * An immutable class that is used to reduce the rows that are returned for a
 * table. The property is the Bean (Or Map) attribute that will used to reduce
 * the results based on the value. Or, in other words, it is simply the column
 * that the user is trying to filter and the value that they entered.
 * </p>
 *
 * <p>
 * The property can use dot (.) notation to access nested classes. For example
 * if you have an object called President that is composed with another object
 * called Name then your property would be name.firstName
 * </p>
 *
 * <pre>
 * public class President {
 *     private Name name;
 *
 *     public Name getName() {
 *         return name;
 *     }
 * }
 *
 * public class Name {
 *     private String firstName;
 *
 *     public String getFirstName() {
 *         return firstName;
 *     }
 * }
 * </pre>
 *
 *
 * @since 2.0
 * @author Jeff Johnston
 */
public interface Filter extends Serializable {

    /**
     * The Bean (Or Map) attribute used to reduce the results.
     */
    String getProperty();

    Object[] getValue();

    /**
     * since 4.3
     * @return comparison operator
     */
    Comparison getComparison();

    /**
     * Filter factory
     * @param property
     * @param value
     * @return filter
     */
    static Filter build(String property, Comparison operator,  Object... value){
        switch (operator) {
            case GT:
            case IS:
            case LT:
            case GTE:
            case LTE:
            case IS_NOT:
            case CONTAIN:
                return new SingleValueFilter(property, operator,  value);
            case IS_NULL:
            case IS_NOT_NULL:
                return new SingleValueFilter(property, operator, null);
            case NOT_IN:
            case IN:

            case BETWEEN:
            case NOT_BETWEEN:
                return new RangeFilter(property, operator, value);
            case EXISTS:
            case NOT_EXISTS:
            default:
                throw new NotImplementedException("Unknown type:"+operator);
        }
    }

}
