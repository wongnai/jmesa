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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Figure out how the user interacted with the table.
 *
 * @author xwx
 * @since 4.1
 */
public class LimitActionFactoryJsonImpl implements LimitActionFactory {

    public static final class Action {
        public static final String CLEAR_FILTER = "clear_filter";
        public static final String CLEAR_WORKSHEET = "clear_worksheet";
        public static final String SAVE_WORKSHEET = "save_worksheet";
        public static final String FILTER_WORKSHEET = "filter_worksheet";
        public static final String ADD_WORKSHEET_ROW = "add_worksheet_row";
    }

    private Logger logger = LoggerFactory.getLogger(LimitActionFactoryJsonImpl.class);

    private final String id;
    private Map<String, Object> data;

    public static final class Keys {
        public static final String ID = "id";
        public static final String ACTION = "action";
        public static final String MAX_ROWS = "maxRows";
        public static final String PAGE = "page";
        public static final String FILTER = "filter";
        public static final String SORT = "sort";
        public static final String EXPORT_TYPE = "exportType";
    }

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
     * @param json
     */
    public LimitActionFactoryJsonImpl(String id, String json) {
        this.id = id;
        ObjectMapper mapper = new ObjectMapper();
        try {
            data = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id   table id.
     * @param data data must be LinkedHashMap of `String, Object` pairs,  where `Object` must Number, Boolean, String or
     *             LinkedHashMap of `String, Object`.
     */
    public LimitActionFactoryJsonImpl(String id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
    }


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
        int maxRows = LimitUtils.getIntValue(data.get(Keys.MAX_ROWS));
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

        int page = LimitUtils.getIntValue(data.get(Keys.PAGE));
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

        String clear = LimitUtils.getValue(data.get(Keys.ACTION));
        if (Action.CLEAR_FILTER.equalsIgnoreCase(clear)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cleared out the filters.");
            }
            return filterSet;
        }

        Object o = data.get(Keys.FILTER);
        if (o instanceof Map) {

            //TODO remove this
            Map<String, Object> map = (Map<String, Object>) o;
            for (String property : map.keySet()) {
                Object v = map.get(property);
                Filter filter = buildFilter(property,Comparison.CONTAIN, v);
                if(filter!=null){
                    filterSet.addFilter(filter);
                }
            }
        }else if(o instanceof List){
            List<Object> list = (List<Object>) o;
            for (Object map : list) {
                String property = (String) ((Map)map).get("key");
                Comparison comparison = Comparison.valueOf(((Map)map).get("comparison").toString().toUpperCase());

                Object[] value =( (List) ((Map)map).get("value")).toArray();

                Filter filter = buildFilter(property,comparison, value);
                if(filter!=null){
                    filterSet.addFilter(filter);
                }
            }

        }

        return filterSet;
    }

    @Override
    public SortSet getSortSet() {

        SortSet sortSet = new SortSet();
        Object o = data.get(Keys.SORT);
        if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            for (String property : map.keySet()) {
                String value = LimitUtils.getValue(map.get(property));
                if (StringUtils.isNotBlank(value)) {
                    Order order = Order.valueOfParam(value);
                    sortSet.addSort(property, order);
                }
            }
        }

        return sortSet;
    }

    /**
     * @return The current export type based on what the user selected.
     */
    @Override
    public String getExportType() {

        String exportType = LimitUtils.getValue(data.get(Keys.EXPORT_TYPE));

        if (logger.isDebugEnabled()) {
            logger.debug("Export Type: " + exportType == null ? "" : exportType);
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
