
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {

    private JMenuBar menubar;
    private JMenu menuP, menuA;
    private JMenuItem itemPeli, itemCat, itemNAlq;
    private JMenuItem itemDevol, itemVAlq;

    private JTable tabla;
    private DefaultTableModel modelo;
    private VideoclubBD bd;
    private JLabel label;

    public Main() {
        bd = new VideoclubBD();

        setTitle("VideoClub Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Título");
        modelo.addColumn("Género");
        modelo.addColumn("Año");
        modelo.addColumn("Disponible");

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        menubar = new JMenuBar();

        menuP = new JMenu("Películas");
        menuA = new JMenu("Alquileres");

        itemPeli = new JMenuItem("Añadir película");
        itemCat = new JMenuItem("Ver catálogo");
        itemNAlq = new JMenuItem("Nuevo alquiler");
        itemDevol = new JMenuItem("Devolver película");
        itemVAlq = new JMenuItem("Ver alquileres");

        menuP.add(itemPeli);
        menuP.add(itemCat);

        menuA.add(itemNAlq);
        menuA.add(itemDevol);
        menuA.add(itemVAlq);

        menubar.add(menuP);
        menubar.add(menuA);

        setJMenuBar(menubar);

        itemPeli.addActionListener(this);
        itemCat.addActionListener(this);
        itemNAlq.addActionListener(this);
        itemDevol.addActionListener(this);
        itemVAlq.addActionListener(this);

        label = new JLabel("Estado: preparado");
        add(label, BorderLayout.SOUTH);

        actualizarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == itemPeli) {
            accionanyadirPelicula();

        } else if (e.getSource() == itemCat) {
            actualizarTabla();
            label.setText("Estado: catálogo actualizado");

        } else if (e.getSource() == itemNAlq) {
            accionNuevoAlquiler();

        } else if (e.getSource() == itemDevol) {
            devolverPelicula();

        } else if (e.getSource() == itemVAlq) {
            accionVerAlquileres();
        }
    }

    // Pedir un número entero
    private Integer pedirEntero(String mensaje) {
        String texto = JOptionPane.showInputDialog(this, mensaje);

        if (texto == null) {
            return null;
        }

        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes introducir un número entero válido.",
                    "Dato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }

    // Añadir película
    private void accionanyadirPelicula() {
        Integer id = pedirEntero("ID de la película:");

        if (id == null) {
            return;
        }

        String titulo = JOptionPane.showInputDialog(this, "Título:");

        if (titulo == null || titulo.trim().isEmpty()) {
            return;
        }

        String genero = JOptionPane.showInputDialog(this, "Género:");

        if (genero == null || genero.trim().isEmpty()) {
            return;
        }

        Integer anyo = pedirEntero("Año:");

        if (anyo == null) {
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿La película está disponible?",
                "Disponibilidad",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.CLOSED_OPTION) {
            return;
        }

        boolean disponible = opcion == JOptionPane.YES_OPTION;

        boolean resultado = bd.accionanyadirPelicula(
                id, titulo.trim(), genero.trim(), anyo, disponible
        );

        if (resultado) {
            actualizarTabla();
            label.setText("Estado: película añadida");
            JOptionPane.showMessageDialog(this,
                    "Película añadida correctamente.");
        } else {
            label.setText("Estado: error al añadir película");
            JOptionPane.showMessageDialog(this,
                    "No se pudo añadir la película. Comprueba los datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Crear un alquiler
    private void accionNuevoAlquiler() {
        Integer id = pedirEntero("ID del alquiler:");

        if (id == null) {
            return;
        }

        Integer id_pelicula = pedirEntero("ID de la película:");

        if (id_pelicula == null) {
            return;
        }

        String cliente = JOptionPane.showInputDialog(this, "Nombre del cliente:");

        if (cliente == null || cliente.trim().isEmpty()) {
            return;
        }

        String dni = JOptionPane.showInputDialog(this, "DNI del cliente:");

        if (dni == null || dni.trim().isEmpty()) {
            return;
        }

        Integer dias = pedirEntero("Número de días del alquiler:");

        if (dias == null) {
            return;
        }

        if (dias <= 0) {
            JOptionPane.showMessageDialog(this,
                    "El número de días debe ser mayor que cero.");
            return;
        }

        boolean resultado = bd.accionNuevoAlquiler(
                id, id_pelicula, cliente.trim(), dni.trim(), dias
        );

        if (resultado) {
            actualizarTabla();
            label.setText("Estado: alquiler registrado");
            JOptionPane.showMessageDialog(this,
                    "Alquiler registrado correctamente.");
        } else {
            label.setText("Estado: no se pudo registrar el alquiler");
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el alquiler. " +
                    "Comprueba que la película exista y esté disponible, " +
                    "y que el ID del alquiler no esté repetido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Devolver película
    private void devolverPelicula() {
        Integer id_pelicula = pedirEntero("ID de la película a devolver:");

        if (id_pelicula == null) {
            return;
        }

        boolean resultado = bd.devolverPelicula(id_pelicula);

        if (resultado) {
            actualizarTabla();
            label.setText("Estado: película devuelta");
            JOptionPane.showMessageDialog(this,
                    "Película marcada como disponible.");
        } else {
            label.setText("Estado: error al devolver película");
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar la película. " +
                    "Comprueba su ID y la conexión a la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Actualizar la tabla de películas
    private void actualizarTabla() {
        Object[][] peliculas = bd.obtenerPeliculas();

        modelo.setRowCount(0);

        for (Object[] fila : peliculas) {
            modelo.addRow(fila);
        }
    }

    // Mostrar los alquileres en una ventana emergente
    private void accionVerAlquileres() {
        Object[][] alquileres = bd.obtenerAlquileres();

        if (alquileres.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay alquileres registrados.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (Object[] fila : alquileres) {
            texto.append("ID: ").append(fila[0])
                    .append(" | ID película: ").append(fila[1])
                    .append(" | Cliente: ").append(fila[2])
                    .append(" | DNI: ").append(fila[3])
                    .append(" | Días: ").append(fila[4])
                    .append("\n");
        }

        JTextArea area = new JTextArea(texto.toString(), 15, 50);
        area.setEditable(false);
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Alquileres registrados",
                JOptionPane.INFORMATION_MESSAGE
        );

        label.setText("Estado: alquileres consultados");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
