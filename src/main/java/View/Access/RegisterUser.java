package View.Access;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Font;

import Controller.UserController;

public class RegisterUser extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tf_nome;
    private JTextField tf_email;
    private JTextField tf_senha;
    private JComboBox<String> comboTipoUsuario;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                RegisterUser frame = new RegisterUser();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public RegisterUser() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 500);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Criar Conta");
        lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        lblTitulo.setBounds(200, 40, 180, 40);
        contentPane.add(lblTitulo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNome.setBounds(100, 110, 100, 25);
        contentPane.add(lblNome);

        tf_nome = new JTextField();
        tf_nome.setBounds(180, 110, 240, 25);
        contentPane.add(tf_nome);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblEmail.setBounds(100, 150, 100, 25);
        contentPane.add(lblEmail);

        tf_email = new JTextField();
        tf_email.setBounds(180, 150, 240, 25);
        contentPane.add(tf_email);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblSenha.setBounds(100, 190, 100, 25);
        contentPane.add(lblSenha);

        tf_senha = new JTextField();
        tf_senha.setBounds(180, 190, 240, 25);
        contentPane.add(tf_senha);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblTipo.setBounds(100, 230, 100, 25);
        contentPane.add(lblTipo);

        comboTipoUsuario = new JComboBox<>();
        comboTipoUsuario.setModel(new DefaultComboBoxModel<>(new String[] { "Doador", "Hemocentro" }));
        comboTipoUsuario.setBounds(180, 230, 240, 25);
        contentPane.add(comboTipoUsuario);

        JButton btnCadastrar = new JButton("Criar Conta");
        btnCadastrar.setBounds(217, 280, 120, 30);
        contentPane.add(btnCadastrar);

		JLabel lbl_japossuiconta = new JLabel("Já possui conta?");
		lbl_japossuiconta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbl_japossuiconta.setBounds(235, 320, 119, 13);
		contentPane.add(lbl_japossuiconta);
		
		JButton btnLogar = new JButton("Logar");
		btnLogar.setBounds(217, 338, 120, 15);
		contentPane.add(btnLogar);

        UserController controller = new UserController(tf_nome, tf_email, tf_senha, comboTipoUsuario);
        btnCadastrar.addActionListener(controller);

        btnLogar.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
    }
}
