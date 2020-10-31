package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.UserModel;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {
    EditText inp_email, inp_password;
    Button btnLogIn;
    TextView gotoSignUp;
    DatabaseHandler dataBaseHandler;
    Config config;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        dataBaseHandler = new DatabaseHandler(this);
        config = new Config(this);
        inp_email = (EditText) findViewById(R.id.userEmail);
        inp_password = (EditText) findViewById(R.id.userPassword);
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        gotoSignUp = (TextView) findViewById(R.id.gotoSignup);
        userModel = new UserModel();
//        ArrayList<UserModel> userModelsDetails = dataBaseHandler.getAllUserData();
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = dataBaseHandler.getUserIdByEmailPassword(inp_email.getText().toString().trim(), inp_password.getText().toString());
                int userPriority = dataBaseHandler.getUserProrityById(id);
                if (id==null) {
                    Toast.makeText(SignInActivity.this, "Invalid Email or Password or deleted by Admin", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(SignInActivity.this, "Suspended Email or Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(userPriority==2){
                        Toast.makeText(SignInActivity.this,dataBaseHandler.getUserDataById(Integer.parseInt(id)).getUserName()+" is restricted by Admin",Toast.LENGTH_SHORT).show();
                    }else if(userPriority!=2){
                    config.setStringValue(Config.USER_LOGIN, id);
                    Toast.makeText(SignInActivity.this,dataBaseHandler.getUserDataById(Integer.parseInt(id)).getUserName()+" is login.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                    }
                    }
            }
        });
        gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(myIntent);
                finish();
            }
        });


    }
}