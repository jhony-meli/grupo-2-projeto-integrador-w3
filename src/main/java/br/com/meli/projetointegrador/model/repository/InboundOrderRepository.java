package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.InboundOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository para trabalhar como uma porta ou janela de acesso a camada do banco da entity inboundOrder
 */

@Repository
public interface InboundOrderRepository extends MongoRepository<InboundOrder, String> {

    Optional<InboundOrder> findByOrderNumber(Integer orderNumber);

}