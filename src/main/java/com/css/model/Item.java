package com.css.model;

public class Item {
    public enum Status {
        Available, Borrowed
    }

    private final int id;
    private String name;
    private String category;
    private Status status = Status.Available;

    public Item(int id, String name, String category){
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getCategory(){
        return category;
    }

    public Status getStatus(){
        return status;
    }

    public boolean isAvailable(){
        return status == Status.Available;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}
