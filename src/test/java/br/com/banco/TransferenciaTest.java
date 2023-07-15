package br.com.banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.banco.dto.TransferenciaDTO;
import br.com.banco.exceptions.NegocioException;
import br.com.banco.models.Conta;
import br.com.banco.models.Transferencia;
import br.com.banco.repository.TransferenciaRepository;
import br.com.banco.services.TransferenciaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TransferenciaTest {

    private TransferenciaRepository transferenciaRepository;

    private TransferenciaService transferenciaService;

    private static String dataFimStr = "2022-08-01 00:00:00 -02:00";

    private static String dataTransferenciaStr = "2022-08-01 00:00:00 -02:00";

    private static String dataInicioStr = "2019-01-01 00:00:00 -02:00";

    OffsetDateTime dataInicio;
    OffsetDateTime dataFim;
    OffsetDateTime dataTransferencia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transferenciaRepository = Mockito.mock(TransferenciaRepository.class);
        transferenciaService = new TransferenciaService(transferenciaRepository);

        dataTransferencia = OffsetDateTime.parse(dataInicioStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));

        dataInicio = OffsetDateTime.parse(dataInicioStr.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));

        dataFim = OffsetDateTime.parse(dataFimStr.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));

    }

    private Transferencia criarTransferencia(Long id, OffsetDateTime dataTransferencia, Double valor, String tipo,
            String nomeOperador, Conta conta) {
        Transferencia transferencia = new Transferencia();
        transferencia.setId(id);
        transferencia.setDataTransferencia(dataTransferencia);
        transferencia.setValor(valor);
        transferencia.setTipo(tipo);
        transferencia.setNomeOperadorTransacao(nomeOperador);
        transferencia.setConta(conta);
        return transferencia;
    }

    private Conta criarConta(Long id) {
        Conta conta = new Conta();
        conta.setIdConta(id);
        return conta;
    }

    @Test
    void testGetAllTransferencias() {
        List<Transferencia> transferencias = new ArrayList<>();
        transferencias
                .add(criarTransferencia(1L, OffsetDateTime.now(), 100.0, "DEPOSITO", "Operador 1", criarConta(1L)));
        when(transferenciaRepository.findAll()).thenReturn(transferencias);

        List<TransferenciaDTO> result = transferenciaService.getAllTransferencias();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(transferenciaRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTransferenciasSemTransacoes() {
        when(transferenciaRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getAllTransferencias();
        });

        verify(transferenciaRepository, times(1)).findAll();
    }

    @Test
    void testGetTransferenciasByNumeroContaSucesso() {
        Long numeroConta = 123456789L;

        List<Transferencia> transferencias = new ArrayList<>();
        transferencias.add(
                criarTransferencia(1L, OffsetDateTime.now(), 100.0, "DEPOSITO", "Operador 1", criarConta(123456789L)));
        transferencias.add(
                criarTransferencia(2L, OffsetDateTime.now(), 200.0, "DEPOSITO", "Operador 2", criarConta(123454569L)));
        when(transferenciaRepository.findByContaIdConta(numeroConta)).thenReturn(transferencias);

        List<TransferenciaDTO> result = transferenciaService.getTransferenciasByNumeroConta(numeroConta);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void testGetTransferenciasByNumeroContaNullErro() {
        Long numeroConta = null;

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByNumeroConta(numeroConta);
        });

    }

    @Test
    void testGetTransferenciasByNumeroContaNaoEncontradoErro() {
        Long numeroConta = 123456789L;

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByNumeroConta(numeroConta);
        });

    }

    @Test
    void testGetTransferenciasByPeriodoSucesso() {
        Long numeroConta = 123456789L;

        List<Transferencia> transferencias = new ArrayList<>();
        transferencias.add(
                criarTransferencia(1L, dataTransferencia, 100.0, "DEPOSITO", "Operador 1", criarConta(numeroConta)));
        when(transferenciaRepository.findByDataTransferenciaBetweenAndContaIdConta(dataInicio, dataFim, numeroConta))
                .thenReturn(transferencias);

        List<TransferenciaDTO> result = transferenciaService.getTransferenciasByPeriodo(dataInicioStr, dataFimStr,
                numeroConta);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(transferenciaRepository, times(1)).findByDataTransferenciaBetweenAndContaIdConta(dataInicio, dataFim,
                numeroConta);
    }

    @Test
    void testGetTransferenciasByPeriodoNaoEncontradoErro() {

        Long numeroConta = 123456789L;

        String dataInicioStr = "2019-01-01 00:00:00 -02:00";
        String dataFimStr = "2019-01-01 00:00:00 -02:00";

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByPeriodo(dataInicioStr, dataFimStr, numeroConta);
        });

    }

    @Test
    public void testGetTransferenciasByPeriodoDataInvalida() {
        String dataInicioStr = "2023-07-15T12:34:56";
        String dataFimStr = "2023-07-20";

        assertThrows(NegocioException.class,
                () -> transferenciaService.getTransferenciasByPeriodo(dataInicioStr, dataFimStr, 12345L));
    }


    @Test
    void testGetTransferenciasByPeriodoNumeroContaNull() {

        Long numeroConta = null;
        String dataInicioStr = "2019-01-01 00:00:00 -02:00";
        String dataFimStr = "2019-01-01 00:00:00 -02:00";

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByPeriodo(dataInicioStr, dataFimStr, numeroConta);
        });

    }

    @Test
    void testGetTransferenciasByNomeOperadorSucesso() {
        String nomeOperador = "Operador 1";
        Long numeroConta = 123456789L;

        List<Transferencia> transferencias = new ArrayList<>();
        transferencias.add(
                criarTransferencia(1L, OffsetDateTime.now(), 100.0, "DEPOSITO", "Operador 1", criarConta(123456789L)));
        when(transferenciaRepository.findByNomeOperadorTransacaoAndContaIdConta(nomeOperador, numeroConta))
                .thenReturn(transferencias);

        List<TransferenciaDTO> result = transferenciaService.getTransferenciasByNomeOperador(nomeOperador, numeroConta);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(transferenciaRepository, times(1)).findByNomeOperadorTransacaoAndContaIdConta(nomeOperador, numeroConta);
    }

    @Test
    void testGetTransferenciasByNomeOperadorNaoEncontradoErro() {
        String nomeOperador = "Operador 1";
        Long numeroConta = 123456789L;

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByNomeOperador(nomeOperador, numeroConta);
        });
    }

    @Test
    void testGetTransferenciasByNomeOperadorNomeOperadorNull() {
        Long numeroConta = 123456789L;

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByNomeOperador(null, numeroConta);
        });
    }

    @Test
    void testGetTransferenciasByNomeOperadorNumeroContaNull() {
        String nomeOperador = "Operador 1";
        Long numeroConta = null;
        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByNomeOperador(nomeOperador, numeroConta);
        });
    }

    @Test
    void testGetTransferenciasByTodosFiltroSucesso() {
        String nomeOperador = "Operador 1";
        Long numeroConta = 123456789L;

        List<Transferencia> transferencias = new ArrayList<>();
        transferencias.add(criarTransferencia(1L, OffsetDateTime.now().minusDays(5), 100.0, "DEPOSITO", "Operador 1",
                criarConta(123456789L)));
        transferencias.add(criarTransferencia(2L, OffsetDateTime.now().minusDays(5), 150.0, "DEPOSITO", "Operador 2",
                criarConta(123456710L)));

        when(transferenciaRepository.findByDataTransferenciaBetweenAndNomeOperadorTransacaoAndContaIdConta(dataInicio,
                dataFim, nomeOperador, numeroConta)).thenReturn(transferencias);

        List<TransferenciaDTO> result = transferenciaService.getTransferenciasByTodosFiltro(dataInicioStr, dataFimStr,
                nomeOperador, numeroConta);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());

        verify(transferenciaRepository, times(1)).findByDataTransferenciaBetweenAndNomeOperadorTransacaoAndContaIdConta(
                dataInicio, dataFim, nomeOperador, numeroConta);
    }

    @Test
    void testGetTransferenciasByTodosOsFiltrosErroQuandoNumeroContaNaoNull() {

        String nomeOperador = "operador 3";

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByTodosFiltro(dataInicioStr, dataFimStr, nomeOperador, null);
        });

    }

    @Test
    void testGetTransferenciasByTodosOsFiltrosErroQuandoNaoEncontradoTransacao() {

        Long numeroConta = 123456789L;

        String nomeOperador = "operador 3";

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getTransferenciasByTodosFiltro(dataInicioStr, dataFimStr, nomeOperador, numeroConta);
        });

    }



    @Test
    public void testGetTransferenciasByTodosFiltroDataInvalida() {

        String dataInicioStr = "2023-07-15T12:34:56";
        String dataFimStr = "2023-07-20";
        String nomeOperador = "Fulano";

        assertThrows(NegocioException.class, () -> transferenciaService.getTransferenciasByTodosFiltro(dataInicioStr,
                dataFimStr, nomeOperador, 12345L));
    }

    @Test
    void testGetSaldoTotalTransferenciasByIdContaSucesso() {

        Long numeroConta = 145456789L;

        Double saldoTotalEsperado = 250.0;

        List<Transferencia> transferencias = new ArrayList<>();
        transferencias.add(criarTransferencia(1L, OffsetDateTime.now().minusDays(5), 100.0, "DEPOSITO", "Operador 1",
                criarConta(123456789L)));
        transferencias.add(criarTransferencia(2L, OffsetDateTime.now().minusDays(5), 150.0, "DEPOSITO", "Operador 2",
                criarConta(123456789L)));

        when(transferenciaRepository.getSaldoTotalTransacoesByIdConta(numeroConta)).thenReturn(250.0);

        Double saldoTotalResult = transferenciaService.getSaldoTotalTransacoes(numeroConta);

        assertEquals(saldoTotalEsperado, saldoTotalResult);

    }

    @Test
    void testGetSaldoTotalTransferenciasByIdContaQuandoEleNull() {

        Long numeroConta = null;

        assertThrows(NegocioException.class, () -> {
            transferenciaService.getSaldoTotalTransacoes(numeroConta);
        });

    }

}
