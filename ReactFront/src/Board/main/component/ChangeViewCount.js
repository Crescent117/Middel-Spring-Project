import styles from "../board.module.css";
function ChangeViewCount({
  selectTabs,
  searchValue,
  currentPage,
  usepageSize,
  pagecount,
  setUsepageSize,
}) {
  const handlePagecountChange = (event) => {
    setUsepageSize(event.target.value);
    const newPath = `/board?b_type=${selectTabs}&viewCnt=${event.target.value}&search=${searchValue}&pageNum=${currentPage}`;
    window.location.href = newPath;
  };

  return (
    <div className={styles.board_menu_select}>
      <div className={styles.board_text}>
        <select
          className="form-select form-select-sm"
          id="view-select"
          aria-label="form-select-sm example"
          value={usepageSize}
          onChange={handlePagecountChange}
        >
          {pagecount.map((sl) => (
            <option key={sl.id} id={sl.id} value={sl.value}>
              {sl.name}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
}
export default ChangeViewCount;
