export default function TikTacToe(props) {
  const onCellClickHandler = (e, val, x, y) => {
    e.stopPropagation();

    if(props.winner > 0){
      alert('Game over!')
      return;
    }

    if (val === " ") {
      props.onPositionClick(x, y);
    }
  };

  return (
    <div className="board">
      {props.data.map((row, i) => (
        <div
          className="row"
          key={i}
          style={{ width: row.length * 48 + "px", height: "48px" }}
        >
          {row.map((col, j) => (
            <div
              key={i +'_' + j}
              className={`col ${col === ' ' && props.winner === 0 ? 'col-hover' : ''}`}
              onClick={(e) => onCellClickHandler(e, col, i, j)}
              title={props.winner > 0 ? 'Game over!' : ''}
            >
              {col}
            </div>
          ))}
        </div>
      ))}
    </div>
  );
}
