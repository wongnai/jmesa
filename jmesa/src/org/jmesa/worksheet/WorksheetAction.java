package org.jmesa.worksheet;

public interface WorksheetAction {
    boolean isSaving();

    boolean isAddingRow();

    boolean isFiltering();
}
