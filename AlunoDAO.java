import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlunoDAO {

    public boolean inserirAlunoResponsavel(String cpfAluno, String nomeAluno, String sobrenomeAluno, String telefoneResponsavel, String emailResponsavel) {
        String sql = "INSERT INTO alunos_responsaveis (cpf_aluno, nome_aluno, sobrenome_aluno, telefone_responsavel, email_responsavel) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpfAluno);
            pstmt.setString(2, nomeAluno);
            pstmt.setString(3, sobrenomeAluno);
            pstmt.setString(4, telefoneResponsavel);
            pstmt.setString(5, emailResponsavel);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir dados do aluno/responsável: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public AlunoInfo buscarDadosPorCPF(String cpfAluno) {
        String sql = "SELECT nome_aluno, sobrenome_aluno, telefone_responsavel FROM alunos_responsaveis WHERE cpf_aluno = ?";
        AlunoInfo alunoInfo = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpfAluno);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nomeAluno = rs.getString("nome_aluno");
                String sobrenomeAluno = rs.getString("sobrenome_aluno");
                String telefoneResponsavel = rs.getString("telefone_responsavel");
                alunoInfo = new AlunoInfo(nomeAluno, sobrenomeAluno, telefoneResponsavel);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar dados do aluno por CPF: " + e.getMessage());
            e.printStackTrace();
        }
        return alunoInfo;
    }

    public static void main(String[] args) {
        // Bloco de teste (opcional, requer que o banco e a tabela existam e db.properties esteja configurado)
        AlunoDAO dao = new AlunoDAO();

        // Teste de inserção
        System.out.println("Testando inserção...");
        boolean inseriu = dao.inserirAlunoResponsavel(
                "12345678901",
                "João",
                "Silva",
                "5511999998888",
                "joao.silva@example.com"
        );
        if (inseriu) {
            System.out.println("Aluno/Responsável inserido com sucesso!");
        } else {
            System.err.println("Falha ao inserir aluno/responsável.");
        }

        System.out.println("\nTestando inserção de aluno duplicado (deve falhar se CPF é PRIMARY KEY)...");
        boolean inseriuDuplicado = dao.inserirAlunoResponsavel(
                "12345678901",
                "João",
                "Silva",
                "5511999998888",
                "joao.silva@example.com"
        );
        if (!inseriuDuplicado) {
            System.out.println("Falha ao inserir aluno duplicado, como esperado.");
        } else {
            System.err.println("Aluno duplicado inserido, o que não deveria acontecer.");
        }


        // Teste de busca
        System.out.println("\nTestando busca por CPF existente...");
        AlunoInfo info = dao.buscarDadosPorCPF("12345678901");
        if (info != null) {
            System.out.println("Aluno encontrado: " + info);
        } else {
            System.err.println("Aluno com CPF 12345678901 não encontrado (após tentativa de inserção).");
        }

        System.out.println("\nTestando busca por CPF inexistente...");
        AlunoInfo infoNaoExistente = dao.buscarDadosPorCPF("00000000000");
        if (infoNaoExistente == null) {
            System.out.println("Aluno com CPF 00000000000 não encontrado, como esperado.");
        } else {
            System.err.println("Erro: Aluno com CPF 00000000000 foi encontrado.");
        }
    }
}
