package com.sparta.java_02.domain.user.entity;

import com.sparta.java_02.domain.purchase.entity.Purchase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

@Entity
@Getter
@DynamicInsert  // 필드 값이 null이 아닌 경우에만 insert 쿼리 작성해줌
@DynamicUpdate  // 필드 값이 변경되어야 update 쿼리 작성해줌
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA는 빈 생성자가 필요함. AccessLevel은 기본 생성자의 접근 레벨
//@AllArgsConstructor  // 모든 필드를 갖고 있는 생성자 생성
//@RequiredArgsConstructor  // private, final 필드만 갖고 있는 생성자 생성
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table
//@Table(name = "user")  // 클래스와 테이블명이 완벽히 일치하면 name 생략 가능
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) //  PK 생성을 DB의 AUTO_INCREMENT에 위임(순서대로 id 부여)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  //  @Column(name = "password_hash", nullable = false) // 카멜케이스와 스네이크케이스를 알아서 매칭해줌
  private String passwordHash;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @OneToMany(fetch = FetchType.EAGER)
  List<Purchase> purchases = new ArrayList<>();


  // 보통 클래스에 붙이지만, 엔티티에 한해서는 생성자에 붙임! (엔티티는 빈 생성자를 요구하기 때문)
  // 빈 생성자가 있는 상태에서 클래스에 @Builder를 달면 에러가 남
  // @Builder를 클래스에 달면 클래스에 있는 모든 필드가 다 있는 생성자를 원함
  @Builder
  public User(String name, String email, String passwordHash) {
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public void setName(String name) {
    if (StringUtils.hasText(name)) {
      this.name = name;
    }
  }

  public void setEmail(String email) {
    if (StringUtils.hasText(email)) {
      this.email = email;
    }
  }

  public void setPasswordHash(String passwordHash) {
    if (StringUtils.hasText(passwordHash)) {
      this.passwordHash = passwordHash;
    }
  }

}

