package com.xab.stock.exception.user;

import com.xab.stock.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author template
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
