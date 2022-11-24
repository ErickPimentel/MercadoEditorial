package com.erickpimentel.mercadoeditorial.utils

import com.erickpimentel.mercadoeditorial.R

enum class ParentalRating(val code: Int): IParentalRating {
    EVERYONE(1) {
        override fun result(): Int {
            return R.drawable.ic_everyone
        }
    },
    YEARS_OLD_10(2) {
        override fun result(): Int {
            return R.drawable.ic_years_old_10
        }
    },
    YEARS_OLD_12(3) {
        override fun result(): Int {
            return R.drawable.ic_years_old_12
        }
    },
    YEARS_OLD_14(4) {
        override fun result(): Int {
            return R.drawable.ic_years_old_14
        }
    },
    YEARS_OLD_16(5) {
        override fun result(): Int {
            return R.drawable.ic_years_old_16
        }
    },
    YEARS_OLD_18(6) {
        override fun result(): Int {
            return R.drawable.ic_years_old_18
        }
    },
    EXCLUSIVELY_FOR_CHILDREN(7) {
        override fun result(): Int {
            return R.drawable.ic_exclusively_for_children
        }
    };

    companion object {
        fun fromInt(code: Int) = values().first { it.code == code }
    }

}