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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

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
 * @since 4.1
 * @author Xing Wanxiang
 */
public final class InFilter extends BaseFilter implements Filter{
    private static  final Logger logger = LoggerFactory.getLogger(InFilter.class);


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public InFilter(@JsonProperty("property") String property,
                    @JsonProperty("comparison")  Comparison comparison,
                    @JsonProperty("value")  Object[] value) {
        super(property, comparison, value);
    }

}
