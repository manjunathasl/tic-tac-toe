package com.game.tictactoe.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    private String id;
    private Character[][] board;
    private Integer playerTurn; //1: Player1, 2: Player2 //expect to be enums
    private Boolean gameOver;
    private Integer winner; //0: no winner 1: Player1, 2: Player2, 3: tie
    private Integer type; //1: human 2:computer
}
