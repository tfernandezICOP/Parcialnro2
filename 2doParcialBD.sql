CREATE TABLE IF NOT EXISTS public.mesa
(
    id integer NOT NULL DEFAULT nextval('mesa_id_seq'::regclass),
    capacidad integer,
    consumo integer,
    estado character varying(50) COLLATE pg_catalog."default",
    idresto integer,
    nro_mesa integer NOT NULL DEFAULT nextval('mesa_nro_mesa_seq'::regclass),
    CONSTRAINT mesa_pkey PRIMARY KEY (id),
    CONSTRAINT mesa_nro_mesa_key UNIQUE (nro_mesa),
    CONSTRAINT mesa_idresto_fkey FOREIGN KEY (idresto)
        REFERENCES public.resto (idresto) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.mesa
    OWNER to postgres;

-- Table: public.reserva

-- DROP TABLE IF EXISTS public.reserva;

CREATE TABLE IF NOT EXISTS public.reserva
(
    id integer NOT NULL DEFAULT nextval('reserva_id_seq'::regclass),
    fecha date,
    nombreapellido character varying(50) COLLATE pg_catalog."default",
    comensales integer,
    id_mesa integer,
    CONSTRAINT reserva_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.reserva
    OWNER to postgres;


CREATE TABLE IF NOT EXISTS public.resto
(
    idresto integer NOT NULL DEFAULT nextval('resto_idresto_seq'::regclass),
    nombre character varying(50) COLLATE pg_catalog."default",
    domicilio character varying(100) COLLATE pg_catalog."default",
    localidad character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT resto_pkey PRIMARY KEY (idresto)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.resto
    OWNER to postgres;