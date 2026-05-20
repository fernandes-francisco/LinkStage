# LinkStage

![Kotlin](https://img.shields.io/badge/Kotlin-Android-blueviolet?logo=kotlin)
![Android](https://img.shields.io/badge/Android-SDK%2036-green?logo=android)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-blue?logo=jetpackcompose)
![Supabase](https://img.shields.io/badge/Supabase-Backend-3ECF8E?logo=supabase)
![Gradle](https://img.shields.io/badge/Gradle-Build-02303A?logo=gradle)
![License](https://img.shields.io/badge/License-Academic-lightgrey)
![Status](https://img.shields.io/badge/Status-v0.1.0%20technical%20base-orange)

Aplicação Android para gestão de estágios académicos, desenvolvida em Kotlin com Jetpack Compose e Supabase.

## Estado atual

Versão técnica inicial: `v0.1.0`

Implementado:

- Models remotos
- Inputs para operações de escrita
- Cliente Supabase
- Repositories da camada de dados
- Autenticação base com Supabase Auth
- Testes instrumentados de integração

Ainda por implementar:

- ViewModels
- Ecrãs finais
- Navegação
- Suporte PT/EN
- Suporte portrait/landscape
- Modo offline/sincronização
- APK final

## Stack

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Supabase
    - Auth
    - Database/PostgREST
    - Edge Functions
- Room
- Kotlin Serialization
- Gradle

## Estrutura principal

```text
app/src/main/java/turmaA/grupoB/LinkStage
├── data
│   ├── remote
│   │   ├── model
│   │   └── supabase
│   └── repository
└── ui
    └── theme
```

## Configuração

Criar/configurar o ficheiro `local.properties` na raiz do projeto:

```properties
SUPABASE_URL=https://<project-ref>.supabase.co
SUPABASE_ANON_KEY=<anon-key>
```

A `service_role key` não deve ser usada na aplicação Android.

## Build

Windows PowerShell:

```powershell
.\gradlew clean build
```

Linux/macOS:

```bash
./gradlew clean build
```

## Testes

Os testes instrumentados estão em:

```text
app/src/androidTest
```

Para executar, é necessário ter um emulador ou dispositivo Android ligado.

Windows PowerShell:

```powershell
.\gradlew connectedAndroidTest
```

Linux/macOS:

```bash
./gradlew connectedAndroidTest
```

## Supabase

O projeto usa Supabase para:

- Autenticação
- Persistência remota
- Queries através do Supabase SDK
- Testes de integração

Durante testes de autenticação, podem ser criados utilizadores temporários com o padrão:

```text
test+<timestamp>@linkstage.test
```

A limpeza destes utilizadores é feita por configuração externa no Supabase.

## Git workflow

Branches principais:

```text
main
develop
feature/<nome-da-feature>
```

Exemplos:

```text
feature/supabase-models
feature/supabase-client
feature/supabase-repositories
feature/supabase-auth
```

## Conventional Commits

Exemplos:

```bash
feat(data): add remote models
feat(data): configure Supabase client
feat(data): add repositories
feat(auth): add auth repository
test(auth): add sign up integration test
fix(data): correct application status enum values
```

## Versão

`v0.1.0` representa a primeira base técnica do projeto:

- camada de dados remota
- integração Supabase
- autenticação base
- testes de integração

Esta versão ainda não representa uma aplicação final utilizável.