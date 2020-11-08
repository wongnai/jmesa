# Jmesa

A table grid manager for java web developing.


Jmesa is a Html table component re-developed by the ExtremeTable project, providing functions such as filtering,
 sorting , paging , and data export. Jmesa is very easy to use and can be extended according to your needs.
  JMesa requires an environment above JDK1.8.

The package structure of Jmesa source code divided into four packages:

* `org.jmesa.core` Jmesa's core package, the core interface CoreContext is defined below. 
The role of CoreContext will be described later. There are four sub-packages below:

* `org.jmesa.core.filter` filter (filter records according to conditions)
* `org.jmesa.core.sort` sorting, this package provides the function of sorting records by specified columns
* `org.jmesa.core.message` I18N resource file implementation, using Java built-in `ResourceBundle` implementation. 

The `org.jmesa.core.preference` Jmesa option using Java built-in Properties. 


`org.jmesa.limit` This package provides the function of filtering records. 
The original meaning of limit is limit, which is represented as filtering here. 
Jmesa provides an interface for developers to customize the place where Limit is stored. 
This interface is the State interface under the sub-package state of the package, 
which is used to save the state of the current user's query options or parameters.
 These parameters may include filter conditions (Filter), sort conditions (Sort), and paging information.
 
`org.jmesa.view` view, the package defines the interface for displaying the table and provides two implementations,
 CSV and HTML. The principle of presentation is very simple. A view has a component and a renderer to be presented.
  Use the renderer to render the component. So naturally, there are two sub-packages under the View package:
  
`org.jmesa.view.component` defines the components required by the table, namely the table, row, and column.

`org.jmesa.view.renderer` defines a series of renderers required by the table, such as row rendering, column rendering,
 cell rendering, etc.
 
`org.jmesa.web` provides a series of convenient classes for using Jmesa in the Web environment.
This is the distribution of Jmesa source code. Personally, I think the structure is very clear,
 and the code does not seem to be laborious. The code is the most convincing document. Sometimes, 
 reading the documentation is not as good as reading the source code directly.
