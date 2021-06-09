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
package org.jmesa.view.pdfp;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.jmesa.view.AbstractExportView;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static com.itextpdf.text.Font.BOLD;
import static com.itextpdf.text.Font.NORMAL;
import static com.itextpdf.text.FontFactory.HELVETICA;
import static com.itextpdf.text.FontFactory.getFont;
import static com.itextpdf.text.pdf.BaseFont.NOT_EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.createFont;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.jmesa.view.ExportConstants.PDF_FONT_ENCODING;
import static org.jmesa.view.ExportConstants.PDF_FONT_NAME;
import static org.jmesa.view.ViewUtils.isRowEven;

/**
 * A PDF view that uses the iText PdfPTable.
 *
 * @since 2.3.4
 * @author Ismail Seyfi
 */
public class PdfPView extends AbstractExportView {

    private static final Logger logger = LoggerFactory.getLogger(PdfPView.class);

    private BaseColor evenCellBackgroundColor;
    private BaseColor oddCellBackgroundColor;
    private BaseColor headerBackgroundColor;
    private BaseColor headerFontColor;
    private BaseColor captionFontColor;
    private int captionAlignment;

    public PdfPView() {

        this.evenCellBackgroundColor = new BaseColor(227, 227, 227);
        this.oddCellBackgroundColor = BaseColor.WHITE;
        this.headerBackgroundColor = new BaseColor(114, 159, 207);
        this.headerFontColor = BaseColor.WHITE;
        this.captionFontColor = BaseColor.BLACK;
        this.captionAlignment = Element.ALIGN_CENTER;
    }

    public Paragraph getTableCaption() {

        Paragraph p = new Paragraph(getTable().getCaption(), getFont(HELVETICA, 18, BOLD, getCaptionFontColor()));
        p.setAlignment(getCaptionAlignment());
        return p;
    }

    @Override
    public PdfPTable render() {

        PdfPTable pdfpTable = new PdfPTable(getTable().getRow().getColumns().size());
        pdfpTable.setSpacingBefore(3);

        Row row = getTable().getRow();

        List<Column> columns = row.getColumns();

        // build table headers
        for (Column column : columns) {
            PdfPCell cell = new PdfPCell(new Paragraph(column.getTitle(), getHeaderCellFont()));
            cell.setPadding(3.0f);
            cell.setBackgroundColor(getHeaderBackgroundColor());
            pdfpTable.addCell(cell);
        }

        // build table body
        Collection<?> items = getCoreContext().getPageItems();
        int rowcount = 0;
        for (Object item : items) {
            rowcount++;

            columns = row.getColumns();

            for (Column column : columns) {
                String property = column.getProperty();
                Object value = column.getCellEditor().getValue(item, property, rowcount);
                PdfPCell cell = new PdfPCell(new Paragraph(value == null ? "" : String.valueOf(value), getCellFont()));
                cell.setPadding(3.0f);

                if (isRowEven(rowcount)) {
                    cell.setBackgroundColor(getEvenCellBackgroundColor());
                } else {
                    cell.setBackgroundColor(getOddCellBackgroundColor());
                }

                pdfpTable.addCell(cell);
            }
        }

        return pdfpTable;
    }

    public int getCaptionAlignment() {

        return captionAlignment;
    }

    public void setCaptionAlignment(int captionAlignment) {

        this.captionAlignment = captionAlignment;
    }

    public BaseColor getCaptionFontColor() {

        return captionFontColor;
    }

    public void setCaptionFontColor(BaseColor captionFontColor) {

        this.captionFontColor = captionFontColor;
    }

    public BaseColor getHeaderBackgroundColor() {

        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(BaseColor headerBackgroundColor) {

        this.headerBackgroundColor = headerBackgroundColor;
    }

    /**
     * Create either the default helvetica 12 point font, or specify the
     * font name and encoding in the preferences. Either way it will use
     * the header font color.
     *
     * <p>
     *  The preference settings are the following:
     *  export.pdf.fontName
     *  export.pdf.fontEncoding
     * </p>
     */
    public Font getHeaderCellFont() {

        return getFontWithColor(getHeaderFontColor());
    }

    public BaseColor getHeaderFontColor() {

        return headerFontColor;
    }

    public void setHeaderFontColor(BaseColor headerFontColor) {

        this.headerFontColor = headerFontColor;
    }

    /**
     * Create either the default helvetica 12 point font, or specify the
     * font name and encoding in the preferences.
     *
     * <p>
     *  The preference settings are the following:
     *  export.pdf.fontName
     *  export.pdf.fontEncoding
     * </p>
     */
    public Font getCellFont() {

        return getFontWithColor(null);
    }

    public BaseColor getEvenCellBackgroundColor() {

        return evenCellBackgroundColor;
    }

    public void setEvenCellBackgroundColor(BaseColor evenCellBackgroundColor) {

        this.evenCellBackgroundColor = evenCellBackgroundColor;
    }

    public BaseColor getOddCellBackgroundColor() {

        return oddCellBackgroundColor;
    }

    public void setOddCellBackgroundColor(BaseColor oddCellBackgroundColor) {

        this.oddCellBackgroundColor = oddCellBackgroundColor;
    }

    private Font getFontWithColor(BaseColor color) {

        String fontName = getCoreContext().getPreference(PDF_FONT_NAME);
        String fontEncoding = getCoreContext().getPreference(PDF_FONT_ENCODING);
        if (isNotBlank(fontName) && isNotBlank(fontEncoding)) {
            try {
                BaseFont baseFont = createFont(fontName, fontEncoding, NOT_EMBEDDED);
                if (color != null) {
                    return new Font(baseFont, 12, 0, color);
                }
                return new Font(baseFont, 12, 0);
            } catch (Exception e) {
                logger.warn("Not able to create the requested font for the PDF export...will use the export.");
            }
        }

        if (color != null) {
            return getFont(HELVETICA, 12, NORMAL, color);
        }

        return getFont(HELVETICA, 12, NORMAL);
    }
}
