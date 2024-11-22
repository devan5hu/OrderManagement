# Order Management System (OMS)

**Version:** 1.0.0  
**Hosted API Documentation:** [SwaggerHub - OrderManagementSystem](https://app.swaggerhub.com/apis-docs/DEVANSHUCHAUDHARI4/OrderManagementSystem/1.0.0#/Customer%20Actions/login)

---

## Problem Statement

The objective is to design a scalable and highly available backend system for an **Order Management System (OMS)**. This system enables users and external systems to interact with the OMS through RESTful APIs for managing orders at scale. The OMS must support asynchronous order processing, real-time status updates, and efficient resource management.

---

## Core Functionalities

### 1. Order Creation API
- **Description**: Create new orders with details like `order_id`, `customer_id`, `items`, `quantity`, and `timestamp`.
- **Response**: Confirmation with `order_id` and status `"Order Placed"`.
- **Key Features**:
  - Basic data validation (valid `customer_id`, non-empty items).
  - Error handling for invalid input.

### 2. Order Status Retrieval API
- **Description**: Retrieve the status of an order using `order_id`.
- **Response**: Real-time status updates such as `"Order Placed"`, `"Shipped"`, or `"Delivered"`.
- **Key Features**:
  - Basic authentication required.
  - Handles non-existent `order_id` gracefully.

### 3. Basic Order Management API
- **Description**: Perform essential operations:
  - **Update Order**: Modify aspects like item quantities or customer details before shipping.
  - **Cancel Order**: Cancel orders that haven’t been shipped or processed.
- **Response**: Success or failure messages depending on the order status.

---

## Technologies Used
- **Backend**: Java (Spring Boot)
- **Database**: MySQL
- **Authentication**: JWT

---

## How to Run

### JDK Version Required: 

Ensure you have Java(TM) SE Runtime Environment version 23.0.1 or higher installed on your system.
You can verify your Java version using the following command:

```
java -version
```

### Expected Output:
```
java version "23.0.1" 2024-10-15
Java(TM) SE Runtime Environment (build 23.0.1+11-39)
Java HotSpot(TM) 64-Bit Server VM (build 23.0.1+11-39, mixed mode, sharing)
```

### Clone the repository:
```bash
git clone https://github.com/devan5hu/OrderManagement
cd OrderManagementSystem
```

Run the Docker Desktop simultaneously for MySQL integrations and have the Apache Tomcat Server Installed.
Open it on the IDE of your choice and run it or execute the following command after navigating to the root folder.

```bash
  .\gradlew bootRun
```
