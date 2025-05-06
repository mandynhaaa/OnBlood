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
            String sql = "INSERT INTO Doacao (id_doador, id_hemocentro, status, volume, data_hora) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idDoador);
            stmt.setInt(2, idHemocentro);
            stmt.setString(3, status);
            stmt.setInt(4, volume);
            stmt.setObject(5, dataHora);
            int rowsInserted = stmt.executeUpdate();

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
            String sql = "SELECT id_hemocentro FROM Hemocentro WHERE razao_social = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            var rs = stmt.executeQuery();
            int id = -1;
            if (rs.next()) {
                id = rs.getInt("id_hemocentro");
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
            String sql = "SELECT razao_social FROM Hemocentro";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hemocentros.add(rs.getString("razao_social"));
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
            String sql = "SELECT d.id_doador, u.nome FROM Doador d JOIN Usuario u ON d.id_usuario = u.id_usuario";
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
                SELECT d.id_doacao,
                       u.nome AS nome_doador,
                       ts.descricao AS tipo_sanguineo,
                       h.razao_social AS hemocentro,
                       d.status,
                       d.volume,
                       d.data_hora
                FROM Doacao d
                JOIN Doador dd ON d.id_doador = dd.id_doador
                JOIN Usuario u ON dd.id_usuario = u.id_usuario
                JOIN Tipo_Sanguineo ts ON dd.id_tipo_sanguineo = ts.id_tipo_sanguineo
                JOIN Hemocentro h ON d.id_hemocentro = h.id_hemocentro
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
            String sql = "UPDATE Doacao SET status = ?, volume = ?, data_hora = ? WHERE id_doacao = ?";
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
            String sql = "DELETE FROM Doacao WHERE id_doacao = ?";
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
}
