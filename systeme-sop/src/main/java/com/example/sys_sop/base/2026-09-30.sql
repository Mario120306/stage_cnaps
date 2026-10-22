/*user DRH*/
CREATE TABLE DRH_DIRECTION(
    code_direction      VARCHAR2(5)     PRIMARY KEY,
    libelle_direction   VARCHAR2(50),
    code_abrev          VARCHAR2(5)
);
CREATE TABLE DRH_SERVICE(
    code_service        VARCHAR2(5)     PRIMARY KEY,
    libelle_service     VARCHAR2(50),
    code_direction      VARCHAR2(5) 
);
/*user INTRANET */
CREATE TABLE VIEW_ANNUAIRE_ELECTRONIQUE(
    matricule           CHAR(6)         PRIMARY KEY,
    nom                 VARCHAR2(100)   NOT NULL,
    prenoms             VARCHAR2(80),
    date_naissaince     DATE,
    lieu                VARCHAR2(5),
    code_direction      VARCHAR2(5),
    libelle_direction   VARCHAR2(100),
    code_service        VARCHAR2(5),
    libelle_service     VARCHAR2(100), 
    code_fonction       VARCHAR2(100),
    libelle_fonction    VARCHAR2(100),
    mail                VARCHAR2(255),
    tel_gsm             VARCHAR2(20)
);
/*TABLE CREER */
CREATE TABLE SOP_TEXT_STATIQUE (
    code_text           VARCHAR2(50)   PRIMARY KEY,
    contenu             CLOB,
    date_maj            DATE           DEFAULT SYSDATE,
    actif               CHAR(1)        DEFAULT 'O' CHECK (actif IN ('O', 'N'))
);
CREATE TABLE SOP_STATUT(
    code_statut         VARCHAR2(20)   PRIMARY KEY,
    libelle             VARCHAR2(100)  NOT NULL,
    ordre               NUMBER         NOT NULL
);
CREATE TABLE SOP(
    id_sop              VARCHAR2(50) PRIMARY KEY,
    numero_sop          VARCHAR2(50) NOT NULL UNIQUE,
    titre               VARCHAR2(100) NOT NULL,
    version_courante    VARCHAR2(50) DEFAULT '1' NOT NULL,
    code_service        VARCHAR2(5) NOT NULL,
    auteur_matricule    CHAR(6) NOT NULL,
    section_validee     NUMBER(1) DEFAULT 0 NOT NULL 
                        CHECK (section_validee BETWEEN 0 AND 8),
    code_statut         VARCHAR2(20) NOT NULL,
    supprime            CHAR(1) DEFAULT 'N' NOT NULL 
                        CHECK (supprime IN ('O', 'N')),
    date_suppression    DATE,
    date_creation       DATE DEFAULT SYSDATE NOT NULL,
    date_modification   DATE,
    CONSTRAINT fk_sop_statut FOREIGN KEY (code_statut) 
        REFERENCES SOP_STATUT(code_statut)   
);

-- Table SOP VERSION (historique du document)
CREATE TABLE SOP_VERSION(
    id_version              VARCHAR2(50) PRIMARY KEY,
    id_sop                  VARCHAR2(50) NOT NULL,
    numero_version          VARCHAR2(2) NOT NULL,
    date_entree             DATE DEFAULT SYSDATE NOT NULL,
    libelle_modification    VARCHAR2(255),
    auteur_modification     VARCHAR2(10),
    code_statut             VARCHAR2(20),
    CONSTRAINT fk_version_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE,
    CONSTRAINT fk_version_statut FOREIGN KEY (code_statut) 
        REFERENCES SOP_STATUT(code_statut)
);

-- Table SOP CONTACT (commentaires sur le document)
CREATE TABLE SOP_CONTACT(
    id_contact      VARCHAR2(50) PRIMARY KEY,
    id_sop          VARCHAR2(50) NOT NULL,
    matricule       VARCHAR2(10),
    email           VARCHAR2(100),
    ordre           NUMBER(3),
    CONSTRAINT fk_contact_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE
);

-- Table SOP DESTINATAIRE
CREATE TABLE SOP_DESTINATAIRE(
    id_destinataire VARCHAR2(50) PRIMARY KEY,
    id_sop          VARCHAR2(50) NOT NULL,
    matricule       VARCHAR2(10),
    nom             VARCHAR2(50),
    organisation    VARCHAR2(100),
    action          VARCHAR2(50),
    date_action     DATE,
    ordre           NUMBER(3),
    CONSTRAINT fk_dest_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE
);

-- Table SOP EQUIPE PROJET (remplace SOP_TEAMS)
CREATE TABLE SOP_EQUIPE_PROJET(
    id_membre       VARCHAR2(50) PRIMARY KEY,
    id_sop          VARCHAR2(50) NOT NULL,
    matricule       VARCHAR2(10),
    nom             VARCHAR2(50),
    fonction        VARCHAR2(50),
    telephone       VARCHAR2(20),
    email           VARCHAR2(100),
    ordre           NUMBER(3), 
    CONSTRAINT fk_equipe_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE
);

-- Table SOP GLOSSAIRE
CREATE TABLE SOP_GLOSSAIRE(
    id_glossaire    VARCHAR2(50) PRIMARY KEY,
    id_sop          VARCHAR2(50) NOT NULL,
    acronyme        VARCHAR2(50),
    terme           VARCHAR2(255),
    ordre           NUMBER(3),
    CONSTRAINT fk_glossaire_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE
);

-- Table SOP REFERENCE
CREATE TABLE SOP_REFERENCE(
    id_reference    VARCHAR2(50) PRIMARY KEY,
    id_sop          VARCHAR2(50) NOT NULL,
    contenu         CLOB,
    ordre           NUMBER(3),
    CONSTRAINT fk_reference_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE
);

-- Table PIECE JOINTE (gestion des fichiers physiques)
CREATE TABLE SOP_PIECE_JOINTE(
    id_piece_jointe     VARCHAR2(50) PRIMARY KEY,
    id_sop              VARCHAR2(50) NOT NULL,
    usage               VARCHAR2(20) CHECK (usage IN ('FLOWCHART', 'ANNEXE')),
    nom_fichier         VARCHAR2(255) NOT NULL,
    type_mime           VARCHAR2(50),
    taille              NUMBER(10),
    contenu             BLOB,
    auteur_matricule    VARCHAR2(10),
    date_insert         DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT fk_pj_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE
);

-- Table SOP ANNEXE
CREATE TABLE SOP_ANNEXE(
    id_annexe       VARCHAR2(50) PRIMARY KEY,
    id_sop          VARCHAR2(50) NOT NULL,
    titre           VARCHAR2(50),
    id_piece_jointe VARCHAR2(50),
    ordre           NUMBER(3),
    CONSTRAINT fk_annexe_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE,
    CONSTRAINT fk_annexe_pj FOREIGN KEY (id_piece_jointe) 
        REFERENCES SOP_PIECE_JOINTE(id_piece_jointe)
);

-- Table SOP FLOWCHART
CREATE TABLE SOP_FLOWCHART(
    id_flowchart    VARCHAR2(50) PRIMARY KEY,
    id_sop          VARCHAR2(50) NOT NULL,
    titre           VARCHAR2(50),
    information     VARCHAR(200),  
    ordre           NUMBER(3),
    id_piece_jointe VARCHAR2(50), 
    CONSTRAINT fk_flow_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE,
    CONSTRAINT fk_flow_pj FOREIGN KEY (id_piece_jointe) 
        REFERENCES SOP_PIECE_JOINTE(id_piece_jointe)
);

-- Table SOP FLOWCHART ETAPE (ex FLOWCHART_DETAIL)
CREATE TABLE SOP_FLOWCHART_ETAPE(
    id_etape            VARCHAR2(50) PRIMARY KEY,
    id_flowchart        VARCHAR2(50) NOT NULL,
    information         CLOB,
    niveau              NUMBER(2),
    mode_numerotation   VARCHAR2(10) CHECK (mode_numerotation IN ('AUTO', 'MANUEL')),
    ordre               NUMBER(3),
    CONSTRAINT fk_etape_flow FOREIGN KEY (id_flowchart) 
        REFERENCES SOP_FLOWCHART(id_flowchart) ON DELETE CASCADE
);
-- Table SOP EXPORT (PDF généré)
CREATE TABLE SOP_EXPORT(
    id_export           VARCHAR2(50) PRIMARY KEY,
    id_sop              VARCHAR2(50) NOT NULL,
    id_version          VARCHAR2(50),
    libelle_export      VARCHAR2(100) DEFAULT 'Draft version',
    nb_pages            NUMBER(5),
    date_export         DATE DEFAULT SYSDATE NOT NULL,
    nom_fichier_pdf     VARCHAR2(255),
    genere_par          VARCHAR2(10),
    contenu_pdf         BLOB,
    CONSTRAINT fk_export_sop FOREIGN KEY (id_sop) 
        REFERENCES SOP(id_sop) ON DELETE CASCADE,
    CONSTRAINT fk_export_version FOREIGN KEY (id_version) 
        REFERENCES SOP_VERSION(id_version)
);

CREATE SEQUENCE seq_sop
    START WITH 1
    INCREMENT BY 1
    NOCACHE;