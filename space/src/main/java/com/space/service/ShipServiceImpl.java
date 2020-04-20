package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {
    @Override
    public void create(Ship ship) {

    }

    @Override
    public List<Ship> readAll() {
        return null;
    }

    @Override
    public Ship read(int id) {
        return null;
    }

    @Override
    public boolean update(Ship client, int id) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
