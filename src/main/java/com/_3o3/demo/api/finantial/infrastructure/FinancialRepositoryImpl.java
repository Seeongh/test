//package com._3o3.demo.api.finantial.infrastructure;
//
//import com._3o3.demo.api.finantial.application.dto.AnnualFinancialAllDTO;
//import com._3o3.demo.api.finantial.domain.AnnualFinancial;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//
//@Slf4j
//@Repository
//public class FinancialRepositoryImpl {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    public Long save(AnnualFinancial annualFinancial) {
//        em.persist(annualFinancial);
//        return annualFinancial.getId();
//    }
//
//    public AnnualFinancialAllDTO findByUserId(String userId) {
//        return  (em.createQuery("select * from Member m where m.userId = :userId", AnnualFinancialAllDTO.class)
//                .setParameter("userId", userId)
//                .getSingleResult());
//    }
//}
