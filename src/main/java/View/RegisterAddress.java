package View;

import javax.swing.*;
import Controller.AddressController;
import java.awt.*;
import java.util.List;

public class RegisterAddress extends JFrame {
    private JComboBox<String> cbUsuarios;
    private JTextField tfRua, tfNumero, tfCidade, tfEstado;
    private AddressController controller;

    public RegisterAddress() {
        controller = new AddressController();
        setTitle("Cadastro de Endereço");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 320);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setBounds(30, 30, 100, 20);
        add(lblUsuario);

        cbUsuarios = new JComboBox<>();
        cbUsuarios.setBounds(120, 30, 220, 22);
        List<String> usuarios = controller.listarUsuarios();
        if (usuarios.isEmpty()) {
            cbUsuarios.addItem("Nenhum cadastrado");
            cbUsuarios.setEnabled(false);
        } else {
            for (String u : usuarios) cbUsuarios.addItem(u);
        }
        add(cbUsuarios);

        JLabel lblRua = new JLabel("Rua:");
        lblRua.setBounds(30, 70, 100, 20);
        add(lblRua);

        tfRua = new JTextField();
        tfRua.setBounds(120, 70, 220, 22);
        add(tfRua);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(30, 110, 100, 20);
        add(lblNumero);

        tfNumero = new JTextField();
        tfNumero.setBounds(120, 110, 220, 22);
        add(tfNumero);

        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setBounds(30, 150, 100, 20);
        add(lblCidade);

        tfCidade = new JTextField();
        tfCidade.setBounds(120, 150, 220, 22);
        add(tfCidade);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(30, 190, 100, 20);
        add(lblEstado);

        tfEstado = new JTextField();
        tfEstado.setBounds(120, 190, 220, 22);
        add(tfEstado);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(120, 230, 90, 25);
        btnSalvar.addActionListener(e -> salvar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 230, 90, 25);
        btnCancelar.addActionListener(e -> dispose());

        add(btnSalvar);
        add(btnCancelar);
    }

    private void salvar() {
        try {
            String selecionado = cbUsuarios.getSelectedItem().toString();
            int idUsuario = Integer.parseInt(selecionado.substring(1, selecionado.indexOf("]")));
            String rua = tfRua.getText();
            String numero = tfNumero.getText();
            String cidade = tfCidade.getText();
            String estado = tfEstado.getText();

            boolean sucesso = controller.cadastrarEndereco(idUsuario, rua, numero, cidade, estado);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Endereço cadastrado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar endereço.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                RegisterAddress frame = new RegisterAddress();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
