package org;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {

    public RegisterDialog(JFrame parent) {
        super(parent, "Register New User", true);

        // Set larger initial size and minimum size
        setSize(500, 400);
        setMinimumSize(new Dimension(500, 400));
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // same light blue background as login
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel title = new JLabel("User Registration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(33, 37, 41));

        JLabel l1 = new JLabel("Username:");
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField tf1 = new JTextField(20);
        tf1.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel l2 = new JLabel("Password:");
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JPasswordField pf1 = new JPasswordField(20);
        pf1.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerBtn.setBackground(new Color(255, 140, 0)); // orange button
        registerBtn.setForeground(Color.WHITE);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(l1, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(tf1, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(l2, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(pf1, gbc);

        // Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerBtn, gbc);

        add(panel);

        registerBtn.addActionListener(e -> {
            String username = tf1.getText().trim();
            String password = new String(pf1.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                System.out.println("Attempting registration for: " + username);
                if (ParkingDAO.registerUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "User registered successfully");

                    dispose(); // close registration dialog
                    if (parent == null) {
                        LoginPage.main(null); // reopen login if launched standalone
                    } else {
                        parent.setVisible(true); // show login again
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterDialog(null));
    }
}
