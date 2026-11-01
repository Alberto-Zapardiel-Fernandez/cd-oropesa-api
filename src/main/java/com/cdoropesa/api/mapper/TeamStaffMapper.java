package com.cdoropesa.api.mapper;

import com.cdoropesa.api.dto.TeamStaffResponse;
import com.cdoropesa.api.model.TeamStaff;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TeamStaffMapper {

  public TeamStaffResponse toResponse(TeamStaff assignment) {
    TeamStaffResponse response = new TeamStaffResponse();
    response.setId(assignment.getId());
    response.setCoachId(assignment.getCoach().getId());
    response.setFirstName(assignment.getCoach().getFirstName());
    response.setLastName(assignment.getCoach().getLastName());
    response.setRoleInTeam(TeamStaffResponse.RoleInTeamEnum.valueOf(assignment.getRoleInTeam().name()));
    return response;
  }

  public List<TeamStaffResponse> toResponseList(List<TeamStaff> assignments) {
    return assignments.stream().map(this::toResponse).toList();
  }
}
