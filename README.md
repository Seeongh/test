<h3>구조설명</h3>
1. [DB구조] https://www.erdcloud.com/d/Bf6oNqiP49R6zrRnN
<br/>
2. [패키지 구조]
   <ul>
     <li>api : RestAPI 구현패키지
       <ul>
         <li>도메인
           <ul>
             <li>application : 서비스 및 DTO적재</li>
             <li>docs : swagger 정보가 적재된 Interface</li>
             <li>domain : JPA Entity적재 (Entity는 DTO로 변환하여 사용)</li>
             <li>infrastructure : Repository Interface 적재</li>
             <li>presentation : RestController 적재</li>
           </ul>
         </li>
       </ul>
     </li>
     <li>
       common : 공통으로 사용하는 응답/에러응답 구현 패키지
         <ul>
           <li>exception :  에러 핸들러 및 커스텀 에러 적재</li>
           <li>ApiResultResponse : 공통 응답 클래스</li>
         </ul>
     </li>
     <li>
        config : swagger 및 webclient 설정파일 적재
     </li>
     <li>
       security : springsecurity 구현 패키지
       <ul>
         <li>Authentication : 인증 관련 필터 및 Provider적재</li>
         <li>config : Spring Security 설정 정보 적재</li>
         <li>handler : Spring Security Filter 에서 발생하는 에러 핸들러</li>
       </ul>
     </li>
     <li>
       util : 사용하는데 필요한 유틸 패키지 (암복호화, BigDecimal 커스텀 바인딩, WebClient통신)
     </li>
   </ul>
3.  [주요 테스트 패키지]
   <ul>    
     <li>controller : 메소드 호출 테스트</li>
     <li>service : Service 로직 테스트</li>
     <li>repository : Member 로직 테스트</li>
   </ul>
