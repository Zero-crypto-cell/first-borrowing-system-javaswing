package com.css.model.service;

import com.css.model.BorrowRequest;
import com.css.model.Item;
import com.css.model.Role;
import com.css.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowingSystem {
    private final List<User> users = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final List<BorrowRequest> requests = new ArrayList<>();
    private int nextItemId = 1;
    private int nextRequestId = 1;

    public BorrowingSystem() {
        users.add(new User(1, "admin", "admin123", "CCS Lab Admin", Role.Admin));
        users.add(new User(2, "student1", "pass123", "Juan Dela Cruz", Role.Borrower));
        addItem("HDMI Cable (2m)", "Cables");
        addItem("Arduino Uno R3 Kit", "Microcontrollers");
        addItem("Logitech Presenter", "Peripherals");
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public Item findItem(int id) {
        return items.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item #" + id + " not found."));
    }

    public List<Item> searchAvailable(String keyword) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase();
        return items.stream()
                .filter(Item::isAvailable)
                .filter(i -> q.isEmpty()
                        || i.getName().toLowerCase().contains(q)
                        || i.getCategory().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public void addItem(String name, String category) {
        if (name == null || name.isBlank() || category == null || category.isBlank()) {
            throw new IllegalArgumentException("Name and category are required.");
        }
        items.add(new Item(nextItemId++, name.trim(), category.trim()));
    }

    public void updateItem(int id, String newName, String newCategory) {
        Item i = findItem(id);
        if (newName != null && !newName.isBlank()) {
            i.setName(newName.trim());
        }
        if (newCategory != null && !newCategory.isBlank()) {
            i.setCategory(newCategory.trim());
        }
    }

    public void removeItem(int id) {
        Item i = findItem(id);
        if (!i.isAvailable()) {
            throw new IllegalStateException("Cannot remove an item that is currently borrowed.");
        }
        items.remove(i);
    }

    public BorrowRequest requestBorrow(User borrower, int itemId, LocalDate dueDate) {
        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Return date cannot be in the past.");
        }
        Item i = findItem(itemId);
        if (!i.isAvailable()) {
            throw new IllegalStateException("Item is not available.");
        }
        BorrowRequest req = new BorrowRequest(nextRequestId++, borrower, i, LocalDate.now(), dueDate);
        requests.add(req);
        return req;
    }

    public List<BorrowRequest> getPendingRequests() {
        return requests.stream()
                .filter(r -> r.getStatus() == BorrowRequest.Status.PENDING)
                .collect(Collectors.toList());
    }

    public List<BorrowRequest> getBorrowedRecords() {
        return requests.stream()
                .filter(r -> r.getStatus() == BorrowRequest.Status.APPROVED)
                .collect(Collectors.toList());
    }

    public List<BorrowRequest> getHistory(User user) {
        if (user.isAdmin()) {
            return new ArrayList<>(requests);
        }
        return requests.stream()
                .filter(r -> r.getBorrower().getId() == user.getId())
                .collect(Collectors.toList());
    }

    private BorrowRequest findRequest(int id) {
        return requests.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Request #" + id + " not found."));
    }

    public void approve(int requestId) {
        findRequest(requestId).approve();
    }

    public void reject(int requestId) {
        findRequest(requestId).reject();
    }

    public void recordReturn(int requestId) {
        findRequest(requestId).markReturned(LocalDate.now());
    }
}
