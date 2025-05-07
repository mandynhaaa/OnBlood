package View.Telephone;

import javax.swing.*;
import Controller.TelephoneController;
import java.awt.*;
import java.util.List;

public class RegisterTelephone extends JFrame {
    private JComboBox<String> cbUsuarios;
    private JTextField tf_description, tf_ddd, tf_number;
    private TelephoneController controller;

    public RegisterTelephone() {
        setTitle("Cadastro de Telefone");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 450);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setBounds(30, 20, 100, 20);
        add(lblUsuario);

        cbUsuarios = new JComboBox<>();
        cbUsuarios.setBounds(130, 20, 220, 22);
        add(cbUsuarios);

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setBounds(30, 60, 100, 20);
        add(lblDescricao);

        tf_description = new JTextField();
        tf_description.setBounds(130, 60, 220, 22);
        add(tf_description);

        JLabel lblDdd = new JLabel("DDD:");
        lblDdd.setBounds(30, 100, 100, 20);
        add(lblDdd);

        tf_ddd = new JTextField();
        tf_ddd.setBounds(130, 100, 80, 22);
        add(tf_ddd);

        JLabel lblNumber = new JLabel("Número:");
        lblNumber.setBounds(30, 140, 100, 20);
        add(lblNumber);

        tf_number = new JTextField();
        tf_number.setBounds(130, 140, 220, 22);
        add(tf_number);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(130, 200, 90, 25);
        add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(230, 200, 90, 25);
        add(btnCancelar);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        controller = new TelephoneController(0, tf_description, tf_ddd, tf_number, cbUsuarios);

        List<String> usuarios = controller.listUsers();
        if (usuarios.isEmpty()) {
            cbUsuarios.addItem("Nenhum cadastrado");
            cbUsuarios.setEnabled(false);
        } else {
            for (String u : usuarios) cbUsuarios.addItem(u);
        }
    }

    private void salvar() {
        controller.executeRegister();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterTelephone().setVisible(true));
    }
}
