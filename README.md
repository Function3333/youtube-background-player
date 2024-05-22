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

## ETC
### 고민사항
- AccessToken, RefreshToken을 구분하는 로직을 넣는게 좋을까?, 
  EX). RefreshToken만으로 AccessToken 발급하는 로직같은데 넣기
- 
### ToDo List
- 오디오 추출까지 남은시간 or 퍼센트 출력
- playList 정렬하게 playList Entity에 저장 날짜 넣어보자
- table의 pk들이 50씩 증가하는거 고치기
- Youtube API를 통해 가져온 검색 결과의 정확도가 너무 낮음

### PlayList 테스트 시나리오
- Audio 저장 시 PlayList 자동등록 [v]
- 중복 Audio Save 시도 시 PlayList에 새롭게 저장하지 않음 [v]
- UserName으로 Audio 가져오기 [v]
- 두명 이상의 사용자가 같은 Audio를 PlayList에 저장, 이후 한명의 사용자가 해당 PlayList를 삭제할때 PlayList에서만 삭제되고 s3 Bucket에서는 삭제 [v]
- Audio가 한명의 사용자에게만 등록되어있고 이후 해당 Audio 삭제 요청이 들어오면 s3 Bucket에서 삭제 [v]


