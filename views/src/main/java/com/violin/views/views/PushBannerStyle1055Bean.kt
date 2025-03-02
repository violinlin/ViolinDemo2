package com.violin.views.views

data class PushBannerStyle1055Bean(
    var bg_url: String? = null,//大背景动图/静图
    var bg_type: Int? = null,//0 动图 1png 2代码实现渐变
    var gradient_colors: ArrayList<String?>? = null,//数组字符串  例：[“#3C5DC1”, “#3C5DC1”,]
    var avatar_1: String? = null,//圆的头像
    var avatar_2: String? = null,//方的头像 ，与avatar_1互斥的
    var right_icon: String? = null,//右侧图标，可为空
    var rich_text: String? = null,//复用营销类固底弹幕富文本的结构
    var url: String? = null//跳转链接
)
