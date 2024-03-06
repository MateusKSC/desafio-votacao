package desafio.votacao.v1.requests;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Request Body para post específico da Pauta
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaPostRequestBody {
    @NotEmpty(message = "O nome da pauta precisa ser informado!")
    private String nome;

    @NotEmpty(message = "A descricao da pauta precisa ser informada!")
    private String descricao;

}
