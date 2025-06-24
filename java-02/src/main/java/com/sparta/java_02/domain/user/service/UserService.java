package com.sparta.java_02.domain.user.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public List<UserSearchResponse> searchAll(Long userId) {

    if (ObjectUtils.isEmpty(userId)) {
      // 에러 발생해도 실행 되는 영역
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA);
    }

    try {

    } catch (Exception ex) {

    } finally {
      // finally에 넣으면 무조건 실행하고 나서 에러 뱉음. catch에 넣으면 실행한 로직을 에러로 둔갑시킬 수 있음
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA);
    }

//    return new ArrayList<>();
  }

  public void save() {
//    userRepository.save();
  }

}
