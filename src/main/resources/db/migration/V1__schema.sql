create table url
(
    id           bigserial primary key,
    original_url text not null,
    short_url    text not null unique,
    created_at   timestamp without time zone not null set default now()
);

CREATE INDEX url_short_url_idx
    ON public.url USING btree
    (short_url ASC NULLS LAST);

ALTER TABLE IF EXISTS public.url
    ADD CONSTRAINT url_short_url_constraint UNIQUE (short_url);

ALTER TABLE IF EXISTS public.url
    ADD CONSTRAINT url_original_short_url_contraint UNIQUE (original_url, short_url);





