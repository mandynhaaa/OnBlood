package View.BloodStock;

import Controller.BloodStockController;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date; 

public class ManagerBloodStock extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    private BloodStockController controller; 
    private ObjectId idUsuarioHemocentro;

    public ManagerBloodStock(ObjectId idUsuarioHemocentro) {
        this.idUsuarioHemocentro = idUsuarioHemocentro;
        
        this.controller = new BloodStockController(this.idUsuarioHemocentro);

        setTitle("Visualizar Estoque");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
                "Tipo Sanguíneo", "Volume (mL)", "Última Atualização"
        });

        tabela = new JTable(modelo);
        tabela.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnAtualizar = new JButton("Atualizar Estoque");
        
        btnAtualizar.addActionListener(e -> {
            controller.updateAllBloodStocksForCenter();
            carregarEstoque();
            JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!");
        });
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarEstoque();
    }

    private void carregarEstoque() {
        modelo.setRowCount(0);
        
        try (MongoCursor<Document> cursor = controller.listBloodCenterStocks()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                Date dateFromDb = doc.get("data_atualizacao", Date.class);
                String dataFormatada = "N/A";
                if (dateFromDb != null) {
                    LocalDateTime data = dateFromDb.toInstant()
                      .atZone(ZoneId.systemDefault())
                      .toLocalDateTime();
                    dataFormatada = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                }
                
                modelo.addRow(new Object[]{
                        doc.getString("tipo_sanguineo"),
                        doc.getDouble("volume"),
                        dataFormatada
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar estoque: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}