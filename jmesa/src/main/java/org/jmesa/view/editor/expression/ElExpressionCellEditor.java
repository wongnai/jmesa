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

import com.sun.el.lang.EvaluationContext;
import com.sun.el.parser.ELParser;
import com.sun.el.parser.Node;
import com.sun.el.parser.ParseException;
import org.jmesa.util.ItemUtils;
import org.jmesa.view.editor.AbstractCellEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.*;
import java.io.StringReader;
import java.util.Map;

import static org.jmesa.util.AssertUtils.notNull;

/**
 * Uses commons-el to evaluate EL expressions.
 * Update: since EL
 *
 * @version 2.4
 * @author bgould
 */
public class ElExpressionCellEditor extends AbstractCellEditor {

    private Logger logger = LoggerFactory.getLogger(ElExpressionCellEditor.class);

    /**
     * cell data variable name, i.e. 'item'
     */
    private final String var;

    private Object template;

    ExpressionFactory expressionFactory = ExpressionFactory.newInstance();

    StandardELContext context = new StandardELContext(expressionFactory);



    public ElExpressionCellEditor(Expression expression) {

        this(expression.getVar(), expression.getTemplate());

    }

    public ElExpressionCellEditor(String var, Object template) {

        notNull("The var is required.", var);
        this.var = var;

        notNull("The template is required.", template);
        this.template = template;
        try {
            this.template = new ELParser(new StringReader(String.valueOf(template))).CompositeExpression();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Object getValue(Object item, String property, int rowcount) {

        Object result = null;

        try {

//             ExpressionString is a mixture of template text and EL
//             expressions; ex. ${lastName}, ${firstName}
            if(template==null) {
                result = ItemUtils.getItemValue(item, property);
            }if (template instanceof Node) {
                result= ((Node)template).getValue(new EvaluationContext(context, this.getFunctionMapper(),this.getVariableMapper(item)));
            // If the expression parsed to a String, it is just template text
            } else {
                logger.warn("template is:" + template.getClass() + " " + template.toString());
                if (template instanceof String) {
                    result = template;
                }
            }
        } catch (ELException e) {
            logger.warn("Could not process el expression editor with property " + property, e);
        }

        return result;
    }

   protected FunctionMapper getFunctionMapper() {
       return context.getFunctionMapper();
   }

   protected VariableMapper getVariableMapper(Object item){
       VariableMapper variableMapper = context.getVariableMapper();
       variableMapper.setVariable(var,  expressionFactory.createValueExpression(item, Map.class));
       return variableMapper;
   }


}
