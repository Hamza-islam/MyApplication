package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.EmployeeModel;

import java.util.ArrayList;

public class EmployeeDetail extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    Config config;
    RecyclerView recyclerView;
    EditText inp_userName, inp_email;
    Button btnAddEmployee;
    EmployeeListAdapter employeeListAdapter;
    LinearLayout parentContainer;
    int userRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);
        databaseHandler = new DatabaseHandler(this);
        config = new Config(this);
        userRole = getIntent().getIntExtra("Role",Config.ROLE_ADMIN);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        inp_userName = (EditText) findViewById(R.id.userName);
        inp_email = (EditText) findViewById(R.id.userEmail);
        btnAddEmployee = (Button) findViewById(R.id.addEmployee);
        parentContainer = findViewById(R.id.parentContainer);
        employeeListAdapter = new EmployeeListAdapter(databaseHandler.getAllEmployeeData(), this);
        recyclerView.setAdapter(employeeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        if(userRole==Config.ROLE_USER) {
            parentContainer.setVisibility(View.GONE);
        }else {
            parentContainer.setVisibility(View.VISIBLE);
        }
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeModel employeeModel = new EmployeeModel();
                if (inp_userName.getText().toString().trim().equals("")) {
                    Toast.makeText(EmployeeDetail.this, "Please Enter user name", Toast.LENGTH_SHORT).show();
                } else if (inp_email.getText().toString().trim().equals("")) {
                    Toast.makeText(EmployeeDetail.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else {
                    employeeModel.setUserName(inp_userName.getText().toString().trim());
                    employeeModel.setEmail(inp_email.getText().toString().trim());
                    employeeModel.setPriority(1); // by default 1 which means its unlock
                    databaseHandler.insertEmployeeData(employeeModel);
                    employeeListAdapter = new EmployeeListAdapter(databaseHandler.getAllEmployeeData(), EmployeeDetail.this);
                    recyclerView.setAdapter(employeeListAdapter);
                    employeeListAdapter.notifyDataSetChanged();
                }

            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        employeeListAdapter = new EmployeeListAdapter(databaseHandler.getAllEmployeeData(), EmployeeDetail.this);
        recyclerView.setAdapter(employeeListAdapter);
        employeeListAdapter.notifyDataSetChanged();
    }

    class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {
        ArrayList<EmployeeModel> employeeModelArrayList;
        Context context;
        public EmployeeListAdapter(ArrayList<EmployeeModel> employeeModelArrayList, Context context) {
            this.employeeModelArrayList = employeeModelArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.recycleviewitem, parent, false);
            return new EmployeeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
            EmployeeModel employeeModel = employeeModelArrayList.get(position);
            holder.name_txt.setText(employeeModel.getUserName());
            holder.email_txt.setText(employeeModel.getEmail());
            if(userRole==Config.ROLE_ADMIN){
                holder.edtDetatils.setVisibility(View.VISIBLE);
                holder.delDetails.setVisibility(View.VISIBLE);
            }else if(userRole==Config.ROLE_MANAGER && employeeModel.getPriority()==1){
                holder.delDetails.setVisibility(View.VISIBLE);

            }

            if(employeeModel.getPriority()==2) {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.yellow));
            }else if(employeeModel.getPriority()==1) {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));

            }

            if(userRole==Config.ROLE_ADMIN){
                holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
//                    int empPriority = employeeModel.getPriority();
                    @Override
                    public boolean onLongClick(View v) {
                        if(employeeModel.getPriority()==1) {
                            employeeModel.setPriority(2); // locked employee
                            databaseHandler.updateEmployeeData(employeeModel);
                            notifyDataSetChanged();
                            Toast.makeText(EmployeeDetail.this, "Locked now", Toast.LENGTH_SHORT).show();
//                        holder.cardView.setCardBackgroundColor();
                        }else if(employeeModel.getPriority()==2) {
                            employeeModel.setPriority(1);  // unlocked employee
                            databaseHandler.updateEmployeeData(employeeModel);
                            notifyDataSetChanged();
                            Toast.makeText(EmployeeDetail.this,"UnLocked now",Toast.LENGTH_SHORT).show();
                        }
                        return false;

                    }
                });
            }

            holder.edtDetatils.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EmployeeDetail.this, UpdateEmployee.class)
                            .putExtra("emp_id", employeeModel.getId())
                            .putExtra("emp_name", employeeModel.getUserName())
                            .putExtra("emp_email", employeeModel.getEmail())
                            .putExtra("emp_priority",employeeModel.getPriority()));

                }
            });

            holder.delDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setCancelable(false)
                            .setTitle("Delete Employee " + employeeModel.getUserName())
                            .setMessage("Do you want to delete this Employee?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseHandler.deleteEmployeeDataById(employeeModel.getId());
                                    employeeModelArrayList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return employeeModelArrayList.size();
        }

        class EmployeeViewHolder extends RecyclerView.ViewHolder {

            TextView name_txt, email_txt;
            ImageView edtDetatils, delDetails;
            CardView cardView;

            public EmployeeViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.recyclerParent);
                name_txt = itemView.findViewById(R.id.empName);
                email_txt = itemView.findViewById(R.id.empEmail);
                edtDetatils = itemView.findViewById(R.id.edtBtn);
                delDetails = itemView.findViewById(R.id.delBtn);


            }
        }
    }
}