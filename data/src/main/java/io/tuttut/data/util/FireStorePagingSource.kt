package io.tuttut.data.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.tuttut.data.model.dto.Crops
import kotlinx.coroutines.tasks.await

class CropsPagingSource(
    private val query: Query
) : PagingSource<QuerySnapshot, Crops>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Crops>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Crops> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Crops::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}