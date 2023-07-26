import React, { useEffect, useState } from "react";
import { Container, Row, Col, ListGroup, ListGroupItem } from "react-bootstrap";
import "./footer.css"
const Footer = () => {
 
  useEffect(() => {
    const footer = document.querySelector(".footer");

    const adjustFooterPosition = () => {
      const windowHeight = window.innerHeight;
      const footerHeight = footer.offsetHeight;
      const contentHeight = document.body.scrollHeight;

      if (contentHeight < windowHeight) {
        // 컨텐츠가 창보다 작은 경우에만 실행
        footer.style.position = "absolute";
        footer.style.bottom = 0;
      } else {
        footer.style.position = "static";
      }
    };

    // 컨텐츠가 로드될 때와 창 크기가 변경될 때마다 푸터 위치 조정
    window.addEventListener("load", adjustFooterPosition);
    window.addEventListener("resize", adjustFooterPosition);

    // 컴포넌트가 언마운트될 때 이벤트 리스너 해제
    return () => {
      window.removeEventListener("load", adjustFooterPosition);
      window.removeEventListener("resize", adjustFooterPosition);
    };
  }, []);

  return (
      <footer className="footer">
      <Container fluid>
        <Row>
          <Col xs={12} md={3}>
            <ListGroup variant="flush">
              <ListGroupItem>고객지원실 운영안내</ListGroupItem>
              <ListGroupItem>
                월~금 09:30~06:30(점심시간 13:15~14:30)
              </ListGroupItem>
              <ListGroupItem>주말/공휴일 제외, 한국시간 기준</ListGroupItem>
            </ListGroup>
          </Col>
          <Col
            xs={{ span: 12, order: "first" }}
            md={{ span: 2, order: "last", offset: 3 }}
          >
            {/* order 속성을 사용해 모바일 환경에서 원하는 순서로 나타나도록 설정 */}
            <p>소개</p>
          </Col>
          <Col xs={12} md={2}>
            <p>파트너</p>
          </Col>
          <Col xs={12} md={2}>
            <p>지원</p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
