package View.Access;

import Main.User;
import View.BloodStock.ManagerBloodStock;
import View.Donation.ManagerDonation;
import View.Donation.MyDonations;
import View.Request.ManagerRequest;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public MainMenu(User user) {
        setTitle("Menu Principal - OnBlood");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null); 
        
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        JLabel welcomeLabel = new JLabel("Bem-vindo, " + user.getName() + "!");
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(welcomeLabel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));

        String userType = user.getUserType(); 

        if ("Administrador".equals(userType)) {
        
        } else if ("Hemocentro".equals(userType)) {
            addButton("Gerenciar Doações Recebidas", e -> new ManagerDonation(user.getId()).setVisible(true));
            addButton("Gerenciar Solicitações", e -> new ManagerRequest(user.getId()).setVisible(true));
            addButton("Verificar Estoque", e -> new ManagerBloodStock(user.getId()).setVisible(true));
            addButton("Configurações da Conta", e -> new AccountConfiguration(user).setVisible(true));

        } else if ("Doador".equals(userType)) {
            addButton("Minhas Doações", e -> new MyDonations(user.getId()).setVisible(true));
            addButton("Configurações da Conta", e -> new AccountConfiguration(user).setVisible(true));
        }

        contentPane.add(Box.createVerticalGlue());
        JButton logoutButton = new JButton("Sair");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(120, 30));
        logoutButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
        contentPane.add(logoutButton);
    }

    private void addButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 40)); 
        button.setFont(new Font("Tahoma", Font.PLAIN, 14));
        button.addActionListener(action);
        contentPane.add(button);
        contentPane.add(Box.createRigidArea(new Dimension(0, 15)));
    }
}