package sky.onlinedatabasesample;

import static android.R.attr.password;

/**
 * Created by Sky on 02-Mar-17.
 */

public class Contacts {

    private int id;
    private String name, url;

    public Contacts(int id, String name, String url){
        this.setId(id);
        this.setName(name);
        this.setUrl(url);
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
