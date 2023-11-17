# Utfordringer ved prosjektet 
## Implementering av regninger for applikasjonen
I henhold til brukerhistorien skulle det være mulig for brukeren å se kommende regninger i applikasjonen og forhåndsvise fremtidig saldo etter betaling. Ved release 2 fantes det en <em>Bill</em>-klasse og gruppen hadde satt opp persistens for klassen. Problemet oppstod derimot da REST-API-et ble koblet opp. Gruppen lagde ikke egen serialisering og deserialisering for klassene, da det virket som at alt fungerte. Senere oppdaget gruppen derimot en feil med serialisering av regninger dersom en profil refererte til en annen profil i JSON. Springboot krasjet som følger dersom feilen ble trigget. Gruppen prøvde å løse problemet, også med hjelp fra læringsassistenter, men uten hell. 

Siden fristen nærmet seg fjernet gruppen implementeringen av regninger helt. Dette virker som enkleste utvei, men valget ble tatt for å forhindre krasj både under bruk og testing. En konsekvens av problemet var at testing av applikasjon risikerte å feile, siden springboot krasjet. Testene var nemlig avhengig av persistens. Gruppen ønsket ikke at testdekningsgraden skulle minkes på grunn av feilen og vi ønsket heller ikke å levere et produkt som ikke fungerte ordentlig. Derfor ble implementeringen fjernet.

Det gruppen har lært av denne hendelsen er å forsikre seg om at alle deler av applikasjonen fungerer ved hver endring. Feilen ble oppdaget ganske sent, så det er usikkert når det egentlig gikk alt. Å sjekke alle *edge-cases* er noe gruppen burde vært flinkere på. Siden testing av applikasjonen også var under utvikling, ble ikke feilen oppdaget gjennom testingen, men under bruk. Gruppen burde derfor manuelt testet ulike cases underveis.



