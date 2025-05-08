package View.Access;

import javax.swing.*;
import java.awt.*;
import Main.User;
import View.BloodStock.ManagerBloodStock;
import View.Donation.ManagerDonation;
import View.Donation.MyDonations;
import View.Request.ManagerRequest;

public class MainMenu extends JFrame {

    private static final long serialVersionUID = 1L;
	private static final String UserController = null;
    private JPanel contentPane;

    public MainMenu(User user) {
        setTitle("Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        JLabel welcomeLabel = new JLabel("Bem-vindo, " + user.getName() + "!");
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(welcomeLabel);

        int userTypeId = user.getUserType().getId(); // 1 = Admin, 2 = Hemocentro, 3 = Doador

        if (userTypeId == 1) {
            addButton("Gerenciar Usuários", e -> {
                // abrir tela de gerenciamento
            });

            addButton("Gerenciar Doações", e -> {
                // abrir tela
            });

            addButton("Gerenciar Solicitações", e -> {
                // abrir tela
            });

            addButton("Gerenciar Estoque", e -> {
                // abrir tela
            });

        // HEMOCENTRO
        } else if (userTypeId == 3) {
            addButton("Doações", e -> {
            	new ManagerDonation(user.getId()).setVisible(true);
            });

            addButton("Solicitações", e -> {
            	new ManagerRequest(user.getId()).setVisible(true);
            });
            
            addButton("Estoque", e -> {
            	new ManagerBloodStock(user.getId()).setVisible(true);
            });
            
            addButton("Configurações de Conta", e -> {
            	new AccountConfiguration(user.getId(), userTypeId).setVisible(true);
            });

        // DOADOR
        } else if (userTypeId == 2) {
        	addButton("Minhas Doações", e -> {
        	    new MyDonations(user.getId()).setVisible(true);
        	});
        	
            addButton("Configurações de Conta", e -> {
            	new AccountConfiguration(user.getId(), userTypeId).setVisible(true);
            });
        }

        JButton logoutButton = new JButton("Sair");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener(e -> {
            new View.Access.Login().setVisible(true);
            dispose();
        });
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPane.add(logoutButton);
    }

    private void addButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(button);
    }
}
