# Notification Listener App

Aplicativo Android que monitora notificações de aplicativos específicos e envia os dados para uma API externa.
Este projeto faz parte do sistema **Gastos**, responsável por registrar e analisar transações a partir de notificações bancárias.

---

## Funcionalidade

O app utiliza um `NotificationListenerService` para capturar notificações recebidas no dispositivo Android.
Quando uma notificação de um dos aplicativos permitidos é detectada, o app extrai as informações principais e as envia em formato JSON para o endpoint configurado no `.env`.
App reinicia com o celular e continua funcionando em segundo plano.

---

## Tecnologias utilizadas

- **Kotlin**
- **Android SDK 26+**
- **OkHttp 4.12.0**
- **Gson 2.11.0**
- **Compose Material 3**

---

## Estrutura principal
```
app/
├── src/
│ ├── main/
│ │ ├── AndroidManifest.xml
│ │ ├── java/com/example/notificationlistener/
│ │ │ ├── core/
│ │ │ │ ├── BootReceiver.kt
│ │ │ │ ├── ForegroundService.kt
│ │ │ │ └── GastosNotificationListener.kt
│ │ │ └── ui/theme/
│ │ │ └── MainActivity.kt
│ │ └── res/
├── build.gradle.kts
└── .gitignore
```
---

## Execução

1. Abra o projeto no **Android Studio**.
2. Aguarde a sincronização do Gradle.
3. Conecte um dispositivo Android ou execute um emulador.
4. Clique em **Run App (Shift + F10)**.
5. Conceda a permissão de acesso às notificações quando solicitado.

---

## Aplicativos monitorados

Por padrão, o listener está configurado para capturar notificações de:

- Nubank (`com.nu.production`)
- Banco Inter (`br.com.intermedium`)
- Discord (`com.discord`) – usado para testes

---

## Estrutura dos dados enviados

```json
{
  "app": "com.nu.production",
  "titulo": "Compra aprovada",
  "mensagem": "R$ 35,90 em Mercado Central",
  "data": "2025-10-23T14:32:00"
}


## Licença

Projeto de uso pessoal e experimental.
Proibida a reprodução sem autorização.



Este projeto foi desenvolvido com o auxílio de ferramentas de IA para apoiar a geração de código e documentação.
