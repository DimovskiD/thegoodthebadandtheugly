package com.deluxe1.thegoodthebadandtheugly.utils;

import android.provider.ContactsContract;

public class Constants {
    public static String[] PROJECTION_NUMBERS = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER};
    public static String[] PROJECTION_DETAILS =  new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
}
