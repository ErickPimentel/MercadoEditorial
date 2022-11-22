package com.erickpimentel.mercadoeditorial.response

data class Catalogacao(
    val areas: String,
    val bisac_principal: List<String>,
    val cdd: String,
    val codigo_thema_categoria: List<String>,
    val codigo_thema_qualificador: List<Any>,
    val palavras_chave: String
)