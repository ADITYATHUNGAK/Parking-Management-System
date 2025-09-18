package org;

import java.sql.*;
import java.util.*;

public class ParkingDAO {
	public static boolean authenticate(String username, String password) throws Exception { 
		Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?"); ps.setString(1, username); ps.setString(2, password); ResultSet rs = ps.executeQuery(); return rs.next(); }

public static boolean registerUser(String username, String password) throws Exception {
   Connection con = DBConnection.getConnection();
   PreparedStatement check = con.prepareStatement("SELECT * FROM users WHERE username=?");
   check.setString(1, username);
   ResultSet rs = check.executeQuery();
   if (rs.next()) return false;
   PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
   ps.setString(1, username);
   ps.setString(2, password);
   ps.executeUpdate();
   return true;
}

public static List<VehicleModel> getAllParkedVehicles() throws Exception {
   Connection con = DBConnection.getConnection();
   Statement st = con.createStatement();
   ResultSet rs = st.executeQuery("SELECT * FROM vehicles WHERE exit_time IS NULL");
   List<VehicleModel> list = new ArrayList<>();
   while (rs.next()) {
       VehicleModel v = new VehicleModel();
       v.id = rs.getInt("id");
       v.vehicleNumber = rs.getString("vehicle_number");
       v.vehicleType = rs.getString("vehicle_type");
       v.slotId = rs.getInt("slot_id");
       v.entryTime = rs.getString("entry_time");
       list.add(v);
   }
   return list;
}

public static List<String> getSlotStatus() throws Exception {
   Connection con = DBConnection.getConnection();
   Statement st = con.createStatement();
   ResultSet rs = st.executeQuery("SELECT id, slot_number, is_occupied FROM slots");
   List<String> list = new ArrayList<>();
   while (rs.next()) {
       String s = "Slot " + rs.getInt("slot_number") + ": " + (rs.getBoolean("is_occupied") ? "Occupied" : "Free");
       list.add(s);
   }
   return list;
}

public static List<VehicleModel> searchByType(String type) throws Exception {
   Connection con = DBConnection.getConnection();
   PreparedStatement ps = con.prepareStatement("SELECT * FROM vehicles WHERE vehicle_type=? AND exit_time IS NULL");
   ps.setString(1, type);
   ResultSet rs = ps.executeQuery();
   List<VehicleModel> list = new ArrayList<>();
   while (rs.next()) {
       VehicleModel v = new VehicleModel();
       v.vehicleNumber = rs.getString("vehicle_number");
       v.slotId = rs.getInt("slot_id");
       v.entryTime = rs.getString("entry_time");
       list.add(v);
   }
   return list;
}

}

