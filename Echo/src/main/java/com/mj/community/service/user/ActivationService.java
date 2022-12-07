package com.mj.community.service.user;

/**
 * @author pzj
 */
public interface ActivationService {
    int activation(int userId, String code);
}
