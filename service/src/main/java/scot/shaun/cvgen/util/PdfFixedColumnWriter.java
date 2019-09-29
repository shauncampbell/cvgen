package scot.shaun.cvgen.util;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

/***
 * PdfFixedColumnWriter.
 *
 * Class for writing text strings spaced at fixed
 * positions across the page.
 */
class PdfFixedColumnWriter
{
    //  Static variables
    private final float COLUMN_WIDTH;
    private int ROW_HEIGHT = 0;

    // Fixed variables.
    private final PdfWriter parent;
    private final PDPageContentStream contentStream;
    private final int referenceX;
    private int x;
    private int y;

    //  State variables.
    private int column = 0;
    private int row = 0;

    /***
     * Create a new fixed column writer.
     *
     * @param contentStream the stream for the page to be written to.
     * @param x the start X coordinates (1/72") for the content.
     * @param y the start Y coordinates (1/72") for the content.
     */
    PdfFixedColumnWriter(PdfWriter parent, PDPageContentStream contentStream, int x, int y, float columnWidth)
    {
        this.COLUMN_WIDTH = columnWidth;
        this.contentStream = contentStream;
        this.x = x;
        this.y = y;
        this.referenceX = x;
        this.parent = parent;
    }

    /***
     * Start writing a new cell.
     *
     * @return this writer.
     */
    PdfFixedColumnWriter beginCell() {
        try {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
        }
        catch (IOException ex)
        {
            throw new PdfWriterException(ex.getMessage(), ex);
        }
        return this;
    }

    /***
     * Write a text cell with the specified font.
     *
     * @param font the font to use to write.
     * @param size the size of the font.
     * @param text the text string to write.
     * @return this writer.
     */
    PdfFixedColumnWriter writeWithFont(PDFont font, int size, String text)
    {
        try
        {
            contentStream.setFont(font, size);
            contentStream.showText(text);

            if (size > ROW_HEIGHT)
            {
                ROW_HEIGHT = size + 2;
            }
        }
        catch (IOException ex)
        {
            throw new PdfWriterException(ex.getMessage(), ex);
        }
        return this;
    }

    /***
     * End writing a new cell and move on to the next one.
     *
     * @return this writer.
     */
    PdfFixedColumnWriter endCell()
    {
        try
        {
            contentStream.endText();
        }
        catch (IOException ex)
        {
            throw new PdfWriterException(ex.getMessage(), ex);
        }

        if (column < 2)
        {
            column++;
            x += COLUMN_WIDTH;
        }
        else
        {
            row++;
            column = 0;
            y -= ROW_HEIGHT;
            x = referenceX;
        }

        return this;
    }

    /***
     * Get the number of rows written by this writer.
     *
     * @return the row count.
     */
    int getRowCount() {
        return row;
    }

    /***
     * Get the height of the rows written by this driver.
     *
     * @return the row count.
     */
    int getRowHeight() {
        return ROW_HEIGHT;
    }
}
