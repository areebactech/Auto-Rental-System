
# 🚗 Vehicle Rental System

A **Java-based desktop application** for managing vehicle rentals, built using **Java Swing** and **SQLite**. It provides a user-friendly interface for customers to book vehicles and for admins to manage bookings, vehicles, and view real-time statistics through a dashboard.

---

## 🧠 Overview

This system was developed as an academic project to demonstrate the use of GUI programming, database integration, and user interaction in Java. It includes:

- A clean and interactive UI
- Admin login system
- Booking management with validation
- Real-time dashboard cards
- SQLite support (no server needed!)

---

## 🎯 Key Features

| Module             | Description |
|--------------------|-------------|
| 👤 **Admin Login**      | Secure login to access admin features |
| 📝 **Vehicle Booking**  | GUI form to book vehicles with date, duration, and auto-price calculation |
| 📈 **Admin Dashboard**  | Displays total bookings, users, revenue, and more |
| 🚗 **Vehicle Selection**| List available vehicles for booking |
| 💾 **SQLite Storage**   | Lightweight DB that requires no setup |

---

## 🖼️ Screenshots

> Replace with actual screenshots in your repo’s `/images` folder.

### 📝 Booking Interface

![Booking UI](screenshots/userpage.png)

### 📊 Admin Dashboard

![Admin Dashboard](screenshots/admin.png)

### Vehicle Selection

![Vehicle Selection](screenshots/selectvehicle.png)

---

## 🛠️ Tech Stack

- 💻 **Java SE** – Core programming language
- 🎨 **Swing** – GUI framework
- 🗃️ **SQLite** – Database engine (offline & lightweight)
- 🧰 **JDBC** – For DB connection
- 🧪 **IntelliJ IDEA** – Development environment

---

## 📁 Project Structure

```plaintext
Vehicle-Rental-System/
│
├── src/
│   ├── BookingPage.java
│   ├── AdminDashboard.java
│   ├── DBConnection.java
│   └── ...
│
├── rs.db      # SQLite database file
├── README.md
└── images/
````

---

## ⚙️ Setup Instructions

### 1. Clone the Repo

```bash
git clone https://github.com/areebactech/Vehicle-Rental-System.git
cd Vehicle-Rental-System
```

### 2. Open in IntelliJ or any Java IDE

* Add the SQLite JDBC driver (e.g., `sqlite-jdbc-3.36.0.3.jar`) to your project classpath.

### 3. Run the App

Run `LoginPage.java` or `BookingPage.java` to get started.

---

## 🔐 Admin Login Credentials

```txt
Username: admin
Password: 1234
```
---

## 🔌 Database Info

* The system uses **SQLite** (no configuration needed).
* DB file is preconfigured: `rs.db`
* You can edit or view it using [DB Browser for SQLite](https://sqlitebrowser.org/)
  
## Tables Used:

users – Customer records

bookings – Booking entries

Vehicle - Vehicle info

## 📦 Sample Data

If you'd like to populate your DB quickly with test data:

* use the GUI to add bookings

---

## 📬 Feedback & Contact

Feel free to reach out or fork the project!

* GitHub: [@areebactech](https://github.com/areebactech)
* Email: *[areebaasifjaved@gmail.com](mailto:areebaasifjaved@gmail.com)* 

---
