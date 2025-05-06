package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connection.ConnectionSQL;

public class AddressController {

    public boolean cadastrarEndereco(int idUsuario, String rua, String numero, String cidade, String estado) {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "INSERT INTO endereco (id_Usuario, rua, numero, cidade, estado) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setString(2, rua);
            stmt.setString(3, numero);
            stmt.setString(4, cidade);
            stmt.setString(5, estado);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listarEnderecos() {
        List<String> enderecos = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = """
                SELECT e.id_Endereco, u.nome, e.rua, e.numero, e.cidade, e.estado
                FROM endereco e
                JOIN usuario u ON e.id_Usuario = u.id_Usuario
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Endereco");
                String nome = rs.getString("nome");
                String rua = rs.getString("rua");
                String numero = rs.getString("numero");
                String cidade = rs.getString("cidade");
                String estado = rs.getString("estado");
                enderecos.add("[" + id + "] " + nome + ": " + rua + ", " + numero + " - " + cidade + "/" + estado);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enderecos;
    }

    public boolean excluirEndereco(int idEndereco) {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "DELETE FROM endereco WHERE id_Endereco = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEndereco);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listarUsuarios() {
        List<String> usuarios = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "SELECT id_Usuario, nome FROM usuario";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Usuario");
                String nome = rs.getString("nome");
                usuarios.add("[" + id + "] " + nome);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }
    
    public boolean atualizarEndereco(int idEndereco, String rua, String numero, String cidade, String estado) {
        try (Connection conn = new ConnectionSQL().getConnection()) {
            String sql = "UPDATE endereco SET rua = ?, numero = ?, cidade = ?, estado = ? WHERE id_Endereco = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, rua);
            stmt.setString(2, numero);
            stmt.setString(3, cidade);
            stmt.setString(4, estado);
            stmt.setInt(5, idEndereco);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}