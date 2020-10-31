package com.example.myapplication.models;

public class EmployeeModel {
    private String userName;
    private String email;

    private int id;
    private int priority;

    public void setUserName(String userName){
        this.userName = userName;
    }
    public void  setEmail(String email){
        this.email=email;
    }
    public void setId(int id){
        this.id=id;
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
    public int getId(){
        return id;
    }
    public int getPriority(){
        return priority;
    }
}
