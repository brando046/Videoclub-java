
import java.sql.*;

public class VideoclubBD {

    public Connection openConnection() {
        String connectionUrl =
                "jdbc:mariadb://localhost:3306/videoclub?user=root&password=1234";

        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }

    // Obtener todas las películas
    public Object[][] obtenerPeliculas() {
        String sql = "SELECT * FROM peliculas";

        try (Connection con = openConnection()) {
            if (con == null) {
                return new Object[0][0];
            }

            try (PreparedStatement prst = con.prepareStatement(sql);
                 ResultSet rs = prst.executeQuery()) {

                int contador = 0;

                while (rs.next()) {
                    contador++;
                }

                Object[][] datos = new Object[contador][5];

                rs.close();

                try (ResultSet rs2 = prst.executeQuery()) {
                    int i = 0;

                    while (rs2.next()) {
                        datos[i][0] = rs2.getInt("id");
                        datos[i][1] = rs2.getString("titulo");
                        datos[i][2] = rs2.getString("genero");
                        datos[i][3] = rs2.getInt("anyo");
                        datos[i][4] = rs2.getBoolean("disponible");
                        i++;
                    }
                }

                return datos;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener películas: " + e.getMessage());
            return new Object[0][0];
        }
    }

    // Añadir una película
    public boolean accionanyadirPelicula(
            int id, String titulo, String genero,
            int anyo, boolean disponible) {

        String sql = "INSERT INTO peliculas " +
                "(id, titulo, genero, anyo, disponible) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = openConnection()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setInt(1, id);
                prst.setString(2, titulo);
                prst.setString(3, genero);
                prst.setInt(4, anyo);
                prst.setBoolean(5, disponible);

                return prst.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al añadir película: " + e.getMessage());
            return false;
        }
    }

    // Registrar un alquiler y marcar la película como no disponible
    public boolean accionNuevoAlquiler(
            int id, int id_pelicula, String cliente,
            String dni, int dias) {

        String sqlComprobar =
                "SELECT disponible FROM peliculas WHERE id = ?";

        String sqlActualizar =
                "UPDATE peliculas SET disponible = false " +
                "WHERE id = ? AND disponible = true";

        String sqlInsertar =
                "INSERT INTO alquileres " +
                "(id, id_pelicula, cliente, dni, dias) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = openConnection()) {
            if (con == null) {
                return false;
            }

            con.setAutoCommit(false);

            try {
                // Comprobar que la película existe y está disponible
                try (PreparedStatement comprobar =
                             con.prepareStatement(sqlComprobar)) {

                    comprobar.setInt(1, id_pelicula);

                    try (ResultSet rs = comprobar.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            System.out.println("La película no existe.");
                            return false;
                        }

                        if (!rs.getBoolean("disponible")) {
                            con.rollback();
                            System.out.println("La película no está disponible.");
                            return false;
                        }
                    }
                }

                // Marcar la película como no disponible
                try (PreparedStatement actualizar =
                             con.prepareStatement(sqlActualizar)) {

                    actualizar.setInt(1, id_pelicula);

                    if (actualizar.executeUpdate() == 0) {
                        con.rollback();
                        return false;
                    }
                }

                // Insertar el alquiler
                try (PreparedStatement insertar =
                             con.prepareStatement(sqlInsertar)) {

                    insertar.setInt(1, id);
                    insertar.setInt(2, id_pelicula);
                    insertar.setString(3, cliente);
                    insertar.setString(4, dni);
                    insertar.setInt(5, dias);

                    insertar.executeUpdate();
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                System.out.println("Error al registrar alquiler: "
                        + e.getMessage());
                return false;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Error de conexión o transacción: "
                    + e.getMessage());
            return false;
        }
    }

    // Devolver una película
    public boolean devolverPelicula(int id_pelicula) {
        String sql =
                "UPDATE peliculas SET disponible = true WHERE id = ?";

        try (Connection con = openConnection()) {
            if (con == null) {
                return false;
            }

            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setInt(1, id_pelicula);

                return prst.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al devolver película: "
                    + e.getMessage());
            return false;
        }
    }

    // Obtener todos los alquileres
    public Object[][] obtenerAlquileres() {
        String sql = "SELECT * FROM alquileres";

        try (Connection con = openConnection()) {
            if (con == null) {
                return new Object[0][0];
            }

            try (PreparedStatement prst = con.prepareStatement(sql);
                 ResultSet rs = prst.executeQuery()) {

                int contador = 0;

                while (rs.next()) {
                    contador++;
                }

                Object[][] datos = new Object[contador][5];

                try (ResultSet rs2 = prst.executeQuery()) {
                    int i = 0;

                    while (rs2.next()) {
                        datos[i][0] = rs2.getInt("id");
                        datos[i][1] = rs2.getInt("id_pelicula");
                        datos[i][2] = rs2.getString("cliente");
                        datos[i][3] = rs2.getString("dni");
                        datos[i][4] = rs2.getInt("dias");
                        i++;
                    }
                }

                return datos;
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener alquileres: "
                    + e.getMessage());
            return new Object[0][0];
        }
    }
}
