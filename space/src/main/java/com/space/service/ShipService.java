package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    /**
     * Создает нового клиента
     * @param ship - клиент для создания
     */
    void create(Ship ship);

    /**
     * Возвращает список всех имеющихся клиентов
     * @return список клиентов
     */
    List<Ship> readAll();

    /**
     * Возвращает клиента по его ID
     * @param id - ID клиента
     * @return - объект клиента с заданным ID
     */
    Ship read(int id);

    /**
     * Обновляет клиента с заданным ID,
     * в соответствии с переданным клиентом
     * @param client - клиент в соответсвии с которым нужно обновить данные
     * @param id - id клиента которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(Ship client, int id);

    /**
     * Удаляет клиента с заданным ID
     * @param id - id клиента, которого нужно удалить
     * @return - true если клиент был удален, иначе false
     */
    boolean delete(int id);
}
