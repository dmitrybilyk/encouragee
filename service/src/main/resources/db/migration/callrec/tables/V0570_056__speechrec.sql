create table if not exists callrec.speechrec (
  cplid integer not null,
  worker varchar(255),
  "index" smallint,
  search smallint,
  error text,
    CONSTRAINT _speechrec_cplid FOREIGN KEY (cplid)
        REFERENCES callrec.couples (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
  );

drop index if exists _speechrec_worker_indexed;
drop index if exists _speechrec_worker_searched;
drop index if exists _speechrec_cplid;

create index _speechrec_worker_indexed on callrec.speechrec (worker, "index");
create index _speechrec_worker_searched on callrec.speechrec (worker, "search");
create index _speechrec_cplid on callrec.speechrec (cplid);

grant all on callrec.speechrec to callrec;
