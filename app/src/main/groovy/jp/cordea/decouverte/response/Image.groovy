package jp.cordea.decouverte.response

import com.google.gson.annotations.SerializedName
import groovy.transform.Immutable

@Immutable
class Image {

    @SerializedName("ID")
    String id

    @SerializedName("Title")
    String title

    @SerializedName("MediaUrl")
    String mediaUrl

    @SerializedName("SourceUrl")
    String sourceUrl

    @SerializedName("DisplayUrl")
    String displayUrl

    @SerializedName("Width")
    int width

    @SerializedName("Height")
    int height

    @SerializedName("FileSize")
    int fileSize

    @SerializedName("ContentType")
    String contentType

    @SerializedName("Thumbnail")
    Thumbnail thumbnail

}