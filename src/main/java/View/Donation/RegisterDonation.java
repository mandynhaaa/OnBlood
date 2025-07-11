package View.Donation;

import Controller.DonationController;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.awt.*;

public class RegisterDonation extends JFrame {

    private JTextField tfVolume, tfDataHora;
    private JComboBox<String> cbDoador, cbStatus;
    private DonationController controller;
    private ManagerDonation parentView;

    public RegisterDonation(ObjectId idHemocentro, ManagerDonation parentView) {
        this.parentView = parentView;

        setTitle("Cadastro de Doação");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        setLocationRelativeTo(parentView);
        setLayout(new GridLayout(5, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("Doador:"));
        cbDoador = new JComboBox<>();
        add(cbDoador);

        add(new JLabel("Status:"));
        cbStatus = new JComboBox<>(new String[]{"Pendente", "Realizada", "Cancelada"});
        add(cbStatus);

        add(new JLabel("Volume (mL):"));
        tfVolume = new JTextField();
        add(tfVolume);

        add(new JLabel("Data e Hora (dd/MM/yyyy HH:mm:ss):"));
        tfDataHora = new JTextField();
        add(tfDataHora);

        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        
        JButton btnCancelar = new JButton("Cancelar");
        add(btnCancelar);
        
        controller = new DonationController(idHemocentro, tfVolume, tfDataHora, cbStatus, cbDoador);
        
        controller.listDonors().forEach(cbDoador::addItem);
        
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }
    
    private void salvar() {
        controller.executeRegister();
        parentView.carregarDoacoes();
        dispose();
    }
}