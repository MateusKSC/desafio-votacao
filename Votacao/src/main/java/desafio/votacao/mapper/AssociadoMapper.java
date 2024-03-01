package desafio.votacao.mapper;

import desafio.votacao.entities.Associado;
import desafio.votacao.requests.AssociadoPostRequestBody;
import desafio.votacao.requests.AssociadoPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AssociadoMapper {
    public static final AssociadoMapper INSTANCE = Mappers.getMapper(AssociadoMapper.class);

    public abstract Associado toAssociado(AssociadoPostRequestBody associadoPostRequestBody);

    public abstract Associado toAssociado(AssociadoPutRequestBody associadoPutRequestBody);
}
