package com.avla.app.Interface;

import com.avla.app.model.User;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component
public abstract class UserComponent {
    public abstract User getUser();
}
