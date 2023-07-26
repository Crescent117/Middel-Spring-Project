import { Link } from "react-router-dom";
import styles from "../board.module.css";

function TabMenu({
  setSelectTabs,
  setTabsCss,
  usepageSize,
  searchValue,
  currentPage,
  pageCount,
  tabsCss,
}) {
  const tabEvent = [
    { name: "전체", id: "board_li-0", link: "" },
    { name: "국내", id: "board_li-1", link: "국내" },
    { name: "해외", id: "board_li-2", link: "해외" },
    { name: "질문", id: "board_li-3", link: "질문" },
    { name: "자유", id: "board_li-4", link: "자유" },
  ];

  const selectMenuHandler = (index, tabsValue) => {
    // parameter로 현재 선택한 인덱스 값을 전달해야 하며, 이벤트 객체(event)는 쓰지 않는다
    // 해당 함수가 실행되면 현재 선택된 Tab Menu 가 갱신.
    setSelectTabs(tabsValue);
    setTabsCss(index);
    const newPath = `/board?b_type=${tabsValue}&viewCnt=${usepageSize}&search=${searchValue}&pageNum=${currentPage}`;
    window.location.href = newPath;
  };
  return (
    <div>
      <ul className={styles.board_tabs}>
        {tabEvent.map((el, index) => (
          <Link
            key={el.id}
            to={`/board?b_type=${el.link}&viewCnt=${pageCount}&search=${searchValue}`}
          >
            <li
              className={
                index === tabsCss ? "board_tab_link current" : "board_tab_link"
              }
              id={el.id}
              onClick={() => selectMenuHandler(index, el.link)}
              style={{ color: "black" }}
            >
              {el.name}
            </li>
          </Link>
        ))}
      </ul>
    </div>
  );
}
export default TabMenu;
