package desafio.votacao.v1.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Associado que é cadastrado para participar de pautas e sessoes de voto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="Associados")
public class Associado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O nome do associado precisa ser informado!")
    private String nome;

    @NotEmpty(message = "O CPF precisa ser informado!")
    @Pattern(regexp = "^([0-9]{3}\\.?){3}-?[0-9]{2}$", message = "O CPF do" +
            " associado possui um formato inválido!")
    private String cpf;

    @Builder.Default
    private boolean voto = true;

    public Long getId() {
        return id;
    }

    @ManyToMany(mappedBy = "associados")
    @JsonIgnore
    private List<Pauta> pautas = new ArrayList<>();
}