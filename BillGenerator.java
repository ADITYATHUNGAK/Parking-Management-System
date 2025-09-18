package org;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class BillGenerator {
	public static void showBillFrame(
	        String name,
	        String vehicleNumber,
	        String type,
	        int slotId,
	        Timestamp entryTime,
	        Timestamp exitTime,
	        long hours,
	        double billAmount
	) {
	    JFrame billFrame = new JFrame("🧾 Parking Bill");
	    billFrame.setSize(500, 500);
	    billFrame.setLocationRelativeTo(null);
	    billFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    panel.setBackground(new Color(255, 255, 240));

	    JLabel titleLabel = new JLabel("✅ Parking Bill Receipt", SwingConstants.CENTER);
	    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
	    titleLabel.setForeground(new Color(0, 102, 102));
	    panel.add(titleLabel, BorderLayout.NORTH);

	    JTextArea billArea = new JTextArea();
	    billArea.setEditable(false);
	    billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
	    billArea.setBackground(Color.WHITE);
	    billArea.setMargin(new Insets(10, 10, 10, 10));

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	    String billText =
	            "---------- Vehicle Parking Bill ----------\n" +
	            "Customer Name   : " + name + "\n" +
	            "Vehicle Number  : " + vehicleNumber + "\n" +
	            "Vehicle Type    : " + type + "\n" +
	            "Slot ID         : " + slotId + "\n" +
	            "------------------------------------------\n" +
	            "Entry Time      : " + formatter.format(entryTime.toLocalDateTime()) + "\n" +
	            "Exit Time       : " + formatter.format(exitTime.toLocalDateTime()) + "\n" +
	            "Duration        : " + hours + " hour(s)\n" +
	            "Rate            : ₹10/hour\n" +
	            "------------------------------------------\n" +
	            "Total Bill      : ₹" + String.format("%.2f", billAmount) + "\n" +
	            "------------------------------------------\n" +
	            "Thank you for using our service!\n";

	    billArea.setText(billText);

	    JScrollPane scroll = new JScrollPane(billArea);
	    scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	    panel.add(scroll, BorderLayout.CENTER);

	    // Save and Print Buttons
	    JPanel buttonPanel = new JPanel();
	    JButton saveButton = new JButton("💾 Save Bill");
	    JButton printButton = new JButton("🖨️ Print Bill");

	    // Save bill as text file
	    saveButton.addActionListener(e -> {
	        try {
	            File dir = new File("bills");
	            if (!dir.exists()) dir.mkdirs();

	            String filename = "bills/Bill_" + vehicleNumber + "_" + System.currentTimeMillis() + ".txt";
	            try (FileWriter fw = new FileWriter(filename)) {
	                fw.write(billArea.getText());
	                JOptionPane.showMessageDialog(billFrame, "Bill saved to:\n" + filename);
	            }
	        } catch (IOException ex) {
	            JOptionPane.showMessageDialog(billFrame, "Error saving bill:\n" + ex.getMessage());
	        }
	    });

	    // Print the bill
	    printButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            try {
	                boolean printed = billArea.print();
	                if (!printed) {
	                    JOptionPane.showMessageDialog(billFrame, "Printing canceled by user.");
	                }
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(billFrame, "Error during printing:\n" + ex.getMessage());
	            }
	        }
	    });

	    buttonPanel.add(saveButton);
	    buttonPanel.add(printButton);
	    panel.add(buttonPanel, BorderLayout.SOUTH);

	    billFrame.setContentPane(panel);
	    billFrame.setVisible(true);
	}

}