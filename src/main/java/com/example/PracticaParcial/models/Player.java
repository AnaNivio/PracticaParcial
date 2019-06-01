package com.example.PracticaParcial.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Player {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlayer;

    @NotNull(message = "Players MUST have a name")
    private String playersName;
    @NotNull(message = "Players MUST have a surname")
    private String playersSurname;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="idEquipo",referencedColumnName = "idEquipo")
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    @JsonBackReference
    @NotNull(message = "Players MUST have a team realeted to them") //por JSON Ignore, no se podia poner un null
    private Team team;
}
