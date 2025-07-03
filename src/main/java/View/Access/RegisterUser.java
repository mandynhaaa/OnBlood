package View.Access;

import Controller.UserController;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.Font;
import java.awt.Component;
import java.text.ParseException;
import java.util.List;

public class RegisterUser extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tf_nome, tf_email;
    private JPasswordField tf_senha;
    private JFormattedTextField tf_cpf, tf_dataNascimento; 
    private JTextField tf_razao_Social, tf_cnpj;
    private JComboBox<String> comboTipoUsuario;
    private JComboBox<String> comboTipoSangue;
    private UserController controller;

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

        addLabelAndField("Nome:", 80, tf_nome = new JTextField());
        addLabelAndField("E-mail:", 120, tf_email = new JTextField());
        addLabelAndField("Senha:", 160, tf_senha = new JPasswordField());
        addLabelAndField("Tipo:", 200, comboTipoUsuario = new JComboBox<>(new String[]{"Doador", "Hemocentro"}));
        
        JLabel lblCPF = addLabel("CPF:", 240);
        JLabel lblTipoSanguineo = addLabel("Tipo Sanguíneo:", 280);
        JLabel lblDataNascimento = addLabel("Nascimento:", 320);
        lblDataNascimento.setToolTipText("Formato: dd/MM/yyyy");

        try {
            MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
            tf_cpf = new JFormattedTextField(cpfFormatter);
            tf_cpf.setBounds(200, 240, 250, 25);
            contentPane.add(tf_cpf);

            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            tf_dataNascimento = new JFormattedTextField(dateFormatter);
            tf_dataNascimento.setBounds(200, 320, 250, 25);
            contentPane.add(tf_dataNascimento);

        } catch (ParseException e) {
            e.printStackTrace();
            tf_cpf = new JFormattedTextField();
            tf_dataNascimento = new JFormattedTextField();
        }

        comboTipoSangue = new JComboBox<>();
        comboTipoSangue.setBounds(200, 280, 250, 25);
        contentPane.add(comboTipoSangue);

        JLabel lblRazaoSocial = addLabel("Razão Social:", 240);
        tf_razao_Social = new JTextField();
        tf_razao_Social.setBounds(200, 240, 250, 25);
        contentPane.add(tf_razao_Social);

        JLabel lblCNPJ = addLabel("CNPJ:", 280);
        tf_cnpj = new JTextField();
        tf_cnpj.setBounds(200, 280, 250, 25);
        contentPane.add(tf_cnpj);

        setupVisibilityAndActions(lblCPF, lblTipoSanguineo, lblDataNascimento, lblRazaoSocial, lblCNPJ);

        controller = new UserController(tf_nome, tf_email, tf_senha, comboTipoUsuario, tf_cpf, tf_dataNascimento, comboTipoSangue, tf_cnpj, tf_razao_Social);
        loadBloodTypes();
    }
    
    private void addLabelAndField(String labelText, int y, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setBounds(100, y, 100, 25);
        contentPane.add(label);
        field.setBounds(200, y, 250, 25);
        contentPane.add(field);
    }

    private JLabel addLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(100, y, 100, 25);
        contentPane.add(label);
        return label;
    }

    private void setupVisibilityAndActions(JLabel lblCPF, JLabel lblTipoSanguineo, JLabel lblDataNascimento, JLabel lblRazaoSocial, JLabel lblCNPJ) {

        lblRazaoSocial.setVisible(false);
        tf_razao_Social.setVisible(false);
        lblCNPJ.setVisible(false);
        tf_cnpj.setVisible(false);
        
        comboTipoUsuario.addItemListener(e -> {
            boolean isDoador = "Doador".equals(comboTipoUsuario.getSelectedItem());
            
            lblCPF.setVisible(isDoador);
            tf_cpf.setVisible(isDoador);
            lblTipoSanguineo.setVisible(isDoador);
            comboTipoSangue.setVisible(isDoador);
            lblDataNascimento.setVisible(isDoador);
            tf_dataNascimento.setVisible(isDoador);

            lblRazaoSocial.setVisible(!isDoador);
            tf_razao_Social.setVisible(!isDoador);
            lblCNPJ.setVisible(!isDoador);
            tf_cnpj.setVisible(!isDoador);
        });
        
        JButton btnCadastrar = new JButton("Criar Conta");
        btnCadastrar.setBounds(230, 380, 120, 30);
        contentPane.add(btnCadastrar);

        JButton btnLogar = new JButton("Já tenho conta");
        btnLogar.setBounds(230, 420, 120, 25);
        contentPane.add(btnLogar);
        
        btnCadastrar.addActionListener(e -> {
            if (controller != null) {
                controller.executeRegister();
            }
        });

        btnLogar.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
    }

    private void loadBloodTypes() {
        List<String> tiposSangue = new UserController().listarTiposSanguineos();
        
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