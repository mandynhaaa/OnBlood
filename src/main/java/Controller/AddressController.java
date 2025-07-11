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

    private ObjectId idUsuario;
    private ObjectId idEndereco;

    private JTextField tf_street;
    private JTextField tf_number;
    private JTextField tf_city;
    private JTextField tf_state;
    private JTextField tf_neighborhood;
    private JTextField tf_complement;
    private JTextField tf_cep;
    private JTextField tf_country;
    private JTextField tf_description;

    public AddressController(ObjectId idUsuario) {
        this.idUsuario = idUsuario;
    }

    public AddressController(ObjectId idUsuario, JTextField tf_description, JTextField tf_cep, JTextField tf_country, 
                             JTextField tf_state, JTextField tf_city, JTextField tf_neighborhood, 
                             JTextField tf_street, JTextField tf_number, JTextField tf_complement) {
        this(idUsuario); 
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

    public AddressController(ObjectId idUsuario, ObjectId idEndereco, JTextField tf_description, JTextField tf_cep, 
                             JTextField tf_country, JTextField tf_state, JTextField tf_city, 
                             JTextField tf_neighborhood, JTextField tf_street, JTextField tf_number, JTextField tf_complement) {
        this(idUsuario, tf_description, tf_cep, tf_country, tf_state, tf_city, tf_neighborhood, tf_street, tf_number, tf_complement);
        this.idEndereco = idEndereco;
    }
    
    private void fillAddressFromFields(Address address) throws NumberFormatException {
        address.setDescription(tf_description.getText());
        address.setCep(tf_cep.getText());
        address.setCountry(tf_country.getText());
        address.setState(tf_state.getText());
        address.setCity(tf_city.getText());
        address.setNeighborhood(tf_neighborhood.getText());
        address.setStreet(tf_street.getText());
        address.setNumber(Integer.parseInt(tf_number.getText()));
        address.setComplement(tf_complement.getText());
    }

    public void executeRegister() {
        User user = new User(this.idUsuario);
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
        if (idEndereco == null) {
            JOptionPane.showMessageDialog(null, "Nenhum endereço selecionado para atualização.");
            return;
        }
        
        User user = new User(this.idUsuario);
        Optional<Address> addressOpt = user.getAddresses().stream()
                                           .filter(a -> a.getId().equals(this.idEndereco))
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
        } else {
            JOptionPane.showMessageDialog(null, "Endereço não encontrado para atualização.");
        }
    }

    public void executeDelete(ObjectId idEnderecoToDelete) {
        User user = new User(this.idUsuario);
        boolean removed = user.getAddresses().removeIf(a -> a.getId().equals(idEnderecoToDelete));

        if (removed) {
            user.update();
            JOptionPane.showMessageDialog(null, "Endereço removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Endereço não encontrado para exclusão.");
        }
    }
    
    public List<String> listAddresses(ObjectId idUsuario) {
        User user = new User(idUsuario);
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