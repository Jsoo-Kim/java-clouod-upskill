package com.sparta.java_02.domain.user.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserResponse;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.dto.UserUpdateRequest;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.mapper.UserMapper;
import com.sparta.java_02.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final EntityManager entityManager;
  private final JdbcTemplate jdbcTemplate;

  private final UserMapper userMapper;

  private final UserRepository userRepository;


  @Transactional
  public List<UserSearchResponse> searchUser() {
    return userRepository.findAll().stream()
        .map(userMapper::toSearch)
        .toList();
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(Long userId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
    return null;
  }

  @Transactional
  public void create(UserCreateRequest request) {
    userRepository.save(userMapper.toEntity(request));
  }

  @Transactional
  public void update(Long userId, UserUpdateRequest request) {
    User user = getUser(userId);

    user.setName(request.getName());
    user.setEmail(request.getEmail());

    userRepository.save(user);
  }

  @Transactional
  public void delete(Long userId) {
    userRepository.delete(getUser(userId));
  }

  private User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
  }

  @Transactional
  public void saveAllUsersEntity(List<User> users) {
//    int batchSize = 1000;
//
//    for (int i = 0; i < users.size(); i++) {
//      User user = users.get(i);
//      entityManager.persist(user);
//
//      // flush()
//      if ((i + 1) % batchSize == 0) {
//        entityManager.flush(); // 쿼리를 내보내지만 영속성에 데이터가 남아 있게 됨
//        entityManager.clear(); // 영속성 클리어
//      }
//    }
//
//    entityManager.flush();
//    entityManager.clear();

    userRepository.saveAll(users);
  }

  @Transactional
  public void saveAllUsers(List<User> users) {
    String sql = "INSERT INTO user (name, email, password_hash, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, users, 1000, (ps, user) -> { // 여기의 batchSize가 yaml에 설정한 것보다 우선
      LocalDateTime now = LocalDateTime.now();
      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getPasswordHash());
      ps.setTimestamp(4, Timestamp.valueOf(now));
      ps.setTimestamp(5, Timestamp.valueOf(now));
    });


  }

}
