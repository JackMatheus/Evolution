/*
Pré-requisitos e Instruções de Execução:

1.  **Software Necessário:**
    *   Java JDK (versão 11 ou superior) instalado e configurado (JAVA_HOME).
    *   Apache Maven instalado e configurado (M2_HOME, e `mvn` no PATH).
    *   Servidor MySQL em execução e acessível.

2.  **Configuração do Banco de Dados MySQL:**
    *   Crie um banco de dados (ex: `escola_db`).
    *   Execute o script SQL (fornecido em `schema.sql`) para criar a tabela `alunos_responsaveis`.
        ```sql
        CREATE TABLE alunos_responsaveis (
            cpf_aluno VARCHAR(11) NOT NULL,
            nome_aluno VARCHAR(100) NOT NULL,
            sobrenome_aluno VARCHAR(100) NOT NULL,
            telefone_responsavel VARCHAR(20) NOT NULL,
            email_responsavel VARCHAR(100) NULL,
            PRIMARY KEY (cpf_aluno)
        );
        ```

3.  **Configuração dos Arquivos de Propriedades:**
    *   Crie e configure `src/main/resources/db.properties`:
        ```properties
        db.url=jdbc:mysql://localhost:3306/escola_db?useSSL=false&serverTimezone=UTC
        db.username=seu_usuario_mysql
        db.password=sua_senha_mysql
        ```
        (Ajuste `escola_db`, `seu_usuario_mysql`, `sua_senha_mysql` conforme seu ambiente.)

    *   Crie e configure `src/main/resources/twilio.properties`:
        ```properties
        twilio.account.sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        twilio.auth.token=your_auth_token_xxxxxxxxxxxxxx
        twilio.whatsapp.number=whatsapp:+14155238886
        ```
        (Substitua pelos seus dados reais da Twilio. O `twilio.whatsapp.number` é o número do Sandbox da Twilio).

4.  **Opt-in do WhatsApp (Sandbox da Twilio):**
    *   O número de telefone do responsável (que receberá a mensagem) deve ter se registrado no Sandbox da Twilio.
    *   Para isso, envie uma mensagem do WhatsApp do telefone do responsável para o número do Sandbox da Twilio (o mesmo que você configurou em `twilio.whatsapp.number`). A mensagem deve ser a palavra-chave do seu Sandbox (ex: "join your-sandbox-keyword").

5.  **Criação do Diretório e Arquivo PDF do Boletim:**
    *   Crie o diretório `C:\evolution\cadastroAluno\boletins\` (ou ajuste o caminho na classe `ServicoBoletim.java`).
    *   Coloque um arquivo PDF de teste neste diretório com o nome esperado. Para os dados de teste abaixo, o nome seria `boletim_Tatiane_A_S.pdf`.
        Ex: `C:\evolution\cadastroAluno\boletins\boletim_Tatiane_A_S.pdf`

6.  **Estrutura do Projeto (Exemplo Maven):**
    ```
    seu-projeto/
    ├── pom.xml
    └── src/
        └── main/
            ├── java/
            │   └── (coloque aqui seus arquivos .java: Main.java, AlunoDAO.java, etc.)
            └── resources/
                ├── db.properties
                └── twilio.properties
    ```

7.  **Compilação e Execução (via Maven):**
    *   Abra um terminal na raiz do projeto (onde está o `pom.xml`).
    *   Compile o projeto:
        ```bash
        mvn compile
        ```
    *   Execute a classe `Main`:
        ```bash
        mvn exec:java -Dexec.mainClass="Main"
        ```
        (Se você usou pacotes, ajuste `Main` para o nome completo da classe, ex: `com.example.Main`)

    *   **Nota sobre `pom.xml`:** Certifique-se de que seu `pom.xml` inclui as dependências para Twilio e MySQL Connector/J:
        ```xml
        <dependencies>
            <dependency>
                <groupId>com.twilio.sdk</groupId>
                <artifactId>twilio</artifactId>
                <version>10.9.0</version> <!-- Ou a versão mais recente -->
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.33</version> <!-- Ou a versão mais recente compatível -->
            </dependency>
            <!-- Adicione SLF4J se a Twilio ou outra lib reclamar de logging -->
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-simple</artifactId>
                <version>1.7.32</version> <!-- Ou mais recente -->
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.codehaus.mojo</groupId>
                    <artifactId>exec-maven-plugin</artifactId>
                    <version>3.0.0</version>
                    <configuration>
                        <mainClass>Main</mainClass> <!-- Ajuste se usar pacote -->
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.8.1</version>
                    <configuration>
                        <source>11</source> 
                        <target>11</target>
                    </configuration>
                </plugin>
            </plugins>
        </build>
        ```
*/

import java.io.File;
import java.io.IOException;
import java.sql.SQLException; // Necessário se AlunoDAO lançar SQLException diretamente

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando aplicação de demonstração do Serviço de Boletim...");

        // 1. Instanciar AlunoDAO
        AlunoDAO alunoDAO = new AlunoDAO();

        // 2. Inserir dados de teste (Opcional, mas recomendado para a primeira execução)
        String cpfTeste = "12345678901";
        String nomeTeste = "Tatiane";
        String sobrenomeTeste = "A S"; // Conforme exemplo do usuário para nome do arquivo
        String telefoneResponsavelTeste = "5511970947799"; // Formato E.164 com código do país
        String emailResponsavelTeste = "responsavel.tatiane@example.com";

        try {
            // Verificar se o aluno já existe antes de tentar inserir
            AlunoInfo alunoExistente = alunoDAO.buscarDadosPorCPF(cpfTeste);
            if (alunoExistente == null) {
                System.out.println("Inserindo dados de teste para o aluno com CPF: " + cpfTeste);
                boolean inseriu = alunoDAO.inserirAlunoResponsavel(
                        cpfTeste,
                        nomeTeste,
                        sobrenomeTeste,
                        telefoneResponsavelTeste,
                        emailResponsavelTeste
                );
                if (inseriu) {
                    System.out.println("Dados de teste inseridos com sucesso!");
                } else {
                    System.err.println("Falha ao inserir dados de teste. Verifique os logs do AlunoDAO.");
                }
            } else {
                System.out.println("Dados de teste para o CPF " + cpfTeste + " já existem no banco. Pular inserção.");
            }
        } catch (Exception e) { // Captura mais genérica para cobrir SQLException ou outras do DAO
            System.err.println("Erro durante a inserção dos dados de teste: " + e.getMessage());
            e.printStackTrace();
            System.err.println("Continuando a execução sem a inserção de teste...");
        }
        
        // Verificação e criação do arquivo PDF de exemplo para o teste
        // Este caminho deve corresponder ao usado em ServicoBoletim
        String caminhoBaseBoletins = "C:\\evolution\\cadastroAluno\\boletins\\"; 
        String nomeArquivoBoletim = "boletim_" + nomeTeste + "_" + sobrenomeTeste.replace(" ", "_") + ".pdf";
        File arquivoBoletim = new File(caminhoBaseBoletins + nomeArquivoBoletim);

        if (!arquivoBoletim.exists()) {
            System.out.println("Tentando criar diretório de boletins em: " + caminhoBaseBoletins);
            File diretorioBoletins = new File(caminhoBaseBoletins);
            if (!diretorioBoletins.exists()) {
                diretorioBoletins.mkdirs();
            }
            System.out.println("Arquivo de boletim de teste não encontrado: " + arquivoBoletim.getAbsolutePath());
            System.out.println("Criando arquivo PDF de exemplo para fins de demonstração...");
            try {
                if (arquivoBoletim.createNewFile()) {
                    System.out.println("Arquivo PDF de exemplo criado em: " + arquivoBoletim.getAbsolutePath());
                    System.out.println("NOTA: Este é um arquivo vazio. Substitua por um PDF real para um teste completo.");
                } else {
                    System.err.println("Não foi possível criar o arquivo PDF de exemplo.");
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo PDF de exemplo: " + e.getMessage());
            }
        } else {
            System.out.println("Arquivo de boletim de teste encontrado: " + arquivoBoletim.getAbsolutePath());
        }


        // 3. Instanciar ServicoBoletim
        ServicoBoletim servicoBoletim = new ServicoBoletim(alunoDAO);

        // 4. Chamar servicoBoletim.enviarBoletimPorCPF()
        System.out.println("\nTentando enviar boletim (simulação) para o CPF: " + cpfTeste);
        boolean enviado = servicoBoletim.enviarBoletimPorCPF(cpfTeste);

        // 5. Imprimir o resultado da operação de envio
        if (enviado) {
            System.out.println("Simulação de envio do boletim para CPF " + cpfTeste + " reportada como SUCESSO.");
        } else {
            System.err.println("Simulação de envio do boletim para CPF " + cpfTeste + " reportada como FALHA.");
            System.err.println("Verifique os logs e as configurações (db.properties, twilio.properties, caminho do PDF, opt-in do WhatsApp).");
        }

        System.out.println("\nAplicação de demonstração concluída.");
    }
}
