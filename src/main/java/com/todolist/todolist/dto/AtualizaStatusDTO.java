package com.todolist.todolist.dto;

import com.todolist.todolist.entity.StatusTarefa;

import jakarta.validation.constraints.NotNull;

public class AtualizaStatusDTO {

    @NotNull(message = "O status é obrigatório")
    private StatusTarefa status;

    public AtualizaStatusDTO() {
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }
}