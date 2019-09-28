package scot.shaun.cvgen.util;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class FormattedString
{

    private final PDFont font;
    private final int fontSize;
    private final String string;
    private boolean bulleted;
    private FormattedString next;


    public FormattedString(PDFont font, int fontSize, String string)
    {
        this.font = font;
        this.fontSize = fontSize;
        this.string = string;
    }

    public FormattedString(PDFont font, int fontSize, String string, boolean bulleted)
    {
        this.font = font;
        this.fontSize = fontSize;
        this.string = string;
        this.bulleted = bulleted;
    }

    public FormattedString bulletIndent()
    {
        FormattedString indent = new FormattedString(font, fontSize, "   ");
        indent.setNext(this);
        return indent;
    }

    public FormattedString bullet()
    {
        FormattedString indent = new FormattedString(font, fontSize +2, "\u2022 ", true);
        indent.setNext(this);
        return indent;
    }

    public boolean isBulleted() {
        return bulleted;
    }

    public void setBulleted(boolean bulleted) {
        this.bulleted = bulleted;
    }

    public void setNext(FormattedString next) {
        this.next = next;
    }

    public PDFont getFont() {
        return font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getString() {
        return string;
    }

    public FormattedString substring(int beginIndex, int endIndex)
    {
        if (beginIndex >= string.length() && hasNext())
        {
            return getNext().substring(beginIndex - string.length(), endIndex - string.length());
        }

        if (endIndex >= string.length() && hasNext())
        {
            FormattedString out =
                new FormattedString(font, fontSize, string.substring(beginIndex));
            out.append(getNext().substring(0, endIndex - string.length()));
            return out;
        }

        return new FormattedString(font, fontSize, string.substring(beginIndex, endIndex));
    }

    public FormattedString substring(int beginIndex)
    {
        return substring(beginIndex, length());
    }

    public float getWidth() throws IOException {
        return (fontSize * font.getStringWidth(string) / 1000) + (hasNext() ? getNext().getWidth() : 0);
    }

    public FormattedString trim()
    {
        return new FormattedString(font, fontSize, string.trim());
    }

    public int indexOf(char ch, int fromIndex)
    {
        if (fromIndex >= string.length() && hasNext())
        {
            int iof = getNext().indexOf(ch,  fromIndex - string.length());
            if (iof >= 0)
            {
                return string.length() + iof;
            }
            return -1;
        }

        int iof = string.indexOf(ch, fromIndex);

        if (iof < 0 && hasNext())
        {
            return string.length() + getNext().indexOf(ch);
        }

        return iof;
    }

    public int indexOf(char ch)
    {
        int iof = string.indexOf(ch);
        if (iof < 0 && hasNext())
        {
            return string.length() + getNext().indexOf(ch);
        }

        return iof;
    }

    public int length()
    {
        return string.length() + (hasNext() ? getNext().length() : 0);
    }

    public boolean hasNext()
    {
        return this.next != null;
    }

    public FormattedString getNext()
    {
        return next;
    }

    public FormattedString append(FormattedString str)
    {
        this.next = str;

        return str;
    }

    public FormattedString append(PDFont font, int fontSize, String str)
    {
        this.next = new FormattedString(font, fontSize, str);
        return next;
    }
}
