package Main;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import Connection.MongoConnection; 

import java.util.List;

public class DataSeeder {

    public static void main(String[] args) {
        System.out.println("Iniciando o processo de semeadura de dados...");
        
        try {
            MongoDatabase database = MongoConnection.getDatabase();
            
            MongoCollection<Document> bloodTypesCollection = database.getCollection("tipos_sanguineo");
            
            if (bloodTypesCollection.countDocuments() == 0) {
                System.out.println("A coleção 'tipos_sanguineo' está vazia. Populando...");
                List<String> types = List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
                
                for (String type : types) {
                    bloodTypesCollection.insertOne(new Document("descricao", type));
                }
                System.out.println("-> Coleção 'tipos_sanguineo' populada com " + types.size() + " documentos.");
            } else {
                System.out.println("-> A coleção 'tipos_sanguineo' já contém dados. Nenhuma ação foi necessária.");
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro durante a semeadura de dados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            MongoConnection.close();
            System.out.println("\nProcesso de semeadura finalizado.");
        }
    }
}
