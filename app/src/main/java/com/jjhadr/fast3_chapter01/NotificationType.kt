package com.jjhadr.fast3_chapter01

enum class NotificationType(val title: String, val id: Int) {
    NORMAL("알림", 0),
    EXPANDABLE("확장현 알림",1),
    CUSTOM("커스텀 알림", 3)
}