package View.Access;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Font;
import java.util.List;

import Controller.UserController;

public class RegisterUser extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tf_nome, tf_email, tf_senha;
    private JTextField tf_cpf, tf_endereco, tf_razao_Social;
    private JComboBox<String> comboTipoUsuario;
    private JTextField tf_dataNascimento;
    private JComboBox<String> comboTipoSangue;
    private JTextField tf_cnpj;
    private UserController controller;

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
        setBounds(100, 100, 600, 550);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Criar Conta");
        lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        lblTitulo.setBounds(220, 20, 180, 40);
        contentPane.add(lblTitulo);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(100, 80, 100, 25);
        contentPane.add(lblNome);

        tf_nome = new JTextField();
        tf_nome.setBounds(200, 80, 250, 25);
        contentPane.add(tf_nome);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setBounds(100, 120, 100, 25);
        contentPane.add(lblEmail);

        tf_email = new JTextField();
        tf_email.setBounds(200, 120, 250, 25);
        contentPane.add(tf_email);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(100, 160, 100, 25);
        contentPane.add(lblSenha);

        tf_senha = new JTextField();
        tf_senha.setBounds(200, 160, 250, 25);
        contentPane.add(tf_senha);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(100, 200, 100, 25);
        contentPane.add(lblTipo);

        comboTipoUsuario = new JComboBox<>(new String[] { "Doador", "Hemocentro" });
        comboTipoUsuario.setBounds(200, 200, 250, 25);
        contentPane.add(comboTipoUsuario);

        tf_cpf = new JTextField();
        tf_cpf.setBounds(200, 240, 250, 25);
        contentPane.add(tf_cpf);

        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setBounds(100, 240, 100, 25);
        contentPane.add(lblCPF);

        JLabel lblTipoSanguineo = new JLabel("Tipo Sanguíneo:");
        lblTipoSanguineo.setBounds(100, 280, 100, 25);
        contentPane.add(lblTipoSanguineo);

        comboTipoSangue = new JComboBox<>();
        comboTipoSangue.setBounds(200, 280, 250, 25);
        contentPane.add(comboTipoSangue);

        JLabel lblDataNascimento = new JLabel("Nascimento:");
        lblDataNascimento.setToolTipText("Formato: dd/MM/aaa");
        lblDataNascimento.setBounds(100, 320, 200, 25);
        contentPane.add(lblDataNascimento);

        JTextField tf_dataNascimento = new JTextField();
        tf_dataNascimento.setBounds(300, 320, 150, 25);
        contentPane.add(tf_dataNascimento);

        tf_razao_Social = new JTextField();
        tf_razao_Social.setBounds(200, 240, 250, 25);
        contentPane.add(tf_razao_Social);
        tf_razao_Social.setVisible(false);

        JLabel lblrazao_Social = new JLabel("Razão Social:");
        lblrazao_Social.setBounds(100, 240, 100, 25);
        contentPane.add(lblrazao_Social);
        lblrazao_Social.setVisible(false);

        JTextField tf_cnpj = new JTextField();
        tf_cnpj.setBounds(200, 280, 250, 25);
        contentPane.add(tf_cnpj);
        tf_cnpj.setVisible(false);

        JLabel lblCNPJ = new JLabel("CNPJ:");
        lblCNPJ.setBounds(100, 280, 100, 25);
        contentPane.add(lblCNPJ);
        lblCNPJ.setVisible(false);

        comboTipoUsuario.addItemListener(e -> {
            boolean isDoador = comboTipoUsuario.getSelectedItem().equals("Doador");

            tf_cpf.setVisible(isDoador);
            lblCPF.setVisible(isDoador);
            comboTipoSangue.setVisible(isDoador);
            lblTipoSanguineo.setVisible(isDoador);
            tf_dataNascimento.setVisible(isDoador);
            lblDataNascimento.setVisible(isDoador);

            tf_razao_Social.setVisible(!isDoador);
            lblrazao_Social.setVisible(!isDoador);
            tf_cnpj.setVisible(!isDoador);
            lblCNPJ.setVisible(!isDoador);
        });

        JButton btnCadastrar = new JButton("Criar Conta");
        btnCadastrar.setBounds(230, 360, 120, 30);
        contentPane.add(btnCadastrar);

        JButton btnLogar = new JButton("Logar");
        btnLogar.setBounds(230, 400, 120, 25);
        contentPane.add(btnLogar);

        controller = new UserController(0, 0,
        	    tf_nome, tf_email, tf_senha, comboTipoUsuario,
        	    tf_cpf, tf_razao_Social, tf_dataNascimento, comboTipoSangue, tf_cnpj
        	);
        btnCadastrar.addActionListener(e -> {
            controller.executeRegister();
            dispose();
        });

        btnLogar.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
        
        List<String> tiposSangue = controller.listarTiposSanguineos();
        if (tiposSangue.isEmpty()) {
        	comboTipoSangue.addItem("Nenhum cadastrado");
        	comboTipoSangue.setEnabled(false);
        } else {
            for (String tipo : tiposSangue) {
            	comboTipoSangue.addItem(tipo);
            }
        }
    }
}
