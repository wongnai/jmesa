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

import org.jmesa.limit.state.State;
import org.jmesa.web.WebContext;

import java.util.Map;

/**
 * <p>
 * Constructs a Limit and a RowSelect object.
 * </p>
 *
 * <p>
 * An example to create a Limit using the LimitFactory is as follows:
 * </p>
 *
 * <p>
 * First you need to pass in the table id and the WebContext. The table id is the unique identifier
 * for the current table being built. The WebContext is an adapter for the servlet request.
 * </p>
 *
 * <pre>
 * String id = &quot;pres&quot;;
 * WebContext webContext = new HttpServletRequestWebContext(request);
 * LimitFactory limitFactory = new LimitFactory(id, webContext);
 * Limit limit = limitFactory.createLimit();
 * </pre>
 *
 * <p>
 * Once you have the Limit you still need to create and set a RowSelect object on the Limit. The
 * RowSelect needs to be added to the Limit so that the row information is available. The catch is
 * the RowSelect cannot be created until the total rows is known. If you are not manually filtering
 * and sorting the table then it is easy because the totalRows is just the size of your items
 * (Collection of Beans or Collection of Maps). However, if you are manually filtering and sorting
 * the table to bring back one page of data the you first need to use the FilterSet on the Limit to
 * figure out the total rows.
 * </p>
 *
 * <p>
 * Either way, once you have the total rows you can now create a RowSelect with the factory. Lastly,
 * set the RowSelect on the Limit and your done!
 * </p>
 *
 * <pre>
 * RowSelect rowSelect = limitFactory.createRowSelect(maxRows, totalRows);
 * limit.setRowSelect(rowSelect);
 * </pre>
 *
 * <p>
 * Note: if you are not manually filtering and sorting there is a convenience method to create the
 * Limit and RowSelect at once.
 * </p>
 *
 * <pre>
 * LimitFactory limitFactory = new LimitFactory(id, webContext);
 * Limit limit = limitFactory.createLimitAndRowSelect(maxRows, totalRows);
 * </pre>
 *
 * <p>
 * Also, if you are manually filtering and sorting and are doing an export then you should create
 * the RowSelect object yourself and set it on the Limit.
 * </p>
 *
 * <pre>
 * RowSelect rowSelect = new RowSelect(1, totalRows, totalRows);
 * limit.setRowSelect(rowSelect);
 * </pre>
 *
 * @since 2.0
 * @author Jeff Johnston
 */
public class LimitFactory {

    private final LimitActionFactory limitActionFactoryMapImpl;
    private State state;

    /**
     * @param id The unique identifier for the current table being built.
     * @param webContext The adapter for the servlet request.
     */
    public LimitFactory(String id, WebContext webContext) {

        this.limitActionFactoryMapImpl = new LimitActionFactoryMapImpl(id, webContext.getParameterMap());
    }

    public LimitFactory(String id, String json) {

        this.limitActionFactoryMapImpl = new LimitActionFactoryJsonImpl(id, json);
    }

    public LimitFactory(String id, Map<String, Object> json) {

        this.limitActionFactoryMapImpl = new LimitActionFactoryJsonImpl(id, json);
    }

    public void setState(State state) {

        this.state = state;
    }

    public Limit createLimit() {
        int defaultMaxRows = 10000000;
        return createLimit(defaultMaxRows);
    }
    /**
     * <p>
     * Create a Limit object that is populated with the FilterSet, SortSet, and Export. Be aware
     * though that the Limit object is still incomplete. You still need to set a RowSelect on the
     * Limit to make the object complete.
     * </p>
     *
     * <p>
     * Also note that if you are using the State feature the returned object will be the Limit as
     * it exists in the State.
     * </p>
     *
     * <p>
     * One reason to create the Limit separately from the RowSelect is if you are going to manually
     * filter and sort the table to only return one page of data. If you are doing that then you
     * should use the FilterSet to manually filter the table to figure out the totalRows.
     * </p>
     */
    public Limit createLimit(int totalRows) {

        Limit limit = getStateLimit();

        if (limit != null) {
            return limit;
        }

        limit = new Limit(limitActionFactoryMapImpl.getId());

        FilterSet filterSet = limitActionFactoryMapImpl.getFilterSet();
        limit.setFilterSet(filterSet);

        SortSet sortSet = limitActionFactoryMapImpl.getSortSet();
        limit.setSortSet(sortSet);

        String exportType = limitActionFactoryMapImpl.getExportType();
        limit.setExportType(exportType);


        try{
            RowSelect rowSelect = limit.getRowSelect();
            rowSelect.setMaxRows(limitActionFactoryMapImpl.getMaxRows());
            rowSelect.setPage(limitActionFactoryMapImpl.getPage());
        }catch (Exception e) {
            limit.setRowSelect(createRowSelect(limitActionFactoryMapImpl.getMaxRows(), totalRows));
        }
        if (state != null && !limit.hasExport()) {
            state.persistLimit(limit);
        }

        return limit;
    }

    /**
     * <p>
     * Create the RowSelect object. This is created with dynamic values for the page and maxRows.
     * What this means is if the user is paginating or selected a different maxRows on the table
     * then the RowSelect will be created using those values.
     * </p>
     *
     * <p>
     * If you do not want the RowSelect to be created based on how the user is interacting with the
     * table, then do not use this method. Instead you should instantiate the RowSelect object
     * yourself and set it on the Limit.
     * </p>
     *
     * @param maxRows The maximum page rows that should be displayed on the current page.
     * @param totalRows The total rows across all the pages.
     */
    public RowSelect createRowSelect(int maxRows, int totalRows) {

        int page = limitActionFactoryMapImpl.getPage();

        maxRows = getMaxRows(maxRows);

        return new RowSelect(page, maxRows, totalRows);
    }

    /**
     * <p>
     * A convenience method to create the Limit and RowSelect at the same time. This takes in
     * account whether or not the table is being exported. If the table is being exported a new
     * RowSelect object is created, starting at page one, and the maxRows and totalRows set to the
     * value of the totalRows. If the table is not being exported then the RowSelect will be created
     * based on how the user is interacting with the table. What this means is if the user is
     * paginating or selected a different maxRows on the table then the RowSelect will be created
     * using those values.
     * </p>
     *
     * <p>
     * If you do not want the RowSelect to be created based on how the user is interacting with the
     * table, then do not use this method. Instead create the Limit using the createLimit() method
     * and then create a RowSelect object separately.
     * </p>
     *
     * <p>
     * This method is also not useful if you are using the Limit to filter and sort manually and
     * only return one page of data. This is because when you are manually filtering you do not know
     * the totalRows until you use the FilterSet to do the filtering.
     * </p>
     *
     * @param maxRows The maximum page rows that should be displayed on the current page.
     * @param totalRows The total rows across all the pages.
     *
     * @return The created Limit that is populated with the RowSelect object.
     */
    public Limit createLimitAndRowSelect(int maxRows, int totalRows) {

        Limit limit = createLimit();

        if (limit.hasRowSelect()) {
            return limit;
        }

        if (limit.hasExport()) {
            RowSelect rowSelect = new RowSelect(1, totalRows, totalRows);
            limit.setRowSelect(rowSelect);
        } else {
            RowSelect rowSelect = createRowSelect(maxRows, totalRows);
            limit.setRowSelect(rowSelect);
        }

        return limit;
    }

    private int getMaxRows(int maxRows) {

        int currentMaxRows = limitActionFactoryMapImpl.getMaxRows();
        if ( currentMaxRows<=0) {
            return maxRows;
        }

        return currentMaxRows;
    }

    private Limit getStateLimit() {

        if (state != null) {
            Limit l = state.retrieveLimit();
            if (l != null) {
                return l;
            }
        }

        return null;
    }
}
