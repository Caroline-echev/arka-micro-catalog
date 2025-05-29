
CREATE TABLE IF NOT EXISTS tb_products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    description TEXT,
    price NUMERIC(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    photo VARCHAR(512),
    brand_id BIGINT NOT NULL,
    CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES tb_brand(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS tb_product_categories (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES tb_products(id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES tb_category(id) ON DELETE CASCADE
);