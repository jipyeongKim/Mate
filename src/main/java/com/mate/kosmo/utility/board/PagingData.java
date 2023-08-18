package com.mate.kosmo.utility.board;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingData<T> {
    // [스프링 프레임워크 사용하여 게시판 만들기]
    // 1) 게시판.txt 페이징 데이터를 저장하는 제너릭 클래스 정의하기
    // 2) 멤버 변수 정의하기

    // 2-1) 데이터베이스에서 가져온 게시판 글 목록 레코드를 저장하는 멤버 변수 정의하기
    private List<T> list;

    // 2-2) 게시판 페이징 데이터를 저장하는 멤버 변수 정의하기
    private Map map;

    // 2-3) 게시판 페이징 문자열을 저장하는 멤버 변수 정의하기
    private String pagingString;

}// PagingData