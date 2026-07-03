package com.todolist.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.todolist.todolist.dto.AtualizaStatusDTO;
import com.todolist.todolist.dto.TarefaRequestDTO;
import com.todolist.todolist.dto.TarefaResponseDTO;
import com.todolist.todolist.entity.StatusTarefa;
import com.todolist.todolist.entity.Tarefa;
import com.todolist.todolist.exception.TarefaNaoEncontradaException;
import com.todolist.todolist.repository.TarefaRepository;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

	@Mock
	private TarefaRepository tarefaRepository;

	@InjectMocks
	private TarefaService tarefaService;

	private Tarefa tarefaExistente;
	private TarefaRequestDTO tarefaRequestDTO;
	private AtualizaStatusDTO atualizaStatusDTO;

	@BeforeEach
	void setUp() {
		tarefaExistente = new Tarefa();
		tarefaExistente.setId(1L);
		tarefaExistente.setTitulo("Estudar Spring");
		tarefaExistente.setDescricao("Revisar service e controller");
		tarefaExistente.setStatus(StatusTarefa.PENDENTE);
		tarefaExistente.setPrazo(LocalDate.of(2026, 7, 10));
		tarefaExistente.setCreatedAt(LocalDateTime.of(2026, 7, 3, 10, 30));

		tarefaRequestDTO = new TarefaRequestDTO();
		tarefaRequestDTO.setTitulo("Novo título");
		tarefaRequestDTO.setDescricao("Nova descrição");
		tarefaRequestDTO.setPrazo(LocalDate.of(2026, 7, 20));

		atualizaStatusDTO = new AtualizaStatusDTO();
		atualizaStatusDTO.setStatus(StatusTarefa.CONCLUIDA);
	}

	@Test
	void listarTodasDeveRetornarTodasAsTarefasConvertidasEmDto() {
		Tarefa outraTarefa = new Tarefa();
		outraTarefa.setId(2L);
		outraTarefa.setTitulo("Outra tarefa");
		outraTarefa.setDescricao("Outra descrição");
		outraTarefa.setStatus(StatusTarefa.EM_ANDAMENTO);
		outraTarefa.setPrazo(LocalDate.of(2026, 7, 11));
		outraTarefa.setCreatedAt(LocalDateTime.of(2026, 7, 3, 11, 0));

		when(tarefaRepository.findAll()).thenReturn(List.of(tarefaExistente, outraTarefa));

		List<TarefaResponseDTO> resultado = tarefaService.listarTodas();

		assertThat(resultado).hasSize(2);
		assertThat(resultado.get(0).getId()).isEqualTo(1L);
		assertThat(resultado.get(0).getTitulo()).isEqualTo("Estudar Spring");
		assertThat(resultado.get(0).getStatus()).isEqualTo(StatusTarefa.PENDENTE);
		assertThat(resultado.get(1).getId()).isEqualTo(2L);
		assertThat(resultado.get(1).getStatus()).isEqualTo(StatusTarefa.EM_ANDAMENTO);
		verify(tarefaRepository).findAll();
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void buscarPorIdDeveRetornarTarefaQuandoExistir() {
		when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExistente));

		TarefaResponseDTO resultado = tarefaService.buscarPorId(1L);

		assertThat(resultado.getId()).isEqualTo(1L);
		assertThat(resultado.getTitulo()).isEqualTo("Estudar Spring");
		assertThat(resultado.getDescricao()).isEqualTo("Revisar service e controller");
		assertThat(resultado.getStatus()).isEqualTo(StatusTarefa.PENDENTE);
		assertThat(resultado.getPrazo()).isEqualTo(LocalDate.of(2026, 7, 10));
		assertThat(resultado.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 7, 3, 10, 30));
		verify(tarefaRepository).findById(1L);
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void buscarPorIdDeveLancarExcecaoQuandoNaoEncontrar() {
		when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> tarefaService.buscarPorId(99L))
				.isInstanceOf(TarefaNaoEncontradaException.class)
				.hasMessage("Tarefa não encontrada com o id 99");
		verify(tarefaRepository).findById(99L);
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void criarDeveSalvarTarefaComStatusPendente() {
		Tarefa tarefaSalva = new Tarefa();
		tarefaSalva.setId(3L);
		tarefaSalva.setTitulo(tarefaRequestDTO.getTitulo());
		tarefaSalva.setDescricao(tarefaRequestDTO.getDescricao());
		tarefaSalva.setStatus(StatusTarefa.PENDENTE);
		tarefaSalva.setPrazo(tarefaRequestDTO.getPrazo());
		tarefaSalva.setCreatedAt(LocalDateTime.of(2026, 7, 3, 12, 0));

		when(tarefaRepository.save(org.mockito.ArgumentMatchers.any(Tarefa.class))).thenReturn(tarefaSalva);

		TarefaResponseDTO resultado = tarefaService.criar(tarefaRequestDTO);

		assertThat(resultado.getId()).isEqualTo(3L);
		assertThat(resultado.getTitulo()).isEqualTo("Novo título");
		assertThat(resultado.getDescricao()).isEqualTo("Nova descrição");
		assertThat(resultado.getStatus()).isEqualTo(StatusTarefa.PENDENTE);
		assertThat(resultado.getPrazo()).isEqualTo(LocalDate.of(2026, 7, 20));
		assertThat(resultado.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 7, 3, 12, 0));

		verify(tarefaRepository).save(org.mockito.ArgumentMatchers.argThat(tarefa ->
				tarefa.getTitulo().equals("Novo título")
						&& tarefa.getDescricao().equals("Nova descrição")
						&& tarefa.getPrazo().equals(LocalDate.of(2026, 7, 20))
						&& tarefa.getStatus() == StatusTarefa.PENDENTE));
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void atualizarDeveAlterarTarefaExistente() {
		Tarefa tarefaAtualizada = new Tarefa();
		tarefaAtualizada.setId(1L);
		tarefaAtualizada.setTitulo(tarefaRequestDTO.getTitulo());
		tarefaAtualizada.setDescricao(tarefaRequestDTO.getDescricao());
		tarefaAtualizada.setStatus(StatusTarefa.PENDENTE);
		tarefaAtualizada.setPrazo(tarefaRequestDTO.getPrazo());
		tarefaAtualizada.setCreatedAt(tarefaExistente.getCreatedAt());

		when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExistente));
		when(tarefaRepository.save(tarefaExistente)).thenReturn(tarefaAtualizada);

		TarefaResponseDTO resultado = tarefaService.atualizar(1L, tarefaRequestDTO);

		assertThat(resultado.getId()).isEqualTo(1L);
		assertThat(resultado.getTitulo()).isEqualTo("Novo título");
		assertThat(resultado.getDescricao()).isEqualTo("Nova descrição");
		assertThat(resultado.getStatus()).isEqualTo(StatusTarefa.PENDENTE);
		assertThat(resultado.getPrazo()).isEqualTo(LocalDate.of(2026, 7, 20));
		verify(tarefaRepository).findById(1L);
		verify(tarefaRepository).save(tarefaExistente);
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void atualizarStatusDeveAlterarSomenteStatusDaTarefa() {
		Tarefa tarefaAtualizada = new Tarefa();
		tarefaAtualizada.setId(1L);
		tarefaAtualizada.setTitulo(tarefaExistente.getTitulo());
		tarefaAtualizada.setDescricao(tarefaExistente.getDescricao());
		tarefaAtualizada.setStatus(StatusTarefa.CONCLUIDA);
		tarefaAtualizada.setPrazo(tarefaExistente.getPrazo());
		tarefaAtualizada.setCreatedAt(tarefaExistente.getCreatedAt());

		when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefaExistente));
		when(tarefaRepository.save(tarefaExistente)).thenReturn(tarefaAtualizada);

		TarefaResponseDTO resultado = tarefaService.atualizarStatus(1L, atualizaStatusDTO);

		assertThat(resultado.getStatus()).isEqualTo(StatusTarefa.CONCLUIDA);
		assertThat(resultado.getTitulo()).isEqualTo("Estudar Spring");
		verify(tarefaRepository).findById(1L);
		verify(tarefaRepository).save(tarefaExistente);
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void atualizarStatusDeveLancarExcecaoQuandoNaoEncontrar() {
		when(tarefaRepository.findById(42L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> tarefaService.atualizarStatus(42L, atualizaStatusDTO))
				.isInstanceOf(TarefaNaoEncontradaException.class)
				.hasMessage("Tarefa não encontrada com o id 42");

		verify(tarefaRepository).findById(42L);
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void deletarDeveRemoverTarefaQuandoExistir() {
		when(tarefaRepository.existsById(1L)).thenReturn(true);

		tarefaService.deletar(1L);

		verify(tarefaRepository).existsById(1L);
		verify(tarefaRepository).deleteById(1L);
		verifyNoMoreInteractions(tarefaRepository);
	}

	@Test
	void deletarDeveLancarExcecaoQuandoNaoExistir() {
		when(tarefaRepository.existsById(7L)).thenReturn(false);

		assertThatThrownBy(() -> tarefaService.deletar(7L))
				.isInstanceOf(TarefaNaoEncontradaException.class)
				.hasMessage("Tarefa não encontrada com o id 7");

		verify(tarefaRepository).existsById(7L);
		verifyNoMoreInteractions(tarefaRepository);
	}
}