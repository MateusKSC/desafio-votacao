package desafio.votacao.v1.requests;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Request Body para put específico da Pauta
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaPutRequestBody {
    @NotNull(message = "O id da pauta precisa ser informado!")
    private Long id;

    @NotEmpty(message = "O nome da pauta precisa ser informado!")
    private String nome;

    @NotEmpty(message = "A descricao da pauta precisa ser informada!")
    private String descricao;
}
