package View.Donation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

import Controller.AddressController;
import Controller.DonationController;

import java.util.List;

public class RegisterDonation extends JFrame {

    private JTextField tfVolume, tfDataHora;
    private JComboBox<String> cbDoador, cbStatus;
    private DonationController controller;
    private int idUsuario;

    public RegisterDonation(int idUser) {
        setTitle("Cadastro de Doações");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        
        JLabel lblDoador = new JLabel("Doador:");
        lblDoador.setBounds(30, 30, 80, 20);
        add(lblDoador);

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
        add(cbDoador);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 110, 80, 20);
        add(lblStatus);

        cbStatus = new JComboBox<>();
        cbStatus.setModel(new DefaultComboBoxModel<>(new String[] {"Pendente", "Realizada", "Cancelada"}));
        cbStatus.setBounds(120, 110, 200, 22);
        add(cbStatus);

        JLabel lblVolume = new JLabel("Volume (mL):");
        lblVolume.setBounds(30, 150, 80, 20);
        add(lblVolume);

        tfVolume = new JTextField();
        tfVolume.setBounds(120, 150, 200, 22);
        add(tfVolume);

        JLabel lblDataHora = new JLabel("Data e Hora:");
        lblDataHora.setBounds(30, 190, 80, 20);
        add(lblDataHora);

        tfDataHora = new JTextField();
        tfDataHora.setBounds(120, 190, 200, 22);
        tfDataHora.setToolTipText("Formato: YYYY-MM-DD HH:MM");
        add(tfDataHora);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(120, 240, 90, 25);
        add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(230, 240, 90, 25);
        add(btnCancelar);
        
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        
        controller = new DonationController(0, this.idUsuario, 0, tfVolume, tfDataHora, cbStatus, cbDoador);
    }
    
    private void salvar() {
        controller.executeRegister();
        dispose();
    }
}
