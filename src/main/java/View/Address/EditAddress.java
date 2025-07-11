package View.Address;

import Controller.AddressController;
import Main.User;
import Main.Address;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.awt.*;

public class EditAddress extends JFrame {
    private JTextField tfRua, tfNumero, tfCidade, tfEstado, tfBairro, tfComplemento, tfCep, tfPais, tfDescricao;
    private AddressController controller;
    private ObjectId idUsuario;
    private ObjectId idEndereco;
    private ManagerAddress parentView;

    public EditAddress(ObjectId idUsuario, ObjectId idEndereco, ManagerAddress parentView) {
        this.idUsuario = idUsuario;
        this.idEndereco = idEndereco;
        this.parentView = parentView;

        setTitle("Editar Endereço");
        setSize(450, 450);
        setLocationRelativeTo(parentView);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        tfDescricao = createField("Descrição:", y++, gbc);
        tfCep = createField("CEP:", y++, gbc);
        tfPais = createField("País:", y++, gbc);
        tfEstado = createField("Estado:", y++, gbc);
        tfCidade = createField("Cidade:", y++, gbc);
        tfBairro = createField("Bairro:", y++, gbc);
        tfRua = createField("Rua:", y++, gbc);
        tfNumero = createField("Número:", y++, gbc);
        tfComplemento = createField("Complemento:", y++, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        controller = new AddressController(idUsuario, idEndereco, tfDescricao, tfCep, tfPais, tfEstado, tfCidade, tfBairro, tfRua, tfNumero, tfComplemento);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        loadData();
    }

	private void loadData() {
        User user = new User(idUsuario);
        user.getAddresses().stream()
            .filter(a -> a.getId().equals(idEndereco))
            .findFirst()
            .ifPresent(address -> {
                tfDescricao.setText(address.getDescription());
                tfCep.setText(address.getCep());
                tfPais.setText(address.getCountry());
                tfEstado.setText(address.getState());
                tfCidade.setText(address.getCity());
                tfBairro.setText(address.getNeighborhood());
                tfRua.setText(address.getStreet());
                tfNumero.setText(String.valueOf(address.getNumber()));
                tfComplemento.setText(address.getComplement());
            });
    }
    
    private void salvar() {
        controller.executeUpdate();
        parentView.carregarEnderecos();
        dispose();
    }
    
    private JTextField createField(String label, int y, GridBagConstraints gbc) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        add(new JLabel(label), gbc);
        JTextField tf = new JTextField(20);
        gbc.gridx = 1;
        add(tf, gbc);
        return tf;
    }
}