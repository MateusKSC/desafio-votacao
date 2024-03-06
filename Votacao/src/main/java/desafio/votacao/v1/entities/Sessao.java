package desafio.votacao.v1.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Entidade Sessao responsável por representar o período do processo de votação
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="Sessoes")
public class Sessao {
    @PrePersist
    public void defineInstantes() {
        Date date = new Date();
        Date date2 = new Date(System.currentTimeMillis() + duracao*60000);
        this.momentoDoInicio = date;
        this.momentoDoFim = date2;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date momentoDoInicio;

    private Date momentoDoFim;

    @Transient
    @Builder.Default
    private int duracao = 1;

}
