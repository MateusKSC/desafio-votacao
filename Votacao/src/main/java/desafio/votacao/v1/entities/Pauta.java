package desafio.votacao.v1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="Pautas")
public class Pauta {
    @PrePersist
    public void criacaoDaPauta() {
        this.dataDeCriacao = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O nome da pauta precisa ser informado!")
    private String nome;

    @NotEmpty(message = "A descricao da pauta precisa ser informada!")
    private String descricao;

    private Date dataDeCriacao;

    @OneToOne(targetEntity = Sessao.class)
    private Sessao sessao;

    private String resultadoVotacao;

    private int votosPositivos;

    private int votosNegativos;

    @ManyToMany
    @JsonIgnore
    private List<Associado> associados = new ArrayList<>();


    @JsonIgnore
    private boolean sessaoIniciada;

    private boolean concluida;

    public void adicionaAssociados(Associado associado){
        this.associados.add(associado);
    }
    public void incrementaVotos(boolean tipoDeVoto) {
        if(tipoDeVoto) {
            this.votosPositivos++;
        }
        else{
            this.votosNegativos++;
        }
    }
    public void verificaResultadoVotacao(){
        if((this.votosPositivos - this.votosNegativos) > 0){
            this.resultadoVotacao = "Pauta Aprovada";
        } else if ((this.votosPositivos - this.votosNegativos) < 0) {
            this.resultadoVotacao = "Pauta Reprovada";
        } else if (this.votosPositivos == this.votosNegativos) {
            this.resultadoVotacao = "Empate na Votacao";
        }
    }

}