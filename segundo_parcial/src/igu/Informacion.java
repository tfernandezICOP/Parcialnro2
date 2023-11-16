package igu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import entidades.Mesa;
import entidades.Resto;
import servicio.MesaServic;

public class Informacion extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Resto restauranteSeleccionado;
    private JFormattedTextField formattedTextField;
    private Mesa mesaSeleccionada; 
    MesaServic mesaServic = new MesaServic(); 

    public Informacion(Resto restauranteSeleccionado) {
        this.restauranteSeleccionado = restauranteSeleccionado;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 784, 461);
        contentPane.add(panel);
        panel.setLayout(null);

        JButton btnVistaActual = new JButton("Vista actual");
        btnVistaActual.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarVistaActual();
            }
        });
        btnVistaActual.setBounds(50, 47, 150, 30);
        panel.add(btnVistaActual);

        JButton btnAcercaDe = new JButton("Acerca de");
        btnAcercaDe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarAcercaDe();
            }
        });
        btnAcercaDe.setBounds(580, 47, 150, 30);
        panel.add(btnAcercaDe);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnSalir.setBounds(700, 400, 80, 30);
        panel.add(btnSalir);

        JButton btnVistaFecha = new JButton("Vista x fecha");
        btnVistaFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarMesasDisponiblesParaFecha();
            }
        });
        btnVistaFecha.setBounds(50, 113, 150, 30);
        panel.add(btnVistaFecha);

        JLabel lblInformacion = new JLabel("Informacion");
        lblInformacion.setFont(new Font("Arial", Font.BOLD, 24));
        lblInformacion.setBounds(10, 0, 764, 40);
        panel.add(lblInformacion);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 160, 700, 200);
        panel.add(scrollPane);

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            formattedTextField = new JFormattedTextField(dateMask);
            formattedTextField.setBounds(210, 114, 150, 30);
            panel.add(formattedTextField);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Domicilio");
        tableModel.addColumn("Localidad");
        tableModel.addColumn("Nro. Mesa");
        tableModel.addColumn("Capacidad");
        tableModel.addColumn("Estado");
        table.setModel(tableModel);

        JButton btnLiberar = new JButton("Liberar");
        btnLiberar.setBounds(435, 400, 150, 30);
        panel.add(btnLiberar);
        btnLiberar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                liberarMesa();
            }
        });

        JButton btnOcupar = new JButton("Ocupar");
        btnOcupar.setBounds(246, 400, 150, 30);
        panel.add(btnOcupar);
        btnOcupar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ocuparMesa();
            }
        });

        JButton btnReservar = new JButton("Reservar");
        btnReservar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reservarMesa();
            }
        });
        btnReservar.setBounds(69, 400, 150, 30);
        panel.add(btnReservar);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() || table.getSelectedRowCount() == 0) {
                    return;
                }
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Object idMesaObject = tableModel.getValueAt(selectedRow, 3);
                    if (idMesaObject != null) {
                        try {
                            if (idMesaObject.toString().matches("\\d+")) {
                                int idMesa = Integer.parseInt(idMesaObject.toString());
                                int capacidad = (int) tableModel.getValueAt(selectedRow, 4);

                                mesaSeleccionada = new Mesa();
                                mesaSeleccionada.setId_mesa(idMesa);
                                mesaSeleccionada.setNro_mesa(idMesa);
                                mesaSeleccionada.setCapacidad(capacidad);
                            } else {
                                System.out.println("El valor no es un número.");
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void mostrarAcercaDe() {
        String mensaje = "\nNombre y Apellido: Tomas Fernandez\nFecha: 15/11/2023\nExamen: Parcial 2";
        JOptionPane.showMessageDialog(this, mensaje, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarVistaActual() {
        int idResto = restauranteSeleccionado.getIdResto();
        String nombreResto = restauranteSeleccionado.getNombre();
        String domicilioResto = restauranteSeleccionado.getDomicilio();
        String localidadResto = restauranteSeleccionado.getLocalidad();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        tableModel.addRow(new Object[] { nombreResto, domicilioResto, localidadResto, "Nro. Mesa", "Capacidad", "Estado" });

        List<Mesa> mesasRestaurante = mesaServic.obtenerTodasLasMesasPorRestaurante(idResto);

        for (Mesa mesa : mesasRestaurante) {
            String nombreEstado = mesa.getEstado().nombreEstado();
            tableModel.addRow(new Object[] { nombreResto, domicilioResto, localidadResto, mesa.getNro_mesa(), mesa.getCapacidad(), nombreEstado });
        }
    }

    private void mostrarMesasDisponiblesParaFecha() {
        try {
            String fechaStr = formattedTextField.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = new Date(sdf.parse(fechaStr).getTime());
            int idResto = restauranteSeleccionado.getIdResto();
            String nombreResto = restauranteSeleccionado.getNombre();
            String domicilioResto = restauranteSeleccionado.getDomicilio();
            String localidadResto = restauranteSeleccionado.getLocalidad();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            tableModel.addRow(new Object[] { nombreResto, domicilioResto, localidadResto, "Nro. Mesa", "Capacidad", "Estado" });

            List<Mesa> mesasDisponibles = mesaServic.obtenerMesasDisponiblesParaFecha(idResto, fecha);

            for (Mesa mesa : mesasDisponibles) {
                String nombreEstado = mesa.getEstado().nombreEstado();
                tableModel.addRow(new Object[] { nombreResto, domicilioResto, localidadResto, mesa.getNro_mesa(), mesa.getCapacidad(), nombreEstado });
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void reservarMesa() {
        if (mesaSeleccionada != null) {
            if (mesaServic.puedeReservarMesa(mesaSeleccionada.getId_mesa())) {
                mesaServic.reservarMesa(mesaSeleccionada.getId_mesa());
                cambiarEstadoYActualizarVista();
            }
            }
        }

    public void liberarMesa() {
        if (mesaSeleccionada != null) {
            if (mesaServic.puedeLiberarMesa(mesaSeleccionada.getId_mesa())) {
                mesaServic.liberarMesa(mesaSeleccionada.getId_mesa());
                cambiarEstadoYActualizarVista();
            }
        }
        }

    public void ocuparMesa() {
        if (mesaSeleccionada != null) {
            if (mesaServic.puedeOcuparMesa(mesaSeleccionada.getId_mesa())) {
                mesaServic.ocuparMesa(mesaSeleccionada.getId_mesa());
                cambiarEstadoYActualizarVista();
           
    }
        }
        }
    private void cambiarEstadoYActualizarVista() {
        mostrarVistaActual();
    }

}
