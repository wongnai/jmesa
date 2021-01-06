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
package org.jmesa.core.sort;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jmesa.core.Name;
import org.jmesa.core.President;
import org.jmesa.core.PresidentDao;
import org.jmesa.limit.Limit;
import org.jmesa.limit.LimitFactory;
import org.jmesa.limit.Order;
import org.jmesa.test.AbstractTestCase;
import org.jmesa.test.ParametersAdapter;
import org.jmesa.test.ParametersBuilder;
import org.jmesa.web.WebContext;
import org.junit.jupiter.api.Test;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public class ColumnSortTest extends AbstractTestCase {

	@Test
	public void sortItems() {

		WebContext webContext = createWebContext();

        HashMap<String, Object> results = new HashMap<String, Object>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("name.firstName", Order.ASC);
        builder.addSort("name.lastName", Order.DESC);
		webContext.setParameterMap(results);

        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();

		MultiColumnSort itemsSort = new MultiColumnSort();

		Collection<?> items = PresidentDao.getPresidents();
		items = itemsSort.sortItems(items, limit);

		assertNotNull(items);

		Iterator<?> iterator = items.iterator();

		President first = (President)iterator.next();
		assertTrue(() -> first.getName().getFirstName().equals("Abraham"), "the first sort order is wrong");

		President second = (President)iterator.next();
		assertTrue(()-> second.getName().getLastName().equals("Johnson"), "the second sort order is wrong");
	}

    @SuppressWarnings("unchecked")
    @Test
    public void sortNullComposedItems() {

        WebContext webContext = createWebContext();

        HashMap<String, Object> results = new HashMap<>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("name.firstName", Order.ASC);
        builder.addSort("name.lastName", Order.DESC);
        webContext.setParameterMap(results);

        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();

        MultiColumnSort itemsSort = new MultiColumnSort();

        Collection items = new ArrayList<President>();

        President president = new President();
        Name  name = new Name("James", "Monroe");
        president.setName(name);
        items.add(president);

        president = new President();
        name = new Name(null, "Washington"); // The null object
        president.setName(name);
        items.add(president);

        president = new President();
        name = new Name("James", "Madison");
        president.setName(name);
        items.add(president);

        items = itemsSort.sortItems(items, limit);

        assertNotNull(items);

        Iterator iterator = items.iterator();

        President first = (President)iterator.next();
        assertTrue(()-> first.getName().getFirstName().equals("James"), "the first sort order is wrong");

        President second = (President)iterator.next();
        assertTrue(() -> second.getName().getLastName().equals("Madison"), "the second sort order is wrong");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sortEmptyComposedItems() {

        WebContext webContext = createWebContext();

        HashMap<String, Object> results = new HashMap<>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("name.firstName", Order.ASC);
        builder.addSort("name.lastName", Order.DESC);
        webContext.setParameterMap(results);

        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();

        MultiColumnSort itemsSort = new MultiColumnSort();

        Collection items = new ArrayList<President>();

        President president = new President();
        Name  name = new Name("James", "Monroe");
        president.setName(name);
        items.add(president);

        president = new President();
        name = new Name("", "Washington"); // The empty object
        president.setName(name);
        items.add(president);

        president = new President();
        name = new Name("James", "Madison");
        president.setName(name);
        items.add(president);

        items = itemsSort.sortItems(items, limit);

        assertNotNull(items);

        Iterator iterator = items.iterator();

        President first = (President)iterator.next();
        assertTrue(()-> first.getName().getFirstName().equals(""), "the first sort order is wrong");

        President second = (President)iterator.next();
        assertTrue(() -> second.getName().getLastName().equals("Monroe"), "the second sort order is wrong");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sortNullItems() {

        WebContext webContext = createWebContext();

        HashMap<String, Object> results = new HashMap<>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("born", Order.ASC);
        webContext.setParameterMap(results);

        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();

        MultiColumnSort itemsSort = new MultiColumnSort();

        Collection items = new ArrayList<President>();

        President president = new President();
        Name name = new Name("Thomas", "Jefferson");
        president.setName(name);
        president.setBorn(PresidentDao.getDate("04/13/1743"));
        items.add(president);

        president = new President();
        name = new Name("John", "Adams");
        president.setName(name);
        president.setBorn(null); // The null object
        items.add(president);

        president = new President();
        name = new Name("George", "Washington");
        president.setName(name);
        president.setBorn(PresidentDao.getDate("02/22/1732"));
        items.add(president);

        items = itemsSort.sortItems(items, limit);

        assertNotNull(items);

        Iterator iterator = items.iterator();

        President first = (President)iterator.next();
        assertTrue(()-> first.getName().getFirstName().equals("George"), "the first sort order is wrong");

        President second = (President)iterator.next();
        assertTrue(()-> second.getName().getLastName().equals("Jefferson"), "the second sort order is wrong");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sortEmptyItems() {

        WebContext webContext = createWebContext();

        HashMap<String, Object> results = new HashMap<String, Object>();
        ParametersAdapter parametersAdapter = new ParametersAdapter(results);
        ParametersBuilder builder = new ParametersBuilder(ID, parametersAdapter);
        builder.addSort("term", Order.ASC);
        webContext.setParameterMap(results);

        LimitFactory limitFactory = new LimitFactory(ID, webContext);
        Limit limit = limitFactory.createLimit();

        MultiColumnSort itemsSort = new MultiColumnSort();

        Collection items = new ArrayList<President>();

        President president = new President();
        president.setTerm(""); // The empty object
        items.add(president);

        president = new President();
        president.setTerm("1801-09");
        items.add(president);

        president = new President();
        president.setTerm("1797-1801");
        items.add(president);

        items = itemsSort.sortItems(items, limit);

        assertNotNull(items);

        Iterator iterator = items.iterator();

        President first = (President)iterator.next();
        assertTrue(() -> first.getTerm().equals(""), "the first sort order is wrong");

        President second = (President)iterator.next();
        assertTrue(()->second.getTerm().equals("1797-1801"), "the second sort order is wrong");
    }
}
