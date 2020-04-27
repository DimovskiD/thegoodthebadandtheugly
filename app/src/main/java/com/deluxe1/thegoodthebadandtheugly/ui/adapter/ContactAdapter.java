package com.deluxe1.thegoodthebadandtheugly.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.deluxe1.thegoodthebadandtheugly.R;
import com.deluxe1.thegoodthebadandtheugly.utils.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact>{

    private List<Contact> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtNumber;
        ImageView imgContact;
    }

    public ContactAdapter(List<Contact> data, Context context) {
        super(context, R.layout.contacts_row, data);
        this.dataSet = data;
        this.mContext=context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contacts_row, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.txtNumber = convertView.findViewById(R.id.number);
            viewHolder.imgContact = convertView.findViewById(R.id.imgContact);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(contact.getName());
        viewHolder.txtNumber.setText(contact.getNumber());
        viewHolder.imgContact.setTag(position);
        Glide.with(mContext).load(contact.getPhoto()).apply(RequestOptions.circleCropTransform()).fallback(android.R.drawable.sym_def_app_icon).into(viewHolder.imgContact);
        // Return the completed view to render on screen
        return convertView;
    }
}