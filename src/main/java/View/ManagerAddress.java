package View;

import javax.swing.*;
import Controller.AddressController;
import java.awt.*;
import java.util.List;

import View.EditAddress;

public class ManagerAddress extends JFrame {
    private JList<String> listEnderecos;
    private DefaultListModel<String> listModel;
    private JButton btnExcluir, btnNovo, btnAtualizar, btnEditar;
    private AddressController controller;


    public ManagerAddress() {
        controller = new AddressController();
        setTitle("Gerenciar Endereços");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listEnderecos = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listEnderecos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        btnAtualizar = new JButton("Atualizar");
        btnNovo = new JButton("Novo Endereço");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");


        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNovo);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> carregarEnderecos());
        btnNovo.addActionListener(e -> new RegisterAddress().setVisible(true));
        btnExcluir.addActionListener(e -> excluirEndereco());
        btnEditar.addActionListener(e -> {
            String selecionado = listEnderecos.getSelectedValue();
            if (selecionado != null) {
                int id = extrairId(selecionado);
                EditAddress editor = new EditAddress(id, controller, this);
                editor.setVisible(true);
            }
        });

        carregarEnderecos();
    }

    public void carregarEnderecos() {
        listModel.clear();
        List<String> enderecos = controller.listarEnderecos();
        for (String e : enderecos) {
            listModel.addElement(e);
        }
    }

    private void excluirEndereco() {
        String selecionado = listEnderecos.getSelectedValue();
        if (selecionado != null) {
            int id = Integer.parseInt(selecionado.substring(1, selecionado.indexOf("]")));
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.excluirEndereco(id)) {
                    JOptionPane.showMessageDialog(this, "Endereço excluído.");
                    carregarEnderecos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir.");
                }
            }
        }
    }
    
    private int extrairId(String texto) {
        return Integer.parseInt(texto.substring(1, texto.indexOf("]")));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerAddress().setVisible(true));
    }
}
