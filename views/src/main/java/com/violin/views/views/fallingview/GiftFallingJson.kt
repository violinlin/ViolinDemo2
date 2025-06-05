package com.violin.views.views.fallingview


data class FallingViewConfig(
    val icon: String? = null,
    val animTimeSecond: Int? = null,// 动画时长
    val iconSizePX: Int = 100,// 礼物大小
    var maxDensity: Int = 60, // 礼物数量
    var direction: Int = 0,//0 向下，1 向上
    var isMoveX: Boolean = true,// x轴是否移动
    var ySpeedBuffer: Int = 0,// y轴的下落速度加成
    var delayTime: Int? = null,// 刷新时间，单位ms
    var sizeScale: Float? = null// 尺寸缩放范围
)