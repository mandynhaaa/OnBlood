package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.*;

import java.sql.*;

import Main.User;
import Main.UserType;
import Standard.PasswordCrypt;
import View.Access.Login;
import View.Access.RegisterUser;
import Connection.ConnectionSQL;

public class UserController implements ActionListener {

    private JTextField tf_name, tf_email, tf_password;
    private JComboBox<String> combo_Type;
    private JTextField tf_cpf, tf_razao_Social, tf_endereco, tf_cnpj;
    private JTextField tf_dataNascimento;
    private JComboBox<String> comboTipoSanguineo;

    public UserController(JTextField name, JTextField email, JTextField password,
            JComboBox<String> type, JTextField cpf,
            JTextField razao_Social, JTextField dataNascimento,
            JComboBox<String> tipoSanguineo, JTextField cnpj) {
    	this.tf_name = name;
        this.tf_email = email;
        this.tf_password = password;
        this.combo_Type = type;
        this.tf_cpf = cpf;
        this.tf_razao_Social = razao_Social;
        this.tf_dataNascimento = dataNascimento;
        this.comboTipoSanguineo = tipoSanguineo;
        this.tf_cnpj = cnpj;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Criar Conta")) {
            executeRegister();
        }
    }

    public void executeRegister() {
        String name = tf_name.getText();
        String email = tf_email.getText();
        String rawPassword = tf_password.getText();
        String password = PasswordCrypt.hash(rawPassword);
        String type = combo_Type.getSelectedItem().toString();
        LocalDateTime creationDate = LocalDateTime.now();

        if (name.isEmpty() || email.isEmpty() || rawPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            return;
        }

        int idType = type.equals("Hemocentro") ? 3 : 2;
        UserType userType = new UserType(idType);
        User user = new User(name, email, password, creationDate, userType);

        User existingUser = user.searchByName();
        if (existingUser != null && existingUser.getId() > 0) {
            JOptionPane.showMessageDialog(null, "Este nome de usuário não está disponível");
            return;
        }

        int userId = user.create();
        if (userId > 0) {
            boolean success = false;

            if (idType == 2) { // Doador
                success = createDoador(userId, tf_cpf.getText());
            } else {
                success = createHemocentro(userId, tf_razao_Social.getText());
            }

            if (success) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                new Login().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Usuário criado, mas erro ao vincular com tabela específica.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Erro ao criar usuário.");
        }
    }

    private boolean createDoador(int userId, String cpfStr) {
        try (Connection conn = new ConnectionSQL().getConnection();) {
            String sql = "INSERT INTO doador (id_Usuario, cpf, data_Nascimento, id_Tipo_Sanguineo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, cpfStr);
            stmt.setDate(3, java.sql.Date.valueOf(tf_dataNascimento.getText()));
            stmt.setInt(4, getBloodTypeIdByName(comboTipoSanguineo.getSelectedItem().toString()));
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createHemocentro(int userId, String razao_Social) {
        try (Connection conn = new ConnectionSQL().getConnection();) {
            String cnpj = tf_cnpj.getText();

            String sql = "INSERT INTO hemocentro (id_Usuario, razao_Social, cnpj) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, razao_Social);
            stmt.setString(3, cnpj);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private int getBloodTypeIdByName(String name) {
        switch (name) {
            case "A+": return 1;
            case "A-": return 2;
            case "B+": return 3;
            case "B-": return 4;
            case "AB+": return 5;
            case "AB-": return 6;
            case "O+": return 7;
            case "O-": return 8;
            default: return 0;
        }
    }
}
