package com.rest.ybp.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public UserRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public int save(User user) {
        em.persist(user);

        return user.getId();
    }

    public void delete(User user) {
        em.remove(user);
    }

    public User getUserById(int id) {
        QUser qUser = QUser.user;

        return queryFactory
                .selectFrom(qUser)
                .where(qUser.id.eq(id))
                .fetchFirst();
    }

    public User getUserByName(String name) {
        QUser qUser = QUser.user;
        
        User user = null;
        try {
            if(name != null) {
                user = queryFactory
                .selectFrom(qUser)
                .where(qUser.name.eq(name))
                .fetchFirst();    
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByEmail(String email) {
        QUser qUser = QUser.user;

        return queryFactory
                .selectFrom(qUser)
                .where(qUser.name.eq(email))
                .fetchFirst();
    }
}
