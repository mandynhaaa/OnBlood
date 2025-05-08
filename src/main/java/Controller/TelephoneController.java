package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Connection.ConnectionSQL;
import Main.Telephone;
import Main.User;

public class TelephoneController {
	
	private int id_Telephone;
	private int id_Usuario;
    private JTextField tf_number;
    private JTextField tf_Ddd;
    private JTextField tf_description;

    public TelephoneController(int id_Telephone, int id_Usuario, JTextField tf_description, JTextField tf_Ddd, JTextField tf_number) {
    	this.id_Telephone = id_Telephone;
    	this.id_Usuario = id_Usuario;
    	this.tf_description = tf_description;
        this.tf_Ddd = tf_Ddd;
        this.tf_number = tf_number;
    }

    public void executeRegister() {
        try {
            if (tf_number.getText().isEmpty()) {
            	JOptionPane.showMessageDialog(null, "O número não pode estar vazio.");
            	return;
            }
            
            User user = new User(id_Usuario);
            
            Telephone phone = new Telephone(
            	tf_description.getText(),
            	tf_Ddd.getText(),
            	tf_number.getText(),
                user
            );
            
            if (phone.create() > 0) {
                JOptionPane.showMessageDialog(null, "Telefone adicionado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao adicionar o telefone.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    
    public void executeUpdate() {
    	try {
            Telephone phone = new Telephone(this.id_Telephone);
            phone.setDdd(tf_Ddd.getText());
            phone.setNumber(tf_number.getText());
            phone.setDescription(tf_description.getText());

            phone.update();
            JOptionPane.showMessageDialog(null, "Telefone alterado com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> listTelephones() {
        List<String> telephones = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = """
                SELECT t.id_Telefone, t.descricao, t.ddd, t.numero
                FROM telefone t
                JOIN usuario u ON t.id_Usuario = u.id_Usuario
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Telefone");
                String description = rs.getString("descricao");
                String ddd = rs.getString("ddd");
                String number = rs.getString("numero");
                telephones.add("[" + id + "] " + description + ": " + ddd + " - " + number);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return telephones;
    }

    public void executeDelete() {
    	try {
            Telephone phone = new Telephone(this.id_Telephone);
            phone.delete();
            JOptionPane.showMessageDialog(null, "Telefone removido com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
