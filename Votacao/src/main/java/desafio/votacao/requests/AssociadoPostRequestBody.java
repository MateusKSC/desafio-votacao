package desafio.votacao.requests;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociadoPostRequestBody {
    @NotEmpty(message = "O nome do associado precisa ser informado!")
    private String nome;

    @NotEmpty(message = "O CPF precisa ser informado!")
    @Pattern(regexp = "^([0-9]{3}\\.?){3}-?[0-9]{2}$", message = "O CPF do" +
            " associado possui um formato inválido!")
    private String cpf;

}
