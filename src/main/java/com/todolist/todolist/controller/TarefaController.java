package com.todolist.todolist.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import com.todolist.todolist.dto.AtualizaStatusDTO;
import com.todolist.todolist.dto.TarefaRequestDTO;
import com.todolist.todolist.dto.TarefaResponseDTO;
import com.todolist.todolist.service.TarefaService;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

	private final TarefaService tarefaService;

	public TarefaController(TarefaService tarefaService) {
		this.tarefaService = tarefaService;
	}

	@GetMapping
	public ResponseEntity<List<TarefaResponseDTO>> listarTodas() {
		return ResponseEntity.ok(tarefaService.listarTodas());
	}

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criar(@Valid @RequestBody TarefaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaService.criar(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequestDTO requestDTO) {
        return ResponseEntity.ok(tarefaService.atualizar(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaResponseDTO> atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizaStatusDTO requestDTO) {
        return ResponseEntity.ok(tarefaService.atualizarStatus(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
