package View;

import javax.swing.*;
import Controller.DonationController;
import java.awt.*;
import java.util.List;

public class ManagerDonation extends JFrame {
    private JList<String> listDoacoes;
    private DefaultListModel<String> listModel;
    private JButton btnEditar, btnExcluir, btnNova, btnAtualizar;
    private DonationController controller;

    public ManagerDonation() {
        controller = new DonationController();
        setTitle("Gerenciar Doações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listDoacoes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listDoacoes);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnNova = new JButton("Nova Doação");
        btnAtualizar = new JButton("Atualizar");
        
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNova);
        panelBotoes.add(btnEditar);      
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        carregarDoacoes();

        btnEditar.addActionListener(e -> {
            String selecionado = listDoacoes.getSelectedValue();
            if (selecionado != null) {
                int id = extrairId(selecionado);
                EditDonation editor = new EditDonation(id, controller, this);
                editor.setVisible(true);
            }
        });

        btnExcluir.addActionListener(e -> {
            String selecionado = listDoacoes.getSelectedValue();
            if (selecionado != null) {
                int id = extrairId(selecionado);
                int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?");
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean sucesso = controller.excluirDoacao(id);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this, "Doação excluída.");
                        carregarDoacoes();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir.");
                    }
                }
            }
        });
        
        btnNova.addActionListener(e -> {
            RegisterDonation telaCadastro = new RegisterDonation();
            telaCadastro.setVisible(true);
        });
        
        btnAtualizar.addActionListener(e -> carregarDoacoes());
    }

    void carregarDoacoes() {
        listModel.clear();
        List<String> doacoes = controller.listarDoacoes();
        for (String d : doacoes) {
            listModel.addElement(d);
        }
    }

    private int extrairId(String texto) {
        return Integer.parseInt(texto.substring(1, texto.indexOf("]")));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManagerDonation().setVisible(true);
        });
    }
}
