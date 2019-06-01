package com.example.PracticaParcial.controller;

import com.example.PracticaParcial.models.Player;
import com.example.PracticaParcial.models.Team;
import com.example.PracticaParcial.repositories.PlayerRepository;
import com.example.PracticaParcial.repositories.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sun.audio.AudioPlayer.player;

@RestController
@RequestMapping("/team")
public class TeamController {

    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("")
    public void createTeam(@RequestBody @Valid Team team){
        if(teamRepository.save(team).equals(null)){
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("An error has occurred while the team was created"));
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<Team> getAllTeams() {
        List<Team> foundTeams = new ArrayList<>();

        foundTeams=teamRepository.findAll();

        if(foundTeams.isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,String.format("There aren't any team uploaded yet"));
        }
        return foundTeams;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Integer id) {

        Team teamFound=teamRepository.findById(id).get();

        if(teamFound == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team with this id doesn't exist: %s",id));
        }

        return teamFound;


    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idEquipos}/team")
    public List<Player> getPlayersByIdTeam(@PathVariable Integer idTeam) {

        List<Player> playersTeam=new ArrayList<>();
        Optional<Team> team=teamRepository.findById(idTeam);

        if(team.get().equals(null)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team doesn't have players yet"));
        }else if(team.get().getPlayersList().isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,String.format("There aren't any players uploaded yet"));
        }

        return team.get().getPlayersList();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}")
    public void updateJugadores(@RequestBody @Valid Team team, @PathVariable Integer id){

        for(Team t: getAllTeams()){
            if(t.getIdTeam().equals(id)){
                t.setTeamsName(team.getTeamsName());
                t.setPlayersList(team.getPlayersList());

                if(teamRepository.save(t).equals(null)){
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("An error has occurred while the team was updated"));
                }
            }
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteJugadores(@PathVariable Integer id){
        for(Team t: getAllTeams()){
            if(t.getIdTeam().equals(id)){
                teamRepository.delete(t);
                if(!getTeamById(id).equals(null)){
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,String.format("An error has occurred while the team was deleted"));
                }
            }
        }
    }

}
