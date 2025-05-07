package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.*;

import Main.User;
import Main.UserType;
import Standard.PasswordCrypt;

public class UserController implements ActionListener {

    private JTextField tf_name;
    private JTextField tf_email;
    private JTextField tf_password;
    private JComboBox<String> combo_Type;

    public UserController(JTextField name, JTextField email, JTextField password, JComboBox<String> type) {
        this.tf_name = name;
        this.tf_email = email;
        this.tf_password = password;
        this.combo_Type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Criar Conta")) {
			executeRegister();
		}
    }
    
    public void executeRegister() {
        String name = tf_name.getText();
        String email = tf_email.getText();
        String password = PasswordCrypt.hash(tf_password.getText());
        String type = combo_Type.getSelectedItem().toString();
        LocalDateTime creationDate = LocalDateTime.now();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
            return;
        }
        
        int idType = 2;
        if (type == "Hemocentro") {
        	idType = 3;
        }
        
        UserType userType = new UserType(idType);
        
        User user = new User(name, email, password, creationDate, userType);
        
        User existingUser = user.searchByName();
        if (existingUser != null && existingUser.getId() > 0) {
            JOptionPane.showMessageDialog(null, "Este nome de usuário não está disponível");
            return;
        }

        if (user.create() > 0) {        	
        	JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
        } else {
        	JOptionPane.showMessageDialog(null, "Ocorreu um erro no cadastro de usuário");
        }
    }
    
}
