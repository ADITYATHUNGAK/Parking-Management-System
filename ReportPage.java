package org;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportPage {
public static void main(String[] args) {
JFrame frame = new JFrame("📝 Vehicle Report");
frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
frame.setSize(600, 500);
frame.setLocationRelativeTo(null);
// Main panel
JPanel panel = new JPanel(new BorderLayout(15, 15));
panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
panel.setBackground(new Color(240, 248, 255));

// Header panel
JPanel headerPanel = new JPanel();
headerPanel.setBackground(new Color(100, 149, 237)); // Cornflower blue
headerPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
));

JLabel titleLabel = new JLabel("📄 Current Parked Vehicle Report");
titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
titleLabel.setForeground(Color.WHITE);
headerPanel.add(titleLabel);

panel.add(headerPanel, BorderLayout.NORTH);

// Text area
JTextArea area = new JTextArea();
area.setEditable(false);
area.setFont(new Font("Consolas", Font.PLAIN, 14));
area.setBackground(Color.WHITE);
area.setMargin(new Insets(10, 10, 10, 10));
area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

JScrollPane scroll = new JScrollPane(area);
scroll.setBorder(BorderFactory.createTitledBorder("🅿️ Vehicle Details"));

panel.add(scroll, BorderLayout.CENTER);

// Button panel
JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
bottomPanel.setBackground(panel.getBackground());

JButton printBtn = new JButton("🖨️ Print Report");
printBtn.setFocusPainted(false);
printBtn.setBackground(new Color(70, 130, 180));
printBtn.setForeground(Color.WHITE);
printBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

JButton saveBtn = new JButton("💾 Save Report");
saveBtn.setFocusPainted(false);
saveBtn.setBackground(new Color(34, 139, 34)); // Forest Green
saveBtn.setForeground(Color.WHITE);
saveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

printBtn.addActionListener((ActionEvent e) -> {
    try {
        boolean printed = area.print();
        if (!printed) {
            JOptionPane.showMessageDialog(frame, "Printing was cancelled.");
        }
    } catch (PrinterException ex) {
        JOptionPane.showMessageDialog(frame, "Error printing report: " + ex.getMessage());
    }
});

saveBtn.addActionListener((ActionEvent e) -> {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Report");
    fileChooser.setSelectedFile(new File("VehicleReport.txt"));
    int result = fileChooser.showSaveDialog(frame);
    if (result == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            writer.write(area.getText());
            JOptionPane.showMessageDialog(frame, "Report saved to:\n" + fileToSave.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving file:\n" + ex.getMessage());
        }
    }
});

bottomPanel.add(saveBtn);
bottomPanel.add(printBtn);
panel.add(bottomPanel, BorderLayout.SOUTH);

// Fetch and display report
try {
    List<VehicleModel> vehicles = ParkingDAO.getAllParkedVehicles();
    area.append("Total Parked Vehicles: " + vehicles.size() + "\n\n");
    for (VehicleModel v : vehicles) {
        area.append("Number: " + v.vehicleNumber +
                " | Slot: " + v.slotId +
                " | Entry: " + v.entryTime + "\n");
    }
} catch (Exception e) {
    area.append("⚠️ Error fetching data: " + e.getMessage());
    e.printStackTrace();
}

frame.setContentPane(panel);
frame.setVisible(true);
}
}