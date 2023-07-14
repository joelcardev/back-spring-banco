package br.com.banco.util;

import java.time.temporal.ChronoUnit;

import br.com.banco.dto.IntervaloDeTempoDto;
import br.com.banco.exceptions.NegocioException;



public class DataUtil {


    public static void validarIntervaloDeDatas(IntervaloDeTempoDto periodo) {

        if (periodo.getDataInicio().truncatedTo(ChronoUnit.DAYS)
                .isAfter(periodo.getDataFim().truncatedTo(ChronoUnit.DAYS))) {

            throw new NegocioException("Data fim menor que data início.");
        }

      
    }

}
