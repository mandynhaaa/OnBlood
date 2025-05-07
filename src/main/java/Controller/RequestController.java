package Controller;

import Connection.ConnectionSQL;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestController {

    public boolean cadastrarSolicitacao(String hemocentro, int idTipoSanguineo, String status, int volume, String dataHoraTexto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraTexto, formatter);

            int idHemocentro = buscarIdHemocentroPorNome(hemocentro);
            if (idHemocentro == -1) {
                System.err.println("Hemocentro não encontrado.");
                return false;
            }

            Connection conn = new ConnectionSQL().getConnection();
            String sql = "INSERT INTO Solicitacao (id_hemocentro, id_tipo_sanguineo, status, volume, data_hora) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHemocentro);
            stmt.setInt(2, idTipoSanguineo);
            stmt.setString(3, status);
            stmt.setInt(4, volume);
            stmt.setObject(5, dataHora);

            int rowsInserted = stmt.executeUpdate();

            if (status.equalsIgnoreCase("Realizada")) {
                BloodStockController estoqueController = new BloodStockController();
                estoqueController.atualizarEstoquePorSolicitacao(idHemocentro, idTipoSanguineo, volume);
            }

            stmt.close();
            conn.close();

            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listarSolicitacoes() {
        List<String> solicitacoes = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = """
                SELECT s.id_solicitacao, h.razao_social, ts.descricao AS tipo_sanguineo, s.status, s.volume, s.data_hora
                FROM Solicitacao s
                JOIN Hemocentro h ON s.id_hemocentro = h.id_hemocentro
                JOIN Tipo_Sanguineo ts ON s.id_tipo_sanguineo = ts.id_tipo_sanguineo
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_solicitacao");
                String hemocentro = rs.getString("razao_social");
                String tipo = rs.getString("tipo_sanguineo");
                String status = rs.getString("status");
                int volume = rs.getInt("volume");
                String dataHora = rs.getString("data_hora");

                solicitacoes.add("[" + id + "] " + hemocentro + " | " + tipo + " | " + status + " | " + volume + "mL | " + dataHora);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }
    
    public boolean atualizarSolicitacao(int idSolicitacao, String status, int volume, String dataHoraTexto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraTexto, formatter);

            Connection conn = new ConnectionSQL().getConnection();
            String sql = "UPDATE Solicitacao SET status = ?, volume = ?, data_hora = ? WHERE id_solicitacao = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, volume);
            stmt.setObject(3, dataHora);
            stmt.setInt(4, idSolicitacao);

            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            conn.close();

            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirSolicitacao(int id) {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "DELETE FROM Solicitacao WHERE id_solicitacao = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            stmt.close();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int buscarIdHemocentroPorNome(String nome) {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT id_hemocentro FROM Hemocentro WHERE razao_social = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            int id = -1;
            if (rs.next()) {
                id = rs.getInt("id_hemocentro");
            }
            rs.close();
            stmt.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public String buscarHemocentroPorID(int id) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT razao_social FROM hemocentro WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            String nome = "";
            if (rs.next()) {
            	nome = rs.getString("razao_social");
            }
            rs.close();
            stmt.close();
            conn.close();
            return nome;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<String> listarTiposSanguineos() {
        List<String> tipos = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT id_tipo_sanguineo, descricao FROM Tipo_Sanguineo";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_tipo_sanguineo");
                String desc = rs.getString("descricao");
                tipos.add("[" + id + "] " + desc);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tipos;
    }

    public List<String> listarHemocentros() {
        List<String> hemocentros = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT razao_social FROM Hemocentro";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hemocentros.add(rs.getString("razao_social"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hemocentros;
    }
    
    public List<String> listarSolicitacoesPorHemocentro(int idUsuario) {
        List<String> solicitacoes = new ArrayList<>();
        try {
            Connection conn = new ConnectionSQL().getConnection();

            String sqlHemocentro = "SELECT id_Hemocentro FROM hemocentro WHERE id_Usuario = ?";
            PreparedStatement stmtHemocentro = conn.prepareStatement(sqlHemocentro);
            stmtHemocentro.setInt(1, idUsuario);
            ResultSet rsHemocentro = stmtHemocentro.executeQuery();

            int idHemocentro = -1;
            if (rsHemocentro.next()) {
                idHemocentro = rsHemocentro.getInt("id_Hemocentro");
            }

            rsHemocentro.close();
            stmtHemocentro.close();

            if (idHemocentro == -1) {
                conn.close();
                return solicitacoes; // retorna vazio se não encontrar o hemocentro
            }

            String sql = """
            	    SELECT d.id_Solicitacao,
            	           d.status,
            	           ts.descricao AS tipo_Sanguineo,
            	           h.razao_Social AS hemocentro,
            	           d.volume,
            	           d.data_Hora
            	    FROM solicitacao d
            	    JOIN tipo_Sanguineo ts ON d.id_Tipo_Sanguineo = ts.id_Tipo_Sanguineo
            	    JOIN hemocentro h ON d.id_Hemocentro = h.id_Hemocentro
            	    WHERE d.id_Hemocentro = ?
            	""";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHemocentro);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_solicitacao");
                String tipo = rs.getString("tipo_sanguineo");
                String hemocentro = rs.getString("hemocentro");
                String status = rs.getString("status");
                int volume = rs.getInt("volume");
                String dataHora = rs.getString("data_hora");

                solicitacoes.add("[" + id + "] " + " (" + tipo + ") | " + hemocentro + " | " + status + " | " + volume + "mL | " + dataHora);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }
    
    
}
