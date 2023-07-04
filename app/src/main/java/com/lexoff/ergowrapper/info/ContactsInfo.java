package com.lexoff.ergowrapper.info;

import java.util.ArrayList;

public class ContactsInfo extends Info {
    private ArrayList<Contact> contactsList;

    private int contactsMax;

    public ContactsInfo(){
        contactsList=new ArrayList<>();
    }


    public ArrayList<Contact> getContacts() {
        return contactsList;
    }

    public void addContact(Contact contact) {
        contactsList.add(contact);
    }

    public int getContactsMax() {
        return contactsMax;
    }

    public void setContactsMax(int contactsMax) {
        this.contactsMax = contactsMax;
    }
}
