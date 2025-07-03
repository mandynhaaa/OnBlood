package View.Request;

import Controller.RequestController;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManagerRequest extends JFrame {
    private JList<String> listSolicitacoes;
    private DefaultListModel<String> listModel;
    private JButton btnEditar, btnExcluir, btnNova, btnAtualizar;
    private RequestController controller;
    private ObjectId idUsuarioHemocentro;

    public ManagerRequest(ObjectId idUsuarioHemocentro) {
        this.idUsuarioHemocentro = idUsuarioHemocentro;
        this.controller = new RequestController(null, idUsuarioHemocentro, null, null, null, null);

        setTitle("Gerenciar Solicitações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listModel = new DefaultListModel<>();
        listSolicitacoes = new JList<>(listModel);
        listSolicitacoes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(listSolicitacoes), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnAtualizar = new JButton("Atualizar");
        btnNova = new JButton("Nova Solicitação");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNova);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> carregarSolicitacoes());
        btnNova.addActionListener(e -> new RegisterRequest(idUsuarioHemocentro, this).setVisible(true));
        btnEditar.addActionListener(e -> editarSolicitacao());
        btnExcluir.addActionListener(e -> excluirSolicitacao());
        
        carregarSolicitacoes();
    }

    public void carregarSolicitacoes() {
        listModel.clear();
        List<String> solicitacoes = controller.listarSolicitacoesPorHemocentro(idUsuarioHemocentro);
        solicitacoes.forEach(listModel::addElement);
    }

    private void editarSolicitacao() {
        String selecionado = listSolicitacoes.getSelectedValue();
        if (selecionado != null) {
            ObjectId idRequest = extrairId(selecionado);
            if (idRequest != null) {
                new EditRequest(idRequest, this).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma solicitação para editar.");
        }
    }

    private void excluirSolicitacao() {
        String selecionado = listSolicitacoes.getSelectedValue();
        if (selecionado != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ObjectId idRequest = extrairId(selecionado);
                if (idRequest != null) {
                    new RequestController(idRequest, null, null, null, null, null).executeDelete();
                    carregarSolicitacoes();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma solicitação para excluir.");
        }
    }

    private ObjectId extrairId(String texto) {
        try {
            String hexId = texto.substring(texto.indexOf('[') + 1, texto.indexOf(']'));
            return new ObjectId(hexId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de ID inválido na lista.");
            return null;
        }
    }
}