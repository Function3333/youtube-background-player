# 기능목록

## FrontEnd
- URL 복사 시 해당 복사를 감지하고 자동으로 붙혀 넣어주기

## BackEnd
### mp3 관련 기능
- youtube URL에 해당하는 mp3 생성 [v]
- 생성한 mp3 파일 AWS S3 업로드 [v]
- AWS S3에 업로드 mp3 파일 CDN을 통해 가져오기 [v]
- 생성한 youtube mp3파일의 Id, Title DB에 저장 => 해당 테이블의 pk를 youtube url ID가 아닌 다른것으로 생성하고 테이블에 S3 CDN URL 필드 추가하기 [v]
- 사용자가 생성하려는 mp3 파일이 DB에 존재 시 다시 오디오 트랙을 만드는것이 아닌 자동으로 해당 오디오 가져오기 [v]4
- mp3 파일 중 앱을 종료후 다시 내려받기 가능, 이후 오디오가 저장되는 폴더에서 cronjob으로 일정 기간 넘어간 part 파일들을 삭제하자
- mp3 생성 중 진행상황 퍼센트로 반환

### 회원 기능
- 회원가입
  - Id, Password, Email만을 입력값으로 가지며 Id 중복검사 , Email인증 과정을 거친다
- 회원탈퇴
  - 바로 탈퇴
- 로그인 
  - 로그인시 token 발급
  - refresh token 기능 추가 

### 재생목록 기능
- token 파싱 후 해당 사용자의 재생목록 반환
- 변환시 기본적으로 “내가 생성한 오디오 트랙”에 자동으로 추가
- 사용자가 재생목록을 생성 가능하고 “내가 생성한 오디오 트랙”에서 음원을 선택하여 원하는 재생목록에 넣을 수 있다

### 공통사항
- token parsing하여 회원일때만 요청을 비지니스 로직에 전달하는 인터셉터 구현
- 인터셉터는 화이트 리스트로 회원가입 로그인만 추가하자

## 고민사항
- 오디오 추출까지 남은시간 or 퍼센트 출력
- AccessToken, RefreshToken을 구분하는 로직을 넣는게 좋을까?, 
  EX). RefreshToken만으로 AccessToken 발급하는 로직같은데 넣기

## 오늘 코딩 시작 지점
- JwtUtil의 isTokenValide 메서드에서 refresh token 파싱 못하는 에러 해결
- 위 에러 해결 후 refresh token이 valide하면 refresh token만으로 다시 access token을 발급하는 UserController method 만들기

