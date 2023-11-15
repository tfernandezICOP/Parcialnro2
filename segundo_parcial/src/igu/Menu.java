package igu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entidades.Resto;
import servicio.RestoServic;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<Resto> mostrarresto;

    public Menu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 597, 391);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel Labelmenu = new JLabel("Menú");
        Labelmenu.setBounds(245, 4, 120, 40);
        Labelmenu.setFont(new Font("Arial", Font.BOLD, 30));
        panel.add(Labelmenu);

        JButton btnReserva = new JButton("Reserva");
        btnReserva.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Resto restauranteSeleccionado = (Resto) mostrarresto.getSelectedItem();
                ReservaIgu reservaFrame = new ReservaIgu(restauranteSeleccionado);
                reservaFrame.setVisible(true);
            }
        });
        btnReserva.setBounds(226, 100, 120, 40);
        panel.add(btnReserva);

        JButton btnGestion = new JButton("Gestión");
        btnGestion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Resto restauranteSeleccionado = (Resto) mostrarresto.getSelectedItem();
                Gestionar gestionFrame = new Gestionar(restauranteSeleccionado);
                gestionFrame.setVisible(true);
            }
        });
        btnGestion.setBounds(226, 170, 120, 40);
        panel.add(btnGestion);

        JButton btnInformacion = new JButton("Información");
        btnInformacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Resto restauranteSeleccionado = (Resto) mostrarresto.getSelectedItem();
                Informacion info = new Informacion(restauranteSeleccionado);
                info.setVisible(true);
            }
        });
        btnInformacion.setBounds(226, 240, 120, 40);
        panel.add(btnInformacion);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnSalir.setBounds(425, 291, 89, 40);
        panel.add(btnSalir);

        mostrarresto = new JComboBox<>();
        mostrarresto.setBounds(10, 4, 203, 40);
        panel.add(mostrarresto);

        RestoServic restoServic = new RestoServic();
        List<Resto> restaurantes = restoServic.obtenerTodosLosRestaurantes();

        DefaultComboBoxModel<Resto> model = new DefaultComboBoxModel<>(restaurantes.toArray(new Resto[0]));
        mostrarresto.setModel(model);

        mostrarresto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Resto restauranteSeleccionado = (Resto) mostrarresto.getSelectedItem();
                boolean isRestauranteSeleccionado = restauranteSeleccionado != null;
                btnReserva.setEnabled(isRestauranteSeleccionado);
                btnGestion.setEnabled(isRestauranteSeleccionado);
                btnInformacion.setEnabled(isRestauranteSeleccionado);
            }
        });
    }
}
