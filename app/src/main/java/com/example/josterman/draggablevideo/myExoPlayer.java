package com.example.josterman.draggablevideo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link myExoPlayer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link myExoPlayer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myExoPlayer extends Fragment implements View.OnClickListener, ExoPlayer.EventListener, PlaybackControlView.VisibilityListener{

  private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
  private OnFragmentInteractionListener mListener;
  private SimpleExoPlayer player;
//  private TrackSelectionHelper trackSelectionHelper;
  private DefaultTrackSelector trackSelector;
  private DataSource.Factory mediaDataSourceFactory;
  private Handler mainHandler;
  private EventLogger eventLogger;
  private PlayerView playerView;

  public myExoPlayer() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment myExoPlayer.
   */
  // TODO: Rename and change types and number of parameters
  public static myExoPlayer newInstance() {
    myExoPlayer fragment = new myExoPlayer();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_my_exo_player, container, false);
    playerView = (PlayerView) v.findViewById(R.id.videoView);
    return v;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  private void initializePlayer(){
    boolean needNewPlayer = player == null;
    if (needNewPlayer){
      DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(), null, EXTENSION_RENDERER_MODE_PREFER);

      TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

      player = ExoPlayerFactory.newSimpleInstance(renderersFactory, new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
      player.addListener(this);
      playerView.setPlayer(player);
    }
    MediaSource mediaSource = buildMediaSource(Uri.parse(getString(R.string.hls)));
    player.prepare(mediaSource, true, false);
  }

  private MediaSource buildMediaSource(Uri uri) {

    DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory("ua");

    DefaultHlsDataSourceFactory hlsDataSourceFactory = new DefaultHlsDataSourceFactory(httpDataSourceFactory);

    return new HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(uri);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
              + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onStart(){
    super.onStart();
    if (Util.SDK_INT > 23){
      initializePlayer();
    }
  }

  @Override
  public void onResume(){
    super.onResume();
    hideSystemUI();
    if ((Util.SDK_INT <= 23 || player == null)){
      initializePlayer();
    }
  }

  @SuppressLint("InlinedApi")
  private void hideSystemUI(){
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

  }

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

  }

  @Override
  public void onLoadingChanged(boolean isLoading) {

  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState){

  }

  @Override
  public void onRepeatModeChanged(int repeatMode) {

  }

  @Override
  public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

  }

  @Override
  public void onPlayerError(ExoPlaybackException error) {

  }

  @Override
  public void onPositionDiscontinuity(int reason) {

  }

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

  }

  @Override
  public void onSeekProcessed() {

  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public void onVisibilityChange(int visibility) {

  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
