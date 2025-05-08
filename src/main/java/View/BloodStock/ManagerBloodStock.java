package View.BloodStock;

import Controller.BloodStockController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagerBloodStock extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private BloodStockController controller;
    private int idUsuario;

    public ManagerBloodStock(int idUser) {
    	this.idUsuario = idUser;
    	
        controller = new BloodStockController(idUser);

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

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarEstoque());
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarEstoque();
    }

    private void carregarEstoque() {
        modelo.setRowCount(0);
        try {
            ResultSet rs = controller.listarEstoqueHemocentro();
            while (rs != null && rs.next()) {
                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                LocalDateTime dataHoraConvertida = LocalDateTime.parse(rs.getString("data_atualizacao"), formatoEntrada);
                String dataFormatada = dataHoraConvertida.format(formatoSaida);
                
                modelo.addRow(new Object[] {
                        rs.getInt("id_estoque"),
                        rs.getString("razao_social"),
                        rs.getString("tipo_sanguineo"),
                        rs.getFloat("volume"),
                        dataFormatada
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar estoque: " + e.getMessage());
        }
    }

}
