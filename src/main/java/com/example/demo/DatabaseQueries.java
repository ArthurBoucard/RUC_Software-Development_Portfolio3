package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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

    public static ArrayList<String> getAllPortName() throws SQLException {
        if (conn == null)
            open();

        ArrayList<String> PortList = new ArrayList<String>();
        Statement stmt = null;
        stmt = conn.createStatement();
        String query = "select name from habour;";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String name = rs.getString("name");
            PortList.add(name);
        }
        stmt.close();
        return PortList;
    }

    public static ArrayList<Vessel> getAvailableVessel(String fromPort, String toPort, int cargo) throws SQLException {
        if (conn == null)
            open();

        ArrayList<Vessel> VesselList = new ArrayList<Vessel>();
        Statement stmt = null;
        stmt = conn.createStatement();
        String query = "select containers, transport, h1.name as fromport, h2.name as toport, vessel.name vesselname, vessel.capacity as vesselcapacity from flow f inner join transport on f.transport = transport.id inner join habour h1 on transport.fromhabour = h1.id inner join habour h2 on transport.tohabour = h2.id inner join vessel on transport.vessel = vessel.id where fromport is '" + fromPort + "' and toport is '" + toPort + "';";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String vesselName = rs.getString("vesselname");
            String fromPortQuery = rs.getString("fromport");
            String toPortQuery = rs.getString("toport");
            int vesselCapacity = Integer.parseInt(rs.getString("vesselcapacity"));
            int container = Integer.parseInt(rs.getString("containers"));
            if (vesselCapacity - container > 0) {
                Vessel vessel = new Vessel(vesselName, container, vesselCapacity);
                VesselList.add(vessel);
            }
        }
        stmt.close();
        return VesselList;
    }

    public static boolean addContainerToVessel(String vesselName, int newCargo) throws SQLException {
        if (conn == null)
            open();

        Statement stmt = null;
        stmt = conn.createStatement();
        String query = "update flow set containers = containers + " + newCargo + " where exists (select containers cont, transport, vessel.name vesselname, vessel.capacity vesselcapacity from flow f inner join transport on f.transport = transport.id inner join vessel on transport.vessel = vessel.id where vessel.name = '" + vesselName + "' and flow.containers = cont);";
        stmt.execute(query);
        stmt.close();
        return true;
    }
}
