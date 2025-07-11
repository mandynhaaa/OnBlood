package Controller;

import Main.BloodStock;
import Main.Donation;
import Main.Request;
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
import java.util.Arrays;
import java.util.List;

public class BloodStockController {
    
    private ObjectId id_BloodCenterUser;

    public BloodStockController(ObjectId id_BloodCenterUser) {
        this.id_BloodCenterUser = id_BloodCenterUser;
    }

	public MongoCursor<Document> listBloodCenterStocks() {
        MongoCollection<Document> bloodStockCollection = new BloodStock().getCollection();
        return bloodStockCollection.find(Filters.eq("id_Hemocentro", this.id_BloodCenterUser)).iterator();
    }
    
    public static void atualizarEstoque(ObjectId id_BloodCenter, String bloodType) {
        MongoCollection<Document> bloodStockCollection = new BloodStock().getCollection();

        List<Bson> donationAggregation = Arrays.asList(
            Aggregates.lookup("usuarios", "id_doador", "_id", "doador_Info"),
            Aggregates.unwind("$doador_Info"),
            Aggregates.match(Filters.and(
                Filters.eq("id_Hemocentro", id_BloodCenter),
                Filters.eq("status", "Realizada"),
                Filters.eq("doador_Info.doador_Info.tipo_Sanguineo", bloodType)
            )),
            Aggregates.group(null, Accumulators.sum("total", "$volume"))
        );
        Document totalDonatedDoc = new Donation(null).getCollection().aggregate(donationAggregation).first();
        float totalDonated = (totalDonatedDoc != null) ? totalDonatedDoc.get("total", Number.class).floatValue() : 0.0f;

        List<Bson> requestAggregation = Arrays.asList(
             Aggregates.match(Filters.and(
                Filters.eq("id_Hemocentro", id_BloodCenter),
                Filters.eq("tipo_Sanguineo", bloodType),
                Filters.eq("status", "Realizada")
            )),
            Aggregates.group(null, Accumulators.sum("total", "$volume"))
        );
        Document totalRequestedDoc = new Request(null).getCollection().aggregate(requestAggregation).first();
        float totalRequested = (totalRequestedDoc != null) ? totalRequestedDoc.get("total", Number.class).floatValue() : 0.0f;
        
        float volumeFinal = totalDonated - totalRequested;
        if (volumeFinal < 0) volumeFinal = 0;

        Bson estoqueFilter = Filters.and(Filters.eq("id_Hemocentro", id_BloodCenter), Filters.eq("tipo_Sanguineo", bloodType));
        
        Document updateDoc = new Document("$set", 
                new Document("volume", (double) volumeFinal)
                .append("data_Atualizacao", LocalDateTime.now())
                .append("id_Hemocentro", id_BloodCenter)
                .append("tipo_Sanguineo", bloodType)
            );

        UpdateOptions options = new UpdateOptions().upsert(true);
        bloodStockCollection.updateOne(estoqueFilter, updateDoc, options);
    }
}