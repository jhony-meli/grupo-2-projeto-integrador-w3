package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o produto e seus atributos
 */

@Data
@Document(collection = "product")
@CompoundIndexes({
        @CompoundIndex(name = "product_productId", def = "{'productId' : 1}", unique = true)
})
public class Product {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String productId;

    private String productName;
    private BigDecimal productPrice;
    private LocalDate dueDate;

    @DBRef
    private SectionCategory category;

    /**
     * @author Jhony Zuim
     * Construcao de construtores fluentes para a classe produto
     */

    public Product id(String id) {
        this.id = id;
        return this;
    }

    public Product productId(String productId) {
        this.productId = productId;
        return this;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public Product productPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public Product dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Product category(SectionCategory category) {
        this.category = category;
        return this;
    }

    public Product build(){
        return this;
    }
}
