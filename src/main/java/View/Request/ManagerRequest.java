package View.Request;

import javax.swing.*;
import Controller.RequestController;
import java.awt.*;
import java.util.List;

public class ManagerRequest extends JFrame {
    private JList<String> listSolicitacoes;
    private DefaultListModel<String> listModel;
    private JButton btnEditar, btnExcluir, btnNova, btnAtualizar;
    private RequestController controller;

    public ManagerRequest() {
        controller = new RequestController();
        setTitle("Gerenciar Solicitações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listSolicitacoes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listSolicitacoes);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        btnAtualizar = new JButton("Atualizar");
        btnNova = new JButton("Nova Solicitação");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

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
                EditRequest editor = new EditRequest(id, controller, this);
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
            RegisterRequest telaCadastro = new RegisterRequest();
            telaCadastro.setVisible(true);
        });

        btnAtualizar.addActionListener(e -> carregarSolicitacoes());
    }

    void carregarSolicitacoes() {
        listModel.clear();
        List<String> solicitacoes = controller.listarSolicitacoes();
        for (String s : solicitacoes) {
            listModel.addElement(s);
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
