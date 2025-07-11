package Controller;

import Main.BloodStock;
import Main.Donation;
import Main.Request;
import Main.BloodType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators; 
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCursor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BloodStockController {
    
    private ObjectId id_BloodCenterUser;

    public BloodStockController(ObjectId id_BloodCenterUser) {
        this.id_BloodCenterUser = id_BloodCenterUser;
    }

	public MongoCursor<Document> listBloodCenterStocks() {
        MongoCollection<Document> bloodStockCollection = new BloodStock().getCollection();
        return bloodStockCollection.find(Filters.eq("id_hemocentro", this.id_BloodCenterUser)).iterator();
    }
    
    public void updateAllBloodStocksForCenter() {
        List<String> bloodTypes = new ArrayList<>();
        MongoCollection<Document> bloodTypeCollection = new BloodType("").getCollection();
        try (MongoCursor<Document> cursor = bloodTypeCollection.find().iterator()) {
            while (cursor.hasNext()) {
                bloodTypes.add(cursor.next().getString("descricao"));
            }
        }

        for (String type : bloodTypes) {
            atualizarEstoque(this.id_BloodCenterUser, type);
        }
    }

    public static void atualizarEstoque(ObjectId id_BloodCenter, String bloodType) {
        MongoCollection<Document> bloodStockCollection = new BloodStock().getCollection();


        List<Bson> donationAggregation = Arrays.asList(
            Aggregates.match(Filters.and(
                Filters.eq("id_hemocentro", id_BloodCenter),
                Filters.eq("status", "Realizada")
            )),
            Aggregates.lookup("usuarios", "id_doador", "_id", "doadorInfo"),
            Aggregates.unwind("$doadorInfo"),
            Aggregates.match(Filters.eq("doadorInfo.doador_info.tipo_sanguineo", bloodType)),
            Aggregates.group(null, Accumulators.sum("total", "$volume"))
        );
        
        Document totalDonatedDoc = new Donation(null).getCollection().aggregate(donationAggregation).first();
        double totalDonated = (totalDonatedDoc != null) ? totalDonatedDoc.get("total", Number.class).doubleValue() : 0.0;

        List<Bson> requestAggregation = Arrays.asList(
             Aggregates.match(Filters.and(
                Filters.eq("id_hemocentro", id_BloodCenter),
                Filters.eq("tipo_sanguineo", bloodType),
                Filters.eq("status", "Realizada")
            )),
            Aggregates.group(null, Accumulators.sum("total", "$volume"))
        );

        Document totalRequestedDoc = new Request(null).getCollection().aggregate(requestAggregation).first();
        double totalRequested = (totalRequestedDoc != null) ? totalRequestedDoc.get("total", Number.class).doubleValue() : 0.0;
        
        double volumeFinal = totalDonated - totalRequested;
        if (volumeFinal < 0) volumeFinal = 0;

        Bson estoqueFilter = Filters.and(Filters.eq("id_hemocentro", id_BloodCenter), Filters.eq("tipo_sanguineo", bloodType));
        
        Document updateDoc = new Document("$set", 
                new Document("volume", volumeFinal)
                .append("data_atualizacao", LocalDateTime.now())
                .append("id_hemocentro", id_BloodCenter)
                .append("tipo_sanguineo", bloodType)
            );

        UpdateOptions options = new UpdateOptions().upsert(true);
        bloodStockCollection.updateOne(estoqueFilter, updateDoc, options);
    }
}