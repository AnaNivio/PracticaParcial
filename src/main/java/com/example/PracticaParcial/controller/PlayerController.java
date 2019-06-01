package com.example.PracticaParcial.controller;

import com.example.PracticaParcial.models.Player;
import com.example.PracticaParcial.models.Team;
import com.example.PracticaParcial.repositories.TeamRepository;
import com.example.PracticaParcial.repositories.PlayerRepository;
import javafx.print.PageLayout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import sun.net.www.http.HttpClient;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("")
    public void createPlayers(@RequestBody @Valid Player player){

        if(playerRepository.save(player).equals(null)){
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("An error has occurred while the player was created"));
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<Player> getAllPlayres() {
        List<Player> foundPlayers = new ArrayList<>();

        foundPlayers=playerRepository.findAll();

        if(foundPlayers.isEmpty()){
            throw  new HttpClientErrorException(HttpStatus.NO_CONTENT,String.format("There aren't any players uploaded yet"));
        }
        return foundPlayers;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Integer id) {
        Player playerFound=playerRepository.findById(id).get();

        if(playerFound == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format("The player with this id doesn't exist: %s",id));
        }

        return playerFound;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}")
    public void updatePlayers(@RequestBody @Valid Player player, @PathVariable Integer id){

        for(Player j: getAllPlayres()){
            if(j.getIdPlayer().equals(id)){
                j.setPlayersName(player.getPlayersName());
                j.setPlayersSurname(player.getPlayersSurname());
                j.setTeam(player.getTeam());

                if(playerRepository.save(j).equals(null)){
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,String.format("An error has occurred while the players was updated"));
                }
            }
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deletePlayers(@PathVariable Integer id){
        for(Player j: getAllPlayres()){
            if(j.getIdPlayer().equals(id)){
                playerRepository.delete(j);

                if(!getPlayerById(id).equals(null)){
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,String.format("An error has occurred while the player was deleted"));
                }
            }


        }
    }





}
