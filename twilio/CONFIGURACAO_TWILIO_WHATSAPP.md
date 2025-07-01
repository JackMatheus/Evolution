# Guia de Configuração da Twilio para Envio de Mensagens WhatsApp

Este guia detalha os passos necessários para configurar sua conta Twilio e o ambiente de teste (Sandbox) para enviar mensagens WhatsApp através da API da Twilio.

## 1. Criar uma Conta na Twilio

*   **Acesse a Twilio:** Vá para [https://www.twilio.com/try-twilio](https://www.twilio.com/try-twilio) para criar sua conta.
*   **Trial Gratuito:** A Twilio oferece um período de trial gratuito que permite testar a maioria das funcionalidades.
    *   **Limitações do Trial:** Durante o trial, geralmente é necessário verificar os números de telefone para os quais você envia mensagens (números de destino). Você também receberá um saldo inicial para testes. Para remover essas limitações e usar seus próprios números de telefone dedicados, será preciso fazer um upgrade da sua conta.

## 2. Obter Credenciais da Conta (Account SID e Auth Token)

Após criar e logar na sua conta Twilio:

*   **Localize suas Credenciais:**
    *   O `Account SID` (Identificador da Conta) e o `Auth Token` (Token de Autenticação) são as suas chaves de acesso à API da Twilio.
    *   Você geralmente encontrará esses valores na página principal do seu console da Twilio (Dashboard) assim que fizer login: [https://console.twilio.com/](https://console.twilio.com/).
    *   Procure por uma seção chamada "Account Info" ou similar. O `Account SID` estará visível, e o `Auth Token` pode estar oculto por padrão (clique em "View" ou "Show" para revelá-lo).
*   **Copie para `twilio.properties`:**
    *   Esses valores são essenciais para que sua aplicação Java possa se autenticar com a Twilio.
    *   Abra (ou crie) o arquivo `twilio.properties` no diretório `src/main/resources/` do seu projeto Java.
    *   Adicione/atualize as seguintes linhas com as suas credenciais:
        ```properties
        twilio.account.sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        twilio.auth.token=your_auth_token_xxxxxxxxxxxxxx
        ```
        (Substitua `ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` pelo seu Account SID e `your_auth_token_xxxxxxxxxxxxxx` pelo seu Auth Token.)

## 3. Configurar o Sandbox do WhatsApp da Twilio

O Sandbox da Twilio para WhatsApp permite que você envie e receba mensagens WhatsApp imediatamente, usando um número de telefone compartilhado pela Twilio, sem precisar esperar pela aprovação de um número próprio.

*   **Acessar o Sandbox:**
    1.  No console da Twilio, navegue até a seção de "Messaging".
    2.  Procure por opções como "Try it out", "Send a WhatsApp message", "WhatsApp Sandbox" ou similar. O caminho exato pode variar um pouco dependendo da versão do console, mas geralmente está em:
        *   `Messaging > Try it out > Send a WhatsApp message`
        *   Ou diretamente em `Messaging > Senders > WhatsApp Sandbox`.
*   **Número do Sandbox (Remetente):**
    *   Dentro da página de configuração do Sandbox, você verá um número de telefone fornecido pela Twilio (ex: `+1 (415) 523-8886`). Este é o número que será usado como remetente das suas mensagens de teste.
    *   Copie este número, incluindo o prefixo `whatsapp:`, para o seu arquivo `twilio.properties`:
        ```properties
        twilio.whatsapp.number=whatsapp:+14155238886
        ```
        (Substitua `+14155238886` pelo número do Sandbox exibido na sua conta.)
*   **Processo de Opt-in (Registro do Destinatário no Sandbox):**
    *   Para que um número de telefone possa receber mensagens do Sandbox da Twilio, ele precisa primeiro se registrar (fazer o "opt-in").
    *   **Como fazer o Opt-in:**
        1.  Na mesma página de configuração do Sandbox no console da Twilio, você encontrará uma "palavra-chave" (keyword) específica para o seu sandbox (ex: `join something-random`).
        2.  Pegue o celular cujo número WhatsApp você deseja usar para receber as mensagens de teste (ex: o seu número pessoal, como `(11) 97094-7799`).
        3.  Abra o WhatsApp neste celular.
        4.  Envie uma mensagem do seu WhatsApp pessoal para o número do Sandbox da Twilio (o `whatsapp:+14155238886` que você configurou).
        5.  O conteúdo dessa mensagem deve ser **exatamente** a palavra-chave do seu sandbox (ex: `join something-random`).
    *   **Confirmação:** Após enviar a mensagem de opt-in, você deverá receber uma resposta automática da Twilio no seu WhatsApp confirmando que seu número foi conectado ao sandbox.
    *   **Importância:** É crucial que o número de destino (o telefone do responsável no seu projeto) complete este processo de opt-in. Caso contrário, as tentativas de envio de mensagens do seu código para este número via Sandbox falharão.

## 4. Saindo do Sandbox (Próximos Passos para Produção)

*   O Sandbox é excelente para desenvolvimento e testes.
*   Para enviar mensagens para qualquer usuário do WhatsApp sem que eles precisem fazer o opt-in no seu sandbox, você precisará de um **Número de Telefone da Twilio habilitado para WhatsApp** (um "WhatsApp Sender" dedicado).
*   Este processo envolve:
    1.  Ter uma conta Twilio com upgrade (não mais a conta trial).
    2.  Adquirir um número de telefone da Twilio (ou portar um existente) que seja capaz de usar o WhatsApp.
    3.  Submeter um pedido de habilitação do seu número para uso com a API do WhatsApp, o que geralmente envolve fornecer informações sobre seu negócio e casos de uso. Este processo é gerenciado pela Twilio em conjunto com o WhatsApp e pode levar algum tempo para aprovação.

Consulte a documentação oficial da Twilio para mais detalhes sobre a transição do Sandbox para um ambiente de produção com seu próprio número.

---

Lembre-se de manter suas credenciais (`Account SID` e `Auth Token`) seguras e nunca as exponha publicamente (por exemplo, em código versionado no GitHub). O uso de variáveis de ambiente ou arquivos de configuração não versionados é recomendado para produção.
