package Controller;

import Main.Address;
import Main.User;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddressController {
	
    private ObjectId id_User;
    private ObjectId id_Address;
    private JTextField tf_street;
    private JTextField tf_number;
    private JTextField tf_city;
    private JTextField tf_state;
    private JTextField tf_neighborhood;
    private JTextField tf_complement;
    private JFormattedTextField tf_cep; // Alterado para JFormattedTextField
    private JTextField tf_country;
    private JTextField tf_description;

    public AddressController(ObjectId idUsuario) {
        this.id_User = idUsuario;
    }

    public AddressController(
		ObjectId id_User, 
		JTextField tf_description, 
		JFormattedTextField tf_cep, // Alterado para JFormattedTextField
		JTextField tf_country, 
		JTextField tf_state, 
		JTextField tf_city, 
		JTextField tf_neighborhood, 
		JTextField tf_street, 
		JTextField tf_number, 
		JTextField tf_complement
    ) {
        this(id_User); 
        this.tf_description = tf_description;
        this.tf_cep = tf_cep;
        this.tf_country = tf_country;
        this.tf_state = tf_state;
        this.tf_city = tf_city;
        this.tf_neighborhood = tf_neighborhood;
        this.tf_street = tf_street;
        this.tf_number = tf_number;
        this.tf_complement = tf_complement;
    }

    public AddressController(
		ObjectId id_User, 
		ObjectId id_Address, 
		JTextField tf_description, 
		JFormattedTextField tf_cep,
		JTextField tf_country, 
		JTextField tf_state, 
		JTextField tf_city, 
		JTextField tf_neighborhood, 
		JTextField tf_street, 
		JTextField tf_number, 
		JTextField tf_complement
    ) {
        this(id_User, tf_description, tf_cep, tf_country, tf_state, tf_city, tf_neighborhood, tf_street, tf_number, tf_complement);
        this.id_Address = id_Address;
    }
    
    private void fillAddressFromFields(Address address) throws NumberFormatException {
        address.setDescription(tf_description.getText());
        address.setCep(tf_cep.getText().replaceAll("[^0-9]", "")); 
        address.setCountry(tf_country.getText());
        address.setState(tf_state.getText());
        address.setCity(tf_city.getText());
        address.setNeighborhood(tf_neighborhood.getText());
        address.setStreet(tf_street.getText());
        address.setNumber(Integer.parseInt(tf_number.getText()));
        address.setComplement(tf_complement.getText());
    }

    public void executeRegister() {
        User user = new User(this.id_User);
        if (user.getId() == null) {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado para adicionar endereço.");
            return;
        }

        Address address = new Address();
        try {
            fillAddressFromFields(address);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "O campo 'número' deve ser um valor numérico válido.");
            return;
        }
        
        user.getAddresses().add(address);
        
        user.update(); 
        
        JOptionPane.showMessageDialog(null, "Endereço adicionado com sucesso!");
    }

    public void executeUpdate() {
        if (id_Address == null) {
            JOptionPane.showMessageDialog(null, "Nenhum endereço selecionado para atualização.");
            return;
        }
        
        User user = new User(this.id_User);
        Optional<Address> addressOpt = user.getAddresses().stream()
                                           .filter(a -> a.getId().equals(this.id_Address))
                                           .findFirst();
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            try {
                fillAddressFromFields(address);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "O campo 'número' deve ser um valor numérico válido.");
                return;
            }
            user.update();
            JOptionPane.showMessageDialog(null, "Endereço alterado com sucesso!");
            return;
        }
        JOptionPane.showMessageDialog(null, "Endereço não encontrado para atualização.");
    }

    public void executeDelete(ObjectId idAddresToDelete) {
        User user = new User(this.id_User);
        if (user.getAddresses() == null) {
            JOptionPane.showMessageDialog(null, "Usuário não possui endereços para excluir.");
            return;
        }
        boolean removed = user.getAddresses().removeIf(a -> a.getId().equals(idAddresToDelete));
        if (removed) {
            user.update();
            JOptionPane.showMessageDialog(null, "Endereço removido com sucesso!");
            return;
        }
        JOptionPane.showMessageDialog(null, "Endereço não encontrado para exclusão.");
    }
    
    public List<String> listAddresses(ObjectId id_User) {
        User user = new User(id_User);
        if (user.getId() == null || user.getAddresses() == null) {
            return new ArrayList<>();
        }
        
        return user.getAddresses().stream()
                .map(a -> String.format("[%s] %s: %s, %d - %s / %s", 
                        a.getId().toHexString(), 
                        a.getDescription(), 
                        a.getStreet(), 
                        a.getNumber(), 
                        a.getCity(),
                        a.getState()))
                .collect(Collectors.toList());
    }
}