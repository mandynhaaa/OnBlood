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
    
    private ObjectId id_Donation;
    private ObjectId id_UserBloodCenter;
    private JTextField tf_volume;
    private JFormattedTextField tf_datetime;
    private JComboBox<String> cb_status;
    private JComboBox<String> cb_donors;

    public DonationController(
		ObjectId id_UserBloodCenter, 
		JTextField tf_volume, 
		JFormattedTextField tf_datetime,
		JComboBox<String> cb_status, 
		JComboBox<String> cb_donors
    ) {
        this.id_UserBloodCenter = id_UserBloodCenter;
        this.tf_volume = tf_volume;
        this.tf_datetime = tf_datetime;
        this.cb_status = cb_status;
        this.cb_donors = cb_donors;
    }

    public DonationController(
    	ObjectId id_Donation, 
    	JTextField tf_volume, 
    	JFormattedTextField tf_datetime,
    	JComboBox<String> cb_status
    ) {
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
            
            String selectedDonorName = (String) cb_donors.getSelectedItem();
            User user = new User(selectedDonorName, "");
            User donorData = user.searchByName();

            if (donorData == null) {
                JOptionPane.showMessageDialog(null, "Doador selecionado não encontrado no sistema.");
                return;
            }
            ObjectId donorId = donorData.getId();

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

    public void executeDelete(ObjectId idDonation) {
        try {
            Donation donation = new Donation(idDonation);
            donation.delete();
            JOptionPane.showMessageDialog(null, "Doação removida com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir doação: " + e.getMessage());
        }
    }

    public List<String> listDonors() {
        List<String> doadores = new ArrayList<>();
        MongoCollection<Document> collection = new User().getCollection();
        try (MongoCursor<Document> cursor = collection.find(Filters.eq("tipo_usuario", "Doador")).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                doadores.add(doc.getString("nome"));
            }
        }
        return doadores;
    }
    
    public List<String> listBloodCenterDonations(ObjectId idHemocentro, String statusFiltro) {
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
                
                String bloodType = (doador.getId() != null && doador.getDonorInfo() != null) ? doador.getDonorInfo().getBloodType() : "N/A";

                doacoesFormatadas.add(String.format("Doador: %s | Tipo: %s | Status: %s | Volume: %.1fmL [%s]",
                        (doador.getId() != null) ? doador.getName() : "N/A",
                        bloodType,
                        doc.getString("status"),
                        doc.getDouble("volume"),
                        doc.getObjectId("_id").toHexString()
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

                 doacoesFormatadas.add(String.format("Hemocentro: %s | Status: %s | Volume: %.1fmL [%s]",
                        (hemocentro.getId() != null && hemocentro.getBloodCenterInfo() != null) ? hemocentro.getBloodCenterInfo().getCompanyName() : "N/A",
                        doc.getString("status"),
                        doc.getDouble("volume"),
                        doc.getObjectId("_id").toHexString()
                ));
            }
        }
        return doacoesFormatadas;
    }
}