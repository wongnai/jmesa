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
package org.jmesa.view.html.editor;

import org.jmesa.limit.Filter;
import org.jmesa.limit.Limit;
import org.jmesa.limit.RangeFilter;
import org.jmesa.limit.SingleValueFilter;
import org.jmesa.view.editor.AbstractFilterEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.component.HtmlColumn;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The default editor for the column filter.
 *
 * @since 2.2
 * @author Jeff Johnston
 */
public class HtmlFilterEditor extends AbstractFilterEditor {

    @Override
    public HtmlColumn getColumn() {

        return (HtmlColumn) super.getColumn();
    }

    @Override
    public Object getValue() {

        HtmlBuilder html = new HtmlBuilder();

        Limit limit = getCoreContext().getLimit();
        HtmlColumn column = getColumn();
        String property = column.getProperty();
        Filter filter = limit.getFilterSet().getFilter(property);

        if(filter instanceof SingleValueFilter) {
            String filterValue = "";
            filterValue =  Arrays.stream((String[])filter.getValue()).collect(Collectors.joining(","));

            html.input().type("text");
            html.name(getCoreContext().getLimit().getId() + "_f_" + property);
            html.value(filterValue);
            html.onkeypress("jQuery.jmesa.filterKeypress('" + limit.getId() + "', event);");
            html.end();
        }else if(filter instanceof RangeFilter) {
                html.input().type("text");
                html.name(getCoreContext().getLimit().getId() + "_f_b_" + property);
                html.value(((RangeFilter)filter).getStartValue());
                html.onkeypress("jQuery.jmesa.filterKeypress('" + limit.getId() + "', event);");
                html.end();

                html.input().type("text");
                html.name(getCoreContext().getLimit().getId() + "_f_e_" + property);
                html.value(((RangeFilter)filter).getEndValue());
                html.onkeypress("jQuery.jmesa.filterKeypress('" + limit.getId() + "', event);");
                html.end();
        }
        return html.toString();
    }
}
