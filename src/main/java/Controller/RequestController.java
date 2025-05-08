package Controller;

import Connection.ConnectionSQL;
import Main.BloodCenter;
import Main.BloodType;
import Main.Request;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RequestController {
	private int id_Request;
	private int id_UserBloodCenter;
    private JTextField tf_volume;
    private JTextField tf_datetime;
    private JComboBox<String> cb_tipoSanguineo;
    private JComboBox<String> cb_status;
	
	public RequestController(int id_Request, int id_UserBloodCenter, JTextField tf_volume, JTextField tf_datetime, JComboBox<String> cb_tipoSanguineo, JComboBox<String> cb_status) {
		this.id_Request = id_Request;
		this.id_UserBloodCenter = id_UserBloodCenter;
		this.tf_volume = tf_volume;
		this.tf_datetime = tf_datetime;
		this.cb_tipoSanguineo = cb_tipoSanguineo;
		this.cb_status = cb_status;
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
	        
	        String selectedBloodType = (String) cb_tipoSanguineo.getSelectedItem();
	        if (selectedBloodType == null || !selectedBloodType.matches("^\\[\\d+\\].*")) {
	            JOptionPane.showMessageDialog(null, "Selecione um tipo sanguíneo válido.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        int idTipoSanguineo;
	        try {
	            String idText = selectedBloodType.substring(selectedBloodType.indexOf('[') + 1, selectedBloodType.indexOf(']'));
	            idTipoSanguineo = Integer.parseInt(idText);
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Erro ao extrair o ID do tipo sanguíneo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        BloodCenter bloodCenter = buscarHemocentroPorIdUsuario(id_UserBloodCenter);
	        BloodType bloodType = new BloodType(idTipoSanguineo);
	        Request request = new Request(status, volume, datetime, bloodCenter, bloodType);

	        if (request.create() > 0) {
	            JOptionPane.showMessageDialog(null, "Solicitação adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao adicionar a solicitação.", "Erro", JOptionPane.ERROR_MESSAGE);
	        }

	        BloodStockController.atualizarEstoque(bloodCenter.getId(), idTipoSanguineo);

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
	        
	        String selectedBloodType = (String) cb_tipoSanguineo.getSelectedItem();
	        if (selectedBloodType == null || !selectedBloodType.matches("^\\[\\d+\\].*")) {
	            JOptionPane.showMessageDialog(null, "Selecione um tipo sanguíneo válido.", "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        int idTipoSanguineo;
	        try {
	            String idText = selectedBloodType.substring(selectedBloodType.indexOf('[') + 1, selectedBloodType.indexOf(']'));
	            idTipoSanguineo = Integer.parseInt(idText);
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Erro ao extrair o ID do tipo sanguíneo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        BloodType bloodType = new BloodType(idTipoSanguineo);
	        Request request = new Request(id_Request);
	        request.setBloodType(bloodType);
	        request.setStatus(status);
	        request.setDatetime(datetime);
	        request.setVolume(volume);

	        request.update();
	        JOptionPane.showMessageDialog(null, "Solicitação alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            BloodStockController.atualizarEstoque(request.getBloodCenter().getId(), idTipoSanguineo);
	        
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(null, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	    }
    }
    
    public void executeDelete() {
    	try {
    		Request request = new Request(id_Request);
    		request.delete();
            JOptionPane.showMessageDialog(null, "Solicitação removida com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean cadastrarSolicitacao(String hemocentro, int idTipoSanguineo, String status, Float volume, String dataHoraTexto) {
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
            stmt.setFloat(4, volume);
            stmt.setObject(5, dataHora);

            int rowsInserted = stmt.executeUpdate();

            if (status.equalsIgnoreCase("Realizada")) {
                BloodStockController estoqueController = new BloodStockController();
                estoqueController.atualizarEstoque(idHemocentro, idTipoSanguineo);
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
                Float volume = rs.getFloat("volume");
                String dataHora = rs.getString("data_hora");

                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                LocalDateTime dataHoraConvertida = LocalDateTime.parse(dataHora, formatoEntrada);
                String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);
                
                solicitacoes.add("[" + id + "] " + hemocentro + " | " + tipo + " | " + status + " | " + volume + "mL | " + dataHoraFormatada);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solicitacoes;
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
            String sql = "SELECT id_Tipo_Sanguineo, descricao FROM tipo_Sanguineo";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_Tipo_Sanguineo");
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
                Float volume = rs.getFloat("volume");
                String dataHora = rs.getString("data_hora");
                
                DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                LocalDateTime dataHoraConvertida = LocalDateTime.parse(dataHora, formatoEntrada);
                String dataHoraFormatada = dataHoraConvertida.format(formatoSaida);

                solicitacoes.add("[" + id + "] " + " (" + tipo + ") | " + hemocentro + " | " + status + " | " + volume + "mL | " + dataHoraFormatada);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solicitacoes;
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
