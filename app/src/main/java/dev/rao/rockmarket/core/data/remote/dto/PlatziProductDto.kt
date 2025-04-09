package dev.rao.rockmarket.core.data.remote.dto

data class PlatziProductDto(
    val id: Int,
    val title: String,
    val price: Int,
    val description: String,
    val category: PlatziCategoryDto,
    val images: List<String>
) {
    data class PlatziCategoryDto(
        val id: Int,
        val name: String,
        val image: String
    )
}