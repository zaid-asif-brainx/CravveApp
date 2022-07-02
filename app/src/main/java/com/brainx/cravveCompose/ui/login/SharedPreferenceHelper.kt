package com.brainx.cravveCompose.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.brainx.cravveCompose.ui.main.UserModel


@SuppressLint("CommitPrefEdits")
class SharedPreferenceHelper(context: Context) {
    private val APP_NAME = "LevelUp"
    private val ACCESS_TOKEN = "accesstoken"
    private val CLIENT_ID = "clientid"
    private val IS_LOGIN = "islogin"
    private val IS_FIRST_TIME_USER = "isfirsttimeuser"
    private val GET_STARTED = "getstarted"
    private val USER_DATA = "userdata"
    private val QUOTE_DATA = "quoteData"
    private val NOTIFICATION_SYNCHRONOUS_ID = "notificationSynchronousId"
    private val TRACK_IT_DATA = "trackItData"
    private val GOAL_PLAN_DATA = "goalPlanData"
    private val ORGANIZATION_DATA = "organizationData"

    private val preferences: SharedPreferences = context.getSharedPreferences(
        APP_NAME,
        Context.MODE_PRIVATE
    )
    private var editor: SharedPreferences.Editor? = null


    var token: String?
        get() = preferences.getString(ACCESS_TOKEN, "")
        set(token) {
            editor = preferences.edit()
            editor?.let {
                it.putString(ACCESS_TOKEN, token)
                it.apply()
            }
        }

    var clientid: String?
        get() = preferences.getString(CLIENT_ID, "")
        set(tokentype) {
            editor = preferences.edit()
            editor?.let {
                it.putString(CLIENT_ID, tokentype)
                it.apply()
            }
        }


    var isuserlogin: Boolean
        get() = preferences.getBoolean(IS_LOGIN, false)
        set(isuserlogin) {
            editor = preferences.edit()
            editor?.let {
                it.putBoolean(IS_LOGIN, isuserlogin)
                it.apply()
            }


        }

    var notFirstTimeLogin: Boolean
        get() = preferences.getBoolean(IS_FIRST_TIME_USER, true)
        set(notFirstTimeLogin) {
            editor = preferences.edit()
            editor?.let {
                it.putBoolean(IS_FIRST_TIME_USER, notFirstTimeLogin)
                it.apply()
            }
        }


    var isgetstarted: Boolean
        get() = preferences.getBoolean(GET_STARTED, true)
        set(isuserlogin) {
            editor = preferences.edit()
            editor?.let {
                it.putBoolean(GET_STARTED, isuserlogin)
                it.apply()
            }
        }


    fun clearPrefs() {
        preferences.edit().clear().apply()
    }

    var userData: UserModel?
        get() {
            val gson = Gson()
            val json = preferences.getString(USER_DATA, "")
            return gson.fromJson(json, UserModel::class.java)
        }
        set(model) {
            editor = preferences.edit()
            val gson = Gson()
            val json = gson.toJson(model)
            editor?.let {
                it.putString(USER_DATA, json)
                it.apply()
            }
        }



    var notificationSynchronousId: Int
        get() {
            return preferences.getInt(NOTIFICATION_SYNCHRONOUS_ID, -1)
        }
        set(model) {
            editor = preferences.edit()
            editor?.let {
                it.putInt(NOTIFICATION_SYNCHRONOUS_ID, model)
                it.apply()
            }
        }






}