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
package org.jmesa.model;

import org.jmesa.core.filter.FilterMatcher;
import org.jmesa.core.filter.FilterMatcherMap;
import org.jmesa.core.filter.MatcherKey;
import org.jmesa.core.filter.RowFilter;
import org.jmesa.core.message.Messages;
import org.jmesa.core.preference.Preferences;
import org.jmesa.core.sort.ColumnSort;
import org.jmesa.facade.TableFacade;
import org.jmesa.limit.Limit;
import org.jmesa.limit.LimitActionFactoryMapImpl;
import org.jmesa.limit.state.State;
import org.jmesa.view.View;
import org.jmesa.view.ViewExporter;
import org.jmesa.view.component.Table;
import org.jmesa.view.html.toolbar.Toolbar;
import org.jmesa.web.WebContext;
import org.jmesa.worksheet.Worksheet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.jmesa.model.TableModelUtils.getItems;

/**
 * @since 3.0
 * @author Jeff Johnston
 */
public class TableModel{

    private String id;
    private HttpServletRequest request;
    private Map<String, Object> requestParameters;
    private Collection<?> items;
    private PageItems pageItems;
    private AllItems allItems;
    private Preferences preferences;
    private Messages messages;
    private String[] exportTypes;
    private String exportFileName;
    private ViewExporter viewExporter;
    private State state;
    private String stateAttr;
    private Limit limit;
    private boolean autoFilterAndSort = true;
    private Map<MatcherKey, FilterMatcher> filterMatchers;
    private FilterMatcherMap filterMatcherMap;
    private ColumnSort columnSort;
    private RowFilter rowFilter;
    private int maxRows;
    private int[] maxRowsIncrements;
    private Toolbar toolbar;
    private View view;
    private Table table;
    private boolean editable;
    private WorksheetSaver worksheetSaver;
    private Object addedRowObject;

    private TableFacade tableFacade;

    // only used to subclass the model
    protected TableModel() {}

    public TableModel(String id, HttpServletRequest request) {
        this.id = id;
        this.request = request;
        this.tableFacade = new TableFacade(id, request);
    }

    public TableModel(String id, HttpServletRequest request, HttpServletResponse response) {

        this.id = id;
        this.request = request;
        this.tableFacade = new TableFacade(id, request, response);
    }

    public TableModel(String id, Map<String, Object> request, Map<String, Object> response){
        this.id = id;
        this.requestParameters = request;
        this.tableFacade = new TableFacade(id, request, response);
    }

    public TableModel(String id, WebContext webContext) {

        this.tableFacade = new TableFacade(id, null);
        tableFacade.setWebContext(webContext);
        setHttpServletRequest(tableFacade);
    }

    public TableModel(String id, WebContext webContext, HttpServletResponse response) {

        this.tableFacade = new TableFacade(id, null, response);
        tableFacade.setWebContext(webContext);
        setHttpServletRequest(tableFacade);
    }

    protected void setTableFacade(TableFacade tableFacade) {

        this.tableFacade = tableFacade;
        this.id = tableFacade.getId();
        setHttpServletRequest(tableFacade);
    }

    private void setHttpServletRequest(TableFacade tableFacade) {

        Object backingObject = tableFacade.getWebContext().getBackingObject();
        if (backingObject instanceof HttpServletRequest) {
            request = (HttpServletRequest) backingObject;
        }
    }

    /**
     * The most common way to set the items. If you need
     * to delay setting the items then call the method
     * with the AllItems interface.
     */
    public void setItems(Collection<?> items) {

        this.items = items;
    }

    /**
     * Use to set one page of items.
     */
    public void setItems(PageItems pageItems) {

        this.pageItems = pageItems;
    }

    /**
     * Use if you need to delay calculating the items until
     * some other action, such as saving to a worksheet.
     */
    public void setItems(AllItems allItems) {

        this.allItems = allItems;
    }

    public void setPreferences(Preferences preferences) {

        this.preferences = preferences;
    }

    public void setMessages(Messages messages) {

        this.messages = messages;
    }

    public void setExportTypes(String... exportTypes) {

        this.exportTypes = exportTypes;
    }

    public void setExportFileName(String exportFileName) {

        this.exportFileName = exportFileName;
    }

    public void setViewExporter(ViewExporter viewExporter) {

        this.viewExporter = viewExporter;
    }

    public void setState(State state) {

        this.state = state;
    }

    public void setStateAttr(String stateAttr) {

        this.stateAttr = stateAttr;
    }

    public void setLimit(Limit limit) {

        this.limit = limit;
    }

    public void autoFilterAndSort(boolean autoFilterAndSort) {

        this.autoFilterAndSort = autoFilterAndSort;
    }

    public void addFilterMatcher(MatcherKey key, FilterMatcher matcher) {

        if (filterMatchers == null) {
            filterMatchers = new HashMap<>(32);
        }
        filterMatchers.put(key, matcher);
    }

    public void addFilterMatcherMap(FilterMatcherMap filterMatcherMap) {

        this.filterMatcherMap = filterMatcherMap;
    }

    public void setColumnSort(ColumnSort columnSort) {

        this.columnSort = columnSort;
    }

    public void setRowFilter(RowFilter rowFilter) {

        this.rowFilter = rowFilter;
    }

    public void setMaxRows(int maxRows) {

        this.maxRows = maxRows;
    }

    public void setMaxRowsIncrements(int... maxRowsIncrements) {

        this.maxRowsIncrements = maxRowsIncrements;
    }

    public void setToolbar(Toolbar toolbar) {

        this.toolbar = toolbar;
    }

    public void setView(View view) {

        this.view = view;
    }

    public void setTable(Table table) {

        this.table = table;
    }

    public void setEditable(boolean editable) {

        this.editable = editable;
    }

    public void addRowObject(Object addedRowObject) {

        this.addedRowObject = addedRowObject;
    }

    public void saveWorksheet(WorksheetSaver worksheetSaver) {

        this.worksheetSaver = worksheetSaver;
    }

    public boolean isExporting() {

        String exportType = getExportType();
        return exportType != null && exportType.trim().length() > 0;
    }

    public String getExportType() {
        if(limit!=null){
            return  limit.getExportType();
        }

        LimitActionFactoryMapImpl actionFactory = new LimitActionFactoryMapImpl(id, getParameterMap());
        return actionFactory.getExportType();
    }

    private Map<String, ?> getParameterMap() {
        if(request!=null)
            return request.getParameterMap();
        return
            requestParameters;
    }

    /**
     * Generate the view.
     *
     * @return An html generated table will return the String markup. An export will be written out
     *         to the response and this method will return null.
     */
    public String render() {

        tableFacade.setEditable(editable);

        if (worksheetSaver != null) {
            TableModelUtils.saveWorksheet(tableFacade, worksheetSaver);
        }

        if (preferences != null) {
            tableFacade.setPreferences(preferences);
        }

        if (messages != null) {
            tableFacade.setMessages(messages);
        }

        if (exportTypes != null) {
            tableFacade.setExportTypes(exportTypes);
        }

        if (exportFileName != null) {
            tableFacade.setExportFileName(exportFileName);
        }

        if (viewExporter != null) {
            tableFacade.setViewExporter(viewExporter);
        }

        if (stateAttr != null) {
            tableFacade.setStateAttr(stateAttr);
        }

        if (state != null) {
            tableFacade.setState(state);
        }

        if (filterMatchers != null) {
            for (Entry<MatcherKey, FilterMatcher> entry : filterMatchers.entrySet()) {
                tableFacade.addFilterMatcher(entry.getKey(), entry.getValue());
            }
        }

        if (filterMatcherMap != null) {
            tableFacade.addFilterMatcherMap(filterMatcherMap);
        }

        if (columnSort != null) {
            tableFacade.setColumnSort(columnSort);
        }

        if (rowFilter != null) {
            tableFacade.setRowFilter(rowFilter);
        }

        if (maxRows != 0) {
            tableFacade.setMaxRows(maxRows);
        }

        if (maxRowsIncrements != null) {
            tableFacade.setMaxRowsIncrements(maxRowsIncrements);
        }

        if (limit != null) {
            tableFacade.setLimit(limit);
        }

        if (pageItems != null) {
            items = getItems(tableFacade, pageItems);
        } else if (allItems != null) {
            items = allItems.getItems();
        }

        tableFacade.setItems(items);
        tableFacade.autoFilterAndSort(autoFilterAndSort);

        if (table != null) {
            tableFacade.setTable(table);
        }

        if (toolbar != null) {
            tableFacade.setToolbar(toolbar);
        }

        if (view != null) {
            tableFacade.setView(view);
        }

        Worksheet worksheet = tableFacade.getWorksheet();
        if (editable && worksheet.isAddingRow()) {
        	if (addedRowObject != null) {
        		tableFacade.addWorksheetRow(addedRowObject);
        	} else {
        		tableFacade.addWorksheetRow();
        	}
        }

        return tableFacade.render();
    }
}
