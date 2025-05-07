package View.Access;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controller.LoginController;
import View.BloodStock.ManagerBloodStock;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_idUsuario, tf_senha;
	private JLabel lbl_idUsuario, lbl_senha;
	private JButton btn_Login, btnRegistrar;
	private LoginController controller;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }

	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tf_idUsuario = new JTextField();
		tf_idUsuario.setBounds(178, 161, 240, 31);
		contentPane.add(tf_idUsuario);
		tf_idUsuario.setColumns(10);
		
		lbl_idUsuario = new JLabel("Usuário:");
		lbl_idUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_idUsuario.setBounds(111, 162, 60, 25);
		contentPane.add(lbl_idUsuario);
		
		tf_senha = new JTextField();
		tf_senha.setColumns(10);
		tf_senha.setBounds(178, 217, 240, 31);
		contentPane.add(tf_senha);
		
		lbl_senha = new JLabel("Senha:");
		lbl_senha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_senha.setBounds(111, 218, 60, 25);
		contentPane.add(lbl_senha);
		
		btn_Login = new JButton("Login");
		btn_Login.setBounds(217, 275, 120, 31);
		contentPane.add(btn_Login);
		
		JLabel lbl_TituloLogin = new JLabel("OnBlood");
		lbl_TituloLogin.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		lbl_TituloLogin.setBounds(217, 52, 127, 61);
		contentPane.add(lbl_TituloLogin);
		
		JLabel lbl_bemvindo = new JLabel("Bem-vindo!");
		lbl_bemvindo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl_bemvindo.setBounds(237, 111, 119, 13);
		contentPane.add(lbl_bemvindo);
		
		btn_Login.addActionListener(e -> logar());
		
		JLabel lbl_naopossuiconta = new JLabel("Não possui conta?");
		lbl_naopossuiconta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbl_naopossuiconta.setBounds(230, 320, 119, 13);
		contentPane.add(lbl_naopossuiconta);
		
		btnRegistrar = new JButton("Registrar-se");
		btnRegistrar.setBounds(217, 338, 120, 15);
		contentPane.add(btnRegistrar);

		btnRegistrar.addActionListener(e -> {
		    new RegisterUser().setVisible(true);
		    dispose();
		});

		controller = new LoginController(tf_idUsuario, tf_senha);
	}
	
    private void logar() {
        controller.executeLogin();
        dispose();
    }
}
