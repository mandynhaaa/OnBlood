package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Main.User;

public class Register implements ActionListener {
	private JTextField tf_username;
	private JTextField tf_email;
	private JTextField tf_password;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Register")) {
			executeRegister(tf_username.getText(), tf_password.getText());
		}
	}
	
	public void executeRegister(String username, String password) {
		User user = new User(username, password);
		if (user.login()) {
			JOptionPane.showMessageDialog(null, "Sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Usuário ou senha incorreta", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}	
}
