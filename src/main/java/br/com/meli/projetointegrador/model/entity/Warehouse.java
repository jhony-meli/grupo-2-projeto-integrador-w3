package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Document(collection = "werehouse")
public class Warehouse {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String werehouseCode;
    private String werehouseName;
    private List<Section> listSections;

    public Warehouse id(String id) {
        this.id = id;
        return this;
    }

    public Warehouse warehouseCode(String warehouseCode) {
        this.werehouseCode = warehouseCode;
        return this;
    }

    public Warehouse warehouseName(String warehouseName) {
        this.werehouseName = warehouseName;
        return this;
    }

    public Warehouse build() {
        return this;
    }

}
