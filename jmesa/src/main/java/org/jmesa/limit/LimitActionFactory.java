package org.jmesa.limit;

public interface LimitActionFactory {
    String getId();

    Integer getMaxRows();

    int getPage();

    FilterSet getFilterSet();

    SortSet getSortSet();

    String getExportType();
}
