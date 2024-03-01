package desafio.votacao.requests;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaPutRequestBody {
    private Long id;

    @NotEmpty(message = "O nome da pauta não pode ser vazia!")
    private String nome;

    @NotEmpty(message = "A descricao da pauta não pode ser vazia!")
    private String descricao;
}
