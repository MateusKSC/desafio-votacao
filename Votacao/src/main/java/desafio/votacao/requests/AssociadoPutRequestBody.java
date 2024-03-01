package desafio.votacao.requests;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociadoPutRequestBody {

    @NotEmpty(message = "O CPF precisa ser informado!")
    @Pattern(regexp = "^([0-9]{3}\\.?){3}-?[0-9]{2}$", message = "O CPF do" +
            " associado possui um formato inválido!")
    private String cpf;

    @NotNull(message = "O voto precisa ser informado!")
    private boolean voto;
}
