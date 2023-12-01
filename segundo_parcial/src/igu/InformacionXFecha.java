package igu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import entidades.Mesa;
import entidades.Resto;
import servicio.MesaServic;
import servicio.ReservaServic;

public class InformacionXFecha extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Resto restauranteSeleccionado;
    private Menu menu;
    private MesaServic mesaServic;
    private Date fechaSeleccionada;
    private ReservaServic reservaservic;

    public InformacionXFecha(Resto restauranteSeleccionado, Menu menu) {
        this.restauranteSeleccionado = restauranteSeleccionado;
        this.menu = menu;
        this.reservaservic = new ReservaServic();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 721, 435);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (menu != null) {
                    menu.setVisible(true);
                    dispose();
                }
            }
        });
        btnVolver.setBounds(545, 357, 150, 30);
        panel.add(btnVolver);

        JLabel lblInformacionXFecha = new JLabel("Informacion x Fecha");
        lblInformacionXFecha.setFont(new Font("Arial", Font.BOLD, 24));
        lblInformacionXFecha.setBounds(10, 11, 283, 40);
        panel.add(lblInformacionXFecha);

        JLabel lblNombreResto = new JLabel("Nombre: " + restauranteSeleccionado.getNombre());
        lblNombreResto.setBounds(308, 11, 300, 20);
        panel.add(lblNombreResto);

        JLabel lblDomicilioResto = new JLabel("Domicilio: " + restauranteSeleccionado.getDomicilio());
        lblDomicilioResto.setBounds(308, 42, 300, 20);
        panel.add(lblDomicilioResto);

        JLabel lblVistaXFecha = new JLabel("Vista x Fecha:");
        lblVistaXFecha.setBounds(10, 121, 84, 30);
        panel.add(lblVistaXFecha);

        JFormattedTextField formattedTextField = createFormattedTextField();
        formattedTextField.setBounds(90, 121, 139, 30);
        panel.add(formattedTextField);
        
        
        JButton btnVistaFecha = new JButton("Buscar");
        btnVistaFecha.setBounds(252, 121, 150, 30);
        panel.add(btnVistaFecha);

        table = new JTable();
        table.setBounds(10, 202, 685, 144);
        panel.add(table);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nro. Mesa");
        tableModel.addColumn("Capacidad");
        tableModel.addColumn("Estado");
        table.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 202, 685, 144);
        panel.add(scrollPane);
        
        JButton btnAcercaDe = new JButton("Acerca de");
        btnAcercaDe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarAcercaDe();
            }
        });
        btnAcercaDe.setBounds(579, 24, 104, 30);
        panel.add(btnAcercaDe);

        table.getColumnModel().getColumn(2).setCellRenderer(new EstadoMesaRenderer());

        btnVistaFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String fechaStr = formattedTextField.getText();
                    fechaStr = convertirFormatoFecha(fechaStr);
                    fechaSeleccionada = Date.valueOf(fechaStr);
                    mostrarTodasLasMesas();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese una fecha válida.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void mostrarTodasLasMesas() {
        if (mesaServic == null) {
            mesaServic = new MesaServic();
        }

        List<Mesa> todasLasMesas = mesaServic.obtenerTodasLasMesasPorRestaurante(restauranteSeleccionado.getIdResto());
        actualizarEstadosMesas(todasLasMesas, fechaSeleccionada);
    }

    private JFormattedTextField createFormattedTextField() {
        MaskFormatter maskFormatter;
        try {
            maskFormatter = new MaskFormatter("##/##/####");
        } catch (ParseException e) {
            e.printStackTrace();
            maskFormatter = new MaskFormatter();
        }

        JFormattedTextField formattedTextField = new JFormattedTextField(maskFormatter);
        formattedTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        return formattedTextField;
    }

    private void actualizarEstadosMesas(List<Mesa> todasLasMesas, Date fecha) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        List<Mesa> mesasDisponibles = reservaservic.buscarMesasDisponibles("", 0, fecha,
                restauranteSeleccionado.getIdResto());

        for (Mesa mesa : todasLasMesas) {
            String estado;

            if (esMesaDisponible(mesa, mesasDisponibles)) {
                estado = "liberada";
            } else {
                estado = "reservada";
            }

            tableModel.addRow(new Object[] { mesa.getNro_mesa(), mesa.getCapacidad(), estado });
        }
    }

    private boolean esMesaDisponible(Mesa mesa, List<Mesa> mesasDisponibles) {
        for (Mesa mesaDisponible : mesasDisponibles) {
            if (mesa.getNro_mesa() == mesaDisponible.getNro_mesa()) {
                return true;
            }
        }
        return false;
    }

    private String convertirFormatoFecha(String fechaStr) {
        try {
            java.util.Date fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    private void mostrarAcercaDe() {
        String mensaje = "\nNombre y Apellido: Tomas Fernandez\nFecha: 15/11/2023\nExamen: Parcial 2";
        JOptionPane.showMessageDialog(this, mensaje, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
    private class EstadoMesaRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                    column);

            String estado = ((String) table.getValueAt(row, 2)).toLowerCase();

            switch (estado) {
                case "liberada":
                    cellComponent.setBackground(Color.GREEN);
                    break;
                case "reservada":
                    cellComponent.setBackground(Color.YELLOW);
                    break;
                case "ocupada":
                    cellComponent.setBackground(Color.RED);
                    break;
                default:
                    cellComponent.setBackground(table.getBackground());
            }

            return cellComponent;
        }
    }
}
