package jp.cordea.decouverte.response

import com.google.gson.annotations.SerializedName
import groovy.transform.Immutable

@Immutable
class Root {
    @SerializedName("d")
    Result result
}