package br.com.banco.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.banco.dto.TransferenciaDTO;
import br.com.banco.exceptions.NegocioException;
import br.com.banco.models.Transferencia;
import br.com.banco.repository.TransferenciaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransferenciaService {

    @Autowired
    TransferenciaRepository transferenciaRepository;

    public List<TransferenciaDTO> getAllTransferencias() {
        List<Transferencia> transferencias = transferenciaRepository.findAll();
        
        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações disponíveis.");
    }

    public List<TransferenciaDTO> getTransferenciasByNumeroConta(Long numeroConta) {
        List<Transferencia> transferencias = transferenciaRepository.findByContaIdConta(numeroConta);
        
        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações para a conta de número " + numeroConta);
    }

    public List<TransferenciaDTO> getTransferenciasByPeriodo(OffsetDateTime dataInicio, OffsetDateTime dataFim, Long numeroConta) {

        List<Transferencia> transferencias = transferenciaRepository.findByDataTransferenciaBetweenAndContaIdConta(dataInicio,
                dataFim, numeroConta);

        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações no período especificado.");
    }

    public List<TransferenciaDTO> getTransferenciasByNomeOperador(String nomeOperador, Long numeroConta) {
        
        List<Transferencia> transferencias = transferenciaRepository.findByNomeOperadorTransacaoAndContaIdConta(nomeOperador, numeroConta);
        
        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações para o operador " + nomeOperador);
    }

    public List<TransferenciaDTO> getTransferenciasByTodosFiltro(OffsetDateTime dataInicio, OffsetDateTime dataFim, String nomeOperador, Long numeroConta) {
        List<Transferencia> transferencias = transferenciaRepository.findByDataTransferenciaBetweenAndNomeOperadorTransacaoAndContaIdConta(dataInicio, dataFim, nomeOperador, numeroConta);
        
        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações para o período e operador especificados.");
    }

    

    public Double getSaldoTotalTransacoes(Long idConta) {
        Double saldoTotal = transferenciaRepository.getSaldoTotalTransacoesByIdConta(idConta);
        
        if (saldoTotal != null) {
            return saldoTotal;
        }
        
        throw new NegocioException("Não foi possível obter o saldo total das suas transações.");
    }
    
    private List<TransferenciaDTO> convertToDTO(List<Transferencia> transferencias) {
        return transferencias.stream()
                .map(TransferenciaDTO::new)
                .collect(Collectors.toList());
    }
    
}
