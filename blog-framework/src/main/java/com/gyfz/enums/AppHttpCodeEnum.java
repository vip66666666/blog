package com.gyfz.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png/jpg文件"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    TAG_NOTNULL(513, "标签不能为空"),
    TAG_EXIST(514,"标签已存在"),
    CATEGORY_NOTNULL(515, "分类名不能为空"),
    CATEGORY_EXIST(516,"分类名已存在"),
    LINK_NAME_NOTNULL(515, "友链名称不能为空"),
    LINK_NAME_EXIST(516,"友链名称已存在"),
    ROLE_NAME(516,"角色名称不能为空"),
    ROLE_KEY(516,"权限字符不能为空"),
    DELETE_EXIST_SUBMENU(517,"存在子菜单不允许删除"),
    EDIT_NOT_PARENT_MENU(518,"父菜单不能为当前菜单"),
    UPLOAD_ERROR(507, "文件上传失败"),
    LOGIN_ERROR(505,"用户名或密码错误");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
