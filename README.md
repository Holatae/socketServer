# SocketRepo
## Hur man laddar ner projektet
### git
Om du har git installerat så kan du köra detta kommando
``git clone https://github.com/Holatae/socketServer.git``

### zip
Om du inte har git installerat så kan du ladda ner projektet som en zip fil under knappen ``Code`` och sedan klicka på ``Download ZIP``

## Hur man kör projektet
### Servern
För att köra servern från terminalen så kör du detta kommando från projektets root
#### Mac/linux
``./gradlew run``
#### Windows
``.\gradlew.bat run``
### Client
För att köra clienten så kör du något av dessa kommandon från projektets root
#### Mac/linux
``./gradlew client:run``
#### Windows
``.\gradlew.bat client:run``

## Bygga jar filer av projektet
Vill du bygga jar filer av både servern och clienten så kan du köra detta kommando.
### Mac/Linux
``./gradlew jar``
### Windows
``.\gradlew.bar jar``

Du kan då hittar jar filen för servern i ``build/libs`` och för clienten i ``client/build/libs``

## Vanliga fel
### Porten är upptagen
Om du får ett fel som säger att porten är upptagen så kan du ändra porten i ``Server.java`` på rad 13
### Kan inte köra gradlew på mac/linux
Om du får ett fel som säger att du inte har tillåtelse att köra gradlew så kan du köra detta kommando
``chmod +x gradlew``
### Kan inte köra gradlew.bat på windows
Om du får ett fel som säger att du inte har tillåtelse att köra gradlew.bat så kan du köra detta kommando
``Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser``
