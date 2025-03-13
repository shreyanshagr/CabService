//package com.cab.user.repository;
//
//import com.cab.user.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, String> {
//   Optional<User> findByEmail(String email);
//   List<User> findAll();
//
//}