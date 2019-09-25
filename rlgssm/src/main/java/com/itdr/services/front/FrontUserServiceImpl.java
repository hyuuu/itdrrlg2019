package com.itdr.services.front;

import com.itdr.common.Const;
import com.itdr.common.ResCode;
import com.itdr.common.TokenCache;
import com.itdr.mappers.UserMapper;
import com.itdr.pojo.User;
import com.itdr.utils.EmailUtil;
import com.itdr.utils.MD5Util;
import com.itdr.utils.Tools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Class: UserServiceImpl
 * version: JDK 1.8
 * create: 2019-09-10 18:53:12
 *
 * @author: Heyuu
 */
@Service
@Transactional
public class FrontUserServiceImpl implements FrontUserService {
    @Resource
    private UserMapper userMapper;

    /* 用户登录 */
    @Override
    public ResCode login(String username, String password) {
        if (username == null || username.equals("")){
            return ResCode.error(Const.UserEnum.USERNAME_NULL.getMsg());
        }
        if (password == null || password.equals("")){
            return ResCode.error(Const.UserEnum.PASSWORD_NULL.getMsg());
        }
        // 检查 用户名 或 邮箱 是否存在
        User user = userMapper.selectByUsernameOrEmail(username, "username");
        if (user == null){
            return ResCode.error(Const.UserEnum.USER_NOT_EXIST.getMsg());
        }
        // 登录密码加密
        String md5Password = MD5Util.getMD5Code(password);
        if (!user.getPassword().equals(md5Password)){
            return ResCode.error(Const.UserEnum.PASSWORD_ERROR.getMsg());
        }
        return ResCode.success(user);
    }

    /* 检查用户名或邮箱是否存在 */
    @Override
    public ResCode checkUserByUsernameOrEmail(String str, String type) {
        // 参数非空判断
        if (str == null || str.equals("")){
            return ResCode.error(Const.UserEnum.PARAMETER_NULL.getMsg());
        }
        if (type == null || type.equals("")){
            return ResCode.error(Const.UserEnum.PARAMETER_TYPE_NULL.getMsg());
        }
        // type转成小写
        try {
            type = type.toLowerCase();
        }catch (Exception e){
            return ResCode.error(Const.UserEnum.PARAMETER_TYPE_ERROR.getMsg());
        }
        // 限制type的值：username,email
        if (!type.equals("username") && !type.equals("email")){
            return ResCode.error(Const.UserEnum.PARAMETER_TYPE_ERROR.getMsg());
        }
        // 检查 用户名 或 邮箱 是否存在
        User user = userMapper.selectByUsernameOrEmail(str, type);
        // 用户名已存在
        if (user != null && type.equals("username")){
            return ResCode.error(Const.UserEnum.USER_EXIST.getMsg());
        }
        // 邮箱已存在
        if (user != null && type.equals("email")){
            return ResCode.error(Const.UserEnum.EMAIL_EXIST.getMsg());
        }
        // 不存在
        return ResCode.success(Const.SUCCESS_CODE,null,Const.UserEnum.CHECK_SUCCESS.getMsg());
    }

    /* 用户注册 */
    @Override
    public ResCode register(User user) {
        if (user.getUsername() == null || user.getUsername().equals("")){
            return ResCode.error(Const.UserEnum.USERNAME_NULL.getMsg());
        }
        if (user.getPassword() == null || user.getPassword().equals("")){
            return ResCode.error(Const.UserEnum.PASSWORD_NULL.getMsg());
        }
        if (user.getEmail() == null || user.getEmail().equals("")){
            return ResCode.error(Const.UserEnum.EMAIL_NULL.getMsg());
        }
        // 验证邮箱格式
        if (!Tools.isEmail(user.getEmail())){
            return ResCode.error(Const.UserEnum.EMAIL_TYPE_ERROR.getMsg());
        }
        if (user.getPhone() == null || user.getPhone().equals("")){
            return ResCode.error(Const.UserEnum.PHONE_NULL.getMsg());
        }
        // 验证手机号码格式
        if (!Tools.isMobilePhone(user.getPhone())){
            return ResCode.error(Const.UserEnum.PHONE_TYPE_ERROR.getMsg());
        }
        if (user.getQuestion() == null || user.getQuestion().equals("")){
            return ResCode.error(Const.UserEnum.QUESTION_NULL.getMsg());
        }
        if (user.getAnswer() == null || user.getAnswer().equals("")){
            return ResCode.error(Const.UserEnum.ANSWER_NULL.getMsg());
        }
        // 检查该用户名和邮箱是否已被注册
        User u1 = userMapper.selectByUsernameOrEmail(user.getUsername(), "username");
        if (u1 != null){
            return ResCode.error(Const.UserEnum.USER_EXIST.getMsg());
        }
        User u2 = userMapper.selectByUsernameOrEmail(user.getEmail(), "email");
        if (u2 != null){
            return ResCode.error(Const.UserEnum.EMAIL_EXIST.getMsg());
        }
        // 生成验证码：六位随机验证码-UUID
        String vCode = Tools.getVCode()+"-"+UUID.randomUUID().toString();
        // 发邮件
        boolean email = EmailUtil.sendEmail(user.getEmail(),vCode);
        if (!email){
            return ResCode.error(Const.UserEnum.EMAIL_SEND_ERROR.getMsg());
        }
        // 以VCode为key，将user存入缓存
        TokenCache.set(Const.TOKEN_PREFIX+vCode,user);
        return ResCode.success(Const.SUCCESS_CODE,null,"请在"+Const.TOKEN_TIMEOUT+"分钟内前往邮箱"+user.getEmail()+"激活！");
    }

    /* 用户激活 */
    @Override
    public ResCode activateUser(String vCode) {
        if (vCode == null || vCode.equals("")){
            return ResCode.error(Const.UserEnum.VCODE_NULL.getMsg());
        }
        // 已vCode为键，从缓存中获取user
        User user = (User) TokenCache.get("token_" + vCode);
        if (user == null){
            return ResCode.error(Const.UserEnum.VCODE_TIMEOUT.getMsg());
        }
        // 清除缓存中的 vCode
        TokenCache.clearCache("token_" + vCode);
        // 检查该用户名和邮箱是否已被注册
        User u1 = userMapper.selectByUsernameOrEmail(user.getUsername(), "username");
        if (u1 != null){
            return ResCode.error(Const.UserEnum.USER_EXIST.getMsg());
        }
        User u2 = userMapper.selectByUsernameOrEmail(user.getEmail(), "email");
        if (u2 != null){
            return ResCode.error(Const.UserEnum.EMAIL_EXIST.getMsg());
        }
        // 密码加密
        user.setPassword(MD5Util.getMD5Code(user.getPassword()));
        int row = userMapper.insert(user);
        if (row <= 0){
            return ResCode.error(Const.UserEnum.REGISTER_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.UserEnum.REGISTER_SUCCESS.getMsg());
    }

    /* 登录状态更新个人信息 */
    @Override
    public ResCode updateInformation(User user) {
        if (user.getEmail() == null || user.getEmail().equals("")){
            return ResCode.error(Const.UserEnum.EMAIL_NULL.getMsg());
        }
        // 验证邮箱格式
        if (!Tools.isEmail(user.getEmail())){
            return ResCode.error(Const.UserEnum.EMAIL_TYPE_ERROR.getMsg());
        }
        if (user.getPhone() == null || user.getPhone().equals("")){
            return ResCode.error(Const.UserEnum.PHONE_NULL.getMsg());
        }
        // 验证手机号码格式
        if (!Tools.isMobilePhone(user.getPhone())){
            return ResCode.error(Const.UserEnum.PHONE_TYPE_ERROR.getMsg());
        }
        if (user.getQuestion() == null || user.getQuestion().equals("")){
            return ResCode.error(Const.UserEnum.QUESTION_NULL.getMsg());
        }
        if (user.getAnswer() == null || user.getAnswer().equals("")){
            return ResCode.error(Const.UserEnum.ANSWER_NULL.getMsg());
        }
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i <= 0){
            return ResCode.error(Const.UserEnum.UPDATE_ERROR.getMsg());
        }
        User userAfterUpdate = userMapper.selectByPrimaryKey(user.getId());
        return ResCode.success(Const.SUCCESS_CODE, userAfterUpdate, Const.UserEnum.UPDATE_SUCCESS.getMsg());
    }

    /* 忘记密码 */
    @Override
    public ResCode forgetGetQuestion(String username) {
        if (username == null || username.equals("")){
            return ResCode.error(Const.UserEnum.USERNAME_NULL.getMsg());
        }
        // 检查该用户是否存在
        User user = userMapper.selectByUsernameOrEmail(username, "username");
        if (user == null){
            return ResCode.error(Const.UserEnum.USER_NOT_EXIST.getMsg());
        }
        if (user.getQuestion() == null || user.equals("")){
            return ResCode.error(Const.UserEnum.QUESTION_NULL_2.getMsg());
        }
        return ResCode.success(user.getQuestion());
    }

    /* 忘记密码提交问题答案 */
    @Override
    public ResCode forgetCheckAnswer(String username, String question, String answer) {
        if (username == null || username.equals("")){
            return ResCode.error(Const.UserEnum.USERNAME_NULL.getMsg());
        }
        if (question == null || question.equals("")){
            return ResCode.error(Const.UserEnum.QUESTION_NULL.getMsg());
        }
        if (answer == null || answer.equals("")){
            return ResCode.error(Const.UserEnum.ANSWER_NULL.getMsg());
        }
        User user = userMapper.selectByUsernameOrEmail(username, "username");
        if (!question.equals(user.getQuestion()) || !answer.equals(user.getAnswer())){
            return ResCode.error(Const.UserEnum.ANSWER_ERROR.getMsg());
        }
        // 产生随机令牌token
        String token = UUID.randomUUID().toString();
        // 令牌存入TokenCache缓存
        TokenCache.set(Const.TOKEN_PREFIX+username,token);
        return ResCode.success(token);
    }

    /* 忘记密码重置密码 */
    @Override
    public ResCode forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (username == null || username.equals("")){
            return ResCode.error(Const.UserEnum.USERNAME_NULL.getMsg());
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ResCode.error(Const.UserEnum.PASSWORD_NULL_NEW.getMsg());
        }
        if (forgetToken == null || forgetToken.equals("")){
            return ResCode.error(Const.UserEnum.TOKEN_ERROR.getMsg());
        }
        String token = (String) TokenCache.get(Const.TOKEN_PREFIX + username);
        if (token == null || token.equals("")){
            return ResCode.error(Const.UserEnum.TOKEN_TIMEOUT.getMsg());
        }
        if (!token.equals(forgetToken)){
            return ResCode.error(Const.UserEnum.TOKEN_ERROR.getMsg());
        }
        // 清除令牌，只能使用一次
        TokenCache.clearCache(Const.TOKEN_PREFIX + username);
        //密码加密
        String md5Password = MD5Util.getMD5Code(passwordNew);
        Integer row = userMapper.updateByUsernameAndPassword(username, md5Password);
        if (row <= 0){
            return ResCode.error(Const.UserEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.UserEnum.UPDATE_SUCCESS.getMsg());
    }

    /* 登录中状态重置密码 */
    @Override
    public ResCode resetPassword(User user, String passwordOld, String passwordNew) {
        if (passwordOld == null || passwordOld.equals("")){
            return ResCode.error(Const.UserEnum.PASSWORD_NULL_OLD.getMsg());
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ResCode.error(Const.UserEnum.PASSWORD_NULL_NEW.getMsg());
        }
        String pwdOld = MD5Util.getMD5Code(passwordOld);
        if (!pwdOld.equals(user.getPassword())){
            return ResCode.error(Const.UserEnum.PASSWORD_ERROR_OLD.getMsg());
        }
        String md5Password = MD5Util.getMD5Code(passwordNew);
        Integer row = userMapper.updateByUsernameAndPassword(user.getUsername(),md5Password);
        if (row <= 0){
            return ResCode.error(Const.UserEnum.UPDATE_ERROR.getMsg());
        }
        return ResCode.success(Const.SUCCESS_CODE,null,Const.UserEnum.UPDATE_SUCCESS.getMsg());
    }
}
