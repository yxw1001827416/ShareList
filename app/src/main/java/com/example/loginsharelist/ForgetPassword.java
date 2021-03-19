package com.example.loginsharelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText resetEmail;
    private Button resetPasswordButton;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        auth = FirebaseAuth.getInstance();

        resetEmail = (EditText) findViewById(R.id.resetEmail);

        // If the user click the reset password button,
        // it will reset the password
        resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.resetPasswordButton) {
            // If the user click the create account button
            // it will start the reset password activity
            resetPasswordActivity();
        }
    }

    private void resetPasswordActivity() {
        // everything will be converted to the string
        String email = resetEmail.getText().toString().trim();

        // validate that everything is not empty
        if (email.isEmpty()) {
            resetEmail.setError("It should not be empty. ");
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPassword.this, "Check your email to reset the password. ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgetPassword.this, "The email is wrong. ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}