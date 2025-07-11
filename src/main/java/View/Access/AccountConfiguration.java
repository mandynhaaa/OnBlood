package View.Access;

import Controller.UserController;
import Main.User;
import Main.Donor;
import Main.BloodCenter;
import View.Address.ManagerAddress;
import View.Telephone.ManagerTelephone;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int gridY = 0;

        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.LINE_END;
        
        mainPanel.add(new JLabel("Nome:"), gbc);
        mainPanel.add(new JLabel("E-mail:"), gbc);
        mainPanel.add(new JLabel("Nova Senha (deixe em branco para não alterar):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.LINE_START;

        tf_nome = new JTextField(25);
        mainPanel.add(tf_nome, gbc);
        
        tf_email = new JTextField(25);
        gbc.gridy = 1;
        mainPanel.add(tf_email, gbc);

        tf_senha = new JPasswordField(25);
        gbc.gridy = 2;
        mainPanel.add(tf_senha, gbc);

        gridY = 3;

        if ("Doador".equals(user.getUserType())) {
            try {
                MaskFormatter cpfFormatter = new MaskFormatter("###.###.###-##");
                tf_cpf = createFormattedField(mainPanel, gbc, "CPF:", gridY++, cpfFormatter);
                
                comboTipoSangue = createComboBox(mainPanel, gbc, "Tipo Sanguíneo:", gridY++);

                MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
                tf_dataNascimento = createFormattedField(mainPanel, gbc, "Nascimento:", gridY++, dateFormatter);

            } catch (ParseException e) {
                e.printStackTrace();
                tf_cpf = new JFormattedTextField();
                tf_dataNascimento = new JFormattedTextField();
            }
            
            controller = new UserController(user.getId(), tf_nome, tf_email, tf_senha, tf_cpf, tf_dataNascimento, comboTipoSangue, null, null);
            
            List<String> tipos = controller.listBloodTypes();
            tipos.forEach(comboTipoSangue::addItem);

        } else if ("Hemocentro".equals(user.getUserType())) {
             tf_razao_Social = createField(mainPanel, gbc, "Razão Social:", gridY++);
             tf_cnpj = createField(mainPanel, gbc, "CNPJ:", gridY++);
            
            controller = new UserController(user.getId(), tf_nome, tf_email, tf_senha, null, null, null, tf_cnpj, tf_razao_Social);
        }

        gbc.gridy = gridY;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 5, 5, 5);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(createButton("Gerenciar Endereços", e -> new ManagerAddress(user.getId()).setVisible(true)));
        buttonPanel.add(createButton("Gerenciar Telefones", e -> new ManagerTelephone(user.getId()).setVisible(true)));
        buttonPanel.add(createButton("Salvar Alterações", e -> {
            controller.executeUpdate();
            dispose();
        }));
        
        mainPanel.add(buttonPanel, gbc);
        
        gbc.gridy = gridY + 1;
        gbc.weighty = 1.0;
        mainPanel.add(new JLabel(""), gbc);

        loadData();
    }
    
    private JTextField createField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(label), gbc);
        
        JTextField tf = new JTextField(25);
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(tf, gbc);
        return tf;
    }
    
    private JFormattedTextField createFormattedField(JPanel panel, GridBagConstraints gbc, String label, int y, MaskFormatter formatter) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(label), gbc);

        JFormattedTextField ftf = new JFormattedTextField(formatter);
        ftf.setColumns(25);
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(ftf, gbc);
        return ftf;
    }
    
    private JComboBox<String> createComboBox(JPanel panel, GridBagConstraints gbc, String label, int y) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(label), gbc);

        JComboBox<String> cb = new JComboBox<>();
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.LINE_START;
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        comboPanel.add(cb);
        panel.add(comboPanel, gbc);
        return cb;
    }
    
    private void loadData() {
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

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
}