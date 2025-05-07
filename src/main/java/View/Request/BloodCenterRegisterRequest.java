package View.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

import Controller.RequestController;

import java.util.List;

public class BloodCenterRegisterRequest extends JFrame {

    private JPanel contentPane;
    private JComboBox<String> cbTipoSanguineo;
    private JTextField tfVolume;
    private JTextField tfDataHora;
    private JComboBox<String> cbStatus;
    private RequestController controller;

    public BloodCenterRegisterRequest(int idUsuario) {
        controller = new RequestController();

        setTitle("Cadastro de Solicitações");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTipo = new JLabel("Tipo Sanguíneo:");
        lblTipo.setBounds(30, 30, 100, 20);
        contentPane.add(lblTipo);

        cbTipoSanguineo = new JComboBox<>();
        cbTipoSanguineo.setBounds(140, 30, 180, 22);
        List<String> tipos = controller.listarTiposSanguineos();
        for (String tipo : tipos) {
            cbTipoSanguineo.addItem(tipo);
        }
        contentPane.add(cbTipoSanguineo);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 70, 80, 20);
        contentPane.add(lblStatus);

        cbStatus = new JComboBox<>();
        cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Pendente", "Realizada", "Cancelada"}));
        cbStatus.setBounds(140, 70, 180, 22);
        contentPane.add(cbStatus);

        JLabel lblVolume = new JLabel("Volume (mL):");
        lblVolume.setBounds(30, 110, 100, 20);
        contentPane.add(lblVolume);

        tfVolume = new JTextField();
        tfVolume.setBounds(140, 110, 180, 22);
        contentPane.add(tfVolume);

        JLabel lblDataHora = new JLabel("Data e Hora:");
        lblDataHora.setBounds(30, 150, 100, 20);
        contentPane.add(lblDataHora);

        tfDataHora = new JTextField();
        tfDataHora.setBounds(140, 150, 180, 22);
        tfDataHora.setToolTipText("Formato: YYYY-MM-DD HH:MM");
        contentPane.add(tfDataHora);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(100, 240, 90, 25);
        btnSalvar.addActionListener(e -> {
            try {
                String tipoSelecionado = cbTipoSanguineo.getSelectedItem().toString();
                int idTipoSanguineo = Integer.parseInt(tipoSelecionado.substring(1, tipoSelecionado.indexOf(']')));

                String hemocentro = controller.buscarHemocentroPorID(idUsuario);
                String status = cbStatus.getSelectedItem().toString();
                int volume = Integer.parseInt(tfVolume.getText());
                String dataHora = tfDataHora.getText();

                boolean sucesso = controller.cadastrarSolicitacao(hemocentro, idTipoSanguineo, status, volume, dataHora);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Solicitação cadastrada com sucesso!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar solicitação.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            }
        });
        contentPane.add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(210, 240, 90, 25);
        btnCancelar.addActionListener(e -> {
            dispose();
            ManagerRequest tela = new ManagerRequest();
            tela.setVisible(true);
        });
        contentPane.add(btnCancelar);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BloodCenterRegisterRequest frame = new BloodCenterRegisterRequest(1); // exemplo de ID
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
