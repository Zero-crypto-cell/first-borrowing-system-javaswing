package com.css.model;
import java.time.LocalDate;

public class BorrowRequest {
    public enum Status {
        PENDING, APPROVED, REJECTED, RETURNED
    }
    
    private final int id;
    private final User borrower;
    private final Item item;
    private final LocalDate requestDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private Status status = Status.PENDING;

    public BorrowRequest(int id, User borrower, Item item, LocalDate requestDate, LocalDate dueDate){
        this.id = id;
        this.borrower = borrower;
        this.item = item;
        this.requestDate = requestDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public int getId(){
        return id;
    }

    public User getBorrower(){
        return borrower;
    }

    public Item getItem(){
        return item;
    }

    public LocalDate getRequestDate(){
        return requestDate;
    }

    public LocalDate getReturnDate(){
        return returnDate;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    public Status getStatus(){
        return status;
    }

    public void approve(){
        if(status != Status.PENDING){
            throw new IllegalStateException("Cannot approve a request that is not pending.");
        }
        status = Status.APPROVED;
        item.setStatus(Item.Status.Borrowed);
    }

    public void reject(){
        if(status != Status.PENDING){
            throw new IllegalStateException("Cannot reject a request that is not pending.");
        }
        status = Status.REJECTED;
    }

    public void markReturned(LocalDate returnDate){
        if(status != Status.APPROVED){
            throw new IllegalStateException("Cannot return a request that is not approved.");
        }
        this.returnDate = returnDate;
        status = Status.RETURNED;
        item.setStatus(Item.Status.Available);
    }

    public boolean isPending(){
        return status == Status.PENDING;
    }
    
    public boolean isApproved(){
        return status == Status.APPROVED;
    }
}
