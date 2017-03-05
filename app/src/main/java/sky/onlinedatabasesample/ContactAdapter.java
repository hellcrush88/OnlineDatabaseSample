package sky.onlinedatabasesample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sky on 02-Mar-17.
 */

public class ContactAdapter extends ArrayAdapter<Contacts> {

    public ContactAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view = convertView;
        ContactHolder contactHolder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_layout, parent, false);
            contactHolder = new ContactHolder();
            contactHolder.nameText = (TextView)view.findViewById(R.id.name_text);
            contactHolder.urlText = (TextView)view.findViewById(R.id.url_text);
            contactHolder.urlText.setSelected(true);
            view.setTag(contactHolder);
        }else{
            contactHolder = (ContactHolder)view.getTag();
        }

        Contacts contacts = (Contacts)this.getItem(position);
        contactHolder.nameText.setText(contacts.getName());
        contactHolder.urlText.setText(contacts.getUrl());

        return view;
    }

    static class ContactHolder{

        TextView nameText, urlText;

    }
}
