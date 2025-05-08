package View.Access;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Connection.ConnectionSQL;
import Controller.UserController;
import View.Address.ManagerAddress;
import View.Telephone.ManagerTelephone;

public class AccountConfiguration extends JFrame {

    private JPanel contentPane;
    private JTextField tf_nome, tf_email, tf_senha, tf_cpf, tf_razao_Social, tf_dataNascimento, tf_cnpj;
    private JComboBox<String> comboTipoSangue;
    private UserController controller;
    private int idUsuario;
    private int userType;

    public AccountConfiguration(int idUser, int userType) {
        this.idUsuario = idUser;
        this.userType = userType;

        setTitle("Configuração de Conta");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNome = new JLabel("Nome:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblNome, gbc);

        tf_nome = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(tf_nome, gbc);

        JLabel lblEmail = new JLabel("E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblEmail, gbc);

        tf_email = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(tf_email, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblSenha, gbc);

        tf_senha = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(tf_senha, gbc);
        
        if (userType == 2) {
            JLabel lblCPF = new JLabel("CPF:");
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(lblCPF, gbc);

            tf_cpf = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 3;
            add(tf_cpf, gbc);

            JLabel lblTipoSangue = new JLabel("Tipo Sanguíneo:");
            gbc.gridx = 0;
            gbc.gridy = 4;
            add(lblTipoSangue, gbc);

            comboTipoSangue = new JComboBox<>();
            gbc.gridx = 1;
            gbc.gridy = 4;
            add(comboTipoSangue, gbc);

            JLabel lblNascimento = new JLabel("Nascimento:");
            lblNascimento.setToolTipText("Formato: dd/MM/aaaa");
            gbc.gridx = 0;
            gbc.gridy = 5;
            add(lblNascimento, gbc);

            tf_dataNascimento = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 5;
            add(tf_dataNascimento, gbc);
            
            controller = new UserController(idUsuario, userType,
                    tf_nome, tf_email, tf_senha, null,
                    tf_cpf, null, tf_dataNascimento, comboTipoSangue, null
            );

            List<String> tipos = controller.listarTiposSanguineos();
            if (tipos.isEmpty()) {
                comboTipoSangue.addItem("Nenhum cadastrado");
                comboTipoSangue.setEnabled(false);
            } else {
                for (String tipo : tipos) comboTipoSangue.addItem(tipo);
            }

        } else if (userType == 3) {
            JLabel lblRazao = new JLabel("Razão Social:");
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(lblRazao, gbc);

            tf_razao_Social = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 3;
            add(tf_razao_Social, gbc);

            JLabel lblCNPJ = new JLabel("CNPJ:");
            gbc.gridx = 0;
            gbc.gridy = 4;
            add(lblCNPJ, gbc);

            tf_cnpj = new JTextField();
            gbc.gridx = 1;
            gbc.gridy = 4;
            add(tf_cnpj, gbc);
            
            controller = new UserController(idUsuario, userType,
                    tf_nome, tf_email, tf_senha, null,
                    null, tf_razao_Social, null, null, tf_cnpj
            );
        }

        addButton("Telefones", e -> new ManagerTelephone(idUsuario).setVisible(true));
        addButton("Endereços", e -> new ManagerAddress(idUsuario).setVisible(true));
        addButton("Salvar", e -> salvar());
        
        carregarDados();
	}
	
    private void addButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(button);
    }
    
    private void salvar() {
        controller.executeUpdate();
        dispose();
    }

    private void carregarDados() {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            
        	String sql = "";
            if (userType == 2) {
            	sql = "SELECT nome, email, cpf, data_Nascimento, id_Tipo_Sanguineo "
        			+ "FROM usuario "
        			+ "INNER JOIN doador ON doador.id_Usuario = usuario.id_Usuario "
        			+ "WHERE usuario.id_Usuario = ?";
            } else if (userType == 3) {
            	sql = "SELECT nome, email, cnpj, razao_Social "
        			+ "FROM usuario "
        			+ "INNER JOIN hemocentro ON hemocentro.id_Usuario = usuario.id_Usuario "
        			+ "WHERE usuario.id_Usuario = ?";
            }
            
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (userType == 2) {
                    tf_cpf.setText(rs.getString("cpf"));
                    
                    DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    LocalDate dataHoraConvertida = LocalDate.parse(rs.getString("data_Nascimento"), formatoEntrada);
                    String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);
                    
                    tf_dataNascimento.setText(dataHoraFormatada);
                    
                    String tipoSanguineo = rs.getString("id_Tipo_Sanguineo");
                    for (int i = 0; i < comboTipoSangue.getItemCount(); i++) {
                        String item = comboTipoSangue.getItemAt(i);
                        if (item.contains("[" + tipoSanguineo + "]")) {
                        	comboTipoSangue.setSelectedIndex(i);
                            break;
                        }
                    }
                } else if (userType == 3) {
                    tf_cnpj.setText(rs.getString("cnpj"));
                    tf_razao_Social.setText(rs.getString("razao_Social"));
                }
                
                tf_nome.setText(rs.getString("nome"));
                tf_email.setText(rs.getString("email"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados.");
        }
    }
}
