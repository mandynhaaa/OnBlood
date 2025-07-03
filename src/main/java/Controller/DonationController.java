package Controller;

import Main.Donation;
import Main.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DonationController {
    
    private ObjectId id_Donation, id_UserBloodCenter;
    private JTextField tf_volume, tf_datetime;
    private JComboBox<String> cb_status, cb_donors;

    public DonationController(ObjectId id_UserBloodCenter, JTextField tf_volume, JTextField tf_datetime, JComboBox<String> cb_status, JComboBox<String> cb_donors) {
        this.id_UserBloodCenter = id_UserBloodCenter;
        this.tf_volume = tf_volume;
        this.tf_datetime = tf_datetime;
        this.cb_status = cb_status;
        this.cb_donors = cb_donors;
    }

    public DonationController(ObjectId id_Donation, JTextField tf_volume, JTextField tf_datetime, JComboBox<String> cb_status) {
        this.id_Donation = id_Donation;
        this.tf_volume = tf_volume;
        this.tf_datetime = tf_datetime;
        this.cb_status = cb_status;
    }
    
    public DonationController() {
    }

    public void executeRegister() {
        try {
            float volume = Float.parseFloat(tf_volume.getText().replace(",", "."));
            LocalDateTime datetime = LocalDateTime.parse(tf_datetime.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String status = (String) cb_status.getSelectedItem();
            
            String selectedDonorString = (String) cb_donors.getSelectedItem();
            String donorIdHex = selectedDonorString.substring(selectedDonorString.indexOf('[') + 1, selectedDonorString.indexOf(']'));
            ObjectId donorId = new ObjectId(donorIdHex);

            Donation donation = new Donation(status, volume, datetime, donorId, this.id_UserBloodCenter);
            donation.create();

            JOptionPane.showMessageDialog(null, "Doação adicionada com sucesso!");

            User donorUser = new User(donorId);
            if (donorUser.getDonorInfo() != null) {
                BloodStockController.atualizarEstoque(this.id_UserBloodCenter, donorUser.getDonorInfo().getBloodType());
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "O volume deve ser um número válido.");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "A data e hora devem estar no formato: dd/MM/yyyy HH:mm:ss");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado ao registar doação: " + e.getMessage());
        }
    }

    public void executeUpdate() {
        try {
            Donation donation = new Donation(this.id_Donation);
            donation.setVolume(Float.parseFloat(tf_volume.getText().replace(",", ".")));
            donation.setDatetime(LocalDateTime.parse(tf_datetime.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            donation.setStatus((String) cb_status.getSelectedItem());
            
            donation.update();
            JOptionPane.showMessageDialog(null, "Doação alterada com sucesso!");
            
            User donorUser = new User(donation.getDonorId());
             if (donorUser.getDonorInfo() != null) {
                BloodStockController.atualizarEstoque(donation.getBloodCenterId(), donorUser.getDonorInfo().getBloodType());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar doação: " + e.getMessage());
        }
    }

    public void executeDelete(ObjectId donationId) {
        Donation donation = new Donation(donationId);
        donation.delete();
        JOptionPane.showMessageDialog(null, "Doação removida com sucesso!");
    }

    public List<String> listarDoadores() {
        List<String> doadores = new ArrayList<>();
        MongoCollection<Document> collection = new User().getCollection();
        try (MongoCursor<Document> cursor = collection.find(Filters.eq("tipo_usuario", "Doador")).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ObjectId id = doc.getObjectId("_id");
                String nome = doc.getString("nome");
                Document donorInfo = doc.get("doador_info", Document.class);
                String cpf = (donorInfo != null) ? donorInfo.getString("cpf") : "N/A";
                doadores.add(String.format("[%s] %s - %s", id.toHexString(), nome, cpf));
            }
        }
        return doadores;
    }
    
    public List<String> listarDoacoesPorHemocentro(ObjectId idHemocentro, String statusFiltro) {
        List<String> doacoesFormatadas = new ArrayList<>();
        MongoCollection<Document> collection = new Donation(null).getCollection();
        
        Bson filter = Filters.eq("id_hemocentro", idHemocentro);
        if (!"Todos".equals(statusFiltro)) {
            filter = Filters.and(filter, Filters.eq("status", statusFiltro));
        }

        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ObjectId idDoador = doc.getObjectId("id_doador");
                User doador = new User(idDoador); 

                doacoesFormatadas.add(String.format("[%s] Doador: %s | Status: %s | Volume: %.1fmL",
                        doc.getObjectId("_id").toHexString(),
                        (doador.getId() != null) ? doador.getName() : "N/A",
                        doc.getString("status"),
                        doc.getDouble("volume")
                ));
            }
        }
        return doacoesFormatadas;
    }

    public List<String> listarDoacoesPorDoador(ObjectId idDoador, String statusFiltro) {
        List<String> doacoesFormatadas = new ArrayList<>();
        MongoCollection<Document> collection = new Donation(null).getCollection();
        
        Bson filter = Filters.eq("id_doador", idDoador);
        if (!"Todos".equals(statusFiltro)) {
            filter = Filters.and(filter, Filters.eq("status", statusFiltro));
        }
        
        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                 Document doc = cursor.next();
                 ObjectId idHemocentro = doc.getObjectId("id_hemocentro");
                 User hemocentro = new User(idHemocentro);

                 doacoesFormatadas.add(String.format("[%s] Hemocentro: %s | Status: %s | Volume: %.1fmL",
                        doc.getObjectId("_id").toHexString(),
                        (hemocentro.getId() != null && hemocentro.getBloodCenterInfo() != null) ? hemocentro.getBloodCenterInfo().getCompanyName() : "N/A",
                        doc.getString("status"),
                        doc.getDouble("volume")
                ));
            }
        }
        return doacoesFormatadas;
    }
}