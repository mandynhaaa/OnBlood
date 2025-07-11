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
    private JComboBox<String> cb_tipoSanguineo;
    private JComboBox<String> cb_status;
    
    public RequestController(ObjectId id_Request, ObjectId id_UserBloodCenter, JTextField tf_volume, JTextField tf_datetime, JComboBox<String> cb_tipoSanguineo, JComboBox<String> cb_status) {
        this.id_Request = id_Request;
        this.id_UserBloodCenter = id_UserBloodCenter;
        this.tf_volume = tf_volume;
        this.tf_datetime = tf_datetime;
        this.cb_tipoSanguineo = cb_tipoSanguineo;
        this.cb_status = cb_status;
    }
    
    public void executeRegister() {
        try {
            float volume = Float.parseFloat(tf_volume.getText().replace(",", "."));
            LocalDateTime datetime = LocalDateTime.parse(tf_datetime.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String status = (String) cb_status.getSelectedItem();
            String bloodType = (String) cb_tipoSanguineo.getSelectedItem();

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
            request.setBloodType((String) cb_tipoSanguineo.getSelectedItem());
            
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
    
    public List<String> listarTiposSanguineos() {
        List<String> tipos = new ArrayList<>();
        try (MongoCursor<Document> cursor = new BloodType("").getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                tipos.add(cursor.next().getString("descricao"));
            }
        }
        return tipos;
    }

    public List<String> listarSolicitacoesPorHemocentro(ObjectId idHemocentro) {
        List<String> solicitacoesFormatadas = new ArrayList<>();
        MongoCollection<Document> collection = new Request(null).getCollection();
        
        try (MongoCursor<Document> cursor = collection.find(Filters.eq("id_hemocentro", idHemocentro)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                solicitacoesFormatadas.add(String.format("[%s] Tipo: %s | Status: %s | Volume: %.1fmL",
                        doc.getObjectId("_id").toHexString(),
                        doc.getString("tipo_sanguineo"),
                        doc.getString("status"),
                        doc.getDouble("volume")
                ));
            }
        }
        return solicitacoesFormatadas;
    }
}