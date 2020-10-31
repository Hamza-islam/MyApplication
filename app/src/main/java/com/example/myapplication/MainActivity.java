package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.UserModel;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView txtWelcome, txtUserDetail;
    Button btnSignOut, showEmp, showUsers;
    DatabaseHandler databaseHandler;
    Config config;
    UserModel userModelDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler = new DatabaseHandler(this);
        config = new Config(this);
        txtWelcome = (TextView)findViewById(R.id.txtWelcomeMsg);
        txtUserDetail = (TextView)findViewById(R.id.txtUserDetail);
        showUsers = (Button)findViewById(R.id.showUsers);
        btnSignOut = (Button) findViewById(R.id.btnsignOut);
        String userId = (config.getStringValue(Config.USER_LOGIN));
        UserModel userModelDetails = databaseHandler.getUserDataById(Integer.parseInt(userId));
        if(userModelDetails==null)
        {
            config.setStringValue(Config.USER_LOGIN,"");
            startActivity(new Intent(this,SignInActivity.class));
            finish();
            return;
        }
        txtWelcome.setText("Welcome " +userModelDetails.getUserName());
        showEmp = findViewById(R.id.showEmployee);
        txtUserDetail.setText(userModelDetails.getEmail().toString());
        if(userModelDetails.getRole()==Config.ROLE_ADMIN){
            showUsers.setVisibility(View.VISIBLE);
        }else {
            showUsers.setVisibility(View.GONE);
        }

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setStringValue(Config.USER_LOGIN,"");
                Intent myIntent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        showEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(getIntent().hasExtra("user")){
//                    Toast.makeText(MainActivity.this, "USER CANNOT ADD", Toast.LENGTH_SHORT).show();
//                }
//                else{
                startActivity(new Intent(MainActivity.this,EmployeeDetail.class).putExtra("Role",userModelDetails.getRole()));
                finish();

//                }
            }
        });

        showUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,UsersDetail.class).putExtra("Role",userModelDetails.getRole()));
                finish();
            }
        });
    }
}