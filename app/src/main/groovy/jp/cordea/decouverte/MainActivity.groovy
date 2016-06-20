package jp.cordea.decouverte

import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnUIThread
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import jp.cordea.decouverte.response.Root

class MainActivity extends AppCompatActivity {

    private static bingSearchImageQueryKey = "bing_search_image_query"

    @InjectView(R.id.toolbar)
    Toolbar toolbar

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView

    @InjectView(R.id.progress_bar)
    ProgressBar progressBar

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        contentView = R.layout.activity_main
        SwissKnife.inject(this)

        setSupportActionBar(toolbar)

        recyclerView.layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        def adapter = new MainListAdapter(this)
        recyclerView.adapter = adapter

        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.instance
        remoteConfig.configSettings = getFirebaseRemoteConfigSettings()
        remoteConfig.setDefaults([(bingSearchImageQueryKey): ""])
        remoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    remoteConfig.activateFetched()
                }
                getImages(adapter, remoteConfig.getString(bingSearchImageQueryKey))
            }
        })
    }

    private static FirebaseRemoteConfigSettings getFirebaseRemoteConfigSettings() {
        return new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
    }

    @OnBackground
    private getImages(adapter, q) {
        if (q.length() == 0) {
            switchLoading(false)
            return
        }
        BingApi.searchImage(this, q, [
                onResponse: { Root it ->
                    switchLoading(false)
                    def images = it.result.images
                    adapter.insertItems(images)
                },
                onFailure: { IOException e ->
                    e.printStackTrace()
                }
        ] as BingApi.OnCallbackListener)
    }

    @OnUIThread
    private switchLoading(isLoading) {
        progressBar.visibility = isLoading ? View.VISIBLE : View.GONE
        recyclerView.visibility = isLoading ? View.GONE : View.VISIBLE
    }
}