# Hur man kör projektet
## Servern
för att köra servern från terminalen så kör du detta kommando från projektets root
### Mac/linux
``./gradlew run``
### Windows
``.\gradlew.bat run``
## Client
För att köra clienten så kör du något av dessa kommandon från projektets root
### Mac/linux
``./gradlew client:run``
### Windows
``.\gradlew.bat client:run``

# Bygga jar filer av projektet
vill du bygga jar filer av både servern och clienten så kan du köra detta kommando.
## Mac/Linux
``./gradlew jar``
## Windows
``.\gradlew.bar jar``

Du kan då hittar jar filen för servern i ``build/libs`` och för clienten i ``client/build/libs``
