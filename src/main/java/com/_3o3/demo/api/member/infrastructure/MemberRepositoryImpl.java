//package com._3o3.demo.api.member.infrastructure;
//
//import com._3o3.demo.api.member.application.dto.MemberSignInDTO;
//import com._3o3.demo.api.member.domain.Member;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.NoResultException;
//import jakarta.persistence.PersistenceContext;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@Repository
//public class MemberRepositoryImpl {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    /**
//     * 회원 가입
//     * @param member
//     * @return
//     */
//
//
//    /**
//     * 시퀀스로 회원 찾기
//     * @param user
//     * @return
//     */
//    public Member findById(Long id) {
//        return em.find(Member.class, id);
//    }
//
//    /**
//     * 로그인
//     * @param signinDto
//     * @return
//     */
//    public Member findOne(MemberSignInDTO signinDto) {
//        return em.find(Member.class, signinDto);
//    }
//
//    /**
//     * 중복 여부 확인 (주민 등록 번호)
//     * @param name
//     * @return
//     */
//    public Optional<Member> findByRegNo(String regNoBirth, String regNoEnc) {
//        List<Member> memberList = em.createQuery("select m from Member m where m.regNoBirth = :regNoBirth and m.regNoEnc = :regNoEnc", Member.class)
//                .setParameter("regNoBirth", regNoBirth)
//                .setParameter("regNoEnc" , regNoEnc)
//                .getResultList();
//
//        return memberList.stream().findAny();
//    }
//
//    /**
//     * 중복 여부 확인 ( 사용자 아이디)
//     */
//    public Optional<Member> findByUserName(String userId) {
//        Optional<Member> member = Optional.empty();
//       try {
//           member = Optional.ofNullable(em.createQuery("select m from Member m where m.userId = :userId", Member.class)
//                     .setParameter("userId", userId)
//                     .getSingleResult());
//        }catch (NoResultException e){
//           member = Optional.empty();
//        }
//
//       return member;
//    }
//}
