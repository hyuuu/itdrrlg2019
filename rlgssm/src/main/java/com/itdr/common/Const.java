package com.itdr.common;

/**
 * Class: Const
 * version: JDK 1.8
 * create: 2019-09-14 16:09:25
 *
 * @author: Heyuu
 */
public class Const {
    // 通用成功状态码
    public static final Integer SUCCESS_CODE = 0;
    // 通用失败状态码
    public static final Integer ERROR_CODE = 1;

    // 所有token前缀
    public static final String TOKEN_PREFIX = "token_";
    // TokenCache初始化缓存项
    public static final Integer TOKEN_INIT_SIZE = 1000;
    // TokenCache最大缓存项
    public static final Integer TOKEN_MAX_SIZE = 10000;
    // TokenCache缓存时长
    public static final Integer TOKEN_TIMEOUT = 30;

    // 普通用户登录，将登录信息存储在session中的变量名
    public static final String USER_LOGIN_SESSION = "userLoginSession";
    // 管理员用户登录，将登录信息存储在session中的变量名
    public static final String MANAGE_USER_LOGIN_SESSION = "manageUserLoginSession";
    // 用户登录，返回给前端用户的变量名
    public static final String USER_LOGIN_RESPONSE = "userLoginResponse";

    // 用户模块
    public enum UserEnum{
        ROLE_ADMIN(0,"管理员用户"),
        ROLE_COMMON(1,"普通用户"),
        STATUS_NORMAL(0,"正常状态"),
        STATUS_DISABLE(1,"禁用状态"),
        // error
        USER_NOT_LOGIN(Const.ERROR_CODE,"用户未登录"),
        USER_LOGOUT(Const.ERROR_CODE,"退出成功"),
        PERMISSION_DENIED(Const.ERROR_CODE,"没有权限，拒绝访问"),

        PARAMETER_NULL(Const.ERROR_CODE,"参数不能为空"),
        PARAMETER_LESS_ZERO(Const.ERROR_CODE,"参数不能小于零"),
        PARAMETER_TYPE_NULL(Const.ERROR_CODE,"参数类型不能为空"),
        PARAMETER_TYPE_ERROR(Const.ERROR_CODE,"参数类型错误"),
        USERNAME_NULL(Const.ERROR_CODE,"用户名不能为空"),
        PASSWORD_NULL(Const.ERROR_CODE,"密码不能为空"),
        PASSWORD_NULL_OLD(Const.ERROR_CODE,"原密码不能为空"),
        PASSWORD_NULL_NEW(Const.ERROR_CODE,"新密码不能为空"),
        PASSWORD_ERROR(Const.ERROR_CODE,"密码错误"),
        PASSWORD_ERROR_OLD(Const.ERROR_CODE,"原密码错误"),
        PHONE_NULL(Const.ERROR_CODE,"手机号不能为空"),
        PHONE_TYPE_ERROR(Const.ERROR_CODE,"手机号格式不正确"),
        QUESTION_NULL(Const.ERROR_CODE,"密保问题不能为空"),
        QUESTION_NULL_2(Const.ERROR_CODE,"该用户未设置找回密码问题"),
        ANSWER_NULL(Const.ERROR_CODE,"密保答案不能为空"),
        ANSWER_ERROR(Const.ERROR_CODE,"密保答案错误"),
        USER_EXIST(Const.ERROR_CODE,"该用户已存在"),
        USER_NOT_EXIST(Const.ERROR_CODE,"该用户不存在"),
        EMAIL_EXIST(Const.ERROR_CODE,"该邮箱已存在"),
        EMAIL_NULL(Const.ERROR_CODE,"邮箱不能为空"),
        EMAIL_TYPE_ERROR(Const.ERROR_CODE,"邮箱格式不正确"),
        EMAIL_SEND_ERROR(Const.ERROR_CODE,"邮件发送失败"),
        VCODE_NULL(Const.ERROR_CODE,"验证码为空"),
        VCODE_TIMEOUT(Const.ERROR_CODE,"验证码失效"),
        VCODE_ERROR(Const.ERROR_CODE,"非法验证码"),
        REGISTER_ERROR(Const.ERROR_CODE,"注册失败"),
        UPDATE_ERROR(Const.ERROR_CODE,"更新失败"),
        SELECT_ERROR(Const.ERROR_CODE,"查询失败"),
        TOKEN_ERROR(Const.ERROR_CODE,"非法令牌"),
        TOKEN_TIMEOUT(Const.ERROR_CODE,"令牌已失效"),
        // success
        UPDATE_SUCCESS(Const.SUCCESS_CODE,"更新成功"),
        REGISTER_SUCCESS(Const.SUCCESS_CODE,"注册成功"),
        CHECK_SUCCESS(Const.SUCCESS_CODE,"校验成功");

        private Integer code;
        private String msg;
        private UserEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        public Integer getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }
    // 商品模块
    public enum ProductEnum{
        PAGE_NUM(0,"默认首页"),
        PAGE_SIZE(10,"默认一页的数量"),

        STATUS_NORMAL(1,"在售"),
        STATUS_DISABLE(2,"下架"),
        STATUS_DELETE(3,"删除"),

        ID_NULL(Const.ERROR_CODE,"产品ID不能为空"),
        ID_LESS_ZERO(Const.ERROR_CODE,"产品ID不能小于零"),
        CATEGORY_ID_NULL(Const.ERROR_CODE,"产品分类ID不能为空"),
        CATEGORY_ID_LESS_ZERO(Const.ERROR_CODE,"产品分类ID不能小于零"),
        NAME_NULL(Const.ERROR_CODE,"产品名称不能为空"),
        SUBTITLE_NULL(Const.ERROR_CODE,"商品副标题不能为空"),
        MAINIMAGE_NULL(Const.ERROR_CODE,"产品主图不能为空"),
        SUBIMAGES_NULL(Const.ERROR_CODE,"图片地址不能为空"),
        DETAIL_NULL(Const.ERROR_CODE,"商品详情不能为空"),
        PRICE_NULL(Const.ERROR_CODE,"商品价格不能为空"),
        STOCK_NULL(Const.ERROR_CODE,"商品库存数量不能为空"),
        STOCK_LESS_ZERO(Const.ERROR_CODE,"商品库存数量不能小于零"),
        STATUS_NULL(Const.ERROR_CODE,"商品状态不能为空"),

        PRODUCT_NOT_EXIST(Const.ERROR_CODE,"产品不存在"),

        SELECT_ERROR(Const.ERROR_CODE,"查询失败"),
        UPDATE_ERROR(Const.ERROR_CODE,"更新失败"),

        UPDATE_SUCCESS(Const.SUCCESS_CODE,"更新成功");

        private Integer code;
        private String msg;
        private ProductEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        public Integer getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }
    // 订单模块
    public enum OrderEnum{
        PAGE_NUM(0,"默认首页"),
        PAGE_SIZE(10,"默认一页的数量"),
        STATUS_0(0,"已取消"),
        STATUS_10(10,"未付款"),
        STATUS_20(20,"已付款"),
        STATUS_40(40,"已发货"),
        STATUS_50(50,"交易成功"),
        STATUS_60(60,"交易关闭"),

        ORDERNO_NULL(Const.ERROR_CODE,"订单号不能为空"),
        ORDER_NOT_EXIST(Const.ERROR_CODE,"订单不存在"),

        ORDER_STATUS_ERROR(Const.ERROR_CODE,"订单状态异常"),
        ORDER_STATUS_0(Const.ERROR_CODE,"订单已取消"),
        ORDER_STATUS_10(Const.ERROR_CODE,"订单未付款"),
        ORDER_STATUS_20(Const.ERROR_CODE,"订单已付款"),
        ORDER_STATUS_40(Const.ERROR_CODE,"订单已发货"),
        ORDER_STATUS_50(Const.ERROR_CODE,"订单已成功"),
        ORDER_STATUS_60(Const.ERROR_CODE,"订单已关闭"),

        SELECT_ERROR(Const.ERROR_CODE,"查询失败"),
        UPDATE_ERROR(Const.ERROR_CODE,"更新失败"),

        UPDATE_SUCCESS(Const.SUCCESS_CODE,"更新成功");

        private Integer code;
        private String msg;
        private OrderEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        public Integer getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }
    // 分类模块
    public enum CategoryEnum{
        ROOT_ID(0,"根节点"),
        STATUS_1(1,"正常"),
        STATUS_2(2,"已废弃"),

        ID_NULL(Const.ERROR_CODE,"分类id不能为空"),
        ID_LESS_ZERO(Const.ERROR_CODE,"分类id不能小于零"),
        PARENT_ID_NULL(Const.ERROR_CODE,"父分类id不能为空"),
        PARENT_LESS_ZERO(Const.ERROR_CODE,"父分类id不能小于零"),
        NAME_NULL(Const.ERROR_CODE,"分类名称不能为空"),
        CATEGORY_NOT_EXIST(Const.ERROR_CODE,"该分类不存在"),
        PARENT_CATEGORY_NOT_EXIST(Const.ERROR_CODE,"父分类不存在"),

        SELECT_ERROR(Const.ERROR_CODE,"查询失败"),
        UPDATE_ERROR(Const.ERROR_CODE,"更新失败"),

        UPDATE_SUCCESS(Const.SUCCESS_CODE,"更新成功");

        private Integer code;
        private String msg;
        private CategoryEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        public Integer getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
    }
}
