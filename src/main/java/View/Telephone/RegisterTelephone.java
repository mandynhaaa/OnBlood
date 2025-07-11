package View.Telephone;

import Controller.TelephoneController;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class RegisterTelephone extends JFrame {
    private JTextField tf_description;
    private JFormattedTextField tf_ddd, tf_number;
    private TelephoneController controller;
    private ManagerTelephone parentView;

    public RegisterTelephone(ObjectId idUsuario, ManagerTelephone parentView) {
        this.parentView = parentView;

        setTitle("Cadastro de Telefone");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 250);
        setLocationRelativeTo(parentView);
        setLayout(new GridLayout(4, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(new JLabel("Descrição:"));
        tf_description = new JTextField();
        add(tf_description);

        add(new JLabel("DDD:"));
        try {
            MaskFormatter dddFormatter = new MaskFormatter("(##)");
            tf_ddd = new JFormattedTextField(dddFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
            tf_ddd = new JFormattedTextField();
        }
        add(tf_ddd);

        add(new JLabel("Número:"));
        try {
            MaskFormatter numberFormatter = new MaskFormatter("#####-####");
            tf_number = new JFormattedTextField(numberFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
            tf_number = new JFormattedTextField();
        }
        add(tf_number);

        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        JButton btnCancelar = new JButton("Cancelar");
        add(btnCancelar);

        controller = new TelephoneController(idUsuario, tf_description, tf_ddd, tf_number);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvar() {
        controller.executeRegister();
        parentView.carregarTelefones();
        dispose();
    }
}