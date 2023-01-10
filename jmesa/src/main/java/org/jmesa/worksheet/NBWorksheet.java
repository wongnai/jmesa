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
package org.jmesa.worksheet;

import org.jmesa.limit.LimitActionFactoryJsonImpl;
import org.jmesa.limit.LimitUtils;
import org.jmesa.web.JsonContextSupport;

import java.util.Map;

/**
 * <p>
 * The Worksheet represents what the user changed on the table. It will contain WorksheetRows which
 * contain WorksheetColumns. A WorksheetColumn represents the edited HtmlColumn. As a developer you
 * will use the Worksheet to know what was modified, added, and deleted. You will also have a way to
 * add an error to individual columns.
 * </p>
 *
 * <p>
 * To get this functionality you will have to tell the TableModel that it is editable.
 * </p>
 *
 * <pre>
 * tableModel.setEditable(true);
 * </pre>
 *
 * <p>
 * You will also have to add the unique item properties to the HtmlRow.
 * </p>
 *
 * <pre>
 * htmlRow.setUniqueProperty(&quot;id&quot;);
 * </pre>
 *
 * <p>
 * In this example "id" is the item property that is used to uniquely identify the row.
 * </p>
 *
 * @since 4.1
 * @author Jeff Johnston
 * @author xwx
 */
public class NBWorksheet  extends Worksheet implements JsonContextSupport{


    private transient Map<String, Object> context;


    public NBWorksheet(String id) {
        super(id);
    }

    @Override
    public Map<String, Object> getContext() {
        return context;
    }

    @Override
    public void setContext(Map<String, Object> context) {
        this.context = context;
    }


    public static boolean isClearingWorksheet(Map<String, Object> context){
        String clear = LimitUtils.getValue(context.get(LimitActionFactoryJsonImpl.Keys.ACTION));
        return  LimitActionFactoryJsonImpl.Action.CLEAR_WORKSHEET.equalsIgnoreCase(clear);
    }

    /**
     * @return Is true if the user is requesting that the worksheet be saved.
     */
    @Override
    public boolean isSaving() {
        String clear = LimitUtils.getValue(context.get(LimitActionFactoryJsonImpl.Keys.ACTION));
        return  LimitActionFactoryJsonImpl.Action.SAVE_WORKSHEET.equalsIgnoreCase(clear);
    }

    /**
     * @return Is true if the user is requesting to add a row in worksheet.
     */
    @Override
    public boolean isAddingRow() {
        String clear = LimitUtils.getValue(context.get(LimitActionFactoryJsonImpl.Keys.ACTION));
        return  LimitActionFactoryJsonImpl.Action.ADD_WORKSHEET_ROW.equalsIgnoreCase(clear);
    }

    /**
     * @return Is true if the user is requesting that the worksheet filter changes.
     */
    @Override
    public boolean isFiltering() {
        String clear = LimitUtils.getValue(context.get(LimitActionFactoryJsonImpl.Keys.ACTION));
        return  LimitActionFactoryJsonImpl.Action.FILTER_WORKSHEET.equalsIgnoreCase(clear);
    }


}
