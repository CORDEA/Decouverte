package jp.cordea.decouverte

import android.net.Uri
import com.google.gson.Gson
import jp.cordea.decouverte.response.Root
import okhttp3.*

class BingApi {

    private static baseUrl = "https://api.datamarket.azure.com/Bing/Search/v1/Image"

    private static client = new OkHttpClient()

    interface OnCallbackListener {
        void onResponse(response) throws IOException
        void onFailure(e)
    }

    static void searchImage(context, q, listener) {
        def gson = new Gson()
        if (BuildConfig.IS_MOCK) {
            def json = context.resources.openRawResource(R.raw.response).text
            def root = gson.fromJson(json, Root.class)
            listener.onResponse(root)
            return
        }
        client.newCall(getBuilder(q).build())
                .enqueue(
                new Callback() {
                    @Override
                    void onFailure(Call call, IOException e) {
                        listener.onFailure(e)
                    }

                    @Override
                    void onResponse(Call call, Response response) throws IOException {
                        def root = gson.fromJson(response.body().string(), Root.class)
                        listener.onResponse(root)
                    }
                })
    }

    private static Request.Builder getBuilder(q) {
        def cred = Credentials.basic("", BuildConfig.BING_API_KEY)
        def uri = Uri.parse(baseUrl)
        def builder = new Uri.Builder().scheme(uri.scheme).authority(uri.host).path(uri.path)
        builder.appendQueryParameter("Query", "'" + q + "'")
        builder.appendQueryParameter("\$format", "json")
        return new Request.Builder()
                .url(builder.build().toString())
                .header("Authorization", cred)
    }
}