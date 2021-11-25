package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PurchaseOrderException;
import br.com.meli.projetointegrador.model.dto.OrderStatusDTO;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.dto.ProductPurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.PurchaseOrderDTO;
import br.com.meli.projetointegrador.model.entity.Buyer;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.PurchaseOrder;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao section
 */

class PurchaseOrderServiceTest {

    private final PurchaseOrderRepository mockPurchaseOrderRepository = mock(PurchaseOrderRepository.class);
    private final ProductService mockProductService = mock(ProductService.class);
    private final BuyerService mockBuyerService = mock(BuyerService.class);
    private final BatchStockService mockBatchStockService = mock(BatchStockService.class);
    private final PurchaseOrderService purchaseOrderService = new PurchaseOrderService(
            mockPurchaseOrderRepository, mockProductService, mockBuyerService,
            mockBatchStockService);

    @Test
    void productPriceTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.now().plusWeeks(3))
                .category(sectionCategory)
                .build();

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Collections.singletonList(product))
                .build();

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("LE")
                        .productName("LEITE")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022,11,30)));
        when(mockProductService.find(productPurchaseOrderDTO2.getProductId()))
                .thenReturn(new Product()
                        .productId("QJ")
                        .productName("QUEIJO")
                        .productPrice(new BigDecimal(3))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022,11,30)));
        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());
        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenReturn(new PurchaseOrder());
        when(mockPurchaseOrderRepository.findById(anyString()))
                .thenReturn(Optional.of(purchaseOrder));
        when(mockBatchStockService.dueDataProduct(any(LocalDate.class)))
                .thenReturn(true);

        BigDecimal total = purchaseOrderService.save(purchaseOrderDTO);
        assertEquals(new BigDecimal(19),total);
    }

    @Test
    void productPricePutTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .id("1")
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.now().plusWeeks(3))
                .category(sectionCategory)
                .build();

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Collections.singletonList(product))
                .build();

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("LE")
                        .productName("LEITE")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2021,11,30)));
        when(mockProductService.find(productPurchaseOrderDTO2.getProductId()))
                .thenReturn(new Product()
                        .productId("QJ")
                        .productName("Queijo")
                        .productPrice(new BigDecimal(3))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022,11,30)));
        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());
        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenReturn(new PurchaseOrder());
        when(mockBatchStockService.dueDataProduct(any(LocalDate.class)))
                .thenReturn(true);
        when(mockPurchaseOrderRepository.findById(anyString()))
                .thenReturn(Optional.of(purchaseOrder));

        BigDecimal total = purchaseOrderService.save(purchaseOrderDTO);
        assertEquals(new BigDecimal(19),total);
    }

    @Test
    void productPriceNoProductTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("FR")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("LE")
                        .productName("Leite")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2021, 1, 3)));
        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());
        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenReturn(new PurchaseOrder());
        when(mockBatchStockService.dueDataProduct(any(LocalDate.class)))
                .thenReturn(true);

        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class,() -> purchaseOrderService.save(purchaseOrderDTO));

        String mensagemEsperada = "Produto nao encontrado";
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void showOrderProduct() {
        AtomicReference<Boolean> valid = new AtomicReference<>(false);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.of(2021,11,30))
                .category(sectionCategory)
                .build();

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Collections.singletonList(product))
                .build();
        when(mockPurchaseOrderRepository.findById(anyString()))
                .thenReturn(Optional.of(purchaseOrder));

        List<ProductDTO> productDTOList = purchaseOrderService.showOrderProduct("LE");
        productDTOList.forEach(p -> {
            if (p.getProductId().equals(product.getProductId()) &
                p.getProductName().equals(product.getProductName()) &
                p.getCategory().equals(product.getCategory().getName()) &
                p.getProductPrice().equals(product.getProductPrice()) &
                p.getDueDate().equals(product.getDueDate())) {
                valid.set(true);
            }
        });
        assertFalse(productDTOList.isEmpty());
        assertTrue(valid.get());
    }

    @Test
    void showOrderProductEmptyList() {
        when(mockPurchaseOrderRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class,() -> purchaseOrderService.showOrderProduct("LE"));

        String mensagemEsperada = "Ordem de compra nao encontrada!!!";
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void calculeTotalDueDateInvalidTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("FR")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("LE")
                        .productName("Leite")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022, 12, 3)));

        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());

        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenReturn(new PurchaseOrder());

        when(mockBatchStockService.dueDataProduct(any(LocalDate.class)))
                .thenReturn(false);

        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class,() -> purchaseOrderService.save(purchaseOrderDTO));

        String mensagemEsperada = "Vencimento inferior a 3 semanas!!! Produto: " + "Leite" + " Vencimento: " + LocalDate.of(2022, 12, 3);
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void calculeTotalProductNotFoundTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("FR")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("CA")
                        .productName("carne")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2000, 12, 3)));

        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());

        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenReturn(new PurchaseOrder());

        when(mockBatchStockService.dueDataProduct(any(LocalDate.class)))
                .thenReturn(true);

        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class,() -> purchaseOrderService.save(purchaseOrderDTO));

        String mensagemEsperada = "Produto nao encontrado";
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void productPricePersistenceExceptionTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.now().plusWeeks(3))
                .category(sectionCategory)
                .build();

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Collections.singletonList(product))
                .build();

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("LE")
                        .productName("LEITE")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022,11,30)));
        when(mockProductService.find(productPurchaseOrderDTO2.getProductId()))
                .thenReturn(new Product()
                        .productId("QJ")
                        .productName("QUEIJO")
                        .productPrice(new BigDecimal(3))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022,11,30)));
        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());
        when(mockPurchaseOrderRepository.findById(anyString()))
                .thenReturn(Optional.of(purchaseOrder));
        when(mockBatchStockService.dueDataProduct(any(LocalDate.class)))
                .thenReturn(true);

        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenThrow(new DataAccessException("") {
                });

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        purchaseOrderService.save(purchaseOrderDTO));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void productPriceIdPersistenceExceptionTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .id("1")
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.now().plusWeeks(3))
                .category(sectionCategory)
                .build();

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Collections.singletonList(product))
                .build();

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("LE")
                        .productName("LEITE")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022,11,30)));
        when(mockProductService.find(productPurchaseOrderDTO2.getProductId()))
                .thenReturn(new Product()
                        .productId("QJ")
                        .productName("QUEIJO")
                        .productPrice(new BigDecimal(3))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .dueDate(LocalDate.of(2022,11,30)));
        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());
        when(mockPurchaseOrderRepository.findById(anyString()))
                .thenReturn(Optional.of(purchaseOrder));
        when(mockBatchStockService.dueDataProduct(any(LocalDate.class)))
                .thenReturn(true);

        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenThrow(new DataAccessException("") {
                });

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        purchaseOrderService.save(purchaseOrderDTO));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void deleteTest(){
        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.now().plusWeeks(3))
                .category(sectionCategory)
                .build();

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .id("111111111111111111111111")
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Collections.singletonList(product))
                .build();

        when(mockPurchaseOrderRepository.findById(anyString())).thenReturn(Optional.ofNullable(purchaseOrder));

        doNothing().when(mockPurchaseOrderRepository).deleteById(anyString());

        purchaseOrderService.delete("111111111111111111111111");

        verify(mockPurchaseOrderRepository, times(1))
                .deleteById(anyString());
    }

    @Test
    void ExceptionIDIncorretoDeleteTest() {
        doNothing().when(mockPurchaseOrderRepository).deleteById(anyString());

        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class, () -> purchaseOrderService.delete("11111111111111111111111"));

        String mensagemEsperada = "ID incorreto, o ID deve conter 24 caracteres!";
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void ExceptionIDInexistenteDeleteTest() {
        doNothing().when(mockPurchaseOrderRepository).deleteById(anyString());

        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class, () -> purchaseOrderService.delete("111111111121111111111111"));

        String mensagemEsperada = "Ordem de compra nao existe!!! Por gentileza inserir um ID existente!";
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }
}
