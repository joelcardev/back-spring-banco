package br.com.banco.resources;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.banco.dto.TransferenciaDTO;
import br.com.banco.services.TransferenciaService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/transferencias")
public class TransferenciaResource {

    @Autowired
    private TransferenciaService transferenciaService;

    @GetMapping
    public ResponseEntity<List<TransferenciaDTO>> getTodasTransferencias() {

        List<TransferenciaDTO> transferencias = transferenciaService.getAllTransferencias();
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/conta/{numeroConta}")
    public ResponseEntity<List<TransferenciaDTO>> getTransferenciasByNumeroConta(@PathVariable Long numeroConta) {
        List<TransferenciaDTO> transferencias = transferenciaService.getTransferenciasByNumeroConta(numeroConta);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<TransferenciaDTO>> getTransferenciasByPeriodo(
            @RequestParam("dataInicio") String dataInicioStr, @RequestParam("dataFim") String dataFimStr,
            @RequestParam("numeroConta") Long numeroConta) {

        OffsetDateTime dataInicio = OffsetDateTime.parse(dataInicioStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));
        OffsetDateTime dataFim = OffsetDateTime.parse(dataFimStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));

        List<TransferenciaDTO> transferencias = transferenciaService.getTransferenciasByPeriodo(dataInicio, dataFim,
                numeroConta);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/operador")
    public ResponseEntity<List<TransferenciaDTO>> getTransferenciasByNomeOperador(
            @RequestParam("nomeOperador") String nomeOperador, @RequestParam("numeroConta") Long numeroConta) {
        
        List<TransferenciaDTO> transferencias = transferenciaService.getTransferenciasByNomeOperador(nomeOperador,
                numeroConta);
        return ResponseEntity.ok(transferencias);
    }

    @GetMapping("/todos-filtros")
    public ResponseEntity<List<TransferenciaDTO>> getTransferenciasAllFiltros(
            @RequestParam("dataInicio") String dataInicioStr, @RequestParam("dataFim") String dataFimStr,
            @RequestParam("nomeOperador") String nomeOperador, @RequestParam("numeroConta") Long numeroConta) {

        OffsetDateTime dataInicio = OffsetDateTime.parse(dataInicioStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));
        OffsetDateTime dataFim = OffsetDateTime.parse(dataFimStr.trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));

        List<TransferenciaDTO> transferencias = transferenciaService.getTransferenciasByTodosFiltro(dataInicio, dataFim,
                nomeOperador, numeroConta);
        return ResponseEntity.ok(transferencias);
    }
    
    @GetMapping("/saldo-total/{numeroConta}")
    public ResponseEntity<Double> getSaldoTotalTransacoes(@PathVariable Long numeroConta) {
        double saldoTotal = transferenciaService.getSaldoTotalTransacoes(numeroConta);
        return ResponseEntity.ok(saldoTotal);
    }


}
