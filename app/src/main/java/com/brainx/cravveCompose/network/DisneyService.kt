/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brainx.cravveCompose.network

import com.google.gson.JsonObject
import com.brainx.cravveCompose.model.Poster
import com.brainx.cravveCompose.ui.login.Constant
import com.brainx.cravveCompose.ui.main.UserModel
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DisneyService {

    @GET("DisneyPosters2.json")
    suspend fun fetchDisneyPosterList(): ApiResponse<List<Poster>>
}

interface CraveService {
    @POST(Constant.SIGN_IN)
    suspend fun signIn(@Body body: JsonObject?): ApiResponse<UserModel>
}

