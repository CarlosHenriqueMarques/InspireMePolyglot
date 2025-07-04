
# InspireMe Polyglot

> Aplicativo Android em Kotlin com Jetpack Compose que exibe frases motivacionais diárias em múltiplos idiomas, com compartilhamento social, notificações diárias e integração de anúncios.

---

## Índice

- [Descrição](#descrição)  
- [Funcionalidades](#funcionalidades)  
- [Arquitetura e Tecnologias](#arquitetura-e-tecnologias)  
- [Como rodar](#como-rodar)  
- [Detalhes Técnicos](#detalhes-técnicos)  
- [Estrutura de Código](#estrutura-de-código)  
- [Uso das Bibliotecas](#uso-das-bibliotecas)  
- [Considerações sobre Performance](#considerações-sobre-performance)  
- [Licença](#licença)

---

## Descrição

InspireMe Polyglot é um app que apresenta frases motivacionais em 4 idiomas (Inglês, Português, Francês, Espanhol). O usuário pode selecionar os idiomas desejados e receber notificações diárias com frases aleatórias. A interface é feita com Jetpack Compose para uma UI moderna e responsiva.

---

## Funcionalidades

- Carregamento assíncrono de frases via JSON local  
- Seleção múltipla de idiomas  
- Troca aleatória de frases  
- Compartilhamento para WhatsApp e Instagram Stories  
- Notificação diária com frases (via WorkManager)  
- Anúncios AdMob com exibição controlada para evitar travamentos  
- Tema adaptado com Material3 (modo claro/escuro)  

---

## Arquitetura e Tecnologias

- **Kotlin** como linguagem principal  
- **Jetpack Compose** para UI declarativa  
- **WorkManager** para tarefas periódicas de notificações  
- **Coroutines** para operações assíncronas (carregamento JSON)  
- **Gson** para parsing de JSON  
- **AdMob** para anúncios nativos (banner)  
- **Material3** para componentes UI modernos  
- Uso do padrão MVVM simplificado com separação entre UI, modelo de dados e utilitários  

---

## Como rodar

1. Clone o repositório  
2. Abra no Android Studio (última versão recomendada)  
3. Certifique-se que o SDK mínimo é compatível (Android 7.0+ recomendado)  
4. Execute o app em um emulador ou dispositivo real  
5. Teste notificações (garanta permissão para `POST_NOTIFICATIONS` em Android 13+)  
6. Configure as chaves do AdMob se desejar testar anúncios reais  

---

## Detalhes Técnicos

- **Carregamento do JSON**: feito com coroutine no dispatcher IO para não bloquear UI  
- **Gerenciamento de estado Compose**: uso de `remember`, `mutableStateOf` e `mutableStateListOf` para reatividade  
- **Controle do banner AdMob**: carregamento atrasado via `LaunchedEffect` com `delay()` dinâmico para evitar ANRs  
- **Notificações**: implementadas via `Worker` do WorkManager com canal de notificações para Android 8+  
- **Compartilhamento social**: utiliza intents explícitas para WhatsApp e Instagram Stories, formatando mensagem com emojis por idioma  

---

## Estrutura de Código

```
/app/src/main/java/com/carlos/inspiremepolyglot/
├── MainActivity.kt            # Activity principal com tema e agendamento de notificações
├── ui/
│   ├── screen/
│   │   └── PhrasesScreen.kt  # UI principal das frases, seleção idiomas e compartilhamento
│   └── components/
│       └── AdBanner.kt       # Componente customizado para exibição de anúncios AdMob
├── utils/
│   ├── JsonUtils.kt          # Funções para carregar JSON com coroutines
│   ├── NotificationWorker.kt # Worker para notificações diárias
│   ├── ShareUtils.kt         # Funções utilitárias para compartilhamento
│   └── ...                   # Outras utilidades
├── data/
│   └── model/
│       └── PhraseList.kt     # Modelo de dados que representa o JSON das frases
└── theme/
    └── InspireMePolyglotTheme.kt # Definições de tema Material3
```

---

## Uso das Bibliotecas

| Biblioteca             | Propósito                       |
|-----------------------|--------------------------------|
| kotlinx.coroutines     | Assíncrono para carregamento e tasks background |
| androidx.work          | WorkManager para notificações periódicas         |
| com.google.gson       | Parsing JSON para objetos Kotlin                   |
| com.google.android.gms:play-services-ads | AdMob para anúncios banner          |
| androidx.compose.*    | UI declarativa moderna com Material3               |

---

## Considerações sobre Performance

- O carregamento do JSON é feito em background para não travar UI  
- O banner de anúncios tem seu carregamento atrasado para evitar ANRs, principalmente em dispositivos mais lentos  
- Notificações são agendadas com WorkManager para eficiência e sobrevivência em background  
- Uso de `remember` e `mutableState` para garantir recomposições mínimas  

---

## Licença

Este projeto é aberto e gratuito para uso pessoal e comercial. Sinta-se livre para adaptar, melhorar e compartilhar.

---
