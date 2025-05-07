package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Connection.ConnectionSQL;
import Main.BloodCenter;
import Main.Donation;
import Main.Donor;
import Standard.PasswordCrypt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.sql.*;

public class DonationController {
	private int id_Donation;
	private int id_UserBloodCenter;
	private int id_Donor;
    private JTextField tf_volume;
    private JTextField tf_datetime;
    private JComboBox<String> cb_status;
    private JComboBox<String> cb_donors;
	
	public DonationController(int id_Donation, int id_UserBloodCenter, int id_Donor, JTextField tf_volume, JTextField tf_datetime, JComboBox<String> cb_status, JComboBox<String> cb_donors) {
		this.id_Donation = id_Donation;
		this.id_UserBloodCenter = id_UserBloodCenter;
		this.id_Donor = id_Donor;
		this.tf_volume = tf_volume;
		this.tf_datetime = tf_datetime;
		this.cb_status = cb_status;
		this.cb_donors = cb_donors;	
	}
		
    public void executeRegister() {
    	try {
	    	Float volume = Float.parseFloat(tf_volume.getText());
	        String status = cb_status.getSelectedItem().toString();
	        int idDonor = Integer.parseInt(cb_donors.getSelectedItem().toString().replaceAll("[^0-9]", ""));
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        
	        LocalDateTime datetime = LocalDateTime.now();
			String rawDate = tf_datetime.getText();
			if (rawDate != null) {
				datetime = LocalDateTime.parse(rawDate, formatter);
			}
			
			Donor donor = new Donor(idDonor);
			BloodCenter bloodCenter = buscarHemocentroPorID(this.id_UserBloodCenter);
	
			Donation donation= new Donation(status, volume, datetime, donor, bloodCenter);
			
			if (donation.create() > 0) {
				JOptionPane.showMessageDialog(null, "Doação adicionada com sucesso!");
	        } else {
	            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao adicionar a doação.");
	        }
	
	        if (status.equalsIgnoreCase("Realizada")) {
	            int idTipoSanguineo = obterTipoSanguineoPorDoador(idDonor);
	            if (idTipoSanguineo != -1) {
	            	BloodStockController estoqueController = new BloodStockController();
	            	BloodStockController.atualizarEstoquePorDoacao(bloodCenter.getId(), idTipoSanguineo, volume);
	            }
	        }
	    } catch (Exception ex) {
	    	JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
	    }

    }

    private int buscarIdHemocentroPorNome(String nome) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT id_Hemocentro FROM hemocentro WHERE  razao_Social = ?";
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
    
    public BloodCenter buscarHemocentroPorID(int id) {
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT id_Hemocentro FROM hemocentro WHERE id_usuario = ?";
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
    
    public List<String> listarDoacoesHemocentro(int idUsuario) {
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
                WHERE h.id_Usuario = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
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
    
    public List<String> listarDoacoesPorUsuario(int idUsuario) {
        List<String> doacoes = new ArrayList<>();
        try {
            Connection conn = new ConnectionSQL().getConnection();
            String sql = """
                SELECT d.id_Doacao,
                       ts.descricao AS tipo_Sanguineo,
                       h.razao_Social AS hemocentro,
                       d.status,
                       d.volume,
                       d.data_Hora
                FROM doacao d
                JOIN doador dd ON d.id_Doador = dd.id_Doador
                JOIN tipo_Sanguineo ts ON dd.id_Tipo_Sanguineo = ts.id_Tipo_Sanguineo
                JOIN hemocentro h ON d.id_Hemocentro = h.id_Hemocentro
                WHERE dd.id_Usuario = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_doacao");
                String tipo = rs.getString("tipo_sanguineo");
                String hemocentro = rs.getString("hemocentro");
                String status = rs.getString("status");
                int volume = rs.getInt("volume");
                String dataHora = rs.getString("data_hora");

                doacoes.add("[" + id + "] " + tipo + " | " + hemocentro + " | " + status + " | " + volume + "mL | " + dataHora);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacoes;
    }
    
    public List<String> listarDoacoesPorHemocentro(int idUsuario) {
        List<String> doacoes = new ArrayList<>();
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
                return doacoes; // retorna vazio se não encontrar o hemocentro
            }

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
                WHERE d.id_Hemocentro = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHemocentro);
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
}
