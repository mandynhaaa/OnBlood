package Controller;

import Main.Telephone;
import Main.User;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelephoneController {

    private ObjectId idUsuario;
    private ObjectId idTelefone;
    private JTextField tf_description, tf_ddd, tf_number;

    public TelephoneController(ObjectId idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TelephoneController(ObjectId idUsuario, JTextField tf_description, JTextField tf_ddd, JTextField tf_number) {
        this(idUsuario);
        this.tf_description = tf_description;
        this.tf_ddd = tf_ddd;
        this.tf_number = tf_number;
    }

    public TelephoneController(ObjectId idUsuario, ObjectId idTelefone, JTextField tf_description, JTextField tf_ddd, JTextField tf_number) {
        this(idUsuario, tf_description, tf_ddd, tf_number);
        this.idTelefone = idTelefone;
    }

    public void executeRegister() {
        User user = new User(this.idUsuario);
        if (user.getId() == null) {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
            return;
        }

        Telephone phone = new Telephone();
        phone.setDescription(tf_description.getText());
        phone.setDdd(tf_ddd.getText());
        phone.setNumber(tf_number.getText());

        user.getTelephones().add(phone);
        user.update();
        JOptionPane.showMessageDialog(null, "Telefone adicionado com sucesso!");
    }

    public void executeUpdate() {
        User user = new User(this.idUsuario);
        user.getTelephones().stream()
            .filter(p -> p.getId().equals(this.idTelefone))
            .findFirst()
            .ifPresent(phone -> {
                phone.setDescription(tf_description.getText());
                phone.setDdd(tf_ddd.getText());
                phone.setNumber(tf_number.getText());
                user.update();
                JOptionPane.showMessageDialog(null, "Telefone alterado com sucesso!");
            });
    }

    public void executeDelete(ObjectId idTelefoneToDelete) {
        User user = new User(this.idUsuario);
        boolean removed = user.getTelephones().removeIf(p -> p.getId().equals(idTelefoneToDelete));

        if (removed) {
            user.update();
            JOptionPane.showMessageDialog(null, "Telefone removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Telefone não encontrado para exclusão.");
        }
    }

    public List<String> listTelephones(ObjectId idUsuario) {
        User user = new User(idUsuario);
        if (user.getTelephones() == null) {
            return new ArrayList<>();
        }

        return user.getTelephones().stream()
                .map(p -> String.format("[%s] %s: (%s) %s",
                        p.getId().toHexString(), p.getDescription(), p.getDdd(), p.getNumber()))
                .collect(Collectors.toList());
    }
}