package br.com.ams.appcatalogo.dao

interface ContractDao<T> {
    fun insertAll(vararg values: T)
}