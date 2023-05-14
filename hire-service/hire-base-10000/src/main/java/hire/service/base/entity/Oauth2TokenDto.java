package hire.service.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Oauth2获取Token返回信息封装
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Oauth2TokenDto {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌头前缀
     */
    private String tokenHead;
    /**
     * 有效时间（秒）
     */
    private int expiresIn;
    /**
     * 用户角色
     */
    private String role;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nick;
    /**
     * 用户性别
     */
    private boolean sex;

    /**
     * 返回未读消息数量
     */
    private int message;
}