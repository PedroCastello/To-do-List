package com.todolist.todolist.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.todolist.todolist.dto.TarefaResponseDTO;
import com.todolist.todolist.entity.Tarefa;
import com.todolist.todolist.repository.TarefaRepository;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<TarefaResponseDTO> listarTodas() {
        return tarefaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TarefaResponseDTO buscarPorId(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarefa não encontrada com o id " + id));

        return toResponseDTO(tarefa);
    }


    private TarefaResponseDTO toResponseDTO(Tarefa tarefa) {
        TarefaResponseDTO responseDTO = new TarefaResponseDTO();
        responseDTO.setId(tarefa.getId());
        responseDTO.setTitulo(tarefa.getTitulo());
        responseDTO.setDescricao(tarefa.getDescricao());
        responseDTO.setStatus(tarefa.getStatus());
        responseDTO.setPrazo(tarefa.getPrazo());
        responseDTO.setCreatedAt(tarefa.getCreatedAt());
        return responseDTO;
    }

}
