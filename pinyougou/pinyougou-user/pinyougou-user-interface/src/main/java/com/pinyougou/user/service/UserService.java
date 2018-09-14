package com.pinyougou.user.service;

import com.pinyougou.pojo.TbUser;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

public interface UserService extends BaseService<TbUser> {

    PageResult search(Integer page, Integer rows, TbUser user);

    /**
     * 发送短信验证码
     * @param phone 手机号
     */
    void sendSmsCode(String phone);

    /**
     * 校验手机号对应在redis中的验证码是否正确
     * @param phone 手机号
     * @param smsCode 验证码
     * @return 校验结果
     */
    boolean checkCode(String phone, String smsCode);
}