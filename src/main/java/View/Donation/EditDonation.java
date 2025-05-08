package View.Donation;

import javax.swing.*;

import Connection.ConnectionSQL;
import Controller.DonationController;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditDonation extends JFrame {
    private JComboBox<String> comboStatus;
    private JTextField txtVolume, txtDataHora;
    private JButton btnSalvar, btnCancelar;
    private int idDonation;
    private DonationController controller;

    public EditDonation(int idDonation) {
        this.idDonation = idDonation;

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

        comboStatus = new JComboBox<>(new String[] {"Pendente", "Realizada", "Cancelada"});
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

        JLabel lblDataHora = new JLabel("Data e Hora");
        lblDataHora.setToolTipText("Formato: dd/MM/aaa HH:mm:ss");
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
        
        controller = new DonationController(idDonation, 0, 0, txtVolume, txtDataHora, comboStatus, null);

        carregarDados();
    }

    private void carregarDados() {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT status, volume, data_hora FROM Doacao WHERE id_doacao = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idDonation);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                comboStatus.setSelectedItem(rs.getString("status"));
                txtVolume.setText(String.valueOf(rs.getFloat("volume")));
                
                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                LocalDateTime dataHoraConvertida = LocalDateTime.parse(rs.getString("data_hora"), formatoEntrada);
                String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);
                
                txtDataHora.setText(dataHoraFormatada);
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
        controller.executeUpdate();
        dispose();
    }
}
