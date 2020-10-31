package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.models.EmployeeModel;
import com.example.myapplication.models.UserModel;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "MyDatabase";
    private static final String EMPLOYEE_TABLE = "EmployeeDatabase";

    private static final int DB_VERSION = 5;
//    public static final String USER_TABLE_NAME = "usertb_app";
    public static final String TABLE_USERNAME = "USERNAME";
    public static final String TABLE_EMAIL = "EMAIL";
    public static final String TABLE_PASSWORD = "PASSWORD";

    public static final String TABLE_ROLE = "ROLE";
    public static final String TABLE_PRIORITY = "PRIORITY";

    Context context;
    public DatabaseHandler(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        this.context= context;
    }

    // creating user database
    public void createDatabase(SQLiteDatabase db) {
        String sql ="CREATE TABLE IF NOT EXISTS "+DB_NAME+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TABLE_USERNAME+" VARCHAR NOT NULL,"+
                TABLE_EMAIL+" VARCHAR NOT NULL,"+
                TABLE_PASSWORD+" VARCHAR NOT NULL,"+
                TABLE_ROLE+" INTEGER NOT NULL,"+
                TABLE_PRIORITY+" INTEGER NOT NULL"+
                ")";
        db.execSQL(sql);
    }

    // Creating employee table
    public void createEmployeeDatabase(SQLiteDatabase db) {
        String sql ="CREATE TABLE IF NOT EXISTS "+EMPLOYEE_TABLE+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TABLE_USERNAME+" VARCHAR NOT NULL,"+
                TABLE_EMAIL+" VARCHAR NOT NULL,"+
                TABLE_PRIORITY+" INTEGER NOT NULL"+
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createDatabase(db);
        createEmployeeDatabase(db);
    }

    // Update Employee Data
    public void updateEmployeeData(EmployeeModel employeeModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERNAME",employeeModel.getUserName());
//        values.put(TABLE_USERNAME,userModel.getUserName());
        values.put("EMAIL",employeeModel.getEmail());
        values.put("PRIORITY",employeeModel.getPriority());
//        values.put(TABLE_EMAIL,userModel.getEmail());


        db.update(EMPLOYEE_TABLE,values,"id="+employeeModel.getId(),null);
    }


    // Update user data
    public void updateUserData(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("USERNAME",userName);
        values.put(TABLE_USERNAME,userModel.getUserName());
//        values.put("EMAIL",email);
        values.put(TABLE_EMAIL,userModel.getEmail());
//        values.put("PASSWORD",password);
        values.put(TABLE_PASSWORD,userModel.getPassword());

        values.put(TABLE_ROLE,userModel.getRole());
        values.put(TABLE_PRIORITY,userModel.getPriority());
        db.update(DB_NAME,values,"id="+userModel.getId(),null);
    }

    // Delete user data by Id
    public void deleteUserDataById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME,"id="+id,null);
//        db.delete(DB_NAME,"id="+userModel.getId(),null);

    }



    // Delete Employee data by Id
    public void deleteEmployeeDataById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EMPLOYEE_TABLE,"id="+id,null);
//        db.delete(DB_NAME,"id="+userModel.getId(),null);

    }

        // get user id by Email and Password
    public String getUserIdByEmailPassword(String email, String password){
        String query = "SELECT id FROM "+DB_NAME+" WHERE EMAIL='"+email+"' AND PASSWORD='"+password+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            return ""+cursor.getInt(cursor.getColumnIndex("id"));
        }else {
            return null;
        }
    }
    // getUser priority by id
    public int getUserProrityById(String id){
        String query="SELECT PRIORITY FROM "+DB_NAME+" WHERE id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            return cursor.getInt(cursor.getColumnIndex("PRIORITY"));
        }
        return 0;
    }
        // Signin logic details
    public UserModel signInUserByEmailAndPassword(UserModel userModel){
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "SELECT * FROM "+DB_NAME+" WHERE EMAIL='"+userModel.getEmail()+"' AND PASSWORD='"+userModel.getPassword()+"'";
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            UserModel userModelDetail=new UserModel();
            userModelDetail.setId(cursor.getInt(cursor.getColumnIndex("id")));
            userModelDetail.setUserName(cursor.getString(cursor.getColumnIndex("USERNAME")));
            userModelDetail.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            userModelDetail.setPassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));
            userModelDetail.setRole(cursor.getInt(cursor.getColumnIndex("ROLE")));
            userModelDetail.setPriority(cursor.getInt(cursor.getColumnIndex("PRIORITY")));
            return userModelDetail;
        }else{
            return null;
        }
    }

    // get all user Data using ArrayList or HashMap

    public ArrayList<UserModel> getAllUserData(){
        ArrayList<UserModel> allData = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String queryToGetAllData = "SELECT * FROM "+DB_NAME;
        Cursor cursor = db.rawQuery(queryToGetAllData,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){   // kia last row guzar nhi(not operator) gayi?han
            UserModel userModelDetail = new UserModel();
            userModelDetail.setUserName(cursor.getString(cursor.getColumnIndex("USERNAME")));
            userModelDetail.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            userModelDetail.setPassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));
            userModelDetail.setId(cursor.getInt(cursor.getColumnIndex("id")));
            userModelDetail.setRole(cursor.getInt(cursor.getColumnIndex("ROLE")));
            userModelDetail.setPriority(cursor.getInt(cursor.getColumnIndex("PRIORITY")));
            allData.add(userModelDetail);
            cursor.moveToNext();
        }
        return allData;
    }

    // get all employee data
    public ArrayList<EmployeeModel> getAllEmployeeData(){
        ArrayList<EmployeeModel> allData = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String queryToGetAllData = "SELECT * FROM "+EMPLOYEE_TABLE;
        Cursor cursor = db.rawQuery(queryToGetAllData,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){   // kia last row guzar nhi(not operator) gayi?han
            EmployeeModel employeeModelDetails = new EmployeeModel();
            employeeModelDetails.setUserName(cursor.getString(cursor.getColumnIndex("USERNAME")));
            employeeModelDetails.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            employeeModelDetails.setId(cursor.getInt(cursor.getColumnIndex("id")));
            employeeModelDetails.setPriority(cursor.getInt(cursor.getColumnIndex("PRIORITY")));
            allData.add(employeeModelDetails);
            cursor.moveToNext();
        }
        return allData;
    }


    // get user data by id
    public UserModel getUserDataById(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "SELECT * FROM "+DB_NAME +" WHERE id="+id;
//        String query = "SELECT * FROM "+DB_NAME +" WHERE id="+userModel.getId();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            UserModel userModelDetail=new UserModel();
            userModelDetail.setId(cursor.getInt(cursor.getColumnIndex("id")));
            userModelDetail.setUserName(cursor.getString(cursor.getColumnIndex("USERNAME")));
            userModelDetail.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            userModelDetail.setPassword(cursor.getString(cursor.getColumnIndex("PASSWORD")));
            userModelDetail.setRole(cursor.getInt(cursor.getColumnIndex("ROLE")));
            userModelDetail.setPriority(cursor.getInt(cursor.getColumnIndex("PRIORITY")));
            return userModelDetail;
        }else{
            return null;
        }

    }

    // get Employee Data by Id

    public EmployeeModel getEmployeeDataById(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "SELECT * FROM "+EMPLOYEE_TABLE +" WHERE id="+id;
//        String query = "SELECT * FROM "+DB_NAME +" WHERE id="+userModel.getId();
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            EmployeeModel employeeModelDetails=new EmployeeModel();
            employeeModelDetails.setId(cursor.getInt(cursor.getColumnIndex("id")));
            employeeModelDetails.setUserName(cursor.getString(cursor.getColumnIndex("USERNAME")));
            employeeModelDetails.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            employeeModelDetails.setPriority(cursor.getInt(cursor.getColumnIndex("PRIORITY")));

            return employeeModelDetails;
        }else{
            return null;
        }

    }

    // insert Employee data
    public void insertEmployeeData(EmployeeModel employeeModel){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("USERNAME", userName);
        values.put("USERNAME", employeeModel.getUserName());
//        values.put("EMAIL", email)
        values.put("EMAIL", employeeModel.getEmail());
//        values.put("PASSWORD",password);
        values.put("PRIORITY",employeeModel.getPriority());

        db.insert(EMPLOYEE_TABLE,null, values);
//        String userID = getUserIdByEmailPassword(email,password);

    }


    // Insert user data row wise
    public void insertData(UserModel userModel){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("USERNAME", userName);
        values.put(TABLE_USERNAME, userModel.getUserName());
//        values.put("EMAIL", email);
        values.put(TABLE_EMAIL, userModel.getEmail());
//        values.put("PASSWORD",password);
        values.put(TABLE_PASSWORD,userModel.getPassword());
        values.put(TABLE_ROLE,userModel.getRole());
        values.put(TABLE_PRIORITY,userModel.getPriority());
        db.insert(DB_NAME,null, values);
//        String userID = getUserIdByEmailPassword(email,password);
        String userID = getUserIdByEmailPassword(userModel.getEmail(),userModel.getPassword());
     //inline object with ternary operator
        new Config(context).setStringValue(Config.USER_LOGIN,userID==null?"":userID);
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXISTS "+DB_NAME;
        String query2 = "DROP TABLE IF EXISTS "+EMPLOYEE_TABLE;
        db.execSQL(query1);
        db.execSQL(query2);
        createDatabase(db);
        createEmployeeDatabase(db);


    }
}
