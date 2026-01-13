CREATE TABLE IF NOT EXISTS product
(
    product_id         INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_name       VARCHAR(128) NOT NULL,
    category           VARCHAR(32)  NOT NULL,
    image_url          VARCHAR(256) NOT NULL,
    price              INT          NOT NULL,
    stock              INT          NOT NULL,
    description        VARCHAR(1024),
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    user_id            INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email              VARCHAR(256) NOT NULL UNIQUE,
    password           VARCHAR(256) NOT NULL,
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id           INT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id            INT       NOT NULL,
    user_name          VARCHAR(12),
    total_amount       INT       NOT NULL,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    status             VARCHAR(12) default '未付款'
);

CREATE TABLE IF NOT EXISTS order_item
(
    order_item_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id      INT NOT NULL,
    product_id    INT NOT NULL,
    `count`      INT NOT NULL, -- 商品數量
    amount        INT NOT NULL,  -- 商品花費
    order_id       INT   default 0
);

CREATE TABLE IF NOT EXISTS buy_item
(
    order_item_id INT NOT NULL default 1,
    user_id      INT NOT NULL,
    product_id    INT NOT NULL,
    `count`      INT NOT NULL, -- 商品數量
    amount        INT NOT NULL,  -- 商品花費
    order_id       INT   default 0
);