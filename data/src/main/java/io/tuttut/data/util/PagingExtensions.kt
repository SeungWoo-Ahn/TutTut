package io.tuttut.data.util

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

open class BasePagingSource<T: Any>(
    private val query: Query,
    private val dataType: Class<T>
) : PagingSource<QuerySnapshot, T>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, T>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, T> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(dataType),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

private class PagingSourceFactory {
    fun <T: Any> providePagingSource(query: Query, dataType: Class<T>): BasePagingSource<T> {
        return BasePagingSource(query, dataType)
    }
}

fun <T: Any> providePager(pageSize: Int, query: Query, dataType: Class<T>): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(pageSize = pageSize),
        pagingSourceFactory = { PagingSourceFactory().providePagingSource(query, dataType) }
    ).flow
}