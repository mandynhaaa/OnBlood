package View;

import javax.swing.*;
import Controller.AddressController;
import java.awt.*;
import java.sql.*;

import Connection.ConnectionSQL;

public class EditAddress extends JFrame {
    private JTextField tfRua, tfNumero, tfCidade, tfEstado;
    private JButton btnSalvar, btnCancelar;
    private int idEndereco;
    private AddressController controller;
    private ManagerAddress parent;

    public EditAddress(int idEndereco, AddressController controller, ManagerAddress parent) {
        this.idEndereco = idEndereco;
        this.controller = controller;
        this.parent = parent;

        setTitle("Editar Endereço");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblRua = new JLabel("Rua:");
        lblRua.setBounds(30, 30, 100, 20);
        add(lblRua);

        tfRua = new JTextField();
        tfRua.setBounds(130, 30, 200, 22);
        add(tfRua);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(30, 70, 100, 20);
        add(lblNumero);

        tfNumero = new JTextField();
        tfNumero.setBounds(130, 70, 200, 22);
        add(tfNumero);

        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setBounds(30, 110, 100, 20);
        add(lblCidade);

        tfCidade = new JTextField();
        tfCidade.setBounds(130, 110, 200, 22);
        add(tfCidade);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(30, 150, 100, 20);
        add(lblEstado);

        tfEstado = new JTextField();
        tfEstado.setBounds(130, 150, 200, 22);
        add(tfEstado);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(80, 200, 100, 25);
        add(btnSalvar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 200, 100, 25);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        carregarDados();
    }

    private void carregarDados() {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT rua, numero, cidade, estado FROM endereco WHERE id_Endereco = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEndereco);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfRua.setText(rs.getString("rua"));
                tfNumero.setText(rs.getString("numero"));
                tfCidade.setText(rs.getString("cidade"));
                tfEstado.setText(rs.getString("estado"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do endereço.");
        }
    }

    private void salvar() {
        String rua = tfRua.getText();
        String numero = tfNumero.getText();
        String cidade = tfCidade.getText();
        String estado = tfEstado.getText();

        boolean sucesso = controller.atualizarEndereco(idEndereco, rua, numero, cidade, estado);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Endereço atualizado com sucesso.");
            parent.carregarEnderecos();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar endereço.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddressController ctrl = new AddressController();
            ManagerAddress parent = new ManagerAddress();
            EditAddress editor = new EditAddress(1, ctrl, parent);
            editor.setVisible(true);
        });
    }
}
