package com.violin.features.common.json

import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentMoshiDemoBinding
import okio.Buffer

class MoshiDemoFragment : BaseFragment<FragmentMoshiDemoBinding>() {


    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMoshiDemoBinding {
        return FragmentMoshiDemoBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.etJson.setText(SAMPLE_JSON)
        binding.tvResult.text = "点击解析按钮开始"

        binding.btnParse.setOnClickListener {
            parseByJsonReader(binding.etJson.text.toString())
        }
        binding.btnReset.setOnClickListener {
            binding.etJson.setText(SAMPLE_JSON)
            binding.tvResult.text = "已重置示例 JSON"
        }
    }

    private fun parseByJsonReader(json: String) {
        val result = runCatching {
            val reader = JsonReader.of(Buffer().writeUtf8(json))
            buildString {
                appendLine("JsonReader 只解析 id、d 字段：")
                readTargetObject(reader)
            }
        }.getOrElse { throwable ->
            buildString {
                appendLine("JsonReader 解析失败：")
                appendLine("${throwable.javaClass.simpleName}: ${throwable.message.orEmpty()}")
            }
        }

        binding.tvResult.text = result
    }

    private val moshi = Moshi.Builder().build()
    private val anyAdapter = moshi.adapter(Any::class.java)

    private fun readValueAsJsonString(reader: JsonReader): String {
        val value = reader.readJsonValue()
        return anyAdapter.toJson(value)
    }


    private fun StringBuilder.readTargetObject(reader: JsonReader) {
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            when (name) {
                "id" -> appendLine("${name}:${reader.nextString()}")
                "d" -> {
                    reader.nextSource().use { source ->
                        val dJson = source.readUtf8()
                        appendLine("d:$dJson")
                    }
                }

                else -> reader.skipValue()
            }
        }
        reader.endObject()
    }


    companion object {
        private val SAMPLE_JSON = """
           {
             "id" : "615ad163a854",
             "t" : 3,
             "cid" : "me-live",
             "d" : {
               "type" : 8888,
               "data" : {
                 "list" : [ {
                   "id" : "d14f5237553f",
                   "t" : 3,
                   "cid" : "me-live",
                   "d" : {
                     "type" : 827,
                     "id" : 20514,
                     "data" : {
                       "room_info_msg" : { }
                     },
                     "s_t" : 1781694958750
                   },
                   "ts" : 1781694958750
                 }, {
                   "id" : "78321784ade4",
                   "t" : 3,
                   "cid" : "me-live",
                   "d" : {
                     "type" : 100,
                     "id" : 20514,
                     "data" : {
                       "member" : {
                         "id" : 17012507,
                         "avatar" : 1346446066,
                         "name" : "测试账号qq",
                         "avatar_urls" : {
                           "aspect_low" : {
                             "urls" : [ "http://me-media-gateway.test03.youyisia.com/accountv2/avatar/id/1346446066/sz/228" ]
                           },
                           "origin" : {
                             "urls" : [ "http://me-media-gateway.test03.youyisia.com/accountv2/view/id/1346446066/sz/src" ]
                           }
                         },
                         "gender" : 2,
                         "live_privilege" : {
                           "has_privilege" : 1,
                           "mid" : 17012507,
                           "medals" : [ {
                             "id" : 50957,
                             "name" : "中东工会SSSS",
                             "name_ar" : " وكالة SSSS",
                             "icon" : "http://img01.mehiya.com/img/png/id/66056218331",
                             "dmk_icon" : "http://img01.mehiya.com/img/png/id/66056218332",
                             "icon_v2" : "http://img01.mehiya.com/img/png/id/66056218336",
                             "et" : 1786861370,
                             "hw_ratio" : 0.3,
                             "msg_type" : 1,
                             "level" : 0,
                             "type" : 0
                           }, {
                             "id" : 30124,
                             "name" : "Highest contribution",
                             "name_ar" : "أعلى مساهمة",
                             "icon" : "http://img01.mehiya.com/img/png/id/2369882452",
                             "dmk_icon" : "http://img01.mehiya.com/img/png/id/2369882452",
                             "icon_v2" : "http://img01.mehiya.com/img/png/id/2369882452",
                             "expire_duration" : 604800,
                             "et" : 1782281968,
                             "hw_ratio" : 1,
                             "level" : 0,
                             "type" : 0
                           }, {
                             "id" : 30123,
                             "name" : "Highest contribution",
                             "name_ar" : "أعلى مساهمة",
                             "icon" : "http://img01.mehiya.com/img/png/id/2369882479",
                             "dmk_icon" : "http://img01.mehiya.com/img/png/id/2369882479",
                             "icon_v2" : "http://img01.mehiya.com/img/png/id/2369882479",
                             "expire_duration" : 604800,
                             "et" : 1783328418,
                             "hw_ratio" : 1,
                             "level" : 0,
                             "type" : 0
                           }, {
                             "id" : 50012,
                             "name" : "家族周榜勋章",
                             "name_ar" : "家族周榜勋章",
                             "icon" : "http://img01.mehiya.com/img/png/id/12956333740",
                             "dmk_icon" : "http://img01.mehiya.com/img/png/id/12956333747",
                             "icon_v2" : "http://img01.mehiya.com/img/png/id/12956333756",
                             "expire_duration" : 111111,
                             "et" : 1781730100,
                             "hw_ratio" : 1,
                             "msg_type" : 1,
                             "level" : 0,
                             "type" : 0
                           }, {
                             "id" : 52359,
                             "name" : "主播中心_等级5",
                             "icon" : "http://img01.mehiya.com/img/png/id/135060890246",
                             "dmk_icon" : "http://img01.mehiya.com/img/png/id/135060890254",
                             "icon_v2" : "http://img01.mehiya.com/img/png/id/135060890270",
                             "et" : 1782546316,
                             "hw_ratio" : 1,
                             "msg_type" : 1,
                             "level" : 0,
                             "type" : 0
                           }, {
                             "id" : 52358,
                             "name" : "主播中心_等级4",
                             "icon" : "http://img01.mehiya.com/img/png/id/135060889598",
                             "dmk_icon" : "http://img01.mehiya.com/img/png/id/135060889612",
                             "icon_v2" : "http://img01.mehiya.com/img/png/id/135060889616",
                             "et" : 1782546311,
                             "hw_ratio" : 1,
                             "msg_type" : 1,
                             "level" : 0,
                             "type" : 0
                           } ],
                           "vip_medal_list" : [ {
                             "id" : 56217,
                             "name" : "SVIP2",
                             "name_ar" : "SVIP2",
                             "icon" : "https://img01.mehiya.com/img/png/id/459783901386",
                             "dmk_icon" : "https://img01.mehiya.com/img/png/id/459783901401",
                             "icon_v2" : "https://img01.mehiya.com/img/png/id/459783901406",
                             "et" : 1785531598,
                             "hw_ratio" : 0.333333,
                             "msg_type" : 1,
                             "level" : 0,
                             "type" : 0
                           } ],
                           "ra_medal_list" : [ {
                             "id" : 53155,
                             "name" : "EN正式币商勋章",
                             "icon" : "https://img01.mehiya.com/img/png/id/185879072798",
                             "dmk_icon" : "https://img01.mehiya.com/img/png/id/185879072798",
                             "icon_v2" : "https://img01.mehiya.com/img/png/id/185879072798",
                             "et" : 2097054089,
                             "hw_ratio" : 0.260869565,
                             "level" : 3,
                             "type" : 0
                           } ],
                           "avatar" : {
                             "id" : 53451,
                             "name" : "贵族公爵-5级",
                             "icon" : "https://img01.mehiya.com/download/file?obj=mefile/65/5d/f8d0-00bf-11ef-ae81-00163e04069f",
                             "h5_icon" : "https://img01.mehiya.com/img/png/id/309845885008",
                             "et" : 1781989199,
                             "zip_url" : "https://img01.mehiya.com/download/file?obj=mefile/64/e4/40d0-00bf-11ef-b0ae-00163e0442bb",
                             "zip_enable" : true,
                             "level" : 0,
                             "from_mid" : 0,
                             "c_level" : 0,
                             "source" : "贵族公爵"
                           },
                           "vfx" : {
                             "id" : 30121,
                             "name" : "炫酷跑车",
                             "expire_duration" : 604800,
                             "light_url" : "https://img01.mehiya.com/img/png/id/4260170",
                             "light_url_v2" : "https://img01.mehiya.com/download/file?obj=mefile/hyfo/ae/83/c6c8-f8d3-11ef-845c-00163e0442bb",
                             "big_car_url" : "http://video01.mehiya.com/mevd/9b/ca/3953-bf29-11ed-ae81-00163e04069f",
                             "bg_url" : "https://img01.mehiya.com/img/png/id/4260197",
                             "avatar_url" : "https://img01.mehiya.com/img/png/id/4260195",
                             "show_avatar" : true,
                             "wel_msg" : "entered the room",
                             "wel_msg_map" : {
                               "ar" : "دخل الغرفة",
                               "en" : "entered the room",
                               "es" : "entró a la sala",
                               "hi" : "रूम में प्रवेश किया",
                               "hk" : "進入了房間",
                               "id" : "memasuki ruangan",
                               "ms" : "masuk bilik",
                               "pa" : "ਕਮਰੇ ਵਿੱਚ ਦਾਖਲ ਹੋਇਆ",
                               "pt" : "entrou na sala",
                               "ta" : "அறைக்குள்",
                               "te" : "గదిలోకి ప్రవేశించారు",
                               "test" : "அறைக்குள் நுழைந்தான்",
                               "th" : "เข้าห้อง",
                               "tr" : "odaya girildi",
                               "vi" : "Vào phòng"
                             },
                             "et" : 1782281968,
                             "time" : 5,
                             "type" : 2,
                             "show_join_bar" : true,
                             "level" : 0,
                             "from_mid" : 0,
                             "c_level" : 40,
                             "source" : "活动奖励"
                           },
                           "dmk_bubble" : {
                             "id" : 53462,
                             "name" : "贵族公爵-5级",
                             "back_ios" : "https://img01.mehiya.com/img/png/id/204417897758",
                             "back_andr" : "https://img01.mehiya.com/img/png/id/204417897770",
                             "left_down" : "https://img01.mehiya.com/img/png/id/204417897780",
                             "right_up" : "https://img01.mehiya.com/img/png/id/204417897783",
                             "right_down" : "https://img01.mehiya.com/img/png/id/204417897784",
                             "et" : 1781989199,
                             "level" : 0,
                             "not_turn_over" : false,
                             "from_mid" : 0,
                             "effect_area" : "",
                             "c_level" : 0,
                             "source" : "贵族公爵"
                           },
                           "profile" : {
                             "id" : 53440,
                             "name" : "贵族公爵-5级",
                             "nck_colors" : {
                               "style" : 2,
                               "colors" : [ "#9900FF", "#FFC200" ]
                             },
                             "et" : 1781989199,
                             "card_url_v2" : "https://img01.mehiya.com/img/png/id/205038663110",
                             "left_url_v2" : "https://img01.mehiya.com/img/png/id/205021858951",
                             "right_url_v2" : "https://img01.mehiya.com/img/png/id/205021858963",
                             "from_mid" : 0,
                             "dynamic_url" : "https://img01.mehiya.com/download/file?obj=mefile/42/ef/affc-009f-11ef-ae81-00163e04069f",
                             "c_level" : 30,
                             "source" : "贵族"
                           },
                           "voice_print" : {
                             "id" : 7,
                             "android_ani" : "https://img01.mehiya.com/download/file?obj=mefile/57/ad/e295-fd5c-11ee-b0ae-00163e0442bb",
                             "ios_ani" : "https://img01.mehiya.com/download/file?obj=mefile/c8/12/8efb-008f-11ef-b0ae-00163e0442bb",
                             "backpack_icon" : "",
                             "et" : 1781989199,
                             "name" : "贵族公爵-5级",
                             "equip_url" : "",
                             "from_mid" : 0,
                             "effect_area" : "",
                             "c_level" : 20,
                             "source" : "贵族"
                           },
                           "ari_medal_list" : [ {
                             "id" : 53428,
                             "name" : "贵族公爵-5级",
                             "icon" : "https://img01.mehiya.com/download/file?obj=mefile/6c/7d/c51b-2a33-11ef-ae81-00163e04069f",
                             "dmk_icon" : "https://img01.mehiya.com/download/file?obj=mefile/6c/7d/c51b-2a33-11ef-ae81-00163e04069f",
                             "icon_v2" : "https://img01.mehiya.com/download/file?obj=mefile/6c/7d/c51b-2a33-11ef-ae81-00163e04069f",
                             "et" : 1781989199,
                             "hw_ratio" : 0.344,
                             "level" : 0,
                             "type" : 0
                           } ],
                           "on_mic_vfx" : null,
                           "ari_level" : 5,
                           "campaign_tags" : null,
                           "mystery_medal_list" : null,
                           "enter_room_effect" : {
                             "id" : 4,
                             "name" : "财富lv61-70进场条",
                             "bg_url" : "https://img01.mehiya.com/download/file?obj=mefile/hyfo/b8/da/aaac-16b6-11f0-845c-00163e0442bb",
                             "bg_type" : 1,
                             "light_url" : "https://img01.mehiya.com/download/file?obj=mefile/hyfo/b8/e6/afd7-16b6-11f0-845c-00163e0442bb",
                             "light_type" : 1,
                             "avatar_url" : "https://img01.mehiya.com/img/png/id/344943835748",
                             "icon" : "https://img01.mehiya.com/img/png/id/344943835764",
                             "name_color" : "#7E2603",
                             "et" : 2096437992,
                             "c_level" : 40,
                             "source" : "财富等级"
                           },
                           "profile_effect" : {
                             "id" : 0,
                             "dynamic" : "https://img01.mehiya.com/download/file?obj=mefile/hyfo/8b/13/067d-2033-11f0-845c-00163e0442bb",
                             "icon" : "https://img01.mehiya.com/img/png/id/350144767558",
                             "et" : 1784258520,
                             "name" : "装扮图鉴 - Lv23个人资料页特效",
                             "c_level" : 20,
                             "source" : "装扮图鉴"
                           },
                           "app_set" : 2
                         },
                         "charm_level" : {
                           "id" : 60,
                           "icon" : "http://img01.mehiya.com/img/png/id/4270818"
                         },
                         "wealth_level" : {
                           "id" : 64,
                           "icon" : "https://img01.mehiya.com/download/file?obj=mefile/hyfo/6f/c3/bdfc-1b39-11f0-845c-00163e0442bb"
                         },
                         "joined_room" : true,
                         "group_role" : 1,
                         "aristocracy" : {
                           "mid" : 17012507,
                           "level" : 5,
                           "badge_icon" : "https://img01.mehiya.com/download/file?obj=mefile/6c/7d/c51b-2a33-11ef-ae81-00163e04069f",
                           "name" : "Duke",
                           "online_colors" : null,
                           "push_cnt" : 0,
                           "is_experience" : false,
                           "exp_type" : 0,
                           "square_icon" : "https://img01.mehiya.com/img/png/id/205374221050"
                         },
                         "dress_info" : null,
                         "is_mystery" : false,
                         "nameplate" : {
                           "star_medal" : "https://img01.mehiya.com/download/file?obj=mefile/fd/24/24a3-44cc-11ef-b0ae-00163e0442bb",
                           "star_medal_webp" : "https://img01.mehiya.com/download/file?obj=mefile/hyfo/32/74/5e4d-4821-11f0-a633-00163e10499f",
                           "nameplate_id" : 3,
                           "nameplate_bg" : "https://img01.mehiya.com/img/png/id/234415569857",
                           "nameplate_text_map" : {
                             "ar" : "الصائد",
                             "en" : "Hunter",
                             "es" : "Hunter",
                             "hi" : "हंटर",
                             "hk" : "獵人",
                             "id" : "Pemburu",
                             "ms" : "Pemburu",
                             "pa" : "ਹੰਟਰ",
                             "pt" : "Hunter",
                             "ta" : "வேட்டைக்காரன்",
                             "te" : "వేటగాడు",
                             "test" : "Hunter",
                             "th" : "นักล่า",
                             "tr" : "Avcı",
                             "vi" : "Thợ săn"
                           },
                           "nameplate_text_color" : [ "#EBFF00" ],
                           "wear" : 0
                         },
                         "name_i18n_map" : null
                       },
                       "to_members" : [ {
                         "id" : 17012507,
                         "name" : "测试账号qq",
                         "avatar_urls" : {
                           "aspect_low" : {
                             "urls" : [ "http://me-media-gateway.test03.youyisia.com/accountv2/avatar/id/1346446066/sz/228" ]
                           },
                           "origin" : {
                             "urls" : [ "http://me-media-gateway.test03.youyisia.com/accountv2/view/id/1346446066/sz/src" ]
                           }
                         }
                       } ],
                       "gift_id" : 13038,
                       "cnt" : 1,
                       "is_all" : false,
                       "to_members_num" : 1,
                       "kind" : 0,
                       "room_id" : 20514,
                       "union_msg_id" : 0,
                       "show_type" : 0,
                       "pk_gift" : false,
                       "get_coins" : 0,
                       "banner_bg" : "",
                       "banner_bg_v2" : "https://img01.mehiya.com/img/png/id/368129934969",
                       "pk_level" : 0,
                       "gift_float" : "",
                       "lucky" : true,
                       "gift" : {
                         "id" : 13038,
                         "pid" : 0,
                         "name" : "卡塔爾",
                         "cost" : 0,
                         "tab" : "TabId(100003)",
                         "tab_id" : 100003,
                         "url" : "https://img01.mehiya.com/img/png/id/557662175021",
                         "thumb_url" : "https://img01.mehiya.com/img/png/id/557662175021",
                         "show_url" : "",
                         "hd_show_url" : "",
                         "to_mid_show_url" : "",
                         "type" : 0,
                         "gift_type" : 0,
                         "tag" : "",
                         "idx" : 0,
                         "idx1" : 0,
                         "idx2" : 0,
                         "idx3" : 0,
                         "enable_send_cnts" : [ 1, 7, 17, 555, 777 ],
                         "status" : 1,
                         "force_payable" : false,
                         "svga_avatar_key" : "",
                         "show_effect_avatar" : false,
                         "avatar_location" : null,
                         "sound" : "",
                         "time" : 0,
                         "world" : false,
                         "relation_type" : 0,
                         "blind_box_gift" : false,
                         "blind_level" : 0,
                         "blind_ani" : "",
                         "blind_ani_times" : null,
                         "avatars" : null,
                         "float_name" : "",
                         "desc" : "",
                         "float_name_key" : "",
                         "desc_key" : "",
                         "jump_url" : "",
                         "float_enable_area_code" : null,
                         "float_st_date" : "",
                         "float_et_date" : "",
                         "blind_box" : false,
                         "weekly_gift" : false,
                         "special_gift" : false,
                         "icon_enable_area_code" : null,
                         "icon_st_date" : "",
                         "icon_et_date" : "",
                         "st" : 0,
                         "et" : 0,
                         "st_date" : "2026-06-12 17:00:00",
                         "et_date" : "2026-07-20 00:00:00",
                         "enable_area_code" : [ 0, 8 ],
                         "activity" : false,
                         "pk_type" : false,
                         "tag_desc" : "",
                         "tag_color" : null,
                         "world_gift" : false,
                         "cover2" : "",
                         "flag" : "",
                         "lucky" : true,
                         "bet_coins" : 9.5,
                         "surprise_price" : 0.5,
                         "lucky_rate_id" : 48,
                         "lucky_score" : {
                           "cur_score" : 0,
                           "target_score" : 0,
                           "reveal_times" : 0,
                           "reveal_odd" : 0,
                           "kind" : 0
                         },
                         "level" : 3,
                         "is_combo" : true,
                         "preload" : false,
                         "user_bag_et" : 0,
                         "room_type" : 0,
                         "naming_gift" : false,
                         "naming_tag" : "",
                         "naming_url" : "",
                         "naming_info" : null,
                         "vip_level" : 0,
                         "vip_lock" : false,
                         "show_time_start" : "",
                         "show_time_end" : "",
                         "is_interactive_gift" : false,
                         "interaction_url" : "0",
                         "is_ari_gift" : false,
                         "ari_level" : 0,
                         "ari_lock" : false,
                         "is_mystery_gift" : false,
                         "cn_name" : "",
                         "lucky_protect_gift_tag" : "",
                         "wealth_level" : 0,
                         "wealth_lock" : false,
                         "remind_icon" : "",
                         "red_packet_entrance" : false,
                         "corner_icon_list" : null,
                         "avatar_index" : null
                       },
                       "cash_back_list" : [ ],
                       "sheet_list" : [ ],
                       "total_cash_back" : 0,
                       "total_rate" : 0,
                       "lucky_recv_mids" : null,
                       "blind_gift_id" : 0,
                       "high_value" : 0,
                       "no_add_switch" : false,
                       "freq_limit" : false,
                       "priority" : 0,
                       "no_limit_mid" : 0,
                       "no_limit_room_id" : 0,
                       "bg_url" : "",
                       "is_animate" : false,
                       "vip_effect" : null,
                       "cs_back_list" : [ ],
                       "total_cs_back" : 0,
                       "surprise_res_list" : [ {
                         "mid" : 17012507,
                         "avatar" : "http://me-media-gateway.test03.youyisia.com/accountv2/avatar/id/1346446066/sz/228",
                         "name" : "测试账号qq",
                         "res_type" : 1,
                         "diamonds" : 1,
                         "diamond_icon" : "https://img01.mehiya.com/img/png/id/522832682903"
                       } ]
                     },
                     "s_t" : 1781694958785
                   },
                   "ts" : 1781694958786
                 } ]
               }
             }
           }
        """.trimIndent()
    }
}
