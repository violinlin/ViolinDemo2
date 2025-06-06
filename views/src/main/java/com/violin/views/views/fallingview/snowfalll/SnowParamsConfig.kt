package com.violin.views.views.fallingview.snowfalll

import android.graphics.Bitmap

data class SnowParamsConfig(
    var parentWidth: Int = 0,
    var parentHeight: Int = 0,
    var imageUrl: String? = null,// 图片地址
    var image: Bitmap? = null,// 需要绘制的图片
    var alphaMin: Int = SnowfallView.DEFAULT_SNOWFLAKE_ALPHA_MIN,// 最小透明度
    var alphaMax: Int = SnowfallView.DEFAULT_SNOWFLAKE_ALPHA_MAX,// 最大透明度
    var angleMax: Int = SnowfallView.DEFAULT_SNOWFLAKE_ANGLE_MAX,
    var sizeMinInPx: Int = 0,// 最小尺寸
    var sizeMaxInPx: Int = 0,// 最大尺寸
    var speedMin: Int = SnowfallView.DEFAULT_SNOWFLAKE_SPEED_MIN,// 最小速度
    var speedMax: Int = SnowfallView.DEFAULT_SNOWFLAKE_SPEED_MAX,// 最大速度
    var fadingEnabled: Boolean = SnowfallView.DEFAULT_SNOWFLAKES_FADING_ENABLED, // 飘落时alpha变化
    var alreadyFalling: Boolean = SnowfallView.DEFAULT_SNOWFLAKES_ALREADY_FALLING, // 第一帧是否有已经下落的bitmap
    var snowflakesNum: Int = 20,
    var animTimeSecond: Int = -1,// 动画持续时间 -1 一直播放
    var direction: Int = 0,// //0 向下，1 向上
    var isMoveX: Boolean = true// x轴是否移动
)

