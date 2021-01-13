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

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class LimitUtils {

    public static final String PAIR_STRING_SEPARATOR = "~";

    /**
     * Convert the input value to a String. A String[] or List will be converted
     * to a String by using the value in the first position. In addition will
     * attempt to do a String conversion for other object types using the
     * String.valueOf() method.
     *
     * @param value
     *            The input object to convert to a String.
     * @return The converted value.
     */
    public static String getValue(Object value) {

        if (value instanceof Object[]) {
            if (((Object[]) value).length > 0) {
                return String.valueOf(((Object[]) value)[0]);
            }
        } else if (value instanceof List) {
            List<?> valueList = (List<?>) value;
            if (valueList.size() > 0) {
                return String.valueOf(valueList.get(0));
            }
        }

        if (value != null) {
            return String.valueOf(value);
        }

        return "";
    }

    public static RangeFilter.Pair getPairValue(Object value) {

        if (value instanceof Object[]) {
            if (((Object[]) value).length > 1) {
                return new RangeFilter.Pair(String.valueOf(((Object[]) value)[0]), String.valueOf(((Object[]) value)[1]));
            }
        } else if (value instanceof List) {
            List<?> valueList = (List<?>) value;
            if (valueList.size() > 1) {
                return new RangeFilter.Pair(String.valueOf(((List) value).get(0)),String.valueOf(((List) value).get(1)));
            }
        }else if (value instanceof String) {
                String v =  String.valueOf(value);
                if(v.contains(PAIR_STRING_SEPARATOR)){
                    String[] s = v.split(PAIR_STRING_SEPARATOR);
                    return new RangeFilter.Pair(s[0],s[1]);
                }
            }
        return null;
    }


    public static int getIntValue(Object value){
        if(value==null) {
            return 0;
        }
        if(value instanceof Number){
            return ((Number)value).intValue();
        }else if(StringUtils.isNumeric(value.toString())){
            return Integer.parseInt(value.toString());
        }
        return 0;
    }
}
