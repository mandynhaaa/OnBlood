package View.Address;

import Controller.AddressController;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.awt.*;

public class RegisterAddress extends JFrame {
    private JTextField tfRua, tfNumero, tfCidade, tfEstado, tfBairro, tfComplemento, tfCep, tfPais, tfDescricao;
    private AddressController controller;
    private ObjectId idUsuario;
    private ManagerAddress parentView; 

    public RegisterAddress(ObjectId idUsuario, ManagerAddress parentView) {
        this.idUsuario = idUsuario;
        this.parentView = parentView;

        setTitle("Cadastro de Endereço");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        setLocationRelativeTo(parentView); 
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

        controller = new AddressController(this.idUsuario, tfDescricao, tfCep, tfPais, tfEstado, tfCidade, tfBairro, tfRua, tfNumero, tfComplemento);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvar() {
        controller.executeRegister();
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