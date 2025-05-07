package Controller;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Connection.ConnectionSQL;

public class BloodStockController {

    public static void atualizarEstoquePorDoacao(int idHemocentro, int idTipoSanguineo, Float volume) {
        atualizarEstoque(idHemocentro, idTipoSanguineo, volume);
    }

    public void atualizarEstoquePorSolicitacao(int idHemocentro, int idTipoSanguineo, Float volume) {
        atualizarEstoque(idHemocentro, idTipoSanguineo, -volume);
    }

    private static void atualizarEstoque(int idHemocentro, int idTipoSanguineo, Float deltaVolume) {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String totalSql = "SELECT SUM(volume) as total FROM doacao "
            		+ "INNER JOIN doador ON doador.id_Doador = doacao.id_Doador "
            		+ "WHERE id_Hemocentro = ? AND id_Tipo_Sanguineo = ?";
            PreparedStatement psTotal = conn.prepareStatement(totalSql);
            psTotal.setInt(1, idHemocentro);
            psTotal.setInt(2, idTipoSanguineo);
            ResultSet rsTotal = psTotal.executeQuery();

            float volumeTotal = 0f;
            if (rsTotal.next()) {
                volumeTotal = rsTotal.getFloat("total");
            }

            rsTotal.close();
            psTotal.close();

            String selectSql = "SELECT id_Estoque FROM estoque WHERE id_Hemocentro = ? AND id_Tipo_Sanguineo = ?";
            PreparedStatement psSelect = conn.prepareStatement(selectSql);
            psSelect.setInt(1, idHemocentro);
            psSelect.setInt(2, idTipoSanguineo);
            ResultSet rs = psSelect.executeQuery();

            String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (rs.next()) {
                String updateSql = "UPDATE estoque SET volume = ?, data_Atualizacao = ? WHERE id_Hemocentro = ? AND id_Tipo_Sanguineo = ?";
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setFloat(1, volumeTotal);
                psUpdate.setString(2, dataAtual);
                psUpdate.setInt(3, idHemocentro);
                psUpdate.setInt(4, idTipoSanguineo);
                psUpdate.executeUpdate();
                psUpdate.close();
            } else {
                if (volumeTotal > 0) {
                    String insertSql = "INSERT INTO estoque (id_Hemocentro, id_Tipo_Sanguineo, volume, data_Atualizacao) VALUES (?, ?, ?, ?)";
                    PreparedStatement psInsert = conn.prepareStatement(insertSql);
                    psInsert.setInt(1, idHemocentro);
                    psInsert.setInt(2, idTipoSanguineo);
                    psInsert.setFloat(3, volumeTotal);
                    psInsert.setString(4, dataAtual);
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
