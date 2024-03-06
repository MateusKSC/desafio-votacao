package desafio.votacao.v1.mapper;

import desafio.votacao.v1.entities.Pauta;
import desafio.votacao.v1.requests.PautaPostRequestBody;
import desafio.votacao.v1.requests.PautaPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
/**
 * Classe mapper específica para a Pauta
 */
@Mapper(componentModel = "spring")
public abstract class PautaMapper {
    public static final PautaMapper INSTANCE = Mappers.getMapper(PautaMapper.class);

    public abstract Pauta toPauta(PautaPostRequestBody pautaPostRequestBody);

    public abstract Pauta toPauta(PautaPutRequestBody pautaPutRequestBody);
}
