package pl.hubertmaka.culinaryagent.mappers.impl;

import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Component;
import pl.hubertmaka.culinaryagent.domain.dtos.RecipeSchemaDto;
import pl.hubertmaka.culinaryagent.mappers.Mapper;

@Component
public class RecipeSchemaMapper implements Mapper<RecipeSchemaDto, String> {

    private final BeanOutputConverter<RecipeSchemaDto> converter;

    public RecipeSchemaMapper() {
        this.converter = new BeanOutputConverter<>(RecipeSchemaDto.class);
    }

    public BeanOutputConverter<RecipeSchemaDto> getConverter() {
        return converter;
    }

    public String getFormat() {
        return converter.getFormat();
    }

    @Override
    public RecipeSchemaDto mapFrom(String source) {
        return converter.convert(source);
    }

    @Override
    public String mapTo(RecipeSchemaDto target) {
        throw new UnsupportedOperationException("Mapping from RecipeSchemaDto to String is not supported");
    }
}

