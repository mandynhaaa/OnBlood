package View.Request;

import javax.swing.*;
import Connection.ConnectionSQL;
import Controller.RequestController;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EditRequest extends JFrame {
    private JComboBox<String> comboStatus, comboBloodType;
    private JTextField txtVolume, txtDataHora;
    private JButton btnSalvar, btnCancelar;
    private int idRequest;
    private RequestController controller;

    public EditRequest(int idRequest) {
        this.idRequest = idRequest;

        setTitle("Editar Solicitação");
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

        comboStatus = new JComboBox<>(new String[]{"Pendente", "Realizada", "Cancelada"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(comboStatus, gbc);

        JLabel lblBloodType = new JLabel("Tipo Sanguíneo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblBloodType, gbc);

        comboBloodType = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(comboBloodType, gbc);

        JLabel lblVolume = new JLabel("Volume (mL):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblVolume, gbc);

        txtVolume = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtVolume, gbc);

        JLabel lblDataHora = new JLabel("Data e Hora");
        lblDataHora.setToolTipText("Formato: dd/MM/aaa HH:mm:ss");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblDataHora, gbc);

        txtDataHora = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(txtDataHora, gbc);

        btnSalvar = new JButton("Salvar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(btnSalvar, gbc);

        btnCancelar = new JButton("Cancelar");
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(btnCancelar, gbc);

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());
        
        controller = new RequestController(idRequest, 0, txtVolume, txtDataHora, comboBloodType, comboStatus);
        
        List<String> tiposSanguineos = controller.listarTiposSanguineos();
        if (tiposSanguineos.isEmpty()) {
        	comboBloodType.addItem("Nenhum cadastrado");
        	comboBloodType.setEnabled(false);
        } else {
            for (String doador : tiposSanguineos) {
            	comboBloodType.addItem(doador);
            }
        }

        carregarDados();
    }

    private void carregarDados() {
        try {
            Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT status, volume, data_Hora, id_Tipo_Sanguineo FROM solicitacao WHERE id_Solicitacao = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idRequest);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                comboStatus.setSelectedItem(rs.getString("status"));
                txtVolume.setText(String.valueOf(rs.getFloat("volume")));
                
                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                LocalDateTime dataHoraConvertida = LocalDateTime.parse(rs.getString("data_Hora"), formatoEntrada);
                String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);
                
                txtDataHora.setText(dataHoraFormatada);
                
                String tipoSanguineo = rs.getString("id_Tipo_Sanguineo");
                for (int i = 0; i < comboBloodType.getItemCount(); i++) {
                    String item = comboBloodType.getItemAt(i);
                    if (item.contains("[" + tipoSanguineo + "]")) {
                        comboBloodType.setSelectedIndex(i);
                        break;
                    }
                }
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados da solicitação.");
        }
    }

    private void salvarAlteracoes() {
        controller.executeUpdate();
        dispose();
    }
}
