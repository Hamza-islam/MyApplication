package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.EmployeeModel;

import java.util.ArrayList;

public class UpdateEmployee extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    Config config;

    EditText inp_userName, inp_email;
    Button btnUpdateEmployee;
    EmployeeModel employeeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);
        databaseHandler = new DatabaseHandler(this);
        config = new Config(this);
        inp_userName = (EditText) findViewById(R.id.userName);
        inp_email = (EditText) findViewById(R.id.userEmail);
        btnUpdateEmployee = (Button) findViewById(R.id.updateEmployee);
        employeeModel = new EmployeeModel();
        employeeModel.setId(getIntent().getIntExtra("emp_id", 0));
        employeeModel.setUserName(getIntent().getStringExtra("emp_name"));
        employeeModel.setEmail(getIntent().getStringExtra("emp_email"));
        employeeModel.setPriority(getIntent().getIntExtra("emp_priority",1));
        inp_userName.setText(employeeModel.getUserName());
        inp_email.setText(employeeModel.getEmail());
        btnUpdateEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inp_userName.getText().toString().trim().equals("")) {
                    Toast.makeText(UpdateEmployee.this, "Please Enter user name", Toast.LENGTH_SHORT).show();
                } else if (inp_email.getText().toString().trim().equals("")) {
                    Toast.makeText(UpdateEmployee.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else {
                    employeeModel.setUserName(inp_userName.getText().toString().trim());
                    employeeModel.setEmail(inp_email.getText().toString().trim());
                    employeeModel.setPriority(employeeModel.getPriority());
                    databaseHandler.updateEmployeeData(employeeModel);
                    finish();
                }

            }
        });

    }

}