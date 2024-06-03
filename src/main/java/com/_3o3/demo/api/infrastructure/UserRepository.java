package com._3o3.demo.api.infrastructure;

import com._3o3.demo.api.application.dto.UserSignInDTO;
import com._3o3.demo.api.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 회원 가입
     * @param user
     * @return
     */
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }


    /**
     * 시퀀스로 회원 찾기
     * @param user
     * @return
     */
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    /**
     * 로그인
     * @param signinDto
     * @return
     */
    public User findOne(UserSignInDTO signinDto) {
        return em.find(User.class, signinDto);
    }

    /**
     * 중복 여부 확인 (주민 등록 번호)
     * @param name
     * @return
     */
    public Optional<User> findByRegNo(String regNo) {
        List<User> userList = em.createQuery("select u from User u where u.regNo = :regNo", User.class)
                .setParameter("regNo", regNo)
                .getResultList();

        return userList.stream().findAny();
    }

    /**
     * 중복 여부 확인 ( 사용자 아이디)
     */
    public Optional<User> findByUserName(String userId) {
        Optional<User> user = Optional.empty();
       try {
             user = Optional.ofNullable(em.createQuery("select u from User u where u.userId = :userId", User.class)
                     .setParameter("userId", userId)
                     .getSingleResult());
        }catch (NoResultException e){
            user = Optional.empty();
        }

       log.info("ash user = {}", user.toString());
       return user;
    }
}
