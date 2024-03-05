package desafio.votacao.v1.mapper;

import desafio.votacao.v1.entities.Associado;
import desafio.votacao.v1.requests.AssociadoPostRequestBody;
import desafio.votacao.v1.requests.AssociadoPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AssociadoMapper {
    public static final AssociadoMapper INSTANCE = Mappers.getMapper(AssociadoMapper.class);

    public abstract Associado toAssociado(AssociadoPostRequestBody associadoPostRequestBody);

    public abstract Associado toAssociado(AssociadoPutRequestBody associadoPutRequestBody);
}
