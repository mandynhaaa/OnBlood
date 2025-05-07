package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Main.User;
import View.MainMenu;

public class LoginController implements ActionListener {
	
	private JTextField tf_username;
	private JTextField tf_password;
	
	public LoginController(JTextField tf_username, JTextField tf_password) {
		this.tf_username = tf_username;
		this.tf_password = tf_password;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Login")) {
			executeLogin();
		}
	}
	
	public void executeLogin() {
	    String username = tf_username.getText();
	    String password = tf_password.getText();
	    
	    User tempUser = new User(username, password);
	    if (!tempUser.login()) {
	        JOptionPane.showMessageDialog(null, "Usuário ou senha incorreta", "Erro", JOptionPane.ERROR_MESSAGE);
	    } else {
	        User loggedUser = tempUser.searchByName(); // Recupera os dados completos
	        new MainMenu(loggedUser).setVisible(true); // <-- redireciona para o menu principal
	    }
	}
}
