package com.deluxe1.thegoodthebadandtheugly.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deluxe1.thegoodthebadandtheugly.R;
import com.deluxe1.thegoodthebadandtheugly.ui.adapter.ContactAdapter;
import com.deluxe1.thegoodthebadandtheugly.utils.AsyncResponseListener;
import com.deluxe1.thegoodthebadandtheugly.utils.Contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheNeutralFragment extends Fragment implements AsyncResponseListener {
    protected ListView listView;
    protected ProgressBar spinner;
    protected Map<Long, List<String>> phones = new HashMap<>();
    protected List<Contact> contacts = new ArrayList<>();
    protected Date start, end;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        listView = v.findViewById(R.id.list);
        spinner = v.findViewById(R.id.spinner);
        return v;
    }

    protected void startLoading() {
        start = Calendar.getInstance().getTime();
    }

    protected void loadAdapter() {
        end = Calendar.getInstance().getTime();
        Toast.makeText(getActivity(), (end.getTime() - start.getTime()) + " ms", Toast.LENGTH_LONG).show();
        Log.i("CONTACT_NUM", String.valueOf(contacts.size()));
        listView.setAdapter(new ContactAdapter(contacts, getActivity()));
        spinner.setVisibility(View.GONE);
    }

    protected void addContact(Contact contact) {
        contacts.add(contact);
    }


    @Override
    public void newContactFetched(Contact contact) {
        addContact(contact);
    }

    @Override
    public void onResponse(List<Contact> contacts) {
        loadAdapter();
    }

}
