package com.sipl.cm.model;

public class ContactDto {

    private String number;
    private String displayName;
    private String contactId;

    public ContactDto(String number, String displayName, String contactId) {
        this.number = number;
        this.displayName = displayName;
        this.contactId = contactId;
    }

    public ContactDto() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
