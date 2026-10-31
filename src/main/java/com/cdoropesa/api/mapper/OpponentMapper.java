package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.OpponentRequest;
import com.cdoropesa.api.dto.OpponentResponse;
import com.cdoropesa.api.model.Opponent;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpponentMapper {

  OpponentResponse toResponse(Opponent opponent);

  List<OpponentResponse> toResponseList(List<Opponent> opponents);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Opponent toEntity(OpponentRequest request);
}
