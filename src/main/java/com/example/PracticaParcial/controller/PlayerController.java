package com.example.PracticaParcial.controller;

import com.example.PracticaParcial.models.Player;
import com.example.PracticaParcial.models.PlayerDTO;
import com.example.PracticaParcial.models.Team;
import com.example.PracticaParcial.repositories.TeamRepository;
import com.example.PracticaParcial.repositories.PlayerRepository;
import javafx.print.PageLayout;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import sun.net.www.http.HttpClient;
import static java.util.Objects.isNull;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createPlayers(@RequestBody @Valid Player player){

        try {
            Player playerCreated= playerRepository.save(player);

        } catch (DataIntegrityViolationException e) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("An error has occurred while the player was created"));
        }

    }


    /*@ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<Player> getAllPlayres(@Param("idTeam") Integer idTeam) {

            if(!isNull(idTeam)){
            Team team= teamRepository.findById(idTeam).orElseThrow(()->
                    new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team doesn't exist")));

            List<Player> playersTeam=team.getPlayersList();

            if(team.getPlayersList().isEmpty()){
                throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,String.format("There aren't any players uploaded in this team yet"));
            }

            return playersTeam;
        }

        List<Player> foundPlayers = playerRepository.findAll();

        if(foundPlayers.isEmpty()){
,            throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,"There aren't any players uploaded yet");
        }
        return foundPlayers;
    }*/

    //localhost:8080/player?idTeam=1 //asi debe ser la llamada ssi se quiere saber los jugadores de un equipo determinado
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<PlayerDTO> getAllPlayersModelMapper(@Param("idTeam") Integer idTeam) {

        //filtro:Jugadores de un equipo con x idTeam
        if(!isNull(idTeam)){
            Team team= teamRepository.findById(idTeam).orElseThrow(()->
                    new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The team doesn't exist")));

            List<Player> playersTeam=team.getPlayersList();

            if(team.getPlayersList().isEmpty()){
                throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,String.format("There aren't any players uploaded in this team yet"));
            }

            return playersTeam.stream()
                    .map(player -> convertToDto(player))
                    .collect(Collectors.toList());
        }

        List<Player> foundPlayers = playerRepository.findAll();

        if(foundPlayers.isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,"There aren't any players uploaded yet");
        }
        return foundPlayers.stream()
                .map(player -> convertToDto(player))
                .collect(Collectors.toList());
    }

    /*
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Integer id) {
        return playerRepository.findById(id).orElseThrow(()->new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The player with this id doesn't exist: %s",id)));
    }*/

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public PlayerDTO getPlayerByIdModelMapper(@PathVariable Integer id) {
        return convertToDto(playerRepository.findById(id).orElseThrow(()->new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The player with this id doesn't exist: %s",id))));

    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updatePlayers(@PathVariable Integer idPlayer){
        Player playerFound = playerRepository.findById(idPlayer).orElseThrow(()->new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The player doesn't exist")));
        playerRepository.save(playerFound);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deletePlayers(@PathVariable Integer idPlayer){
        Player playerFound = playerRepository.findById(idPlayer).orElseThrow(()->new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The player doesn't exist")));

        playerRepository.delete(playerFound);
    }

    private PlayerDTO convertToDto(Player player){
        return modelMapper.map(player,PlayerDTO.class);
    }

    private Player convertToEntity(PlayerDTO playerDTO) throws ParseException {
        return modelMapper.map(playerDTO,Player.class);
    }





}
