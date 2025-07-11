package View.Request;

import Controller.RequestController;
import Main.Request;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class EditRequest extends JFrame {
    private JComboBox<String> comboStatus, comboBloodType;
    private JTextField txtVolume, txtDataHora;
    private JButton btnSalvar, btnCancelar;
    private RequestController controller;
    private ManagerRequest parentView;
    private ObjectId idRequest;

    public EditRequest(ObjectId idRequest, ManagerRequest parentView) {
        this.idRequest = idRequest;
        this.parentView = parentView;

        setTitle("Editar Solicitação");
        setSize(450, 300);
        setLocationRelativeTo(parentView);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(new String[]{"Pendente", "Realizada", "Cancelada"});
        add(comboStatus);

        add(new JLabel("Tipo Sanguíneo:"));
        comboBloodType = new JComboBox<>();
        add(comboBloodType);

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

        controller = new RequestController(idRequest, null, txtVolume, txtDataHora, comboBloodType, comboStatus);
        controller.listarTiposSanguineos().forEach(comboBloodType::addItem);

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());

        carregarDados();
    }

    private void carregarDados() {
        Request request = new Request(idRequest);
        if (request.getId() != null) {
            comboStatus.setSelectedItem(request.getStatus());
            txtVolume.setText(String.valueOf(request.getVolume()));
            if(request.getDatetime() != null) {
                txtDataHora.setText(request.getDatetime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
            comboBloodType.setSelectedItem(request.getBloodType());
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível carregar os dados da solicitação.");
            dispose();
        }
    }

    private void salvarAlteracoes() {
        controller.executeUpdate();
        parentView.carregarSolicitacoes();
        dispose();
    }
}