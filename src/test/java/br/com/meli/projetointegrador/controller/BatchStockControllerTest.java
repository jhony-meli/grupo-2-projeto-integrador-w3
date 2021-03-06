package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.LoginRequest;
import br.com.meli.projetointegrador.model.dto.SignupRequest;
import br.com.meli.projetointegrador.model.dto.TokenTest;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ERole;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
class BatchStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private SectionByCategoryRepository sectionByCategoryRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TokenTest tokenTest = new TokenTest();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @BeforeEach
    void setUp() throws Exception {
        clearBase();
        createData();

        Set<String> roles = new HashSet<>();
        roles.add(ERole.ROLE_USER.toString());
        roles.add(ERole.ROLE_ADMIN.toString());
        roles.add(ERole.ROLE_MODERATOR.toString());
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("lucas");
        signupRequest.setEmail("lucas@gmail.com");
        signupRequest.setPassword("12345678");
        signupRequest.setRole(roles);
        signupRequest.setCpf("11122233344");
        signupRequest.setWarehouseCode("SP");
        mockMvc.perform(post("http://localhost:8080/api/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andReturn().getResponse();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("lucas");
        loginRequest.setPassword("12345678");
        MockHttpServletResponse responseSignin = mockMvc.perform(post("http://localhost:8080/api/auth/signin")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String tokenNoFormat = responseSignin.getContentAsString();
        tokenTest = objectMapper.readValue(tokenNoFormat, TokenTest.class);
    }

    @AfterEach
    void tearDown() {
        clearBase();
    }

    @Test
    void getProductOrder() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/listsorder/")
                .param("product","LE")
                .param("order","L")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getProductById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/lists/")
                .param("productId", "LE")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getProductOrderDyas() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/due-date/list/")
                .param("days","30")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getProductOrderDCO() throws Exception {
        setUp();
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/due-date/lists/")
                .param("days","30")
                .param("category","FS")
                .param("order","asc")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    void createData() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("22233344411").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agent);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now())
                .category(sectionCategory)
                .build();
        productRepository.save(product);

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2022,  12,  1))
                .section(section)
                .agent(agent)
                .build();
        batchStockRepository.save(batchStock);

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now().plusWeeks(+2))
                .category(sectionCategory)
                .build();
        productRepository.save(productDois);

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+2))
                .section(section)
                .agent(agent)
                .build();
        batchStockRepository.save(batchStockDois);

        SectionByCategory sectionByCategory = new SectionByCategory()
                .category(sectionCategory)
                .section(section)
                .build();
        sectionByCategoryRepository.save(sectionByCategory);

        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        roleRepository.save(role);

        Role role2 = new Role();
        role.setName(ERole.ROLE_MODERATOR);
        roleRepository.save(role2);

        Role role3 = new Role();
        role.setName(ERole.ROLE_ADMIN);
        roleRepository.save(role3);
    }

    void clearBase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        sectionRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
        warehouseRepository.deleteAll();
        productRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
    }
}