package View.Telephone;

import Controller.TelephoneController;
import Main.User;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class EditTelephone extends JFrame {
    private JTextField tfDescricao, tfDdd, tfNumero;
    private TelephoneController controller;
    private ManagerTelephone parentView;
    private ObjectId idUsuario;
    private ObjectId idTelefone;

    public EditTelephone(ObjectId idUsuario, ObjectId idTelefone, ManagerTelephone parentView) {
        this.idUsuario = idUsuario;
        this.idTelefone = idTelefone;
        this.parentView = parentView;

        setTitle("Editar Telefone");
        setSize(400, 250);
        setLocationRelativeTo(parentView);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        add(new JLabel("Descrição:"));
        tfDescricao = new JTextField();
        add(tfDescricao);
        
        add(new JLabel("DDD:"));
        tfDdd = new JTextField();
        add(tfDdd);
        
        add(new JLabel("Número:"));
        tfNumero = new JTextField();
        add(tfNumero);
        
        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        JButton btnCancelar = new JButton("Cancelar");
        add(btnCancelar);

        controller = new TelephoneController(idUsuario, idTelefone, tfDescricao, tfDdd, tfNumero);
        
        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
        
        carregarDados();
    }

    private void carregarDados() {
        User user = new User(idUsuario);
        user.getTelephones().stream()
            .filter(p -> p.getId().equals(idTelefone))
            .findFirst()
            .ifPresent(phone -> {
                tfDescricao.setText(phone.getDescription());
                tfDdd.setText(phone.getDdd());
                tfNumero.setText(phone.getNumber());
            });
    }

    private void salvar() {
        controller.executeUpdate();
        parentView.carregarTelefones();
        dispose();
    }
}