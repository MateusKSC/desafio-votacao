package desafio.votacao.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(mappedBy = "associados")
    @JsonIgnore
    private List<Pauta> pautas = new ArrayList<>();




    /*
    @ManyToMany(mappedBy = "authors")
    @JsonIgnoreProperties("authors")
    private List<Book> booksMade = new ArrayList<>();

     */
}