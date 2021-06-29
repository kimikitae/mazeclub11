package com.mazeclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        TextInputEditText input_email_text = findViewById(R.id.logIn_input_email);
        TextInputEditText input_password_text = findViewById(R.id.logIn_input_password);

        Button btnLogIn = (Button) findViewById(R.id.btn_logIn);                 //로그인 버튼 눌렀을때
        btnLogIn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input_email_text.getText().toString().equals("") && !input_password_text.getText().toString().equals("")) {
                    loginUser(input_email_text.getText().toString(), input_password_text.getText().toString());
                } else {
                    Toast.makeText(LogInActivity.this, "Please enter your account and password.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnSignUp = (Button)findViewById(R.id.btn_signUp);                 //사인업버튼 눌렀을때
        btnSignUp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LogInActivity.this, "not", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {                                // 로그인 성공
                            Toast.makeText(LogInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);
                        }
                        else {
                            //로그인 실패
                            Toast.makeText(LogInActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() { super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(firebaseAuthListener!=null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


}




