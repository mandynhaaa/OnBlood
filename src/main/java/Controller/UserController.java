package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.DateFormatter;

import java.sql.*;

import Main.BloodCenter;
import Main.BloodType;
import Main.Donor;
import Main.User;
import Main.UserType;
import Standard.PasswordCrypt;
import View.Access.Login;
import View.Access.RegisterUser;
import Connection.ConnectionSQL;

public class UserController {

	private int id_Usuario, id_userType;
    private JTextField tf_name, tf_email, tf_password;
    private JComboBox<String> combo_Type;
    private JTextField tf_cpf, tf_razao_Social, tf_endereco, tf_cnpj;
    private JTextField tf_dataNascimento;
    private JComboBox<String> comboTipoSanguineo;

    public UserController(int id_Usuario, int id_userType, JTextField name, JTextField email, JTextField password,
            JComboBox<String> type, JTextField cpf,
            JTextField razao_Social, JTextField dataNascimento,
            JComboBox<String> tipoSanguineo, JTextField cnpj) {
    	this.id_Usuario = id_Usuario;
    	this.id_userType = id_userType;
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
        	int idDoador = 0;
        	int idHemocentro = 0;

            if (idType == 2) {
                idDoador = createDoador(userId);
            } else {
                idHemocentro = createHemocentro(userId);
            }

            if (idDoador > 0 || idHemocentro > 0) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                new Login().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Usuário criado, mas erro ao vincular com tabela específica.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Erro ao criar usuário.");
        }
    }
    
    public void executeUpdate() {
        String name = tf_name.getText();
        String email = tf_email.getText();
        String password = tf_password.getText();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            return;
        }

        UserType userType = new UserType(id_userType);
        User user = new User(id_Usuario);
        user.setName(name);
        user.setEmail(email);
        
        if (!password.isEmpty()) {        	
        	user.setPassword(password);
        }
        
        user.update();
        JOptionPane.showMessageDialog(null, "Usuário alterado com sucesso!");

        if (id_userType == 2) {
            updateDoador(id_Usuario);
        } else {
            updateHemocentro(id_Usuario);
        }
    }

    private int createDoador(int userId) {
    	
        String selectedBloodType = (String) comboTipoSanguineo.getSelectedItem();
        if (selectedBloodType == null || !selectedBloodType.matches("^\\[\\d+\\].*")) {
            JOptionPane.showMessageDialog(null, "Selecione um tipo sanguíneo válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return 0;
        }

        int idTipoSanguineo = 1;
        try {
            String idText = selectedBloodType.substring(selectedBloodType.indexOf('[') + 1, selectedBloodType.indexOf(']'));
            idTipoSanguineo = Integer.parseInt(idText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao extrair o ID do tipo sanguíneo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        LocalDate birthDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
        	birthDate = LocalDate.parse(tf_dataNascimento.getText(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "A data deve estar no formato: dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        String cpf = tf_cpf.getText();
        
        User user = new User(userId);
        BloodType bloodType = new BloodType(idTipoSanguineo);
        Donor donor = new Donor(cpf, birthDate, user, bloodType);
        return donor.create();
    }
    
    private void updateDoador(int userId) {
        String selectedBloodType = (String) comboTipoSanguineo.getSelectedItem();
        if (selectedBloodType == null || !selectedBloodType.matches("^\\[\\d+\\].*")) {
            JOptionPane.showMessageDialog(null, "Selecione um tipo sanguíneo válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idTipoSanguineo = 1;
        try {
            String idText = selectedBloodType.substring(selectedBloodType.indexOf('[') + 1, selectedBloodType.indexOf(']'));
            idTipoSanguineo = Integer.parseInt(idText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao extrair o ID do tipo sanguíneo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        LocalDate birthDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
        	birthDate = LocalDate.parse(tf_dataNascimento.getText(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "A data deve estar no formato: dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        BloodType bloodType = new BloodType(idTipoSanguineo);
        
        Donor donor = buscarDoadorPorIdUsuario(userId);
        donor.setCpf(tf_cpf.getText());
        donor.setBirthDate(birthDate);
        donor.setBloodType(bloodType);
        donor.update();
        
        return;
    }

    private int createHemocentro(int userId) {
    	String cnpj = tf_cnpj.getText();
    	String razao_Social = tf_razao_Social.getText();
    	
    	User user = new User(userId);
        BloodCenter bloodCenter = new BloodCenter(cnpj, razao_Social, user);
        return bloodCenter.create();
    }
    
    private void updateHemocentro(int userId) {
    	String cnpj = tf_cnpj.getText();
    	String razao_Social = tf_razao_Social.getText();
    	
    	BloodCenter bloodCenter = buscarHemocentroPorIdUsuario(userId);
    	bloodCenter.setCnpj(tf_cnpj.getText());
    	bloodCenter.setCompanyName(tf_razao_Social.getText());
    	bloodCenter.update();
    	
        return ;
    }
    
    public List<String> listarTiposSanguineos() {
        List<String> tipos = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT id_Tipo_Sanguineo, descricao FROM tipo_Sanguineo";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Tipo_Sanguineo");
                String desc = rs.getString("descricao");
                tipos.add("[" + id + "] " + desc);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipos;
    }
    
    public Donor buscarDoadorPorIdUsuario(int id) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT id_Doador FROM doador WHERE id_Usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            
            int id_Doador = 0;
            if (rs.next()) {
            	id_Doador = Integer.parseInt(rs.getString("id_Doador"));
            }
            rs.close();
            stmt.close();
            conn.close();
            
            return new Donor(id_Doador);
        } catch (Exception e) {
            e.printStackTrace();
            return new Donor(0);
        }
    }
    
    public BloodCenter buscarHemocentroPorIdUsuario(int id) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT id_Hemocentro FROM hemocentro WHERE id_Usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            
            int id_Hemocentro = 0;
            if (rs.next()) {
            	id_Hemocentro = Integer.parseInt(rs.getString("id_Hemocentro"));
            }
            rs.close();
            stmt.close();
            conn.close();
            
            return new BloodCenter(id_Hemocentro);
        } catch (Exception e) {
            e.printStackTrace();
            return new BloodCenter(0);
        }
    }
}
