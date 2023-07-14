package br.com.banco.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IntervaloDeTempoDto {

    @NotNull(message = "Necessário informar o campo dataInicio.")
    @PastOrPresent(message = "O valor informado corresponde a uma data futura.")
    protected LocalDateTime dataInicio;

    @NotNull(message = "Necessário informar o campo dataFim.")
    @PastOrPresent(message = "O valor informado corresponde a uma data futura.")
    protected LocalDateTime dataFim;
}
