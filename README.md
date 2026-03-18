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
- RQ10 Systém umožní filtrovať projekty podľa kategórie, rozpočtu alebo stavu.
- RQ11 Systém umožní používateľovi upravovať svoj profil.
- RQ12 Systém umožní notifikovať používateľov o nových ponukách alebo zmenách projektu.
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

## Use Cases

### UC01 – Registrácia používateľa
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ zadá email a heslo  
2. Systém overí validitu údajov  
3. Systém vytvorí nový účet  
4. Používateľ je uložený v systéme  

---

### UC02 – Prihlásenie používateľa
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ zadá prihlasovacie údaje  
2. Systém overí údaje  
3. Systém vygeneruje JWT token  
4. Používateľ je prihlásený  

---

### UC03 – Vytvorenie projektu
**Aktér:** Klient  

**Hlavný scenár:**
1. Klient zadá názov, popis a rozpočet  
2. Systém vytvorí projekt  
3. Projekt je dostupný freelancerom  

---

### UC04 – Odoslanie ponuky
**Aktér:** Freelancer  

**Hlavný scenár:**
1. Freelancer si vyberie projekt  
2. Zadá cenu a správu  
3. Systém uloží ponuku  
4. Klient je notifikovaný  

---

### UC05 – Prijatie ponuky
**Aktér:** Klient  

**Hlavný scenár:**
1. Klient si zobrazí ponuky  
2. Vyberie jednu ponuku  
3. Systém nastaví projekt ako „IN_PROGRESS“  
4. Ostatné ponuky sú zamietnuté  

---

### UC06 – Hodnotenie freelancera
**Aktér:** Klient  

**Hlavný scenár:**
1. Projekt je dokončený  
2. Klient priradí hodnotenie  
3. Systém aktualizuje rating freelancera  

---

### UC07 – Vyhľadávanie freelancerov
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ zadá filter (skill)  
2. Systém vyhľadá freelancerov  
3. Zobrazí výsledky  

---

### UC08 – Správa profilu
**Aktér:** Používateľ  

**Hlavný scenár:**
1. Používateľ upraví profil  
2. Systém uloží zmeny

## Class diagram

<img width="1271" height="796" alt="class-scheme" src="https://github.com/user-attachments/assets/fa8750eb-1156-4880-99ee-caffffe0ccf7" />

