package igu;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entidades.Resto;
import servicio.RestoServic;

public class InicializarResto extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<Resto> mostrarresto;

    public InicializarResto() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 434, 261);
        contentPane.add(panel);
        panel.setLayout(null);

        mostrarresto = new JComboBox<>();
        mostrarresto.setBounds(134, 92, 166, 40);  
        panel.add(mostrarresto);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Resto restauranteSeleccionado = (Resto) mostrarresto.getSelectedItem();
                Menu menu = new Menu(restauranteSeleccionado); 
                menu.setVisible(true);
                dispose();
            }
        });
        btnAceptar.setBounds(157, 143, 120, 25);  
        panel.add(btnAceptar);

        cargarRestaurantesEnComboBox();
    }

    private void cargarRestaurantesEnComboBox() {
        RestoServic restoServic = new RestoServic();
        List<Resto> restaurantes = restoServic.obtenerTodosLosRestaurantes();

        DefaultComboBoxModel<Resto> model = new DefaultComboBoxModel<>(restaurantes.toArray(new Resto[0]));
        mostrarresto.setModel(model);
    }
}
