package com.ecommerce.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String productName, int available, int requested) {
        super(String.format("Insufficient stock for '%s': available %d, requested %d",
                productName, available, requested));
    }
}
