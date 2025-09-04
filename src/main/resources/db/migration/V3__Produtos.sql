INSERT INTO categorias (
    nome, slug, produto_ativo)
    VALUES
    ('Categoria 1', 'categoria-1', true),
    ('Categoria 2', 'categoria-2', true),
    ('Categoria 3', 'categoria-3', true);

INSERT INTO produtos (
    categoria_id, nome_produto, slug_produto, descricao, 
    preco, codigo_produto, estoque, 
    produto_ativo)
VALUES
    (1, 'Produto 1', 'produto-1', 'Descrição do Produto 1', 
    100.00, 'COD001', 10, true),
    (2, 'Produto 2', 'produto-2', 'Descrição do Produto 2', 
    200.00, 'COD002', 5, true),
    (3, 'Produto 3', 'produto-3', 'Descrição do Produto 3', 
    300.00, 'COD003', 0, true);