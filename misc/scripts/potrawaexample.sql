-- klient 00
INSERT INTO uzytkownicy (identyfikator, typ)
VALUES ('klient00', 'KLIENT');

INSERT INTO klienci (identyfikator, imie, nazwisko)
VALUES ('klient00', 'Klient', '00');


-- dostawca 00
INSERT INTO uzytkownicy (identyfikator, typ)
VALUES ('dostawca00', 'DOSTAWCA');

INSERT INTO dostawcy (identyfikator, imie, nazwisko)
VALUES ('dostawca00', 'Dostawca', '00');


-- restauracja 00
INSERT INTO uzytkownicy (identyfikator, typ)
VALUES ('restauracja00', 'RESTAURACJA');

INSERT INTO restauracje (identyfikator, nazwa)
VALUES ('restauracja00', 'Restauracja 00');


-- restauracja 01
INSERT INTO uzytkownicy (identyfikator, typ)
VALUES ('restauracja01', 'RESTAURACJA');

INSERT INTO restauracje (identyfikator, nazwa)
VALUES ('restauracja01', 'Restauracja 01');


-- restauracja 02
INSERT INTO uzytkownicy (identyfikator, typ)
VALUES ('restauracja02', 'RESTAURACJA');

INSERT INTO restauracje (identyfikator, nazwa)
VALUES ('restauracja02', 'Restauracja 02');


-- restauracja 03
INSERT INTO uzytkownicy (identyfikator, typ)
VALUES ('restauracja03', 'RESTAURACJA');

INSERT INTO restauracje (identyfikator, nazwa)
VALUES ('restauracja03', 'Restauracja 03');


INSERT INTO opinie (identyfikator_klienta, identyfikator_restauracji, ocena, komentarz)
VALUES ('INF141240', 'restauracja02', 5, 'Fajne jedzonko');
INSERT INTO opinie (identyfikator_klienta, identyfikator_restauracji, ocena, komentarz)
VALUES ('INF141240', 'restauracja01', 5, 'Fajne jedzonko');
INSERT INTO opinie (identyfikator_klienta, identyfikator_restauracji, ocena, komentarz)
VALUES ('INF141240', 'restauracja00', 5, 'Fajne jedzonko');
INSERT INTO opinie (identyfikator_klienta, identyfikator_restauracji, ocena, komentarz)
VALUES ('INF141240', 'restauracja03', 5, 'Fajne jedzonko');


INSERT INTO dania (identyfikator_restauracji, nazwa, opis, cena, nazwa_kategorii)
VALUES ('restauracja00', 'Danie 00', 'Fajne danie', 20, null);
INSERT INTO dania (identyfikator_restauracji, nazwa, opis, cena, nazwa_kategorii)
VALUES ('restauracja00', 'Danie 01', 'Fajne danie', 20, null);
INSERT INTO dania (identyfikator_restauracji, nazwa, opis, cena, nazwa_kategorii)
VALUES ('restauracja00', 'Danie 02', 'Fajne danie', 20, null);
INSERT INTO dania (identyfikator_restauracji, nazwa, opis, cena, nazwa_kategorii)
VALUES ('restauracja00', 'Danie 03', 'Fajne danie', 20, null);
INSERT INTO dania (identyfikator_restauracji, nazwa, opis, cena, nazwa_kategorii)
VALUES ('restauracja00', 'Danie 04', 'Fajne danie', 20, null);

COMMIT;