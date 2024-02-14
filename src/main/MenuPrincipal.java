package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
        setTitle("Menu Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setSize(1200, 800); // Default size


        ImageIcon backgroundImage = new ImageIcon("res/images/backgroundMainMenu.png");
        JLabel backgroundLabel = new JLabel(backgroundImage) {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(backgroundLabel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        JButton startButton = new JButton("Commencer");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Démarrez le jeu !");
            }
        });

        JButton optionsButton = new JButton("Options");
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Affichez les options.");
            }
        });

        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Quittez le jeu.");
                System.exit(0);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 0, 20, 0); // Add spacing between buttons
        buttonPanel.add(startButton, gbc);

        gbc.gridy++;
        buttonPanel.add(optionsButton, gbc);

        gbc.gridy++;
        buttonPanel.add(quitButton, gbc);

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(buttonPanel);

        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}
