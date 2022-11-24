package com.erickpimentel.mercadoeditorial.utils

enum class Status(val code: Int): IResult{
    AVAILABLE(1) {
        override fun result(): String {
            return "Available"
        }
    },
    UNAVAILABLE(2) {
        override fun result(): String {
            return "Unavailable"
        }
    },
    PRE_RELEASE(3) {
        override fun result(): String {
            return "Pre release"
        }
    },
    OUT_OF_CATALOG(4) {
        override fun result(): String {
            return "Out of catalog"
        }
    };

    companion object {
        fun fromInt(code: Int) = values().first { it.code == code }
    }
}