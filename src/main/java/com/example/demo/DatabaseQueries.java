package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

class DatabaseQueries {
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

    public static void getAllPortName() throws SQLException {
        if (conn == null)
            open();

        Statement stmt = null;
        stmt = conn.createStatement();
        String query = "select name from habour;";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Ports :");
        while (rs.next()) {
            String name = rs.getString("name");
            System.out.println(name);
        }
        stmt.close();
    }

    public static void getAvailableVessel(String fromPort, String toPort, int cargo) throws SQLException { // need to choose if make if statetment in SQL query or in java code
        if (conn == null)
            open();

        Statement stmt = null;
        stmt = conn.createStatement();
        String query = "select containers, transport, h1.name as fromport, h2.name as toport, vessel.name vesselname, vessel.capacity as vesselcapacity from flow f inner join transport on f.transport = transport.id inner join habour h1 on transport.fromhabour = h1.id inner join habour h2 on transport.tohabour = h2.id inner join vessel on transport.vessel = vessel.id;";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Available vessels :");
        while (rs.next()) {
            String vesselName = rs.getString("vesselname");
            String fromPortQuery = rs.getString("fromport");
            String toPortQuery = rs.getString("toport");
            int vesselCapacity = Integer.parseInt(rs.getString("vesselcapacity"));
            int container = Integer.parseInt(rs.getString("containers"));
            if (vesselCapacity - container > 0) // need to calculate with cargo
                System.out.println(vesselName + " " + vesselCapacity + " " + container);
        }
        stmt.close();
    }
}
