-- ============================================
-- 1. DRH_DIRECTION
-- ============================================
INSERT INTO DRH_DIRECTION (code_direction, libelle_direction, code_abrev) VALUES ('D001', 'Direction des Ressources Humaines', 'DRH');
INSERT INTO DRH_DIRECTION (code_direction, libelle_direction, code_abrev) VALUES ('D002', 'Direction Financière', 'DF');
INSERT INTO DRH_DIRECTION (code_direction, libelle_direction, code_abrev) VALUES ('D003', 'Direction Informatique', 'DI');
INSERT INTO DRH_DIRECTION (code_direction, libelle_direction, code_abrev) VALUES ('D004', 'Direction Juridique', 'DJ');
INSERT INTO DRH_DIRECTION (code_direction, libelle_direction, code_abrev) VALUES ('D005', 'Direction Générale', 'DG');
INSERT INTO DRH_DIRECTION (code_direction, libelle_direction, code_abrev) VALUES ('D006', 'Direction des Opérations', 'DO');
INSERT INTO DRH_DIRECTION (code_direction, libelle_direction, code_abrev) VALUES ('D007', 'Direction Commerciale', 'DC');

-- ============================================
-- 2. DRH_SERVICE
-- ============================================
-- Services rattachés à la DRH (D001)
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S001', 'Service Recrutement', 'D001');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S002', 'Service Formation', 'D001');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S003', 'Service Paie', 'D001');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S004', 'Service Administration du Personnel', 'D001');

-- Services rattachés à la Direction Financière (D002)
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S005', 'Service Comptabilité', 'D002');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S006', 'Service Budget', 'D002');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S007', 'Service Trésorerie', 'D002');

-- Services rattachés à la Direction Informatique (D003)
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S008', 'Service Développement', 'D003');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S009', 'Service Infrastructure', 'D003');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S010', 'Service Support Technique', 'D003');

-- Services rattachés à la Direction Juridique (D004)
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S011', 'Service Contentieux', 'D004');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S012', 'Service Marchés Publics', 'D004');

-- Services rattachés à la Direction Générale (D005)
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S013', 'Secrétariat Général', 'D005');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S014', 'Service Communication', 'D005');

-- Services rattachés à la Direction des Opérations (D006)
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S015', 'Service Logistique', 'D006');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S016', 'Service Maintenance', 'D006');

-- Services rattachés à la Direction Commerciale (D007)
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S017', 'Service Ventes', 'D007');
INSERT INTO DRH_SERVICE (code_service, libelle_service, code_direction) VALUES ('S018', 'Service Marketing', 'D007');

-- ============================================
-- 3. VIEW_ANNUAIRE_ELECTRONIQUE
-- ============================================
-- NB : matricule = CHAR(6) → toujours 6 caractères

-- Direction Générale (D005)
INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000001', 'RAKOTO', 'Jean Claude', TO_DATE('1965-03-15','YYYY-MM-DD'), 'L001', 'D005', 'Direction Générale', 'S013', 'Secrétariat Général', 'F001', 'Directeur Général', 'jc.rakoto@entreprise.mg', '0340000001');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000002', 'RANDRIA', 'Soa Heriniaina', TO_DATE('1970-07-22','YYYY-MM-DD'), 'L002', 'D005', 'Direction Générale', 'S014', 'Service Communication', 'F002', 'Directeur Adjoint', 'sh.randria@entreprise.mg', '0340000002');

-- Direction des Ressources Humaines (D001)
INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000003', 'RABE', 'Marie Claire', TO_DATE('1975-11-08','YYYY-MM-DD'), 'L001', 'D001', 'Direction des Ressources Humaines', 'S001', 'Service Recrutement', 'F003', 'Directeur RH', 'mc.rabe@entreprise.mg', '0340000003');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000004', 'ANDRIANARIVO', 'Paul', TO_DATE('1982-04-30','YYYY-MM-DD'), 'L003', 'D001', 'Direction des Ressources Humaines', 'S002', 'Service Formation', 'F004', 'Responsable Formation', 'p.andrianarivo@entreprise.mg', '0340000004');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000005', 'RAZAFY', 'Hery', TO_DATE('1988-09-12','YYYY-MM-DD'), 'L002', 'D001', 'Direction des Ressources Humaines', 'S003', 'Service Paie', 'F005', 'Gestionnaire Paie', 'h.razafy@entreprise.mg', '0340000005');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000006', 'RAHARISON', 'Voahangy', TO_DATE('1990-02-25','YYYY-MM-DD'), 'L004', 'D001', 'Direction des Ressources Humaines', 'S004', 'Service Administration du Personnel', 'F006', 'Assistant RH', 'v.raharison@entreprise.mg', '0340000006');

-- Direction Financière (D002)
INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000007', 'RANAIVO', 'Tiana', TO_DATE('1978-06-18','YYYY-MM-DD'), 'L001', 'D002', 'Direction Financière', 'S005', 'Service Comptabilité', 'F007', 'Directeur Financier', 't.ranaivo@entreprise.mg', '0340000007');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000008', 'ANDRIAMAMPIONONA', 'Lova', TO_DATE('1985-12-03','YYYY-MM-DD'), 'L005', 'D002', 'Direction Financière', 'S006', 'Service Budget', 'F008', 'Contrôleur de Gestion', 'l.andriamampionona@entreprise.mg', '0340000008');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000009', 'RASOLOFO', 'Nirina', TO_DATE('1992-08-14','YYYY-MM-DD'), 'L002', 'D002', 'Direction Financière', 'S007', 'Service Trésorerie', 'F009', 'Comptable', 'n.rasolofo@entreprise.mg', '0340000009');

-- Direction Informatique (D003)
INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000010', 'RAKOTOMALALA', 'Mamy', TO_DATE('1980-01-20','YYYY-MM-DD'), 'L001', 'D003', 'Direction Informatique', 'S008', 'Service Développement', 'F010', 'Directeur Informatique', 'm.rakotomalala@entreprise.mg', '0340000010');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000011', 'RAJAONARIVELO', 'Fenosoa', TO_DATE('1991-05-09','YYYY-MM-DD'), 'L003', 'D003', 'Direction Informatique', 'S008', 'Service Développement', 'F011', 'Développeur Senior', 'f.rajaonarivelo@entreprise.mg', '0340000011');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000012', 'RANDRIAMANANA', 'Toky', TO_DATE('1993-10-27','YYYY-MM-DD'), 'L004', 'D003', 'Direction Informatique', 'S009', 'Service Infrastructure', 'F012', 'Administrateur Système', 't.randriamanana@entreprise.mg', '0340000012');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000013', 'RABEMANANJARA', 'Aina', TO_DATE('1994-07-16','YYYY-MM-DD'), 'L005', 'D003', 'Direction Informatique', 'S010', 'Service Support Technique', 'F013', 'Technicien Support', 'a.rabemananjara@entreprise.mg', '0340000013');

-- Direction Juridique (D004)
INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000014', 'RAHARIMALALA', 'Bodo', TO_DATE('1976-03-11','YYYY-MM-DD'), 'L001', 'D004', 'Direction Juridique', 'S011', 'Service Contentieux', 'F014', 'Directeur Juridique', 'b.raharimalala@entreprise.mg', '0340000014');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000015', 'ANDRIANJAFY', 'Rado', TO_DATE('1987-09-05','YYYY-MM-DD'), 'L002', 'D004', 'Direction Juridique', 'S012', 'Service Marchés Publics', 'F015', 'Juriste', 'r.andrianjafy@entreprise.mg', '0340000015');

-- Direction des Opérations (D006)
INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000016', 'RAKOTONDRABE', 'Solo', TO_DATE('1983-11-30','YYYY-MM-DD'), 'L003', 'D006', 'Direction des Opérations', 'S015', 'Service Logistique', 'F016', 'Directeur Opérations', 's.rakotondrabe@entreprise.mg', '0340000016');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000017', 'RASOAMANANA', 'Fanja', TO_DATE('1989-04-21','YYYY-MM-DD'), 'L004', 'D006', 'Direction des Opérations', 'S016', 'Service Maintenance', 'F017', 'Chef d''équipe', 'f.rasoamanana@entreprise.mg', '0340000017');

-- Direction Commerciale (D007)
INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000018', 'RAZANADRAKOTO', 'Miora', TO_DATE('1986-08-08','YYYY-MM-DD'), 'L005', 'D007', 'Direction Commerciale', 'S017', 'Service Ventes', 'F018', 'Directeur Commercial', 'm.razanadrakoto@entreprise.mg', '0340000018');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000019', 'RANDRIANASOLO', 'Haja', TO_DATE('1995-01-17','YYYY-MM-DD'), 'L001', 'D007', 'Direction Commerciale', 'S017', 'Service Ventes', 'F019', 'Commercial', 'h.randrianasolo@entreprise.mg', '0340000019');

INSERT INTO VIEW_ANNUAIRE_ELECTRONIQUE 
(matricule, nom, prenoms, date_naissaince, lieu, code_direction, libelle_direction, code_service, libelle_service, code_fonction, libelle_fonction, mail, tel_gsm)
VALUES 
('000020', 'RAKOTOARISOA', 'Naina', TO_DATE('1997-06-23','YYYY-MM-DD'), 'L002', 'D007', 'Direction Commerciale', 'S018', 'Service Marketing', 'F020', 'Chargé Marketing', 'n.rakotoarisoa@entreprise.mg', '0340000020');

-- ============================================
-- 4. SOP (31 enregistrements)
-- ============================================
INSERT INTO SOP_STATUT (code_statut, libelle, ordre) VALUES ('STAT001', 'ACTIF', 1);
INSERT INTO SOP_STATUT (code_statut, libelle, ordre) VALUES ('STAT002', 'ARCHIVE', 2);

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-001', 'SOP-RH-001', 'Procedure de recrutement', 'S001', '000003', 'STAT001',
        TO_DATE('2025-01-05','YYYY-MM-DD'), TO_DATE('2025-01-12','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-002', 'SOP-RH-002', 'Procedure de formation du personnel', 'S002', '000004', 'STAT001',
        TO_DATE('2025-01-15','YYYY-MM-DD'), TO_DATE('2025-01-22','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-003', 'SOP-RH-003', 'Procedure de traitement de la paie', 'S003', '000005', 'STAT001',
        TO_DATE('2025-02-01','YYYY-MM-DD'), TO_DATE('2025-02-10','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-004', 'SOP-RH-004', 'Procedure de gestion des dossiers du personnel', 'S004', '000006', 'STAT001',
        TO_DATE('2025-02-12','YYYY-MM-DD'), TO_DATE('2025-02-19','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-005', 'SOP-FIN-001', 'Procedure de traitement des factures', 'S005', '000007', 'STAT002',
        TO_DATE('2025-03-01','YYYY-MM-DD'), TO_DATE('2025-03-08','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-006', 'SOP-FIN-002', 'Procedure de suivi budgetaire', 'S006', '000008', 'STAT001',
        TO_DATE('2025-03-10','YYYY-MM-DD'), TO_DATE('2025-03-18','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-007', 'SOP-IT-001', 'Procedure de gestion des incidents informatiques', 'S010', '000011', 'STAT001',
        TO_DATE('2025-04-02','YYYY-MM-DD'), TO_DATE('2025-04-11','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-008', 'SOP-JUR-001', 'Procedure de gestion des contrats et marches publics', 'S012', '000015', 'STAT001',
        TO_DATE('2025-04-15','YYYY-MM-DD'), TO_DATE('2025-04-23','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-009', 'SOP-OPS-001', 'Procedure de gestion des stocks et maintenance', 'S016', '000017', 'STAT001',
        TO_DATE('2025-05-03','YYYY-MM-DD'), TO_DATE('2025-05-15','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-010', 'SOP-COM-001', 'Procedure de gestion des campagnes marketing', 'S018', '000020', 'STAT002',
        TO_DATE('2025-05-20','YYYY-MM-DD'), TO_DATE('2025-06-02','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-011', 'SOP-COM-002', 'Procedure de gestion des campagnes diretion', 'S018', '000020', 'STAT002',
        TO_DATE('2025-05-20','YYYY-MM-DD'), TO_DATE('2025-06-02','YYYY-MM-DD'));        




/*autre donne sop */
INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-012', 'SOP-RH-005', 'Procedure de suivi des absences', 'S004', '000006', 'STAT001',
        TO_DATE('2025-06-05','YYYY-MM-DD'), TO_DATE('2025-06-09','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-013', 'SOP-RH-006', 'Procedure de gestion des conges', 'S004', '000006', 'STAT001',
        TO_DATE('2025-06-10','YYYY-MM-DD'), TO_DATE('2025-06-13','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-014', 'SOP-FIN-003', 'Procedure de rapprochement bancaire', 'S005', '000007', 'STAT001',
        TO_DATE('2025-06-15','YYYY-MM-DD'), TO_DATE('2025-06-18','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-015', 'SOP-FIN-004', 'Procedure de validation des depenses', 'S006', '000008', 'STAT001',
        TO_DATE('2025-06-20','YYYY-MM-DD'), TO_DATE('2025-06-24','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-016', 'SOP-IT-002', 'Procedure de sauvegarde des donnees', 'S009', '000012', 'STAT001',
        TO_DATE('2025-06-25','YYYY-MM-DD'), TO_DATE('2025-06-29','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-017', 'SOP-IT-003', 'Procedure de gestion des acces utilisateurs', 'S008', '000011', 'STAT001',
        TO_DATE('2025-07-01','YYYY-MM-DD'), TO_DATE('2025-07-04','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-018', 'SOP-JUR-002', 'Procedure de validation des contrats', 'S012', '000015', 'STAT001',
        TO_DATE('2025-07-05','YYYY-MM-DD'), TO_DATE('2025-07-09','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-019', 'SOP-JUR-003', 'Procedure de traitement des litiges', 'S011', '000014', 'STAT002',
        TO_DATE('2025-07-10','YYYY-MM-DD'), TO_DATE('2025-07-15','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-020', 'SOP-OPS-002', 'Procedure de controle des stocks', 'S015', '000016', 'STAT001',
        TO_DATE('2025-07-16','YYYY-MM-DD'), TO_DATE('2025-07-20','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-021', 'SOP-OPS-003', 'Procedure de maintenance preventive', 'S016', '000017', 'STAT001',
        TO_DATE('2025-07-21','YYYY-MM-DD'), TO_DATE('2025-07-25','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-022', 'SOP-COM-003', 'Procedure de traitement des commandes clients', 'S017', '000019', 'STAT001',
        TO_DATE('2025-07-26','YYYY-MM-DD'), TO_DATE('2025-07-30','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-023', 'SOP-COM-004', 'Procedure de suivi des reclamations clients', 'S017', '000019', 'STAT001',
        TO_DATE('2025-08-01','YYYY-MM-DD'), TO_DATE('2025-08-04','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-024', 'SOP-MKT-001', 'Procedure de planification des campagnes marketing', 'S018', '000020', 'STAT001',
        TO_DATE('2025-08-05','YYYY-MM-DD'), TO_DATE('2025-08-09','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-025', 'SOP-MKT-002', 'Procedure de publication sur les reseaux sociaux', 'S018', '000020', 'STAT001',
        TO_DATE('2025-08-10','YYYY-MM-DD'), TO_DATE('2025-08-13','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-026', 'SOP-COMM-001', 'Procedure de redaction et diffusion des communications', 'S014', '000002', 'STAT001',
        TO_DATE('2025-08-14','YYYY-MM-DD'), TO_DATE('2025-08-18','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-027', 'SOP-DG-001', 'Procedure de preparation des reunions de direction', 'S013', '000001', 'STAT001',
        TO_DATE('2025-08-19','YYYY-MM-DD'), TO_DATE('2025-08-22','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-028', 'SOP-RH-007', 'Procedure d''integration des nouveaux employes', 'S001', '000003', 'STAT001',
        TO_DATE('2025-08-23','YYYY-MM-DD'), TO_DATE('2025-08-27','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-029', 'SOP-FIN-005', 'Procedure de cloture comptable mensuelle', 'S005', '000007', 'STAT001',
        TO_DATE('2025-08-28','YYYY-MM-DD'), TO_DATE('2025-09-02','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-030', 'SOP-IT-004', 'Procedure de resolution des incidents reseau', 'S010', '000013', 'STAT001',
        TO_DATE('2025-09-03','YYYY-MM-DD'), TO_DATE('2025-09-07','YYYY-MM-DD'));

INSERT INTO SOP (id_sop, numero_sop, titre, code_service, auteur_matricule, code_statut, date_creation, date_modification)
VALUES ('SOP-031', 'SOP-OPS-004', 'Procedure de reception des livraisons', 'S015', '000016', 'STAT002',
        TO_DATE('2025-09-08','YYYY-MM-DD'), TO_DATE('2025-09-12','YYYY-MM-DD'));

COMMIT;







