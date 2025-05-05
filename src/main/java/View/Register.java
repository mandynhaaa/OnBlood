package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controller.LoginController;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class Register extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tf_idUsuario;
	private JTextField tf_senha;
	private JLabel lbl_email;
	private JTextField textField;
	private JTextField tf_DDD;
	private JTextField tf_numero;
	private JTextField tf_pais;
	private JTextField tf_estado;
	private JTextField tf_cidade;
	private JTextField tf_rua;
	private JTextField tf_numeroEndereco;
	private JTextField tf_cep;
	private JTextField tf_descricaoEndereco;
	private JTextField tf_complemento;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Register() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tf_idUsuario = new JTextField();
		tf_idUsuario.setBounds(178, 135, 240, 31);
		contentPane.add(tf_idUsuario);
		tf_idUsuario.setColumns(10);
		
		JLabel lbl_idUsuario = new JLabel("Usuário:");
		lbl_idUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_idUsuario.setBounds(111, 136, 60, 25);
		contentPane.add(lbl_idUsuario);
		
		tf_senha = new JTextField();
		tf_senha.setColumns(10);
		tf_senha.setBounds(178, 176, 240, 31);
		contentPane.add(tf_senha);
		
		lbl_email = new JLabel("E-mail:");
		lbl_email.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_email.setBounds(121, 177, 50, 25);
		contentPane.add(lbl_email);
		
		JButton btn_Resgiter = new JButton("Registrar");
		btn_Resgiter.setBounds(217, 373, 111, 31);
		contentPane.add(btn_Resgiter);
		
		JLabel lbl_TituloLogin = new JLabel("OnBlood");
		lbl_TituloLogin.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		lbl_TituloLogin.setBounds(217, 26, 127, 61);
		contentPane.add(lbl_TituloLogin);
		
		JLabel lbl_bemvindo = new JLabel("Bem-vindo!");
		lbl_bemvindo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl_bemvindo.setBounds(237, 85, 119, 13);
		contentPane.add(lbl_bemvindo);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(178, 217, 240, 31);
		contentPane.add(textField);
		
		JLabel lbl_senha = new JLabel("Senha:");
		lbl_senha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_senha.setBounds(121, 218, 50, 25);
		contentPane.add(lbl_senha);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Doador", "Hemocentro"}));
		comboBox.setBounds(211, 258, 133, 21);
		contentPane.add(comboBox);
		
		JPanel panel_telefone = new JPanel();
		panel_telefone.setVisible(false);
		panel_telefone.setBounds(121, 135, 336, 220);
		contentPane.add(panel_telefone);
		panel_telefone.setLayout(null);
		
		JLabel lbl_TItuloNovoTelefone = new JLabel("Novo Telefone");
		lbl_TItuloNovoTelefone.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_TItuloNovoTelefone.setBounds(110, 33, 146, 17);
		panel_telefone.add(lbl_TItuloNovoTelefone);
		
		tf_DDD = new JTextField();
		tf_DDD.setBounds(163, 99, 69, 19);
		panel_telefone.add(tf_DDD);
		tf_DDD.setColumns(10);
		
		JLabel lbl_DDD = new JLabel("DDD:");
		lbl_DDD.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_DDD.setBounds(102, 102, 45, 13);
		panel_telefone.add(lbl_DDD);
		
		tf_numero = new JTextField();
		tf_numero.setColumns(10);
		tf_numero.setBounds(163, 128, 127, 19);
		panel_telefone.add(tf_numero);
		
		JLabel lbl_numero = new JLabel("Número:");
		lbl_numero.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_numero.setBounds(84, 131, 63, 13);
		panel_telefone.add(lbl_numero);
		
		JButton btn_AdicionarTelefone = new JButton("Adicionar");
		btn_AdicionarTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_AdicionarTelefone.setBounds(121, 168, 111, 31);
		panel_telefone.add(btn_AdicionarTelefone);
		
		JButton btn_NovoTelefone = new JButton("+ Adicionar Telefone");
		btn_NovoTelefone.setBounds(201, 289, 150, 31);
		contentPane.add(btn_NovoTelefone);
		
		JButton btn_NovoEndereco = new JButton("+ Adicionar Endereço");
		btn_NovoEndereco.setBounds(201, 332, 150, 31);
		contentPane.add(btn_NovoEndereco);
		
		JLabel lbl_TipoUsuario = new JLabel("Tipo de Usuário:");
		lbl_TipoUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_TipoUsuario.setBounds(98, 254, 111, 25);
		contentPane.add(lbl_TipoUsuario);
		
		btn_NovoTelefone.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	panel_telefone.setVisible(!panel_telefone.isVisible());
		    }
		});

		LoginController loginController = new LoginController(tf_idUsuario, tf_senha);
		
		JPanel panel_endereco = new JPanel();
		panel_endereco.setVisible(false);
		panel_endereco.setBounds(121, 85, 369, 322);
		contentPane.add(panel_endereco);
		panel_endereco.setLayout(null);
		
		JLabel lbl_TItuloNovoEndereco = new JLabel("Novo Endereço");
		lbl_TItuloNovoEndereco.setBounds(122, 22, 134, 25);
		lbl_TItuloNovoEndereco.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_endereco.add(lbl_TItuloNovoEndereco);
		
		tf_pais = new JTextField();
		tf_pais.setColumns(10);
		tf_pais.setBounds(168, 98, 127, 19);
		panel_endereco.add(tf_pais);
		
		JLabel lbl_pais = new JLabel("País:");
		lbl_pais.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_pais.setBounds(66, 98, 63, 13);
		panel_endereco.add(lbl_pais);
		
		tf_estado = new JTextField();
		tf_estado.setColumns(10);
		tf_estado.setBounds(168, 124, 127, 19);
		panel_endereco.add(tf_estado);
		
		JLabel lbl_estado = new JLabel("Estado:");
		lbl_estado.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_estado.setBounds(66, 124, 63, 13);
		panel_endereco.add(lbl_estado);
		
		JLabel lbl_cidade = new JLabel("Cidade:");
		lbl_cidade.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_cidade.setBounds(66, 150, 63, 13);
		panel_endereco.add(lbl_cidade);
		
		tf_cidade = new JTextField();
		tf_cidade.setColumns(10);
		tf_cidade.setBounds(168, 150, 127, 19);
		panel_endereco.add(tf_cidade);
		
		JLabel lbl_Rua = new JLabel("Rua:");
		lbl_Rua.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_Rua.setBounds(66, 176, 63, 13);
		panel_endereco.add(lbl_Rua);
		
		tf_rua = new JTextField();
		tf_rua.setColumns(10);
		tf_rua.setBounds(168, 176, 127, 19);
		panel_endereco.add(tf_rua);
		
		JLabel lbl_numero_1_1_3 = new JLabel("Número:");
		lbl_numero_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_numero_1_1_3.setBounds(66, 202, 63, 13);
		panel_endereco.add(lbl_numero_1_1_3);
		
		tf_numeroEndereco = new JTextField();
		tf_numeroEndereco.setColumns(10);
		tf_numeroEndereco.setBounds(168, 202, 127, 19);
		panel_endereco.add(tf_numeroEndereco);
		
		tf_cep = new JTextField();
		tf_cep.setColumns(10);
		tf_cep.setBounds(168, 228, 127, 19);
		panel_endereco.add(tf_cep);
		
		JLabel lbl_cep = new JLabel("CEP:");
		lbl_cep.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_cep.setBounds(66, 228, 63, 13);
		panel_endereco.add(lbl_cep);
		
		JLabel lbl_descricaoEndereco = new JLabel("Descrição:");
		lbl_descricaoEndereco.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_descricaoEndereco.setBounds(66, 72, 69, 13);
		panel_endereco.add(lbl_descricaoEndereco);
		
		tf_descricaoEndereco = new JTextField();
		tf_descricaoEndereco.setColumns(10);
		tf_descricaoEndereco.setBounds(168, 72, 127, 19);
		panel_endereco.add(tf_descricaoEndereco);
		
		JLabel lbl_complemento = new JLabel("Complemento:");
		lbl_complemento.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lbl_complemento.setBounds(66, 254, 92, 13);
		panel_endereco.add(lbl_complemento);
		
		tf_complemento = new JTextField();
		tf_complemento.setColumns(10);
		tf_complemento.setBounds(168, 254, 127, 19);
		panel_endereco.add(tf_complemento);
		
		JButton btn_AdicionarEndereco = new JButton("Adicionar");
		btn_AdicionarEndereco.setBounds(133, 281, 111, 31);
		panel_endereco.add(btn_AdicionarEndereco);
		
		btn_NovoEndereco.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	panel_endereco.setVisible(!panel_endereco.isVisible());
		    }
		});
		
		btn_Resgiter.addActionListener(loginController);
	}
}
