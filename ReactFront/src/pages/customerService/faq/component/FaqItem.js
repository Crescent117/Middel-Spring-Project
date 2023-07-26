// FaqItem.js
import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronDown, faTimes } from "@fortawesome/free-solid-svg-icons";
import parse from "html-react-parser";
import axios from "axios";

const FaqItem = ({ faq, index, toggleFaq, setFaqs }) => {
  const [useupdateFaq, setsuseUpdateFaq] = useState(false);
  const [useTitle, setUseTitle] = useState(faq.title);
  const [useContent, setUseContent] = useState(faq.content);

  const showUpdateFaq = () => {
    setsuseUpdateFaq(true);
  };

  const cancleUpdate = () => {
    setsuseUpdateFaq(false);
  };

  const changeTitle = (e) => {
    setUseTitle(e.target.value);
  };

  const changeContent = (e) => {
    setUseContent(e.target.value);
  };

  const commitFaqUpdate = () => {
    if (window.confirm("수정확정하시겠습니까?")) {
      const commitUpdateTitle = useTitle;
      const commitUpdateCotent = useContent;
      const faq_num = faq.faq_num;
      console.log(commitUpdateTitle);
      console.log(commitUpdateCotent);
      axios
        .put("http://localhost:8080/customer/updateFaq", {
          title: commitUpdateTitle,
          content: commitUpdateCotent,
          faq_num: faq_num,
          f_type: faq.f_type,
        })
        .then(() => {
          window.alert("수정완료");
          window.location.reload();
        });
    }
  };

  const commitFaqDelete = () => {
    if (window.confirm("삭제하시겠습니까?")) {
      axios
        .delete("http://localhost:8080/customer/deleteFaq", {
          params: {
            faq_num: faq.faq_num,
          },
        })
        .then(() => {
          window.alert("삭제완료");
          window.location.reload();
        });
    }
  };
  return (
    <>
      {useupdateFaq ? (
        <div key={faq.faq_num}>
          <p style={{ color: "grey" }}> {parse(faq.f_type)}</p>
          <input
            className="faq-title"
            value={useTitle}
            style={{ width: 900 }}
            onChange={changeTitle}
          ></input>
          <br></br>
          <textarea
            rows="10"
            cols="93"
            value={useContent}
            onChange={changeContent}
          ></textarea>
          <br />
          <button style={{ padding: 2 }} onClick={commitFaqUpdate}>
            확인
          </button>
          <button style={{ padding: 2 }} onClick={cancleUpdate}>
            취소
          </button>
        </div>
      ) : (
        <div
          key={faq.faq_num}
          className={`faq ${faq.active ? "active" : ""}`}
          onClick={() => toggleFaq(index)}
        >
          <p style={{ color: "grey" }}> {parse(faq.f_type)}</p>
          <h3 className="faq-title">{parse(faq.title)}</h3>
          <p className="faq-text">{parse(faq.content)}</p>
          <br />
          {sessionStorage.getItem("ADMIN") === null ? (
            <div></div>
          ) : (
            <>
              <button style={{ padding: 2 }} onClick={showUpdateFaq}>
                수정
              </button>
              <button style={{ padding: 2 }} onClick={commitFaqDelete}>
                삭제
              </button>
              <button type="button" className="faq-toggle">
                {!faq.active ? (
                  <FontAwesomeIcon icon={faChevronDown} />
                ) : (
                  <FontAwesomeIcon icon={faTimes} />
                )}
              </button>
            </>
          )}
        </div>
      )}
    </>
  );
};

export default FaqItem;
