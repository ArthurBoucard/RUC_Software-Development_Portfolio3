package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

class databaseCommands{
    protected static String url = "jdbc:sqlite:identifier.sqlite";
    protected static Connection conn = null;

    public static void open() {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

    public static void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void getAllHabourName() throws SQLException {
        if (conn == null)
            open();

        Statement stmt = null;
        stmt = conn.createStatement();
        String query = "select name from habour;";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Habours :");
        while (rs.next()) {
            String name = rs.getString("name");
            System.out.println(name);
        }
        stmt.close();
    }

    public static void getAvailableVessel() throws SQLException {
        if (conn == null)
            open();

        Statement stmt = null;
        stmt = conn.createStatement();
        String query = "select transport from flow;"; //need to get all info from transport
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Available vessels :");
        while (rs.next()) {
            String name = rs.getString("name");
            String capacity = rs.getString("capacity");
            System.out.println(name + " " + capacity);
        }
        stmt.close();
    }
}
