package View.Donation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

import Controller.DonationController;

import java.util.List;

public class RegisterDonation extends JFrame {

    protected static final int NULL = 0;
	private JPanel contentPane;
    private JTextField tfVolume, tfDataHora;
    private JComboBox<String> cbDoador, cbStatus;
    private DonationController controller;
    private int idUsuario;

    public RegisterDonation(int idUser) {
        controller = new DonationController();

        setTitle("Cadastro de Doações");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblDoador = new JLabel("Doador:");
        lblDoador.setBounds(30, 30, 80, 20);
        contentPane.add(lblDoador);

        cbDoador = new JComboBox<>();
        cbDoador.setBounds(120, 30, 200, 22);

        List<String> doadores = controller.listarDoadores();
        if (doadores.isEmpty()) {
            cbDoador.addItem("Nenhum cadastrado");
            cbDoador.setEnabled(false);
        } else {
            for (String doador : doadores) {
                cbDoador.addItem(doador);
            }
        }
        contentPane.add(cbDoador);

        JLabel lblHemocentro = new JLabel("Hemocentro:");
        lblHemocentro.setBounds(30, 70, 80, 20);
        contentPane.add(lblHemocentro);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 110, 80, 20);
        contentPane.add(lblStatus);

        cbStatus = new JComboBox<>();
        cbStatus.setModel(new DefaultComboBoxModel<>(new String[] {"Pendente", "Realizada", "Cancelada"}));
        cbStatus.setBounds(120, 110, 200, 22);
        contentPane.add(cbStatus);

        JLabel lblVolume = new JLabel("Volume (mL):");
        lblVolume.setBounds(30, 150, 80, 20);
        contentPane.add(lblVolume);

        tfVolume = new JTextField();
        tfVolume.setBounds(120, 150, 200, 22);
        contentPane.add(tfVolume);

        JLabel lblDataHora = new JLabel("Data e Hora:");
        lblDataHora.setBounds(30, 190, 80, 20);
        contentPane.add(lblDataHora);

        tfDataHora = new JTextField();
        tfDataHora.setBounds(120, 190, 200, 22);
        tfDataHora.setToolTipText("Formato: YYYY-MM-DD HH:MM");
        contentPane.add(tfDataHora);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(120, 240, 90, 25);
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	String doadorSelecionado = cbDoador.getSelectedItem().toString();
                	int idDoador = Integer.parseInt(doadorSelecionado.substring(1, doadorSelecionado.indexOf("]")));
                    String status = cbStatus.getSelectedItem().toString();
                    int volume = Integer.parseInt(tfVolume.getText());
                    String dataHora = tfDataHora.getText();

                    boolean sucesso = controller.cadastrarDoacao(idDoador, this.idUsuario, status, volume, dataHora);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(null, "Doação cadastrada com sucesso!");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao cadastrar doação.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });
        contentPane.add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(230, 240, 90, 25);
        contentPane.add(btnCancelar);
        btnCancelar.addActionListener(e -> {
        	dispose();
            ManagerDonation telaDoacao = new ManagerDonation(this.idUsuario);
            telaDoacao.setVisible(true);
        });
        
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        
    }
    
    private void salvar() {
        controller.executeRegister();
        dispose();
    }
}
