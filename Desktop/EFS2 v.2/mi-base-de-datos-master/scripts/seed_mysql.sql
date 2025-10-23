-- Base de datos para Bella sin culpa (MySQL)
-- Ejecuta este script en tu servidor MySQL

CREATE DATABASE IF NOT EXISTS base;
USE base;

-- Usuario administrador
-- Contraseña: admin123 (encriptada con BCrypt)
-- IMPORTANTE: La contraseña 'admin123' se encriptará automáticamente por el backend al crear el usuario
INSERT INTO usuarios (id, nombre, email, password, rol, estado, fecha_creacion) VALUES
  (1, 'Administrador', 'admin@bellasincupa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.Ik0J/Hi/qv8lAfD5/8K4/xjDbp3Emu', 'superadmin', 'activo', NOW());

-- Categorías de ejemplo
INSERT INTO categoria (id, nombre) VALUES 
  (1, 'Ropa interior'),
  (2, 'Prendas superiores'),
  (3, 'Prendas inferiores'),
  (4, 'Conjuntos'),
  (5, 'Accesorios');

-- Productos de ejemplo (15) con stock, imagen_url y fecha_creacion
-- Nota: La columna categoria_id corresponde a la FK en Producto
-- Algunos productos tienen stock bajo para probar el endpoint /low-stock
INSERT INTO producto (id, nombre, descripcion, precio, stock, imagen_url, activo, categoria_id, fecha_creacion) VALUES
  (1, 'Ropa interior 1', 'Algodón cómodo para uso diario', 29.90, 15, 'https://example.com/images/ropa-interior-1.jpg', true, 1, NOW()),
  (2, 'Ropa interior 2', 'Clásico con costuras reforzadas', 29.90, 8, 'https://example.com/images/ropa-interior-2.jpg', true, 1, NOW()),
  (3, 'Ropa interior 3', 'Tela fresca y transpirable', 29.90, 3, 'https://example.com/images/ropa-interior-3.jpg', true, 1, NOW()),
  (4, 'Top Bella', 'Prenda superior suave y elástica', 49.90, 12, 'https://example.com/images/top-bella.jpg', true, 2, NOW()),
  (5, 'Crop top aire', 'Diseño juvenil para verano', 59.90, 2, 'https://example.com/images/crop-top-aire.jpg', true, 2, NOW()),
  (6, 'Polera básica', 'Algodón premium', 39.90, 20, 'https://example.com/images/polera-basica.jpg', true, 2, NOW()),
  (7, 'Short confort', 'Tejido suave que no irrita', 49.90, 7, 'https://example.com/images/short-confort.jpg', true, 3, NOW()),
  (8, 'Legging fit', 'Ajuste perfecto de alta elasticidad', 79.90, 1, 'https://example.com/images/legging-fit.jpg', true, 3, NOW()),
  (9, 'Pantalón relax', 'Combinación de algodón y elastano', 59.90, 10, 'https://example.com/images/pantalon-relax.jpg', true, 3, NOW()),
  (10, 'Conjunto clásico', 'Conjunto 2 piezas cómodo', 99.90, 6, 'https://example.com/images/conjunto-clasico.jpg', true, 4, NOW()),
  (11, 'Conjunto elegante', 'Detalles minimalistas', 129.90, 4, 'https://example.com/images/conjunto-elegante.jpg', true, 4, NOW()),
  (12, 'Conjunto sport', 'Estilo deportivo', 109.90, 9, 'https://example.com/images/conjunto-sport.jpg', true, 4, NOW()),
  (13, 'Pack calcetines', '3 pares algodón', 29.90, 25, 'https://example.com/images/pack-calcetines.jpg', true, 5, NOW()),
  (14, 'Cinturón fino', 'Accesorio versátil', 39.90, 0, 'https://example.com/images/cinturon-fino.jpg', true, 5, NOW()),
  (15, 'Scrunchie', 'Accesorio para el cabello', 19.90, 18, 'https://example.com/images/scrunchie.jpg', true, 5, NOW());

-- Información importante:
-- Usuario admin: admin@bellasincupa.com / admin123
-- Productos con stock bajo (< 5): IDs 3, 5, 8, 11, 14
-- Producto sin stock (= 0): ID 14
-- Precios actualizados a formato decimal (ej: 29.90 en lugar de 2990)