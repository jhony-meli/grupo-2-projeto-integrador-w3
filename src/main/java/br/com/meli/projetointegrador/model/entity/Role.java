package br.com.meli.projetointegrador.model.entity;

import br.com.meli.projetointegrador.model.enums.ERole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o Role e seus atributos
 */

@Data
@Document(collection = "roles")
public class Role {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private ERole name;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

}
