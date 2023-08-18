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

import com.bixuebihui.jmesa.entry.Query;
import com.bixuebihui.jmesa.entry.Sort;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Figure out how the user interacted with the table.
 *
 * @author xwx
 * @since 4.1
 */
public class LimitActionFactoryQueryImpl implements LimitActionFactory {

    private final String id;
    private Logger logger = LoggerFactory.getLogger(LimitActionFactoryQueryImpl.class);
    private Query data;

    /**
     * json format
     * {
     * "id": "table-id",
     * "action": "",
     * "maxRows": 500,
     * "page": 123,
     * "filter": {
     * "property1":value1,
     * "property2":value2,
     * },
     * "sort":{
     * "property1":"asc",
     * "property2":"desc",
     * },
     * "exportType":"json",
     * }
     *
     * @param id
     * @param query
     */
    public LimitActionFactoryQueryImpl(String id, Query query) {
        this.id = id;
        this.data = query;
    }

    /**
     * @return table id.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return The max rows based on what the user selected. A null returned implies the default
     * must be used.
     */
    @Override
    public int getMaxRows() {
        int maxRows = LimitUtils.getIntValue(data.getMaxRows());
        if (logger.isDebugEnabled()) {
            logger.debug("Max Rows:" + maxRows);
        }
        return maxRows;
    }

    /**
     * @return The current page based on what the user selected. The default is to return the first
     * page.
     */
    @Override
    public int getPage() {

        int page = LimitUtils.getIntValue(data.getPage());
        if (logger.isDebugEnabled()) {
            logger.debug("On Page :" + page);
        }
        if (page == 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Defaulting to Page 1");
            }
            return 1;
        } else {
            return page;
        }

    }

    @Override
    public FilterSet getFilterSet() {

        FilterSet filterSet = new FilterSet();

//        if (data.getFilterSet() != null) {
//            return data.getFilterSet();
//        }

        List<Filter> sets = data.getFilter().stream().map(filter ->
                new BaseFilter(filter.getKey(),
                        Comparison.valueOf(filter.getComparison()),
                        filter.getValue().toArray())).collect(Collectors.toList());

        extractFilterSet(filterSet, sets);
        filterSet.setOperator(FilterSet.Operator.AND);

        filterSet.setFilterSets(Collections.singletonList(data.getFilterSet()));

        return filterSet;
    }



    private void extractFilterSet(FilterSet filterSet, List<Filter> filters) {
        filters.forEach(it -> filterSet.addFilter(it));
    }


    @Override
    public SortSet getSortSet() {

        SortSet sortSet = new SortSet();
        List<Sort> sort = data.getSort();

        AtomicInteger atom = new AtomicInteger();
        sort.forEach(s -> {
            if (s != null && s.getOrder() != null) {
                sortSet.addSort(new org.jmesa.limit.Sort(atom.getAndIncrement(),
                        s.getField(), Order.valueOfParam(s.getOrder().toLowerCase())));
            }
        });


        return sortSet;
    }

    /**
     * @return The current export type based on what the user selected.
     */
    @Override
    public String getExportType() {

        String exportType = LimitUtils.getValue(data.getExportType());

        if (logger.isDebugEnabled()) {
            logger.debug("Export Type: " + (exportType == null ? "" : exportType));
        }

        return exportType;
    }

    @Override
    public String toString() {

        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("id", id);
        if (data != null) {
            builder.append(data.toString());
        }
        return builder.toString();
    }

}
