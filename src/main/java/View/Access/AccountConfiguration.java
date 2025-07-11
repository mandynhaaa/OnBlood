package View.Access;

import Controller.UserController;
import Main.User;
import Main.Donor;
import Main.BloodCenter;
import View.Address.ManagerAddress;
import View.Telephone.ManagerTelephone;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccountConfiguration extends JFrame {

    private JTextField tf_nome, tf_email;
    private JPasswordField tf_senha;
    private JFormattedTextField tf_cpf, tf_dataNascimento;
    private JTextField tf_razao_Social, tf_cnpj;
    private JComboBox<String> comboTipoSangue;
    private UserController controller;
    private User user; 

    public AccountConfiguration(User user) {
        this.user = user;

        setTitle("Configuração de Conta");
        setSize(600, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int gridY = 0;

        tf_nome = createField(gbc, "Nome:", gridY++);
        tf_email = createField(gbc, "E-mail:", gridY++);
        tf_senha = createPasswordField(gbc, "Nova Senha (deixe em branco para não alterar):", gridY++);
        
        if ("Doador".equals(user.getUserType())) {
            try {
                MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
                tf_cpf = createFormattedField(gbc, "CPF:", gridY++, cpfFormatter);
                
                comboTipoSangue = createComboBox(gbc, "Tipo Sanguíneo:", gridY++);

                MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
                tf_dataNascimento = createFormattedField(gbc, "Nascimento:", gridY++, dateFormatter);

            } catch (ParseException e) {
                e.printStackTrace();
                tf_cpf = new JFormattedTextField();
                tf_dataNascimento = new JFormattedTextField();
            }
            
            controller = new UserController(user.getId(), tf_nome, tf_email, tf_senha, tf_cpf, tf_dataNascimento, comboTipoSangue, null, null);
            
            List<String> tipos = controller.listarTiposSanguineos();
            tipos.forEach(comboTipoSangue::addItem);

        } else if ("Hemocentro".equals(user.getUserType())) {
            tf_razao_Social = createField(gbc, "Razão Social:", gridY++);
            tf_cnpj = createField(gbc, "CNPJ:", gridY++);
            
            controller = new UserController(user.getId(), tf_nome, tf_email, tf_senha, null, null, null, tf_cnpj, tf_razao_Social);
        }

        gbc.gridy = gridY;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.add(createButton("Gerenciar Endereços", e -> new ManagerAddress(user.getId()).setVisible(true)));
        buttonPanel.add(createButton("Gerenciar Telefones", e -> new ManagerTelephone(user.getId()).setVisible(true)));
        buttonPanel.add(createButton("Salvar Alterações", e -> {
            controller.executeUpdate(); 
            dispose();
        }));
        
        JButton btnVoltar = createButton("Voltar ao Menu", e -> dispose()); 
        buttonPanel.add(btnVoltar);
        
        add(buttonPanel, gbc);
        
        carregarDados();
    }

    private void carregarDados() {
        tf_nome.setText(user.getName());
        tf_email.setText(user.getEmail());

        if ("Doador".equals(user.getUserType())) {
            Donor donorInfo = user.getDonorInfo();
            if (donorInfo != null) {
                tf_cpf.setText(donorInfo.getCpf());
                if (donorInfo.getBirthDate() != null) {
                    tf_dataNascimento.setText(donorInfo.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                comboTipoSangue.setSelectedItem(donorInfo.getBloodType());
            }
        } else if ("Hemocentro".equals(user.getUserType())) {
            BloodCenter centerInfo = user.getBloodCenterInfo();
            if (centerInfo != null) {
                tf_razao_Social.setText(centerInfo.getCompanyName());
                tf_cnpj.setText(centerInfo.getCnpj());
            }
        }
    }

    private JTextField createField(GridBagConstraints gbc, String label, int y) {
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel(label), gbc);
        JTextField tf = new JTextField(25);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(tf, gbc);
        return tf;
    }
    
    private JPasswordField createPasswordField(GridBagConstraints gbc, String label, int y) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel(label), gbc);
        JPasswordField pf = new JPasswordField(25);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(pf, gbc);
        return pf;
    }
    
    private JFormattedTextField createFormattedField(GridBagConstraints gbc, String label, int y, MaskFormatter formatter) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel(label), gbc);
        JFormattedTextField ftf = new JFormattedTextField(formatter);
        ftf.setColumns(25);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(ftf, gbc);
        return ftf;
    }

    private JComboBox<String> createComboBox(GridBagConstraints gbc, String label, int y) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel(label), gbc);
        JComboBox<String> cb = new JComboBox<>();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(cb, gbc);
        return cb;
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
}