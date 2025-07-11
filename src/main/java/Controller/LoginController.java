package Controller;

import Main.User;
import View.Access.MainMenu;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {

    private JTextField tf_username;
    private JTextField tf_password;

    public LoginController(JTextField tf_username, JTextField tf_password) {
        this.tf_username = tf_username;
        this.tf_password = tf_password;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Login".equals(e.getActionCommand())) {
            executeLogin();
        }
    }

    public void executeLogin() {
        String username = tf_username.getText();
        String password = tf_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Usuário e senha são obrigatórios.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User tempUser = new User(username, password);

        if (!tempUser.login()) {
            JOptionPane.showMessageDialog(null, "Usuário ou senha incorreta", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        } else {
            User loggedUser = tempUser.searchByName();
            if (loggedUser != null) {
                new MainMenu(loggedUser).setVisible(true);
                SwingUtilities.getWindowAncestor(tf_username).dispose();
            } else {
                 JOptionPane.showMessageDialog(null, "Erro ao carregar os dados do usuário.", "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}