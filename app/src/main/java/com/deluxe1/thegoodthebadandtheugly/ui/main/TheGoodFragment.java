package com.deluxe1.thegoodthebadandtheugly.ui.main;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.deluxe1.thegoodthebadandtheugly.utils.Constants;
import com.deluxe1.thegoodthebadandtheugly.utils.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TheGoodFragment extends TheNeutralFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static TheGoodFragment newInstance() {
        return new TheGoodFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        startLoading();
        switch (id) {
            case 0:
                return new CursorLoader(
                        getActivity(),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        Constants.PROJECTION_NUMBERS,
                        null,
                        null,
                        null
                );
            default:
                return new CursorLoader(
                        getActivity(),
                        ContactsContract.Contacts.CONTENT_URI,
                        Constants.PROJECTION_DETAILS,
                        null,
                        null,
                        null
                );
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                phones = new HashMap<>();
                if (data != null) {
                    while (!data.isClosed() && data.moveToNext()) {
                        long contactId = data.getLong(0);
                        String phone = data.getString(1);
                        List<String> list;
                        if (phones.containsKey(contactId)) {
                            list = phones.get(contactId);
                        } else {
                            list = new ArrayList<>();
                            phones.put(contactId, list);
                        }
                        list.add(phone);
                    }
                    data.close();
                }
                LoaderManager.getInstance(TheGoodFragment.this).initLoader(1,null,this);
                break;
            case 1:
                if (data!=null) {
                    while (!data.isClosed() && data.moveToNext()) {
                        long contactId = data.getLong(0);
                        String name = data.getString(1);
                        String photo = data.getString(2);
                        List<String> contactPhones = phones.get(contactId);
                        if (contactPhones != null) {
                            for (String phone :
                                    contactPhones) {
                                addContact(new Contact(contactId, name, phone, photo));
                            }
                        }
                    }
                    data.close();
                    loadAdapter();
                }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
