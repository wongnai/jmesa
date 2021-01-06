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
package org.jmesa.view.html;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.jmesa.core.CoreContext;
import org.jmesa.limit.Limit;
import org.jmesa.limit.RowSelect;
import org.jmesa.view.AbstractContextSupport;
import org.jmesa.view.ViewUtils;
import org.jmesa.view.component.Column;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.editor.PatternSupport;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;
import org.jmesa.view.html.toolbar.Toolbar;
import org.jmesa.worksheet.Worksheet;
import org.jmesa.worksheet.WorksheetRow;
import org.jmesa.worksheet.WorksheetRowStatus;

import com.google.gson.Gson;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class HtmlSnippets extends AbstractContextSupport {
		
    private static final Gson GSON = new Gson();
    private HtmlTable table;
    private Toolbar toolbar;

    public HtmlSnippets(HtmlTable table, Toolbar toolbar, CoreContext coreContext) {
		
        this.table = table;
        this.toolbar = toolbar;
        setCoreContext(coreContext);
    }

    protected HtmlTable getHtmlTable() {
		
        return table;
    }

    protected Toolbar getToolbar() {
		
        return toolbar;
    }

    public String themeStart() {
		
        HtmlBuilder html = new HtmlBuilder();
        html.div().styleClass(table.getTheme()).close();
        return html.toString();
    }

    public String themeEnd() {
		
        HtmlBuilder html = new HtmlBuilder();
        html.newline();
        html.divEnd();
        return html.toString();
    }

    public String tableStart() {
		
        HtmlBuilder html = new HtmlBuilder();
        html.append(table.getTableRenderer().render());
        return html.toString();
    }

    public String tableEnd() {
		
        HtmlBuilder html = new HtmlBuilder();
        html.tableEnd(0);
        return html.toString();
    }

    public String theadStart() {
		
        HtmlBuilder html = new HtmlBuilder();
        html.thead(1).close();
        return html.toString();
    }

    public String theadEnd() {
		
        HtmlBuilder html = new HtmlBuilder();
        html.theadEnd(1);
        return html.toString();
    }

    public String tbodyStart() {
		
        HtmlBuilder html = new HtmlBuilder();
        String tbodyClass = getCoreContext().getPreference(HtmlConstants.TBODY_CLASS);
        html.tbody(1).styleClass(tbodyClass).close();
        return html.toString();
    }

    public String tbodyEnd() {
		
        HtmlBuilder html = new HtmlBuilder();
        html.tbodyEnd(1);
        return html.toString();
    }

    @SuppressWarnings("unchecked")
    public String filter() {
		
        HtmlRow row = table.getRow();
        List columns = row.getColumns();

        if (!ViewUtils.isFilterable(columns)) {
            return "";
        }

        HtmlBuilder html = new HtmlBuilder();
        String filterClass = getCoreContext().getPreference(HtmlConstants.FILTER_CLASS);
        html.tr(1).styleClass(filterClass).close();


        for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
            HtmlColumn column = (HtmlColumn) iter.next();
            if (column.isFilterable()) {
                html.append(column.getFilterRenderer().render());
            } else {
                html.td(2).close().tdEnd();
            }
        }

        html.trEnd(1);
        return html.toString();
    }

    public String header() {
		
        HtmlBuilder html = new HtmlBuilder();
        String headerClass = getCoreContext().getPreference(HtmlConstants.HEADER_CLASS);
        html.tr(1).styleClass(headerClass).close();

        HtmlRow row = table.getRow();
        List<Column> columns = row.getColumns();

        for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
            HtmlColumn column = (HtmlColumn) iter.next();
            html.append(column.getHeaderRenderer().render());
        }

        html.trEnd(1);
        return html.toString();
    }

    public String footer() {
		
        return null;
    }

    public String body() {
		
        HtmlBuilder html = new HtmlBuilder();

        CoreContext coreContext = getCoreContext();

        html.append(worksheetRowsAdded());

        int rowcount = HtmlUtils.startingRowcount(coreContext);

        Collection<?> items = coreContext.getPageItems();
        for (Object item : items) {
            rowcount++;

            HtmlRow row = table.getRow();
            List<Column> columns = row.getColumns();

            html.append(row.getRowRenderer().render(item, rowcount));

            for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
                HtmlColumn column = (HtmlColumn) iter.next();
                html.append(column.getCellRenderer().render(item, rowcount));
            }

            html.trEnd(1);
        }
        return html.toString();
    }

    public String worksheetRowsAdded() {
		
        HtmlBuilder html = new HtmlBuilder();

        CoreContext coreContext = getCoreContext();
        Worksheet worksheet = coreContext.getWorksheet();
        if (worksheet == null) {
            return "";
        }

        List<WorksheetRow> worksheetRows = worksheet.getRowsByStatus(WorksheetRowStatus.ADD);
        if (worksheetRows.isEmpty()) {
            return "";
        }

        int rowcount = 0;

        for (WorksheetRow worksheetRow : worksheetRows) {
            Object item = worksheetRow.getItem();

            HtmlRow row = table.getRow();
            List<Column> columns = row.getColumns();

            html.append(row.getRowRenderer().render(item, --rowcount));

            for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
                HtmlColumn column = (HtmlColumn) iter.next();
                html.append(column.getCellRenderer().render(item, 0));
            }

            html.trEnd(1);
        }

        html.append(worksheetRowsAddedHeader("", table.getRow().getColumns().size() + 1));

        return html.toString();
    }

    private String worksheetRowsAddedHeader(String title, int colspan) {
		
        HtmlBuilder html = new HtmlBuilder();

        html.tr(1).styleClass("addRow").close();
        html.td(2).colspan(String.valueOf(colspan)).close().append(title).tdEnd();
        html.trEnd(1);

        return html.toString();
    }

    public String statusBarText() {
		
        CoreContext coreContext = getCoreContext();
        Limit limit = coreContext.getLimit();
        RowSelect rowSelect = limit.getRowSelect();

        if (rowSelect.getTotalRows() == 0) {
            return coreContext.getMessage(HtmlConstants.STATUSBAR_NO_RESULTS_FOUND);
        }

        Integer total = rowSelect.getTotalRows();
        Integer from = rowSelect.getRowStart() + 1;
        Integer to = rowSelect.getRowEnd();
        Object[] messageArguments = { total, from, to };
        return coreContext.getMessage(HtmlConstants.STATUSBAR_RESULTS_FOUND, messageArguments);
    }

    public String toolbar() {
		
        HtmlBuilder html = new HtmlBuilder();

        HtmlRow row = table.getRow();
        List<Column> columns = row.getColumns();

        String toolbarClass = getCoreContext().getPreference(HtmlConstants.TOOLBAR_CLASS);
        html.tr(1).styleClass(toolbarClass).close();
        html.td(2).colspan(String.valueOf(columns.size())).close();

        html.append(toolbar.render());

        html.tdEnd();
        html.trEnd(1);

        return html.toString();
    }

    public String statusBar() {
		
        HtmlBuilder html = new HtmlBuilder();

        HtmlRow row = table.getRow();
        List<Column> columns = row.getColumns();

        html.tbody(1).close();

        String toolbarClass = getCoreContext().getPreference(HtmlConstants.STATUS_BAR_CLASS);
        html.tr(1).styleClass(toolbarClass).close();
        html.td(2).align("left").colspan(String.valueOf(columns.size())).close();

        html.append(statusBarText());

        html.tdEnd();
        html.trEnd(1);

        html.tbodyEnd(1);

        return html.toString();
    }
    
    public String hiddenFields() {
        
        HtmlBuilder html = new HtmlBuilder();
        
        Limit limit = getCoreContext().getLimit();
        
        /* limit fields */
        html.newline().input().type("hidden").name(limit.getId() + "_mr_").value(String.valueOf(limit.getRowSelect().getMaxRows())).end();
        html.newline().input().type("hidden").name(limit.getId() + "_p_").value(String.valueOf(limit.getRowSelect().getPage())).end();
        
        /* export fields */
        html.newline().input().type("hidden").name(limit.getId() + "_e_").end();

        /* worksheet fields */
        html.newline().input().type("hidden").name(limit.getId() + "_sw_").end();
        html.newline().input().type("hidden").name(limit.getId() + "_cw_").end();
        html.newline().input().type("hidden").name(limit.getId() + "_fw_").end();
        html.newline().input().type("hidden").name(limit.getId() + "_awr_").end();
        html.newline().input().type("hidden").name(limit.getId() + "_rwr_").end();

        /* context fields */
        html.newline().input().type("hidden").name(limit.getId() + "_ctx_").value(getWebContext().getContextPath()).end();
        
        return html.toString();
    }

    /**
     * Create a Limit implementation in JavaScript. Will be invoked when the page is loaded.
     *
     * @return The JavaScript Limit.
     * @deprecated Use the hiddenFields() method instead, which is the new way to capture the table state.
     */
    @Deprecated
    public String initJavascriptLimit() {

        return hiddenFields();
    }
    
    public String createExportField() {

        Map<String,  Map<String,String>> columnInfo = new LinkedHashMap<String,  Map<String,String>>();
        for (Column column : table.getRow().getColumns()) {
            if (!column.isExportable()) {
                continue;
            }
            Map<String,String> info = new HashMap<String,String>();
            info.put("title", column.getTitle());
            CellEditor exportEditor = column.getExportEditor();
            info.put("cellEditor", exportEditor.getClass().getName());
            if (exportEditor instanceof PatternSupport) {
                info.put("pattern", ((PatternSupport)exportEditor).getPattern());
            }
            columnInfo.put(column.getProperty(), info);
            
        }
        
        HtmlBuilder html = new HtmlBuilder();
        Limit limit = getCoreContext().getLimit();
        String name = limit.getId() + "_exp_";
        html.newline().input().type("hidden");
        html.name(name);
        html.value(StringEscapeUtils.escapeHtml(GSON.toJson(columnInfo)));
        html.end();

        return html.toString();
    }
    

}
