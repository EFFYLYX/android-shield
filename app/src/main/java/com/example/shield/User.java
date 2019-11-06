package com.example.shield;

import java.util.ArrayList;

/**
 * Created by effy on 2018/10/29.
 */

class User {
    // User 对象 的角色 既是受害者也是监督人

    String phoneNo;
    String firstName;
    String secondName;
    String secureCode;

    UserLocation userLocation;

//    ArrayList<User> contactList; //监督人列表
//    ArrayList<User> superviseeList;//User要监督的用户
public User(){

    }

    public User(String phoneNo,String firstName,String secondName){

            this.phoneNo=phoneNo;
            this.firstName=firstName;
            this.secondName = secondName;

    }


    public User(String phoneNo, String firstName,String secondName, String secureCode){
        this.phoneNo = phoneNo;
        this.firstName = firstName;

        this.secondName = secondName;

        this.secureCode = secureCode;

    }

    public String getFirstSymbol(){
        return String.valueOf(firstName.charAt(0));
    }

    public String getUsername(){
        return firstName + " " + secondName;
    }

    private ArrayList<String> contactList;
    private ArrayList<String> superviseeList;


    public void setPhoneNo(String phoneNo){
        this.phoneNo=phoneNo;
    }


    public void setFirstName(String firstName){
        this.firstName=firstName;
    }


    public void setSecondName(String secondName){
        this.secondName=secondName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getPhoneNo(){
        return phoneNo;
    }

    public String getSecondName(){
        return secondName;
    }

    public String getSecureCode(){
        return secureCode;
    }

    public ArrayList<String> getContactList(){
        return contactList;
    }

    public ArrayList<String> getSuperviseeList(){
        return superviseeList;
    }




    public void setSecureCode(String secureCode){
        this.secureCode = secureCode;
    }

    public void setContactList(ArrayList<String> contactList){
        this.contactList = contactList;
    }

    public void setSuperviseeList(ArrayList<String> superviseeList){
        this.superviseeList = superviseeList;
    }

    public void getSupervisorPhoneList(){
        ArrayList<String> supervisorPhoneList = new ArrayList<>();
        for (String s : superviseeList){
            String[] strings = s.split(":");
            supervisorPhoneList.add(strings[1]);
        }
    }

//    public void getSupervisorNameList(){
//        ArrayList<String> supervisorPhoneList = new ArrayList<>();
//        for (String s : superviseeList){
//            String[] strings = s.split(":");
//            supervisorPhoneList.add(strings[1]);
//        }
//    }


//    public void getSupervisor

}
