--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

-- Started on 2024-05-17 22:20:30

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE binarfud_hamid;
--
-- TOC entry 4918 (class 1262 OID 90258)
-- Name: binarfud_hamid; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE binarfud_hamid WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';


\connect binarfud_hamid

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 257 (class 1255 OID 90260)
-- Name: add_user(character varying, character varying, character varying); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.add_user(IN usrname character varying, IN mail character varying, IN passwd character varying)
    LANGUAGE plpgsql
    AS $$
DECLARE
    hashed_password TEXT;
BEGIN
    hashed_password := crypt(passwd, gen_salt('bf'));
    INSERT INTO users (id, username, email, password, created_at)
    VALUES (gen_random_uuid(), usrname, mail, hashed_password, NOW());
END;
$$;


--
-- TOC entry 263 (class 1255 OID 90354)
-- Name: delete_user_by_id(uuid); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.delete_user_by_id(IN usr_id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM users 
    WHERE id = usr_id;

    IF FOUND THEN
        RAISE NOTICE 'User with ID % deleted successfully', usr_id;
    ELSE
        RAISE NOTICE 'User with ID % not found', usr_id;
    END IF;
END;
$$;


--
-- TOC entry 270 (class 1255 OID 163902)
-- Name: transfer_balance(uuid, uuid, double precision); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.transfer_balance(IN sender_id uuid, IN receiver_id uuid, IN amount double precision)
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE users SET balance = balance - amount, updated_at = NOW() WHERE id = sender_id;
    UPDATE users SET balance = balance + amount, updated_at = NOW() WHERE id = receiver_id;
END;
$$;


--
-- TOC entry 258 (class 1255 OID 90348)
-- Name: update_user(uuid, character varying, character varying, character varying); Type: PROCEDURE; Schema: public; Owner: -
--

CREATE PROCEDURE public.update_user(IN usr_id uuid, IN usrname character varying, IN mail character varying, IN passwd character varying)
    LANGUAGE plpgsql
    AS $$
DECLARE
    hashed_password TEXT;
BEGIN
    IF passwd IS NOT NULL THEN
        hashed_password := crypt(passwd, gen_salt('bf'));
        UPDATE users 
        SET username = usrname,
            email = mail,
            password = hashed_password,
            updated_at = NOW()
        WHERE id = usr_id;
    ELSE
        UPDATE users 
        SET username = usrname,
            email = mail,
            updated_at = NOW()
        WHERE id = usr_id;
    END IF;
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 163840)
-- Name: merchants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.merchants (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    open boolean NOT NULL,
    location character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    user_id uuid NOT NULL
);


--
-- TOC entry 217 (class 1259 OID 163847)
-- Name: order_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.order_details (
    id uuid NOT NULL,
    order_id uuid NOT NULL,
    price double precision NOT NULL,
    product_id uuid NOT NULL,
    product_name character varying(255) NOT NULL,
    quantity integer NOT NULL
);


--
-- TOC entry 218 (class 1259 OID 163852)
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    address character varying(255) NOT NULL,
    payment_method character varying(255) NOT NULL,
    status character varying(255),
    user_id uuid,
    CONSTRAINT orders_payment_method_check CHECK (((payment_method)::text = ANY ((ARRAY['BALANCE'::character varying, 'TRANSFER'::character varying])::text[]))),
    CONSTRAINT orders_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'PROCESS'::character varying, 'SUCCESS'::character varying, 'CANCELLED'::character varying, 'EXPIRED'::character varying])::text[])))
);


--
-- TOC entry 219 (class 1259 OID 163861)
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    merchant_id uuid NOT NULL,
    name character varying(255),
    price double precision NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 163866)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    balance double precision NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


--
-- TOC entry 4908 (class 0 OID 163840)
-- Dependencies: 216
-- Data for Name: merchants; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.merchants VALUES ('0996be8b-1f58-41b9-bdb5-73cfdf6522aa', '2024-05-17 21:47:41.878', NULL, '2024-05-17 21:48:13.846', false, 'Jalan unknown', 'Warung ABCE', '1d77a128-a1eb-4e90-acff-1da29c050cb8');


--
-- TOC entry 4909 (class 0 OID 163847)
-- Dependencies: 217
-- Data for Name: order_details; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.order_details VALUES ('78ffe32b-b9d4-4605-aa78-de65313f5c5e', 'a4b8ab72-f8a5-49bd-9729-6595f6503664', 20000, '0dd3d37e-f1cd-4198-afaa-91de31b04ee4', 'Bakso Goreng', 2);


--
-- TOC entry 4910 (class 0 OID 163852)
-- Dependencies: 218
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.orders VALUES ('a4b8ab72-f8a5-49bd-9729-6595f6503664', '2024-05-17 22:19:36.031', NULL, '2024-05-17 22:19:36.031', 'Jalan Putar Arah', 'BALANCE', 'PENDING', 'c5a3a9d1-ab30-4640-9e24-608c55f04030');


--
-- TOC entry 4911 (class 0 OID 163861)
-- Dependencies: 219
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.products VALUES ('0dd3d37e-f1cd-4198-afaa-91de31b04ee4', '2024-05-17 22:16:29.466', NULL, '2024-05-17 22:16:29.467', '0996be8b-1f58-41b9-bdb5-73cfdf6522aa', 'Bakso Goreng', 20000);


--
-- TOC entry 4912 (class 0 OID 163866)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES ('d6d6fab3-2678-42d6-969a-8407fc372ceb', '2024-05-17 21:46:32.528', NULL, '2024-05-17 21:46:32.537', 0, 'test@test.com', '$2a$10$3TKe8HnLi4LRnJouZR1wKeUL5WjVygoySb1AjhbCXtMUzdKTMYazO', 'test');
INSERT INTO public.users VALUES ('c5a3a9d1-ab30-4640-9e24-608c55f04030', '2024-05-17 21:47:05.139', NULL, '2024-05-17 22:19:36.02232', 960000, 'test2@test.com', '$2a$10$Vl9apGXWm89H81tKM5SkruzxRfLf2euiORHsqirgdBPJPt1Mq9kky', 'test2');
INSERT INTO public.users VALUES ('1d77a128-a1eb-4e90-acff-1da29c050cb8', '2024-05-17 21:47:13.954', NULL, '2024-05-17 22:19:36.02232', 40000, 'test3@test.com', '$2a$10$kqqp2QeZo58lPxcfhhwBiei0LFd/KbILXqZUKDh6iqT.yPw5F8dXG', 'test3');


--
-- TOC entry 4747 (class 2606 OID 163846)
-- Name: merchants merchants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.merchants
    ADD CONSTRAINT merchants_pkey PRIMARY KEY (id);


--
-- TOC entry 4749 (class 2606 OID 163851)
-- Name: order_details order_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT order_details_pkey PRIMARY KEY (id);


--
-- TOC entry 4751 (class 2606 OID 163860)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 4753 (class 2606 OID 163865)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 4755 (class 2606 OID 163874)
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- TOC entry 4757 (class 2606 OID 163876)
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 4759 (class 2606 OID 163872)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4763 (class 2606 OID 163892)
-- Name: orders fk32ql8ubntj5uh44ph9659tiih; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4761 (class 2606 OID 163887)
-- Name: order_details fk4q98utpd73imf4yhttm3w0eax; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT fk4q98utpd73imf4yhttm3w0eax FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 4760 (class 2606 OID 163877)
-- Name: merchants fka759srj6ts95j9qh089b6gbei; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.merchants
    ADD CONSTRAINT fka759srj6ts95j9qh089b6gbei FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4762 (class 2606 OID 163882)
-- Name: order_details fkjyu2qbqt8gnvno9oe9j2s2ldk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT fkjyu2qbqt8gnvno9oe9j2s2ldk FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 4764 (class 2606 OID 163897)
-- Name: products fkt1yvv81v320ba41fq28k7had2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fkt1yvv81v320ba41fq28k7had2 FOREIGN KEY (merchant_id) REFERENCES public.merchants(id);


-- Completed on 2024-05-17 22:20:30

--
-- PostgreSQL database dump complete
--

