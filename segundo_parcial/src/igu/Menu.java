package igu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entidades.Resto;
import servicio.RestoServic;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public Menu(Resto restauranteSeleccionado) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 597, 391);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Opciones");
        menuBar.add(menu);

        JMenuItem menuItemReserva = new JMenuItem("Reserva");
        menuItemReserva.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReservaIgu reservaFrame = new ReservaIgu(restauranteSeleccionado, Menu.this);
                reservaFrame.setVisible(true);
                dispose();
            }
        });
        menu.add(menuItemReserva);

        JMenuItem menuItemGestion = new JMenuItem("Gestión");
        menuItemGestion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Gestionar gestionFrame = new Gestionar(restauranteSeleccionado, Menu.this);
                gestionFrame.setVisible(true);
                dispose();
            }
        });
        menu.add(menuItemGestion);

        JMenu subMenuInformacion = new JMenu("Información");
        JMenuItem menuItemActual = new JMenuItem("Actual");
        JMenuItem menuItemPorFecha = new JMenuItem("Por Fecha");

        menuItemActual.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Informacion info = new Informacion(restauranteSeleccionado, Menu.this);
                info.setVisible(true);
                dispose();
            }
        });

        menuItemPorFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InformacionXFecha infoFecha = new InformacionXFecha(restauranteSeleccionado, Menu.this);
                infoFecha.setVisible(true);
                dispose();
            }
        });

        subMenuInformacion.add(menuItemActual);
        subMenuInformacion.add(menuItemPorFecha);
        menu.add(subMenuInformacion);

        JMenuItem menuItemEstadisticas = new JMenuItem("Estadísticas");
        menuItemEstadisticas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Estadistica estadisticaFrame = new Estadistica(restauranteSeleccionado,  Menu.this);
                estadisticaFrame.setVisible(true);
                dispose();
            }
        });
        menu.add(menuItemEstadisticas);

        JMenuItem menuItemSalir = new JMenuItem("Salir");
        menuItemSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        menu.add(menuItemSalir);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        RestoServic restoServic = new RestoServic();
        List<Resto> restaurantes = restoServic.obtenerTodosLosRestaurantes();

        DefaultComboBoxModel<Resto> model = new DefaultComboBoxModel<>(restaurantes.toArray(new Resto[0]));
    }
}
