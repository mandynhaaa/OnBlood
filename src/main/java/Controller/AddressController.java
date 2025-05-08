package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.sql.*;

import Connection.ConnectionSQL;
import Main.Address;
import Main.User;

public class AddressController {
	
	private int id_Address;
	private int id_Usuario;
    private JTextField tf_street;
    private JTextField tf_number;
    private JTextField tf_city;
    private JTextField tf_state;
    private JTextField tf_neighborhood;
    private JTextField tf_complement;
    private JTextField tf_cep;
    private JTextField tf_country;
    private JTextField tf_description;

    public AddressController(
        int id_Address, int id_Usuario, JTextField street, JTextField number, JTextField city, JTextField state,
        JTextField neighborhood, JTextField complement, JTextField cep,
        JTextField country, JTextField description
    ) {
    	this.id_Address = id_Address;
    	this.id_Usuario = id_Usuario;
        this.tf_street = street;
        this.tf_number = number;
        this.tf_city = city;
        this.tf_state = state;
        this.tf_neighborhood = neighborhood;
        this.tf_complement = complement;
        this.tf_cep = cep;
        this.tf_country = country;
        this.tf_description = description;
    }

    public void executeRegister() {
        try {
            User user = new User(id_Usuario);
            
            Address address = new Address(
            	tf_description.getText(),
            	tf_cep.getText(),
            	tf_country.getText(),
            	tf_state.getText(),
            	tf_city.getText(),
            	tf_neighborhood.getText(),
                tf_street.getText(),
                Integer.parseInt(tf_number.getText()),
                tf_complement.getText(),
                user
            );

            if (address.create() > 0) {
                JOptionPane.showMessageDialog(null, "Endereço adicionado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao adicionar o endereço.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
    
    public void executeUpdate() {
    	try {
            Address address = new Address(this.id_Address);
            address.setStreet(tf_street.getText());
            address.setNumber(Integer.parseInt(tf_number.getText()));
            address.setCity(tf_city.getText());
            address.setState(tf_state.getText());
            address.setNeighborhood(tf_neighborhood.getText());
            address.setComplement(tf_complement.getText());
            address.setCep(tf_cep.getText());
            address.setCountry(tf_country.getText());
            address.setDescription(tf_description.getText());

            address.update();
            JOptionPane.showMessageDialog(null, "Endereço alterado com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> listAddresses() {
        List<String> addresses = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = """
                SELECT e.id_Endereco, e.descricao, e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep
                FROM endereco e
                JOIN usuario u ON e.id_Usuario = u.id_Usuario
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Endereco");
                String description = rs.getString("descricao");
                String street = rs.getString("rua");
                String number = rs.getString("numero");
                String neighborhood = rs.getString("bairro");
                String city = rs.getString("cidade");
                String state = rs.getString("estado");
                String cep = rs.getString("cep");
                addresses.add("[" + id + "] " + description + ": " + street + " n° " + number + ", " + neighborhood + " - " + city + " / " + state + " - " + cep);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public void executeDelete() {
    	try {
            Address address = new Address(this.id_Address);
            address.delete();
            JOptionPane.showMessageDialog(null, "Endereço removido com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}