package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do product
 */

@Data
public class ProductDTO {

    private String productId;
    private String productName;
    private String sectionName;

    public ProductDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public ProductDTO productName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductDTO sectionName(String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public ProductDTO build(){
        return this;
    }
}