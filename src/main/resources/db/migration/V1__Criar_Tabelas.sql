create extension if not exists citext;
create table usuarios (
  id bigint generated always as identity primary key,
  nome text not null,
  email citext unique not null,
  senha_hash text not null,
  perfil text not null check (perfil in ('CLIENTE','ADMINISTRADOR', 'SUPER_ADMIN')),
  criado_em timestamptz not null default now()
);

CREATE TABLE IF NOT EXISTS enderecos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

create table categorias (
  id bigint generated always as identity primary key,
  nome text not null
);

create table produtos (
  id bigint generated always as identity primary key,
  categoria_id bigint references categorias(id),
  nome_produto text not null,
  slug_produto text unique not null,
  descricao text,
  preco numeric(12,2) not null,
  codigo_produto text unique,
  estoque int not null default 0,
  produto_ativo boolean not null default true,
  usuario_criacao bigint references usuarios(id)
);

create table carrinhos (
  id bigint generated always as identity primary key,
  usuario_id bigint references usuarios(id) on delete cascade,
  criado_em timestamptz not null default now(),
  atualizado_em timestamptz not null default now()
);

create table itens_carrinho (
  id bigint generated always as identity primary key,
  carrinho_id bigint references carrinhos(id) on delete cascade,
  produto_id bigint references produtos(id),
  quantidade int not null check (quantidade > 0),
  preco_unitario numeric(12,2) not null
);

CREATE TABLE IF NOT EXISTS pedidos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    endereco_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'CRIADO',
    metodo_pagamento VARCHAR(50) NOT NULL,
    sub_total DECIMAL(12, 2) NOT NULL,
    frete DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    total DECIMAL(12, 2) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    pago_em TIMESTAMP,
    enviado_em TIMESTAMP,
    entregue_em TIMESTAMP,
    cancelado_em TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES usuarios(id),
    FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);

CREATE TABLE IF NOT EXISTS itens_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);