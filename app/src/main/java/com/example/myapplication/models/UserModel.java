package com.example.myapplication.models;

public class UserModel {

    private String userName;
    private String email;
    private String password;
    private int id;
    private int role;   // 1= Admin, 2=Manager, 3=User
    private int priority;  // by defaul 1


    public void setUserName(String userName){
        this.userName = userName;
    }
    public void  setEmail(String email){
        this.email=email;
    }
    public void setPassword(String password){
        this.password= password;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setRole(int role){
        this.role=role;
    }
    public void setPriority(int priority){
        this.priority=priority;
    }


    public String getUserName(){
        return userName;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public int getId(){
        return id;
    }
    public int getRole(){
        return role;
    }
    public int getPriority(){
        return priority;
    }

}
