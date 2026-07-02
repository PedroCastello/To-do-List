package com.todolist.todolist.entity;

public enum StatusTarefa {

    PENDENTE("PENDENTE"),
    EM_ANDAMENTO("EM_ANDAMENTO"),
    CONCLUIDA("CONCLUIDA");

    private final String status;

    StatusTarefa(String status) {
        this.status = status;
    }
}
