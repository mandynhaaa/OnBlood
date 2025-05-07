package View.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controller.RequestController;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RegisterRequest extends JFrame {

    private JPanel contentPane;
    private JComboBox<String> cbHemocentro;
    private JComboBox<String> cbTipoSanguineo;
    private JTextField tfVolume;
    private JTextField tfDataHora;
    private JComboBox<String> cbStatus;
    private RequestController controller;

    public RegisterRequest() {
        controller = new RequestController();

        setTitle("Cadastro de Solicitação");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblHemocentro = new JLabel("Hemocentro:");
        lblHemocentro.setBounds(30, 30, 90, 20);
        contentPane.add(lblHemocentro);

        cbHemocentro = new JComboBox<>();
        cbHemocentro.setBounds(130, 30, 200, 22);
        List<String> hemocentros = controller.listarHemocentros();
        if (hemocentros.isEmpty()) {
            cbHemocentro.addItem("Nenhum cadastrado");
            cbHemocentro.setEnabled(false);
        } else {
            for (String h : hemocentros) {
                cbHemocentro.addItem(h);
            }
        }
        contentPane.add(cbHemocentro);

        JLabel lblTipo = new JLabel("Tipo Sanguíneo:");
        lblTipo.setBounds(30, 70, 100, 20);
        contentPane.add(lblTipo);

        cbTipoSanguineo = new JComboBox<>();
        cbTipoSanguineo.setBounds(130, 70, 200, 22);
        List<String> tiposSangue = controller.listarTiposSanguineos();
        if (tiposSangue.isEmpty()) {
            cbTipoSanguineo.addItem("Nenhum cadastrado");
            cbTipoSanguineo.setEnabled(false);
        } else {
            for (String tipo : tiposSangue) {
                cbTipoSanguineo.addItem(tipo);
            }
        }
        contentPane.add(cbTipoSanguineo);

        JLabel lblVolume = new JLabel("Volume (mL):");
        lblVolume.setBounds(30, 110, 100, 20);
        contentPane.add(lblVolume);

        tfVolume = new JTextField();
        tfVolume.setBounds(130, 110, 200, 22);
        contentPane.add(tfVolume);

        JLabel lblDataHora = new JLabel("Data e Hora:");
        lblDataHora.setBounds(30, 150, 100, 20);
        contentPane.add(lblDataHora);

        tfDataHora = new JTextField();
        tfDataHora.setBounds(130, 150, 200, 22);
        tfDataHora.setToolTipText("Formato: YYYY-MM-DD HH:MM");
        contentPane.add(tfDataHora);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 190, 100, 20);
        contentPane.add(lblStatus);

        cbStatus = new JComboBox<>(new String[] {"Pendente", "Realizada", "Cancelada"});
        cbStatus.setBounds(130, 190, 200, 22);
        contentPane.add(cbStatus);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(130, 240, 90, 25);
        btnSalvar.addActionListener(e -> {
            try {
                String hemocentro = cbHemocentro.getSelectedItem().toString();
                String tipoSelecionado = cbTipoSanguineo.getSelectedItem().toString();
                int idTipoSanguineo = Integer.parseInt(tipoSelecionado.substring(1, tipoSelecionado.indexOf("]")));

                int volume = Integer.parseInt(tfVolume.getText());
                String dataHora = tfDataHora.getText();
                String status = cbStatus.getSelectedItem().toString();

                boolean sucesso = controller.cadastrarSolicitacao(hemocentro, idTipoSanguineo, status, volume, dataHora );
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
        btnCancelar.setBounds(240, 240, 90, 25);
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
                RegisterRequest frame = new RegisterRequest();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
