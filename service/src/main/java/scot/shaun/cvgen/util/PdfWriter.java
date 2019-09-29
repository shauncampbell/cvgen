package scot.shaun.cvgen.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class PdfWriter
{
    protected final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protected final PDDocument document = new PDDocument();

    protected PDPageContentStream contentStream;
    protected int linePosition = 725;
    protected PDPage page;
    protected int pageNumber = 0;

    private static final int MARGIN = 50;
    private static final float WIDTH = PDRectangle.LETTER.getWidth();
    private static final int TITLE_SIZE = 30;
    private static final int H1_SIZE = 16;
    private static final int H2_SIZE = 12;
    private static final int H3_SIZE = 10;

    protected PdfWriter()
    {
        newPage();
    }

    public abstract byte[] getPdf() throws IOException;
    protected abstract void setTitleAndAuthor(String title, String author);

    public PdfWriter title(String title)
    {
        try
        {
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, linePosition);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, TITLE_SIZE);
            contentStream.showText(title);
            contentStream.endText();
        }
        catch (IOException e) {
            throw new PdfWriterException(e.getMessage(), e);
        }

        linePosition -= 10;
        return this;
    }

    public PdfWriter horizontalLine()
    {
        try {
            contentStream.moveTo(MARGIN, linePosition);
            contentStream.lineTo(WIDTH - MARGIN, linePosition);
            contentStream.stroke();
            linePosition -= 15;
        }
        catch (IOException ex)
        {
            throw new PdfWriterException(ex.getMessage(), ex);
        }
        return this;
    }

    public PdfFixedColumnWriter beginColumnLayout(int number)
    {
        PdfFixedColumnWriter table = new PdfFixedColumnWriter(this, contentStream, MARGIN, linePosition, ((WIDTH - 2*MARGIN)/number + 40) );

        return table;
    }

    public PdfWriter header1(String string) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(MARGIN, linePosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, H1_SIZE);
        contentStream.showText(string);
        contentStream.endText();

        linePosition -= H1_SIZE;
        return this;
    }

    public PdfWriter header2(String string) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, linePosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, H2_SIZE);
        contentStream.showText(string);
        contentStream.endText();

        linePosition -= H2_SIZE +2;
        return this;
    }

    public PdfWriter header3(String string) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, linePosition);
        contentStream.setFont(PDType1Font.HELVETICA, H3_SIZE);
        contentStream.showText(string);
        contentStream.endText();

        linePosition -= H3_SIZE;
        return this;
    }

    protected PdfWriter ensureLength(int length)
    {
        if (linePosition < (length + 200))
        {
            return newPage();
        }
        return this;
    }

    protected PdfWriter newPage()
    {
        try {
            if (this.contentStream != null) {
                this.contentStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.page = new PDPage(PDRectangle.LETTER);
            this.pageNumber++;
            this.document.addPage(page);
            this.contentStream = new PDPageContentStream(document, page);
            this.linePosition = 60;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String formatDateTime = now.format(formatter);

            FormattedString pagenum = new FormattedString(PDType1Font.HELVETICA, 10, "Page " + pageNumber);
            FormattedString genDate = new FormattedString(PDType1Font.HELVETICA, 10, formatDateTime);

            horizontalLine().line(genDate).line(pagenum, WIDTH - MARGIN - pagenum.getWidth());

            this.linePosition = 725;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    protected PdfWriter paragraph(FormattedString string) throws IOException {

        List<FormattedString> strings = parseLines(string, WIDTH - 2*MARGIN);

        for (FormattedString line : strings)
        {
            line(line);

            linePosition -= line.getFontSize();
        }

        return this;
    }

    protected PdfWriter paragraph(FormattedString string, int size) throws IOException {

        List<FormattedString> strings = parseLines(string, WIDTH - 2*MARGIN);

        for (FormattedString line : strings)
        {
            line(line);

            linePosition -= size;
        }

        return this;
    }

    protected PdfWriter line(FormattedString line) throws IOException {
        return line(line, MARGIN);
    }

    protected PdfWriter line(FormattedString line, float indent) throws IOException
    {
        contentStream.beginText();
        contentStream.newLineAtOffset(indent, linePosition);

        FormattedString str = line;

        while (str != null)
        {
            contentStream.setFont(str.getFont(), str.getFontSize());
            contentStream.showText(str.getString());
            str = str.getNext();
        }

        contentStream.endText();
        return this;
    }

    protected static List<FormattedString> parseLines(FormattedString text, float width) throws IOException {
        List<FormattedString> lines = new ArrayList<FormattedString>();
        boolean bulleted = text.isBulleted();
        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = text.length();
            FormattedString subString = text.substring(0, spaceIndex);
            float size = subString.getWidth();
            if (size > width) {
                if (lastSpace < 0){
                    lastSpace = spaceIndex;
                }

                if (bulleted && lines.size() > 0) {
                    subString = text.substring(0, lastSpace).bulletIndent();
                }
                else
                {
                    subString = text.substring(0, lastSpace);
                }

                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                if (bulleted && lines.size() > 0) {
                    lines.add(text.bulletIndent());
                }
                else
                {
                    lines.add(text);
                }
                text = new FormattedString(text.getFont(), text.getFontSize(), "");
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }

}
