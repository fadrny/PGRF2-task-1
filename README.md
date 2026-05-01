# PGRF2 - 3D Renderer (Úloha 1)

**Autor:** Marek Fadrný, kombinovaná forma

---

## Hodnotící tabulka

| Požadavky | Splněno (0-1) | Řešení ovládání | Případné komentáře k řešení |
| :--- | :---: | :---: | :--- |
| **Zobrazení těles** - koule definovaná středem a poloměrem | **1** | | |
| **Zobrazení těles** - krychle, čtyřstěn | **1** | | Krychle |
| **Zobrazení těles** - válec, kužel, netriviální víceboký hranol... | **1** | | Válec |
| **Reprezentace** - topologie, geometrie, rozšířený vertex | **1** | | |
| **Reprezentace** - možnost ukládání hran i ploch | **1** | | |
| **Transformace** - translace | **1** | Z/U, H/J, V/B | |
| **Transformace** - rotace | **1** | I/O, K/L, N/M | |
| **Transformace** - zoom (scale) | **1** | +/- | |
| **Transformace** - výběr aktivního tělesa | **1** | TAB | |
| **Kamera** - rozhlížení myší, azimut a zenit | **1** | Tažení myší | |
| **Kamera** - pohyb vpřed, vzad, vlevo, vpravo, klávesy WSAD | **1** | WSAD + Space/C | WSAD + c pro pohyb dolů a mezerník pro pohyb nahoru |
| **Projekce (klávesa P)** - pravoúhlá | **1** | P | |
| **Projekce (klávesa P)** - perspektivní | **1** | P | |
| **Rasterizace** - hran | **1** | | |
| **Rasterizace** - ploch | **1** | | |
| **Řešení viditelnosti** - hran pomocí algoritmu ZBuffer | **1** | | |
| **Řešení viditelnosti** - ploch pomocí algoritmu ZBuffer | **1** | | |
| **Ořezání bez mizení** - rychlé ořezání zobrazovacím objemem | **1** | | |
| **Ořezání bez mizení** - ořezání z – rozklad úseček/trojúhelníku | **1** | | |
| **Ořezání bez mizení** - ořezání xy – při rasterizaci | **1** | | |
| **Zobrazení drátového modelu nebo vyplněných ploch** | **1** | Q | M již využito pro otáčení, zvoleno Q |
| **Zobrazení povrchu** - jednobarevné plochy, interpolace barvy | **1** | | Interpolace barev na krychli, nutno vypnout texturu |
| **Zobrazení povrchu** - mapování textury | **1** | | |
| **Zobrazení povrchu** - zapnutí a vypnutí textury na aktivním tělese | **1** | T | |
| **Zobrazení os RGB, šipka (1 hrana a 1 trojúhelník)** | **1** | | Šipky jako čtyřboké jehlany |
| **Osvětlení povrchu** - ambientní složka | **1** | F | |
| **Osvětlení povrchu** - difúzní složka | **1** | | |
| **Osvětlení povrchu** - znázornění polohy zdroje světla (koule) | **1** | | |
| **Osvětlení povrchu** - barva koule odpovídá barvě difúzní složky | **1** | | |
| **Osvětlení povrchu** - zdroj světla lze zvolit jako aktivní těleso... | **1** | | |
| **Odevzdání výsledné aplikace v požadovaném formátu** | **0.5** | | Pozdní odevzdání |
| **Verzování na GitLab** - vytvoření privátního repozitáře + ODKAZ! | **1** | | https://github.com/fadrny/PGRF2-task-1 |
| **Verzování na GitLab** - pravidelné komentované commity | **1** | | |
| **Bonusy** - vhodná animace zdroje osvětlení v čase | **0** | | |
| **Bonusy** - reprezentace jiné topologie než seznam... | **0** | | |
| **Bonusy** - funkcionální interface pro funkci shader | **0** | | |
| **Bonusy** - perspektivně korektní interpolace barvy/textury | **1** | | UV se před rastrizací dělí W, při vykreslení se dělí 1/W |
| **Bonusy** - implementace osvětlení včetně spekulární složky | **0** | | |
| **Bonusy** - těleso s využitím bikubické plochy... | **0** | | |
| **Vlastní rozšíření** - UI (infopanel, buttony a combobox) | **1** | | Infopanel vlevo nahoře |
| **Vlastní rozšíření** - Resize okna | **1** | | |

---

## Zadání projektu

Vytvořte program pro zobrazení jednoduché grafické scény složené minimálně ze tří 3D těles. Složení scény se řídí následujícími pravidly:

1.  **Povinné těleso:** Scéna musí obsahovat kouli definovanou středem a poloměrem.
2.  **Další dvě tělesa:** Zbylá dvě tělesa vyberte tak, aby každé pocházelo z jiné skupiny z následujícího seznamu:
    *   1. skupina: Krychle, čtyřstěn.
    *   2. skupina: Válec, kužel, netriviální víceboký hranol, komolý jehlan/kužel.

Projekt je samostatná práce.

*   Navrhněte reprezentaci scény pomocí vhodných datových struktur a navržených objektových tříd.
*   Implementujte uložení těles pomocí vertex a index buferu, předpokládejte tělesa složená z hran i ploch (trojúhelníků).
*   Implementujte modelovací transformace (posunutí, otočení, změna měřítka) jednotlivých těles ve scéně, řízené klávesnicí.
*   Implementujte výběr aktivního tělesa. Modelovací transformace se budou aplikovat na aktivní těleso.
*   Implementujte pohledovou transformaci řízenou pohybem pozorovatele pomocí klávesnice (WSAD) a rozhlížení pomocí myši (kamera).
*   Implementujte transformaci zobrazovacího objemu tj. projekci prostorové scény, možnost přepínání perspektivní a pravoúhlé projekce (klávesa P).
*   Implementujte rychlé ořezání scény zobrazovacím objemem. Ořezání podle z a ořezání x, y při rasterizaci.
*   Implementujte rasterizaci a algoritmus viditelnosti Z-buffer.
*   Umožněte přepínání zobrazení drátového modelu a vyplněných ploch (klávesa M). Zobrazení se aplikuje na celou scénu.
*   Obarvěte tělesa různou barvou. Barva může být konstantní nebo spočítaná interpolací barvy ve vrcholech.
*   Implementujte mapovaní textury na povrch těles. Každé těleso bude mít jinou texturu.
*   Umožněte zapnutí a vypnutí textury na aktivním tělese. Ostatní tělesa v ten moment zůstanou v původním režimu.
*   Znázorněte soustavu souřadnic scény zobrazením barevně odlišených souřadnicových os. Osy budou mít podobu šipky (1 hrana a 1 trojúhelník).
*   Implementujte osvětlení ve scéně (Phongův osvětlovací model). Do výpočtu zahrňte ambientní a difúzní složku. Spekulární složka bude počítána jako bonus.
*   Pozici zdroje světla znázorněte jako kouli o vhodné velikosti. Barva koule bude odpovídat barvě difúzní složky.
*   Zdroj světla lze zvolit jako aktivní těleso a aplikovat na něj modelovací transformaci posunutí.
*   Natočte scénu a umístěte tělesa tak, aby byla zřejmá správná funkce algoritmu viditelnosti, vhodné je například protnutí dvou těles.
*   Vytvořte si GITový repozitář a pravidelně commitujte postup. Repozitář nasdílejte cvičícímu podle instrukcí, které se dozvíte na cvičení.

Před odevzdáním si znovu přečtěte pravidla odevzdávání a hodnocení projektů uvedené v Průvodci studiem. Nedoržení pravidel znamená, že váš projekt nebude hodnocen bez možnosti opravy.

### Bonusy:
*   Vhodná animace zdroje osvětlení v čase.
*   Reprezentace jiné topologie než seznam trojúhelníků/hran.
*   Funkcionální interface pro funkci shader.
*   Perspektivně korektní interpolace barvy/textury.
*   Implementace osvětlení včerně spekulární složky.
*   Těleso s využitím bikubické plochy s funkčním osvětlením na povrchu.

---

## Prohlášení o využití AI
Pro transparentnost uvádím, že při vypracování a ladění tohoto projektu byl využit nástroj GitHub Copilot / GitHub Copilot Chat.
