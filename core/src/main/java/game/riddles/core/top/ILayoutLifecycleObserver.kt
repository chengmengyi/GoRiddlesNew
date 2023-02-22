package game.riddles.core.top

import android.content.Intent
import android.os.Bundle

interface ILayoutLifecycleObserver {

    fun onLayoutCreate(savedInstanceState: Bundle?) {}
    fun onLayoutStart() {}
    fun onLayoutResume() {}
    fun onLayoutPause() {}
    fun onLayoutStop() {}
    fun onLayoutDestroy() {}
    fun onLayoutNewIntent(i: Intent?) {}
}