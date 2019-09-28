package scot.shaun.cvgen.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.buf.StringUtils;
import scot.shaun.cvgen.model.CvContent;
import scot.shaun.cvgen.model.CvModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CVPdfWriter
{
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final CvModel model;
    private final PDDocument document = new PDDocument();
    private PDPageContentStream contentStream;
    private PDPage page;

    private int linePosition = 725;

    private static final String ICON_GITHUB = "\ueab0";
    private static final String ICON_HOME = "\ue902";
    private static final String ICON_PHONE = "\ue942";
    private static final String ICON_EMAIL = "\ue945";
    private static final String ICON_LINKEDIN = "\ueac9";
    private static final String ICON_WWW = ICON_HOME;

    public CVPdfWriter(CvModel model)
    {
        this.model = model;
        this.page = new PDPage(PDRectangle.LETTER);
        this.document.addPage(page);
    }


    public byte[] getPdf() throws IOException {
        try
        {
            //  Update the document information.
            PDDocumentInformation info = new PDDocumentInformation();
            info.setAuthor(model.getContent().getForename() + " " + model.getContent().getSurname());
            info.setCreationDate(Calendar.getInstance());
            info.setTitle(info.getAuthor() + " - Curriculum Vitae");
            document.setDocumentInformation(info);

            //  Create the initial page.
            this.contentStream = new PDPageContentStream(document, page);

            writePageHeader(model.getContent().getForename() + " " + model.getContent().getSurname());

            writeHorizontalLine();

            writeContactDetails(document, model,50, linePosition);
            writeTextSection("About Me", model.getContent().getAbout());
            writeSkillsSection( "Primary Skills", model.getContent().getSkills());
            writeProfessionalExperienceSection("Professional Experience", model.getContent().getExperience());
            writeEducationSection("Education", model.getContent().getEducation());
            writeLanguageSection("Languages", model.getContent().getLanguages());
            writeHobbiesSection("Personal Interests", model.getContent().getHobbies());

        }
        finally {
            this.contentStream.close();
            document.save(baos);

            this.document.close();
        }


        return baos.toByteArray();
    }

    private void ensureLength(int length)
    {
        if (linePosition < (length + 200))
        {
            try {
                this.contentStream.close();
                this.page = new PDPage(PDRectangle.LETTER);
                this.document.addPage(page);
                this.contentStream = new PDPageContentStream(document, page);
                this.linePosition = 725;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeHobbiesSection(String header, List<String> hobbies) throws IOException {
        writeSectionHeader(header);
        for (String hobby: hobbies)
        {
            FormattedString s = new FormattedString(PDType1Font.HELVETICA, 10, hobby).bullet();

            writeParagraph(s);
//            linePosition -= 10;
        }

        linePosition -= 20;

    }

    private void writeProfessionalExperienceSection(String header, List<CvContent.Employment> employment) throws IOException {
        writeSectionHeader(header);
        for (CvContent.Employment e : employment)
        {
            //  Prepare the update and check length.
            List<FormattedString> duties = new ArrayList<>();

            int length = 34;
            for (String paragraph : e.getMainDuties())
            {
                FormattedString s = new FormattedString(PDType1Font.HELVETICA, 14, "\u2022 ", true);
                s.append(PDType1Font.HELVETICA, 10, paragraph);

                length += parseLines(s, 512).size();
                duties.add(s);
            }

            FormattedString s = new FormattedString(PDType1Font.HELVETICA_BOLD, 10, "Key Skills: ");
            s.append(PDType1Font.HELVETICA, 10, StringUtils.join(e.getSkills(), ',').replace(",",", "));

            length += parseLines(s, 512).size();


            ensureLength(length);

            writeSectionSubheader(e.getTitle());
            writeSectionSubheader2( e.getEmployer().getName() + ", " + e.getEmployer().getLocation());
            for (FormattedString duty : duties)
            {
                writeParagraph(duty, 10);
            }
            linePosition -= 10;
            writeParagraph(s);
            linePosition -= 10;
        }
        linePosition -= 15;
    }

    private void writeEducationSection(String header, List<CvContent.Education> education) throws IOException {
        ensureLength(16 + (34*education.size()));
        writeSectionHeader(header);
        for (CvContent.Education e : education)
        {
            //  Prepare the update and check length.
            List<FormattedString> notes = new ArrayList<>();
            int length = 34;
            for (String paragraph : e.getNotes())
            {
                FormattedString s = new FormattedString(PDType1Font.HELVETICA, 14, "\u2022 ", true);
                s.append(PDType1Font.HELVETICA, 10, paragraph);

                length += parseLines(s, 512).size();
                notes.add(s);
            }

            ensureLength(length);

            writeSectionSubheader(e.getTitle());
            writeSectionSubheader2( e.getInstitution().getName() + ", " + e.getInstitution().getLocation());
            for (FormattedString note : notes)
            {
                writeParagraph(note, 10);
            }
            linePosition -= 10;
        }
        linePosition -= 15;
    }

    private void writeTextSection(String header, List<String> paragraphs) throws IOException {
        //  Check page length
        int height = 16;
        for (String paragraph : paragraphs)
        {
            height += parseLines(new FormattedString(PDType1Font.HELVETICA, 10, paragraph), 512).size();
        }

        ensureLength(height);

        writeSectionHeader(header);
        for (String paragraph : paragraphs)
        {
            writeParagraph(new FormattedString(PDType1Font.HELVETICA, 10, paragraph));
            linePosition -= 10;
        }
        linePosition -= 10;
    }

    private void writeSkillsSection( String header, List<CvContent.Skill> skills) throws IOException {
        writeSectionHeader(header);
        for (CvContent.Skill skill : skills)
        {
            FormattedString s = new FormattedString(PDType1Font.HELVETICA_BOLD, 10, skill.getCategory() + ": ");
            s.append(PDType1Font.HELVETICA, 10, StringUtils.join(skill.getSkills(), ',').replace(",",", "));

            writeParagraph(s.bullet());
//            linePosition -= 10;
        }

        linePosition -= 20;
    }

    private void writeLanguageSection( String header, List<CvContent.Language> languages) throws IOException {
        writeSectionHeader(header);
        for (CvContent.Language language : languages)
        {
            FormattedString s = new FormattedString(PDType1Font.HELVETICA_BOLD, 10, language.getLanguage() + ": ");
            s.append(PDType1Font.HELVETICA, 10, language.getNote());
            writeParagraph(s.bullet());
        }

        linePosition -= 20;
    }

    private void writeParagraph(FormattedString string) throws IOException {

        List<FormattedString> strings = parseLines(string, 512);

        for (FormattedString line : strings)
        {
            writeLine(line);

            linePosition -= line.getFontSize();
        }
    }

    private void writeParagraph(FormattedString string, int size) throws IOException {

        List<FormattedString> strings = parseLines(string, 512);

        for (FormattedString line : strings)
        {
            writeLine(line);

            linePosition -= size;
        }
    }

    private void writeLine(FormattedString line) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, linePosition);

        FormattedString str = line;

        while (str != null)
        {
            contentStream.setFont(str.getFont(), str.getFontSize());
            contentStream.showText(str.getString());
            str = str.getNext();
        }

        contentStream.endText();

    }

    private void writePageHeader(String string) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, linePosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 30);
        contentStream.showText(string);
        contentStream.endText();

        linePosition -= 10;
    }

    private void writeSectionHeader(String string) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, linePosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.showText(string);
        contentStream.endText();

        linePosition -= 16;
    }

    private void writeSectionSubheader(String string) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, linePosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText(string);
        contentStream.endText();

        linePosition -= 14;
    }

    private void writeSectionSubheader2(String string) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, linePosition);
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.showText(string);
        contentStream.endText();

        linePosition -= 20;
    }

    private void writeHorizontalLine() throws IOException {
        contentStream.moveTo(50, linePosition);
        contentStream.lineTo(562, linePosition);
        contentStream.stroke();
        linePosition -= 15;
    }

    private void writeContactDetails(PDDocument document, CvModel model, int startx, int starty) throws IOException {
        PDType0Font symbolFont = null;
        int fontSize = 10;

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("icomoon.ttf"))
        {
            symbolFont = PDType0Font.load(document, resourceAsStream);
        }

        PdfTableWriter table = new PdfTableWriter(contentStream, startx, starty);

        //  Add address info
        table.beginCell()
             .writeWithFont(symbolFont, fontSize, ICON_HOME + " ")
             .writeWithFont(PDType1Font.HELVETICA, fontSize,"11 Juanita Dr, " + model.getContent().getContacts().getAddress().getCity() + ", " +
                     model.getContent().getContacts().getAddress().getProvince() + ", L9C 2G2" )
             .endCell();

        //  Add Phone numbers
        for (CvContent.ContactTelephone telephone : model.getContent().getContacts().getTelephone())
        {
            table.beginCell()
                 .writeWithFont(symbolFont, fontSize, ICON_PHONE + " ")
                 .writeWithFont(PDType1Font.HELVETICA, fontSize,
                    "+" + telephone.getNumber().getCountry() +
                            " (" + telephone.getNumber().getArea() + ") " +
                            telephone.getNumber().getNumber())
                 .endCell();
        }

        //  Add email
        for (CvContent.ContactEmail email : model.getContent().getContacts().getEmail())
        {
            table.beginCell()
                 .writeWithFont(symbolFont, fontSize, ICON_EMAIL + " ")
                 .writeWithFont(PDType1Font.HELVETICA, fontSize, email.getRecipient() + "@" + email.getDomain())
                 .endCell();
        }

        for (CvContent.ContactLink link : model.getContent().getContacts().getLinks())
        {
            table.beginCell()
                    .writeWithFont(symbolFont, fontSize, getIconForLink(link.getType()) + " ")
                    .writeWithFont(PDType1Font.HELVETICA, fontSize, link.getValue())
                    .endCell();
        }

        linePosition -= ((1+table.getRow()) * table.getColumnHeight() + 10);
    }

    private String getIconForLink(String type)
    {
        switch (type) {
            case "github":
                return ICON_GITHUB;
            case "linkedin":
                return ICON_LINKEDIN;
            case "www":
                return ICON_WWW;
            default:
                return "";
        }
    }

    private static List<FormattedString> parseLines(FormattedString text, float width) throws IOException {
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
