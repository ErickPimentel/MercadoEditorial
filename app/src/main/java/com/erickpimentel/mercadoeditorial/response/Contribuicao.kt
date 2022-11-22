package com.erickpimentel.mercadoeditorial.response

data class Contribuicao(
    val codigo_contribuicao: String,
    val nome: String,
    val sobrenome: String,
    val tipo_de_contribuicao: String
)