package com.infix.phukiencongnghe.data.source.local.source.user;

import com.infix.phukiencongnghe.data.source.local.dao.UserDAO;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;

import javax.inject.Inject;

public class UserLocalDataSourceImpl implements IUserLocalDataSource {
    private final UserDAO userDAO;

    @Inject
    public UserLocalDataSourceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void insert(UserEntity user) {
        userDAO.insert(user);
    }
}
