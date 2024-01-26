package com.sipl.cm.contact;

import static android.content.Context.MODE_PRIVATE;
import static com.sipl.cm.themes.DynamicTheme.setDynamicTheme;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sipl.cm.R;
import com.sipl.cm.model.ContactDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactFragment extends Fragment {

    private static final String TAG = "ContactFragment";
    private static final int REQUEST_READ_CONTACTS = 1;
    private final Set<ContactDto> hashSetForContactList = new HashSet<>();
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int selectedTheme = sharedPreferences.getInt("selectedTheme", 0);
        setDynamicTheme(requireActivity(), selectedTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view) {
        Button button = view.findViewById(R.id.i_am_contact);


        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            getContacts();
        }
        requestPermissionCode();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            }
        });
    }


    public void getContacts() {
        if (!hashSetForContactList.isEmpty()) {
            hashSetForContactList.clear();
        }
        Cursor cursor = requireActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        requireActivity().startManagingCursor(cursor);

        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);

        if (numberIndex != -1 && displayNameIndex != -1 && contactIdIndex != -1) {
            if (cursor.moveToFirst()) {
                do {
                    String phoneNumber = cursor.getString(numberIndex);
                    String displayName = cursor.getString(displayNameIndex);
                    String contactId = cursor.getString(contactIdIndex);
                    ContactDto contactDto = new ContactDto();
                    contactDto.setContactId(contactId);
                    contactDto.setDisplayName(displayName);
                    contactDto.setNumber(phoneNumber);
                    hashSetForContactList.add(contactDto);
                } while (cursor.moveToNext());
            }
            contacts();
        }

        cursor.close();
    }

    private void requestPermissionCode() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS)) {
                    openAppSettings();
                } else {
                    requestPermissionCode();
                }
            }
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireActivity().getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private String getUserName() {
        SharedPreferences sp = requireActivity().getSharedPreferences("loginInfoContactMGMT", MODE_PRIVATE);
        return sp.getString("userNameSPKCM", null);
    }

    private List<ContactDto> contacts() {
        if (hashSetForContactList != null) {
            if (hashSetForContactList.size() > 0) {
                List<ContactDto> a = new ArrayList<>(hashSetForContactList);
                for (ContactDto s : a) {
                    Log.d("Apple", "contacts: Id : " + s.getContactId() + " name : " + s.getDisplayName() + " number : " + s.getNumber());
                }
                return a;
            } else {
                Log.e("Apple", "contacts: no contact");
                return null;
            }
        } else {
            Log.e("Apple", "null contact");
            return null;
        }
    }


    private void setData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        List<ContactDto> contactsMap = contacts();
        DatabaseReference contactsRef = database.getReference("registerUser/user/" + getUserName() + "/userPhoneInfo/userPhoneContact");

        contactsRef.child("contact").setValue(contactsMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Data successfully written to Firebase
                    Log.i(TAG, "onComplete: successful");
                } else {
                    // Handle the error
                    Log.i(TAG, "onComplete: " + error.getDetails());
                }
            }
        });
    }
}