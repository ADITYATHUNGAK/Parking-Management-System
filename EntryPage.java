package org;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EntryPage {
	public static void main(String[] args) {
	    JFrame frame = new JFrame("Vehicle Entry");
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setSize(500, 400);
	    frame.setResizable(false);
	    frame.getContentPane().setBackground(new Color(255, 248, 230)); // Light orange/white mix

	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(new Color(255, 248, 230));
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 10, 10);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.anchor = GridBagConstraints.CENTER;

	    JLabel titleLabel = new JLabel("Vehicle Entry Form", SwingConstants.CENTER);
	    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    titleLabel.setForeground(new Color(0, 102, 102));
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    panel.add(titleLabel, gbc);

	    gbc.gridwidth = 1;

	    JLabel nameLabel = new JLabel("Name:");
	    nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    panel.add(nameLabel, gbc);

	    JTextField nameField = new JTextField(15);
	    gbc.gridx = 1;
	    panel.add(nameField, gbc);

	    JLabel phoneLabel = new JLabel("Phone Number:");
	    phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    panel.add(phoneLabel, gbc);

	    JTextField phoneField = new JTextField(15);
	    // Restrict to digits and max 10 length
	    ((AbstractDocument) phoneField.getDocument()).setDocumentFilter(new DocumentFilter() {
	        @Override
	        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
	            if (string == null) return;
	            StringBuilder sb = new StringBuilder();
	            for (char c : string.toCharArray()) {
	                if (Character.isDigit(c)) {
	                    sb.append(c);
	                }
	            }
	            if ((fb.getDocument().getLength() + sb.length()) <= 10) {
	                super.insertString(fb, offset, sb.toString(), attr);
	            }
	        }

	        @Override
	        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
	            if (string == null) return;
	            StringBuilder sb = new StringBuilder();
	            for (char c : string.toCharArray()) {
	                if (Character.isDigit(c)) {
	                    sb.append(c);
	                }
	            }
	            int currentLength = fb.getDocument().getLength();
	            int overLimit = (currentLength - length + sb.length()) - 10;
	            if (overLimit <= 0) {
	                super.replace(fb, offset, length, sb.toString(), attr);
	            }
	        }
	    });
	    gbc.gridx = 1;
	    panel.add(phoneField, gbc);

	    JLabel vehicleLabel = new JLabel("Vehicle Number:");
	    vehicleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    panel.add(vehicleLabel, gbc);

	    JTextField vehicleField = new JTextField(15);
	    gbc.gridx = 1;
	    panel.add(vehicleField, gbc);

	    JLabel typeLabel = new JLabel("Vehicle Type:");
	    typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    panel.add(typeLabel, gbc);

	    String[] vehicleTypes = { "Two Wheeler", "Four Wheeler", "Heavy Vehicle" };
	    JComboBox<String> typeCombo = new JComboBox<>(vehicleTypes);
	    typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    gbc.gridx = 1;
	    panel.add(typeCombo, gbc);

	    JButton submit = new JButton("Submit");
	    submit.setBackground(new Color(255, 140, 0));
	    submit.setForeground(Color.WHITE);
	    submit.setFont(new Font("Segoe UI", Font.BOLD, 14));
	    gbc.gridx = 0;
	    gbc.gridy = 5;
	    gbc.gridwidth = 2;
	    panel.add(submit, gbc);

	    submit.addActionListener(e -> {
	        String name = nameField.getText().trim();
	        String phone = phoneField.getText().trim();
	        String number = vehicleField.getText().trim();
	        String type = (String) typeCombo.getSelectedItem();

	        if (name.isEmpty() || phone.isEmpty() || number.isEmpty() || type == null || type.isEmpty()) {
	            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
	            return;
	        }

	        if (phone.length() != 10) {
	            JOptionPane.showMessageDialog(frame, "Phone number must be exactly 10 digits.");
	            return;
	        }

	        try (Connection con = DBConnection.getConnection()) {
	            PreparedStatement slotStmt = con.prepareStatement("SELECT id FROM slots WHERE is_occupied = FALSE LIMIT 1");
	            ResultSet rs = slotStmt.executeQuery();

	            if (rs.next()) {
	                int slotId = rs.getInt("id");

	                PreparedStatement insertStmt = con.prepareStatement(
	                        "INSERT INTO vehicles(name, phone_number, vehicle_number, vehicle_type, slot_id) VALUES (?, ?, ?, ?, ?)"
	                );
	                insertStmt.setString(1, name);
	                insertStmt.setString(2, phone);
	                insertStmt.setString(3, number);
	                insertStmt.setString(4, type);
	                insertStmt.setInt(5, slotId);
	                insertStmt.executeUpdate();

	                PreparedStatement updateSlot = con.prepareStatement("UPDATE slots SET is_occupied = TRUE WHERE id = ?");
	                updateSlot.setInt(1, slotId);
	                updateSlot.executeUpdate();

	                JOptionPane.showMessageDialog(frame, "Vehicle parked in slot ID: " + slotId);
	            } else {
	                JOptionPane.showMessageDialog(frame, "No available slots.");
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
	            ex.printStackTrace();
	        }
	    });

	    frame.add(panel);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}

}