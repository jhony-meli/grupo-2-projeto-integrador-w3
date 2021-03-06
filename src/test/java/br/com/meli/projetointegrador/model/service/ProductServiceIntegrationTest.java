package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao product
 */

@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @BeforeEach
    void setUp(){
        clearBase();
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();

        Section sectionDois = new Section()
                .sectionCode("FR")
                .sectionName("Frios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();
        sectionRepository.saveAll(Arrays.asList(section, sectionDois));

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        SectionCategory sectionCategoryRF = new SectionCategory()
                .name(ESectionCategory.RF)
                .build();
        sectionCategoryRepository.save(sectionCategoryRF);

        Product product = new Product()
                .productId("LE")
                .productName("Leite")
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now())
                .category(sectionCategory)
                .build();
        productRepository.save(product);
    }

    @AfterEach
    void cleanUpDataBase(){
        clearBase();
    }

    @Test
    void findExistTest() {
        Product productReturn = productService.find("LE");
        assertEquals("LE", productReturn.getProductId());
    }

    @Test
    void findNotExistTest() {
        ProductException productException = assertThrows(ProductException.class, () ->
                productService.find("LK"));

        String expectedMessage = "Produto (" + "LK" + ") nao cadastrado!!! Por gentileza cadastrar";

        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    @Test
    void validListProductByCategoryTest(){
        assertEquals(1, productService.listProdutcByCategory(ESectionCategory.FF.toString()).size());
    }

    @Test
    void validListProductByCategoryTestEmpty(){
        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> productService.listProdutcByCategory("RF"));

        String mensagemEsperada = "Nao temos produtos nessa categoria " + ESectionCategory.RF.toString() + ", por favor informar a categoria correta!";
        String mensagemRecebida = productExceptionNotFound.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void findAllProducts() {
        assertFalse(productService.findAllProducts().isEmpty());
    }

    void clearBase() {
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        productRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
    }
}
