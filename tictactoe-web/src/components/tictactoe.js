import { useState, useEffect } from "react";

import Board from "./board";

import { createGame, move, getGame, getNextMove } from "../services/game";

export default function TikTacToe() {
  const playerTypes = [
    { key: 1, value: "Player2" },
    { key: 2, value: "Computer" },
  ];

  const [loading, setLoading] = useState(false);
  const [game, setGame] = useState(null);
  const [n, setN] = useState(4);
  const [board, setBoard] = useState([[]]);
  const [playerType, setPlayerType] = useState(playerTypes[0].key);

  const handleInputChange = (e) => {
    const value = e.target.value;

    if (value && !isNaN(value) && parseInt(value) < 2) {
      alert("Here does not make any sense if the value is less than 2 ");
    } else {
      setN(value);
    }
  };

  const composeBoard = async () => {
    const existingGame = localStorage.getItem("game");
    if (existingGame) {
      setLoading(true);
      try {
        const res = await getGame(existingGame);
        if (res?.data) {
          setGame(res.data);
          setBoard(res.data.board);
        }
      } catch (error) {
        alert(error.response.data.message);
        createDefaultBoard();
      } finally {
        setLoading(false);
      }
    } else {
      createDefaultBoard();
    }
  };

  const createDefaultBoard = async () => {
    if (n && !isNaN(n)) {
      const parseInput = parseInt(n);
      const board = Array(parseInput).fill(
        Array(parseInput).fill(" ", 0, n),
        0,
        n
      );
      setBoard(board);
    }
  };

  const startGame = async () => {
    setLoading(true);
    try {
      const res = await createGame({ playerType, n });
      if (res?.data) {
        setGame(res.data);
        setBoard(res.data.board);
        localStorage.setItem("game", res.data.id);
      }
    } catch (error) {
      alert(error.response.data.message);
    } finally {
      setLoading(false);
    }
  };

  const movePosition = async (x, y) => {
    if(!game)
      return

    setLoading(true);
    try {
      const res = await move(localStorage.getItem("game"), {
        point: { x, y },
        player: game.playerTurn,
      });
      if (res?.data) {
        setGame(res.data);
        setBoard(res.data.board);
      }
    } catch (error) {
      alert(error.response.data.message);
    } finally {
      setLoading(false);
    }
  };

  const letPlayComputer = async () => {
    try {
      const res = await getNextMove(localStorage.getItem("game"), 2);
      if (res?.data) {
        movePosition(res.data.x, res.data.y);
      }
    } catch (error) {
      alert(error.response.data.message);
    }

    return;
  };

  const resetGame = () => {
    setGame(null);
    localStorage.removeItem("game");

    setN(4);
    composeBoard();
  };

  const decoratePlayer = (p) => {
    return `item ${
      game?.playerTurn === p && !loading && !game?.winner ? "active-player" : ""
    }  ${game?.winner === p ? "winner-player" : ""}`;
  };

  useEffect(() => {
    if (game && game.playerTurn === 2 && game.type === 2 && !game.gameOver) {
      letPlayComputer();
    }
  }, [game]);

  useEffect(() => {
    composeBoard();
  }, []);

  return (
    <div className="align-center">
      <div className="content-box">
        {game?.winner > 0 && <div>Game Over!</div>}
        <div>
          <input
            disabled={game || loading}
            type="number"
            max="10"
            min="2"
            value={n}
            onChange={handleInputChange}
            onBlur={composeBoard}
          ></input>
        </div>
        <div>{`${n} X ${n}`}</div>

        <Board
          data={board}
          onPositionClick={movePosition}
          winner={game?.winner ?? 0}
        />

        <div className="item-cntr">
          <div className={decoratePlayer(1)}>Player 1</div>
          <div className="item">VS</div>

          {game?.type ? (
            <div className={decoratePlayer(2)}>
              {playerTypes.find((p) => p.key === game.type).value}
            </div>
          ) : (
            <select
              className="item"
              disabled={game || loading}
              value={playerType}
              onChange={(e) => setPlayerType(e.target.value)}
            >
              {playerTypes.map((p) => (
                <option value={p.key} key={p.key}>
                  {p.value}
                </option>
              ))}
            </select>
          )}
        </div>
        <div className="ele-margin">
          {game ? (
            <button onClick={resetGame} disabled={loading}>
              Reset
            </button>
          ) : (
            <button onClick={startGame} disabled={loading}>
              Start
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
