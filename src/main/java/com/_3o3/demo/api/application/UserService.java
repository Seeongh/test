package com._3o3.demo.api.application;


import com._3o3.demo.api.application.dto.UserCreateDTO;
import com._3o3.demo.api.domain.User;
import com._3o3.demo.api.infrastructure.UserRepository;
import com._3o3.demo.common.ApiResponse;
import com._3o3.demo.common.CustomValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly=true) //성능 최적화
@RequiredArgsConstructor
public class UserService {

    //유저 저장공간
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원 가입
     * @param user
     * @return
     */
    @Transactional
    public ApiResponse<Long> join(UserCreateDTO createDto) {

        //DTO -> Entity 로 변경
        //비밀번호, 주소 암호화
        User user = createDto.toEntity(bCryptPasswordEncoder);

        validationDuplicateType(user); // 중복 및 오타 검사

        //DB 전달
        Long result = userRepository.save(user);

        return result > 0 ? ApiResponse.of(HttpStatus.OK, result)
                : ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, result);
    }


    private void validationDuplicateType(User user) {
        Optional<User> findUsers = userRepository.findByRegNo(user.getName());
        if(findUsers.isPresent()) { //이미 존재
            throw new CustomValidationException();
        }
    }


}
