# Simulator de zaruri 4.0 — noutăți față de versiunea din 2025

Document de lucru pentru selectarea noutăților care vor fi publicate în Google Play.

## Reperul folosit pentru comparație

- Baza comparației este starea proiectului din 17 septembrie 2025, identificată în Git prin commitul `6d52cd3`.
- La acel moment proiectul avea `versionName 3.1` și `versionCode 13`.
- Versiunea pregătită acum este **4.0**, cu `versionCode 15`.
- Codul versiunii a rămas 15 deoarece această versiune nu a fost încă publicată în Google Play.
- Data afișată în secțiunea „Despre” este 7 octombrie 2026.

## Rezumatul principalelor noutăți

Versiunea 4.0 aduce o interfață refăcută, zaruri vectoriale mai mari, mod întunecat, o pictogramă adaptivă, animații, protecție împotriva aruncărilor repetate accidental, anunțuri vocale configurabile, redarea rezultatului prin vibrații, limba italiană, îmbunătățiri ample pentru cititoarele de ecran, opțiuni pentru menținerea ecranului activ și pentru aruncarea prin scuturare când ecranul este blocat.

## 1. Interfață refăcută și modernizată

- Ecranul principal a fost reorganizat și simplificat.
- Au fost introduse carduri vizuale pentru selectorul numărului de zaruri și pentru rezultat.
- Culorile, spațierile, dimensiunile textelor și butoanele au fost uniformizate.
- Interfața respectă zonele ocupate de barele sistemului și decupajele ecranului.
- A fost adăugat un aspect separat și optimizat pentru orientarea peisaj.
- Toate ecranele de setări au fost refăcute în același stil vizual.
- Controalele importante au zone tactile de cel puțin 48–56 dp.
- Numărul zarurilor se schimbă direct din ecranul principal, cu butoanele minus și plus.
- Vechiul ecran separat pentru alegerea numărului de zaruri a fost eliminat.
- Meniul principal a fost reorganizat:
  - comutatorul vocal apare primul în bara de sus;
  - „Setări” apare imediat după el;
  - „Șterge istoricul”, „Despre” și „Ieșire” sunt în meniul „Mai multe”.
- Secțiunea „Despre” afișează versiunea 4.0 și data actualizării.

## 2. Zaruri grafice noi și redimensionare adaptivă

- Imaginile fixe vechi au fost înlocuite cu zaruri desenate vectorial direct de aplicație.
- Zarurile sunt clare la orice densitate și rezoluție a ecranului.
- Dimensiunea lor se adaptează automat la numărul selectat:
  - un zar este afișat foarte mare;
  - două zaruri sunt afișate până la 140 dp fiecare;
  - între trei și șase zaruri sunt redimensionate ca să încapă în grilă;
  - șase zaruri sunt afișate într-o grilă 3 × 2.
- În orientarea peisaj se aplică limite suplimentare de dimensiune pentru ca zarurile, rezultatul și comenzile să rămână vizibile simultan.
- Rezultatul rămâne afișat și ca text.
- Totalul rămâne afișat vizual sub valorile zarurilor.
- A fost eliminat „procentajul de noroc”, împreună cu vechiul calcul asociat, deoarece valoarea nu avea o semnificație suficient de clară.

## 3. Animații și feedback la aruncare

- Zarurile au o animație scurtă la apariția unui rezultat nou.
- Animația combină mărirea ușoară cu apariția progresivă.
- A fost adăugat un feedback haptic scurt la aruncare.
- Vibrația scurtă poate fi activată sau dezactivată din „Alte setări”.
- Sunetul clasic al zarurilor poate fi activat sau dezactivat independent.

## 4. Protecție împotriva aruncărilor repetate accidental

- După fiecare aruncare există un interval minim de cinci secunde în care nu poate fi generat un alt rezultat.
- Protecția se aplică atât butonului, cât și scuturării telefonului.
- În timpul pauzei, butonul „Aruncă acum!” rămâne dezactivat.
- Un cronometru discret apare sub buton și arată secundele rămase.
- Cronometrul nu primește focalizarea de accesibilitate și nu este citit repetat de TalkBack.
- Textul butonului rămâne constant; nu mai conține numărătoarea inversă.
- Dacă este activată redarea haptică a rezultatului și modelul de vibrații durează mai mult de cinci secunde, o nouă aruncare rămâne blocată până la terminarea vibrațiilor.
- O nouă aruncare este blocată și cât timp aplicația redă vocal rezultatul.

## 5. Anunțuri vocale configurabile

- Anunțarea vocală a rezultatului este activată implicit.
- Utilizatorul poate alege între:
  - **voci înregistrate** incluse în aplicație;
  - **vocea telefonului**, prin Android Text to Speech.
- Vocile înregistrate rămân alegerea implicită.
- În bara de sus există o pictogramă difuzor pentru activarea sau dezactivarea rapidă a anunțurilor vocale.
- Pictograma și descrierea accesibilă se schimbă în funcție de starea activă sau mută.
- Opțiunea poate fi dezactivată complet, caz în care rezultatul rămâne numai vizual.
- Anunțul a fost simplificat intenționat: sunt rostite numai valorile zarurilor, de exemplu „6, 3.”
- Nu se mai rostesc expresii precum „Rezultatul aruncării” și nu se mai rostește totalul.
- Android TTS folosește limba aleasă pentru vocea zarurilor.
- Inițializarea și închiderea motorului TTS sunt gestionate astfel încât aplicația să nu rămână blocată după schimbarea ecranului sau dezactivarea vocii.
- Dacă limba cerută nu este disponibilă în motorul TTS, aplicația folosește o alternativă accesibilă.

### Comportamentul împreună cu un cititor de ecran

| Alegerea utilizatorului | Cititor de ecran activ | Cititor de ecran inactiv |
|---|---|---|
| Voci înregistrate | Se redau vocile înregistrate | Se redau vocile înregistrate |
| Vocea telefonului | Rezultatul este anunțat de TalkBack sau cititorul activ | Rezultatul este redat prin Android TTS |
| Anunțare dezactivată | Nu există anunț automat al rezultatului | Aplicația rămâne mută |

Cititorul de ecran continuă să citească în mod normal comenzile și informațiile atunci când utilizatorul navighează prin interfață.

## 6. Redarea rezultatului prin vibrații

- A fost introdusă opțiunea „Redă rezultatul prin vibrații”.
- Opțiunea este separată de vibrația scurtă obișnuită de la aruncare.
- Este dezactivată implicit și poate fi activată din „Alte setări”.
- Fiecare zar este redat ca un grup haptic distinct:
  - o vibrație mai lungă marchează începutul grupului;
  - urmează una până la șase vibrații scurte, corespunzătoare valorii zarului;
  - între zaruri există o pauză clară.
- Modelul final folosește:
  - marcaj de 300 ms;
  - pauză de 160 ms după marcaj;
  - impulsuri de 45 ms pentru valoare;
  - pauze de 140 ms între impulsurile scurte;
  - pauză de 300 ms între grupurile zarurilor.
- A fost corectată o problemă care putea face ca două zaruri să pară trei grupuri haptice.
- Vibrațiile au fost distanțate mai bine pentru a putea fi numărate mai ușor.
- Modelul este trimis ca feedback de accesibilitate, pentru o integrare mai bună cu serviciile sistemului.

## 7. Îmbunătățiri pentru TalkBack și alte cititoare de ecran

- Rezultatul este expus ca un singur element accesibil, în forma simplă „6, 3.”.
- Zarurile grafice individuale sunt ascunse din arborele de accesibilitate pentru a evita repetarea informației.
- Selectorul numărului de zaruri are etichete explicite:
  - „Mai puține zaruri”;
  - „Mai multe zaruri”;
  - „2 zaruri selectate” etc.
- După apăsarea pe plus sau minus, TalkBack anunță automat numai noul număr de zaruri, fără ca utilizatorul să fie nevoit să caute din nou valoarea pe ecran.
- Comutatorul vocal din bara de sus indică prin etichetă acțiunea disponibilă.
- Numărătoarea inversă nu întrerupe utilizatorul și nu este anunțată la fiecare secundă.
- Modul cu voci înregistrate funcționează și atunci când TalkBack este activ.
- Modul „Vocea telefonului” folosește automat cititorul de ecran când acesta este activ, evitând vocile suprapuse.
- Rândurile istoricului sunt elemente accesibile individuale; de exemplu, TalkBack citește „Aruncarea 1: 4, 2”.
- Ecranele de setări folosesc controale standard Android, ușor de explorat și activat prin cititorul de ecran.
- Butoanele, textele și stările dezactivate au fost verificate în ierarhia reală de accesibilitate de pe telefon.

## 8. Limba italiană și alegerea limbii vocilor

- A fost adăugată localizarea completă în limba italiană.
- Aplicația declară oficial trei limbi acceptate:
  - engleză;
  - italiană;
  - română.
- Limba vocilor zarurilor poate fi aleasă separat din „Setări de limbă”.
- Alegerea limbii este folosită atât pentru vocile înregistrate, cât și pentru Android TTS.
- Interfața urmează limba telefonului sau limba aplicației stabilită prin Android.
- Dacă telefonul folosește o altă limbă decât cele trei disponibile, aplicația revine la engleză.
- Alegerea manuală a limbii vocilor este salvată și păstrată indiferent de limba telefonului.

## 9. Ecranul activ, scuturarea și ecranul blocat

- A fost adăugată opțiunea „Menținere ecran activ”.
- Opțiunea este activată implicit și împiedică stingerea ecranului cât timp aplicația este folosită.
- Poate fi dezactivată pentru economisirea bateriei.
- Aruncarea prin scuturarea telefonului rămâne disponibilă și poate fi activată sau dezactivată.
- A fost adăugată opțiunea „Aruncă și cu ecranul blocat”.
- Această opțiune este dezactivată implicit și avertizează că poate consuma mai multă baterie.
- Când este activată, aplicația poate continua să urmărească accelerometrul după blocarea ecranului.
- Este folosit un wake lock limitat la situația necesară, iar senzorul și wake lock-ul sunt eliberate când nu mai sunt necesare.
- Protecția de cinci secunde se aplică și aruncărilor pornite prin scuturare.

## 10. Mod întunecat și pictogramă nouă

- A fost adăugat un mod întunecat complet, cu paletă separată pentru fundal, carduri, texte și controale.
- Tema urmează configurarea sistemului.
- Contrastul elementelor a fost verificat vizual pe dispozitive reale.
- A fost creată o pictogramă adaptivă pentru versiunile moderne de Android.
- Sunt furnizate variante pentru pictograma normală și cea rotundă.
- Pictograma rămâne compatibilă și cu dispozitivele care nu folosesc pictograme adaptive.

## 11. Istoricul aruncărilor

- Sunt păstrate ultimele șapte aruncări.
- Cea mai recentă aruncare apare prima.
- A fost adăugat subtitlul „Cea mai recentă aruncare este prima”, tradus în toate cele trei limbi.
- Subtitlul apare numai dacă istoricul conține cel puțin o aruncare.
- Fiecare rând este afișat într-un card și conține numărul poziției și valorile zarurilor.
- Istoricul este salvat local și este restaurat la următoarea pornire a aplicației.
- Ștergerea istoricului este disponibilă în meniul „Mai multe”.

## 12. Alte simplificări funcționale

- Sortarea zarurilor rămâne configurabilă:
  - fără sortare;
  - ordine crescătoare;
  - ordine descrescătoare.
- Setările implicite au fost actualizate pentru noile opțiuni.
- Ecranele și codul vechi pentru statistici au fost eliminate.
- Vechiul calcul al procentajului de noroc a fost eliminat complet.
- Gestionarea sunetelor a fost simplificată și resursele audio sunt eliberate corect.
- Setările și istoricul sunt păstrate local prin preferințele aplicației.

## 13. Modernizare tehnică

- `compileSdk` și `targetSdk` au fost actualizate la API 36.
- Proiectul folosește Java 17.
- Versiunea minimă acceptată este Android API 24 (Android 7.0).
- Namespace-ul aplicației este declarat în configurația Gradle modernă.
- Au fost actualizate Android Gradle Plugin și Gradle Wrapper.
- A fost eliminată permisiunea Internet, deoarece aplicația nu are nevoie de acces la rețea.
- A fost eliminată acceptarea traficului HTTP necriptat.
- Au fost adăugate numai permisiunile necesare pentru noile funcții:
  - vibrații;
  - wake lock.
- Configurația limbilor acceptate este declarată prin `localeConfig`.
- Au fost simplificate activitățile și resursele vechi care nu mai erau necesare.
- Codul pentru redarea audio, ciclul de viață al senzorilor, TTS și actualizarea interfeței a fost refăcut pentru versiunile Android actuale.

## 14. Verificări efectuate

- Construire APK debug reușită.
- Construire release și Android App Bundle verificate în timpul modernizării.
- Android Lint rulat fără erori care să blocheze build-ul.
- Sarcina de teste unitare rulează cu succes; proiectul nu conține în prezent teste unitare propriu-zise, astfel încât Gradle raportează `NO-SOURCE` pentru acestea.
- Verificări repetate cu `git diff --check`.
- Instalări succesive peste versiunea existentă, prin cablu USB.
- Verificări vizuale în portret și peisaj.
- Verificări cu unul, două și șase zaruri.
- Verificări pentru:
  - animație;
  - pauza de cinci secunde;
  - contor;
  - sunet;
  - voci înregistrate;
  - Android TTS;
  - TalkBack;
  - feedback haptic;
  - istoric;
  - meniuri și setări;
  - mod întunecat;
  - rotația ecranului.
- Versiunea 4.0 a fost instalată și verificată pe dispozitivul Android folosit pe parcursul dezvoltării.
- A fost instalată și verificată separat pe **Google Pixel 10 cu Android 17 / API 37**:
  - prima pornire;
  - TalkBack activ;
  - aruncare cu vocile înregistrate;
  - blocarea temporară a butonului;
  - istoric;
  - setări audio;
  - șase zaruri în peisaj;
  - lipsa suprapunerilor, crashurilor și ANR-urilor.
- Pe Pixel 10 au fost confirmate `versionName 4.0`, `versionCode 15` și `targetSdk 36`; înainte de finalizare, cerința minimă a fost actualizată la `minSdk 24` (Android 7.0).
- Senzația fizică a vibrațiilor și scuturarea telefonului cu ecranul blocat necesită confirmare manuală; acestea nu pot fi evaluate complet prin capturi și comenzi ADB.

## 15. Setările implicite în versiunea 4.0

- Două zaruri.
- Sortare descrescătoare.
- Sunetul aruncării activat.
- Anunțarea vocală activată.
- Voci înregistrate selectate.
- Aruncare prin scuturare activată.
- Aruncare cu ecranul blocat dezactivată.
- Menținerea ecranului activată.
- Vibrația scurtă la aruncare activată.
- Redarea completă a rezultatului prin vibrații dezactivată.
- Pentru o limbă de sistem neacceptată se folosește engleza.

## 16. Variante orientative pentru Google Play

Aceste texte sunt propuneri de lucru. Pot fi scurtate sau combinate înainte de publicare.

### Variantă scurtă

> Interfață complet modernizată, zaruri mai mari și adaptive, mod întunecat, animații, limba italiană și accesibilitate îmbunătățită. Au fost adăugate Android TTS, redarea rezultatului prin vibrații, protecția împotriva aruncărilor repetate, menținerea ecranului activ și aruncarea opțională cu ecranul blocat.

### Variantă foarte scurtă

> Interfață nouă, zaruri adaptive, mod întunecat, italiană, voce TTS, feedback haptic și compatibilitate îmbunătățită cu TalkBack și Android 17.

### Listă pentru triere

- Interfață complet refăcută.
- Zaruri vectoriale mai mari și adaptive.
- Aspect optimizat pentru portret și peisaj.
- Mod întunecat.
- Pictogramă adaptivă nouă.
- Animație la aruncare.
- Selectarea rapidă a unu până la șase zaruri.
- Pauză de cinci secunde între aruncări.
- Cronometru discret pentru următoarea aruncare.
- Voci înregistrate sau vocea telefonului.
- Comutator vocal rapid în bara de sus.
- Anunțuri simplificate la valorile zarurilor.
- Integrare corectă cu TalkBack.
- Redarea rezultatului prin vibrații.
- Vibrație scurtă opțională la aruncare.
- Limba italiană.
- Alegerea separată a limbii vocilor.
- Menținerea ecranului activ.
- Aruncare opțională prin scuturare cu ecranul blocat.
- Istoric clarificat și persistent.
- Eliminarea procentajului de noroc.
- Modernizare pentru versiunile Android actuale.
- Verificat pe Pixel 10 cu Android 17.

## 17. Istoricul commiturilor principale pentru versiunea 4.0

În ordinea în care au fost realizate:

1. `86a9267` — Various improvements for a new version.
2. `c80a234` — Prepare version 3.2 release.
3. `f2a36ea` — Add animated rolls and optional vibration.
4. `8de15b6` — Add dark theme and adaptive launcher icon.
5. `21d3d09` — Improve screen reader announcements.
6. `ae9145a` — Add Italian localization.
7. `19db054` — Prevent accidental repeated rolls.
8. `0666a7d` — Set version 4.0.
9. `fd8ce84` — Add haptic dice result playback.
10. `d52bd48` — Fix haptic dice grouping.
11. `7126fd0` — Move roll countdown outside button.
12. `a1fd268` — Add selectable voice announcements.
13. `77a7928` — Use selected voice with screen readers.
14. `e4aa9a7` — Prioritize settings in the app bar.
15. `8bc26e7` — Enlarge dice and remove luck score.
16. `56bf41e` — Clarify history order.
17. `3916c4f` — Finished version 4.0 (anunțarea automată a noului număr de zaruri pentru TalkBack).
