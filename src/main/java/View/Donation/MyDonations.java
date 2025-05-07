package View.Donation;

import Controller.DonationController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyDonations extends JFrame {
    private JList<String> listDoacoes;
    private DefaultListModel<String> listModel;
    private DonationController controller;
    private int idUsuario;

    public MyDonations(int idUsuario) {
        this.idUsuario = idUsuario;
        this.controller = new DonationController(0, 0, idUsuario, null, null, null, null);

        setTitle("Minhas Doações");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listDoacoes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listDoacoes);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarDoacoes());
        add(btnAtualizar, BorderLayout.SOUTH);

        carregarDoacoes();
    }

    private void carregarDoacoes() {
        listModel.clear();
        List<String> doacoes = controller.listarDoacoesPorUsuario(idUsuario);
        for (String d : doacoes) {
            listModel.addElement(d);
        }
    }

}
