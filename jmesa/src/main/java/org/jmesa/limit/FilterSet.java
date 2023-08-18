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

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * The FilterSet is an Collection of Filter objects. A Filter contains a bean property and the
 * filter value. Or, in other words, it is simply the column that the user is trying to filter and
 * the value that they entered.
 * </p>
 *
 * @since 2.0
 * @author Jeff Johnston
 */
public class FilterSet implements Serializable {
    private List<Filter> filters;
    private List<FilterSet> filterSets;
    private Operator operator;

    @Override
    public String toString() {
        return "{" +
                (filters.isEmpty() ? "": "filters=" + filters+", ") +
                (filterSets.isEmpty()? "": "filterSets=" + filterSets+", ") +
                "operator=" + operator +
                '}';
    }

    public FilterSet() {
        filters = new ArrayList<>();
        filterSets = new ArrayList<>();
        operator = Operator.AND;
    }

    public List<FilterSet> getFilterSets() {
        return filterSets;
    }

    public void setFilterSets(List<FilterSet> filterSets) {
        this.filterSets = filterSets;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public boolean isFilterable() {
        return isFiltered();
    }

    /**
     * @return Is true if there are any columns that need to be filtered.
     */
    public boolean isFiltered() {

        return filters != null && !filters.isEmpty();
    }

    /**
     * @return The set of Filter objects.
     */
    public Collection<Filter> getFilters() {
        return filters;
    }

    /**
     * <p>
     * For a given item property, retrieve the Filter object based on the property.
     * </p>
     *
     * @param property The Filter property, which is also a column property.
     * @return The Filter object.
     */
    public Filter getFilter(String property) {

        for (Iterator<Filter> iter = filters.iterator(); iter.hasNext();) {
            Filter filter = iter.next();
            if (filter.getProperty().equals(property)) {
                return filter;
            }
        }

        return null;
    }

    /**
     * <p>
     * For a given property, retrieve the Filter value.
     * </p>
     *
     * @param property The Filter property, which is also a column property.
     * @return The Filter value.
     */
    public String getFilterValue(String property) {
        Object[] v = getFilter(property).getValue();
        if(v.length==1) {
            return v[0].toString();
        }
        return Arrays.stream(v).map(k -> k.toString()).collect(Collectors.joining(","));
    }

    /**
     * @param filter The Filter to add to the set.
     */
    public void addFilter(Filter filter) {

        if (filters.contains(filter)) {
            filters.remove(filter);
        }

        filters.add(filter);
    }

    public enum Operator{
        /**
         * while set use AND operation
         */
        AND,
        /**
         * or use OR between all filters.
         */
        OR,
        /**
         * not use any operation
         */
        NOT

        // ; String value;
//        Operator(final String value) {
//            this.value = value;
//        }
//
//        @Override
//        public String toString() {
//            return  value;
//        }
    }
}
