package com.lexoff.ergowrapper.extractor;

import com.lexoff.ergowrapper.Client;
import com.lexoff.ergowrapper.XMLUtils;
import com.lexoff.ergowrapper.info.Contact;
import com.lexoff.ergowrapper.info.ContactsInfo;
import com.lexoff.ergowrapper.info.Info;

public class ContactsInfoExtractor extends Extractor {
    private String PATH="xml_action.cgi?method=set";
    private String DATA="<?xml version=\"1.0\" encoding=\"US-ASCII\"?> <RGW><param><method>call</method><session>000</session><obj_path>phonebook</obj_path><obj_method>getcontactbypage</obj_method></param><phonebook><getcontactbypage><pagecap>1000</pagecap><pageindex>%d</pageindex></getcontactbypage></phonebook></RGW>";

    public ContactsInfoExtractor(Client client, int page){
        super(client, "");

        setUrl(BASE_URL+PATH);
        setData(String.format(DATA, page));
    }

    @Override
    protected Info buildInfo() {
        ContactsInfo contactsInfo=new ContactsInfo();

        int total = Integer.parseInt(XMLUtils.getString(xmlStrResponse, "contactcount"));
        contactsInfo.setContactsMax(total);

        String contactlist=XMLUtils.getString(xmlStrResponse, "contactlist");

        int searchAfter=0;

        while (true){
            try {
                Contact contact = new Contact();

                int contactOpen = contactlist.indexOf("<contact>", searchAfter) + "<contact>".length();
                int contactClose = contactlist.indexOf("</contact>", searchAfter);

                String contactXML = contactlist.substring(contactOpen, contactClose);

                int index=Integer.parseInt(XMLUtils.getString(contactXML, "index"));

                contact.setIndex(index);

                String name = XMLUtils.getString(contactXML, "name");

                contact.setName(name);

                String mobile = XMLUtils.getString(contactXML, "mobile");

                contact.setMobile(mobile);

                if (contactXML.contains("<home>")) {
                    String home = XMLUtils.getString(contactXML, "home");

                    contact.setHome(home);
                }

                if (contactXML.contains("<email>")) {
                    String email = XMLUtils.getString(contactXML, "email");

                    contact.setEmail(email);
                }

                if (contactXML.contains("<office>")) {
                    String office = XMLUtils.getString(contactXML, "office");

                    contact.setOffice(office);
                }

                int location=Integer.parseInt(XMLUtils.getString(contactXML, "location"));

                contact.setLocation(location);

                contactsInfo.addContact(contact);

                searchAfter = contactClose + "</contact>".length() - 1;

                if (searchAfter < 0 || searchAfter >= contactlist.length()) break;
            } catch (Exception e){
                break;
            }
        }

        return contactsInfo;
    }
}
