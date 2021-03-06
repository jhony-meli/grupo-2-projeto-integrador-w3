package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Agent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco da entity agent
 */

@DataMongoTest
class AgentRepositoryTest {

    @Autowired
    private AgentRepository agentRepository;

    @BeforeEach
    void setup() {
        agentRepository.deleteAll();
        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();
        agentRepository.save(agent);
    }

    @AfterEach
    void cleanUpDatabase() {
        agentRepository.deleteAll();
    }

    @Test
    void findByCpfTest() {
        Optional<Agent> agent = agentRepository.findByCpf("11122233344");
        assertTrue(agent.isPresent());
    }

}
