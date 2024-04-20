-- public.merchant definition

-- Drop table

-- DROP TABLE public.merchant;

CREATE TABLE public.merchant (
	id uuid NOT NULL,
	merchant_name varchar(255) NULL,
	merchant_location varchar(255) NULL,
	"open" bool NULL,
	CONSTRAINT merchant_pkey PRIMARY KEY (id)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id uuid NOT NULL,
	username varchar(255) NULL,
	email_address varchar(255) NULL,
	"password" varchar(255) NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE TABLE public.orders (
	id uuid NOT NULL,
	order_time timestamp NULL,
	destination_address varchar(255) NULL,
	user_id uuid NULL,
	completed bool NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (id),
	CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);


-- public.product definition

-- Drop table

-- DROP TABLE public.product;

CREATE TABLE public.product (
	id uuid NOT NULL,
	product_name varchar(255) NULL,
	price numeric NULL,
	merchant_id uuid NULL,
	CONSTRAINT product_pkey PRIMARY KEY (id),
	CONSTRAINT product_merchant_id_fkey FOREIGN KEY (merchant_id) REFERENCES public.merchant(id)
);


-- public.order_detail definition

-- Drop table

-- DROP TABLE public.order_detail;

CREATE TABLE public.order_detail (
	id uuid NOT NULL,
	order_id uuid NULL,
	product_id uuid NULL,
	quantity int4 NULL,
	CONSTRAINT order_detail_pkey PRIMARY KEY (id),
	CONSTRAINT order_detail_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id),
	CONSTRAINT order_detail_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product(id)
);