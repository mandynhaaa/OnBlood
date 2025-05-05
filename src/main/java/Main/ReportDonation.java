package Main;
import java.sql.*;
import javax.swing.*;

import Connection.ConnectionSQL;


public class ReportDonation {

	    public static void create() {
	        String cpfDoador = JOptionPane.showInputDialog("Digite o CPF do doador:");
	        String dataInicio = JOptionPane.showInputDialog("Digite a data de início (YYYY-MM-DD):");
	        String dataFim = JOptionPane.showInputDialog("Digite a data de fim (YYYY-MM-DD):");

	        String sql = "SELECT d.nome, do.data_doacao, h.nome AS hemocentro " +
	                     "FROM Doacao do " +
	                     "JOIN Doador d ON do.id_doador = d.id " +
	                     "JOIN Hemocentro h ON do.id_hemocentro = h.id " +
	                     "WHERE d.cpf = ? AND do.data_doacao BETWEEN ? AND ?";

	        try (Connection conn = new ConnectionSQL().getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	        	
	            stmt.setString(1, cpfDoador);
	            stmt.setString(2, dataInicio);
	            stmt.setString(3, dataFim);

	            ResultSet rs = stmt.executeQuery();
	            StringBuilder resultado = new StringBuilder();

	            while (rs.next()) {
	                resultado.append("Doador: ").append(rs.getString("nome"))
	                         .append(" | Data: ").append(rs.getDate("data_doacao"))
	                         .append(" | Hemocentro: ").append(rs.getString("hemocentro"))
	                         .append("\n");
	            }

	            JOptionPane.showMessageDialog(null, resultado.length() > 0 ? resultado : "Nenhuma doação encontrada.");
	        } catch (SQLException e) {
	             e.printStackTrace();
	        }
	    }
}
