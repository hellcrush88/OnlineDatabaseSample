package sky.onlinedatabasesample;

/**
 * Created by Sky on 02-Mar-17.
 */

public class Contacts {

    private String name, email, contact, password;

    public Contacts(String name, String email, String contact, String password){
        this.setName(name);
        this.setEmail(email);
        this.setContact(contact);
        this.setPassword(password);
    }

    public String getName(){
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
