package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Connection.ConnectionSQL;
import Main.Address;
import Main.BloodCenter;
import Main.Donation;
import Main.Donor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	        String volumeText = tf_volume.getText().trim();
	        if (volumeText.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "O campo volume é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        float volume;
	        try {
	            volume = Float.parseFloat(volumeText.replace(",", "."));
	            if (volume <= 0) {
	                JOptionPane.showMessageDialog(null, "O volume deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(null, "O volume deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        String dataHoraText = tf_datetime.getText().trim();
	        if (dataHoraText.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "O campo data/hora é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        LocalDateTime datetime;
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	        try {
	            datetime = LocalDateTime.parse(dataHoraText, formatter);
	        } catch (DateTimeParseException e) {
	            JOptionPane.showMessageDialog(null, "A data e hora devem estar no formato: dd/MM/yyyy HH:mm:ss", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        Object statusSelected = cb_status.getSelectedItem();
	        if (statusSelected == null) {
	            JOptionPane.showMessageDialog(null, "Selecione um status válido.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        String status = statusSelected.toString();

	        String selectedDoador = (String) cb_donors.getSelectedItem();
	        if (selectedDoador == null || !selectedDoador.matches("^\\[\\d+\\].*")) {
	            JOptionPane.showMessageDialog(null, "Selecione um doador válido.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        int idDonor;
	        try {
	            String idText = selectedDoador.substring(selectedDoador.indexOf('[') + 1, selectedDoador.indexOf(']'));
	            idDonor = Integer.parseInt(idText);
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Erro ao extrair o ID do doador: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        Donor donor = new Donor(idDonor);
	        BloodCenter bloodCenter = buscarHemocentroPorIdUsuario(id_UserBloodCenter);
	        Donation donation = new Donation(status, volume, datetime, donor, bloodCenter);

	        if (donation.create() > 0) {
	            JOptionPane.showMessageDialog(null, "Doação adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao adicionar a doação.", "Erro", JOptionPane.ERROR_MESSAGE);
	        }

            int idTipoSanguineo = obterTipoSanguineoPorDoador(idDonor);
            if (idTipoSanguineo != -1) {
                BloodStockController.atualizarEstoque(bloodCenter.getId(), idTipoSanguineo);
            }
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(null, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
    public void executeUpdate() {
    	try {
    		String volumeText = tf_volume.getText().trim();
	        if (volumeText.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "O campo volume é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        float volume;
	        try {
	            volume = Float.parseFloat(volumeText.replace(",", "."));
	            if (volume <= 0) {
	                JOptionPane.showMessageDialog(null, "O volume deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(null, "O volume deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        String dataHoraText = tf_datetime.getText().trim();
	        if (dataHoraText.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "O campo data/hora é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        LocalDateTime datetime;
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	        try {
	            datetime = LocalDateTime.parse(dataHoraText, formatter);
	        } catch (DateTimeParseException e) {
	            JOptionPane.showMessageDialog(null, "A data e hora devem estar no formato: dd/MM/yyyy HH:mm:ss", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        Object statusSelected = cb_status.getSelectedItem();
	        if (statusSelected == null) {
	            JOptionPane.showMessageDialog(null, "Selecione um status válido.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        String status = statusSelected.toString();
	        
            Donation donation = new Donation(id_Donation);
            donation.setVolume(volume);
            donation.setDatetime(datetime);
            donation.setStatus(status);

            donation.update();
            JOptionPane.showMessageDialog(null, "Doação alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            int idTipoSanguineo = obterTipoSanguineoPorDoacao(id_Donation);
            if (idTipoSanguineo != -1) {
                BloodStockController.atualizarEstoque(obterHemocentroPorDoacao(id_Donation), idTipoSanguineo);
            }
	        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void executeDelete() {
    	try {
    		Donation donation = new Donation(id_Donation);
    		donation.delete();
            JOptionPane.showMessageDialog(null, "Doação removida com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
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
    
    public List<String> listarDoadores() {
        List<String> doadores = new ArrayList<>();
        try {
        	Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT d.id_Doador, u.nome, d.cpf FROM doador d JOIN usuario u ON d.id_Usuario = u.id_Usuario";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_doador");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                doadores.add("[" + id + "] " + nome + " - " + cpf);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doadores;
    }
    
    public List<String> listarDoacoesHemocentro(int idUsuario, String filtroStatus) {
        List<String> doacoes = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            StringBuilder sql = new StringBuilder("""
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
            """);

            if (!filtroStatus.equalsIgnoreCase("Todos")) {
                sql.append(" AND d.status = ?");
            }

            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            stmt.setInt(1, idUsuario);

            if (!filtroStatus.equalsIgnoreCase("Todos")) {
                stmt.setString(2, filtroStatus);
            }

            ResultSet rs = stmt.executeQuery();
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            while (rs.next()) {
                int id = rs.getInt("id_doacao");
                String nome = rs.getString("nome_doador");
                String tipo = rs.getString("tipo_sanguineo");
                String hemocentro = rs.getString("hemocentro");
                String status = rs.getString("status");
                Float volume = rs.getFloat("volume");
                LocalDateTime dataHoraConvertida = LocalDateTime.parse(rs.getString("data_hora"), formatoEntrada);
                String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);

                doacoes.add("[" + id + "] " + nome + " (" + tipo + ") | " + hemocentro + " | " + status + " | " + volume + "mL | " + dataHoraFormatada);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacoes;
    }
    public List<String> listarTodasDoacoes(String filtroStatus) {
        List<String> doacoes = new ArrayList<>();
        try (Connection conn = new ConnectionSQL().getConnection()) {
            StringBuilder sql = new StringBuilder("""
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
            """);


            PreparedStatement stmt = conn.prepareStatement(sql.toString());

            ResultSet rs = stmt.executeQuery();
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            while (rs.next()) {
                int id = rs.getInt("id_doacao");
                String nome = rs.getString("nome_doador");
                String tipo = rs.getString("tipo_sanguineo");
                String hemocentro = rs.getString("hemocentro");
                String status = rs.getString("status");
                Float volume = rs.getFloat("volume");
                LocalDateTime dataHoraConvertida = LocalDateTime.parse(rs.getString("data_hora"), formatoEntrada);
                String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);

                doacoes.add("[" + id + "] " + nome + " (" + tipo + ") | " + hemocentro + " | " + status + " | " + volume + "mL | " + dataHoraFormatada);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacoes;
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
    
    private int obterTipoSanguineoPorDoacao(int idDoacao) {
        int idTipoSanguineo = -1;
        String sql = "SELECT id_Tipo_Sanguineo FROM doacao"
        		+ " INNER JOIN doador ON doador.id_Doador = doacao.id_Doador WHERE id_Doacao = ?";
        

        try (Connection conn = new ConnectionSQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDoacao);
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
    
    private int obterHemocentroPorDoacao(int idDoacao) {
        int id_Hemocentro = -1;
        String sql = "SELECT id_Hemocentro FROM doacao WHERE id_Doacao = ?";

        try (Connection conn = new ConnectionSQL().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDoacao);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	id_Hemocentro = rs.getInt("id_Hemocentro");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_Hemocentro;
    }
    
    public List<String> listarDoacoesPorUsuario(int idUsuario, String status, String hemocentroFiltro) {
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

            if (status != null && !status.equalsIgnoreCase("Todos")) {
                sql += " AND d.status = ?";
            }

            if (hemocentroFiltro != null && !hemocentroFiltro.equalsIgnoreCase("Todos")) {
                sql += " AND h.razao_Social = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            int index = 2;

            if (status != null && !status.equalsIgnoreCase("Todos")) {
                stmt.setString(index++, status);
            }
            if (hemocentroFiltro != null && !hemocentroFiltro.equalsIgnoreCase("Todos")) {
                stmt.setString(index, hemocentroFiltro);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_doacao");
                String tipo = rs.getString("tipo_sanguineo");
                String hemocentro = rs.getString("hemocentro");
                String statusDoacao = rs.getString("status");
                Float volume = rs.getFloat("volume");
                String dataHora = rs.getString("data_hora");

                doacoes.add("[" + id + "] " + tipo + " | " + hemocentro + " | " + statusDoacao + " | " + volume + "mL | " + dataHora);
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
                return doacoes; 
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
                Float volume = rs.getFloat("volume");
                String dataHora = rs.getString("data_hora");
                
                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                LocalDateTime dataHoraConvertida = LocalDateTime.parse(dataHora, formatoEntrada);
                String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);

                doacoes.add("[" + id + "] " + nome + " (" + tipo + ") | " + hemocentro + " | " + status + " | " + volume + "mL | " + dataHoraFormatada);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doacoes;
    }
    
    public List<String> listarHemocentros() {
        List<String> hemocentros = new ArrayList<>();
        try {
            Connection conn = new ConnectionSQL().getConnection();
            String sql = "SELECT razao_Social FROM hemocentro";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            hemocentros.add("Todos");

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
}
