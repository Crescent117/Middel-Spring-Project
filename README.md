# 스프링부트 + 리엑트 프로젝트 + 엘라스틱서치 + 키바나
<br/>
<br/>

## 사용기술 
### JAVA : 11
### DB : Mysql
### SpringBoot FrameWork : 2.7.14-SNAPSHOT
### JPA
### node :v14.21.3
### npm v6.14.18
### react@18.2.0
### Elasticsearch-7.17.10
### kibana-7.17.10
### logstash-7.17.10
<br/><br/>
<hr/>



## 팀프로젝트 게시판 Board, 고객센터 CustomerService 담당
<br/>
- 게시판(board)
  - ElasticSearch를 활용한 게시글 출력
  - ElasticSearch 쿼리문을 이용하여 Tab분류, 화면최대글갯수, 검색값, 페이징을 조건에 따라 추가함으로서 원하는 값을 얻을 수 있습니다.
  <details>
    <summary>자세히</summary>

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
  
  </details>
  - Ckeditor를 사용한 다양한 글꼴등등 기능 사용
  - React 상태관리를 통한 전체페이지 랜더링을 안하고 필요한 부분만 재랜더링 ( ex)댓글 )
  - SpringBootSecurity로 외부폴더의 권한을 풀고 이미지 저장
- CustomerService
  -공지사항
   - 게시판과 기능이 동일

  - FAQ
    - React 상태관리를 통한 페이지 접기펴기 기능
    - React 상태관리를 통한 비동기적으로 관리자 View 와 Write 화면으로 전환
    - Tab분류와 페이징을 동시에 적용시키기 위해 Jpa Specification 사용

  - 1:1문의
    - 회원의 비밀을 지키기위해 해당 해원이 아니면 질문글이 보이지 않도록 설정
    - 관리자는 답변을 달아야함으로 모든 글이 보이도록 설정
    - 검색을 통해 관리자가 글을 찾기 쉽도록 했습니다.
 
  ### 어려운 점과 해결방안
   - ElasticSearch에서 Query문에 검색값등 있어도 되고 없어도 되는 값에 대해 어떻게 처리할까 고민했고 이에 대해 책이나 검색을 통해 해결법을 찾아냈습니다.
   - React와 Tomcat의 포트가 달라서 Cors 에러가 자주 등장했는데 자료를 찾아본 결과 포트 권한을 열어주면 된다는 결론이 나와 예상보다 쉽게 해결하였습니다.
   - JSP에서 React로 변경하고 중간과정에서 프로젝트를 합칠때 html 코드를 자바스크립트 형식에서 리턴을 하고 css 코드도 리액트가 작동을 하면서 App.js에서 루트에 연결된 모든 컴포넌트들이 실행되다보니
    css충돌이 발생했습니다.
     - 이를 해결하기위해 ClassName, 전역변수명 등을 전체적으로 손봐야했고 이에 대해 자료를 찾아본 결과 CSS in CSS 와 CSS in JS 방식을 찾아내었고 그 중에서 CSS in CSS를 적용하여 해결했습니다. 
   - 서버 외부에 이미지를 저장하는 기능을 만드는 도중 해당 폴더가 없으면 에러가 터지는 것을 발견했습니다.
     - 이에 관해서 application.properties에 저장경로를 설정하고 FileUtils라는 클래스를 생성해 서버 시작시 자동으로 application.properties에 저장된
       경로에 폴더가 생성되도록 했습니다.
   - 1:1문의에 게시글을 출력할때 많은 조건문이 붙고 이에 대해 repository의 Query가 증가하거나 상황에따라 method가 많아지고 통신을 많이해야하는 문제점이 있었습니다.
     - 이것에 관해 코드량을 줄이고 repository를 좀 더 깔끔하게 쓸 방법을 찾아본 결과 JpaSpecification이란 기능을 찾아내었고 적용시켰습니다. 



# Reference

## Board 메인 게시판 ElasticSearch 사용법
<br/>
1. Elasticsearch/bin/elasticsearch.bat 실행
<br/>
2. Kibana/bin/kibana.bat 실행
<br/>
3. logstash.conf를 config 폴더에 넣는다
<br/>
4. logstash/bin ---> cmd ---> logstash.bat -f ../config/logstash.conf
<br/><br/>
혹시 실행하기 힘드시다면 localhost3000 기준 "http://localhost:3000/detail?board_id={db에 저장되어있는 board_id 입력}"
이렇게 하시면 상세페이지를 보실 수 있습니다.
<br>
## 사이트 관리자 ID : hong1
## 사이트 관리자 비밀번호 : !string1234
<br/><br/>
