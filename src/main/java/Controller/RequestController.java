package Controller;

import Main.Request;
import Main.User;
import Main.BloodType;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class RequestController {
    private ObjectId id_Request;
    private ObjectId id_UserBloodCenter;
    private JTextField tf_volume;
    private JTextField tf_datetime;
    private JComboBox<String> cb_bloodTypesanguineo;
    private JComboBox<String> cb_status;
    
    public RequestController(
    	ObjectId id_Request, 
    	ObjectId id_UserBloodCenter, 
    	JTextField tf_volume, 
    	JTextField tf_datetime, 
    	JComboBox<String> cb_bloodTypesanguineo, 
    	JComboBox<String> cb_status
    ) {
        this.id_Request = id_Request;
        this.id_UserBloodCenter = id_UserBloodCenter;
        this.tf_volume = tf_volume;
        this.tf_datetime = tf_datetime;
        this.cb_bloodTypesanguineo = cb_bloodTypesanguineo;
        this.cb_status = cb_status;
    }
    
    public void executeRegister() {
        try {
            float volume = Float.parseFloat(tf_volume.getText().replace(",", "."));
            LocalDateTime datetime = LocalDateTime.parse(tf_datetime.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String status = (String) cb_status.getSelectedItem();
            String bloodType = (String) cb_bloodTypesanguineo.getSelectedItem();

            Request request = new Request(status, volume, datetime, this.id_UserBloodCenter, bloodType);
            request.create();

            JOptionPane.showMessageDialog(null, "Solicitação adicionada com sucesso!");

            BloodStockController.atualizarEstoque(this.id_UserBloodCenter, bloodType);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "O volume deve ser um número válido.");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "A data e hora devem estar no formato: dd/MM/yyyy HH:mm:ss");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage());
        }
    }
    
    public void executeUpdate() {
        try {
            Request request = new Request(this.id_Request);
            request.setVolume(Float.parseFloat(tf_volume.getText().replace(",", ".")));
            request.setDatetime(LocalDateTime.parse(tf_datetime.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            request.setStatus((String) cb_status.getSelectedItem());
            request.setBloodType((String) cb_bloodTypesanguineo.getSelectedItem());
            
            request.update();
            JOptionPane.showMessageDialog(null, "Solicitação alterada com sucesso!");
            
            BloodStockController.atualizarEstoque(request.getBloodCenterId(), request.getBloodType());
            
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Erro ao atualizar solicitação: " + e.getMessage());
        }
    }

    public void executeDelete() {
        try {
            Request request = new Request(id_Request);
            request.delete();
            JOptionPane.showMessageDialog(null, "Solicitação removida com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public List<String> listBloodTypes() {
        List<String> bloodTypes = new ArrayList<>();
        try (MongoCursor<Document> cursor = new BloodType("").getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                bloodTypes.add(cursor.next().getString("descricao"));
            }
        }
        return bloodTypes;
    }

    public List<String> listBloodCenterRequests(ObjectId idHemocentro) {
        List<String> formatedResquests = new ArrayList<>();
        MongoCollection<Document> collection = new Request(null).getCollection();
        
        try (MongoCursor<Document> cursor = collection.find(Filters.eq("id_Hemocentro", idHemocentro)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                formatedResquests.add(String.format("[%s] Tipo: %s | Status: %s | Volume: %.1fmL",
                        doc.getObjectId("_id").toHexString(),
                        doc.getString("tipo_Sanguineo"),
                        doc.getString("status"),
                        doc.getDouble("volume")
                ));
            }
        }
        return formatedResquests;
    }
}