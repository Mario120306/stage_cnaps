-- Migration a executer une seule fois sur le schema DEV_DEPL.
-- Necessaire si SOP_EXPORT a deja ete creee avant l'ajout du stockage PDF.
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_count
      FROM USER_TAB_COLUMNS
     WHERE TABLE_NAME = 'SOP_EXPORT'
       AND COLUMN_NAME = 'CONTENU_PDF';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE SOP_EXPORT ADD (contenu_pdf BLOB)';
    END IF;
END;
/
