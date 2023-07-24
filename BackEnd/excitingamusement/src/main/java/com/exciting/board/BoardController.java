package com.exciting.board;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.exciting.entity.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.exciting.board.service.BoardService;
import com.exciting.dto.BoardDTO;
import com.exciting.dto.BoardFavoriteDTO;
import com.exciting.dto.BoardImgDTO;
import com.exciting.dto.BoardReplyDTO;
import com.exciting.dto.ResponseDTO;
import com.exciting.utils.ChangeJson;
import com.exciting.utils.ChangeTEXT;

import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
//import utils.BoardPage;
//import utils.ChangeJava;
//import utils.ChangeJavanontextarea;
//import utils.ChangeHtml;

@RestController
@Log4j2
@RequestMapping("/board")
public class BoardController {

	@Autowired
	BoardService service;

	private static String BOARD_UPLOAD_PATH = "D:/static/uploads/";

	// 이미지삭제 deleteimage
	@DeleteMapping("/deleteBoardImg")
	@ResponseBody
	public void deleteBoardImg(@RequestParam(value = "boardimg_num", required = false) Integer boardimg_num,
			@RequestParam(value = "board_id", required = false) Integer board_id) {
		BoardImgEntity boardImgEntity = createBoardImgEntity(boardimg_num, board_id, null);
		List<BoardImgEntity> originData = service.boardImgDelete(boardImgEntity);
		deleteFiles(originData);
	}

	// 이미지업로드 imageUpload
	@PostMapping("/imageUpload/{board_id}")
	@ResponseBody
	public String imageUpload(@RequestParam(value = "file", required = false) List<MultipartFile> mf,
			@PathVariable("board_id") int board_id) {
		try {
			if (mf != null && !mf.isEmpty()) {
				String firstFileName = mf.get(0).getOriginalFilename();
				boolean isFileNotEmpty = firstFileName != null && !firstFileName.equals("");
				if (isFileNotEmpty) {
					for (MultipartFile file : mf) {
						String originalFileName = System.currentTimeMillis() + file.getOriginalFilename();
						String safeFile = getFilePath(originalFileName);
						BoardImgEntity boardImgEntity = createBoardImgEntity(null, board_id, originalFileName);
						service.boardImgInsert(boardImgEntity);

						file.transferTo(new File(safeFile));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("BoardImgInsert Error");
		}

		return "/board/createBoard";
	}

	// 게시판글쓰기 boardWrite
	@PostMapping("/addBoard")
	public int createBoardPost(@RequestBody BoardDTO boardDTO) {

		BoardEntity boardEntity = BoardDTO.toEntity(boardDTO);

		int board_id = service.createBoard(boardEntity);

		return board_id;
	}
	@GetMapping("/view")
	@ResponseBody
	public ResponseEntity<?> boardView(BoardDTO boardDTO) {
		try {
			BoardEntity boardEntity = BoardDTO.toEntity(boardDTO);
			Long boardReplyCnt = service.boardReplyCnt(boardDTO.getBoard_id());
			service.boardVisit(boardEntity);
			int board_id =boardDTO.getBoard_id();

			List<BoardImgDTO> boardImgDTOList = selectBoardImgAndToDTOList(board_id);

			BoardDTO boardViewData = service.boardView(boardDTO.getBoard_id());
			JSONObject jsonObj = createBoardJsonObject(boardViewData, boardImgDTOList, boardReplyCnt);

			ResponseDTO<JSONObject> response = ResponseDTO.<JSONObject>builder().json(jsonObj).build();
			return ResponseEntity.ok().body(response.getJson());
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/favoriteBoard")
	@ResponseBody
	public ResponseEntity<?> getFavoriteBoard(BoardDTO boardDTO) {
		try {
			BoardEntity entity = BoardDTO.toEntity(boardDTO);
			BoardDTO favoriteData = service.boardView(entity.getBoard_id());
			JSONObject favoriteJson = ChangeJson.ToChangeJson(favoriteData);

			List<JSONObject> responseData = new ArrayList<>();
			responseData.add(favoriteJson);

			ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().data(responseData).build();

			return ResponseEntity.ok().body(response.getData());
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/favoriteBoard")
	@ResponseBody
	public int favoriteBoardPost(@RequestBody BoardFavoriteDTO boardFavoriteDTO) {

		try {
			BoardFavoriteEntity entity = BoardFavoriteDTO.toEntity(boardFavoriteDTO);
			// db 작업
			service.changeFavorite(entity, boardFavoriteDTO.getCheckData());
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}


	@GetMapping("/replyList")
	@ResponseBody
	public ResponseEntity<?> getReplyList(BoardReplyDTO boardReplyDTO) {

		try {

			BoardReplyEntity replyEntity = BoardReplyDTO.ToEntity(boardReplyDTO);
			List<BoardReplyEntity> replyList = service.getCommentList(replyEntity);
			List<BoardReplyDTO> replyDTOList  = replyList.stream().map(BoardReplyDTO::new).collect(Collectors.toList());
			List<JSONObject> replyJsonList = replyDTOList.stream()
					.map(replyDTO -> ChangeJson.ToChangeJson(replyDTO))
					.collect(Collectors.toList());

			ResponseDTO<JSONObject> response = ResponseDTO.<JSONObject>builder().data(replyJsonList).build();

			return ResponseEntity.ok().body(response.getData());
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<JSONObject> response = ResponseDTO.<JSONObject>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}

	}

	@PostMapping("/reply-insert")
	@ResponseBody
	public ResponseEntity<?> reply_insert(@RequestBody BoardReplyDTO boardReplyDTO) {

		try {
			// 문자 치환
			boardReplyDTO.setB_reply(ChangeTEXT.ToJAVA(boardReplyDTO.getB_reply()));

			BoardReplyEntity entity = BoardReplyDTO.ToEntity(boardReplyDTO);

			// 댓글 추가 작업
			service.replyInsert(entity);

			return ResponseEntity.ok().body(1);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<BoardReplyDTO> response = ResponseDTO.<BoardReplyDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}


	@PostMapping("/insertReReply")
	@ResponseBody
	public void reReplyinsert(@RequestBody BoardReplyDTO boardReplyDTO) {
		boardReplyDTO.setB_reply(ChangeTEXT.ToJAVA(boardReplyDTO.getB_reply()));
		BoardReplyEntity entity = BoardReplyDTO.ToEntity(boardReplyDTO);
		service.reReplyInsert(entity);
	}

	@PutMapping("/replyUpdate")
	@ResponseBody
	public int replyUpdate(@RequestBody BoardReplyDTO boardReplyDTO) {

		BoardReplyEntity ReplyEntity = BoardReplyDTO.ToEntity(boardReplyDTO);

		service.boardReply(ReplyEntity);

		return 0;
	}


	@DeleteMapping("/replyDelete")
	@ResponseBody
	public void replyDelete(BoardReplyDTO boardReplyDTO) {
		BoardReplyEntity boardReplyEntity = BoardReplyDTO.ToEntity(boardReplyDTO);
		service.replyDelete(boardReplyEntity);
	}

	@GetMapping("/updateBoard")
	@ResponseBody
	public ResponseEntity<?> updateBoard(BoardDTO boardDTO) {
		try {
			BoardEntity entity = BoardDTO.toEntity(boardDTO);
			BoardEntity boardDataEntity = service.updateBoard(entity);
			BoardDTO boardDataDTO = new BoardDTO(boardDataEntity);
			JSONObject jsonData = ChangeJson.ToChangeJson(boardDataDTO);

			List<BoardImgEntity> imgListEntity = service.boardImgSelect(boardDataEntity.getBoard_id());
			List<BoardImgDTO> imgList = imgListEntity.stream().map(BoardImgDTO::new).collect(Collectors.toList());
			List<BoardImgDTO> finalImgData = conversionImgPath(imgList);

			jsonData.put("boardImg", finalImgData);

			ResponseDTO<JSONObject> response = ResponseDTO.<JSONObject>builder().json(jsonData).build();
			return ResponseEntity.ok().body(response.getJson());
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<JSONObject> response = ResponseDTO.<JSONObject>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/updateBoard")
	public int updateBoardpost(@RequestBody BoardDTO boardDTO) {
		try {
			BoardEntity boardentity = BoardDTO.toEntity(boardDTO);
			service.commitUpdateBoard(boardentity);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@DeleteMapping("/deleteBoard")
	public void deleteBoard(BoardDTO boardDTO) {
		BoardEntity entity = BoardDTO.toEntity(boardDTO);
		List<BoardImgEntity> deleteImgs = service.deleteBoard(entity);
		deleteFiles(deleteImgs);
	}


	// 파일삭제로직 fileDelete(여러개)
	private void deleteFiles(List<BoardImgEntity> originData) {
		for (BoardImgEntity imgEntity : originData) {
			String filePath = getFilePath(imgEntity.getBoardimg());
			deleteFile(filePath);
		}
	}

	private void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	private String getFilePath(String fileName) {
		return BOARD_UPLOAD_PATH + fileName;
	}

	private BoardImgEntity createBoardImgEntity(Integer boardimg_num, Integer board_id, String originalFileName) {
		BoardImgEntity boardImgEntity = new BoardImgEntity();
		if (boardimg_num != null) {
			boardImgEntity.setBoardimg_num(boardimg_num);
		}
		if (board_id != null) {
			boardImgEntity.setBoard_id(board_id);
		}
		boardImgEntity.setBoardimg(originalFileName);
		return boardImgEntity;
	}

	private List<BoardImgDTO> selectBoardImgAndToDTOList(int board_id){
		List<BoardImgEntity> boardImgEntityList = service.boardImgSelect(board_id);
		List<BoardImgDTO> boardImgDTOList = boardImgEntityList.stream().map(BoardImgDTO::new).collect(Collectors.toList());
		boardImgDTOList = conversionImgPath(boardImgDTOList);
		return boardImgDTOList;
	}

	private List<BoardImgDTO> combineImgPath(List<BoardImgDTO> boardImgDTOList, List<String> boardimgPathData){
		int num = 0;
		for (String path : boardimgPathData) {
			boardImgDTOList.get(num).setBoardimg(path);
			num++;
		}
		return boardImgDTOList;
	}

	private List<BoardImgDTO> conversionImgPath(List<BoardImgDTO> boardImgDTOList){
		List<String> boardimgPathData = boardImgDTOList.stream()
				.map(img -> "http://localhost:8080/uploads/" + img.getBoardimg())
				.collect(Collectors.toList());
		return combineImgPath(boardImgDTOList, boardimgPathData);
	}

	private JSONObject createBoardJsonObject(BoardDTO boardViewData, List<BoardImgDTO> boardImgDTOList, Long boardReplyCnt) {
		JSONObject boardJsonObj = null;
		if (Objects.nonNull(boardViewData)) {
			boardJsonObj = ChangeJson.ToChangeJson(boardViewData);
			if (!boardImgDTOList.isEmpty()) {
				List<JSONObject> imgJsonList = boardImgDTOList.stream()
						.map(img -> ChangeJson.ToChangeJson(img))
						.collect(Collectors.toList());
				boardJsonObj.put("boardimg", imgJsonList);
			}
			if (boardReplyCnt != null) {
				boardJsonObj.put("cnt", boardReplyCnt);
			}
		}
		return boardJsonObj;
	}



	/*
	 *
	 * Board 게시글 Delete 삭제 END
	 *
	 */

}
