-- senha admin
INSERT INTO USUARIOS (NOME, EMAIL, senha_Hash, PERFIL, criado_Em) 
VALUES ('Admin', 
'admin@exemplo.com', 
'$2a$12$5AirjYcZHBmV65y17pArcuXwNNe76zwOKic6vd739.jqg2ba8rwui', 
'ADMINISTRADOR', 
now()) ON CONFLICT (EMAIL) DO NOTHING;

-- senha admin
INSERT INTO USUARIOS (NOME, EMAIL, senha_Hash, PERFIL, criado_Em) 
VALUES ('Super Admin', 'superadmin@exemplo.com', 
'$2a$12$5AirjYcZHBmV65y17pArcuXwNNe76zwOKic6vd739.jqg2ba8rwui', 
'SUPER_ADMIN', 
now()) ON CONFLICT (EMAIL) DO NOTHING;