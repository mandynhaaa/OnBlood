package View;

import javax.swing.*;

import Connection.ConnectionSQL;
import Controller.DonationController;
import java.awt.*;
import java.sql.*;

public class EditDonation extends JFrame {
    private JComboBox<String> comboStatus;
    private JTextField txtVolume, txtDataHora;
    private JButton btnSalvar, btnCancelar;
    private int idDoacao;
    private DonationController controller;
    private ManagerDonation parent;

    public EditDonation(int idDoacao, DonationController controller, ManagerDonation parent) {
        this.idDoacao = idDoacao;
        this.controller = controller;
        this.parent = parent;

        setTitle("Editar Doação");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblStatus = new JLabel("Status:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblStatus, gbc);

        comboStatus = new JComboBox<>(new String[] {"Pendente", "Concluído", "Cancelado"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(comboStatus, gbc);

        JLabel lblVolume = new JLabel("Volume (mL):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblVolume, gbc);

        txtVolume = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtVolume, gbc);

        JLabel lblDataHora = new JLabel("Data e Hora (yyyy-MM-dd HH:mm):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblDataHora, gbc);

        txtDataHora = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtDataHora, gbc);

        btnSalvar = new JButton("Salvar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(btnSalvar, gbc);

        btnCancelar = new JButton("Cancelar");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(btnCancelar, gbc);

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());

        carregarDados();
    }

    private void carregarDados() {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT status, volume, data_hora FROM Doacao WHERE id_doacao = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idDoacao);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                comboStatus.setSelectedItem(rs.getString("status"));
                txtVolume.setText(String.valueOf(rs.getInt("volume")));
                txtDataHora.setText(rs.getString("data_hora"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados da doação.");
        }
    }

    private void salvarAlteracoes() {
        try {
            String status = (String) comboStatus.getSelectedItem();
            int volume = Integer.parseInt(txtVolume.getText());
            String dataHora = txtDataHora.getText();

            boolean sucesso = controller.atualizarDoacao(idDoacao, status, volume, dataHora);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Doação atualizada com sucesso.");
                parent.carregarDoacoes();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar doação.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Volume inválido.");
        }
    }
}
