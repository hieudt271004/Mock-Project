CREATE TYPE public."e_gender" AS ENUM (
	'MALE',
	'FEMALE',
	'OTHER');

CREATE TYPE public."e_user_status" AS ENUM (
	'ACTIVE',
	'INACTIVE',
	'NONE');

CREATE TABLE my_user (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(50),
    gender public."e_gender",
    bod DATE,
    phone VARCHAR(50),
    email VARCHAR(100),
    created_at DATE NOT NULL,
    updated_at DATE,
    status public."e_user_status" NOT NULL
);

