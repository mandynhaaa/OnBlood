package View.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controller.RequestController;

import java.util.List;

public class RegisterRequest extends JFrame {

    private JPanel contentPane;
    private JComboBox<String> cbHemocentro, cbTipoSanguineo, cbStatus;
    private JTextField tfVolume, tfDataHora;
    private RequestController controller;
    private int idUsuario;

    public RegisterRequest(int idUser) {
    	this.idUsuario = idUser;

        setTitle("Cadastro de Solicitação");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTipo = new JLabel("Tipo Sanguíneo:");
        lblTipo.setBounds(30, 70, 100, 20);
        contentPane.add(lblTipo);

        cbTipoSanguineo = new JComboBox<>();
        cbTipoSanguineo.setBounds(130, 70, 200, 22);
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
        tfDataHora.setToolTipText("Formato: dd/MM/aaa HH:mm:ss");
        contentPane.add(tfDataHora);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 190, 100, 20);
        contentPane.add(lblStatus);

        cbStatus = new JComboBox<>(new String[] {"Pendente", "Realizada", "Cancelada"});
        cbStatus.setBounds(130, 190, 200, 22);
        contentPane.add(cbStatus);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(130, 240, 90, 25);
        btnSalvar.addActionListener(e -> salvar());
        contentPane.add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(240, 240, 90, 25);
        btnCancelar.addActionListener(e -> {
            dispose();
        });
        contentPane.add(btnCancelar);
        
        controller = new RequestController(0, this.idUsuario, tfVolume, tfDataHora, cbTipoSanguineo, cbStatus);
        
        List<String> tiposSangue = controller.listarTiposSanguineos();
        if (tiposSangue.isEmpty()) {
            cbTipoSanguineo.addItem("Nenhum cadastrado");
            cbTipoSanguineo.setEnabled(false);
        } else {
            for (String tipo : tiposSangue) {
                cbTipoSanguineo.addItem(tipo);
            }
        }
    }
    
    private void salvar() {
        controller.executeRegister();
        dispose();
    }
}
