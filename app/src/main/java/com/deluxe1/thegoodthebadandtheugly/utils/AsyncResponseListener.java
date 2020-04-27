package com.deluxe1.thegoodthebadandtheugly.utils;

import java.util.List;

public interface AsyncResponseListener {

    void newContactFetched(Contact contact);

    void onResponse(List<Contact> contacts);
}
