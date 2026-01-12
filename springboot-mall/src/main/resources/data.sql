-- product

-- ==========================================
-- 新增書籍 (BOOK) - 10筆 (使用 Unsplash 真實圖片)
-- ==========================================
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('Java 程式設計寶典', 'BOOK', 'https://images.unsplash.com/photo-1580894732444-8ecded7900cd?auto=format&fit=crop&w=600&q=80', 680, 50, '適合初學者的 Java 入門書，涵蓋 Spring Boot 基礎。', '2023-01-10 10:00:00', '2023-01-10 10:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('Vue.js 前端開發實戰', 'BOOK', 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&w=600&q=80', 550, 30, '掌握 Vue 3 核心技術，輕鬆打造響應式網頁。', '2023-01-12 11:30:00', '2023-01-12 11:30:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('原子習慣', 'BOOK', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80', 350, 200, '每天進步 1%，一年強大 37 倍的複利效應。', '2023-02-01 09:00:00', '2023-02-01 09:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('被討厭的勇氣', 'BOOK', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=600&q=80', 300, 150, '阿德勒心理學，自我啟發的經典之作。', '2023-02-05 14:20:00', '2023-02-05 14:20:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('富爸爸，窮爸爸', 'BOOK', 'https://images.unsplash.com/photo-1579621970563-ebec7560ff3e?auto=format&fit=crop&w=600&q=80', 320, 80, '改變你對金錢的觀念，邁向財務自由。', '2023-03-10 16:00:00', '2023-03-10 16:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('哈利波特：神秘的魔法石', 'BOOK', 'https://images.unsplash.com/photo-1618666012174-83b441c0bc76?auto=format&fit=crop&w=600&q=80', 450, 60, 'J.K.羅琳經典奇幻小說系列第一集。', '2023-04-01 10:00:00', '2023-04-01 10:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('小王子', 'BOOK', 'https://images.unsplash.com/photo-1633477189729-9290b3261d0a?auto=format&fit=crop&w=600&q=80', 250, 100, '獻給長大後的大人，找回純真的感動。', '2023-04-15 13:45:00', '2023-04-15 13:45:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('人類大歷史', 'BOOK', 'https://images.unsplash.com/photo-1461360370896-922624d12aa1?auto=format&fit=crop&w=600&q=80', 520, 40, '從採集到現代，剖析人類歷史的重大進程。', '2023-05-20 09:30:00', '2023-05-20 09:30:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('演算法圖鑑', 'BOOK', 'https://images.unsplash.com/photo-1509228468518-180dd4864904?auto=format&fit=crop&w=600&q=80', 480, 25, '用圖解方式輕鬆學習資料結構與演算法。', '2023-06-01 11:00:00', '2023-06-01 11:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('投資金律', 'BOOK', 'https://images.unsplash.com/photo-1611974765270-ca1258822981?auto=format&fit=crop&w=600&q=80', 420, 35, '建立正確的長期投資觀念與資產配置。', '2023-06-15 15:30:00', '2023-06-15 15:30:00');
-- ==========================================
-- 新增食物 (FOOD) - 5筆 (使用 Unsplash 真實圖片)
-- ==========================================
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('澳洲 M9 和牛牛排', 'FOOD', 'https://images.unsplash.com/photo-1600891964092-4316c288032e?auto=format&fit=crop&w=600&q=80', 2500, 10, '頂級大理石紋油花，入口即化。', '2023-07-01 17:00:00', '2023-07-01 17:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('大湖新鮮草莓 (一籃)', 'FOOD', 'https://images.unsplash.com/photo-1464965911861-746a04b4bca6?auto=format&fit=crop&w=600&q=80', 350, 50, '冬季限定，酸甜好滋味。', '2023-07-05 08:00:00', '2023-07-05 08:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('北海道特選鮮乳', 'FOOD', 'https://images.unsplash.com/photo-1563636619-e9143da7973b?auto=format&fit=crop&w=600&q=80', 180, 60, '濃郁香醇，來自日本北國的純淨乳源。', '2023-07-10 09:00:00', '2023-07-10 09:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('有機雞蛋 (10入)', 'FOOD', 'https://images.unsplash.com/photo-1506976785307-8732e854ad03?auto=format&fit=crop&w=600&q=80', 120, 100, '人道飼養，健康無抗生素。', '2023-07-12 10:30:00', '2023-07-12 10:30:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('屏東金鑽鳳梨', 'FOOD', 'https://images.unsplash.com/photo-1550258987-190a2d41a8ba?auto=format&fit=crop&w=600&q=80', 80, 40, '果肉細緻，甜度高不咬舌。', '2023-07-15 11:00:00', '2023-07-15 11:00:00');
-- ==========================================
-- 新增汽車 (CAR) - 5筆 (使用 Unsplash 真實圖片)
-- ==========================================
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('Porsche 911', 'CAR', 'https://images.unsplash.com/photo-1503376763036-066120622c74?auto=format&fit=crop&w=600&q=80', 6500000, 1, '經典跑車代名詞，水平對臥引擎。', '2023-08-01 14:00:00', '2023-08-01 14:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('Ferrari 488 GTB', 'CAR', 'https://images.unsplash.com/photo-1592198084033-aade902d1aae?auto=format&fit=crop&w=600&q=80', 13000000, 1, '義大利紅鬃烈馬，V8 渦輪增壓。', '2023-08-05 16:30:00', '2023-08-05 16:30:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('Honda CR-V', 'CAR', 'https://images.unsplash.com/photo-1568844293986-8d04aad2b303?auto=format&fit=crop&w=600&q=80', 950000, 20, '家庭休旅首選，空間大省油。', '2023-08-10 09:00:00', '2023-08-10 09:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('Ford Kuga', 'CAR', 'https://images.unsplash.com/photo-1551830447-45ea720087f4?auto=format&fit=crop&w=600&q=80', 900000, 15, '歐系底盤，操控性佳的運動休旅。', '2023-08-12 10:00:00', '2023-08-12 10:00:00');
INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
VALUES ('Lamborghini Aventador', 'CAR', 'https://images.unsplash.com/photo-1544605995-285dc2515b22?auto=format&fit=crop&w=600&q=80', 27000000, 0, '極致超跑，V12 自然進氣引擎 (缺貨中)。', '2023-08-20 18:00:00', '2023-08-20 18:00:00');


-- user
INSERT INTO users (email, password, created_date, last_modified_date) VALUES ('admin@gmail.com', '698d51a19d8a121ce581499d7b701668', '2022-06-30 10:30:00', '2022-06-30 10:30:00');
INSERT INTO users (email, password, created_date, last_modified_date) VALUES ('user1@gmail.com', '698d51a19d8a121ce581499d7b701668', '2022-06-30 10:40:00', '2022-06-30 10:40:00');

-- order, order_item

