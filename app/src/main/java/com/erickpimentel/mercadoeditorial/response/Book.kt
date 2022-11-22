package com.erickpimentel.mercadoeditorial.response

data class Book(
    val ano_edicao: String,
    val ano_escolar: Any,
    val bisac_principal: String,
    val booktrailer: String,
    val catalogacao: Catalogacao,
    val certificacao_inmetro: String,
    val classificacao_indicativa: String,
    val codigo_de_barras: String,
    val codigo_fiscal: String,
    val codigo_interno: String,
    val colecao: String,
    val contribuicao: List<Contribuicao>,
    val cst_code: String,
    val data_publicacao: String,
    val detalhes_da_edicao: String,
    val dimensoes: String,
    val ebook_distribuicao: Any,
    val ebook_drm: String,
    val edicao: String,
    val editora: Editora,
    val faixa_etaria: String,
    val formato: String,
    val grau_escolar: Any,
    val idioma: String,
    val imagens: Imagens,
    val isbn: String,
    val isbn_relacionados: List<IsbnRelacionado>,
    val link: String,
    val materia_escolar: String,
    val medidas: Medidas,
    val moeda: String,
    val origem: String,
    val preco: String,
    val previsao_disponibilidade: String,
    val registro_atualizado_em: String,
    val registro_criado_em: String,
    val selo: Selo,
    val sinopse: String,
    val status: Int,
    val subtitulo: String,
    val sumario: String,
    val titulo: String,
    val titulo_original: String,
    val volume: String,
    val volume_colecao: String
)