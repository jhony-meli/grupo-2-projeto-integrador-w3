package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 */

@Data
@Document(collection = "section")
public class Section {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String sectionCode;
    private String sectionName;
    private Integer maxLength;

    @DBRef(lazy = true)
    private List<BatchStock> batchStockList;

    /**
     * @author Jhony Zuim
     * @version 1.0.0
     * Construcao de construtores fluentes para a classe Section
     */

    public Section id(String id) {
        this.id = id;
        return this;
    }

    public Section sectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
        return this;
    }

    public Section sectionName(String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public Section maxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public Section build() {
        return this;
    }

}
