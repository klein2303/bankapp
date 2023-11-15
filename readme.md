# Group gr2315 repository: Bankapp-prosjekt

<em>Dette prosjektet krever Java version **16**, maven versjon **4.0.0**, maven compiler.source **16** og javafx version **17.0.2**. 

Eclipse-che link: https://che.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2023/gr2315/gr2315?new

Merk: 
- Det er mulig at eclipse-che krever at man har en gyldig personal access token

- (kjent problem med nyeste Mac programvare): Dersom du har en mac med MacOs Sonoma 14.0 kan det hende at applikasjonen kjører i bakgrunnen. Derfor er det viktig å gå ut av VSCode når man tester, slik at testene fungerer. 
</em>

Dette prosjektet er et utviklingsprosjekt tilsvarende det man skal gjennom i IT1901. 

Prosjektet bruker maven til bygging og kjøring. For å bygge, kjør `mvn install`, med `SpringBoot` kjørende i bakgrunenn, fra rot-prosjektet (Bankapp-mappa). Dette vil kjøre alle tester og kvalitetssjekker. Dersom dette feiler, kjør `mvn clean install -DskipTests` uten `SpringBoot`. Dette skal nå fungere. 

Prosjektet skal så kjøres fra **fxui**-modulen, enten med `mvn javafx:run -f fxui/pom.xml` eller ved å først kjøre `cd fxui` og så `mvn javafx:run`. Her må `SpringBoot` kjøre i bakgrunnen. Merk at man må først bør ha kjørt `mvn install` på modulene som **fxui**-modulen er avhengig av (**bankapp**, **core**, **fxui** og **springboot**), for å være sikker på at det fungerer som tenkt (se skjermdump nederst).

Applikasjonen vi har å lagd er en bankapplikasjon, basert løst på bankapplikasjoner for mobil. All form for fxui, fxml komponenter og kontrollere ligger inne i bankapp-fxui modulen. All logikk som blir brukt av appen ligger i bankapp-core modulen. Lagring og henting av informasjon skjer gjennom bankapp-springboot modulen. 

![](images/Skjermdump_Av_MvnInstall.png)
