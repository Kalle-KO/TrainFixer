Gruppemedlemmer: Kalle 

Tests er skrevet i JUnit 5.  
Du kan køre dem ved at:

- Åbne projektet i IntelliJ og højreklikke på `test`-mappen eller en testklasse og vælge **Run Tests**.

Delopgave 4:
Fix-funktionen kører i O(n) tid og bruger O(n) plads.
2 gennemløb af toget: ét til at gruppere vogne, ét til at bygge ny rækkefølge.
Dette tælles med train.setFullPasses(2).
Fordel ved 2 pass: enkel og korrekt løsning.
Ulempe: lidt ekstra gennemløb ift. en mere kompliceret 1-pass løsning.
Konklusion: O(n), 2 gennemløb, valgt for at gøre koden enkel og korrekt.
