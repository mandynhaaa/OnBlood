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
    
    private ObjectId idUsuarioHemocentro;

    public BloodStockController(ObjectId idUsuarioHemocentro) {
        this.idUsuarioHemocentro = idUsuarioHemocentro;
    }

    public MongoCursor<Document> listarEstoqueHemocentro() {
        MongoCollection<Document> estoqueCollection = new BloodStock().getCollection();
        return estoqueCollection.find(Filters.eq("id_hemocentro", this.idUsuarioHemocentro)).iterator();
    }
    
    public static void atualizarEstoque(ObjectId bloodCenterId, String bloodType) {
        MongoCollection<Document> estoqueCollection = new BloodStock().getCollection();

        List<Bson> donationAggregation = Arrays.asList(
            Aggregates.lookup("usuarios", "id_doador", "_id", "doador_info"),
            Aggregates.unwind("$doador_info"),
            Aggregates.match(Filters.and(
                Filters.eq("id_hemocentro", bloodCenterId),
                Filters.eq("status", "Realizada"),
                Filters.eq("doador_info.doador_info.tipo_sanguineo", bloodType)
            )),
            Aggregates.group(null, Accumulators.sum("total", "$volume"))
        );
        Document totalDoadoDoc = new Donation(null).getCollection().aggregate(donationAggregation).first();
        float totalDoado = (totalDoadoDoc != null) ? totalDoadoDoc.get("total", Number.class).floatValue() : 0.0f;

        List<Bson> requestAggregation = Arrays.asList(
             Aggregates.match(Filters.and(
                Filters.eq("id_hemocentro", bloodCenterId),
                Filters.eq("tipo_sanguineo", bloodType),
                Filters.eq("status", "Realizada")
            )),
            Aggregates.group(null, Accumulators.sum("total", "$volume"))
        );
        Document totalSolicitadoDoc = new Request(null).getCollection().aggregate(requestAggregation).first();
        float totalSolicitado = (totalSolicitadoDoc != null) ? totalSolicitadoDoc.get("total", Number.class).floatValue() : 0.0f;
        
        float volumeFinal = totalDoado - totalSolicitado;
        if (volumeFinal < 0) volumeFinal = 0;

        Bson estoqueFilter = Filters.and(Filters.eq("id_hemocentro", bloodCenterId), Filters.eq("tipo_sanguineo", bloodType));
        
        Document updateDoc = new Document("$set", 
                new Document("volume", (double) volumeFinal)
                .append("data_atualizacao", LocalDateTime.now())
                .append("id_hemocentro", bloodCenterId)
                .append("tipo_sanguineo", bloodType)
            );

        UpdateOptions options = new UpdateOptions().upsert(true);
        estoqueCollection.updateOne(estoqueFilter, updateDoc, options);
    }
}