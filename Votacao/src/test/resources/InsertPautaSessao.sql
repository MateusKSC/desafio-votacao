-- Inserindo nova Pauta
INSERT INTO pautas (id,nome,descricao, data_de_criacao,resultado_votacao,votos_positivos,votos_negativos,sessao_iniciada,concluida,sessao_id)
 VALUES (1, 'Pauta Teste', 'descricao','2024-05-20 13:00:00-00', false,0,0,false,false,1);
INSERT INTO pautas (id,nome,descricao, data_de_criacao,resultado_votacao,votos_positivos,votos_negativos,sessao_iniciada,concluida,sessao_id)
 VALUES (2, 'Pauta Teste', 'descricao2','2025-04-19 13:00:00-00', false,0,0,false,false,2);
-- Inserindo associados na Pauta
INSERT INTO pautas_associados (pautas_id,associados_id) VALUES (1, 2);
INSERT INTO pautas_associados (pautas_id,associados_id) VALUES (1, 1);

