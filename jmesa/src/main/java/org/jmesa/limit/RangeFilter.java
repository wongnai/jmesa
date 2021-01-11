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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

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
public final class RangeFilter extends BaseFilter implements Filter{
    private static  final Logger logger = LoggerFactory.getLogger(RangeFilter.class);

    public static class Pair{


        String startValueInclusive;
        String endValueExclusive;
        public Pair(String startValueInclusive, String endValueExclusive){
            this.startValueInclusive = startValueInclusive;
            this.endValueExclusive = endValueExclusive;
        }

        public String getStartValueInclusive() {
            return startValueInclusive;
        }

        public String getEndValueExclusive() {
            return endValueExclusive;
        }

        /**
         * ISO8601 formatter for date without time zone.
         * The format used is <tt>yyyy-MM-dd</tt>.
         */
        public static final String ISO_DATE_FORMAT="yyyy-MM-dd";

        /**
         * ISO8601 formatter for date-time without time zone.
         * The format used is <tt>yyyy-MM-dd'T'HH:mm:ss</tt>.
         */
        public static final String  ISO_DATETIME_FORMAT="yyyy-MM-dd'T'HH:mm:ss";

        /**
         * ISO8601 formatter for date-time with time zone.
         * The format used is <tt>yyyy-MM-dd'T'HH:mm:ssZZ</tt>.
         */
        public static final String ISO_DATETIME_TIME_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ";
        public static final String DEFAULT_DATETIME_TIME_ZONE_FORMAT = "yyyy-MM-dd HH:mm:ss";



        /**
         * ISO8601-like formatter for date with time zone.
         * The format used is <tt>yyyy-MM-ddZZ</tt>.
         * This pattern does not comply with the formal ISO8601 specification
         * as the standard does not allow a time zone  without a time.
         */
        public static final String ISO_DATE_TIME_ZONE_FORMAT = "yyyy-MM-ddZZ";

        public static final String[] DATE_PATTERNS={
                ISO_DATE_FORMAT,
                ISO_DATE_TIME_ZONE_FORMAT
        };
        public static final String[] DATETIME_PATTERNS={
                DEFAULT_DATETIME_TIME_ZONE_FORMAT,
                ISO_DATETIME_FORMAT,
                ISO_DATETIME_TIME_ZONE_FORMAT
        };

        @Override
        public boolean equals(Object o) {
            if (this == o){
                return true;
            }
            if (o == null || getClass() != o.getClass()){
                return false;
            }
            Pair pair = (Pair) o;
            return Objects.equals(startValueInclusive, pair.startValueInclusive) && Objects.equals(endValueExclusive, pair.endValueExclusive);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startValueInclusive, endValueExclusive);
        }

        @Override
        public String toString() {
            return "[" +
                    "\"" + StringEscapeUtils.escapeJavaScript(startValueInclusive) + '\"' +
                    ", \"" + StringEscapeUtils.escapeJavaScript(endValueExclusive) + '\"' +
                    ']';
        }

        public boolean inRange(Object itemValue) {
            if(itemValue instanceof Number){
                double v= Double.parseDouble(String.valueOf(itemValue));
                double b= Double.parseDouble(startValueInclusive);
                double e = Double.parseDouble(endValueExclusive);
                return v>=b && v<e;
            }
            if(itemValue instanceof Date){
                Date d =  (Date)itemValue;
                try {
                    return !d.before(DateUtils.parseDate(startValueInclusive, DATE_PATTERNS))
                            && d.before(DateUtils.parseDate(endValueExclusive, DATE_PATTERNS));
                }catch (ParseException e){
                    logger.warn("Unknown date format: "+startValueInclusive+","+endValueExclusive,e);
                }
                return false;
            }

            if(itemValue instanceof DateTime){
                DateTime d =  (DateTime)itemValue;
                    try {
                        return !d.isBefore(DateUtils.parseDate(startValueInclusive, DATETIME_PATTERNS).getTime())
                                && d.isBefore(DateUtils.parseDate(endValueExclusive, DATETIME_PATTERNS).getTime());
                    }catch (ParseException e){
                        logger.warn("Unknown date format: "+startValueInclusive+","+endValueExclusive,e);
                    }
                    return false;
            }

            return false;
        }
    }

    public RangeFilter(String property, Pair value) {
        super(property, value);
    }

    public String getStartValue() {
        return ((Pair)value).startValueInclusive;
    }
    public String getEndValue() {
        return ((Pair)value).endValueExclusive;
    }

}
