package org;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // bigger window to give room for half page form
        frame.setLocationRelativeTo(null); // center on screen

        // Custom panel with orange-white gradient background
        JPanel outerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(255, 153, 51);  // orange
                Color color2 = Color.WHITE;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        outerPanel.setLayout(new GridBagLayout());

        // Inner form panel - make this roughly half the frame size
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(380, 300));  // around half width and height
        formPanel.setBackground(new Color(245, 245, 245)); // off-white
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 120, 30), 2, true), // subtle orange border with rounded corners
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        JLabel title = new JLabel("Parking System Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(33, 37, 41)); // dark grey

        JLabel l1 = new JLabel("Username:");
        l1.setFont(labelFont);

        JTextField tf1 = new JTextField(15);
        tf1.setFont(fieldFont);

        JLabel l2 = new JLabel("Password:");
        l2.setFont(labelFont);

        JPasswordField pf1 = new JPasswordField(15);
        pf1.setFont(fieldFont);

        JButton login = new JButton("Login");
        login.setFont(buttonFont);
        login.setBackground(new Color(255, 140, 0));  // dark orange
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);

        JButton register = new JButton("Register");
        register.setFont(buttonFont);
        register.setBackground(new Color(255, 165, 79)); // lighter orange
        register.setForeground(Color.WHITE);
        register.setFocusPainted(false);

        // Title row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(title, gbc);

        // Username label
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(l1, gbc);

        // Username field
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        formPanel.add(tf1, gbc);

        // Password label
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(l2, gbc);

        // Password field
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        formPanel.add(pf1, gbc);

        // Buttons row: put login and register side by side with a gap
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // transparent background

        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.insets = new Insets(0, 10, 0, 10);
        btnGbc.fill = GridBagConstraints.HORIZONTAL;
        btnGbc.weightx = 0.5;

        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        buttonPanel.add(login, btnGbc);

        btnGbc.gridx = 1;
        buttonPanel.add(register, btnGbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);

        outerPanel.add(formPanel);
        frame.add(outerPanel);
        frame.setVisible(true);

        // Shared action listener for login
        ActionListener loginAction = e -> {
            try {
                String username = tf1.getText().trim();
                String password = new String(pf1.getPassword()).trim();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (ParkingDAO.authenticate(username, password)) {
                    frame.dispose();
                    Dashboard.main(null);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
            }
        };

        // Trigger login on button click or Enter key in text fields
        login.addActionListener(loginAction);
        tf1.addActionListener(loginAction);   // Enter on username field
        pf1.addActionListener(loginAction);   // Enter on password field

        register.addActionListener(e -> {
            new RegisterDialog(frame);
        });
    }
}
