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

import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.AbstractCellEditor;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

/**
 * The default html cell editor that includes escape characters.
 *
 * @since 2.4
 * @author frode.reinertsen
 */
public class HtmlCellEditor extends AbstractCellEditor {

    @Override
    public Object getValue(Object item, String property, int rowcount) {

        Object itemValue = ItemUtils.getItemValue(item, property);
        if (itemValue != null) {
            itemValue = escapeHtml4(itemValue.toString());
        }
        return itemValue;
    }
}
