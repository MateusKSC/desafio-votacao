package desafio.votacao.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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
        Date date2 = new Date(System.currentTimeMillis() + 60000);
        this.momentoDoInicio = date;
        this.momentoDoFim = date2;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date momentoDoInicio;
    private Date momentoDoFim;

    public Date getMomentoDoInicio() {
        return momentoDoInicio;
    }

    public void setMomentoDoInicio(Date momentoDoInicio) {
        this.momentoDoInicio = momentoDoInicio;
    }

    public Date getMomentoDoFim() {
        return momentoDoFim;
    }

    public void setMomentoDoFim(Date momentoDoFim) {
        this.momentoDoFim = momentoDoFim;
    }

}
