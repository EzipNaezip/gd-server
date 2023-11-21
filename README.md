<div align="center">

<img  width="150" src="https://github.com/EzipNaezip/gd-app/assets/39869096/89a6d6c4-f08e-4b98-8b53-be7036eef66c"/>

<p><b>이집내집 - 웹사이트</b></p>

![build](https://img.shields.io/badge/build-1.0.0-brightgreen?logo=github)
![hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FEzipNaezip&count_bg=%2379C83D&title_bg=%23555555&icon=github.svg&icon_color=%23E7E7E7&title=hits&edge_flat=false)

**당신만의 인테리어 디자이너 AI와 함께하세요!**

[🏠 팀에 대해](https://github.com/EzipNaezip)
<span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
[📄 기획서 보기](https://github.com/EzipNaezip/documentation)
<span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
[🖥️ 서비스 바로가기]()

</div>

이집내집은 사용자의 인테리어 요구사항을 채팅을 통해 분석하고 AI 이미지 생성 기술을 활용하여 쉽고 편리하게 인테리어 디자인을 제공하는 서비스입니다. 사용자들은 전문적인 디자인 지식이 없이도 편리하게 인테리어 디자인을 할 수 있도록 도와 손쉽게 시각적으로 표현하고 디자인 할 수 있습니다.

- Goolge Oauth
- JWT 인증 인가
- OpenAI API

## Features

| <img alt="구글 로그인" src="https://github.com/EzipNaezip/gd-app/assets/39869096/f868c00e-85b8-49eb-bfca-9c931974bb96"> | <img alt="인테리어 생성" src="https://github.com/EzipNaezip/gd-app/assets/39869096/ad04a736-0c54-495b-964c-6df386da6345"> | <img alt="커뮤니티" src="https://github.com/EzipNaezip/gd-app/assets/39869096/d33daa97-55cb-4e5f-9432-7f00d477ce5d"> |
| :----------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: |
|                                                        구글 소셜 로그인                                                        |                                                           인테리어 생성                                                            |                                                       커뮤니티                                                       |

| <img alt="메인화면" src="https://github.com/EzipNaezip/gd-app/assets/39869096/75400823-e6cf-47d0-88ae-ab0c143f519a"> | <img alt="검색" src="https://github.com/EzipNaezip/gd-app/assets/39869096/84edbad6-58e0-4f80-bbe5-9f6d4f573e56"> | <img alt="마이페이지" src="https://github.com/EzipNaezip/gd-app/assets/39869096/92b6e479-ebfa-4b33-a7d9-a7b557fb6f01"> |
| :------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------: |
|                                                       메인화면                                                       |                                                       검색                                                       |                                                       마이페이지                                                       |

## Architecture

<img width="800" alt="Devops" src="https://github.com/EzipNaezip/gd-app/assets/39869096/06178c52-616d-4d92-89ad-6ddf1ca5e61c">

주요 브랜치에 대한 풀 리퀘스트가 성공적으로 이루어지면, `Jenkins`는 `GitHub Hook`을 통해 이를 감지하여 자동으로 테스트 및 빌드 과정을 시작합니다. 이 과정에서 발생하는 에러 없이 빌드가 성공적으로 완료되면, `Publish Over SSH` 기능을 이용하여 빌드된 산출물이 각각 컨테이너로 배포되어 무중단 배포가 이뤄집니다.

## Database ERD

<img width="605" alt="ERD" src="https://github.com/EzipNaezip/gd-app/assets/39869096/a4e42e82-8a72-402e-ba57-de903d19bb9b">

## Skills

| 구분            | 스킬                                                                                                                                                                                                              |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Core            | ![Java](https://img.shields.io/badge/Java-F40D12?style=flat-square&logo=openjdk&logoColor=white) ![SpringBoot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) |
| Database        | ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariadb&logoColor=white)                                                                                                            |
| Cloud Platform  | ![Naver](https://img.shields.io/badge/Naver_Cloud_Platform-03C75A?style=flat-square&logo=naver&logoColor=white)                                                                                                   |
| Package Manager | ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)                                                                                                               |
| CI&CD           | ![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white)                                                                                                            |

## Commit Message Convention

| 제목     | 내용                                                        |
| -------- | ----------------------------------------------------------- |
| init     | 작업 세팅 커밋 (패키지 설치 등)                             |
| feat     | 새로운 기능을 추가할 경우                                   |
| fix      | 버그를 고친 경우                                            |
| refactor | 코드 리팩토링                                               |
| docs     | 문서를 수정한 경우, 파일 삭제, 파일명 수정 등 ex) README.md |

## Members

| <img src="https://avatars.githubusercontent.com/u/85067003?v=4" width="96" /> | <img src="https://avatars.githubusercontent.com/u/105531824?v=4" width="96" /> | <img src="https://avatars.githubusercontent.com/u/109639168?v=4" width="96" /> | <img src="https://placekitten.com/96/96" width="96" /> | <img src="https://avatars.githubusercontent.com/u/102720771?v=4" width="96" /> |
| :---------------------------------------------------------------------------: | :----------------------------------------------------------------------------: | :----------------------------------------------------------------------------: | :----------------------------------------------------: | :----------------------------------------------------------------------------: |
|                     [한관희](https://github.com/limehee)                      |                    [김성준](https://github.com/Sung-june27)                    |                      [김도희](https://github.com/dohee01)                      |         [김혜빈](https://github.com/sunkong12)         |                   [이영학](https://github.com/younghak9905)                    |

## ETC

- [gd-web](https://github.com/EzipNaezip/gd-web) - 웹사이트
- [gd-app](https://github.com/EzipNaezip/gd-app) - 안드로이드
