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

import org.apache.commons.lang3.StringUtils;
import org.jmesa.limit.Comparison;

/**
 * Will do a case insensitive string match.
 *
 * @author Jeff Johnston
 * @since 2.0
 */
public class StringFilterMatcher implements FilterMatcher {

    @Override
    public boolean evaluate(Object itemValue, Comparison comparison, Object... filterValue) {

        if(itemValue==null && filterValue==null && comparison == Comparison.IS_NULL){
            return true;
        }
        String item = StringUtils.lowerCase(String.valueOf(itemValue));
        if(filterValue!=null) {
            for (Object o : filterValue) {
                String filter = StringUtils.lowerCase(String.valueOf(o));
                return StringUtils.contains(item, filter);
            }
        }
        return false;
    }
}
