package scot.shaun.cvgen.util;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.buf.StringUtils;
import scot.shaun.cvgen.model.CurriculumVitae;
import scot.shaun.cvgen.model.CvModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CVPdfWriter extends PdfWriter
{
    private static final String ICON_LINKEDIN = "\ueac9";
    private static final String ICON_GITHUB = "\ueab0";
    private static final String ICON_PHONE = "\ue942";
    private static final String ICON_EMAIL = "\ue945";
    private static final String ICON_HOME = "\ue902";
    private static final String ICON_WWW = "\ue9ca";
    private static final String ICON_PDF = "\ueadf";
    private static final String ICON_DOWNLOAD = "\ue95e";

    private final CurriculumVitae model;

    public CVPdfWriter(CvModel model)
    {
        super();
        this.model = model.getContent();
    }

    @Override
    public void setTitleAndAuthor(String title, String author)
    {
        //  Update the document information.
        PDDocumentInformation info = new PDDocumentInformation();
        info.setAuthor(author);
        info.setCreationDate(Calendar.getInstance());
        info.setTitle(author + " - Curriculum Vitae");
        document.setDocumentInformation(info);
    }

    @Override
    public byte[] getPdf() throws IOException {
        try
        {
            setTitleAndAuthor(
                    model.getForename() + " " + model.getSurname(),
                    model.getForename() + " " + model.getSurname() + " - Curriculum Vitae");

            //  Create the initial page.

            title(model.getForename() + " " + model.getSurname()).horizontalLine();

            writeContactDetails();
            writeTextSection("About Me", model.getAbout());
            writeSkillsSection( "Primary Skills", model.getSkills());
            writeProfessionalExperienceSection("Professional Experience", model.getExperience());
            writeEducationSection("Education", model.getEducation());
            writeLanguageSection("Languages", model.getLanguages());
            writeHobbiesSection("Personal Interests", model.getHobbies());

        }
        finally {
            this.contentStream.close();
            document.save(baos);

            this.document.close();
        }


        return baos.toByteArray();
    }




    private void writeHobbiesSection(String header, List<CurriculumVitae.Hobby> hobbies) throws IOException {
        header1(header);
        for (CurriculumVitae.Hobby hobby: hobbies)
        {
            FormattedString s = new FormattedString(PDType1Font.HELVETICA_BOLD, 10, hobby.getHobby() + ": ");
            s.append(PDType1Font.HELVETICA, 10, hobby.getDescription());

            paragraph(s.bullet());
        }

        linePosition -= 20;

    }

    private void writeProfessionalExperienceSection(String header, List<CurriculumVitae.Employment> employment) throws IOException {
        header1(header);
        for (CurriculumVitae.Employment e : employment)
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

            header2(e.getTitle());
            header3( e.getEmployer().getName() + ", " + e.getEmployer().getLocation());
            paragraph(new FormattedString(PDType1Font.HELVETICA, 10,
                    e.getStartDate() + " - " + e.getEndDate()));
            linePosition -= 10;
            for (FormattedString duty : duties)
            {
                paragraph(duty, 10);
            }
            linePosition -= 10;
            paragraph(s);
            linePosition -= 15;
        }
        linePosition -= 10;
    }

    private void writeEducationSection(String header, List<CurriculumVitae.Education> education) throws IOException {
        ensureLength(16 + (34*education.size()));
        header1(header);
        for (CurriculumVitae.Education e : education)
        {
            //  Prepare the update and check length.
            List<FormattedString> notes = new ArrayList<>();
            int length = 34;
            if (e.getNotes() != null) {
                for (String paragraph : e.getNotes()) {
                    FormattedString s = new FormattedString(PDType1Font.HELVETICA, 14, "\u2022 ", true);
                    s.append(PDType1Font.HELVETICA, 10, paragraph);

                    length += parseLines(s, 512).size();
                    notes.add(s);
                }
            }

            ensureLength(length);

            header2(e.getTitle());
            header3( e.getInstitution().getName() + ", " + e.getInstitution().getLocation());
            paragraph(new FormattedString(PDType1Font.HELVETICA, 10,
                    e.getStartDate() + " - " + e.getEndDate()));
            linePosition -= 10;
            if (e.getNotes() != null) {
                for (FormattedString note : notes) {
                    paragraph(note, 10);
                }
                linePosition -= 10;
            }
        }
        linePosition -= 10;
    }

    private void writeTextSection(String header, List<String> paragraphs) throws IOException {
        //  Check page length
        int height = 16;
        for (String paragraph : paragraphs)
        {
            height += parseLines(new FormattedString(PDType1Font.HELVETICA, 10, paragraph), 512).size();
        }

        ensureLength(height);

        header1(header);
        for (String paragraph : paragraphs)
        {
            paragraph(new FormattedString(PDType1Font.HELVETICA, 10, paragraph));
            linePosition -= 10;
        }
        linePosition -= 10;
    }

    private void writeSkillsSection( String header, List<CurriculumVitae.Skill> skills) throws IOException {
        header1(header);
        for (CurriculumVitae.Skill skill : skills)
        {
            FormattedString s = new FormattedString(PDType1Font.HELVETICA_BOLD, 10, skill.getCategory() + ": ");
            s.append(PDType1Font.HELVETICA, 10, StringUtils.join(skill.getSkills(), ',').replace(",",", "));

            paragraph(s.bullet());
//            linePosition -= 10;
        }

        linePosition -= 20;
    }

    private void writeLanguageSection( String header, List<CurriculumVitae.Language> languages) throws IOException {
        header1(header);

        for (CurriculumVitae.Language language : languages)
        {
            FormattedString s = new FormattedString(PDType1Font.HELVETICA_BOLD, 10, language.getLanguage() + ": ");
            s.append(PDType1Font.HELVETICA, 10, language.getNote());
            paragraph(s.bullet());
        }

        linePosition -= 20;
    }


    private void writeContactDetails() throws IOException {
        final PDType0Font symbolFont;
        int fontSize = 10;

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("icomoon.ttf"))
        {
            symbolFont = PDType0Font.load(document, resourceAsStream);
        }

        PdfFixedColumnWriter table = beginColumnLayout(3);

        //  Add address info
        table.beginCell()
             .writeWithFont(symbolFont, fontSize, ICON_HOME + " ")
             .writeWithFont(PDType1Font.HELVETICA, fontSize,
                     model.getContacts().getAddress().getStreet() + ", " +
                          model.getContacts().getAddress().getCity() + ", " +
                          model.getContacts().getAddress().getProvince() + ", " +
                          model.getContacts().getAddress().getPostalCode())
             .endCell();

        //  Add Phone numbers
        model.getContacts().getTelephone()
             .forEach( (telephone) -> table.beginCell()
                                           .writeWithFont(symbolFont, fontSize, ICON_PHONE + " ")
                                           .writeWithFont(PDType1Font.HELVETICA, fontSize,
                                                 "+" + telephone.getNumber().getCountry() +
                                                         " (" + telephone.getNumber().getArea() + ") " +
                                                         telephone.getNumber().getNumber())
                                           .endCell());

        //  Add emails
        model.getContacts().getEmail()
             .forEach( (email) ->  table.beginCell()
                                        .writeWithFont(symbolFont, fontSize, ICON_EMAIL + " ")
                                        .writeWithFont(PDType1Font.HELVETICA, fontSize, email.getRecipient() + "@" + email.getDomain())
                                        .endCell());

        //  Add links
        model.getContacts().getLinks()
             .forEach( (link) -> table.beginCell()
                                      .writeWithFont(symbolFont, fontSize, getIconForLink(link.getType()) + " ")
                                      .writeWithFont(PDType1Font.HELVETICA, fontSize, link.getValue())
                                      .endCell());

        linePosition -= ((1+table.getRowCount()) * table.getRowHeight() + 10);
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

}
