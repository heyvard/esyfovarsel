CREATE INDEX syketilfelle_id_index ON SYKETILFELLEBIT(id);
CREATE INDEX syketilfelle_fnr_index ON SYKETILFELLEBIT(fnr);
CREATE INDEX syketilfelle_orgnummer_index ON SYKETILFELLEBIT(orgnummer);
CREATE INDEX syketilfelle_opprettet_index ON SYKETILFELLEBIT(opprettet);
CREATE INDEX syketilfelle_opprettet_opprinnelig_index ON SYKETILFELLEBIT(opprettet_opprinnelig);
CREATE INDEX syketilfelle_inntruffet_index ON SYKETILFELLEBIT(inntruffet);
CREATE INDEX syketilfelle_tags_index ON SYKETILFELLEBIT(tags);
CREATE INDEX syketilfelle_ressurs_id_index ON SYKETILFELLEBIT(ressurs_id);
CREATE INDEX syketilfelle_fom_index ON SYKETILFELLEBIT(fom);
CREATE INDEX syketilfelle_tom_index ON SYKETILFELLEBIT(tom);
CREATE INDEX syketilfelle_korrigert_soknad_index ON SYKETILFELLEBIT(korrigert_soknad);