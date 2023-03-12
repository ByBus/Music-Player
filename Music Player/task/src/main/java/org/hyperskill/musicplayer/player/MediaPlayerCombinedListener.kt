package org.hyperskill.musicplayer.player

import android.media.MediaPlayer

interface MediaPlayerCombinedListener : MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener