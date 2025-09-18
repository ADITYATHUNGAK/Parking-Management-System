package org;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ExitPage {
public static void main(String[] args) {
JFrame frame = new JFrame("Vehicle Exit");
frame.getContentPane().setBackground(new Color(255, 250, 240));
frame.setSize(420, 260);
frame.setLayout(null);
frame.setLocationRelativeTo(null);
frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);    
JLabel titleLabel = new JLabel("🚪 Vehicle Exit Checkout", SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    titleLabel.setForeground(new Color(128, 0, 0));
    titleLabel.setBounds(50, 10, 320, 30);

    JLabel vehicleLabel = new JLabel("Vehicle Number:");
    vehicleLabel.setBounds(40, 70, 120, 30);
    JTextField vehicleField = new JTextField();
    vehicleField.setBounds(170, 70, 180, 30);

    JButton submit = new JButton("Checkout & Bill");
    submit.setBounds(130, 130, 150, 40);
    submit.setBackground(new Color(220, 20, 60));
    submit.setForeground(Color.WHITE);
    submit.setFont(new Font("SansSerif", Font.BOLD, 14));

    frame.add(titleLabel);
    frame.add(vehicleLabel);
    frame.add(vehicleField);
    frame.add(submit);

    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String number = vehicleField.getText().trim();

            if (number.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a vehicle number.");
                return;
            }

            try (Connection con = DBConnection.getConnection()) {
                PreparedStatement fetchStmt = con.prepareStatement(
                    "SELECT id, entry_time, slot_id, name, vehicle_type FROM vehicles WHERE vehicle_number = ? AND exit_time IS NULL"
                );
                fetchStmt.setString(1, number);
                ResultSet rs = fetchStmt.executeQuery();

                if (rs.next()) {
                    int vehicleId = rs.getInt("id");
                    int slotId = rs.getInt("slot_id");
                    String name = rs.getString("name");
                    String type = rs.getString("vehicle_type");
                    Timestamp entryTime = rs.getTimestamp("entry_time");

                    LocalDateTime entryDateTime = entryTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();
                    long hours = Duration.between(entryDateTime, now).toHours();
                    hours = (hours == 0) ? 1 : hours;

                    double bill = hours * 10.0;
                    Timestamp exitTimestamp = Timestamp.valueOf(now);

                    PreparedStatement updateVehicle = con.prepareStatement(
                        "UPDATE vehicles SET exit_time = ?, bill = ? WHERE id = ?"
                    );
                    updateVehicle.setTimestamp(1, exitTimestamp);
                    updateVehicle.setDouble(2, bill);
                    updateVehicle.setInt(3, vehicleId);
                    updateVehicle.executeUpdate();

                    PreparedStatement freeSlot = con.prepareStatement("UPDATE slots SET is_occupied = FALSE WHERE id = ?");
                    freeSlot.setInt(1, slotId);
                    freeSlot.executeUpdate();

                    BillGenerator.showBillFrame(name, number, type, slotId, entryTime, exitTimestamp, hours, bill);
                } else {
                    JOptionPane.showMessageDialog(frame, "Vehicle not found or already checked out.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "❌ Error: " + ex.getMessage());
            }
        }
    });

    frame.setVisible(true);
}}
