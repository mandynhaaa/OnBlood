package View.Request;

import javax.swing.*;
import Controller.DonationController;
import Controller.RequestController;

import java.awt.*;
import java.util.List;

public class BloodCenterRequests extends JFrame {
    private static final ManagerRequest NULL = null;
	private JList<String> listSolicitacoes;
    private DefaultListModel<String> listModel;
    private JButton btnEditar, btnExcluir, btnNova, btnAtualizar;
    private RequestController controller;
    private int idUsuario;

    public BloodCenterRequests(int idUsuario) {
    	this.idUsuario = idUsuario;
        controller = new RequestController();
        setTitle("Gerenciar Solicitacoes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listSolicitacoes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listSolicitacoes);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnNova = new JButton("Nova Solicitação");
        btnAtualizar = new JButton("Atualizar");
        
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNova);
        panelBotoes.add(btnEditar);      
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        carregarSolicitacoes();

        btnEditar.addActionListener(e -> {
            String selecionado = listSolicitacoes.getSelectedValue();
            if (selecionado != null) {
                int id = extrairId(selecionado);
                EditRequest editor = new EditRequest(id, controller, NULL, this);
                editor.setVisible(true);
            }
        });

        btnExcluir.addActionListener(e -> {
            String selecionado = listSolicitacoes.getSelectedValue();
            if (selecionado != null) {
                int id = extrairId(selecionado);
                int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?");
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean sucesso = controller.excluirSolicitacao(id);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this, "Solicitação excluída.");
                        carregarSolicitacoes();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir.");
                    }
                }
            }
        });
        
        btnNova.addActionListener(e -> {
        	BloodCenterRegisterRequest telaCadastro = new BloodCenterRegisterRequest(idUsuario);
            telaCadastro.setVisible(true);
        });
        
        btnAtualizar.addActionListener(e -> carregarSolicitacoes());
    }

    void carregarSolicitacoes() {
        listModel.clear();
        List<String> solicitacoes = controller.listarSolicitacoesPorHemocentro(idUsuario);
        for (String d : solicitacoes) {
            listModel.addElement(d);
        }
    }

    private int extrairId(String texto) {
        return Integer.parseInt(texto.substring(1, texto.indexOf("]")));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManagerRequest().setVisible(true);
        });
    }
}
