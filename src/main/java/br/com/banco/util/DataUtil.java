package br.com.banco.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import br.com.banco.exceptions.NegocioException;

public class DataUtil {
    public static void validarData(String data) throws NegocioException {
        try {
            OffsetDateTime.parse(data);
        } catch (DateTimeParseException e) {
            throw new NegocioException("O offset de data fornecido não está em um formato válido.");
        }
    }

    public static void validarDuasDatas(String dataInicio, String dataFim) throws NegocioException {
        
        try {
            
            OffsetDateTime inicio = OffsetDateTime.parse(dataInicio.trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));
            OffsetDateTime fim = OffsetDateTime.parse(dataFim.trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));


            ZoneId zoneId = ZoneId.systemDefault();

            if (inicio.isAfter(fim)) {
                throw new NegocioException("A data de início é posterior à data de término.");
            }

            OffsetDateTime horarioAtual = OffsetDateTime.now(zoneId);
            
            if(fim.isAfter(horarioAtual)) {
                throw new NegocioException("A data de Fim é posterior à data de hoje");
            }
            
            
        } catch (DateTimeParseException e) {
            throw new NegocioException("Um dos offsets de data fornecidos não está em um formato válido.");
        }
    }
}
