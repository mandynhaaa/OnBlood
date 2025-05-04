package Main;
import java.sql.*;
import javax.swing.*;

import Connection.ConnectionSQL;

public class ReportBloodStock {
	
	public static void create() {
        String nomeHemocentro = JOptionPane.showInputDialog("Digite o nome do hemocentro:");
        	
        String sql = "SELECT h.nome AS hemocentro, t.tipo, e.quantidade " +
                     "FROM Estoque e " +
                     "JOIN Hemocentro h ON e.id_hemocentro = h.id " +
                     "JOIN TipoSanguineo t ON e.id_tipo_sanguineo = t.id " +
                     "WHERE h.nome = ?";

        try (Connection conn = new ConnectionSQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeHemocentro);

            ResultSet rs = stmt.executeQuery();
            StringBuilder resultado = new StringBuilder("Estoque do hemocentro:\n");

            while (rs.next()) {
                resultado.append("Tipo: ").append(rs.getString("tipo"))
                         .append(" | Quantidade: ").append(rs.getInt("quantidade"))
                         .append("\n");
            }

            JOptionPane.showMessageDialog(null, resultado.length() > 0 ? resultado : "Nenhum dado encontrado.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
