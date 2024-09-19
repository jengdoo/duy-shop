package com.example.shopapp.Model;

public class OrderStatus {
   public static final String PENDING = "PENDING";
   public static final String PROCESSING = "PROCESSING";
   public static final String SHIPPED = "SHIPPED";
   public static final String DELIVERED = "DELIVERED";
   public static final String CANCELLED = "CANCELLED";
   public static boolean isValidStatus(String status) {
      return status.equals(PENDING) || status.equals(PROCESSING) ||
              status.equals(SHIPPED) || status.equals(DELIVERED) ||
              status.equals(CANCELLED);
   }
}
