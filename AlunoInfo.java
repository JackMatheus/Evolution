public class AlunoInfo {

    private String nomeAluno;
    private String sobrenomeAluno;
    private String telefoneResponsavel;

    // Construtor padrão
    public AlunoInfo() {
    }

    // Construtor com todos os campos
    public AlunoInfo(String nomeAluno, String sobrenomeAluno, String telefoneResponsavel) {
        this.nomeAluno = nomeAluno;
        this.sobrenomeAluno = sobrenomeAluno;
        this.telefoneResponsavel = telefoneResponsavel;
    }

    // Getters e Setters
    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getSobrenomeAluno() {
        return sobrenomeAluno;
    }

    public void setSobrenomeAluno(String sobrenomeAluno) {
        this.sobrenomeAluno = sobrenomeAluno;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    @Override
    public String toString() {
        return "AlunoInfo{" +
                "nomeAluno='" + nomeAluno + '\'' +
                ", sobrenomeAluno='" + sobrenomeAluno + '\'' +
                ", telefoneResponsavel='" + telefoneResponsavel + '\'' +
                '}';
    }
}
