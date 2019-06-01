package com.example.PracticaParcial.repositories;

import com.example.PracticaParcial.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player,Integer> {
}
