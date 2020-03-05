package com.example.l2595.lab4;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

//public class ContactTest extends ContentProvider {
//    private static final String TAG= "ContactTest";
//    public void testGetContact() {
//        ContentResolver contentResolver = this.getContext().getContactResolver();
//        Uri uri = Uri.parse("content://com.android.contacts/contacts");
//        Cursor cursor = contentResolver.query(uri, null, null, null, null);
//        while (cursor.moveToNext()) {
//            StringBuilder sb = new StringBuilder();
//            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            sb.append("contactId=").append(contactId).append(",name=").append(name);
//            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactId,null,null);
//            while (phones.moveToNext()){
//                String phone = phones.getString(phones.getColumnIndex("datal"));
//                sb.append(",phone=").append(phone);
//            }
//            Log.i(TAG,sb.toString());
//        }
//
//    }
//}
