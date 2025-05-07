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



public class AddressController implements ActionListener {
	
	private int idAddress;
    private JTextField tf_street;
    private JTextField tf_number;
    private JTextField tf_city;
    private JTextField tf_state;
    private JTextField tf_neighborhood;
    private JTextField tf_complement;
    private JTextField tf_cep;
    private JTextField tf_country;
    private JTextField tf_description;
    private JComboBox<String> cb_users;

    public AddressController(
        int idAddress, JTextField street, JTextField number, JTextField city, JTextField state,
        JTextField neighborhood, JTextField complement, JTextField cep,
        JTextField country, JTextField description, JComboBox<String> users
    ) {
    	this.idAddress = idAddress;
        this.tf_street = street;
        this.tf_number = number;
        this.tf_city = city;
        this.tf_state = state;
        this.tf_neighborhood = neighborhood;
        this.tf_complement = complement;
        this.tf_cep = cep;
        this.tf_country = country;
        this.tf_description = description;
        this.cb_users = users;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Adicionar".equals(e.getActionCommand())) {
            executeRegister();
        }
    }

    public void executeRegister() {
        try {
            int userId = Integer.parseInt(cb_users.getSelectedItem().toString().replaceAll("[^0-9]", ""));
            
            User user = new User(userId);
            
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
            Address address = new Address(this.idAddress);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> listAddresses() {
        List<String> addresses = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = """
                SELECT e.id_Endereco, u.nome, e.rua, e.numero, e.cidade, e.estado
                FROM endereco e
                JOIN usuario u ON e.id_Usuario = u.id_Usuario
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Endereco");
                String userName = rs.getString("nome");
                String street = rs.getString("rua");
                String number = rs.getString("numero");
                String city = rs.getString("cidade");
                String state = rs.getString("estado");
                addresses.add("[" + id + "] " + userName + ": " + street + ", " + number + " - " + city + "/" + state);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public void deleteAddress() {
    	try {
            Address address = new Address(this.idAddress);
            address.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> listUsers() {
        List<String> users = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT id_Usuario, nome FROM usuario";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Usuario");
                String name = rs.getString("nome");
                users.add("[" + id + "] " + name);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

}