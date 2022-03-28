package com.game.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveDTO {

    private PointDTO point;

    private int player; //1: Player1 2: Player2

}
