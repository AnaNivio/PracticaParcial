package com.example.PracticaParcial.controller;

import com.example.PracticaParcial.models.Player;
import com.example.PracticaParcial.models.PlayerDTO;
import com.example.PracticaParcial.models.Team;
import com.example.PracticaParcial.models.TeamDTO;
import com.example.PracticaParcial.repositories.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static sun.audio.AudioPlayer.player;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createTeam(@RequestBody @Valid Team team){

        try {
            teamRepository.save(team);
        } catch (DataIntegrityViolationException e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("An error has occurred while the team was created"));
        }
    }

    /*
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<Team> getAllTeams() {
        List<Team> foundTeams = new ArrayList<>();

        foundTeams=teamRepository.findAll();

        if(foundTeams.isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,String.format("There aren't any team uploaded yet"));
        }
        return foundTeams;

    }*/

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<TeamDTO> getAllTeamsModelMapper() {
        List<Team> foundTeams = teamRepository.findAll();

        if(foundTeams.isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,String.format("There aren't any team uploaded yet"));
        }
        return foundTeams.stream()
                .map(team -> convertToDto(team))
                .collect(Collectors.toList());

    }

    /*
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Integer id) {

        return teamRepository.findById(id).orElseThrow(()->
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team with this id doesn't exist: %s",id)));

    }*/


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public TeamDTO getTeamByIdModelMapper(@PathVariable Integer id) {

        return convertToDto(teamRepository.findById(id).orElseThrow(()->
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team with this id doesn't exist: %s",id))));

    }
    

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateTeam(@PathVariable("id") Integer idTeam){
        Team teamFound= teamRepository.findById(idTeam).orElseThrow(()->
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team doesn't exist")));

        teamRepository.save(teamFound);
        
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable("id") Integer idTeam){
        Team teamFound= teamRepository.findById(idTeam).orElseThrow(()->
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team doesn't exist")));

        teamRepository.delete(teamFound);

    }



    private TeamDTO convertToDto(Team team){
        return modelMapper.map(team,TeamDTO.class);
    }

    private Team convertToEntity(TeamDTO teamDTO) throws ParseException {
        return modelMapper.map(teamDTO,Team.class);
    }
}
