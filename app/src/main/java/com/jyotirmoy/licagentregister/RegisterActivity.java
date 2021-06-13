package com.jyotirmoy.licagentregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextView goLogin, register;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText name, email, password;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        pd = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        goLogin = findViewById(R.id.goLogin);
        email = findViewById(R.id.emailRegister);
        name = findViewById(R.id.nameRegister);
        password = findViewById(R.id.passRegister);
        register = findViewById(R.id.register_Btn);

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Please Wait..");
                pd.setCancelable(false);
                pd.show();
                String user_name = name.getText().toString().trim();
                String email_id = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (TextUtils.isEmpty(user_name)) {
                    pd.dismiss();
                    name.setError("please Enter Name");
                    Toast.makeText(RegisterActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email_id)) {
                    pd.dismiss();
                    email.setError("please Enter Email");
                    Toast.makeText(RegisterActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!email_id.matches(emailPattern)) {
                    pd.dismiss();
                    email.setError("please enter valid Email");
                    Toast.makeText(RegisterActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    pd.dismiss();
                    password.setError("Please Enter Password");
                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    pd.dismiss();
                    password.setError("Password should be minimum 6 characters");
                    Toast.makeText(RegisterActivity.this, "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(email_id, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = database.getReference().child("user").child(mAuth.getUid());
                                Users users = new Users(mAuth.getUid(), user_name, email_id);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            sendEmailVerification();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Error in create account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                name.setText("");
                                email.setText("");
                                password.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            //show proper error message
                            Toast.makeText(RegisterActivity.this, "Failed!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }

    private void sendEmailVerification() {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please Check Email for Verification", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();


                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
        System.exit(0);
    }
}