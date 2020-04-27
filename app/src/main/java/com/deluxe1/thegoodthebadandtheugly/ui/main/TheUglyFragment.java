package com.deluxe1.thegoodthebadandtheugly.ui.main;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.deluxe1.thegoodthebadandtheugly.utils.AsyncResponseListener;
import com.deluxe1.thegoodthebadandtheugly.utils.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheUglyFragment extends TheNeutralFragment implements AsyncResponseListener {

    private TheUglyFragment.UglyLoading t;

    public static TheUglyFragment newInstance() {
        return new TheUglyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoading();
        t = new TheUglyFragment.UglyLoading(getActivity().getContentResolver(), TheUglyFragment.this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                t.execute();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (t!=null)
            t.cancel(true);
    }

    private static class UglyLoading extends AsyncTask<Void, Void, List<Contact>> {

        private ContentResolver resolver;
        private AsyncResponseListener listener;

        private UglyLoading(ContentResolver contentResolver, AsyncResponseListener responseListener) {
            this.resolver = contentResolver;
            this.listener = responseListener;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);
            listener.onResponse(contacts);
        }

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            List<Contact> contacts = new ArrayList<>();

            Map<Long, List<String>> phones = new HashMap<>();
            Cursor getContactsCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
                    null, null, null);
            if (getContactsCursor != null) {
                while (getContactsCursor.moveToNext()) {
                    long contactId = getContactsCursor.getLong(0);
                    String phone = getContactsCursor.getString(1);
                    List<String> list;
                    if (phones.containsKey(contactId)) {
                        list = phones.get(contactId);
                    } else {
                        list = new ArrayList<>();
                        phones.put(contactId, list);
                    }
                    list.add(phone);
                }
                getContactsCursor.close();
            }
            getContactsCursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                    new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.PHOTO_URI},
                    null, null, null);
            while (getContactsCursor != null && getContactsCursor.moveToNext()) {
                long contactId = getContactsCursor.getLong(0);
                String name = getContactsCursor.getString(1);
                String photo = getContactsCursor.getString(2);
                List<String> contactPhones = phones.get(contactId);
                if (contactPhones != null) {
                    for (String phone :
                            contactPhones) {
                        Contact c = new Contact(contactId, name, phone, photo);
                        listener.newContactFetched(c);
                        contacts.add(c);
                    }
                }
            }
            return contacts;
        }
    }
}
