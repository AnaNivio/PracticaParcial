package com.example.PracticaParcial.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private String playerName;
    private String playerSurname;
    private String teamName;
}
