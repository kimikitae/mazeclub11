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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;                             //auth 인스턴스 선언
    private TextInputEditText signUp_input_email,signUp_input_passWord,signUp_input_reEnterPassword,signUp_input_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();                 //auth 인스턴스 초기화

        signUp_input_email = findViewById(R.id.signUp_input_email);
        signUp_input_passWord = findViewById(R.id.signUp_input_password);
        signUp_input_reEnterPassword = findViewById(R.id.signUp_input_reEnterPassword);
        signUp_input_userName = findViewById(R.id.signUp_input_userName);

        Button btn_signUp = findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    Toast.makeText(SignUpActivity.this, "SignUp failed", Toast.LENGTH_LONG).show();
                }
                signUp();
            }
        });
    }

    private void signUp(){

        String name = signUp_input_userName.getText().toString();
        String email = signUp_input_email.getText().toString();
        String password = signUp_input_passWord.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeData();
                            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                            intent.putExtra("userName",name);
                            intent.putExtra("userEmail",email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean validate() {
        boolean valid = true;

        String name = signUp_input_userName.getText().toString();
        String email = signUp_input_email.getText().toString();
        String password = signUp_input_passWord.getText().toString();
        String reEnterPassword = signUp_input_reEnterPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            signUp_input_userName.setError("at least 3 characters");
            valid = false;
        } else {
            signUp_input_userName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUp_input_email.setError("enter a valid email address");
            valid = false;
        } else {
            signUp_input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            signUp_input_passWord.setError("between 6 and 20 alphanumeric characters");
            valid = false;
        } else {
            signUp_input_passWord.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 20 || !(reEnterPassword.equals(password))) {
            signUp_input_reEnterPassword.setError("Password Do not match");
            valid = false;
        } else {
            signUp_input_reEnterPassword.setError(null);
        }

        return valid;
    }

    public void writeData(){
        DatabaseReference User_Ref = FirebaseDatabase.getInstance().getReference().child("app").child("users");

        String name = signUp_input_userName.getText().toString();
        String email = signUp_input_email.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("userName", name);
        map.put("userEmail", email);
        DatabaseReference Name_Ref = User_Ref.child(name);
        Name_Ref.setValue(map);
    }

    @Override
    public void onStart() { super.onStart(); }
}