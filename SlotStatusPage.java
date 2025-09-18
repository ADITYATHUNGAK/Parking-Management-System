package org;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SlotStatusPage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("📊 Parking Slot Status");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 550);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(250, 250, 255)); // Light gray-blue

        // Title with icon
        JLabel titleLabel = new JLabel("🅿️ Parking Slot Status Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 42, 86));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Center panel with white card-like appearance
        JPanel centerCard = new JPanel(new BorderLayout(10, 10));
        centerCard.setBackground(Color.WHITE);
        centerCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Section heading
        JLabel subHeader = new JLabel("📌 Current Slot Assignments");
        subHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subHeader.setForeground(new Color(72, 84, 96));
        centerCard.add(subHeader, BorderLayout.NORTH);

        // Text area
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setBackground(new Color(248, 248, 248));
        area.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        JScrollPane scroll = new JScrollPane(area);
        centerCard.add(scroll, BorderLayout.CENTER);

        // Status message
        JLabel status = new JLabel(" ");
        status.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        status.setForeground(Color.DARK_GRAY);
        centerCard.add(status, BorderLayout.SOUTH);

        panel.add(centerCard, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnPanel.setBackground(panel.getBackground());

        JButton saveBtn = new JButton("💾 Save Report");
        JButton printBtn = new JButton("🖨️ Print");

        saveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        saveBtn.setBackground(new Color(52, 152, 219));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        printBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        printBtn.setBackground(new Color(46, 204, 113));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFocusPainted(false);
        printBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnPanel.add(saveBtn);
        btnPanel.add(printBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Fetch slot data
        try {
            List<String> slots = ParkingDAO.getSlotStatus();
            if (slots.isEmpty()) {
                area.append("❗ No slot status data available.");
            } else {
                for (String s : slots) {
                    area.append("• " + s + "\n");
                }
            }
        } catch (Exception e) {
            area.append("❌ Error fetching slot data: " + e.getMessage());
            e.printStackTrace();
        }

        // Save action
        saveBtn.addActionListener((ActionEvent e) -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Slot Status Report");
            chooser.setSelectedFile(new File("SlotStatusReport.txt"));
            int result = chooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(chooser.getSelectedFile()))) {
                    writer.write(area.getText());
                    status.setText("✅ Report saved: " + chooser.getSelectedFile().getName());
                } catch (IOException ex) {
                    status.setText("❌ Failed to save file.");
                }
            }
        });

        // Print action
        printBtn.addActionListener((ActionEvent e) -> {
            try {
                area.print();
                status.setText("🖨️ Printing completed.");
            } catch (PrinterException ex) {
                status.setText("❌ Print failed.");
            }
        });

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
