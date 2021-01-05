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
package org.jmesa.view.editor.expression;

import org.jmesa.view.editor.*;
import java.util.HashMap;
import java.util.Map;
import javax.el.*;

import org.jmesa.test.AbstractTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @version 2.4
 * @author bgould
 */
public class ElExpressionCellEditorTest extends AbstractTestCase {

    @Test
    public void testVariableResolver()
            throws ELException {
        Map<?, ?> item = getVariableContext();
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("item", item);


        StandardELContext context = new StandardELContext(ExpressionFactory.newInstance());

        ELResolver resolver = new MapELResolver();

        Object result = resolver.getValue(context, ctx, "item");
        assertSame(item, result);
    }

    @Test
    public void testGetValue() {

        Object item = getVariableContext();

        CellEditor editor = new ElExpressionCellEditor(new Expression(Language.EL, "item", "item.one + item.two"));
        Object result = editor.getValue(item, "test", 0);
        assertEquals("item.one + item.two", result);

        editor = new ElExpressionCellEditor(new Expression(Language.EL, "item", "${item.one + item.two}"));
        result = editor.getValue(item, "test", 0);
        assertEquals(new Integer(3).toString(), result.toString());

        editor = new ElExpressionCellEditor("item", "${item.one} + ${item.two} = ${item.one + item.two}");
        result = editor.getValue(item, "test", 0);
        assertEquals("1 + 2 = 3", result);
    }

    protected Map<?, ?> getVariableContext() {

        Integer one = new Integer(1);
        Integer two = new Integer(2);
        Map<String, Integer> item = new HashMap<String, Integer>();
        item.put("one", one);
        item.put("two", two);
        return item;
    }
}
