PGDMP                         |            vet    14.12    14.12 >    E           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            F           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            G           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            H           1262    17747    vet    DATABASE     N   CREATE DATABASE vet WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'C';
    DROP DATABASE vet;
                postgres    false            �            1259    17968    animal_vaccine    TABLE     f   CREATE TABLE public.animal_vaccine (
    animal_id bigint NOT NULL,
    vaccine_id bigint NOT NULL
);
 "   DROP TABLE public.animal_vaccine;
       public         heap    postgres    false            �            1259    17959    animals    TABLE       CREATE TABLE public.animals (
    id bigint NOT NULL,
    animal_breed character varying(255) NOT NULL,
    animal_colour character varying(255) NOT NULL,
    animal_date_of_birth date NOT NULL,
    animal_gender character varying(255) NOT NULL,
    animal_name character varying(255) NOT NULL,
    animal_species character varying(255) NOT NULL,
    customer_id bigint,
    CONSTRAINT animals_animal_gender_check CHECK (((animal_gender)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying])::text[])))
);
    DROP TABLE public.animals;
       public         heap    postgres    false            �            1259    17958    animals_id_seq    SEQUENCE     w   CREATE SEQUENCE public.animals_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.animals_id_seq;
       public          postgres    false    210            I           0    0    animals_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.animals_id_seq OWNED BY public.animals.id;
          public          postgres    false    209            �            1259    17972    appointments    TABLE     �   CREATE TABLE public.appointments (
    id bigint NOT NULL,
    appointment_date timestamp(6) without time zone NOT NULL,
    animal_id bigint,
    customer_id bigint,
    doctor_id bigint
);
     DROP TABLE public.appointments;
       public         heap    postgres    false            �            1259    17971    appointments_id_seq    SEQUENCE     |   CREATE SEQUENCE public.appointments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.appointments_id_seq;
       public          postgres    false    213            J           0    0    appointments_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.appointments_id_seq OWNED BY public.appointments.id;
          public          postgres    false    212            �            1259    17979    available_dates    TABLE     b   CREATE TABLE public.available_dates (
    id bigint NOT NULL,
    available_date date NOT NULL
);
 #   DROP TABLE public.available_dates;
       public         heap    postgres    false            �            1259    17978    available_dates_id_seq    SEQUENCE        CREATE SEQUENCE public.available_dates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.available_dates_id_seq;
       public          postgres    false    215            K           0    0    available_dates_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.available_dates_id_seq OWNED BY public.available_dates.id;
          public          postgres    false    214            �            1259    17986 	   customers    TABLE     =  CREATE TABLE public.customers (
    id bigint NOT NULL,
    customer_address character varying(255) NOT NULL,
    customer_city character varying(255) NOT NULL,
    customer_mail character varying(255) NOT NULL,
    customer_name character varying(255) NOT NULL,
    customer_phone character varying(255) NOT NULL
);
    DROP TABLE public.customers;
       public         heap    postgres    false            �            1259    17985    customers_id_seq    SEQUENCE     y   CREATE SEQUENCE public.customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.customers_id_seq;
       public          postgres    false    217            L           0    0    customers_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.customers_id_seq OWNED BY public.customers.id;
          public          postgres    false    216            �            1259    17994    doctor_available_date    TABLE     t   CREATE TABLE public.doctor_available_date (
    doctor_id bigint NOT NULL,
    available_date_id bigint NOT NULL
);
 )   DROP TABLE public.doctor_available_date;
       public         heap    postgres    false            �            1259    17998    doctors    TABLE     1  CREATE TABLE public.doctors (
    id bigint NOT NULL,
    doctor_address character varying(255) NOT NULL,
    doctor_city character varying(255) NOT NULL,
    doctor_mail character varying(255) NOT NULL,
    doctor_name character varying(255) NOT NULL,
    doctor_phone character varying(255) NOT NULL
);
    DROP TABLE public.doctors;
       public         heap    postgres    false            �            1259    17997    doctors_id_seq    SEQUENCE     w   CREATE SEQUENCE public.doctors_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.doctors_id_seq;
       public          postgres    false    220            M           0    0    doctors_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.doctors_id_seq OWNED BY public.doctors.id;
          public          postgres    false    219            �            1259    18007    vaccines    TABLE        CREATE TABLE public.vaccines (
    id bigint NOT NULL,
    vaccine_code character varying(255) NOT NULL,
    vaccine_name character varying(255) NOT NULL,
    vaccine_protection_finish_date date NOT NULL,
    vaccine_protection_start_date date NOT NULL
);
    DROP TABLE public.vaccines;
       public         heap    postgres    false            �            1259    18006    vaccines_id_seq    SEQUENCE     x   CREATE SEQUENCE public.vaccines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.vaccines_id_seq;
       public          postgres    false    222            N           0    0    vaccines_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.vaccines_id_seq OWNED BY public.vaccines.id;
          public          postgres    false    221            �           2604    17962 
   animals id    DEFAULT     h   ALTER TABLE ONLY public.animals ALTER COLUMN id SET DEFAULT nextval('public.animals_id_seq'::regclass);
 9   ALTER TABLE public.animals ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    209    210    210            �           2604    17975    appointments id    DEFAULT     r   ALTER TABLE ONLY public.appointments ALTER COLUMN id SET DEFAULT nextval('public.appointments_id_seq'::regclass);
 >   ALTER TABLE public.appointments ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    213    212    213            �           2604    17982    available_dates id    DEFAULT     x   ALTER TABLE ONLY public.available_dates ALTER COLUMN id SET DEFAULT nextval('public.available_dates_id_seq'::regclass);
 A   ALTER TABLE public.available_dates ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    215    214    215            �           2604    17989    customers id    DEFAULT     l   ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.customers_id_seq'::regclass);
 ;   ALTER TABLE public.customers ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    217    217            �           2604    18001 
   doctors id    DEFAULT     h   ALTER TABLE ONLY public.doctors ALTER COLUMN id SET DEFAULT nextval('public.doctors_id_seq'::regclass);
 9   ALTER TABLE public.doctors ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    220    219    220            �           2604    18010    vaccines id    DEFAULT     j   ALTER TABLE ONLY public.vaccines ALTER COLUMN id SET DEFAULT nextval('public.vaccines_id_seq'::regclass);
 :   ALTER TABLE public.vaccines ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    221    222    222            7          0    17968    animal_vaccine 
   TABLE DATA           ?   COPY public.animal_vaccine (animal_id, vaccine_id) FROM stdin;
    public          postgres    false    211   �J       6          0    17959    animals 
   TABLE DATA           �   COPY public.animals (id, animal_breed, animal_colour, animal_date_of_birth, animal_gender, animal_name, animal_species, customer_id) FROM stdin;
    public          postgres    false    210   K       9          0    17972    appointments 
   TABLE DATA           _   COPY public.appointments (id, appointment_date, animal_id, customer_id, doctor_id) FROM stdin;
    public          postgres    false    213   �K       ;          0    17979    available_dates 
   TABLE DATA           =   COPY public.available_dates (id, available_date) FROM stdin;
    public          postgres    false    215   jL       =          0    17986 	   customers 
   TABLE DATA           v   COPY public.customers (id, customer_address, customer_city, customer_mail, customer_name, customer_phone) FROM stdin;
    public          postgres    false    217   �L       >          0    17994    doctor_available_date 
   TABLE DATA           M   COPY public.doctor_available_date (doctor_id, available_date_id) FROM stdin;
    public          postgres    false    218   ]M       @          0    17998    doctors 
   TABLE DATA           j   COPY public.doctors (id, doctor_address, doctor_city, doctor_mail, doctor_name, doctor_phone) FROM stdin;
    public          postgres    false    220   �M       B          0    18007    vaccines 
   TABLE DATA           �   COPY public.vaccines (id, vaccine_code, vaccine_name, vaccine_protection_finish_date, vaccine_protection_start_date) FROM stdin;
    public          postgres    false    222   N       O           0    0    animals_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.animals_id_seq', 19, true);
          public          postgres    false    209            P           0    0    appointments_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.appointments_id_seq', 14, true);
          public          postgres    false    212            Q           0    0    available_dates_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.available_dates_id_seq', 27, true);
          public          postgres    false    214            R           0    0    customers_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.customers_id_seq', 10, true);
          public          postgres    false    216            S           0    0    doctors_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.doctors_id_seq', 28, true);
          public          postgres    false    219            T           0    0    vaccines_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.vaccines_id_seq', 33, true);
          public          postgres    false    221            �           2606    17967    animals animals_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.animals
    ADD CONSTRAINT animals_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.animals DROP CONSTRAINT animals_pkey;
       public            postgres    false    210            �           2606    17977    appointments appointments_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT appointments_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.appointments DROP CONSTRAINT appointments_pkey;
       public            postgres    false    213            �           2606    17984 $   available_dates available_dates_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.available_dates
    ADD CONSTRAINT available_dates_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.available_dates DROP CONSTRAINT available_dates_pkey;
       public            postgres    false    215            �           2606    17993    customers customers_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.customers DROP CONSTRAINT customers_pkey;
       public            postgres    false    217            �           2606    18005    doctors doctors_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.doctors
    ADD CONSTRAINT doctors_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.doctors DROP CONSTRAINT doctors_pkey;
       public            postgres    false    220            �           2606    18016 &   customers uk_5vhox5jsqitujs1k7bcsb2rj8 
   CONSTRAINT     j   ALTER TABLE ONLY public.customers
    ADD CONSTRAINT uk_5vhox5jsqitujs1k7bcsb2rj8 UNIQUE (customer_mail);
 P   ALTER TABLE ONLY public.customers DROP CONSTRAINT uk_5vhox5jsqitujs1k7bcsb2rj8;
       public            postgres    false    217            �           2606    18022 $   doctors uk_7s4l7559eox2hor73b3qp3vq2 
   CONSTRAINT     g   ALTER TABLE ONLY public.doctors
    ADD CONSTRAINT uk_7s4l7559eox2hor73b3qp3vq2 UNIQUE (doctor_phone);
 N   ALTER TABLE ONLY public.doctors DROP CONSTRAINT uk_7s4l7559eox2hor73b3qp3vq2;
       public            postgres    false    220            �           2606    18020 $   doctors uk_amsyrdrr2f0d48ciy29o9hvjm 
   CONSTRAINT     f   ALTER TABLE ONLY public.doctors
    ADD CONSTRAINT uk_amsyrdrr2f0d48ciy29o9hvjm UNIQUE (doctor_mail);
 N   ALTER TABLE ONLY public.doctors DROP CONSTRAINT uk_amsyrdrr2f0d48ciy29o9hvjm;
       public            postgres    false    220            �           2606    18018 &   customers uk_bn3wq4vhuqco545y52xp96gqd 
   CONSTRAINT     k   ALTER TABLE ONLY public.customers
    ADD CONSTRAINT uk_bn3wq4vhuqco545y52xp96gqd UNIQUE (customer_phone);
 P   ALTER TABLE ONLY public.customers DROP CONSTRAINT uk_bn3wq4vhuqco545y52xp96gqd;
       public            postgres    false    217            �           2606    18014    vaccines vaccines_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.vaccines
    ADD CONSTRAINT vaccines_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.vaccines DROP CONSTRAINT vaccines_pkey;
       public            postgres    false    222            �           2606    18038 (   appointments fk95vepu86o8syqtux9gkr71bhy    FK CONSTRAINT     �   ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT fk95vepu86o8syqtux9gkr71bhy FOREIGN KEY (animal_id) REFERENCES public.animals(id);
 R   ALTER TABLE ONLY public.appointments DROP CONSTRAINT fk95vepu86o8syqtux9gkr71bhy;
       public          postgres    false    3471    210    213            �           2606    18023 #   animals fkb36lt3kj4mrbdx5btxmp4j60n    FK CONSTRAINT     �   ALTER TABLE ONLY public.animals
    ADD CONSTRAINT fkb36lt3kj4mrbdx5btxmp4j60n FOREIGN KEY (customer_id) REFERENCES public.customers(id);
 M   ALTER TABLE ONLY public.animals DROP CONSTRAINT fkb36lt3kj4mrbdx5btxmp4j60n;
       public          postgres    false    3477    217    210            �           2606    18033 *   animal_vaccine fkfq2oegy209kk7kq3fudavlem1    FK CONSTRAINT     �   ALTER TABLE ONLY public.animal_vaccine
    ADD CONSTRAINT fkfq2oegy209kk7kq3fudavlem1 FOREIGN KEY (animal_id) REFERENCES public.animals(id);
 T   ALTER TABLE ONLY public.animal_vaccine DROP CONSTRAINT fkfq2oegy209kk7kq3fudavlem1;
       public          postgres    false    210    211    3471            �           2606    18028 *   animal_vaccine fkgnqohv3egsjycutvdidsojiun    FK CONSTRAINT     �   ALTER TABLE ONLY public.animal_vaccine
    ADD CONSTRAINT fkgnqohv3egsjycutvdidsojiun FOREIGN KEY (vaccine_id) REFERENCES public.vaccines(id);
 T   ALTER TABLE ONLY public.animal_vaccine DROP CONSTRAINT fkgnqohv3egsjycutvdidsojiun;
       public          postgres    false    211    222    3489            �           2606    18058 1   doctor_available_date fki1p6t0ridmyxfq3lj9lock6wt    FK CONSTRAINT     �   ALTER TABLE ONLY public.doctor_available_date
    ADD CONSTRAINT fki1p6t0ridmyxfq3lj9lock6wt FOREIGN KEY (doctor_id) REFERENCES public.doctors(id);
 [   ALTER TABLE ONLY public.doctor_available_date DROP CONSTRAINT fki1p6t0ridmyxfq3lj9lock6wt;
       public          postgres    false    220    218    3483            �           2606    18048 (   appointments fkmujeo4tymoo98cmf7uj3vsv76    FK CONSTRAINT     �   ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT fkmujeo4tymoo98cmf7uj3vsv76 FOREIGN KEY (doctor_id) REFERENCES public.doctors(id);
 R   ALTER TABLE ONLY public.appointments DROP CONSTRAINT fkmujeo4tymoo98cmf7uj3vsv76;
       public          postgres    false    213    220    3483            �           2606    18053 1   doctor_available_date fkoc0kk1nl3akg4dla0furg1owa    FK CONSTRAINT     �   ALTER TABLE ONLY public.doctor_available_date
    ADD CONSTRAINT fkoc0kk1nl3akg4dla0furg1owa FOREIGN KEY (available_date_id) REFERENCES public.available_dates(id);
 [   ALTER TABLE ONLY public.doctor_available_date DROP CONSTRAINT fkoc0kk1nl3akg4dla0furg1owa;
       public          postgres    false    215    3475    218            �           2606    18043 (   appointments fkrlbb09f329sfsmftrh7y0yxtk    FK CONSTRAINT     �   ALTER TABLE ONLY public.appointments
    ADD CONSTRAINT fkrlbb09f329sfsmftrh7y0yxtk FOREIGN KEY (customer_id) REFERENCES public.customers(id);
 R   ALTER TABLE ONLY public.appointments DROP CONSTRAINT fkrlbb09f329sfsmftrh7y0yxtk;
       public          postgres    false    217    213    3477            7   5   x���� C�s~1<�U�^���\Ə2����ؕ������/����X���&      6   �   x���AK1��3�"=y�d�����=hAPa^��.�%�R��fֵA����������C���|�w>�.�����S	w<�'d�aՇZ:X��%뼥�����ɼ�kD�ICJn�$�Q&w�4�?r8��m��&���=DU������F��_����B4���H�Iio]q������`��Hx=�A�M���	�����Cw��~$���)z6F�_#�
�t�      9   \   x�u���0�w2E�Ӵ%���sТ"�ɿ�-�dj.�O�j�)��U��6����]�	X���u�}���'7AI���/���1��<�      ;   1   x�3�4202�50�54�24���t����<0��3�54����� 9�

      =   �   x�uб
�0���)�/A��n�8�N.��`i���:���"������/B�1.������՛]Q"�55�C@�t�1�b�ݽ��ѳ�J�1=^83��b�«(������w��GE��c���JX��+�T�����t�I
���g_Rf~�WkE      >   &   x�3��42�22�44�22�4�22��F�\1z\\\ R8�      @   {   x�u�;�@���>'��q���CT�4V�Eɢx�/6[f��}�ȕ̃��-_7
qLqn���L�.�$ж�vf2[�X�G�b�N��?��+w��=8$��	����)�f�diQp      B   �   x���1�0����]*�wM[g��{7ۡPt���C�.!���G��IV���[���:�~��3m2���&�6Q��&���D%�(��0J�3�� ���'皚(U�'�׾T�� ��l���֠�-���]y�����)�����     