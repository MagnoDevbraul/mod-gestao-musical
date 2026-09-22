--
-- PostgreSQL database dump
--

\restrict vxbvayeMKT5J283hRFoQbVs3ANWSouBmtKuy3w6TPmY0SX4BgwaZE1whrnXIE1O

-- Dumped from database version 17.10
-- Dumped by pg_dump version 17.10

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: aluno; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.aluno (
    id bigint NOT NULL,
    nome character varying(150) NOT NULL,
    comum_id bigint NOT NULL,
    nivel_id bigint NOT NULL,
    cargo_ministerio_id bigint NOT NULL,
    possui_instrumento boolean DEFAULT false NOT NULL,
    data_inicio_gem date,
    situacao character varying(30) DEFAULT 'ATIVO'::character varying NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_batismo date,
    CONSTRAINT ck_aluno_situacao CHECK (((situacao)::text = ANY ((ARRAY['ATIVO'::character varying, 'ARQUIVADO'::character varying])::text[])))
);


--
-- Name: aluno_compartilhamento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.aluno_compartilhamento (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    comum_destino_id bigint NOT NULL,
    compartilhado_por_usuario_id bigint NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: aluno_compartilhamento_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.aluno_compartilhamento ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.aluno_compartilhamento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: aluno_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.aluno ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.aluno_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: aluno_instrumento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.aluno_instrumento (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    instrumento_id bigint NOT NULL,
    tipo character varying(20) NOT NULL,
    tonalidade_id bigint NOT NULL,
    CONSTRAINT ck_aluno_instrumento_tipo CHECK (((tipo)::text = ANY ((ARRAY['TOCA'::character varying, 'DESEJA_TOCAR'::character varying])::text[])))
);


--
-- Name: aluno_instrumento_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.aluno_instrumento ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.aluno_instrumento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: auditoria; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.auditoria (
    id bigint NOT NULL,
    usuario_id bigint,
    acao character varying(50) NOT NULL,
    tabela_afetada character varying(100) NOT NULL,
    registro_id bigint,
    descricao text,
    dados_anteriores jsonb,
    dados_novos jsonb,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_auditoria_acao CHECK ((TRIM(BOTH FROM acao) <> ''::text)),
    CONSTRAINT ck_auditoria_tabela CHECK ((TRIM(BOTH FROM tabela_afetada) <> ''::text))
);


--
-- Name: auditoria_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.auditoria_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: auditoria_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.auditoria_id_seq OWNED BY public.auditoria.id;


--
-- Name: cargo_ministerio; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cargo_ministerio (
    id bigint NOT NULL,
    nome character varying(100) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


--
-- Name: cargo_ministerio_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.cargo_ministerio ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.cargo_ministerio_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: comum_congregacao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.comum_congregacao (
    id bigint NOT NULL,
    setor_id bigint NOT NULL,
    nome character varying(150) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


--
-- Name: comum_congregacao_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.comum_congregacao ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.comum_congregacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: escala; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.escala (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    data date NOT NULL,
    nome_escala character varying(150) NOT NULL,
    tonalidade_id bigint NOT NULL,
    observacoes text,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    clave character varying(10),
    autorizado_por_usuario_id bigint,
    CONSTRAINT ck_escala_clave CHECK (((clave IS NULL) OR ((clave)::text = ANY ((ARRAY['SOL'::character varying, 'DÓ'::character varying, 'FÁ'::character varying])::text[]))))
);


--
-- Name: escala_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.escala ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.escala_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: exclusao_aluno; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.exclusao_aluno (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    motivo text NOT NULL,
    data_hora timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_exclusao_aluno_motivo CHECK ((TRIM(BOTH FROM motivo) <> ''::text))
);


--
-- Name: exclusao_aluno_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.exclusao_aluno_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: exclusao_aluno_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.exclusao_aluno_id_seq OWNED BY public.exclusao_aluno.id;


--
-- Name: hinario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hinario (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    data date NOT NULL,
    hino integer NOT NULL,
    voz character varying(20) NOT NULL,
    observacoes text,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    clave character varying(10),
    autorizado_por_usuario_id bigint,
    CONSTRAINT ck_hinario_clave CHECK (((clave IS NULL) OR ((clave)::text = ANY ((ARRAY['SOL'::character varying, 'DÓ'::character varying, 'FÁ'::character varying])::text[])))),
    CONSTRAINT ck_hinario_hino CHECK ((hino > 0)),
    CONSTRAINT ck_hinario_voz CHECK (((voz)::text = ANY ((ARRAY['PRINCIPAL'::character varying, 'ALTERNATIVA'::character varying])::text[])))
);


--
-- Name: hinario_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.hinario ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.hinario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: historico; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.historico (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    tipo_evento character varying(50) NOT NULL,
    data_hora timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    descricao text,
    valor_anterior text,
    valor_novo text,
    CONSTRAINT ck_historico_tipo_evento CHECK ((TRIM(BOTH FROM tipo_evento) <> ''::text))
);


--
-- Name: historico_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.historico_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: historico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.historico_id_seq OWNED BY public.historico.id;


--
-- Name: instrumento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.instrumento (
    id bigint NOT NULL,
    nome character varying(100) NOT NULL,
    categoria character varying(50) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


--
-- Name: instrumento_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.instrumento ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.instrumento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: metodo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.metodo (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    data date NOT NULL,
    nome_metodo character varying(150) NOT NULL,
    pagina_inicial integer NOT NULL,
    pagina_final integer NOT NULL,
    licao_inicial integer NOT NULL,
    licao_final integer NOT NULL,
    observacoes text,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    clave character varying(10),
    autorizado_por_usuario_id bigint,
    CONSTRAINT ck_metodo_clave CHECK (((clave IS NULL) OR ((clave)::text = ANY ((ARRAY['SOL'::character varying, 'DÓ'::character varying, 'FÁ'::character varying])::text[])))),
    CONSTRAINT ck_metodo_licoes CHECK (((licao_inicial >= 1) AND (licao_final >= licao_inicial))),
    CONSTRAINT ck_metodo_paginas CHECK (((pagina_inicial >= 1) AND (pagina_final >= pagina_inicial)))
);


--
-- Name: metodo_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.metodo ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.metodo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: msa; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.msa (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    data date NOT NULL,
    fase character varying(4) NOT NULL,
    pagina_inicial integer,
    pagina_final integer,
    licao_inicial integer,
    licao_final integer,
    clave character varying(3) NOT NULL,
    observacoes text,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    autorizado_por_usuario_id bigint,
    CONSTRAINT ck_msa_clave CHECK (((clave)::text = ANY ((ARRAY['SOL'::character varying, 'DÓ'::character varying, 'FÁ'::character varying])::text[]))),
    CONSTRAINT ck_msa_fase CHECK (((fase)::text = ANY ((ARRAY['1.1'::character varying, '1.2'::character varying, '1.3'::character varying, '2.1'::character varying, '2.2'::character varying, '2.3'::character varying, '3.1'::character varying, '3.2'::character varying, '3.3'::character varying, '4.1'::character varying, '4.2'::character varying, '4.3'::character varying, '5.1'::character varying, '5.2'::character varying, '5.3'::character varying, '6.1'::character varying, '6.2'::character varying, '6.3'::character varying, '7.1'::character varying, '7.2'::character varying, '7.3'::character varying, '8.1'::character varying, '8.2'::character varying, '8.3'::character varying, '9.1'::character varying, '9.2'::character varying, '9.3'::character varying, '10.1'::character varying, '10.2'::character varying, '10.3'::character varying, '11.1'::character varying, '11.2'::character varying, '11.3'::character varying, '12.1'::character varying, '12.2'::character varying, '12.3'::character varying, '13.1'::character varying, '13.2'::character varying, '13.3'::character varying, '14.1'::character varying, '14.2'::character varying, '14.3'::character varying, '15.1'::character varying, '15.2'::character varying, '15.3'::character varying, '16.1'::character varying, '16.2'::character varying, '16.3'::character varying])::text[]))),
    CONSTRAINT ck_msa_licoes CHECK ((((licao_inicial IS NULL) AND (licao_final IS NULL)) OR ((licao_inicial IS NOT NULL) AND (licao_final IS NOT NULL) AND (licao_inicial >= 1) AND (licao_final >= licao_inicial)))),
    CONSTRAINT ck_msa_paginas CHECK ((((pagina_inicial IS NULL) AND (pagina_final IS NULL)) OR ((pagina_inicial IS NOT NULL) AND (pagina_final IS NOT NULL) AND (pagina_inicial >= 1) AND (pagina_final >= pagina_inicial))))
);


--
-- Name: msa_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.msa ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.msa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: mts; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mts (
    id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    data date NOT NULL,
    modulo integer NOT NULL,
    licao integer NOT NULL,
    observacoes text,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    pagina_inicial integer,
    pagina_final integer,
    autorizado_por_usuario_id bigint,
    CONSTRAINT ck_mts_intervalo_paginas CHECK (((pagina_inicial IS NULL) OR (pagina_final IS NULL) OR (pagina_final >= pagina_inicial))),
    CONSTRAINT ck_mts_licao CHECK ((licao >= 0)),
    CONSTRAINT ck_mts_modulo CHECK (((modulo >= 1) AND (modulo <= 12))),
    CONSTRAINT ck_mts_pagina_final CHECK (((pagina_final IS NULL) OR (pagina_final >= 1))),
    CONSTRAINT ck_mts_pagina_inicial CHECK (((pagina_inicial IS NULL) OR (pagina_inicial >= 1))),
    CONSTRAINT ck_mts_paginas_preenchimento CHECK ((((pagina_inicial IS NULL) AND (pagina_final IS NULL)) OR ((pagina_inicial IS NOT NULL) AND (pagina_final IS NOT NULL))))
);


--
-- Name: mts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.mts ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.mts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: nivel; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.nivel (
    id bigint NOT NULL,
    nome character varying(80) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


--
-- Name: nivel_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.nivel ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.nivel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: notificacao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notificacao (
    id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    aluno_id bigint NOT NULL,
    tipo_evento character varying(50) NOT NULL,
    titulo character varying(150) NOT NULL,
    mensagem text NOT NULL,
    data_hora timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    lida boolean DEFAULT false NOT NULL,
    data_leitura timestamp without time zone,
    CONSTRAINT ck_notificacao_leitura CHECK ((((lida = false) AND (data_leitura IS NULL)) OR ((lida = true) AND (data_leitura IS NOT NULL)))),
    CONSTRAINT ck_notificacao_mensagem CHECK ((TRIM(BOTH FROM mensagem) <> ''::text)),
    CONSTRAINT ck_notificacao_tipo_evento CHECK ((TRIM(BOTH FROM tipo_evento) <> ''::text)),
    CONSTRAINT ck_notificacao_titulo CHECK ((TRIM(BOTH FROM titulo) <> ''::text))
);


--
-- Name: notificacao_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.notificacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: notificacao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.notificacao_id_seq OWNED BY public.notificacao.id;


--
-- Name: perfil_permissao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.perfil_permissao (
    id bigint NOT NULL,
    perfil_usuario_id bigint NOT NULL,
    permissao_id bigint NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: perfil_permissao_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.perfil_permissao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: perfil_permissao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.perfil_permissao_id_seq OWNED BY public.perfil_permissao.id;


--
-- Name: perfil_usuario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.perfil_usuario (
    id bigint NOT NULL,
    nome character varying(50) NOT NULL,
    descricao character varying(255),
    ativo boolean DEFAULT true NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_perfil_usuario_nome CHECK ((TRIM(BOTH FROM nome) <> ''::text))
);


--
-- Name: perfil_usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.perfil_usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: perfil_usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.perfil_usuario_id_seq OWNED BY public.perfil_usuario.id;


--
-- Name: permissao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.permissao (
    id bigint NOT NULL,
    nome character varying(80) NOT NULL,
    descricao character varying(255),
    ativo boolean DEFAULT true NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_permissao_nome CHECK ((TRIM(BOTH FROM nome) <> ''::text))
);


--
-- Name: permissao_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.permissao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: permissao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.permissao_id_seq OWNED BY public.permissao.id;


--
-- Name: relatorio_mensal; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.relatorio_mensal (
    id bigint NOT NULL,
    periodo_inicio date NOT NULL,
    periodo_fim date NOT NULL,
    total_alunos_ativos integer DEFAULT 0 NOT NULL,
    total_alunos_arquivados integer DEFAULT 0 NOT NULL,
    total_mts integer DEFAULT 0 NOT NULL,
    total_msa integer DEFAULT 0 NOT NULL,
    total_metodo integer DEFAULT 0 NOT NULL,
    total_hinario integer DEFAULT 0 NOT NULL,
    total_escala integer DEFAULT 0 NOT NULL,
    total_alunos_sem_movimentacao_60_dias integer DEFAULT 0 NOT NULL,
    total_compartilhamentos integer DEFAULT 0 NOT NULL,
    total_exclusoes_arquivamentos integer DEFAULT 0 NOT NULL,
    total_restauracoes integer DEFAULT 0 NOT NULL,
    total_notificacoes integer DEFAULT 0 NOT NULL,
    total_eventos_auditoria integer DEFAULT 0 NOT NULL,
    gerado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT ck_relatorio_mensal_periodo CHECK ((periodo_fim >= periodo_inicio)),
    CONSTRAINT ck_relatorio_mensal_totais CHECK (((total_alunos_ativos >= 0) AND (total_alunos_arquivados >= 0) AND (total_mts >= 0) AND (total_msa >= 0) AND (total_metodo >= 0) AND (total_hinario >= 0) AND (total_escala >= 0) AND (total_alunos_sem_movimentacao_60_dias >= 0) AND (total_compartilhamentos >= 0) AND (total_exclusoes_arquivamentos >= 0) AND (total_restauracoes >= 0) AND (total_notificacoes >= 0) AND (total_eventos_auditoria >= 0)))
);


--
-- Name: relatorio_mensal_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.relatorio_mensal ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.relatorio_mensal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: setor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.setor (
    id bigint NOT NULL,
    nome character varying(100) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


--
-- Name: setor_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.setor ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.setor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: tonalidade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tonalidade (
    id bigint NOT NULL,
    nome character varying(10) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


--
-- Name: tonalidade_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.tonalidade ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.tonalidade_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario (
    id bigint NOT NULL,
    nome character varying(150) NOT NULL,
    email character varying(150) NOT NULL,
    senha character varying(255) NOT NULL,
    perfil_usuario_id bigint NOT NULL,
    ativo boolean DEFAULT true NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    comum_id bigint,
    CONSTRAINT ck_usuario_email CHECK ((TRIM(BOTH FROM email) <> ''::text)),
    CONSTRAINT ck_usuario_nome CHECK ((TRIM(BOTH FROM nome) <> ''::text))
);


--
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.usuario_id_seq OWNED BY public.usuario.id;


--
-- Name: usuario_setor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario_setor (
    id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    setor_id bigint NOT NULL,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: usuario_setor_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.usuario_setor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: usuario_setor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.usuario_setor_id_seq OWNED BY public.usuario_setor.id;


--
-- Name: auditoria id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria ALTER COLUMN id SET DEFAULT nextval('public.auditoria_id_seq'::regclass);


--
-- Name: exclusao_aluno id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exclusao_aluno ALTER COLUMN id SET DEFAULT nextval('public.exclusao_aluno_id_seq'::regclass);


--
-- Name: historico id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historico ALTER COLUMN id SET DEFAULT nextval('public.historico_id_seq'::regclass);


--
-- Name: notificacao id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacao ALTER COLUMN id SET DEFAULT nextval('public.notificacao_id_seq'::regclass);


--
-- Name: perfil_permissao id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_permissao ALTER COLUMN id SET DEFAULT nextval('public.perfil_permissao_id_seq'::regclass);


--
-- Name: perfil_usuario id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_usuario ALTER COLUMN id SET DEFAULT nextval('public.perfil_usuario_id_seq'::regclass);


--
-- Name: permissao id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissao ALTER COLUMN id SET DEFAULT nextval('public.permissao_id_seq'::regclass);


--
-- Name: usuario id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario ALTER COLUMN id SET DEFAULT nextval('public.usuario_id_seq'::regclass);


--
-- Name: usuario_setor id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_setor ALTER COLUMN id SET DEFAULT nextval('public.usuario_setor_id_seq'::regclass);


--
-- Name: aluno_compartilhamento aluno_compartilhamento_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_compartilhamento
    ADD CONSTRAINT aluno_compartilhamento_pkey PRIMARY KEY (id);


--
-- Name: auditoria auditoria_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria
    ADD CONSTRAINT auditoria_pkey PRIMARY KEY (id);


--
-- Name: exclusao_aluno exclusao_aluno_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exclusao_aluno
    ADD CONSTRAINT exclusao_aluno_pkey PRIMARY KEY (id);


--
-- Name: historico historico_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historico
    ADD CONSTRAINT historico_pkey PRIMARY KEY (id);


--
-- Name: notificacao notificacao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacao
    ADD CONSTRAINT notificacao_pkey PRIMARY KEY (id);


--
-- Name: perfil_permissao perfil_permissao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_permissao
    ADD CONSTRAINT perfil_permissao_pkey PRIMARY KEY (id);


--
-- Name: perfil_usuario perfil_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_usuario
    ADD CONSTRAINT perfil_usuario_pkey PRIMARY KEY (id);


--
-- Name: permissao permissao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissao
    ADD CONSTRAINT permissao_pkey PRIMARY KEY (id);


--
-- Name: aluno pk_aluno; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT pk_aluno PRIMARY KEY (id);


--
-- Name: aluno_instrumento pk_aluno_instrumento; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_instrumento
    ADD CONSTRAINT pk_aluno_instrumento PRIMARY KEY (id);


--
-- Name: cargo_ministerio pk_cargo_ministerio; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cargo_ministerio
    ADD CONSTRAINT pk_cargo_ministerio PRIMARY KEY (id);


--
-- Name: comum_congregacao pk_comum_congregacao; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comum_congregacao
    ADD CONSTRAINT pk_comum_congregacao PRIMARY KEY (id);


--
-- Name: escala pk_escala; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.escala
    ADD CONSTRAINT pk_escala PRIMARY KEY (id);


--
-- Name: hinario pk_hinario; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hinario
    ADD CONSTRAINT pk_hinario PRIMARY KEY (id);


--
-- Name: instrumento pk_instrumento; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.instrumento
    ADD CONSTRAINT pk_instrumento PRIMARY KEY (id);


--
-- Name: metodo pk_metodo; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.metodo
    ADD CONSTRAINT pk_metodo PRIMARY KEY (id);


--
-- Name: msa pk_msa; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.msa
    ADD CONSTRAINT pk_msa PRIMARY KEY (id);


--
-- Name: mts pk_mts; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mts
    ADD CONSTRAINT pk_mts PRIMARY KEY (id);


--
-- Name: nivel pk_nivel; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nivel
    ADD CONSTRAINT pk_nivel PRIMARY KEY (id);


--
-- Name: setor pk_setor; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.setor
    ADD CONSTRAINT pk_setor PRIMARY KEY (id);


--
-- Name: tonalidade pk_tonalidade; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tonalidade
    ADD CONSTRAINT pk_tonalidade PRIMARY KEY (id);


--
-- Name: relatorio_mensal relatorio_mensal_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relatorio_mensal
    ADD CONSTRAINT relatorio_mensal_pkey PRIMARY KEY (id);


--
-- Name: aluno_compartilhamento uk_compartilhamento_aluno; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_compartilhamento
    ADD CONSTRAINT uk_compartilhamento_aluno UNIQUE (aluno_id);


--
-- Name: perfil_permissao uk_perfil_permissao; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_permissao
    ADD CONSTRAINT uk_perfil_permissao UNIQUE (perfil_usuario_id, permissao_id);


--
-- Name: perfil_usuario uk_perfil_usuario_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_usuario
    ADD CONSTRAINT uk_perfil_usuario_nome UNIQUE (nome);


--
-- Name: permissao uk_permissao_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissao
    ADD CONSTRAINT uk_permissao_nome UNIQUE (nome);


--
-- Name: relatorio_mensal uk_relatorio_mensal_periodo; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.relatorio_mensal
    ADD CONSTRAINT uk_relatorio_mensal_periodo UNIQUE (periodo_inicio, periodo_fim);


--
-- Name: usuario uk_usuario_email; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT uk_usuario_email UNIQUE (email);


--
-- Name: usuario_setor uk_usuario_setor; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_setor
    ADD CONSTRAINT uk_usuario_setor UNIQUE (usuario_id, setor_id);


--
-- Name: aluno_instrumento uq_aluno_instrumento_tipo; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_instrumento
    ADD CONSTRAINT uq_aluno_instrumento_tipo UNIQUE (aluno_id, instrumento_id, tipo);


--
-- Name: cargo_ministerio uq_cargo_ministerio_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cargo_ministerio
    ADD CONSTRAINT uq_cargo_ministerio_nome UNIQUE (nome);


--
-- Name: comum_congregacao uq_comum_setor_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comum_congregacao
    ADD CONSTRAINT uq_comum_setor_nome UNIQUE (setor_id, nome);


--
-- Name: instrumento uq_instrumento_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.instrumento
    ADD CONSTRAINT uq_instrumento_nome UNIQUE (nome);


--
-- Name: nivel uq_nivel_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.nivel
    ADD CONSTRAINT uq_nivel_nome UNIQUE (nome);


--
-- Name: setor uq_setor_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.setor
    ADD CONSTRAINT uq_setor_nome UNIQUE (nome);


--
-- Name: tonalidade uq_tonalidade_nome; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tonalidade
    ADD CONSTRAINT uq_tonalidade_nome UNIQUE (nome);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- Name: usuario_setor usuario_setor_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_setor
    ADD CONSTRAINT usuario_setor_pkey PRIMARY KEY (id);


--
-- Name: idx_mts_autorizado_por_usuario; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mts_autorizado_por_usuario ON public.mts USING btree (autorizado_por_usuario_id);


--
-- Name: aluno fk_aluno_cargo_ministerio; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT fk_aluno_cargo_ministerio FOREIGN KEY (cargo_ministerio_id) REFERENCES public.cargo_ministerio(id);


--
-- Name: aluno fk_aluno_comum; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT fk_aluno_comum FOREIGN KEY (comum_id) REFERENCES public.comum_congregacao(id);


--
-- Name: aluno_instrumento fk_aluno_instrumento_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_instrumento
    ADD CONSTRAINT fk_aluno_instrumento_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: aluno_instrumento fk_aluno_instrumento_instrumento; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_instrumento
    ADD CONSTRAINT fk_aluno_instrumento_instrumento FOREIGN KEY (instrumento_id) REFERENCES public.instrumento(id);


--
-- Name: aluno_instrumento fk_aluno_instrumento_tonalidade; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_instrumento
    ADD CONSTRAINT fk_aluno_instrumento_tonalidade FOREIGN KEY (tonalidade_id) REFERENCES public.tonalidade(id);


--
-- Name: aluno fk_aluno_nivel; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno
    ADD CONSTRAINT fk_aluno_nivel FOREIGN KEY (nivel_id) REFERENCES public.nivel(id);


--
-- Name: auditoria fk_auditoria_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.auditoria
    ADD CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- Name: aluno_compartilhamento fk_compartilhamento_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_compartilhamento
    ADD CONSTRAINT fk_compartilhamento_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: aluno_compartilhamento fk_compartilhamento_comum_destino; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_compartilhamento
    ADD CONSTRAINT fk_compartilhamento_comum_destino FOREIGN KEY (comum_destino_id) REFERENCES public.comum_congregacao(id);


--
-- Name: aluno_compartilhamento fk_compartilhamento_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aluno_compartilhamento
    ADD CONSTRAINT fk_compartilhamento_usuario FOREIGN KEY (compartilhado_por_usuario_id) REFERENCES public.usuario(id);


--
-- Name: comum_congregacao fk_comum_setor; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comum_congregacao
    ADD CONSTRAINT fk_comum_setor FOREIGN KEY (setor_id) REFERENCES public.setor(id);


--
-- Name: escala fk_escala_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.escala
    ADD CONSTRAINT fk_escala_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: escala fk_escala_autorizado_por_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.escala
    ADD CONSTRAINT fk_escala_autorizado_por_usuario FOREIGN KEY (autorizado_por_usuario_id) REFERENCES public.usuario(id);


--
-- Name: escala fk_escala_tonalidade; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.escala
    ADD CONSTRAINT fk_escala_tonalidade FOREIGN KEY (tonalidade_id) REFERENCES public.tonalidade(id);


--
-- Name: exclusao_aluno fk_exclusao_aluno_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exclusao_aluno
    ADD CONSTRAINT fk_exclusao_aluno_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: exclusao_aluno fk_exclusao_aluno_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.exclusao_aluno
    ADD CONSTRAINT fk_exclusao_aluno_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- Name: hinario fk_hinario_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hinario
    ADD CONSTRAINT fk_hinario_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: hinario fk_hinario_autorizado_por_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hinario
    ADD CONSTRAINT fk_hinario_autorizado_por_usuario FOREIGN KEY (autorizado_por_usuario_id) REFERENCES public.usuario(id);


--
-- Name: historico fk_historico_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historico
    ADD CONSTRAINT fk_historico_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: historico fk_historico_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historico
    ADD CONSTRAINT fk_historico_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- Name: metodo fk_metodo_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.metodo
    ADD CONSTRAINT fk_metodo_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: metodo fk_metodo_autorizado_por_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.metodo
    ADD CONSTRAINT fk_metodo_autorizado_por_usuario FOREIGN KEY (autorizado_por_usuario_id) REFERENCES public.usuario(id);


--
-- Name: msa fk_msa_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.msa
    ADD CONSTRAINT fk_msa_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: msa fk_msa_autorizado_por_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.msa
    ADD CONSTRAINT fk_msa_autorizado_por_usuario FOREIGN KEY (autorizado_por_usuario_id) REFERENCES public.usuario(id);


--
-- Name: mts fk_mts_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mts
    ADD CONSTRAINT fk_mts_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: mts fk_mts_autorizado_por_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mts
    ADD CONSTRAINT fk_mts_autorizado_por_usuario FOREIGN KEY (autorizado_por_usuario_id) REFERENCES public.usuario(id);


--
-- Name: notificacao fk_notificacao_aluno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacao
    ADD CONSTRAINT fk_notificacao_aluno FOREIGN KEY (aluno_id) REFERENCES public.aluno(id);


--
-- Name: notificacao fk_notificacao_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacao
    ADD CONSTRAINT fk_notificacao_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);


--
-- Name: perfil_permissao fk_perfil_permissao_perfil; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_permissao
    ADD CONSTRAINT fk_perfil_permissao_perfil FOREIGN KEY (perfil_usuario_id) REFERENCES public.perfil_usuario(id);


--
-- Name: perfil_permissao fk_perfil_permissao_permissao; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.perfil_permissao
    ADD CONSTRAINT fk_perfil_permissao_permissao FOREIGN KEY (permissao_id) REFERENCES public.permissao(id);


--
-- Name: usuario fk_usuario_comum; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_comum FOREIGN KEY (comum_id) REFERENCES public.comum_congregacao(id);


--
-- Name: usuario fk_usuario_perfil; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_perfil FOREIGN KEY (perfil_usuario_id) REFERENCES public.perfil_usuario(id);


--
-- Name: usuario_setor fk_usuario_setor_setor; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_setor
    ADD CONSTRAINT fk_usuario_setor_setor FOREIGN KEY (setor_id) REFERENCES public.setor(id) ON DELETE CASCADE;


--
-- Name: usuario_setor fk_usuario_setor_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_setor
    ADD CONSTRAINT fk_usuario_setor_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict vxbvayeMKT5J283hRFoQbVs3ANWSouBmtKuy3w6TPmY0SX4BgwaZE1whrnXIE1O

