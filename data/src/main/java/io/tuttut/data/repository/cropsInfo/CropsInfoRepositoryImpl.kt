package io.tuttut.data.repository.cropsInfo

import com.google.firebase.firestore.CollectionReference
import io.tuttut.data.constant.CRAWLING_BASE_URL
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Recipe
import io.tuttut.data.model.dto.isRecommended
import io.tuttut.data.util.asFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Named

class CropsInfoRepositoryImpl @Inject constructor(
    @Named("cropsInfoRef") val cropsInfoRef: CollectionReference
): CropsInfoRepository {
    override val cropsInfoList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val monthlyCropsList: MutableStateFlow<List<CropsInfo>> = MutableStateFlow(emptyList())
    override val cropsInfoMap: HashMap<String, CropsInfo> = HashMap()

    override fun getCropsInfoList(currentMonth: Int): Flow<List<CropsInfo>> =
        cropsInfoRef.asFlow(CropsInfo::class.java) {
            if (cropsInfoList.value.isEmpty()) {
                cropsInfoList.value = it
                monthlyCropsList.value = it.flatMap { cropsInfo ->
                    cropsInfo.plantingSeasons.filter { season ->
                        season.isRecommended(currentMonth)
                    }.map { cropsInfo }
                }
                for (cropsInfo in it) {
                    cropsInfoMap[cropsInfo.key] = cropsInfo
                }
            }
        }

    override fun getCropsRecipes(keyword: String): Flow<List<Recipe>> = flow {
        val crawlingUrl = "${CRAWLING_BASE_URL}/recipe/list.html?q=${keyword}"
        val doc = withContext(Dispatchers.IO) { Jsoup.connect(crawlingUrl).get() }
        val recipes = doc.select(".common_sp_list_ul.ea4 li").take(10).map { element ->
            val aTag = element.select(".common_sp_thumb a")
            val imgTag = aTag.select("img")
            val divTag = element.select(".common_sp_caption_tit.line2")
            Recipe(
                title = divTag.text(),
                imgUrl = imgTag.attr("src"),
                link = aTag.attr("href")
            )
        }
        emit(recipes)
    }.flowOn(Dispatchers.IO)
}