package Controller;

import Main.Address;
import Main.Telephone;
import Main.User;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelephoneController {

    private ObjectId id_User;
    private ObjectId id_Telephone;
    private JTextField tf_description;
    private JFormattedTextField tf_ddd; 
    private JFormattedTextField tf_number; 

    public TelephoneController(ObjectId id_User) {
        this.id_User = id_User;
    }

    public TelephoneController(ObjectId id_User, JTextField tf_description, JFormattedTextField tf_ddd, JFormattedTextField tf_number) {
        this(id_User);
        this.tf_description = tf_description;
        this.tf_ddd = tf_ddd;
        this.tf_number = tf_number;
    }

    public TelephoneController(ObjectId id_User, ObjectId id_Telephone, JTextField tf_description, JFormattedTextField tf_ddd, JFormattedTextField tf_number) {
        this(id_User, tf_description, tf_ddd, tf_number);
        this.id_Telephone = id_Telephone;
    }

    public void executeRegister() {
        User user = new User(this.id_User);
        if (user.getId() == null) {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
            return;
        }

        Telephone phone = new Telephone();
        phone.setDescription(tf_description.getText());
        phone.setDdd(tf_ddd.getText().replaceAll("[^0-9]", ""));
        phone.setNumber(tf_number.getText().replaceAll("[^0-9]", ""));
        phone.create();

        user.getTelephones().add(phone);
        user.update();
        JOptionPane.showMessageDialog(null, "Telefone adicionado com sucesso!");
    }

    public void executeUpdate() {
        User user = new User(this.id_User);
        user.getTelephones().stream()
            .filter(p -> p.getId().equals(this.id_Telephone))
            .findFirst()
            .ifPresent(phone -> {
                phone.setDescription(tf_description.getText());
                phone.setDdd(tf_ddd.getText().replaceAll("[^0-9]", ""));
                phone.setNumber(tf_number.getText().replaceAll("[^0-9]", ""));
                user.update();
                JOptionPane.showMessageDialog(null, "Telefone alterado com sucesso!");
            });
    }

    public void executeDelete(ObjectId id_TelephoneToDelete) {
        User user = new User(this.id_User);
        boolean removed = user.getTelephones().removeIf(p -> p.getId().equals(id_TelephoneToDelete));

        if (removed) {
            user.update();
            JOptionPane.showMessageDialog(null, "Telefone removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Telefone não encontrado para exclusão.");
        }
    }

    public List<String> listTelephones(ObjectId id_User) {        
        List<String> telefonesFormatados = new ArrayList<>();
        MongoCollection<Document> collection = new Telephone().getCollection();
        
        Bson filter = Filters.eq("id_Usuario", id_User);
        
        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                 Document doc = cursor.next();
                 telefonesFormatados.add(String.format("%s: (%s) %s [%s]",
                     doc.getString("descricao"), 
                     doc.getString("ddd"), 
                     doc.getDouble("numero"),
                     doc.getObjectId("_id").toHexString()
                ));
            }
        }
        return telefonesFormatados;
    }
}