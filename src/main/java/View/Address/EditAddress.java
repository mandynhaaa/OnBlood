package View.Address;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import Controller.AddressController;
import Standard.OnlyNumbersDocumentFilter;

import java.awt.*;
import java.sql.*;

import Connection.ConnectionSQL;

public class EditAddress extends JFrame {
    private JTextField tfRua, tfNumero, tfCidade, tfEstado, tfBairro, tfComplemento, tfCep, tfPais, tfDescricao;
    private JButton btnSalvar, btnCancelar;
    private int idEndereco;
    private AddressController controller;

    public EditAddress(int idEndereco) {
        this.idEndereco = idEndereco;

        setTitle("Editar Endereço");
        setSize(450, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        int y = 30;
        tfDescricao = createField("Descrição:", y);
        tfCep = createField("CEP:", y += 40);
        tfPais = createField("País:", y += 40);
        tfEstado = createField("Estado:", y += 40);
        tfCidade = createField("Cidade:", y += 40);
        tfBairro = createField("Bairro:", y += 40);
        tfRua = createField("Rua:", y += 40);
        tfNumero = createField("Número:", y += 40);
        tfComplemento = createField("Complemento:", y += 40);
        
        ((AbstractDocument) tfNumero.getDocument()).setDocumentFilter(new OnlyNumbersDocumentFilter());

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(100, y + 50, 100, 25);
        add(btnSalvar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(220, y + 50, 100, 25);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        controller = new AddressController(this.idEndereco, 0, tfRua, tfNumero, tfCidade, tfEstado, tfBairro, tfComplemento, tfCep, tfPais, tfDescricao);

        carregarDados();
    }

    private JTextField createField(String label, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(30, y, 100, 20);
        add(lbl);
        JTextField tf = new JTextField();
        tf.setBounds(130, y, 250, 22);
        add(tf);
        return tf;
    }

    private void carregarDados() {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT descricao, cep, pais, estado, cidade, bairro, rua, numero, complemento FROM endereco WHERE id_Endereco = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEndereco);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfDescricao.setText(rs.getString("descricao"));
                tfCep.setText(rs.getString("cep"));
                tfPais.setText(rs.getString("pais"));
                tfEstado.setText(rs.getString("estado"));
                tfCidade.setText(rs.getString("cidade"));
                tfBairro.setText(rs.getString("bairro"));
                tfRua.setText(rs.getString("rua"));
                tfNumero.setText(rs.getString("numero"));
                tfComplemento.setText(rs.getString("complemento"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do endereço.");
        }
    }
    
    private void salvar() {
        controller.executeUpdate();
        dispose();
    }

}
