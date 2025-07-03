package View.Donation;

import Controller.DonationController;
import Main.Donation;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class EditDonation extends JFrame {
    private JComboBox<String> comboStatus;
    private JTextField txtVolume, txtDataHora;
    private JButton btnSalvar, btnCancelar;
    private DonationController controller;
    private ManagerDonation parentView;

    public EditDonation(ObjectId idDonation, ManagerDonation parentView) {
        this.parentView = parentView;

        setTitle("Editar Doação");
        setSize(400, 250);
        setLocationRelativeTo(parentView);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(new String[]{"Pendente", "Realizada", "Cancelada"});
        add(comboStatus);

        add(new JLabel("Volume (mL):"));
        txtVolume = new JTextField();
        add(txtVolume);

        add(new JLabel("Data e Hora (dd/MM/yyyy HH:mm:ss):"));
        txtDataHora = new JTextField();
        add(txtDataHora);

        btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        
        btnCancelar = new JButton("Cancelar");
        add(btnCancelar);

        controller = new DonationController(idDonation, txtVolume, txtDataHora, comboStatus);

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());

        carregarDados(idDonation);
    }

    private void carregarDados(ObjectId idDonation) {
        Donation donation = new Donation(idDonation);
        if(donation.getId() != null) {
            comboStatus.setSelectedItem(donation.getStatus());
            txtVolume.setText(String.valueOf(donation.getVolume()));
            if (donation.getDatetime() != null) {
                txtDataHora.setText(donation.getDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
        } else {
             JOptionPane.showMessageDialog(this, "Não foi possível carregar os dados da doação.");
             dispose();
        }
    }

    private void salvarAlteracoes() {
        controller.executeUpdate();
        parentView.carregarDoacoes();
        dispose();
    }
}