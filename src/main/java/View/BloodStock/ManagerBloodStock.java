package View.BloodStock;

import Controller.BloodStockController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerBloodStock extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private BloodStockController controller;

    public ManagerBloodStock() {
        controller = new BloodStockController();

        setTitle("Visualizar Estoque");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[] {
                "ID Estoque", "Hemocentro", "Tipo Sanguíneo", "Volume (mL)", "Última Atualização"
        });

        tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAtualizar = new JButton("Atualizar Estoque");
        btnAtualizar.addActionListener(e -> carregarEstoque());
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarEstoque();
    }

    private void carregarEstoque() {
        modelo.setRowCount(0);
        try {
            ResultSet rs = controller.listarEstoque();
            while (rs != null && rs.next()) {
                modelo.addRow(new Object[] {
                        rs.getInt("id_estoque"),
                        rs.getString("razao_social"),
                        rs.getString("tipo_sanguineo"),
                        rs.getInt("volume"),
                        rs.getString("data_atualizacao")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar estoque: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerBloodStock().setVisible(true));
    }
}
