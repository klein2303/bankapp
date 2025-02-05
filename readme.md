# Group gr2315 repository: Bankapp-prosjekt

Dette prosjektet er et utviklingsprosjekt utviklet i forbindelse med emnet IT1901 ved NTNU. Emnet gir kunnskap og ferdigheter i smidig applikasjonsutvikling i team. Applikasjonen er laget ved å bruke en klientserverarkitektur, strukturert i moduler og konfigurert med et byggesystem. Prosjektet ble utviklet av meg og 3 andre gruppemedlemmer. 

<em>Dette prosjektet krever Java version **16**, maven versjon **4.0.0**, maven compiler.source **16** og javafx version **17.0.2**. 

- (kjent problem med nyeste Mac programvare): Dersom du har en mac med MacOs Sonoma 14.0 kan det hende at applikasjonen kjører i bakgrunnen. Derfor er det viktig å gå ut av din IDE når man tester, slik at testene fungerer. 
</em>

## Om applikasjonen:

Applikasjonen er en bankapplikasjon, basert løst på bankapplikasjoner for mobil. All form for fxui, fxml komponenter og kontrollere ligger inne i bankapp-fxui modulen. All logikk som blir brukt av appen ligger i bankapp-core modulen. Lagring og henting av informasjon skjer gjennom bankapp-springboot modulen. 

Som bruker av applikasjonen har man muligheten til å opprette en bruker, opprette ulike kontoer, overføre penger og se transaksjoner. 

## Bygge prosjektet:

Prosjektet bruker maven til bygging og kjøring. 

![](images/Skjermdump_Av_MvnInstall.png)

**Mac:**
- For å bygge, kjør `mvn clean install`, med `SpringBoot` kjørende i bakgrunenn, fra rot-prosjektet (Bankapp-mappen). Dette vil kjøre alle tester og kvalitetssjekker.
  
**Windows:**
- Kjør `mvn clean install -DskipTests` uten `SpringBoot` kjørende i bakgrunnen, fra rot-prosjektet (Bankapp-mappen). Dette vil kjøre alle tester og kvalitetssjekker.

## Kjøring:

Prosjektet kjøres fra **fxui**-modulen, ved å først kjøre `SpringBoot` og deretter kjøre applikasjonen ved å enten kjøre `mvn javafx:run -f fxui/pom.xml` inni bankapp-mappen, eller ved å først gå inn i fxui-mappen ved å kjøre `cd fxui` og deretter kjøre `mvn javafx:run`. 

