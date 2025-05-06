package Controller;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Connection.ConnectionSQL;

public class BloodStockController {

    public static void atualizarEstoquePorDoacao(int idHemocentro, int idTipoSanguineo, int volume) {
        atualizarEstoque(idHemocentro, idTipoSanguineo, volume);
    }

    public void atualizarEstoquePorSolicitacao(int idHemocentro, int idTipoSanguineo, int volume) {
        atualizarEstoque(idHemocentro, idTipoSanguineo, -volume);
    }

    private static void atualizarEstoque(int idHemocentro, int idTipoSanguineo, int deltaVolume) {
        try (Connection conn = new ConnectionSQL().getConnection();) {
            String selectSql = "SELECT volume FROM Estoque WHERE id_hemocentro = ? AND id_tipo_sanguineo = ?";
            PreparedStatement psSelect = conn.prepareStatement(selectSql);
            psSelect.setInt(1, idHemocentro);
            psSelect.setInt(2, idTipoSanguineo);
            ResultSet rs = psSelect.executeQuery();

            if (rs.next()) {
                int volumeAtual = rs.getInt("volume");
                int novoVolume = volumeAtual + deltaVolume;
                if (novoVolume < 0) novoVolume = 0;

                String updateSql = "UPDATE Estoque SET volume = ?, data_atualizacao = ? WHERE id_hemocentro = ? AND id_tipo_sanguineo = ?";
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setInt(1, novoVolume);
                psUpdate.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                psUpdate.setInt(3, idHemocentro);
                psUpdate.setInt(4, idTipoSanguineo);
                psUpdate.executeUpdate();
                psUpdate.close();
            } else {
                if (deltaVolume > 0) {
                    String insertSql = "INSERT INTO Estoque (id_hemocentro, id_tipo_sanguineo, volume, data_atualizacao) VALUES (?, ?, ?, ?)";
                    PreparedStatement psInsert = conn.prepareStatement(insertSql);
                    psInsert.setInt(1, idHemocentro);
                    psInsert.setInt(2, idTipoSanguineo);
                    psInsert.setInt(3, deltaVolume);
                    psInsert.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    psInsert.executeUpdate();
                    psInsert.close();
                }
            }
            rs.close();
            psSelect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet listarEstoque() {
        try {
            Connection conn = new ConnectionSQL().getConnection();
            String sql = """
                SELECT 
                    e.*, 
                    ts.descricao AS tipo_sanguineo, 
                    h.razao_social 
                FROM 
                    Estoque e
                JOIN 
                    Tipo_Sanguineo ts ON e.id_tipo_sanguineo = ts.id_tipo_sanguineo
                JOIN 
                    Hemocentro h ON e.id_hemocentro = h.id_hemocentro
            """;
            return conn.prepareStatement(sql).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
