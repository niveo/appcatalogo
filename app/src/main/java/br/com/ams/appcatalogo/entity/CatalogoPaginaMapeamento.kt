package br.com.ams.appcatalogo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "CatalogoPaginaMapeamento")
class CatalogoPaginaMapeamento(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "catalogoPaginaId") val catalogoPaginaId: Long?,
    @ColumnInfo(name = "inicialPosicalX") val inicialPosicalX: Float?,
    @ColumnInfo(name = "finalPosicalX") val finalPosicalX: Float?,
    @ColumnInfo(name = "inicialPosicalY") val inicialPosicalY: Float?,
    @ColumnInfo(name = "finalPosicalY") val finalPosicalY: Float?,
    @ColumnInfo(name = "width") val width: Float?,
    @ColumnInfo(name = "height") val height: Float?
)