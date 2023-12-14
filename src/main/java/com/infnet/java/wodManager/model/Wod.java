package com.infnet.java.wodManager.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wod {
    private int id;

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @NotBlank(message = "O tipo não pode ser vazio.")
    private String tipo;

    @NotNull(message = "A duração é obrigatória.")
    @Positive(message = "A duração deve ser um número positivo.")
    private Integer duracaoMinutos;

    @NotBlank(message = "A categoria não pode ser vazia.")
    private String categoria;

    @NotEmpty(message = "A descrição é obrigatória.")
    private List<String> descricao;

    @NotBlank(message = "O score não pode ser vazio.")
    private String score;

    @NotNull(message = "A data é obrigatória.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate data;

    private String frase;

}
