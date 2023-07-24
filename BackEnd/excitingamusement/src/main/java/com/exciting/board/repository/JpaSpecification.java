package com.exciting.board.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.exciting.dto.BoardFavoriteDTO;
import com.exciting.entity.BoardFavoriteEntity;
import com.exciting.entity.BoardImgEntity;

public class JpaSpecification {


	public static Specification<?> LikeSearch(String searchValue, String searchType, String member_id, String roles ,String notAbleType) {
		return (root, query, criteriaBuilder) -> {
			Predicate likePredicate = criteriaBuilder.like(root.get(searchType), "%" + searchValue + "%");
			Predicate notEqualPredicate = criteriaBuilder.notEqual(root.get(notAbleType), "답변");
			Predicate predicate;
			//관리자 체크 true == 회원
			boolean adminCheck = member_id != null && (roles == null || roles.equals("ROLE_user"));

			// 검색값 유무 체크
			if (searchValue == null || searchValue.isEmpty()) {
				if (adminCheck) {
					Predicate memberIdPredicate = criteriaBuilder.equal(root.get("member_id"), member_id);
					predicate = criteriaBuilder.and(notEqualPredicate, memberIdPredicate);
				} else {
					predicate =  criteriaBuilder.and(notEqualPredicate);
				}
			} else {
				if (adminCheck) {
					Predicate memberIdPredicate = criteriaBuilder.equal(root.get("member_id"), member_id);
					predicate = criteriaBuilder.and(likePredicate, notEqualPredicate, memberIdPredicate);
				} else {
					predicate = criteriaBuilder.and(likePredicate, notEqualPredicate);
				}
			}
			query.where(predicate); // 필터링 조건을 query에 설정
			return query.getRestriction();
		};
	}



    public static Specification<BoardImgEntity> equalInteger(String key, Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (key != null || value != null) {
                return criteriaBuilder.equal(root.get(key), value);
            } else {
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
    }

    public static Specification<?> equalString(String key, String value) {
        return (root, query, criteriaBuilder) -> {
            System.out.println(value);
            System.out.println(value != ("전체"));
            if (key != null && value != null && !value.equals("전체")) {
                return criteriaBuilder.equal(root.get(key), value);
            } else {
                return criteriaBuilder.isNotNull(root.get(key));
            }
        };
    }

//	
}
