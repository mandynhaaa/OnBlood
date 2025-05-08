package Controller;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Connection.ConnectionSQL;
import Main.BloodCenter;

public class BloodStockController {
	private int id_Usuario;
	
	public BloodStockController(int id_Usuario) {
		this.id_Usuario = id_Usuario;
	}
	
	public BloodStockController() {
	}

	public static void atualizarEstoque(int idHemocentro, int idTipoSanguineo) {
	    try (Connection conn = new ConnectionSQL().getConnection()) {

	        String sqlDoacoes = """
	            SELECT SUM(d.volume) AS total_doado
	            FROM doacao d
	            INNER JOIN doador dr ON dr.id_Doador = d.id_Doador
	            WHERE d.id_Hemocentro = ? AND dr.id_Tipo_Sanguineo = ? AND d.status = "Realizada"
	        """;
	        PreparedStatement psDoacoes = conn.prepareStatement(sqlDoacoes);
	        psDoacoes.setInt(1, idHemocentro);
	        psDoacoes.setInt(2, idTipoSanguineo);
	        ResultSet rsDoacoes = psDoacoes.executeQuery();

	        float totalDoado = 0f;
	        if (rsDoacoes.next()) {
	            totalDoado = rsDoacoes.getFloat("total_doado");
	        }
	        rsDoacoes.close();
	        psDoacoes.close();

	        String sqlSolicitacoes = """
	            SELECT SUM(s.volume) AS total_solicitado
	            FROM solicitacao s
	            WHERE s.id_Hemocentro = ? AND s.id_Tipo_Sanguineo = ? AND s.status = "Realizada"
	        """;
	        PreparedStatement psSolicitacoes = conn.prepareStatement(sqlSolicitacoes);
	        psSolicitacoes.setInt(1, idHemocentro);
	        psSolicitacoes.setInt(2, idTipoSanguineo);
	        ResultSet rsSolicitacoes = psSolicitacoes.executeQuery();

	        float totalSolicitado = 0f;
	        if (rsSolicitacoes.next()) {
	            totalSolicitado = rsSolicitacoes.getFloat("total_solicitado");
	        }
	        rsSolicitacoes.close();
	        psSolicitacoes.close();

	        float volumeFinal = totalDoado - totalSolicitado;
	        if (volumeFinal < 0) volumeFinal = 0;

	        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	        String selectSql = "SELECT id_Estoque FROM estoque WHERE id_Hemocentro = ? AND id_Tipo_Sanguineo = ?";
	        PreparedStatement psSelect = conn.prepareStatement(selectSql);
	        psSelect.setInt(1, idHemocentro);
	        psSelect.setInt(2, idTipoSanguineo);
	        ResultSet rs = psSelect.executeQuery();

	        if (rs.next()) {
	            String updateSql = "UPDATE estoque SET volume = ?, data_Atualizacao = ? WHERE id_Hemocentro = ? AND id_Tipo_Sanguineo = ?";
	            PreparedStatement psUpdate = conn.prepareStatement(updateSql);
	            psUpdate.setFloat(1, volumeFinal);
	            psUpdate.setString(2, dataAtual);
	            psUpdate.setInt(3, idHemocentro);
	            psUpdate.setInt(4, idTipoSanguineo);
	            psUpdate.executeUpdate();
	            psUpdate.close();
	        } else if (volumeFinal > 0) {
	            String insertSql = "INSERT INTO estoque (id_Hemocentro, id_Tipo_Sanguineo, volume, data_Atualizacao) VALUES (?, ?, ?, ?)";
	            PreparedStatement psInsert = conn.prepareStatement(insertSql);
	            psInsert.setInt(1, idHemocentro);
	            psInsert.setInt(2, idTipoSanguineo);
	            psInsert.setFloat(3, volumeFinal);
	            psInsert.setString(4, dataAtual);
	            psInsert.executeUpdate();
	            psInsert.close();
	        }

	        rs.close();
	        psSelect.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    public ResultSet listarEstoqueHemocentro() {
    	BloodCenter bloodCenter = buscarHemocentroPorIdUsuario(id_Usuario);
    	
        try {
            Connection conn = new ConnectionSQL().getConnection();
            String sql = """
                SELECT 
                    e.*, 
                    ts.descricao AS tipo_sanguineo, 
                    h.razao_social 
                FROM 
                    estoque e
                JOIN 
                    tipo_Sanguineo ts ON e.id_tipo_sanguineo = ts.id_tipo_sanguineo
                JOIN 
                    hemocentro h ON e.id_hemocentro = h.id_hemocentro
                WHERE h.id_Hemocentro = ?
            """;
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bloodCenter.getId());
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public BloodCenter buscarHemocentroPorIdUsuario(int id) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT id_Hemocentro FROM hemocentro WHERE id_Usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            
            int id_Hemocentro = 0;
            if (rs.next()) {
            	id_Hemocentro = Integer.parseInt(rs.getString("id_Hemocentro"));
            }
            rs.close();
            stmt.close();
            conn.close();
            
            return new BloodCenter(id_Hemocentro);
        } catch (Exception e) {
            e.printStackTrace();
            return new BloodCenter(0);
        }
    }
}
