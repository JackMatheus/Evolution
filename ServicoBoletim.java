import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.File;

public class ServicoBoletim {

    private final AlunoDAO alunoDAO;

    // Caminho base para os boletins.
    // IMPORTANTE: Este caminho deve ser configurável ou verificado cuidadosamente em um ambiente de produção.
    // Pode ser uma propriedade em um arquivo de configuração, uma variável de ambiente, etc.
    // Certifique-se de que a aplicação tenha permissões de leitura neste diretório.
    private static final String CAMINHO_BASE_BOLETINS = "C:\\evolution\\cadastroAluno\\boletins\\";


    public ServicoBoletim(AlunoDAO alunoDAO) {
        this.alunoDAO = alunoDAO;
    }

    public boolean enviarBoletimPorCPF(String cpfAluno) {
        try {
            // a. Inicializar Twilio
            TwilioConfig.init();

            // b. Buscar dados do aluno
            AlunoInfo alunoInfo = alunoDAO.buscarDadosPorCPF(cpfAluno);
            if (alunoInfo == null) {
                System.err.println("Aluno com CPF " + cpfAluno + " não encontrado.");
                return false;
            }

            String telefoneResponsavel = alunoInfo.getTelefoneResponsavel();
            if (telefoneResponsavel == null || telefoneResponsavel.trim().isEmpty()) {
                System.err.println("Telefone do responsável não encontrado para o aluno com CPF: " + cpfAluno);
                return false;
            }

            // c. Construir nome do arquivo do boletim
            String nomeArquivo = "boletim_" + alunoInfo.getNomeAluno() + "_" + alunoInfo.getSobrenomeAluno() + ".pdf";
            nomeArquivo = nomeArquivo.replace(" ", "_"); // Remover espaços para nomes de arquivo

            // d. Construir caminho local completo para o arquivo PDF
            String caminhoCompletoPDF = CAMINHO_BASE_BOLETINS + nomeArquivo;

            // e. Verificar se o arquivo PDF existe localmente
            File arquivoBoletim = new File(caminhoCompletoPDF);
            if (!arquivoBoletim.exists()) {
                System.err.println("Arquivo do boletim não encontrado em: " + caminhoCompletoPDF);
                return false;
            }

            // f. Lógica de Envio do PDF Local com Twilio (Simulação)
            // A API padrão da Twilio para envio de mensagens WhatsApp (usada aqui) espera uma mediaUrl
            // para enviar arquivos de mídia como PDFs. Isso significa que o arquivo PDF precisaria
            // estar acessível publicamente através de uma URL.
            //
            // Para enviar um arquivo local diretamente, seria necessário:
            // 1. Fazer upload do arquivo para um serviço de hospedagem (ex: Amazon S3, Google Cloud Storage,
            //    ou um servidor web próprio) para obter uma URL pública temporária ou permanente.
            // 2. Ou usar a API de Mídia da Twilio para fazer upload do arquivo para a Twilio e obter um MediaSid,
            //    que pode então ser usado no Message.creator. Esta API é um pouco mais complexa e pode
            //    ter custos associados.
            // 3. Ou utilizar Twilio Functions para hospedar a lógica de upload e disponibilizar uma URL.
            //
            // Para esta etapa, vamos SIMULAR o envio, enviando uma mensagem de texto indicando o nome do arquivo.

            String mensagemTexto = "Prezado(a) responsável,\n\nSegue em anexo o boletim escolar: " + nomeArquivo +
                                   "\n\nEste é um serviço de notificação. Em caso de dúvidas, entre em contato com a secretaria." +
                                   "\n\n(Simulação: O arquivo PDF estaria anexado aqui se uma mediaUrl fosse fornecida).";


            PhoneNumber para = new PhoneNumber("whatsapp:" + telefoneResponsavel);
            PhoneNumber de = new PhoneNumber(TwilioConfig.getWhatsappNumber());

            Message message = Message.creator(para, de, mensagemTexto).create();

            System.out.println("Mensagem (simulada) enviada com SID: " + message.getSid());
            System.out.println("Conteúdo da mensagem: " + mensagemTexto);
            System.out.println("Para: " + para.getEndpoint());
            System.out.println("De: " + de.getEndpoint());

            return true;

        } catch (ApiException e) {
            System.err.println("Erro da API da Twilio ao enviar mensagem: " + e.getMessage());
            System.err.println("Código do erro: " + e.getCode());
            System.err.println("Mais informações: " + e.getMoreInfo());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao tentar enviar boletim: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // Bloco de teste (requer configuração de twilio.properties, db.properties,
        // banco de dados com a tabela alunos_responsaveis e um arquivo PDF de exemplo)

        // 1. Crie um AlunoDAO (pode ser mockado ou real se o DB estiver configurado)
        AlunoDAO alunoDAO = new AlunoDAO(); // Supondo que DatabaseConfig esteja funcional

        // 2. Insira um aluno de teste no banco de dados (ou certifique-se de que exista um)
        // Exemplo: CPF "11122233344", Nome "Maria", Sobrenome "Souza", Telefone "5521987654321"
        // boolean inseriu = alunoDAO.inserirAlunoResponsavel("11122233344", "Maria", "Souza", "5521987654321", "maria.souza@email.com");
        // System.out.println("Aluno de teste inserido: " + inseriu);


        // 3. Crie um arquivo PDF de teste no caminho esperado:
        // C:\evolution\cadastroAluno\boletins\boletim_Maria_Souza.pdf
        // (Crie um arquivo PDF qualquer com este nome para o teste passar na verificação de existência)
        File pastaBoletins = new File(CAMINHO_BASE_BOLETINS);
        if (!pastaBoletins.exists()){
            pastaBoletins.mkdirs();
        }
        try {
            // Cria um arquivo de exemplo se não existir.
            // Substitua isso pela criação real de um PDF ou coloque um PDF de teste manualmente.
            File pdfExemplo = new File(CAMINHO_BASE_BOLETINS + "boletim_Maria_Souza.pdf");
            if(!pdfExemplo.exists()){
                 pdfExemplo.createNewFile();
                 System.out.println("Arquivo PDF de exemplo criado em: " + pdfExemplo.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo PDF de exemplo: " + e.getMessage());
        }


        // 4. Crie o serviço de boletim
        ServicoBoletim servicoBoletim = new ServicoBoletim(alunoDAO);

        // 5. Teste o envio
        System.out.println("\nTestando envio de boletim...");
        String cpfTeste = "11122233344"; // CPF do aluno de teste
        boolean enviado = servicoBoletim.enviarBoletimPorCPF(cpfTeste);

        if (enviado) {
            System.out.println("Simulação de envio de boletim para CPF " + cpfTeste + " concluída com sucesso.");
        } else {
            System.err.println("Falha na simulação de envio de boletim para CPF " + cpfTeste + ".");
        }
    }
}
