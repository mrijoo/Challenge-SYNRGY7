--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

-- Started on 2024-05-03 23:05:28

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
-- TOC entry 4915 (class 1262 OID 90258)
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
-- TOC entry 4916 (class 0 OID 0)
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
-- TOC entry 216 (class 1259 OID 106496)
-- Name: merchants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.merchants (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    open boolean,
    location character varying(255),
    name character varying(255),
    user_id uuid
);


--
-- TOC entry 217 (class 1259 OID 106503)
-- Name: order_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.order_details (
    id uuid NOT NULL,
    order_id uuid,
    product_id uuid,
    quantity integer NOT NULL,
    total_price double precision
);


--
-- TOC entry 218 (class 1259 OID 106508)
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    address character varying(255),
    payment_method bytea,
    status bytea,
    user_id uuid
);


--
-- TOC entry 219 (class 1259 OID 106515)
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    merchant_id uuid,
    name character varying(255),
    price double precision NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 106520)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


--
-- TOC entry 4905 (class 0 OID 106496)
-- Dependencies: 216
-- Data for Name: merchants; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.merchants VALUES ('62e5fd22-2730-44e3-99f6-88f944da6ada', '2024-05-03 22:14:51.413', NULL, '2024-05-03 22:14:51.422', true, 'Jalan unknown', 'Warung ABC', '070eed85-07b9-4bcd-8288-89dc507c7f46');
INSERT INTO public.merchants VALUES ('5269f04b-05fa-40d7-8d41-985d1735908e', '2024-05-03 22:55:05.881712', NULL, NULL, true, 'Yogyakarta, Indonesia', 'Book Emporium', '6259036e-77b5-4efd-91c3-98b7cfb73640');
INSERT INTO public.merchants VALUES ('854c72ff-c6e6-4528-96d4-4f60f78a3b86', '2024-05-03 22:55:05.881712', NULL, NULL, false, 'Surabaya, Indonesia', 'Gadget Hub', '45cd8461-596c-47e0-91db-f6aa67c1ca5a');
INSERT INTO public.merchants VALUES ('c9d0b23f-9c41-42db-adb2-515bcbd462c6', '2024-05-03 22:55:05.881712', NULL, NULL, false, 'Bali, Indonesia', 'Sports Arena', '9d913170-817e-40cc-895e-bda2554d1edc');
INSERT INTO public.merchants VALUES ('d313fbb3-e7b1-4127-89f2-88717d71e52c', '2024-05-03 22:55:05.881712', NULL, NULL, false, 'Jakarta, Indonesia', 'Coffee Corner', '5a23758f-ee8c-400f-a548-d85ae1a82387');
INSERT INTO public.merchants VALUES ('89c044b0-a490-4f49-b52c-fb4ef4b4a4b9', '2024-05-03 22:55:05.881712', NULL, NULL, false, 'Bandung, Indonesia', 'Fashion Zone', '1382b252-dd6a-427d-a06e-6af07df4f67c');


--
-- TOC entry 4906 (class 0 OID 106503)
-- Dependencies: 217
-- Data for Name: order_details; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.order_details VALUES ('8ea124f1-1aec-4694-9f14-ded934b32912', 'ee1017ba-ec65-4dc0-af53-4fef4f125d85', '31acec91-707d-4faa-b83a-5bcf76d51c55', 2, 50000);
INSERT INTO public.order_details VALUES ('608c65e3-9754-4b06-a84d-1221d498b2ab', 'c9db6f8c-b7cf-44bb-86f5-5d3a9360a6d6', 'da9a935b-a137-41cd-8af4-ce83a5fcec74', 1, 150000);
INSERT INTO public.order_details VALUES ('25c5c9a0-a38c-4a0d-8254-173d222ed05f', 'be2ca6bd-7d67-451e-aea0-d11366828977', 'bdc82751-1645-433b-9681-43d2ef0ed3b8', 1, 5000000);
INSERT INTO public.order_details VALUES ('971ceb29-1a42-4bab-a38c-ba63b11a01fe', '669a5b1f-823f-4c00-818e-bced0e300be7', 'f47be7e6-19c4-43df-9948-c4bc6ea12f31', 2, 1000000);
INSERT INTO public.order_details VALUES ('c353445c-555a-40ab-8c00-fd0fc98ca628', 'bc4b8993-a417-4e87-91c1-d1a0a89fc197', 'd4d1f0d0-4fee-4044-812e-18df9c004acd', 3, 450000);
INSERT INTO public.order_details VALUES ('20221d7c-727e-4331-8fe9-dd6d696918a8', 'ee1017ba-ec65-4dc0-af53-4fef4f125d85', '31acec91-707d-4faa-b83a-5bcf76d51c55', 2, 50000);
INSERT INTO public.order_details VALUES ('7b3114ed-c522-4175-8c57-994233b6a472', 'c9db6f8c-b7cf-44bb-86f5-5d3a9360a6d6', 'da9a935b-a137-41cd-8af4-ce83a5fcec74', 1, 150000);
INSERT INTO public.order_details VALUES ('e35c7933-1617-48d2-9f09-8e964f0c6027', 'be2ca6bd-7d67-451e-aea0-d11366828977', 'bdc82751-1645-433b-9681-43d2ef0ed3b8', 1, 5000000);
INSERT INTO public.order_details VALUES ('36c2a4df-06ef-4eeb-981d-ad33fade7a8b', '669a5b1f-823f-4c00-818e-bced0e300be7', 'f47be7e6-19c4-43df-9948-c4bc6ea12f31', 2, 1000000);
INSERT INTO public.order_details VALUES ('da556f85-be55-42fc-9d88-14f8e990c90c', 'bc4b8993-a417-4e87-91c1-d1a0a89fc197', 'd4d1f0d0-4fee-4044-812e-18df9c004acd', 3, 450000);


--
-- TOC entry 4907 (class 0 OID 106508)
-- Dependencies: 218
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.orders VALUES ('ee1017ba-ec65-4dc0-af53-4fef4f125d85', '2024-05-03 22:55:05.898317', NULL, NULL, 'Jl. Sudirman No. 123, Jakarta', '\x43415348', '\x50454e44494e47', '5a23758f-ee8c-400f-a548-d85ae1a82387');
INSERT INTO public.orders VALUES ('c9db6f8c-b7cf-44bb-86f5-5d3a9360a6d6', '2024-05-03 22:55:05.898317', NULL, NULL, 'Jl. Riau No. 45, Bandung', '\x5452414e53464552', '\x50454e44494e47', '1382b252-dd6a-427d-a06e-6af07df4f67c');
INSERT INTO public.orders VALUES ('be2ca6bd-7d67-451e-aea0-d11366828977', '2024-05-03 22:55:05.898317', NULL, NULL, 'Jl. Panglima Sudirman No. 78, Surabaya', '\x43415348', '\x50454e44494e47', '45cd8461-596c-47e0-91db-f6aa67c1ca5a');
INSERT INTO public.orders VALUES ('669a5b1f-823f-4c00-818e-bced0e300be7', '2024-05-03 22:55:05.898317', NULL, NULL, 'Jl. Malioboro No. 10, Yogyakarta', '\x5452414e53464552', '\x50454e44494e47', '6259036e-77b5-4efd-91c3-98b7cfb73640');
INSERT INTO public.orders VALUES ('bc4b8993-a417-4e87-91c1-d1a0a89fc197', '2024-05-03 22:55:05.898317', NULL, NULL, 'Jl. Sunset Road No. 15, Bali', '\x43415348', '\x50454e44494e47', '9d913170-817e-40cc-895e-bda2554d1edc');


--
-- TOC entry 4908 (class 0 OID 106515)
-- Dependencies: 219
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.products VALUES ('31acec91-707d-4faa-b83a-5bcf76d51c55', '2024-05-03 22:55:05.89228', NULL, NULL, 'd313fbb3-e7b1-4127-89f2-88717d71e52c', 'Espresso', 25000);
INSERT INTO public.products VALUES ('4f6faa5d-c263-4bd1-b6a1-668bfb625c5a', '2024-05-03 22:55:05.89228', NULL, NULL, 'd313fbb3-e7b1-4127-89f2-88717d71e52c', 'Latte', 30000);
INSERT INTO public.products VALUES ('da9a935b-a137-41cd-8af4-ce83a5fcec74', '2024-05-03 22:55:05.89228', NULL, NULL, '89c044b0-a490-4f49-b52c-fb4ef4b4a4b9', 'T-Shirt', 150000);
INSERT INTO public.products VALUES ('fcf3fdc8-39c2-4b18-a504-7b9d54168279', '2024-05-03 22:55:05.89228', NULL, NULL, '89c044b0-a490-4f49-b52c-fb4ef4b4a4b9', 'Jeans', 300000);
INSERT INTO public.products VALUES ('bdc82751-1645-433b-9681-43d2ef0ed3b8', '2024-05-03 22:55:05.89228', NULL, NULL, '854c72ff-c6e6-4528-96d4-4f60f78a3b86', 'Smartphone', 5000000);
INSERT INTO public.products VALUES ('256b3bff-78fb-4961-81fc-e5709a36a715', '2024-05-03 22:55:05.89228', NULL, NULL, '854c72ff-c6e6-4528-96d4-4f60f78a3b86', 'Headphones', 1000000);
INSERT INTO public.products VALUES ('e1b08448-9445-4226-8101-40cc43f4bdae', '2024-05-03 22:55:05.89228', NULL, NULL, '5269f04b-05fa-40d7-8d41-985d1735908e', 'Novel', 75000);
INSERT INTO public.products VALUES ('f47be7e6-19c4-43df-9948-c4bc6ea12f31', '2024-05-03 22:55:05.89228', NULL, NULL, '5269f04b-05fa-40d7-8d41-985d1735908e', 'Textbook', 500000);
INSERT INTO public.products VALUES ('d4d1f0d0-4fee-4044-812e-18df9c004acd', '2024-05-03 22:55:05.89228', NULL, NULL, 'c9d0b23f-9c41-42db-adb2-515bcbd462c6', 'Football', 150000);
INSERT INTO public.products VALUES ('ccc508bd-5c84-4e71-be0c-2e09f75ef3bb', '2024-05-03 22:55:05.89228', NULL, NULL, 'c9d0b23f-9c41-42db-adb2-515bcbd462c6', 'Tennis Racket', 500000);


--
-- TOC entry 4909 (class 0 OID 106520)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES ('070eed85-07b9-4bcd-8288-89dc507c7f46', '2024-05-03 21:40:45.329', NULL, '2024-05-03 21:40:45.343', 'test@test.com', '$2a$10$cU64QHYuy25h7fTEw00sXeJJdBRcxROLMAEaFGbfTQic2JnlXKzhG', 'test');
INSERT INTO public.users VALUES ('5a23758f-ee8c-400f-a548-d85ae1a82387', '2024-05-03 22:52:49.660948', NULL, NULL, 'john.doe@example.com', '$2a$06$ALQcDOHPjJs7CFCwo4s7MejqLzAzS.VzPbnx7XUJvDWJx4MyA1SkC', 'john_doe');
INSERT INTO public.users VALUES ('1382b252-dd6a-427d-a06e-6af07df4f67c', '2024-05-03 22:52:49.68146', NULL, NULL, 'jane.smith@example.com', '$2a$06$ZjRv/EmRNqDM.PceYErq3ey4eJL/4/q8gFuzW2dvEkcJWCRLor.NW', 'jane_smith');
INSERT INTO public.users VALUES ('45cd8461-596c-47e0-91db-f6aa67c1ca5a', '2024-05-03 22:52:49.686952', NULL, NULL, 'alex.wong@example.com', '$2a$06$0j/suzrH9S/TRxTnVHnwA.qxAp9/1eQMVMF1iCuFbKdEK2buHHiUS', 'alex_wong');
INSERT INTO public.users VALUES ('6259036e-77b5-4efd-91c3-98b7cfb73640', '2024-05-03 22:52:49.691958', NULL, NULL, 'emily.jones@example.com', '$2a$06$ov8.xA196xLtMArXR8PHSO7xt2qCH8qtd7gZN79kFMr.I1FQXMSIK', 'emily_jones');
INSERT INTO public.users VALUES ('9d913170-817e-40cc-895e-bda2554d1edc', '2024-05-03 22:52:49.697884', NULL, NULL, 'david.brown@example.com', '$2a$06$/u5MwQcwIhPsanu3Rz6Vvuinw0.52MRadk8vUEn7NkuRpo827glWC', 'david_brown');


--
-- TOC entry 4744 (class 2606 OID 106502)
-- Name: merchants merchants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.merchants
    ADD CONSTRAINT merchants_pkey PRIMARY KEY (id);


--
-- TOC entry 4746 (class 2606 OID 106507)
-- Name: order_details order_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT order_details_pkey PRIMARY KEY (id);


--
-- TOC entry 4748 (class 2606 OID 106514)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 4750 (class 2606 OID 106519)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 4752 (class 2606 OID 106528)
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- TOC entry 4754 (class 2606 OID 106530)
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 4756 (class 2606 OID 106526)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4760 (class 2606 OID 106546)
-- Name: orders fk32ql8ubntj5uh44ph9659tiih; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4758 (class 2606 OID 106541)
-- Name: order_details fk4q98utpd73imf4yhttm3w0eax; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT fk4q98utpd73imf4yhttm3w0eax FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 4757 (class 2606 OID 106531)
-- Name: merchants fka759srj6ts95j9qh089b6gbei; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.merchants
    ADD CONSTRAINT fka759srj6ts95j9qh089b6gbei FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4759 (class 2606 OID 106536)
-- Name: order_details fkjyu2qbqt8gnvno9oe9j2s2ldk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_details
    ADD CONSTRAINT fkjyu2qbqt8gnvno9oe9j2s2ldk FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 4761 (class 2606 OID 106551)
-- Name: products fkt1yvv81v320ba41fq28k7had2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fkt1yvv81v320ba41fq28k7had2 FOREIGN KEY (merchant_id) REFERENCES public.merchants(id);


-- Completed on 2024-05-03 23:05:29

--
-- PostgreSQL database dump complete
--

