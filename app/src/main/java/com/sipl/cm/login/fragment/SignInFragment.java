package com.sipl.cm.login.fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.sipl.cm.utils.PreferencesConstants.LOGIN_CREDENTIALS;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.sipl.cm.MainActivity;
import com.sipl.cm.R;
import com.sipl.cm.login.activity.LoginActivity;

public class SignInFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view) {

        edtPassword = view.findViewById(R.id.log_in_edt_password);
        edtUserName = view.findViewById(R.id.log_in_edt_user_name);
        Button btnRegister = view.findViewById(R.id.log_in_btn_register);
        Button btnLogin = view.findViewById(R.id.log_in_btn_login);

        btnRegister.setOnClickListener(view1 -> ((LoginActivity) requireActivity()).loadFragment(new RegisterationFragment(), 1));

        btnLogin.setOnClickListener(view12 -> {
            if (checkValidation()) {
                loginIn();
            }
        });
    }

    private void loginIn() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("registerUser").child("user");

        userRef.child(edtUserName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (edtPassword.getText().toString().equals(storedPassword)) {
                        String userName = dataSnapshot.child("userName").getValue(String.class);
                        String userRole = dataSnapshot.child("userRole").getValue(String.class);
                        Log.d(TAG, "onDataChange: SignInDone with username : " + userName + " : Password : " + storedPassword + " : userRoles : " + userRole);
                        saveUserDetails(userName, userRole);
                    } else {
                        Toast.makeText(requireContext(), "Username or password is wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Username or password is wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void saveUserDetails(String userName, String userRole) {
        SharedPreferences sp = requireActivity().getSharedPreferences(LOGIN_CREDENTIALS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName_uc", userName).apply();
        editor.putString("userRole_uc", userRole).apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
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
}