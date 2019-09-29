package scot.shaun.cvgen.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurriculumVitae
{
    private String forename;
    private String surname;
    private ContactInformation contacts;
    private List<String> about;
    private List<Skill> skills;
    private List<Employment> experience;
    private List<Education> education;
    private List<Hobby> hobbies;
    private List<Language> languages;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hobby
    {
        private String hobby;
        private String description;

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactInformation
    {
        private ContactAddress address;
        private List<ContactTelephone> telephone;
        private List<ContactLink> links;
        private List<ContactEmail> email;

        public ContactAddress getAddress() {
            return address;
        }

        public void setAddress(ContactAddress address) {
            this.address = address;
        }

        public List<ContactTelephone> getTelephone() {
            return telephone;
        }

        public void setTelephone(List<ContactTelephone> telephone) {
            this.telephone = telephone;
        }

        public List<ContactLink> getLinks() {
            return links;
        }

        public void setLinks(List<ContactLink> links) {
            this.links = links;
        }

        public List<ContactEmail> getEmail() {
            return email;
        }

        public void setEmail(List<ContactEmail> email) {
            this.email = email;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactAddress
    {
        private String street;
        private String city;
        private String province;
        private String postalCode;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactTelephone
    {
        private String type;
        private ContactTelephoneNumber number;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ContactTelephoneNumber getNumber() {
            return number;
        }

        public void setNumber(ContactTelephoneNumber number) {
            this.number = number;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactTelephoneNumber
    {
        private String country;
        private String area;
        private String number;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactLink
    {
        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContactEmail
    {
        private String type;
        private String recipient;
        private String domain;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Skill
    {
        private String category;
        private List<String> skills;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(List<String> skills) {
            this.skills = skills;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Employment
    {
        private String title;
        private String startDate;
        private String endDate;
        private List<String> skills;
        private List<String> mainDuties;
        private Employer employer;

        public String getEndDate() {
            return endDate != null ? endDate : "Present";
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(List<String> skills) {
            this.skills = skills;
        }

        public List<String> getMainDuties() {
            return mainDuties;
        }

        public void setMainDuties(List<String> mainDuties) {
            this.mainDuties = mainDuties;
        }

        public Employer getEmployer() {
            return employer;
        }

        public void setEmployer(Employer employer) {
            this.employer = employer;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Employer
    {
        private String name;
        private String location;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Education
    {
        private String title;
        private String startDate;
        private String endDate;
        private Employer institution;
        private List<String> notes;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public Employer getInstitution() {
            return institution;
        }

        public void setInstitution(Employer institution) {
            this.institution = institution;
        }

        public List<String> getNotes() {
            return notes;
        }

        public void setNotes(List<String> notes) {
            this.notes = notes;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Language
    {
        private String language;
        private String note;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public List<String> getAbout() {
        return about;
    }

    public void setAbout(List<String> about) {
        this.about = about;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ContactInformation getContacts() {
        return contacts;
    }

    public void setContacts(ContactInformation contacts) {
        this.contacts = contacts;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Employment> getExperience() {
        return experience;
    }

    public void setExperience(List<Employment> experience) {
        this.experience = experience;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
}
