package com.css.model;

public class User {
   private final int id;
   private final String username;
   private final String password;
   private final String fullname;
   private final Role role;

   public User(int id, String username, String password, String fullname, Role role){
    this.id = id;
    this.username = username;
    this.password = password;
    this.fullname = fullname;
    this.role = role;
   }

   public int getId(){
    return id;
   }

   public String getUsername(){
    return username;
   }

    public String getPassword(){
     return password;
    }

    public String getFullname(){
    return fullname;
    }

   public String getFullName(){
    return fullname;
   }

    public boolean isAdmin(){
        return role == Role.Admin;
    }
    
    @Override
    public String toString(){
        return fullname + " ( " + role + ")";
    }
}
