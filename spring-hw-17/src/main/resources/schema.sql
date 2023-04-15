DROP TABLE IF EXISTS acl_entry;
DROP TABLE IF EXISTS acl_object_identity;
DROP TABLE IF EXISTS acl_class;
DROP TABLE IF EXISTS acl_sid;

CREATE TABLE IF NOT EXISTS acl_sid (
    id bigserial NOT NULL,
    principal boolean NOT NULL,
    sid varchar(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (sid,principal)
    );

CREATE TABLE IF NOT EXISTS acl_class (
  id bigserial NOT NULL,
  class varchar(255) NOT NULL,
  class_id_type varchar(100),
  PRIMARY KEY (id),
  UNIQUE (class)
);

CREATE TABLE IF NOT EXISTS acl_entry (
  id bigserial NOT NULL,
  acl_object_identity bigint NOT NULL,
  ace_order int NOT NULL,
  sid bigint NOT NULL,
  mask int NOT NULL,
  granting boolean NOT NULL,
  audit_success boolean NOT NULL,
  audit_failure boolean NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (acl_object_identity,ace_order)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id bigserial NOT NULL,
  object_id_class bigint NOT NULL,
  object_id_identity varchar(100) NOT NULL,
  parent_object bigint DEFAULT NULL,
  owner_sid bigint DEFAULT NULL,
  entries_inheriting boolean NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (object_id_class,object_id_identity)
);
 
ALTER TABLE acl_entry
ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);