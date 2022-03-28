package com.game.tictactoe.service;

import com.game.tictactoe.dto.GameDTO;
import com.game.tictactoe.dto.MoveDTO;
import com.game.tictactoe.dto.PointDTO;
import com.game.tictactoe.entity.Game;
import com.game.tictactoe.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Game createGame(GameDTO gameDTO) {
        var game = new Game();
        int n = gameDTO.getN();
        var board = new Character[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = Character.valueOf(' ');
            }
        }

        game.setPlayerTurn(1);
        game.setBoard(board);
        game.setWinner(0);
        game.setType(gameDTO.getPlayerType());
        game.setGameOver(false);

        return this.gameRepository.save(game);
    }

    public Game getGame(String gameId) {

        return this.gameRepository.findById(gameId).orElseThrow(() -> new IllegalStateException("Game not found"));
    }

    public Game move(String gameId, MoveDTO moveDTO) {

        Game game = this.getGame(gameId);

        var x = moveDTO.getPoint().getX();
        var y = moveDTO.getPoint().getY();
        var board = game.getBoard();
        var player = moveDTO.getPlayer() == 1 ? 'X' : 'O';

        if (x > board[0].length - 1 || y > board[1].length - 1) {
            throw new IllegalStateException("Invalid input data");
        }

        if (board[x][y] != ' ') {
            throw new IllegalStateException("move not possible");
        }

        if (moveDTO.getPlayer() != game.getPlayerTurn()) {
            throw new IllegalStateException("its not your turn");
        }

        board[x][y] = player;

        game.setBoard(board);

        if (this.isWinningMove(board, player, x, y)) {
            game.setGameOver(true);
            game.setWinner(moveDTO.getPlayer());
        } else if (this.isBoardFull(board)) {
            game.setWinner(3);
            game.setGameOver(true);
        } else {
            game.setPlayerTurn(moveDTO.getPlayer() == 1 ? 2 : 1);
        }

        return this.gameRepository.save(game);
    }

    private boolean isWinningMove(Character[][] board, Character player, int x, int y) {
        int row = 0, col = 0, lDiag = 0, rDiag = 0, n = board[0].length;

        for (int i = 0; i < n; i++) {
            if (board[x][i] == player) {
                row++;
            }
            if (board[i][y] == player) {
                col++;
            }
            if (board[i][i] == player) {
                lDiag++;
            }
            if (board[i][n - (i + 1)] == player) {
                rDiag++;
            }
        }

        if (row == n || col == n || lDiag == n || rDiag == n) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull(Character[][] board) {
        var n = board[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == Character.valueOf(' ')) {
                    return false;
                }
            }
        }
        return true;
    }

    public PointDTO findNextMove(String gameId, int inputPlayer) {
        Game game = this.getGame(gameId);

        if (game.getGameOver() == true) {
            throw new IllegalStateException("Game over!");
        }
        var player = inputPlayer == 1 ? 'X' : 'O';
        return this.findBestMove(game.getBoard(), player);
    }

    private PointDTO findBestMove(Character[][] board, Character player) {
        var n = board[0].length;
        var opponent = player == 'X' ? 'O' : 'X';
        Character[] vs = new Character[]{player, opponent};

        int bestScore = -1000;
        PointDTO position = null;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == Character.valueOf(' ')) {
                    board[i][j] = vs[0];
                    var score = this.minMax(board, vs, i, j, 0, false);
                    board[i][j] = Character.valueOf(' ');
                    if (score > bestScore) {
                        bestScore = score;
                        position = new PointDTO(i, j);
                    }
                }
            }
        }

        return position;
    }

    private int minMax(Character[][] board, Character[] vs, int x, int y, int depth, boolean isMaximizing) {

        var score = this.getScoreForTheMove(board, vs, x, y);
        if (score != null)
            return score;

        var n = board[0].length;

        if (isMaximizing) {
            int bestScore = -1000;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (board[i][j] == Character.valueOf(' ')) {
                        board[i][j] = vs[0];
                        var newScore = this.minMax(board, vs, i, j, depth + 1, false);
                        board[i][j] = Character.valueOf(' ');
                        bestScore = Math.max(newScore, bestScore);                        
                    }
                }
            }
            return bestScore;

        } else {
            int bestScore = 1000;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (board[i][j] == Character.valueOf(' ')) {
                        board[i][j] = vs[1];
                        var newScore = this.minMax(board, vs, i, j, depth + 1, true);
                        board[i][j] = Character.valueOf(' ');
                        bestScore = Math.min(newScore, bestScore);
                       
                    }
                }
            }
            return bestScore;
        }
    }

    private Integer getScoreForTheMove(Character[][] board, Character[] vs, int x, int y) {

        if (this.isWinningMove(board, vs[0], x, y)) {
            return 10;
        }

        if (this.isWinningMove(board, vs[1], x, y)) {
            return -10;
        }

        if (this.isBoardFull(board))
            return 0;

        return null;
    }
}
