package View.Request;

import Controller.RequestController;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.awt.*;

public class RegisterRequest extends JFrame {

    private JComboBox<String> cbTipoSanguineo, cbStatus;
    private JTextField tfVolume, tfDataHora;
    private RequestController controller;
    private ManagerRequest parentView;

    public RegisterRequest(ObjectId idHemocentro, ManagerRequest parentView) {
        this.parentView = parentView;

        setTitle("Cadastro de Solicitação");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(parentView);
        setLayout(new GridLayout(5, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("Tipo Sanguíneo:"));
        cbTipoSanguineo = new JComboBox<>();
        add(cbTipoSanguineo);

        add(new JLabel("Volume (mL):"));
        tfVolume = new JTextField();
        add(tfVolume);

        add(new JLabel("Data e Hora (dd/MM/yyyy HH:mm:ss):"));
        tfDataHora = new JTextField();
        add(tfDataHora);

        add(new JLabel("Status:"));
        cbStatus = new JComboBox<>(new String[]{"Pendente", "Realizada", "Cancelada"});
        add(cbStatus);

        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        
        JButton btnCancelar = new JButton("Cancelar");
        add(btnCancelar);

        controller = new RequestController(null, idHemocentro, tfVolume, tfDataHora, cbTipoSanguineo, cbStatus);
        controller.listBloodTypes().forEach(cbTipoSanguineo::addItem);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvar() {
        controller.executeRegister();
        parentView.carregarSolicitacoes();
        dispose();
    }
}