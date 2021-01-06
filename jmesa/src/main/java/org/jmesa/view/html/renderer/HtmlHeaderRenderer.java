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
package org.jmesa.view.html.renderer;

import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlConstants;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.renderer.AbstractHeaderRenderer;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class HtmlHeaderRenderer extends AbstractHeaderRenderer {
		
    @Override
    public HtmlColumn getColumn() {
		
        return (HtmlColumn) super.getColumn();
    }

    @Override
    public Object render() {
		
        HtmlBuilder html = new HtmlBuilder();

        String element = getCoreContext().getPreference(HtmlConstants.HEADER_RENDERER_ELEMENT);
        if (element.equalsIgnoreCase("td")) {
            html.td(2);
        } else {
            html.th(2);
        }
        
        HtmlColumn column = getColumn();

        html.width(getColumn().getWidth());
        html.style(column.getHeaderStyle());
        html.styleClass(column.getHeaderClass());
        html.close();

        html.append(column.getHeaderEditor().getValue());

        if (element.equalsIgnoreCase("td")) {
            html.tdEnd();
        } else {
            html.thEnd();
        }

        return html.toString();
    }
}
