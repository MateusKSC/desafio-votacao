package desafio.votacao.mapper;

import desafio.votacao.entities.Pauta;
import desafio.votacao.requests.PautaPostRequestBody;
import desafio.votacao.requests.PautaPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class PautaMapper {
    public static final PautaMapper INSTANCE = Mappers.getMapper(PautaMapper.class);

    public abstract Pauta toPauta(PautaPostRequestBody pautaPostRequestBody);

    public abstract Pauta toPauta(PautaPutRequestBody pautaPutRequestBody);
}
