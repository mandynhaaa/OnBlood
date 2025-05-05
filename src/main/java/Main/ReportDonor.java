package Main;
import java.sql.*;
import javax.swing.*;

import Connection.ConnectionSQL;

public class ReportDonor {
	
	public static void create() {
        String dataInicio = JOptionPane.showInputDialog("Digite a data de início (YYYY-MM-DD):");
        String dataFim = JOptionPane.showInputDialog("Digite a data de fim (YYYY-MM-DD):");

        String sql = "SELECT d.nome, d.cpf, COUNT(*) AS total_doacoes " +
                     "FROM Doacao do " +
                     "JOIN Doador d ON do.id_doador = d.id " +
                     "WHERE do.data_doacao BETWEEN ? AND ? " +
                     "GROUP BY d.nome, d.cpf " +
                     "ORDER BY total_doacoes DESC " +
                     "LIMIT 1";

        try (Connection conn = new ConnectionSQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dataInicio);
            stmt.setString(2, dataFim);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String resultado = "Maior doador no período:\n" +
                                   "Nome: " + rs.getString("nome") + "\n" +
                                   "CPF: " + rs.getString("cpf") + "\n" +
                                   "Total de Doações: " + rs.getInt("total_doacoes");
                JOptionPane.showMessageDialog(null, resultado);
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma doação encontrada no período.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
