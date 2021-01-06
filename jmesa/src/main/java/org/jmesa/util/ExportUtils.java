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
package org.jmesa.util;

import org.apache.commons.lang.StringUtils;
import org.jmesa.view.View;

/**
 * <p>
 * Utility class to work with the Exports.
 * </p>
 * 
 * @since 2.2
 * @author Jeff Johnston
 */
public class ExportUtils {
		
    /**
     * Use the view caption for the export. If the caption is not defined then use a default.
     * 
     * @param view The view to export.
     * @return The file name of export.
     */
    public static String exportFileName(View view) {
		
        String caption = view.getTable().getCaption();
        if (StringUtils.isNotBlank(caption)) {
            StringUtils.replace(caption, " ", "_");
            return caption.toLowerCase();
        } 
        
        return "table-data";
    }
}
