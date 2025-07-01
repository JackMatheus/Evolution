// Lembre-se de criar o arquivo twilio.properties em src/main/resources com o seguinte conteúdo:
// twilio.account.sid=ACxxxxxxxxxxxxxxx
// twilio.auth.token=your_auth_token
// twilio.whatsapp.number=whatsapp:+14155238886

import com.twilio.Twilio;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TwilioConfig {

    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "twilio.properties";

    private static String accountSid;
    private static String authToken;
    private static String twilioWhatsappNumber;

    static {
        try (InputStream input = TwilioConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("Desculpe, não foi possível encontrar o arquivo " + PROPERTIES_FILE);
                // Considerar lançar uma RuntimeException para interromper a inicialização se as props são críticas
            } else {
                properties.load(input);
                accountSid = properties.getProperty("twilio.account.sid");
                authToken = properties.getProperty("twilio.auth.token");
                twilioWhatsappNumber = properties.getProperty("twilio.whatsapp.number");

                if (accountSid == null || authToken == null || twilioWhatsappNumber == null ||
                    accountSid.trim().isEmpty() || authToken.trim().isEmpty() || twilioWhatsappNumber.trim().isEmpty()) {
                    System.err.println("As propriedades twilio.account.sid, twilio.auth.token, ou twilio.whatsapp.number não estão configuradas corretamente em " + PROPERTIES_FILE);
                    // Considerar lançar uma RuntimeException
                }
            }
        } catch (IOException ex) {
            System.err.println("Erro ao ler o arquivo " + PROPERTIES_FILE + ": " + ex.getMessage());
            // Considerar lançar uma RuntimeException
        }
    }

    public static String getAccountSid() {
        return accountSid;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static String getWhatsappNumber() {
        return twilioWhatsappNumber;
    }

    public static void init() {
        if (getAccountSid() == null || getAuthToken() == null) {
            System.err.println("Não é possível inicializar Twilio. SID da conta ou Token de autenticação não estão carregados.");
            // Poderia lançar uma IllegalStateException aqui
            return;
        }
        Twilio.init(getAccountSid(), getAuthToken());
        System.out.println("Twilio SDK inicializado.");
    }

    public static void main(String[] args) {
        // Teste rápido (opcional)
        System.out.println("Account SID: " + getAccountSid());
        System.out.println("Auth Token: " + getAuthToken());
        System.out.println("Twilio WhatsApp Number: " + getWhatsappNumber());
        // Para testar a inicialização, descomente a linha abaixo após configurar o twilio.properties
        // init();
    }
}
