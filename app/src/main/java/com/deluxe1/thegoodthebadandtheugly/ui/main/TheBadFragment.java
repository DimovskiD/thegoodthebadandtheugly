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
import java.util.List;

public class TheBadFragment extends TheNeutralFragment {

    TheBadFragment.BadLoading t;

    public static TheBadFragment newInstance() {
        return new TheBadFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoading();
        t = new TheBadFragment.BadLoading(getActivity().getContentResolver(), TheBadFragment.this);
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

    private static class BadLoading extends AsyncTask<Void, Void, List<Contact>> {
        private ContentResolver cr;
        private AsyncResponseListener listener;

        private BadLoading(ContentResolver contentResolver, AsyncResponseListener responseListener) {
            this.cr = contentResolver;
            this.listener = responseListener;
        }

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            List<Contact> contacts = new ArrayList<>();
            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    long id = cur.getLong(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));
                    String photo = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{String.valueOf(id)}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Contact c = new Contact(id,name,phoneNo, photo);
                            contacts.add(c);
                            listener.newContactFetched(c);
                        }
                        pCur.close();
                    }
                }
            }
            if(cur!=null){
                cur.close();
            }
            return contacts;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);
            listener.onResponse(contacts);
        }
    }
}
