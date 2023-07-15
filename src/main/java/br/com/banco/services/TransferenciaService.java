package br.com.banco.services;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.banco.dto.TransferenciaDTO;
import br.com.banco.exceptions.NegocioException;
import br.com.banco.models.Transferencia;
import br.com.banco.repository.TransferenciaRepository;
import br.com.banco.util.DataUtil;
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

    public List<TransferenciaDTO> getTransferenciasByPeriodo(String dataInicioStr, String dataFimStr,
            Long numeroConta) {

        DataUtil.validarDuasDatas(dataInicioStr, dataFimStr);

        OffsetDateTime dataInicio = OffsetDateTime.parse(dataInicioStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));
        OffsetDateTime dataFim = OffsetDateTime.parse(dataFimStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));

        List<Transferencia> transferencias = transferenciaRepository
                .findByDataTransferenciaBetweenAndContaIdConta(dataInicio, dataFim, numeroConta);

        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações no período especificado.");
    }

    public List<TransferenciaDTO> getTransferenciasByNomeOperador(String nomeOperador, Long numeroConta) {

        List<Transferencia> transferencias = transferenciaRepository
                .findByNomeOperadorTransacaoAndContaIdConta(nomeOperador, numeroConta);

        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações para o operador " + nomeOperador);
    }

    public List<TransferenciaDTO> getTransferenciasByTodosFiltro(String dataInicioStr, String dataFimStr,
            String nomeOperador, Long numeroConta) {

        DataUtil.validarDuasDatas(dataInicioStr, dataFimStr);

        OffsetDateTime dataInicio = OffsetDateTime.parse(dataInicioStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));
        OffsetDateTime dataFim = OffsetDateTime.parse(dataFimStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));

        List<Transferencia> transferencias = transferenciaRepository
                .findByDataTransferenciaBetweenAndNomeOperadorTransacaoAndContaIdConta(dataInicio, dataFim,
                        nomeOperador, numeroConta);

        if (!transferencias.isEmpty()) {
            return convertToDTO(transferencias);
        }

        throw new NegocioException("Não existem transações para o período e operador especificados.");
    }

    public Double getSaldoTotalTransacoes(Long idConta) {

        if (idConta == null) {
            throw new NegocioException("Id conta não pode ser nulo.");
        }

        Double saldoTotal = transferenciaRepository.getSaldoTotalTransacoesByIdConta(idConta);

        if (saldoTotal != null) {
            return saldoTotal;
        }

        throw new NegocioException("Não foi possível obter o saldo total das suas transações.");
    }

    public List<TransferenciaDTO> convertToDTO(List<Transferencia> transferencias) {
        return transferencias.stream().map(TransferenciaDTO::new).collect(Collectors.toList());
    }

}
