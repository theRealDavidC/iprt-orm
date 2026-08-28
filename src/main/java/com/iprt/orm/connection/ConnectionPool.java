package com.iprt.orm.connection;

import java.sql.*;
import java.util.concurrent.*;

public class ConnectionPool {
    private BlockingQueue<Connection> available;
    private String url;
    private String username;
    private String password;

    public ConnectionPool(String url, String username, String password, int poolSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        available = new LinkedBlockingQueue<>();
        try {
            for (int i = 0; i < poolSize; i++) {
                available.add(DriverManager.getConnection(url, username, password));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public Connection borrow() {
        try {
            return available.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to borrow connection", e);
        }
    }

    public void release(Connection conn) {
        available.offer(conn);
    }

    public void close() {
        try {
            for (Connection conn : available) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close connection pool", e);
        }
    }
}
