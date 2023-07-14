package br.com.banco.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.banco.models.Transferencia;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByContaIdConta(Long numeroConta);

    List<Transferencia> findByDataTransferenciaBetweenAndContaIdConta(OffsetDateTime dataInicio,OffsetDateTime dataFim, Long numeroConta);

    List<Transferencia> findByNomeOperadorTransacaoAndContaIdConta(String nomeOperador, Long numeroConta);

    List<Transferencia> findByDataTransferenciaBetweenAndNomeOperadorTransacaoAndContaIdConta(OffsetDateTime dataInicio,OffsetDateTime dataFim, String nomeOperador, Long numeroConta);

    @Query(value = "SELECT SUM(valor) FROM transferencia WHERE conta_id = :idConta", nativeQuery = true)
    Double getSaldoTotalTransacoesByIdConta(@Param("idConta") Long idConta);
    
}
