# Atipera Recruitment Task

Rozwiązanie zadania rekrutacyjnego polegającego na stworzeniu API do pobierania informacji o repozytoriach użytkownika z serwisu GitHub.

## Technologie
* **Java 25**
* **Spring Boot 4.0.6**
* **Gradle**
* **WireMock**
* **AssertJ**

## Funkcjonalności
* Pobieranie listy repozytoriów użytkownika z GitHub API.
* Logika biznesowa: filtrowanie – aplikacja automatycznie pomija repozytoria będące forkami.
* Wzbogacanie danych: dla każdego repozytorium pobierane są informacje o branchach (nazwa oraz SHA ostatniego commita).
* Obsługa błędów: zwracanie ustandaryzowanego JSON-a w przypadku nieistniejącego użytkownika (404).

## Dokumentacja API
Aplikacja wystawia RESTowy endpoint:
'GET /api/github/{username}'

### Przykładowa odpowiedź (200 OK)
```json
[
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "master",
        "lastCommitSha": "7e068727fdb347b685b658d2981f8c85f7bf0585"
      }
    ]
  }
]
```

## Uruchomienie projektu
Aby uruchomić aplikację lokalnie:
1. Sklonuj repozytorium.
2. Uruchom aplikację komendą:
   ./gradlew bootRun

