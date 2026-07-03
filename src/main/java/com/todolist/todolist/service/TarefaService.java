package com.todolist.todolist.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.todolist.todolist.dto.AtualizaStatusDTO;
import com.todolist.todolist.dto.TarefaRequestDTO;
import com.todolist.todolist.dto.TarefaResponseDTO;
import com.todolist.todolist.entity.Tarefa;
import com.todolist.todolist.entity.StatusTarefa;
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

    public TarefaResponseDTO criar(TarefaRequestDTO requestDTO) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(requestDTO.getTitulo());
        tarefa.setDescricao(requestDTO.getDescricao());
        tarefa.setPrazo(requestDTO.getPrazo());
        tarefa.setStatus(StatusTarefa.PENDENTE);

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        return toResponseDTO(tarefaSalva);
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO requestDTO) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarefa não encontrada com o id " + id));

        tarefaExistente.setTitulo(requestDTO.getTitulo());
        tarefaExistente.setDescricao(requestDTO.getDescricao());
        tarefaExistente.setPrazo(requestDTO.getPrazo());

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefaExistente);
        return toResponseDTO(tarefaAtualizada);
    }

    public TarefaResponseDTO atualizarStatus(Long id, AtualizaStatusDTO atualizaStatusDTO) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarefa não encontrada com o id " + id));

        tarefaExistente.setStatus(atualizaStatusDTO.getStatus());

        Tarefa tarefaAtualizada = tarefaRepository.save(tarefaExistente);
        return toResponseDTO(tarefaAtualizada);
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
