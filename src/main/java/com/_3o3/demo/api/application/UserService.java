package com._3o3.demo.api.application;


import com._3o3.demo.api.application.dto.UserCreateDTO;
import com._3o3.demo.api.domain.User;
import com._3o3.demo.api.infrastructure.UserRepository;
import com._3o3.demo.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly=true) //성능 최적화
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 가입
     * @param user
     * @return
     */
    @Transactional
    public ApiResponse<Long> join(UserCreateDTO createDto) {

        //받은 객체를 Entity로 변환
        User user = createDto.toEntity();

        validationDuplicateType(user); // 중복 및 오타 검사

        //DB 전달
        Long result = userRepository.save(user);

        return result > 0 ? ApiResponse.of(HttpStatus.OK, result)
                : ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, result);
    }

    private void validationDuplicateType(User user) {
        Optional<User> findUsers = userRepository.findByName(user.getName());
        if(findUsers.isPresent()) { //이미 존재
            //throw
        }
    }


}
