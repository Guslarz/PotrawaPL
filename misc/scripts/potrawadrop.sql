-- PACKAGES
DROP PACKAGE Wspolne;

DROP PACKAGE Klient;

DROP PACKAGE Dostawca;

DROP PACKAGE Restauracja;


-- ROLES
DROP ROLE rola_klient;

DROP ROLE rola_dostawca;

DROP ROLE rola_restauracja;


-- INDICES
DROP INDEX dania_nazwa_index;

DROP INDEX dania_cena_index;


-- TABLES
DROP TABLE dania_w_zamowieniu;

DROP TABLE zamowienia;

DROP TABLE alergeny_w_daniach;

DROP TABLE dania;

DROP TABLE opinie;

DROP TABLE restauracje;

DROP TABLE dostawcy;

DROP TABLE klienci;

DROP TABLE uzytkownicy;

DROP TABLE metody_platnosci;

DROP TABLE alergeny;

DROP TABLE kategorie;

COMMIT;