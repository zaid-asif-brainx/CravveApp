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

package com.brainx.cravveCompose.ui.main

import androidx.annotation.WorkerThread
import com.google.gson.JsonObject
import com.brainx.cravveCompose.network.CraveService
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class LoginRepo @Inject constructor(
    private val craveService: CraveService,
) {

    init {
        Timber.d("Injection LoginRepo")
    }

    @WorkerThread
    fun login(
        email: String,
        password: String,
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) = flow {
        val bodyObj = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
            addProperty("device_token", "fcmToken")
            addProperty("app_platform", "android")
            addProperty("app_version", "1")
        }      // request API network call asynchronously.
        craveService.signIn(bodyObj)
            // handle the case when the API request gets a success response.
            .suspendOnSuccess {
                if (this.response.isSuccessful)
                    emit(data)
                else
                    emit(null)
            }
            // handle the case when the API request gets an error response.
            // e.g. internal server error.
            .onError {
                onError(message())
            }
            // handle the case when the API request gets an exception response.
            // e.g. network connection error.
            .onException {
                onError(message())
            }

    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)
}
