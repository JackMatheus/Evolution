// Lembre-se de criar o arquivo db.properties em src/main/resources com o seguinte conteúdo:
// db.url=jdbc:mysql://localhost:3306/NOMEDOBANCO
// db.username=USUARIO
// db.password=SENHA

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Desculpe, não foi possível encontrar o arquivo db.properties");
                // Considerar lançar uma exceção aqui ou tratar de outra forma
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            System.err.println("Erro ao ler o arquivo db.properties: " + ex.getMessage());
            // Considerar lançar uma exceção aqui ou tratar de outra forma
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do MySQL não encontrado: " + e.getMessage());
            // Considerar lançar uma exceção aqui ou tratar de outra forma
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (url == null || username == null || password == null) {
            throw new SQLException("Configurações do banco de dados (url, username, password) não encontradas em db.properties.");
        }

        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        // Teste rápido de conexão (opcional)
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            } else {
                System.err.println("Falha ao estabelecer conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao tentar conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
