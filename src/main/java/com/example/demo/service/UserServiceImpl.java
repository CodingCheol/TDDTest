package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserEntity> getFindAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getFindByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Transactional
    public Long save(UserEntity user) {
        UserEntity userEntity= new UserEntity();
        userEntity.setUserName(user.getUserName());
        userEntity.setUserEmail(user.getUserEmail());
        userEntity.setUserAddress(user.getUserAddress());
        userRepository.save(userEntity);

        if( user.getSeqNo() > 0){
            return user.getSeqNo();
        }

        return 0L;
    }
}
