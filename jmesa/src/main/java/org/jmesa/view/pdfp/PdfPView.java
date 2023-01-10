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


import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.jmesa.view.AbstractExportView;
import org.jmesa.view.component.Column;
import org.jmesa.view.component.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
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

    private Color evenCellBackgroundColor;
    private Color oddCellBackgroundColor;
    private Color headerBackgroundColor;
    private Color headerFontColor;
    private Color captionFontColor;
    private int captionAlignment;

    public PdfPView() {

        this.evenCellBackgroundColor = new DeviceRgb( 227, 227, 227);
        this.oddCellBackgroundColor = DeviceRgb.WHITE;
        this.headerBackgroundColor = new DeviceRgb(114, 159, 207);
        this.headerFontColor = DeviceRgb.WHITE;
        this.captionFontColor = DeviceRgb.BLACK;
        this.captionAlignment = PdfFormField.ALIGN_CENTER;
    }

    public Paragraph getTableCaption() {
        Paragraph p = new Paragraph(getTable().getCaption());
        p.setFirstLineIndent(getCaptionAlignment());
        p.setFont(getFont(StandardFonts.HELVETICA_BOLD));
        p.setFontColor(getCaptionFontColor());
        p.setFontSize(18);

        return p;
    }

    @Override
    public Table render() {

        Table pdfpTable = new Table(getTable().getRow().getColumns().size());
       // pdfpTable.setSpacingBefore(3);

        Row row = getTable().getRow();

        List<Column> columns = row.getColumns();

        // build table headers
        for (Column column : columns) {
            Cell cell = new Cell();
            cell.setFont(getHeaderCellFont());
            cell.setPadding(3.0f);
            cell.setBackgroundColor(getHeaderBackgroundColor());
            cell.add(new Paragraph(column.getTitle()));
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
                Cell cell = new Cell();//
                cell.add( new Paragraph(value == null ? "" : String.valueOf(value)));
                cell.setFont(getCellFont());
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

    public Color getCaptionFontColor() {

        return captionFontColor;
    }

    public void setCaptionFontColor(Color captionFontColor) {

        this.captionFontColor = captionFontColor;
    }

    public Color getHeaderBackgroundColor() {

        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(Color headerBackgroundColor) {

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
    public PdfFont getHeaderCellFont() {

        return getFontWithColor(getHeaderFontColor());
    }

    public Color getHeaderFontColor() {

        return headerFontColor;
    }

    public void setHeaderFontColor(Color headerFontColor) {

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
    public PdfFont getCellFont() {

        return getFontWithColor(null);
    }

    public Color getEvenCellBackgroundColor() {

        return evenCellBackgroundColor;
    }

    public void setEvenCellBackgroundColor(Color evenCellBackgroundColor) {

        this.evenCellBackgroundColor = evenCellBackgroundColor;
    }

    public Color getOddCellBackgroundColor() {

        return oddCellBackgroundColor;
    }

    public void setOddCellBackgroundColor(Color oddCellBackgroundColor) {

        this.oddCellBackgroundColor = oddCellBackgroundColor;
    }

    private PdfFont getFontWithColor(Color color) {

        String fontName = getCoreContext().getPreference(PDF_FONT_NAME);
        String fontEncoding = getCoreContext().getPreference(PDF_FONT_ENCODING);
        if (isNotBlank(fontName) && isNotBlank(fontEncoding)) {
            try {
                //BaseFont baseFont = createFont(fontName, fontEncoding, NOT_EMBEDDED);
                PdfFont baseFont  = PdfFontFactory.createFont(fontName, fontEncoding,  PdfFontFactory.EmbeddingStrategy.PREFER_NOT_EMBEDDED);

                    return  baseFont;//.createFont(baseFont, 12, 0, color);

            } catch (Exception e) {
                logger.warn("Not able to create the requested font for the PDF export...will use the export.");
            }
        }

        if (color != null) {
            return getFont(StandardFonts.HELVETICA);
        }

        return getFont(StandardFonts.HELVETICA);
    }


    PdfFont getFont(String name)  {
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont(name, "");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return font;
    }
}

