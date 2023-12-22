package br.com.ams.appcatalogo.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "CatalogoPaginaMapeamentoProduto",
    primaryKeys = ["catalogoPaginaMapeamentoId", "produtoId"],
)
class CatalogoPaginaMapeamentoProduto(
    val catalogoPaginaMapeamentoId: Long,
    val produtoId: Long,
)