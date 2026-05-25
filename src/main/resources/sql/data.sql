-- Dados de exemplo para teste
INSERT INTO produtos (nome, descricao, preco, quantidade, quantidade_minima, sku, categoria, data_criacao, data_atualizacao) 
VALUES 
('Teclado Mecânico RGB', 'Teclado com switches mecânicos e iluminação RGB', 299.99, 50, 10, 'TEC-001', 'Periféricos', NOW(), NOW()),
('Mouse Gamer 16000 DPI', 'Mouse com 16000 DPI de precisão para jogos', 149.99, 30, 5, 'MOUSE-001', 'Periféricos', NOW(), NOW()),
('Monitor 4K 27"', 'Monitor 4K com 144Hz para gaming', 1299.99, 15, 3, 'MON-001', 'Monitores', NOW(), NOW()),
('Headset Wireless', 'Fone de ouvido wireless com cancelamento de ruído', 499.99, 20, 5, 'HEAD-001', 'Audio', NOW(), NOW()),
('Webcam 1080p', 'Webcam Full HD com microfone integrado', 199.99, 40, 8, 'WEB-001', 'Periféricos', NOW(), NOW()),
('SSD 1TB NVMe', 'SSD NVMe 1TB com velocidade até 7400MB/s', 399.99, 25, 5, 'SSD-001', 'Armazenamento', NOW(), NOW()),
('Mousepad Grande', 'Mousepad resistente de 80x40cm', 79.99, 100, 20, 'PAD-001', 'Acessórios', NOW(), NOW()),
('Cabo HDMI 2.1', 'Cabo HDMI 2.1 com suporte a 4K 120Hz', 39.99, 200, 30, 'CAB-001', 'Cabos', NOW(), NOW());
