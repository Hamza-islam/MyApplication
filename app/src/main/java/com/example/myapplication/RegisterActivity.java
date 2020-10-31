package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.UserModel;

public class RegisterActivity extends AppCompatActivity {
    EditText inp_userName, inp_email, inp_password;
    Button btnRegister;
    TextView gotoSignIn;
    DatabaseHandler dataBaseHandler;
    Config config;
    UserModel userModelDetails;
    RadioGroup radioGroup;
    RadioButton admin, manager, user;

    int rId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inp_userName = (EditText) findViewById(R.id.userName);
        inp_email = (EditText) findViewById(R.id.userEmail);
        inp_password = (EditText) findViewById(R.id.userPassword);
        gotoSignIn = (TextView) findViewById(R.id.gotoSignIn);
        btnRegister = (Button) findViewById(R.id.btnSignUp);
        radioGroup = (RadioGroup) findViewById(R.id.radioGrp);
        admin = findViewById(R.id.admin);
        manager = findViewById(R.id.manager);
        user = findViewById(R.id.user);

        dataBaseHandler = new DatabaseHandler(this);
        userModelDetails = new UserModel();
        gotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inp_userName.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter user name", Toast.LENGTH_SHORT).show();
                } else if (inp_email.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (inp_password.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    int role= Config.ROLE_ADMIN;
                    int selectedId=((RadioGroup)findViewById(R.id.radioGrp)).getCheckedRadioButtonId();
                    if(selectedId==R.id.admin){

                        role= Config.ROLE_ADMIN;
                    }else if (selectedId==R.id.manager){
                        role= Config.ROLE_MANAGER;

                    }else {
                        role= Config.ROLE_USER;
                    }
                    userModelDetails.setUserName(inp_userName.getText().toString().trim());
                    userModelDetails.setEmail(inp_email.getText().toString().trim());
                    userModelDetails.setPassword(inp_password.getText().toString().trim());
                    userModelDetails.setRole(role);
                    userModelDetails.setPriority(1);
                    dataBaseHandler.insertData(userModelDetails);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                }
            }
        }
        );

    }
}