package com.vitiligo.breathe.domain.repository

import com.vitiligo.breathe.domain.model.LocationSearchResult
import com.vitiligo.breathe.domain.util.Resource

interface LocationSearchRepository {

    suspend fun search(query: String): Resource<List<LocationSearchResult>>
}
