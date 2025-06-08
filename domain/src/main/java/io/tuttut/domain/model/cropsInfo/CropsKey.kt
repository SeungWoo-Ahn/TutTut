package io.tuttut.domain.model.cropsInfo

enum class CropsKey(val key: String) {
    BOK_KYUNG_CHOY("bokChoy"), // 청경채
    CARROT("carrot"), // 당근
    CHIVES("chives"), // 부추
    CUCUMBER("cucumber"), // 오이
    EGGPLANT("eggPlant"), // 가지
    GREEN_ONION("greenOnion"), // 파
    KALE("kale"), // 케일
    LETTUCE("lettuce"), // 상추
    MUGWORT("mugwort"), // 쑥갓
    NAPA_CABBAGE("napaCabbage"), // 배추
    PEANUT("peanut"), // 땅콩
    PEPPER("pepper"), // 고추
    PERILLA("perilla"), // 깻잎
    POTATO("potato"), // 감자
    PUMPKIN("pumpkin"), // 호박
    RADISH("radish"), // 무
    SWEET_POTATO("sweetPotato"), // 고구마
    TOMATO("tomato"), // 토마토
    CUSTOM("custom"); // 커스텀

    companion object {
        private val map = entries.associateBy(CropsKey::key)

        fun fromKey(key: String): CropsKey = map[key] ?: CUSTOM
    }
}