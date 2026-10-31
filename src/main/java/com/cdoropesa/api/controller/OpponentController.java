package com.cdoropesa.api.controller;

import com.cdoropesa.api.dto.OpponentRequest;
import com.cdoropesa.api.dto.OpponentResponse;
import com.cdoropesa.api.generated.OpponentsApi;
import com.cdoropesa.api.mapper.OpponentMapper;
import com.cdoropesa.api.model.Opponent;
import com.cdoropesa.api.service.OpponentService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OpponentController implements OpponentsApi {

  private final OpponentService opponentService;
  private final OpponentMapper opponentMapper;

  public OpponentController(OpponentService opponentService, OpponentMapper opponentMapper) {
    this.opponentService = opponentService;
    this.opponentMapper = opponentMapper;
  }

  @Override
  public ResponseEntity<List<OpponentResponse>> getAllOpponents(String search) {
    List<Opponent> opponents = this.opponentService.findOpponentsByName(search);
    return ResponseEntity.ok(this.opponentMapper.toResponseList(opponents));
  }

  @Override
  public ResponseEntity<OpponentResponse> createOpponent(OpponentRequest opponentRequest) {
    Opponent opponent = this.opponentMapper.toEntity(opponentRequest);
    Opponent saved = this.opponentService.saveOpponent(opponent);
    return ResponseEntity.status(HttpStatus.CREATED).body(this.opponentMapper.toResponse(saved));
  }

  @Override
  public ResponseEntity<OpponentResponse> getOpponentById(UUID id) {
    return this.opponentService.findOpponentById(id)
        .map(opponent -> ResponseEntity.ok(this.opponentMapper.toResponse(opponent)))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<OpponentResponse> updateOpponent(
      UUID id, OpponentRequest opponentRequest) {
    return this.opponentService.findOpponentById(id)
        .map(existingOpponent -> {
          Opponent updatedOpponent = this.opponentMapper.toEntity(opponentRequest);
          updatedOpponent.setId(existingOpponent.getId());
          updatedOpponent.setCreatedAt(existingOpponent.getCreatedAt());
          Opponent saved = this.opponentService.saveOpponent(updatedOpponent);
          return ResponseEntity.ok(this.opponentMapper.toResponse(saved));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<Void> deleteOpponent(UUID id) {
    if (this.opponentService.findOpponentById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    this.opponentService.deleteOpponent(id);
    return ResponseEntity.noContent().build();
  }
}
