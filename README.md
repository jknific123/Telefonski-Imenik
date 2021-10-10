# Telefonski-Imenik
Java implementation of phonebook.

Pri implementaciji aplikacije sem uporabljal podatkovno bazo postgreSQL, ki sem jo vzpostavil v okolju Docker.

V kolikor aplikacije Docker še nimate nameščene lahko to storite na tej povezavi https://docs.docker.com/desktop/windows/install/.

Nato v terminalu izvedete ukaz: docker run -d --name postgres-jdbc -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=kontakti -p 5432:5432 postgres:13

S tem se ustvari podatkovna baza kontakti, katero aplikacija Telefonski imenik uporablja. Uporabniško ime in geslo baze sta enaka: postgres.

V kolikor ne želite uporabljati podatkovne baze, aplikacija Telefonski imenik omogoča shranjevanje in branje kontaktov na disk.
