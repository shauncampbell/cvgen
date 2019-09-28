package scot.shaun.cvgen.util;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PdfTableWriter
{
    private PDPageContentStream contentStream;
    private final int startX;
    private final int startY;
    private int columnWidth = 205;
    private int columnHeight = 13;
    private int column = 0;
    private int row = 0;

    public PdfTableWriter(PDPageContentStream contentStream, int startX, int startY)
    {
        this.contentStream = contentStream;
        this.startX = startX;
        this.startY = startY;
    }

    public PdfTableWriter beginCell() throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(startX + (column*columnWidth), startY - (row * columnHeight));
        return this;
    }

    public PdfTableWriter writeWithFont(PDFont font, int size, String text) throws IOException
    {
        contentStream.setFont(font, size);
        contentStream.showText(text);
        return this;
    }

    public PdfTableWriter endCell() throws IOException
    {
        contentStream.endText();

        if (column < 2)
        {
            column++;
        }
        else
        {
            row++;
            column = 0;
        }

        return this;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public int getColumnHeight() {
        return columnHeight;
    }
}
