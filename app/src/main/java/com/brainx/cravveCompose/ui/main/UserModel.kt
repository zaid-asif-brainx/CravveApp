package com.brainx.cravveCompose.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    @Expose
    var id: Int? = 0,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("uid")
    @Expose
    var uid: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("device_token")
    @Expose
    var device_token: String? = null,

    @SerializedName("app_platform")
    @Expose
    var app_platform: String? = null,

    @SerializedName("app_version")
    @Expose
    var app_version: String? = null,

    @SerializedName("active?")
    @Expose
    var active: Boolean? = false,

    @SerializedName("first_login?")
    @Expose
    var first_login: Boolean? = false,
    @SerializedName("office")
    @Expose
    var office: Office? = null,

    @SerializedName("manager")
    @Expose
    var manager: Manager? = null,


    @SerializedName("can_update_progress?")
    @Expose
    var can_update_progress: Boolean? = false,

    @SerializedName("coach")
    @Expose
    var coach: Coach? = null

) {


}



data class Coach(
    @SerializedName("name")
    @Expose
    var name: String? = null
)
data class Manager(
    @SerializedName("name")
    @Expose
    var name: String? = null
)
data  class Office(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("address")
    @Expose
    var address: String? = null
)