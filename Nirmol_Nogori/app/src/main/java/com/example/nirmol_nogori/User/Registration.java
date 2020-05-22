package com.example.nirmol_nogori.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.nirmol_nogori.Ui.Home_Activity;
import com.example.nirmol_nogori.Users;
import com.example.nirmol_nogori.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        //Underline some text
        binding.textViewLoginFromRegistration.setPaintFlags(binding.textViewLoginFromRegistration.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.textViewFromRegistrationNeedHelp.setPaintFlags(binding.textViewFromRegistrationNeedHelp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        binding.buttonRegistration.setOnClickListener(this);
        binding.textViewLoginFromRegistration.setOnClickListener(this);
        binding.textViewFromRegistrationNeedHelp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == binding.textViewLoginFromRegistration) {

            startActivity(new Intent(Registration.this, Login_User.class));

        } else if (binding.buttonRegistration == v) {
            userRegistration();
        } else {
            //Todo perform need help to create another method
        }

    }


    String first_name, last_name, user_email, user_password, user_con_password;

    public void userRegistration() {

        first_name = binding.edittextFirstname.getText().toString().trim();
        last_name = binding.edittextLastname.getText().toString().trim();
        user_email = binding.edittextRegistrationEmail.getText().toString().trim();
        user_password = binding.edittextRegistrationPassword.getText().toString().trim();
        user_con_password = binding.edittextRegistrationConPassword.getText().toString().trim();

        userRegistrationValidation();

        firebaseAuth.createUserWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Users user = new Users(first_name, last_name, user_email);
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Registration.this, "Registration complete", Toast.LENGTH_SHORT).show();
                                    after_registration();
                                    Intent intent = new Intent(Registration.this, Home_Activity.class);
                                    startActivity(intent);

                                }
                            });
                        } else {
                            Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    //set all filed null
    private void after_registration() {
        binding.edittextFirstname.setText(" ");
        binding.edittextLastname.setText(" ");
        binding.edittextRegistrationEmail.setText(" ");
        binding.edittextRegistrationPassword.setText(" ");
        binding.edittextRegistrationConPassword.setText(" ");
    }


    //check all Validation
    public void userRegistrationValidation() {

        if (first_name.isEmpty()) {
            binding.edittextFirstname.setError("Enter a First name please");
            binding.edittextFirstname.requestFocus();
            return;
        }

        if (last_name.isEmpty()) {

            binding.edittextLastname.setError("Enter a Last name please");
            binding.edittextLastname.requestFocus();
            return;

        }

        if (user_email.isEmpty()) {

            binding.edittextRegistrationEmail.setError("Enter a Email please");
            binding.edittextRegistrationEmail.requestFocus();
            return;

        }

        if (user_password.isEmpty()) {

            binding.edittextRegistrationPassword.setError("Enter a Password please");
            binding.edittextRegistrationPassword.requestFocus();
            return;

        }

        if (user_con_password.isEmpty()) {

            binding.edittextRegistrationConPassword.setError("Enter a Confirm Password please");
            binding.edittextRegistrationConPassword.requestFocus();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {

            binding.edittextRegistrationEmail.setError("Enter a Valid Email please");
            binding.edittextRegistrationEmail.requestFocus();
            return;

        }

        if (user_password.length() < 6) {
            binding.edittextRegistrationPassword.setError("Enter a atleast 6 digit Password please");
            binding.edittextRegistrationPassword.requestFocus();
            return;
        }
        if (user_con_password.length() < 6) {
            binding.edittextRegistrationConPassword.setError("Enter a atleast 6 digit Password please");
            binding.edittextRegistrationConPassword.requestFocus();
            return;
        }
        if (!user_password.equals(user_con_password)) {
            binding.edittextRegistrationPassword.setError("Password and Confirm Password must be same");
            binding.edittextRegistrationPassword.requestFocus();
            return;

        }

    }

}
