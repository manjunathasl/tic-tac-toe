package com.game.tictactoe.controller;

import com.game.tictactoe.dto.GameDTO;
import com.game.tictactoe.dto.MoveDTO;
import com.game.tictactoe.dto.PointDTO;
import com.game.tictactoe.entity.Game;
import com.game.tictactoe.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin //TODO: remove once dockerized
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable String id) {

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Invalid input, please provide the game id");
        }
        var game = this.gameService.getGame(id);
        return new ResponseEntity<Game>(game, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Game> createGame(@RequestBody GameDTO gameDto) {

        if (gameDto == null) {
            throw new IllegalArgumentException("Invalid input, please provide input data");
        }
        Game game = this.gameService.createGame(gameDto);
        return new ResponseEntity<Game>(game, HttpStatus.OK);
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<Game> move(@PathVariable String id, @RequestBody MoveDTO moveDto) {

        if (moveDto == null) {
            throw new IllegalArgumentException("Invalid input, please provide input data");
        }
        var game = this.gameService.move(id, moveDto);
        return new ResponseEntity<Game>(game, HttpStatus.OK);
    }

    @GetMapping("/{id}/move")
    public ResponseEntity<PointDTO> findBestMove(@PathVariable String id, @RequestParam int player) {

        if (player < 0) {
            throw new IllegalArgumentException("Invalid input, please provide input data");
        }

        var position = this.gameService.findNextMove(id, player);
       
        return new ResponseEntity<PointDTO>(position, HttpStatus.OK);
    }

}
