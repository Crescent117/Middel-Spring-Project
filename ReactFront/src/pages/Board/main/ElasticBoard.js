import React, { useEffect, useState } from "react";
//import './ElasticBoard.css'
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import $ from "jquery";
import { useLocation } from "react-router-dom";
import styles from "./board.module.css";
import TabMenu from "./component/TapMenu";
import ChangeViewCount from "./component/ChangeViewCount";
import ElasticBoardList from "./component/ElasticBoardList";

function ElasticBoard() {
  const [currentPage, setCurrentPage] = useState();
  const [usepageSize, setUsepageSize] = useState(10);
  const [tabsCss, setTabsCss] = useState("11");
  const [pagevalue, setPagevalue] = useState(100);
  const [elasticResults, setElasticResults] = useState([]);
  const [searchValue, setSearchValue] = useState("");
  const [selectTabs, setSelectTabs] = useState();
  const [pageCount, setPageCount] = useState(10);
  const [pagingSize, setPagingSize] = useState(10);

  const location = useLocation();
  const addressParams = new URLSearchParams(location.search);

  // 페이지 사이즈 업데이트 함수

  useEffect(() => {
    elastick();
    console.log(sessionStorage.getItem("MEMBER_ID"));
    console.log(sessionStorage.getItem("ADMIN"));
  }, []);

  // $(document).on('click', '.pagination-button', function() {
  //     setCurrentPage(parseInt($(this).val()));
  //     elastick(currentPage);
  //   });

  const elastick = async () => {
    let search = document.getElementById("text").value;
    let selectField = document.querySelector("select[name=selectField]").value;
    let pageSize = addressParams.get("viewCnt");
    let b_type = addressParams.get("b_type");
    let pageNum = addressParams.get("pageNum");

    //setCurrentPage(currentPage)

    //pageSize가 null일땐 10
    if (pageSize === null) {
      pageSize = 10;
    }
    if (b_type === null) {
      b_type = "";
      setSelectTabs("");
    }
    if (pageNum === null) {
      pageNum = 1;
    }
    if (search === null) {
      search = "";
    }
    setSearchValue(search);
    setCurrentPage(pageNum);
    setSelectTabs(b_type);
    setUsepageSize(pageSize);
    //기본적으로 틀만 만들어놓고 값이 있는것만 push
    const query = {
      from: (pageNum - 1) * pageSize,
      size: pageSize,
      sort: [
        {
          board_id: {
            order: "desc",
          },
        },
      ],
      query: {
        bool: {
          must: [],
        },
      },
    };

    if (search) {
      query.query.bool.must.push({
        wildcard: {
          [selectField]: {
            value: `*${search}*`,
          },
        },
      });
    }

    if (b_type) {
      query.query.bool.must.push({
        term: {
          b_type: b_type,
        },
      });
    }

    if (query.query.bool.must.length === 0) {
      query.query.match_all = {};
      delete query.query.bool;
    }

    getElasticBoardList(query, pageSize, pageNum);
  };

  const getElasticBoardList = async (query, pageSize, pageNum) => {
    const sel = [query];

    const url = "http://localhost:9200/board_index/_search";

    const headers = { "Content-Type": "application/json" };
    const data = JSON.stringify(sel[0]);
    const requestOption = {
      method: "POST",
      url: url,
      headers: headers,
      data: data,
    };

    await axios(requestOption)
      .then((response) => {
        let pagevalue = response.data.hits.total.value;

        setPagevalue(pagevalue);

        const data = response.data.hits.hits;
        setElasticResults(data);
        paging(pagevalue, pageNum);
      })
      .catch((error) => {});
  };

  function paging(pagevalue, pageNum) {
    const buttons = [];
    $(".page_test").html("");
    let lowPage = 1;
    let maxPage = 10;

    lowPage = Math.ceil(pagevalue / usepageSize);

    const pageGroup = Math.ceil(currentPage / maxPage);

    const last = Math.min(pageGroup * maxPage, lowPage);

    if (last > lowPage) {
      last = lowPage;
    }
    let first = last - (maxPage - 1); // 화면에 보여질 첫번째 페이지 번호
    const next = last + 1;
    const prev = first - 1;

    if (lowPage < 1) {
      first = last;
    }

    if (first > 10) {
      buttons.push(
        <button className={styles.board_pagination_button} value="1" key="1">
          Top
        </button>
      );
      buttons.push(
        // <Link to={`/board?b_type=${selectTabs}&viewCnt=${pageSize}&search=${currentPage}`}>
        <button
          className={styles.board_pagination_button}
          value={prev}
          key={prev}
        >
          Prev
        </button>
        //</Link>
      );
    }
    for (let j = first; j <= last; j++) {
      if (currentPage === j) {
        buttons.push(
          <button className={styles.board_pagination_button} value={j} key={j}>
            {j}
          </button>
        );
      } else if (j > 0) {
        buttons.push(
          <button className={styles.board_pagination_button} value={j} key={j}>
            {j}
          </button>
        );
      }
    }
    if (next > 10 && next < lowPage) {
      buttons.push(
        <button
          className={styles.board_pagination_button}
          value={next}
          key={next}
        >
          Next
        </button>
      );

      buttons.push(
        <button
          className={styles.board_pagination_button}
          value={lowPage}
          key={lowPage}
        >
          End
        </button>
      );
    }

    return buttons;
  }

  const tabEvent = [
    { name: "전체", id: "board_li-0", link: "" },
    { name: "국내", id: "board_li-1", link: "국내" },
    { name: "해외", id: "board_li-2", link: "해외" },
    { name: "질문", id: "board_li-3", link: "질문" },
    { name: "자유", id: "board_li-4", link: "자유" },
  ];

  const pagecount = [
    { name: "10", id: "board_select-1", value: "10" },
    { name: "30", id: "board_select-2", value: "30" },
    { name: "50", id: "board_select-3", value: "50" },
  ];

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  };

  const moveWriteForm = () => {
    if (!sessionStorage.getItem("MEMBER_ID")) {
      window.alert("로그인 해주세요!");
      window.location.href = "/login";
    } else {
      window.location.href = "/addboard";
    }
  };

  function handlePageChange(newPage) {
    setCurrentPage(newPage);
    const newPath = `/board?b_type=${selectTabs}&viewCnt=${usepageSize}&search=${searchValue}&pageNum=${newPage}`;
    window.location.href = newPath;
    //elastick(newPage);
  }

  const searchHref = () => {
    const newPath = `/board?b_type=${selectTabs}&viewCnt=${usepageSize}&search=${searchValue}&pageNum=${currentPage}`;
    window.location.href = newPath;
  };

  return (
    <>
      <section className={styles.board_notice}>
        {/* board list area */}
        <div className={styles.board_main_wrap} style={{ left: 150 }}>
          <div className={styles.board_list}>
            <div className={styles.board_page_title}>
              <h1>게시판</h1>
            </div>

            <div className={styles.board_container}>
              <div className={styles.board_write_from_wrap}>
                {/* tabs menu */}
                <TabMenu
                  setTabsCss={setTabsCss}
                  setSelectTabs={setSelectTabs}
                  usepageSize={usepageSize}
                  searchValue={searchValue}
                  currentPage={currentPage}
                  pageCount={pageCount}
                  tabsCss={tabsCss}
                />
                {/* 글작성 버튼 */}
                <div className={styles.board_write_form}>
                  <button
                    type="submit"
                    id="write-top"
                    className={styles.board_btn_board_top}
                    onClick={moveWriteForm}
                  >
                    <img
                      src={
                        "https://cdn-icons-png.flaticon.com/512/5218/5218705.png"
                      }
                      className={styles.img_write}
                      id="img_write"
                    />
                    글쓰기
                  </button>
                </div>
              </div>

              {/* 글 출력수량 설정 */}
              <ChangeViewCount
                usepageSize={usepageSize}
                setUsepageSize={setUsepageSize}
                selectTabs={selectTabs}
                searchValue={searchValue}
                currentPage={currentPage}
                pagecount={pagecount}
              />

              {/*게시글 출력*/ }
              <ElasticBoardList
                elasticResults={elasticResults}
              />

              <div className={styles.board_page_wrap}>
                <span
                  className={styles.board_page_nation}
                  style={{ padding: "0 0 0 20%" }}
                >
                  {/* board paging start */}
                  {paging(pagevalue, currentPage, pagingSize).map((button) =>
                    React.cloneElement(button, {
                      onClick: () =>
                        handlePageChange(parseInt(button.props.value)),
                    })
                  )}
                </span>

                <span
                  className={styles.board_write_bottom_wrap}
                  style={{ float: "right" }}
                >
                  <button
                    type="submit"
                    id="write-bottom"
                    className={[styles.board_btn, styles.board_btn_blue].join(
                      " "
                    )}
                    onClick={moveWriteForm}
                    style={{ height: "40px" }}
                  >
                    글쓰기
                  </button>
                </span>
              </div>
              {/* board paging end */}
            </div>
          </div>
          <div style={{ clear: "both" }}></div>

          {/* board search area */}
          <div className={styles.board_main_search}>
            <div className={styles.board_container}>
              <div className={styles.board_search_window}>
                <div className={styles.board_search_wrap}>
                  <select
                    name="selectField"
                    className={styles.board_selectField}
                    style={{
                      width: "20%",
                      height: "40px",
                      float: "left",
                      textAlign: "center",
                      fontSize: "14px",
                    }}
                  >
                    <option value="b_title">제목</option>
                    <option value="b_content">내용</option>
                    <option value="member_id">작성자</option>
                  </select>
                  <label htmlFor="search" className={styles.board_blind}>
                    검색
                  </label>
                  <input
                    id="text"
                    className={styles.board_earchField}
                    type="search"
                    name="search"
                    value={searchValue}
                    onChange={handleSearchChange}
                  />
                  <button
                    type="button"
                    id="board_searchButton"
                    className={[styles.board_btn, styles.board_btn_search].join(
                      " "
                    )}
                    onClick={() => searchHref()}
                  >
                    검색
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}

export default ElasticBoard;
