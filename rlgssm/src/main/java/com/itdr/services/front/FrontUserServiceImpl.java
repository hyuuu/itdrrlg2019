package com.itdr.services.front;

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
            return ResCode.error("用户名不能为空！");
        }
        if (password == null || password.equals("")){
            return ResCode.error("密码不能为空！");
        }
        // 检查 用户名 或 邮箱 是否存在
        User u = userMapper.selectByUsernameOrEmail(username, "username");
        if (u == null){
            return ResCode.error("用户不存在！");
        }
        if (u.getStatus() == 3){
            return ResCode.error("用户未激活！");
        }
        // 登录密码加密
        String md5Password = MD5Util.getMD5Code(password);
        // 根据用户名和密码查找一个用户
        User user = userMapper.selectByUsernameAndPassword(username, md5Password);
        if (user == null){
            return ResCode.error("密码错误！");
        }
        // user存入session，loginResponse作为返回对象
        User loginResponse = new User();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setCreateTime(user.getCreateTime());
        loginResponse.setUpdateTime(user.getUpdateTime());
        Map<String,User> userMap = new HashMap<String,User>();
        userMap.put("user",user);
        userMap.put("loginResponse",loginResponse);
        return ResCode.success(userMap);
    }

    /* 检查用户名或邮箱是否存在 */
    @Override
    public ResCode checkUserByUsernameOrEmail(String str, String type) {
        // 参数非空判断
        if (str == null || str.equals("")){
            return ResCode.error("参数不能为空！");
        }
        if (type == null || type.equals("")){
            return ResCode.error("参数类型不能为空！");
        }
        // type转成小写
        try {
            type = type.toLowerCase();
        }catch (Exception e){
            return ResCode.error("参数类型错误！");
        }
        // 限制type的值：username,email
        if (!type.equals("username") && !type.equals("email")){
            return ResCode.error("参数类型错误！");
        }
        // 检查 用户名 或 邮箱 是否存在
        User user = userMapper.selectByUsernameOrEmail(str, type);
        // 用户名已存在
        if (user != null && type.equals("username")){
            return ResCode.error("该用户名已存在！");
        }
        // 邮箱已存在
        if (user != null && type.equals("email")){
            return ResCode.error("该邮箱已存在！");
        }
        // 不存在
        return ResCode.success(0,null,"校验成功！");
    }

    /* 用户注册 */
    @Override
    public ResCode register(User user) {
        if (user.getUsername() == null || user.getUsername().equals("")){
            return ResCode.error("用户名不能为空！");
        }
        if (user.getPassword() == null || user.getPassword().equals("")){
            return ResCode.error("密码不能为空！");
        }
        if (user.getEmail() == null || user.getEmail().equals("")){
            return ResCode.error("邮箱不能为空！");
        }
        // 验证邮箱格式
        if (!Tools.isEmail(user.getEmail())){
            return ResCode.error("邮箱格式不正确！");
        }
        if (user.getPhone() == null || user.getPhone().equals("")){
            return ResCode.error("手机号不能为空！");
        }
        // 验证手机号码格式
        if (!Tools.isMobilePhone(user.getPhone())){
            return ResCode.error("手机号码格式不正确！");
        }
        if (user.getQuestion() == null || user.getQuestion().equals("")){
            return ResCode.error("密保问题不能为空！");
        }
        if (user.getAnswer() == null || user.getAnswer().equals("")){
            return ResCode.error("密保答案不能为空！");
        }
        // 检查该用户名和邮箱是否已被注册
        User u1 = userMapper.selectByUsernameOrEmail(user.getUsername(), "username");
        if (u1 != null){
            return ResCode.error("该用户名已存在！");
        }
        User u2 = userMapper.selectByUsernameOrEmail(user.getEmail(), "email");
        if (u2 != null){
            return ResCode.error("该邮箱已被注册！");
        }
        // MD5加密
        user.setPassword(MD5Util.getMD5Code(user.getPassword()));
        // 增加一个用户
        int insert = userMapper.insert(user);
        if (insert <= 0){
            return ResCode.error("注册失败！");
        }
        // 生成验证码
        String vCode = Tools.getVCode();
        // 发邮件
        boolean email = EmailUtil.sendEmail(user.getEmail(), user.getUsername(), vCode);
        if (!email){
            return ResCode.error("邮件发送失败！");
        }
        // 将username和vCode写入TokenCache缓存
        TokenCache.set("token_"+user.getUsername(),vCode);
        return ResCode.success(0,null,"用户注册成功，请在30分钟内激活！");
    }

    /* 用户激活 */
    @Override
    public ResCode activateUser(String username, String vCode) {
        if (username == null || username.equals("")){
            return ResCode.error("用户名为空，激活失败！");
        }
        if (vCode == null || vCode.equals("")){
            return ResCode.error("验证码为空，激活失败！");
        }
        String code = (String) TokenCache.get("token_" + username);
        if (code == null){
            return ResCode.error("激活超时，验证码失效！");
        }
        if (!code.equals(vCode)){
            return ResCode.error("非法验证码！");
        }
        User user = userMapper.selectByUsernameOrEmail(username, "username");
        if (user == null){
            return ResCode.error("用户不存在，激活失败！");
        }
        // 拦截通过激活用户来启用被禁用的用户的非法操作
        if (user.getStatus() == 1){
            return ResCode.error("非法操作！");
        }
        if (user.getStatus() == 0){
            return ResCode.error("用户已激活！");
        }
        Integer row = userMapper.updateStatusByUsername(username,0);
        if (row <= 0){
            return ResCode.error("激活失败！");
        }
        return ResCode.success(0,null,"激活成功！");
    }

    /* 登录状态更新个人信息 */
    @Override
    public ResCode updateInformation(User user) {
        if (user.getEmail() == null || user.getEmail().equals("")){
            return ResCode.error("邮箱不能为空！");
        }
        // 验证邮箱格式
        if (!Tools.isEmail(user.getEmail())){
            return ResCode.error("邮箱格式不正确！");
        }
        if (user.getPhone() == null || user.getPhone().equals("")){
            return ResCode.error("手机号不能为空！");
        }
        // 验证手机号码格式
        if (!Tools.isMobilePhone(user.getPhone())){
            return ResCode.error("手机号码格式不正确！");
        }
        if (user.getQuestion() == null || user.getQuestion().equals("")){
            return ResCode.error("密保问题不能为空！");
        }
        if (user.getAnswer() == null || user.getAnswer().equals("")){
            return ResCode.error("密保答案不能为空！");
        }
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i <= 0){
            return ResCode.error("更新失败，出现异常！");
        }
        return ResCode.success(0,null,"更新个人信息成功");
    }

    /* 忘记密码 */
    @Override
    public ResCode forgetGetQuestion(String username) {
        if (username == null || username.equals("")){
            return ResCode.error("参数不能为空！");
        }
        // 检查该用户是否存在
        User user = userMapper.selectByUsernameOrEmail(username, "username");
        if (user == null){
            return ResCode.error("该用户不存在！");
        }
        if (user.getQuestion() == null || user.equals("")){
            return ResCode.error("该用户未设置找回密码问题！");
        }
        return ResCode.success(user.getQuestion());
    }

    /* 忘记密码提交问题答案 */
    @Override
    public ResCode forgetCheckAnswer(String username, String question, String answer) {
        if (username == null || username.equals("")){
            return ResCode.error("用户名不能为空！");
        }
        if (question == null || question.equals("")){
            return ResCode.error("问题不能为空！");
        }
        if (answer == null || answer.equals("")){
            return ResCode.error("答案不能为空！");
        }
        User user = userMapper.selectByUsernameOrEmail(username, "username");
        if (!question.equals(user.getQuestion()) || !answer.equals(user.getAnswer())){
            return ResCode.error("问题答案错误！");
        }
        // 产生随机令牌token
        String token = UUID.randomUUID().toString();
        // 令牌存入TokenCache缓存
        TokenCache.set("token_"+username,token);
        return ResCode.success(token);
    }

    /* 忘记密码重置密码 */
    @Override
    public ResCode forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (username == null || username.equals("")){
            return ResCode.error("用户名不能为空！");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ResCode.error("新密码不能为空！");
        }
        if (forgetToken == null || forgetToken.equals("")){
            return ResCode.error("非法令牌1！");
        }
        String token = (String) TokenCache.get("token_" + username);
        if (token == null || token.equals("")){
            return ResCode.error("令牌已失效！");
        }
        if (!token.equals(forgetToken)){
            return ResCode.error("非法令牌2！");
        }
        //密码加密
        String md5Password = MD5Util.getMD5Code(passwordNew);
        Integer row = userMapper.updateByUsernameAndPassword(username, md5Password);
        if (row <= 0){
            return ResCode.error("修改密码操作失败");
        }
        return ResCode.success(0,null,"修改密码成功");
    }

    /* 登录中状态重置密码 */
    @Override
    public ResCode resetPassword(User user, String passwordOld, String passwordNew) {
        if (passwordOld == null || passwordOld.equals("")){
            return ResCode.error("原密码不能为空！");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ResCode.error("新密码不能为空！");
        }
        String pwdOld = MD5Util.getMD5Code(passwordOld);
        if (!pwdOld.equals(user.getPassword())){
            return ResCode.error("原密码错误！");
        }
        String md5Password = MD5Util.getMD5Code(passwordNew);
        Integer row = userMapper.updateByUsernameAndPassword(user.getUsername(),md5Password);
        if (row <= 0){
            return ResCode.error("更新失败！");
        }
        return ResCode.success(0,null,"修改密码成功");
    }


}
