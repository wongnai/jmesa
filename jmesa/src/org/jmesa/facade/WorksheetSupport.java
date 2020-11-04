package org.jmesa.facade;

import org.jmesa.limit.Limit;
import org.jmesa.limit.RowSelect;
import org.jmesa.worksheet.Worksheet;

/**
 * @author xwx
 */
public interface WorksheetSupport {
    void setEditable(boolean editable);

    Worksheet getWorksheet();

    void addWorksheetRow();

    void addWorksheetRow(Object item);

    void persistWorksheet(Worksheet worksheet);

    Limit getLimit();

    void setLimit(Limit limit);

    RowSelect setTotalRows(int totalRows);
}
