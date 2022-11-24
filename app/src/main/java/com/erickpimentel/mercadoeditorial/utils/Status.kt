package com.erickpimentel.mercadoeditorial.utils

enum class Status: IRequestCode{
    AVAILABLE {
        override fun code(): Int {
            return 1
        }
    },
    UNAVAILABLE {
        override fun code(): Int {
            return 2
        }
    },
    PRE_RELEASE {
        override fun code(): Int {
            return 3
        }
    },
    OUT_OF_CATALOG {
        override fun code(): Int {
            return 4
        }
    }
}