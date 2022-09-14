package com.gyfz.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /** 正常状态 */
    public static final String  STATUS_NORMAL = "0";
    /**
     * 友情链接状态审核通过
     */
    public static final String  LINK_STATUS_NORMAL = "0";
    /**
     * 评论类型为:文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型为:友链评论
     */
    public static final String LINK_COMMENT = "1";
    /****
     * 初始化文章浏览量redis Key
     */
    public static  final String ARTICLE_VIEW_COUNT = "article:viewCount";
    public static final String MENU = "C";
    public static final String BUTTON = "F";
    public static final String ADMIN ="1";
}
