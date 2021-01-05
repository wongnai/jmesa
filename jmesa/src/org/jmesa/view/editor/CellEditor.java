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
package org.jmesa.view.editor;

/**
 * A CellEditor is used to display the column value. You can either use the pre-canned
 * ones included in JMesa or create something custom. Typically most tables you create
 * will have at least one custom editor.
 *
 * @since 2.0
 * @author Jeff Johnston
 */
public interface CellEditor {
    /**
     * get cell value by item and property
     * @param item row object or Map
     * @param property property of item or Map Key
     * @param rowcount index of row
     * @return if no property return ""
     */
    Object getValue(Object item, String property, int rowcount);
}
