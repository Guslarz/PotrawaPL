-- TABLES
CREATE TABLE kategorie (
    nazwa   VARCHAR2(50),
    
    CONSTRAINT kategorie_pk 
        PRIMARY KEY (nazwa)
);


CREATE TABLE alergeny (
    nazwa   VARCHAR2(50),
    
    CONSTRAINT alergeny_pk
        PRIMARY KEY (nazwa)
);


CREATE TABLE metody_platnosci (
    nazwa   VARCHAR2(50), 
    
    CONSTRAINT metody_platonosci_pk
        PRIMARY KEY (nazwa)
);


CREATE TABLE uzytkownicy (
    identyfikator   VARCHAR2(30),
    typ             VARCHAR2(15) NOT NULL,
    
    CONSTRAINT uzytkownicy_pk
        PRIMARY KEY (identyfikator),
            
    CONSTRAINT uzytkownicy_chk_typ
        CHECK (typ IN ('KLIENT', 'DOSTAWCA', 'RESTAURACJA'))
);


CREATE TABLE klienci (
    identyfikator   VARCHAR2(30),
    imie            VARCHAR2(50) NOT NULL,
    nazwisko        VARCHAR2(50) NOT NULL,
    domyslny_adres  VARCHAR2(100),
    
    CONSTRAINT klienci_pk
        PRIMARY KEY (identyfikator),
        
    CONSTRAINT klienci_fk
        FOREIGN KEY (identyfikator)
            REFERENCES uzytkownicy(identyfikator)
            ON DELETE CASCADE
);


CREATE TABLE dostawcy (
    identyfikator   VARCHAR2(30),
    imie            VARCHAR2(50) NOT NULL,
    nazwisko        VARCHAR2(50) NOT NULL,
    czy_dostepny    CHAR(1) DEFAULT 'N' NOT NULL,
    
    CONSTRAINT dostawcy_pk
        PRIMARY KEY (identyfikator),
        
    CONSTRAINT dostawcy_fk
        FOREIGN KEY (identyfikator)
            REFERENCES uzytkownicy(identyfikator)
            ON DELETE CASCADE
);


CREATE TABLE restauracje (
    identyfikator   VARCHAR2(30),
    nazwa           VARCHAR2(100) NOT NULL,
    opis            VARCHAR2(255),
    
    CONSTRAINT restauracje_pk
        PRIMARY KEY (identyfikator),
        
    CONSTRAINT restauracje_fk
        FOREIGN KEY (identyfikator)
            REFERENCES uzytkownicy(identyfikator)
            ON DELETE CASCADE,
            
    CONSTRAINT nazwa_unique
        UNIQUE (nazwa)
);


CREATE TABLE opinie (
    identyfikator_klienta       VARCHAR2(30),
    identyfikator_restauracji   VARCHAR2(30),
    ocena                       NUMBER(1) NOT NULL,
    komentarz                   VARCHAR2(255),
    
    CONSTRAINT opinie_pk
        PRIMARY KEY (identyfikator_klienta, identyfikator_restauracji),
        
    CONSTRAINT opinie_fk_klienci
        FOREIGN KEY (identyfikator_klienta)
            REFERENCES klienci(identyfikator)
            ON DELETE CASCADE,
            
    CONSTRAINT opinie_fk_restauracje
        FOREIGN KEY (identyfikator_restauracji)
            REFERENCES restauracje(identyfikator)
            ON DELETE CASCADE,
            
    CONSTRAINT opinie_chk_ocena
        CHECK (ocena BETWEEN 1 AND 5)
);


CREATE TABLE dania (
    identyfikator_restauracji   VARCHAR2(30),
    nazwa                       VARCHAR2(50),
    opis                        VARCHAR2(255),
    cena                        NUMBER(5, 2) NOT NULL,
    nazwa_kategorii             VARCHAR2(50),
    
    CONSTRAINT dania_pk
        PRIMARY KEY (identyfikator_restauracji, nazwa),
        
    CONSTRAINT dania_fk_restauracje
        FOREIGN KEY (identyfikator_restauracji)
            REFERENCES restauracje(identyfikator)
            ON DELETE CASCADE,
            
    CONSTRAINT dania_fk_kategorie
        FOREIGN KEY (nazwa_kategorii)
            REFERENCES kategorie(nazwa),
            
    CONSTRAINT dania_chk_cena
        CHECK (cena > 0)
);


CREATE TABLE alergeny_w_daniach (
    identyfikator_restauracji   VARCHAR2(30),
    nazwa_dania                 VARCHAR2(50),
    nazwa_alergenu              VARCHAR2(50),
    
    CONSTRAINT alergeny_w_daniach_pk
        PRIMARY KEY (identyfikator_restauracji, nazwa_dania, nazwa_alergenu),
        
    CONSTRAINT alergeny_w_daniach_fk_dania
        FOREIGN KEY (identyfikator_restauracji, nazwa_dania)
            REFERENCES dania(identyfikator_restauracji, nazwa)
            ON DELETE CASCADE,
            
    CONSTRAINT alergeny_w_daniach_fk_alergeny
        FOREIGN KEY (nazwa_alergenu)
            REFERENCES alergeny(nazwa)        
);


CREATE TABLE zamowienia (
    id_zamowienia               NUMBER(4) GENERATED ALWAYS AS IDENTITY,
    identyfikator_klienta       VARCHAR2(30) NOT NULL,
    identyfikator_dostawcy      VARCHAR2(30),
    identyfikator_restauracji   VARCHAR2(30) NOT NULL,
    nazwa_metody_platnosci      VARCHAR2(50) NOT NULL,
    status                      VARCHAR2(15) DEFAULT 'REALIZACJA' NOT NULL,
    data_godzina                TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    adres                       VARCHAR2(100) NOT NULL,
    dodatkowe_informacje        VARCHAR2(255),
    
    CONSTRAINT zamowienia_pk
        PRIMARY KEY (id_zamowienia),
        
    CONSTRAINT zamowienia_fk_klienci
        FOREIGN KEY (identyfikator_klienta)
            REFERENCES klienci(identyfikator)
            ON DELETE CASCADE,
    
    CONSTRAINT zamowienia_fk_dostawcy
        FOREIGN KEY (identyfikator_dostawcy)
            REFERENCES dostawcy(identyfikator) 
            ON DELETE CASCADE,
            
    CONSTRAINT zamowienia_fk_restauracje
        FOREIGN KEY (identyfikator_restauracji)
            REFERENCES restauracje(identyfikator)
            ON DELETE CASCADE,
            
    CONSTRAINT zamowienia_fk_metody_platnosci
        FOREIGN KEY (nazwa_metody_platnosci)
            REFERENCES metody_platnosci(nazwa),
            
    CONSTRAINT zamowienia_chk_status
        CHECK (status IN ('REALIZACJA', 'DOSTAWA', 'ZAKONCZONE'))
);


CREATE TABLE dania_w_zamowieniu (
    identyfikator_restauracji   VARCHAR2(30),
    nazwa_dania                 VARCHAR2(50),
    id_zamowienia               NUMBER(4),
    licznosc                    NUMBER(3),
    
    CONSTRAINT dania_w_zamowieniu_pk
        PRIMARY KEY (identyfikator_restauracji, nazwa_dania, id_zamowienia, licznosc),
        
    CONSTRAINT dania_w_zamowieniu_fk_dania
        FOREIGN KEY (identyfikator_restauracji, nazwa_dania)
            REFERENCES dania(identyfikator_restauracji, nazwa)
            ON DELETE CASCADE,
            
    CONSTRAINT dania_w_zamowieniu_fk_zamowienia
        FOREIGN KEY (id_zamowienia)
            REFERENCES zamowienia(id_zamowienia)
            ON DELETE CASCADE,
            
    CONSTRAINT dania_w_zamowieniu_chk_licznosc
        CHECK (licznosc > 0)
);


-- EXPLICIT INDICES
CREATE INDEX dania_nazwa_index
ON dania(nazwa);

CREATE INDEX dania_cena_index
ON dania(cena);


-- CONSTANT INSERTS
INSERT INTO metody_platnosci(nazwa)
VALUES ('GOTÓWKA PRZY ODBIORZE');
INSERT INTO metody_platnosci(nazwa)
VALUES ('KARTA PRZY ODBIORZE');
INSERT INTO metody_platnosci(nazwa)
VALUES ('BLIKIEM');


-- FUNCTIONS AND PROCEDURES
CREATE PACKAGE Wspolne AUTHID DEFINER AS
    -- current user type (uzytkownicy.typ) or NULL
    FUNCTION Pobierz_Typ_Uzytkownika RETURN uzytkownicy.typ%TYPE;
    
    -- identyfikator == USER
    PROCEDURE Wstaw_Klienta(
        pImie IN klienci.imie%TYPE, 
        pNazwisko IN klienci.nazwisko%TYPE, 
        pDomyslnyAdres IN klienci.domyslny_adres%TYPE);
    PROCEDURE Wstaw_Dostawce(
        pImie IN dostawcy.imie%TYPE, 
        pNazwisko IN dostawcy.nazwisko%TYPE);
    PROCEDURE Wstaw_Restauracje(
        pNazwa IN restauracje.nazwa%TYPE,
        pOpis IN restauracje.opis%TYPE);
    PROCEDURE Usun_Konto;
        
    FUNCTION Cena_Zamowienia(pIdZamowienia IN zamowienia.id_zamowienia%TYPE) RETURN NUMBER;
END Wspolne;
/

CREATE PACKAGE Klient AS
    PROCEDURE Zmien_Domyslny_Adres(pDomyslnyAdres IN klienci.domyslny_adres%TYPE);
        
    PROCEDURE Wstaw_Opinie(
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pOcena IN opinie.ocena%TYPE,
        pKomentarz IN opinie.komentarz%TYPE);
        
    PROCEDURE Zmien_Opinie(
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pOcena IN opinie.ocena%TYPE,
        pKomentarz IN opinie.komentarz%TYPE);
        
    PROCEDURE Usun_Opinie(pIdRestauracji IN restauracje.identyfikator%TYPE);
        
    FUNCTION Wstaw_Zamowienie(
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pNazwaPlatnosci IN metody_platnosci.nazwa%TYPE,
        pAdres IN zamowienia.adres%TYPE,
        pDodatkoweInformacje IN zamowienia.dodatkowe_informacje%TYPE) 
        RETURN zamowienia.id_zamowienia%TYPE;
        
    PROCEDURE Wstaw_Dania_W_Zamowieniu(
        pIdZamowienia IN zamowienia.id_zamowienia%TYPE,
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pNazwaDania IN dania.nazwa%TYPE,
        pLicznosc IN dania_w_zamowieniu.licznosc%TYPE);
END Klient;
/

CREATE PACKAGE Dostawca AS
    PROCEDURE Zmien_Czy_Dostepny(pCzyDostepny IN dostawcy.czy_dostepny%TYPE);
    
    PROCEDURE Zakoncz_Zamowienie(pIdZamowienia IN zamowienia.id_zamowienia%TYPE);
END Dostawca;
/

CREATE PACKAGE Restauracja AS
    PROCEDURE Wstaw_Kategorie(pNazwa IN kategorie.nazwa%TYPE);
    
    PROCEDURE Wstaw_Alergen(pNazwa IN alergeny.nazwa%TYPE);
    
    PROCEDURE Zmien_Opis(pOpis IN restauracje.opis%TYPE);
    
    PROCEDURE Wstaw_Lub_Zmien_Danie(
        pNazwa IN dania.nazwa%TYPE,
        pOpis IN dania.opis%TYPE,
        pCena IN dania.cena%TYPE,
        pNazwaKategorii IN kategorie.nazwa%TYPE);
    
    PROCEDURE Dodaj_Alergen_Do_Dania(
        pNazwaDania IN dania.nazwa%TYPE,
        pNazwaAlergenu IN alergeny.nazwa%TYPE);
        
    PROCEDURE Usun_Alergen_Z_Dania(
        pNazwaDania IN dania.nazwa%TYPE,
        pNazwaAlergenu IN alergeny.nazwa%TYPE);
    
    PROCEDURE Zmien_Status_Zamowienia(
        pIdZamowienia IN zamowienia.id_zamowienia%TYPE,
        pIdDostawcy IN dostawcy.identyfikator%TYPE);
END Restauracja;
/

-- ROLES AND PRIVILAGES
-- ALTER SESSION SET "_ORACLE_SCRIPT"=TRUE;

--      public

--      klient
CREATE ROLE rola_klient;

GRANT SELECT
ON kategorie
TO rola_klient;

GRANT SELECT
ON alergeny
TO rola_klient;

GRANT SELECT 
ON metody_platnosci 
TO rola_klient;

GRANT SELECT, UPDATE 
ON klienci 
TO rola_klient;

GRANT SELECT 
ON dostawcy 
TO rola_klient;

GRANT SELECT 
ON restauracje 
TO rola_klient;

GRANT SELECT, INSERT, UPDATE, DELETE 
ON opinie 
TO rola_klient;

GRANT SELECT
ON dania
TO rola_klient;

GRANT SELECT
ON alergeny_w_daniach
TO rola_klient;

GRANT SELECT, INSERT
ON zamowienia
TO rola_klient;

GRANT SELECT, INSERT
ON dania_w_zamowieniu
TO rola_klient;

--      dostawca
CREATE ROLE rola_dostawca;

GRANT SELECT
ON klienci
TO rola_dostawca;

GRANT SELECT, UPDATE
ON dostawcy
TO rola_dostawca;

GRANT SELECT
ON restauracje
TO rola_dostawca;

GRANT SELECT
ON dania
TO rola_dostawca;

GRANT SELECT, UPDATE
ON zamowienia
TO rola_dostawca;

GRANT SELECT
ON dania_w_zamowieniu
TO rola_dostawca;

--      restauracja
CREATE ROLE rola_restauracja;

GRANT SELECT, INSERT
ON kategorie
TO rola_restauracja;

GRANT SELECT, INSERT
ON alergeny
TO rola_restauracja;

GRANT SELECT, UPDATE
ON restauracje
TO rola_restauracja;

GRANT SELECT
ON dostawcy
TO rola_restauracja;

GRANT SELECT
ON opinie
TO rola_restauracja;

GRANT SELECT, INSERT, UPDATE, DELETE
ON dania
TO rola_restauracja;

GRANT SELECT, INSERT, DELETE
ON alergeny_w_daniach
TO rola_restauracja;

GRANT SELECT, UPDATE
ON zamowienia
TO rola_restauracja;

GRANT SELECT
ON dania_w_zamowieniu
TO rola_restauracja;


-- IMPLEMENTATION
CREATE OR REPLACE PACKAGE BODY Wspolne AS
    FUNCTION Pobierz_Typ_Uzytkownika RETURN uzytkownicy.typ%TYPE AS
        vTyp uzytkownicy.typ%TYPE;
    BEGIN 
        SELECT typ
        INTO vTyp
        FROM uzytkownicy
        WHERE identyfikator = USER;
        
        RETURN vTyp;
    END;
    
    PROCEDURE Wstaw_Klienta(
        pImie IN klienci.imie%TYPE, 
        pNazwisko IN klienci.nazwisko%TYPE, 
        pDomyslnyAdres IN klienci.domyslny_adres%TYPE) AS
    BEGIN
        INSERT INTO uzytkownicy(identyfikator, typ)
        VALUES(USER, 'KLIENT');
        
        INSERT INTO klienci(identyfikator, imie, nazwisko, domyslny_adres)
        VALUES(USER, pImie, pNazwisko, pDomyslnyAdres);
    END;
    
    PROCEDURE Wstaw_Dostawce(
        pImie IN dostawcy.imie%TYPE, 
        pNazwisko IN dostawcy.nazwisko%TYPE) AS
    BEGIN
        INSERT INTO uzytkownicy(identyfikator, typ)
        VALUES(USER, 'DOSTAWCA');
        
        INSERT INTO dostawcy(identyfikator, imie, nazwisko)
        VALUES(USER, pImie, pNazwisko);
    END;
        
    PROCEDURE Wstaw_Restauracje(
        pNazwa IN restauracje.nazwa%TYPE,
        pOpis IN restauracje.opis%TYPE) AS
    BEGIN
        INSERT INTO uzytkownicy(identyfikator, typ)
        VALUES(USER, 'RESTAURACJA');
        
        INSERT INTO restauracje(identyfikator, nazwa, opis)
        VALUES(USER, pNazwa, pOpis);
    END;
    
    PROCEDURE Usun_Konto AS
    BEGIN
        DELETE FROM uzytkownicy
        WHERE identyfikator = USER;
    END;
    
    FUNCTION Cena_Zamowienia(pIdZamowienia IN zamowienia.id_zamowienia%TYPE) RETURN NUMBER AS
        vSuma NUMBER;
    BEGIN
        SELECT SUM(cena * licznosc)
        INTO vSuma
        FROM dania
            JOIN dania_w_zamowieniu ON dania.identyfikator_restauracji = dania_w_zamowieniu.identyfikator_restauracji
                AND dania.nazwa = dania_w_zamowieniu.nazwa_dania
        WHERE id_zamowienia = pIdZamowienia;
        
        RETURN vSuma;
    END;
END Wspolne;
/

CREATE OR REPLACE PACKAGE BODY Klient AS
    PROCEDURE Zmien_Domyslny_Adres(pDomyslnyAdres IN klienci.domyslny_adres%TYPE) AS
    BEGIN
        UPDATE klienci
        SET domyslny_adres = pDomyslnyAdres
        WHERE identyfikator = USER;
    END;
        
    PROCEDURE Wstaw_Opinie(
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pOcena IN opinie.ocena%TYPE,
        pKomentarz IN opinie.komentarz%TYPE) AS
    BEGIN
        INSERT INTO opinie(identyfikator_klienta, identyfikator_restauracji, ocena, komentarz)
        VALUES(USER, pIdRestauracji, pOcena, pKomentarz);
    END;
    
    PROCEDURE Zmien_Opinie(
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pOcena IN opinie.ocena%TYPE,
        pKomentarz IN opinie.komentarz%TYPE) AS
    BEGIN
        UPDATE opinie
        SET ocena = pOcena,
            komentarz = pKomentarz
        WHERE identyfikator_klienta = USER
            AND identyfikator_restauracji = pIdRestauracji;
    END;
    
    PROCEDURE Usun_Opinie(pIdRestauracji IN restauracje.identyfikator%TYPE) AS
    BEGIN
        DELETE FROM opinie
        WHERE identyfikator_restauracji = pIdRestauracji;
    END;
        
    FUNCTION Wstaw_Zamowienie(
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pNazwaPlatnosci IN metody_platnosci.nazwa%TYPE,
        pAdres IN zamowienia.adres%TYPE,
        pDodatkoweInformacje IN zamowienia.dodatkowe_informacje%TYPE)
        RETURN zamowienia.id_zamowienia%TYPE AS
        vIdZamowienia zamowienia.id_zamowienia%TYPE;
    BEGIN
        INSERT INTO zamowienia(identyfikator_klienta, identyfikator_restauracji, 
            nazwa_metody_platnosci, adres, dodatkowe_informacje)
        VALUES (USER, pIdRestauracji, pNazwaPlatnosci, pAdres, pDodatkoweInformacje)
        RETURNING id_zamowienia INTO vIdZamowienia;
        
        RETURN vIdZamowienia;
    END;
        
    PROCEDURE Wstaw_Dania_W_Zamowieniu(
        pIdZamowienia IN zamowienia.id_zamowienia%TYPE,
        pIdRestauracji IN restauracje.identyfikator%TYPE,
        pNazwaDania IN dania.nazwa%TYPE,
        pLicznosc IN dania_w_zamowieniu.licznosc%TYPE) AS
    BEGIN
        INSERT INTO dania_w_zamowieniu(id_zamowienia, identyfikator_restauracji, nazwa_dania, licznosc)
        VALUES (pIdZamowienia, pIdRestauracji, pNazwaDania, pLicznosc);
    END;
END Klient;
/

CREATE OR REPLACE PACKAGE BODY Dostawca IS
    PROCEDURE Zmien_Czy_Dostepny(pCzyDostepny IN dostawcy.czy_dostepny%TYPE) IS
    BEGIN
        UPDATE dostawcy
        SET czy_dostepny = pCzyDostepny
        WHERE identyfikator = USER;
    END;
    
    PROCEDURE Zakoncz_Zamowienie(pIdZamowienia IN zamowienia.id_zamowienia%TYPE) IS
    BEGIN
        UPDATE zamowienia
        SET status = 'ZAKONCZONE'
        WHERE id_zamowienia = pIdZamowienia;
    END;
END Dostawca;
/

CREATE PACKAGE BODY Restauracja IS
    PROCEDURE Wstaw_Kategorie(pNazwa IN kategorie.nazwa%TYPE) IS
    BEGIN
        INSERT INTO kategorie(nazwa)
        VALUES (pNazwa);
    END;
    
    PROCEDURE Wstaw_Alergen(pNazwa IN alergeny.nazwa%TYPE) IS
    BEGIN
        INSERT INTO alergeny(nazwa)
        VALUES (pNazwa);
    END;
    
    PROCEDURE Zmien_Opis(pOpis IN restauracje.opis%TYPE) IS
    BEGIN
        UPDATE restauracje
        SET opis = pOpis
        WHERE identyfikator = USER;
    END;
    
    PROCEDURE Wstaw_Lub_Zmien_Danie(
        pNazwa IN dania.nazwa%TYPE,
        pOpis IN dania.opis%TYPE,
        pCena IN dania.cena%TYPE,
        pNazwaKategorii IN kategorie.nazwa%TYPE) AS
        vCount NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO vCount
        FROM dania
        WHERE identyfikator_restauracji = USER
            AND nazwa = pNazwa;
            
        IF vCount > 0 THEN
            UPDATE dania
            SET opis = pOpis,
                cena = pCena,
                nazwa_kategorii = pNazwaKategorii
            WHERE identyfikator_restauracji = USER
                AND nazwa = pNazwa;
        ELSE
            INSERT INTO dania(identyfikator_restauracji, nazwa, opis, cena, nazwa_kategorii)
            VALUES (USER, pNazwa, pOpis, pCena, pNazwaKategorii);
        END IF;
    END;
    
    PROCEDURE Dodaj_Alergen_Do_Dania(
        pNazwaDania IN dania.nazwa%TYPE,
        pNazwaAlergenu IN alergeny.nazwa%TYPE) IS
    BEGIN
        INSERT INTO alergeny_w_daniach(identyfikator_restauracji, nazwa_dania, nazwa_alergenu)
        VALUES (USER, pNazwaDania, pNazwaAlergenu);
    END;
    
    PROCEDURE Usun_Alergen_Z_Dania(
        pNazwaDania IN dania.nazwa%TYPE,
        pNazwaAlergenu IN alergeny.nazwa%TYPE) IS
    BEGIN
        DELETE FROM alergeny_w_daniach
        WHERE identyfikator_restauracji = USER
            AND nazwa_dania = pNazwaDania
            AND nazwa_alergenu = pNazwaAlergenu;
    END;
    
    PROCEDURE Zmien_Status_Zamowienia(
        pIdZamowienia IN zamowienia.id_zamowienia%TYPE,
        pIdDostawcy IN dostawcy.identyfikator%TYPE) IS
    BEGIN
        UPDATE zamowienia
        SET status = 'DOSTAWA',
            identyfikator_dostawcy = pIdDostawcy
        WHERE id_zamowienia = pIdZamowienia;
    END;
END Restauracja;
/

-- ROLES EXECUTE
GRANT EXECUTE 
ON Wspolne 
TO PUBLIC;

GRANT EXECUTE
ON Klient
TO rola_klient;

GRANT EXECUTE
ON Dostawca
TO rola_dostawca;

GRANT EXECUTE
ON Restauracja
TO rola_restauracja;

COMMIT;