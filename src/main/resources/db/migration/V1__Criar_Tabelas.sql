create extension if not exists citext;
create table usuarios (
  id bigint generated always as identity primary key,
  nome text not null,
  email citext unique not null,
  senha_hash text not null,
  perfil text not null check (perfil in ('CLIENTE','ADMINISTRADOR')),
  criado_em timestamptz not null default now()
);

create table enderecos (
  id bigint generated always as identity primary key,
  usuario_id bigint references usuarios(id) on delete cascade,
  logradouro text not null,
  numero text,
  complemento text,
  bairro text,
  cidade text not null,
  estado text not null,
  cep text not null,
  pais text not null,
  padrao boolean not null default false
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
  produto_ativo boolean not null default true
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

create table pedidos (
  id bigint generated always as identity primary key,
  usuario_id bigint references usuarios(id),
  endereco_id bigint references enderecos(id),
  status text not null check (status in ('CRIADO','PAGO','ENVIADO','ENTREGUE','CANCELADO')),
  subtotal numeric(12,2) not null,
  frete numeric(12,2) not null,
  total numeric(12,2) not null,
  status_pagamento text not null check (status_pagamento in ('PENDENTE','APROVADO','RECUSADO')) default 'PENDENTE',
  criado_em timestamptz not null default now()
);

create table itens_pedido (
  id bigint generated always as identity primary key,
  pedido_id bigint references pedidos(id) on delete cascade,
  produto_id bigint references produtos(id),
  quantidade int not null check (quantidade > 0),
  preco_unitario numeric(12,2) not null
);