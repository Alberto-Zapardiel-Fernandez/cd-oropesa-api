package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.TeamPlayerResponse;
import com.cdoropesa.api.model.TeamPlayer;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TeamPlayerMapper {

  public TeamPlayerResponse toResponse(TeamPlayer teamPlayer) {
    TeamPlayerResponse response = new TeamPlayerResponse();
    response.setId(teamPlayer.getId());
    response.setPlayerId(teamPlayer.getPlayer().getId());
    response.setFirstName(teamPlayer.getPlayer().getFirstName());
    response.setLastName(teamPlayer.getPlayer().getLastName());
    response.setDorsal(teamPlayer.getDorsal());
    response.setStatus(TeamPlayerResponse.StatusEnum.valueOf(teamPlayer.getStatus().name()));
    response.setPositions(teamPlayer.getPositions().stream()
        .map(position -> TeamPlayerResponse.PositionsEnum.valueOf(position))
        .toList());
    return response;
  }

  public List<TeamPlayerResponse> toResponseList(List<TeamPlayer> teamPlayers) {
    return teamPlayers.stream().map(this::toResponse).toList();
  }
}
