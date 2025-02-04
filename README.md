# This repository will be used for the PCBE Project.

## USAGE

### First setup:
- clone repository
- install **Intelij IDEA** to run the java application (backend)
- install **NodeJS** for npm (frontend)
- run `npm install` inside frontend folder at ./frontend/ to get dependencies

### After first setup:
- run `npm start` inside frontend folder at ./frontend/ to start frontend
- start `Application.java` inside IDEA.

### Where to find the pages:
- user API is found at: `localhost:3000`
- admin API is found at: `localhost:3000/settings`
- swagger UI is found at: `localhost:8080/swagger-ui/index.html`

## ABOUT

### Jocul vietii
Simulati o populatie de celule vii ce au fiecare ca scop hranirea si inmultirea.

Exista un numar limitat de unitati de hrana (resurse) pe care celulele le consuma. O unitate de hrana ii ajunge unei celule un timp dat T_full dupa care i se face foame. Daca nu mananca un alt timp dat T_starve, celula moare rezultind un numar aleator intre 1 si 5 de unitati de hrana.

Dupa ce a mincat de minim 10 ori, o celula se va inmulti inainte sa i se faca din nou foame. Exista doua tipuri de celule: sexuate si asexuate. Celulele asexuate se inmultesc prin diviziune, rezultind doua celule flaminde.

Cele sexuate se inmultesc doar daca gasesc o alta celula ce cauta sa se reproduca iar atunci rezulta o a treia celula, initial infometata.

In simulare, celulele vor fi reprezentate de fire de executie distincte.

### Probleme de concurenta
- 2 celule vor sa manance aceeasi resursa de hrana
- 1 celula mananca o resursa care nu mai exista - sincronizarea starii si a pozitiilor
- numarul de celule sa fie corect incrementat la diviziune sau reproducere
- incrementarea/decrementarea unitatilor de hrana comune
- respectarea limitei de N fire de executie

### Team Members
1. Vinitchi Robert-Valentin
2. Ungur Viktor
3. Copilu Tudor
4. Dobra Denisa-Loredana
5. Cristea Alexandra
