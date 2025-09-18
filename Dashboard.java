package org;

import javax.swing.*;
import java.awt.*;

public class Dashboard {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Parking Management Dashboard");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Custom panel with gradient background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, height, new Color(255, 140, 0)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(new BorderLayout());

        // Title label
        JLabel title = new JLabel("🚗 Parking Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        gradientPanel.add(title, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Create buttons with listeners
        JButton entryButton = createDashboardButton("➕ Vehicle Entry");
        entryButton.addActionListener(e -> EntryPage.main(null));  // <-- ADD LISTENER

        JButton exitButton = createDashboardButton("🚪 Vehicle Exit");
        exitButton.addActionListener(e -> ExitPage.main(null));    // <-- ADD LISTENER

        JButton slotStatusButton = createDashboardButton("📊 Slot Status");
        slotStatusButton.addActionListener(e -> SlotStatusPage.main(null));  // if exists

        

        JButton reportButton = createDashboardButton("📝 Generate Report");
        reportButton.addActionListener(e -> ReportPage.main(null));  // if exists

        // Add buttons
        buttonPanel.add(entryButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(slotStatusButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        
        
        buttonPanel.add(reportButton);

        // Centered layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.setPreferredSize(new Dimension(350, 400));

        gradientPanel.add(centerPanel, BorderLayout.CENTER);

        frame.setContentPane(gradientPanel);
        frame.setVisible(true);
    }

    private static JButton createDashboardButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(300, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}
