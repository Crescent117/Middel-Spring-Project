import styles from "../board.module.css";
function ElasticBoardList({ elasticResults }) {
  const columns = [
    { label: "번호", className: "th-num", width: "5%" },
    { label: "분류", className: "th-num", width: "10%" },
    { label: "제목", className: "th-title", width: "30%" },
    { label: "작성자", className: "th-member", width: "10%" },
    { label: "등록일", className: "th-date", width: "30%" },
    { label: "추천수", className: "th-date", width: "5%" },
    { label: "조회수", className: "th-date", width: "5%" },
  ];

  const ElasticResultRow = ({ hit }) => {
    const source = hit._source;
    const member_id = source.member_id;
    const postdate = source.postdate;

    const ymd = postdate
      ? postdate.substring(0, 10) + " " + postdate.substring(14, 19)
      : "";
    const ymd2 = ymd.replaceAll("-", ".");

    return (
      <tr key={hit._id}>
        <td>{hit._id}</td>
        <td>{hit._source.b_type}</td>
        <td style={{ textAlign: "center", padding: "0 0 0 10px" }}>
          <a href={"/detail?board_id=" + hit._id}>{hit._source.b_title}</a>
        </td>
        <td>{member_id}</td>
        <td>{ymd2}</td>
        <td>{hit._source.favorite}</td>
        <td>{hit._source.visitcount}</td>
      </tr>
    );
  };
  return (
    <table className={styles.board_table}>
      <thead>
        <tr>
          {columns.map((col) => (
            <th
              scope="col"
              className={col.className}
              style={{ width: col.width }}
            >
              {col.label}
            </th>
          ))}
        </tr>
      </thead>
      <tbody id="elasti">
        {elasticResults.map((hit) => (
          <ElasticResultRow key={hit._id} hit={hit} />
        ))}
      </tbody>
    </table>
  );
}

export default ElasticBoardList;
