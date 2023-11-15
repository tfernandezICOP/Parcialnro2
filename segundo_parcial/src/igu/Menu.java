package igu;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Menu frame = new Menu();
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null); // Centra el marco en la pantalla
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Menu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel Labelmenu = new JLabel("Menu");
        Labelmenu.setBounds(188, 5, 48, 22);
        Labelmenu.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(Labelmenu);
        
        JButton btnNewButton = new JButton("Reserva");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton.setBounds(170, 44, 89, 23);
        panel.add(btnNewButton);
        
        JButton btnInformacion = new JButton("Informacion");
        btnInformacion.setBounds(170, 112, 89, 23);
        panel.add(btnInformacion);
        
        JButton btnAcercaDe = new JButton("Acerca de");
        btnAcercaDe.setBounds(170, 185, 89, 23);
        panel.add(btnAcercaDe);
        
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(325, 217, 89, 23);
        panel.add(btnSalir);
    }
}
