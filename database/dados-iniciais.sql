--
-- PostgreSQL database dump
--

\restrict UETyRhoEfBWaGKj7THSkJ8LEOmc3dcgFm7rmNFd9pqhCHGTM1G00ZmYph4inpKt

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

--
-- Data for Name: cargo_ministerio; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (1, 'Ancião', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (2, 'Ancião da Parque Musical', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (3, 'Diácono', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (4, 'Cooperador do Ofício Ministerial', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (5, 'Cooperador de Jovens e menores', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (6, 'Secretaria', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (7, 'Encarregado Regional', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (8, 'Examinadora de organistas', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (9, 'Encarregado Local', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (10, 'Músico', true);
INSERT INTO public.cargo_ministerio (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (11, 'Organista', true);


--
-- Data for Name: setor; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.setor (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (1, 'Setor Paranoá', true);


--
-- Data for Name: comum_congregacao; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (1, 1, 'SOBRADINHO DOS MELOS', true);
INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (2, 1, 'PARANOÁ - QUADRA 30 (CENTRAL)', true);
INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (3, 1, 'ALTIPLANO LESTE', true);
INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (4, 1, 'ITAPOÃ I', true);
INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (5, 1, 'DEL LAGO II', true);
INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (6, 1, 'SETOR MANDALA', true);
INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (7, 1, 'PARANOÁ PARQUE', true);
INSERT INTO public.comum_congregacao (id, setor_id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (8, 1, 'VILA MARGARIDA - ROTA DO CAVALO', true);


--
-- Data for Name: instrumento; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (1, 'VIOLINO', 'Cordas', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (2, 'VIOLA', 'Cordas', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (3, 'VIOLONCELO', 'Cordas', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (4, 'BARÍTONO DE PISTO', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (5, 'CORNET', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (6, 'EUPHONIUM', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (7, 'FLUGELHORN', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (8, 'MELOFONE', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (9, 'POCKET', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (10, 'SAX HORN', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (11, 'TROMBONE', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (12, 'TROMBONITO', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (13, 'TROMPA', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (14, 'TROMPETE', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (15, 'TUBA', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (16, 'TUBA HELICON', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (17, 'TUBA WAGNERIANA', 'Metais', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (18, 'CLARINETE', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (19, 'CLARINETE ALTO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (20, 'CLARINETE BAIXO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (21, 'CLARINETE CONTRA BAIXO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (22, 'CORNE INGLÊS', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (23, 'FAGOTE', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (24, 'FLAUTA', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (25, 'FLAUTA BAIXO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (26, 'FLAUTA CONTRALTO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (27, 'OBOÉ', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (28, 'OBOÉ D''AMORE', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (29, 'SAXOFONE ALTO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (30, 'SAXOFONE BAIXO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (31, 'SAXOFONE BARÍTONO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (32, 'SAXOFONE SOPRANINO C', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (33, 'SAXOFONE SOPRANINO RETO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (34, 'SAXOFONE SOPRANO CURVO', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (35, 'SAXOFONE SOPRANO RET', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (36, 'SAXOFONE TENOR', 'Madeiras', true);
INSERT INTO public.instrumento (id, nome, categoria, ativo) OVERRIDING SYSTEM VALUE VALUES (37, 'ÓRGÃO ELETRÔNICO', 'Teclas', true);


--
-- Data for Name: nivel; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (1, 'Candidato(a)', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (2, 'Culto oficial', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (3, 'Ensaio', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (4, 'Meia hora', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (5, 'RJM', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (6, 'RJM/Culto oficial', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (7, 'RJM/Ensaio', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (8, 'RJM/Meia hora', true);
INSERT INTO public.nivel (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (9, 'RJM/Oficializado(a)', true);


--
-- Data for Name: perfil_usuario; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.perfil_usuario (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (1, 'SECRETARIA', 'Acesso administrativo completo ao MOD, incluindo auditoria.', true, '2026-09-02 15:45:38.224453', '2026-09-02 15:45:38.224453');
INSERT INTO public.perfil_usuario (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (2, 'ENCARREGADO_REGIONAL', 'Responsável pela gestão musical no âmbito regional.', true, '2026-09-02 15:45:38.224453', '2026-09-02 15:45:38.224453');
INSERT INTO public.perfil_usuario (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (3, 'ENCARREGADO_LOCAL', 'Responsável pela gestão musical no âmbito da congregação.', true, '2026-09-02 15:45:38.224453', '2026-09-02 15:45:38.224453');
INSERT INTO public.perfil_usuario (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (4, 'INSTRUTOR', 'Usuário responsável pelas atividades de instrução musical permitidas pelo sistema.', true, '2026-09-02 15:45:38.224453', '2026-09-02 15:45:38.224453');


--
-- Data for Name: permissao; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (1, 'ALUNO_CONSULTAR', 'Permite consultar os dados dos alunos conforme o escopo do usuário.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (2, 'ALUNO_CADASTRAR', 'Permite cadastrar novos alunos.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (3, 'ALUNO_EDITAR', 'Permite editar os dados permitidos do aluno.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (4, 'ALUNO_ARQUIVAR', 'Permite arquivar um aluno, preservando seu histórico no MOD.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (5, 'ALUNO_ALTERAR_NIVEL', 'Permite alterar o nível musical do aluno.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (6, 'LICAO_INSERIR', 'Permite inserir registros de lições e atividades musicais.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (7, 'HISTORICO_MUSICAL_CONSULTAR', 'Permite consultar o histórico e a evolução musical do aluno.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (8, 'AUDITORIA_CONSULTAR', 'Permite consultar o histórico administrativo e as operações realizadas no sistema.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (9, 'USUARIO_GERENCIAR', 'Permite cadastrar, editar, ativar e desativar usuários.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');
INSERT INTO public.permissao (id, nome, descricao, ativo, criado_em, atualizado_em) VALUES (10, 'PERMISSAO_GERENCIAR', 'Permite administrar permissões e associações entre perfis e permissões.', true, '2026-09-02 15:59:59.382583', '2026-09-02 15:59:59.382583');


--
-- Data for Name: perfil_permissao; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (1, 1, 1, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (2, 1, 2, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (3, 1, 3, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (4, 1, 4, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (5, 1, 5, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (6, 1, 6, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (7, 1, 7, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (8, 1, 8, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (9, 1, 9, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (10, 1, 10, '2026-09-02 16:01:08.61135');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (11, 2, 1, '2026-09-02 16:01:33.561969');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (12, 2, 2, '2026-09-02 16:01:33.561969');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (13, 2, 3, '2026-09-02 16:01:33.561969');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (14, 2, 4, '2026-09-02 16:01:33.561969');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (15, 2, 5, '2026-09-02 16:01:33.561969');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (16, 2, 6, '2026-09-02 16:01:33.561969');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (17, 2, 7, '2026-09-02 16:01:33.561969');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (18, 3, 1, '2026-09-02 16:01:49.954733');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (19, 3, 2, '2026-09-02 16:01:49.954733');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (20, 3, 3, '2026-09-02 16:01:49.954733');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (21, 3, 4, '2026-09-02 16:01:49.954733');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (22, 3, 5, '2026-09-02 16:01:49.954733');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (23, 3, 6, '2026-09-02 16:01:49.954733');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (24, 3, 7, '2026-09-02 16:01:49.954733');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (25, 4, 1, '2026-09-02 16:02:08.677218');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (26, 4, 2, '2026-09-02 16:02:08.677218');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (27, 4, 3, '2026-09-02 16:02:08.677218');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (28, 4, 6, '2026-09-02 16:02:08.677218');
INSERT INTO public.perfil_permissao (id, perfil_usuario_id, permissao_id, criado_em) VALUES (29, 4, 7, '2026-09-02 16:02:08.677218');


--
-- Data for Name: tonalidade; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.tonalidade (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (1, 'Dó', true);
INSERT INTO public.tonalidade (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (2, 'Fá', true);
INSERT INTO public.tonalidade (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (3, 'Fá/Sib', true);
INSERT INTO public.tonalidade (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (4, 'Láb', true);
INSERT INTO public.tonalidade (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (5, 'Mib', true);
INSERT INTO public.tonalidade (id, nome, ativo) OVERRIDING SYSTEM VALUE VALUES (6, 'Sib', true);


--
-- Name: cargo_ministerio_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval(
               'public.cargo_ministerio_id_seq',
               (SELECT MAX(id) FROM public.cargo_ministerio),
               true
       );

SELECT pg_catalog.setval(
               'public.comum_congregacao_id_seq',
               (SELECT MAX(id) FROM public.comum_congregacao),
               true
       );

SELECT pg_catalog.setval(
               'public.instrumento_id_seq',
               (SELECT MAX(id) FROM public.instrumento),
               true
       );

SELECT pg_catalog.setval(
               'public.nivel_id_seq',
               (SELECT MAX(id) FROM public.nivel),
               true
       );

SELECT pg_catalog.setval(
               'public.perfil_permissao_id_seq',
               (SELECT MAX(id) FROM public.perfil_permissao),
               true
       );

SELECT pg_catalog.setval(
               'public.perfil_usuario_id_seq',
               (SELECT MAX(id) FROM public.perfil_usuario),
               true
       );

SELECT pg_catalog.setval(
               'public.permissao_id_seq',
               (SELECT MAX(id) FROM public.permissao),
               true
       );

SELECT pg_catalog.setval(
               'public.setor_id_seq',
               (SELECT MAX(id) FROM public.setor),
               true
       );

SELECT pg_catalog.setval(
               'public.tonalidade_id_seq',
               (SELECT MAX(id) FROM public.tonalidade),
               true
       );


--
-- PostgreSQL database dump complete
--

\unrestrict UETyRhoEfBWaGKj7THSkJ8LEOmc3dcgFm7rmNFd9pqhCHGTM1G00ZmYph4inpKt

