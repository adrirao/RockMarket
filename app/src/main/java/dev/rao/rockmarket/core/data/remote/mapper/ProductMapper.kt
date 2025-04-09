package dev.rao.rockmarket.core.data.remote.mapper

import dev.rao.rockmarket.core.data.remote.dto.PlatziProductDto
import dev.rao.rockmarket.core.data.remote.dto.ProductDto
import dev.rao.rockmarket.core.domain.model.Product
import dev.rao.rockmarket.core.domain.model.Rating

fun ProductDto.toProduct(): Product {
    return Product(
        id = id.toString(),
        title = title,
        price = price,
        description = description,
        category = category,
        imageUrl = image,
        rating = rating.toRating()
    )
}

fun ProductDto.RatingDto.toRating(): Rating {
    return Rating(
        rate = rate,
        count = count
    )
}

fun PlatziProductDto.toProduct(): Product {
    return Product(
        id = id.toString(),
        title = title,
        price = price.toDouble(),
        description = description,
        category = category.name,
        imageUrl = images.firstOrNull() ?: ""
    )
}