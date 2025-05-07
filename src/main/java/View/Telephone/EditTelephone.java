package View.Telephone;

import javax.swing.*;
import Controller.TelephoneController;
import Connection.ConnectionSQL;

import java.sql.*;

public class EditTelephone extends JFrame {
    private JTextField tfDescricao, tfDdd, tfNumero;
    private JButton btnSalvar, btnCancelar;
    private int id_Telefone;
    private TelephoneController controller;
    private ManagerTelephone parent;

    public EditTelephone(int id_Telefone, ManagerTelephone parent) {
        this.id_Telefone = id_Telefone;
        this.parent = parent;

        setTitle("Editar Telefone");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        int y = 30;
        tfDescricao = createField("Descrição:", y);
        tfDdd = createField("DDD:", y += 50);
        tfNumero = createField("Número:", y += 50);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(130, y + 60, 90, 25);
        add(btnSalvar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(230, y + 60, 90, 25);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        controller = new TelephoneController(this.id_Telefone, tfDescricao, tfDdd, tfNumero, null);

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
            String sql = "SELECT descricao, ddd, numero FROM telefone WHERE id_Telefone = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_Telefone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfDescricao.setText(rs.getString("descricao"));
                tfDdd.setText(rs.getString("ddd"));
                tfNumero.setText(rs.getString("numero"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do telefone.");
        }
    }

    private void salvar() {
        controller.executeUpdate();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerTelephone parent = new ManagerTelephone();
            EditTelephone editor = new EditTelephone(1, parent);
            editor.setVisible(true);
        });
    }
}
