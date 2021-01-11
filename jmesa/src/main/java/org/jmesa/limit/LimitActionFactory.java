package org.jmesa.limit;

import org.apache.commons.lang.StringUtils;

/**
 * @author xwx
 */
public interface LimitActionFactory {
    String getId();

    int getMaxRows();

    int getPage();

    FilterSet getFilterSet();

    SortSet getSortSet();

    String getExportType();

    /**
     * Create Filter, value mast be one of String, separated by ~, or list of [beginInclusiveValue, endExclusiveValue]
     * @param property field name
     * @param filterObject string or list of string
     * @return null if format error
     */
    default Filter buildFilter(String property, Object  filterObject){
        RangeFilter.Pair pair = LimitUtils.getPairValue(filterObject);
        if(pair!=null){
            return  Filter.build(property, pair);
        }else {
            String value = LimitUtils.getValue(filterObject);
            if (StringUtils.isNotBlank(value)) {
                return Filter.build(property, value);
            }
        }
        return null;
    }
}
