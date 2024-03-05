delete from pautas_associados;
delete from pautas;
ALTER TABLE pautas ALTER COLUMN ID RESTART WITH 1;
delete from associados;
ALTER TABLE associados ALTER COLUMN ID RESTART WITH 1;
delete from sessoes;
ALTER TABLE sessoes ALTER COLUMN ID RESTART WITH 1;
