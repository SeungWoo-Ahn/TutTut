package io.tuttut.data.repository

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.mapper.toDomain
import io.tuttut.data.network.constant.CRAWLING_BASE_URL
import io.tuttut.data.network.di.CropsInfoReference
import io.tuttut.data.network.model.CropsInfoDto
import io.tuttut.data.network.model.RecipeDto
import io.tuttut.data.util.getOneShot
import io.tuttut.domain.model.cropsInfo.CropsInfo
import io.tuttut.domain.model.cropsInfo.CropsKey
import io.tuttut.domain.model.cropsInfo.Recipe
import io.tuttut.domain.repository.CropsInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropsInfoRepositoryImpl @Inject constructor(
    @CropsInfoReference val cropsInfoRef: CollectionReference
): CropsInfoRepository {
    override suspend fun getCropsInfoList(): List<CropsInfo> =
        cropsInfoRef
            .getOneShot<CropsInfoDto>()
            .map(CropsInfoDto::toDomain)

    override suspend fun getCropsInfoByKey(cropsKey: CropsKey): CropsInfo =
        cropsInfoRef
            .document(cropsKey.key)
            .getOneShot<CropsInfoDto>()
            .toDomain()

    override fun getCropsRecipeList(keyword: String): Flow<List<Recipe>> = flow {
        val crawlingUrl = "$CRAWLING_BASE_URL/recipe/list.html?q=${keyword}"
        val doc = Jsoup.connect(crawlingUrl).timeout(10_000).get()
        val recipeList = doc
            .select(".common_sp_list_ul.ea4 li")
            .take(10)
            .map { element ->
                val divTag = element.select(".common_sp_caption_tit.line2")
                val aTag = element.select(".common_sp_thumb a")
                val imgTag = aTag.select("img")
                RecipeDto(
                    title = divTag.text(),
                    imgUrl = imgTag.attr("src"),
                    link = aTag.attr("href")
                )
            }
            .map(RecipeDto::toDomain)
        emit(recipeList)
    }.flowOn(Dispatchers.IO)
}