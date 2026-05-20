# SkillMarket

## Zadanie
Cieľom je vytvoriť webovú platformu, ktorá umožní prepájať klientov a freelancerov pri realizácii projektov. Klienti môžu publikovať projekty, ku ktorým môžu freelanceri posielať svoje ponuky. Klienti následne vyberú vhodného freelancera, ktorý projekt zrealizuje.

Platforma má umožniť jednoduché vyhľadávanie freelancerov podľa zručností, komunikáciu medzi používateľmi a hodnotenie kvality spolupráce.

Používatelia môžu vystupovať ako klienti alebo freelanceri. Freelanceri môžu prezentovať svoje schopnosti a skúsenosti, zatiaľ čo klienti môžu publikovať projekty s požiadavkami a rozpočtom.

## Zber požiadaviek

- RQ01 Systém umožní registrovať nového používateľa.
- RQ02 Systém umožní používateľovi prihlásiť sa pomocou e-mailu a hesla.
- RQ03 Systém umožní klientovi vytvoriť nový projekt.
- RQ04 Systém umožní klientovi upravovať alebo zrušiť projekt, pokiaľ ešte nebol prijatý freelancerom.
- RQ05 Systém umožní freelancerom prezerať zoznam dostupných projektov.
- RQ06 Systém umožní freelancerovi poslať ponuku na projekt.
- RQ07 Systém umožní klientovi prijať alebo odmietnuť ponuku freelancera.
- RQ08 Systém umožní klientovi hodnotiť freelancera po dokončení projektu.
- RQ09 Systém umožní vyhľadávať freelancerov podľa zručností.
- RQ10 Systém umožní filtrovať projekty (v súčasnosti zobrazenie všetkých dostupných projektov alebo projektov používateľa).
- RQ11 Systém umožní používateľovi upravovať svoj profil.
- RQ12 Systém umožní notifikovať používateľov o nových ponukách alebo zmenách projektu (v doméne pripravené, REST rozhranie zatiaľ nepodporuje push notifikácie).
- RQ13 Systém umožní administrátorovi spravovať používateľov a projekty.
- RQ14 Systém umožní zobrazovať históriu projektov používateľa.

## Slovník pojmov

|Pojem | Anglický názov | Definícia |
| --- | --- | --- |
| Používateľ | User| Osoba registrovaná v systéme, ktorá môže vystupovať ako klient alebo freelancer. |
| Klient | Client | Používateľ, ktorý vytvára projekty a hľadá freelancerov na ich realizáciu. |
| Freelancer | Freelancer | Používateľ, ktorý ponúka svoje služby a môže reagovať na projekty klientov. |
| Projekt |	Project | Zadanie vytvorené klientom obsahujúce opis práce, požiadavky a rozpočet. |
| Ponuka | Offer | Návrh freelancera na realizáciu projektu vrátane ceny a správy pre klienta. |
| Zručnosť | Skill | Schopnosť alebo technológia, ktorú freelancer ovláda (napr. programovanie, dizajn). |
| Hodnotenie | Rating | Spätná väzba klienta na freelancera po dokončení projektu. |
| Profil | Profile | Informácie o používateľovi vrátane zručností, skúseností a hodnotení. |
| Administrátor | Administrator | Používateľ, ktorý nie je klient, ani freelancer. Má oprávnenia spravovať systém a jeho obsah. |

## Class diagram

<img width="1460" height="992" alt="class-scheme" src="https://github.com/user-attachments/assets/328396b7-2585-4c5c-a8a8-5cdf67120745" />


## Use Cases

### UC01 – Registrácia používateľa
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ zadá email, heslo a meno  
2. Systém vytvorí používateľa v Keycloak a priradí mu rolu  
3. Systém vytvorí lokálny profil používateľa  
4. Používateľ je uložený v systéme  

---

### UC02 – Prihlásenie používateľa
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ zadá prihlasovacie údaje do Keycloak  
2. Keycloak overí údaje  
3. Keycloak vygeneruje JWT token  
4. Používateľ pristupuje k API s týmto tokenom  

---

### UC03 – Vytvorenie projektu
**Aktér:** Klient  

**Hlavný scenár:**
1. Klient zadá názov, popis a rozpočet  
2. Systém vytvorí projekt v stave OPEN  
3. Projekt je dostupný freelancerom v marketplace  

---

### UC04 – Odoslanie ponuky
**Aktér:** Freelancer  

**Hlavný scenár:**
1. Freelancer si vyberie projekt v stave OPEN  
2. Zadá cenu a správu  
3. Systém uloží ponuku v stave PENDING  

---

### UC05 – Prijatie ponuky
**Aktér:** Klient  

**Hlavný scenár:**
1. Klient si zobrazí ponuky k svojmu projektu  
2. Vyberie jednu ponuku a prijme ju  
3. Systém nastaví projekt ako „IN_PROGRESS“ a priradí freelancera  
4. Prijatá ponuka sa nastaví na ACCEPTED, ostatné na REJECTED  

---

### UC06 – Hodnotenie freelancera
**Aktér:** Klient  

**Hlavný scenár:**
1. Projekt je v stave IN_PROGRESS alebo COMPLETED (zvyčajne po dokončení)  
2. Klient priradí číselné hodnotenie a komentár  
3. Systém uloží hodnotenie a aktualizuje celkový rating freelancera  

---

### UC07 – Vyhľadávanie freelancerov
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ zadá filter (zručnosť)  
2. Systém vyhľadá freelancerov s danou zručnosťou  
3. Zobrazí výsledky vrátane ich priemerného hodnotenia  

---

### UC08 – Správa profilu
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ si načíta svoj profil  
2. Používateľ upraví meno, bio alebo zoznam zručností  
3. Systém uloží zmeny v lokálnom profile  

---

### UC09 – Zobrazenie mojich projektov
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ si vyžiada zoznam svojich projektov  
2. Systém vráti projekty, kde je používateľ klientom alebo priradeným freelancerom  

---

## 1. Požiadavky

- Java 25
- Docker Desktop (bežiaci)
- Maven Wrapper (`mvnw.cmd` je súčasť repo)

## 1.1 Architektúra modulov

Projekt je rozdelený na samostatné Maven moduly podľa hexagonálnej architektúry:

- `application/domain` - doména, porty a business služby bez Spring/JPA závislostí
- `application/api-spec` - OpenAPI kontrakt a generované REST rozhrania a DTO (používa `openapi-generator-maven-plugin`)
- `application/inbound-controller-rest` - inbound REST adaptér, mapping a security (implementuje generované rozhrania z `api-spec`)
- `application/outbound-repository-jpa` - outbound JPA adaptér a perzistenčné mapovanie (implementuje doménové porty)
- `application/springboot` - bootstrap modul, bean wiring, runtime konfigurácia a integračné testy

## 2. Prvé spustenie infra (DB + Keycloak)

V koreňovom priečinku projektu:

```powershell
docker compose up -d
```

Overenie:

```powershell
docker compose ps
```

Služby:

- PostgreSQL: `localhost:5433/skill_market` (`skill_market` / `skill_market`)
- Keycloak: `http://localhost:8081`
- Keycloak admin: `admin` / `admin`

## 3. Spustenie aplikácie

Najprv je potrebné zostaviť a nainštalovať všetky moduly do lokálneho Maven repozitára:

```powershell
.\mvnw.cmd install -DskipTests
```

Lokálny workshop režim:

```powershell
.\mvnw.cmd -f application/springboot/pom.xml spring-boot:run
```

Keycloak profil:

```powershell
.\mvnw.cmd -f application/springboot/pom.xml spring-boot:run -Dspring-boot.run.profiles=keycloak
```

Aplikácia beží na:

- `http://localhost:8080`

## 4. OpenAPI

- JSON: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

OpenAPI kontrakt je v súbore:

- `application/api-spec/src/main/resources/openapi/skill-market-api.yaml`

## 5. Dôležité API endpointy

Aplikácia poskytuje nasledujúce REST rozhrania:

### Používatelia a registrácia
- `POST /api/v1/registrations` - Registrácia nového používateľa (vytvorí účet v Keycloak a lokálny profil)

### Projekty
- `GET /api/v1/projects` - Marketplace: Zoznam projektov dostupných pre freelancerov
- `GET /api/v1/projects/my` - Moje projekty: Zoznam projektov, ktorých je používateľ súčasťou
- `POST /api/v1/projects` - Vytvorenie nového projektu
- `GET /api/v1/projects/{projectId}/detail` - Detail konkrétneho projektu
- `PUT /api/v1/projects/{projectId}` - Úprava projektu (iba v stave OPEN)
- `DELETE /api/v1/projects/{projectId}` - Zrušenie projektu (iba v stave OPEN)
- `GET /api/v1/freelancers/{freelancerId}/projects/assigned` - Projekty priradené konkrétnemu freelancerovi

### Ponuky (Offers)
- `GET /api/v1/projects/{projectId}/offers` - Zobrazenie ponúk k projektu
- `POST /api/v1/projects/{projectId}/offers` - Odoslanie ponuky na projekt
- `POST /api/v1/projects/{projectId}/offers/{offerId}/accept` - Prijatie ponuky
- `POST /api/v1/projects/{projectId}/offers/{offerId}/reject` - Odmietnutie ponuky
- `DELETE /api/v1/projects/{projectId}/offers/{offerId}` - Stiahnutie ponuky

### Freelanceri a Profily
- `GET /api/v1/freelancers?skill=java` - Vyhľadávanie freelancerov podľa zručnosti
- `GET /api/v1/profiles/{userId}` - Získanie profilu používateľa
- `PUT /api/v1/profiles/{userId}` - Aktualizácia profilu

### Hodnotenia
- `POST /api/v1/projects/{projectId}/ratings` - Ohodnotenie freelancera po skončení projektu

## 6. Otestovanie zabezpečeného endpointu

V lokálnom workshop režime sú endpointy dostupné bez tokenu.
V `keycloak` profile bez tokenu vráti API `401`.

### Získanie JWT tokenu (PowerShell)

Príklad pre používateľa `freelancer`:

```powershell
$token = (Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8081/realms/skill-market/protocol/openid-connect/token" `
  -ContentType "application/x-www-form-urlencoded" `
  -Body "client_id=skill-market-client&grant_type=password&username=freelancer&password=freelancer123").access_token
```

### Volanie endpointu s tokenom

```powershell
curl -H "Authorization: Bearer $token" http://localhost:8080/api/v1/projects
```

## 7. Testovacie údaje

### Test používatelia:
- `freelancer` / `freelancer123` (rola `FREELANCER`)
- `client` / `client123` (rola `CLIENT`)
- `admin-user` / `admin123` (rola `ADMIN`)

Všetci používatelia sú predkonfigurovaní v Keycloak realm `skill-market`.

## 8. Vypnutie infra

```powershell
docker compose down
```

Ak chceš zmazať aj DB dáta:

```powershell
docker compose down -v
```
