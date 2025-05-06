package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Connection.ConnectionSQL;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DonationController {

    public boolean cadastrarDoacao(int idDoador, String hemocentroNome, String status, int volume, String dataHoraTexto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraTexto, formatter);

            int idHemocentro = buscarIdHemocentroPorNome(hemocentroNome);
            if (idHemocentro == -1) {
                System.err.println("Hemocentro não encontrado.");
                return false;
            }

            Connection conn = new ConnectionSQL().getConnection();
            String sql = "INSERT INTO doacao (id_Doador, id_Hemocentro, status, volume, data_Hora) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idDoador);
            stmt.setInt(2, idHemocentro);
            stmt.setString(3, status);
            stmt.setInt(4, volume);
            stmt.setObject(5, dataHora);
            int rowsInserted = stmt.executeUpdate();

            if (status.equalsIgnoreCase("Realizada")) {
                int idTipoSanguineo = obterTipoSanguineoPorDoador(idDoador);
                if (idTipoSanguineo != -1) {
                	BloodStockController estoqueController = new BloodStockController();
                	BloodStockController.atualizarEstoquePorDoacao(idHemocentro, idTipoSanguineo, volume);
                }
            }
            
            stmt.close();
            conn.close();

            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int buscarIdHemocentroPorNome(String nome) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT id_Hemocentro FROM hemocentro WHERE razao_Social = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            var rs = stmt.executeQuery();
            int id = -1;
            if (rs.next()) {
                id = rs.getInt("id_Hemocentro");
            }
            rs.close();
            stmt.close();
            conn.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public List<String> listarHemocentros() {
        List<String> hemocentros = new ArrayList<>();
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT razao_Social FROM hemocentro";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hemocentros.add(rs.getString("razao_Social"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hemocentros;
    }
    
    public List<String> listarDoadores() {
        List<String> doadores = new ArrayList<>();
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT d.id_Doador, u.nome FROM doador d JOIN usuario u ON d.id_Usuario = u.id_Usuario";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_doador");
                String nome = rs.getString("nome");
                doadores.add("[" + id + "] " + nome);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doadores;
    }
    
    public List<String> listarDoacoes() {
        List<String> doacoes = new ArrayList<>();
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = """
                SELECT d.id_Doacao,
                       u.nome AS nome_Doador,
                       ts.descricao AS tipo_Sanguineo,
                       h.razao_Social AS hemocentro,
                       d.status,
                       d.volume,
                       d.data_Hora
                FROM doacao d
                JOIN doador dd ON d.id_Doador = dd.id_Doador
                JOIN usuario u ON dd.id_Usuario = u.id_Usuario
                JOIN tipo_Sanguineo ts ON dd.id_Tipo_Sanguineo = ts.id_Tipo_Sanguineo
                JOIN hemocentro h ON d.id_Hemocentro = h.id_Hemocentro
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_doacao");
                String nome = rs.getString("nome_doador");
                String tipo = rs.getString("tipo_sanguineo");
                String hemocentro = rs.getString("hemocentro");
                String status = rs.getString("status");
                int volume = rs.getInt("volume");
                String dataHora = rs.getString("data_hora");

                doacoes.add("[" + id + "] " + nome + " (" + tipo + ") | " + hemocentro + " | " + status + " | " + volume + "mL | " + dataHora);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacoes;
    }
    
    public boolean atualizarDoacao(int idDoacao, String status, int volume, String dataHoraTexto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraTexto, formatter);

            Connection conn = new ConnectionSQL().getConnection();
            String sql = "UPDATE doacao SET status = ?, volume = ?, data_Hora = ? WHERE id_Doacao = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, volume);
            stmt.setObject(3, dataHora);
            stmt.setInt(4, idDoacao);

            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            conn.close();

            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean excluirDoacao(int idDoacao) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "DELETE FROM doacao WHERE id_Doacao = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idDoacao);
            int rowsDeleted = stmt.executeUpdate();
            stmt.close();
            conn.close();

            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private int obterTipoSanguineoPorDoador(int idDoador) {
        int idTipoSanguineo = -1;
        String sql = "SELECT id_Tipo_Sanguineo FROM doador WHERE id_Doador = ?";

        try (Connection conn = new ConnectionSQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDoador);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idTipoSanguineo = rs.getInt("id_Tipo_Sanguineo");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idTipoSanguineo;
    }
}
