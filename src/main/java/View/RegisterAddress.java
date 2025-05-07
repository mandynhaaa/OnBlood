package View;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import Controller.AddressController;
import Standard.OnlyNumbersDocumentFilter;

import java.awt.*;
import java.util.List;

public class RegisterAddress extends JFrame {
    private JComboBox<String> cbUsuarios;
    private JTextField tfRua, tfNumero, tfCidade, tfEstado;
    private JTextField tfBairro, tfComplemento, tfCep, tfPais, tfDescricao;
    private AddressController controller;

    public RegisterAddress() {
        setTitle("Cadastro de Endereço");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 530);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setBounds(30, 20, 100, 20);
        add(lblUsuario);

        cbUsuarios = new JComboBox<>();
        cbUsuarios.setBounds(130, 20, 220, 22);
        add(cbUsuarios);

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setBounds(30, 60, 100, 20);
        add(lblDescricao);
        tfDescricao = new JTextField();
        tfDescricao.setBounds(130, 60, 220, 22);
        add(tfDescricao);

        JLabel lblCep = new JLabel("CEP:");
        lblCep.setBounds(30, 90, 100, 20);
        add(lblCep);
        tfCep = new JTextField();
        tfCep.setBounds(130, 90, 220, 22);
        add(tfCep);

        JLabel lblPais = new JLabel("País:");
        lblPais.setBounds(30, 120, 100, 20);
        add(lblPais);
        tfPais = new JTextField();
        tfPais.setBounds(130, 120, 220, 22);
        add(tfPais);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(30, 150, 100, 20);
        add(lblEstado);
        tfEstado = new JTextField();
        tfEstado.setBounds(130, 150, 220, 22);
        add(tfEstado);

        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setBounds(30, 180, 100, 20);
        add(lblCidade);
        tfCidade = new JTextField();
        tfCidade.setBounds(130, 180, 220, 22);
        add(tfCidade);

        JLabel lblBairro = new JLabel("Bairro:");
        lblBairro.setBounds(30, 210, 100, 20);
        add(lblBairro);
        tfBairro = new JTextField();
        tfBairro.setBounds(130, 210, 220, 22);
        add(tfBairro);

        JLabel lblRua = new JLabel("Rua:");
        lblRua.setBounds(30, 240, 100, 20);
        add(lblRua);
        tfRua = new JTextField();
        tfRua.setBounds(130, 240, 220, 22);
        add(tfRua);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(30, 270, 100, 20);
        add(lblNumero);

        tfNumero = new JTextField();
        tfNumero.setBounds(130, 270, 220, 22);
        add(tfNumero);
        
        ((AbstractDocument) tfNumero.getDocument()).setDocumentFilter(new OnlyNumbersDocumentFilter());

        JLabel lblComplemento = new JLabel("Complemento:");
        lblComplemento.setBounds(30, 300, 100, 20);
        add(lblComplemento);
        tfComplemento = new JTextField();
        tfComplemento.setBounds(130, 300, 220, 22);
        add(tfComplemento);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(130, 350, 90, 25);
        add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(230, 350, 90, 25);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        controller = new AddressController(0,
            tfRua, tfNumero, tfCidade, tfEstado,
            tfBairro, tfComplemento, tfCep,
            tfPais, tfDescricao, cbUsuarios
        );

        List<String> usuarios = controller.listUsers();
        if (usuarios.isEmpty()) {
            cbUsuarios.addItem("Nenhum cadastrado");
            cbUsuarios.setEnabled(false);
        } else {
            for (String u : usuarios) cbUsuarios.addItem(u);
        }
    }

    private void salvar() {
        controller.executeRegister();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterAddress().setVisible(true));
    }
}
