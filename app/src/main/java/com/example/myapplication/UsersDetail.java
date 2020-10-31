package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.models.EmployeeModel;
import com.example.myapplication.models.UserModel;

import java.util.ArrayList;

public class UsersDetail extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    Config config;
    RecyclerView recyclerView;
//    int userPriority;
    UserDetailsAdapter userDetailsAdapter;
    UserModel userModel;
    int userRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_detail);
        databaseHandler = new DatabaseHandler(this);
        config = new Config(this);
        userModel = new UserModel();
        userRole = getIntent().getIntExtra("Role",Config.ROLE_ADMIN);
//        userPriority = 1;

        userRole = getIntent().getIntExtra("Role",1);
        recyclerView = findViewById(R.id.recyclerViewUsers);
//        userRole = 1;
        userDetailsAdapter = new UserDetailsAdapter(databaseHandler.getAllUserData(), this);
        recyclerView.setAdapter(userDetailsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UserDetailViewHolder> {
        ArrayList<UserModel> userModelArrayList;
        Context context;

        public UserDetailsAdapter(ArrayList<UserModel> userModelArrayList, Context context) {
            this.userModelArrayList = userModelArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public UserDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.recyclerviewitemusers, parent, false);
            return new UserDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserDetailViewHolder holder, int position) {
            UserModel userModel = userModelArrayList.get(position);
            holder.userName.setText(userModel.getUserName());
            holder.userEmail.setText(userModel.getEmail());

            if (userModel.getRole() == Config.ROLE_ADMIN) {
                holder.userRole.setText("Admin");
//                userModel.setRole(1);
                holder.delDetails.setVisibility(View.GONE);
            } else if (userModel.getRole() == Config.ROLE_MANAGER) {
//                userModel.setRole(2);
                holder.delDetails.setVisibility(View.VISIBLE);
                holder.userRole.setText("Manager");
            } else if (userModel.getRole() == Config.ROLE_USER) {
                holder.delDetails.setVisibility(View.VISIBLE);
                holder.userRole.setText("User");
//                userModel.setRole(3);
            }

            holder.delDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setCancelable(false)
                            .setTitle("Delete User " + userModel.getUserName())
                            .setMessage("Do you want to delete this User?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseHandler.deleteUserDataById(userModel.getId());
                                    userModelArrayList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }).show();

                }
            });

            if(userModel.getPriority()==2) {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.yellow));
            }else if(userModel.getPriority()==1){
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            }


            if(userModel.getRole()!=Config.ROLE_ADMIN){
                holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v) {
                     if(userModel.getPriority()==1) {
                         userModel.setPriority(2); // restrict now (only manager and user)
                         databaseHandler.updateUserData(userModel);
                         Toast.makeText(UsersDetail.this,  "User is restrict now", Toast.LENGTH_SHORT).show();
                         notifyDataSetChanged();
                     }else if(userModel.getPriority()==2){
                         userModel.setPriority(1); // unrestrict now (only manager and user)
                         databaseHandler.updateUserData(userModel);
                         Toast.makeText(UsersDetail.this, "User is Unrestrict now", Toast.LENGTH_SHORT).show();
                         notifyDataSetChanged();
                     }
                     return false;
                 }
             });

            }
             else {
                    holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(UsersDetail.this, "You cannot restrict Admin", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                }
            }


        @Override
        public int getItemCount() {
            return userModelArrayList.size();
        }

        public class UserDetailViewHolder extends RecyclerView.ViewHolder {
            TextView userName, userEmail, userRole;
            ImageView delDetails;
            CardView cardView;

            public UserDetailViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.recyclerUserParent);
                delDetails = itemView.findViewById(R.id.delBtn);
                userName = itemView.findViewById(R.id.usrName);
                userEmail = itemView.findViewById(R.id.usrEmail);
                userRole = itemView.findViewById(R.id.usrRole);

            }


        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        userDetailsAdapter = new UserDetailsAdapter(databaseHandler.getAllUserData(), UsersDetail.this);
        recyclerView.setAdapter(userDetailsAdapter);
        userDetailsAdapter.notifyDataSetChanged();
    }
}