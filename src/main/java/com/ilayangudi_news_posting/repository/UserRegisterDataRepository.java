package com.ilayangudi_news_posting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ilayangudi_news_posting.entity.UserRegisterData;

@Repository
public interface UserRegisterDataRepository extends JpaRepository<UserRegisterData, Long>{

}
