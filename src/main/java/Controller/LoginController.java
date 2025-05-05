package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Main.User;

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
			executeLogin(tf_username.getText(), tf_password.getText());
		}
	}
	
	public void executeLogin(String username, String password) {
		User user = new User(username, password);
		if (user.login()) {
			JOptionPane.showMessageDialog(null, "Sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Usuário ou senha incorreta", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
}
