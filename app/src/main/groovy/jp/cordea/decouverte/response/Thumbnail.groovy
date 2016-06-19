package jp.cordea.decouverte.response

import com.google.gson.annotations.SerializedName
import groovy.transform.Immutable

@Immutable
class Thumbnail {

    @SerializedName("MediaUrl")
    String mediaUrl

    @SerializedName("ContentType")
    String contentType

    @SerializedName("Width")
    int width

    @SerializedName("Height")
    int height

    @SerializedName("FileSize")
    int fileSize

}