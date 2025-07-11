package Controller;

import Main.User;
import Main.Donor;
import Main.BloodCenter;
import Main.BloodType;
import View.Access.Login;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private ObjectId id_User;
    private JTextField tf_name, tf_email;
    private JPasswordField tf_senha;
    private JComboBox<String> combo_Type;
    private JFormattedTextField tf_cpf, tf_dataNascimento;
    private JTextField tf_razao_Social, tf_cnpj;
    private JComboBox<String> combo_Blood_Type;

    public UserController() {}

    public UserController(
    	JTextField name, 
    	JTextField email, 
    	JPasswordField password, 
    	JComboBox<String> type,
    	JFormattedTextField cpf, 
    	JFormattedTextField dataNascimento, 
    	JComboBox<String> tipoSanguineo,
    	JTextField cnpj, 
    	JTextField razao_Social
    ) {
        this.tf_name = name;
        this.tf_email = email;
        this.tf_senha = password;
        this.combo_Type = type;
        this.tf_cpf = cpf;
        this.tf_dataNascimento = dataNascimento;
        this.combo_Blood_Type = tipoSanguineo;
        this.tf_cnpj = cnpj;
        this.tf_razao_Social = razao_Social;
    }
    
    public UserController(
    	ObjectId id_User, 
    	JTextField name, 
    	JTextField email, 
    	JPasswordField password,
    	JFormattedTextField cpf, 
    	JFormattedTextField dataNascimento, 
    	JComboBox<String> tipoSanguineo,
    	JTextField cnpj, 
    	JTextField razao_Social
    ) {
        this(name, email, password, null, cpf, dataNascimento, tipoSanguineo, cnpj, razao_Social);
        this.id_User = id_User;
    }

    public void executeRegister() {
        String name = tf_name.getText();
        String email = tf_email.getText();
        String rawPassword = new String(tf_senha.getPassword());
        String userType = (String) combo_Type.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || rawPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            return;
        }
        
        User existingUser = new User(name, "").searchByName();
        if (existingUser != null) {
            JOptionPane.showMessageDialog(null, "Este nome de usuário já está em uso.");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(rawPassword);
        user.setCreationDate(LocalDateTime.now());
        user.setUserType(userType);

        if ("Doador".equals(userType)) {
            try {
                String cpf = tf_cpf.getText().replaceAll("[^0-9]", "");
                String data = tf_dataNascimento.getText();

                if (cpf.length() != 11 || data.trim().replace("/", "").isEmpty()) {
                     JOptionPane.showMessageDialog(null, "CPF e Data de Nascimento são obrigatórios.");
                     return;
                }
                
                LocalDate birthDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String bloodType = (String) combo_Blood_Type.getSelectedItem();
                user.setDonorInfo(new Donor(cpf, birthDate, bloodType));

            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido. Use dd/MM/yyyy.");
                return;
            }
        } else if ("Hemocentro".equals(userType)) {
            String cnpj = tf_cnpj.getText();
            String razaoSocial = tf_razao_Social.getText();
            user.setBloodCenterInfo(new BloodCenter(cnpj, razaoSocial));
        }

        ObjectId newId = user.create();
        if (newId != null) {
            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao criar usuário.");
        }
    }
    
    public boolean executeUpdate() {
        if (this.id_User == null) {
            JOptionPane.showMessageDialog(null, "Erro: ID do usuário não encontrado para atualização.");
            return false;
        }
        
        User user = new User(this.id_User);
        if(user.getId() == null) {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado na base de dados.");
            return false;
        }

        user.setName(tf_name.getText());
        user.setEmail(tf_email.getText());
        
        String newPassword = new String(tf_senha.getPassword());
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(newPassword); 
        }

        if ("Doador".equals(user.getUserType())) {
            Donor donorInfo = user.getDonorInfo() != null ? user.getDonorInfo() : new Donor();
            try {
                donorInfo.setCpf(tf_cpf.getText().replaceAll("[^0-9]", ""));
                donorInfo.setBloodType((String) combo_Blood_Type.getSelectedItem());
                String data = tf_dataNascimento.getText();
                if (!data.trim().replace("/", "").isEmpty()) {
                    donorInfo.setBirthDate(LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                user.setDonorInfo(donorInfo);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido. Use dd/MM/yyyy.");
                return false;
            }
        } else if ("Hemocentro".equals(user.getUserType())) {
            BloodCenter centerInfo = user.getBloodCenterInfo() != null ? user.getBloodCenterInfo() : new BloodCenter();
            centerInfo.setCnpj(tf_cnpj.getText());
            centerInfo.setCompanyName(tf_razao_Social.getText());
            user.setBloodCenterInfo(centerInfo);
        }

        try {
            user.update(); 
            JOptionPane.showMessageDialog(null, "Dados alterados com sucesso!");
            return true; 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao salvar as alterações.");
            e.printStackTrace();
            return false; 
        }
    }
    
    public List<String> listBloodTypes() {
        List<String> tipos = new ArrayList<>();
        MongoCollection<Document> collection = new BloodType("").getCollection();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                tipos.add(doc.getString("descricao"));
            }
        }
        return tipos;
    }
}