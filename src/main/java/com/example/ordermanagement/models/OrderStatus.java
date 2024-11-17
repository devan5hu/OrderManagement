package com.example.ordermanagement.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orderStatus")  // MongoDB Document annotation
public class OrderStatus {
//
//    @Id
//    private String id; // MongoDB uses String as the id by default
//
//    private Long orderId;
//    private List<StatusUpdate> statusUpdates;
//
//    // Constructors
//    public OrderStatus() {}
//
//    public OrderStatus(Long orderId, List<StatusUpdate> statusUpdates) {
//        this.orderId = orderId;
//        this.statusUpdates = statusUpdates;
//    }
//
//    // Inner class for Status Updates
//    public static class StatusUpdate {
//        private String status;
//        private Date timestamp;
//
//        public StatusUpdate() {}
//
//        public StatusUpdate(String status, Date timestamp) {
//            this.status = status;
//            this.timestamp = timestamp;
//        }
//
//        public String getStatus() { return status; }
//        public void setStatus(String status) { this.status = status; }
//
//        public Date getTimestamp() { return timestamp; }
//        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
//    }
//
//    // Getters and Setters
//    public String getId() { return id; }
//    public void setId(String id) { this.id = id; }
//
//    public Long getOrderId() { return orderId; }
//    public void setOrderId(Long orderId) { this.orderId = orderId; }
//
//    public List<StatusUpdate> getStatusUpdates() { return statusUpdates; }
//    public void setStatusUpdates(List<StatusUpdate> statusUpdates) { this.statusUpdates = statusUpdates; }
}
