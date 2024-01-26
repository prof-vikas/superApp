package com.sipl.cm.login.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sipl.cm.R;
import com.sipl.cm.login.activity.LoginActivity;


public class RegisterationFragment extends Fragment {

    private static final String TAG = "RegisterationFragment";
    private EditText edtUserName;
    private EditText edtPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registeration, container, false);

        initialization(view);
        return view;
    }

    private void initialization(View view) {
        edtPassword = view.findViewById(R.id.reg_edt_password);
        edtUserName = view.findViewById(R.id.reg_edt_user_name);
        Button btnRegister = view.findViewById(R.id.reg_btn_register);
        Button btnLogin = view.findViewById(R.id.reg_btn_login);

        btnRegister.setOnClickListener(view1 -> {
            if (checkValidation()) {
                checkAndCreateUser();
            }
        });

        btnLogin.setOnClickListener(view12 -> ((LoginActivity)requireActivity()).loadFragment(new SignInFragment(), 1));
    }

    private boolean checkValidation() {
        if (edtUserName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(requireContext(), "User name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtPassword.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(requireContext(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkAndCreateUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference registerUserRef = database.getReference("registerUser");

        String userId = edtUserName.getText().toString();
        DatabaseReference userRef = registerUserRef.child("user").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User with ID " + userId + " already exists");
                } else {
                    userRef.child("userName").setValue(edtUserName.getText().toString());
                    userRef.child("password").setValue(edtPassword.getText().toString());
                    userRef.child("userRole").setValue("client");

                    DatabaseReference userPhoneInfo = userRef.child("userPhoneInfo");
                    userPhoneInfo.child("userPhoneModelNo").setValue(getPhoneModel());
                    userPhoneInfo.child("userPhoneAndroidVersion").setValue(getAndroidVersion());
                    userPhoneInfo.child("userPhoneAndroidVersionName").setValue(getAndroidVersionName());

                    Log.d(TAG, "User with ID " + userId + " added successfully");
                    edtPassword.setText(null);
                    edtUserName.setText(null);
                    Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("Firebase", "Error checking user existence: " + error.getMessage());
            }
        });
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getAndroidVersionName() {
        return Build.VERSION.RELEASE;
    }

}