package com.example.login_register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.Objects;

public class page_login extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    Button btnLogin;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(task ->{
                            if(task.isSuccessful()){
                                saveLoginState(email, password);
                                Toast.makeText(page_login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(page_login.this, Home.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                                Toast.makeText(page_login.this, "Login failed: " + errorCode, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(page_login.this, RegisterPage.class);
                                startActivity(intent);
                                finish();
                            }
                        } );
            }
        });
    }

    private void saveLoginState(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }
}